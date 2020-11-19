package fr.matelots.polytech.core.game;

import fr.matelots.polytech.core.game.goalcards.CardObjectiveParcel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

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
        this.obj = new CardObjectiveParcel(this.board, 1);
    }

    @Test @DisplayName("test card objective success")
    public void objectiveAccomplished() {
        this.board.addParcel(0, 1, -1, new Parcel());
        this.board.addParcel(1, 0, -1, new Parcel());
        assertTrue(obj.verify());
    }

    @Test @DisplayName("test card objective completed")
    public void objectiveCompleted() {
        this.board.addParcel(0, 1, -1, new Parcel());
        this.board.addParcel(1, 0, -1, new Parcel());
        obj.verify();
        assertTrue(obj.isCompleted());
    }

    @Test @DisplayName("test card objective fail")
    public void objectiveFail() {
        this.board.addParcel(0, 1, -1, new Parcel());
        this.board.addParcel(1, -1, 0, new Parcel());
        assertFalse(obj.verify());
    }

    @Test @DisplayName("test card objective not completed")
    public void objectiveNotCompleted() {
        this.board.addParcel(0, 1, -1, new Parcel());
        this.board.addParcel(1, -1, 0, new Parcel());
        obj.verify();
        assertFalse(obj.isCompleted());
    }

    @Test @DisplayName("test card score = 1")
    public void objectiveScoreEqualOne() {
        assertEquals(1, obj.getScore());
    }

    @Test @DisplayName("test card score = 492")
    public void objectiveScoreEqualHighNumber() {
        CardObjectiveParcel obj = new CardObjectiveParcel(this.board, 492);
        assertEquals(492, obj.getScore());
    }

    @Test @DisplayName("test card score = 0")
    public void objectiveScoreEqualZero() {
        CardObjectiveParcel obj = new CardObjectiveParcel(this.board, 0);
        assertEquals(1, obj.getScore());
    }

    @Test @DisplayName("test card score = -1")
    public void objectiveScoreEqualMinusOne() {
        CardObjectiveParcel obj = new CardObjectiveParcel(this.board, -1);
        assertEquals(1, obj.getScore());
    }

    @Test @DisplayName("test card score = -492")
    public void objectiveScoreEqualLowNumber() {
        CardObjectiveParcel obj = new CardObjectiveParcel(this.board, -492);
        assertEquals(1, obj.getScore());
    }

}
