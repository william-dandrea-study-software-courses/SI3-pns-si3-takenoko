package fr.matelots.polytech.core.players.bots;

import fr.matelots.polytech.core.game.Game;
import fr.matelots.polytech.core.game.goalcards.CardObjective;
import fr.matelots.polytech.core.game.goalcards.CardObjectiveParcel;
import fr.matelots.polytech.core.players.Bot;
import fr.matelots.polytech.core.players.bots.logger.TurnLog;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class PremierBotFinal extends Bot {

    private TurnLog turnLogger;
    private List<Optional<CardObjective>> listOfCurrentsObjectives;

    public PremierBotFinal(Game game, String name) { super(game, name); }
    public PremierBotFinal(Game game) {
        super(game);
        listOfCurrentsObjectives = new ArrayList<>();
    }




    @Override
    public void playTurn(TurnLog log) {
        turnLogger = log;

    }


    /**
     * This method update or initialize all the objectives from the static list.
     * @return True if the 5 objectives are initialized, else false
     */
    void inilializeOrUpdateListOfCurrentsObjective() {

        for (Optional<CardObjective> card : listOfCurrentsObjectives) {

            if (card.get().verify()) {
                listOfCurrentsObjectives.remove(card);
            }

        }


        if (listOfCurrentsObjectives.size() > 5) {
            throw new IllegalArgumentException("We can't possessed more than 5 objectives");
        }

        if (listOfCurrentsObjectives.size() != 5) {

            for (int i = 0; i < 5; i++) {
                if (listOfCurrentsObjectives.size() < 5) {
                    Optional<CardObjective> card = pickParcelObjective(turnLogger);
                    if (card.isPresent()) {
                        listOfCurrentsObjectives.add(card);
                    }
                }
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
