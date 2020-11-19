package fr.matelots.polytech.core;

/**
 * Extension of RunTimeException for tell at the player that we placed all the parcels
 * @author Gabriel Cogne
 */
public class NoParcelLeftToPlaceException extends RuntimeException {
    public NoParcelLeftToPlaceException() {
        super ("There is no more parcels to place");
    }
}
