package fr.matelots.polytech.core.players.bots;

import fr.matelots.polytech.core.game.Game;
import fr.matelots.polytech.core.game.goalcards.CardObjective;
import fr.matelots.polytech.core.game.goalcards.CardObjectivePanda;
import fr.matelots.polytech.core.game.goalcards.CardObjectiveParcel;
import fr.matelots.polytech.core.players.Bot;

import java.util.ArrayList;
import java.util.List;

/**
 * This bot goal is to rush the panda objective.
 * - [OK] Tant que le tableau d'objectif n'est pas plein, tirer carte objectif panda
 * - [ ] Essayer de résoudre l'objectif panda
 *      - [ ] Si il n'y a aucune parcelle, placer une parcelle
 *      - [ ] Regarder la différence entre le nombre de bambou en stock (de chaque couleurs) et l'objectif de bambou en stock (on va prendre 3)
 *          - [ ] En extraire une couleur favorite, couleur a laquelle nous allons essayer de combler le trou
 *          - [ ] Regarder sur le board de jeu l'endroit ou il y a un bambou de cette couleur
 *          - [ ] Si il y a une parcelle avec des bambou de cette couleur
 *              - [ ] Deplacer le panda sur cette parcelle
 *          - [ ] Sinon, s'il n'y a pas de parcelle avec des bambou de cette couleur
 *              - [ ] Regarder si il y a une parcelles avec des bambou d'une autre couleur
 *              - [ ] S'il y a une parcelle avec des bambou d'une autre couleur
 *                  - [ ] Deplacer le panda sur celle-ci
 *              - [ ] Sinon
 *                  - [ ] Si le nombre de parcelle est inferieur à 6
 *                      - [ ] Placer une parcelle n'importe ou
 *                  - [ ] Sinon
 *                      - [ ] Deplacer le jardinier n'importe ou
 * @author williamdandrea
 */
public class FifthBot extends Bot {

    private CardObjectivePanda currentObjective;
    private List<CardObjectivePanda> unfinishedBotPandasObjectives;
    private BotAction action = BotAction.NONE;

    private int OBJECTIVE_NUMBER_OF_BAMBOO_STOCK = 3;




    public FifthBot(Game game) {
        super(game);
        unfinishedBotPandasObjectives = new ArrayList<>();
    }

    @Override
    public void playTurn() {
        action = BotAction.NONE;

        // 1 // We pick objectives to have in total 5 objectives.
        pickThe5TheObjectives();

        // 2 // Try to resolve panda objective
        tryToResolvePandaObjective();

    }

    void tryToResolvePandaObjective() {
        // 1 // Si il n'y a aucune parcelle, placer une parcelle
        placeAnParcelAnywhere();
    }


    public List<CardObjectivePanda> getUnfinishedBotPandasObjectives() {
        return unfinishedBotPandasObjectives;
    }

    /**
     * pick a new objective and add it to the player board and to the unfinishedBotPandasObjectives
     * @return true if we pick a new objective, false if not
     */
    boolean pickAnPandaObjectiveAndAddToThePlayerBoard() {
        if (individualBoard.countUnfinishedPandaObjectives() < 5) {
            pickPandaObjective();
            updateUnfinishedPandasObjectives();
            return true;
        }
        updateUnfinishedPandasObjectives();
        return false;

    }

    /**
     * This method will complete the list of objective to have in total 5 objectives in the individual board
     */
    void pickThe5TheObjectives() {
        while (pickAnPandaObjectiveAndAddToThePlayerBoard());
    }

    /**
     * update the list unfinishedPandasObjectives with the individualBoard Current objectives
     * @return true if all the objectives are in progress, else return false
     */
    boolean updateUnfinishedPandasObjectives() {
        unfinishedBotPandasObjectives = getIndividualBoard().getUnfinishedPandaObjectives();

        //We verify if all the objectives are in progress
        for (int i = 0; i < unfinishedBotPandasObjectives.size() ; i++) {
            if (checkObjective(unfinishedBotPandasObjectives.get(i)) == false) {
                return false;
            }
        }
        return true;
    }

    /**
     * This function check the current objective
     * @return true if the currentObjective is in progress or false if there is any currentObjective or if
     * the currentObjective is finish
     */
    boolean checkObjective(CardObjectivePanda objective) {
        // If the currentObjective == null, or if the current goal is finish (the function verify return true if an objective is completed)
        // Else, we return true because the objective in progress
        if (objective != null) {
            objective.setIndividualBoard(individualBoard);
            return !objective.verify();
        }
        return false;
    }



    @Override
    public boolean canPlay() {
        return false;
    }

    @Override
    public String getTurnMessage() {
        return null;
    }
}
