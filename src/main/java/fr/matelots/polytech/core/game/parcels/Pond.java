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
    public void destroyUnitOfBamboo () {}

    @Override
    public boolean isPond() {
        return true;
    }

    @Override
    public BambooColor getBambooColor() {
        return null;
    }

    @Override
    public void setIrrigate(Side side) {} //L'étang est irrigué de chaque côté, impossible de changer ça

    @Override
    public boolean isIrrigate() {
        return true; //permet juste d'éviter de faire le calcul
    }

    @Override
    public boolean isIrrigate(Side side) {
        return true;//L'étang est irrigué de tout les côtés
    }

    @Override
    public void draw(ParcelDrawer drawer) {
        drawer.set(HexagonePosition.CENTER, ConsoleColor.BLUE, 'P');
    }
}
