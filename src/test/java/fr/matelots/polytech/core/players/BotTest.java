package fr.matelots.polytech.core.players;

import fr.matelots.polytech.core.game.Game;
import fr.matelots.polytech.core.players.bots.PremierBot;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class BotTest {
    private Bot bot;
    private Game game;

    @BeforeEach
    public void init () {
        game = new Game();
        bot = new PremierBot(game);
    }

    @Test
    public void testPiocheDUnObjectifParcelle () {
        assertTrue(bot.pickParcelObjective());
    }

    @Test
    public void testGetUnfinishedParcelObjectives () {
        assertTrue(bot.getIndividualBoard().getUnfinishedParcelObjectives().isEmpty());

        assertTrue(bot.pickParcelObjective());

        assertFalse(bot.getIndividualBoard().getUnfinishedParcelObjectives().isEmpty());
    }

    @Test
    public void TestStrategie() {
    }
}
