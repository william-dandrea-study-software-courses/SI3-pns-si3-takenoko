package fr.matelots.polytech.core.game;

import fr.matelots.polytech.core.NoParcelLeftToPlaceException;
import fr.matelots.polytech.core.game.movables.Gardener;
import fr.matelots.polytech.core.game.parcels.Parcel;
import fr.matelots.polytech.core.game.parcels.Pond;
import fr.matelots.polytech.engine.util.Position;

import java.util.*;

/**
 * This class describe the main game board 
 * @author Gabriel Cogne
 */
public class Board {
    // Attributes
    private final Map<Position, Parcel> grid;
    private final DeckParcelObjective deckParcelObjective;
    private int parcelLeftToPlace;

    // Constructors
    public Board () {
        grid = new HashMap<>();
        this.deckParcelObjective = new DeckParcelObjective(this);
        // On ajoute l'étang
        grid.put(Config.BOND_POSITION, new Pond());
        placePawn(new Gardener(this, Config.BOND_POSITION), Config.BOND_POSITION);

        parcelLeftToPlace = Config.NB_PLACEABLE_PARCEL;
    }

    // Methods and Function
    public Parcel getParcel (int x, int y, int z) {
        return getParcel(new Position(x, y, z));
    }

    public Parcel getParcel (Position position) {
        for (Position pos : grid.keySet()) {
            if (pos.equals(position))
                return grid.get(pos);
        }
        return null;
    }

    public boolean addParcel (int x, int y, int z, Parcel p) throws NoParcelLeftToPlaceException {
        if (parcelLeftToPlace <= 0)
            throw new NoParcelLeftToPlaceException();

        if (isPlaceValid(x, y, z)) {
            grid.put(new Position(x, y, z), p);
            parcelLeftToPlace--;
            return true;
        }
        return false;
    }

    public boolean addParcel(Position position, Parcel p) throws NoParcelLeftToPlaceException {
        return addParcel(position.getX(), position.getY(), position.getZ(), p);
    }

    public boolean isPlaceValid (int x, int y, int z) {
        if (getParcel(x, y, z) != null)
            return false;

        List<Parcel> neighbours = getNeighbours(x, y, z);

        if (neighbours.size() > 1)
            return true;
        else
            return neighbours.stream().anyMatch(Parcel::isPond);
    }
    public boolean isPlaceValid (Position position) {
        return isPlaceValid(position.getX(), position.getY(), position.getZ());
    }

    public Set<Position> getValidPlaces() {
        Set<Position> validPlaces = new HashSet<>();
        var positions = getPositions();
        for(var position : positions) {
            for (var direction : Config.CUBE_DIRECTIONS) {
                Position checkingPosition = position.add(direction);
                if(isPlaceValid(checkingPosition))
                    validPlaces.add(checkingPosition);
            }
        }

        return validPlaces;
    }

    public List<Parcel> getNeighbours(int x, int y, int z) {
        final List<Parcel> res = new ArrayList<>();

        Config.CUBE_DIRECTIONS.forEach(direction -> {
                Parcel tmp = getParcel(x + direction.getX(), y + direction.getY(), z + direction.getZ());
                if (tmp != null)
                    res.add(tmp);
        });

        return res;
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

    public Set<Position> getPositions() {
        return new HashSet<>(this.grid.keySet()); // évite la modification
    }

    public boolean containTile(Position position) {
        return getParcel(position) != null;
    }

    public DeckParcelObjective getDeckParcelObjective() {
        return deckParcelObjective;
    }

    public int getParcelCount() {
        return grid.size();
    }

    public int getParcelLeftToPlace() {
        return parcelLeftToPlace;
    }

    /**
     * Place a game pawn on a parcel defined by her position
     * @param gardener The pawn to place
     * @param position The position of the parcel
     */
    public boolean placePawn (Gardener gardener, Position position) {
        Parcel tmp = getParcel(position);
        if (tmp != null) {
            return tmp.placeOn(gardener);
        } else {
            return false;
        }
    }

    public Gardener getGardener () {
        for (Parcel p : grid.values()) {
            if (p.getGardener() != null)
                return p.getGardener();
        }
        return null;
    }
}
