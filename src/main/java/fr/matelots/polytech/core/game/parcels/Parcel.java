package fr.matelots.polytech.core.game.parcels;

import fr.matelots.polytech.core.game.graphics.ParcelDrawer;
import fr.matelots.polytech.core.game.movables.Gardener;
import fr.matelots.polytech.core.game.movables.Panda;
import fr.matelots.polytech.core.game.movables.Pawn;

import java.util.EnumMap;

/**
 * @author Gabriel Cogne, Alexandre Arcil
 */
public abstract class Parcel {

    private Gardener gardener;
    private Panda panda;
    private final EnumMap<Side, Boolean> irrigate;
    protected Layout layout;

    public Parcel() {
        this.irrigate = new EnumMap<>(Side.class);
        for(Side side : Side.values())
            this.irrigate.put(side, false);
    }

    /**
     * @return true si la parcelle a un aménagement
     */
    public boolean hasLayout() {
        return this.layout != null;
    }

    public Layout getLayout() {
        return layout;
    }

    /**
     * Met un aménagement s'il en a déjà pas, s'il n'est l'étang et s'il n'a pas de bambou
     * @param layout L'aménagement à poser
     * @return true si la parcelle à un aménagement, false sinon
     */
    public boolean setLayout(Layout layout) {
        if (!hasLayout() && !isPond() && this.getBambooSize() == 0) {
            this.layout = layout;
        }
        return hasLayout();
    }

    public abstract boolean isPond();

    public abstract int getBambooSize();

    /**
     * Made the bamboo grow by one unit only if it's irrigate and size stay under the limit. Grow by two unit if
     * it have the layout Fertilizer.
     */
    public abstract void growBamboo();

    /**
     * Destroy a unit of bamboo if there is no enclosure and the size is not under the limit.
     * @return true if the a unit of bambou has been destroyed, false otherwise
     */
    public abstract boolean destroyUnitOfBamboo();

    public abstract BambooColor getBambooColor();

    /**
     * Met le côté <core>ridge</core> comme irrigué.
     */
    public void setIrrigate(Side side) {
        this.irrigate.put(side, true);
    }

    /**
     * Vérifie si la parcelle est irrigué par au moins un côté.
     * @return true si la parcelle est irrigué, false sinon
     */
    public boolean isIrrigate() {
        return this.irrigate.values().stream().anyMatch(bool -> bool);
    }

    /**
     * @param side Le côté
     * @return true si le côté est irrigué, false sinon
     */
    public boolean isIrrigate(Side side) {
        return this.irrigate.get(side);
    }

    /**
     * Place le jardinier ou le panda sur cette parcelle.
     * @param pawn le jardinier ou le panda
     * @return true si le jardinier ou le panda se trouve sur la parcelle, false sinon
     */
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

    public void draw(ParcelDrawer drawer) {

        // Si elle n'est pas irrigué, elle peut partager des bordures qui le sont
        irrigate.forEach((side, isIrrigated) -> {

            boolean irrigate = isIrrigate(side);
            if(irrigate) {
                drawer.setIrrigate(side);
            }
        });
    }

    @Override
    public String toString() {
        return super.toString();
    }

}
