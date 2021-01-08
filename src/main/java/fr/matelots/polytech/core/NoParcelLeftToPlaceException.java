package fr.matelots.polytech.core;

/**
 * @author Gabriel Cogne
 */
public class NoParcelLeftToPlaceException extends RuntimeException {
    public NoParcelLeftToPlaceException() {
        super ("There is no more parcels to place");
    }
}
