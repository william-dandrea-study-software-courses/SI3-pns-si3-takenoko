package fr.matelots.polytech.core.players.bots;

import fr.matelots.polytech.core.game.CardObjectiveParcel;
import fr.matelots.polytech.core.game.Game;
import fr.matelots.polytech.core.game.Parcel;
import fr.matelots.polytech.core.players.Bot;
import fr.matelots.polytech.engine.util.Position;
import java.util.Random;
import java.util.Set;


public class PremierBot extends Bot {

    private CardObjectiveParcel currentGoal;

    private boolean filling = true;
    private boolean deckEmpty = false;

    public PremierBot (Game game) {
        super (game);
    }

    @Override
    public void playTurn() {
        if(filling) {
            boolean hasPickGoal = pickGoal();
            if(!hasPickGoal) deckEmpty = true;
        }
        else {
            if(currentGoal == null)
                selectGoal();

            currentGoal.verify();
            if(currentGoal.isCompleted())
                selectGoal();

            attemptToPlaceParcelWithGoal();
        }
        strategie();
    }

    public void strategie() {
        if(getIndividualBoard().countUnfinishedParcelObjectives() == 0 && !filling) filling = true;
        else if((getIndividualBoard().countUnfinishedParcelObjectives() >= 5 || deckEmpty) && filling) filling = false;
    }


    /**
     * Place a parcel at random place
     */
    private void attemptToPlaceParcelWithGoal(){
        Set<Position> validPlace = board.getValidPlaces();
        Random rnd = new Random();

        Position[] positions = validPlace.toArray(new Position[] {});
        Position position = positions[rnd.nextInt(positions.length)];

        board.addParcel(position, new Parcel());
    }

    private boolean pickGoal() {
        return pickParcelObjective();
    }


    /**
     * The bot select a goal
     */
    private void selectGoal() {
        currentGoal = getIndividualBoard().getNextParcelGoal();
    }

    @Override
    public String toString() {
        return "Bot 1 ready to play";
    }
}
