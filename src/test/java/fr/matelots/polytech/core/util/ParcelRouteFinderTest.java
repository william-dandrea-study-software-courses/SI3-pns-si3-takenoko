package fr.matelots.polytech.core.util;

import fr.matelots.polytech.core.game.Board;
import fr.matelots.polytech.core.game.Config;
import fr.matelots.polytech.core.game.Game;
import fr.matelots.polytech.core.game.graphics.BoardDrawer;
import fr.matelots.polytech.core.game.parcels.BambooColor;
import fr.matelots.polytech.core.game.parcels.BambooPlantation;
import fr.matelots.polytech.core.game.parcels.Side;
import fr.matelots.polytech.engine.util.AbsolutePositionIrrigation;
import fr.matelots.polytech.engine.util.Position;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.function.Predicate;

import static fr.matelots.polytech.engine.util.ParcelRouteFinder.*;
import static fr.matelots.polytech.engine.util.ParcelRouteFinder.getBestPathToIrrigate;
import static org.junit.jupiter.api.Assertions.*;

public class ParcelRouteFinderTest {
    Game game;
    Board board;
    BoardDrawer drawer;
    @BeforeEach
    void init() {
        game = new Game();
        board = game.getBoard();
        drawer = new BoardDrawer(board);

    }

    void initMap() {
        for(var color : BambooColor.values()) {
            while (board.getParcelLeftToPlace(color) != 0) {
                board.getValidPlaces().stream().findAny().ifPresent(p -> board.addParcel(p, new BambooPlantation(color)));
            }
        }
    }

    @Test
    void AllIsNormal() {
        initMap();
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
        initMap();
        board.placeIrrigation(new Position(1, -1, 0), Side.UPPER_LEFT);
        board.placeIrrigation(new Position(1, -1, 0), Side.UPPER_RIGHT);

        var optpath = getBestPathToIrrigate(board, new Position(3, -1, -2));
        assertTrue(optpath.isPresent());
        var path = optpath.get();

        var nextIrr = getNextParcelToIrrigate(path);
        while(nextIrr.isPresent()) {
            nextIrr.get().irrigate();
            nextIrr = getNextParcelToIrrigate(path);
        }

        drawer.print();

        assertTrue(board.getParcel(3, -1, -2).isIrrigate());
    }

    @Test
    void TestIrrigateOnMany() {
        for(int i = 0; i < 500; i++) {
            init();
            initMap();

            var posOpt = board.getPositions().stream().filter(p -> !board.getParcel(p).isIrrigate()).findAny();
            if(posOpt.isEmpty()) continue;

            var pos = posOpt.get();
            var path = getBestPathToIrrigate(board, pos);

            if(path.isEmpty()) continue;

            var nextIrr = getNextParcelToIrrigate(path.get());
            while(nextIrr.isPresent()) {
                nextIrr.get().irrigate();
                nextIrr = getNextParcelToIrrigate(path.get());
            }
            // Force Render
            //path.get().forEach(api -> board.getParcel(api.getPosition()).setIrrigate(api.getSide()));

            if(!board.getParcel(pos).isIrrigate()) {

                drawer.print();
                assertTrue(false);
            }

        }

    }

    @Test
    void testVerySmallPath() {
        initMap();
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

    @Test
    void diamondTest() {
        BambooPlantation plantation = new BambooPlantation(BambooColor.GREEN);
        this.board.addParcel(new Position(0, 1, -1), plantation);
        BambooPlantation plantation2 = new BambooPlantation(BambooColor.GREEN);
        this.board.addParcel(new Position(1, 0, -1), plantation2);
        BambooPlantation plantation3 = new BambooPlantation(BambooColor.YELLOW);
        this.board.addParcel(new Position(1, 1, -2), plantation3);
        drawer.print();


        var path = getBestPathToIrrigate(board, new Position(1, 1, -2));
        assertTrue(path.isPresent());

        var nextP = getNextParcelToIrrigate(path.get());
        while(nextP.isPresent()) {
            nextP.get().irrigate();
            nextP = getNextParcelToIrrigate(path.get());
        }

        assertTrue(plantation3.isIrrigate());
    }

    @Test
    void testBestPathFinderDoNotReturnIrrigatedPosition() {
        for(int i = 0; i < 500; i++) {
            init();
            initMap();

            var randomPosition = board.getPositions().toArray(Position[]::new)[Config.RANDOM.nextInt(board.getPositions().size())];
            var path = getBestPathToIrrigate(board, randomPosition);
            if(path.isEmpty()) continue;

            assertTrue(path.get().stream().noneMatch(AbsolutePositionIrrigation::isIrrigate));
        }
    }
}
