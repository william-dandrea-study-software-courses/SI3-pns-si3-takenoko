package fr.matelots.polytech.core.players.bots;

import fr.matelots.polytech.core.game.Config;
import fr.matelots.polytech.core.game.Game;
import fr.matelots.polytech.core.game.parcels.BambooColor;
import fr.matelots.polytech.core.game.parcels.BambooPlantation;
import fr.matelots.polytech.core.players.IndividualBoard;
import fr.matelots.polytech.core.players.bots.logger.TurnLog;
import fr.matelots.polytech.engine.util.Position;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.Assert.*;

public class PremierBotTest {

    Game game;
    TurnLog log;
    PremierBot bot;
    IndividualBoard indBoard;

    @BeforeEach
    public void init() {
        game = new Game();
        bot = new PremierBot(game);
        log = new TurnLog(bot);
        indBoard = bot.getIndividualBoard();
        game.addBot(bot);
    }

    @Test
    public void testOneBotOnSinglePass() {
        // Test filling phase
        assertEquals(indBoard.getUnfinishedParcelObjectives().size(), 0);


        for(int i = 1; i <= 5; i++) {
            bot.playTurn(log);
            assertEquals(indBoard.getUnfinishedParcelObjectives().size(), i);
        }

        // test complete phase
        int oldSize = indBoard.getUnfinishedParcelObjectives().size();
        while(oldSize > 0) {
            bot.playTurn(log);
            int currentSize = indBoard.getUnfinishedParcelObjectives().size();
            assertTrue(currentSize <= oldSize);
            oldSize = currentSize;
        }
    }

    @Test
    public void testBotCantPickParcelGoal() {
        for(int i = 0; i <= Config.DECK_SIZE; i++) {
            game.getNextParcelObjective();
        }
        //game.run();

        assertFalse(bot.canPlay());
    }

    @Test
    public void testBotCantPlaceParcel() throws Exception {

        int oldVal = game.getBoard().getParcelLeftToPlace();
        while(game.getBoard().getParcelLeftToPlace() != 0) {
            ArrayList<Position> valids = new ArrayList<>(game.getBoard().getValidPlaces());
            if(valids.size() == 0) assertTrue(false); // No valid places
            else {
                game.getBoard().addParcel(valids.get(0), new BambooPlantation(BambooColor.GREEN));

                // dodge infinite loop
                if(game.getBoard().getParcelLeftToPlace() >= oldVal) throw  new Exception("The system of parcel limitation dont working properly, now");
            }
        }

        assertFalse(bot.canPlay());
    }
}
