package fr.matelots.polytech.core.players.bots;

import fr.matelots.polytech.core.game.Config;
import fr.matelots.polytech.core.game.Game;
import fr.matelots.polytech.core.game.goalcards.CardObjectiveParcel;
import fr.matelots.polytech.core.game.goalcards.pattern.PositionColored;
import fr.matelots.polytech.core.game.parcels.BambooColor;
import fr.matelots.polytech.core.game.parcels.BambooPlantation;
import fr.matelots.polytech.core.players.Bot;
import fr.matelots.polytech.core.players.bots.logger.BotActionType;
import fr.matelots.polytech.core.players.bots.logger.TurnLog;
import fr.matelots.polytech.engine.util.Position;

import java.util.ArrayList;
import java.util.Random;
import java.util.stream.Collectors;

/**
 * @author Yann Clodong
 * @author D'Andrea William
 *
 * Prendre plusieurs objectifs parcelles et essayer de les resoudres
 */
public class PremierBot extends Bot {

    private CardObjectiveParcel currentGoal;

    // Represent the phase of the bot :
    // True => Fill the board;
    // False => Attempt to resolve goal
    private boolean filling = true;
    //BotActionType action = BotActionType.NONE;

    public PremierBot (Game game) {
        super (game);
    }
    public PremierBot(Game game, String name) {
        super(game, name);
    }


    /**
     * Emulate the turn of the bot
     */
    @Override
    public void playTurn(TurnLog log) {
        //if(board.getParcelCount() > 1) getIndividualBoard().checkAllParcelGoal();
        if(!canPlay())
            return; // le bot ne peut pas jouer alors il passe son tour

        if(filling)
            pickGoal(log);
        else {
            if(board.getParcelCount() == 1) {
                // si le plateau est vierge, le bot ajoute une tuile au hasard
                placeRandom(log);
            }
            else {
                selectGoalIfEmpty();

                if(currentGoal == null) {
                    // Le bot a déjà tenté de séléctionner un objectif mais tous ceux qu'il a sont déjà complété
                    pickGoal(log);
                }
                else attemptToPlaceParcelWithGoal(log);
            }
        }
        strategy();
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
     * Represent strategy of the bot.
     * Here the bot have to pass once to fill his board of 5 objectives
     * another to accomplish the objectives
     */
    private void strategy() {
        int nParcelGoal = getIndividualBoard().countUnfinishedParcelObjectives();
        if(nParcelGoal == 0 && !filling) filling = true;
        else if(nParcelGoal >= 5 && filling) filling = false;
    }

    /**
     * Represent the attempt of the bot to resolve the goal
     */
    private void attemptToPlaceParcelWithGoal(TurnLog log) {
        if(board.getParcelCount() == 1) {
            placeRandom(log);
            return;
        }

        currentGoal.verify();
        var goodPlaces = currentGoal.getMissingPositionsToComplete()
                .stream().map(PositionColored::getPosition).collect(Collectors.toList());
        var listPlaces = new ArrayList<Position>();

        goodPlaces.stream().filter(p -> board.isPlaceValid(p) && !p.equals(Config.POND_POSITION)).forEach(listPlaces::add);

        if(listPlaces.size() == 0)
            placeRandom(log);
        else
            board.addParcel(listPlaces.get(0), new BambooPlantation(BambooColor.GREEN));
        //action = BotActionType.PLACE_PARCEL;
    }


    /**
     * Place une parcel sur le board au hasard
     */
    private void placeRandom(TurnLog log) {
        var validPlacesSet = board.getValidPlaces();
        var validPlaces = new ArrayList<>(validPlacesSet);

        var rnd = new Random();
        var position = validPlaces.get(rnd.nextInt(validPlaces.size()));

        board.addParcel(position, new BambooPlantation(BambooColor.GREEN));
        //action = BotActionType.PLACE_PARCEL;
        log.addAction(BotActionType.PLACE_PARCEL, position.toString());
    }

    /**
     * The bot take a Parcel goal card
     */
    private void pickGoal(TurnLog log) {
        var obj = pickParcelObjective(log);

        obj.ifPresent(cardObjectiveParcel -> log.addAction(BotActionType.PICK_PARCEL_GOAL, obj.get().toString()));
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


    @Override
    public String getTurnMessage() {
        return ""; //action.getMessage(this, "Some parameter");
    }
}
