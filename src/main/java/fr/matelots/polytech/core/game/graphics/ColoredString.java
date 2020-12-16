package fr.matelots.polytech.core.game.graphics;

public class ColoredString {

    private ConsoleColor color;
    private final String content;

    ColoredString(ConsoleColor color, String content) {
        this.color = color;
        this.content = content;
    }

    @Override
    public String toString() {
        return color.getColorCode() + content;
    }

    public void setColor(ConsoleColor newColor) {
        this.color = newColor;
    }

    public String getContent() {
        return content;
    }
}
