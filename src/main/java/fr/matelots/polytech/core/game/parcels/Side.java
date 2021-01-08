package fr.matelots.polytech.core.game.parcels;

import fr.matelots.polytech.engine.util.Position;

/**
 * Représente le côté d'une parcelle
 */
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

    /**
     * Donne le côté touché par pos2 sur pos1.
     * Equivalent à {@code pos2 = pos1.add(side.getDirection());}
     * @param pos1 La position initiale
     * @param pos2 La position qui touche la position initiale
     * @return Le côté de pos1 où pos2 le touche. Null s'ils ne sont pas voisins.
     */
    public static Side getTouchedSide(Position pos1, Position pos2) {
        Position diff = pos2.remove(pos1);
        for (Side side : values()) {
            if(side.direction.equals(diff))
                return side;
        }
        return null;
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
