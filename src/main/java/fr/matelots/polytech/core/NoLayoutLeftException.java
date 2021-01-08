package fr.matelots.polytech.core;

public class NoLayoutLeftException extends RuntimeException {
    public NoLayoutLeftException() {
        super("There is no more layoutss to place");
    }
}
