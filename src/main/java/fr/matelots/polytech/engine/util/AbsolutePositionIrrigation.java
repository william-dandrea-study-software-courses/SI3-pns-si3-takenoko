package fr.matelots.polytech.engine.util;

import fr.matelots.polytech.core.game.Board;
import fr.matelots.polytech.core.game.parcels.Parcel;
import fr.matelots.polytech.core.game.parcels.Side;

public class AbsolutePositionIrrigation {
    private final Board board;
    private final Position position;
    private final Side side;


    public AbsolutePositionIrrigation(Position position, Side side) {
        this.position = position;
        this.side = side;
        this.board = null;
    }
    public AbsolutePositionIrrigation(Position position, Side side, Board board) {
        this.position = position;
        this.side = side;
        this.board = board;
    }

    public Side getSide() {
        return side;
    }

    public Position getPosition() {
        return position;
    }

    /**
     * Return the state of irrigation of the edge
     * @return
     */
    public boolean isIrrigate() {
        if(board == null) throw new RuntimeException("Board were null");
        if(board.getParcel(position) != null) return board.getParcel(position).isIrrigate(side);
        else if(board.getParcel(position.add(side.getDirection())) != null) return board.getParcel(position.add(side.getDirection())).isIrrigate(side.oppositeSide());
        else return false;
    }

    /**
     * Set Irrigation to the correct position
     * @return true if an irrigation is has been placed else return false
     */
    public boolean irrigate() {
        if(board == null) throw new RuntimeException("Board were null");
        return board.placeIrrigation(position, side);
    }

    @Override
    public String toString() {
        return side + " of " + position;
    }

    @Override
    public boolean equals(Object obj) {
        if(obj == null) return false;
        if(!(obj instanceof AbsolutePositionIrrigation)) return false;
        var api = (AbsolutePositionIrrigation)obj;
        return (position.equals(api.position) && side.equals(api.side)) ||
                (position.add(side.getDirection()).equals(api.position) && side.oppositeSide().equals(api.side));
    }

    public boolean canBeIrrigated() {
        if(board == null) throw new RuntimeException("Board were null");
        return board.canPlaceIrrigation(position, side);
    }
}
