package fr.matelots.polytech.core.game.graphics;

public class ColoredCharacters {

    private ConsoleColor color;
    private char character;

    ColoredCharacters(ConsoleColor color, char character) {
        this.color = color;
        this.character = character;
    }

    ConsoleColor getColor() {
        return color;
    }

    char getCharacter() {
        return character;
    }

    @Override
    public String toString() {
        return color.getColorCode() + character;
    }
}
