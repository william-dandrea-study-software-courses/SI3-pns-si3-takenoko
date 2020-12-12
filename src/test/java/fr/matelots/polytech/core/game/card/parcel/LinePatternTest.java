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
public class LinePatternTest {

    private Set<PositionColored> positions;

    @BeforeEach
    public void init() {
        this.positions = new HashSet<>();
        this.positions.add(new PositionColored(Config.POND_POSITION, null));
    }

   /* @Test
    @DisplayName("Aucune position")
    public void emptyPosition() {
        assertThrows(IllegalArgumentException.class, () -> Patterns.LINE.check(new HashSet<>()));
    }

    @Test @DisplayName("Seulement l'étang")
    public void onlyPond() {
        assertThrows(IllegalArgumentException.class, () -> Patterns.LINE.check(this.positions));
    }*/

    @Test @DisplayName("Nombre de couleurs différent du nombre d'offsets")
    public void colorNumberDifferenceOffsetNumber() {
        assertThrows(IllegalArgumentException.class, () -> Patterns.LINE.check(this.positions, BambooColor.GREEN, BambooColor.GREEN));
        assertThrows(IllegalArgumentException.class, () -> Patterns.LINE.check(this.positions, BambooColor.GREEN, BambooColor.GREEN, BambooColor.GREEN, BambooColor.GREEN));
    }

    @Test @DisplayName("Une position")
    public void onePosition() {
        this.positions.add(new PositionColored(new Position(1, -1, 0), BambooColor.GREEN));
        HashSet<PositionColored> expected = new HashSet<>();
        expected.add(new PositionColored(new Position(1, -2, 1), BambooColor.GREEN));
        expected.add(new PositionColored(new Position(1, -3, 2), BambooColor.GREEN));
        assertTrue(expected.containsAll(Patterns.LINE.check(this.positions, BambooColor.GREEN, BambooColor.GREEN, BambooColor.GREEN)));
    }

    @Test @DisplayName("Il manque le bas de la ligne")
    public void missingDown() {
        this.positions.add(new PositionColored(new Position(1, -1, 0), BambooColor.GREEN));
        this.positions.add(new PositionColored(new Position(1, -2, 1), BambooColor.GREEN));
        HashSet<PositionColored> expected = new HashSet<>();
        expected.add(new PositionColored(new Position(1, -3, 2), BambooColor.GREEN));
        assertTrue(expected.containsAll(Patterns.LINE.check(this.positions, BambooColor.GREEN, BambooColor.GREEN, BambooColor.GREEN)));
    }

    @Test @DisplayName("Il manque le centre de la ligne")
    public void missingCenter() {
        this.positions.add(new PositionColored(new Position(1, -1, 0), BambooColor.GREEN));
        this.positions.add(new PositionColored(new Position(1, -3, 2), BambooColor.GREEN));
        HashSet<PositionColored> expected = new HashSet<>();
        expected.add(new PositionColored(new Position(1, -2, 1), BambooColor.GREEN));
        assertTrue(expected.containsAll(Patterns.LINE.check(this.positions, BambooColor.GREEN, BambooColor.GREEN, BambooColor.GREEN)));
    }

    @Test @DisplayName("motif trouvé")
    public void patternFound() {
        this.positions.add(new PositionColored(new Position(1, -1, 0), BambooColor.GREEN));
        this.positions.add(new PositionColored(new Position(1, -2, 1), BambooColor.GREEN));
        this.positions.add(new PositionColored(new Position(1, -3, 2), BambooColor.GREEN));
        assertEquals(new HashSet<>(), Patterns.LINE.check(this.positions, BambooColor.GREEN, BambooColor.GREEN, BambooColor.GREEN));
    }

    @Test @DisplayName("check ne retourne pas l'étang qui se trouve au centre")
    public void checkNotReturnPondCenter() {
        this.positions.add(new PositionColored(new Position(0, 1, -1), BambooColor.GREEN));
        HashSet<PositionColored> expected = new HashSet<>();
        expected.add(new PositionColored(new Position(0, 2, -2), BambooColor.GREEN));
        expected.add(new PositionColored(new Position(0, 3, -3), BambooColor.GREEN));
        assertTrue(expected.containsAll(Patterns.LINE.check(this.positions, BambooColor.GREEN, BambooColor.GREEN, BambooColor.GREEN)));
    }

    @Test @DisplayName("check ne retourne pas l'étang qui se trouve en haut")
    public void checkNotReturnPondDown() { //checkNotReturnPondCenter le fait déjà en soit
        this.positions.add(new PositionColored(new Position(0, 2, -2), BambooColor.GREEN));
        HashSet<PositionColored> expected = new HashSet<>();
        expected.add(new PositionColored(new Position(0, 1, -1), BambooColor.GREEN));
        expected.add(new PositionColored(new Position(0, 3, -3), BambooColor.GREEN));
        assertTrue(expected.containsAll(Patterns.LINE.check(this.positions, BambooColor.GREEN, BambooColor.GREEN, BambooColor.GREEN)));
    }

    @Test @DisplayName("Une position avec pas la bonne couleur")
    public void onePositionWithNotTheRightColor() {
        this.positions.add(new PositionColored(new Position(-1, 0,  1), BambooColor.YELLOW));
        HashSet<PositionColored> expected = new HashSet<>();
        expected.add(new PositionColored(new Position(0, -1, 1), BambooColor.GREEN));
        expected.add(new PositionColored(new Position(0, -2, 2), BambooColor.GREEN));
        expected.add(new PositionColored(new Position(0, -3, 3), BambooColor.GREEN));
        Set<PositionColored> check = Patterns.LINE.check(this.positions, BambooColor.GREEN, BambooColor.GREEN, BambooColor.GREEN);
        assertEquals(check, expected);
    }

    @Test @DisplayName("la position au centre n'a pas la bonne couleur")
    public void notRightColorCenterVertex() {
        this.positions.add(new PositionColored(new Position(-1, 0, 1), BambooColor.GREEN));
        this.positions.add(new PositionColored(new Position(-1, -1, 2), BambooColor.YELLOW));
        HashSet<PositionColored> expected = new HashSet<>();
        expected.add(new PositionColored(new Position(-1, 2, -1), BambooColor.GREEN));
        expected.add(new PositionColored(new Position(-1, 1, 0), BambooColor.GREEN));
        assertEquals(Patterns.LINE.check(this.positions, BambooColor.GREEN, BambooColor.GREEN, BambooColor.GREEN), expected);
    }

    @Test @DisplayName("La position en haut n'a pas la bonne couleur")
    public void notRightColorUpVertex() {
        this.positions.add(new PositionColored(new Position(-1, 0, 1), BambooColor.GREEN));
        this.positions.add(new PositionColored(new Position(-1, -2, 3), BambooColor.YELLOW));
        HashSet<PositionColored> expected = new HashSet<>();
        expected.add(new PositionColored(new Position(-1, 1, 0), BambooColor.GREEN));
        expected.add(new PositionColored(new Position(-1, -1, 2), BambooColor.GREEN));
        Set<PositionColored> check = Patterns.LINE.check(this.positions, BambooColor.GREEN, BambooColor.GREEN, BambooColor.GREEN);
        assertEquals(check, expected);
    }

}
