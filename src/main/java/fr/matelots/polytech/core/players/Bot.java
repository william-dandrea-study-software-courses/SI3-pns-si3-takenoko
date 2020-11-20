package fr.matelots.polytech.core.players;

import fr.matelots.polytech.core.game.Board;
import fr.matelots.polytech.core.game.CardObjectiveParcel;
import fr.matelots.polytech.core.game.Game;
import fr.matelots.polytech.core.game.goalcards.CardObjectiveParcel;

/**
 * @author Gabriel Cogne
 */
public abstract class Bot {
    private final int nCardsForVictory = 9;
    private final Game game;
    protected Board board;

    private final IndividualBoard individualBoard;

    public Bot(Game game) {
        this.game = game;
        individualBoard = new IndividualBoard();
    }

    public IndividualBoard getIndividualBoard() {
        return individualBoard;
    }

    public boolean pickParcelObjective () {
        CardObjectiveParcel obj = game.getNextParcelObjective();
        if (obj == null)
            return false;
        return individualBoard.addNewParcelObjective(obj);
    }

    public boolean isVictorious() {
        return getIndividualBoard().getCompletedObjectives() >= nCardsForVictory;
    }

    public abstract void playTurn ();

    public void setBoard(Board board) {
        this.board = board;
    }
}
