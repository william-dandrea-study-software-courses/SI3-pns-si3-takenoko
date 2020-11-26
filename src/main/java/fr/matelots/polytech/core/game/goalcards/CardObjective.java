package fr.matelots.polytech.core.game.goalcards;

import fr.matelots.polytech.core.game.Board;

import java.util.Objects;

/**
 * @author Alexandre Arcil
 */
public abstract class CardObjective {

    protected boolean completed;
    protected final Board board;
    private final int score;

    public CardObjective(Board board, int score) {
        this.board = board;
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
                score == that.score &&
                board.equals(that.board);
    }

    @Override
    public int hashCode() {
        return Objects.hash(completed, board, score);
    }
}
