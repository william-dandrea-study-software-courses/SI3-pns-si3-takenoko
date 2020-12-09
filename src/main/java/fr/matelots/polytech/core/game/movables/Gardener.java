package fr.matelots.polytech.core.game.movables;

import fr.matelots.polytech.core.game.Board;
import fr.matelots.polytech.core.game.parcels.BambooPlantation;
import fr.matelots.polytech.core.game.parcels.Parcel;
import fr.matelots.polytech.engine.util.Position;

import java.util.List;

/**
 * @author Gabriel Cogne
 */
public class Gardener extends Pawn {

    public Gardener (Board board, Position pos) {
        super(board, pos);
    }

    /**
     * Make the bamboo grow on is current position and all neighbour parcel that is the
     * same bamboo color
     */
    @Override
    void makeAction () {
        List<Parcel> neighbours = getBoard().getNeighbours(getPosition().getX(),
                getPosition().getY(), getPosition().getZ());
        Parcel current = getBoard().getParcel(getPosition());
        current.growBamboo();

        neighbours.stream()
                .filter(n -> current.getBambooColor().equals(n.getBambooColor()))
                .forEach(Parcel::growBamboo);
    }
}
