package fr.matelots.polytech.core.players.bots;

import fr.matelots.polytech.core.game.Board;
import fr.matelots.polytech.core.game.Game;
import fr.matelots.polytech.core.game.goalcards.CardObjectiveGardener;
import fr.matelots.polytech.core.game.goalcards.CardObjectiveParcel;
import fr.matelots.polytech.core.game.goalcards.pattern.Patterns;
import fr.matelots.polytech.core.game.graphics.BoardDrawer;
import fr.matelots.polytech.core.game.parcels.BambooColor;
import fr.matelots.polytech.core.game.parcels.BambooPlantation;
import fr.matelots.polytech.core.players.IndividualBoard;
import fr.matelots.polytech.core.players.bots.logger.TurnLog;
import fr.matelots.polytech.engine.util.Position;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author williamdandrea
 */
public class FourthBotTest {

    Game game;
    FourthBot bot;
    TurnLog log;
    IndividualBoard individualBoard;
    Board board;

    @BeforeEach
    public void init() {
        game = new Game();
        bot = new FourthBot(game);
        log = new TurnLog(bot);
        board = bot.getBoard();
        individualBoard = bot.getIndividualBoard();
        game.addBot(bot);
    }

    /**
     * Here, we test the start of the game, we verify if we take MAX_NUMBER_OF_PARCEL_OBJECTIVES and the
     * MAX_NUMBER_OF_GARDENER_OBJECTIVES
     */
    @Test
    public void launchGameTest(){
        // We verify if the firstLaunch boolean variable is at true (because we launch the game)
        //assertTrue(bot.isFirstLaunch()); -> devenue inutile

        // Now we launch the bot
        bot.playTurn2(log, null);

        // Now we verify if we pick the good number of objectives into the individualBoard
        assertEquals(individualBoard.countUnfinishedParcelObjectives(), bot.getNumberOfParcelObjectivesAtTheStart());

        bot.playTurn2(log, null);
        assertEquals(individualBoard.countUnfinishedGardenerObjectives(), bot.getNumberOfGardenerObjectivesAtTheStart());

        // Now, we verify if the firstLaunch variable is False because we pick the good number of objectives
        // assertFalse(bot.isFirstLaunch()); -> devenue inutile
    }

    @Test @DisplayName("L'objectif parcelle qui sera tenté d'être résolu est bien celle où il ya le moins de parcelles manquantes")
    public void testBestObjectiveParcel() {
        CardObjectiveParcel obj = new CardObjectiveParcel(this.bot.getBoard(), 1, Patterns.LINE, BambooColor.PINK, BambooColor.PINK, BambooColor.PINK);
        this.individualBoard.addNewParcelObjective(new CardObjectiveParcel(this.bot.getBoard(), 1, Patterns.LINE, BambooColor.GREEN, BambooColor.GREEN, BambooColor.GREEN));
        this.individualBoard.addNewParcelObjective(obj);
        this.bot.getBoard().addParcel(0, 1, -1, new BambooPlantation(BambooColor.GREEN));
        this.bot.getBoard().addParcel(1, 0, -1, new BambooPlantation(BambooColor.PINK));
        this.bot.getBoard().addParcel(1, 1, -2, new BambooPlantation(BambooColor.PINK));
        this.bot.playTurn2(log, null);
        assertEquals(bot.currentParcelObjective, obj);
    }

    @Test @DisplayName("L'objectif gardener qui sera tenté d'être résolu est bien celle où il ya le moins de bamboo manquantes")
    public void testBestGardenerParcel() {
        CardObjectiveGardener obj = new CardObjectiveGardener(this.bot.getBoard(), 1, BambooColor.PINK, 3, 3);
        this.individualBoard.addNewGardenerObjective(new CardObjectiveGardener(this.bot.getBoard(), 1, BambooColor.GREEN, 3, 3));
        this.individualBoard.addNewGardenerObjective(obj);
        BambooPlantation plantation = new BambooPlantation(BambooColor.GREEN);
        BambooPlantation plantation1 = new BambooPlantation(BambooColor.PINK);
        BambooPlantation plantation2 = new BambooPlantation(BambooColor.PINK);
        this.bot.getBoard().addParcel(0, 1, -1, plantation);
        this.bot.getBoard().addParcel(1, 0, -1, plantation1);
        this.bot.getBoard().addParcel(1, -1, 0, plantation2);
        plantation.growBamboo();
        plantation.growBamboo();
        plantation.growBamboo();
        plantation1.growBamboo();
        plantation1.growBamboo();
        plantation1.growBamboo();
        plantation2.growBamboo();
        plantation2.growBamboo();
        plantation2.growBamboo();
        this.bot.playTurn2(log, null);
        assertEquals(bot.currentGardenerObjective, obj);
    }

    @Test @DisplayName("Aucune carte objectif est la plus simple")
    public void testNoEasiestObjective() {
        assertNull(this.bot.easiestObjectiveToResolve());
    }

    @Test @DisplayName("La carte objectif la plus simple à compléter est celle de la jardinier car aucun objectif parcelle")
    public void testEasiestGardenerObjectiveNoParcel() {
        CardObjectiveGardener cardObjectiveGardener = new CardObjectiveGardener(bot.getBoard(), 1, BambooColor.GREEN, 1, 1);
        this.bot.currentGardenerObjective = cardObjectiveGardener;
        assertEquals(cardObjectiveGardener, this.bot.easiestObjectiveToResolve());
    }

    @Test @DisplayName("La carte objectif la plus simple à compléter et celle de la parcelle car aucun objectif jardinier")
    public void testEasiestParcelObjectiveNoGardener() {
        CardObjectiveParcel cardObjectiveParcel = new CardObjectiveParcel(bot.getBoard(), 1, Patterns.C, BambooColor.GREEN, BambooColor.GREEN, BambooColor.GREEN);
        this.bot.currentParcelObjective = cardObjectiveParcel;
        assertEquals(cardObjectiveParcel, this.bot.easiestObjectiveToResolve());
    }

    @Test @DisplayName("La carte objectif la plus simple à compléter est celle de la jardinier")
    public void testEasiestGardenerObjective() {
        CardObjectiveGardener cardObjectiveGardener = new CardObjectiveGardener(bot.getBoard(), 1, BambooColor.GREEN, 1, 1);
        this.bot.currentGardenerObjective = cardObjectiveGardener;
        this.bot.currentParcelObjective = new CardObjectiveParcel(bot.getBoard(), 2, Patterns.C, BambooColor.GREEN, BambooColor.GREEN, BambooColor.GREEN);
        assertEquals(cardObjectiveGardener, this.bot.easiestObjectiveToResolve());
    }

    @Test @DisplayName("La carte objectif la plus simple à compléter et celle de la parcelle")
    public void testEasiestParcelObjective() {
        CardObjectiveParcel cardObjectiveParcel = new CardObjectiveParcel(bot.getBoard(), 1, Patterns.C, BambooColor.GREEN, BambooColor.GREEN, BambooColor.GREEN);
        this.bot.currentParcelObjective = cardObjectiveParcel;
        this.bot.currentGardenerObjective = new CardObjectiveGardener(bot.getBoard(), 2, BambooColor.GREEN, 1, 1);
        assertEquals(cardObjectiveParcel, this.bot.easiestObjectiveToResolve());
    }

    @Test @DisplayName("Aucune parcelle posé, il va donc poser une parcelle quelque part plutôt qu'essaie de résoudre l'objectif parcelle")
    public void testTryToResolveParcelObjectiveOnlyPond() {
        this.bot.currentParcelObjective = new CardObjectiveParcel(bot.getBoard(), 1, Patterns.C, BambooColor.GREEN, BambooColor.GREEN, BambooColor.GREEN);
        this.bot.tryToResolveParcelObjective(this.log);
        assertEquals(2, this.bot.getBoard().getParcelCount());
    }

    @Test @DisplayName("Aucune objectif parcelle, il va donc poser une parcelle quelque part plutôt qu'essaie de résoudre l'objectif parcelle")
    public void testTryToResolveParcelObjectiveNoParcelObjective() {
        this.bot.getBoard().addParcel(1, 0, -1, new BambooPlantation(BambooColor.GREEN));
        this.bot.tryToResolveParcelObjective(this.log);
        assertEquals(3, this.bot.getBoard().getParcelCount());
    }

    @Test @DisplayName("Place une parcelle à la position manquante pour compléter l'objectif")
    public void testPlaceParcelToCompleteParcelObjective() {
        this.bot.getBoard().addParcel(1, -1, 0, new BambooPlantation(BambooColor.GREEN));
        this.bot.currentParcelObjective = new CardObjectiveParcel(bot.getBoard(), 2, Patterns.C, BambooColor.GREEN, BambooColor.GREEN, BambooColor.GREEN);
        this.bot.currentParcelObjective.verify();
        this.bot.tryToResolveParcelObjective(this.log);
        assertTrue(this.bot.getBoard().containTile(new Position(0, -1, 1)));
    }

    //Il manque l'ago
    @Test @DisplayName("Place une parcelle pour pouvoir placer une parcelle manquante pour compléter l'objectif")
    public void testPlaceParcelToPlaceParcelCompleteParcelObjective() {
        this.bot.getBoard().addParcel(1, -1, 0, new BambooPlantation(BambooColor.GREEN));
        this.bot.getBoard().addParcel(0, -1, 1, new BambooPlantation(BambooColor.GREEN));
        this.bot.currentParcelObjective = new CardObjectiveParcel(bot.getBoard(), 2, Patterns.C, BambooColor.GREEN, BambooColor.GREEN, BambooColor.GREEN);
        this.bot.currentParcelObjective.verify();
        this.bot.tryToResolveParcelObjective(this.log);
        assertTrue(this.bot.getBoard().containTile(new Position(1, -2, 1)));
    }

    @Test @DisplayName("Place une parcelle quelque part car ne peut pas compléter l'objective jardinier")
    public void testResolveGardenerPlaceParcel() {
        this.bot.getBoard().addParcel(1, -1, 0, new BambooPlantation(BambooColor.PINK));
        this.bot.currentGardenerObjective = new CardObjectiveGardener(bot.getBoard(), 2, BambooColor.GREEN, 1, 1);
        this.bot.tryToResolveGardenerObjective(this.log);
        assertEquals(3, this.bot.getBoard().getParcelCount());
    }

    @Test @DisplayName("Déplace le jardinier sur la meilleur parcelle pour compléter l'objectif jardinier")
    public void testResolveGardenerMoveGardenerToBestParcel() {
        this.bot.getBoard().addParcel(1, -1, 0, new BambooPlantation(BambooColor.PINK));
        this.bot.getBoard().addParcel(1, 0, -1, new BambooPlantation(BambooColor.GREEN));
        this.bot.currentGardenerObjective = new CardObjectiveGardener(bot.getBoard(), 2, BambooColor.GREEN, 2, 1);
        this.bot.tryToResolveGardenerObjective(this.log);
        assertEquals(new Position(1, 0, -1), this.bot.getBoard().getGardener().getPosition());
    }

    @Test @DisplayName("L'objectif jardinier est complété et une autre carte a été tiré")
    public void testCheckObjectiveGardener() {
        this.bot.currentGardenerObjective = new CardObjectiveGardener(bot.getBoard(), 2, BambooColor.GREEN, 1, 1);
        this.bot.getBoard().addParcel(1, 0, -1, new BambooPlantation(BambooColor.GREEN));
        this.bot.playTurn2(this.log, null);
        assertEquals(1, bot.numberOfResolveObjective);
        assertEquals(1, this.bot.getIndividualBoard().countCompletedObjectives());
        assertTrue(this.bot.currentGardenerObjective.isCompleted());
    }

    @Test @DisplayName("L'objectif parcelle est complété et une autre carte a été tiré")
    public void testCheckObjectiveParcelCompleted() {
        this.individualBoard.addNewParcelObjective(new CardObjectiveParcel(bot.getBoard(), 2, Patterns.C, BambooColor.GREEN, BambooColor.GREEN, BambooColor.GREEN));
        this.individualBoard.addNewParcelObjective(new CardObjectiveParcel(bot.getBoard(), 2, Patterns.LINE, BambooColor.GREEN, BambooColor.GREEN, BambooColor.GREEN));
        this.individualBoard.addNewGardenerObjective(new CardObjectiveGardener(bot.getBoard(), 3, BambooColor.GREEN, 1, 1));
        this.individualBoard.addNewGardenerObjective(new CardObjectiveGardener(bot.getBoard(), 3, BambooColor.GREEN, 1, 1));
        this.bot.getBoard().addParcel(1, -1, 0, new BambooPlantation(BambooColor.GREEN));
        this.bot.getBoard().addParcel(1, 0, -1, new BambooPlantation(BambooColor.GREEN));
        this.bot.getBoard().addParcel(2, -1, -1, new BambooPlantation(BambooColor.GREEN));
        this.bot.getBoard().addParcel(2, 0, -2, new BambooPlantation(BambooColor.GREEN));
        this.bot.playTurn2(this.log, null);
        assertEquals(1, bot.numberOfResolveObjective);
        assertEquals(1, this.bot.getIndividualBoard().countCompletedObjectives());
        assertTrue(this.bot.currentParcelObjective.isCompleted());
    }

    @Test @DisplayName("L'objectif jardinier est complété et une autre carte a été tiré")
    public void testCheckObjectiveGardenerCompleted() {
        this.individualBoard.addNewParcelObjective(new CardObjectiveParcel(bot.getBoard(), 2, Patterns.C, BambooColor.GREEN, BambooColor.GREEN, BambooColor.GREEN));
        this.individualBoard.addNewParcelObjective(new CardObjectiveParcel(bot.getBoard(), 2, Patterns.LINE, BambooColor.GREEN, BambooColor.GREEN, BambooColor.GREEN));
        this.individualBoard.addNewGardenerObjective(new CardObjectiveGardener(bot.getBoard(), 1, BambooColor.GREEN, 1, 1));
        this.individualBoard.addNewGardenerObjective(new CardObjectiveGardener(bot.getBoard(), 1, BambooColor.GREEN, 2, 1));
        BambooPlantation plantation = new BambooPlantation(BambooColor.GREEN);
        this.bot.getBoard().addParcel(1, -1, 0, plantation);
        plantation.growBamboo();
        this.bot.playTurn2(this.log, null);
        assertEquals(1, bot.numberOfResolveObjective);
        assertEquals(1, this.bot.getIndividualBoard().countCompletedObjectives());
        assertTrue(this.bot.currentGardenerObjective.isCompleted());
    }

    @Test @DisplayName("Le jardinier est déplacé sur une ligne droite")
    public void testGardenerMoveLine() {
        Position goal = new Position(3, 0, -3);
        board.addBambooPlantation(new Position(1, 0, -1));
        board.addBambooPlantation(new Position(1, -1, 0));
        board.addBambooPlantation(new Position(2, -1, -1));
        board.addBambooPlantation( new Position(2, 0, -2));
        board.addBambooPlantation(new Position(3, -1, -2));
        board.addBambooPlantation(goal);
        assertTrue(this.bot.moveGardener(goal, log));
        assertEquals(goal, board.getGardener().getPosition());
    }

    @Test @DisplayName("Le jardinier doit être déplacé 3 fois pour atteindre l'objectif")
    public void testGardenerMoveComplex() {
        Position goal = new Position(1, 2, -3);
        Position firstStop = new Position(2, 0, -2);
        Position secondStop = new Position(2, 1, -3);
        board.addBambooPlantation(new Position(1, 0, -1));
        board.addBambooPlantation(new Position(1, -1, 0));
        board.addBambooPlantation(new Position(2, -1, -1));
        board.addBambooPlantation(firstStop);
        board.addBambooPlantation(new Position(3, -1, -2));
        board.addBambooPlantation(new Position(3, 0, -3));
        board.addBambooPlantation(secondStop);
        board.addBambooPlantation(new Position(3, 1, -4));
        board.addBambooPlantation(new Position(2, 2, -4));
        board.addBambooPlantation(goal);
        BoardDrawer drawer = new BoardDrawer(board);
        drawer.print();
        this.bot.moveGardener(goal, log);
        assertEquals(firstStop, board.getGardener().getPosition());
        this.bot.moveGardener(goal, log);
        assertEquals(secondStop, board.getGardener().getPosition());
        bot.setCurrentNumberOfAction(0);
        this.bot.moveGardener(goal, log);
        assertEquals(goal, board.getGardener().getPosition());
    }




    /**
     * Here, we test the after start
     */
    /*@Test
    public void gameTest(){

        // We launch the bot one time
        bot.playTurn(log);
        // Now we verify if we pick the good number of objectives into the individualBoard
        assertEquals(individualBoard.countUnfinishedParcelObjectives(), bot.getNumberOfParcelObjectivesAtTheStart());
        assertEquals(individualBoard.countUnfinishedGardenerObjectives(), bot.getNumberOfGardenerObjectivesAtTheStart());

        // Now we relauch a turn
        bot.playTurn(log);
        for (int i = 0; i< 20 ; i++) {
            bot.playTurn(log);
        }

    }*/
}
