package fr.matelots.polytech.core;

/**
 * @author William d'Andrea
 */
public class NoLayoutLeftException extends RuntimeException {
    public NoLayoutLeftException() {
        super("There is no more layout to place");
    }
}
