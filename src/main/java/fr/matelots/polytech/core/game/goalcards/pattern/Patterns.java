package fr.matelots.polytech.core.game.goalcards.pattern;

import fr.matelots.polytech.engine.util.Position;

import java.util.Set;

/**
 * @author Alexandre Arcil
 * Les différents types de paternes
 */
public enum Patterns {

    TRIANGLE(/*UPPER_VERTEX - RIGHT_VERTEX*/new Position(1, 0, -1), new Position(1, -1, 0)),
    LINE(/*CENTER - DOWN*/new Position(0, -1, 1), new Position(0, -2, 2)),
    RHOMBUS(/*LEFT_VERTEX - RIGHT_VERTEX - UPPER_VERTEX*/new Position(0, 1, -1), new Position(1, 0, -1), new Position(1, 1, -2)),
    C(/*CENTER - UPPER*/new Position(0, 1, -1), new Position(1, 1, -2));

    private final PatternChecker patternChecker;

    Patterns(Position... offsets) {
        this.patternChecker = new PatternChecker(offsets);
    }

    public Set<Position> check(Set<Position> positions) {
        return this.patternChecker.check(positions);
    }

}
