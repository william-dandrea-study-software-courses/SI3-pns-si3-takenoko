package fr.matelots.polytech.core.game.movables;

import fr.matelots.polytech.core.game.Board;
import fr.matelots.polytech.core.game.Config;
import fr.matelots.polytech.core.game.parcels.BambooColor;
import fr.matelots.polytech.core.game.parcels.BambooPlantation;
import fr.matelots.polytech.core.game.parcels.Parcel;
import fr.matelots.polytech.core.game.parcels.Side;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Gabriel Cogne
 */
public class GardenerTest {
    private Board board;
    private Gardener gardener;

    @BeforeEach
    public void init () {
        board = new Board();
        gardener = board.getGardener();
    }

    @Test
    public void testInitialisation () {
        assertNotNull(board.getParcel(Config.POND_POSITION).getGardener());
    }

    @Test
    public void testMoveTo () {
        Parcel p1 = new BambooPlantation(BambooColor.GREEN);
        p1.setIrrigate(Side.BOTTOM_LEFT);
        Parcel p2 = new BambooPlantation(BambooColor.GREEN);
        p2.setIrrigate(Side.BOTTOM_LEFT);

        board.addParcel(1, -1, 0, p1);
        board.addParcel(-1, 1, 0, p2);

        assertTrue(gardener.moveTo(1, -1, 0));
        assertNotNull(p1.getGardener());
        assertTrue(gardener.moveTo(-1, 1, 0));
        assertNotNull(p2.getGardener());
    }

    @Test
    public void testActionWithoutConstraints() {
        Parcel p1 = new BambooPlantation(BambooColor.GREEN);
        p1.setIrrigate(Side.LEFT);
        Parcel p2 = new BambooPlantation(BambooColor.GREEN);
        p2.setIrrigate(Side.LEFT);
        Parcel p3 = new BambooPlantation(BambooColor.GREEN);
        p3.setIrrigate(Side.LEFT);

        board.addParcel(1, -1, 0, p1);
        board.addParcel(1, 0, -1, p2);
        board.addParcel(-1, 1, 0, p3);

        assertTrue(gardener.moveTo(1, -1, 0));

        assertEquals(2, p1.getBambooSize());
        assertEquals(2, p2.getBambooSize());
        assertEquals(1, p3.getBambooSize());
        assertEquals(0, board.getParcel(Config.POND_POSITION).getBambooSize());
    }

    @Test
    public void testActionWithConstraints () {
        Parcel p1 = new BambooPlantation(BambooColor.GREEN);
        p1.setIrrigate(Side.LEFT);
        Parcel p2 = new BambooPlantation(BambooColor.PINK);
        Parcel p3 = new BambooPlantation(BambooColor.GREEN);

        board.addParcel(1, -1, 0, p1);
        board.addParcel(1, 0, -1, p2);
        board.addParcel(-1, 1, 0, p3);

        assertTrue(gardener.moveTo(1, -1, 0));

        assertEquals(2, p1.getBambooSize());
        assertEquals(1, p2.getBambooSize());
        assertEquals(1, p3.getBambooSize());
        assertEquals(0, board.getParcel(Config.POND_POSITION).getBambooSize());
    }

    @Test
    public void testIsGoneAfterMovingSomewhereElse () {
        Parcel p1 = new BambooPlantation(BambooColor.GREEN);
        p1.setIrrigate(Side.LEFT);
        Parcel p2 = new BambooPlantation(BambooColor.PINK);
        p2.setIrrigate(Side.LEFT);
        Parcel p3 = new BambooPlantation(BambooColor.GREEN);
        p3.setIrrigate(Side.LEFT);

        board.addParcel(1, -1, 0, p1);
        board.addParcel(1, 0, -1, p2);
        board.addParcel(-1, 1, 0, p3);

        assertTrue(gardener.moveTo(1, -1, 0));

        assertTrue(gardener.moveTo(-1, 1, 0));

        assertNotNull(p3.getGardener());
        assertNull(p1.getGardener());
    }
}
