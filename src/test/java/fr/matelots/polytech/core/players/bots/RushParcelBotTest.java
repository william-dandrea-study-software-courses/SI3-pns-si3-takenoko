package fr.matelots.polytech.core.players.bots;

import fr.matelots.polytech.core.game.Game;
import fr.matelots.polytech.core.players.bots.logger.TurnLog;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

public class RushParcelBotTest {

    private RushParcelBot bot;
    private Game game;
    private TurnLog turnLog;

    @BeforeEach
    public void init () {
        game = new Game();
        bot = new RushParcelBot(game);
        turnLog = new TurnLog(bot);
        bot.setTurnLogger(turnLog);


    }




    @Test
    public void testInilializeOrUpdateListOfCurrentsObjective2() {


        bot.setCurrentNumberOfAction(0);
        bot.initializeOrUpdateListOfCurrentsObjective2();
        bot.setCurrentNumberOfAction(0);
        bot.initializeOrUpdateListOfCurrentsObjective2();
        bot.setCurrentNumberOfAction(0);
        bot.initializeOrUpdateListOfCurrentsObjective2();
        bot.setCurrentNumberOfAction(0);
        bot.initializeOrUpdateListOfCurrentsObjective2();
        bot.setCurrentNumberOfAction(0);
        bot.initializeOrUpdateListOfCurrentsObjective2();


        assertEquals(3, bot.getIndividualBoard().getUnfinishedParcelObjectives().size());
        assertEquals(3, bot.getBoard().getPositions().size());
    }

    @Test
    public void testEasiestObjectiveToResolve2() {


        bot.initializeOrUpdateListOfCurrentsObjective2();
        bot.setCurrentNumberOfAction(0);
        bot.initializeOrUpdateListOfCurrentsObjective2();
        bot.setCurrentNumberOfAction(0);
        bot.initializeOrUpdateListOfCurrentsObjective2();
        bot.setCurrentNumberOfAction(0);
        bot.initializeOrUpdateListOfCurrentsObjective2();
        bot.setCurrentNumberOfAction(0);
        bot.initializeOrUpdateListOfCurrentsObjective2();
        bot.setCurrentNumberOfAction(0);
        bot.initializeOrUpdateListOfCurrentsObjective2();
        bot.setCurrentNumberOfAction(0);
        bot.initializeOrUpdateListOfCurrentsObjective2();
        bot.setCurrentNumberOfAction(0);
        bot.initializeOrUpdateListOfCurrentsObjective2();
        bot.setCurrentNumberOfAction(0);
        bot.initializeOrUpdateListOfCurrentsObjective2();
        bot.setCurrentNumberOfAction(0);

        bot.easiestObjectiveToResolve2();
        assertTrue(bot.getCardWeActuallyTryToResolve() != null);

    }

}
