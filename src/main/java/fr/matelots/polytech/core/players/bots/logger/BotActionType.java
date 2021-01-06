package fr.matelots.polytech.core.players.bots.logger;

import fr.matelots.polytech.core.players.Bot;

public enum BotActionType {
    NONE,

    PICK_PARCEL_GOAL,
    PICK_GARDENER_GOAL,
    PICK_PANDA_GOAL,

    PLACE_PARCEL,
    MOVE_GARDENER,
    MOVE_PANDA,

    PLACE_IRRIGATION,
    PLACE_LAYOUT;


    public String getMessage(Bot bot, Object param) {
        switch(this) {
            case NONE:
                return bot.getName() + " jump his turn.";
            case PICK_PARCEL_GOAL:
                return bot.getName() + " pick parcel goal : " + param;
            case PICK_PANDA_GOAL:
                return bot.getName() + " pick panda goal : " + param;
            case PICK_GARDENER_GOAL:
                return bot.getName() + " pick gardener goal : " + param;
            case PLACE_PARCEL:
                return bot.getName() + " place parcel : " + param;
            case MOVE_GARDENER:
                return bot.getName() + " move gardener : " + param;
            case MOVE_PANDA:
                return bot.getName() + " move panda : " + param;
            case PLACE_IRRIGATION:
                return bot.getName() + " place an irrigation on the edge " + param;
            case PLACE_LAYOUT:
                return bot.getName() + " place a layout " + param;
        }

        return "";
    }
}
