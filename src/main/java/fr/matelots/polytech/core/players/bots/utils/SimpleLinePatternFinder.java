package fr.matelots.polytech.core.players.bots.utils;

import fr.matelots.polytech.core.game.Board;
import fr.matelots.polytech.core.players.bots.utils.boardpattern.SimpleLinePattern;
import fr.matelots.polytech.engine.util.Position;
import fr.matelots.polytech.engine.util.Vector;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class SimpleLinePatternFinder {

    /**
     * Get the longer line from a tile along a direction
     * No differences on the tiles
     * @param board: Board of the game
     * @param direction: Direction along wich running
     * @param start: the from where start
     * @return SimpleLinePattern return the longer line
     */
    SimpleLinePattern GetLargestLineAlong(Board board, Vector<Integer> direction, Position<Integer> start) {
        int i = 0;
        Position<Integer> lineStart = null;
        Position<Integer> lineEnd = null;

        Position<Integer> currentPosition = Position.add(start, Vector.multiply(direction, i));

        int totalSize = 0;


        // Getting all tile from a side
        while (board.ContainTile(currentPosition)) {
            i++;
            currentPosition = Position.add(start, Vector.multiply(direction, i));
        }
        lineStart = Position.add(start, Vector.multiply(direction, i - 1));;

        totalSize += i;
        i = 0;
        // Getting all tile from the other side
        while (board.ContainTile(currentPosition)) {
            i--;
            currentPosition = Position.add(start, Vector.multiply(direction, i));
        }
        lineEnd = Position.add(start, Vector.multiply(direction, i + 1));
        totalSize -= i;

        return new SimpleLinePattern(lineStart, lineEnd, totalSize);
    }

    /**
     * Get the longer line but shorter than the goal accross all direction on th board
     * @param board Game board
     * @param maxLength Maximum length of the line
     * @return SimpleLinePattern
     */
    public SimpleLinePattern GetLinePatterns(Board board, int maxLength) {
        Vector<Integer>[] directions = Vector.GetAllDirections();
        Set<Position<Integer>> positions = board.getPositions();

        List<SimpleLinePattern> halfResult = new ArrayList<>();

        for(Vector<Integer> direction : directions) {
            for(Position<Integer> position : positions) {
                halfResult.add(GetLargestLineAlong(board, direction, position));
            }
        }

        List<SimpleLinePattern> results = new ArrayList<>();
        halfResult.stream()
                .filter(p -> p.GetLength() < maxLength)  // Get by length
                .distinct()
                .forEach(results::add);

        SimpleLinePattern result = null;

        for(SimpleLinePattern p : results)
            if(result == null || result.GetLength() < p.GetLength()) result = p;

        return result;
    }


}
