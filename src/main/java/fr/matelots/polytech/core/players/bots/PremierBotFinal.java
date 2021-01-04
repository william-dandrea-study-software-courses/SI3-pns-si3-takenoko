package fr.matelots.polytech.core.players.bots;

import fr.matelots.polytech.core.game.Game;
import fr.matelots.polytech.core.game.goalcards.CardObjective;
import fr.matelots.polytech.core.players.Bot;
import fr.matelots.polytech.core.players.bots.logger.TurnLog;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class PremierBotFinal extends Bot {

    private TurnLog turnLogger;
    private List<Optional<CardObjective>> listOfCurrentsObjectives = new ArrayList<>();

    public PremierBotFinal(Game game, String name) { super(game, name); }
    public PremierBotFinal(Game game) { super(game); }




    @Override
    public void playTurn(TurnLog log) {
        turnLogger = log;
    }




    void inilializeOrUpdateListOfCurrentsObjective() {
        if (listOfCurrentsObjectives.size() > 5) {
            throw new IllegalArgumentException("We can't possessed more than 5 objectives");
        } else {
            while (listOfCurrentsObjectives.size() < 5) {
                listOfCurrentsObjectives.add(pickParcelObjective(turnLogger));
            }
        }
    }


    public List<Optional<CardObjective>> getListOfCurrentsObjectives() {
        return listOfCurrentsObjectives;
    }

    public void setTurnLogger(TurnLog turnLogger) {
        this.turnLogger = turnLogger;
    }

    @Override
    public boolean canPlay() {
        return false;
    }

    @Override
    public String getTurnMessage() {
        return null;
    }
}
