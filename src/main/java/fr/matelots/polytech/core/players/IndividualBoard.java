package fr.matelots.polytech.core.players;

import fr.matelots.polytech.core.game.goalcards.CardObjectiveParcel;

import java.util.ArrayList;
import java.util.List;

/**
 * This class describe a Player board. A board is composed of :
 *      - All the bamboo eat by the panda
 *      - The reserve of irrigations
 *      - The reserve of amenagements
 *      - List of each type of objectives
 * @author Gabriel Cogne
 * @author Clodong Yann
 */
public class IndividualBoard {

    private static final int MAX_UNFINISHED_OBJECTIVES_IN_HAND = 5;

    // List of the objectives
    private final List<CardObjectiveParcel> objectiveParcels;

    public IndividualBoard() {
        objectiveParcels = new ArrayList<>();
    }


    /**
     * Get the score of the player
     * @return Number representing the score of the player
     */
    public int getPlayerScore() {
        return objectiveParcels.stream().filter(CardObjectiveParcel::isCompleted)
                .mapToInt(CardObjectiveParcel::getScore).sum();
    }


    public void checkAllParcelGoal() {
        objectiveParcels.forEach(CardObjectiveParcel::verify);
    }

    // = = = = = = = = = = = = = = = = = = = OBJECTIVES = = = = = = = = = = = = = = = = = = =

    /** @return a objective in the list of objectives who is not completed*/
    public CardObjectiveParcel getNextParcelGoal() {
        for(var obj : objectiveParcels) {
            if(!obj.isCompleted())
                return obj;
        }
        return null;
    }

    // @return the number of unfinished objectives (all types of objectives)
    public int countUnfinishedObjectives () {
        return countUnfinishedParcelObjectives();
    }

    // @return the number of completed objectives (all types of objectives)
    public int getCompletedObjectives() {
        int n = 0;
        for(var goal : objectiveParcels) {
            if(goal.isCompleted()) n++;
        }
        return n;
    }

    public int countCompletedObjectives() {
        int score = 0;
        score += (int)objectiveParcels.stream().filter(CardObjectiveParcel::isCompleted).count();
        return score;
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

    // = = = = = = = = = = = = = = = = = = = PARCELS = = = = = = = = = = = = = = = = = = =

    // @return the number of completed objectives (all types of objectives)
    public List<CardObjectiveParcel> getUnfinishedParcelObjectives() {
        List<CardObjectiveParcel> result = new ArrayList<>();
        for(var obj : objectiveParcels) {
            if(!obj.isCompleted())
                result.add(obj);
        }
        return result;
    }

    public int countUnfinishedParcelObjectives () {
        return (int) objectiveParcels.stream().filter(obj -> !obj.isCompleted()).count();
    }




}
