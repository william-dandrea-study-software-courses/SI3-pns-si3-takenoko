package fr.matelots.polytech.engine.util;

import fr.matelots.polytech.core.game.Board;
import fr.matelots.polytech.core.game.parcels.Side;

import java.util.*;

/**
 * @author Alexandre Arcil
 */
public class ShortestPathAlgorithm {

    private final Board board;

    public ShortestPathAlgorithm(Board board) {
        this.board = board;
    }

    public List<Position> shortestPath(Position start, Position goal) {
        Queue<Position> frontier = new LinkedList<>();
        frontier.add(start);
        Map<Position, Position> cameFrom = new HashMap<>();
        cameFrom.put(start, null);
        while(!frontier.isEmpty()) {
            Position current = frontier.poll();
            for (Position neighbor : this.neighbors(current)) {
                if(!cameFrom.containsKey(neighbor) && this.board.containTile(neighbor)) {
                    frontier.add(neighbor);
                    cameFrom.put(neighbor, current);
                }
            }
        }
        Position current = goal;
        List<Position> path = new ArrayList<>();
        while (current != start) {
            path.add(current);
            current = cameFrom.get(current);
        }
        path.add(start);
        Collections.reverse(path);
        return path;
    }

    private Set<Position> neighbors(Position pos) {
        Set<Position> positions = new HashSet<>();
        for(Side side : Side.values())
            positions.add(pos.add(side.getDirection()));
        return positions;
    }

}
