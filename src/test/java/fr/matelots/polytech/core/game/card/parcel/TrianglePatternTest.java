package fr.matelots.polytech.core.game.card.parcel;

import fr.matelots.polytech.core.game.Config;
import fr.matelots.polytech.core.game.goalcards.pattern.Patterns;
import fr.matelots.polytech.core.game.goalcards.pattern.PositionColored;
import fr.matelots.polytech.core.game.parcels.BambooColor;
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

    private Set<PositionColored> positions;

    @BeforeEach
    public void init() {
        this.positions = new HashSet<>();
        this.positions.add(new PositionColored(Config.POND_POSITION, null));
    }

    /*@Test @DisplayName("Aucune position")
    public void emptyPosition() {
        assertThrows(IllegalArgumentException.class, () -> Patterns.TRIANGLE.check(new HashSet<>()));
    }

    @Test @DisplayName("Seulement l'étang")
    public void onlyPond() {
        assertThrows(IllegalArgumentException.class, () -> Patterns.TRIANGLE.check(this.positions));
    }*/

    @Test @DisplayName("Nombre de couleurs différent du nombre d'offsets")
    public void colorNumberDifferenceOffsetNumber() {
        assertThrows(IllegalArgumentException.class, () -> Patterns.TRIANGLE.check(this.positions, BambooColor.GREEN, BambooColor.GREEN));
        assertThrows(IllegalArgumentException.class, () -> Patterns.TRIANGLE.check(this.positions, BambooColor.GREEN, BambooColor.GREEN, BambooColor.GREEN, BambooColor.GREEN));
    }

    @Test @DisplayName("Une position")
    public void onePosition() {
        this.positions.add(new PositionColored(new Position(-1, 0, 1), BambooColor.GREEN));
        HashSet<PositionColored> expected = new HashSet<>();
        expected.add(new PositionColored(new Position(-2, 0, 2), BambooColor.GREEN));
        expected.add(new PositionColored(new Position(-1, -1, 2), BambooColor.GREEN));
        Set<PositionColored> check = Patterns.TRIANGLE.check(this.positions, BambooColor.GREEN, BambooColor.GREEN, BambooColor.GREEN);
        assertEquals(check, expected);
    }

    @Test @DisplayName("Il manque le sommet haut du triangle")
    public void missingUpperVertex() {
        this.positions.add(new PositionColored(new Position(1, -1, 0), BambooColor.GREEN));
        this.positions.add(new PositionColored(new Position(2, -2, 0), BambooColor.GREEN));
        HashSet<PositionColored> expected = new HashSet<>();
        expected.add(new PositionColored(new Position(2, -1, -1), BambooColor.GREEN));
        assertTrue(expected.containsAll(Patterns.TRIANGLE.check(this.positions, BambooColor.GREEN, BambooColor.GREEN, BambooColor.GREEN)));
    }

    @Test @DisplayName("Il manque le sommet droit du triangle")
    public void missingRightVertex() {
        this.positions.add(new PositionColored(new Position(1, -1, 0), BambooColor.GREEN));
        this.positions.add(new PositionColored(new Position(2, -1, -1), BambooColor.GREEN));
        HashSet<PositionColored> expected = new HashSet<>();
        expected.add(new PositionColored(new Position(2, -2, 0), BambooColor.GREEN));
        assertTrue(expected.containsAll(Patterns.TRIANGLE.check(this.positions, BambooColor.GREEN, BambooColor.GREEN, BambooColor.GREEN)));
    }

    @Test @DisplayName("motif trouvé")
    public void patternFound() {
        this.positions.add(new PositionColored(new Position(1, -1, 0), BambooColor.GREEN));
        this.positions.add(new PositionColored(new Position(2, -1, -1), BambooColor.GREEN));
        this.positions.add(new PositionColored(new Position(2, -2, 0), BambooColor.GREEN));
        assertEquals(new HashSet<>(), Patterns.TRIANGLE.check(this.positions, BambooColor.GREEN, BambooColor.GREEN, BambooColor.GREEN));
    }

    @Test @DisplayName("check ne retourne pas l'étang qui se trouve dans l'angle à droite")
    public void checkNotReturnPondRight() {
        this.positions.add(new PositionColored(new Position(-1, 1, 0), BambooColor.GREEN));
        HashSet<PositionColored> expected = new HashSet<>();
        expected.add(new PositionColored(new Position(-2, 1, 1), BambooColor.GREEN));
        expected.add(new PositionColored(new Position(-1, 0, 1), BambooColor.GREEN));
        assertTrue(expected.containsAll(Patterns.TRIANGLE.check(this.positions, BambooColor.GREEN, BambooColor.GREEN, BambooColor.GREEN)));
    }

    @Test @DisplayName("check ne retourne pas l'étang qui se trouve dans l'angle en haut")
    public void checkNotReturnPondUp() {
        this.positions.add(new PositionColored(new Position(-1, 0, 1), BambooColor.GREEN));
        HashSet<PositionColored> expected = new HashSet<>();
        expected.add(new PositionColored(new Position(-2, 0, 2), BambooColor.GREEN));
        expected.add(new PositionColored(new Position(-1, -1, 2), BambooColor.GREEN));
        assertTrue(expected.containsAll(Patterns.TRIANGLE.check(this.positions, BambooColor.GREEN, BambooColor.GREEN, BambooColor.GREEN)));
    }

    @Test @DisplayName("Une position avec pas la bonne couleur")
    public void onePositionWithNotTheRightColor() {
        this.positions.add(new PositionColored(new Position(-1, 0,  1), BambooColor.YELLOW));
        HashSet<PositionColored> expected = new HashSet<>();
        expected.add(new PositionColored(new Position(1, -2, 1), BambooColor.GREEN));
        expected.add(new PositionColored(new Position(0, -1, 1), BambooColor.GREEN));
        expected.add(new PositionColored(new Position(1, -1, 0), BambooColor.GREEN));
        Set<PositionColored> check = Patterns.TRIANGLE.check(this.positions, BambooColor.GREEN, BambooColor.GREEN, BambooColor.GREEN);
        assertTrue(expected.containsAll(check));
    }

    @Test @DisplayName("la position en haut n'a pas la bonne couleur")
    public void notRightColorUpperVertex() {
        this.positions.add(new PositionColored(new Position(-1, 0, 1), BambooColor.GREEN));
        this.positions.add(new PositionColored(new Position(-1, 1, 0), BambooColor.YELLOW));
        HashSet<PositionColored> expected = new HashSet<>();
        expected.add(new PositionColored(new Position(-2, 0, 2), BambooColor.GREEN));
        expected.add(new PositionColored(new Position(-1, -1, 2), BambooColor.GREEN));
        assertEquals(Patterns.TRIANGLE.check(this.positions, BambooColor.GREEN, BambooColor.GREEN, BambooColor.GREEN), expected);
    }

    @Test @DisplayName("La position à gauche n'a pas la bonne couleur")
    public void notRightColorLeftVertex() {
        this.positions.add(new PositionColored(new Position(-1, 0, 1), BambooColor.GREEN));
        this.positions.add(new PositionColored(new Position(-2, 1, 1), BambooColor.YELLOW));
        HashSet<PositionColored> expected = new HashSet<>();
        expected.add(new PositionColored(new Position(-2, 0, 2), BambooColor.GREEN));
        expected.add(new PositionColored(new Position(-1, -1, 2), BambooColor.GREEN));
        assertTrue(expected.containsAll(Patterns.TRIANGLE.check(this.positions, BambooColor.GREEN, BambooColor.GREEN, BambooColor.GREEN)));
    }

}
