package fr.matelots.polytech.core.players;

import fr.matelots.polytech.core.game.Board;
import fr.matelots.polytech.core.game.Game;
import fr.matelots.polytech.core.game.goalcards.CardObjectiveGardener;
import fr.matelots.polytech.core.game.goalcards.CardObjectiveParcel;

/**
 * @author Gabriel Cogne
 */
public abstract class Bot {

    private final Game game;
    protected Board board;

    private final IndividualBoard individualBoard;

    public Bot(Game game) {
        this.game = game;
        this.board = game.getBoard();

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

    public boolean pickGardenerObjective() {
        CardObjectiveGardener obj = game.getNextGardenerObjective();
        if (obj == null)
            return false;
        return individualBoard.addNewGardenerObjective(obj);
    }

    public abstract void playTurn ();

    public abstract boolean canPlay();

}
