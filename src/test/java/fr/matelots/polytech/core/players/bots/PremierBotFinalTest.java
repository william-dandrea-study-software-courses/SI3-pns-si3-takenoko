package fr.matelots.polytech.core.players.bots;

import fr.matelots.polytech.core.game.Game;
import fr.matelots.polytech.core.game.goalcards.CardObjective;
import fr.matelots.polytech.core.game.goalcards.CardObjectiveParcel;
import fr.matelots.polytech.core.game.goalcards.pattern.Patterns;
import fr.matelots.polytech.core.game.goalcards.pattern.PositionColored;
import fr.matelots.polytech.core.game.parcels.BambooColor;
import fr.matelots.polytech.core.players.Bot;
import fr.matelots.polytech.core.players.bots.logger.TurnLog;
import fr.matelots.polytech.engine.util.Position;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.List;
import java.util.Optional;
import java.util.Set;

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
    public void testInilializeOrUpdateListOfCurrentsObjectiveWithAnyPreviousObjectivesInTheList() {

        List<Optional<CardObjective>> currentList = bot.getListOfCurrentsObjectives();
        assertEquals(0, currentList.size());

        bot.inilializeOrUpdateListOfCurrentsObjective();

        assertTrue(currentList.get(0).isPresent());
        assertTrue(currentList.get(1).isPresent());

    }

    @Test
    public void testInilializeOrUpdateListOfCurrentsObjectiveWith2PreviousObjectivesInTheList() {
        // On tente de rajouter des objectifs
        List<Optional<CardObjective>> currentList = bot.getListOfCurrentsObjectives();


        // Pick 2 objectives
        bot.inilializeOrUpdateListOfCurrentsObjective();
        bot.setCurrentNumberOfAction(0);
        bot.inilializeOrUpdateListOfCurrentsObjective();
        bot.setCurrentNumberOfAction(0);
        bot.inilializeOrUpdateListOfCurrentsObjective();
        bot.setCurrentNumberOfAction(0);
        bot.inilializeOrUpdateListOfCurrentsObjective();


        assertTrue(currentList.get(0).isPresent());
        assertTrue(currentList.get(1).isPresent());
        assertTrue(currentList.get(2).isPresent());
        assertTrue(currentList.get(3).isPresent());
        assertTrue(currentList.get(4).isPresent());

    }

    @Test
    public void testInilializeOrUpdateListOfCurrentsObjectiveAndVerifySomeObjectives() {


        //CardObjectiveParcel cardActive = (CardObjectiveParcel) currentList.get(0).get();
        bot.placeAnParcelAnywhere(turnLog);
        CardObjectiveParcel cardActive = new CardObjectiveParcel(bot.getBoard(), 2, Patterns.TRIANGLE, BambooColor.GREEN, BambooColor.GREEN, BambooColor.GREEN);

        System.out.println(cardActive.toString());


    }
}
