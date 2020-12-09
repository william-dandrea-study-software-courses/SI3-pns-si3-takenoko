package fr.matelots.polytech.core.players;

import fr.matelots.polytech.core.game.Board;
import fr.matelots.polytech.core.game.Game;
import fr.matelots.polytech.core.game.goalcards.CardObjectiveGardener;
import fr.matelots.polytech.core.game.goalcards.CardObjectivePanda;
import fr.matelots.polytech.core.game.goalcards.CardObjectiveParcel;
import fr.matelots.polytech.core.game.parcels.BambooColor;
import fr.matelots.polytech.core.game.parcels.BambooPlantation;
import fr.matelots.polytech.core.players.bots.BotAction;
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



    public Bot(Game game) {
        this.game = game;
        this.board = game.getBoard();

        individualBoard = new IndividualBoard();
    }

    public IndividualBoard getIndividualBoard() {
        return individualBoard;
    }

    public Board getBoard() {
        return board;
    }

    public boolean pickParcelObjective () {
        CardObjectiveParcel obj = game.getNextParcelObjective();
        if (obj == null)
            return false;
        return individualBoard.addNewParcelObjective(obj);
    }

    public boolean pickGardenerObjective() {
        CardObjectiveGardener obj = game.getNextGardenerObjective();
        if (obj == null)
            return false;
        return individualBoard.addNewGardenerObjective(obj);
    }

    public boolean pickPandaObjective() {
        CardObjectivePanda obj = game.getNextPandaObjective();
        if (obj == null)
            return false;
        return individualBoard.addNewPandaObjective(obj);
    }


    public abstract void playTurn ();

    public abstract boolean canPlay();



    /**
     * This method will place a parcel anywhere in the board
     * @return true if we have place a parcel, false else
     */
    public boolean placeAnParcelAnywhere() {
        if (board.getParcelCount() <= 27) {
            // We check where we can put an parcel
            ArrayList<Position> placeWhereWeCanPlaceAnParcel = new ArrayList<>(board.getValidPlaces());
            // Now, we have an ArrayList of the potentials places where we can add a parcel

            // We choose a random parcel in the potential list
            Random randomNumber = new Random();
            int position = randomNumber.nextInt(placeWhereWeCanPlaceAnParcel.size());

            // We finally add to the board the new parcel

            //board.addParcel(placeWhereWeCanPlaceAnParcel.get(position), new BambooPlantation(BambooColor.GREEN));
            board.addParcel(placeWhereWeCanPlaceAnParcel.get(position), new BambooPlantation(BambooColor.values()[random.nextInt(BambooColor.values().length)]));
            return true;
        }
        return false;

    }

    public abstract String getTurnMessage();
}
