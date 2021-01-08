package fr.matelots.polytech.core.game.graphics;

import fr.matelots.polytech.core.game.Board;

/**
 * Cette classe permet d'afficher les éléments du board
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

    /**
     * Dessine le plateau de jeu: les parcelles, la position du panda, jardinier, les aménagements et les irrigations
     */
    public void print() {
        BoardDrawingBuffer bdb = new BoardDrawingBuffer(board);
        bdb.drawHexas();
    }
}
