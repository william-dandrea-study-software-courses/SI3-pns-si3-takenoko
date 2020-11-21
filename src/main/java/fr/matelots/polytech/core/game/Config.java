package fr.matelots.polytech.core.game;

public class Config {
    public static final int NB_PLACEABLE_PARCEL = 27;

    private Config () throws InstantiationException {
        throw new InstantiationException("This is a static class");
    }
}
