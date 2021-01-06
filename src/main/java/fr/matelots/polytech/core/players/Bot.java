package fr.matelots.polytech.core.players;

import fr.matelots.polytech.core.IllegalActionRepetitionException;
import fr.matelots.polytech.core.NoParcelLeftToPlaceException;
import fr.matelots.polytech.core.UnreachableParcelException;
import fr.matelots.polytech.core.game.Board;
import fr.matelots.polytech.core.game.Config;
import fr.matelots.polytech.core.game.Game;
import fr.matelots.polytech.core.game.goalcards.CardObjective;
import fr.matelots.polytech.core.game.goalcards.CardObjectiveGardener;
import fr.matelots.polytech.core.game.goalcards.CardObjectivePanda;
import fr.matelots.polytech.core.game.goalcards.CardObjectiveParcel;
import fr.matelots.polytech.core.game.goalcards.pattern.PositionColored;
import fr.matelots.polytech.core.game.movables.Gardener;
import fr.matelots.polytech.core.game.movables.Panda;
import fr.matelots.polytech.core.game.parcels.*;
import fr.matelots.polytech.core.players.bots.logger.BotActionType;
import fr.matelots.polytech.core.players.bots.logger.TurnLog;
import fr.matelots.polytech.engine.util.AbsolutePositionIrrigation;
import fr.matelots.polytech.engine.util.Position;

import java.util.*;

/**
 * @author Gabriel Cogne
 * @author D'Andrea William
 * @author Alexandre Arcil
 */
public abstract class Bot {

    private final Game game;
    protected final Board board;
    protected final IndividualBoard individualBoard;
    protected static final Random random = Config.RANDOM;
    private String name;
    protected int currentNumberOfAction;

    private BotActionType lastAction;
    protected final Panda panda;
    protected final Gardener gardener;

    public Bot(Game game, String name) {
        this(game);
        this.name = name;
    }

    public Bot(Game game) {
        this.game = game;
        this.board = game.getBoard();
        this.name = toString();
        this.individualBoard = new IndividualBoard();
        panda = board.getPanda();
        gardener = board.getGardener();
    }

    protected BotActionType getLastAction () {
        return lastAction;
    }

    /**
     * This method pick a new parcel objective from the pile of card and add this objective to the individual board
     * @return the objective that is picked
     */
    public Optional<CardObjective> pickParcelObjective(TurnLog log) {
        if (Config.isPickAction(lastAction))
            throw new IllegalActionRepetitionException();

        if(this.canDoAction()) {
            CardObjectiveParcel obj = game.getNextParcelObjective();
            if (obj == null) {
                return Optional.empty();
            }
            if (!individualBoard.addNewParcelObjective(obj)) {
                return Optional.empty();
            }
            log.addAction(BotActionType.PICK_PARCEL_GOAL, obj.toString());
            lastAction = BotActionType.PICK_PARCEL_GOAL;
            currentNumberOfAction++;
            return Optional.of(obj);
        }
        return Optional.empty();
    }

    /**
     * This method pick a new Gardener objective from the pile of card and add this objective to the individual board
     * @return the objective that is picked
     */
    public Optional<CardObjective> pickGardenerObjective(TurnLog log) {
        if (Config.isPickAction(lastAction))
            throw new IllegalActionRepetitionException();

        if(this.canDoAction()) {
            CardObjectiveGardener obj = game.getNextGardenerObjective();
            if (obj == null) {
                return Optional.empty();
            }
            if (!individualBoard.addNewGardenerObjective(obj)) {
                return Optional.empty();
            }
            log.addAction(BotActionType.PICK_GARDENER_GOAL, obj.toString());
            lastAction = BotActionType.PICK_GARDENER_GOAL;
            currentNumberOfAction++;
            return Optional.of(obj);
        }
        return Optional.empty();
    }

    /**
     * This method pick a new Panda objective from the pile of card and add this objective to the individual board
     * @return the objective that is picked
     */
    public Optional<CardObjective> pickPandaObjective(TurnLog log) {
        if (Config.isPickAction(lastAction))
            throw new IllegalActionRepetitionException();

        if (this.canDoAction()) {
            CardObjectivePanda obj = game.getNextPandaObjective();
            if (obj == null) {
                return Optional.empty();
            }
            if(!individualBoard.addNewPandaObjective(obj)) {
                return Optional.empty();
            }

            log.addAction(BotActionType.PICK_PANDA_GOAL, obj.toString());
            currentNumberOfAction++;
            lastAction = BotActionType.PICK_PANDA_GOAL;
            return Optional.of(obj);
        }
        return Optional.empty();
    }

    /**
     * This function check the current objective
     * @return true if the cardObjective is in progress or false if there is any currentObjective or if
     * the currentObjective is finish
     */
    public boolean checkObjective(Optional<CardObjective> cardObjective) {

        if (cardObjective == null) {
            throw new IllegalArgumentException("You can't check an Objective with a null value");
        }

        // If the currentObjective == null, or if the current goal
        // is finish (the function verify return true if an objective is completed)
        return cardObjective.isPresent() && !cardObjective.get().verify();
        // Else, we return true because the objective in progress
    }

    /**
     * This function check all the objectives
     */
    public void checkAllObjectives() {
        getIndividualBoard().checkAllGoal();
    }

    /**
     * Play a turn. He can't do more than {@link Config#TOTAL_NUMBER_OF_ACTIONS} actions.
     * @param log A logger to log action made
     */
    public void playTurn (TurnLog log) {
        lastAction = null;
    }

    public void playTurn() {
        TurnLog log = new TurnLog(this);
        playTurn(log);
        currentNumberOfAction = 0;
    }

    protected boolean movePanda(TurnLog log, Position pos) {
        if (BotActionType.MOVE_PANDA.equals(lastAction))
            throw new IllegalActionRepetitionException();

        panda.setCurrentPlayer(this);
        boolean res = panda.moveTo(pos.getX(), pos.getY(), pos.getZ());
        if (res) {
            log.addAction(BotActionType.MOVE_PANDA, pos.toString());
            lastAction = BotActionType.MOVE_PANDA;
        }

        return res;
    }

    protected boolean moveGardener(TurnLog log, Position pos) {
        if (BotActionType.MOVE_GARDENER.equals(lastAction))
            throw new IllegalActionRepetitionException();

        boolean res = gardener.moveTo(pos.getX(), pos.getY(), pos.getZ());
        if (res) {
            log.addAction(BotActionType.MOVE_GARDENER, pos.toString());
            lastAction = BotActionType.MOVE_GARDENER;
        }

        return res;
    }

    protected boolean placeParcel (TurnLog log, Position pos, Parcel parcel) {
        if (BotActionType.PLACE_PARCEL.equals(lastAction))
            throw new IllegalActionRepetitionException();

        boolean res = board.addParcel(pos, parcel);

        if (res) {
            log.addAction(BotActionType.PLACE_PARCEL, parcel.toString() + " in" + pos.toString());
            lastAction = BotActionType.PLACE_PARCEL;
        }

        return res;
    }

    protected void placeIrrigation (TurnLog log, Parcel parcel, Side side) {
        if (BotActionType.PLACE_IRRIGATION.equals(lastAction))
            throw new IllegalActionRepetitionException();

        parcel.setIrrigate(side);
        log.addAction(BotActionType.PLACE_IRRIGATION, parcel.toString() + " on " + side.toString());
        lastAction = BotActionType.PLACE_IRRIGATION;
    }

    protected void placeLayout (TurnLog log, BambooPlantation parcel, Layout layout) {
        if (BotActionType.PLACE_LAYOUT.equals(lastAction))
            throw new IllegalActionRepetitionException();

        if (parcel.setLayout(layout)) {
            log.addAction(BotActionType.PLACE_LAYOUT, layout.name() + " on " + parcel.toString());
            lastAction = BotActionType.PLACE_LAYOUT;
        } else {
            log.addAction(BotActionType.NONE, "");
        }
    }

    /**
     * Describe if the bot can continue to play. He needs to return false if he thinks he's blocked and can't play to
     * avoid a infinite loop.
     * @return true if the bot can continue to play, false otherwise
     */
    public abstract boolean canPlay();

    /**
     * This method will place a parcel anywhere in the board, the color of the new parcel is random
     * @return true if we have place a parcel, false else
     */
    public Optional<Position> placeAnParcelAnywhere(TurnLog log) {
        if (board.getParcelCount() < Config.MAX_PARCEL_ON_BOARD && this.canDoAction()) {
            // We get where we can put an parcel
            ArrayList<Position> validPositions = new ArrayList<>(board.getValidPlaces());
            // Now, we have an ArrayList of the potentials places where we can add a parcel

            // We choose a random parcel in the potential list
            int position = random.nextInt(validPositions.size());

            // We finally add to the board the new parcel
            Position pos = validPositions.get(position);
            BambooColor color = this.getRandomPlaceableColor();
            if (color != null) {
                board.addParcel(pos, new BambooPlantation(color));
                log.addAction(BotActionType.PLACE_PARCEL, pos.toString());
                currentNumberOfAction++;
                return Optional.of(pos);
            }
        }
        return Optional.empty();
    }


    /**
     * List<Parcel> availableParcels = board.pickParcels();
     *                 if (!availableParcels.isEmpty() && availableParcels != null) {
     *                     placeAnParcelAnywhere(turnLogger, availableParcels.get(random.nextInt(availableParcels.size())));
     *                 }
     * @param log
     * @param parcel
     * @return
     */
    public Optional<Position> placeAnParcelAnywhere(TurnLog log, Parcel parcel) {

        if (board.getParcelCount() < Config.MAX_PARCEL_ON_BOARD && this.canDoAction()) {
            // We get where we can put an parcel
            ArrayList<Position> validPositions = new ArrayList<>(board.getValidPlaces());
            // Now, we have an ArrayList of the potentials places where we can add a parcel

            // We choose a random parcel in the potential list
            int position = random.nextInt(validPositions.size());


            // We finally add to the board the new parcel
            Position pos = validPositions.get(position);

            placeParcel(pos, parcel.getBambooColor(), log);

        }
        return Optional.empty();
    }

    /**
     * @return A random bamboo color that can be place on the board, null otherwise
     */
    BambooColor getRandomPlaceableColor() {
        List<BambooColor> colors = new ArrayList<>();
        for(BambooColor color : BambooColor.values()) {
            if(this.board.getParcelLeftToPlace(color) > 0)
                colors.add(color);
        }
        Collections.shuffle(colors);
        return colors.isEmpty() ? null : colors.get(0);
    }

    /**
     * Check if the color can be placed on the board.
     * @param color The color to check
     * @return true if this color can be placed, false otherwise
     */
    boolean verifyIfWeCanPlaceAColoredParcel(BambooColor color) {
        switch (color) {
            case GREEN: {
                if (getBoard().getGreenParcelLeftToPlace() > 0) {
                    return true;
                }
                break;
            }

            case YELLOW:{
                if (getBoard().getYellowParcelLeftToPlace() > 0) {
                    return true;
                }
                break;
            }
            case PINK:{
                if (getBoard().getPinkParcelLeftToPlace() > 0) {
                    return true;
                }
                break;
            }
        }
        return false;
    }

    /**
     * This method will place a parcel anywhere in the board, the color of the new parcel is the color in parameter
     * @param color the color of the parcel we want
     * @return true if we have place a parcel, false else
     */
    public Optional<Position> placeAnParcelAnywhere(BambooColor color, TurnLog log) {
        if (board.getParcelCount() < Config.MAX_PARCEL_ON_BOARD && this.canDoAction()) {

            ArrayList<Position> placeWhereWeCanPlaceAnParcel = new ArrayList<>(board.getValidPlaces());

            int position = random.nextInt(placeWhereWeCanPlaceAnParcel.size());

            Position pos = placeWhereWeCanPlaceAnParcel.get(position);
            if (this.verifyIfWeCanPlaceAColoredParcel(color)) {
                if (this.board.addParcel(pos, new BambooPlantation(color))) {
                    this.currentNumberOfAction++;
                    log.addAction(BotActionType.PLACE_PARCEL, pos.toString());
                }
                return Optional.of(pos);
            }

            /*switch (color) {
                case PINK: {

                }

                case YELLOW: {
                    if (verifyIfWeCanPlaceAColoredParcel(color)) {
                        if (board.addParcel(pos, new BambooPlantation(color))) {
                            currentNumberOfAction++;
                            log.addAction(BotActionType.PLACE_PARCEL, pos.toString());
                        }
                        return Optional.of(pos);
                    }
                }


                case GREEN: {
                    if (verifyIfWeCanPlaceAColoredParcel(color)) {
                        if (board.addParcel(pos, new BambooPlantation(color))) {
                            currentNumberOfAction++;
                            log.addAction(BotActionType.PLACE_PARCEL, pos.toString());
                        }
                        return Optional.of(pos);
                    }
                }
            }*/
        }

        return Optional.empty();
    }

    /**
     * This method recover the missing positions to complete a Parcel Objective
     * @param card The parcel objective to verify
     * @return The missing position to complete the objective. Can be empty if he's completed.
     */
     public Set<PositionColored> recoverTheMissingsPositionsToCompleteForParcelObjective(CardObjectiveParcel card) {
         Set<PositionColored> missingPositionsToComplete = new HashSet<>();
         if (card != null) {
             card.verify();
             if (card.getMissingPositionsToComplete() != null) { //Normalement tjr false
                 missingPositionsToComplete.addAll(card.getMissingPositionsToComplete());
             }
         }
         return missingPositionsToComplete;
    }


    /**
     * Allow to place a parcel.
     * @param position: Position of the parcel to place
     * @param color: Color of the parcel
     * @param log: Logger par tour
     * @return true if the parcel is placed else return false
     */
    public boolean placeParcel(Position position, BambooColor color, TurnLog log) {
        if(!this.canDoAction()) return false;

        if (board.getParcelLeftToPlace(color) != 0) {
            try {

                var success = board.addParcel(position, new BambooPlantation(color));
                if (success) {
                    log.addAction(BotActionType.PLACE_PARCEL, position.toString());
                    currentNumberOfAction++;
                }
                return success;

            } catch (NoParcelLeftToPlaceException e) {
                return false;
            }
        }
       return false;
    }

    /**
     * @return true if the bot can do action, like placing a parcel, or false otherwise
     */
    public boolean canDoAction() {
        return currentNumberOfAction < Config.TOTAL_NUMBER_OF_ACTIONS;
    }

    public IndividualBoard getIndividualBoard() {
        return individualBoard;
    }

    public Board getBoard() {
        return board;
    }

    public void setCurrentNumberOfAction(int currentNumberOfAction) {
        this.currentNumberOfAction = currentNumberOfAction;
    }

    /**
     * Move the gardener at the position given in parameter. Nothing is done if he can't do an action or if the move
     * fail.
     * @param position The position where to move the gardener
     * @param log The logger
     */
    public boolean moveGardener(Position position, TurnLog log) {
        if(!this.canDoAction()) return false;
        boolean success;
        try {
            success = board.getGardener().moveTo(position.getX(), position.getY(), position.getZ());
            if(success) {
                currentNumberOfAction++;
                log.addAction(BotActionType.MOVE_GARDENER, position.toString());
            }
            return success;
        } catch (UnreachableParcelException e) {
            return false;
        }
    }

    /**
     * Place an irrigation on an edge
     * @param position: Absolute position of the edge
     * @return true if success, return false otherwise
     */
    public boolean irrigate(AbsolutePositionIrrigation position, TurnLog log) {
        if(!canDoAction()) return false;
        if(!individualBoard.canPlaceIrrigation()) return false;

        var success = position.irrigate();
        if(success) {
            log.addAction(BotActionType.PLACE_IRRIGATION, position.toString());
            individualBoard.placeIrrigation();
            currentNumberOfAction++;
        }
        return success;
    }

    public boolean pickIrrigation(TurnLog log) {
        if(!canDoAction()) return false;
        if(board.canPickIrrigation()) {
            board.pickIrrigation();
            individualBoard.addIrrigation();
            log.addAction(BotActionType.PICK_IRRIGATION, "");
            currentNumberOfAction++;
            return true;
        }
        else return false;
    }


    public String getName() {
        return name;
    }

    public int getCurrentNumberOfAction() {
        return currentNumberOfAction;
    }
}
