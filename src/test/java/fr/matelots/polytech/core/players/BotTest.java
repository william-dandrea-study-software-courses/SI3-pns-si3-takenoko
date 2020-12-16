package fr.matelots.polytech.core.players;

import fr.matelots.polytech.core.game.Game;
import fr.matelots.polytech.core.game.goalcards.CardObjective;
import fr.matelots.polytech.core.game.goalcards.CardObjectiveGardener;
import fr.matelots.polytech.core.game.goalcards.CardObjectivePanda;
import fr.matelots.polytech.core.game.goalcards.CardObjectiveParcel;
import fr.matelots.polytech.core.game.parcels.BambooColor;
import fr.matelots.polytech.core.players.bots.PremierBot;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.Assert.assertNotEquals;
import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Yann Clodong
 * @author williamdandrea
 */
public class BotTest {
    private Bot bot;
    private Game game;

    @BeforeEach
    public void init () {
        game = new Game();
        bot = new PremierBot(game);
    }



    @Test
    public void testGetUnfinishedParcelObjectives () {
        assertTrue(bot.getIndividualBoard().getUnfinishedParcelObjectives().isEmpty());

        assertTrue(bot.pickParcelObjective().isPresent());

        assertFalse(bot.getIndividualBoard().getUnfinishedParcelObjectives().isEmpty());
    }



    @Test
    public void testPlaceAnParcelAnywhereRandomColor() {
        int initialNumber = bot.getBoard().getParcelCount();
        bot.placeAnParcelAnywhere();
        assertEquals(bot.getBoard().getParcelCount(), initialNumber + 1);

        // We verify if we can't place more than 27 parcels
        for (int i = 0; i < 35 ; i++) {
            bot.placeAnParcelAnywhere();
        }

        assertEquals(bot.getBoard().getParcelCount(), 28);
    }

    /**
     * In total, we have at the maximum 11 GREEN PARCELS ; 7 PINK PARCELS ; 9 YELLOW PARCELS
     */

    @Test
    public void testPlaceAnParcelAnywhereChosenColorGREEN() {


        // We verify if we can't place more than 27 parcels
        for (int i = 0; i < 35 ; i++) {
            bot.placeAnParcelAnywhere(BambooColor.GREEN);
        }
        for (int i = 0; i < 35 ; i++) {
            bot.placeAnParcelAnywhere(BambooColor.YELLOW);
        }
        for (int i = 0; i < 35 ; i++) {
            bot.placeAnParcelAnywhere(BambooColor.PINK);
        }


        assertEquals(11, bot.getBoard().getParcelCount(BambooColor.GREEN));
        assertEquals(9, bot.getBoard().getParcelCount(BambooColor.YELLOW) );
        assertEquals(7, bot.getBoard().getParcelCount(BambooColor.PINK));
    }



    /**
     * We try to verify if an objective is resolve or not
     */
    @Test
    public void testCheckCurrentObjective(){

        // We try the function with a null objective
        Optional<CardObjective> currentCardObjective = null;
        Assertions.assertThrows(IllegalArgumentException.class, () -> {bot.checkObjective(currentCardObjective);});

        // Now, we will create a objective and check if this objective is in progress (return true) or not (return false)
        Optional<CardObjective> currentCardObjective2 = Optional.of(game.getNextParcelObjective());
        assertTrue(bot.checkObjective(currentCardObjective2));
    }

    /**
     * We try if we really pick a new parcel objective
     */
    @Test
    public void testPickParcelObjective() {
        int numberOfObjectiveAtStart = bot.getIndividualBoard().countUnfinishedObjectives() + bot.getIndividualBoard().countCompletedObjectives();
        assertEquals(0, numberOfObjectiveAtStart);

        // We test with just one objective and the number of actions we do
        int numberOfActions = bot.getCurrentNumberOfAction();
        Optional<CardObjectiveParcel> cardObjective = bot.pickParcelObjective();
        numberOfActions = bot.getCurrentNumberOfAction();
        assertEquals(1, numberOfActions);
        numberOfObjectiveAtStart = bot.getIndividualBoard().countUnfinishedObjectives() + bot.getIndividualBoard().countCompletedObjectives();
        assertEquals(1, numberOfObjectiveAtStart);

        // We can't have more than 5 objectives into the individualBoard
        for (int i = 0; i < 6; i++) {
            cardObjective = bot.pickParcelObjective();
        }
        assertEquals(Optional.empty(), cardObjective);
    }

    /**
     * We try if we really pick a new gardener objective
     */
    @Test
    public void testPickGardenerObjective() {
        int numberOfObjectiveAtStart = bot.getIndividualBoard().countUnfinishedObjectives() + bot.getIndividualBoard().countCompletedObjectives();
        assertEquals(0, numberOfObjectiveAtStart);

        // We test with just one objective and the number of actions we do
        int numberOfActions = bot.getCurrentNumberOfAction();
        Optional<CardObjectiveGardener> cardObjective = bot.pickGardenerObjective();
        numberOfActions = bot.getCurrentNumberOfAction();
        assertEquals(1, numberOfActions);
        numberOfObjectiveAtStart = bot.getIndividualBoard().countUnfinishedObjectives() + bot.getIndividualBoard().countCompletedObjectives();
        assertEquals(1, numberOfObjectiveAtStart);

        // We can't have more than 5 objectives into the individualBoard
        for (int i = 0; i < 6; i++) {
            cardObjective = bot.pickGardenerObjective();
        }
        assertEquals(Optional.empty(), cardObjective);
    }

    /**
     * We try if we really pick a new gardener objective
     */
    @Test
    public void testPickPandaObjective() {
        int numberOfObjectiveAtStart = bot.getIndividualBoard().countUnfinishedObjectives() + bot.getIndividualBoard().countCompletedObjectives();
        assertEquals(0, numberOfObjectiveAtStart);

        // We test with just one objective and the number of actions we do

        Optional<CardObjectivePanda> cardObjective = bot.pickPandaObjective();

        int numberOfActions = bot.getCurrentNumberOfAction();
        assertEquals(1, numberOfActions);

        numberOfObjectiveAtStart = bot.getIndividualBoard().countUnfinishedObjectives() + bot.getIndividualBoard().countCompletedObjectives();
        assertEquals(1, numberOfObjectiveAtStart);

        // We can't have more than 5 objectives into the individualBoard
        for (int i = 0; i < 6; i++) {
            cardObjective = bot.pickPandaObjective();
        }
        assertEquals(Optional.empty(), cardObjective);
    }
}
