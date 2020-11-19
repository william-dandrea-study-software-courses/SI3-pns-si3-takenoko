package fr.matelots.polytech.engine.util;

import java.util.Objects;

/**
 * This class describe a position of a point in a normed system
 * @author Gabriel Cogne
 */

public class Position {
    // Attributes
    private final int x;
    private final int y;
    private final int z;

    // Constructor
    public Position(int x, int y, int z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    // Accessors
    public int getX () {
        return x;
    }
    public int getY() {
        return y;
    }
    public int getZ() {
        return z;
    }

    // Methods and Functions
    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Position) {
            Position tmp = (Position) obj;
            return (getX() == (tmp.getX())) &&
                    (getY() == (tmp.getY())) &&
                    (getZ() == (tmp.getZ()));
        }
        return false;
    }

    public static Position add(Position position, Vector vector) {
        int x = Integer.sum(position.getX(), vector.getX());
        int y = Integer.sum(position.getY(), vector.getY());
        int z = Integer.sum(position.getZ(), vector.getZ());

        return new Position(x, y, z);
    }

    public Position add(Position position) {
        int x = this.getX() + position.getX();
        int y = this.getY() + position.getY();
        int z = this.getZ() + position.getZ();
        return new Position(x, y, z);
    }


    public static Vector substract(Position a, Position b) {
        return new Vector(a.getX() - b.getX(), a.getY() - b.getY(), a.getZ() - b.getZ());
    }

    public static Position translate(Position depart, Vector direction, int distance) {
        return add(depart, Vector.multiply(direction, distance));
    }

    @Override
    public String toString() {
        final StringBuilder builder = new StringBuilder();
        builder.append("x=");
        builder.append(getX());
        builder.append(", y=");
        builder.append(getY());
        builder.append(", z=");
        builder.append(getZ());
        return builder.toString();
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y, z);
    }
}
