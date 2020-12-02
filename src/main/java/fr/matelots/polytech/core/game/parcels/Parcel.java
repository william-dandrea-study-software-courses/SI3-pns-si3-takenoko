package fr.matelots.polytech.core.game.parcels;

import fr.matelots.polytech.core.game.graphics.ParcelDrawer;
import fr.matelots.polytech.core.game.movables.Gardener;
import fr.matelots.polytech.core.game.movables.Panda;
import fr.matelots.polytech.core.game.movables.Pawn;

/**
 * @author Gabriel Cogne
 */
public abstract class Parcel {

    private Gardener gardener;
    private Panda panda;

    // Constructors
    public abstract boolean isPond();

    public abstract int getBambooSize ();

    public abstract void growBamboo ();

    public abstract void destroyUnitOfBamboo ();

    public abstract BambooColor getBambooColor();

    public boolean placeOn (Pawn pawn) {
        if (pawn instanceof Gardener) {
            this.gardener = (Gardener) pawn;
            return getGardener() != null;
        }
        else if (pawn instanceof Panda) {
            this.panda = (Panda) pawn;
            return getPanda() != null;
        }
        return false;
    }

    public void removePawn(Pawn pawn) {
        if (pawn instanceof Gardener) {
            this.gardener = null;
        }
        else if (pawn instanceof Panda) {
            this.panda = null;
        }
    }

    public Gardener getGardener () {
        return gardener;
    }

    public Panda getPanda () {
        return panda;
    }

    public abstract void draw(ParcelDrawer drawer);

    // Methods
    @Override
    public String toString() {
        return super.toString();
    }
}
