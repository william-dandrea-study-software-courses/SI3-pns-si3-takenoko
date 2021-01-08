package fr.matelots.polytech;

import fr.matelots.polytech.core.game.Game;
import fr.matelots.polytech.core.players.Bot;
import fr.matelots.polytech.engine.util.LogHandler;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.logging.LogManager;
import java.util.logging.Logger;

/**
 * @author AlexandreArcil
 * @author Gabriel Cogne
 */
public class Takenoko {

    public static void main(String[] args) {
        LogManager.getLogManager().reset();
        Logger rootLogger = LogManager.getLogManager().getLogger("");
        rootLogger.addHandler(new LogHandler());
        long time = System.currentTimeMillis();

        run1000Game(rootLogger, false);
        run1000Game(rootLogger, true);

        long duration = System.currentTimeMillis() - time;
        System.out.println("Temps " + (new SimpleDateFormat("mm:ss:SSS")).format(new Date(duration)));
    }

    private static void run1000Game (Logger log, boolean twiceSameBot) {
        List<Double> percents = new ArrayList<>();
        for (double i = 0; i < 10; i++)
            percents.add(i / 10.);

        final int NB_GAMES = 1000;
        final boolean LOG_DETAIL = false;
        Map<String, Integer> results = new HashMap<>();
        int nbCanceledGame = 0;
        int nbDraw = 0;

        Game game;

        List<List<Bot>> ranksTmp;
        List<Bot> winnerTmp;

        StringBuilder stringBuilder = new StringBuilder("Lancement de ").append(NB_GAMES);
        stringBuilder.append(" partie(s)...");
        if (twiceSameBot) {
            stringBuilder.append(" Le meilleur bot contre lui même.");
        }
        else {
            stringBuilder.append(" Deux bots différents s'affrontent.");
        }

        log.info(stringBuilder.toString());

        for (int nbGame = 0; nbGame < NB_GAMES; nbGame++) {
            if(percents.contains(nbGame / ((double) NB_GAMES)))
                System.out.print("" + nbGame + "-");
            game = new Game();

            game.run(LOG_DETAIL, twiceSameBot);

            game.getBots().forEach(bot -> {
                if (!results.containsKey(bot.getName())) {
                    results.put(bot.getName(), 0);
                }
            });

            if (game.isCanceledGame()) {
                nbCanceledGame++;
            }

            ranksTmp = game.getRanks();
            if (!ranksTmp.isEmpty()) {
                winnerTmp = game.separate(ranksTmp.get(0));

                if (winnerTmp.size() > 1) {
                    nbDraw++;
                }

                winnerTmp.forEach(bot -> {
                    if (results.containsKey(bot.getName())) {
                        results.put(bot.getName(), results.get(bot.getName()) + 1);
                    }
                    else {
                        results.put(bot.getName(), 1);
                    }
                });
            }
        }

        System.out.println("" + NB_GAMES + "!");

        StringBuilder builder = new StringBuilder("Résultats :\n");
        results.keySet().forEach(bot ->
                builder.append(bot).append(" : ").append(results.get(bot))
                        .append(" (").append(results.get(bot) * 100. / NB_GAMES)
                        .append("%) victoire(s).\n")
        );
        builder.append("Dont : ");
        builder.append(nbCanceledGame).append(" partie(s) annulée(s) et ");
        builder.append(nbDraw).append(" égalité(s).\n");

        log.info(builder.toString());
    }

}
