package fr.matelots.polytech.core.players;

import fr.matelots.polytech.core.game.Game;
import fr.matelots.polytech.core.players.bots.PremierBot;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class PlayerTest {
    private Player player;
    private Game game;

    @BeforeEach
    public void init () {
        player = new PremierBot();
        game = new Game();
    }

    @Test
    public void testPiocheDUnObjectifParcelle () {
        assertTrue(player.pickParcelObjective(game));
    }

    @Test
    public void testGetUnfinishedParcelObjectives () {
        assertTrue(player.getIndividualBoard().getUnfinishedParcelObjectives().isEmpty());

        assertTrue(player.pickParcelObjective(game));

        assertFalse(player.getIndividualBoard().getUnfinishedParcelObjectives().isEmpty());
    }
}
