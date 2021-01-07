package fr.matelots.polytech.core.game.deck;

import fr.matelots.polytech.core.game.Board;
import fr.matelots.polytech.core.game.parcels.Layout;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class DeckLayout extends Deck{
    public DeckLayout(Board board) {
        super(board);
    }

    @Override
    protected void fill() {


        this.cards.add(Layout.BASIN);
        this.cards.add(Layout.BASIN);
        this.cards.add(Layout.BASIN);
        this.cards.add(Layout.FERTILIZER);
        this.cards.add(Layout.FERTILIZER);
        this.cards.add(Layout.FERTILIZER);
        this.cards.add(Layout.ENCLOSURE);
        this.cards.add(Layout.ENCLOSURE);
        this.cards.add(Layout.ENCLOSURE);
    }
}
