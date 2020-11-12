package fr.matelots.polytech.core.players;

import fr.matelots.polytech.core.game.Game;

public abstract class Player {
    private final IndividualBoard individualBoard;

    public Player () {
        individualBoard = new IndividualBoard();
    }

    public IndividualBoard getIndividualBoard() {
        return individualBoard;
    }

    public boolean pickParcelObjective (Game game) {
        return individualBoard.addNewParcelObjective(game.getNextParcelObjective());
    }
}
