package fr.matelots.polytech.core.game.goalcards;

import fr.matelots.polytech.core.game.Board;
import fr.matelots.polytech.core.game.goalcards.pattern.Patterns;
import fr.matelots.polytech.engine.util.Position;

import java.util.HashSet;
import java.util.Set;

/**
 * @author Alexandre Arcil
 * Représente une carte objectif parcelle. Il faut que le plateau contienne le motif des parcelles et qu'elles soit de la
 * bonne couleur pour la compléter.
 * Il en existe 15, qui peuvent prendre 4 formes différentes ainsi que des couleurs différentes.
 * Le nombre de point dépend généralement de la difficulté à les compléter.
 */
public class CardObjectiveParcel {

    private final Board board;
    private final int score;
    private final Patterns pattern;
    private Set<Position> missingPositions;
    private boolean completed;

    public CardObjectiveParcel(Board board, int score, Patterns pattern) {
        this.board = board;
        this.score = Math.max(score, 1); //éviter score <= 0
        this.pattern = pattern;
    }

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

    public boolean isCompleted() {
        return completed;
    }

    public int getScore() {
        return score;
    }
}
