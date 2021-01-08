package fr.matelots.polytech.core.game.graphics;

import fr.matelots.polytech.core.game.Board;

/**
 * Cette classe permet d'afficher les élements du board
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
