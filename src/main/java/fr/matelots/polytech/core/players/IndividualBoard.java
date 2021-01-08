package fr.matelots.polytech.core.players;

import fr.matelots.polytech.core.game.Config;
import fr.matelots.polytech.core.game.goalcards.*;
import fr.matelots.polytech.core.game.parcels.BambooColor;
import fr.matelots.polytech.core.game.parcels.Layout;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

/**
 * This class describe a Player board. A board is composed of :
 *      - All the bamboo eat by the panda
 *      - The reserve of irrigations
 *      - The reserve of amenagements
 *      - List of each type of objectives
 * @author Gabriel Cogne
 * @author Clodong Yann
 * @author williamdandrea
 */
public class IndividualBoard {

    // List of the objectives
    private final List<CardObjectiveParcel> objectiveParcels;
    private final List<CardObjectiveGardener> objectiveGardeners;
    private final List<CardObjectivePanda> objectivePandas;
    private final List<Layout> layouts;
    private int irrigations;
    private CardObjectiveEmperor emperor;

    // Eaten bamboo
    private final int[] bamboos;

    public IndividualBoard() {
        objectiveParcels = new ArrayList<>();
        objectiveGardeners = new ArrayList<>();
        objectivePandas = new ArrayList<>();
        layouts = new ArrayList<>();
        emperor = null;
        irrigations = 0;

        bamboos = new int[BambooColor.values().length];
        Arrays.fill(bamboos, 0);
    }

    public void setEmperor(CardObjectiveEmperor emperor) {
        this.emperor = emperor;
    }

    /**
     * Get the score of the player
     * @return Number representing the score of the player
     */
    public int getPlayerScore() {
        return objectiveParcels.stream().filter(CardObjective::isCompleted)
                .mapToInt(CardObjective::getScore).sum()
                + objectiveGardeners.stream().filter(CardObjective::isCompleted)
                .mapToInt(CardObjective::getScore).sum()
                + objectivePandas.stream().filter(CardObjective::isCompleted)
                .mapToInt(CardObjective::getScore).sum()
                + (emperor != null ? emperor.getScore() : 0);
    }

    /**
     * Get the score of the player regarding only the panda objectives
     * @return Number representing the score of the player
     */
    public int getObjectivesPandaScore () {
        return objectivePandas.stream().filter(CardObjective::isCompleted)
                .mapToInt(CardObjective::getScore).sum();
    }


    /**
     * Vérifie tous les objectifs parcelles
     */
    public void checkAllParcelGoal() {
        objectiveParcels.forEach(card -> {
            if(card.isCompleted()) return;
            card.verify();
        });
    }

    /**
     * Vérifie tous les objectifs
     */
    public void checkAllGoal() {
        checkAllParcelGoal();
        checkAllGardenerGoal();
        checkAllPandaGoal();
    }

    /**
     * Vérifie tous les objectifs panda
     */
    public void checkAllPandaGoal() {
        objectivePandas.forEach(this::verify);
    }

    /**
     * Vérifie tous les objectifs jardinier
     */
    public void checkAllGardenerGoal() {
        for (CardObjectiveGardener card : objectiveGardeners) {
            if(card.isCompleted()) return;
            card.verify();
            //if(card.isCompleted()) System.out.println("Gardener objective completed: +"+card.getScore());
        }
    }
    // = = = = = = = = = = = = = = = = = = = OBJECTIVES = = = = = = = = = = = = = = = = = = =

    /**
     *
     * @return the number of unfinished objectives (all types of objectives)
     */
    public int countUnfinishedObjectives () {
        return countUnfinishedParcelObjectives()
                + countUnfinishedGardenerObjectives() + countUnfinishedPandaObjectives();
    }

    /**
     * @return the number of completed objectives (all types of objectives)
     */
    public CardObjective[] getCompletedObjectives() {
        return Stream.concat(
                Stream.concat(
                    objectiveParcels.stream().filter(CardObjective::isCompleted),
                    objectivePandas.stream().filter(CardObjective::isCompleted)
                ),
                objectiveGardeners.stream().filter(CardObjective::isCompleted))
        .toArray(CardObjective[]::new);
    }

    /**
     * @return the number of completed objectives
     */
    public int countCompletedObjectives() {
        return getCompletedObjectives().length;
    }

    /**
     * This method allow to pick another parcel objective to complete.
     * It will return if it's possible to pick a new objective and if true if the objective has been successfully added
     * to the list
     * @param parcelObjective The new picked objective
     * @return false if you can pick the new objective or true if you can
     */
    public boolean addNewParcelObjective (CardObjectiveParcel parcelObjective) {
        if (countUnfinishedObjectives() >= Config.MAX_NUMBER_OF_OBJECTIVES_CARD_IN_HAND)
            return false;

        return objectiveParcels.add(parcelObjective);
    }

    /**
     * Ajoute un objectif jardinier à la plaque de status
     * @param gardenerObjective, l'objectif
     * @return true si l'objectif est ajouté avec succès
     */
    public boolean addNewGardenerObjective(CardObjectiveGardener gardenerObjective) {
        if (countUnfinishedObjectives() >= Config.MAX_NUMBER_OF_OBJECTIVES_CARD_IN_HAND)
            return false;

        return objectiveGardeners.add(gardenerObjective);
    }

    /**
     * Ajoute un objectif panda à la plaque de status
     * @param pandaObjective, l'objectif
     * @return true si l'objectif est ajouté avec succès
     */
    public boolean addNewPandaObjective(CardObjectivePanda pandaObjective) {
        if (countUnfinishedObjectives() >= Config.MAX_NUMBER_OF_OBJECTIVES_CARD_IN_HAND)
            return false;

        return objectivePandas.add(pandaObjective);
    }

    // = = = = = = = = = = = = = = = = = = IRRIGATION = = = = = = = = = = = = = = = = = =

    /**
     * Ajoute une irrigation à la plaque de status
     */
    public void addIrrigation() {
        irrigations++;
    }

    /**
     * @return true si il reste des irrigation sur la plaque de status
     */
    public boolean canPlaceIrrigation() {
        return irrigations > 0;
    }

    /**
     * Retire une irrigation de la plaque de status
     * @return true si il y avais assez d'irrigation
     */
    public boolean placeIrrigation() {
        if(!canPlaceIrrigation()) return false;
        irrigations--;
        return false;
    }

    /**
     * @return Le nombre d'irrigation disponible sur la plaque de status
     */
    public int getNumberOfIrrigations() {
        return irrigations;
    }

    // = = = = = = = = = = = = = = = = = = = PARCELS = = = = = = = = = = = = = = = = = = =

    /**
     * @return La liste des tous les objectifs parcelle non résolu
     */
    public List<CardObjectiveParcel> getUnfinishedParcelObjectives() {
        List<CardObjectiveParcel> result = new ArrayList<>();
        for(var obj : objectiveParcels) {
            if(!obj.isCompleted())
                result.add(obj);
        }
        return result;
    }

    public List<CardObjectiveGardener> getUnfinishedGardenerObjectives() {
        List<CardObjectiveGardener> result = new ArrayList<>();
        objectiveGardeners.stream()
                .filter(o -> !o.isCompleted())
                .forEach(result::add);
        return result;
    }

    public List<CardObjectivePanda> getUnfinishedPandaObjectives() {
        List<CardObjectivePanda> result = new ArrayList<>();
        objectivePandas.stream()
                .filter(o -> !o.isCompleted())
                .forEach(result::add);
        return result;
    }

    public int countUnfinishedParcelObjectives () {
        return (int) objectiveParcels.stream().filter(obj -> !obj.isCompleted()).count();
    }

    public int countUnfinishedGardenerObjectives () {
        return (int) objectiveGardeners.stream().filter(obj -> !obj.isCompleted()).count();
    }

    public int countUnfinishedPandaObjectives () {
        return (int) objectivePandas.stream().filter(obj -> !obj.isCompleted()).count();
    }

    public void addAnEatenUnitOfBamboo (BambooColor color) {
        if (color != null)
            bamboos[color.ordinal()]++;
    }

    public int getGreenEatenBamboo () {
        return bamboos[BambooColor.GREEN.ordinal()];
    }

    public int getYellowEatenBamboo () {
        return bamboos[BambooColor.YELLOW.ordinal()];
    }

    public int getPinkEatenBamboo () {
        return bamboos[BambooColor.PINK.ordinal()];
    }

    public void verify (CardObjectivePanda objectivePanda) {
        objectivePanda.setIndividualBoard(this);
        if (objectivePanda.verify()) {
            bamboos[BambooColor.GREEN.ordinal()] -= objectivePanda.getCountForColor(BambooColor.GREEN);
            bamboos[BambooColor.PINK.ordinal()] -= objectivePanda.getCountForColor(BambooColor.PINK);
            bamboos[BambooColor.YELLOW.ordinal()] -= objectivePanda.getCountForColor(BambooColor.YELLOW);
        }
    }

    // ====== LAYOUT


    public List<Layout> getLayouts() {
        return layouts;
    }

    public boolean addLayouts(Layout layout) {
        return layouts.add(layout);
    }

    public boolean getLayoutFromIndBoard(Layout lay) {
        return layouts.remove(lay);
    }
}
