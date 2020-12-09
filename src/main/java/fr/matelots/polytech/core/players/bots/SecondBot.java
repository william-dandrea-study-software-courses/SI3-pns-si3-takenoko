package fr.matelots.polytech.core.players.bots;

import fr.matelots.polytech.core.game.Config;
import fr.matelots.polytech.core.game.Game;
import fr.matelots.polytech.core.game.goalcards.CardObjectiveParcel;
import fr.matelots.polytech.core.game.parcels.BambooColor;
import fr.matelots.polytech.core.game.parcels.BambooPlantation;
import fr.matelots.polytech.core.players.Bot;
import fr.matelots.polytech.core.players.bots.botLogger.BotActionType;
import fr.matelots.polytech.core.players.bots.botLogger.TurnLog;
import fr.matelots.polytech.engine.util.Position;

import java.util.ArrayList;
import java.util.Random;
import java.util.Set;


/**
 * Je pioche un objectif, je l'accompli, j'en pioche un autre... jusqu'à la fin de la partie.
 * Je choisi un emplacement random pour accomplir mon objectif.
 * @author williamdandrea
 */
public class SecondBot extends Bot {

    private TurnLog turnLogger;
    private CardObjectiveParcel currentObjective;

    public SecondBot(Game game) {
        super(game);
    }
    public SecondBot(Game game, String name) {
        super(game, name);
    }

    @Override
    public void playTurn(TurnLog log) {
        turnLogger = log;
        // State represent the state of the objective, if an objective is in progress, he is true, else he is false
        boolean state = checkCurrentObjective();

        if (!state) {
            // We have any objective so we will pick a new parcel objective
            pickAnObjectiveAndAddToPlayerBoard();
            selectObjectiveFromPlayerDeck();
            // We launch the strategy
        }
        strategy();

        turnLogger = null;
    }

    public CardObjectiveParcel getCurrentObjective() {
        return currentObjective;
    }

    @Override
    public boolean canPlay() {
        return false;
    }

    /**
     * This function pick an new objective and add this objective to the player deck
     */
    void pickAnObjectiveAndAddToPlayerBoard() {
        var obj = pickParcelObjective();

        if(obj.isPresent())
            turnLogger.addAction(BotActionType.PICK_PARCEL_GOAL, obj.get().toString());
    }

    /**
     * This method take an active objective in the player deck
     */
    void selectObjectiveFromPlayerDeck() {
        currentObjective = getIndividualBoard().getNextParcelGoal();
    }

    /**
     * This function check the current objective
     * @return true if the currentObjective is in progress or false if there is any currentObjective or if
     * the currentObjective is finish
     */
    boolean checkCurrentObjective() {

        // If the currentObjective == null, or if the current goal
        // is finish (the function verify return true if an objective is completed)
        return this.currentObjective != null && !currentObjective.verify();

        // Else, we return true because the objective in progress
    }

    /**
     * This method represent the bot strategy, it is this method who represent the game strategy of the bot (alias
     * the player)
     */
    void strategy() {

        // We check if the game board is just composed of the pond (étang) or if we have more parcels
        if (board.getParcelCount() == 1) {
            // We need to place a parcel anywhere in the game board
            var obj = placeAnParcelAnywhere();

            if(obj.isPresent())
                turnLogger.addAction(BotActionType.PLACE_PARCEL, obj.get().toString());

        } else {


            // We check the place where we can place a new parcel
            Set<Position> placeWhereWeCanPlaceAnParcel = null;
            if (currentObjective != null) {
                placeWhereWeCanPlaceAnParcel = currentObjective.getMissingPositionsToComplete();
            }
            ArrayList<Position> positionsWeChoose = new ArrayList<>();


            // We browse all the place where we can place a parcel and we add this positions to the ArrayList positionsWeChoose
            if (placeWhereWeCanPlaceAnParcel != null)
                placeWhereWeCanPlaceAnParcel.stream()
                        .filter(p -> board.isPlaceValid(p) && !p.equals(Config.BOND_POSITION))
                        .forEach(positionsWeChoose::add);

            if(positionsWeChoose.size() != 0) {
                // We have an place to put the new parcel

                // We choose a random parcel in the potential list
                Random randomNumber = new Random();
                int position = randomNumber.nextInt(positionsWeChoose.size());

                // We add the new parcel
                board.addParcel(positionsWeChoose.get(position), new BambooPlantation(BambooColor.GREEN));
                String param = positionsWeChoose.get(position).toString();

                turnLogger.addAction(BotActionType.PLACE_PARCEL, param);
            } else {
                // We put a parcel anywhere
                var obj = placeAnParcelAnywhere();
                if(obj.isPresent())
                    turnLogger.addAction(BotActionType.PLACE_PARCEL, obj.get().toString());

            }

        }

    }



    @Override
    public String toString() {
        return "Bot 2";
    }




    @Override
    public String getTurnMessage() {
        return ""; //action.getMessage(this, "Some parameter");
    }

}
