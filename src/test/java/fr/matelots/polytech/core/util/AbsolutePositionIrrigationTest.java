package fr.matelots.polytech.core.util;

import fr.matelots.polytech.core.game.Board;
import fr.matelots.polytech.core.game.Game;
import fr.matelots.polytech.core.game.parcels.BambooColor;
import fr.matelots.polytech.core.game.parcels.BambooPlantation;
import fr.matelots.polytech.core.game.parcels.Side;
import fr.matelots.polytech.engine.util.AbsolutePositionIrrigation;
import fr.matelots.polytech.engine.util.Position;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class AbsolutePositionIrrigationTest {

    Game game;
    Board board;
    BambooPlantation firstTile;

    @BeforeEach
    void init() {
        game = new Game();
        board = game.getBoard();
        firstTile = new BambooPlantation(BambooColor.GREEN);
        board.addParcel(new Position(1, 0, -1), firstTile);
    }

    @Test
    void isIrrigate() {
        AbsolutePositionIrrigation api = new AbsolutePositionIrrigation(new Position(1, 0, -1), Side.BOTTOM_LEFT, board);
        assertTrue(api.isIrrigate());
    }

    @Test
    void isIrrigateBorder() {
        AbsolutePositionIrrigation api = new AbsolutePositionIrrigation(new Position(1, 0, -1), Side.LEFT, board);
        assertFalse(api.isIrrigate());
        board.placeIrrigation(new Position(1, 0, -1), Side.LEFT);
        assertFalse(api.isIrrigate());
    }

    @Test
    void isIrrigatePositionTargetVoidButNeighbourNotVoid() {
        AbsolutePositionIrrigation api = new AbsolutePositionIrrigation(new Position(0, 1, -1), Side.RIGHT, board);
        assertFalse(api.isIrrigate());
        board.placeIrrigation(new Position(1, 0, -1), Side.LEFT);
        assertFalse(api.isIrrigate());
    }

    @Test
    void isIrrigatePositionTargetVoidAndNeighbourVoid() {
        AbsolutePositionIrrigation api = new AbsolutePositionIrrigation(new Position(0, 4, -1), Side.RIGHT, board);
        assertFalse(api.isIrrigate());
        board.placeIrrigation(new Position(1, 0, -1), Side.LEFT);
        assertFalse(api.isIrrigate());
    }

    @Test
    void testEquals() {
        AbsolutePositionIrrigation api1 = new AbsolutePositionIrrigation(new Position(0, 1, -1), Side.RIGHT, board);
        AbsolutePositionIrrigation api2 = new AbsolutePositionIrrigation(new Position(0, 1, -1), Side.RIGHT, board);
        AbsolutePositionIrrigation api3 = new AbsolutePositionIrrigation(new Position(1, 0, -1), Side.LEFT, board);
        AbsolutePositionIrrigation api5Different = new AbsolutePositionIrrigation(new Position(1, 0, -1), Side.RIGHT, board);
        AbsolutePositionIrrigation api4Different = new AbsolutePositionIrrigation(new Position(5, 0, -1), Side.LEFT, board);

        assertEquals(api1, api2);
        assertEquals(api1, api3);
        assertNotEquals(api1, api4Different);
        assertNotEquals(api1, api5Different);
    }
}
