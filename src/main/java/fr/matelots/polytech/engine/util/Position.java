package fr.matelots.polytech.engine.util;

import java.util.Objects;

/**
 * This class describe a position of a point in a normed system
 * @author Gabriel Cogne
 */

public class Position {

    private final int x;
    private final int y;
    private final int z;

    public Position(int x, int y, int z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public int getX () {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getZ() {
        return z;
    }

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

    /**
     * Crée un nouvelle position résultante de l'addition entre cette position et une autre
     * @param position position à ajouter à la courante
     * @return une nouvelle position
     */
    public Position add(Position position) {
        int x = this.getX() + position.getX();
        int y = this.getY() + position.getY();
        int z = this.getZ() + position.getZ();
        return new Position(x, y, z);
    }

    /**
     * Calcul une position avec les coordonnées de cette position, soustrait avec les coordonnées de {@param position}
     * @param position La position avec qui soustraire
     * @return Une nouvelle instance de position qui est le résultat de ce calcul
     */
    public Position remove(Position position) {
        int x = this.getX() - position.getX();
        int y = this.getY() - position.getY();
        int z = this.getZ() - position.getZ();
        return new Position(x, y, z);
    }

    /**
     * Multiplie les coordonnées par {@param i} et retourne le résultat par une nouvelle instance
     * @param i Le nombre par lequel mutliplier les coordonées
     * @return Une nouvelle instance de Poistion qui est le résultat de ce calcul
     */
    public Position mul(int i) {
        int x = i * this.x;
        int y = i * this.y;
        int z = i * this.z;
        return new Position(x, y, z);
    }

    @Override
    public String toString() {
        return "Position{" +
                "x=" + x +
                ", y=" + y +
                ", z=" + z +
                '}';
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y, z);
    }
}
