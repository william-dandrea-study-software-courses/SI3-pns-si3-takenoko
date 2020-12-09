package fr.matelots.polytech.core.game.parcels;

import fr.matelots.polytech.core.game.Config;
import fr.matelots.polytech.core.game.graphics.ConsoleColor;
import fr.matelots.polytech.core.game.graphics.HexagonePosition;
import fr.matelots.polytech.core.game.graphics.ParcelDrawer;

/**
 * @author Gabriel Cogne, Alexandre Arcil
 */
public class BambooPlantation extends Parcel {

    private int bambooSize;
    private final BambooColor bambooColor;

    public BambooPlantation (BambooColor color) {
        bambooSize = 0;
        this.bambooColor = color;
    }

    /**
     * @return the current size of the bamboo
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
        if (!isIrrigate())
            return; // The bamboo can't grow without irrigation

        if (bambooSize < Config.MAX_SIZE_BAMBOO)
            bambooSize++;
    }

    @Override
    public void destroyUnitOfBamboo() {
        if (bambooSize > Config.MIN_SIZE_BAMBOO)
            bambooSize--;
    }

    @Override
    public void setIrrigate(Side side) {
        //un bamboo est automatiquement placé quand la parcelle s'irrigue
        if(!this.isIrrigate())
            this.growBamboo();
        super.setIrrigate(side);
    }

    @Override
    public boolean isPond() {
        return false;
    }

    @Override
    public void draw(ParcelDrawer drawer) {
        drawer.set(HexagonePosition.CENTER,
                ConsoleColor.getFromBambooColor(getBambooColor()),
                String.valueOf(getBambooSize()).charAt(0));
    }
}
