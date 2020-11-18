package fr.matelots.polytech.core.game;

import fr.matelots.polytech.core.game.goalcards.AlignedParcelGoal;
import fr.matelots.polytech.core.game.graphics.BoardDrawer;
import fr.matelots.polytech.core.players.Bot;
import fr.matelots.polytech.core.players.bots.PremierBot;

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
        bots.add(new PremierBot(this));
        //bots.add(new PremierBot(this));

        board = new Board();
        drawer = new BoardDrawer(board);
    }

    public Bot getWinner () {
        for (Bot bot : bots) {
            if (bot.isVictorious()) return bot;
        }
        return null;
    }

    // Methods
    public void run () {
        System.out.print("Joueurs: ");
        bots.forEach(System.out::println);
        System.out.println();

        bots.forEach(bot -> bot.setBoard(board));

        drawer.print();

        while (getWinner() == null && !lastTurn) {
            bots.forEach(Bot::playTurn);

            bots.forEach(bot -> {
                bot.playTurn();
                if (bot.getIndividualBoard().countUnfinishedObjectives() >= OBJ_TO_COMPLETE_FOR_LAST_TURN)
                    lastTurn = true;
            });

            drawer.print();
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
