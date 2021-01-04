package fr.matelots.polytech.core.players.bots;

import fr.matelots.polytech.core.game.Game;
import fr.matelots.polytech.core.game.goalcards.CardObjective;
import fr.matelots.polytech.core.players.Bot;
import fr.matelots.polytech.core.players.bots.logger.TurnLog;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

public class PremierBotFinalTest {

    private PremierBotFinal bot;
    private Game game;
    private TurnLog turnLog;

    @BeforeEach
    public void init () {
        game = new Game();
        bot = new PremierBotFinal(game);
        turnLog = new TurnLog(bot);
        bot.setTurnLogger(turnLog);

    }

    @Test
    public void testInilializeOrUpdateListOfCurrentsObjective() {

        List<Optional<CardObjective>> currentList = bot.getListOfCurrentsObjectives();
        assertEquals(0, currentList.size());

        bot.inilializeOrUpdateListOfCurrentsObjective();
        assertEquals(2, currentList.size());



    }
}
