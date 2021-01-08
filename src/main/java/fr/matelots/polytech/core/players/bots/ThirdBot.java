package fr.matelots.polytech.core.players.bots;

import fr.matelots.polytech.core.game.Config;
import fr.matelots.polytech.core.game.Game;
import fr.matelots.polytech.core.game.Weather;
import fr.matelots.polytech.core.game.goalcards.CardObjectiveGardener;
import fr.matelots.polytech.core.game.goalcards.CardObjectiveParcel;
import fr.matelots.polytech.core.game.goalcards.pattern.PositionColored;
import fr.matelots.polytech.core.game.parcels.BambooColor;
import fr.matelots.polytech.core.game.parcels.BambooPlantation;
import fr.matelots.polytech.core.game.parcels.Parcel;
import fr.matelots.polytech.core.players.Bot;
import fr.matelots.polytech.core.players.bots.logger.BotAction;
import fr.matelots.polytech.core.players.bots.logger.BotActionType;
import fr.matelots.polytech.core.players.bots.logger.TurnLog;
import fr.matelots.polytech.engine.util.Position;

import java.util.Arrays;
import java.util.Optional;

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
                .filter(this::canSolveParcelObjective) // Get solvable objectifs
                .findAny()
                .ifPresent(card -> objectiveParcel = card);
    }
    private void getNextGardenerGoal() {
        getIndividualBoard().getUnfinishedGardenerObjectives().stream()
                .filter(this::canSolveGardenerObjective) // get solvable goal
                .findAny()
                .ifPresent(card -> objectiveGardener = card);
    }

    boolean canSolveParcelObjective(CardObjectiveParcel objectiveParcel) {
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

    boolean canSolveGardenerObjective(CardObjectiveGardener objectiveGardener) {
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
        //              b - has no layout or there is the good layout

        boolean goodColorAndReachableByIrrigation = canPerformAction(BotActionType.PLACE_IRRIGATION) &&
                (getIndividualBoard().canPlaceIrrigation() || board.canPickIrrigation()) &&
                board.getPositions().stream().anyMatch(position ->
                    board.getParcel(position).getBambooColor() == bambooColor &&            // Good color
                    !board.getParcel(position).isIrrigate() &&                              // Position irrigate
                    (                                                                       // Check layouts
                            objectiveGardener.getLayout() == null ||                                 // Check objectiveCard has no layout
                            board.getParcel(position).getLayout() == null ||
                            board.getParcel(position).getLayout() == objectiveGardener.getLayout()
                    ) &&
                    isIrrigable(position) &&
                    (board.canPickIrrigation() || getIndividualBoard().canPlaceIrrigation())
        );
        if(goodColorAndReachableByIrrigation)
            return true;

        //      3 - there is no tile a tile of the good color and is irrigated
        //              a - reachable by the gardenner
        //              b - has no layout or there is the good layout

        boolean irrigatedAndReachableByGardenner = canPerformAction(BotActionType.MOVE_GARDENER) && board.getPositions().stream().anyMatch(position ->
                board.getParcel(position).isIrrigate() &&
                board.getParcel(position).getBambooColor() == bambooColor &&
                board.getParcel(position).getBambooSize() < bambooSize &&
                (                                                                       // Check layouts
                        (objectiveGardener.getLayout() == null ||                                 // Check objectiveCard has no layout
                                board.getParcel(position).getLayout() == null ||
                                board.getParcel(position).getLayout() == objectiveGardener.getLayout()) &&
                            getIndividualBoard().getLayouts().contains(objectiveGardener.getLayout())
                ) &&
                isReachableByGardener(position));
        if(irrigatedAndReachableByGardenner)
            return true;

        return false;
    }

    boolean haveObjectiveSolvable() {
        if(haveParcelObjectiveSolvable())
            return true;
        boolean haveGardenerSolvable = haveGardenerObjectiveSolvable();
        return haveGardenerSolvable;
    }
    boolean haveGardenerObjectiveSolvable() {
        var solvable = getIndividualBoard()
                .getUnfinishedGardenerObjectives().stream()
                .anyMatch(this::canSolveGardenerObjective);
        return solvable;
    }
    boolean haveParcelObjectiveSolvable() {
        if(!canPerformAction(BotActionType.PLACE_PARCEL)) return false;
        if(!board.getDeckParcel().canPick()) return false;

        return getIndividualBoard().getUnfinishedParcelObjectives().stream()
                .anyMatch(this::canSolveParcelObjective);
    }

    boolean canPickObjective() {
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

        if(!canSolveParcelObjective(objectiveParcel)) {
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
        if(!canSolveGardenerObjective(objectiveGardener)) {
            objectiveGardener = getIndividualBoard().getUnfinishedGardenerObjectives().stream()
                    .filter(this::canSolveGardenerObjective).findAny().get();
        }

        var gardenerPosition = board.getGardener().getPosition();

        // 3 Case :
        //      1 - there is no tile of the good color, but can pose tile of the color
        //      2 - there is no tile of the good color irrigated
        //              a - reachable by Irrigations
        //      3 - there is no tile a tile of the good color and is irrigated
        //              a - reachable by the gardenner


        /*
            Check if there is a tile needing layout
         */
        if(objectiveGardener.getLayout() != null &&
            getIndividualBoard().getLayouts().contains(objectiveGardener.getLayout()) &&
            board.getPositions().stream().map(board::getParcel).noneMatch(p ->
                    p.getBambooColor() == objectiveGardener.getColor() &&
                    p.getBambooSize() == objectiveGardener.getSize() &&
                    p.getLayout() == objectiveGardener.getLayout()
            )
        ) {
            var tile = board.getPositions().stream().map(board::getParcel).filter(p ->
                    p.getBambooColor() == objectiveGardener.getColor() &&
                    p.getBambooSize() == objectiveGardener.getSize() &&
                    p.getLayout() == null &&
                    p instanceof BambooPlantation).map(p -> (BambooPlantation)p).findAny();

            tile.ifPresent(val -> {
                placeLayout(turnLog, val, objectiveGardener.getLayout());
            });
            return;
        }


        //      3 - there is a tile of the good color and is irrigated
        //              a - reachable by the gardenner
        //              b - Good layout
        var tileIrrigatedReachableByGardener = board.getPositions().stream().filter(p ->
                board.getParcel(p).isIrrigate() &&
                board.getParcel(p).getBambooColor() == objectiveGardener.getColor() &&
                board.getParcel(p).getBambooSize() < objectiveGardener.getSize() &&
                (
                        objectiveGardener.getLayout() == null ||
                        board.getParcel(p).getLayout() == null ||
                        board.getParcel(p).getLayout() == objectiveGardener.getLayout()
                ) &&
                isReachableByGardener(p)
        ).findAny();

        if(tileIrrigatedReachableByGardener.isPresent() && canPerformAction(BotActionType.MOVE_GARDENER)) {
            var nextPosition = getGardenerRoute(tileIrrigatedReachableByGardener.get()).get();
            moveGardener(nextPosition, turnLog);
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

    private int nothingDoneDuring = 50;
    private int duringTurn = 5;
    void DecideAction(TurnLog log) {

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

        if(!canPlay()) return;

        turnLog = log;
        duringTurn = 20;
        while(canDoAction() && canPlayThisTurn() && duringTurn >= 0) {
            int qOfAction = turnLog.getActions().length;
            DecideAction(log);
            boolean actionDone = (turnLog.getActions().length - qOfAction) > 0;
            if(!actionDone) duringTurn--;
        }

        if(turnLog.getActions().length == 0)
            nothingDoneDuring--;
        else
            nothingDoneDuring = 20;

        turnLog = null;
    }


    boolean canPlayThisTurn() {
        checkAllObjectives();
        boolean canPick = canPickObjective();
        boolean haveObjectiveSolvable = haveObjectiveSolvable();
        return canPick || haveObjectiveSolvable;
    }

    @Override
    public boolean canPlay() {
        if((turnLog == null || turnLog.getActions().length == 0) && !canPlayThisTurn()) return false;
        return nothingDoneDuring >= 0;
    }

}
