package fr.matelots.polytech.core.players.bots;

import fr.matelots.polytech.core.game.Game;
import fr.matelots.polytech.core.game.goalcards.CardObjective;
import fr.matelots.polytech.core.game.goalcards.CardObjectivePanda;
import fr.matelots.polytech.core.game.goalcards.CardObjectiveParcel;
import fr.matelots.polytech.core.players.Bot;

import java.util.List;

/**
 * This bot goal is to rush the panda objective.
 * - [ ] Tirer une carte objectif panda
 * - [ ] Essayer de résoudre l'objectif panda
 * @author williamdandrea
 */
public class FifthBot extends Bot {

    private CardObjectivePanda currentObjective;
    private List<CardObjectivePanda> unfinishedBotPandasObjectives;
    private BotAction action = BotAction.NONE;




    public FifthBot(Game game) {
        super(game);
    }

    @Override
    public void playTurn() {
        action = BotAction.NONE;

    }

    @Override
    public boolean canPlay() {
        return false;
    }

    @Override
    public String getTurnMessage() {
        return null;
    }

    public List<CardObjectivePanda> getUnfinishedBotPandasObjectives() {
        return unfinishedBotPandasObjectives;
    }

    // TODO: 08/12/2020  : mettre une condition de verification si on a piocher plus de 5 objectifs.
    void pickAnPandaObjectiveAndAddToThePlayerBoard() {
        if (individualBoard.countUnfinishedPandaObjectives() < 5) {
            pickPandaObjective();
        }
        updateUnfinishedPandasObjectives();
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

}
