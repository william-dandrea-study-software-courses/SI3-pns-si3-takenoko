package fr.matelots.polytech.core.players;

import fr.matelots.polytech.core.game.CardObjectiveParcel;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

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
        return (int ) objectiveParcels.stream().filter(CardObjectiveParcel::isCompleted).count();
    }

    public Set<CardObjectiveParcel> getUnfinishedParcelObjectives () {
        return objectiveParcels.stream()
                .filter(CardObjectiveParcel::isCompleted)
                .collect(Collectors.toUnmodifiableSet());
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