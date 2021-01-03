package fr.matelots.polytech.core.players.bots;

import fr.matelots.polytech.core.game.Config;
import fr.matelots.polytech.core.game.Game;
import fr.matelots.polytech.core.game.goalcards.CardObjective;
import fr.matelots.polytech.core.game.goalcards.CardObjectiveParcel;
import fr.matelots.polytech.core.game.goalcards.pattern.PositionColored;
import fr.matelots.polytech.core.game.parcels.BambooColor;
import fr.matelots.polytech.core.game.parcels.BambooPlantation;
import fr.matelots.polytech.core.players.Bot;
import fr.matelots.polytech.core.players.bots.logger.BotActionType;
import fr.matelots.polytech.core.players.bots.logger.TurnLog;
import fr.matelots.polytech.engine.util.Position;

import java.util.*;


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


     - [ ] Essayer de résoudre un objectif parcelle
        - [ ] Enregistrer le nombre de parcelles de chaques couleurs qu’il faut pour résoudre l’objectif et leur emplacement
        - [ ] Si l’objectif comporte que des parcelles de meme couleur
            - [ ] Si il y a sur le board une parcelle de la meme couleur
                - [ ] Parcourir le board a la recherche de parcelle ou l’on peut placer une parcelle autour
                    - [ ] Si on peut placer une parcelle autour
                        - [ ] Placer la parcelle de la bonne couleur a cet endroit
                        - [ ] Sortir de la boucle
            - [ ] Sinon (s’il n’y pas pas de parcelle de la meme couleur que les parcelles de l’objectif sur le board)
                - [ ] Placer une parcelle de la couleur de l’objectif n’importe ou
        - [ ] Si l’objectif comporte des parcelles de couleur différentes
            - [ ] Mettre dans des variables la couleur1 et la couleur2 (couleur des parcelles de l’objectif)
            - [ ] Si il y a sur le board une parcelle de la couleur1
                - [ ] Parcourir le board a la recherche de parcelle ou l’on peut placer une parcelle autour
                    - [ ] Placer la parcelle de la bonne couleur a cet endroit
                    - [ ] Sortir de la boucle
        - [ ] Si il y a sur le board une parcelle de la couleur2
            - [ ] Parcourir le board a la recherche de parcelle ou l’on peut placer une parcelle autour
                - [ ] Placer la parcelle de la bonne couleur a cet endroit
                - [ ] Sortir de la boucle
        - [ ] Sinon, placer une parcelle avec la couleur1 n’importe ou
     - [ ] Si l’objectif est résolu
        - [ ] Piocher un nouvel objectif
     - [ ] Sinon
        - [ ] Garder le même objectif et recommancer
 * @author williamdandrea
 *
 *
 * @todo Impossible de checker les objectifs donc boucle infini, a revoir
 */


public class SecondBotTemporaire extends Bot {

    private TurnLog turnLogger;
    private Optional<CardObjective> currentObjective;


    public SecondBotTemporaire(Game game) { super(game); }
    public SecondBotTemporaire(Game game, String name) { super(game, name); }

    @Override
    public void playTurn(TurnLog log) {
        currentNumberOfAction = 0;
        turnLogger = log;



        int numberOfObjectiveInIndividualBoard = individualBoard.countUnfinishedObjectives();
        //checkObjective(currentObjective);

        // 1.0 // Si on a aucun objectif dans le deck
        if (numberOfObjectiveInIndividualBoard == 0) {
            currentObjective = pickParcelObjective(log);
            tryToResolveParcelObjective(log);

        } else {
            if (canDoAnAction() && currentObjective.isPresent()) {
                tryToResolveParcelObjective(log);
            }
        }

        currentNumberOfAction = 0;



    }

    /**
     * We except that the currentObjective is present
     */
    void tryToResolveParcelObjective() {

        CardObjectiveParcel actualCard = (CardObjectiveParcel) currentObjective.get();
        BambooColor[] colors = actualCard.getColors();

        if (!checkIfTheColorsInAnObjectiveAreTheSameOrNot(colors)) {
            // Si l’objectif comporte que des parcelles de meme couleur

            Set<PositionColored> missingPositionsToComplete = new HashSet<>();
            if (actualCard.getMissingPositionsToComplete() != null) {
                for (PositionColored positionColored : actualCard.getMissingPositionsToComplete()) {
                    missingPositionsToComplete.add(positionColored);
                }
            }


        } else {

        }


    }

    /**
     *
     * @param colors
     * @return true if we have differents colors
     */
    boolean checkIfTheColorsInAnObjectiveAreTheSameOrNot(BambooColor[] colors) {
        for (int i=0; i<colors.length; i++) {
            if (!colors[i].equals(colors[i+1])) {
                return true;
            }
        }
        return false;

    }





    void tryToResolveParcelObjective(TurnLog log) {
        System.out.println("tryToResolveParcelObjective");
        // Si on a juste l'etang, je met une parcelle n'importe ou
        if(board.getParcelCount() == 1) {
            if (canDoAnAction()) {
                Optional<Position> placeOfParcel = placeAnParcelAnywhere(log);
            }
        } else {
            // On récupère l'emplacement des parcelles candidates pour résoudre l'objectif
            CardObjectiveParcel cardObjectiveParcel = (CardObjectiveParcel) currentObjective.get();

            Set<PositionColored> missingPositionsToComplete = new HashSet<>();

            if (cardObjectiveParcel.getMissingPositionsToComplete() != null) {
                for (PositionColored positionColored : cardObjectiveParcel.getMissingPositionsToComplete()) {
                    missingPositionsToComplete.add(positionColored);
                }

                ArrayList<PositionColored> positionsWeChooseForPlaceAnParcel = new ArrayList<>();

                // We browse all the place where we can place a parcel and we add this positions to the ArrayList positionsWeChoose
                if (missingPositionsToComplete != null) {
                    missingPositionsToComplete.stream()
                            .filter(p -> board.isPlaceValid(p.getPosition()) && !p.equals(Config.POND_POSITION))
                            .forEach(positionsWeChooseForPlaceAnParcel::add);
                }

                if(positionsWeChooseForPlaceAnParcel.size() != 0) {
                    // We have an place to put the new parcel

                    // We choose a random parcel in the potential list
                    Random randomNumber = new Random();
                    int position = randomNumber.nextInt(positionsWeChooseForPlaceAnParcel.size());

                    // We add the new parcel
                    board.addParcel(positionsWeChooseForPlaceAnParcel.get(position).getPosition(),
                            new BambooPlantation(positionsWeChooseForPlaceAnParcel.get(position).getColor())
                    );

                    if(checkCurrentObjective() && canDoAnAction()) {
                        currentObjective = pickParcelObjective(log);
                    }

                }

            } else {
                // We put a parcel anywhere
                placeAnParcelAnywhere(log);
            }
        }
    }

    /**
     * This methid check the currentObjective, if it is completed, we pick a new Parcel objective
     * @return true if the objective is completed, false if the currentObjective doesn't change
     */
    private boolean checkCurrentObjective() {

        if (currentObjective.isPresent()) {
            if (currentObjective.get().isCompleted() && currentObjective.get().verify()) {
                return true;
            }
        }

        return false;


    }



    public boolean canDoAnAction() {
        if (this.currentNumberOfAction <= 2) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public boolean canPlay() {

        if (board.getParcelCount() <= Config.DECK_PARCEL_SIZE) {
            if (this.currentNumberOfAction < 0)
                throw new IllegalArgumentException("We can't have negative actions");
            if (this.currentNumberOfAction <= 2)
                return true;
        }
        return false;


    }

    @Override
    public String getTurnMessage() {
        return null;
    }



    public Optional<CardObjective> getCurrentObjective() {
        return currentObjective;
    }

    public TurnLog getLogger() {
        return turnLogger;
    }
}
