package fr.matelots.polytech.core.game;

import fr.matelots.polytech.core.NoParcelLeftToPlaceException;
import fr.matelots.polytech.core.game.parcels.BambooColor;
import fr.matelots.polytech.core.game.parcels.BambooPlantation;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Gabriel Cogne
 */
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
    public void testValidePositionPour2VoisinesSiNonVoisineALEtang () {
        board.addParcel(1, -1, 0, new BambooPlantation(BambooColor.GREEN));
        board.addParcel(0, -1, 1, new BambooPlantation(BambooColor.GREEN));
        assertTrue(board.isPlaceValid(1, -2, 1));
    }

    @Test
    public void testInvalidePositionPour1VoisineSiNonVoisineALEtang () {
        board.addParcel(1, -1, 0, new BambooPlantation(BambooColor.GREEN));
        assertFalse(board.isPlaceValid(1, -2, 1));
    }

    @Test
    public void testInvalidPositionCarPasVoisine () {
        assertFalse(board.isPlaceValid(2, -1, -1));
    }

    @Test
    public void testPoseParcelVoisineALEtang () {
        assertTrue(board.addParcel(1, -1, 0, new BambooPlantation(BambooColor.GREEN)));
        assertNotNull(board.getParcel(1, -1, 0));
    }

    @Test
    public void testPoseParcelVoisineARien () {
        assertFalse(board.addParcel(2, -1, -1, new BambooPlantation(BambooColor.GREEN)));
        assertNull(board.getParcel(2, -1, -1));
    }

    @Test
    public void testPoseParcelVoisineAVoisineEtang () {
        assertTrue(board.addParcel(1, -1, 0, new BambooPlantation(BambooColor.GREEN)));
        assertTrue(board.addParcel(1, 0, -1, new BambooPlantation(BambooColor.GREEN)));
        assertTrue(board.addParcel(2, -1, -1, new BambooPlantation(BambooColor.GREEN)));
        assertNotNull(board.getParcel(2, -1, -1));
    }

    @Test
    public void testAlreadyUsedLocationByParcel () {
        assertTrue(board.addParcel(1, -1, 0, new BambooPlantation(BambooColor.GREEN)));
        assertFalse(board.addParcel(1, -1, 0, new BambooPlantation(BambooColor.GREEN)));
    }

    @Test
    public void testNumberOfParcelLeftToPlaceGoingDown () {
        board.addParcel(1, -1, 0, new BambooPlantation(BambooColor.GREEN));
        assertEquals(Config.NB_PLACEABLE_PARCEL - 1, board.getParcelLeftToPlace());
    }

    @Test
    public void testNoParcelLeftToPlace () {
        board.addParcel(1, 0, -1, new BambooPlantation(BambooColor.GREEN));
        for (int i = 1; i < Config.NB_PLACEABLE_PARCEL / 2 + 1; i++) {
            board.addParcel(i, -i, 0, new BambooPlantation(BambooColor.GREEN));
            board.addParcel(i+1, -i, -1, new BambooPlantation(BambooColor.GREEN));
        }

        assertThrows(NoParcelLeftToPlaceException.class,
                () -> board.addParcel(-1, 1, 0, new BambooPlantation(BambooColor.GREEN)));
    }

    @Test
    public void testGetValidPlace_TestValid() {
        var validPlaces = board.getValidPlaces();

        for(var validPlace : validPlaces) {
            assertFalse(board.containTile(validPlace));
            assertTrue(board.isPlaceValid(validPlace));
        }
    }
}
