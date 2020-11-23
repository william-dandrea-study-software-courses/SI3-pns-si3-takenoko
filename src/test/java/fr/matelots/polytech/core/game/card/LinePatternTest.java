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
 * @author Alexandre Arcil
 */
public class LinePatternTest {

    private Set<Position> positions;

    @BeforeEach
    public void init() {
        this.positions = new HashSet<>();
        this.positions.add(new Position(0, 0, 0));
    }

    @Test
    @DisplayName("Aucune position")
    public void emptyPosition() {
        assertThrows(IllegalArgumentException.class, () -> Patterns.LINE.check(new HashSet<>()));
    }

    @Test @DisplayName("Seulement l'étang")
    public void onlyPond() {
        assertThrows(IllegalArgumentException.class, () -> Patterns.LINE.check(this.positions));
    }

    @Test @DisplayName("Une position")
    public void onePosition() {
        this.positions.add(new Position(1, -1, 0));
        HashSet<Position> expected = new HashSet<>();
        expected.add(new Position(1, -2, 1));
        expected.add(new Position(1, -3, 2));
        assertTrue(expected.containsAll(Patterns.LINE.check(this.positions)));
    }

    @Test @DisplayName("Il manque le bas de la ligne")
    public void missingDown() {
        this.positions.add(new Position(1, -1, 0));
        this.positions.add(new Position(1, -2, 1));
        HashSet<Position> expected = new HashSet<>();
        expected.add(new Position(1, -3, 2));
        assertTrue(expected.containsAll(Patterns.LINE.check(this.positions)));
    }

    @Test @DisplayName("Il manque le centre de la ligne")
    public void missingCenter() {
        this.positions.add(new Position(1, -1, 0));
        this.positions.add(new Position(1, -3, 2));
        HashSet<Position> expected = new HashSet<>();
        expected.add(new Position(1, -2, 1));
        assertTrue(expected.containsAll(Patterns.LINE.check(this.positions)));
    }

    @Test @DisplayName("motif trouvé")
    public void patternFound() {
        this.positions.add(new Position(1, -1, 0));
        this.positions.add(new Position(1, -2, 1));
        this.positions.add(new Position(1, -3, 2));
        assertEquals(new HashSet<>(), Patterns.LINE.check(this.positions));
    }

    @Test @DisplayName("check ne retourne pas l'étang qui se trouve au centre")
    public void checkNotReturnPondCenter() {
        this.positions.add(new Position(0, 1, -1));
        HashSet<Position> expected = new HashSet<>();
        expected.add(new Position(0, 2, -2));
        expected.add(new Position(0, 3, -3));
        assertTrue(expected.containsAll(Patterns.LINE.check(this.positions)));
    }

    @Test @DisplayName("check ne retourne pas l'étang qui se trouve en haut")
    public void checkNotReturnPondDown() { //checkNotReturnPondCenter le fait déjà en soit
        this.positions.add(new Position(0, 2, -2));
        HashSet<Position> expected = new HashSet<>();
        expected.add(new Position(0, 1, -1));
        expected.add(new Position(0, 3, -3));
        assertTrue(expected.containsAll(Patterns.LINE.check(this.positions)));
    }

}
