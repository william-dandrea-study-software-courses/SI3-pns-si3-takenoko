package fr.matelots.polytech.core.game.goalcards;

import java.util.Objects;

/**
 * @author Alexandre Arcil
 * Représente une carte objectif.
 */
public abstract class CardObjective {

    protected boolean completed;
    private final int score;

    public CardObjective(int score) {
        this.score = Math.max(score, 1);
    }

    /**
     * Vérifie que l'objectif est validé.
     * @return true si l'objetif est rempli, false sinon.
     */
    public abstract boolean verify();

    /**
     * Permet de savoir si l'objectif est complété.
     * @return true si l'objectif est complété, false sinon.
     */
    public boolean isCompleted() {
        return completed;
    }

    /**
     * Le score remporté par le bot quand l'objectif est complété.
     * @return
     */
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
