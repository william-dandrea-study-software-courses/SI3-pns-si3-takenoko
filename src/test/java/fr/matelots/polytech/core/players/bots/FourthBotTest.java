
package fr.matelots.polytech.core.players.bots;


import fr.matelots.polytech.core.game.Board;
import fr.matelots.polytech.core.game.Config;
import fr.matelots.polytech.core.game.Game;
import fr.matelots.polytech.core.game.goalcards.CardObjectiveGardener;
import fr.matelots.polytech.core.game.goalcards.CardObjectiveParcel;
import fr.matelots.polytech.core.game.goalcards.pattern.Patterns;
import fr.matelots.polytech.core.game.graphics.BoardDrawer;
import fr.matelots.polytech.core.game.parcels.BambooColor;
import fr.matelots.polytech.core.game.parcels.BambooPlantation;
import fr.matelots.polytech.core.game.parcels.Layout;
import fr.matelots.polytech.core.game.parcels.Side;
import fr.matelots.polytech.core.players.IndividualBoard;
import fr.matelots.polytech.core.players.bots.logger.BotActionType;
import fr.matelots.polytech.core.players.bots.logger.TurnLog;
import fr.matelots.polytech.engine.util.Position;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

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

    /*@Test @DisplayName("Le bot prend 2 cartes parcelle puis 2 cartes jardinier pendant les 4 premiers tours")
    public void takeFourCardsAtTheBeginning() {
        for (int i = 0; i < 4; i++)
            this.bot.playTurn(log);
        List<CardObjective> collect = Arrays.stream(this.individualBoard.getCompletedObjectives())
                .filter(cardObjective -> cardObjective instanceof CardObjectiveParcel)
                .collect(Collectors.toList());
        assertTrue(2 <= this.individualBoard.countUnfinishedParcelObjectives() + collect.size());
        List<CardObjective> collect2 = Arrays.stream(this.individualBoard.getCompletedObjectives())
                .filter(cardObjective -> cardObjective instanceof CardObjectiveGardener)
                .collect(Collectors.toList());
        assertTrue(2 <= this.individualBoard.countUnfinishedGardenerObjectives() + collect2.size());
    }*/



    @Test @DisplayName("Si le bot a aucune carte jardinier non complété et peut tirer une carte, il le fait et l'ajout à une list")
    public void noIncompleteGardener() {
        for (int i = 0; i < 4; i++)
            this.individualBoard.addNewParcelObjective(new CardObjectiveParcel(this.board, 1, Patterns.LINE, BambooColor.GREEN,  BambooColor.GREEN,  BambooColor.GREEN));
        this.individualBoard.addNewParcelObjective(new CardObjectiveParcel(this.board, 1, Patterns.C, BambooColor.GREEN,  BambooColor.GREEN,  BambooColor.GREEN));
        this.board.addParcel(0, 1, -1, new BambooPlantation(BambooColor.GREEN));
        this.board.addParcel(-1, 1, 0, new BambooPlantation(BambooColor.GREEN));
        this.board.addParcel(-1, 0, 1, new BambooPlantation(BambooColor.GREEN));
        assertDoesNotThrow(() -> this.bot.playTurn(log));
    }

    @Test @DisplayName("Prend une irrigation quand souhaité")
    public void takeNeededIrrigation() {
        this.bot.irrigationNeeded = 1;
        this.bot.fillIrrigations(log);
        assertEquals(0, this.bot.irrigationNeeded);
        assertEquals(1, this.individualBoard.getNumberOfIrrigations());
    }

    @Test @DisplayName("Ne prend pas d'irrigation quand cela n'est pas demandé")
    public void dontTakeIrrigation() {
        this.bot.fillIrrigations(log);
        assertEquals(0, this.bot.irrigationNeeded);
        assertEquals(0, this.individualBoard.getNumberOfIrrigations());
    }

    @Test @DisplayName("Prend seulement une irrigation quand souhaité")
    public void takeOneNeededIrrigation() {
        this.bot.irrigationNeeded = 3;
        this.bot.fillIrrigations(log);
        assertEquals(2, this.bot.irrigationNeeded);
        assertEquals(1, this.individualBoard.getNumberOfIrrigations());
    }

    @Test @DisplayName("hasAlreadyPickGoal retourne true car le bot à pris un objective parcelle")
    public void hasAlreadyPickGoalPickParcelObjective() {
        this.bot.pickParcelObjective(log);
        assertTrue(this.bot.hasAlreadyPickGoal());
    }

    @Test @DisplayName("hasAlreadyPickGoal retourne true car le bot à pris un objective jardinier")
    public void hasAlreadyPickGoalPickGardenerObjective() {
        this.bot.pickGardenerObjective(log);
        assertTrue(this.bot.hasAlreadyPickGoal());
    }

    @Test @DisplayName("hasAlreadyPickGoal retourne true car le bot à pris un objective panda")
    public void hasAlreadyPickGoalPandaObjective() {
        this.bot.pickPandaObjective(log);
        assertTrue(this.bot.hasAlreadyPickGoal());
    }

    @Test @DisplayName("hasAlreadyPickGoal retourne false car le bot n'a jamais pris une carte objective")
    public void hasAlreadyPickGoalNoObjectiveTaken() {
        Position pos = new Position(0, 1, -1);
        this.board.addParcel(pos, new BambooPlantation(BambooColor.GREEN));
        assertFalse(this.bot.hasAlreadyPickGoal());
        this.bot.placeParcel(new Position(1, 0, -1), BambooColor.GREEN, log);
        assertFalse(this.bot.hasAlreadyPickGoal());
        this.bot.playTurn(log);
        this.bot.moveGardener(pos, log);
        assertFalse(this.bot.hasAlreadyPickGoal());
        this.bot.playTurn(log);
        this.bot.movePanda(log, pos);
        assertFalse(this.bot.hasAlreadyPickGoal());
        this.bot.playTurn(log);
        this.bot.placeLayout(log, new BambooPlantation(BambooColor.GREEN), Layout.ENCLOSURE);
        assertFalse(this.bot.hasAlreadyPickGoal());
        this.bot.playTurn(log);
        this.bot.pickIrrigation(log);
        assertFalse(this.bot.hasAlreadyPickGoal());
    }

    @Test
    public void canDoParcelObjectiveMissingParcelNull() {
        CardObjectiveParcel card = new CardObjectiveParcel(board, 1, Patterns.C, BambooColor.GREEN, BambooColor.GREEN, BambooColor.GREEN);
        assertTrue(this.bot.canDoParcelObjective(card));
    }

    @Test
    public void canDoParcelObjectiveNecessaryParcelAvailable() {
        CardObjectiveParcel card = new CardObjectiveParcel(board, 1, Patterns.C, BambooColor.GREEN, BambooColor.GREEN, BambooColor.GREEN);
        card.verify();
        assertTrue(this.bot.canDoParcelObjective(card));
    }

    @Test
    public void canDoParcelObjectiveStrictNecessaryParcelAvailable() {
        CardObjectiveParcel card = new CardObjectiveParcel(board, 1, Patterns.C, BambooColor.GREEN, BambooColor.GREEN, BambooColor.GREEN);
        card.verify();
        for (int i = 0; i < Config.NB_MAX_GREEN_PARCELS - card.getColors().length; i++) {
            this.placeAnParcelAnywhere(BambooColor.GREEN);
        }
        assertTrue(this.bot.canDoParcelObjective(card));
    }

    @Test
    public void canDoParcelObjectiveParcelNotAvailable() {
        CardObjectiveParcel card = new CardObjectiveParcel(board, 1, Patterns.C, BambooColor.GREEN, BambooColor.GREEN, BambooColor.GREEN);
        card.verify();
        for (int i = 0; i < Config.NB_MAX_GREEN_PARCELS - card.getColors().length + 1; i++) {
            this.placeAnParcelAnywhere(BambooColor.GREEN);
        }
        assertFalse(this.bot.canDoParcelObjective(card));
    }

    @Test
    public void canDoParcelObjectiveParcelMultipleColors() {
        CardObjectiveParcel card = new CardObjectiveParcel(board, 1, Patterns.RHOMBUS, BambooColor.GREEN, BambooColor.GREEN, BambooColor.YELLOW, BambooColor.YELLOW);
        card.verify();
        assertTrue(this.bot.canDoParcelObjective(card));
    }

    @Test
    public void canDoParcelObjectiveParcelStrictNecessaryParcelAvailableMultipleColors() {
        CardObjectiveParcel card = new CardObjectiveParcel(board, 1, Patterns.RHOMBUS, BambooColor.GREEN, BambooColor.GREEN, BambooColor.YELLOW, BambooColor.YELLOW);
        card.verify();
        for (int i = 0; i < Config.NB_MAX_GREEN_PARCELS - 2; i++) {
            this.placeAnParcelAnywhere(BambooColor.GREEN);
        }
        for (int i = 0; i < Config.NB_MAX_YELLOW_PARCELS - 2; i++) {
            this.placeAnParcelAnywhere(BambooColor.YELLOW);
        }
        assertTrue(this.bot.canDoParcelObjective(card));
    }

    @Test
    public void canDoParcelObjectiveParcelNotAvailableMultipleColorOne() {
        CardObjectiveParcel card = new CardObjectiveParcel(board, 1, Patterns.C, BambooColor.GREEN, BambooColor.GREEN, BambooColor.GREEN);
        card.verify();
        for (int i = 0; i < Config.NB_MAX_GREEN_PARCELS - 1; i++) {
            this.placeAnParcelAnywhere(BambooColor.GREEN);
        }
        for (int i = 0; i < Config.NB_MAX_YELLOW_PARCELS - 2; i++) {
            this.placeAnParcelAnywhere(BambooColor.YELLOW);
        }
        assertFalse(this.bot.canDoParcelObjective(card));
    }

    @Test
    public void canDoParcelObjectiveParcelNotAvailableMultipleColorTwo() {
        CardObjectiveParcel card = new CardObjectiveParcel(board, 1, Patterns.C, BambooColor.GREEN, BambooColor.GREEN, BambooColor.GREEN);
        card.verify();
        for (int i = 0; i < Config.NB_MAX_GREEN_PARCELS - 2; i++) {
            this.placeAnParcelAnywhere(BambooColor.GREEN);
        }
        for (int i = 0; i < Config.NB_MAX_YELLOW_PARCELS - 1; i++) {
            this.placeAnParcelAnywhere(BambooColor.YELLOW);
        }
        assertFalse(this.bot.canDoParcelObjective(card));
    }

    @Test
    public void cantDoParcelObjectivesAllParcelsPlaced() {
        for (int i = 0; i < Config.NB_PLACEABLE_PARCEL; i++) {
            this.placeAnParcelAnywhere();
        }
        assertTrue(bot.cantDoParcelObjectives());
    }

    @Test
    public void cantDoParcelObjectivesNoObjectivesCompletable() {
        this.boardImpossibleToCompleteObjectiveParcel();
        /*while(board.getDeckParcelObjective().canPick() && this.individualBoard.countUnfinishedObjectives() < 5) {
            CardObjectiveParcel pick = board.getDeckParcelObjective().pick();
            pick.verify();
            if(!pick.isCompleted())
                individualBoard.addNewParcelObjective(pick);
        }*/
        while(board.getDeckParcelObjective().canPick()) {
            individualBoard.addNewParcelObjective(board.getDeckParcelObjective().pick());
        }
        assertTrue(bot.cantDoParcelObjectives());
    }

    @Test
    public void objectiveParcelsCompletableAll() {
        List<CardObjectiveParcel> parcels = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            CardObjectiveParcel card = new CardObjectiveParcel(board, 1, Patterns.C, BambooColor.GREEN, BambooColor.GREEN, BambooColor.GREEN);
            parcels.add(card);
            individualBoard.addNewParcelObjective(card);
        }
        assertTrue(parcels.containsAll(bot.getObjectiveParcelsCompletable(log)));
    }

    @Test
    public void objectiveParcelsCompletableEmptyCantDo() {
        for (int i = 0; i < Config.NB_PLACEABLE_PARCEL; i++) {
            this.placeAnParcelAnywhere();
        }
        for (int i = 0; i < 5; i++) {
            CardObjectiveParcel card = new CardObjectiveParcel(board, 1, Patterns.C, BambooColor.GREEN, BambooColor.GREEN, BambooColor.GREEN);
            individualBoard.addNewParcelObjective(card);
        }
        assertTrue(bot.getObjectiveParcelsCompletable(log).isEmpty());
    }

    @Test
    public void objectiveParcelsCompletableEmptyNoOneCanDo() {
        this.boardImpossibleToCompleteObjectiveParcel();
        bot.pickParcelObjective(log);
        for (int i = 0; i < 5; i++) {
            individualBoard.addNewParcelObjective(board.getDeckParcelObjective().pick());
        }
        assertTrue(bot.getObjectiveParcelsCompletable(log).isEmpty());
    }

    @Test
    public void objectiveParcelsCompletableEmptyNoOneCanDoIndBoardFull() {
        this.boardImpossibleToCompleteObjectiveParcel();
        for (int i = 0; i < 5; i++) {
            individualBoard.addNewParcelObjective(board.getDeckParcelObjective().pick());
        }
        assertTrue(bot.getObjectiveParcelsCompletable(log).isEmpty());
    }

    @Test
    public void objectiveParcelsCompletableEmptyPickCardNoCompletable() {
        this.boardImpossibleToCompleteObjectiveParcel();
        assertTrue(bot.getObjectiveParcelsCompletable(log).isEmpty());
    }

    @Test
    public void objectiveParcelsCompletableEmptyPickCardCompletable() {
        this.boardOneObjectiveParcelCanCompleteMissingOne();
        CardObjectiveParcel uncompletable = new CardObjectiveParcel(board, 1, Patterns.LINE, BambooColor.PINK, BambooColor.PINK, BambooColor.PINK);
        uncompletable.verify();
        CardObjectiveParcel completable = new CardObjectiveParcel(board, 3, Patterns.RHOMBUS, BambooColor.YELLOW, BambooColor.YELLOW, BambooColor.GREEN, BambooColor.GREEN);
        Game mock = mock(Game.class);
        when(mock.getNextParcelObjective()).thenReturn(completable);
        when(mock.getBoard()).thenReturn(board);
        FourthBot bot = new FourthBot(mock);
        bot.getIndividualBoard().addNewParcelObjective(uncompletable);
        assertTrue(bot.getObjectiveParcelsCompletable(log).contains(completable));
    }

    @Test
    public void getObjectiveGardenersCompletableEmpty() {
        this.boardImpossibleToCompleteObjectiveParcel();
        CardObjectiveGardener card = new CardObjectiveGardener(board, 1, BambooColor.GREEN, 1, 1);
        card.verify();
        individualBoard.addNewGardenerObjective(card);
        assertTrue(bot.getObjectiveGardenersCompletable().isEmpty());
    }

    @Test
    public void getObjectiveGardenersCompletableCompletable() {
        this.boardImpossibleToCompleteObjectiveParcel();
        CardObjectiveGardener card = new CardObjectiveGardener(board, 1, BambooColor.GREEN, 2, 1);
        card.verify();
        individualBoard.addNewGardenerObjective(card);
        assertEquals(1, bot.getObjectiveGardenersCompletable().size());
    }

    @Test
    public void getObjectiveGardenersCompletableEmptyUncompletable() {
        this.boardImpossibleToCompleteObjectiveParcel();
        CardObjectiveGardener card = new CardObjectiveGardener(board, 1, BambooColor.GREEN, 2, 1);
        card.verify();
        individualBoard.addNewGardenerObjective(card);
        assertEquals(1, bot.getObjectiveGardenersCompletable().size());
    }


    /*@Test
    public void launchGameTest() {
        // We verify if the firstLaunch boolean variable is at true (because we launch the game)
        //assertTrue(bot.isFirstLaunch()); -> devenue inutile

        // Now we launch the bot
        bot.playTurn(log, null);

        // Now we verify if we pick the good number of objectives into the individualBoard
        assertEquals(individualBoard.countUnfinishedParcelObjectives(), bot.getNumberOfParcelObjectivesAtTheStart());

        bot.playTurn(log, null);
        assertEquals(individualBoard.countUnfinishedGardenerObjectives(), bot.getNumberOfGardenerObjectivesAtTheStart());

        // Now, we verify if the firstLaunch variable is False because we pick the good number of objectives
        // assertFalse(bot.isFirstLaunch()); -> devenue inutile
    }

    @Test
    @DisplayName("L'objectif parcelle qui sera tenté d'être résolu est bien celle où il ya le moins de parcelles manquantes")
    public void testBestObjectiveParcel() {
        CardObjectiveParcel obj = new CardObjectiveParcel(this.bot.getBoard(), 1, Patterns.LINE, BambooColor.PINK, BambooColor.PINK, BambooColor.PINK);
        this.individualBoard.addNewParcelObjective(new CardObjectiveParcel(this.bot.getBoard(), 1, Patterns.LINE, BambooColor.GREEN, BambooColor.GREEN, BambooColor.GREEN));
        this.individualBoard.addNewParcelObjective(obj);
        this.bot.getBoard().addParcel(0, 1, -1, new BambooPlantation(BambooColor.GREEN));
        this.bot.getBoard().addParcel(1, 0, -1, new BambooPlantation(BambooColor.PINK));
        this.bot.getBoard().addParcel(1, 1, -2, new BambooPlantation(BambooColor.PINK));
        this.bot.playTurn(log, null);
        assertEquals(bot.currentParcelObjective, obj);
    }

    @Test
    @DisplayName("L'objectif gardener qui sera tenté d'être résolu est bien celle où il ya le moins de bamboo manquantes")
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
        this.bot.playTurn(log, null);
        assertEquals(bot.currentGardenerObjective, obj);
    }

    @Test
    @DisplayName("Aucune carte objectif est la plus simple")
    public void testNoEasiestObjective() {
        assertNull(this.bot.easiestObjectiveToResolve());
    }

    @Test
    @DisplayName("La carte objectif la plus simple à compléter est celle de la jardinier car aucun objectif parcelle")
    public void testEasiestGardenerObjectiveNoParcel() {
        CardObjectiveGardener cardObjectiveGardener = new CardObjectiveGardener(bot.getBoard(), 1, BambooColor.GREEN, 1, 1);
        this.bot.currentGardenerObjective = cardObjectiveGardener;
        assertEquals(cardObjectiveGardener, this.bot.easiestObjectiveToResolve());
    }

    @Test
    @DisplayName("La carte objectif la plus simple à compléter et celle de la parcelle car aucun objectif jardinier")
    public void testEasiestParcelObjectiveNoGardener() {
        CardObjectiveParcel cardObjectiveParcel = new CardObjectiveParcel(bot.getBoard(), 1, Patterns.C, BambooColor.GREEN, BambooColor.GREEN, BambooColor.GREEN);
        this.bot.currentParcelObjective = cardObjectiveParcel;
        assertEquals(cardObjectiveParcel, this.bot.easiestObjectiveToResolve());
    }

    @Test
    @DisplayName("La carte objectif la plus simple à compléter est celle de la jardinier")
    public void testEasiestGardenerObjective() {
        CardObjectiveGardener cardObjectiveGardener = new CardObjectiveGardener(bot.getBoard(), 1, BambooColor.GREEN, 1, 1);
        this.bot.currentGardenerObjective = cardObjectiveGardener;
        this.bot.currentParcelObjective = new CardObjectiveParcel(bot.getBoard(), 2, Patterns.C, BambooColor.GREEN, BambooColor.GREEN, BambooColor.GREEN);
        assertEquals(cardObjectiveGardener, this.bot.easiestObjectiveToResolve());
    }

    @Test
    @DisplayName("La carte objectif la plus simple à compléter et celle de la parcelle")
    public void testEasiestParcelObjective() {
        CardObjectiveParcel cardObjectiveParcel = new CardObjectiveParcel(bot.getBoard(), 1, Patterns.C, BambooColor.GREEN, BambooColor.GREEN, BambooColor.GREEN);
        this.bot.currentParcelObjective = cardObjectiveParcel;
        this.bot.currentGardenerObjective = new CardObjectiveGardener(bot.getBoard(), 2, BambooColor.GREEN, 1, 1);
        assertEquals(cardObjectiveParcel, this.bot.easiestObjectiveToResolve());
    }

    @Test
    @DisplayName("Aucune parcelle posé, il va donc poser une parcelle quelque part plutôt qu'essaie de résoudre l'objectif parcelle")
    public void testTryToResolveParcelObjectiveOnlyPond() {
        this.bot.currentParcelObjective = new CardObjectiveParcel(bot.getBoard(), 1, Patterns.C, BambooColor.GREEN, BambooColor.GREEN, BambooColor.GREEN);
        this.bot.tryToResolveParcelObjective(this.log);
        assertEquals(2, this.bot.getBoard().getParcelCount());
    }

    @Test
    @DisplayName("Aucune objectif parcelle, il va donc poser une parcelle quelque part plutôt qu'essaie de résoudre l'objectif parcelle")
    public void testTryToResolveParcelObjectiveNoParcelObjective() {
        this.bot.getBoard().addParcel(1, 0, -1, new BambooPlantation(BambooColor.GREEN));
        this.bot.tryToResolveParcelObjective(this.log);
        assertEquals(3, this.bot.getBoard().getParcelCount());
    }

    @Test
    @DisplayName("Place une parcelle à la position manquante pour compléter l'objectif")
    public void testPlaceParcelToCompleteParcelObjective() {
        this.bot.getBoard().addParcel(1, -1, 0, new BambooPlantation(BambooColor.GREEN));
        this.bot.currentParcelObjective = new CardObjectiveParcel(bot.getBoard(), 2, Patterns.C, BambooColor.GREEN, BambooColor.GREEN, BambooColor.GREEN);
        this.bot.currentParcelObjective.verify();
        this.bot.tryToResolveParcelObjective(this.log);
        assertTrue(this.bot.getBoard().containTile(new Position(0, -1, 1)));
    }

    //Il manque l'ago
    @Test
    @DisplayName("Place une parcelle pour pouvoir placer une parcelle manquante pour compléter l'objectif")
    public void testPlaceParcelToPlaceParcelCompleteParcelObjective() {
        this.bot.getBoard().addParcel(1, -1, 0, new BambooPlantation(BambooColor.GREEN));
        this.bot.getBoard().addParcel(0, -1, 1, new BambooPlantation(BambooColor.GREEN));
        this.bot.currentParcelObjective = new CardObjectiveParcel(bot.getBoard(), 2, Patterns.C, BambooColor.GREEN, BambooColor.GREEN, BambooColor.GREEN);
        this.bot.currentParcelObjective.verify();
        this.bot.tryToResolveParcelObjective(this.log);
        assertTrue(this.bot.getBoard().containTile(new Position(1, -2, 1)));
    }

    @Test
    @DisplayName("Place une parcelle quelque part car ne peut pas compléter l'objective jardinier")
    public void testResolveGardenerPlaceParcel() {
        this.bot.getBoard().addParcel(1, -1, 0, new BambooPlantation(BambooColor.PINK));
        this.bot.currentGardenerObjective = new CardObjectiveGardener(bot.getBoard(), 2, BambooColor.GREEN, 1, 1);
        this.bot.tryToResolveGardenerObjective(this.log);
        assertEquals(3, this.bot.getBoard().getParcelCount());
    }

    @Test
    @DisplayName("Déplace le jardinier sur la meilleur parcelle pour compléter l'objectif jardinier")
    public void testResolveGardenerMoveGardenerToBestParcel() {
        this.bot.getBoard().addParcel(1, -1, 0, new BambooPlantation(BambooColor.PINK));
        this.bot.getBoard().addParcel(1, 0, -1, new BambooPlantation(BambooColor.GREEN));
        this.bot.currentGardenerObjective = new CardObjectiveGardener(bot.getBoard(), 2, BambooColor.GREEN, 2, 1);
        this.bot.tryToResolveGardenerObjective(this.log);
        assertEquals(new Position(1, 0, -1), this.bot.getBoard().getGardener().getPosition());
    }

    @Test
    @DisplayName("L'objectif jardinier est complété et une autre carte a été tiré")
    public void testCheckObjectiveGardener() {
        this.bot.currentGardenerObjective = new CardObjectiveGardener(bot.getBoard(), 2, BambooColor.GREEN, 1, 1);
        this.bot.getBoard().addParcel(1, 0, -1, new BambooPlantation(BambooColor.GREEN));
        this.bot.playTurn(this.log, null);
        assertEquals(1, bot.numberOfResolveObjective);
        assertEquals(1, this.bot.getIndividualBoard().countCompletedObjectives());
        assertTrue(this.bot.currentGardenerObjective.isCompleted());
    }

    @Test
    @DisplayName("L'objectif parcelle est complété et une autre carte a été tiré")
    public void testCheckObjectiveParcelCompleted() {
        this.individualBoard.addNewParcelObjective(new CardObjectiveParcel(bot.getBoard(), 2, Patterns.C, BambooColor.GREEN, BambooColor.GREEN, BambooColor.GREEN));
        this.individualBoard.addNewParcelObjective(new CardObjectiveParcel(bot.getBoard(), 2, Patterns.LINE, BambooColor.GREEN, BambooColor.GREEN, BambooColor.GREEN));
        this.individualBoard.addNewGardenerObjective(new CardObjectiveGardener(bot.getBoard(), 3, BambooColor.GREEN, 1, 1));
        this.individualBoard.addNewGardenerObjective(new CardObjectiveGardener(bot.getBoard(), 3, BambooColor.GREEN, 1, 1));
        this.bot.getBoard().addParcel(1, -1, 0, new BambooPlantation(BambooColor.GREEN));
        this.bot.getBoard().addParcel(1, 0, -1, new BambooPlantation(BambooColor.GREEN));
        this.bot.getBoard().addParcel(2, -1, -1, new BambooPlantation(BambooColor.GREEN));
        this.bot.getBoard().addParcel(2, 0, -2, new BambooPlantation(BambooColor.GREEN));
        this.bot.playTurn(this.log, null);
        assertEquals(1, bot.numberOfResolveObjective);
        assertEquals(1, this.bot.getIndividualBoard().countCompletedObjectives());
        assertTrue(this.bot.currentParcelObjective.isCompleted());
    }

    @Test
    @DisplayName("L'objectif jardinier est complété et une autre carte a été tiré")
    public void testCheckObjectiveGardenerCompleted() {
        this.individualBoard.addNewParcelObjective(new CardObjectiveParcel(bot.getBoard(), 2, Patterns.C, BambooColor.GREEN, BambooColor.GREEN, BambooColor.GREEN));
        this.individualBoard.addNewParcelObjective(new CardObjectiveParcel(bot.getBoard(), 2, Patterns.LINE, BambooColor.GREEN, BambooColor.GREEN, BambooColor.GREEN));
        this.individualBoard.addNewGardenerObjective(new CardObjectiveGardener(bot.getBoard(), 1, BambooColor.GREEN, 1, 1));
        this.individualBoard.addNewGardenerObjective(new CardObjectiveGardener(bot.getBoard(), 1, BambooColor.GREEN, 2, 1));
        BambooPlantation plantation = new BambooPlantation(BambooColor.GREEN);
        this.bot.getBoard().addParcel(1, -1, 0, plantation);
        plantation.growBamboo();
        this.bot.playTurn(this.log, null);
        assertEquals(1, bot.numberOfResolveObjective);
        assertEquals(1, this.bot.getIndividualBoard().countCompletedObjectives());
        assertTrue(this.bot.currentGardenerObjective.isCompleted());
    }

    @Test
    @DisplayName("Le jardinier est déplacé sur une ligne droite")
    public void testGardenerMoveLine() {
        Position goal = new Position(3, 0, -3);
        board.addBambooPlantation(new Position(1, 0, -1));
        board.addBambooPlantation(new Position(1, -1, 0));
        board.addBambooPlantation(new Position(2, -1, -1));
        board.addBambooPlantation(new Position(2, 0, -2));
        board.addBambooPlantation(new Position(3, -1, -2));
        board.addBambooPlantation(goal);
        assertTrue(this.bot.moveGardener(goal, log));
        assertEquals(goal, board.getGardener().getPosition());
    }

    @Test
    @DisplayName("Le jardinier doit être déplacé 3 fois pour atteindre l'objectif")
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

    @Test
    @DisplayName("Bouge le panda pour compléter l'objectif")
    public void movePanda() {
        BambooPlantation plantation = new BambooPlantation(BambooColor.GREEN);
        Position position = new Position(0, 1, -1);
        this.board.addParcel(position, plantation);
        plantation.growBamboo();
        plantation.growBamboo();
        plantation.growBamboo();
        plantation.growBamboo();
        this.bot.currentGardenerObjective = new CardObjectiveGardener(board, 1, BambooColor.GREEN, 3, 1);
        this.bot.tryToResolveGardenerObjective(log);
        assertEquals(position, this.board.getPanda().getPosition());
    }

    @Test
    @DisplayName("Ne bouge pas le panda si la parcelle contient un enclos, et pose une parcelle")
    public void dontMovePanda() {
        BambooPlantation plantation = new BambooPlantation(BambooColor.GREEN, Layout.ENCLOSURE);
        this.board.addParcel(new Position(0, 1, -1), plantation);
        plantation.growBamboo();
        plantation.growBamboo();
        plantation.growBamboo();
        plantation.growBamboo();
        this.bot.currentGardenerObjective = new CardObjectiveGardener(board, 1, BambooColor.GREEN, 3, 1);
        this.bot.tryToResolveGardenerObjective(log);
        assertEquals(Config.POND_POSITION, this.board.getPanda().getPosition());
        assertEquals(3, this.bot.getBoard().getParcelCount());
        assertEquals(2, this.bot.getBoard().getParcelCount(BambooColor.GREEN));
    }

    @Test
    @DisplayName("Prend des irrigations quand nécessaire")
    public void takeIrrigations() {
        BambooPlantation plantation = new BambooPlantation(BambooColor.GREEN);
        this.board.addParcel(new Position(0, 1, -1), plantation);
        BambooPlantation plantation2 = new BambooPlantation(BambooColor.GREEN);
        this.board.addParcel(new Position(1, 0, -1), plantation2);
        BambooPlantation plantation3 = new BambooPlantation(BambooColor.YELLOW);
        this.board.addParcel(new Position(1, 1, -2), plantation3);
        BoardDrawer drawer = new BoardDrawer(this.board);
        drawer.print();
        this.bot.currentGardenerObjective = new CardObjectiveGardener(board, 1, BambooColor.YELLOW, 3, 1);
        this.bot.tryToResolveGardenerObjective(log);
        assertEquals(2, bot.irrigationNeeded);
        this.bot.playTurn(log, null);
        assertEquals(1, bot.irrigationNeeded);
        assertEquals(1, bot.getIndividualBoard().getNumberOfIrrigations());
    }

    @Test
    public void gameTest() {

        // We launch the bot one time
        bot.playTurn(log);
        // Now we verify if we pick the good number of objectives into the individualBoard
        assertEquals(individualBoard.countUnfinishedParcelObjectives(), bot.getNumberOfParcelObjectivesAtTheStart());
        assertEquals(individualBoard.countUnfinishedGardenerObjectives(), bot.getNumberOfGardenerObjectivesAtTheStart());

        // Now we relauch a turn
        bot.playTurn(log);
        for (int i = 0; i < 20; i++) {
            bot.playTurn(log);
        }

    }*/

    public void placeAnParcelAnywhere(BambooColor color) {
        if (board.getParcelCount() < Config.MAX_PARCEL_ON_BOARD) {
            // We get where we can put an parcel
            ArrayList<Position> validPositions = new ArrayList<>(board.getValidPlaces());
            // Now, we have an ArrayList of the potentials places where we can add a parcel

            // We choose a random parcel in the potential list
            int position = Config.RANDOM.nextInt(validPositions.size());

            // We finally add to the board the new parcel
            Position pos = validPositions.get(position);
            //BambooColor color = this.getRandomPlaceableColor();
            if (color != null) {
                board.addParcel(pos, new BambooPlantation(color));
                log.addAction(BotActionType.PLACE_PARCEL, pos.toString());
            }
        }
    }

    public void placeAnParcelAnywhere() {
        if (board.getParcelCount() < Config.MAX_PARCEL_ON_BOARD) {
            // We get where we can put an parcel
            ArrayList<Position> validPositions = new ArrayList<>(board.getValidPlaces());
            // Now, we have an ArrayList of the potentials places where we can add a parcel

            // We choose a random parcel in the potential list
            int position = Config.RANDOM.nextInt(validPositions.size());

            // We finally add to the board the new parcel
            Position pos = validPositions.get(position);
            BambooColor color = this.getRandomPlaceableColor();
            if (color != null) {
                board.addParcel(pos, new BambooPlantation(color));
                log.addAction(BotActionType.PLACE_PARCEL, pos.toString());
            }
        }
    }

    BambooColor getRandomPlaceableColor() {
        List<BambooColor> colors = new ArrayList<>();
        for(BambooColor color : BambooColor.values()) {
            if(this.board.getParcelLeftToPlace(color) > 0)
                colors.add(color);
        }
        Collections.shuffle(colors);
        return colors.isEmpty() ? null : colors.get(0);
    }

    @Test
    public void noParcelObjectiveCompleted() {
        this.boardImpossibleToCompleteObjectiveParcel();
        while (this.board.getDeckParcelObjective().canPick()) {
            CardObjectiveParcel pick = this.board.getDeckParcelObjective().pick();
            assertFalse(pick.verify());
        }
    }

    public void boardImpossibleToCompleteObjectiveParcel() {
        int x = 1;
        int y = -1;
        int z = 0;
        boolean up = false;
        List<BambooColor> colors = new ArrayList<>();
        for (int i = 0; i < Config.NB_MAX_PINK_PARCELS; i++) {
            colors.add(BambooColor.GREEN);
            colors.add(BambooColor.YELLOW);
            colors.add(BambooColor.PINK);
        }
        for (int i = 0; i < Config.NB_MAX_YELLOW_PARCELS - Config.NB_MAX_PINK_PARCELS; i++) {
            colors.add(BambooColor.GREEN);
            colors.add(BambooColor.YELLOW);
        }
        for (int i = 0; i < Config.NB_MAX_GREEN_PARCELS - Config.NB_MAX_YELLOW_PARCELS; i++) {
            colors.add(BambooColor.GREEN);
        }
        for (int i = 0; i < colors.size() - 3; i++) {
            board.addParcel(x, y, z, new BambooPlantation(colors.get(i)));
            if(up) {
                x++;
                y--;
                up = false;
            } else {
                y++;
                z--;
                up = true;
            }
        }
        board.addParcel(x, y, z, new BambooPlantation(colors.get(colors.size() - 2)));
        board.addParcel(x, ++y, --z, new BambooPlantation(colors.get(colors.size() - 3)));
        board.addParcel(++x, --y, z, new BambooPlantation(colors.get(colors.size() - 1)));
        /*BoardDrawer boardDrawer = new BoardDrawer(board);
        boardDrawer.print();*/
    }

    @Test
    public void oneParcelObjectiveCompleted() {
        this.boardOneObjectiveParcelCanComplete();
        CardObjectiveParcel completable = new CardObjectiveParcel(board, 3, Patterns.RHOMBUS, BambooColor.YELLOW, BambooColor.YELLOW, BambooColor.GREEN, BambooColor.GREEN);
        while (this.board.getDeckParcelObjective().canPick()) {
            CardObjectiveParcel pick = this.board.getDeckParcelObjective().pick();
            if(pick.equals(completable))
                assertTrue(pick.verify());
            else
                assertFalse(pick.verify());
        }
    }

    public void boardOneObjectiveParcelCanComplete() {
        int x = -1;
        int y = 1;
        int z = 0;
        boolean up = false;
        List<BambooColor> colors = new ArrayList<>();
        for (int i = 0; i < Config.NB_MAX_PINK_PARCELS; i++) {
            colors.add(BambooColor.GREEN);
            colors.add(BambooColor.YELLOW);
            colors.add(BambooColor.PINK);
        }
        for (int i = 0; i < Config.NB_MAX_YELLOW_PARCELS - Config.NB_MAX_PINK_PARCELS; i++) {
            colors.add(BambooColor.GREEN);
            colors.add(BambooColor.YELLOW);
        }
        for (int i = 0; i < Config.NB_MAX_GREEN_PARCELS - Config.NB_MAX_YELLOW_PARCELS; i++) {
            colors.add(BambooColor.GREEN);
        }
        for (int i = 0; i < colors.size(); i++) {
            if(y == 11) {
                if(z == -10)
                    board.addParcel(x, y, z, new BambooPlantation(colors.get(i + 1)));
                else if(z == -11)
                    board.addParcel(x, y, z, new BambooPlantation(colors.get(i - 1)));
            } else
                board.addParcel(x, y, z, new BambooPlantation(colors.get(i)));
            if(up) {
                x--;
                y++;
                up = false;
            } else {
                x++;
                z--;
                up = true;
            }
        }
        BoardDrawer boardDrawer = new BoardDrawer(board);
        boardDrawer.print();
    }

    @Test
    public void oneParcelObjectiveCompletedOneMissing() {
        this.boardOneObjectiveParcelCanCompleteMissingOne();
        CardObjectiveParcel completable = new CardObjectiveParcel(board, 3, Patterns.RHOMBUS, BambooColor.YELLOW, BambooColor.YELLOW, BambooColor.GREEN, BambooColor.GREEN);
        while (this.board.getDeckParcelObjective().canPick()) {
            CardObjectiveParcel pick = this.board.getDeckParcelObjective().pick();
            if(pick.equals(completable))
                assertTrue(pick.verify());
            else
                assertFalse(pick.verify());
        }
        assertEquals(1, board.getParcelLeftToPlace(BambooColor.GREEN));
    }

    public void boardOneObjectiveParcelCanCompleteMissingOne() {
        int x = -1;
        int y = 1;
        int z = 0;
        boolean up = false;
        List<BambooColor> colors = new ArrayList<>();
        for (int i = 0; i < Config.NB_MAX_PINK_PARCELS; i++) {
            colors.add(BambooColor.GREEN);
            colors.add(BambooColor.YELLOW);
            colors.add(BambooColor.PINK);
        }
        for (int i = 0; i < Config.NB_MAX_YELLOW_PARCELS - Config.NB_MAX_PINK_PARCELS; i++) {
            colors.add(BambooColor.GREEN);
            colors.add(BambooColor.YELLOW);
        }
        for (int i = 0; i < Config.NB_MAX_GREEN_PARCELS - Config.NB_MAX_YELLOW_PARCELS - 1; i++) {
            colors.add(BambooColor.GREEN);
        }
        for (int i = 0; i < colors.size(); i++) {
            if(y == 11) {
                if(z == -10)
                    board.addParcel(x, y, z, new BambooPlantation(colors.get(i + 1)));
                else if(z == -11)
                    board.addParcel(x, y, z, new BambooPlantation(colors.get(i - 1)));
            } else
                board.addParcel(x, y, z, new BambooPlantation(colors.get(i)));
            if(up) {
                x--;
                y++;
                up = false;
            } else {
                x++;
                z--;
                up = true;
            }
        }
        BoardDrawer boardDrawer = new BoardDrawer(board);
        boardDrawer.print();
    }

    @Test
    public void greenGardenerObjectiveUnCompletable() {
        this.boardGreenGardenerObjectiveUnCompletable();
        while (this.board.getDeckGardenerObjective().canPick()) {
            CardObjectiveGardener pick = this.board.getDeckGardenerObjective().pick();
            pick.verify();
            if(pick.getColor() == BambooColor.GREEN)
                assertFalse(bot.canDoObjectiveGardener(pick));
            else if(pick.getColor() == BambooColor.PINK)
                assertTrue(bot.canDoObjectiveGardener(pick));
        }
    }

    /**
     * Génère un board ou les obj jardinier:
     * - rose: tous faisable
     * - yellow: certain faisable
     * - vert: aucun faisable
     */
    public void boardGreenGardenerObjectiveUnCompletable() {
        int x = 1;
        int y = -1;
        int z = 0;
        boolean up = false;
        List<BambooColor> colors = new ArrayList<>();
        for (int i = 0; i < Config.NB_MAX_PINK_PARCELS; i++) {
            colors.add(BambooColor.PINK);
        }
        for (int i = 0; i < Config.NB_MAX_YELLOW_PARCELS; i++) {
            colors.add(BambooColor.YELLOW);
        }
        for (int i = 0; i < Config.NB_MAX_GREEN_PARCELS; i++) {
            colors.add(BambooColor.GREEN);
        }
        for (int i = 0; i < colors.size(); i++) {
            board.addParcel(x, y, z, new BambooPlantation(colors.get(i)));
            for (Side value : Side.values()) {
                if(board.canPickIrrigation() && board.placeIrrigation(new Position(x, y, z), value))
                    board.pickIrrigation();

            }
            if(up) {
                x++;
                y--;
                up = false;
            } else {
                y++;
                z--;
                up = true;
            }
        }
        BoardDrawer boardDrawer = new BoardDrawer(board);
        boardDrawer.print();
    }


}

