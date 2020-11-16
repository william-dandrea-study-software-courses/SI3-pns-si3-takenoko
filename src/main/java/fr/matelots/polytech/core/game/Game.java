package fr.matelots.polytech.core.game;

import fr.matelots.polytech.core.game.GoalCards.AlignedParcelGoal;
import fr.matelots.polytech.core.game.graphics.BoardDrawer;
import fr.matelots.polytech.core.players.Player;
import fr.matelots.polytech.core.players.VirtualPlayer;
import fr.matelots.polytech.core.players.bots.PremierBot;

import java.util.ArrayList;
import java.util.List;

public class Game {
    // Attributes
    private final List<Player> players;
    private final Board board;
    private final BoardDrawer drawer;
    private Player winner;

    PremierBot pb = new PremierBot();
    AlignedParcelGoal goal = new AlignedParcelGoal(4);
    // Constructors
    public Game () {
        players = new ArrayList<>();

        pb.ResolveGoal(goal);
        players.add(pb);
        //players.add(pb);

        board = new Board();
        board.addParcel(-1, 0, 1, new Parcel());
        board.addParcel(0, -1, 1, new Parcel());
        drawer = new BoardDrawer(board);
    }

    public Player getWinner () {
        return winner;
    }

    // Methods
    public void run () {
        System.out.print("Joueurs: ");
        players.forEach(System.out::println);
        System.out.println();

        players.forEach((Player p) -> {
            if(p instanceof  VirtualPlayer) {
                ((VirtualPlayer) p).setBoard(board);
            }
        });

        //System.out.println(board);
        drawer.Print();

        int i = 0;

        while(!goal.getComplete()) {
            pb.playTurn();
        }

        /*while (getWinner() == null) {
            for (Player p : players) {
                if (p instanceof VirtualPlayer) {
                    ((VirtualPlayer) p).playTurn();
                }
            }

            if (i > 0) {
                break;
            }
            i++;
        }*/


    }

    /**
     * This return the hidden top card of the parcel objective deck
     * @return the hidden top card of the parcel objective deck
     */
    public CardObjectiveParcel getNextParcelObjective () {
        try {
            return board.getDeckParcelObjective().pick();
        } catch (Exception e) {
            return null;
        }
    }
}
