package core.game;

import engine.util.Position;

import java.util.HashMap;
import java.util.Map;

public class Board {
    // Attributes
    private final Map<Position<Integer>, Parcel> grid;

    // Constructors
    public Board () {
        grid = new HashMap<>();
        // On ajout l'étang
        grid.put(new Position<>(0, 0, 0), new Parcel());
    }

    // Methods and Function
    public boolean addParcel (int x, int y, int z, Parcel p) {
        if (isPlaceValid(x, y, z)) {
            grid.put(new Position<>(x, y, z), p);
            return true;
        }
        return false;
    }

    private boolean isPlaceValid (int x, int y, int z) {
        return getNbNeighbors(x, y, z) > 0;
    }

    private int getNbNeighbors(int x, int y, int z) {
        return (int) grid.keySet()
                .stream()
                .filter(pos ->
                        (Math.abs(pos.getX() - x) <= 1) &&
                                (Math.abs(pos.getY() - y) <= 1) &&
                                (Math.abs(pos.getZ() - z) <= 1))
                .count();
    }

    @Override
    public String toString() {
        final StringBuilder builder = new StringBuilder();
        grid.keySet().forEach(pos -> {
            builder.append("(x=");
            builder.append(pos.getX());
            builder.append(", y=");
            builder.append(pos.getY());
            builder.append(", z=");
            builder.append(pos.getZ());
            builder.append(",");
            builder.append(grid.get(pos));
            builder.append(")\n");
        });

        return "Plateau: \n" + builder.toString();
    }
}
