package fr.matelots.polytech.core.game.parcels;

/**
 * @author Gabriel Cogne
 */
public class Parcel {
    private final boolean lake;

    // Constructors
    public Parcel()  {
        this.lake = false;
    }
    public Parcel(boolean isLake)  {
        this.lake = isLake;
    }

    public boolean isLake() {
        return lake;
    }

    // Methods
    @Override
    public String toString() {
        return super.toString();
    }
}
