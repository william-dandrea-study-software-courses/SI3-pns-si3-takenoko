package fr.matelots.polytech.core.game;

import fr.matelots.polytech.core.game.goalcards.CardObjectiveGardener;
import fr.matelots.polytech.core.game.goalcards.CardObjectivePanda;
import fr.matelots.polytech.core.game.goalcards.CardObjectiveParcel;
import fr.matelots.polytech.core.game.graphics.BoardDrawer;
import fr.matelots.polytech.core.players.Bot;
import fr.matelots.polytech.core.players.bots.FifthBot;
import fr.matelots.polytech.core.players.bots.FourthBot;
import fr.matelots.polytech.core.players.bots.SecondBot;
import fr.matelots.polytech.core.players.bots.ThirdBot;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

/**
 * @author Gabriel Cogne
 * @author Yann Clodong
 */
public class Game {
    private static final Logger ACTIONLOGGER = Logger.getLogger("actionLogger");
    // Attributes
    private final List<Bot> bots;
    private final Board board;
    private final BoardDrawer drawer;
    private boolean lastTurn;

    // Constructors
    public Game () {
        initLogger();
        bots = new ArrayList<>();
        board = new Board();
        drawer = new BoardDrawer(board);
    }

    void initLogger() {
        String loggerName = "board";
        /*Logger boardLogger = Logger.getLogger("loggerName");

        boardLogger.info(loggerName);*/
    }

    private void setDemoBots() {
        bots.add(new SecondBot(this));
        //bots.add(new ThirdBot(this));
        //bots.add(new ThirdBot(this));
        //bots.add(new FourthBot(this));
        //bots.add(new FifthBot(this));
        //bots.add(new FifthBot(this));
        bots.add(new FifthBot(this));
    }

    public void addBot(Bot bot) {
        if(bots.contains(bot)) return; // Eviter les doubles coups ;)
        bots.add(bot);
    }

    // Methods
    public Bot getWinner () {
        Bot winner = null;
        int bestScore = 0;
        for (Bot bot : bots) {
            int score = bot.getIndividualBoard().getPlayerScore();
            if(score > bestScore) {
                winner = bot;
                bestScore = score;
            }
        }

        return winner;
    }

    private List<List<Bot>> getRanks()
    {
        /* Pas utile en fait
        bots.sort(new Comparator<Bot>() {
            @Override
            public int compare(Bot o1, Bot o2) {
                return o1.getIndividualBoard().getPlayerScore() - o2.getIndividualBoard().getPlayerScore();
            }
        });*/

        List<List<Bot>> ranked = new ArrayList<>(); // une liste de (liste de bot ayant le meme score) classé par score

        for(Bot bot : bots) {
            var goodList = ranked.stream()
                    .filter(a -> a.get(0) /* les listes contiennent au moins un element cf suite */ .getIndividualBoard().getPlayerScore() == bot.getIndividualBoard().getPlayerScore())
                    .findAny(); // si il existe déjà une liste correspondant au score de ce bot, on la récupère

            if(goodList.isEmpty()) {
                ArrayList<Bot> listForThisScore = new ArrayList<>(); // si elle n'existe pas on la créée
                listForThisScore.add(bot);
                ranked.add(listForThisScore); // donc les liste etant creee uniquement ici, elles contiennent au moins un element chacune
            } else {
                goodList.get().add(bot);
            }
        }

        // on classe les groupe par score (il devrait deja y etre, mais pour etre sur)
        ranked.sort((o1, o2) -> {
            // les listes contiennent au moins un element
            return o2.get(0).getIndividualBoard().getPlayerScore() - o1.get(0).getIndividualBoard().getPlayerScore();
        });

        return ranked;
    }


    private void drawRanks() {
        List<List<Bot>> ranked = getRanks();

        if(ranked.size() == 0) return;
        StringBuilder result = new StringBuilder();

        // drawing winner
        int winnerScore = ranked.get(0).get(0).getIndividualBoard().getPlayerScore();
        if(ranked.get(0).size() == 1)
            result.append("The winner (score : ").append(winnerScore).append(") is : ");
        else
            result.append("The following bots are winning with equal score (score: ").append(winnerScore).append(") : ");

        for(var bot : ranked.get(0)) {
            result.append(bot).append(", ");
        }
        result.delete(result.length() - 2, result.length());
        ranked.remove(0); // make sure the winner will not be displayed two times
        ACTIONLOGGER.info(result.toString());

        for(var sameScored : ranked) {
            int scoreStep = sameScored.get(0).getIndividualBoard().getPlayerScore();

            StringBuilder res = new StringBuilder();
            res.append("Score ").append(scoreStep).append(" : ");
            if(sameScored.size() != 1)
                res.append("equality between ");

            for(var bot : sameScored) {
                res.append(bot).append(", ");
            }
            res.delete(res.length() - 2, res.length());
            ACTIONLOGGER.info(res.toString());
        }
    }



    public void run () {
        setDemoBots();

        if(bots.size() == 0) {
            //System.out.println("No players !");
            return;
        }

        /*System.out.print("Joueurs: ");
        bots.forEach(System.out::println);
        System.out.println();*/
        drawer.print();

        launchTurnLoop();

        // this is the winner ! ;)
        //var winner = getWinner();
        /*System.out.println("Winner is : " + winner.toString());
        System.out.println("Winner score : " + winner.getIndividualBoard().getPlayerScore());*/

        /*for(Bot bot : bots) {
            if(winner == bot) continue;
            /*System.out.println("Loser is : " + bot.toString());
            System.out.println("Loser score : " + bot.getIndividualBoard().getPlayerScore());*/
        //}

        drawRanks();

    }

    public void launchTurnLoop() {
        while (!lastTurn) {
            bots.forEach(bot -> {
                bot.playTurn();
                if (bot.getIndividualBoard().countCompletedObjectives() >= Config.OBJ_TO_COMPLETE_FOR_LAST_TURN)
                    lastTurn = true;
                /*System.out.println("Completed : " + bot.getIndividualBoard().countCompletedObjectives());
                System.out.println("Player : " + bot.toString());*/
                drawer.print();
                ACTIONLOGGER.info(bot.getTurnMessage());
            });

            if(bots.stream().noneMatch(Bot::canPlay)) { // Si aucun bot ne peut jouer, on coupe la partie.
                //System.out.println("aucun bot ne peux jouer la partie, on l'annule");
                break;
            }
        }
    }

    /**
     * This return the hidden top card of the parcel objective deck
     * @return the hidden top card of the parcel objective deck
     */
    public CardObjectiveParcel getNextParcelObjective () {
        if (board.getDeckParcelObjective().canPick())
            return board.getDeckParcelObjective().pick();
        return null;
    }

    /**
     * This return the hidden top card of the gardener objective deck
     * @return the hidden top card of the gardener objective deck
     */
    public CardObjectiveGardener getNextGardenerObjective() {
        if (board.getDeckGardenerObjective().canPick())
            return board.getDeckGardenerObjective().pick();
        return null;
    }

    /**
     * This return the hidden top card of the panda objective deck
     * @return the hidden top card of the panda objective deck
     */
    public CardObjectivePanda getNextPandaObjective() {
        if (board.getDeckGardenerObjective().canPick())
            return board.getDeckPandaObjective().pick();
        return null;
    }

    public Board getBoard() {
        return board;
    }

}
