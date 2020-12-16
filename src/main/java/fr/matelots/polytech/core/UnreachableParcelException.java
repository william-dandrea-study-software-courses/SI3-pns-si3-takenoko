package fr.matelots.polytech.core;

/**
 * @author Gabriel Cogne
 */
public class UnreachableParcelException  extends RuntimeException {
    public UnreachableParcelException() {
        super ("The position specified is unreachable");
    }
}
