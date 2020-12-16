package fr.matelots.polytech.core.game.deck;

import fr.matelots.polytech.core.game.Board;
import fr.matelots.polytech.core.game.goalcards.CardObjectivePanda;

/**
 * @author Alexandre Arcil
 */
public class DeckPandaObjective extends Deck<CardObjectivePanda> {

    public DeckPandaObjective(Board board) {
        super(board);
    }

    @Override
    protected void fill() {
        for (int i = 0; i < 5; i++)
            this.cards.add(new CardObjectivePanda(3, 2, 0, 0));
        for (int i = 0; i < 4; i++)
            this.cards.add(new CardObjectivePanda(4, 0, 0, 2));
        for (int i = 0; i < 3; i++)
            this.cards.add(new CardObjectivePanda(5, 0, 2, 0));
        for (int i = 0; i < 3; i++)
            this.cards.add(new CardObjectivePanda(6, 1, 1, 1));
    }
}
