package fr.matelots.polytech.core.players.bots.goalresolvers;

import fr.matelots.polytech.core.game.Board;
import fr.matelots.polytech.core.game.goalcards.AlignedParcelGoal;
import fr.matelots.polytech.core.game.Parcel;
import fr.matelots.polytech.core.players.bots.utils.boardpattern.SimpleLinePattern;
import fr.matelots.polytech.engine.util.Position;
import fr.matelots.polytech.engine.util.Vector;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Yann Clodong
 */
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
        Vector[] directions = new Vector[] {
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

                Position end;
                Position p = position;
                do { // Check though positive direction
                    end = p;
                    p = Position.add(p, direction);
                    length++;
                } while(board.containTile(p));

                p = position;
                Position start;
                Vector oppDir = Vector.oposite(direction);
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
            if(l.getLength() < goalCard.getLength() && (line == null || l.getLength() > line.getLength()))
                line = l;
        }


        // Add parcel
        if(line != null) {
            Position action = line.getSide();
            board.addParcel(action, new Parcel());
        }
        return false;
    }


    /**
     * Enlarge the line
     * @param line: The line to enlarge
     * @param goal: the goal to fulfill
     * @return true if the goal is completed, false else
     */
    boolean resolveGoal(SimpleLinePattern line, AlignedParcelGoal goal) {
        Position p = line.getEnd();

        if(board.containTile(p)) p = Position.add(line.getEnd(), line.getCalculatedDirection());

        boolean successful = board.addParcel(p.getX(), p.getY(), p.getZ(), new Parcel());

        //System.out.println(successful);

        if(line.getLength() == goal.getLength() - 1)
            return true;
        return false;
    }
}
