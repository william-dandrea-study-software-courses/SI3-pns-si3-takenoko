package fr.matelots.polytech.core.players.bots.logger;

import fr.matelots.polytech.core.players.Bot;

class BotAction {
    private final Bot bot;
    private final BotActionType action;
    private final String parameter;

    BotAction (Bot bot, BotActionType action, String parameter) {
        this.bot = bot;
        this.action = action;
        this.parameter = parameter;
    }

    @Override
    public String toString() {
        return action.getMessage(bot, parameter);
    }
}