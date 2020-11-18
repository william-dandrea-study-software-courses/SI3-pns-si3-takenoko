package fr.matelots.polytech.core;

/**
 * @author Gabriel Cogne
 */
public class NoParcelLeftToPlaceException extends RuntimeException {
    public NoParcelLeftToPlaceException() {
        super ("Il n'y a plus de parcelle à placer");
    }
}
