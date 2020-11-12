package fr.matelots.polytech.core.players.bots.utils.boardpattern;

import fr.matelots.polytech.engine.util.Position;
import fr.matelots.polytech.engine.util.Vector;

public class SimpleLinePattern {
    private final Position<Integer> start, end;
    private final int length;

    public SimpleLinePattern(Position<Integer> start, Position<Integer> end, int length) {
        this.start = start;
        this.end = end;
        this.length = length;
    }

    /**
     * return the length of the line
     * @return
     */
    public int GetLength() {
        return length;
        /*Vector<Integer> dist = Position.substract(end, start);
        return (int)Math.sqrt(
                Math.pow(dist.getX(), 2) +
                Math.pow(dist.getY(), 2) +
                Math.pow(dist.getZ(), 2)
        );*/
    }

    /**
     * Return the end of the line
     * @return Position<Integer>
     */
    public Position<Integer> getEnd() {
        return end;
    }

    /**
     * Return the direction of the line
     * @return
     */
    public Vector<Integer> GetDirection() {
        return Position.substract(end, start);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof SimpleLinePattern) {
            SimpleLinePattern tmp = (SimpleLinePattern) obj;
            return (tmp.start.equals(start) && tmp.end.equals(end)) || // Verify in both ways
                    (tmp.start.equals(end) && tmp.end.equals(start));
        }
        return false;
    }


    @Override
    public String toString() {
        return "Start : " + start + ", End : " + end;
    }
}
