package fr.matelots.polytech.core.game;

import fr.matelots.polytech.core.game.deck.DeckPandaObjective;
import fr.matelots.polytech.core.game.goalcards.CardObjectivePanda;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Alexandre Arcil
 */
public class DeckPandaObjectiveTest {

    private DeckPandaObjective deck;

    @Test
    @DisplayName("Le paquet contient bien le bon nombre de carte")
    public void fillSuccessTest() {
        assertDoesNotThrow(() -> new DeckPandaObjective(new Board()));
    }

    @BeforeEach
    public void init() {
        this.deck = new DeckPandaObjective(new Board());
    }

    @Test @DisplayName("taille respecté")
    public void sizeCheck() {
        for(int i = 0; i < Config.DECK_SIZE; i++)
            assertDoesNotThrow(() -> this.deck.pick());
        assertFalse(this.deck.canPick());
    }

    @Test @DisplayName("contient 5 cartes '2 bamboo verts'")
    public void containTwoGreenBamboo() {
        CardObjectivePanda card = new CardObjectivePanda(3, 2, 0, 0);
        this.containCards(card, 5);
    }

    @Test @DisplayName("contient 5 cartes '2 bamboo roses'")
    public void containTwoPinkBamboo() {
        CardObjectivePanda card = new CardObjectivePanda(5, 0, 2, 0);
        this.containCards(card, 3);
    }
    @Test @DisplayName("contient 5 cartes '2 bamboo yellow'")
    public void containTwoYellowBamboo() {
        CardObjectivePanda card = new CardObjectivePanda(4, 0, 0, 2);
        this.containCards(card, 4);
    }
    @Test @DisplayName("contient 5 cartes '1 bamboo de toute les couleurs'")
    public void containEveryColorBamboo() {
        CardObjectivePanda card = new CardObjectivePanda(6, 1, 1, 1);
        this.containCards(card, 3);
    }

    private void containCards(CardObjectivePanda card, int count) {
        int found = 0;
        for(int i = 0; i < Config.DECK_SIZE; i++) {
            CardObjectivePanda pick = this.deck.pick();
            if(pick.equals(card))
                found++;
        }
        assertEquals(count, found);
    }

}
