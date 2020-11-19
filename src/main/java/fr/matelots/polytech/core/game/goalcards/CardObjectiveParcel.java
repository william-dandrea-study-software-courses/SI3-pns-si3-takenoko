package fr.matelots.polytech.core.game.goalcards;

import fr.matelots.polytech.core.game.Board;
import fr.matelots.polytech.engine.util.Position;

import java.util.Set;

/**
 * @author Alexandre Arcil
 */
public class CardObjectiveParcel {

    private final Board board;
    private final int score;
    private boolean completed;

    public CardObjectiveParcel(Board board, int score) {
        this.board = board;
        this.score = Math.max(score, 1); //éviter score <= 0
        /*
        new Position(0, -1, 1), new..., new... -> forme de triangle
         */
    }

    public boolean verify() {
        Set<Position> positions = this.board.getPositions();
        //On enlève l'étang
        positions.removeIf(pos -> pos.getX() == 0 && pos.getY() == 0 && pos.getZ() == 0);
        for (Position position : positions) {
            if(this.board.getNbNeighbors(position.getX(), position.getY(), position.getZ()) >= 2)
                return this.completed = true;
        }
        return false;
    }

    public boolean isCompleted() {
        return completed;
    }

    public int getScore() {
        return score;
    }
}
