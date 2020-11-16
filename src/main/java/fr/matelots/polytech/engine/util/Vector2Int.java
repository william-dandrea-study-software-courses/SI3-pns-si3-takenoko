package fr.matelots.polytech.engine.util;

public class Vector2Int {
    private final int X, Y;

    public Vector2Int(int x, int y) {
        this.X = x;
        this.Y = y;
    }

    public int getX() {
        return X;
    }

    public int getY() {
        return Y;
    }

    public static Vector2Int add(Vector2Int a, Vector2Int b) {
        return new Vector2Int(a.getX() + b.getX(), a.getY() + b.getY());
    }
}
