package fr.matelots.polytech.core.game;

import fr.matelots.polytech.engine.util.Position;

import java.util.Arrays;
import java.util.List;

public class Config {
    public static final int NB_PLACEABLE_PARCEL = 27;

    public static final List<Position> CUBE_DIRECTIONS = Arrays.asList(
                                                                new Position(1, -1, 0),
                                                                new Position(0, -1, 1),
                                                                new Position(-1, 0, 1),
                                                                new Position(-1, 1, 0),
                                                                new Position(0, 1, -1),
                                                                new Position(1, 0, -1)
                                                        );

    private Config () throws InstantiationException {
        throw new InstantiationException("This is a static class");
    }
}
