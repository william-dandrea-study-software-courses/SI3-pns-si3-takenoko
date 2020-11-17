package fr.matelots.polytech.core.game.graphics;

import fr.matelots.polytech.core.game.Board;
import fr.matelots.polytech.core.game.Parcel;
import fr.matelots.polytech.engine.util.Position;
import fr.matelots.polytech.engine.util.Vector;
import fr.matelots.polytech.engine.util.Vector2Int;

import java.util.ArrayList;
import java.util.List;

public class BoardDrawingBuffer {

    List<Position<Integer>> allreadyDrawn = new ArrayList<Position<Integer>>();
    Board board;
    int width = 1;
    int height = 1;

    int offsetX = 0;
    int offsetY = 0;
    ArrayList<ArrayList<Character>> buffer = new ArrayList<>();

    public BoardDrawingBuffer(Board board) {
        this.board = board;
        buffer.add(new ArrayList<>());
        buffer.get(0).add(' ');
    }


    void DrawHexas() {
        beginDraw(new Position<>(0, 0, 0), new Vector2Int(0, 0));
        Print();
    }

    void beginDraw(Position<Integer> currentParcel, Vector2Int position) {
        if(board.containTile(currentParcel) && !allreadyDrawn.contains(currentParcel)) {
            Hexagone hexa = new Hexagone(position);
            Parcel  parcel = board.getParcel(currentParcel);
            allreadyDrawn.add(currentParcel);
            hexa.PrintHexa(this, parcel);

            hexa.GetNeighboors().forEach((Vector<Integer> p, Vector2Int rp) -> {
                beginDraw(Position.add(currentParcel, p), Vector2Int.add(position, rp));
            });
        }
    }

    private void Print() {
        for(var line : buffer) {
            String lineContent = "";
            for(var c : line) {
                lineContent += c;
            }
            System.out.println(lineContent);
        }
    }

    void SetCharac(int X, int Y, char charac) {
        int tX = X + offsetX;
        int tY = Y + offsetY;

        if(tX < 0) {
            AddColumnStart();
            SetCharac(X, Y, charac);
            return;
        }
        if(tY < 0) {
            AddLineStart();
            SetCharac(X, Y, charac);
            return;
        }
        if(tX >= width) {
            AddColumnEnd();
            SetCharac(X, Y, charac);
            return;
        }
        if(tY >= height) {
            AddLineEnd();
            SetCharac(X, Y, charac);
            return;
        }

        buffer.get(tY).set(tX, charac);
    }

    void AddColumnStart() {
        offsetX++;
        for(int i = 0; i < height; i++) {
            buffer.get(i).add(0, ' ');
        }
        width++;
    }
    void AddColumnEnd() {
        for(int i = 0; i < height; i++) {
            buffer.get(i).add(' ');
        }
        width++;
    }
    void AddLineStart() {
        offsetY++;
        ArrayList<Character> t = new ArrayList<>();
        for(int i = 0; i < width; i++) {
            t.add(' ');
        }
        height++;
        buffer.add(0, t);
    }
    void AddLineEnd() {
        ArrayList<Character> t = new ArrayList<>();
        for(int i = 0; i < width; i++) {
            t.add(' ');
        }
        height++;
        buffer.add(t);
    }
}
