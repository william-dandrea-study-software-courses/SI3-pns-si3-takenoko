package fr.matelots.polytech.core.game;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;

public class GameTest {

    private Board board;
    private Game game;


    @BeforeEach
    public void init () {
        board = new Board();
        game = new Game();
    }

    @Test
    public void testDiceRandomWeather() {

        System.out.println(game.diceRandomWeather());
        assertNotNull(board.getParcel(0, 0, 0));
    }
}
