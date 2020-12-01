package fr.matelots.polytech.core.players.bots;

import fr.matelots.polytech.core.game.Game;
import fr.matelots.polytech.core.game.goalcards.CardObjective;
import fr.matelots.polytech.core.game.goalcards.CardObjectiveGardener;
import fr.matelots.polytech.core.game.goalcards.CardObjectiveParcel;
import fr.matelots.polytech.core.players.Bot;
import fr.matelots.polytech.engine.util.Position;

import java.util.List;
import java.util.Set;

/**
 * Piocher 2/3 obj parcelles et 2/3 jardinier, je choisi a l’instant t l’objectif le plus intéressant
 * et je l’accompli (un objectif a la fois) puis j’en repioche un
 *
 * - [OK] Tirer 2 objectifs jardinier       [TESTED]
 * - [OK] Tirer 2 objectifs parcelles      [TESTED]
 *
 * - [EN COURS] Analyser les objectifs parcelle
 *     - [EN COURS] Prendre la carte objectif jardinier ayant le pattern le plus petit
 *     - [EN COURS] Regarder si on peut le résoudre
 * - [EN COURS] Analyser les objectifs jardinier
 *     - [EN COURS] Prendre la carte objectif avec le moins de bambou
 *     - [EN COURS] Regarder si on peu le résoudre
 * - [ ] Regarder qui est le plus facile à résoudre entre les 2 objectifs choisies
 * - [ ] Si objectif parcelle plus facile à résoudre
 *     - [ ] Placer une case au bon endroit
 *     - [ ] Vérifier si l’objectif est valider
 *         - [ ] VALIDE :
 *             - [ ] On tire un nouvel objectif
 *             - [ ] On analyse a nouveau lequel est le plus facile à résoudre
 *         - [ ] NON VALIDE :
 *             - [ ] On analyse quel objectif est le plus facile à résoudre parmi la liste
 *                 - [ ] Si c’est toujours celui la, on essaye de le résoudre en rajoutant des cases
 *                 - [ ] Sinon, on passe à l’objectif le plus simple à résoudre
 * - [ ] Si objectif jardinier plus facile à résoudre
 *     - [ ] Placer le jardinier au bon endroit (verifier si la case est valide, et quand il arrive il peut faire pousser un bambou)
 *     - [ ] Vérifier si l’objectif est valider
 *         - [ ] VALIDE :
 *             - [ ] On tire un nouvel objectif
 *             - [ ] On analyse a nouveau lequel est le plus facile à résoudre
 *         - [ ] NON VALIDE :
 *             - [ ] On analyse quel objectif est le plus facile à résoudre parmi la liste
 *                 - [ ] Si c’est toujours celui la, on essaye de le résoudre en rajoutant des cases
 *                 - [ ] Sinon, on passe à l’objectif le plus simple à résoudre
 *
 * @author williamdandrea
 */
public class FourthBot extends Bot {

    private final int NUMBER_OF_PARCEL_OBJECTIVES_AT_THE_START = 2;
    private final int NUMBER_OF_GARDENER_OBJECTIVES_AT_THE_START = 2;
    private boolean firstLaunch = true;
    private CardObjectiveParcel currentParcelObjective;
    private CardObjectiveGardener currentGardenerObjective;

    public FourthBot(Game game) {
        super(game);
    }

    @Override
    public void playTurn() {

        // If it is the first game launch, we pick 2 (number in parameters) parcels objectives and 2 gardener objectives
        // (number in parameters). After this brackets, we will be not in the first launch, so we will try to analyse
        // directly the objectives.
        if (firstLaunch) {
            firstLaunchPickObjectives();
            firstLaunch = false;
        } else {

            // If we have any parcels on the game board, we need to place parcels


            // Now we will analyse the parcels objectives and put the more easier to the currentParcelObjective variable
            analyzeParcelsObjectives();
            analyzeGardenerObjectives();

            // Now we need to compare the easiest objective to resolve
            easiestObjectiveToResolve();

        }

    }

    public int getNumberOfParcelObjectivesAtTheStart() {
        return NUMBER_OF_PARCEL_OBJECTIVES_AT_THE_START;
    }

    public int getNumberOfGardenerObjectivesAtTheStart() {
        return NUMBER_OF_GARDENER_OBJECTIVES_AT_THE_START;
    }

    public boolean isFirstLaunch() {
        return firstLaunch;
    }

    @Override
    public boolean canPlay() { return false; }

    /**
     * This function pick an new Parcel objective and add this objective to the player deck
     */
    void pickAnParcelObjectiveAndAddToPlayerBoard() {
        pickParcelObjective();
    }

    /**
     * This function pick an new Gardener objective and add this objective to the player deck
     */
    void pickAnGardenerObjectiveAndAddToPlayerBoard() {
        pickGardenerObjective();
    }

    /**
     * If it is the first game launch, we pick 2 (number in parameters) parcels objectives and 2 gardener objectives
     * (number in parameters)
     */
    void firstLaunchPickObjectives() {
        for (int i = 0; i < NUMBER_OF_PARCEL_OBJECTIVES_AT_THE_START ; i++) {
            pickAnParcelObjectiveAndAddToPlayerBoard();
        }
        for (int i = 0; i < NUMBER_OF_GARDENER_OBJECTIVES_AT_THE_START ; i++) {
            pickAnGardenerObjectiveAndAddToPlayerBoard();
        }
    }

    /**
     * This function will analyze all the parcels objectives and put the easier parcel objective to the
     * currentParcelObjective variable
     */
    void analyzeParcelsObjectives() {
        // We put the unfinished parcels objectives into the variable unfinishedParcelsObjectives in order to
        // choose the easiest objective
        List<CardObjectiveParcel> unfinishedParcelsObjectives = individualBoard.getUnfinishedParcelObjectives();

        // To determine if a parcel objective is easy to resolve, we will count the number of parcels the objectives need
        // Less this number is, more easy the objective will be resolve

        // TODO: 01/12/2020 Analyze the easiest card
        currentParcelObjective = unfinishedParcelsObjectives.get(1);

    }

    /**
     * This function will analyze all the gardeners objectives and put the easier parcel objective to the
     * currentParcelObjective variable
     */
    void analyzeGardenerObjectives() {
        List<CardObjectiveGardener> unfinishedGardenersObjectives = individualBoard.getUnfinishedGardenerObjectives();

        // TODO: 01/12/2020 Analyze the easiest card
        currentGardenerObjective = unfinishedGardenersObjectives.get(1);
    }


    /**
     * This function will compare the currentGardenerObjective and currentParcelObjective to determine witch is
     * the easiest to resolve
     */
    void easiestObjectiveToResolve() {

        // Here, we regard the number of parcels we need to resolve the current parcel objective
        int numberOfParcelsToResolveCurrentParcelObjective = currentParcelObjective.getMissingPositionsToComplete().size();

        // Here, we regard how many bamboo we need to complete one of the objective

    }


}
