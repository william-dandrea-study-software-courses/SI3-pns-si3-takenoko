package fr.matelots.polytech.core.players.bots;

import fr.matelots.polytech.core.game.Board;
import fr.matelots.polytech.core.game.Config;
import fr.matelots.polytech.core.game.Game;
import fr.matelots.polytech.core.game.goalcards.CardObjectiveParcel;
import fr.matelots.polytech.core.game.parcels.BambooColor;
import fr.matelots.polytech.core.game.parcels.BambooPlantation;
import fr.matelots.polytech.core.players.IndividualBoard;
import fr.matelots.polytech.engine.util.Position;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.Assert.*;

public class SecondBotTest {

    Game game;
    SecondBot bot;
    IndividualBoard individualBoard;
    CardObjectiveParcel testCurrentObjective;

    @BeforeEach
    public void init() {
        game = new Game();
        bot = new SecondBot(game);
        individualBoard = bot.getIndividualBoard();
        game.addBot(bot);
    }

    @Test
    public void testOneBotOnSinglePass() {
        assertEquals(individualBoard.getUnfinishedParcelObjectives().size(), 0);

        // Tant que l'on a pas réaliser un objectif, la liste d'objectifs comprends un seul objectif
        for(int i = 1; i <= 5; i++) {
            bot.playTurn();
            assertEquals(individualBoard.getUnfinishedParcelObjectives().size(), 1);
        }

        // test complete phase
        int oldSize = individualBoard.getUnfinishedParcelObjectives().size();
        while(oldSize > 0) {
            bot.playTurn();
            int currentSize = individualBoard.getUnfinishedParcelObjectives().size();
            assertTrue(currentSize <= oldSize);
            oldSize = currentSize;
        }
    }

    /**
     * We will test the function PickAnObjectiveAndAddToPlayerBoard()
     * This method pick a new objective and add it to the individual player board
     * The test consists of counting the number of objective in the individual board, call the method (pick a new
     * objective), and compare the actual number of objective with before. Normally, we have one more objective in
     * the individual board
     */
    @Test
    public void testPickAnObjectiveAndAddToPlayerBoard() {
        int individualBoardLength = individualBoard.getUnfinishedParcelObjectives().size();
        bot.pickAnObjectiveAndAddToPlayerBoard();
        assertEquals(individualBoard.getUnfinishedParcelObjectives().size(), individualBoardLength + 1);
    }

    @Test
    public void testSelectObjectiveFromPlayerDeck() {

        bot.pickAnObjectiveAndAddToPlayerBoard();
        testCurrentObjective = bot.getCurrentObjective();
        bot.selectObjectiveFromPlayerDeck();
        assertTrue(testCurrentObjective != bot.getCurrentObjective());
    }

    @Test
    public void testPlaceAnParcelAnywhere() {
        int initialNumber = bot.getBoard().getParcelCount();
        bot.placeAnParcelAnywhere();
        assertNotEquals(initialNumber, bot.getBoard().getParcelCount());
    }

    @Test
    public void testCheckCurrentObjective() {

        // We test if the method return false when the currentObjective is Null
        assertFalse(bot.checkCurrentObjective());

        // We have any parcels so we have an exception
        bot.pickAnObjectiveAndAddToPlayerBoard();
        bot.selectObjectiveFromPlayerDeck();
        assertThrows(IllegalArgumentException.class, () -> {bot.checkCurrentObjective(); });

        // We pick a new objective and put it in the currentObjective, checkCurrentObjective need to be true
        // because the objective is "active"
        bot.placeAnParcelAnywhere();
        bot.pickAnObjectiveAndAddToPlayerBoard();
        bot.selectObjectiveFromPlayerDeck();
        assertTrue(bot.checkCurrentObjective());

    }

    @Test
    public void testStrategyStart() {

        Board testBoard = bot.getBoard();

        // Actually we have just one parcel in the game because we launch it :
        int count = testBoard.getParcelCount();
        assertEquals(count, 1);
        // When we have just one parcel, we can't resolve objective, so the strategy function place a new parcel anywhere
        bot.strategy();
        assertEquals(testBoard.getParcelCount(), count + 1);

    }






}
