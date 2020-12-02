package fr.matelots.polytech.core.game.graphics;

public class ColoredCharacters {

    private final ConsoleColor color;
    private final char character;

    ColoredCharacters(ConsoleColor color, char character) {
        this.color = color;
        this.character = character;
    }

    @Override
    public String toString() {
        return color.getColorCode() + character;
    }
}
