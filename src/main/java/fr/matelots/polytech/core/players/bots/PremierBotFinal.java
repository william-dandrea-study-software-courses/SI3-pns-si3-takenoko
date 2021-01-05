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
    private CardObjectiveParcel cardWeActuallyTryToResolve;
    private int canPutMoreParcel = 0;

    public PremierBotFinal(Game game, String name) { super(game, name); }
    public PremierBotFinal(Game game) {
        super(game);
        listOfCurrentsObjectives = new ArrayList<>();
        cardWeActuallyTryToResolve = null;
    }




    @Override
    public void playTurn(TurnLog log) {
        turnLogger = log;

        // En premier lieu, on vérifie / initialise / met a jour la liste d'objectifs si celle-ci n'est pas rempli
        if (listOfCurrentsObjectives.size() != 5) {
            inilializeOrUpdateListOfCurrentsObjective();
        }

        easiestObjectiveToResolve();



    }


    /**
     * This method affect at cardWeActuallyTryToResolve the card from the 5 objectives we have. This method select
     * this objective who have the less missing position to complete
     */
    void easiestObjectiveToResolve() {
        boolean weDontArrive = true;
        int min = 5;
        for (Optional<CardObjective> cardOp: listOfCurrentsObjectives) {
            if (cardOp.isPresent()) {
                CardObjectiveParcel card = (CardObjectiveParcel) cardOp.get();
                if (card.getMissingPositionsToComplete().size() <= min) {
                    min = card.getMissingPositionsToComplete().size();
                }
            }
        }

        for (Optional<CardObjective> cardOp: listOfCurrentsObjectives) {
            if (cardOp.isPresent()) {
                CardObjectiveParcel card = (CardObjectiveParcel) cardOp.get();
                if (card.getMissingPositionsToComplete().size() == min) {
                    cardWeActuallyTryToResolve = card;
                } else {
                    weDontArrive = false;
                }
            }
        }

        if (weDontArrive) {
            cardWeActuallyTryToResolve = (CardObjectiveParcel) listOfCurrentsObjectives.get(0).get();
        }
    }


    /**
     * This method update or initialize all the objectives from the static list.
     * @return True if the 5 objectives are initialized, else false
     */
    void inilializeOrUpdateListOfCurrentsObjective() {

        if (canPutMoreParcel < 4) {

            canPutMoreParcel = 0;
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
                        } else {
                            canPutMoreParcel++;
                        }
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
