package fr.matelots.polytech.core.game.deck;

import fr.matelots.polytech.core.game.Board;
import fr.matelots.polytech.core.game.parcels.BambooColor;
import fr.matelots.polytech.core.game.parcels.BambooPlantation;
import fr.matelots.polytech.core.game.parcels.Layout;

/**
 * This class represent the deck of the parcels :
 * @author Alexandre Arcil
 */
public class DeckParcel extends Deck<BambooPlantation> {

    public DeckParcel(Board board) {
        super(board);
    }

    @Override
    protected void fill() {
        for(int i = 0; i < 6; i++)
            this.cards.add(new BambooPlantation(BambooColor.GREEN));
        this.cards.add(new BambooPlantation(BambooColor.GREEN, Layout.BASIN));
        this.cards.add(new BambooPlantation(BambooColor.GREEN, Layout.BASIN));
        this.cards.add(new BambooPlantation(BambooColor.GREEN, Layout.ENCLOSURE));
        this.cards.add(new BambooPlantation(BambooColor.GREEN, Layout.ENCLOSURE));
        this.cards.add(new BambooPlantation(BambooColor.GREEN, Layout.FERTILIZER));
        for(int i = 0; i < 6; i++)
            this.cards.add(new BambooPlantation(BambooColor.YELLOW));
        this.cards.add(new BambooPlantation(BambooColor.YELLOW, Layout.BASIN));
        this.cards.add(new BambooPlantation(BambooColor.YELLOW, Layout.ENCLOSURE));
        this.cards.add(new BambooPlantation(BambooColor.YELLOW, Layout.FERTILIZER));
        for(int i = 0; i < 4; i++)
            this.cards.add(new BambooPlantation(BambooColor.PINK));
        this.cards.add(new BambooPlantation(BambooColor.PINK, Layout.BASIN));
        this.cards.add(new BambooPlantation(BambooColor.PINK, Layout.ENCLOSURE));
        this.cards.add(new BambooPlantation(BambooColor.PINK, Layout.FERTILIZER));

    }
}
