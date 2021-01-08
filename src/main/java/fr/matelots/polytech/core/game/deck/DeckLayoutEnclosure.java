package fr.matelots.polytech.core.game.deck;

import fr.matelots.polytech.core.game.Board;
import fr.matelots.polytech.core.game.parcels.Layout;

/**
 * Représente la pioche des aménagements enclos
 * @author William d'Andrea
 */
public class DeckLayoutEnclosure extends Deck<Layout> {

    public DeckLayoutEnclosure(Board board) {
        super(board);
    }

    @Override
    protected void fill() {
        this.cards.add(Layout.ENCLOSURE);
        this.cards.add(Layout.ENCLOSURE);
        this.cards.add(Layout.ENCLOSURE);
    }
}
