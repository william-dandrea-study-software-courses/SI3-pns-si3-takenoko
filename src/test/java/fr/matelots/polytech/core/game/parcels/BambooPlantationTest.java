package fr.matelots.polytech.core.game.parcels;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * @author Gabriel Cogne, Alexandre Arcil
 */
public class BambooPlantationTest {

    BambooPlantation parcel;
    BambooPlantation parcelBasin;
    BambooPlantation parcelFertilizer;
    BambooPlantation parcelEnclosure;

    @BeforeEach
    public void init () {
        parcel = new BambooPlantation(BambooColor.GREEN);
        parcelBasin = new BambooPlantation(BambooColor.GREEN, Layout.BASIN);
        parcelFertilizer = new BambooPlantation(BambooColor.GREEN, Layout.FERTILIZER);
        parcelEnclosure = new BambooPlantation(BambooColor.GREEN, Layout.ENCLOSURE);
    }

    @Test
    public void testInitialisationBambooNoLayout(){
        assertFalse(this.parcel.hasLayout());
    }

    @Test
    public void testInitialisationBambooLayoutBasin(){
        assertTrue(this.parcelBasin.hasLayout());
    }

    @Test
    public void testInitialisationBambooLayoutFertilizer(){
        assertTrue(this.parcelFertilizer.hasLayout());
    }

    @Test
    public void testInitialisationBambooLayoutEnclosure(){
        assertTrue(this.parcelEnclosure.hasLayout());
    }

    @Test
    public void testInitialisationBambooSize (){
        assertEquals(0, parcel.getBambooSize());
    }

    @Test
    public void testInitialisationBambooSizeParcelBasin(){
        assertEquals(1, parcelBasin.getBambooSize());
    }

    @Test
    public void testInitialisationNotIrrigate() {
        assertFalse(this.parcel.isIrrigate());
    }

    @Test
    public void testInitialisationParcelBasinIrrigate() {
        assertTrue(this.parcelBasin.isIrrigate());
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
    public void testGrowBambooByOneUnitParcelFertilizer () {
        this.parcelFertilizer.setIrrigate(Side.RIGHT);
        this.parcelFertilizer.growBamboo();
        assertEquals(2, this.parcelFertilizer.getBambooSize());
    }

    @Test
    public void testGrowBambooOutOfBound () {
        parcel.setIrrigate(Side.RIGHT);
        for (int i = 0; i < 5; i++)
            parcel.growBamboo();

        assertEquals(4, parcel.getBambooSize());
    }

    @Test
    public void testGrowBambooOutOfBoundParcelFertilizer () {
        this.parcelFertilizer.setIrrigate(Side.RIGHT);
        for (int i = 0; i < 3; i++)
            this.parcelFertilizer.growBamboo();

        assertEquals(4, this.parcelFertilizer.getBambooSize());
    }

    @Test
    public void testNotIrrigateParcelWhereBambooCanGrow() {
        parcel.growBamboo();
        assertEquals(0, parcel.getBambooSize());
    }

    @Test
    public void testCantDestroyBambooParcelEnclosure() {
        this.parcelEnclosure.setIrrigate(Side.RIGHT);
        this.parcelEnclosure.growBamboo();
        this.parcelEnclosure.destroyUnitOfBamboo();
        assertEquals(1, this.parcelEnclosure.getBambooSize());
    }

}
