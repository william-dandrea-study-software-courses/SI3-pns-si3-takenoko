package fr.matelots.polytech.core.game;

import fr.matelots.polytech.core.PickDeckEmptyException;
import fr.matelots.polytech.core.game.deck.DeckParcelObjective;
import fr.matelots.polytech.core.game.goalcards.CardObjectiveParcel;
import fr.matelots.polytech.core.game.goalcards.pattern.Patterns;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Alexandre Arcil
 */
public class DeckParcelObjectiveTest {

    private DeckParcelObjective deck;
    private Board board;

    //Test de DeckObjective
    @BeforeEach
    public void init() {
        this.board = new Board();
        this.deck = new DeckParcelObjective(this.board);
    }

    @Test @DisplayName("Peut-on tirer une carte")
    public void canPickTestTest() {
        assertTrue(this.deck.canPick());
    }

    @Test @DisplayName("Tirer une carte")
    public void pickSuccessTest() {
        assertDoesNotThrow(() -> this.deck.pick());
    }

    @Test @DisplayName("Tirer une carte non null")
    public void pickNotNullTest() {
        assertNotNull(this.deck.pick());
    }

    @Test @DisplayName("Aucune cartes null")
    public void noNullCardTest() {
        for (int i = 0; i < Config.DECK_SIZE; i++)
            assertNotNull(this.deck.pick());
    }

    @Test @DisplayName("Peut-on tirer toute les cartes")
    public void canPickAllCardTest() {
        for (int i = 0; i < Config.DECK_SIZE; i++) {
            assertTrue(this.deck.canPick());
            this.deck.pick();
        }
    }

    @Test @DisplayName("Peut-on tirer une carte d'un paquet vide")
    public void canPickEmptyDeckTest() {
        for (int i = 0; i < Config.DECK_SIZE; i++)
            this.deck.pick();
        assertFalse(this.deck.canPick());
    }

    @Test @DisplayName("Tirer une carte d'un paquet vide")
    public void pickEmptyDeckTest() {
        for (int i = 0; i < Config.DECK_SIZE; i++)
            this.deck.pick();
        assertThrows(PickDeckEmptyException.class, () -> this.deck.pick());
    }

    //Test de DeckParcelObjective
    @Test @DisplayName("taille respecté")
    public void sizeCheck() {
        for(int i = 0; i < Config.DECK_SIZE; i++)
            assertDoesNotThrow(() -> this.deck.pick());
        assertFalse(this.deck.canPick());
    }

    @Test @DisplayName("contient 3 cartes de la forme 'triangle'")
    public void containThreeTrianglePatternCards() {
        CardObjectiveParcel card = new CardObjectiveParcel(this.board, 1, Patterns.TRIANGLE);
        this.containCards(card, 3);
    }

    @Test @DisplayName("contient 3 cartes de la forme 'C'")
    public void containThreeCPatternCards() {
        CardObjectiveParcel card = new CardObjectiveParcel(this.board, 1, Patterns.C);
        this.containCards(card, 3);
    }

    @Test @DisplayName("contient 6 cartes de la forme 'Losange'")
    public void containThreeRhombusPatternCards() {
        CardObjectiveParcel card = new CardObjectiveParcel(this.board, 1, Patterns.RHOMBUS);
        this.containCards(card, 6);
    }

    @Test @DisplayName("contient 3 cartes de la forme 'Ligne'")
    public void containThreeLinePatternCards() {
        CardObjectiveParcel card = new CardObjectiveParcel(this.board, 1, Patterns.LINE);
        this.containCards(card, 3);
    }

    private void containCards(CardObjectiveParcel card, int count) {
        int found = 0;
        for(int i = 0; i < Config.DECK_SIZE; i++) {
            CardObjectiveParcel pick = this.deck.pick();
            if(pick.equals(card))
                found++;
        }
        assertEquals(count, found);
    }


}
