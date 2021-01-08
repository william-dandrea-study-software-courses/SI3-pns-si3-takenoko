package fr.matelots.polytech.core.players.bots;

import fr.matelots.polytech.core.PickDeckEmptyException;
import fr.matelots.polytech.core.game.Config;
import fr.matelots.polytech.core.game.Game;
import fr.matelots.polytech.core.game.Weather;
import fr.matelots.polytech.core.game.goalcards.CardObjectiveGardener;
import fr.matelots.polytech.core.game.goalcards.CardObjectivePanda;
import fr.matelots.polytech.core.game.goalcards.CardObjectiveParcel;
import fr.matelots.polytech.core.game.parcels.BambooColor;
import fr.matelots.polytech.core.game.parcels.Parcel;
import fr.matelots.polytech.core.players.Bot;
import fr.matelots.polytech.core.players.bots.logger.BotActionType;
import fr.matelots.polytech.core.players.bots.logger.TurnLog;
import fr.matelots.polytech.engine.util.Position;

import java.util.*;

/**
 * This bot goal is to rush the panda objective.
 * Je pioche 4 objectifs panda
 *
 * @author williamdandrea
 * @author Gabriel Cogne
 */
public class QuintusBot extends Bot {
    private int turnLeftToPick;
    private int turnDoingNothing = 0;
    private int turnPastMovingGardener = 0;

    private List<BambooColor> neededColors;

    private static final Random random = Config.RANDOM;

    private final int[] lastState = new int[3];
    private int turnWithUnchangedState = 0;

    private final Game game;

    private boolean nextStep = false;

    public QuintusBot(Game game, String name) {
        super(game, name);
        this.game = game;
        turnLeftToPick = 5;
        Arrays.fill(lastState, 0);
    }

    public QuintusBot(Game game) {
        this(game, "Jojo (Rush Panda)");
    }

    @Override
    public void playTurn(TurnLog log, Weather weatherCard) {
        super.playTurn(log, weatherCard);

        setCurrentNumberOfAction(0);
        int action = 0;

        for ( ; action < getMaxNumberOfActions(); action++){
            if (!canPlay())
                return;

            if (nextStep) {
                // There is no more panda objective to resolve
                // So I'm resolving gardener objective instead
                if (turnLeftToPick > 0 &&
                        (!(Config.isPickAction(getLastAction()))
                        || canDoSameActionInOneTour())) {
                    pickNextStepObjective(log);
                }
                else if (!isThereAPlantationWhereYouCanEat() &&
                        (!BotActionType.PLACE_PARCEL.equals(getLastAction())
                        || canDoSameActionInOneTour())) {
                    placeAParcel(log);
                }
                else if (!isThereAnythingInterestingToEat() &&
                        (!BotActionType.MOVE_GARDENER.equals(getLastAction())
                        || canDoSameActionInOneTour()))
                    moveGardener(log);
                else {
                    turnDoingNothing++;
                    log.addAction(BotActionType.NONE, "");
                }
                checkObjectives();
                continue;
            }

            // Do an action
            if (getIndividualBoard().countUnfinishedPandaObjectives() < 1) {
                turnLeftToPick = Math.max(2, turnLeftToPick);
            }

            neededColors = getNeededColor();

            if (turnLeftToPick > 0 &&
                !(Config.isPickAction(getLastAction()))) {
                pickObjectif(log);
            }
            else if (!isThereAPlantationWhereYouCanEat() &&
                        !BotActionType.PLACE_PARCEL.equals(getLastAction())) {
                placeAParcel(log);
            }
            else if (!isThereAnythingInterestingToEat() &&
                        !BotActionType.MOVE_GARDENER.equals(getLastAction()))
                moveGardener(log);
            else if (!BotActionType.MOVE_PANDA.equals(getLastAction()))
                movePanda(log);
            else {
                turnDoingNothing++;
                log.addAction(BotActionType.NONE, "");
            }
        }

        if (log.getActions().length <= 0)
            turnDoingNothing++;

        turnLeftToPick = Math.max(0, turnLeftToPick - 1);
    }

    @Override
    protected void weatherCaseThunderstormInitial(TurnLog log) {
        if (neededColors == null || neededColors.isEmpty()) {
            neededColors = getNeededColor();
        }

        Position chosen = null;
        if (!neededColors.isEmpty()) {
            for (Position pos : board.getPositions()) {
                Parcel p = board.getParcel(pos);
                if (p != null &&
                        neededColors.get(0).equals(p.getBambooColor()) &&
                        p.getBambooSize() > 0) {
                    chosen = pos;
                    break;
                }
            }
        }
        if (chosen == null) {
            chosen = (new ArrayList<>(board.getPositions())).get(random.nextInt(board.getPositions().size()));
        }
        game.movePandaWhenWeather(getLastAction(), this, chosen, log);
    }

    @Override
    protected void weatherCaseRainInitial() {
        if (neededColors == null || neededColors.isEmpty()) {
            neededColors = getNeededColor();
        }

        Parcel chosen = null;
        if (!neededColors.isEmpty()) {
            for (Position pos : board.getPositions()) {
                Parcel p = board.getParcel(pos);
                if (p != null &&
                        neededColors.get(0).equals(p.getBambooColor()) &&
                        p.getBambooSize() > 0) {
                    chosen = p;
                    break;
                }
            }
        }
        if (chosen == null) {
            chosen = board.getParcel(
                    (new ArrayList<>(board.getPositions())).get(random.nextInt(board.getPositions().size())));
        }
        chosen.growBamboo();
    }

    @Override
    protected void weatherCaseInterrogationInitial(TurnLog log) {
        if (neededColors == null || neededColors.isEmpty()) {
            neededColors = getNeededColor();
        }

        if (nextStep) {
            whatWeCanDoWithWeather(Weather.RAIN, log);
        }
        else {
            if (isThereAnythingInterestingToEat()) {
                whatWeCanDoWithWeather(Weather.THUNDERSTORM, log);
            }
            else {
                whatWeCanDoWithWeather(Weather.RAIN, log);
            }
        }
    }

    /**
     * Pick a panda objective if possible or do nothing
     */
    void pickObjectif (TurnLog log) {
        try {
            pickPandaObjective(log);
            neededColors = getNeededColor();
            turnDoingNothing = 0;
            turnPastMovingGardener = 0;
        } catch (PickDeckEmptyException e) {
            if (getIndividualBoard().countCompletedObjectives() -
                    Config.getNbObjectivesToCompleteForLastTurn(game.getBots().size()) > 0) {
                nextStep = true;
                turnWithUnchangedState = 0;
                pickNextStepObjective(log);
                return;
            }

            log.addAction(BotActionType.NONE, "");
            turnDoingNothing++;
        }
    }

    private void pickNextStepObjective (TurnLog log) {
        int dice = random.nextInt(2);
        if (dice >= 1) {
            pickGardenerObjective(log);
            neededColors = getNeededColorsNextStep();
        }
        else {
            pickParcelObjective(log);
            neededColors = new ArrayList<>(List.of(BambooColor.values()));
            Collections.shuffle(neededColors);
        }
        turnDoingNothing = 0;
        turnPastMovingGardener = 0;
    }

    /**
     * Move the panda on a new Parcel
     */
    void movePanda (TurnLog log) {
        List<Position> accessibles = board.getReachablePositionFrom(panda.getPosition());
        accessibles.remove(panda.getPosition());

        Position chosen = null;

        for (Position position : accessibles) {
            Parcel tmp = board.getParcel(position);
            assert tmp != null;
            if (neededColors.contains(tmp.getBambooColor())) {
                chosen = position;
                break;
            }
        }

        if (chosen == null)
            chosen = accessibles.get(random.nextInt(accessibles.size()));

        super.movePanda(log, chosen);
        turnDoingNothing = 0;
        turnPastMovingGardener = 0;
        checkObjectives();

        checkState();

        final int MAX_EATEN_BEFORE_ERROR = 100;
        if (getIndividualBoard().getGreenEatenBamboo() > MAX_EATEN_BEFORE_ERROR
            || getIndividualBoard().getPinkEatenBamboo() > MAX_EATEN_BEFORE_ERROR
            || getIndividualBoard().getYellowEatenBamboo() > MAX_EATEN_BEFORE_ERROR)
            turnDoingNothing = 3;
    }

    private void checkState () {
        boolean changed = false;
        if (lastState[BambooColor.GREEN.ordinal()] != getIndividualBoard().getGreenEatenBamboo()) {
            changed = true;
        }
        else if (lastState[BambooColor.PINK.ordinal()] != getIndividualBoard().getPinkEatenBamboo()) {
            changed = true;
        }
        else if (lastState[BambooColor.YELLOW.ordinal()] != getIndividualBoard().getYellowEatenBamboo()) {
            changed = true;
        }

        if (changed) {
            turnWithUnchangedState = 0;
            lastState[BambooColor.GREEN.ordinal()] = getIndividualBoard().getGreenEatenBamboo();
            lastState[BambooColor.PINK.ordinal()] = getIndividualBoard().getPinkEatenBamboo();
            lastState[BambooColor.YELLOW.ordinal()] = getIndividualBoard().getYellowEatenBamboo();
        }
        else {
            turnWithUnchangedState++;
        }
    }

    /**
     * Move the gardener on a new parcel
     */
    void moveGardener (TurnLog log) {
        List<Position> accessibles = board.getReachablePositionFrom(gardener.getPosition());
        accessibles.remove(gardener.getPosition());

        Position chosen = null;

        for (Position position : accessibles) {
            Parcel tmp = board.getParcel(position);
            assert tmp != null;
            if (neededColors.contains(tmp.getBambooColor())) {
                chosen = position;
                break;
            }
        }

        if (chosen == null)
            chosen = accessibles.get(random.nextInt(accessibles.size()));

        super.moveGardener(chosen, log);
        turnDoingNothing = 0;
        turnPastMovingGardener++;
    }

    /**
     * Place a parcel of a needed color
     */
    void placeAParcel (TurnLog log) {
        final List<Position> placeablePositions = new ArrayList<>();
        board.getPositions().forEach(position ->
                Config.CUBE_DIRECTIONS.forEach(direction -> {
                    Position tmp = position.add(direction);
                    if (!board.containTile(tmp) && board.isPlaceValid(tmp)
                            && !placeablePositions.contains(tmp)) {
                        placeablePositions.add(tmp);
                    }
                }));

        Set<Position> positions = board.getPositions();
        List<BambooColor> colors = new ArrayList<>(List.of(BambooColor.values()));
        for (Position position : positions) {
            Parcel tmp = board.getParcel(position);
            assert tmp != null;
            colors.remove(tmp.getBambooColor());
        }
        if (colors.isEmpty()) {
            return;
        }

        Position chosen = placeablePositions.get(random.nextInt(placeablePositions.size()));
        Parcel chosenParcel = null;

        List<Parcel> placeable = board.pickParcels();

        if (placeable.isEmpty()) {
            log.addAction(BotActionType.NONE, "");
            turnDoingNothing++;
            return;
        }

        for (Parcel p : placeable) {
            if (colors.get(0).equals(p.getBambooColor())) {
                chosenParcel = p;
                break;
            }
        }
        if (chosenParcel == null) {
            chosenParcel = placeable.get(random.nextInt(placeable.size()));
        }

        super.placeParcel(log, chosen, chosenParcel);
        turnDoingNothing = 0;
        turnPastMovingGardener = 0;
    }

    /**
     * @return Return a list of all color needed to complete all panda objectives
     */
    List<BambooColor> getNeededColor () {
        final int[] count = new int[BambooColor.values().length];
        Arrays.fill(count, 0);
        getPandaObjectives().forEach(objective -> {
            for (BambooColor color : BambooColor.values())
                count[color.ordinal()] += objective.getCountForColor(color);
        });

        count[BambooColor.GREEN.ordinal()] -= getIndividualBoard().getGreenEatenBamboo();
        count[BambooColor.PINK.ordinal()] -= getIndividualBoard().getPinkEatenBamboo();
        count[BambooColor.YELLOW.ordinal()] -= getIndividualBoard().getYellowEatenBamboo();

        List<BambooColor> colors = new ArrayList<>();
        for (BambooColor color : BambooColor.values())
            if (count[color.ordinal()] > 0)
                colors.add(color);
        return colors;
    }

    /**
     * @return Return a list of all color needed to complete all gardener objectives
     */
    List<BambooColor> getNeededColorsNextStep () {
        List<BambooColor> colors = new ArrayList<>();
        for (var obj : getIndividualBoard().getUnfinishedGardenerObjectives()) {
            if (!colors.contains(obj.getColor())) {
                colors.add(obj.getColor());
            }
        }
        return colors;
    }

    /**
     * @return Short access to panda objectives in individual board
     */
    private List<CardObjectivePanda> getPandaObjectives () {
        return getIndividualBoard().getUnfinishedPandaObjectives();
    }

    /**
     * @return if there is a plantation matching to all needed colors
     */
    boolean isThereAPlantationWhereYouCanEat () {
        Set<Position> positions = board.getPositions();

        if (neededColors == null
            || getIndividualBoard().countUnfinishedParcelObjectives() > 0)
            return false;

        List<BambooColor> required = new ArrayList<>(neededColors);

        for (Position position : positions) {
            Parcel tmp = board.getParcel(position);
            assert tmp != null;
            if (tmp.isIrrigate())
                required.remove(tmp.getBambooColor());
        }

        return required.isEmpty();
    }

    /**
     * @return if there is at least one bamboo to eat that match to the needs of an objective
     */
    boolean isThereAnythingInterestingToEat() {
        Set<Position> positions = board.getPositions();

        if (neededColors == null) {
            neededColors = getNeededColor();
            if (neededColors.isEmpty()) {
                return false;
            }
        }

        for (Position position : positions) {
            Parcel tmp = board.getParcel(position);
            assert tmp != null;
            if (neededColors.contains(tmp.getBambooColor()) && tmp.getBambooSize() > 0) {
                return true;
            }
        }

        return false;
    }

    @Override
    public boolean canPlay() {
        final int MAX_TURN_DOING_NOTHING = 3;
        final int MAX_TURN_MOVING_GARDENNER = 6;
        final int MAX_TURN_STATE_UNCHANGING = 9;
        return turnDoingNothing < MAX_TURN_DOING_NOTHING
                && turnPastMovingGardener < MAX_TURN_MOVING_GARDENNER
                && turnWithUnchangedState < MAX_TURN_STATE_UNCHANGING;
    }

    /**
     * See which objective is now complete
     */
    private void checkObjectives () {
        getIndividualBoard().getUnfinishedPandaObjectives().forEach(obj -> getIndividualBoard().verify(obj));
        getIndividualBoard().getUnfinishedGardenerObjectives().forEach(CardObjectiveGardener::verify);
        getIndividualBoard().getUnfinishedParcelObjectives().forEach(CardObjectiveParcel::verify);
    }
}
