package fr.matelots.polytech.core.game;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Alexandre Arcil
 */
public class CardObjectiveParcelTest {

    private Board board;

    @BeforeEach
    public void init() {
        this.board = new Board();
    }

    @Test @DisplayName("test card objective success")
    public void objectiveAccomplished() {
        CardObjectiveParcel obj = new CardObjectiveParcel(this.board);
        this.board.addParcel(0, 1, -1, new Parcel());
        this.board.addParcel(1, 0, -1, new Parcel());
        assertTrue(obj.verify());
    }

    @Test @DisplayName("test card objective completed")
    public void objectiveCompleted() {
        CardObjectiveParcel obj = new CardObjectiveParcel(this.board);
        this.board.addParcel(0, 1, -1, new Parcel());
        this.board.addParcel(1, 0, -1, new Parcel());
        obj.verify();
        assertTrue(obj.isCompleted());
    }

    @Test @DisplayName("test card objective fail")
    public void objectiveFail() {
        CardObjectiveParcel obj = new CardObjectiveParcel(this.board);
        this.board.addParcel(0, 1, -1, new Parcel());
        this.board.addParcel(1, -1, 0, new Parcel());
        assertFalse(obj.verify());
    }

    @Test @DisplayName("test card objective not completed")
    public void objectiveNotCompleted() {
        CardObjectiveParcel obj = new CardObjectiveParcel(this.board);
        this.board.addParcel(0, 1, -1, new Parcel());
        this.board.addParcel(1, -1, 0, new Parcel());
        obj.verify();
        assertFalse(obj.isCompleted());
    }

}
