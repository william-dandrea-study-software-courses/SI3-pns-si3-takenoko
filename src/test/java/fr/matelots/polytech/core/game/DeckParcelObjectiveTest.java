package fr.matelots.polytech.core.game;

import fr.matelots.polytech.core.PickDeckEmptyException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Alexandre Arcil
 */
public class DeckParcelObjectiveTest {

    private DeckParcelObjective deck;

    @BeforeEach
    public void init() {
        this.deck = new DeckParcelObjective(new Board());
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

}
