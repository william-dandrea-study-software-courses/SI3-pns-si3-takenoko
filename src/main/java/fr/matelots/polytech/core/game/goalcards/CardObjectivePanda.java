package fr.matelots.polytech.core.game.goalcards;

import fr.matelots.polytech.core.game.parcels.BambooColor;
import fr.matelots.polytech.core.players.IndividualBoard;

import java.util.Objects;

/**
 * @author Alexandre Arcil
 * Représente une carte objectif panda. Il faut que le joueur ai le nombre de bamboo des couleurs demandées pour
 * la compléter.
 */
public class CardObjectivePanda extends CardObjective {

    private final int greenCount;
    private final int pinkCount;
    private final int yellowCount;
    private IndividualBoard individualBoard;

    public CardObjectivePanda(int score, int greenCount, int pinkCount, int yellowCount) {
        super(score);
        this.greenCount = greenCount;
        this.pinkCount = pinkCount;
        this.yellowCount = yellowCount;
    }

    @Override
    public boolean verify() {
        return this.completed = this.individualBoard.getGreenEatenBamboo() >= this.greenCount &&
                this.individualBoard.getPinkEatenBamboo() >= this.pinkCount &&
                this.individualBoard.getYellowEatenBamboo() >= this.yellowCount;
    }

    public void setIndividualBoard(IndividualBoard individualBoard) {
        this.individualBoard = individualBoard;
    }

    public int getCountForColor (BambooColor color) {
        switch (color) {
            case GREEN:
                return greenCount;
            case PINK:
                return pinkCount;
            case YELLOW:
                return yellowCount;
            default:
                return 0;
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        CardObjectivePanda that = (CardObjectivePanda) o;
        return greenCount == that.greenCount && pinkCount == that.pinkCount && yellowCount == that.yellowCount && Objects.equals(individualBoard, that.individualBoard);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), greenCount, pinkCount, yellowCount, individualBoard);
    }

    @Override
    public String toString() {
        return "CardObjectivePanda{" +
                "completed=" + completed +
                ", greenCount=" + greenCount +
                ", pinkCount=" + pinkCount +
                ", yellowCount=" + yellowCount +
                ", individualBoard=" + individualBoard +
                '}';
    }
}
