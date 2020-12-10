package fr.matelots.polytech.core.game.parcels;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Gabriel Cogne
 */
public class BambooPlantationTest {
    BambooPlantation parcel;

    @BeforeEach
    public void init () {
        parcel = new BambooPlantation(BambooColor.GREEN);
    }

    @Test
    public void testInitialisationBambooSize (){
        assertEquals(0, parcel.getBambooSize());
    }

    @Test
    public void testInitialisationNotIrrigate() {
        assertFalse(this.parcel.isIrrigate());
    }

    @Test
    public void testSetIrrigateUpperRight() {
        this.parcel.setIrrigate(Side.UPPER_RIGHT);
        assertTrue(this.parcel.isIrrigate(Side.UPPER_RIGHT));
    }

    @Test
    public void testSetIrrigateRight() {
        this.parcel.setIrrigate(Side.RIGHT);
        assertTrue(this.parcel.isIrrigate(Side.RIGHT));
    }

    @Test
    public void testSetIrrigateBottomRight() {
        this.parcel.setIrrigate(Side.BOTTOM_RIGHT);
        assertTrue(this.parcel.isIrrigate(Side.BOTTOM_RIGHT));
    }

    @Test
    public void testSetIrrigateBottomLeft() {
        this.parcel.setIrrigate(Side.BOTTOM_LEFT);
        assertTrue(this.parcel.isIrrigate(Side.BOTTOM_LEFT));
    }

    @Test
    public void testSetIrrigateLeft() {
        this.parcel.setIrrigate(Side.LEFT);
        assertTrue(this.parcel.isIrrigate(Side.LEFT));
    }

    @Test
    public void testSetIrrigateUpperLeft() {
        this.parcel.setIrrigate(Side.UPPER_LEFT);
        assertTrue(this.parcel.isIrrigate(Side.UPPER_LEFT));
    }

    @Test
    public void testGrowBambooByOneUnit () {
        parcel.setIrrigate(Side.RIGHT);
        parcel.growBamboo();
        assertEquals(1, parcel.getBambooSize());
    }

    @Test
    public void testGrowBambooOutOfBound () {
        parcel.setIrrigate(Side.RIGHT);
        for (int i = 0; i < 5; i++)
            parcel.growBamboo();

        assertEquals(4, parcel.getBambooSize());
    }

    @Test
    public void testNotIrrigateParcelWhereBambooCanGrow() {
        parcel.growBamboo();
        assertEquals(0, parcel.getBambooSize());
    }
}
