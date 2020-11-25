package fr.matelots.polytech.core.game.parcels;

/**
 * @author Gabriel Cogne
 */
public class BambooPlantation extends Parcel {
    private int bambooSize;

    public BambooPlantation () {
        bambooSize = 0;
    }

    /**
     * Return the current size of the bamboo
     * @return
     */
    @Override
    public int getBambooSize () {
        return bambooSize;
    }

    /**
     * Made the bamboo grow by one unit
     */
    @Override
    public void growBamboo () {
        if (bambooSize < 4)
            bambooSize++;
    }

    @Override
    public boolean isPond() {
        return false;
    }
}
