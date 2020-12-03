package fr.matelots.polytech.core.game.deck;

import fr.matelots.polytech.core.game.Board;
import fr.matelots.polytech.core.game.goalcards.CardObjectiveParcel;
import fr.matelots.polytech.core.game.goalcards.pattern.Patterns;

/**
 * @author Alexandre Arcil
 */
public class DeckParcelObjective extends DeckObjective<CardObjectiveParcel> {

    public DeckParcelObjective(Board board) {
        super(board);
    }

    @Override
    protected void fill() {
        for(int i = 0; i < 3; i++)
            this.objectives.add(new CardObjectiveParcel(board, 1, Patterns.TRIANGLE));
        for(int i = 0; i < 6; i++)
            this.objectives.add(new CardObjectiveParcel(board, 1, Patterns.RHOMBUS));
        for(int i = 0; i < 3; i++)
            this.objectives.add(new CardObjectiveParcel(board, 1, Patterns.LINE));
        for(int i = 0; i < 3; i++)
            this.objectives.add(new CardObjectiveParcel(board, 1, Patterns.C));
    }

}
