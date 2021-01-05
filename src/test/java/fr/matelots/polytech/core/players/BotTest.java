package fr.matelots.polytech.core.players;

import fr.matelots.polytech.core.game.Config;
import fr.matelots.polytech.core.game.Game;
import fr.matelots.polytech.core.game.goalcards.CardObjective;
import fr.matelots.polytech.core.game.goalcards.CardObjectiveGardener;
import fr.matelots.polytech.core.game.goalcards.CardObjectivePanda;
import fr.matelots.polytech.core.game.goalcards.CardObjectiveParcel;
import fr.matelots.polytech.core.game.goalcards.pattern.Patterns;
import fr.matelots.polytech.core.game.goalcards.pattern.PositionColored;
import fr.matelots.polytech.core.game.parcels.BambooColor;
import fr.matelots.polytech.core.game.parcels.BambooPlantation;
import fr.matelots.polytech.core.players.bots.PremierBot;
import fr.matelots.polytech.core.players.bots.logger.TurnLog;
import fr.matelots.polytech.engine.util.Position;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Yann Clodong
 * @author williamdandrea
 */
public class BotTest {
    private Bot bot;
    private Game game;
    private TurnLog turnLog;

    @BeforeEach
    public void init () {
        game = new Game();
        bot = new PremierBot(game);
        turnLog = new TurnLog(bot);
    }



    @Test
    public void testGetUnfinishedParcelObjectives () {
        assertTrue(bot.getIndividualBoard().getUnfinishedParcelObjectives().isEmpty());

        assertTrue(bot.pickParcelObjective(turnLog).isPresent());

        assertFalse(bot.getIndividualBoard().getUnfinishedParcelObjectives().isEmpty());
    }

    @Test
    public void testRandomPlaceableColor() {
        for (int i = 0; i < Config.NB_PLACEABLE_PARCEL; i++)
            bot.board.addParcel(bot.board.getValidPlaces().iterator().next(), new BambooPlantation(bot.getRandomPlaceableColor()));
        assertEquals(0, bot.board.getParcelLeftToPlace(BambooColor.GREEN));
        assertEquals(0, bot.board.getParcelLeftToPlace(BambooColor.YELLOW));
        assertEquals(0, bot.board.getParcelLeftToPlace(BambooColor.PINK));
        assertNull(bot.getRandomPlaceableColor());
    }

    @Test
    public void testPlaceAnParcelAnywhereRandomColor() {
        int initialNumber = bot.getBoard().getParcelCount();
        bot.placeAnParcelAnywhere(turnLog);
        assertEquals(bot.getBoard().getParcelCount(), initialNumber + 1);

        bot.setCurrentNumberOfAction(0);

        // We verify if we can't place more than 27 parcels
        for (int i = 0; i < Config.NB_PLACEABLE_PARCEL - 1; i++) {
            bot.setCurrentNumberOfAction(0);
            assertNotEquals(Optional.empty(), bot.placeAnParcelAnywhere(turnLog));
        }
        assertEquals(Config.NB_PLACEABLE_PARCEL, bot.getBoard().getParcelCount());
        assertEquals(Optional.empty(), bot.placeAnParcelAnywhere(turnLog));
    }

    /**
     * In total, we have at the maximum 11 GREEN PARCELS ; 7 PINK PARCELS ; 9 YELLOW PARCELS
     */

    @Test
    public void testPlaceAnParcelAnywhereChosenColor() {
        bot.setCurrentNumberOfAction(0);
        // We verify if we can't place more than 27 parcels
        for (int i = 0; i < 50 ; i++) {
            bot.placeAnParcelAnywhere(BambooColor.GREEN, turnLog);
            bot.setCurrentNumberOfAction(0);
        }
        for (int i = 0; i < 50 ; i++) {
            bot.placeAnParcelAnywhere(BambooColor.YELLOW, turnLog);
            bot.setCurrentNumberOfAction(0);
        }
        for (int i = 0; i < 50 ; i++) {
            bot.setCurrentNumberOfAction(0);
            bot.placeAnParcelAnywhere(BambooColor.PINK, turnLog);
        }

        assertEquals(Config.NB_MAX_GREEN_PARCELS, bot.getBoard().getParcelCount(BambooColor.GREEN));
        assertEquals(Config.NB_MAX_YELLOW_PARCELS, bot.getBoard().getParcelCount(BambooColor.YELLOW) );
        assertEquals(Config.NB_MAX_PINK_PARCELS, bot.getBoard().getParcelCount(BambooColor.PINK));
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
        Optional<CardObjective> cardObjective = bot.pickParcelObjective(turnLog);
        assertTrue(cardObjective.get() instanceof CardObjectiveParcel);


        numberOfActions = bot.getCurrentNumberOfAction();
        assertEquals(1, numberOfActions);
        numberOfObjectiveAtStart = bot.getIndividualBoard().countUnfinishedObjectives() + bot.getIndividualBoard().countCompletedObjectives();
        assertEquals(1, numberOfObjectiveAtStart);

        // We can't have more than 5 objectives into the individualBoard
        for (int i = 0; i < 6; i++) {
            cardObjective = bot.pickParcelObjective(turnLog);
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
        Optional<CardObjective> cardObjective = bot.pickGardenerObjective(turnLog);
        assertTrue(cardObjective.get() instanceof CardObjectiveGardener);


        numberOfActions = bot.getCurrentNumberOfAction();
        assertEquals(1, numberOfActions);
        numberOfObjectiveAtStart = bot.getIndividualBoard().countUnfinishedObjectives() + bot.getIndividualBoard().countCompletedObjectives();
        assertEquals(1, numberOfObjectiveAtStart);

        // We can't have more than 5 objectives into the individualBoard
        for (int i = 0; i < 6; i++) {
            cardObjective = bot.pickGardenerObjective(turnLog);
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

        Optional<CardObjective> cardObjective = bot.pickPandaObjective(turnLog);
        assertTrue(cardObjective.get() instanceof CardObjectivePanda);


        int numberOfActions = bot.getCurrentNumberOfAction();
        assertEquals(1, numberOfActions);

        numberOfObjectiveAtStart = bot.getIndividualBoard().countUnfinishedObjectives() + bot.getIndividualBoard().countCompletedObjectives();
        assertEquals(1, numberOfObjectiveAtStart);

        // We can't have more than 5 objectives into the individualBoard
        for (int i = 0; i < 6; i++) {
            cardObjective = bot.pickPandaObjective(turnLog);
        }
        assertEquals(Optional.empty(), cardObjective);
    }

    /*@Test
    public void testGetTheColorsWhoseComposeAnCardbjectiveParcel() {
        CardObjective cardObjective = new CardObjectiveParcel(bot.getBoard(), 2, Patterns.TRIANGLE, BambooColor.GREEN, BambooColor.GREEN, BambooColor.GREEN);
        BambooColor[] list1 = bot.getTheColorsWhoseComposeAnCardbjectiveParcel(Optional.of(cardObjective));
        for (BambooColor bambooColor: list1) {
            assertTrue(bambooColor.equals(bambooColor.GREEN));
        }

        CardObjective cardObjective2 = new CardObjectiveParcel(bot.getBoard(), 3, Patterns.RHOMBUS, BambooColor.YELLOW, BambooColor.YELLOW, BambooColor.GREEN, BambooColor.GREEN);
        BambooColor[] list2 = bot.getTheColorsWhoseComposeAnCardbjectiveParcel(Optional.of(cardObjective2));
        for (BambooColor bambooColor: list2) {
            assertTrue(bambooColor.equals(bambooColor.GREEN) || bambooColor.equals(bambooColor.YELLOW));
        }
    }*/

    @Test
    public void testRecoverTheMissingsPositionsToCompleteForParcelObjective() {
        CardObjectiveParcel cardObjective = new CardObjectiveParcel(bot.getBoard(), 2, Patterns.TRIANGLE, BambooColor.GREEN, BambooColor.GREEN, BambooColor.GREEN);


        bot.placeAnParcelAnywhere(BambooColor.GREEN, turnLog);
        bot.placeAnParcelAnywhere(BambooColor.GREEN, turnLog);
        bot.placeAnParcelAnywhere(BambooColor.GREEN, turnLog);

        Set<PositionColored> missingPositionsToComplete = bot.recoverTheMissingsPositionsToCompleteForParcelObjective(cardObjective);
        assertTrue(missingPositionsToComplete.size() > 0);

        CardObjectiveParcel cardObjective2 = new CardObjectiveParcel(bot.getBoard(), 3, Patterns.RHOMBUS, BambooColor.YELLOW, BambooColor.YELLOW, BambooColor.GREEN, BambooColor.GREEN);

        bot.placeAnParcelAnywhere(BambooColor.YELLOW, turnLog);
        bot.placeAnParcelAnywhere(BambooColor.YELLOW, turnLog);
        bot.placeAnParcelAnywhere(BambooColor.YELLOW, turnLog);
        bot.placeAnParcelAnywhere(BambooColor.YELLOW, turnLog);

        Set<PositionColored> missingPositionsToComplete2 = bot.recoverTheMissingsPositionsToCompleteForParcelObjective(cardObjective2);
        assertTrue(missingPositionsToComplete2.size() > 0);


    }

    @Test
    void placeParcelTestLimitOfColoredParcelVersion() {
        for(int i = 0; i < Config.NB_MAX_GREEN_PARCELS; i++) {
            assertTrue(bot.getBoard().addParcel(bot.getBoard().getValidPlaces().stream().findAny().get(), new BambooPlantation(BambooColor.GREEN)));
        }

        assertFalse(bot.placeParcel(bot.getBoard().getValidPlaces().stream().findAny().get(), BambooColor.GREEN, turnLog));
    }

    @Test
    void placeParcelTestInvalidPlace() {
        assertFalse(bot.placeParcel(new Position(50, 50, 50), BambooColor.GREEN, turnLog));
    }

    @Test
    void placeParcelNumberOfActions() {
        for(int i = 0; i < Config.TOTAL_NUMBER_OF_ACTIONS; i++) {
            bot.placeParcel(bot.getBoard().getValidPlaces().stream().findAny().get(), BambooColor.GREEN, turnLog);
        }
        assertFalse(bot.placeParcel(bot.getBoard().getValidPlaces().stream().findAny().get(), BambooColor.GREEN, turnLog));
    }

    @Test
    public void testCantMoveGardener() {
        Position pos = new Position(0, 1, -1);
        bot.board.addBambooPlantation(pos);
        bot.setCurrentNumberOfAction(Config.TOTAL_NUMBER_OF_ACTIONS);
        assertFalse(bot.moveGardener(pos, turnLog));
    }

    @Test
    public void testMoveGardenerNotSuccess() {
        Position pos = new Position(0, 1, -1);
        assertFalse(bot.moveGardener(pos, turnLog));
    }

    @Test
    public void testMoveGardenerSuccess() {
        Position pos = new Position(0, 1, -1);
        bot.board.addBambooPlantation(pos);
        assertTrue(bot.moveGardener(pos, turnLog));
    }

}
