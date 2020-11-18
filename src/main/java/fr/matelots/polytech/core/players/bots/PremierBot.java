package fr.matelots.polytech.core.players.bots;

import fr.matelots.polytech.core.game.CardObjectiveParcel;
import fr.matelots.polytech.core.game.Game;
import fr.matelots.polytech.core.game.goalcards.AlignedParcelGoal;
import fr.matelots.polytech.core.players.Bot;
import fr.matelots.polytech.core.players.bots.goalresolvers.ParcelLineResolver;
import jdk.jshell.spi.ExecutionControl;

/**
 * @author Yann Clodong
 */
public class PremierBot extends Bot {

    private CardObjectiveParcel currentGoal;

    private boolean filling = true;
    private boolean deckEmpty = false;

    public PremierBot (Game game) {
        super (game);
    }

    @Override
    public void playTurn() {
        if(filling) {
            boolean hasPickGoal = pickGoal();
            if(!hasPickGoal) deckEmpty = true;
        }
        else {
            selectGoal();
            //attemptToPlaceParcelWithGoal();
        }
        strategie();
    }

    public void strategie() {
        if(getIndividualBoard().countUnfinishedParcelObjectives() == 0 && !filling) filling = true;
        else if((getIndividualBoard().countUnfinishedParcelObjectives() >= 5 || deckEmpty) && filling) filling = false;
    }

    private void attemptToPlaceParcelWithGoal() throws ExecutionControl.NotImplementedException {
        /*ParcelLineResolver resolver = new ParcelLineResolver(board);
        //boolean resolved = resolver.attemptResolve(currentGoal);
        if(resolved) currentGoal.setComplete();*/
        throw new ExecutionControl.NotImplementedException("Place parcel with goal not implemented !");
    }

    private boolean pickGoal() {
        return pickParcelObjective();
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
