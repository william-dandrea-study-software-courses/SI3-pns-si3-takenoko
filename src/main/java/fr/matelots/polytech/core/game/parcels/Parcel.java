package fr.matelots.polytech.core.game.parcels;

import fr.matelots.polytech.core.game.graphics.ParcelDrawer;
import fr.matelots.polytech.core.game.movables.Gardener;

/**
 * @author Gabriel Cogne
 */
public abstract class Parcel {

    private Gardener gardener;

    // Constructors
    public abstract boolean isPond();

    public abstract int getBambooSize ();

    public abstract void growBamboo ();

    public abstract BambooColor getBambooColor();

    public boolean placeOn (Gardener gardener) {
        this.gardener = gardener;
        return this.gardener != null;
    }

    public void removeGardener () {
        this.gardener = null;
    }

    public Gardener getGardener () {
        return gardener;
    }

    public abstract void draw(ParcelDrawer drawer);

    // Methods
    @Override
    public String toString() {
        return super.toString();
    }
}
