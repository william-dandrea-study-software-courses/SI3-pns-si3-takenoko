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
 */
public class QuintusBot extends Bot {
    private final Panda panda;
    private final Gardener gardener;
    private int turnLeftToPick;
    private int turnDoingNothing = 0;

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

        for ( ; action < Config.TOTAL_NUMBER_OF_ACTIONS; action++){
            // Do an action
            if (getIndividualBoard().countUnfinishedPandaObjectives() < 1) {
                turnLeftToPick = Math.max(1, turnLeftToPick);
            }

            neededColors = getNeededColor();

            if (turnLeftToPick > 0) {
                pickObjectif(log);
            }
            else if (!isThereAPlantationWhereYouCanEat()) {
                placeAParcel(log);
            }
            else if (!isThereAnythingInterestingToEat())
                moveGardener(log);
            else
                movePanda(log);
        }

        turnLeftToPick = Math.max(0, turnLeftToPick - 1);
        displayState();
    }

    private void pickObjectif (TurnLog log) {
        try {
            pickPandaObjective(log);
            neededColors = getNeededColor();
            turnDoingNothing = 0;
        } catch (PickDeckEmptyException e) {
            log.addAction(BotActionType.NONE, "");
            turnDoingNothing++;
        }
    }

    private void movePanda (TurnLog log) {
        List<Position> accessibles = board.getReachablePositionFrom(panda.getPosition());

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
    }

    private void moveGardener (TurnLog log) {
        List<Position> accessibles = board.getReachablePositionFrom(gardener.getPosition());

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
    }

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
    }

    private List<BambooColor> getNeededColor () {
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

    private List<CardObjectivePanda> getPandaObjectives () {
        return getIndividualBoard().getUnfinishedPandaObjectives();
    }

    private boolean isThereAPlantationWhereYouCanEat () {
        Set<Position> positions = board.getPositions();

        List<BambooColor> required = new ArrayList<>(neededColors);

        for (Position position : positions) {
            Parcel tmp = board.getParcel(position);
            assert tmp != null;
            if (tmp.isIrrigate())
                required.remove(tmp.getBambooColor());
        }

        return required.isEmpty();
    }

    private boolean isThereAnythingInterestingToEat() {
        Set<Position> positions = board.getPositions();

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
        return turnDoingNothing < MAX_TURN_DOING_NOTHING;
    }

    @Override
    public String getTurnMessage() {
        return getName();
    }

    private void checkObjectives () {
        getIndividualBoard().getUnfinishedPandaObjectives().forEach(obj -> {
            getIndividualBoard().verify(obj);
        });
    }

    private void displayState () {
        StringBuilder builder = new StringBuilder("Objectifs :\n");
        getIndividualBoard().getUnfinishedPandaObjectives().forEach(obj -> {
            builder.append("{green ")
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
                    .append(")\n");
        });
        builder.append("Needed colors :\n");
        neededColors.forEach(bambooColor -> builder.append(bambooColor).append("\n"));

        builder.append("Objectifs complets : ").append(getIndividualBoard().countCompletedObjectives());
        builder.append("\nScore : ").append(getIndividualBoard().getPlayerScore());

        Logger.getGlobal().info(builder.toString());
    }
}
