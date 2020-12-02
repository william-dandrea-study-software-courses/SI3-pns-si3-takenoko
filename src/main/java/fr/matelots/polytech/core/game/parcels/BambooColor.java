package fr.matelots.polytech.core.game.parcels;

public enum BambooColor {
    green() {
        @Override
        public String toString() {
            return "G";
        }
    }, pink() {
        @Override
        public String toString() {
            return "P";
        }
    }, yellow() {
        @Override
        public String toString() {
            return "Y";
        }
    };
}
