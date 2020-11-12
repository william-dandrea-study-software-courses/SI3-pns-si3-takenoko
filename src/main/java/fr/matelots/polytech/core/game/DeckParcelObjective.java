package fr.matelots.polytech.core.game;

import fr.matelots.polytech.core.PickDeckEmptyException;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Alexandre Arcil
 */
public class DeckParcelObjective {

    public static final int DECK_SIZE = 14;
    private final List<CardObjectiveParcel> objectives;

    public DeckParcelObjective(Board board) {
        this.objectives = new ArrayList<>();
        for(int i = 0; i < DECK_SIZE; i++)
            this.objectives.add(new CardObjectiveParcel(board));
    }

    public CardObjectiveParcel pick() {
        if(this.canPick())
            return this.objectives.remove(0);
        else
            throw new PickDeckEmptyException();
    }

    public boolean canPick() {
        return !this.objectives.isEmpty();
    }

}
