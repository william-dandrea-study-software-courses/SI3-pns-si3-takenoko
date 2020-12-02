package fr.matelots.polytech.core.game.movables;

import fr.matelots.polytech.core.game.Board;
import fr.matelots.polytech.core.game.Config;
import fr.matelots.polytech.core.game.parcels.BambooColor;
import fr.matelots.polytech.core.game.parcels.BambooPlantation;
import fr.matelots.polytech.core.game.parcels.Parcel;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Gabriel Cogne
 */
public class PandaTest {
    private Board board;
    private Panda panda;

    @BeforeEach
    public void init () {
        board = new Board();
        panda = board.getPanda();
    }

    @Test
    public void testInitPlacement () {
        assertNotNull(board.getParcel(Config.BOND_POSITION).getPanda());
    }

    @Test
    public void testMoveTo () {
        Parcel p1 = new BambooPlantation(BambooColor.GREEN);
        Parcel p2 = new BambooPlantation(BambooColor.GREEN);

        board.addParcel(1, -1, 0, p1);
        board.addParcel(-1, 1, 0, p2);

        assertTrue(panda.moveTo(1, -1, 0));
        assertNotNull(p1.getPanda());
        assertTrue(panda.moveTo(-1, 1, 0));
        assertNotNull(p2.getPanda());
    }

    @Test
    public void testActionWithConstraints () {
        Parcel p1 = new BambooPlantation(BambooColor.GREEN);
        p1.growBamboo();p1.growBamboo();
        Parcel p2 = new BambooPlantation(BambooColor.PINK);
        p2.growBamboo();p2.growBamboo();
        Parcel p3 = new BambooPlantation(BambooColor.PINK);
        p3.growBamboo();p3.growBamboo();

        board.addParcel(1, -1, 0, p1);
        board.addParcel(1, 0, -1, p2);
        board.addParcel(-1, 1, 0, p3);

        assertTrue(panda.moveTo(1, -1, 0));

        assertEquals(1, p1.getBambooSize());
        assertEquals(2, p2.getBambooSize());
        assertEquals(2, p3.getBambooSize());
        assertEquals(0, board.getParcel(Config.BOND_POSITION).getBambooSize());
    }

    @Test
    public void testIsGoneAfterMovingSomewhereElse () {
        Parcel p1 = new BambooPlantation(BambooColor.GREEN);
        Parcel p2 = new BambooPlantation(BambooColor.PINK);
        Parcel p3 = new BambooPlantation(BambooColor.GREEN);

        board.addParcel(1, -1, 0, p1);
        board.addParcel(1, 0, -1, p2);
        board.addParcel(-1, 1, 0, p3);

        assertTrue(panda.moveTo(1, -1, 0));

        assertTrue(panda.moveTo(-1, 1, 0));

        assertNotNull(p3.getPanda());
        assertNull(p1.getPanda());
    }
}
