package fr.matelots.polytech.core;

/**
 * @author Alexandre Arcil
 */
public class PickDeckEmptyException extends RuntimeException {

    public PickDeckEmptyException() {
        super("Impossible de tirer une carte, le deck est vide");
    }
}
