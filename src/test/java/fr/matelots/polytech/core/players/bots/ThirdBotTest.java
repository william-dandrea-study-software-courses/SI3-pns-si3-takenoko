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
    void AfterCompleteParcelObjectives() {
        bot.DecideAction(log);
        var opt = log.getLastAction();
        while(bot.canPlay() && (opt.isEmpty()
                || opt.get().getType() == BotActionType.PICK_PARCEL_GOAL
                || opt.get().getType() == BotActionType.PICK_GARDENER_GOAL
                || opt.get().getType() == BotActionType.PLACE_PARCEL )) {
            bot.moveGardener(Config.POND_POSITION, log);
            bot.DecideAction(log);
            try {
                opt = log.getLastAction();
            } catch (IllegalActionRepetitionException e) {
                System.out.println(log.toString());
                throw e;
            }
            bot.setCurrentNumberOfAction(0);
            log = new TurnLog(bot);
        }
        var action = opt.get();
        //assertTrue(action.getType() == BotActionType.MOVE_GARDENER || action.getType() == BotActionType.PLACE_IRRIGATION);
    }

    @Disabled
    @Test
    void testStability() {
        for(int i = 0; i < 1500; i++) {
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

}
