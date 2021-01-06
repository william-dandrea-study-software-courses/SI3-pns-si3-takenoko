package fr.matelots.polytech.core.players.bots;

import fr.matelots.polytech.core.game.Config;
import fr.matelots.polytech.core.game.Game;
import fr.matelots.polytech.core.game.Weather;
import fr.matelots.polytech.core.game.goalcards.CardObjective;
import fr.matelots.polytech.core.game.goalcards.CardObjectiveGardener;
import fr.matelots.polytech.core.game.goalcards.CardObjectiveParcel;
import fr.matelots.polytech.core.game.goalcards.pattern.PositionColored;
import fr.matelots.polytech.core.game.parcels.BambooColor;
import fr.matelots.polytech.core.game.parcels.BambooPlantation;
import fr.matelots.polytech.core.game.parcels.Parcel;
import fr.matelots.polytech.core.players.Bot;
import fr.matelots.polytech.core.players.bots.logger.BotActionType;
import fr.matelots.polytech.core.players.bots.logger.TurnLog;
import fr.matelots.polytech.engine.util.Position;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Piocher 2/3 obj parcelles et 2/3 jardinier, je choisi a l’instant t l’objectif le plus intéressant
 * et je l’accompli (un objectif a la fois) puis j’en repioche un
 *
 * - [OK] Tirer 2 objectifs jardinier       [TESTED]
 * - [OK] Tirer 2 objectifs parcelles      [TESTED]
 *
 * - [OK] Analyser les objectifs parcelle
 *     - [OK] Prendre la carte objectif jardinier ayant le pattern le plus petit
 *     - [OK] Regarder si on peut le résoudre
 * - [OK] Analyser les objectifs jardinier
 *     - [OK] Prendre la carte objectif avec le moins de bambou
 *     - [OK] Regarder si on peu le résoudre
 * - [OK] Regarder qui est le plus facile à résoudre entre les 2 objectifs choisies
 * - [OK] Si objectif parcelle plus facile à résoudre
 *     - [OK] Placer une case au bon endroit
 *     - [OK] Vérifier si l’objectif est valider
 *         - [OK] VALIDE :
 *             - [OK] On tire un nouvel objectif
 *             - [OK] On analyse a nouveau lequel est le plus facile à résoudre
 *         - [OK] NON VALIDE :
 *             - [OK] On analyse quel objectif est le plus facile à résoudre parmi la liste
 *                 - [OK] Si c’est toujours celui la, on essaye de le résoudre en rajoutant des cases
 *                 - [OK] Sinon, on passe à l’objectif le plus simple à résoudre
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
    private int numberOfResolveObjective = 0;

    private TurnLog turnLogger;

    public FourthBot(Game game) {
        super(game);
    }
    public FourthBot(Game game, String name, TurnLog log) { super(game, name); }

    @Override
    public void playTurn(TurnLog log, Weather weatherCard) {
        //action = BotActionType.NONE;


        // If it is the first game launch, we pick 2 (number in parameters) parcels objectives and 2 gardener objectives
        // (number in parameters). After this brackets, we will be not in the first launch, so we will try to analyse
        // directly the objectives.
        if (firstLaunch) {
            firstLaunchPickObjectives(log);
            placeAnParcelAnywhere(log);
            firstLaunch = false;
        } else {

            checkTheObjectives(log);

            // If we have any parcels on the game board, we need to place parcels


            // Now we will analyse the parcels objectives and put the more easier to the currentParcelObjective variable
            analyzeParcelsObjectives();
            analyzeGardenerObjectives();



            // Now we need to compare the easiest objective to resolve
            CardObjective easiestObjective = easiestObjectiveToResolve();

            // Now we will resolve the easiest objectives
            if (easiestObjective instanceof CardObjectiveParcel) {
                // We try to resolve the objective
                tryToResolveParcelObjective(log);

                // Now we check if the objective is completed
                if (checkObjective(easiestObjective)) {
                    // We select a new parcel objective
                    var obj = pickParcelObjective(turnLogger);

                    obj.ifPresent(cardObjectiveParcel -> log.addAction(BotActionType.PICK_PARCEL_GOAL, cardObjectiveParcel.toString()));
                }

            }
            if (easiestObjective instanceof CardObjectiveGardener) {

                // We try to resolve the objective
                tryToResolveGardenerObjective(log);

                // Now we check if the objective is completed
                if (checkObjective(easiestObjective)) {
                    // We select a new parcel objective
                    var obj = pickGardenerObjective(log);

                    obj.ifPresent(cardObjectiveGardener -> log.addAction(BotActionType.PICK_GARDENER_GOAL, cardObjectiveGardener.toString()));
                }

            }

        }
        //log.addAction(action, actionParameter);
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
    public boolean canPlay() {
        return numberOfResolveObjective <= 9;
    }

    /**
     * This function pick an new Parcel objective and add this objective to the player deck
     */
    void pickAnParcelObjectiveAndAddToPlayerBoard(TurnLog log) {
        var obj = pickParcelObjective(log);
        obj.ifPresent(cardObjectiveParcel -> log.addAction(BotActionType.PICK_PARCEL_GOAL, cardObjectiveParcel.toString()));
    }

    /**
     * This function pick an new Gardener objective and add this objective to the player deck
     */
    void pickAnGardenerObjectiveAndAddToPlayerBoard(TurnLog log) {
        var obj = pickGardenerObjective(log);
        obj.ifPresent(cardObjectiveGardener -> log.addAction(BotActionType.PICK_GARDENER_GOAL, cardObjectiveGardener.toString()));
    }

    /**
     * This function will check if we have currents objectives to resolve
     */
    void checkTheObjectives(TurnLog log) {
        int unfinishedParcelObjectives = getIndividualBoard().countUnfinishedParcelObjectives();
        int unfinishedGardenerObjectives = getIndividualBoard().countUnfinishedGardenerObjectives();

        if (unfinishedParcelObjectives <= 0) {
            pickAnParcelObjectiveAndAddToPlayerBoard(log);
            pickAnParcelObjectiveAndAddToPlayerBoard(log);
        }
        if (unfinishedGardenerObjectives <= 0) {
            pickAnGardenerObjectiveAndAddToPlayerBoard(log);
            pickAnGardenerObjectiveAndAddToPlayerBoard(log);
        }
        if (unfinishedParcelObjectives == 1) {
            pickAnParcelObjectiveAndAddToPlayerBoard(log);

        }
        if (unfinishedGardenerObjectives == 1) {
            pickAnGardenerObjectiveAndAddToPlayerBoard(log);

        }

    }

    /**
     * This function check the current objective
     * @return true if the currentObjective is in progress or false if there is any currentObjective or if
     * the currentObjective is finish
     */
    boolean checkObjective(CardObjective objective) {

        if (objective != null && !objective.verify()) {
            numberOfResolveObjective += 1;
        }
        // If the currentObjective == null, or if the current goal
        // is finish (the function verify return true if an objective is completed)
        return objective != null && !objective.verify();

        // Else, we return true because the objective in progress
    }

    /**
     * If it is the first game launch, we pick 2 (number in parameters) parcels objectives and 2 gardener objectives
     * (number in parameters)
     */
    void firstLaunchPickObjectives(TurnLog log) {
        for (int i = 0; i < NUMBER_OF_PARCEL_OBJECTIVES_AT_THE_START ; i++) {
            pickAnParcelObjectiveAndAddToPlayerBoard(log);
        }
        for (int i = 0; i < NUMBER_OF_GARDENER_OBJECTIVES_AT_THE_START ; i++) {
            pickAnGardenerObjectiveAndAddToPlayerBoard(log);
        }
    }

    /**
     * This function will analyze all the parcels objectives and put the easier parcel objective to the
     * currentParcelObjective variable
     */
    void analyzeParcelsObjectives() {
        // We put the unfinished parcels objectives into the variable unfinishedParcelsObjectives in order to
        // choose the easiest objective

        List<CardObjectiveParcel> unfinishedParcelsObjectives = getIndividualBoard().getUnfinishedParcelObjectives();


        // To determine if a parcel objective is easy to resolve, we will count the number of parcels the objectives need
        // Less this number is, more easy the objective will be resolve

        for (CardObjectiveParcel unfinishedParcelsObjective : unfinishedParcelsObjectives) {
            unfinishedParcelsObjective.verify();
        }


        if (unfinishedParcelsObjectives.size() >= 2) {

            if (unfinishedParcelsObjectives.get(0).getMissingPositionsToComplete().size() <=
                    unfinishedParcelsObjectives.get(1).getMissingPositionsToComplete().size()) {
                currentParcelObjective = unfinishedParcelsObjectives.get(0);
            } else {
                currentParcelObjective = unfinishedParcelsObjectives.get(1);
            }
        }

    }

    /**
     * This function will analyze all the gardeners objectives and put the easier parcel objective to the
     * currentParcelObjective variable
     */
    void analyzeGardenerObjectives() {
        List<CardObjectiveGardener> unfinishedGardenersObjectives = getIndividualBoard().getUnfinishedGardenerObjectives();




        for (CardObjectiveGardener unfinishedGardenersObjective : unfinishedGardenersObjectives) {
            unfinishedGardenersObjective.verify();
        }



        if (unfinishedGardenersObjectives.size() >= 2 ) {

            if (unfinishedGardenersObjectives.get(0).getSize() <= unfinishedGardenersObjectives.get(0).getSize()) {
                currentGardenerObjective = unfinishedGardenersObjectives.get(0);
            }

        }
    }


    static int value = 0;
    /**
     * This function will compare the currentGardenerObjective and currentParcelObjective to determine witch is
     * the easiest to resolve. The easier is the objective with the less score
     *
     */
    CardObjective easiestObjectiveToResolve() {

        //if (currentParcelObjective.getScore() <= currentGardenerObjective.getScore()) {
        if (value == 0) {
            value = 1;
            return currentParcelObjective;
        }
        value = 0;
        return currentGardenerObjective;

    }


    void tryToResolveParcelObjective(TurnLog log) {


        // We check if the game board is just composed of the pond (etang) or if we have more parcels
        if (board.getParcelCount() == 1) {
            // We need to place a parcel anywhere in the game board
            placeAnParcelAnywhere(log);
        } else {


            // We check the place where we can place a new parcel
            Set<Position> placeWhereWeCanPlaceAnParcel = null;
            if (currentParcelObjective != null) {
                placeWhereWeCanPlaceAnParcel = currentParcelObjective.getMissingPositionsToComplete()
                        .stream().map(PositionColored::getPosition).collect(Collectors.toSet());
            }
            ArrayList<Position> positionsWeChoose = new ArrayList<>();


            // We browse all the place where we can place a parcel and we add this positions to the ArrayList positionsWeChoose
            if (placeWhereWeCanPlaceAnParcel != null)
                placeWhereWeCanPlaceAnParcel.stream()
                        .filter(p -> board.isPlaceValid(p) && !p.equals(Config.POND_POSITION))
                        .forEach(positionsWeChoose::add);

            if(positionsWeChoose.size() != 0) {
                // We have an place to put the new parcel

                // We choose a random parcel in the potential list
                Random randomNumber = new Random();
                int position = randomNumber.nextInt(positionsWeChoose.size());

                // We add the new parcel
                board.addParcel(positionsWeChoose.get(position), new BambooPlantation(BambooColor.GREEN));

                log.addAction(BotActionType.PLACE_PARCEL, positionsWeChoose.get(position).toString());
            } else {
                // We put a parcel anywhere
                var position = placeAnParcelAnywhere(log);

                position.ifPresent(position1 -> log.addAction(BotActionType.PLACE_PARCEL, position1.toString()));
            }

        }
    }

    private <T> T getRandomIn(List<T> objs) {
        Random rnd = new Random();
        return objs.get(rnd.nextInt(objs.size()));
    }
    private void placeParcelSomewhere(Parcel parcel, TurnLog log) {
        List<Position> positions = new ArrayList<>(board.getValidPlaces());
        var position = getRandomIn(positions);
        board.addParcel(position, parcel);

        log.addAction(BotActionType.PLACE_PARCEL, position.toString());
    }

    void tryToResolveGardenerObjective(TurnLog log) {


        var position = board.getPositions().stream()
                .filter(p ->
                        !board.getParcel(p).isPond() &&
                                board.getParcel(p).getBambooColor().equals(currentGardenerObjective.getColor()) &&
                                board.getParcel(p).getBambooSize() < currentGardenerObjective.getSize())
                .max(Comparator.comparingInt( (Position p) -> board.getParcel(p).getBambooSize() ));

        if(position.isEmpty()) {
            // Aucune parcel ne peut resoudre l'objectif : soit la hauteur de bamboo est trop grande soit elle n'est pas de la bonne couleur.
            placeParcelSomewhere(new BambooPlantation(currentGardenerObjective.getColor()), log);
        }
        else {
            // si cette parcelle existe alors on bouge le jardinier dessus.
            board.getGardener().moveTo(position.get().getX(), position.get().getY(), position.get().getZ());
            log.addAction(BotActionType.MOVE_GARDENER, position.get().toString());
        }
    }

    @Override
    public String toString() {
        return "Bot 4";
    }
}