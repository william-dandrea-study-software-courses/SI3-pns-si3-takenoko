package fr.matelots.polytech.core.game;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class BoardTest {
    private Board board;

    @BeforeEach
    public void init () {
        board = new Board();
    }

    @Test
    public void testEtangEnPlaceACreationDuPlateau () {
        assertNotNull(board.getParcel(0, 0, 0));
    }

    @Test
    public void testValiditePositionPourVoisineALEtang () {
        assertTrue(board.isPlaceValid(1, -1, 0));
        assertTrue(board.isPlaceValid(0, -1, +1));
        assertTrue(board.isPlaceValid(-1, 0, 1));
        assertTrue(board.isPlaceValid(-1, 1, 0));
        assertTrue(board.isPlaceValid(0, 1, -1));
        assertTrue(board.isPlaceValid(+1, 0, -1));
    }

    @Test
    public void testInvalidPositionCarPAsVoisine () {
        assertFalse(board.isPlaceValid(2, -1, -1));
    }
}
