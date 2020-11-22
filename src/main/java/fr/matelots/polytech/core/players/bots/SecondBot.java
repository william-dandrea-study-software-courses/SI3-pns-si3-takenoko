package fr.matelots.polytech.core.players.bots;

import fr.matelots.polytech.core.game.Game;
import fr.matelots.polytech.core.game.goalcards.CardObjectiveParcel;
import fr.matelots.polytech.core.players.Bot;


/**
 * Je pioche un objectif, je l'accompli, j'en pioche un autre... jusqu'à la fin de la partie.
 * Je choisi un emplacement random pour accomplir mon objectif.
 * @author williamdandrea
 *
 * TODO: 22/11/2020 Implement the strategy method with the current bot strategy
 * TODO: 22/11/2020 Test the methods
 * TODO: 22/11/2020 Implement the SecondBot in the game
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
        }
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
     * @return true if the currentObjective is in progress
     * @return false if there is any currentObjective or if the currentObjective is finish
     */
    private boolean checkCurrentObjective() {

        // If the currentObjective == null, or if the current goal
        // is finish (the function verify return true if an objective is completed)
        if (this.currentObjective == null || currentObjective.verify()) {
            return false;
        }

        // Else, we return true because the objective in progress
        return true;
    }

    /**
     * This method represent the bot strategie, it is this method who represent the game strategie of the bot (alias
     * the player)
     */
    private void strategy() {

    }





}
