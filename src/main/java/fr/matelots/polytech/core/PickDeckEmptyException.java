package fr.matelots.polytech.core;

/**
 * Extension of RunTimeException for tell at the player that the deck is empty, so we can't drew a card
 * @author Alexandre Arcil
 */
public class PickDeckEmptyException extends RuntimeException {

    public PickDeckEmptyException() {
        super("Game Error : Impossible to drew a card because the deck is empty");
    }
}
