package fr.matelots.polytech.core.players;

import fr.matelots.polytech.core.game.Board;
import fr.matelots.polytech.core.game.Game;
import fr.matelots.polytech.core.game.goalcards.CardObjective;
import fr.matelots.polytech.core.game.goalcards.CardObjectiveGardener;
import fr.matelots.polytech.core.game.goalcards.CardObjectivePanda;
import fr.matelots.polytech.core.game.goalcards.CardObjectiveParcel;
import fr.matelots.polytech.core.game.parcels.BambooColor;
import fr.matelots.polytech.core.game.parcels.BambooPlantation;
import fr.matelots.polytech.core.players.bots.logger.TurnLog;
import fr.matelots.polytech.engine.util.Position;

import java.util.*;

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


    /**
     * This method pick a new parcel objective from the pile of card and add this objective to the individual board
     * @return the objective that we pick
     */
    public Optional<CardObjectiveParcel> pickParcelObjective() {
        CardObjectiveParcel obj = game.getNextParcelObjective();
        if (obj == null) {
            return Optional.empty();
        }
        if(!individualBoard.addNewParcelObjective(obj)) {
            return Optional.empty();
        }
        currentNumberOfAction++;
        return Optional.of(obj);
    }

    /**
     * This method pick a new Gardener objective from the pile of card and add this objective to the individual board
     * @return the objective that we pick
     */
    public Optional<CardObjectiveGardener> pickGardenerObjective() {
        CardObjectiveGardener obj = game.getNextGardenerObjective();
        if (obj == null) {
            return Optional.empty();
        }
        if(!individualBoard.addNewGardenerObjective(obj)) {
            return Optional.empty();
        }
        currentNumberOfAction++;
        return Optional.of(obj);
    }

    /**
     * This method pick a new Panda objective from the pile of card and add this objective to the individual board
     * @return the objective that we pick
     */
    public Optional<CardObjectivePanda> pickPandaObjective() {
        CardObjectivePanda obj = game.getNextPandaObjective();
        if (obj == null) {
            return Optional.empty();
        }
        if(!individualBoard.addNewPandaObjective(obj)) {
            return Optional.empty();
        }
        currentNumberOfAction++;
        return Optional.of(obj);
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


    public abstract void playTurn (TurnLog log);

    public void playTurn() {
        TurnLog log = new TurnLog(this);
        playTurn(log);
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
    public Optional<Position> placeAnParcelAnywhere() {
        if (board.getParcelCount() <= 27) {
            // We check where we can put an parcel
            ArrayList<Position> placeWhereWeCanPlaceAnParcel = new ArrayList<>(board.getValidPlaces());
            // Now, we have an ArrayList of the potentials places where we can add a parcel

            // We choose a random parcel in the potential list
            Random randomNumber = new Random();
            int position = randomNumber.nextInt(placeWhereWeCanPlaceAnParcel.size());

            // We finally add to the board the new parcel

            //board.addParcel(placeWhereWeCanPlaceAnParcel.get(position), new BambooPlantation(BambooColor.GREEN));
            Position pos = placeWhereWeCanPlaceAnParcel.get(position);
            board.addParcel(pos, new BambooPlantation(randomEnum(BambooColor.class)));
            return Optional.of(pos);
        }
        return Optional.empty();
    }

    /**
     * This method will place a parcel anywhere in the board, the color of the new parcel is the color in parameter
     * @param color the color of the parcel we want
     * @return true if we have place a parcel, false else
     */
    public Optional<Position> placeAnParcelAnywhere(BambooColor color) {
        if (board.getParcelCount() <= 27) {
            // We check where we can put an parcel
            ArrayList<Position> placeWhereWeCanPlaceAnParcel = new ArrayList<>(board.getValidPlaces());
            // Now, we have an ArrayList of the potentials places where we can add a parcel

            // We choose a random parcel in the potential list
            Random randomNumber = new Random();
            int position = randomNumber.nextInt(placeWhereWeCanPlaceAnParcel.size());

            // We finally add to the board the new parcel

            //board.addParcel(placeWhereWeCanPlaceAnParcel.get(position), new BambooPlantation(BambooColor.GREEN));
            Position pos = placeWhereWeCanPlaceAnParcel.get(position);
            board.addParcel(pos, new BambooPlantation(color));
            return Optional.of(pos);
        }
        return Optional.empty();
    }



    public abstract String getTurnMessage();

    public String getName() {
        return name;
    }

    public int getCurrentNumberOfAction() {
        return currentNumberOfAction;
    }
}
