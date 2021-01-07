package fr.matelots.polytech.core.game.parcels;

import fr.matelots.polytech.core.game.graphics.ParcelDrawer;
import fr.matelots.polytech.core.game.movables.Gardener;
import fr.matelots.polytech.core.game.movables.Panda;
import fr.matelots.polytech.core.game.movables.Pawn;

import java.util.Arrays;
import java.util.EnumMap;

/**
 * @author Gabriel Cogne, Alexandre Arcil
 */
public abstract class Parcel {

    private Gardener gardener;
    private Panda panda;
    private final EnumMap<Side, Boolean> irrigate;
    private Layout layout;

    public Parcel() {
        this.irrigate = new EnumMap<>(Side.class);
        for(Side side : Side.values())
            this.irrigate.put(side, false);
    }



    // Constructors
    public abstract boolean isPond();

    public abstract int getBambooSize();

    public abstract void growBamboo();

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
        //System.out.println("irrigate : ");
        /*if(isIrrigate()) {
            Arrays.stream(Side.values()).forEach(drawer::setIrrigate);
            return;
        }*/

        // Si elle n'est pas irrigué, elle peut partager des bordures qui le sont
        irrigate.forEach((side, isIrrigated) -> {

            boolean irr = isIrrigate(side);
            //System.out.println(side + " : " + irr);
            if(irr) {
                drawer.setIrrigate(side);
            }
        });
    }



    // Methods
    @Override
    public String toString() {
        return super.toString();
    }

}
