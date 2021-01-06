package fr.matelots.polytech.core.game;

import fr.matelots.polytech.engine.util.Position;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class Config {

    /**Nombre de parcel qu'on peut poser*/
    public static final int NB_PLACEABLE_PARCEL = 27;

    public static final List<Position> CUBE_DIRECTIONS = Arrays.asList(
                                                                new Position(1, -1, 0),
                                                                new Position(0, -1, 1),
                                                                new Position(-1, 0, 1),
                                                                new Position(-1, 1, 0),
                                                                new Position(0, 1, -1),
                                                                new Position(1, 0, -1)
                                                        );

    /**Taille du paquet de carte objectifs*/
    public static final int DECK_OBJECTIVE_SIZE = 15;

    /**Taille du paquet de carte parcelles*/
    public static final int DECK_PARCEL_SIZE = 27;

    /**Nombre de parcelles pouvant être sur le plateau*/
    public static final int MAX_PARCEL_ON_BOARD = 28;

    /**Position de l'étang*/
    public static final Position POND_POSITION = new Position(0, 0, 0);

    /** Nombre d'objectif à compléter pour atteindre le dernier tour*/
    public static final int OBJ_TO_COMPLETE_FOR_LAST_TURN = 9;

    public static final int MIN_SIZE_BAMBOO = 0;
    public static final int MAX_SIZE_BAMBOO = 4;

    public static final int NB_MAX_GREEN_PARCELS = 11;
    public static final int NB_MAX_PINK_PARCELS = 7;
    public static final int NB_MAX_YELLOW_PARCELS = 9;

    private static final int NB_OBJ_LAST_TURN_2_PLAYERS = 9;
    private static final int NB_OBJ_LAST_TURN_3_PLAYERS = 8;
    private static final int NB_OBJ_LAST_TURN_4_PLAYERS = 7;

    public static final int TOTAL_NUMBER_OF_ACTIONS = 2;
    public static final int MAX_NUMBER_OF_OBJECTIVES_CARD_IN_HAND = 5;

    public static final int EMPEROR_VALUE = 2;

    public static final int NB_PARCEL_PICKED = 3;

    public static final int NB_IRRIGATION = 20;

    public static final Random RANDOM = new Random();


    private Config () throws InstantiationException {
        throw new InstantiationException("This is a static class");
    }

    /**
     * Returns the number of objectives a player must have completed in order to start the last round.
     * @param nbPlayer the number of player in game
     * @return the number to reach or -1 if the number of player is invalid
     */
    public static int getNbObjectivesToCompleteForLastTurn (int nbPlayer) {
        switch (nbPlayer) {
            case 2:
                return NB_OBJ_LAST_TURN_2_PLAYERS;
            case 3:
                return NB_OBJ_LAST_TURN_3_PLAYERS;
            case 4:
                return NB_OBJ_LAST_TURN_4_PLAYERS;
            default:
                return -1;
        }
    }
}
