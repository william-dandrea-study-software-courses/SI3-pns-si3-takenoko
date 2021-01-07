package fr.matelots.polytech.core.players.bots;

import fr.matelots.polytech.core.game.Board;
import fr.matelots.polytech.core.game.Config;
import fr.matelots.polytech.core.game.Game;
import fr.matelots.polytech.core.game.goalcards.CardObjectivePanda;
import fr.matelots.polytech.core.game.parcels.BambooColor;
import fr.matelots.polytech.core.game.parcels.BambooPlantation;
import fr.matelots.polytech.core.players.bots.logger.TurnLog;
import fr.matelots.polytech.engine.util.Position;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class QuintusBotTest {
    private Game game;
    private QuintusBot bot;
    private TurnLog log;

    @BeforeEach
    void init () {
        game = new Game();
        bot = new QuintusBot(game);
        log = new TurnLog(bot);
    }

    @Test
    void numberOfAction () {
        bot.playTurn(log, null);
        assertEquals(Config.TOTAL_NUMBER_OF_ACTIONS, log.getActions().length);
    }

    @Test
    void testFirstTwoTurn() {
        bot .playTurn(log, null);
        bot.playTurn(log, null);

        assertEquals(2, bot.getIndividualBoard().countUnfinishedPandaObjectives());
        assertEquals(0, bot.getIndividualBoard().countUnfinishedParcelObjectives());
        assertEquals(0, bot.getIndividualBoard().countUnfinishedGardenerObjectives());
    }

    @Test
    void testCorrectNeededColor () {
        CardObjectivePanda card = new CardObjectivePanda(3, 2, 0, 0);
        Board board = new Board();
        game = mock(Game.class);
        when(game.getBoard()).thenReturn(board);
        bot = new QuintusBot(game);
        when(game.getNextPandaObjective()).thenReturn(card);
        bot.pickObjectif(log);
        assertArrayEquals(new BambooColor[] {BambooColor.GREEN},
                bot.getNeededColor().toArray(new BambooColor[0]));
    }

    @Test
    void testIsThereAPlantationWhereYouCanEatWithEmptyBoard() {
        bot.pickObjectif(log);
        assertFalse(bot.isThereAPlantationWhereYouCanEat());
    }

    @Test
    void testPlaceAParcel () {
        CardObjectivePanda card = new CardObjectivePanda(3, 2, 0, 0);
        Board board = new Board();
        game = mock(Game.class);
        when(game.getBoard()).thenReturn(board);
        bot = new QuintusBot(game);
        when(game.getNextPandaObjective()).thenReturn(card);
        bot.playTurn(log, null);
        bot.playTurn(log, null);
        bot.playTurn(log, null);

        List<Position> positions = new ArrayList<>(board.getPositions());
        positions.remove(Config.POND_POSITION);
        assertEquals(1, positions.size());
        Position pos = positions.get(0);

        assertEquals(BambooColor.GREEN, board.getParcel(pos).getBambooColor());
    }

    @Test
    void testIsThereAnythingToEat () {
        CardObjectivePanda card = new CardObjectivePanda(3, 2, 0, 0);
        Board board = new Board();
        game = mock(Game.class);
        when(game.getBoard()).thenReturn(board);
        bot = new QuintusBot(game);
        when(game.getNextPandaObjective()).thenReturn(card);
        bot.pickObjectif(log);

        // The board is empty but the bot already have all objectives
        assertFalse(bot.isThereAnythingInterestingToEat());

        board.addParcel(1, -1, 0, new BambooPlantation(BambooColor.GREEN));

        assertTrue(bot.isThereAnythingInterestingToEat());

        bot.movePanda(log);

        assertFalse(bot.isThereAnythingInterestingToEat());
    }
}
