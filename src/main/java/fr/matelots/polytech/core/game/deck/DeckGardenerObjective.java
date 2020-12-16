package fr.matelots.polytech.core.game.deck;

import fr.matelots.polytech.core.game.Board;
import fr.matelots.polytech.core.game.Config;
import fr.matelots.polytech.core.game.goalcards.CardObjectiveGardener;
import fr.matelots.polytech.core.game.parcels.BambooColor;

/**
 * @author Alexandre Arcil
 */
public class DeckGardenerObjective extends Deck<CardObjectiveGardener> {

    public DeckGardenerObjective(Board board) {
        super(board);
    }

    @Override
    protected void fill() {
        for(BambooColor bambooColor : BambooColor.values()) {
            for(int i = 0; i < 4; i++)
                this.cards.add(new CardObjectiveGardener(board, 5, bambooColor, 4, 1));
        }
        this.cards.add(new CardObjectiveGardener(board, 6, BambooColor.PINK, 3, 2));
        this.cards.add(new CardObjectiveGardener(board, 7, BambooColor.YELLOW, 3, 3));
        this.cards.add(new CardObjectiveGardener(board, 8, BambooColor.GREEN, 3, 4));
        if(this.cards.size() != Config.DECK_OBJECTIVE_SIZE)
            throw new RuntimeException("La taille du paquet est de "+this.cards.size()
                    + " alors qu'elle devrait être de "+Config.DECK_OBJECTIVE_SIZE);
    }
}
