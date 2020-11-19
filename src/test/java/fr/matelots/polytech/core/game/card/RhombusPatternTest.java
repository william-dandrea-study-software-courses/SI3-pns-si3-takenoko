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
public class RhombusPatternTest {

    private Set<Position> positions;

    @BeforeEach
    public void init() {
        this.positions = new HashSet<>();
        this.positions.add(new Position(0, 0, 0));
    }

    @Test
    @DisplayName("Aucune position")
    public void emptyPosition() {
        assertThrows(IllegalArgumentException.class, () -> Patterns.RHOMBUS.check(new HashSet<>()));
    }

    @Test @DisplayName("Seulement l'étang")
    public void onlyPond() {
        assertThrows(IllegalArgumentException.class, () -> Patterns.RHOMBUS.check(this.positions));
    }

    @Test @DisplayName("Une position")
    public void onePosition() {
        this.positions.add(new Position(1, -1, 0));
        HashSet<Position> expected = new HashSet<>();
        expected.add(new Position(1, 0, -1));
        expected.add(new Position(2, -1, -1));
        expected.add(new Position(2, 0, -2));
        Set<Position> check = Patterns.RHOMBUS.check(this.positions);
        assertTrue(expected.containsAll(check));
    }

    @Test @DisplayName("Il manque la position du haut et de droite")
    public void missingUpperAndRightVertex() {
        this.positions.add(new Position(1, -1, 0));
        this.positions.add(new Position(1, 0, -1));
        HashSet<Position> expected = new HashSet<>();
        expected.add(new Position(2, -1, -1));
        expected.add(new Position(2, 0, -2));
        assertTrue(expected.containsAll(Patterns.RHOMBUS.check(this.positions)));
    }

    @Test @DisplayName("Il manque la position du haut et de gauche")
    public void missingUpperAndLeftVertex() {
        this.positions.add(new Position(1, -1, 0));
        this.positions.add(new Position(2, -1, -1));
        HashSet<Position> expected = new HashSet<>();
        expected.add(new Position(1, 0, -1));
        expected.add(new Position(2, 0, -2));
        assertTrue(expected.containsAll(Patterns.RHOMBUS.check(this.positions)));
    }

    @Test @DisplayName("Il manque la position de droite et de gauche")
    public void missingRightAndLeftVertex() {
        this.positions.add(new Position(1, -1, 0));
        this.positions.add(new Position(2, 0, -2));
        HashSet<Position> expected = new HashSet<>();
        expected.add(new Position(1, 0, -1));
        expected.add(new Position(2, -1, -1));
        assertTrue(expected.containsAll(Patterns.RHOMBUS.check(this.positions)));
    }

    @Test @DisplayName("Il manque la position de droite")
    public void missingRightVertex() {
        this.positions.add(new Position(1, -1, 0));
        this.positions.add(new Position(2, 0, -2));
        this.positions.add(new Position(1, 0, -1));
        HashSet<Position> expected = new HashSet<>();
        expected.add(new Position(2, -1, -1));
        assertTrue(expected.containsAll(Patterns.RHOMBUS.check(this.positions)));
    }

    @Test @DisplayName("Il manque la position de gauche")
    public void missingLeftVertex() {
        this.positions.add(new Position(1, -1, 0));
        this.positions.add(new Position(2, 0, -2));
        this.positions.add(new Position(2, -1, -1));
        HashSet<Position> expected = new HashSet<>();
        expected.add(new Position(1, 0, -1));
        assertTrue(expected.containsAll(Patterns.RHOMBUS.check(this.positions)));
    }

    @Test @DisplayName("Il manque la position en haut")
    public void missingUpperVertex() {
        this.positions.add(new Position(1, -1, 0));
        this.positions.add(new Position(2, -1, -1));
        this.positions.add(new Position(1, 0, -1));
        HashSet<Position> expected = new HashSet<>();
        expected.add(new Position(2, 0, -2));
        assertTrue(expected.containsAll(Patterns.RHOMBUS.check(this.positions)));
    }

    @Test @DisplayName("motif trouvé")
    public void patternFound() {
        this.positions.add(new Position(1, -1, 0));
        this.positions.add(new Position(2, -1, -1));
        this.positions.add(new Position(1, 0, -1));
        this.positions.add(new Position(2, 0, -2));
        assertEquals(new HashSet<>(), Patterns.RHOMBUS.check(this.positions));
    }
    
}
