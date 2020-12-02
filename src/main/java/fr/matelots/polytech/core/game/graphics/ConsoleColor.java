package fr.matelots.polytech.core.game.graphics;

import fr.matelots.polytech.core.game.parcels.BambooColor;

public enum ConsoleColor {
    none("\u001B[0m"),
    green("\u001B[32m"),
    yellow("\u001B[33m"),
    pink("\u001B[35m"),
    blue("\u001B[36m");

    private String colorCode;
    ConsoleColor(String colorCode) {
        this.colorCode = colorCode;
    }

    public static ConsoleColor getFromBambooColor(BambooColor color) {
        switch (color) {
            case pink:
                return ConsoleColor.pink;
            case green:
                return  ConsoleColor.green;
            case yellow:
                return ConsoleColor.yellow;
        }
        return ConsoleColor.none;
    }

    String getColorCode() {
        return colorCode;
    }
}
