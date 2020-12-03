package fr.matelots.polytech.core.game.card.panda;

import fr.matelots.polytech.core.game.goalcards.CardObjectivePanda;
import fr.matelots.polytech.core.game.parcels.BambooColor;
import fr.matelots.polytech.core.players.IndividualBoard;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Alexandre Arcil
 */
public class CardObjectivePandaTest {

    private IndividualBoard board;
    private CardObjectivePanda greenBambooCard;
    private CardObjectivePanda pinkBambooCard;
    private CardObjectivePanda yellowBambooCard;
    private CardObjectivePanda everyColorCard;

    @BeforeEach
    public void init() {
        this.board = new IndividualBoard();
        this.greenBambooCard = new CardObjectivePanda(1, 2, 0, 0);
        this.pinkBambooCard = new CardObjectivePanda(1, 0, 2, 0);
        this.yellowBambooCard = new CardObjectivePanda(1, 0, 0, 2);
        this.everyColorCard = new CardObjectivePanda(1, 1, 1, 1);
    }

    @Test @DisplayName("carte '2 bambous verts' avec aucun bambou")
    public void twoGreenBambooNoBamboo() {
        this.greenBambooCard.setIndividualBoard(this.board);
        assertFalse(this.greenBambooCard.verify());
    }

    @Test @DisplayName("carte '2 bambous verts' avec 1 bambou vert")
    public void twoGreenBambooOneBambooGreen() {
        this.board.addAnEatenUnitOfBamboo(BambooColor.GREEN);
        this.greenBambooCard.setIndividualBoard(this.board);
        assertFalse(this.greenBambooCard.verify());
    }

    @Test @DisplayName("carte '2 bambous verts' complété")
    public void twoGreenBambooCompleted() {
        this.board.addAnEatenUnitOfBamboo(BambooColor.GREEN);
        this.board.addAnEatenUnitOfBamboo(BambooColor.GREEN);
        this.greenBambooCard.setIndividualBoard(this.board);
        assertTrue(this.greenBambooCard.verify());
    }

    @Test @DisplayName("carte '2 bambous verts' complété avec 3 bamboos verts")
    public void twoGreenBambooCompletedExtra() {
        this.board.addAnEatenUnitOfBamboo(BambooColor.GREEN);
        this.board.addAnEatenUnitOfBamboo(BambooColor.GREEN);
        this.board.addAnEatenUnitOfBamboo(BambooColor.GREEN);
        this.greenBambooCard.setIndividualBoard(this.board);
        assertTrue(this.greenBambooCard.verify());
    }

    @Test @DisplayName("carte '2 bambous verts' avec 2 bamboo rose")
    public void twoGreenBambooTwoPinkBamboo() {
        this.board.addAnEatenUnitOfBamboo(BambooColor.PINK);
        this.board.addAnEatenUnitOfBamboo(BambooColor.PINK);
        this.greenBambooCard.setIndividualBoard(this.board);
        assertFalse(this.greenBambooCard.verify());
    }

    @Test @DisplayName("carte '2 bambous verts' avec 2 bambous jaune")
    public void twoGreenBambooTwoYellowBamboo() {
        this.board.addAnEatenUnitOfBamboo(BambooColor.YELLOW);
        this.board.addAnEatenUnitOfBamboo(BambooColor.YELLOW);
        this.greenBambooCard.setIndividualBoard(this.board);
        assertFalse(this.greenBambooCard.verify());
    }




    @Test @DisplayName("carte '2 bambous rose' avec aucun bambou")
    public void twoPinkBambooNoBamboo() {
        this.pinkBambooCard.setIndividualBoard(this.board);
        assertFalse(this.pinkBambooCard.verify());
    }

    @Test @DisplayName("carte '2 bambous rose' avec 1 bambou rose")
    public void twoPinkBambooOneBambooPink() {
        this.board.addAnEatenUnitOfBamboo(BambooColor.PINK);
        this.pinkBambooCard.setIndividualBoard(this.board);
        assertFalse(this.pinkBambooCard.verify());
    }

    @Test @DisplayName("carte '2 bambous rose' complété")
    public void twoPinkBambooCompleted() {
        this.board.addAnEatenUnitOfBamboo(BambooColor.PINK);
        this.board.addAnEatenUnitOfBamboo(BambooColor.PINK);
        this.pinkBambooCard.setIndividualBoard(this.board);
        assertTrue(this.pinkBambooCard.verify());
    }

    @Test @DisplayName("carte '2 bambous rose' complété avec 3 bamboos rose")
    public void twoPinkBambooCompletedExtra() {
        this.board.addAnEatenUnitOfBamboo(BambooColor.PINK);
        this.board.addAnEatenUnitOfBamboo(BambooColor.PINK);
        this.board.addAnEatenUnitOfBamboo(BambooColor.PINK);
        this.pinkBambooCard.setIndividualBoard(this.board);
        assertTrue(this.pinkBambooCard.verify());
    }

    @Test @DisplayName("carte '2 bambous rose' avec 2 bamboo verts")
    public void twoPinkBambooTwoGreenBamboo() {
        this.board.addAnEatenUnitOfBamboo(BambooColor.GREEN);
        this.board.addAnEatenUnitOfBamboo(BambooColor.GREEN);
        this.pinkBambooCard.setIndividualBoard(this.board);
        assertFalse(this.pinkBambooCard.verify());
    }

    @Test @DisplayName("carte '2 bambous rose' avec 2 bambous jaune")
    public void twoPinkBambooTwoYellowBamboo() {
        this.board.addAnEatenUnitOfBamboo(BambooColor.YELLOW);
        this.board.addAnEatenUnitOfBamboo(BambooColor.YELLOW);
        this.pinkBambooCard.setIndividualBoard(this.board);
        assertFalse(this.pinkBambooCard.verify());
    }




    @Test @DisplayName("carte '2 bambous jaune' avec aucun bambou")
    public void twoYellowBambooNoBamboo() {
        this.yellowBambooCard.setIndividualBoard(this.board);
        assertFalse(this.yellowBambooCard.verify());
    }

    @Test @DisplayName("carte '2 bambous jaune' avec 1 bambou jaune")
    public void twoYellowBambooOneBambooYellow() {
        this.board.addAnEatenUnitOfBamboo(BambooColor.YELLOW);
        this.yellowBambooCard.setIndividualBoard(this.board);
        assertFalse(this.yellowBambooCard.verify());
    }

    @Test @DisplayName("carte '2 bambous jaune' complété")
    public void twoYellowBambooCompleted() {
        this.board.addAnEatenUnitOfBamboo(BambooColor.YELLOW);
        this.board.addAnEatenUnitOfBamboo(BambooColor.YELLOW);
        this.yellowBambooCard.setIndividualBoard(this.board);
        assertTrue(this.yellowBambooCard.verify());
    }

    @Test @DisplayName("carte '2 bambous jaune' complété avec 3 bamboos jaune")
    public void twoYellowBambooCompletedExtra() {
        this.board.addAnEatenUnitOfBamboo(BambooColor.YELLOW);
        this.board.addAnEatenUnitOfBamboo(BambooColor.YELLOW);
        this.board.addAnEatenUnitOfBamboo(BambooColor.YELLOW);
        this.yellowBambooCard.setIndividualBoard(this.board);
        assertTrue(this.yellowBambooCard.verify());
    }

    @Test @DisplayName("carte '2 bambous jaune' avec 2 bamboo verts")
    public void twoYellowBambooTwoGreenBamboo() {
        this.board.addAnEatenUnitOfBamboo(BambooColor.GREEN);
        this.board.addAnEatenUnitOfBamboo(BambooColor.GREEN);
        this.yellowBambooCard.setIndividualBoard(this.board);
        assertFalse(this.yellowBambooCard.verify());
    }

    @Test @DisplayName("carte '2 bambous jaune' avec 2 bambous rose")
    public void twoYellowBambooTwoPinkBamboo() {
        this.board.addAnEatenUnitOfBamboo(BambooColor.PINK);
        this.board.addAnEatenUnitOfBamboo(BambooColor.PINK);
        this.yellowBambooCard.setIndividualBoard(this.board);
        assertFalse(this.yellowBambooCard.verify());
    }




    @Test @DisplayName("carte '1 bambou de toute les couleurs' avec aucun bambou")
    public void everyColorBambooNoBamboo() {
        this.everyColorCard.setIndividualBoard(this.board);
        assertFalse(this.everyColorCard.verify());
    }

    @Test @DisplayName("carte '1 bambou de toute les couleurs' avec 1 bambou vert")
    public void everyColorBambooOneGreen() {
        this.everyColorCard.setIndividualBoard(this.board);
        this.board.addAnEatenUnitOfBamboo(BambooColor.GREEN);
        assertFalse(this.everyColorCard.verify());
    }

    @Test @DisplayName("carte '1 bambou de toute les couleurs' avec 1 bambou vert et 1 bamboo rose")
    public void everyColorBambooOneGreenOnePink() {
        this.everyColorCard.setIndividualBoard(this.board);
        this.board.addAnEatenUnitOfBamboo(BambooColor.GREEN);
        this.board.addAnEatenUnitOfBamboo(BambooColor.PINK);
        assertFalse(this.everyColorCard.verify());
    }

    @Test @DisplayName("carte '1 bambou de toute les couleurs' avec 1 bambou vert, rose et jaune")
    public void everyColorBambooOneGreenOnePinkOneYellow() {
        this.everyColorCard.setIndividualBoard(this.board);
        this.board.addAnEatenUnitOfBamboo(BambooColor.GREEN);
        this.board.addAnEatenUnitOfBamboo(BambooColor.PINK);
        this.board.addAnEatenUnitOfBamboo(BambooColor.YELLOW);
        assertTrue(this.everyColorCard.verify());
    }

    @Test @DisplayName("carte '1 bambou de toute les couleurs' avec 2 bambou vert, 1 rose et 1 jaune")
    public void everyColorBambooTwoGreenOnePinkOneYellow() {
        this.everyColorCard.setIndividualBoard(this.board);
        this.board.addAnEatenUnitOfBamboo(BambooColor.GREEN);
        this.board.addAnEatenUnitOfBamboo(BambooColor.GREEN);
        this.board.addAnEatenUnitOfBamboo(BambooColor.PINK);
        this.board.addAnEatenUnitOfBamboo(BambooColor.YELLOW);
        assertTrue(this.everyColorCard.verify());
    }

    @Test @DisplayName("carte '1 bambou de toute les couleurs' avec 2 bambou vert, 2 rose et 1 jaune")
    public void everyColorBambooTwoGreenTwoPinkOneYellow() {
        this.everyColorCard.setIndividualBoard(this.board);
        this.board.addAnEatenUnitOfBamboo(BambooColor.GREEN);
        this.board.addAnEatenUnitOfBamboo(BambooColor.GREEN);
        this.board.addAnEatenUnitOfBamboo(BambooColor.PINK);
        this.board.addAnEatenUnitOfBamboo(BambooColor.PINK);
        this.board.addAnEatenUnitOfBamboo(BambooColor.YELLOW);
        assertTrue(this.everyColorCard.verify());
    }

    @Test @DisplayName("carte '1 bambou de toute les couleurs' avec 2 bambou vert, rose et jaune")
    public void everyColorBambooTwoGreenTwoPinkTwoYellow() {
        this.everyColorCard.setIndividualBoard(this.board);
        this.board.addAnEatenUnitOfBamboo(BambooColor.GREEN);
        this.board.addAnEatenUnitOfBamboo(BambooColor.GREEN);
        this.board.addAnEatenUnitOfBamboo(BambooColor.PINK);
        this.board.addAnEatenUnitOfBamboo(BambooColor.PINK);
        this.board.addAnEatenUnitOfBamboo(BambooColor.YELLOW);
        this.board.addAnEatenUnitOfBamboo(BambooColor.YELLOW);
        assertTrue(this.everyColorCard.verify());
    }


}
