package fr.matelots.polytech.core.game;

import fr.matelots.polytech.core.IllegalActionRepetitionException;
import fr.matelots.polytech.core.game.goalcards.CardObjectiveEmperor;
import fr.matelots.polytech.core.game.goalcards.CardObjectiveGardener;
import fr.matelots.polytech.core.game.goalcards.CardObjectivePanda;
import fr.matelots.polytech.core.game.goalcards.CardObjectiveParcel;
import fr.matelots.polytech.core.game.graphics.BoardDrawer;
import fr.matelots.polytech.core.players.Bot;
import fr.matelots.polytech.core.players.bots.*;
import fr.matelots.polytech.core.players.bots.logger.BotActionType;
import fr.matelots.polytech.core.players.bots.logger.TurnLog;

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
        bots = new ArrayList<>();
        board = new Board();
        drawer = new BoardDrawer(board);
    }

    private void setDemoBots() {
        //bots.add(new PremierBot(this));
        //bots.add(new PremierBot(this));
        //bots.add(new SecondBotTemporaire(this, "BadBot"));


        //bots.add(new ThirdBot(this));
        //bots.add(new FourthBot(this));
        //bots.add(new FifthBot(this, "GentleBot"));
        //bots.add(new QuintusBot(this));
        bots.add(new QuintusBot(this, "Jojo"));
        bots.add(new RushParcelBot(this, "RushParcel"));
    }

    public void addBot(Bot bot) {
        if(bots.contains(bot)) return; // Eviter les doubles coups ;)
        bots.add(bot);
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

    private List<Bot> separate (List<Bot> winners) {
        List<Bot> trueWinners = new ArrayList<>();

        if (winners != null && !winners.isEmpty()) {
            Bot max = winners.get(0);
            int score = max.getIndividualBoard().getObjectivesPandaScore();
            trueWinners.add(max);

            for (int i = 1; i < winners.size(); i++) {
                if (winners.get(i).getIndividualBoard().getObjectivesPandaScore() > score) {
                    trueWinners.clear();
                    max = winners.get(i);
                    score = max.getIndividualBoard().getObjectivesPandaScore();
                    trueWinners.add(max);
                }
                else if (winners.get(i).getIndividualBoard().getObjectivesPandaScore() == score) {
                    trueWinners.add(winners.get(i));
                }
            }
        }

        return trueWinners;
    }


    private void drawRanks() {
        List<List<Bot>> ranked = getRanks();

        if(ranked.size() == 0) return;
        StringBuilder result = new StringBuilder();
        result.append("========== RESULTS ==========\n");

        // drawing winner
        int winnerScore = ranked.get(0).get(0).getIndividualBoard().getPlayerScore();
        if(ranked.get(0).size() == 1)
            result.append("The winner (score : ").append(winnerScore).append(") is : ");
        else {
            List<Bot> winners = new ArrayList<>(ranked.get(0));
            int rank = 1;
            while (!winners.isEmpty()) {
                List<Bot> trueWinners = separate(winners);

                if (rank == 1) {
                    if (trueWinners.size() == 1) {
                        result.append("The winner (score : ")
                                .append(winnerScore)
                                .append(", panda : ")
                                .append(trueWinners.get(0).getIndividualBoard().getObjectivesPandaScore())
                                .append(") is : ")
                        .append(trueWinners.get(0));
                    }
                    else {
                        result.append("The following bots are winning with equal score (score: ")
                                .append(winnerScore)
                                .append(", panda : ")
                                .append(trueWinners.get(0).getIndividualBoard().getObjectivesPandaScore())
                                .append(") : ");
                        for (Bot bot : trueWinners)
                            result.append(bot.getName()).append(", ");
                        result.append("\n");
                    }
                }
                else {
                    for (Bot bot : trueWinners) {
                        result.append("Score ")
                                .append(bot.getIndividualBoard().getPlayerScore())
                                .append(" (panda : ")
                                .append(bot.getIndividualBoard().getObjectivesPandaScore())
                                .append(") : ")
                                .append(bot.getName());
                    }
                }

                winners.removeAll(trueWinners);
                rank++;
            }
            ranked.get(0).removeAll(winners);
        }


        for(var bot : ranked.get(0)) {
            result.append(bot.getName()).append(", ");
        }
        result.delete(result.length() - 2, result.length());
        result.append('\n');
        ranked.remove(0); // make sure the winner will not be displayed two times
        for(var sameScored : ranked) {
            int scoreStep = sameScored.get(0).getIndividualBoard().getPlayerScore();

            result.append("Score ").append(scoreStep).append(" : ");
            if(sameScored.size() != 1)
                result.append("equality between ");

            for(var bot : sameScored) {
                result.append(bot.getName()).append(", ");
            }
            result.delete(result.length() - 2, result.length());
            result.append('\n');
        }
        result.deleteCharAt(result.length() - 1);
        ACTIONLOGGER.info(result.toString());

    }



    public void run () {
        setDemoBots();

        if(bots.size() < 2 || bots.size() > 4) {
            //System.out.println("No players !");
            ACTIONLOGGER.info("Pas le bon nombre de joueurs");
            return;
        }

        /*System.out.print("Joueurs: ");
        bots.forEach(System.out::println);
        System.out.println();*/
        drawer.print();

        launchTurnLoop(true);

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
        launchTurnLoop(false);
    }
    public void launchTurnLoop(boolean draw) {
        while (!lastTurn) {
            bots.forEach(bot -> {
                TurnLog log = new TurnLog(bot);
                try {
                    bot.playTurn(log);
                } catch (IllegalActionRepetitionException e) {
                    log.addAction(BotActionType.NONE, "");
                }
                if (bot.getIndividualBoard().countCompletedObjectives() >= Config.getNbObjectivesToCompleteForLastTurn(bots.size())) {
                    if (!lastTurn) {
                        if (draw)
                            ACTIONLOGGER.info(bot.getName() + " is the Emperor owner !!");
                        bot.getIndividualBoard().setEmperor(new CardObjectiveEmperor());
                    }
                    lastTurn = true;
                }


                if(draw) {
                    ACTIONLOGGER.info("========== ACTIONS ==========\n" + log.toString() + "\n");
                    drawer.print();
                }

                //System.out.println(bot.canPlay());
                //System.out.println( bot.toString() + " " + bot.getIndividualBoard().countCompletedObjectives());
            });

            if(bots.stream().noneMatch(Bot::canPlay)) { // Si aucun bot ne peut jouer, on coupe la partie.
                System.out.println("aucun bot ne peux jouer la partie, on l'annule");
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
