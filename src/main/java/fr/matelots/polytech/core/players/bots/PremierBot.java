package fr.matelots.polytech.core.players.bots;

import fr.matelots.polytech.core.game.Game;
import fr.matelots.polytech.core.game.goalcards.CardObjectiveParcel;
import fr.matelots.polytech.core.players.Bot;

/**
 * @author Yann Clodong
 */
public class PremierBot extends Bot {

    private CardObjectiveParcel currentGoal;

    private boolean filling = true;

    public PremierBot (Game game) {
        super (game);
    }

    @Override
    public void playTurn() {
        if(filling)
            pickGoal();
        else {
            selectGoal();
            attemptToPlaceParcelWithGoal();
        }
        strategie();
    }

    public void strategie() {
        if(getIndividualBoard().countUnfinishedParcelObjectives() == 0 && !filling) filling = true;
        else if(getIndividualBoard().countUnfinishedParcelObjectives() >= 5 && filling) filling = false;
    }

    private void attemptToPlaceParcelWithGoal() {
        this.currentGoal.verify();
        /*ParcelLineResolver resolver = new ParcelLineResolver(board);
        boolean resolved = resolver.attemptResolve(currentGoal);
        if(resolved) currentGoal.setComplete();*/
    }

    private void pickGoal() {
        pickParcelObjective();
    }

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
