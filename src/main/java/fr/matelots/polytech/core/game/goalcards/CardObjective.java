package fr.matelots.polytech.core.game.goalcards;

import java.util.Objects;

/**
 * @author Alexandre Arcil
 */
public abstract class CardObjective {

    protected boolean completed;
    private final int score;

    public CardObjective(int score) {
        this.score = Math.max(score, 1);
    }

    public abstract boolean verify();

    public boolean isCompleted() {
        return completed;
    }

    public int getScore() {
        return score;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CardObjective that = (CardObjective) o;
        return completed == that.completed &&
                score == that.score;
    }

    @Override
    public int hashCode() {
        return Objects.hash(completed, score);
    }

}
