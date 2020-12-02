package fr.matelots.polytech.core.game.movables;

import fr.matelots.polytech.core.game.Board;
import fr.matelots.polytech.core.game.parcels.Parcel;
import fr.matelots.polytech.core.players.Bot;
import fr.matelots.polytech.engine.util.Position;

import java.util.List;

/**
 * @author Gabriel Cogne
 */
public class Panda extends Pawn {
    private Bot currentPlayer;

    public Panda(Board board, Position position) {
        super(board, position);
    }

    /**
     * Must be used to define who's the current player
     * a null pointer will stop the panda action
     * @param currentPlayer current player
     */
    public void setCurrentPlayer(Bot currentPlayer) {
        this.currentPlayer = currentPlayer;
    }

    /**
     * Make the bamboo grow on is current position and all neighbour parcel that is the
     * same bamboo color
     */
    @Override
    public void makeAction () {
        if (currentPlayer == null) {
            throw new RuntimeException("The current player must be known in order to" +
                    "made this panda action");
        }

        getBoard().getParcel(getPosition()).destroyUnitOfBamboo();
        currentPlayer.getIndividualBoard()
                .addAnEatenUnitOfBamboo(getBoard()
                                        .getParcel(getPosition())
                                        .getBambooColor());

        currentPlayer = null;
    }
}
