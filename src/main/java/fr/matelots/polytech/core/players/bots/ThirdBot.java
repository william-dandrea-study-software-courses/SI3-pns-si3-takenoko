package fr.matelots.polytech.core.players.bots;

import fr.matelots.polytech.core.game.Config;
import fr.matelots.polytech.core.game.Game;
import fr.matelots.polytech.core.game.Weather;
import fr.matelots.polytech.core.game.goalcards.CardObjectiveGardener;
import fr.matelots.polytech.core.game.goalcards.CardObjectiveParcel;
import fr.matelots.polytech.core.game.goalcards.pattern.PositionColored;
import fr.matelots.polytech.core.game.parcels.BambooColor;
import fr.matelots.polytech.core.game.parcels.Parcel;
import fr.matelots.polytech.core.players.Bot;
import fr.matelots.polytech.core.players.bots.logger.BotAction;
import fr.matelots.polytech.core.players.bots.logger.BotActionType;
import fr.matelots.polytech.core.players.bots.logger.TurnLog;
import fr.matelots.polytech.engine.util.AbsolutePositionIrrigation;
import fr.matelots.polytech.engine.util.Position;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

import static fr.matelots.polytech.engine.util.LineDrawer.lineOverVoid;
import static fr.matelots.polytech.engine.util.ParcelRouteFinder.getBestPathToIrrigate;
import static fr.matelots.polytech.engine.util.ParcelRouteFinder.getNextParcelToIrrigate;

public class ThirdBot extends Bot {

    private CardObjectiveParcel objectiveParcel;
    private CardObjectiveGardener objectiveGardener;

    private boolean filling = true;
    /// loop vars
    private TurnLog turnLog = null;

    /// constructors
    public ThirdBot(Game game) {
        super(game, "ThirdBot");
    }
    public ThirdBot(Game game, String name) {
        super(game, name);
    }

    /// Helping methods
    private void getNextParcelGoal() {
        getIndividualBoard().getUnfinishedParcelObjectives().stream()
                .filter(this::parcelObjectiveCanBeSolved) // Get solvable objectifs
                .findAny()
                .ifPresent(card -> objectiveParcel = card);
    }
    private void getNextGardenerGoal() {
        getIndividualBoard().getUnfinishedGardenerObjectives().stream()
                .filter(this::gardenerObjectiveCanBeSolved) // get solvable goal
                .findAny()
                .ifPresent(card -> objectiveGardener = card);
    }

    private boolean lastActionIsPlaceParcel() {
        return Arrays.stream(turnLog.getActions()).anyMatch(ba -> ba.getType() == BotActionType.PLACE_PARCEL);
    }

    private boolean parcelObjectiveCanBeSolved(CardObjectiveParcel objectiveParcel) {
        if(objectiveParcel.verify()) return false;
        if(!canPerformAction(BotActionType.PLACE_PARCEL)) return false;

        var missingTiles = objectiveParcel.getMissingPositionsToComplete();

        // controle qu'il y aura assez de parcel tout genre confondu
        if(missingTiles.stream().count() > board.getParcelLeftToPlace()) return false;

        var requiredColors = missingTiles.stream()
                .map(PositionColored::getColor).distinct();

        // Controle qu'il y aura assez de parcel de chaque couleur
        return
                // Can place at least a parcel
                missingTiles.stream().anyMatch(pc -> board.isPlaceValid(pc.getPosition())) &&
                // there is at least the number of required color
                requiredColors
                .map((BambooColor el) ->
                        objectiveParcel.getMissingPositionsToComplete().stream()
                            .filter(missing -> el == missing.getColor()).count() <= board.getParcelLeftToPlace(el))
                .reduce(true, (before, el) -> before & el);
    }

    private boolean isIrrigable(Position parcelPosition) {
        return getBestPathToIrrigate(board, parcelPosition).isPresent();
    }

    private boolean canPerformAction(BotActionType type) {
        if(turnLog == null) return true;
        if(Config.isPickAction(type)) {
            return !alreadyPickGoal();
        } else {
            return Arrays.stream(turnLog.getActions()).noneMatch(a -> a.getType() == type);
        }
    }

    private boolean alreadyPickGoal() {
        if(turnLog == null) return false;
        return Arrays.stream(turnLog.getActions()).map(BotAction::getType).anyMatch(Config::isPickAction);
    }

    private boolean gardenerObjectiveCanBeSolved(CardObjectiveGardener objectiveGardener) {
        if(objectiveGardener.verify()) return false;

        // 3 Case :
        //      1 - there is no tile of the good color, but can pose tile of the color
        //      2 - there is no tile of the good color irrigated
        //              a - reachable by Irrigations
        //      3 - there is no tile a tile of the good color and is irrigated
        //              a - reachable by the gardenner

        var bambooSize = objectiveGardener.getSize();
        var bambooColor = objectiveGardener.getColor();

        //      1 - there is no tile of the good color, but can pose tile of the color
        boolean canPlaceParcel = board.getParcelLeftToPlace(bambooColor) > 0 && canPerformAction(BotActionType.PLACE_PARCEL);
        if(canPlaceParcel)
            return true;

        //      2 - there is no tile of the good color irrigated
        //              a - reachable by Irrigations
        boolean goodColorAndReachableByIrrigation = canPerformAction(BotActionType.PLACE_IRRIGATION) &&
                (getIndividualBoard().canPlaceIrrigation() || board.canPickIrrigation()) &&
                board.getPositions().stream().anyMatch(position ->
                board.getParcel(position).getBambooColor() == bambooColor &&
                !board.getParcel(position).isIrrigate() &&
                isIrrigable(position) &&
                (board.canPickIrrigation() || getIndividualBoard().canPlaceIrrigation())
        );
        if(goodColorAndReachableByIrrigation)
            return true;

        //      3 - there is no tile a tile of the good color and is irrigated
        //              a - reachable by the gardenner
        boolean irrigatedAndReachableByGardenner = canPerformAction(BotActionType.MOVE_GARDENER) && board.getPositions().stream().anyMatch(position ->
                board.getParcel(position).isIrrigate() &&
                board.getParcel(position).getBambooColor() == bambooColor &&
                board.getParcel(position).getBambooSize() < bambooSize &&
                isReachableByGardener(position));
        if(irrigatedAndReachableByGardenner)
            return true;

        return false;
    }

    private boolean haveObjectiveSolvable() {
        if(haveParcelObjectiveSolvable()) return true;
        return haveGardenerObjectiveSolvable();
    }
    private boolean haveGardenerObjectiveSolvable() {
        var solvable = getIndividualBoard()
                .getUnfinishedGardenerObjectives().stream()
                .anyMatch(this::gardenerObjectiveCanBeSolved);
        return solvable;
    }
    private boolean haveParcelObjectiveSolvable() {
        if(!canPerformAction(BotActionType.PLACE_PARCEL)) return false;
        if(!board.getDeckParcel().canPick()) return false;

        return getIndividualBoard().getUnfinishedParcelObjectives().stream()
                .anyMatch(this::parcelObjectiveCanBeSolved);
    }

    private boolean canPickObjective() {
        var already = alreadyPickGoal();
        var canPickSomewhere = board.getDeckGardenerObjective().canPick() || board.getDeckParcelObjective().canPick();
        var havePlace = getIndividualBoard().countUnfinishedObjectives() < Config.MAX_NUMBER_OF_OBJECTIVES_CARD_IN_HAND;
        return havePlace
                && canPickSomewhere
                && !already;
    }

    private void pickObjective() {
        if(!canPickObjective()) return;

        var nParcel = getIndividualBoard().countUnfinishedParcelObjectives();
        var nGardener = getIndividualBoard().countUnfinishedGardenerObjectives();
        var tot = nParcel + nGardener;

        if(tot >= 5)
            filling = false;
        else {
            if(nParcel < 2 && board.getDeckParcelObjective().canPick() && canPerformAction(BotActionType.PICK_PARCEL_GOAL)) {
                var res = pickParcelObjective(turnLog);
                if(res.isPresent() && tot + 1 >= 5) filling = false;
            } else if(nGardener < 3 && board.getDeckGardenerObjective().canPick() && canPerformAction(BotActionType.PICK_GARDENER_GOAL)) {
                var res = pickGardenerObjective(turnLog);
                if(res.isPresent() && tot + 1 >= 5) filling = false;
            } else {
                if(board.getDeckGardenerObjective().canPick() && canPerformAction(BotActionType.PICK_GARDENER_GOAL)) {
                    pickGardenerObjective(turnLog);
                } else if(board.getDeckParcelObjective().canPick() && canPerformAction(BotActionType.PICK_PARCEL_GOAL)) {
                    pickParcelObjective(turnLog);
                } else filling = false;
            }
        }
    }

    private boolean isReachableByGardener(Position destination) {
        return getGardenerRoute(destination).isPresent();
    }

    private Optional<Position> getGardenerRoute(Position destination) {
        var gardenerPosition = board.getGardener().getPosition();

        var reachableFromEnd = board.getReachablePositionFrom(destination);
        if(reachableFromEnd.contains(gardenerPosition)) return Optional.of(destination);

        var reachableFromStart = board.getReachablePositionFrom(gardenerPosition);
        return reachableFromStart.stream().filter(reachableFromEnd::contains).findAny();
    }

    private Parcel ChooseParcel(BambooColor preferedColor) {
        var choice = board.pickParcels();
        var goodColor = choice.stream().filter(p -> p.getBambooColor() == preferedColor).findAny();
        return goodColor.orElseGet(() -> choice.get(Config.RANDOM.nextInt(choice.size())));
    }
    private Parcel ChooseParcelRandom() {
        var choice = board.pickParcels();
        return choice.get(Config.RANDOM.nextInt(choice.size()));
    }

    /// Core methods
    private void attemptSolveParcel() {
        if(!canPerformAction(BotActionType.PLACE_PARCEL)) return;
        // Supprime l'objectif si il est complété
        if(objectiveParcel != null && objectiveParcel.verify())
            objectiveParcel = null;

        // Check the goal exist
        if(objectiveParcel == null)
            getNextParcelGoal();
        if(objectiveParcel == null) return; /// il n'y a plus d'objectifs resolvables, on piochera donc d'autres objectifs parcels plus tard

        if(!parcelObjectiveCanBeSolved(objectiveParcel)) {
            getNextParcelGoal();
        }

        // On résout l'objectif
        var missingTiles = objectiveParcel.getMissingPositionsToComplete();

        Optional<PositionColored> opt = missingTiles.stream()
                .filter(positionColored -> board.isPlaceValid(positionColored.getPosition()))
                .findAny();

        // Peut on resoudre l'objectif immediatement
        if(opt.isPresent()) {
            var coloredPosition = opt.get();
            var tileColor = coloredPosition.getColor();
            var tilePosition = coloredPosition.getPosition();
            var parcelChoosen = ChooseParcel(tileColor);

            if(parcelChoosen.getBambooColor() == tileColor) {
                placeParcel(turnLog, tilePosition, parcelChoosen);
            } else {
                var validPlaceOpt = board.getValidPlaces().stream().findAny();
                validPlaceOpt.ifPresent(position -> placeParcel(turnLog, position, parcelChoosen));
            }
            //placeParcel(tilePosition, tileColor, turnLog);

        } else {
            //placeAnParcelAnywhere(turnLog);
            placeAnParcelAnywhere(turnLog, ChooseParcelRandom());
        }


    }

    private void attemptSolveGardener() {
        // Y a plein de get surligner mais c parce que soit la condition d'existance est déjà verifier dans le stream soit dans have solvable gardener objectif

        // L'objectif est il déjà complété ?
        if(objectiveGardener != null && objectiveGardener.verify())
            objectiveGardener = null;

        // On recupère la prochaine carte
        if(objectiveGardener == null)
            getNextGardenerGoal();
        if(objectiveGardener == null) {
            return;
        }
        if(!gardenerObjectiveCanBeSolved(objectiveGardener)) {
            objectiveGardener = getIndividualBoard().getUnfinishedGardenerObjectives().stream()
                    .filter(this::gardenerObjectiveCanBeSolved).findAny().get();
        }

        var gardenerPosition = board.getGardener().getPosition();

        // 3 Case :
        //      1 - there is no tile of the good color, but can pose tile of the color
        //      2 - there is no tile of the good color irrigated
        //              a - reachable by Irrigations
        //      3 - there is no tile a tile of the good color and is irrigated
        //              a - reachable by the gardenner


        //      3 - there is a tile of the good color and is irrigated
        //              a - reachable by the gardenner
        var tileIrrigatedReachableByGardener = board.getPositions().stream().filter(p ->
                board.getParcel(p).isIrrigate() &&
                board.getParcel(p).getBambooColor() == objectiveGardener.getColor() &&
                board.getParcel(p).getBambooSize() < objectiveGardener.getSize() &&
                isReachableByGardener(p)).findAny();

        if(tileIrrigatedReachableByGardener.isPresent() && canPerformAction(BotActionType.MOVE_GARDENER)) {
            var nextPosition = getGardenerRoute(tileIrrigatedReachableByGardener.get()).get();
            moveGardener(turnLog, nextPosition);
            return;
        }


        if(getIndividualBoard().canPlaceIrrigation()) {
            //      2 - there is no tile of the good color irrigated
            //              a - reachable by Irrigations
            var notIrrigatedTile = board.getPositions().stream()
                    .filter(p ->
                            board.getParcel(p).getBambooColor() == objectiveGardener.getColor() &&
                                    !board.getParcel(p).isIrrigate() &&
                                    isIrrigable(p)).findAny();

            if (notIrrigatedTile.isPresent() && canPerformAction(BotActionType.PLACE_IRRIGATION)) {
                // Irrigate parcel
                var path = getBestPathToIrrigate(board, notIrrigatedTile.get()).get();
                var opt = getNextParcelToIrrigate(path);
                /*AbsolutePositionIrrigation nextIrrigation = opt.orElseThrow(() -> {
                    //throw new RuntimeException("Error in path finding algorithm");
                });*/
                opt.ifPresent(api -> irrigate(api, turnLog));
                //irrigate(nextIrrigation, turnLog);
                return;
            }
        } else if(board.canPickIrrigation()) {
            pickIrrigation(turnLog);
            return;
        }

        // Place tile
        placeAnParcelAnywhere(objectiveGardener.getColor(), turnLog);
    }

    private int nothingDoneDuring = 30;
    void DecideAction(TurnLog log) {

        nothingDoneDuring = 30;
        checkAllObjectives();
        this.turnLog = log;

        var haveParcel = haveParcelObjectiveSolvable();
        var haveGardenner = haveGardenerObjectiveSolvable();

        var canPick = canPickObjective();
        if(filling && canPick) {
            pickObjective();
        } else if(haveParcel) {
            // Solve Objectif Parcel
            attemptSolveParcel();
        } else if(haveGardenner) {
            // Solve Objective Gardener
            attemptSolveGardener();
        } else if(canPickObjective()) {
            // pick objective
            filling = true;
        } else
            throw new RuntimeException("Can't play !");

        if(objectiveGardener != null)
            objectiveGardener.verify();
        if(objectiveParcel != null)
            objectiveParcel.verify();
    }

    /// interface
    @Override
    public void playTurn(TurnLog log, Weather weatherCard) {
        super.playTurn(log, weatherCard);
        setCurrentNumberOfAction(0);

        turnLog = log;
        while(canDoAction() && canPlayThisTurn())
            DecideAction(log);

        if(log.getActions().length == 0) {
            nothingDoneDuring--;
        }
        turnLog = null;
    }


    private boolean canPlayThisTurn() {
        checkAllObjectives();
        boolean canPick = canPickObjective();
        boolean haveObjectiveSolvable = haveObjectiveSolvable();
        return canPick || haveObjectiveSolvable;
    }

    @Override
    public boolean canPlay() {
        if(canPlayThisTurn()) return true;
        return !(turnLog == null || turnLog.getActions().length == 0); //nothingDoneDuring > 0;
    }

}
