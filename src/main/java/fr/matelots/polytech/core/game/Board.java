package fr.matelots.polytech.core.game;

import fr.matelots.polytech.engine.util.Position;

import java.util.*;

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
    public Parcel getParcel (int x, int y, int z) {
        for (Position<Integer> pos : grid.keySet()) {
            if (pos.equals(new Position<>(x, y, z)))
                return grid.get(pos);
        }
        return null;
    }

    public boolean addParcel (int x, int y, int z, Parcel p) {
        if (isPlaceValid(x, y, z)) {
            grid.put(new Position<>(x, y, z), p);
            return true;
        }
        return false;
    }

    public boolean isPlaceValid (int x, int y, int z) {
        return getNbNeighbors(x, y, z) > 0;
    }

    public int getNbNeighbors(int x, int y, int z) {
        List<Position<Integer>> cubeDirections = Arrays.asList(
                new Position<>(1, -1, 0),
                new Position<>(0, -1, 1),
                new Position<>(-1, 0, 1),
                new Position<>(-1, 1, 0),
                new Position<>(0, 1, -1),
                new Position<>(1, 0, -1)
                );

        int nb = 0;
        Position<Integer> tmp;
        for (Position<Integer> pos : grid.keySet()) {
            tmp = new Position<>(pos.getX() - x, pos.getY() - y, pos.getZ() - z);
            if (cubeDirections.contains(tmp)) {
                nb++;
            }
        }

        return nb;
    }

    @Override
    public String toString() {
        final StringBuilder builder = new StringBuilder();
        grid.keySet().forEach(pos -> {
            builder.append("(");
            builder.append(pos);
            builder.append(",");
            builder.append(grid.get(pos));
            builder.append(")\n");
        });

        return "Plateau: \n" + builder.toString();
    }

    public Set<Position<Integer>> getPositions() {
        return new HashSet<>(this.grid.keySet()); //évite la modification
    }
}
