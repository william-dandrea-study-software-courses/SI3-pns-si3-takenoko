package fr.matelots.polytech.core.game.card.layout;

import fr.matelots.polytech.core.game.Board;
import fr.matelots.polytech.core.game.goalcards.CardObjectivePanda;
import fr.matelots.polytech.core.game.parcels.BambooColor;
import fr.matelots.polytech.core.game.parcels.BambooPlantation;
import fr.matelots.polytech.core.game.parcels.Layout;
import fr.matelots.polytech.core.players.IndividualBoard;
import fr.matelots.polytech.engine.util.Position;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class LayoutTest {



    private Board board;

    @BeforeEach
    public void init() {
        this.board = new Board();
    }

    @Test
    public void testPlaceParcelWhenWeCant() {
        board.addParcel(0,1,-1, new BambooPlantation(BambooColor.GREEN));
        Position position = new Position(0,1,-1);

        assertFalse(board.placeLayout(position, Layout.ENCLOSURE));


    }

    @Test
    public void testPlaceParcelWhenWeCan() {
        board.addParcel(0,1,-1, new BambooPlantation(BambooColor.GREEN));
        board.addParcel(1,0,-1, new BambooPlantation(BambooColor.GREEN));
        board.addParcel(1,1,-2, new BambooPlantation(BambooColor.GREEN));
        Position position = new Position(1,1,-2);

        assertTrue(board.placeLayout(position, Layout.ENCLOSURE));
    }
}
