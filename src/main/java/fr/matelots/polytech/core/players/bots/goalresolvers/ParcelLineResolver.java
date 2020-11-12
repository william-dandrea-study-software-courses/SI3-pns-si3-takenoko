package fr.matelots.polytech.core.players.bots.goalresolvers;

import fr.matelots.polytech.core.game.Board;
import fr.matelots.polytech.core.game.CardObjectiveParcel;
import fr.matelots.polytech.core.game.GoalCards.AlignedParcelGoal;
import fr.matelots.polytech.core.game.Parcel;
import fr.matelots.polytech.core.players.bots.utils.SimpleLinePatternFinder;
import fr.matelots.polytech.core.players.bots.utils.boardpattern.SimpleLinePattern;
import fr.matelots.polytech.engine.util.Position;

import java.util.List;

public class ParcelLineResolver {

    private final Board board;

    public ParcelLineResolver(Board board) {
        this.board = board;
    }

    /**
     * Search for resolving the goal if it is resolvable, attempt to resolve
     * @param goalCard: goal to fulfill
     * @return return True if the goal is resolvable, false if it is not
     */
    public boolean attemptResolve(AlignedParcelGoal goalCard) {
        SimpleLinePatternFinder finder = new SimpleLinePatternFinder();
        SimpleLinePattern line = finder.GetLinePatterns(board, goalCard.getLength());

        if(line == null) {
            return false;
        } else {
            boolean complete = ResoveGoal(line, goalCard);
            if(complete) goalCard.setComplete();

            return true;
        }
    }


    /**
     * Enlarge the line
     * @param line: The line to enlarge
     * @param goal: the goal to fulfill
     * @return true if the goal is completed, false else
     */
    boolean ResoveGoal(SimpleLinePattern line, AlignedParcelGoal goal) {
        Position<Integer> p = line.getEnd();

        if(board.ContainTile(p)) p = Position.add(line.getEnd(), line.GetDirection());

        boolean successful = board.addParcel(p.getX(), p.getY(), p.getZ(), new Parcel());

        System.out.println(successful);

        if(line.GetLength() == goal.getLength() - 1)
            return true;
        return false;
    }
}
