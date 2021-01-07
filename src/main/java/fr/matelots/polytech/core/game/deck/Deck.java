package fr.matelots.polytech.core.game.deck;

import fr.matelots.polytech.core.PickDeckEmptyException;
import fr.matelots.polytech.core.game.Board;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * This method represent the deck of each objectives
 * @author Alexandre Arcil
 */
public abstract class Deck<T> {

    protected final List<T> cards;
    protected final Board board;

    public Deck(Board board) {
        this.board = board;
        this.cards = new ArrayList<>();
        this.fill();
        Collections.shuffle(this.cards);
    }

    /**
     * It is in this method that the different objectives will have to be fulfilled:  : {@link #cards}
     */
    protected abstract void fill();

    /**
     * Draws the card at the top of the deck.
     * @return a card
     * @throws PickDeckEmptyException if the deck is empty
     */
    public T pick() {
        if(this.canPick())
            return this.cards.remove(0);
        else
            throw new PickDeckEmptyException();
    }

    /**
     * Lets you know if the deck contains cards. It must be called before calling : {@link #pick()}
     * @return true if the deck is empty, false otherwise
     */
    public boolean canPick() {
        return !this.cards.isEmpty();
    }

}
