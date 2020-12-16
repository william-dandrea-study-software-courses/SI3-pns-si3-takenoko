package fr.matelots.polytech.core.game.parcels;

import fr.matelots.polytech.engine.util.Position;

public enum Side {

    UPPER_RIGHT(0, new Position(1, 0, -1)),
    RIGHT(1, new Position(1, -1, 0)),
    BOTTOM_RIGHT(2, new Position(0, -1, 1)),
    BOTTOM_LEFT(3, new Position(-1, 0, 1)),
    LEFT(4, new Position(-1, 1, 0)),
    UPPER_LEFT(5, new Position(0, 1, -1));

    private final Position direction;
    private final int n;
    private final int nRidgeAdjacent;
    private final int nRightRidge;
    private final int nLeftRidge;

    Side(int n, Position direction) {
        this.direction = direction;
        this.n = n;
        this.nRidgeAdjacent = n >= 3 ? n - 3 : n + 3;
        this.nRightRidge = n == 5 ? 0 : n + 1;
        this.nLeftRidge = n == 0 ? 5 : n - 1;
    }

    /**
     * @return Le côté opposé
     */
    public Side oppositeSide() {
        for(Side ridge : values()) {
            if(ridge.n == this.nRidgeAdjacent)
                return ridge;
        }
        throw new RuntimeException("Le ridge '"+this.toString()+"' n'a pas de ridge adjacent");
    }

    /**
     * @return Le côté à droite
     */
    public Side rightSide() {
        for(Side ridge : values()) {
            if(ridge.n == this.nRightRidge)
                return ridge;
        }
        throw new RuntimeException("Le ridge '"+this.toString()+"' n'a pas de ridge à sa droite");
    }

    /**
     * @return Le côté à gauche
     */
    public Side leftSide() {
        for(Side ridge : values()) {
            if(ridge.n == this.nLeftRidge)
                return ridge;
        }
        throw new RuntimeException("Le ridge '"+this.toString()+"' n'a pas de ridge à sa gauche");
    }

    public Position getConsoleDelta() {
        switch (this) {
            case LEFT: return new Position(-2, 0, 0);
            case RIGHT: return new Position(2, 0, 0);
            case BOTTOM_LEFT: return new Position(-1, 1, 0);
            case BOTTOM_RIGHT: return new Position(1, 1, 0);
            case UPPER_LEFT: return new Position(-1, -1, 0);
            case UPPER_RIGHT: return new Position(1, -1, 0);
        }
        return null;
    }

    public Position getDirection() {
        return direction;
    }

}
