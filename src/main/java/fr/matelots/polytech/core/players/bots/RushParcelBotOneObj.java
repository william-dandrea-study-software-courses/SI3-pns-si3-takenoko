package fr.matelots.polytech.core.players.bots;

import fr.matelots.polytech.core.game.Config;
import fr.matelots.polytech.core.game.Game;
import fr.matelots.polytech.core.game.Weather;
import fr.matelots.polytech.core.game.goalcards.CardObjectiveParcel;
import fr.matelots.polytech.core.game.goalcards.pattern.PositionColored;
import fr.matelots.polytech.core.game.parcels.BambooColor;
import fr.matelots.polytech.core.players.Bot;
import fr.matelots.polytech.core.players.bots.logger.TurnLog;

import java.util.Optional;
import java.util.Set;

public class RushParcelBotOneObj extends Bot {

    private TurnLog turnLogger;
    private Optional<CardObjectiveParcel> currentObjective;

    private final int minNumberOfParcels = 6;
    private int finishIncrement = 0;
    private int inc = 0;

    public RushParcelBotOneObj(Game game) { super(game); }

    public RushParcelBotOneObj(Game game, String name) { super(game, name); }

    @Override
    public void playTurn(TurnLog log, Weather weatherCard) {
        currentNumberOfAction = 0;
        turnLogger = log;
        inc++;



        int numberOfObjectiveInIndividualBoard = individualBoard.countUnfinishedObjectives();
        //checkObjective(currentObjective);

        if (minimumParcelsInTheBoard()) {
            // 1.0 // Si on a aucun objectif dans le deck
            if (numberOfObjectiveInIndividualBoard == 0) {
                currentObjective = pickParcelObjective(log);
                if (currentObjective.isPresent()) {
                    tryToResolveParcelObjective2();
                } else {
                    currentObjective = pickParcelObjective(log);
                    placeAnParcelAnywhere(turnLogger);
                    finishIncrement++;
                }
            } else {

                if (numberOfObjectiveInIndividualBoard >= 1) {
                    if (canDoAction() && currentObjective.isPresent()) {
                        tryToResolveParcelObjective2();
                    } else {
                        currentObjective = pickParcelObjective(log);
                        //pickParcelObjective(log);
                        placeAnParcelAnywhere(turnLogger);
                        finishIncrement++;
                    }
                }

            }
        } else {
            placeAnParcelAnywhere(turnLogger);
        }

        if (currentNumberOfAction != 2) {
            placeAnParcelAnywhere(log);
        }



    }

    /**
     * We except that the currentObjective is present
     */
    void tryToResolveParcelObjective2() {

        CardObjectiveParcel actualCard = currentObjective.get();

        Set<PositionColored> missingPositionsToComplete = getMissingPositions(actualCard);

        for (PositionColored positionColored : missingPositionsToComplete) {
            placeAnParcelAnywhere(positionColored.getColor(), turnLogger);
        }

    }

    boolean minimumParcelsInTheBoard() {
        return getBoard().getParcelCount() <= minNumberOfParcels;
    }

    /**
     *
     * @param colors
     * @return true if we have differents colors
     */
    boolean checkIfTheColorsInAnObjectiveAreTheSameOrNot(BambooColor[] colors) {
        for (int i=0; i<colors.length; i++) {
            if (!colors[i].equals(colors[i+1])) {
                return true;
            }
        }
        return false;

    }

    @Override
    public boolean canPlay() {

        if (board.getParcelCount() <= Config.DECK_PARCEL_SIZE && inc <= 200) {
            if (this.currentNumberOfAction < 0)
                throw new IllegalArgumentException("We can't have negative actions");
            if (this.currentNumberOfAction <= 2)
                return true;
        }
        return false;


    }

    public TurnLog getLogger() {
        return turnLogger;
    }
}
