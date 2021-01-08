package fr.matelots.polytech.core.players;

import fr.matelots.polytech.core.IllegalActionRepetitionException;
import fr.matelots.polytech.core.UnreachableParcelException;
import fr.matelots.polytech.core.game.Board;
import fr.matelots.polytech.core.game.Config;
import fr.matelots.polytech.core.game.Game;
import fr.matelots.polytech.core.game.Weather;
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
import fr.matelots.polytech.engine.util.ShortestPathAlgorithm;

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

    // Weather
    private boolean playWithWeather;
    private int maxNumberOfActions;             // SUN
    private boolean canDoSameActionInOneTour;   // WIND
    private boolean canPlaceOneMoreBamboo;      // RAIN
    private boolean canMovePandaSomewhere;      // THUNDERSTORM
    private boolean canAddAmenagement;          // CLOUD

    public Bot(Game game, String name) {
        this(game);
        this.name = name;
    }

    public Bot(Game game) {
        this.game = game;
        this.board = game.getBoard();
        this.name = toString();
        this.individualBoard = new IndividualBoard();
        maxNumberOfActions = Config.TOTAL_NUMBER_OF_ACTIONS;
        panda = board.getPanda();
        gardener = board.getGardener();
        playWithWeather = false;
    }

    protected int getMaxNumberOfActions () {
        return maxNumberOfActions;
    }

    protected boolean canDoSameActionInOneTour () {
        return canDoSameActionInOneTour;
    }


    /**
     * Play a turn. He can't do more than {@link Config#TOTAL_NUMBER_OF_ACTIONS} actions.
     * @param log A logger to log action made
     * @param weatherCard
     */
    public void playTurn (TurnLog log, Weather weatherCard) {
        lastAction = null;
        currentNumberOfAction = 0;
        maxNumberOfActions = Config.TOTAL_NUMBER_OF_ACTIONS;    // SUN
        canDoSameActionInOneTour = false;                       // WIND
        canPlaceOneMoreBamboo = false;                          // RAIN
        canMovePandaSomewhere = false;                          // THUNDERSTORM
        canAddAmenagement = false;                              // CLOUD
        whatWeCanDoWithWeather(weatherCard, log);
    }

    public void playTurn(Weather weatherCard) {
        TurnLog log = new TurnLog(this);
        playTurn(log, weatherCard);
    }

    public void playTurn(TurnLog log) {
        playTurn(log, null);
    }


    protected void whatWeCanDoWithWeather(Weather weather, TurnLog log) {
        //System.out.println(weather);
        if (weather!= null) {
            switch (weather) {
                case SUN: {
                    maxNumberOfActions = 3;
                    break;
                }
                case RAIN: {
                    canPlaceOneMoreBamboo = true;
                    weatherCaseRainInitial();
                    break;
                }
                case WIND: {
                    canDoSameActionInOneTour = true;
                    break;
                }
                case CLOUD: {
                    canAddAmenagement = true;
                    weatherCaseCloudInitial();
                    break;
                }
                case THUNDERSTORM: {
                    canMovePandaSomewhere = true;
                    weatherCaseThunderstormInitial(log);
                    break;
                }
                case INTERROGATION: {
                    weatherCaseInterrogationInitial(log);
                    break;
                }
            }
        }
    }

    protected void weatherCaseRainInitial() {
        Optional<Position> place = board.getPositions().stream().filter(p -> board.getParcel(p).isIrrigate() && !board.getParcel(p).equals(Config.POND_POSITION)).findAny();
        place.ifPresent(position -> getBoard().getParcel(position).growBamboo());
    }

    protected void weatherCaseCloudInitial() {
        //getIndividualBoard().addLayouts(Layout.BASIN);
    }

    protected void weatherCaseThunderstormInitial(TurnLog log) {
        Optional<Position> place = board.getPositions().stream().filter(p -> !p.equals(getBoard().getPanda().getPosition())).findAny();
        place.ifPresent(position -> game.movePandaWhenWeather(lastAction, this, position, log));
    }
    protected void weatherCaseInterrogationInitial(TurnLog log) {
        playTurn(log, game.diceRandomWeather());
    }




    protected BotActionType getLastAction () {
        return lastAction;
    }

    public void setPlayWithWeather(boolean playWithWeather) {
        this.playWithWeather = playWithWeather;
    }

    /**
     * This method pick a new parcel objective from the pile of card and add this objective to the individual board
     * He can be empty if:
     *  - The deck is empty
     *  - The bot already done the maximum number of actions he can
     *  - The bot have the maximum number of objective cards in his individual board
     * @return the objective picked
     * @throws IllegalActionRepetitionException if the last action was already been of picking a objective card
     */
    public final Optional<CardObjectiveParcel> pickParcelObjective(TurnLog log) {
        if (Config.isPickAction(lastAction) && !canDoSameActionInOneTour)
            throw new IllegalActionRepetitionException();

        if(canDoAction()) {
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
    public final Optional<CardObjectiveGardener> pickGardenerObjective(TurnLog log) {
        if (Config.isPickAction(lastAction)  && !canDoSameActionInOneTour)
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
    public final Optional<CardObjectivePanda> pickPandaObjective(TurnLog log) {
        if (Config.isPickAction(lastAction)  && !canDoSameActionInOneTour)
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

    public final boolean movePanda(TurnLog log, Position pos) {
        if (BotActionType.MOVE_PANDA.equals(lastAction)  && !canDoSameActionInOneTour)
            throw new IllegalActionRepetitionException();

        panda.setCurrentPlayer(this);
        boolean res = panda.moveTo(pos.getX(), pos.getY(), pos.getZ());
        if (res) {
            log.addAction(BotActionType.MOVE_PANDA, pos.toString());
            lastAction = BotActionType.MOVE_PANDA;
            currentNumberOfAction++;
        }

        return res;
    }

    protected final boolean placeParcel (TurnLog log, Position pos, Parcel parcel) {
        if (BotActionType.PLACE_PARCEL.equals(lastAction)  && !canDoSameActionInOneTour)
            throw new IllegalActionRepetitionException();

        if (!canPlay() || currentNumberOfAction >= maxNumberOfActions) {
            return false;
        }

        boolean res = board.addParcel(pos, parcel);

        if (res) {
            log.addAction(BotActionType.PLACE_PARCEL, parcel.toString() + " in" + pos.toString());
            lastAction = BotActionType.PLACE_PARCEL;
            currentNumberOfAction++;
        }
        return res;
    }

    /**
     * Place un aménagement sur la parcelle. Il faut que le bot ai l'aménagement dans son plateau individuelle.
     * Une fois placé, l'aménagement est enlevé du plateau individuelle.
     * @param log Les logs
     * @param parcel La parcelle où placé l'aménagement
     * @param layout L'aménagement à placé
     * @return true si l'aménagement est placé, false sinon.
     */
    public final boolean placeLayout(TurnLog log, BambooPlantation parcel, Layout layout) {
        if(layout != null) {
            if (this.individualBoard.getLayouts().contains(layout) && parcel.setLayout(layout)) {
                this.individualBoard.getLayouts().remove(layout);
                log.addAction(BotActionType.PLACE_LAYOUT, layout.name() + " on " + parcel.toString());
                return true;
            }
        }
        log.addAction(BotActionType.NONE, "");
        return false;
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
        if (BotActionType.PLACE_PARCEL.equals(lastAction)  && !canDoSameActionInOneTour)
            throw new IllegalActionRepetitionException();

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
                lastAction = BotActionType.PLACE_PARCEL;
                return Optional.of(pos);
            }
        }
        return Optional.empty();
    }


    /**
     * List<Parcel> availableParcels = board.pickParcels();
     * if (!availableParcels.isEmpty() && availableParcels != null) {
     *      placeAnParcelAnywhere(turnLogger, availableParcels.get(random.nextInt(availableParcels.size())));
     * }
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

            return Optional.of(pos);

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
        if (BotActionType.PLACE_PARCEL.equals(lastAction)  && !canDoSameActionInOneTour)
            throw new IllegalActionRepetitionException();

        if (board.getParcelCount() < Config.MAX_PARCEL_ON_BOARD && this.canDoAction()) {

            ArrayList<Position> placeWhereWeCanPlaceAnParcel = new ArrayList<>(board.getValidPlaces());

            int position = random.nextInt(placeWhereWeCanPlaceAnParcel.size());

            Position pos = placeWhereWeCanPlaceAnParcel.get(position);
            if (this.verifyIfWeCanPlaceAColoredParcel(color)) {
                if (this.board.addParcel(pos, new BambooPlantation(color))) {
                    this.currentNumberOfAction++;
                    log.addAction(BotActionType.PLACE_PARCEL, pos.toString());
                    lastAction = BotActionType.PLACE_PARCEL;
                }
                return Optional.of(pos);
            }
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
        if (BotActionType.PLACE_PARCEL.equals(lastAction)  && !canDoSameActionInOneTour)
            throw new IllegalActionRepetitionException();
        return this.placeParcel(log, position, new BambooPlantation(color));
    }

    /**
     * @return true if the bot can do action, like placing a parcel, or false otherwise
     */
    public boolean canDoAction() {
        return currentNumberOfAction < maxNumberOfActions;
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
        if (BotActionType.MOVE_GARDENER.equals(lastAction)  && !canDoSameActionInOneTour)
            throw new IllegalActionRepetitionException();

        if(!this.canDoAction()) return false;
        boolean success;
        try {
            success = board.getGardener().moveTo(position.getX(), position.getY(), position.getZ());
            if(success) {
                currentNumberOfAction++;
                log.addAction(BotActionType.MOVE_GARDENER, position.toString());
                lastAction = BotActionType.MOVE_GARDENER;
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
        /*if (BotActionType.PLACE_IRRIGATION.equals(lastAction)  && !canDoSameActionInOneTour)
            throw new IllegalActionRepetitionException();*/

        if(!canDoAction()) return false;
        if(!individualBoard.canPlaceIrrigation()) return false;

        var success = position.irrigate();
        if(success) {
            log.addAction(BotActionType.PLACE_IRRIGATION, position.toString());
            individualBoard.placeIrrigation();
            //currentNumberOfAction++;
            lastAction = BotActionType.PLACE_IRRIGATION;
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

    


    public Optional<Layout> pickBasinLayout(TurnLog log) {
        if (!canAddAmenagement && !canDoSameActionInOneTour)
            throw new IllegalActionRepetitionException();

        if (this.canDoAction()) {
            Layout layout = game.getNextBasinLayout();
            if (layout == null) {
                return Optional.empty();
            }
            if(!individualBoard.addLayouts(layout)) {
                return Optional.empty();
            }
            return Optional.of(layout);
        }
        return Optional.empty();
    }
    public Optional<Layout> pickFertilizerLayout(TurnLog log) {
        if (!canAddAmenagement && !canDoSameActionInOneTour)
            throw new IllegalActionRepetitionException();

        if (this.canDoAction()) {
            Layout layout = game.getNextFertilizerLayout();
            if (layout == null) {
                return Optional.empty();
            }
            if(!individualBoard.addLayouts(layout)) {
                return Optional.empty();
            }
            return Optional.of(layout);
        }
        return Optional.empty();
    }
    public Optional<Layout> pickEnclosureLayout(TurnLog log) {
        if (!canAddAmenagement && !canDoSameActionInOneTour)
            throw new IllegalActionRepetitionException();

        if (this.canDoAction()) {
            Layout layout = game.getNextEnclosureLayout();
            if (layout == null) {
                return Optional.empty();
            }
            if(!individualBoard.addLayouts(layout)) {
                return Optional.empty();
            }
            return Optional.of(layout);
        }
        return Optional.empty();
    }

    /**
     * Donne une position où déplacer la panda ou jardinier pour atteindre la position souhaité en respectant leurs
     * contraintes de mouvement. Si le déplacement respecte déjà la contrainte, donne alors la position souhaité.
     * @param start Le position de départ
     * @param goal La position où aller
     * @return Une position intermédiaire pour atteindre la position souhaité, ou directement celle souhaité si possible
     */
    protected Position getStepMovePosition(Position start, Position goal) {
        List<Position> path = ShortestPathAlgorithm.shortestPath(start, goal, this.board);
        if(path.size() == 1 || path.size() == 2) {
            if(path.size() == 2)
                path.remove(start);
            return goal;
        }
        Side sideDirection = Side.getTouchedSide(path.get(0), path.get(1));
        for(int i = 2; i < path.size(); i++) {
            Side side = Side.getTouchedSide(path.get(i - 1), path.get(i));
            if(side != sideDirection) {
                return path.get(i - 1);
            }
        }
        return goal;
    }


    public String getName() {
        return name;
    }

    public int getCurrentNumberOfAction() {
        return currentNumberOfAction;
    }

    public boolean isCanMovePandaSomewhere() {
        return canMovePandaSomewhere;
    }

}
