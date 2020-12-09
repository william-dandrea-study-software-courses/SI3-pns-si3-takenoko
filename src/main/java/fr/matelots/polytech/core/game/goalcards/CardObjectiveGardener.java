package fr.matelots.polytech.core.game.goalcards;

import fr.matelots.polytech.core.game.Board;
import fr.matelots.polytech.core.game.parcels.BambooColor;
import fr.matelots.polytech.core.game.parcels.BambooPlantation;
import fr.matelots.polytech.core.game.parcels.Parcel;
import fr.matelots.polytech.engine.util.Position;

import java.util.Objects;

/**
 * @author Alexandre Arcil
 * Représente une carte jardinier. Il faut que le jardinier récupère un certaine nombre de bamboos d'une couleur et d'une
 * certaine taille pour compléter l'objectif.
 */
public class CardObjectiveGardener extends CardObjective {

    private final Board board;
    private final BambooColor color;
    private final int size;
    private final int count;

    public CardObjectiveGardener(Board board, int score, BambooColor color, int size, int count) {
        super(score);
        this.board = board;
        this.color = color;
        this.size = size;
        this.count = count;
    }

    @Override
    public boolean verify() {
        if(this.completed)
            return true;
        int count = 0;
        for (Position pos : this.board.getPositions()) {
            Parcel parcel = this.board.getParcel(pos);
            if(parcel instanceof BambooPlantation) {
                BambooPlantation plantation = (BambooPlantation) parcel;
                if(plantation.getBambooSize() == this.size && plantation.getBambooColor() == this.color)
                    count++;
            }
        }
        this.completed = count >= this.count;
        return this.completed;
    }

    public BambooColor getColor() {
        return color;
    }

    public int getSize() {
        return size;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        CardObjectiveGardener that = (CardObjectiveGardener) o;
        return size == that.size &&
                count == that.count &&
                color == that.color;
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), color, size, count);
    }

    @Override
    public String toString() {
        return "CardObjectiveGardener{" +
                "completed=" + completed +
                ", board=" + board +
                ", color=" + color +
                ", size=" + size +
                ", count=" + count +
                '}';
    }
}
