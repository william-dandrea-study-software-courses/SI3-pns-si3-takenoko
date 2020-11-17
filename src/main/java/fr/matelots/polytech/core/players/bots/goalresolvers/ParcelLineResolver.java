package fr.matelots.polytech.core.players.bots.goalresolvers;

import fr.matelots.polytech.core.game.Board;
import fr.matelots.polytech.core.game.GoalCards.AlignedParcelGoal;
import fr.matelots.polytech.core.game.Parcel;
import fr.matelots.polytech.core.players.bots.utils.SimpleLinePatternFinder;
import fr.matelots.polytech.core.players.bots.utils.boardpattern.SimpleLinePattern;
import fr.matelots.polytech.engine.util.Position;
import fr.matelots.polytech.engine.util.Vector;

import java.util.ArrayList;
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
        /*SimpleLinePatternFinder finder = new SimpleLinePatternFinder();
        SimpleLinePattern line = finder.GetLinePatterns(board, goalCard.getLength());

        if(line == null) {
            return false;
        } else {
            boolean complete = ResoveGoal(line, goalCard);
            if(complete) goalCard.setComplete();

            return true;
        }*/
        Vector<Integer>[] directions = new Vector[] {
            new Vector(0, -1, 1),
            new Vector(1, -1, 0),
            new Vector(1, 0, -1)
        };

        var positions = board.getPositions();
        List<SimpleLinePattern> lines = new ArrayList<>();


        // Find lines
        for(var position : positions) {
            for(var direction : directions) {

                int length = 1;

                Position<Integer> end = position;
                Position<Integer> p = position;
                do { // Check though positive direction
                    end = p;
                    p = Position.add(p, direction);
                    length++;
                } while(board.containTile(p));

                p = position;
                Position<Integer> start = position;
                Vector<Integer> oppDir = Vector.oposite(direction);
                do {
                    start = p;
                    p = Position.add(p, oppDir);
                    length++;
                } while(board.containTile(p));

                length -= 2;

                // Check for 1 remaining line
                if(length == goalCard.getLength() - 1) {
                    board.addParcel(p.getX(), p.getY(), p.getZ(), new Parcel());
                    return true;
                }
                lines.add(new SimpleLinePattern(start, end, direction, length));
            }
        }

        // Get longer line bellow goal limit
        SimpleLinePattern line = null;
        for(var l : lines ){
            if(l.GetLength() < goalCard.getLength() && (line == null || l.GetLength() > line.GetLength()))
                line = l;
        }


        // Add parcel
        if(line == null) return false;
        else {
            Position<Integer> action = line.getSide();
            board.addParcel(action, new Parcel());
            return false;
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

        if(board.containTile(p)) p = Position.add(line.getEnd(), line.GetDirection());

        boolean successful = board.addParcel(p.getX(), p.getY(), p.getZ(), new Parcel());

        //System.out.println(successful);

        if(line.GetLength() == goal.getLength() - 1)
            return true;
        return false;
    }
}
