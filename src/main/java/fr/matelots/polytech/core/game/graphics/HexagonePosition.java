package fr.matelots.polytech.core.game.graphics;

import fr.matelots.polytech.engine.util.Position;

public enum HexagonePosition {

    CENTER(new Position(0, 0, 0)),
    UP(new Position(0, -1, 0)),
    DOWN(new Position(0, 1, 0)),
    LEFT(new Position(-1, 0, 0)),
    RIGHT(new Position(1, 0, 0));

    private final Position pos;
    HexagonePosition(Position pos) {
        this.pos = pos;
    }

    Position getPosition() {
        return pos;
    }
}
