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

/**
 * @author williamdandrea
 */
public class FourthBotTest {

    Game game;
    FourthBot bot;
    IndividualBoard individualBoard;
    CardObjectiveParcel testCurrentObjective;

    @BeforeEach
    public void init() {
        game = new Game();
        bot = new FourthBot(game);
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
        bot.playTurn();

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
        bot.playTurn();
        // Now we verify if we pick the good number of objectives into the individualBoard
        assertEquals(individualBoard.countUnfinishedParcelObjectives(), bot.getNumberOfParcelObjectivesAtTheStart());
        assertEquals(individualBoard.countUnfinishedGardenerObjectives(), bot.getNumberOfGardenerObjectivesAtTheStart());

        // Now we relauch a turn
        bot.playTurn();
        System.out.println(game.getBoard().toString());

        bot.playTurn();
        System.out.println(game.getBoard().toString());
        bot.playTurn();
        System.out.println(game.getBoard().toString());
        bot.playTurn();
        System.out.println(game.getBoard().toString());
        bot.playTurn();
        System.out.println(game.getBoard().toString());
        bot.playTurn();
        System.out.println(game.getBoard().toString());
        bot.playTurn();
        System.out.println(game.getBoard().toString());
        bot.playTurn();
        System.out.println(game.getBoard().toString());
        bot.playTurn();
        System.out.println(game.getBoard().toString());
    }
}
