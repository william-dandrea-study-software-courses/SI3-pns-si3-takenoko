package fr.matelots.polytech.core.game.card.parcel;

import fr.matelots.polytech.core.game.Board;
import fr.matelots.polytech.core.game.goalcards.CardObjectiveParcel;
import fr.matelots.polytech.core.game.goalcards.pattern.Patterns;
import fr.matelots.polytech.core.game.parcels.BambooColor;
import fr.matelots.polytech.core.game.parcels.BambooPlantation;
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
public class CardObjectiveParcelTest {

    private Board board;
    private CardObjectiveParcel obj;

    @BeforeEach
    public void init() {
        this.board = new Board();
        this.obj = new CardObjectiveParcel(this.board, 1, Patterns.TRIANGLE);
    }

    @Test @DisplayName("test card objective success")
    public void objectiveAccomplished() {
        this.board.addParcel(0, 1, -1, new BambooPlantation(BambooColor.GREEN));
        this.board.addParcel(1, 0, -1, new BambooPlantation(BambooColor.GREEN));
        this.board.addParcel(1, 1, -2, new BambooPlantation(BambooColor.GREEN));
        assertTrue(obj.verify());
    }

    @Test @DisplayName("test card objective completed")
    public void objectiveCompleted() {
        this.board.addParcel(0, 1, -1, new BambooPlantation(BambooColor.GREEN));
        this.board.addParcel(1, 0, -1, new BambooPlantation(BambooColor.GREEN));
        this.board.addParcel(1, 1, -2, new BambooPlantation(BambooColor.GREEN));
        obj.verify();
        assertTrue(obj.isCompleted());
    }

    @Test @DisplayName("test missing parcels position to complete return null")
    public void missingParcelsNull() {
        assertNull(obj.getMissingPositionsToComplete());
    }

    @Test @DisplayName("test missing parcels position to complete return right positions")
    public void missingParcelsRightPositions() {
        this.board.addParcel(0, 1, -1, new BambooPlantation(BambooColor.GREEN));
        this.board.addParcel(1, 0, -1, new BambooPlantation(BambooColor.GREEN));
        Set<Position> positions = new HashSet<>();
        positions.add(new Position(1, 1, -2));
        obj.verify();
        assertEquals(positions, obj.getMissingPositionsToComplete());
    }

    @Test @DisplayName("test card objective fail")
    public void objectiveFail() {
        this.board.addParcel(0, 1, -1, new BambooPlantation(BambooColor.GREEN));
        this.board.addParcel(1, -1, 0, new BambooPlantation(BambooColor.GREEN));
        assertFalse(obj.verify());
    }

    @Test @DisplayName("test card objective not completed")
    public void objectiveNotCompleted() {
        this.board.addParcel(0, 1, -1, new BambooPlantation(BambooColor.GREEN));
        this.board.addParcel(1, -1, 0, new BambooPlantation(BambooColor.GREEN));
        obj.verify();
        assertFalse(obj.isCompleted());
    }

    @Test @DisplayName("test card score = 1")
    public void objectiveScoreEqualOne() {
        assertEquals(1, obj.getScore());
    }

    @Test @DisplayName("test card score = 492")
    public void objectiveScoreEqualHighNumber() {
        CardObjectiveParcel obj = new CardObjectiveParcel(this.board, 492, Patterns.TRIANGLE);
        assertEquals(492, obj.getScore());
    }

    @Test @DisplayName("test card score = 0")
    public void objectiveScoreEqualZero() {
        CardObjectiveParcel obj = new CardObjectiveParcel(this.board, 0, Patterns.TRIANGLE);
        assertEquals(1, obj.getScore());
    }

    @Test @DisplayName("test card score = -1")
    public void objectiveScoreEqualMinusOne() {
        CardObjectiveParcel obj = new CardObjectiveParcel(this.board, -1, Patterns.TRIANGLE);
        assertEquals(1, obj.getScore());
    }

    @Test @DisplayName("test card score = -492")
    public void objectiveScoreEqualLowNumber() {
        CardObjectiveParcel obj = new CardObjectiveParcel(this.board, -492, Patterns.TRIANGLE);
        assertEquals(1, obj.getScore());
    }

}
