package fr.matelots.polytech.core.game.parcels;

/**
 * @author Gabriel Cogne
 */
public abstract class Parcel {
    private final boolean lake;

    // Constructors
    public Parcel()  {
        this.lake = false;
    }

    public abstract boolean isPond();

    public abstract int getBambooSize ();

    public abstract void growBamboo ();

    // Methods
    @Override
    public String toString() {
        return super.toString();
    }
}
