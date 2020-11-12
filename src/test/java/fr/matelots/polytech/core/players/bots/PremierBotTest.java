package fr.matelots.polytech.core.players.bots;

import fr.matelots.polytech.core.game.Board;
import fr.matelots.polytech.core.game.GoalCards.AlignedParcelGoal;
import fr.matelots.polytech.core.players.VirtualPlayer;
import org.junit.jupiter.api.Test;

import static org.junit.Assert.assertEquals;

public class PremierBotTest {

    @Test
    public void ResolveTest() {
        for(int length = 2; length <= 3; length++) {
            AlignedParcelGoal goal1 = new AlignedParcelGoal(length);

            PremierBot bot1 = new PremierBot();
            bot1.ResolveGoal(goal1);

            Board board = new Board();
            ((VirtualPlayer)bot1).setBoard(board);

            while(!goal1.getComplete()) {
                bot1.playTurn();
            }

            assertEquals(board.getPositions().size(), length);
        }
    }
}
