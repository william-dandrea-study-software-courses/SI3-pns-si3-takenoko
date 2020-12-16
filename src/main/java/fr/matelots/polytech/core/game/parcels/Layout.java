package fr.matelots.polytech.core.game.parcels;

/**
 * Les aménagements qui peuvent être mis ou être directement inclus sur une parcelle.
 * @author Alexandre Arcil
 */
public enum Layout {

    /**
     * La parcelle est irrigué. Une unité de bamboo est déjà présent si l'aménagement est inclus sur la parcelle,
     * ou est ajouté si posé.
     */
    BASIN,
    /**
     * A chaque fois que le bamboo pousse, 2 sections sont ajoutées au lieu d'une.
     */
    FERTILIZER,
    /**
     * Le panda ne peut plus manger le bamboo qui se trouve sur la parcelle.
     */
    ENCLOSURE

}
