package fr.matelots.polytech.core.game.graphics;

import fr.matelots.polytech.core.game.Board;

/**
 * @author Yann Clodong
 */
public class BoardDrawer {
    //  / \ / \
    // |   |   |
    //  \ / \ /
    //   |   |
    //    \ /

    private final Board board;

    public BoardDrawer(Board board) {
        this.board = board;
    }

    public void print() {
        BoardDrawingBuffer bdb = new BoardDrawingBuffer(board);
        bdb.drawHexas();
    }
}
