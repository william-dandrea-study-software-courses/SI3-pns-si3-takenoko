package core.players;

import core.game.Board;

public abstract class VirtualPlayer extends Player {
    protected Board board;

    public abstract void playTurn ();
}
