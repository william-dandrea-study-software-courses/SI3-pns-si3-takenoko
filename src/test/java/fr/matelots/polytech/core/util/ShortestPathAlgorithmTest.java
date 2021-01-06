package fr.matelots.polytech.core.util;

import fr.matelots.polytech.core.game.Board;
import fr.matelots.polytech.core.game.Config;
import fr.matelots.polytech.engine.util.Position;
import fr.matelots.polytech.engine.util.ShortestPathAlgorithm;
import org.junit.jupiter.api.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * @author Alexandre Arcil
 */
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class ShortestPathAlgorithmTest {

    private static Board board;

    @BeforeAll
    public static void init() {
        board = new Board();
    }

    @Test @DisplayName("La position de départ et d'arrivé est la même") @Order(1)
    public void samePosition() {
        List<Position> positions = ShortestPathAlgorithm.shortestPath(Config.POND_POSITION, Config.POND_POSITION, board);
        assertTrue(Collections.singletonList(Config.POND_POSITION).containsAll(positions));
    }

    @Test @DisplayName("Les positions forment une ligne droite") @Order(2)
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
        List<Position> positions = ShortestPathAlgorithm.shortestPath(Config.POND_POSITION, goal, board);
        assertTrue(Arrays.asList(Config.POND_POSITION, posPath1, posPath2, goal).containsAll(positions));
    }

    @Test @DisplayName("La position a atteindre n'est pas en ligne droite") @Order(3)
    public void complexPositions() {
        Position goal = new Position(1, 2, -3);
        Position posPath1 = new Position(1, 0, -1);
        Position posPath2 = new Position(2, 0, -2);
        Position posPath3 = new Position(2, 1, -3);
        board.addBambooPlantation(posPath3);
        board.addBambooPlantation(new Position(3, 1, -4));
        board.addBambooPlantation(new Position(2, 2, -4));
        board.addBambooPlantation(goal);
        List<Position> positions = ShortestPathAlgorithm.shortestPath(Config.POND_POSITION, goal, board);
        assertTrue(Arrays.asList(Config.POND_POSITION, posPath1, posPath2, posPath3, goal).containsAll(positions));
    }

    @Test @DisplayName("La position de début et d'objectif est la même donc forme une ligne")
    public void isLineOnePosition() {
        List<Position> path = ShortestPathAlgorithm.shortestPath(Config.POND_POSITION, Config.POND_POSITION, board);
        assertTrue(ShortestPathAlgorithm.isLine(path));
    }

    @Test @DisplayName("La position de début et d'objectif se trouve à côté donc forme une ligne")
    public void isLineTwoPositions() {
        List<Position> path = ShortestPathAlgorithm.shortestPath(Config.POND_POSITION, new Position(1, 0, -1), board);
        assertTrue(ShortestPathAlgorithm.isLine(path));
    }

    @Test @DisplayName("La position de début et d'objectif forme une ligne")
    public void isLine() {
        List<Position> path = ShortestPathAlgorithm.shortestPath(Config.POND_POSITION, new Position(3, 0, -3), board);
        assertTrue(ShortestPathAlgorithm.isLine(path));
    }

    @Test @DisplayName("La position de début et d'objectif ne forme pas une ligne")
    public void notLine() {
        List<Position> path = ShortestPathAlgorithm.shortestPath(Config.POND_POSITION, new Position(1, 2, -3), board);
        assertFalse(ShortestPathAlgorithm.isLine(path));
    }

}
