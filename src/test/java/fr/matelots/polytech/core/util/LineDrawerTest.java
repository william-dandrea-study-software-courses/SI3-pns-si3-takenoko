package fr.matelots.polytech.core.util;

import fr.matelots.polytech.core.game.Board;
import fr.matelots.polytech.core.game.Game;
import fr.matelots.polytech.core.game.graphics.BoardDrawer;
import fr.matelots.polytech.core.game.parcels.BambooColor;
import fr.matelots.polytech.core.game.parcels.BambooPlantation;
import fr.matelots.polytech.engine.util.LineDrawer;
import fr.matelots.polytech.engine.util.Position;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

public class LineDrawerTest {

    Game game;
    Board board;
    BoardDrawer drawer;
    @BeforeEach
    void init() {
        game = new Game();
        board = game.getBoard();
        drawer = new BoardDrawer(board);

        for(var color : BambooColor.values()) {
            while (board.getParcelLeftToPlace(color) != 0) {
                board.getValidPlaces().stream().findAny().ifPresent(p -> board.addParcel(p, new BambooPlantation(color)));
            }
        }
    }

    @Test
    void testInit() {
        drawer.print();
    }

    @Test
    void testLineLigneDroite() {
        var result = LineDrawer.getLine(new Position(0, 0, 0), new Position(-3, 0, 3));
        var expected = new Position [] {
                new Position(0, 0, 0),
                new Position(-1, 0, 1),
                new Position(-2, 0, 2),
                new Position(-3, 0, 3)
        };
        assertTrue(result.containsAll(Arrays.asList(expected)));
    }

    @Test
    void testLineGeneral() {
        var result = LineDrawer.getLine(new Position(0, 0, 0), new Position(2, -3, 1));
        var expected = new Position [] {
                new Position(0, 0, 0),
                new Position(1, -1, 0),
                new Position(1, -2, 1),
                new Position(2, -3, 1)
        };
        assertArrayEquals(expected, result.toArray());
    }

    @Test
    void testDiamondLine() {
        var result = LineDrawer.getLine(new Position(0, 0, 0), new Position(-1, -1, 2));
        System.out.println(result);
    }

    @Test
    void RandomLine() {
        var results = LineDrawer.getLine(new Position(0, 0, 0), new Position(3, -1, -2));
        var expecteds = Arrays.asList(new Position(0, 0, 0),
                new Position(1, 0, -1),
                new Position(2, -1, -1),
                new Position(3, -1, -2));

        for(var result : results) {
            assertTrue(expecteds.stream().anyMatch(result::equals), "result unexpected : " + result);
        }
        for(var expected : expecteds) {
            assertTrue(results.stream().anyMatch(expected::equals), "A position is missing : " + expected);
        }
    }
}
