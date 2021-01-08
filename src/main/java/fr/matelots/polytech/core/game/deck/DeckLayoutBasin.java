package fr.matelots.polytech.core.game.deck;

import fr.matelots.polytech.core.game.Board;
import fr.matelots.polytech.core.game.parcels.Layout;

/**
 * Représente la pioche des aménagements bassin
 * @author William d'Andrea
 */
public class DeckLayoutBasin extends Deck<Layout> {

    public DeckLayoutBasin(Board board) {
        super(board);
    }

    @Override
    protected void fill() {
        this.cards.add(Layout.BASIN);
        this.cards.add(Layout.BASIN);
        this.cards.add(Layout.BASIN);
    }
}
