package fr.matelots.polytech.core.players;

import fr.matelots.polytech.core.NoParcelLeftToPlaceException;
import fr.matelots.polytech.core.game.Board;
import fr.matelots.polytech.core.game.Config;
import fr.matelots.polytech.core.game.Game;
import fr.matelots.polytech.core.game.goalcards.CardObjective;
import fr.matelots.polytech.core.game.goalcards.CardObjectiveGardener;
import fr.matelots.polytech.core.game.goalcards.CardObjectivePanda;
import fr.matelots.polytech.core.game.goalcards.CardObjectiveParcel;
import fr.matelots.polytech.core.game.goalcards.pattern.PositionColored;
import fr.matelots.polytech.core.game.parcels.BambooColor;
import fr.matelots.polytech.core.game.parcels.BambooPlantation;
import fr.matelots.polytech.core.game.parcels.Parcel;
import fr.matelots.polytech.core.players.bots.logger.BotActionType;
import fr.matelots.polytech.core.players.bots.logger.TurnLog;
import fr.matelots.polytech.engine.util.Position;

import javax.swing.text.html.Option;
import java.util.*;
import java.util.logging.Logger;

/**
 * @author Gabriel Cogne
 * @author D'Andrea William
 */
public abstract class Bot {

    private final Game game;
    protected Board board;
    protected final IndividualBoard individualBoard;
    private static final Random random = new Random();
    private String name;
    private int numberOfParcelsGreenInTheGame = 0;
    private int numberOfParcelsYellowInTheGame = 0;
    private int numberOfParcelsPinkInTheGame = 0;


    protected int currentNumberOfAction;

    public Bot(Game game, String name) {
        this(game);
        this.name = name;
    }

    public Bot(Game game) {
        this.game = game;
        this.board = game.getBoard();
        individualBoard = new IndividualBoard();
        name = toString();
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
     * This method pick a new parcel objective from the pile of card and add this objective to the individual board
     * @return the objective that we pick
     */
    public Optional<CardObjective> pickParcelObjective(TurnLog log) {
        if (canDoAction()) {
            CardObjective obj = game.getNextParcelObjective();
            if (obj == null) {
                return Optional.empty();
            }
            if(!individualBoard.addNewParcelObjective((CardObjectiveParcel) obj)) {
                return Optional.empty();
            }

            log.addAction(BotActionType.PICK_PARCEL_GOAL, obj.toString());
            currentNumberOfAction++;
            return Optional.of(obj);
        }
        return Optional.empty();
    }

    public Optional<CardObjective> selectParcelObjectiveFromIndividualBoard(TurnLog log) {
        if (getIndividualBoard().countUnfinishedParcelObjectives() == 0) {
            pickParcelObjective(log);
        } else {
            return Optional.of(getIndividualBoard().getNextParcelGoal());
        }
        return Optional.empty();
    }

    /**
     * This method pick a new Gardener objective from the pile of card and add this objective to the individual board
     * @return the objective that we pick
     */
    public Optional<CardObjective> pickGardenerObjective(TurnLog log) {
        if (currentNumberOfAction < 2) {
            CardObjective obj = game.getNextGardenerObjective();
            if (obj == null) {
                return Optional.empty();
            }
            if(!individualBoard.addNewGardenerObjective((CardObjectiveGardener) obj)) {
                return Optional.empty();
            }
            log.addAction(BotActionType.PICK_GARDENER_GOAL, obj.toString());
            currentNumberOfAction++;
            return Optional.of(obj);
        }
        return Optional.empty();
    }

    /**
     * This method pick a new Panda objective from the pile of card and add this objective to the individual board
     * @return the objective that we pick
     */
    public Optional<CardObjective> pickPandaObjective(TurnLog log) {
        if (currentNumberOfAction < 2) {
            CardObjective obj = game.getNextPandaObjective();
            if (obj == null) {
                return Optional.empty();
            }
            if(!individualBoard.addNewPandaObjective((CardObjectivePanda) obj)) {
                return Optional.empty();
            }

            log.addAction(BotActionType.PICK_PANDA_GOAL, obj.toString());
            currentNumberOfAction++;

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
     * This function check the current objective
     * @return true if the cardObjective is in progress or false if there is any currentObjective or if
     * the currentObjective is finish
     */
    public void checkAllObjectives() {

        getIndividualBoard().checkAllGoal();

    }


    public abstract void playTurn (TurnLog log);

    public void playTurn() {
        TurnLog log = new TurnLog(this);
        playTurn(log);
        currentNumberOfAction = 0;
    }

    public abstract boolean canPlay();

    public static <T extends Enum<?>> T randomEnum(Class<T> classe){
        int x = random.nextInt(classe.getEnumConstants().length);
        return classe.getEnumConstants()[x];
    }


    /**
     * This method will place a parcel anywhere in the board, the color of the new parcel is random
     * @return true if we have place a parcel, false else
     */
    public Optional<Position> placeAnParcelAnywhere(TurnLog log) {
        if (board.getParcelCount() <= 27 && canDoAction()) {
            // We check where we can put an parcel
            ArrayList<Position> placeWhereWeCanPlaceAnParcel = new ArrayList<>(board.getValidPlaces());
            // Now, we have an ArrayList of the potentials places where we can add a parcel

            // We choose a random parcel in the potential list
            int position = random.nextInt(placeWhereWeCanPlaceAnParcel.size());

            // We finally add to the board the new parcel

            //board.addParcel(placeWhereWeCanPlaceAnParcel.get(position), new BambooPlantation(BambooColor.GREEN));
            Position pos = placeWhereWeCanPlaceAnParcel.get(position);

            BambooColor color = randomEnum(BambooColor.class);
            if (verifyIfWeCanPlaceAColoredParcel(color)) {
                currentNumberOfAction++;
                board.addParcel(pos, new BambooPlantation(color));

                Optional.of(pos).ifPresent(positions -> log.addAction(BotActionType.PLACE_PARCEL, positions.toString()));
                return Optional.of(pos);
            }
        }
        return Optional.empty();
    }

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
        if (board.getParcelCount() <= 27 && canDoAction()) {

            ArrayList<Position> placeWhereWeCanPlaceAnParcel = new ArrayList<>(board.getValidPlaces());

            int position = random.nextInt(placeWhereWeCanPlaceAnParcel.size());

            Position pos = placeWhereWeCanPlaceAnParcel.get(position);

            switch (color) {
                case PINK: {
                    if (verifyIfWeCanPlaceAColoredParcel(BambooColor.PINK)) {
                        if (board.addParcel(pos, new BambooPlantation(BambooColor.PINK))) {
                            numberOfParcelsPinkInTheGame++;
                            currentNumberOfAction++;
                        }
                        Optional.of(pos).ifPresent(positions -> log.addAction(BotActionType.PLACE_PARCEL, positions.toString()));
                        return Optional.of(pos);
                    }
                }

                case YELLOW: {
                    if (verifyIfWeCanPlaceAColoredParcel(BambooColor.YELLOW)) {
                        if (board.addParcel(pos, new BambooPlantation(BambooColor.YELLOW))) {
                            numberOfParcelsYellowInTheGame++;
                            currentNumberOfAction++;
                        }
                        Optional.of(pos).ifPresent(positions -> log.addAction(BotActionType.PLACE_PARCEL, positions.toString()));
                        return Optional.of(pos);
                    }
                }


                case GREEN: {
                    if ((verifyIfWeCanPlaceAColoredParcel(BambooColor.GREEN))) {
                        if (board.addParcel(pos, new BambooPlantation(BambooColor.GREEN))) {
                            numberOfParcelsGreenInTheGame++;
                            currentNumberOfAction++;
                        }
                        Optional.of(pos).ifPresent(positions -> log.addAction(BotActionType.PLACE_PARCEL, positions.toString()));
                        return Optional.of(pos);
                    }
                }
            }
        }

        return Optional.empty();
    }

    /**
     * This method return the colors whose compose the card objective parcel, for exemple Green
     * @param objective
     * @return
     */
    public BambooColor[] getTheColorsWhoseComposeAnCardbjectiveParcel(Optional<CardObjective> objective) {
        CardObjectiveParcel card = (CardObjectiveParcel) objective.get();
        return card.getColors();
    }

    /**
     * This method recover the missings positions to complete a Parcel Objective
     * @param card
     * @return
     */
     public Set<PositionColored> recoverTheMissingsPositionsToCompleteForParcelObjective(CardObjectiveParcel card) {

        Set<PositionColored> missingPositionsToComplete = new HashSet<>();
        card.verify();
        if (card.getMissingPositionsToComplete() != null) {
            for (PositionColored positionColored : card.getMissingPositionsToComplete()) {
                missingPositionsToComplete.add(positionColored);
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
        if(currentNumberOfAction >= Config.TOTAL_NUMBER_OF_ACTIONS) return false;
         try {
             var success = board.addParcel(position, new BambooPlantation(color));
             if(success) {
                 log.addAction(BotActionType.PLACE_PARCEL, position.toString());
                 currentNumberOfAction++;
             }
             return success;
         } catch (NoParcelLeftToPlaceException e) {
             return false;
         }
    }

    public boolean canDoAction() {
        if (currentNumberOfAction < Config.TOTAL_NUMBER_OF_ACTIONS) {
            return true;
        } else {
            return false;
        }
    }

    public void moveGardener(Position position, TurnLog log) {
        if(currentNumberOfAction >= Config.TOTAL_NUMBER_OF_ACTIONS) return;
        var success = board.getGardener().moveTo(position.getX(), position.getY(), position.getZ());
        if(success) {
            currentNumberOfAction++;
            log.addAction(BotActionType.MOVE_GARDENER, position.toString());
        }
    }

    public abstract String getTurnMessage();

    public String getName() {
        return name;
    }

    public int getCurrentNumberOfAction() {
        return currentNumberOfAction;
    }


}
