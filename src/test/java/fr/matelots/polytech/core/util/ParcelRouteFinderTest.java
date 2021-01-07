package fr.matelots.polytech.core.util;

import fr.matelots.polytech.core.game.Board;
import fr.matelots.polytech.core.game.Game;
import fr.matelots.polytech.core.game.graphics.BoardDrawer;
import fr.matelots.polytech.core.game.parcels.BambooColor;
import fr.matelots.polytech.core.game.parcels.BambooPlantation;
import fr.matelots.polytech.core.game.parcels.Side;
import fr.matelots.polytech.engine.util.AbsolutePositionIrrigation;
import fr.matelots.polytech.engine.util.Position;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.function.Predicate;

import static fr.matelots.polytech.engine.util.ParcelRouteFinder.*;
import static org.junit.jupiter.api.Assertions.*;

public class ParcelRouteFinderTest {
    Game game;
    Board board;
    BoardDrawer drawer;
    @BeforeEach
    void Init() {
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
    void AllIsNormal() {
        var result = getIrrigationToIrrigate(board, new Position(0, 0, 0), new Position(2, -3, 1));
        drawer.print();
        assertFalse(result.isEmpty());


        while(result.get().stream().filter(Predicate.not(AbsolutePositionIrrigation::isIrrigate)).anyMatch(AbsolutePositionIrrigation::canBeIrrigated)) {
            result.get().stream()
                    .filter(val -> !val.isIrrigate() && val.canBeIrrigated())
                    .forEach(AbsolutePositionIrrigation::irrigate);
        }

        drawer.print();
        result.stream().map(Object::toString).forEach(System.out::println);


        var expecteds = new AbsolutePositionIrrigation[] {
            new AbsolutePositionIrrigation(new Position(1, -2, 1), Side.UPPER_RIGHT),
            new AbsolutePositionIrrigation(new Position(1, -1, 0), Side.BOTTOM_RIGHT),
            new AbsolutePositionIrrigation(new Position(1, -2, 1), Side.RIGHT),
            new AbsolutePositionIrrigation(new Position(1, -1, 0), Side.BOTTOM_LEFT)
        };


        /*for(var expected : expecteds) {
            assertTrue(result.get().stream().anyMatch(api -> api.equals(expected)), expected.toString());
        }*/
    }

    @Test
    void testGoingTo_3_minus1_minus2() {
        board.placeIrrigation(new Position(1, -1, 0), Side.UPPER_LEFT);
        board.placeIrrigation(new Position(1, -1, 0), Side.UPPER_RIGHT);

        var optpath = getBestPathToIrrigate(board, new Position(3, -1, -2));
        assertTrue(optpath.isPresent());
        var path = optpath.get();

        while(path.stream().filter(Predicate.not(AbsolutePositionIrrigation::isIrrigate)).anyMatch(AbsolutePositionIrrigation::canBeIrrigated)) {
            path.stream()
                    .filter(val -> !val.isIrrigate() && val.canBeIrrigated())
                    .forEach(api -> board.placeIrrigation(api.getPosition(), api.getSide()));
        }

        assertTrue(board.getParcel(3, -1, -2).isIrrigate());
    }

    @Test
    void TestIrrigateOnMany() {
        for(int i = 0; i < 15; i++) {
            var posOpt = board.getPositions().stream().filter(p -> !board.getParcel(p).isIrrigate()).findAny();
            if(posOpt.isEmpty()) continue;

            var pos = posOpt.get();
            var path = getBestPathToIrrigate(board, pos);

            if(path.isEmpty()) continue;

            while(path.get().stream().filter(Predicate.not(AbsolutePositionIrrigation::isIrrigate)).anyMatch(AbsolutePositionIrrigation::canBeIrrigated)) {
                path.get().stream()
                        .filter(val -> !val.isIrrigate() && val.canBeIrrigated())
                        .forEach(api -> board.placeIrrigation(api.getPosition(), api.getSide()));
            }
            // Force Render
            //path.get().forEach(api -> board.getParcel(api.getPosition()).setIrrigate(api.getSide()));

            assertTrue(board.getParcel(pos).isIrrigate());
        }

    }


    @Test
    void testVerySmallPath() {
        var path = getBestPathToIrrigate(board, new Position(1, -3, 2));
        assertTrue(path.isPresent());

        var nextToIrrigate = getNextParcelToIrrigate(path.get());

        while(nextToIrrigate.isPresent()) {
            nextToIrrigate.get().irrigate();
            nextToIrrigate = getNextParcelToIrrigate(path.get());
        }

        assertTrue(board.getParcel(1, -3, 2).isIrrigate());

        var path2 = getBestPathToIrrigate(board, new Position(2, -3, 1));
        assertTrue(path2.isPresent());
    }
}
