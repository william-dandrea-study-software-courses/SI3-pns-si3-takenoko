package fr.matelots.polytech.core.players.bots;

import fr.matelots.polytech.core.IllegalActionRepetitionException;
import fr.matelots.polytech.core.game.Board;
import fr.matelots.polytech.core.game.Config;
import fr.matelots.polytech.core.game.Game;
import fr.matelots.polytech.core.game.graphics.BoardDrawer;
import fr.matelots.polytech.core.players.bots.logger.BotActionType;
import fr.matelots.polytech.core.players.bots.logger.TurnLog;
import fr.matelots.polytech.engine.util.Position;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.stream.Stream;

import static org.junit.Assert.*;
import static org.junit.jupiter.api.Assertions.assertTimeout;

public class ThirdBotTest {

    private ThirdBot bot;
    private TurnLog log;
    private Game game;
    private Board board;

    @BeforeEach
    void init() {
        game = new Game();
        board = game.getBoard();
        bot = new ThirdBot(game);

        log = new TurnLog(bot);
    }

    @Test
    void FillHand() {
        bot.DecideAction(log);
        var opt = log.getLastAction();
        while(opt.isEmpty()) {
            bot.DecideAction(log);
            opt = log.getLastAction();
        }
        var action = opt.get();
        assertTrue(action.getType() == BotActionType.PICK_GARDENER_GOAL || action.getType() == BotActionType.PICK_PARCEL_GOAL);
    }

    @Test
    void AfterFilling() {
        bot.DecideAction(log);
        var opt = log.getLastAction();
        while(opt.isEmpty() || (opt.get().getType() == BotActionType.PICK_PARCEL_GOAL || opt.get().getType() == BotActionType.PICK_GARDENER_GOAL)) {
            bot.DecideAction(log);
            opt = log.getLastAction();
            bot.setCurrentNumberOfAction(0);
        }
        var action = opt.get();
        assertEquals(action.getType(), BotActionType.PLACE_PARCEL);
        bot.checkAllObjectives();

    }

    @Test
    void testCycleOfTheBot() {
        //bot.DecideAction(log);
        boolean canPlay = bot.canPlay();
        BotActionType[] gardenerPhaseActions = new BotActionType[] {
                BotActionType.MOVE_GARDENER,
                BotActionType.PLACE_IRRIGATION,
                BotActionType.PLACE_PARCEL
        };
        BotActionType[] parcelPhase = new BotActionType[] {
                BotActionType.PLACE_PARCEL
        };
        while(bot.canPlayThisTurn()) {
            bot.setCurrentNumberOfAction(0);

            boolean haveParcelSolvable = bot.haveParcelObjectiveSolvable();
            boolean haveGardenerSolvable = bot.haveGardenerObjectiveSolvable();
            boolean willPick = !haveGardenerSolvable && !haveGardenerSolvable && bot.canPickObjective();
            bot.DecideAction(log);

            if(willPick) {
                log.getLastAction().ifPresent(ba -> assertTrue("No solvable objective and return : " + ba.getType(), Config.isPickAction(ba.getType())));
            } else {
                if(haveParcelSolvable) {
                    log.getLastAction().ifPresent(ba -> assertTrue(Arrays.stream(parcelPhase).anyMatch(ba2 -> ba.getType() == ba2)));
                } else if(haveGardenerSolvable) {
                    log.getLastAction().ifPresent(ba -> assertTrue(Arrays.stream(gardenerPhaseActions).anyMatch(ba2 -> ba.getType() == ba2)));
                }
            }

        }

    }

    @Disabled
    @Test
    void testStability() {
        for(int i = 0; i < 500; i++) {
            init();
            int n = 0;
            while (bot.canPlay()) {
                log = new TurnLog(bot);
                try {
                    bot.playTurn(log, null);
                } catch (RuntimeException e) {
                    var board = new BoardDrawer(bot.getBoard());
                    board.print();
                    throw e;
                }
                n++;
            }
            System.out.println("Game end in " + n);
        }
    }

     @Test
    void AfterCompleteGardenerObjectives() {

         bot.DecideAction(log);
         var opt = log.getLastAction();
         BoardDrawer drawer = new BoardDrawer(board);

         while(bot.canPlay() && (opt.isEmpty()
                 || opt.get().getType() == BotActionType.PICK_PARCEL_GOAL
                 || opt.get().getType() == BotActionType.PICK_GARDENER_GOAL)) {
             bot.DecideAction(log);
             opt = log.getLastAction();
             bot.setCurrentNumberOfAction(0);
             log = new TurnLog(bot);
         }
         while(bot.canPlay() && (opt.isEmpty() ||
                 opt.get().getType() == BotActionType.PLACE_PARCEL ||
                    opt.get().getType() == BotActionType.MOVE_GARDENER ||
                opt.get().getType() == BotActionType.PLACE_IRRIGATION)) {
             try {
                 bot.DecideAction(log);
             } catch (RuntimeException e) {
                 drawer.print();
                 throw e;
             }
             opt = log.getLastAction();
             bot.setCurrentNumberOfAction(0);
             log = new TurnLog(bot);
         }
         var action = opt.get();
         assertTrue(action.getType() == BotActionType.PICK_GARDENER_GOAL || action.getType() == BotActionType.PICK_PARCEL_GOAL);
     }

     @Disabled
     @Test
    void bot3vs3bot() {
        for(int i = 0; i < 250; i++) {
            ThirdBot bot2 = new ThirdBot(game);
            game.addBot(bot2);

            int n = 0;
            while (bot.canPlay() && bot2.canPlay()) {
                init();
                bot.playTurn(log);
                log = new TurnLog(bot2);
                bot2.playTurn(log);
                n++;
            }
            System.out.println("Game finished in " + n);
        }
    }


}
