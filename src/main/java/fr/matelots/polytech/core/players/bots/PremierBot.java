package fr.matelots.polytech.core.players.bots;

import fr.matelots.polytech.core.game.Config;
import fr.matelots.polytech.core.game.Game;
import fr.matelots.polytech.core.game.goalcards.CardObjectiveParcel;
import fr.matelots.polytech.core.game.parcels.BambooColor;
import fr.matelots.polytech.core.game.parcels.BambooPlantation;
import fr.matelots.polytech.core.game.parcels.Parcel;
import fr.matelots.polytech.core.players.Bot;
import fr.matelots.polytech.engine.util.Position;

import java.util.ArrayList;
import java.util.Random;

/**
 * @author Yann Clodong
 */
public class PremierBot extends Bot {

    private CardObjectiveParcel currentGoal;

    // Represent the phase of the bot :
    // True => Fill the board;
    // False => Attempt to resolve goal
    private boolean filling = true;
    private final String name = "";

    public PremierBot (Game game) {
        super (game);
    }


    /**
     * Emulate the turn of the bot
     */
    @Override
    public void playTurn() {
        //if(board.getParcelCount() > 1) getIndividualBoard().checkAllParcelGoal();
        if(!canPlay())
            return; // le bot ne peut pas jouer alors il passe son tour

        if(filling)
            pickGoal();
        else {
            if(board.getParcelCount() == 1) {
                // si le plateau est vierge, le bot ajoute une tuile au hasard
                placeRandom();
            }
            else {
                selectGoalIfEmpty();

                if(currentGoal == null)
                {
                    // Le bot a déjà tenté de séléctionner un objectif mais tous ceux qu'il a sont déjà complété
                    pickGoal();
                }
                else attemptToPlaceParcelWithGoal();
            }
        }
        strategie();
    }

    @Override
    public boolean canPlay() {
        boolean canPick = canPickParcelCard();
        return canPlaceParcel() &&  // il peut placer des parcels et
                (canPick || getIndividualBoard().countUnfinishedParcelObjectives() > 0); // soit il lui reste des objectifs soit il peut en piocher
    }

    private boolean canPickParcelCard() {
        return board.getDeckParcelObjective().canPick();
    }

    private boolean canPlaceParcel()  {
        return board.getParcelLeftToPlace() > 0;
    }

    private void selectGoalIfEmpty() {
        if(currentGoal != null && currentGoal.verify()) currentGoal = null;

        while(currentGoal == null && getIndividualBoard().getUnfinishedParcelObjectives().size() > 0) {

            if (currentGoal == null) selectGoal(); // Verifie que current goal n'est pas nul, on l'affecte sinon

            if (currentGoal.verify()) {
                // L'objectif est atteint
                selectGoal();
            }
        }
    }

    /**
     * Represent strategie of the bot.
     * Here the bot do have to pass one for fill his board of 5 objectives
     * another to accomplish the objectives
     */
    private void strategie() {
        int nParcelGoal = getIndividualBoard().countUnfinishedParcelObjectives();
        if(nParcelGoal == 0 && !filling) filling = true;
        else if(nParcelGoal >= 5 && filling) filling = false;
    }

    /**
     * Represent the attempt of the bot to resolve the goal
     */
    private void attemptToPlaceParcelWithGoal() {
        if(board.getParcelCount() == 1) {
            placeRandom();
            return;
        }

        currentGoal.verify();
        var goodPlaces = currentGoal.getMissingPositionsToComplete();
        var listPlaces = new ArrayList<Position>();

        goodPlaces.stream().filter(p -> board.isPlaceValid(p) && !p.equals(Config.BOND_POSITION)).forEach(listPlaces::add);

        if(listPlaces.size() == 0)
            placeRandom();
        else
            board.addParcel(listPlaces.get(0), new BambooPlantation(BambooColor.green));
    }


    /**
     * Place une parcel sur le board au hasard
     */
    private void placeRandom() {
        var validPlacesSet = board.getValidPlaces();
        var validPlaces = new ArrayList<>(validPlacesSet);

        var rnd = new Random();
        var position = validPlaces.get(rnd.nextInt(validPlaces.size()));

        board.addParcel(position, new BambooPlantation(BambooColor.green));
    }

    /**
     * The bot take a Parcel goal card
     */
    private void pickGoal() {
        boolean canPick = pickParcelObjective();
    }


    /**
     * the bot select the goal card in the deck
     */
    private void selectGoal() {
        currentGoal = getIndividualBoard().getNextParcelGoal();
    }

    @Override
    public String toString() {
        return "Bot 1";
    }
}
