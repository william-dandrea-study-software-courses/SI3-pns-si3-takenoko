package fr.matelots.polytech.core.game.graphics;

import fr.matelots.polytech.core.game.Board;
import fr.matelots.polytech.core.game.Config;
import fr.matelots.polytech.engine.util.Position;
import fr.matelots.polytech.engine.util.Vector;
import fr.matelots.polytech.engine.util.Vector2Int;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Yann Clodong
 */
public class BoardDrawingBuffer {

    private final List<Position> allReadyDrawn = new ArrayList<>();
    private final Board board;
    private int width = 1;
    private int height = 1;

    private int offsetX = 0;
    private int offsetY = 0;
    private final ArrayList<ArrayList<Character>> buffer = new ArrayList<>();

    public BoardDrawingBuffer(Board board) {
        this.board = board;
        buffer.add(new ArrayList<>());
        buffer.get(0).add(' ');
    }


    void drawHexas() {
        beginDraw(Config.BOND_POSITION, new Vector2Int(0, 0));
        print();
    }

    void beginDraw(Position currentParcel, Vector2Int position) {
        if(board.containTile(currentParcel) && !allReadyDrawn.contains(currentParcel)) {
            Hexagone hexa = new Hexagone(position);
            // Parcel  parcel = board.getParcel(currentParcel);
            allReadyDrawn.add(currentParcel);
            hexa.printHexa(this);

            hexa.getNeighbours().forEach((Vector p, Vector2Int rp) ->
                    beginDraw(Position.add(currentParcel, p), Vector2Int.add(position, rp))
            );
        }
    }

    private void print() {
        for(var line : buffer) {
            StringBuilder lineContent = new StringBuilder();
            for(var c : line) {
                lineContent.append(c);
            }
            System.out.println(lineContent);
        }
    }

    void setCharacter(int x, int y, char character) {
        int tX = x + offsetX;
        int tY = y + offsetY;

        if(tX < 0) {
            addColumnStart();
            setCharacter(x, y, character);
            return;
        }
        if(tY < 0) {
            addLineStart();
            setCharacter(x, y, character);
            return;
        }
        if(tX >= width) {
            addColumnEnd();
            setCharacter(x, y, character);
            return;
        }
        if(tY >= height) {
            addLineEnd();
            setCharacter(x, y, character);
            return;
        }

        buffer.get(tY).set(tX, character);
    }

    private void addColumnStart() {
        offsetX++;
        for(int i = 0; i < height; i++) {
            buffer.get(i).add(0, ' ');
        }
        width++;
    }

    private void addColumnEnd() {
        for(int i = 0; i < height; i++) {
            buffer.get(i).add(' ');
        }
        width++;
    }

    private void addLineStart() {
        offsetY++;
        ArrayList<Character> t = new ArrayList<>();
        for(int i = 0; i < width; i++) {
            t.add(' ');
        }
        height++;
        buffer.add(0, t);
    }

    private void addLineEnd() {
        ArrayList<Character> t = new ArrayList<>();
        for(int i = 0; i < width; i++) {
            t.add(' ');
        }
        height++;
        buffer.add(t);
    }
}
