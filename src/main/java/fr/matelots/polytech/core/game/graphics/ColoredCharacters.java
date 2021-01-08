package fr.matelots.polytech.core.game.graphics;

public class ColoredCharacters extends ColoredString {

    ColoredCharacters(ConsoleColor color, char content) {
        super(color, "" + content);
    }

}
