package fr.matelots.polytech.core.game.graphics;

import fr.matelots.polytech.core.game.Board;
import fr.matelots.polytech.core.game.parcels.Parcel;
import fr.matelots.polytech.engine.util.Position;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Yann Clodong
 */
public class Hexagone {
    //  / \ / \
    // | c |   |
    //  \ / \ /
    //   |   |
    //    \ /


    private final int radius = 2;
    private final Position consolePosition;
    private final Position boardPosition;

    public Hexagone(Position consolePosition, Position boardPosition) {
        this.boardPosition = boardPosition;
        this.consolePosition = consolePosition;
    }

    public void printHexa(BoardDrawingBuffer buffer, Board board) {
        int x = consolePosition.getX();
        int y = consolePosition.getY();

        boolean hasGardener = boardPosition.equals(board.getGardener().getPosition());
        boolean hasPanda = boardPosition.equals(board.getPanda().getPosition());

        if(hasGardener && hasPanda)
            buffer.setCharacter(x , y - 1, 'B');
        else if(hasGardener)
            buffer.setCharacter(x, y - 1, 'G', ConsoleColor.WHITE);
        else if(hasPanda)
            buffer.setCharacter(x, y - 1, 'P', ConsoleColor.WHITE);


        Parcel parcel = board.getParcel(boardPosition);
        buffer.setCharacter(x - radius, y, '|');
        buffer.setCharacter(x + radius, y, '|');
        buffer.setCharacter(x - radius / 2, y + radius / 2, '\\');
        buffer.setCharacter(x + radius / 2, y - radius / 2, '\\');
        buffer.setCharacter(x - radius / 2, y - radius / 2, '/');
        buffer.setCharacter(x + radius / 2, y + radius / 2, '/');


        ParcelDrawer drawer = new ParcelDrawer(buffer, consolePosition);
        parcel.draw(drawer);
    }

    public Map<Position, Position> getNeighbours() {
        Map<Position, Position> cores = new HashMap<>();
        cores.put(new Position(0, 1, -1), new Position(-radius, -radius, 0));
        cores.put(new Position(1, 0, -1), new Position(radius, -radius, 0));
        cores.put(new Position(1, -1, 0), new Position(2*radius, 0, 0));
        cores.put(new Position(0, -1, 1), new Position(+radius, radius, 0));
        cores.put(new Position(-1, 0, 1), new Position(-radius, radius, 0));
        cores.put(new Position(-1, 1, 0), new Position(-2 * radius, 0, 0));
        return cores;
    }
}
