package fr.matelots.polytech.core.players;

import fr.matelots.polytech.core.game.Board;

public abstract class VirtualPlayer extends Player {


    protected Board board;

    public VirtualPlayer () {
        super ();
    }

    public abstract void playTurn ();

    public void setBoard(Board board) {
        this.board = board;
    }
}
