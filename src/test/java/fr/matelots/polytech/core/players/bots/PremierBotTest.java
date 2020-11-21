package fr.matelots.polytech.core.players.bots;

import fr.matelots.polytech.core.game.Game;
import fr.matelots.polytech.core.players.IndividualBoard;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class PremierBotTest {

    Game game;
    PremierBot bot;
    IndividualBoard indBoard;

    @BeforeEach
    public void init() {
        game = new Game();
        bot = new PremierBot(game);
        indBoard = bot.getIndividualBoard();
    }

    @Test
    public void testOneBotOnSinglePass() {
        // Test filling phase
        assertEquals(indBoard.getUnfinishedParcelObjectives().size(), 0);


        for(int i = 1; i <= 5; i++) {
            bot.playTurn();
            assertEquals(indBoard.getUnfinishedParcelObjectives().size(), i);
        }

        // test complete phase
        int oldSize = indBoard.getUnfinishedParcelObjectives().size();
        while(oldSize > 0) {
            bot.playTurn();
            int currentSize = indBoard.getUnfinishedParcelObjectives().size();
            assertTrue(currentSize <= oldSize);
            oldSize = currentSize;
        }
    }
}
