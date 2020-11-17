package fr.matelots.polytech.core.players.bots;

import fr.matelots.polytech.core.game.CardObjectiveParcel;
import fr.matelots.polytech.core.game.GoalCards.AlignedParcelGoal;
import fr.matelots.polytech.core.players.VirtualPlayer;
import fr.matelots.polytech.core.players.bots.goalresolvers.GoalResolver;
import fr.matelots.polytech.core.players.bots.goalresolvers.ParcelGoalResolver;
import fr.matelots.polytech.core.players.bots.goalresolvers.ParcelLineResolver;

import java.util.List;

public class PremierBot extends VirtualPlayer {

    AlignedParcelGoal currentGoal;
    public PremierBot () {
        super ();
    }

    @Override
    public void playTurn() {
        // todo
        SelectGoal();
        AttemptToPlaceParcelWithGoal();
    }

    public void ResolveGoal(AlignedParcelGoal goal) {
        this.currentGoal = goal;
    }

    private void AttemptToPlaceParcelWithGoal() {
        ParcelLineResolver resolver = new ParcelLineResolver(board);
        boolean resolved = resolver.attemptResolve(currentGoal);
        if(resolved) currentGoal.setComplete();
    }

    private void SelectGoal() {
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
