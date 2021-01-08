package fr.matelots.polytech.core.game.graphics;

import fr.matelots.polytech.core.game.parcels.BambooColor;

public enum ConsoleColor {

    NONE("\u001B[0m"),
    WHITE("\u001B[37m"),
    GREEN("\u001B[32m"),
    YELLOW("\u001B[33m"),
    PINK("\u001B[35m"),
    BLUE("\u001B[36m");

    private final String colorCode;
    ConsoleColor(String colorCode) {
        this.colorCode = colorCode;
    }

    public static ConsoleColor getFromBambooColor(BambooColor color) {
        switch (color) {
            case PINK:
                return ConsoleColor.PINK;
            case GREEN:
                return  ConsoleColor.GREEN;
            case YELLOW:
                return ConsoleColor.YELLOW;
        }
        return ConsoleColor.NONE;
    }

    String getColorCode() {
        return colorCode;
    }

}
