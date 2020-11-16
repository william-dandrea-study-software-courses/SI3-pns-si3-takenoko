package fr.matelots.polytech.core.players;

import fr.matelots.polytech.core.game.CardObjectiveParcel;

import java.util.ArrayList;
import java.util.List;

public class IndividualBoard {
    private static final int MAX_UNFINISHED_OBJECTIVES_IN_HAND = 5;


    private final List<CardObjectiveParcel> objectiveParcels;

    public IndividualBoard() {
        objectiveParcels = new ArrayList<>();
    }

    private int countUnfinishedObjectives () {
        return countUnfinishedParcelObjectives();
    }

    private int countUnfinishedParcelObjectives () {
        return (int ) objectiveParcels.stream().filter(obj -> !obj.isCompleted()).count();
    }

    public List<CardObjectiveParcel> getUnfinishedParcelObjectives () {
        List<CardObjectiveParcel> unfinishedObj = new ArrayList<>();
        objectiveParcels.stream()
                .filter(obj -> !obj.isCompleted())
                .forEach(obj -> unfinishedObj.add(obj));
        return unfinishedObj;
    }

    public int getCompletedGoals() {
        int n = 0;
        for(var goal : objectiveParcels) {
            if(goal.isCompleted()) n++;
        }
        return n;
    }


    public List<CardObjectiveParcel> getObjectiveParcels () {
        return new ArrayList<>(objectiveParcels);
    }

    /**
     * This method allow to pick another parcel objective to complete.
     * It will return if it's possible to pick a new objective and if true if the objective has been successfully added
     * to the list
     * @param parcelObjective The new picked objective
     * @return false if you can pick the new objective or true if you can
     */
    public boolean addNewParcelObjective (CardObjectiveParcel parcelObjective) {
        if (countUnfinishedObjectives() >= MAX_UNFINISHED_OBJECTIVES_IN_HAND)
            return false;

        return objectiveParcels.add(parcelObjective);
    }
}
