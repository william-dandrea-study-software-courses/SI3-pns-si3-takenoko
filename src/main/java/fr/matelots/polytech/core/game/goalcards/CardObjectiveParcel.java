package fr.matelots.polytech.core.game.goalcards;

import fr.matelots.polytech.core.game.Board;
import fr.matelots.polytech.core.game.goalcards.pattern.Patterns;
import fr.matelots.polytech.engine.util.Position;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * @author Alexandre Arcil
 * Représente une carte objectif parcelle. Il faut que le plateau contienne le motif des parcelles et qu'elles soit de la
 * bonne couleur pour la compléter.
 * Il en existe 15, qui peuvent prendre 4 formes différentes ainsi que des couleurs différentes.
 * Le nombre de point dépend généralement de la difficulté à les compléter.
 */
public class CardObjectiveParcel extends CardObjective {

    private final Board board;
    private final Patterns pattern;
    private Set<Position> missingPositions;

    public CardObjectiveParcel(Board board, int score, Patterns pattern) {
        super(score);
        this.board = board;
        this.pattern = pattern;
    }

    @Override
    public boolean verify() {
        if(this.completed)
            return true;
        Set<Position> positions = this.board.getPositions();
        this.missingPositions = new HashSet<>(this.pattern.check(positions));
        this.completed = this.missingPositions.isEmpty();
        return this.completed;
    }

    /**
     * @return Les positions où ajouter des tuiles pour compléter l'objectif. Donne toujours le cas dans lequel il faut
     * placer le moins de parcelles possible. Ou null si {@link #verify()} n'a jamais été appelée avant.
     */
    public Set<Position> getMissingPositionsToComplete() {
        return this.missingPositions;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        CardObjectiveParcel that = (CardObjectiveParcel) o;
        return board.equals(that.board) &&
                pattern == that.pattern &&
                Objects.equals(missingPositions, that.missingPositions);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), board, pattern, missingPositions);
    }

    public Patterns getPattern() {
        return pattern;
    }

    @Override
    public String toString() {
        return "CardObjectiveParcel{" +
                "board=" + board +
                ", pattern=" + pattern +
                ", missingPositions=" + missingPositions +
                '}';
    }
}
