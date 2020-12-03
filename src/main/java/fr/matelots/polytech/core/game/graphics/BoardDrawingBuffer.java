package fr.matelots.polytech.core.game.graphics;

import fr.matelots.polytech.core.game.Board;
import fr.matelots.polytech.core.game.Config;
import fr.matelots.polytech.engine.util.Position;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author Yann Clodong
 */
public class BoardDrawingBuffer {

    private static final Logger LOGGER = Logger.getLogger("boardLogger");

    private final List<Position> allReadyDrawn = new ArrayList<>();
    private final Board board;
    private int width = 1;
    private int height = 1;

    private int offsetX = 0;
    private int offsetY = 0;
    private final ArrayList<ArrayList<ColoredCharacters>> buffer = new ArrayList<>();

    public BoardDrawingBuffer(Board board) {
        this.board = board;
        buffer.add(new ArrayList<>());
        buffer.get(0).add(new ColoredCharacters(ConsoleColor.NONE, ' '));
    }


    void drawHexas() {
        beginDraw(Config.BOND_POSITION, new Position(0, 0, 0));
        print();
    }

    void beginDraw(Position currentParcel, Position position) {
        if(board.containTile(currentParcel) && !allReadyDrawn.contains(currentParcel)) {
            Hexagone hexa = new Hexagone(position, currentParcel);
            // Parcel  parcel = board.getParcel(currentParcel);
            allReadyDrawn.add(currentParcel);
            hexa.printHexa(this, board);

            hexa.getNeighbours().forEach((Position p, Position rp) ->
                    beginDraw(currentParcel.add(p), position.add(rp))
            );
        }
    }

    private void print() {
        LOGGER.finest("\n");
        StringBuilder lineContent = new StringBuilder();
        lineContent.append(ConsoleColor.WHITE.getColorCode());
        lineContent.append('\n');
        for(var line : buffer) {
            for(var c : line) {
                lineContent.append(c);
            }
            lineContent.append('\n');
            //System.out.println(lineContent);
            //LOGGER.info(lineContent.toString());
        }
        LOGGER.info(lineContent.toString());
    }

    void setCharacter(int x, int y, char c) {
        setCharacter(x, y, c, ConsoleColor.NONE);
    }

    void setCharacter(int x, int y, char c, ConsoleColor color) {
        int tX = x + offsetX;
        int tY = y + offsetY;

        ColoredCharacters character = new ColoredCharacters(color, c);

        if(tX < 0) {
            addColumnStart();
            setCharacter(x, y, c, color);
            return;
        }
        if(tY < 0) {
            addLineStart();
            setCharacter(x, y, c, color);
            return;
        }
        if(tX >= width) {
            addColumnEnd();
            setCharacter(x, y, c, color);
            return;
        }
        if(tY >= height) {
            addLineEnd();
            setCharacter(x, y, c, color);
            return;
        }

        buffer.get(tY).set(tX, character);
    }

    private void addColumnStart() {
        offsetX++;
        for(int i = 0; i < height; i++) {
            var c = new ColoredCharacters(ConsoleColor.NONE, ' ');
            buffer.get(i).add(0, c);
        }
        width++;
    }

    private void addColumnEnd() {
        for(int i = 0; i < height; i++) {
            var c = new ColoredCharacters(ConsoleColor.NONE, ' ');
            buffer.get(i).add(c);
        }
        width++;
    }

    private void addLineStart() {
        offsetY++;
        ArrayList<ColoredCharacters> line = new ArrayList<>();
        for(int i = 0; i < width; i++) {
            var c = new ColoredCharacters(ConsoleColor.NONE, ' ');
            line.add(c);
        }
        height++;
        buffer.add(0, line);
    }

    private void addLineEnd() {
        ArrayList<ColoredCharacters> line = new ArrayList<>();
        for(int i = 0; i < width; i++) {
            var c = new ColoredCharacters(ConsoleColor.NONE, ' ');
            line.add(c);
        }
        height++;
        buffer.add(line);
    }
}
