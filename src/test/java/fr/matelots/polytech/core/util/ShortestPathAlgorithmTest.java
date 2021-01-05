package fr.matelots.polytech.core.util;

import fr.matelots.polytech.core.game.Board;
import fr.matelots.polytech.core.game.Config;
import fr.matelots.polytech.core.game.graphics.BoardDrawer;
import fr.matelots.polytech.engine.util.Position;
import fr.matelots.polytech.engine.util.ShortestPathAlgorithm;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * @author Alexandre Arcil
 */
public class ShortestPathAlgorithmTest {

    private static ShortestPathAlgorithm algo;
    private static Board board;
    private static BoardDrawer drawer;

    @BeforeAll
    public static void init() {
        board = new Board();
        algo = new ShortestPathAlgorithm(board);
        drawer = new BoardDrawer(board);
    }

    @Test @DisplayName("La position de départ et d'arrivé est la même")
    public void samePosition() {
        List<Position> positions = algo.shortestPath(Config.POND_POSITION, Config.POND_POSITION);
        assertTrue(Collections.singletonList(Config.POND_POSITION).containsAll(positions));
    }

    @Test @DisplayName("Les positions forment une ligne droite")
    public void linePositions() {
        Position goal = new Position(3, 0, -3);
        Position posPath1 = new Position(1, 0, -1);
        Position posPath2 = new Position(2, 0, -2);
        board.addBambooPlantation(posPath1);
        board.addBambooPlantation(new Position(1, -1, 0));
        board.addBambooPlantation(new Position(2, -1, -1));
        board.addBambooPlantation(posPath2);
        board.addBambooPlantation(new Position(3, -1, -2));
        board.addBambooPlantation(goal);
        List<Position> positions = algo.shortestPath(Config.POND_POSITION, goal);
        assertTrue(Arrays.asList(Config.POND_POSITION, posPath1, posPath2, goal).containsAll(positions));
    }

    @Test @DisplayName("La position a atteindre n'est pas en ligne droite")
    public void complexPositions() {
        Position goal = new Position(1, 2, -3);
        Position posPath1 = new Position(1, 0, -1);
        Position posPath2 = new Position(2, 0, -2);
        Position posPath3 = new Position(2, 1, -3);
        board.addBambooPlantation(posPath3);
        board.addBambooPlantation(new Position(3, 1, -4));
        board.addBambooPlantation(new Position(2, 2, -4));
        board.addBambooPlantation(goal);
        drawer.print();
        List<Position> positions = algo.shortestPath(Config.POND_POSITION, goal);
        assertTrue(Arrays.asList(Config.POND_POSITION, posPath1, posPath2, posPath3, goal).containsAll(positions));
    }

}
