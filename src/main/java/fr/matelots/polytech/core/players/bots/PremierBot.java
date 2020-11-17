package fr.matelots.polytech.core.players.bots;

import fr.matelots.polytech.core.game.CardObjectiveParcel;
import fr.matelots.polytech.core.game.Game;
import fr.matelots.polytech.core.game.GoalCards.AlignedParcelGoal;
import fr.matelots.polytech.core.players.VirtualPlayer;
import fr.matelots.polytech.core.players.bots.goalresolvers.GoalResolver;
import fr.matelots.polytech.core.players.bots.goalresolvers.ParcelGoalResolver;
import fr.matelots.polytech.core.players.bots.goalresolvers.ParcelLineResolver;

import java.util.List;

public class PremierBot extends VirtualPlayer {

    AlignedParcelGoal currentGoal;

    boolean filling = true;
    public PremierBot (Game game) {
        super (game);
    }

    @Override
    public void playTurn() {
        // todo
        if(filling)
            pickGoal();
        else {
            SelectGoal();
            AttemptToPlaceParcelWithGoal();
        }
        Strategie();
    }

    public void Strategie() {
        if(getIndividualBoard().countUnfinishedParcelObjectives() == 0 && !filling) filling = true;
        else if(getIndividualBoard().countUnfinishedParcelObjectives() >= 5 && filling) filling = false;
    }

    public void ResolveGoal(AlignedParcelGoal goal) {
        this.currentGoal = goal;
        getIndividualBoard().addNewParcelObjective(goal);
    }

    private void AttemptToPlaceParcelWithGoal() {
        ParcelLineResolver resolver = new ParcelLineResolver(board);
        boolean resolved = resolver.attemptResolve(currentGoal);
        if(resolved) currentGoal.setComplete();
    }

    private void pickGoal() {
        pickParcelObjective();
    }

    private void SelectGoal() {
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
