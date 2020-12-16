package fr.matelots.polytech.core.game.deck;

import fr.matelots.polytech.core.game.Board;
import fr.matelots.polytech.core.game.Config;
import fr.matelots.polytech.core.game.parcels.BambooColor;
import fr.matelots.polytech.core.game.parcels.BambooPlantation;
import fr.matelots.polytech.core.game.parcels.Layout;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Alexandre Arcil
 */
public class DeckParcelTest {

    private DeckParcel deck;

    @BeforeEach
    public void init() {
        this.deck = new DeckParcel(new Board());
    }

    @Test @DisplayName("Le paquet contient bien 27 cartes")
    public void containTwentySevenCards() {
        for(int i = 0; i < Config.DECK_PARCEL_SIZE; i++)
            assertDoesNotThrow(() -> this.deck.pick());
        assertFalse(this.deck.canPick());
    }

    @Test @DisplayName("Contient bien les 6 cartes vertes sans aménagements")
    public void containGreenCardsNoLayout() {
        this.containCards(new BambooPlantation(BambooColor.GREEN), 6);
    }

    @Test @DisplayName("Contient bien les 2 cartes vertes avec l'aménagement 'Bassin'")
    public void containGreenCardsWithBasinLayout() {
        this.containCards(new BambooPlantation(BambooColor.GREEN, Layout.BASIN), 2);
    }

    @Test @DisplayName("Contient bien les 2 cartes vertes avec l'aménagement 'Enclos'")
    public void containGreenCardsWithEnclosureLayout() {
        this.containCards(new BambooPlantation(BambooColor.GREEN, Layout.ENCLOSURE), 2);
    }

    @Test @DisplayName("Contient bien les 1 carte verte avec l'aménagement 'Engrais'")
    public void containGreenCardsWithFertilizerLayout() {
        this.containCards(new BambooPlantation(BambooColor.GREEN, Layout.FERTILIZER), 1);
    }

    @Test @DisplayName("Contient bien les 6 cartes jaunes sans aménagements")
    public void containYellowCardsNoLayout() {
        this.containCards(new BambooPlantation(BambooColor.YELLOW), 6);
    }

    @Test @DisplayName("Contient bien les 2 cartes jaunes avec l'aménagement 'Bassin'")
    public void containYellowCardsWithBasinLayout() {
        this.containCards(new BambooPlantation(BambooColor.YELLOW, Layout.BASIN), 1);
    }

    @Test @DisplayName("Contient bien les 2 cartes jaunes avec l'aménagement 'Enclos'")
    public void containYellowCardsWithEnclosureLayout() {
        this.containCards(new BambooPlantation(BambooColor.YELLOW, Layout.ENCLOSURE), 1);
    }

    @Test @DisplayName("Contient bien les 1 carte jaunes avec l'aménagement 'Engrais'")
    public void containYellowCardsWithFertilizerLayout() {
        this.containCards(new BambooPlantation(BambooColor.YELLOW, Layout.FERTILIZER), 1);
    }

    @Test @DisplayName("Contient bien les 6 cartes roses sans aménagements")
    public void containPinkCardsNoLayout() {
        this.containCards(new BambooPlantation(BambooColor.PINK), 4);
    }

    @Test @DisplayName("Contient bien les 1 carte rose avec l'aménagement 'Bassin'")
    public void containPinkCardsWithBasinLayout() {
        this.containCards(new BambooPlantation(BambooColor.PINK, Layout.BASIN), 1);
    }

    @Test @DisplayName("Contient bien les 1 carte rose avec l'aménagement 'Enclos'")
    public void containPinkCardsWithEnclosureLayout() {
        this.containCards(new BambooPlantation(BambooColor.PINK, Layout.ENCLOSURE), 1);
    }

    @Test @DisplayName("Contient bien les 1 carte rose avec l'aménagement 'Engrais'")
    public void containPinkCardsWithFertilizerLayout() {
        this.containCards(new BambooPlantation(BambooColor.PINK, Layout.FERTILIZER), 1);
    }

    private void containCards(BambooPlantation card, int count) {
        int found = 0;
        for(int i = 0; i < Config.DECK_PARCEL_SIZE; i++) {
            BambooPlantation pick = this.deck.pick();
            if(pick.equals(card))
                found++;
        }
        assertEquals(count, found);
    }

}
