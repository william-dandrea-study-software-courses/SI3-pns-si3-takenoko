package fr.matelots.polytech.core.game.graphics;

import fr.matelots.polytech.core.game.parcels.Layout;
import fr.matelots.polytech.core.game.parcels.Side;
import fr.matelots.polytech.engine.util.Position;

public class ParcelDrawer {

    private final Position pos;
    private final BoardDrawingBuffer buffer;

    public ParcelDrawer(BoardDrawingBuffer buffer, Position boardPosition) {
        this.pos = boardPosition;
        this.buffer = buffer;
    }

    private Position getPosition(Position position) {
        return position.add(pos);
    }

    public void set(HexagonePosition position, ConsoleColor color, char c) {
        var p = getPosition(position.getPosition());
        buffer.setCharacter(p.getX(), p.getY(), c, color);
    }

    public void setIrrigate(Side side) {
        var position = getPosition(side.getConsoleDelta());
        buffer.changeColor(position.getX(), position.getY(), ConsoleColor.BLUE);
    }

    private void setLayoutChar(char charac) {
        buffer.setCharacter(pos.getX(), pos.getY() + 1, charac, ConsoleColor.WHITE);
    }

    private void setFertilizer() {
        setLayoutChar('G');
    }

    private void setPool() {
        setLayoutChar('P');
    }

    private void setFence() {
        setLayoutChar('F');
    }

    public void setLayout(Layout layout) {
        if(layout != null) {
            switch (layout) {
                case FERTILIZER:
                    setFertilizer();
                    break;
                case BASIN:
                    setPool();
                    break;
                case ENCLOSURE:
                    setFence();
                    break;
            }
        }
    }

    // Amenagement : "⚡💧🚪"

}
