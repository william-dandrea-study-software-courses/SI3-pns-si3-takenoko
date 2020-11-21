package fr.matelots.polytech.core.players.bots;

import fr.matelots.polytech.core.game.Game;
import fr.matelots.polytech.core.game.parcels.Parcel;
import fr.matelots.polytech.core.game.goalcards.CardObjectiveParcel;
import fr.matelots.polytech.core.players.Bot;

/**
 * @author Yann Clodong
 */
public class PremierBot extends Bot {

    private CardObjectiveParcel currentGoal;

    // Represent the phase of the bot :
    // True => Fill the board;
    // False => Attempt to resolve goal
    private boolean filling = true;

    public PremierBot (Game game) {
        super (game);
    }


    /**
     * Emulate the turn of the bot
     */
    @Override
    public void playTurn() {
        if(filling)
            pickGoal();
        else {
            if(checkGoal()) {
                attemptToPlaceParcelWithGoal();
            }
        }
        strategie();
    }

    /**
     * Represent strategie of the bot.
     * Here the bot do have to pass one for fill his board of 5 objectives
     * another to accomplish the objectives
     */
    private void strategie() {
        if(getIndividualBoard().countUnfinishedParcelObjectives() == 0 && !filling) filling = true;
        else if(getIndividualBoard().countUnfinishedParcelObjectives() >= 5 && filling) filling = false;
    }

    /**
     * Represent the attempt of the bot to resolve the goal
     */
    private void attemptToPlaceParcelWithGoal() {
        var goodPlaces = currentGoal.getMissingPositionsToComplete();

        for (var position : goodPlaces) {
            if(board.isPlaceValid(position)) {
                board.addParcel(position, new Parcel());
                return;
            }
        }

        /*ParcelLineResolver resolver = new ParcelLineResolver(board);
        boolean resolved = resolver.attemptResolve(currentGoal);
        if(resolved) currentGoal.setComplete();*/
    }

    /**
     * The bot take a Parcel goal card
     */
    private void pickGoal() {
        pickParcelObjective();
    }

    /**
     * The bot check if the goal is complete
     * @return if the bot can attempt to resolve the goal
     */
    private boolean checkGoal() {
        if(this.currentGoal == null)
            selectGoal();

        this.currentGoal.verify();
        if(currentGoal.isCompleted())
        {
            selectGoal();
            if(this.currentGoal == null) return false;
        }

        return true;
    }


    /**
     * the bot select the goal card in the deck
     */
    private void selectGoal() {
        currentGoal = getIndividualBoard().getNextParcelGoal();
        /*if(currentGoal == null || currentGoal.isCompleted()) {
            List<CardObjectiveParcel> unfinished = getIndividualBoard().getUnfinishedParcelObjectives();
            currentGoal = unfinished.get(0);
        }*/
    }

    @Override
    public String toString() {
        return "Bot 1 ready to play";
    }
}
