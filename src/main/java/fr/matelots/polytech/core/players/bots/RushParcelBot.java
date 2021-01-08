package fr.matelots.polytech.core.players.bots;

import fr.matelots.polytech.core.game.Config;
import fr.matelots.polytech.core.game.Game;
import fr.matelots.polytech.core.game.Weather;
import fr.matelots.polytech.core.game.goalcards.CardObjective;
import fr.matelots.polytech.core.game.goalcards.CardObjectiveParcel;
import fr.matelots.polytech.core.game.goalcards.pattern.PositionColored;
import fr.matelots.polytech.core.game.parcels.Parcel;
import fr.matelots.polytech.core.players.Bot;
import fr.matelots.polytech.core.players.MarganIA;
import fr.matelots.polytech.core.players.bots.logger.BotActionType;
import fr.matelots.polytech.core.players.bots.logger.TurnLog;

import java.util.*;

public class RushParcelBot extends Bot {

    private TurnLog turnLogger;
    private CardObjectiveParcel cardWeActuallyTryToResolve;

    private Random random = new Random();

    public RushParcelBot(Game game, String name) {
        super(game, name);
        cardWeActuallyTryToResolve = null;
    }
    public RushParcelBot(Game game) {
        super(game);
        cardWeActuallyTryToResolve = null;
    }




    @Override
    public void playTurn(TurnLog log, Weather weatherCard) {
        super.playTurn(log, weatherCard);

        turnLogger = log;
        // En premier lieu, on vérifie / initialise / met a jour la liste d'objectifs si celle-ci n'est pas rempli
        if (getIndividualBoard().getUnfinishedParcelObjectives().size() != 5) {
            inilializeOrUpdateListOfCurrentsObjective2();
        }

        easiestObjectiveToResolve2();

        //Set<PositionColored> missingsPositions = recoverTheMissingsPositionsToCompleteForParcelObjective(cardWeActuallyTryToResolve);

        if (cardWeActuallyTryToResolve != null) {
            PositionColored missingPosition = MarganIA.findTheBestPlaceToPlaceAnParcel(cardWeActuallyTryToResolve, getBoard());
            if (missingPosition != null && getBoard().getPositions().size() < Config.DECK_PARCEL_SIZE) {

                List<Parcel> availableParcels = board.pickParcels();
                if (!availableParcels.isEmpty() && availableParcels != null) {
                    placeAnParcelAnywhere(turnLogger, availableParcels.get(random.nextInt(availableParcels.size())));
                }

            }
        }
    }


    /**
     * This method affect at cardWeActuallyTryToResolve the card from the 5 objectives we have. This method select
     * this objective who have the less missing position to complete
     */
    void easiestObjectiveToResolve2() {
        int min = 10;

        if (individualBoard.getUnfinishedParcelObjectives() != null) {
            cardWeActuallyTryToResolve = individualBoard.getUnfinishedParcelObjectives().get(0);
        }


        for (CardObjective cardOp: individualBoard.getUnfinishedParcelObjectives()) {

            CardObjectiveParcel card = (CardObjectiveParcel) cardOp;
            cardOp.verify();
            if (card.getMissingPositionsToComplete().size() <= min) {
                min = card.getMissingPositionsToComplete().size();
                cardWeActuallyTryToResolve = card;
            }

        }

    }



    void inilializeOrUpdateListOfCurrentsObjective2() {

        BotActionType lastAction = getLastAction();
        if (lastAction != null)
        {
            if (lastAction.equals(BotActionType.PICK_PARCEL_GOAL)) {
                // We pick a new parcel
                List<Parcel> availableParcels = board.pickParcels();
                if (!availableParcels.isEmpty() && availableParcels != null && getBoard().getPositions().size() < Config.DECK_PARCEL_SIZE) {
                    placeAnParcelAnywhere(turnLogger, availableParcels.get(random.nextInt(availableParcels.size())));
                }
            } else {
                pickParcelObjective(turnLogger);
            }
        } else {
            pickParcelObjective(turnLogger);
        }
    }


    public CardObjectiveParcel getCardWeActuallyTryToResolve() {
        return cardWeActuallyTryToResolve;
    }

    public void setTurnLogger(TurnLog turnLogger) {
        this.turnLogger = turnLogger;
    }

    @Override
    public boolean canPlay() {
        if (getBoard().getParcelCount() <= 26) {
            return true;
        } else {
            return false;
        }
    }

}
