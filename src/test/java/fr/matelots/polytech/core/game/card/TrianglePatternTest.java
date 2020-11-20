package fr.matelots.polytech.core.game.card;

import fr.matelots.polytech.core.game.goalcards.pattern.Patterns;
import fr.matelots.polytech.engine.util.Position;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author AlexandreArcil
 */
public class TrianglePatternTest {

    private Set<Position> positions;

    @BeforeEach
    public void init() {
        this.positions = new HashSet<>();
        this.positions.add(new Position(0, 0, 0));
    }

    @Test @DisplayName("Aucune position")
    public void emptyPosition() {
        assertThrows(IllegalArgumentException.class, () -> Patterns.TRIANGLE.check(new HashSet<>()));
    }

    @Test @DisplayName("Seulement l'étang")
    public void onlyPond() {
        assertThrows(IllegalArgumentException.class, () -> Patterns.TRIANGLE.check(this.positions));
    }

    @Test @DisplayName("Une position")
    public void onePosition() {
        this.positions.add(new Position(1, -1, 0));
        HashSet<Position> expected = new HashSet<>();
        expected.add(new Position(2, -2, 0));
        expected.add(new Position(2, -1, -1));
        assertTrue(expected.containsAll(Patterns.TRIANGLE.check(this.positions)));
    }

    @Test @DisplayName("Il manque le sommet haut du triangle")
    public void missingUpperVertex() {
        this.positions.add(new Position(1, -1, 0));
        this.positions.add(new Position(2, -2, 0));
        HashSet<Position> expected = new HashSet<>();
        expected.add(new Position(2, -1, -1));
        assertTrue(expected.containsAll(Patterns.TRIANGLE.check(this.positions)));
    }

    @Test @DisplayName("Il manque le sommet droit du triangle")
    public void missingRightVertex() {
        this.positions.add(new Position(1, -1, 0));
        this.positions.add(new Position(2, -1, -1));
        HashSet<Position> expected = new HashSet<>();
        expected.add(new Position(2, -2, 0));
        assertTrue(expected.containsAll(Patterns.TRIANGLE.check(this.positions)));
    }

    @Test @DisplayName("motif trouvé")
    public void patternFound() {
        this.positions.add(new Position(1, -1, 0));
        this.positions.add(new Position(2, -1, -1));
        this.positions.add(new Position(2, -2, 0));
        assertEquals(new HashSet<>(), Patterns.TRIANGLE.check(this.positions));
    }

}
