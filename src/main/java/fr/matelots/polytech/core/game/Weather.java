package fr.matelots.polytech.core.game;

import java.security.SecureRandom;

/**
 * This enum represent the different weather
 * @author williamdandrea
 */

public enum Weather {

    SUN() {

    },
    RAIN() {

    },
    WIND() {

    },
    THUNDERSTORM() {

    },
    CLOUD() {

    },
    INTERROGATION() {

    };


    public Weather getRandomWither() {
        int x = Config.RANDOM.nextInt(Weather.class.getEnumConstants().length);
        return Weather.class.getEnumConstants()[x];

    }
}
