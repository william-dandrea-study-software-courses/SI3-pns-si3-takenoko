package fr.matelots.polytech.core.game.parcels;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Alexandre Arcil
 */
public class PondTest {

    private Pond pond;

    @BeforeEach
    public void init() {
        pond = new Pond();
    }

    @Test @DisplayName("C'est bien un étang")
    public void isPond() {
        assertTrue(this.pond.isPond());
    }

    @Test @DisplayName("La taille du bamboo est de 0")
    public void bambooSizeEqualsZero() {
        assertEquals(0, this.pond.getBambooSize());
    }

    @Test @DisplayName("Faire grandir le bamboo ne fait rien")
    public void growBambooDoNothing() {
        this.pond.growBamboo();
        assertEquals(0, this.pond.getBambooSize());
    }

    @Test @DisplayName("Détruire le bamboo ne fait rien")
    public void destroyBambooDoNothing() {
        this.pond.destroyUnitOfBamboo();
        assertEquals(0, this.pond.getBambooSize());
    }

    @Test @DisplayName("Il n'a pas de couleur")
    public void noColor() {
        assertNull(this.pond.getBambooColor());
    }

    @Test @DisplayName("L'étang est irrigué")
    public void isIrrigate() {
        assertTrue(this.pond.isIrrigate());
    }

    @Test @DisplayName("L'étang est irrigué de tout les côtés")
    public void isIrrigateInEverySide() {
        for(Side side : Side.values())
            assertTrue(this.pond.isIrrigate(side));
    }

}
