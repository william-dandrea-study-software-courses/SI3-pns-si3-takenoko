package fr.matelots.polytech.core.game.goalcards.pattern;

import fr.matelots.polytech.engine.util.Position;

import java.util.Set;

/**
 * @author Alexandre Arcil
 * Ancien algorithme, doit-il être supprimé ou PatternChecker doit-il l'implémenter ?
 */
public interface Pattern {

    /**
     * Check si le motif est contenue dans @param positionSet.
     * @param positionSet Les positions
     * @return La liste des positions manquantes pour compléter le motif. Vide s'il le trouve.
     * @throws IllegalArgumentException si @param positionSet est vide
     */
    public Set<Position> check(Set<Position> positionSet);

}
