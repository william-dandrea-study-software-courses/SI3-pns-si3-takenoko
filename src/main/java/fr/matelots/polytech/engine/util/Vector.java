package fr.matelots.polytech.engine.util;

/**
 * This class describe a vector in 3 dimensions and can do operations on 3 dimensions vectors
 * @author Yann CLODONG
 */
public class Vector {
    private final int x, y, z;

    public Vector(int X, int Y, int Z) {
        this.x = X;
        this.y = Y;
        this.z = Z;
    }

    public int getX() {
        return x;
    } public int getY() {
        return y;
    } public int getZ() {
        return z;
    }

    public static Vector[] GetAllDirections() {
        return new Vector[]{
                new Vector(1, -1, 0),
                new Vector(0, -1, 1),
                new Vector(-1, 0, 1),
                new Vector(-1, 1, 0),
                new Vector(0, 1, -1),
                new Vector(1, 0, -1)
        };
    }

    public static Vector oposite(Vector v) {
        return new Vector(-v.x, -v.y, -v.z);
    }

    // Methods and Functions
    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Vector) {
            Position tmp = (Position) obj;
            return (getX() == (tmp.getX())) &&
                    (getY() == (tmp.getY())) &&
                    (getZ() == (tmp.getZ()));
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

    public static Vector multiply(Vector i, int t) {
        return new Vector(i.x * t, i.y * t, i.z * t);
    }

    public static Vector substract(Vector a, Vector b) {
        return new Vector(a.getX() - b.getX(), a.getY() - b.getY(), a.getZ() - b.getZ());
    }

    public static Vector naiveNormalize(Vector v) {
        if(v.x == 0 && v.y == 0 && v.z == 0) {
            return new Vector(0, 0, 0);
        }
        else if(v.x == 0 ) {
            int sign = v.y > 0 ? 1 : -1;
            return new Vector(0, sign, -sign);
        }
        else if(v.y == 0) {
            int sign = v.x > 0 ? 1 : -1;
            return new Vector(sign, -sign, 0);
        }
        else if(v.z == 0) {
            int sign = v.x > 0 ? 1 : -1;
            return new Vector(sign, 0, -sign);
        }
        return null;
    }
}
