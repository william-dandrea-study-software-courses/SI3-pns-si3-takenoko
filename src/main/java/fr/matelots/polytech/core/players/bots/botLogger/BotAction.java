package fr.matelots.polytech.core.players.bots.botLogger;

import fr.matelots.polytech.core.players.Bot;

class BotAction {
    private Bot bot;
    private BotActionType action;
    private String parameter;

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