package fr.matelots.polytech.core.players.bots.botLogger;

import fr.matelots.polytech.core.players.Bot;

import java.util.ArrayList;
import java.util.List;

public class TurnLog {
    private final Bot bot;
    private final List<BotAction> actions;

    public TurnLog(Bot bot) {
        this.bot = bot;
        actions = new ArrayList<>();
    }

    public void addAction(BotActionType action, String parameter) {
        this.actions.add(new BotAction(bot, action, parameter));
    }

    @Override
    public String toString() {
        if(actions.size() == 0) return "";
        StringBuilder sb = new StringBuilder();
        for(var action : actions) {
            sb.append(action).append("\n");
        }
        if (sb.length() > 0) {
            sb.deleteCharAt(sb.length() - 1);
        }
        return sb.toString();
    }
}