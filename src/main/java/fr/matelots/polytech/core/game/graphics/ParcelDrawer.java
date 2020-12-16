package fr.matelots.polytech.core.game.graphics;

import fr.matelots.polytech.core.game.parcels.Side;
import fr.matelots.polytech.engine.util.Position;

import java.awt.*;

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

    public void setIrrigate(Side side) {
        var characPos = pos.add(side.getConsoleDelta());
        buffer.changeColor(characPos.getX(), characPos.getY(), ConsoleColor.BLUE);
    }

    public void setFertilizer() {
        buffer.setCharacter(0, 1, '⚡', ConsoleColor.WHITE);
    }

    public void setPool() {
        buffer.setCharacter(0, 1, 'P', ConsoleColor.WHITE);
    }

    public void setFence() {
        buffer.setCharacter(0, 1, 'F', ConsoleColor.WHITE);
    }

    // Amenagement : "⚡💧🚪"

}
