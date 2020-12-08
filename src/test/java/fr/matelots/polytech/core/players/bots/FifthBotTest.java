package fr.matelots.polytech.core.players.bots;

import fr.matelots.polytech.core.game.Board;
import fr.matelots.polytech.core.game.Game;
import fr.matelots.polytech.core.game.goalcards.CardObjectivePanda;
import fr.matelots.polytech.core.game.goalcards.CardObjectiveParcel;
import fr.matelots.polytech.core.game.movables.Panda;
import fr.matelots.polytech.core.game.parcels.BambooColor;
import fr.matelots.polytech.core.game.parcels.BambooPlantation;
import fr.matelots.polytech.core.players.IndividualBoard;
import fr.matelots.polytech.engine.util.Position;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Comparator;
import java.util.Set;

import static fr.matelots.polytech.core.game.parcels.BambooColor.*;
import static org.junit.Assert.*;

/**
 * @author williamdandrea
 */

public class FifthBotTest {

    Game game;
    FifthBot bot;
    IndividualBoard individualBoard;
    CardObjectiveParcel testCurrentObjective;
    Board board;
    Panda panda;

    @BeforeEach
    public void init() {
        game = new Game();
        bot = new FifthBot(game);
        individualBoard = bot.getIndividualBoard();
        board = game.getBoard();
        game.addBot(bot);
        panda = board.getPanda();
    }

    /**
     * This method test the function pickAnPandaObjectiveAndAddToThePlayerBoard
     */
    @Test
    public void testPickAnPandaObjectiveAndAddToThePlayerBoard(){
        // We mesure the number of objective in the player board
        int actualNumberOfObjective = individualBoard.countUnfinishedPandaObjectives();
        assertEquals(actualNumberOfObjective, 0);

        bot.pickAnPandaObjectiveAndAddToThePlayerBoard();
        // We mesure now the number of objective in the player board
        int newNumberOfObjective = individualBoard.countUnfinishedPandaObjectives();
        assertEquals(newNumberOfObjective, 1);

        // Now we test if we can't pick more than five objective (we try 8 times)
        for (int i = 0; i < 8; i++) {
            bot.pickAnPandaObjectiveAndAddToThePlayerBoard();
        }
        // And so, the total of objective need to be 5, not 8
        int finalNumberOfObjective = individualBoard.countUnfinishedPandaObjectives();
        assertEquals(finalNumberOfObjective, 5);
    }

    /**
     * This method test the function checkObjective
     */
    @Test
    public void testCheckObjective() {
        // We pick a new objective
        bot.pickAnPandaObjectiveAndAddToThePlayerBoard();

        // We check if this objective is in the individual board
        assertEquals(individualBoard.countUnfinishedPandaObjectives(), 1);

        // We recover the objective
        CardObjectivePanda pandaObjective = individualBoard.getUnfinishedPandaObjectives().get(0);

        // Now we verify if the objective is finished or not - The objective is in progress so it return True
        assertTrue(bot.checkObjective(pandaObjective));
    }

    @Test
    public void testUpdateUnfinishedPandasObjectives() {
        // We add new objectives

        for (int i = 0; i < 3 ; i++) {
            bot.pickAnPandaObjectiveAndAddToThePlayerBoard();
        }

        assertTrue(bot.updateUnfinishedPandasObjectives());
        assertEquals(bot.getUnfinishedBotPandasObjectives().stream().count(), 3);
        for (int i = 0; i < 10 ; i++) {
            bot.pickAnPandaObjectiveAndAddToThePlayerBoard();
        }

        assertTrue(bot.updateUnfinishedPandasObjectives());
        assertEquals(bot.getUnfinishedBotPandasObjectives().stream().count(), 5);
    }

    @Test
    public void testPickThe5TheObjectives() {
        // We check that e have 0 objectives
        assertEquals(individualBoard.countUnfinishedPandaObjectives(), 0);
        assertEquals(bot.getUnfinishedBotPandasObjectives().size(), 0);
        bot.pickThe5TheObjectives();
        // We check that we have 5 objectives
        assertEquals(individualBoard.countUnfinishedPandaObjectives(), 5);
        assertEquals(bot.getUnfinishedBotPandasObjectives().size(), 5);

        // We add one more objective, so we have 6 objectives but we can't have 6 objectives
        bot.pickAnPandaObjectiveAndAddToThePlayerBoard();
        assertEquals(individualBoard.countUnfinishedPandaObjectives(), 5);
        assertEquals(bot.getUnfinishedBotPandasObjectives().size(), 5);

    }

    @Test
    public void testWatchTheDifferenceBetweenTheWantedNumberOfBambooInStockAndTheNumberOfBambooInIndividualBoard() {
        // With any parcels, normally, we can receive all the color
        assertNotEquals(bot.watchTheDifferenceBetweenTheWantedNumberOfBambooInStockAndTheNumberOfBambooInIndividualBoard(), null);

    }

    @Test
    public void testSearchTheParcelWhereWeHaveABambooWithTheGoodColorGREEN() {
        assertNull(bot.searchTheParcelWhereWeHaveABambooWithTheGoodColor(GREEN));
        // We will place a lot of parcels and try to find a bamboo
        for (int i = 0; i < 30 ; i++) {
            bot.placeAnParcelAnywhere();
        }

        // We move a lot of time the garder in order to grow the bamboo
        bot.moveTheGardenerAnywhere();
        assertTrue(bot.searchTheParcelWhereWeHaveABambooWithTheGoodColor(GREEN) != null ||
                bot.searchTheParcelWhereWeHaveABambooWithTheGoodColor(YELLOW) != null ||
                bot.searchTheParcelWhereWeHaveABambooWithTheGoodColor(PINK) != null);

    }
    @Test
    public void testMoveThePandaAnywhere() {
        for (int i = 0; i < 30 ; i++) {
            bot.placeAnParcelAnywhere();
        }

        Position position = panda.getPosition();
        bot.moveThePandaAnywhere();
        assertNotEquals(position, panda.getPosition());

    }


}
