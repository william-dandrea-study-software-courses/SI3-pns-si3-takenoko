package fr.matelots.polytech.core.game.parcels;

import fr.matelots.polytech.engine.util.Position;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Alexandre Arcil
 */
public class SideTest {

    @Test @DisplayName("Le side touché vaut UPPER_RIGHT")
    public void touchedSideUpperRightSide() {
        Position pos = new Position(1, 0, -1);
        Position offset = pos.add(Side.UPPER_RIGHT.getDirection());
        assertEquals(Side.UPPER_RIGHT, Side.getTouchedSide(pos, offset));
    }

    @Test @DisplayName("Le side touché vaut RIGHT")
    public void touchedSideRightSide() {
        Position pos = new Position(1, 0, -1);
        Position offset = pos.add(Side.RIGHT.getDirection());
        assertEquals(Side.RIGHT, Side.getTouchedSide(pos, offset));
    }

    @Test @DisplayName("Le side touché vaut BOTTOM_RIGHT")
    public void touchedSideBottomRightSide() {
        Position pos = new Position(1, 0, -1);
        Position offset = pos.add(Side.BOTTOM_RIGHT.getDirection());
        assertEquals(Side.BOTTOM_RIGHT, Side.getTouchedSide(pos, offset));
    }

    @Test @DisplayName("Le side touché vaut BOTTOM_LEFT")
    public void touchedSideBottomLeftSide() {
        Position pos = new Position(1, 0, -1);
        Position offset = pos.add(Side.BOTTOM_LEFT.getDirection());
        assertEquals(Side.BOTTOM_LEFT, Side.getTouchedSide(pos, offset));
    }

    @Test @DisplayName("Le side touché vaut LEFT")
    public void touchedSideLeftSide() {
        Position pos = new Position(1, 0, -1);
        Position offset = pos.add(Side.LEFT.getDirection());
        assertEquals(Side.LEFT, Side.getTouchedSide(pos, offset));
    }

    @Test @DisplayName("Le side touché vaut UPPER_LEFT")
    public void touchedSideUpperLeftSide() {
        Position pos = new Position(1, 0, -1);
        Position offset = pos.add(Side.UPPER_LEFT.getDirection());
        assertEquals(Side.UPPER_LEFT, Side.getTouchedSide(pos, offset));
    }

    @Test @DisplayName("Le side touché vaut null")
    public void notTouchedSide() {
        Position pos = new Position(1, 0, -1);
        Position pos2 = new Position(-1, 0, 1);
        assertNull(Side.getTouchedSide(pos, pos2));
    }

}
