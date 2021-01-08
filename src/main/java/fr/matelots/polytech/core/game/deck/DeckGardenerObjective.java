package fr.matelots.polytech.core.game.deck;

import fr.matelots.polytech.core.game.Board;
import fr.matelots.polytech.core.game.goalcards.CardObjectiveGardener;
import fr.matelots.polytech.core.game.parcels.BambooColor;
import fr.matelots.polytech.core.game.parcels.Layout;

/**
 * This class represent the deck of gardener objective who is composed of 15 objectives :
 * @author Alexandre Arcil
 */
public class DeckGardenerObjective extends Deck<CardObjectiveGardener> {

    public DeckGardenerObjective(Board board) {
        super(board);
    }

    @Override
    protected void fill() {
        this.cards.add(new CardObjectiveGardener(board, 4, BambooColor.YELLOW, 4, 1, Layout.FERTILIZER));
        this.cards.add(new CardObjectiveGardener(board, 5, BambooColor.YELLOW, 4, 1, Layout.ENCLOSURE));
        this.cards.add(new CardObjectiveGardener(board, 5, BambooColor.YELLOW, 4, 1, Layout.BASIN));
        this.cards.add(new CardObjectiveGardener(board, 6, BambooColor.YELLOW, 4, 1, null));
        this.cards.add(new CardObjectiveGardener(board, 7, BambooColor.YELLOW, 3, 3, null));

        this.cards.add(new CardObjectiveGardener(board, 3, BambooColor.GREEN, 4, 1, Layout.FERTILIZER));
        this.cards.add(new CardObjectiveGardener(board, 4, BambooColor.GREEN, 4, 1, Layout.ENCLOSURE));
        this.cards.add(new CardObjectiveGardener(board, 4, BambooColor.GREEN, 4, 1, Layout.BASIN));
        this.cards.add(new CardObjectiveGardener(board, 5, BambooColor.GREEN, 4, 1, null));
        this.cards.add(new CardObjectiveGardener(board, 8, BambooColor.GREEN, 3, 4, null));

        this.cards.add(new CardObjectiveGardener(board, 5, BambooColor.PINK, 4, 1, Layout.FERTILIZER));
        this.cards.add(new CardObjectiveGardener(board, 6, BambooColor.PINK, 4, 1, Layout.ENCLOSURE));
        this.cards.add(new CardObjectiveGardener(board, 6, BambooColor.PINK, 4, 1, Layout.BASIN));
        this.cards.add(new CardObjectiveGardener(board, 6, BambooColor.PINK, 3, 2, null));
        this.cards.add(new CardObjectiveGardener(board, 7, BambooColor.PINK, 4, 1, null));
    }
}
