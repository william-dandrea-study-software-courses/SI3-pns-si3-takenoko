package fr.matelots.polytech.core.game.parcels;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * @author Gabriel Cogne
 */
public class BambooPlantationTest {
    BambooPlantation parcel;

    @BeforeEach
    public void init () {
        parcel = new BambooPlantation(BambooColor.green);
    }

    @Test
    public void testInitialisation (){
        assertEquals(0, parcel.getBambooSize());
    }

    @Test
    public void testGrowBambooByOneUnit () {
        parcel.growBamboo();
        assertEquals(1, parcel.getBambooSize());
    }

    @Test
    public void testGrowBambooOutOfBound () {
        for (int i = 0; i < 5; i++)
            parcel.growBamboo();

        assertEquals(4, parcel.getBambooSize());
    }
}
