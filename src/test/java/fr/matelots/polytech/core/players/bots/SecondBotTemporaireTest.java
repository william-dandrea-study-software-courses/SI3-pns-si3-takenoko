package fr.matelots.polytech.core.players.bots;

import fr.matelots.polytech.core.game.Board;
import fr.matelots.polytech.core.game.Game;
import fr.matelots.polytech.core.game.goalcards.CardObjective;
import fr.matelots.polytech.core.game.goalcards.CardObjectiveParcel;
import fr.matelots.polytech.core.game.movables.Panda;
import fr.matelots.polytech.core.players.IndividualBoard;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;


public class SecondBotTemporaireTest {

    Game game;
    FifthBot bot;
    IndividualBoard individualBoard;
    CardObjectiveParcel testCurrentObjective;
    Board board;
    Panda panda;

    @BeforeEach
    public void init() {
        game = new Game();
        bot = new FifthBot(game);
        individualBoard = bot.getIndividualBoard();
        board = game.getBoard();
        game.addBot(bot);
        panda = board.getPanda();
    }


}
