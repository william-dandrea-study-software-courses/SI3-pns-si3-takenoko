package fr.matelots.polytech.core.game.goalcards.pattern;

import fr.matelots.polytech.core.game.parcels.BambooColor;
import fr.matelots.polytech.engine.util.Position;

import java.util.Objects;

/**
 * Représente une position avec une couleur
 * @author Alexandre Arcil
 */
public class PositionColored {

    private final Position position;
    private final BambooColor color;

    public PositionColored(Position position, BambooColor color) {
        this.position = position;
        this.color = color;
    }

    public Position getPosition() {
        return position;
    }

    public BambooColor getColor() {
        return color;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PositionColored that = (PositionColored) o;
        return Objects.equals(position, that.position) && color == that.color;
    }

    @Override
    public int hashCode() {
        return Objects.hash(position, color);
    }

    @Override
    public String toString() {
        return "PositionColored{" +
                "position=" + position +
                ", color=" + color +
                '}';
    }

}
