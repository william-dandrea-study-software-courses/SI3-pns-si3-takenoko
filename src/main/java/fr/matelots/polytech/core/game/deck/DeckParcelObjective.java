package fr.matelots.polytech.core.game.deck;

import fr.matelots.polytech.core.game.Board;
import fr.matelots.polytech.core.game.goalcards.CardObjectiveParcel;
import fr.matelots.polytech.core.game.goalcards.pattern.Patterns;
import fr.matelots.polytech.core.game.parcels.BambooColor;

/**
 * @author Alexandre Arcil
 */
public class DeckParcelObjective extends DeckObjective<CardObjectiveParcel> {

    public DeckParcelObjective(Board board) {
        super(board);
    }

    @Override
    protected void fill() {
        this.objectives.add(new CardObjectiveParcel(board, 2, Patterns.TRIANGLE, BambooColor.GREEN, BambooColor.GREEN, BambooColor.GREEN));
        this.objectives.add(new CardObjectiveParcel(board, 3, Patterns.TRIANGLE, BambooColor.YELLOW, BambooColor.YELLOW, BambooColor.YELLOW));
        this.objectives.add(new CardObjectiveParcel(board, 4, Patterns.TRIANGLE, BambooColor.PINK, BambooColor.PINK, BambooColor.PINK));
        this.objectives.add(new CardObjectiveParcel(board, 2, Patterns.LINE, BambooColor.GREEN, BambooColor.GREEN, BambooColor.GREEN));
        this.objectives.add(new CardObjectiveParcel(board, 3, Patterns.LINE, BambooColor.YELLOW, BambooColor.YELLOW, BambooColor.YELLOW));
        this.objectives.add(new CardObjectiveParcel(board, 4, Patterns.LINE, BambooColor.PINK, BambooColor.PINK, BambooColor.PINK));
        this.objectives.add(new CardObjectiveParcel(board, 2, Patterns.C, BambooColor.GREEN, BambooColor.GREEN, BambooColor.GREEN));
        this.objectives.add(new CardObjectiveParcel(board, 3, Patterns.C, BambooColor.YELLOW, BambooColor.YELLOW, BambooColor.YELLOW));
        this.objectives.add(new CardObjectiveParcel(board, 4, Patterns.C, BambooColor.PINK, BambooColor.PINK, BambooColor.PINK));
        this.objectives.add(new CardObjectiveParcel(board, 3, Patterns.RHOMBUS, BambooColor.GREEN, BambooColor.GREEN, BambooColor.GREEN, BambooColor.GREEN));
        this.objectives.add(new CardObjectiveParcel(board, 3, Patterns.RHOMBUS, BambooColor.YELLOW, BambooColor.YELLOW, BambooColor.GREEN, BambooColor.GREEN));
        this.objectives.add(new CardObjectiveParcel(board, 4, Patterns.RHOMBUS, BambooColor.YELLOW, BambooColor.YELLOW, BambooColor.YELLOW, BambooColor.YELLOW));
        this.objectives.add(new CardObjectiveParcel(board, 4, Patterns.RHOMBUS, BambooColor.PINK, BambooColor.PINK, BambooColor.GREEN, BambooColor.GREEN));
        this.objectives.add(new CardObjectiveParcel(board, 5, Patterns.RHOMBUS, BambooColor.PINK, BambooColor.YELLOW, BambooColor.PINK, BambooColor.PINK));
    }

}
