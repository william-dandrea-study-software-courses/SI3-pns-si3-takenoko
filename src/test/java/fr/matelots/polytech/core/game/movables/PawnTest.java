package fr.matelots.polytech.core.game.movables;

import fr.matelots.polytech.core.UnreachableParcelException;
import fr.matelots.polytech.core.game.Board;
import fr.matelots.polytech.core.game.parcels.BambooColor;
import fr.matelots.polytech.core.game.parcels.BambooPlantation;
import fr.matelots.polytech.core.game.parcels.Parcel;
import fr.matelots.polytech.engine.util.Position;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Gabriel Cogne
 */
public class PawnTest {
    private Pawn pawn;
    private Board board;

    @BeforeEach
    void init () {
        board = new Board();
        pawn = board.getGardener();
    }

    @Test
    void testMoveFromStartToStart () {
        Position tmp = pawn.getPosition();
        assertTrue(pawn.moveTo(tmp.getX(), tmp.getY(), tmp.getZ()));
    }

    @Test
    void testMoveToNonexistentPosition() {
        Position tmp = pawn.getPosition();
        tmp = tmp.add(new Position(1, -1, 0));
        assertFalse(board.containTile(tmp));
        Position finalTmp = tmp;
        assertThrows(UnreachableParcelException.class, () -> pawn.moveTo(finalTmp.getX(), finalTmp.getY(), finalTmp.getZ()));
    }

    @Test
    void testMoveToNeighboursOfStart () {
        Parcel p1 = new BambooPlantation(BambooColor.GREEN);
        board.addParcel(1, -1, 0, p1);

        assertTrue(pawn.moveTo(1, -1, 0));
    }

    @Test
    void testMoveToCorrectPositionNotNeighbour () {
        board.addParcel(1, -1, 0, new BambooPlantation(BambooColor.GREEN));
        board.addParcel(1, 0, -1, new BambooPlantation(BambooColor.GREEN));
        board.addParcel(2, -1, -1, new BambooPlantation(BambooColor.GREEN));
        board.addParcel(2, -2, 0, new BambooPlantation(BambooColor.GREEN));

        assertTrue(board.containTile(new Position(2, -2, 0)));
        assertTrue(pawn.moveTo(2, -2, 0));
    }

    @Test
    void testMoveToUnalignedPosition () {
        board.addParcel(1, -1, 0, new BambooPlantation(BambooColor.GREEN));
        board.addParcel(1, 0, -1, new BambooPlantation(BambooColor.GREEN));
        board.addParcel(2, -1, -1, new BambooPlantation(BambooColor.GREEN));

        assertTrue(board.containTile(new Position(2, -1, -1)));
        assertThrows(UnreachableParcelException.class, () -> pawn.moveTo(2, -1, -1));
    }

    @Test
    void testMoveOverBlankSpace () {
        board.addParcel(-1, 0, 1, new BambooPlantation(BambooColor.GREEN));
        board.addParcel(0, -1, 1, new BambooPlantation(BambooColor.GREEN));
        board.addParcel(-1, -1, 2, new BambooPlantation(BambooColor.GREEN));
        board.addParcel(0, -2, 2, new BambooPlantation(BambooColor.GREEN));
        board.addParcel(1, -2, 1, new BambooPlantation(BambooColor.GREEN));
        board.addParcel(1, -3, 2, new BambooPlantation(BambooColor.GREEN));
        board.addParcel(2, -3, 1, new BambooPlantation(BambooColor.GREEN));
        board.addParcel(2, -2, 0, new BambooPlantation(BambooColor.GREEN));

        assertFalse(board.containTile(new Position(1, -1, 0)));
        assertTrue(board.containTile(new Position(2, -2, 0)));
        assertThrows(UnreachableParcelException.class, () -> pawn.moveTo(2, -2, 0));
    }
}
