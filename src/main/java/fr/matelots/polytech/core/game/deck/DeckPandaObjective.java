package fr.matelots.polytech.core.game.deck;

import fr.matelots.polytech.core.game.Board;
import fr.matelots.polytech.core.game.Config;
import fr.matelots.polytech.core.game.goalcards.CardObjectivePanda;

/**
 * This class represent the deck of panda objective who is composed of 15 objectives :
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

        if (this.cards.size() != Config.DECK_OBJECTIVE_SIZE)
            throw new RuntimeException("The deck size is " + this.cards.size()
                    + " when it should be " + Config.DECK_OBJECTIVE_SIZE);
    }
}
