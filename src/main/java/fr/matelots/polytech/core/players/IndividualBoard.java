package fr.matelots.polytech.core.players;

import fr.matelots.polytech.core.game.goalcards.AlignedParcelGoal;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Gabriel Cogne
 */
public class IndividualBoard {

    private static final int MAX_UNFINISHED_OBJECTIVES_IN_HAND = 5;

    private final List<AlignedParcelGoal> objectiveParcels;

    public IndividualBoard() {
        objectiveParcels = new ArrayList<>();
    }

    public AlignedParcelGoal getNextParcelGoal() {
        for(var objs : objectiveParcels) {
            if(!objs.getComplete())
                return objs;
        }
        return null;
    }


    private int countUnfinishedObjectives () {
        return countUnfinishedParcelObjectives();
    }

    public ArrayList<AlignedParcelGoal> getUnfinishedParcelObjectives() {
        ArrayList<AlignedParcelGoal> result = new ArrayList<>();
        for(var obj : objectiveParcels) {
            if(!obj.getComplete())
                result.add(obj);
        }
        return result;
    }

    public int countUnfinishedParcelObjectives () {
        return (int) objectiveParcels.stream().filter(obj -> !obj.getComplete()).count();
    }

    public int getCompletedGoals() {
        int n = 0;
        for(var goal : objectiveParcels) {
            if(goal.getComplete()) n++;
        }
        return n;
    }

    /**
     * This method allow to pick another parcel objective to complete.
     * It will return if it's possible to pick a new objective and if true if the objective has been successfully added
     * to the list
     * @param parcelObjective The new picked objective
     * @return false if you can pick the new objective or true if you can
     */
    public boolean addNewParcelObjective (AlignedParcelGoal parcelObjective) {
        if (countUnfinishedObjectives() >= MAX_UNFINISHED_OBJECTIVES_IN_HAND)
            return false;

        return objectiveParcels.add(parcelObjective);
    }
}
