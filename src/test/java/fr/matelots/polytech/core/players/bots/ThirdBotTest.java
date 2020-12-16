package fr.matelots.polytech.core.players.bots;

import fr.matelots.polytech.core.game.Game;
import fr.matelots.polytech.core.game.parcels.BambooColor;
import fr.matelots.polytech.core.game.parcels.BambooPlantation;
import fr.matelots.polytech.core.players.bots.logger.TurnLog;
import fr.matelots.polytech.engine.util.Position;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertTimeout;

public class ThirdBotTest {

    private ThirdBot bot;
    private ThirdBot bot2;
    private TurnLog log;
    private TurnLog log2;
    private Game game;

    @BeforeEach
    void Init() {
        game = new Game();
        bot = new ThirdBot(game);
        log = new TurnLog(bot);

        bot2 = new ThirdBot(game);
        log2 = new TurnLog(bot2);
    }

    @Test
    @Timeout(2)
    void testConvergenceSolo() {
        game.addBot(bot);
        game.launchTurnLoop();
    }

    @Test
    @Timeout(2)
    void testCovergenceDualBot() {
        game.addBot(bot);
        game.addBot(bot2);

        Timer timer = new Timer(true);
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                assertTrue(false);
                throw new RuntimeException("too long !");
            }
        }, 2000);
        game.launchTurnLoop();
        timer.cancel();
    }

    @Test
    @Timeout(2)
    void testObjectiveParcelDeckEmpty() {
        game.addBot(bot);
        bot.playTurn(log);
        while(game.getNextParcelObjective() != null) ;
        game.launchTurnLoop();
    }

    @Test
    @Timeout(1)
    void testGardenerGardenerDeckEmpty() {
        game.addBot(bot);
        //bot.playTurn();
        while(game.getNextGardenerObjective() != null) ;
        game.launchTurnLoop();
    }

    @Test
    @Timeout(1)
    void testGardenerAndParcelEmpty() {
        while(game.getNextGardenerObjective() != null) ;
        while(game.getNextParcelObjective() != null) ;

        game.addBot(bot);
        game.launchTurnLoop();
    }

    @Test
    @Timeout(1)
    void test_Have_Not_Parcel_To_Place_And_No_Gardener_Goals() throws Exception {
        game.addBot(bot);

        int oldVal = game.getBoard().getParcelLeftToPlace();
        while(game.getBoard().getParcelLeftToPlace() != 0) {
            ArrayList<Position> valids = new ArrayList<>(game.getBoard().getValidPlaces());
            if(valids.size() == 0) throw new Exception("No place valid !");
            else {
                game.getBoard().addParcel(valids.get(0), new BambooPlantation(BambooColor.GREEN));

                // dodge infinite loop
                if(game.getBoard().getParcelLeftToPlace() >= oldVal) throw  new Exception("The system of parcel limitation dont working properly, now");
            }
        }

        while(game.getNextGardenerObjective() != null) ;

        assertFalse(bot.canPlay());
    }
}
