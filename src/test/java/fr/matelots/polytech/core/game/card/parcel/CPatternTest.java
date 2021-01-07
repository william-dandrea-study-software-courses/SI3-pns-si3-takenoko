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
 * @author Alexandre Arcil
 */
public class CPatternTest {

    private Set<PositionColored> positions;

    @BeforeEach
    public void init() {
        this.positions = new HashSet<>();
        this.positions.add(new PositionColored(Config.POND_POSITION, null));
    }

    /*@Test Plus un problème
    @DisplayName("Aucune position")
    public void emptyPosition() {
        assertThrows(IllegalArgumentException.class, () -> Patterns.C.check(new HashSet<>()));
    }

    @Test @DisplayName("Seulement l'étang")
    public void onlyPond() {
        assertThrows(IllegalArgumentException.class, () -> Patterns.C.check(this.positions));
    }*/

    @Test @DisplayName("Nombre de couleurs inférieur au nombre de parcelles du motif")
    public void colorNumberDifferenceOffsetNumberLess() {
        assertThrows(IllegalArgumentException.class, () -> Patterns.C.check(this.positions, BambooColor.GREEN, BambooColor.GREEN));
    }

    @Test @DisplayName("Nombre de couleurs supérieur au nombre de parcelles du motif")
    public void colorNumberDifferenceOffsetNumberMore() {
        assertThrows(IllegalArgumentException.class, () -> Patterns.C.check(this.positions, BambooColor.GREEN, BambooColor.GREEN, BambooColor.GREEN, BambooColor.GREEN));
    }

    @Test @DisplayName("Seulement l'étang")
    public void onlyPond() {
        HashSet<PositionColored> expected = new HashSet<>();
        expected.add(new PositionColored(new Position(-1, 0, 1), BambooColor.GREEN));
        expected.add(new PositionColored(new Position(-1, 1, 0), BambooColor.GREEN));
        expected.add(new PositionColored(new Position(0, 1, -1), BambooColor.GREEN));
        Set<PositionColored> check = Patterns.C.check(this.positions, BambooColor.GREEN, BambooColor.GREEN, BambooColor.GREEN);
        assertTrue(expected.containsAll(check));
    }

    @Test @DisplayName("Une position")
    public void onePosition() {
        this.positions.add(new PositionColored(new Position(1, -1, 0), BambooColor.GREEN));
        HashSet<PositionColored> expected = new HashSet<>();
        expected.add(new PositionColored(new Position(0, -1, 1), BambooColor.GREEN));
        expected.add(new PositionColored(new Position(0, -2, 2), BambooColor.GREEN));
        Set<PositionColored> check = Patterns.C.check(this.positions, BambooColor.GREEN, BambooColor.GREEN, BambooColor.GREEN);
        assertTrue(expected.containsAll(check));
    }

    @Test @DisplayName("Il manque la position du haut")
    public void missingUpperVertex() {
        this.positions.add(new PositionColored(new Position(1, -1, 0), BambooColor.GREEN));
        this.positions.add(new PositionColored(new Position(1, 0, -1), BambooColor.GREEN));
        HashSet<PositionColored> expected = new HashSet<>();
        expected.add(new PositionColored(new Position(2, 0, -2), BambooColor.GREEN));
        assertTrue(expected.containsAll(Patterns.C.check(this.positions, BambooColor.GREEN, BambooColor.GREEN, BambooColor.GREEN)));
    }

    @Test @DisplayName("Il manque la position gauche")
    public void missingLeftVertex() {
        this.positions.add(new PositionColored(new Position(1, -1, 0), BambooColor.GREEN));
        this.positions.add(new PositionColored(new Position(2, 0, -2), BambooColor.GREEN));
        HashSet<PositionColored> expected = new HashSet<>();
        expected.add(new PositionColored(new Position(1, 0, -1), BambooColor.GREEN));
        assertTrue(expected.containsAll(Patterns.C.check(this.positions, BambooColor.GREEN, BambooColor.GREEN, BambooColor.GREEN)));
    }

    @Test @DisplayName("motif trouvé")
    public void patternFound() {
        this.positions.add(new PositionColored(new Position(1, -1, 0), BambooColor.GREEN));
        this.positions.add(new PositionColored(new Position(1, 0, -1), BambooColor.GREEN));
        this.positions.add(new PositionColored(new Position(2, 0, -2), BambooColor.GREEN));
        assertEquals(new HashSet<>(), Patterns.C.check(this.positions, BambooColor.GREEN, BambooColor.GREEN, BambooColor.GREEN));
    }

    @Test @DisplayName("check ne retourne pas l'étang qui se trouve au centre")
    public void checkNotReturnPondCenter() {
        this.positions.add(new PositionColored(new Position(0, -1, 1), BambooColor.GREEN));
        HashSet<PositionColored> expected = new HashSet<>();
        expected.add(new PositionColored(new Position(0, -2, 2), BambooColor.GREEN));
        expected.add(new PositionColored(new Position(1, -1, 0), BambooColor.GREEN));
        assertTrue(expected.containsAll(Patterns.C.check(this.positions, BambooColor.GREEN, BambooColor.GREEN, BambooColor.GREEN)));
    }

    @Test @DisplayName("check ne retourne pas l'étang qui se trouve en haut")
    public void checkNotReturnPondUp() {
        this.positions.add(new PositionColored(new Position(-1, -1, 2), BambooColor.GREEN));
        HashSet<PositionColored> expected = new HashSet<>();
        expected.add(new PositionColored(new Position(-1, -2, 3), BambooColor.GREEN));
        expected.add(new PositionColored(new Position(0, -1, 1), BambooColor.GREEN));
        assertTrue(expected.containsAll(Patterns.C.check(this.positions, BambooColor.GREEN, BambooColor.GREEN, BambooColor.GREEN)));
    }

    @Test @DisplayName("Une position avec pas la bonne couleur")
    public void onePositionWithNotTheRightColor() {
        this.positions.add(new PositionColored(new Position(-1, 0,  1), BambooColor.YELLOW));
        HashSet<PositionColored> expected = new HashSet<>();
        expected.add(new PositionColored(new Position(0, -2, 2), BambooColor.GREEN));
        expected.add(new PositionColored(new Position(0, -1, 1), BambooColor.GREEN));
        expected.add(new PositionColored(new Position(1, -1, 0), BambooColor.GREEN));
        Set<PositionColored> check = Patterns.C.check(this.positions, BambooColor.GREEN, BambooColor.GREEN, BambooColor.GREEN);
        assertTrue(expected.containsAll(check));
    }

    @Test @DisplayName("la position à gauche n'a pas la bonne couleur")
    public void notRightColorUpperVertex() {
        this.positions.add(new PositionColored(new Position(-1, 0, 1), BambooColor.GREEN));
        this.positions.add(new PositionColored(new Position(-1, 1, 0), BambooColor.YELLOW));
        HashSet<PositionColored> expected = new HashSet<>();
        expected.add(new PositionColored(new Position(-2, 0, 2), BambooColor.GREEN));
        expected.add(new PositionColored(new Position(-2, -1, 3), BambooColor.GREEN));
        assertEquals(Patterns.C.check(this.positions, BambooColor.GREEN, BambooColor.GREEN, BambooColor.GREEN), expected);
    }

    @Test @DisplayName("La position gauche n'a pas la bonne couleur")
    public void notRightColorLeftVertex() {
        this.positions.add(new PositionColored(new Position(-1, 0, 1), BambooColor.GREEN));
        this.positions.add(new PositionColored(new Position(0, 1, -1), BambooColor.YELLOW));
        HashSet<PositionColored> expected = new HashSet<>();
        expected.add(new PositionColored(new Position(-1, 0, 1), BambooColor.GREEN));
        expected.add(new PositionColored(new Position(-2, 0, 2), BambooColor.GREEN));
        expected.add(new PositionColored(new Position(-2, -1, 3), BambooColor.GREEN));
        assertTrue(expected.containsAll(Patterns.C.check(this.positions, BambooColor.GREEN, BambooColor.GREEN, BambooColor.GREEN)));
    }

}
