package fr.matelots.polytech.core.game.graphics;

import fr.matelots.polytech.core.game.Parcel;
import fr.matelots.polytech.engine.util.Vector;
import fr.matelots.polytech.engine.util.Vector2Int;

import java.util.HashMap;
import java.util.Map;

public class Hexagone {
    //  / \ / \
    // | c |   |
    //  \ / \ /
    //   |   |
    //    \ /


    private final int radius = 2;
    Vector2Int position;

    public Hexagone(Vector2Int position) {
        this.position = position;
    }

    public void PrintHexa(BoardDrawingBuffer buffer, Parcel parcel) {
        int X = position.getX();
        int Y = position.getY();

        buffer.SetCharac(X - radius, Y, '|');
        buffer.SetCharac(X + radius, Y, '|');
        buffer.SetCharac(X - radius / 2, Y + radius / 2, '\\');
        buffer.SetCharac(X + radius / 2, Y - radius / 2, '\\');
        buffer.SetCharac(X - radius / 2, Y - radius / 2, '/');
        buffer.SetCharac(X + radius / 2, Y + radius / 2, '/');
    }

    public Map<Vector<Integer>, Vector2Int> GetNeighboors() {
        Map<Vector<Integer>, Vector2Int> corres = new HashMap<>();
        corres.put(new Vector<>(0, -1, 1), new Vector2Int(-radius, -radius));
        corres.put(new Vector<>(1, -1, 0), new Vector2Int(radius, -radius));
        corres.put(new Vector<>(1, 0, -1), new Vector2Int(2*radius, 0));
        corres.put(new Vector<>(0, 1, -1), new Vector2Int(+radius, radius));
        corres.put(new Vector<>(-1, 1, 0), new Vector2Int(-radius, radius));
        corres.put(new Vector<>(-1, 0, 1), new Vector2Int(-2 * radius, 0));
        return corres;
    }
}
