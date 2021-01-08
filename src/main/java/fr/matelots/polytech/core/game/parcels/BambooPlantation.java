package fr.matelots.polytech.core.game.parcels;

import fr.matelots.polytech.core.game.Config;
import fr.matelots.polytech.core.game.graphics.ConsoleColor;
import fr.matelots.polytech.core.game.graphics.HexagonePosition;
import fr.matelots.polytech.core.game.graphics.ParcelDrawer;

import java.util.Objects;

/**
 * @author Gabriel Cogne, Alexandre Arcil
 */
public class BambooPlantation extends Parcel {

    private int bambooSize;
    private final BambooColor bambooColor;

    public BambooPlantation (BambooColor color) {
        this(color, null);
    }

    public BambooPlantation (BambooColor color, Layout layout) {
        setLayout(layout);
        this.bambooSize = layout == Layout.BASIN ? 1 : 0;
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

    @Override
    public void growBamboo () {
        if (!isIrrigate())
            return; // The bamboo can't grow without irrigation

        if (bambooSize < Config.MAX_SIZE_BAMBOO) {
            bambooSize++;
            if(getLayout() == Layout.FERTILIZER && bambooSize < Config.MAX_SIZE_BAMBOO)
                bambooSize++;
        }
    }

    @Override
    public boolean destroyUnitOfBamboo() {
        if(this.getLayout() != Layout.ENCLOSURE) {
            if (bambooSize > Config.MIN_SIZE_BAMBOO) {
                bambooSize--;
                return true;
            }
        }
        return false;
    }

    @Override
    public void setIrrigate(Side side) {
        //un bamboo est automatiquement placé quand la parcelle s'irrigue
        if(!this.isIrrigate()) {
            super.setIrrigate(side);
            this.growBamboo();
        } else
            super.setIrrigate(side);
    }

    @Override
    public boolean isIrrigate() {
        return this.getLayout() == Layout.BASIN || super.isIrrigate();
    }

    @Override
    public boolean isPond() {
        return false;
    }

    @Override
    public void draw(ParcelDrawer drawer) {
        super.draw(drawer);
        drawer.set(HexagonePosition.CENTER,
                ConsoleColor.getFromBambooColor(getBambooColor()),
                String.valueOf(getBambooSize()).charAt(0));

        drawer.setLayout(getLayout());
    }

    @Override
    public String toString() {
        return "BambooPlantation{" +
                "bambooSize=" + bambooSize +
                ", bambooColor=" + bambooColor +
                ", layout=" + layout +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BambooPlantation that = (BambooPlantation) o;
        return bambooSize == that.bambooSize && bambooColor == that.bambooColor && getLayout() == that.getLayout();
    }

    @Override
    public int hashCode() {
        return Objects.hash(bambooSize, bambooColor, getLayout());
    }
}
