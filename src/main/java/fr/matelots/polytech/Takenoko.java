package fr.matelots.polytech;

import fr.matelots.polytech.core.game.Game;

import java.util.HashMap;
import java.util.Map;

/**
 * @author AlexandreArcil
 */
public class Takenoko {

    Map<String, Integer> winnersAndLosers = new HashMap<>();

    public static void main(String[] args) {
        Game game = new Game();
        game.runHundredParties();

    }

}
