package fr.matelots.polytech.engine.util;

/**
 * @author Yann CLODONG
 */
public class Vector<E extends Number> {
    private final E x, y, z;

    public Vector(E X, E Y, E Z) {
        this.x = X;
        this.y = Y;
        this.z = Z;
    }

    public E getX() {
        return x;
    } public E getY() {
        return y;
    } public E getZ() {
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

    public static Vector<Integer> oposite(Vector<Integer> v) {
        return new Vector<>(-v.x, -v.y, -v.z);
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

    public static Vector<Integer> substract(Vector<Integer> a, Vector<Integer> b) {
        return new Vector<>(a.getX() - b.getX(), a.getY() - b.getY(), a.getZ() - b.getZ());
    }

    public static Vector<Integer> naiveNormalize(Vector<Integer> v) {
        if(v.x == 0 && v.y == 0 && v.z == 0) {
            return new Vector<>(0, 0, 0);
        }
        else if(v.x == 0 ) {
            int sign = v.y > 0 ? 1 : -1;
            return new Vector<>(0, sign, -sign);
        }
        else if(v.y == 0) {
            int sign = v.x > 0 ? 1 : -1;
            return new Vector<>(sign, -sign, 0);
        }
        else if(v.z == 0) {
            int sign = v.x > 0 ? 1 : -1;
            return new Vector<>(sign, 0, -sign);
        }
        return null;
    }
}
