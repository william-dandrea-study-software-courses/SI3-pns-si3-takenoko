package fr.matelots.polytech.engine.util;

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
