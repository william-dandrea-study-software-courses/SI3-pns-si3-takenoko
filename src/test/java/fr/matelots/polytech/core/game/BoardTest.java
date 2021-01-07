package fr.matelots.polytech.core.game;

import fr.matelots.polytech.core.NoParcelLeftToPlaceException;
import fr.matelots.polytech.core.game.parcels.BambooColor;
import fr.matelots.polytech.core.game.parcels.BambooPlantation;
import fr.matelots.polytech.core.game.parcels.Side;
import fr.matelots.polytech.engine.util.Position;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

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
    public void testGetValidPlace_TestValid() {
        var validPlaces = board.getValidPlaces();

        for(var validPlace : validPlaces) {
            assertFalse(board.containTile(validPlace));
            assertTrue(board.isPlaceValid(validPlace));
        }
    }

    @Test
    public void testParcelProcheEtangIrrigue() {
        BambooPlantation bambooPlantation = new BambooPlantation(BambooColor.GREEN);
        board.addParcel(1, -1, 0, bambooPlantation);
        assertTrue(bambooPlantation.isIrrigate());
    }

    @Test
    public void testParcelProcheEtangIrrigueCoteHautDroit() {
        BambooPlantation bambooPlantation = new BambooPlantation(BambooColor.GREEN);
        board.addParcel(-1, 0, 1, bambooPlantation);
        assertTrue(bambooPlantation.isIrrigate(Side.UPPER_RIGHT));
    }

    @Test
    public void testParcelProcheEtangIrrigueCoteDroit() {
        BambooPlantation bambooPlantation = new BambooPlantation(BambooColor.GREEN);
        board.addParcel(-1, 1, 0, bambooPlantation);
        assertTrue(bambooPlantation.isIrrigate(Side.RIGHT));
    }

    @Test
    public void testParcelProcheEtangIrrigueCoteBasDroit() {
        BambooPlantation bambooPlantation = new BambooPlantation(BambooColor.GREEN);
        board.addParcel(0, 1, -1,  bambooPlantation);
        assertTrue(bambooPlantation.isIrrigate(Side.BOTTOM_RIGHT));
    }

    @Test
    public void testParcelProcheEtangIrrigueCoteBasGauche() {
        BambooPlantation bambooPlantation = new BambooPlantation(BambooColor.GREEN);
        board.addParcel(1, 0, -1, bambooPlantation);
        assertTrue(bambooPlantation.isIrrigate(Side.BOTTOM_LEFT));
    }

    @Test
    public void testParcelProcheEtangIrrigueCoteGauche() {
        BambooPlantation bambooPlantation = new BambooPlantation(BambooColor.GREEN);
        board.addParcel(1, -1, 0, bambooPlantation);
        assertTrue(bambooPlantation.isIrrigate(Side.LEFT));
    }

    @Test
    public void testParcelProcheEtangIrrigueCoteHautGauche() {
        BambooPlantation bambooPlantation = new BambooPlantation(BambooColor.GREEN);
        board.addParcel(0, -1, 1, bambooPlantation);
        assertTrue(bambooPlantation.isIrrigate(Side.UPPER_LEFT));
    }

    @Test
    public void testPlacementIrrigationPositionFausse() {
        assertFalse(board.placeIrrigation(new Position(1, 0, -1), Side.UPPER_RIGHT));
    }

    @Test
    public void testPlacementIrrigationSecondeParcelExistePas() {
        BambooPlantation bambooPlantation = new BambooPlantation(BambooColor.GREEN);
        board.addParcel(0, 1, -1, bambooPlantation);
        assertFalse(board.placeIrrigation(new Position(0, 1, -1), Side.UPPER_RIGHT));
    }

    @Test
    public void testPlacementIrrigationSansCoteDejaIrrigue() {
        BambooPlantation bambooPlantation = new BambooPlantation(BambooColor.GREEN);
        BambooPlantation bambooPlantation2 = new BambooPlantation(BambooColor.GREEN);
        board.addParcel(0, 1, -1, bambooPlantation);
        board.addParcel(0, 2, -2, bambooPlantation2);
        assertFalse(board.placeIrrigation(new Position(0, 1, -1), Side.UPPER_LEFT));
    }

    @Test
    public void testPlacementIrrigation() {
        BambooPlantation bambooPlantation = new BambooPlantation(BambooColor.GREEN);
        BambooPlantation bambooPlantation2 = new BambooPlantation(BambooColor.GREEN);
        board.addParcel(0, 1, -1, bambooPlantation);
        board.addParcel(1, 0, -1, bambooPlantation2);
        assertTrue(board.placeIrrigation(new Position(0, 1, -1), Side.RIGHT));
    }

    @Test
    public void testPlacementIrrigationParcelIrrigue() {
        BambooPlantation bambooPlantation = new BambooPlantation(BambooColor.GREEN);
        BambooPlantation bambooPlantation2 = new BambooPlantation(BambooColor.GREEN);
        board.addParcel(0, 1, -1, bambooPlantation);
        board.addParcel(1, 0, -1, bambooPlantation2);
        board.placeIrrigation(new Position(0, 1, -1), Side.RIGHT);
        assertTrue(bambooPlantation.isIrrigate(Side.RIGHT));
        assertTrue(bambooPlantation2.isIrrigate(Side.LEFT));
    }

    @Test
    public void testPlacementIrrigationParcelDejaIrrigue() {
        BambooPlantation bambooPlantation = new BambooPlantation(BambooColor.GREEN);
        BambooPlantation bambooPlantation2 = new BambooPlantation(BambooColor.GREEN);
        board.addParcel(0, 1, -1, bambooPlantation);
        board.addParcel(1, 0, -1, bambooPlantation2);
        board.placeIrrigation(new Position(0, 1, -1), Side.RIGHT);
        assertFalse(board.placeIrrigation(new Position(0, 1, -1), Side.RIGHT));
    }

    @Test
    public void testIrrigateFromTheBorderOfTheMap() {
        BambooPlantation border = new BambooPlantation(BambooColor.GREEN);
        board.addParcel(0, 1, -1, border);

        assertTrue(board.placeIrrigation(new Position(0, 1, -1), Side.RIGHT));
        assertTrue(border.isIrrigate(Side.RIGHT));

        BambooPlantation afterPlaceIrrigation = new BambooPlantation(BambooColor.GREEN);
        board.addParcel(1, 0, -1, afterPlaceIrrigation);
        assertTrue(afterPlaceIrrigation.isIrrigate(Side.BOTTOM_LEFT));
    }

    @Test
    public void testIrrigateFromEmptyPositionButNotEmptyNeighbour() {
        BambooPlantation border = new BambooPlantation(BambooColor.GREEN);
        board.addParcel(0, 1, -1, border);

        assertTrue(board.placeIrrigation(new Position(1, 0, -1), Side.LEFT));
        assertTrue(border.isIrrigate(Side.RIGHT));

        BambooPlantation afterPlaceIrrigation = new BambooPlantation(BambooColor.GREEN);
        board.addParcel(1, 0, -1, afterPlaceIrrigation);
        assertTrue(afterPlaceIrrigation.isIrrigate(Side.BOTTOM_LEFT));
    }

    @Test
    public void testLimitOfVersionOnPinkParcel() {
        for(int i = 0; i < Config.NB_MAX_PINK_PARCELS; i++) {
            var validPlace = board.getValidPlaces().stream().findAny();
            validPlace.ifPresent(position -> board.addParcel(position, new BambooPlantation(BambooColor.PINK)));
        }
        var finalValidPlace = board.getValidPlaces().stream().findAny();
        assertThrows(NoParcelLeftToPlaceException.class, () ->
                finalValidPlace.ifPresent(position -> board.addParcel(position, new BambooPlantation(BambooColor.PINK))));
    }

    @Test
    public void testLimitOfVersionOnYellowParcel() {
        for(int i = 0; i < Config.NB_MAX_YELLOW_PARCELS; i++) {
            var validPlace = board.getValidPlaces().stream().findAny();
            validPlace.ifPresent(position -> board.addParcel(position, new BambooPlantation(BambooColor.YELLOW)));
        }
        var finalValidPlace = board.getValidPlaces().stream().findAny();
        assertThrows(NoParcelLeftToPlaceException.class, () ->
                finalValidPlace.ifPresent(position -> board.addParcel(position, new BambooPlantation(BambooColor.YELLOW))));
    }

    @Test
    public void testLimitOfVersionOnGreenParcel() {
        for(int i = 0; i < Config.NB_MAX_GREEN_PARCELS; i++) {
            var validPlace = board.getValidPlaces().stream().findAny();
            validPlace.ifPresent(position -> board.addParcel(position, new BambooPlantation(BambooColor.GREEN)));
        }
        var finalValidPlace = board.getValidPlaces().stream().findAny();
        assertThrows(NoParcelLeftToPlaceException.class, () ->
                finalValidPlace.ifPresent(position -> board.addParcel(position, new BambooPlantation(BambooColor.GREEN))));
    }

    @Test
    public void testLimitOfIrrigations() {
        for(int i = 0; i < Config.NB_IRRIGATION; i++) {
            assertTrue(board.canPickIrrigation());
            assertEquals(board.getIrrigationLeft(), Config.NB_IRRIGATION - i);
            assertTrue(board.pickIrrigation());
        }
        assertEquals(board.getIrrigationLeft(), 0);
        assertFalse(board.canPickIrrigation());
        assertFalse(board.pickIrrigation());
    }
}
