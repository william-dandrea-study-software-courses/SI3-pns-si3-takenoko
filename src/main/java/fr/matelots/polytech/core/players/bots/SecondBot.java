package fr.matelots.polytech.core.players.bots;

import fr.matelots.polytech.core.game.Config;
import fr.matelots.polytech.core.game.Game;
import fr.matelots.polytech.core.game.goalcards.CardObjectiveParcel;
import fr.matelots.polytech.core.game.parcels.BambooColor;
import fr.matelots.polytech.core.game.parcels.BambooPlantation;
import fr.matelots.polytech.core.game.parcels.Parcel;
import fr.matelots.polytech.core.players.Bot;
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

    private CardObjectiveParcel currentObjective;
    private boolean state = false;

    public SecondBot(Game game) {
        super(game);
    }

    @Override
    public void playTurn() {

        // State represent the state of the objective, if an objective is in progress, he is true, else he is false
        state = checkCurrentObjective();

        if (state) {
            // An objective is chosen and this objective is in progress, we can continue (or start) or bot strategy
            strategy();

        } else {
            // We have any objective so we will pick a new parcel objective
            pickAnObjectiveAndAddToPlayerBoard();
            selectObjectiveFromPlayerDeck();
            // We launch the strategy
            strategy();
        }
    }

    @Override
    public boolean canPlay() {
        return false;
    }

    /**
     * This function pick an new objective and add this objective to the player deck
     */
    private void pickAnObjectiveAndAddToPlayerBoard() {
        pickParcelObjective();
    }

    /**
     * This method take an active objective in the player deck
     */
    private void selectObjectiveFromPlayerDeck() {
        currentObjective = getIndividualBoard().getNextParcelGoal();
    }

    /**
     * This function check the current objective
     * @return true if the currentObjective is in progress or false if there is any currentObjective or if
     * the currentObjective is finish
     */
    private boolean checkCurrentObjective() {

        // If the currentObjective == null, or if the current goal
        // is finish (the function verify return true if an objective is completed)
        return this.currentObjective != null && !currentObjective.verify();

        // Else, we return true because the objective in progress
    }

    /**
     * This method represent the bot strategy, it is this method who represent the game strategy of the bot (alias
     * the player)
     */
    private void strategy() {

        // We check if the game board is just composed of the pond (etang) or if we have more parcels
        if (board.getParcelCount() == 1) {
            // We need to place a parcel anywhere in the game board
            placeAnParcelAnywhere();
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
                board.addParcel(positionsWeChoose.get(position), new BambooPlantation(BambooColor.green));
            } else {
                // We put a parcel anywhere
                placeAnParcelAnywhere();
            }

        }

    }

    /**
     * This method will place a parcel anywhere in the board
     */
    private void placeAnParcelAnywhere() {
        // We check where we can put an parcel
        ArrayList<Position> placeWhereWeCanPlaceAnParcel = new ArrayList<>(board.getValidPlaces());
        // Now, we have an ArrayList of the potentials places where we can add a parcel

        // We choose a random parcel in the potential list
        Random randomNumber = new Random();
        int position = randomNumber.nextInt(placeWhereWeCanPlaceAnParcel.size());

        // We finally add to the board the new parcel
        board.addParcel(placeWhereWeCanPlaceAnParcel.get(position), new BambooPlantation(BambooColor.green));

    }

    @Override
    public String toString() {
        return "Bot 2";
    }





}
