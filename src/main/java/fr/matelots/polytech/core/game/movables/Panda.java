package fr.matelots.polytech.core.game.movables;

import fr.matelots.polytech.core.game.Board;
import fr.matelots.polytech.core.game.parcels.Parcel;
import fr.matelots.polytech.engine.util.Position;

import java.util.List;

/**
 * @author Gabriel Cogne
 */
public class Panda extends Pawn {

    public Panda(Board board, Position position) {
        super(board, position);
    }

    /**
     * Make the bamboo grow on is current position and all neighbour parcel that is the
     * same bamboo color
     */
    @Override
    public void makeAction () {
        getBoard().getParcel(getPosition()).destroyUnitOfBamboo();
    }
}
