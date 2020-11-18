package fr.matelots.polytech.core.game.graphics;

import fr.matelots.polytech.engine.util.Vector;
import fr.matelots.polytech.engine.util.Vector2Int;

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
    private final Vector2Int position;

    public Hexagone(Vector2Int position) {
        this.position = position;
    }

    public void printHexa(BoardDrawingBuffer buffer) {
        int x = position.getX();
        int y = position.getY();

        buffer.setCharacter(x - radius, y, '|');
        buffer.setCharacter(x + radius, y, '|');
        buffer.setCharacter(x - radius / 2, y + radius / 2, '\\');
        buffer.setCharacter(x + radius / 2, y - radius / 2, '\\');
        buffer.setCharacter(x - radius / 2, y - radius / 2, '/');
        buffer.setCharacter(x + radius / 2, y + radius / 2, '/');
    }

    public Map<Vector, Vector2Int> getNeighbours() {
        Map<Vector, Vector2Int> cores = new HashMap<>();
        cores.put(new Vector(0, -1, 1), new Vector2Int(-radius, -radius));
        cores.put(new Vector(1, -1, 0), new Vector2Int(radius, -radius));
        cores.put(new Vector(1, 0, -1), new Vector2Int(2*radius, 0));
        cores.put(new Vector(0, 1, -1), new Vector2Int(+radius, radius));
        cores.put(new Vector(-1, 1, 0), new Vector2Int(-radius, radius));
        cores.put(new Vector(-1, 0, 1), new Vector2Int(-2 * radius, 0));
        return cores;
    }
}
