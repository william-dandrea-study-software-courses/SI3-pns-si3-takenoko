package fr.matelots.polytech.engine.util;

/**
 * @author Gabriel Cogne
 * @param <E>
 */

public class Position<E extends Number> {
    // Attributes
    private final E x;
    private final E y;
    private final E z;

    // Constructor
    public Position(E x, E y, E z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    // Accessors
    public E getX () {
        return x;
    }
    public E getY() {
        return y;
    }
    public E getZ() {
        return z;
    }

    // Methods and Functions
    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Position) {
            Position<? extends Number> tmp = (Position<? extends Number>) obj;
            return (getX().equals(tmp.getX())) &&
                    (getY().equals(tmp.getY())) &&
                    (getZ().equals(tmp.getZ()));
        }
        return false;
    }

    public static Position<Integer> add(Position<Integer> position, Vector<Integer> vector) {
        int x = Integer.sum(position.getX(), vector.getX());
        int y = Integer.sum(position.getY(), vector.getY());
        int z = Integer.sum(position.getZ(), vector.getZ());

        return new Position<Integer>(x, y, z);
    }


    public static Vector<Integer> substract(Position<Integer> a, Position<Integer> b) {
        return new Vector<>(a.getX() - b.getX(), a.getY() - b.getY(), a.getZ() - b.getZ());
    }

    public static Position<Integer> translate(Position<Integer> depart, Vector<Integer> direction, int distance) {
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
}
