package fr.matelots.polytech.core.players.bots;

import fr.matelots.polytech.core.game.Game;
import fr.matelots.polytech.core.game.goalcards.CardObjectiveParcel;
import fr.matelots.polytech.core.players.Bot;
import fr.matelots.polytech.core.players.bots.logger.BotActionType;
import fr.matelots.polytech.core.players.bots.logger.TurnLog;
import fr.matelots.polytech.engine.util.Position;

import java.util.Optional;


/**
 * Je pioche un objectif parcelle, je le resoud, j'en pioche un autre ...
 *
 *

 - [OK] Si on a aucun objectif dans le deck
 -      [OK] Tirer un objectif parcelle (TOUR+1)
 -      [OK] Regardé si l’objectif peut déjà être réalisé sans actions de la part du joueur
 -          [OK] Si oui, tirer un objectif parcelle (TOUR+1)
 - [ ] Sinon
 -      [OK] Récolter l’objectif actuel
 -      [ ] Essayer de résoudre l’objectif actuel
 -          [ ] Regarder le pattern de l’objectif
 -          [ ] Regarder sur le board de jeu si on trouve le même pattern avec une case en moins
 -          [ ] Si on le trouve
 -              [ ] Mettre une parcelle de la bonne couleur au bon endroit (TOUR+1)
 -          [ ] Sinon
 -              [ ] Mettre une parcelle de couleur random a un endroit random (TOUR+1)
 - [ ] Regarder si l’objectif est réalisé

 * @author williamdandrea
 */


public class SecondBotTemporaire extends Bot {

    private TurnLog turnLogger;
    private Optional<CardObjectiveParcel> currentObjective;


    public SecondBotTemporaire(Game game) { super(game); }
    public SecondBotTemporaire(Game game, String name) { super(game, name); }

    @Override
    public void playTurn(TurnLog log) {
        turnLogger = log;
        currentNumberOfAction = 0;


        int numberOfObjectiveInIndividualBoard = individualBoard.countUnfinishedObjectives();
        checkCurrentObjective();

        // 1.0 // Si on a aucun objectif dans le deck
        if (numberOfObjectiveInIndividualBoard == 0) {

            // 1.1 // Tirer un objectif parcelle (TOUR+1)
            pickParcelObjectiveAndAddItToThePlayerBoard();

            // 1.2 // Regarder si l’objectif peut déjà être réalisé sans actions de la part du joueur
            if(checkCurrentObjective() && canPlay()) {
                pickParcelObjectiveAndAddItToThePlayerBoard();

                if (canPlay()) {
                    tryToResolveParcelObjective();
                }
            }
        } else {
            if (canPlay()) {
                tryToResolveParcelObjective();
            }
        }



    }

    private void tryToResolveParcelObjective() {

        // Si on a juste l'etang, je met une parcelle n'importe ou
        if(board.getParcelCount() == 1) {
            if (canPlay()) {
                Optional<Position> placeOfParcel = placeAnParcelAnywhere();
                placeOfParcel.ifPresent(position -> turnLogger.addAction(BotActionType.PLACE_PARCEL, position.toString()));
            }
        }
    }

    /**
     * This method pick a new objective parcel, watch if it is present, and add it to the currentGoal class parameter
     */
    private void pickParcelObjectiveAndAddItToThePlayerBoard() {

        Optional<CardObjectiveParcel> objectiveParcel = pickParcelObjective();
        if(objectiveParcel.isPresent()) {
            turnLogger.addAction(BotActionType.PICK_PARCEL_GOAL, objectiveParcel.get().toString());
            currentObjective = objectiveParcel;
        }

    }

    /**
     * This function check the current objective
     * @return true if the currentObjective is in progress or false if there is any currentObjective or if
     * the currentObjective is finish
     */
    private boolean checkCurrentObjective() {

        // If the currentObjective == null, or if the current goal
        // is finish (the function verify return true if an objective is completed)
        return this.currentObjective.isPresent() && !currentObjective.get().verify();

        // Else, we return true because the objective in progress
    }


    @Override
    public boolean canPlay() {
        if (currentNumberOfAction <= 2) {
            return true;
        }
        return false;
    }


    @Override
    public String getTurnMessage() {
        return null;
    }
}
