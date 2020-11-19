package fr.matelots.polytech.engine.util;

/**
 * This class describe a vector in 2 dimensions
 * @author Yann CLODONG
 */
public class Vector2Int {
    private final int x, y;

    public Vector2Int(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return x;
    }
    public int getY() {
        return y;
    }


    public static Vector2Int add(Vector2Int a, Vector2Int b) {
        return new Vector2Int(a.getX() + b.getX(), a.getY() + b.getY());
    }
}
