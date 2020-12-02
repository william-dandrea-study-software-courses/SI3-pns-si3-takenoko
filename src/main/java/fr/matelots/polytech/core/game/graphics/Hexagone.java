package fr.matelots.polytech.core.game.graphics;

import fr.matelots.polytech.core.game.parcels.Parcel;
import fr.matelots.polytech.engine.util.Position;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Yann Clodong
 */
public class Hexagone {
    //  / \ / \
    // | c |   |
    //  \ / \ /
    //   |   |
    //    \ /


    private final int radius = 2;
    private final Position position;

    public Hexagone(Position position) {
        this.position = position;
    }

    public void printHexa(BoardDrawingBuffer buffer, Parcel parcel) {
        int x = position.getX();
        int y = position.getY();

        String plantationStr = parcel.toString();
        buffer.setCharacter(x - radius, y, '|');
        buffer.setCharacter(x + radius, y, '|');
        buffer.setCharacter(x - radius / 2, y + radius / 2, '\\');
        buffer.setCharacter(x + radius / 2, y - radius / 2, '\\');
        buffer.setCharacter(x - radius / 2, y - radius / 2, '/');
        buffer.setCharacter(x + radius / 2, y + radius / 2, '/');

        if(plantationStr.length() > 1)
            buffer.setCharacter(x - radius / 2, y, plantationStr.charAt(0));
        else if(plantationStr.length() == 1)
            buffer.setCharacter(x, y, plantationStr.charAt(0));

        if(plantationStr.length() >= 2)
            buffer.setCharacter(x + radius / 2, y, plantationStr.charAt(1));
    }

    public Map<Position, Position> getNeighbours() {
        Map<Position, Position> cores = new HashMap<>();
        cores.put(new Position(0, -1, 1), new Position(-radius, -radius, 0));
        cores.put(new Position(1, -1, 0), new Position(radius, -radius, 0));
        cores.put(new Position(1, 0, -1), new Position(2*radius, 0, 0));
        cores.put(new Position(0, 1, -1), new Position(+radius, radius, 0));
        cores.put(new Position(-1, 1, 0), new Position(-radius, radius, 0));
        cores.put(new Position(-1, 0, 1), new Position(-2 * radius, 0, 0));
        return cores;
    }
}
