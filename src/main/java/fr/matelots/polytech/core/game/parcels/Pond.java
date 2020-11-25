package fr.matelots.polytech.core.game.parcels;

/**
 * @author Gabriel Cogne
 */
public class Pond extends Parcel {

    /**
     * In this context, the bamboo can't grow so the size is always 0
     * @return 0
     */
    @Override
    public int getBambooSize () {
        return 0;
    }

    /**
     * In this context, the bamboo can't grow
     */
    @Override
    public void growBamboo () {}

    @Override
    public boolean isPond() {
        return true;
    }
}
