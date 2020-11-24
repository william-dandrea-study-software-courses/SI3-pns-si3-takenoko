package fr.matelots.polytech.core.game;

import fr.matelots.polytech.core.game.goalcards.CardObjectiveParcel;
import fr.matelots.polytech.core.game.graphics.BoardDrawer;
import fr.matelots.polytech.core.players.Bot;
import fr.matelots.polytech.core.players.bots.PremierBot;

import javax.sound.midi.SysexMessage;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Gabriel Cogne
 * @author Yann Clodong
 */
public class Game {

    private static int OBJ_TO_COMPLETE_FOR_LAST_TURN = 9;

    // Attributes
    private final List<Bot> bots;
    private final Board board;
    private final BoardDrawer drawer;
    //private Player winner;
    private boolean lastTurn = false;

    // Constructors
    public Game () {
        bots = new ArrayList<>();
        board = new Board();

        bots.add(new PremierBot(this));
        bots.add(new PremierBot(this));
        //bots.add(new PremierBot(this));

        drawer = new BoardDrawer(board);
    }



    // Methods
    public Bot getWinner () {
        Bot winner = null;
        int bestScore = 0;
        for (Bot bot : bots) {
            int score = bot.getIndividualBoard().getPlayerScore();
            if(score > bestScore) {
                winner = bot;
                bestScore = score;
            }
        }
        return winner;
    }

    public Board getBoard() {
        return board;
    }

    public void run () {
        System.out.print("Joueurs: ");
        bots.forEach(System.out::println);
        System.out.println();

        //bots.forEach(bot -> bot.setBoard(board));

        drawer.print();

        while (!lastTurn) {
            //bots.forEach(Bot::playTurn);

            bots.forEach(bot -> {
                bot.playTurn();
                if (bot.getIndividualBoard().countCompletedObjectives() >= OBJ_TO_COMPLETE_FOR_LAST_TURN)
                    lastTurn = true;
                System.out.println("Completed : " + bot.getIndividualBoard().countCompletedObjectives());


                //System.out.println("Deck empty ? " + (getNextParcelObjective() == null));
                //System.out.println("Can add parcel ? " + (board.getParcelLeftToPlace() == 0));


                drawer.print();
            });

            if(bots.stream().allMatch(b -> !b.canPlay())) // Si aucun bot ne peut jouer, on coupe la partie.
                break;
            //drawer.print();
        }

        // this is the winner ! ;)
        var winner = getWinner();
        System.out.println("Winner score : " + winner.getIndividualBoard().getPlayerScore());

        for(Bot bot : bots) {
            if(winner == bot) continue;
            System.out.println("Loser score : " + bot.getIndividualBoard().getPlayerScore());
        }

    }

    /**
     * This return the hidden top card of the parcel objective deck
     * @return the hidden top card of the parcel objective deck
     */
    public CardObjectiveParcel getNextParcelObjective () {
        if (board.getDeckParcelObjective().canPick())
            return board.getDeckParcelObjective().pick();
        return null;
    }
}
