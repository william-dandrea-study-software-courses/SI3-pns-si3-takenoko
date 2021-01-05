package fr.matelots.polytech.core.players;

import fr.matelots.polytech.core.game.Board;
import fr.matelots.polytech.core.game.Game;
import fr.matelots.polytech.core.game.goalcards.CardObjectiveParcel;
import fr.matelots.polytech.core.game.goalcards.pattern.Patterns;
import fr.matelots.polytech.core.game.goalcards.pattern.PositionColored;
import fr.matelots.polytech.core.game.parcels.BambooColor;
import fr.matelots.polytech.core.game.parcels.BambooPlantation;
import fr.matelots.polytech.core.game.parcels.Parcel;
import fr.matelots.polytech.core.players.bots.PremierBot;
import fr.matelots.polytech.core.players.bots.logger.TurnLog;
import fr.matelots.polytech.engine.util.Position;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

public class IAforBotTest {


    private Game game;
    private Board board;
    private IAforBot ia;

    @BeforeEach
    public void init () {
        game = new Game();
        board = new Board();
    }



    @Test
    public void resolvePatternLineWhenWeCanPlaceAnParcelWhereIsTheMissingPosition() {


        game.getBoard().addParcel(0,1,-1, new BambooPlantation(BambooColor.GREEN));
        game.getBoard().addParcel(1,0,-1, new BambooPlantation(BambooColor.GREEN));
        game.getBoard().addParcel(1,1,-2, new BambooPlantation(BambooColor.GREEN));
        game.getBoard().addParcel(2,-1,-1, new BambooPlantation(BambooColor.GREEN));
        game.getBoard().addParcel(2,1,-1, new BambooPlantation(BambooColor.GREEN));

        CardObjectiveParcel cardObjectiveParcelLine = new CardObjectiveParcel(game.getBoard(), 2, Patterns.LINE, BambooColor.GREEN, BambooColor.GREEN, BambooColor.GREEN);
        cardObjectiveParcelLine.verify();

        assertTrue(IAforBot.resolvePatternLine(cardObjectiveParcelLine, game.getBoard()) != null);

    }

    @Test
    public void resolvePatternLineWhenWeCanPlaceAnParcelWhereIsTheMissingPositionWithLessParcels() {


        game.getBoard().addParcel(0,1,-1, new BambooPlantation(BambooColor.GREEN));

        CardObjectiveParcel cardObjectiveParcelLine = new CardObjectiveParcel(game.getBoard(), 2, Patterns.LINE, BambooColor.GREEN, BambooColor.GREEN, BambooColor.GREEN);
        cardObjectiveParcelLine.verify();

        PositionColored pos = new PositionColored(new Position(1,0,-1), BambooColor.GREEN);
        assertEquals(pos, IAforBot.resolvePatternLine(cardObjectiveParcelLine, game.getBoard()));

    }

    @Test
    public void resolvePatternLineWhenTheObjIsAlreadyResolve() {
        game.getBoard().addParcel(0,1,-1, new BambooPlantation(BambooColor.GREEN));
        game.getBoard().addParcel(1,0,-1, new BambooPlantation(BambooColor.GREEN));
        game.getBoard().addParcel(1,1,-2, new BambooPlantation(BambooColor.GREEN));
        game.getBoard().addParcel(1,-1,0, new BambooPlantation(BambooColor.GREEN));

        CardObjectiveParcel cardObjectiveParcelLine = new CardObjectiveParcel(game.getBoard(), 2, Patterns.LINE, BambooColor.GREEN, BambooColor.GREEN, BambooColor.GREEN);
        cardObjectiveParcelLine.verify();

        assertNull(IAforBot.resolvePatternLine(cardObjectiveParcelLine, game.getBoard()));
    }

    @Test
    public void resolvePatternLineWhenWeCanPlaceAnParcelWhereIsTheMissingPositionWithMoreParcels() {

        game.getBoard().addParcel(0,1,-1, new BambooPlantation(BambooColor.GREEN));
        game.getBoard().addParcel(1,0,-1, new BambooPlantation(BambooColor.GREEN));
        game.getBoard().addParcel(-1,1,0, new BambooPlantation(BambooColor.GREEN));

        CardObjectiveParcel cardObjectiveParcelLine = new CardObjectiveParcel(game.getBoard(), 2, Patterns.LINE, BambooColor.GREEN, BambooColor.GREEN, BambooColor.GREEN);
        cardObjectiveParcelLine.verify();

        PositionColored potentialPos1 = new PositionColored(new Position(1,1,-2), BambooColor.GREEN);
        PositionColored potentialPos2 = new PositionColored(new Position(1,-1,0), BambooColor.GREEN);
        PositionColored potentialPos3 = new PositionColored(new Position(-1,0,1), BambooColor.GREEN);
        PositionColored potentialPos4 = new PositionColored(new Position(-1,-1,2), BambooColor.GREEN);

        PositionColored test = IAforBot.resolvePatternLine(cardObjectiveParcelLine, game.getBoard());

        assertTrue(test.equals(potentialPos1) || test.equals(potentialPos2) || test.equals(potentialPos3) ||test.equals(potentialPos4));

    }

    @Test
    public void resolvePatternLineWithALotOfParcels() {

        game.getBoard().addParcel(0,1,-1, new BambooPlantation(BambooColor.GREEN));
        game.getBoard().addParcel(1,0,-1, new BambooPlantation(BambooColor.YELLOW));
        game.getBoard().addParcel(1,-1,0, new BambooPlantation(BambooColor.PINK));
        game.getBoard().addParcel(0,-1,1, new BambooPlantation(BambooColor.GREEN));
        game.getBoard().addParcel(-1,0,1, new BambooPlantation(BambooColor.YELLOW));
        game.getBoard().addParcel(-1,1,0, new BambooPlantation(BambooColor.PINK));
        game.getBoard().addParcel(1,1,-2, new BambooPlantation(BambooColor.YELLOW));
        game.getBoard().addParcel(-1,2,-1, new BambooPlantation(BambooColor.GREEN));
        game.getBoard().addParcel(0,2,-2, new BambooPlantation(BambooColor.GREEN));
        game.getBoard().addParcel(1,-2,1, new BambooPlantation(BambooColor.YELLOW));
        game.getBoard().addParcel(1,-1,2, new BambooPlantation(BambooColor.PINK));



        CardObjectiveParcel cardObjectiveParcelLine = new CardObjectiveParcel(game.getBoard(), 2, Patterns.LINE, BambooColor.GREEN, BambooColor.GREEN, BambooColor.GREEN);
        cardObjectiveParcelLine.verify();

        PositionColored placeWeWant = new PositionColored(new Position(1,2,-3), BambooColor.GREEN);

        PositionColored test = IAforBot.resolvePatternLine(cardObjectiveParcelLine, game.getBoard());
        assertEquals(placeWeWant, test);


    }

    @Test
    public void resolvePatternLineWithCObjective() {

        game.getBoard().addParcel(0,1,-1, new BambooPlantation(BambooColor.GREEN));
        game.getBoard().addParcel(1,0,-1, new BambooPlantation(BambooColor.YELLOW));
        game.getBoard().addParcel(1,-1,0, new BambooPlantation(BambooColor.PINK));
        game.getBoard().addParcel(0,-1,1, new BambooPlantation(BambooColor.GREEN));
        game.getBoard().addParcel(-1,0,1, new BambooPlantation(BambooColor.YELLOW));
        game.getBoard().addParcel(-1,1,0, new BambooPlantation(BambooColor.PINK));
        game.getBoard().addParcel(1,1,-2, new BambooPlantation(BambooColor.YELLOW));
        game.getBoard().addParcel(-1,2,-1, new BambooPlantation(BambooColor.GREEN));
        game.getBoard().addParcel(0,2,-2, new BambooPlantation(BambooColor.GREEN));
        game.getBoard().addParcel(1,-2,1, new BambooPlantation(BambooColor.YELLOW));
        game.getBoard().addParcel(1,-1,2, new BambooPlantation(BambooColor.PINK));

        CardObjectiveParcel cardObjectiveParcelC = new CardObjectiveParcel(game.getBoard(), 2, Patterns.C, BambooColor.GREEN, BambooColor.GREEN, BambooColor.GREEN);
        cardObjectiveParcelC.verify();

        PositionColored placeWeWant = new PositionColored(new Position(1,2,-3), BambooColor.GREEN);

        PositionColored test = IAforBot.resolvePatternLine(cardObjectiveParcelC, game.getBoard());
        assertEquals(placeWeWant, test);

    }
    @Test
    public void resolvePatternLineWithTriangleObjective() {

        game.getBoard().addParcel(0,1,-1, new BambooPlantation(BambooColor.GREEN));
        game.getBoard().addParcel(1,0,-1, new BambooPlantation(BambooColor.GREEN));
        game.getBoard().addParcel(1,-1,0, new BambooPlantation(BambooColor.YELLOW));
        game.getBoard().addParcel(-1,1,0, new BambooPlantation(BambooColor.PINK));

        CardObjectiveParcel cardObjectiveParcelLine = new CardObjectiveParcel(game.getBoard(), 2, Patterns.TRIANGLE, BambooColor.GREEN, BambooColor.GREEN, BambooColor.GREEN);
        cardObjectiveParcelLine.verify();

        PositionColored placeWeWant = new PositionColored(new Position(1,1,-2), BambooColor.GREEN);
        PositionColored test = IAforBot.resolvePatternLine(cardObjectiveParcelLine, game.getBoard());
        assertEquals(placeWeWant, test);

    }

    @Test
    public void resolvePatternLineWithTriangleObjectiveWithOneParcel() {

        game.getBoard().addParcel(-1,0,1, new BambooPlantation(BambooColor.YELLOW));
        game.getBoard().addParcel(0,-1,1, new BambooPlantation(BambooColor.YELLOW));
        game.getBoard().addParcel(-1,-1,2, new BambooPlantation(BambooColor.GREEN));

        CardObjectiveParcel cardObjectiveParcelLine = new CardObjectiveParcel(game.getBoard(), 2, Patterns.TRIANGLE, BambooColor.GREEN, BambooColor.GREEN, BambooColor.GREEN);
        cardObjectiveParcelLine.verify();

        PositionColored placeWeWant = new PositionColored(new Position(1,1,-2), BambooColor.GREEN);
        PositionColored test = IAforBot.resolvePatternLine(cardObjectiveParcelLine, game.getBoard());
        //assertEquals(placeWeWant, test);

    }


}
