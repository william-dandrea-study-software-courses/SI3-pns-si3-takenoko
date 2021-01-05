package fr.matelots.polytech.core.players.bots;

import fr.matelots.polytech.core.PickDeckEmptyException;
import fr.matelots.polytech.core.game.Config;
import fr.matelots.polytech.core.game.Game;
import fr.matelots.polytech.core.game.goalcards.CardObjectivePanda;
import fr.matelots.polytech.core.game.movables.Gardener;
import fr.matelots.polytech.core.game.movables.Panda;
import fr.matelots.polytech.core.game.parcels.BambooColor;
import fr.matelots.polytech.core.game.parcels.BambooPlantation;
import fr.matelots.polytech.core.game.parcels.Parcel;
import fr.matelots.polytech.core.players.Bot;
import fr.matelots.polytech.core.players.bots.logger.BotActionType;
import fr.matelots.polytech.core.players.bots.logger.TurnLog;
import fr.matelots.polytech.engine.util.Position;

import java.util.*;
import java.util.logging.Logger;

/**
 * This bot goal is to rush the panda objective.
 * Je pioche 4 objectifs panda
 *
 * @author williamdandrea
 * @author Gabriel Cogne
 */
public class QuintusBot extends Bot {
    private final Panda panda;
    private final Gardener gardener;
    private int turnLeftToPick;
    private int turnDoingNothing = 0;
    private int turnPastMovingGardener = 0;

    private BotActionType lastAction;

    private List<BambooColor> neededColors;

    private static final Random random = new Random();

    public QuintusBot(Game game, String name) {
        super(game, name);
        panda = board.getPanda();
        gardener = board.getGardener();
        turnLeftToPick = 2;
    }

    public QuintusBot(Game game) {
        this(game, "Quintus the Rush Panda");
    }

    @Override
    public void playTurn(TurnLog log) {
        setCurrentNumberOfAction(0);
        int action = 0;
        lastAction = null;

        for ( ; action < Config.TOTAL_NUMBER_OF_ACTIONS; action++){
            if (!canPlay())
                return;

            // Do an action
            if (getIndividualBoard().countUnfinishedPandaObjectives() < 1) {
                turnLeftToPick = Math.max(1, turnLeftToPick);
            }

            neededColors = getNeededColor();

            if (turnLeftToPick > 0 &&
                !(new ArrayList<>(List.of(BotActionType.PICK_GARDENER_GOAL, BotActionType.PICK_PANDA_GOAL,
                        BotActionType.PICK_PARCEL_GOAL))).contains(lastAction)) {
                pickObjectif(log);
            }
            else if (!isThereAPlantationWhereYouCanEat() &&
                        !BotActionType.PLACE_PARCEL.equals(lastAction)) {
                placeAParcel(log);
            }
            else if (!isThereAnythingInterestingToEat() &&
                        !BotActionType.MOVE_GARDENER.equals(lastAction))
                moveGardener(log);
            else if (!BotActionType.MOVE_PANDA.equals(lastAction))
                movePanda(log);
            else {
                turnDoingNothing++;
                log.addAction(BotActionType.NONE, "");
            }
        }

        if (log.getActions().length <= 0)
            turnDoingNothing++;

        turnLeftToPick = Math.max(0, turnLeftToPick - 1);
        //displayState();
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
            lastAction = BotActionType.PICK_PANDA_GOAL;
        } catch (PickDeckEmptyException e) {
            log.addAction(BotActionType.NONE, "");
            turnDoingNothing++;
        }
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

        panda.setCurrentPlayer(this);
        panda.moveTo(chosen.getX(), chosen.getY(), chosen.getZ());

        checkObjectives();
        log.addAction(BotActionType.MOVE_PANDA, chosen.toString());
        turnDoingNothing = 0;
        turnPastMovingGardener = 0;
        lastAction = BotActionType.MOVE_PANDA;

        final int MAX_EATEN_BEFORE_ERROR = 100;
        if (getIndividualBoard().getGreenEatenBamboo() > MAX_EATEN_BEFORE_ERROR
            || getIndividualBoard().getPinkEatenBamboo() > MAX_EATEN_BEFORE_ERROR
            || getIndividualBoard().getYellowEatenBamboo() > MAX_EATEN_BEFORE_ERROR)
            turnDoingNothing = 3;
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

        gardener.moveTo(chosen.getX(), chosen.getY(), chosen.getZ());
        log.addAction(BotActionType.MOVE_GARDENER, chosen.toString());
        turnDoingNothing = 0;
        turnPastMovingGardener++;
        lastAction = BotActionType.MOVE_GARDENER;
    }

    /**
     * Place a parcel of a needed color
     */
    private void placeAParcel (TurnLog log) {
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
        board.addParcel(chosen, new BambooPlantation(colors.get(0)));
        log.addAction(BotActionType.PLACE_PARCEL, chosen.toString());
        turnDoingNothing = 0;
        turnPastMovingGardener = 0;
        lastAction = BotActionType.PLACE_PARCEL;
    }

    /**
     * @return Return a list of all color needed to complete all objectives
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

        if (neededColors == null)
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
            return false;
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
        return turnDoingNothing < MAX_TURN_DOING_NOTHING && turnPastMovingGardener < MAX_TURN_MOVING_GARDENNER;
    }

    /**
     * See which objective is now complete
     */
    private void checkObjectives () {
        getIndividualBoard().getUnfinishedPandaObjectives().forEach(obj -> getIndividualBoard().verify(obj));
    }

    /**
     * Log the unfinished objectives, the needed colors, the number of objectives complete and the score
     */
    private void displayState () {
        StringBuilder builder = new StringBuilder("Objectifs :\n");
        getIndividualBoard().getUnfinishedPandaObjectives().forEach(obj -> builder.append("{green ")
                .append(obj.getCountForColor(BambooColor.GREEN))
                .append(" (")
                .append(getIndividualBoard().getGreenEatenBamboo())
                .append("), pink ")
                .append(obj.getCountForColor(BambooColor.PINK))
                .append(" (")
                .append(getIndividualBoard().getPinkEatenBamboo())
                .append("), yellow ")
                .append(obj.getCountForColor(BambooColor.YELLOW))
                .append(" (")
                .append(getIndividualBoard().getYellowEatenBamboo())
                .append(")\n"));
        builder.append("Needed colors :\n");
        neededColors.forEach(bambooColor -> builder.append(bambooColor).append("\n"));

        builder.append("Objectifs complets : ").append(getIndividualBoard().countCompletedObjectives());
        builder.append("\nScore : ").append(getIndividualBoard().getPlayerScore());

        Logger.getGlobal().info(builder.toString());
    }
}
