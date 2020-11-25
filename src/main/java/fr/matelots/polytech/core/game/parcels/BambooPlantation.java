package fr.matelots.polytech.core.game.parcels;

/**
 * @author Gabriel Cogne
 */
public class BambooPlantation extends Parcel {
    private int bambooSize;
    private BambooColor bambooColor;

    public BambooPlantation (BambooColor color) {
        bambooSize = 0;
        this.bambooColor = color;
    }

    /**
     * Return the current size of the bamboo
     * @return
     */
    @Override
    public int getBambooSize () {
        return bambooSize;
    }

    public BambooColor getBambooColor() {
        return bambooColor;
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
