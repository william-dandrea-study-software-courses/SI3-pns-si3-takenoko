package fr.matelots.polytech.core.players.bots;

import fr.matelots.polytech.core.game.Board;
import fr.matelots.polytech.core.game.Game;
import fr.matelots.polytech.core.game.goalcards.CardObjective;
import fr.matelots.polytech.core.game.goalcards.CardObjectiveParcel;
import fr.matelots.polytech.core.game.goalcards.pattern.Patterns;
import fr.matelots.polytech.core.game.movables.Panda;
import fr.matelots.polytech.core.game.parcels.BambooColor;
import fr.matelots.polytech.core.players.IndividualBoard;
import fr.matelots.polytech.core.players.bots.logger.TurnLog;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


import java.util.Optional;

import static org.mockito.Mockito.*;


import static org.junit.jupiter.api.Assertions.*;


public class SecondBotTemporaireTest {

    Game game;

    IndividualBoard individualBoard;
    CardObjectiveParcel testCurrentObjective;
    Board board;
    Panda panda;
    TurnLog log;

    SecondBotTemporaire bot;


    @BeforeEach
    public void init() {
        game = new Game();
        bot = new SecondBotTemporaire(game);
        individualBoard = bot.getIndividualBoard();
        board = game.getBoard();
        game.addBot(bot);
        panda = board.getPanda();
        log = new TurnLog(bot);
    }

    @Test
    public void testCanPlay() {


        bot.setCurrentNumberOfAction(-1);
        Assertions.assertThrows(IllegalArgumentException.class, () -> {bot.canPlay();});

        bot.setCurrentNumberOfAction(0);
        assertTrue(bot.canPlay());

        bot.setCurrentNumberOfAction(3);
        assertFalse(bot.canPlay());


    }


    @Test
    public void testActionIfWeHaveAnyObjectivesWhenAnyNewParcelObjectives() throws Exception{

        SecondBotTemporaire botMoc = mock(SecondBotTemporaire.class);
        IndividualBoard individualBoard1 = botMoc.getIndividualBoard();

        //when(botMoc.pickParcelObjective(any(TurnLog.class))).thenReturn(Optional.empty());
        doNothing().when(botMoc).tryToResolveParcelObjective(isA(TurnLog.class));

        botMoc.pickParcelObjective(log);
        botMoc.pickParcelObjective(log);


        botMoc.actionIfWeHaveAnyObjectives(botMoc.getLogger());
        System.out.println(individualBoard1.countUnfinishedObjectives());
    }


}
