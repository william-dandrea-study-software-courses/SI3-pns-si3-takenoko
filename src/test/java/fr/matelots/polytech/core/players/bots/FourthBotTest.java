package fr.matelots.polytech.core.players.bots;

import fr.matelots.polytech.core.game.Game;
import fr.matelots.polytech.core.game.goalcards.CardObjectiveParcel;
import fr.matelots.polytech.core.players.IndividualBoard;
import fr.matelots.polytech.core.players.bots.logger.TurnLog;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.Assert.*;

/**
 * @author williamdandrea
 */
public class FourthBotTest {

    Game game;
    FourthBot bot;
    TurnLog log;
    IndividualBoard individualBoard;
    CardObjectiveParcel testCurrentObjective;

    @BeforeEach
    public void init() {
        game = new Game();
        bot = new FourthBot(game);
        log = new TurnLog(bot);
        individualBoard = bot.getIndividualBoard();
        game.addBot(bot);
    }

    /**
     * Here, we test the start of the game, we verify if we take NUMBER_OF_PARCEL_OBJECTIVES_AT_THE_START and the
     * NUMBER_OF_GARDENER_OBJECTIVES_AT_THE_START
     */
    @Test
    public void launchGameTest(){
        // We verify if the firstLaunch boolean variable is at true (because we launch the game)
        assertTrue(bot.isFirstLaunch());

        // Now we launch the bot
        bot.playTurn(log, null);

        // Now we verify if we pick the good number of objectives into the individualBoard
        assertEquals(individualBoard.countUnfinishedParcelObjectives(), bot.getNumberOfParcelObjectivesAtTheStart());
        assertEquals(individualBoard.countUnfinishedGardenerObjectives(), bot.getNumberOfGardenerObjectivesAtTheStart());

        // Now, we verify if the firstLaunch variable is False because we pick the good number of objectives
        assertFalse(bot.isFirstLaunch());
    }


    /**
     * Here, we test the after start
     */
    @Test
    public void gameTest(){

        // We launch the bot one time
        bot.playTurn(log, null);
        // Now we verify if we pick the good number of objectives into the individualBoard
        assertEquals(individualBoard.countUnfinishedParcelObjectives(), bot.getNumberOfParcelObjectivesAtTheStart());
        assertEquals(individualBoard.countUnfinishedGardenerObjectives(), bot.getNumberOfGardenerObjectivesAtTheStart());

        // Now we relauch a turn
        bot.playTurn(log, null);
        for (int i = 0; i< 20 ; i++) {
            bot.playTurn(log, null);
        }

    }
}
