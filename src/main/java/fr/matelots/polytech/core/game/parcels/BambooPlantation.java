package fr.matelots.polytech.core.game.parcels;

import fr.matelots.polytech.core.game.graphics.ConsoleColor;
import fr.matelots.polytech.core.game.graphics.HexagonePosition;
import fr.matelots.polytech.core.game.graphics.ParcelDrawer;

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

    @Override
    public void draw(ParcelDrawer drawer) {
        drawer.set(HexagonePosition.center,
                ConsoleColor.getFromBambooColor(getBambooColor()),
                String.valueOf(getBambooSize()).charAt(0));
    }
}
