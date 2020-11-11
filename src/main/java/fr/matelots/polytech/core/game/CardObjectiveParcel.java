package fr.matelots.polytech.core.game;

import fr.matelots.polytech.engine.util.Position;

import java.util.Set;

/**
 * @author Alexandre Arcil
 */
public class CardObjectiveParcel {

    private boolean completed;
    private final Board board;

    public CardObjectiveParcel(Board board) {
        this.board = board;
    }

    public boolean verify() {
        Set<Position<Integer>> positions = this.board.getPositions();
        //On enlève l'étang
        positions.removeIf(pos -> pos.getX() == 0 && pos.getY() == 0 && pos.getZ() == 0);
        for (Position<Integer> position : positions) {
            if(this.board.getNbNeighbors(position.getX(), position.getY(), position.getZ()) >= 2)
                return this.completed = true;
        }
        return false;
    }

    public boolean isCompleted() {
        return completed;
    }
}
