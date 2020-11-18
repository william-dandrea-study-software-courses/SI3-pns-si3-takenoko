package fr.matelots.polytech.core.game.graphics;

import fr.matelots.polytech.core.game.Board;
import fr.matelots.polytech.core.game.Parcel;
import fr.matelots.polytech.engine.util.Position;

import java.util.List;
import java.util.Set;

/**
 * @author Yann CLODONG
 */

public class BoardDrawer {
    //  / \ / \
    // |   |   |
    //  \ / \ /
    //   |   |
    //    \ /

    private Board board;

    public BoardDrawer(Board board) {
        this.board = board;
    }

    public void Print() {
        BoardDrawingBuffer bdb = new BoardDrawingBuffer(board);
        bdb.DrawHexas();
    }
}
