package fr.matelots.polytech.core.game.deck;

import fr.matelots.polytech.core.game.Board;
import fr.matelots.polytech.core.game.parcels.Layout;

public class DeckLayoutFertilizer extends Deck<Layout> {

    public DeckLayoutFertilizer(Board board) {
        super(board);
    }

    @Override
    protected void fill() {
        this.cards.add(Layout.FERTILIZER);
        this.cards.add(Layout.FERTILIZER);
        this.cards.add(Layout.FERTILIZER);
    }

}
