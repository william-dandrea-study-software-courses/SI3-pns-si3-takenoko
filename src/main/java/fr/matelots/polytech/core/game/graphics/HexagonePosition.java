package fr.matelots.polytech.core.game.graphics;

import fr.matelots.polytech.engine.util.Position;

public enum HexagonePosition {
    center(new Position(0, 0, 0)),
    up(new Position(0, -1, 0)),
    down(new Position(0, 1, 0)),
    left(new Position(-1, 0, 0)),
    right(new Position(1, 0, 0));

    private Position pos;
    HexagonePosition(Position pos) {
        this.pos = pos;
    }

    Position getPosition() {
        return pos;
    }
}
