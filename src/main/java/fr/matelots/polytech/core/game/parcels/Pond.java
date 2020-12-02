package fr.matelots.polytech.core.game.parcels;

import fr.matelots.polytech.core.game.graphics.ConsoleColor;
import fr.matelots.polytech.core.game.graphics.HexagonePosition;
import fr.matelots.polytech.core.game.graphics.ParcelDrawer;

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

    @Override
    public BambooColor getBambooColor() {
        return null;
    }

    @Override
    public void draw(ParcelDrawer drawer) {
        drawer.set(HexagonePosition.center, ConsoleColor.none, 'B');
    }
}
