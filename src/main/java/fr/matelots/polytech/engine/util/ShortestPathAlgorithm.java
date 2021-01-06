package fr.matelots.polytech.engine.util;

import fr.matelots.polytech.core.game.Board;
import fr.matelots.polytech.core.game.parcels.Side;

import java.util.*;

/**
 * @author Alexandre Arcil
 * Algorithme utilisé: "Breadth First Search"
 */
public class ShortestPathAlgorithm {

    /**
     * Calcul le chemin le plus court pour atteindre une position à partir d'une position de départ. Contient la
     * position de départ est d'arrivé. Commence par la position de départ et fini par la position d'arrivée.
     * @param start La position de départ
     * @param goal La position à atteindre
     * @param board Le board
     * @return Les positions à aller pour aller du point de départ à l'arrivé
     */
    public static List<Position> shortestPath(Position start, Position goal, Board board) {
        Queue<Position> frontier = new LinkedList<>();
        frontier.add(start);
        Map<Position, Position> cameFrom = new HashMap<>();
        cameFrom.put(start, null);
        while(!frontier.isEmpty()) {
            Position current = frontier.poll();
            for (Position neighbor : neighbors(current)) {
                if(!cameFrom.containsKey(neighbor) && board.containTile(neighbor)) {
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

    /**
     * Permet de savoir s'il le chemin forme une ligne. Utile pour savoir si on peut déplacer le jardinier ou le panda
     * entre 2 points directement.
     * @param path Le chemin calculé avec {@link #shortestPath(Position, Position, Board)}
     * @return true si le chemin est une ligne, false sinon
     */
    public static boolean isLine(List<Position> path) {
        if(path.size() == 1 || path.size() == 2)
            return true;
        Side side = Side.getTouchedSide(path.get(0), path.get(1));
        for(int i = 2; i < path.size(); i++) {
            if(Side.getTouchedSide(path.get(i - 1), path.get(i)) != side)
                return false;
        }
        return true;
    }

    private static Set<Position> neighbors(Position pos) {
        Set<Position> positions = new HashSet<>();
        for(Side side : Side.values())
            positions.add(pos.add(side.getDirection()));
        return positions;
    }

}
