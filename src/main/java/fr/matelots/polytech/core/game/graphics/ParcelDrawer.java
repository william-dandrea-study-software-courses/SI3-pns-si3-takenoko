package fr.matelots.polytech.core.game.graphics;

import fr.matelots.polytech.engine.util.Position;

public class ParcelDrawer {

    private final Position pos;
    private final BoardDrawingBuffer buffer;

    public ParcelDrawer(BoardDrawingBuffer buffer, Position boardPosition) {
        this.pos = boardPosition;
        this.buffer = buffer;
    }

    public void set(HexagonePosition position, ConsoleColor color, char c) {
        Position p = position.getPosition().add(pos);

        buffer.setCharacter(p.getX(), p.getY(), c, color);
    }

}
