package fr.matelots.polytech.engine.util;

public class Vector<E extends Number> {
    private final E x, y, z;

    public Vector(E X, E Y, E Z) {
        this.x = X;
        this.y = Y;
        this.z = Z;
    }

    public E getX() {
        return x;
    }public E getY() {
        return y;
    }public E getZ() {
        return z;
    }

    public static Vector<Integer>[] GetAllDirections() {
        return new Vector[]{
                new Vector(1, -1, 0),
                new Vector<>(0, -1, 1),
                new Vector<>(-1, 0, 1),
                new Vector<>(-1, 1, 0),
                new Vector<>(0, 1, -1),
                new Vector<>(1, 0, -1)
        };
    }

    // Methods and Functions
    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Vector) {
            Position<? extends Number> tmp = (Position<? extends Number>) obj;
            return (getX().equals(tmp.getX())) &&
                    (getY().equals(tmp.getY())) &&
                    (getZ().equals(tmp.getZ()));
        }
        return false;
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

    public static Vector<Integer> multiply(Vector<Integer> i, int t) {
        return new Vector<>(i.x * t, i.y * t, i.z * t);
    }
}
