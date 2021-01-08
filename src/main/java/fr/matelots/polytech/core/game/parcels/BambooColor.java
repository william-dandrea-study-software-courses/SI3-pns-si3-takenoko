package fr.matelots.polytech.core.game.parcels;

/**
 * Représente la couleur d'un bambou
 * @author Alexandre Arcil
 */
public enum BambooColor {

    GREEN() {
        @Override
        public String toString() {
            return "Green";
        }
    }, PINK() {
        @Override
        public String toString() {
            return "Pink";
        }
    }, YELLOW() {
        @Override
        public String toString() {
            return "Yellow";
        }
    }

}
