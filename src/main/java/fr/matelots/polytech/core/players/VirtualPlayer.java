package fr.matelots.polytech.core.players;

import fr.matelots.polytech.core.game.Board;
import fr.matelots.polytech.core.game.Game;

public abstract class VirtualPlayer extends Player {


    protected Board board;

    public VirtualPlayer (Game game) {
        super (game);
    }

    public abstract void playTurn ();

    public void setBoard(Board board) {
        this.board = board;
    }
}
