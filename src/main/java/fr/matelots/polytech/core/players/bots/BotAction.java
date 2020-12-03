package fr.matelots.polytech.core.players.bots;

import fr.matelots.polytech.core.players.Bot;

public enum BotAction {
    NONE,

    PICK_PARCEL_GOAL,
    PICK_GARDENER_GOAL,
    PICK_PANDA_GOAL,

    PLACE_PARCEL,
    MOVE_GARDENER,
    MOVE_PANDA;


    public String getMessage(Bot bot, Object param) {
        switch(this) {
            case NONE:
                return bot + " jump his turn.";
            case PICK_PARCEL_GOAL:
                return bot + " pick parcel goal : " + param;
            case PICK_PANDA_GOAL:
                return bot + " pick panda goal : " + param;
            case PICK_GARDENER_GOAL:
                return bot + " pick gardener goal : " + param;
            case PLACE_PARCEL:
                return bot + " place parcel : " + param;
            case MOVE_GARDENER:
                return bot + " move gardener : " + param;
            case MOVE_PANDA:
                return bot + " move panda : " + param;
        }

        return "";
    }
}
