package fr.matelots.polytech.core.players.bots;

import fr.matelots.polytech.core.game.Game;
import fr.matelots.polytech.core.game.Weather;
import fr.matelots.polytech.core.game.goalcards.CardObjective;
import fr.matelots.polytech.core.game.goalcards.CardObjectiveParcel;
import fr.matelots.polytech.core.game.goalcards.pattern.PositionColored;
import fr.matelots.polytech.core.game.parcels.Parcel;
import fr.matelots.polytech.core.players.Bot;
import fr.matelots.polytech.core.players.bots.logger.BotActionType;
import fr.matelots.polytech.core.players.bots.logger.TurnLog;

import java.util.*;

public class RushParcelBot extends Bot {

    private TurnLog turnLogger;
    private List<Optional<CardObjective>> listOfCurrentsObjectives;
    private CardObjectiveParcel cardWeActuallyTryToResolve;
    private int canPutMoreParcel = 0;
    private int inc = 0;

    private Random random = new Random();

    public RushParcelBot(Game game, String name) {
        super(game, name);
        cardWeActuallyTryToResolve = null;
        listOfCurrentsObjectives = new ArrayList<Optional<CardObjective>>();
    }
    public RushParcelBot(Game game) {
        super(game);
        cardWeActuallyTryToResolve = null;
        listOfCurrentsObjectives = new ArrayList<Optional<CardObjective>>();
    }




    @Override
    public void playTurn(TurnLog log, Weather weatherCard) {
        super.playTurn(log, weatherCard);
        inc++;

        setCurrentNumberOfAction(0);

        turnLogger = log;
        // En premier lieu, on vérifie / initialise / met a jour la liste d'objectifs si celle-ci n'est pas rempli
        if (listOfCurrentsObjectives.size() != 5) {
            inilializeOrUpdateListOfCurrentsObjective2();
        }

        easiestObjectiveToResolve2();

        Set<PositionColored> missingsPositions = recoverTheMissingsPositionsToCompleteForParcelObjective(cardWeActuallyTryToResolve);

        if (missingsPositions != null) {
            for (PositionColored positionColored : missingsPositions) {
                placeParcel(positionColored.getPosition(), positionColored.getColor(), turnLogger);
            }
        }

        if (currentNumberOfAction == 0) {
            placeAnParcelAnywhere(turnLogger);
        }
    }


    /**
     * This method affect at cardWeActuallyTryToResolve the card from the 5 objectives we have. This method select
     * this objective who have the less missing position to complete
     */



    void easiestObjectiveToResolve2() {
        boolean weDontArrive = true;
        int min = 10;
        for (CardObjective cardOp: individualBoard.getUnfinishedParcelObjectives()) {

            CardObjectiveParcel card = (CardObjectiveParcel) cardOp;
            cardOp.verify();
            if (card.getMissingPositionsToComplete().size() <= min) {
                min = card.getMissingPositionsToComplete().size();
            }

        }

        for (CardObjective cardOp: individualBoard.getUnfinishedParcelObjectives()) {

            CardObjectiveParcel card = (CardObjectiveParcel) cardOp;
            if (card.getMissingPositionsToComplete().size() == min) {
                cardWeActuallyTryToResolve = card;
            } else {
                weDontArrive = false;
            }

        }

        if (weDontArrive) {
            pickParcelObjective(turnLogger);
            //cardWeActuallyTryToResolve = (CardObjectiveParcel) individualBoard.getUnfinishedParcelObjectives().get(0);
        }
    }

    void inilializeOrUpdateListOfCurrentsObjective2() {

        BotActionType lastAction = getLastAction();
        if (lastAction != null)
        {
            if (lastAction.equals(BotActionType.PICK_PARCEL_GOAL)) {
                // We pick a new parcel
                List<Parcel> availableParcels = board.pickParcels();
                if (!availableParcels.isEmpty() && availableParcels != null) {
                    placeAnParcelAnywhere(turnLogger, availableParcels.get(random.nextInt(availableParcels.size())));
                }
            } else {
                pickParcelObjective(turnLogger);
            }
        } else {
            pickParcelObjective(turnLogger);
        }


        for (int i = 0; i < 5; i++) {
            if (individualBoard.getUnfinishedParcelObjectives().size() < 5) {
                pickParcelObjective(turnLogger);
            }
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
                            card.get().verify();
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
        if (getBoard().getParcelCount() <= 26 && inc <= 200) {
            return true;
        } else {
            return false;
        }
    }

}
