package fr.matelots.polytech.core;

/**
 * @author Alexandre Arcil
 */
public class PickDeckEmptyException extends RuntimeException {

    public PickDeckEmptyException() {
        super("impossible de tirer une carte, le deck est vide");
    }
}
