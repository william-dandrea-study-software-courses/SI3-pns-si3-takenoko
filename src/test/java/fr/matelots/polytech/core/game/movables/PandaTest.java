package fr.matelots.polytech.core.game.movables;

import fr.matelots.polytech.core.game.Board;
import fr.matelots.polytech.core.game.Config;
import fr.matelots.polytech.core.game.Game;
import fr.matelots.polytech.core.game.parcels.BambooColor;
import fr.matelots.polytech.core.game.parcels.BambooPlantation;
import fr.matelots.polytech.core.game.parcels.Parcel;
import fr.matelots.polytech.core.game.parcels.Side;
import fr.matelots.polytech.core.players.Bot;
import fr.matelots.polytech.core.players.bots.PremierBot;
import fr.matelots.polytech.core.players.bots.QuintusBot;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Gabriel Cogne
 */
public class PandaTest {
    private Board board;
    private Panda panda;
    private Bot bot;

    @BeforeEach
    public void init () {
        board = new Board();
        panda = board.getPanda();
        bot = new QuintusBot(new Game());
    }

    @Test
    public void testInitPlacement () {
        assertNotNull(board.getParcel(Config.POND_POSITION).getPanda());
    }

    @Test
    public void testMoveTo () {
        Parcel p1 = new BambooPlantation(BambooColor.GREEN);
        p1.setIrrigate(Side.LEFT);
        Parcel p2 = new BambooPlantation(BambooColor.GREEN);
        p2.setIrrigate(Side.LEFT);

        board.addParcel(1, -1, 0, p1);
        board.addParcel(-1, 1, 0, p2);

        panda.setCurrentPlayer(bot);
        assertTrue(panda.moveTo(1, -1, 0));
        assertNotNull(p1.getPanda());
        panda.setCurrentPlayer(bot);
        assertTrue(panda.moveTo(-1, 1, 0));
        assertNotNull(p2.getPanda());
    }

    @Test
    public void testActionWithConstraints () {
        Parcel p1 = new BambooPlantation(BambooColor.GREEN);
        p1.setIrrigate(Side.LEFT);
        p1.growBamboo();p1.growBamboo();
        Parcel p2 = new BambooPlantation(BambooColor.PINK);
        p2.setIrrigate(Side.LEFT);
        p2.growBamboo();p2.growBamboo();
        Parcel p3 = new BambooPlantation(BambooColor.PINK);
        p3.setIrrigate(Side.LEFT);
        p3.growBamboo();p3.growBamboo();

        board.addParcel(1, -1, 0, p1);
        board.addParcel(1, 0, -1, p2);
        board.addParcel(-1, 1, 0, p3);

        panda.setCurrentPlayer(bot);
        assertTrue(panda.moveTo(1, -1, 0));

        assertEquals(2, p1.getBambooSize());
        assertEquals(3, p2.getBambooSize());
        assertEquals(3, p3.getBambooSize());
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

        panda.setCurrentPlayer(bot);
        assertTrue(panda.moveTo(1, -1, 0));

        panda.setCurrentPlayer(bot);
        assertTrue(panda.moveTo(-1, 1, 0));

        assertNotNull(p3.getPanda());
        assertNull(p1.getPanda());
    }

    @Test
    public void testEatenBambooGoOnCurrentPlayerIndividualBoard () {
        Parcel p1 = new BambooPlantation(BambooColor.GREEN);
        p1.setIrrigate(Side.LEFT);
        p1.growBamboo();p1.growBamboo();

        board.addParcel(1, -1, 0, p1);

        panda.setCurrentPlayer(bot);
        assertTrue(panda.moveTo(1, -1, 0));

        assertEquals(1, bot.getIndividualBoard().getGreenEatenBamboo());
    }

}
