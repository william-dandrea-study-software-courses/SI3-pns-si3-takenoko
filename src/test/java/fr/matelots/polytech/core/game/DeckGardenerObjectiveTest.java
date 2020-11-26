package fr.matelots.polytech.core.game;

import fr.matelots.polytech.core.game.deck.DeckGardenerObjective;
import fr.matelots.polytech.core.game.goalcards.CardObjectiveGardener;
import fr.matelots.polytech.core.game.parcels.BambooColor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;


/**
 * @author Alexandre Arcil
 */
public class DeckGardenerObjectiveTest {

    private DeckGardenerObjective deck;
    private Board board;

    @BeforeEach
    public void init() {
        this.board = new Board();
        this.deck = new DeckGardenerObjective(this.board);
    }

    @Test @DisplayName("taille respecté")
    public void sizeCheck() {
        for(int i = 0; i < Config.DECK_SIZE; i++)
            assertDoesNotThrow(() -> this.deck.pick());
        assertFalse(this.deck.canPick());
    }

    @Test @DisplayName("contient 4 cartes '1 bamboo vert de 4 de hauteur'")
    public void containGreenBambooSizeFour() {
        CardObjectiveGardener card = new CardObjectiveGardener(this.board, 5, BambooColor.green, 4, 1);
        this.containCards(card, 4);
    }

    @Test @DisplayName("contient 4 cartes '1 bamboo jaune de 4 de hauteur'")
    public void containYellowBambooSizeFour() {
        CardObjectiveGardener card = new CardObjectiveGardener(this.board, 5, BambooColor.yellow, 4, 1);
        this.containCards(card, 4);
    }

    @Test @DisplayName("contient 4 cartes '1 bamboo rose de 4 de hauteur'")
    public void containPinkBambooSizeFour() {
        CardObjectiveGardener card = new CardObjectiveGardener(this.board, 5, BambooColor.pink, 4, 1);
        this.containCards(card, 4);
    }

    @Test @DisplayName("contient 1 cartes '4 bamboo vert de 3 de hauteur'")
    public void containFourGreenBambooSizeThree() {
        CardObjectiveGardener card = new CardObjectiveGardener(this.board, 8, BambooColor.green, 3, 4);
        this.containCards(card, 1);
    }

    @Test @DisplayName("contient 1 cartes '3 bamboo jaune de 3 de hauteur'")
    public void containThreeYellowBambooSizeThree() {
        CardObjectiveGardener card = new CardObjectiveGardener(this.board, 7, BambooColor.yellow, 3, 3);
        this.containCards(card, 1);
    }

    @Test @DisplayName("contient 1 cartes '2 bamboo rose de 3 de hauteur'")
    public void containTwoPinkBambooSizeThree() {
        CardObjectiveGardener card = new CardObjectiveGardener(this.board, 6, BambooColor.pink, 3, 2);
        this.containCards(card, 1);
    }

    private void containCards(CardObjectiveGardener card, int count) {
        int found = 0;
        for(int i = 0; i < Config.DECK_SIZE; i++) {
            CardObjectiveGardener pick = this.deck.pick();
            if(pick.equals(card))
                found++;
        }
        assertEquals(count, found);
    }


}
