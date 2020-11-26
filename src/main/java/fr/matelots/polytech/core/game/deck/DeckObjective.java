package fr.matelots.polytech.core.game.deck;

import fr.matelots.polytech.core.PickDeckEmptyException;
import fr.matelots.polytech.core.game.Board;
import fr.matelots.polytech.core.game.goalcards.CardObjective;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Alexandre Arcil
 */
public class DeckObjective<T extends CardObjective> {

    protected final List<T> objectives;
    protected final Board board;

    public DeckObjective(Board board) {
        this.board = board;
        this.objectives = new ArrayList<>();
    }

    public T pick() {
        if(this.canPick())
            return this.objectives.remove(0);
        else
            throw new PickDeckEmptyException();
    }

    public boolean canPick() {
        return !this.objectives.isEmpty();
    }

}
