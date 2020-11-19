package fr.matelots.polytech.core.game;

import fr.matelots.polytech.core.PickDeckEmptyException;
import fr.matelots.polytech.core.game.goalcards.CardObjectiveParcel;
import fr.matelots.polytech.core.game.goalcards.pattern.Patterns;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Alexandre Arcil
 */
public class DeckParcelObjective {

    public static final int DECK_SIZE = 15;
    private final List<CardObjectiveParcel> objectives;

    public DeckParcelObjective(Board board) {
        this.objectives = new ArrayList<>();
        for(int i = 0; i < 3; i++)
            this.objectives.add(new CardObjectiveParcel(board, 1, Patterns.TRIANGLE));
        for(int i = 0; i < 6; i++)
            this.objectives.add(new CardObjectiveParcel(board, 1, Patterns.RHOMBUS));
        for(int i = 0; i < 3; i++)
            this.objectives.add(new CardObjectiveParcel(board, 1, Patterns.LINE));
        for(int i = 0; i < 3; i++)
            this.objectives.add(new CardObjectiveParcel(board, 1, Patterns.C));
        if(this.objectives.size() != DECK_SIZE)
            throw new RuntimeException("La taille du paquet est de "+this.objectives.size()
                    + " alors qu'elle devrait être de "+DECK_SIZE);
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
