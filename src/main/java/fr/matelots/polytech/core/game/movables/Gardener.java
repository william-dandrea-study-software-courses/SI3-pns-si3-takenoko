package fr.matelots.polytech.core.game.movables;

import fr.matelots.polytech.core.game.Board;
import fr.matelots.polytech.core.game.parcels.Parcel;
import fr.matelots.polytech.engine.util.Position;

import java.util.List;

public class Gardener {
    private final Board board;
    private Position position;

    public Gardener (Board board, Position pos) {
        this.board = board;
        this.position = pos;
    }

    public boolean moveTo (int x, int y, int z) {
        Position tmp = new Position(x, y, z);
        if (board.placePawn(this, tmp)) {
            position = tmp;
            makeAction();
            return true;
        } else
            return false;
    }

    public void makeAction () {
        List<Parcel> neighbours = board.getNeighbours(position.getX(),
                position.getY(), position.getZ());
        Parcel current = board.getParcel(position);
        current.growBamboo();

        neighbours.stream()
                .filter(n -> current.getBambooColor().equals(n.getBambooColor()))
                .forEach(Parcel::growBamboo);
    }
}
