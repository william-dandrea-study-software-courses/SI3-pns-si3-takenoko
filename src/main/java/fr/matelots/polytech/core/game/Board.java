package fr.matelots.polytech.core.game;

import fr.matelots.polytech.core.NoParcelLeftToPlaceException;
import fr.matelots.polytech.core.game.deck.*;
import fr.matelots.polytech.core.game.movables.Gardener;
import fr.matelots.polytech.core.game.movables.Panda;
import fr.matelots.polytech.core.game.movables.Pawn;
import fr.matelots.polytech.core.game.parcels.*;
import fr.matelots.polytech.core.NoLayoutLeftException;
import fr.matelots.polytech.engine.util.Position;

import java.util.*;

/**
 * This class describe the main game board 
 * @author Gabriel Cogne, Alexandre Arcil
 */
public class Board {
    // Attributes
    private final Map<Position, Parcel> grid;
    private final DeckParcelObjective deckParcelObjective;
    private final DeckGardenerObjective deckGardenerObjective;
    private final DeckPandaObjective deckPandaObjective;
    private final DeckParcel deckParcel;
    private final DeckLayoutBasin deckLayoutBasin;
    private final DeckLayoutEnclosure deckLayoutEnclosure;
    private final DeckLayoutFertilizer deckLayoutFertilizer;


    private int layoutBasinLeftToPlace;
    private int layoutFertilizerLeftToPlace;
    private int layoutEnclosureLeftToPlace;

    private int parcelLeftToPlace;
    private int yellowParcelLeftToPlace;
    private int pinkParcelLeftToPlace;
    private int greenParcelLeftToPlace;

    private int irrigationLeft;

    private final Gardener gardener;
    private final Panda panda;

    // Constructors
    public Board () {
        grid = new HashMap<>();
        this.deckParcelObjective = new DeckParcelObjective(this);
        this.deckGardenerObjective = new DeckGardenerObjective(this);
        this.deckPandaObjective = new DeckPandaObjective(this);
        this.deckParcel = new DeckParcel(this);

        this.deckLayoutBasin = new DeckLayoutBasin(this);
        this.deckLayoutFertilizer = new DeckLayoutFertilizer(this);
        this.deckLayoutEnclosure = new DeckLayoutEnclosure(this);
        // On ajoute l'étang
        grid.put(Config.POND_POSITION, new Pond());
        gardener = new Gardener(this, Config.POND_POSITION);
        placePawn(gardener, Config.POND_POSITION);
        panda = new Panda(this, Config.POND_POSITION);
        placePawn(panda, Config.POND_POSITION);

        irrigationLeft = Config.NB_IRRIGATION;

        parcelLeftToPlace = Config.NB_PLACEABLE_PARCEL;
        yellowParcelLeftToPlace = Config.NB_MAX_YELLOW_PARCELS;
        greenParcelLeftToPlace = Config.NB_MAX_GREEN_PARCELS;
        pinkParcelLeftToPlace = Config.NB_MAX_PINK_PARCELS;

        layoutBasinLeftToPlace = Config.NB_LAYOUT_BASIN;
        layoutFertilizerLeftToPlace = Config.NB_LAYOUT_FERTILIZER;
        layoutEnclosureLeftToPlace = Config.NB_LAYOUT_ENCLOSURE;
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
            if(p.getBambooColor() != null) {
                // The tile have a color
                switch (p.getBambooColor()) {
                    case YELLOW:
                        // if tile of color exhausted, throw error
                        if(yellowParcelLeftToPlace <= 0) throw  new NoParcelLeftToPlaceException();
                            // else decrement corresponding counter
                        else yellowParcelLeftToPlace--;
                        break;
                    case GREEN:
                        if(greenParcelLeftToPlace <= 0) throw new NoParcelLeftToPlaceException();
                        else greenParcelLeftToPlace--;
                        break;
                    case PINK:
                        if(pinkParcelLeftToPlace <= 0) throw new NoParcelLeftToPlaceException();
                        else pinkParcelLeftToPlace--;
                        break;
                }
            }

            Position position = new Position(x, y, z);
            grid.put(position, p);
            parcelLeftToPlace--;

            for(Side side : Side.values()) { //Les parcels à côté de l'étang sont irrigués
                Position posAdjacent = position.add(side.getDirection());
                Parcel parcel = this.getParcel(posAdjacent);
                if(parcel instanceof Pond) {
                    p.setIrrigate(side);
                }
            }
            return true;
        }
        return false;
    }

    public boolean addParcel(Position position, Parcel p) throws NoParcelLeftToPlaceException {
        return addParcel(position.getX(), position.getY(), position.getZ(), p);
    }

    public boolean addBambooPlantation(Position position) {
        // Will change with the parcel deck
        // Will be renamed with others parcels

        Random rnd = new Random();
        var colors = BambooColor.values();
        var color = colors[rnd.nextInt(colors.length)];

        return addParcel(position, new BambooPlantation(color));
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
                if (tmp != null) // containTile? ;)
                    res.add(tmp);
        });

        return res;
    }


    public boolean placeLayout(Position position, Layout layout) {

        boolean res = false;
        boolean canAdd = false;
        switch (layout) {
            case BASIN: {
                if (layoutBasinLeftToPlace > 0)
                    canAdd = true;
                else throw new NoLayoutLeftException();
            } break;
            case ENCLOSURE: {
                if (layoutEnclosureLeftToPlace > 0)
                    canAdd = true;
                else throw new NoLayoutLeftException();
            }break;
            case FERTILIZER: {
                if (layoutFertilizerLeftToPlace > 0)
                    canAdd = true;
                else throw new NoLayoutLeftException();
            }break;
        }

        if (getPositions().contains(position) && !getParcel(position).hasLayout() && getParcel(position).getBambooSize() == 0 && canAdd) {
            res = getParcel(position).setLayout(layout);
        }

        if (res) {
            switch (layout) {
                case BASIN: { layoutBasinLeftToPlace--; } break;
                case ENCLOSURE: { layoutEnclosureLeftToPlace--; }break;
                case FERTILIZER: { layoutFertilizerLeftToPlace--;}break;
            }
        }

        return res;
    }

    public int getLayoutBasinLeftToPlace() {
        return layoutBasinLeftToPlace;
    }

    public int getLayoutFertilizerLeftToPlace() {
        return layoutFertilizerLeftToPlace;
    }

    public int getLayoutEnclosureLeftToPlace() {
        return layoutEnclosureLeftToPlace;
    }

    /**
     * Place une irrigation sur le côté <code>side</code> de la parcelle se trouvant à la position <code>position</code>.
     * Pour cela, il faut qu'elle se trouve entre 2 parcelles, et qu'au moins un côté adjacent soit irrigué. Si le
     * côté est déjà irrigué, rien ne se passe.
     * @param position La position d'une parcelle où poser l'irrigation
     * @param side Le côté de la parcelle où poser l'irrigation
     * @return true si l'irrigation a été posée, false sinon
     */
    public boolean placeIrrigation(Position position, Side side) {
        Parcel parcel = this.getParcel(position);
        Side sideAdjacent = side.oppositeSide();
        Parcel parcelAdjacent = this.getParcel(position.add(side.getDirection()));

        // si parcel null on recommence avec la parcelle adjacente.
        // parcelAdjacent != null evite les boucles infinies
        if(parcel == null && parcelAdjacent != null) return placeIrrigation(position.add(side.getDirection()), side.oppositeSide());

        if (canPlaceIrrigation(position, side)) {
            if(parcel.isIrrigate(side)) return false;
            parcel.setIrrigate(side);
            if(parcelAdjacent != null)
                parcelAdjacent.setIrrigate(sideAdjacent);

            return true;
        }
        return false;
    }

    public boolean canPlaceIrrigation(Position position, Side side) {
        Parcel parcel = this.getParcel(position);
        Side sideAdjacent = side.oppositeSide();
        Parcel parcelAdjacent = this.getParcel(position.add(side.getDirection()));

        // si parcel null on recommence avec la parcelle adjacente.
        // parcelAdjacent != null evite les boucles infinies

        if(parcelAdjacent == null || parcel == null) return false;

        if(parcel.isIrrigate(side.rightSide()) || parcel.isIrrigate(side.leftSide())) return true;
        return parcelAdjacent.isIrrigate(sideAdjacent.rightSide()) || parcelAdjacent.isIrrigate(sideAdjacent.leftSide());
    }

    public boolean isInterstice(Position position, Side side) {
        Parcel parcel = getParcel(position);
        Parcel adjacenteParcel = getParcel(position.add(side.getDirection()));

        return parcel != null && adjacenteParcel != null;
    }

    @Override
    public String toString() {
        final StringBuilder builder = new StringBuilder();
        grid.keySet().forEach(pos -> {
            builder.append("(");
            builder.append(pos);
            builder.append(",");
            builder.append(grid.get(pos).getBambooColor());
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

    public DeckGardenerObjective getDeckGardenerObjective() {
        return deckGardenerObjective;
    }

    public DeckPandaObjective getDeckPandaObjective() {
        return deckPandaObjective;
    }


    public DeckLayoutBasin getDeckBasinLayout() {
        return deckLayoutBasin;
    }
    public DeckLayoutFertilizer getDeckFertilizerLayout() {
        return deckLayoutFertilizer;
    }

    public DeckLayoutEnclosure getDeckEnclosureLayout() {
        return deckLayoutEnclosure;
    }

    public int getParcelCount() {
        return grid.size();
    }

    public int getParcelCount(BambooColor color) {
        int i = 0;

        for (Map.Entry<Position, Parcel> entry : grid.entrySet()) {
            if (entry.getValue().getBambooColor() == color) {
                i++;
            }
        }

        return i;
    }


    public int getParcelLeftToPlace() {
        return parcelLeftToPlace;
    }

    public int getParcelLeftToPlace(BambooColor color) {
        switch (color) {
            case PINK: return pinkParcelLeftToPlace;
            case YELLOW: return yellowParcelLeftToPlace;
            case GREEN: return greenParcelLeftToPlace;
        }
        return 0;
    }

    public int getYellowParcelLeftToPlace() {
        return yellowParcelLeftToPlace;
    }

    public int getPinkParcelLeftToPlace() {
        return pinkParcelLeftToPlace;
    }

    public int getGreenParcelLeftToPlace() {
        return greenParcelLeftToPlace;
    }

    /**
     * Place a game pawn on a parcel defined by her position
     * @param pawn The pawn to place
     * @param position The position of the parcel
     */
    public boolean placePawn (Pawn pawn, Position position) {
        Parcel tmp = getParcel(position);
        if (tmp != null) {
            getParcel(pawn.getPosition()).removePawn(pawn);
            return tmp.placeOn(pawn);
        }
        return false;
    }

    public Gardener getGardener () {
        return gardener;
    }

    public Panda getPanda () {
        return panda;
    }

    /**
     * Get all reachable positions following to rules :<br>
     *  - The position must be aligned with the starting point<br>
     *  - There can be any blanks between the start and the position
     * @param start the position where you start
     * @return all reachable positions from the <i>start</i> position
     */
    public List<Position> getReachablePositionFrom (Position start) {
        List<Position> res = new ArrayList<>();
        res.add(start);

        for (Position direction : Config.CUBE_DIRECTIONS) {
            // We don't start from 0 because we won't have six time the start position
            for (int i = 1; i < Config.NB_PLACEABLE_PARCEL; i++) {
                Position tmp = new Position(start.getX() + (i * direction.getX()),
                                            start.getY() + (i * direction.getY()),
                                            start.getZ() + (i * direction.getZ()));
                if (containTile(tmp)) {
                    res.add(tmp);
                }
                else {
                    break;
                }
            }
        }

        return res;
    }

    public DeckParcel getDeckParcel() {
        return deckParcel;
    }
    
    public List<Parcel> pickParcels () {
        List<Parcel> res = new ArrayList<>();

        if (greenParcelLeftToPlace + pinkParcelLeftToPlace + yellowParcelLeftToPlace <= 0)
            return res;

        int dice;
        for (int i = 0; i < Config.NB_PARCEL_PICKED; i++) {
            dice = Config.RANDOM.nextInt(greenParcelLeftToPlace + pinkParcelLeftToPlace + yellowParcelLeftToPlace);
            if (dice < greenParcelLeftToPlace) {
                res.add(new BambooPlantation(BambooColor.GREEN));
            }
            else if (dice < greenParcelLeftToPlace + pinkParcelLeftToPlace) {
                res.add(new BambooPlantation(BambooColor.PINK));
            }
            else {
                res.add(new BambooPlantation(BambooColor.YELLOW));
            }
        }

        return res;
    }

    public int getIrrigationLeft() {
        return irrigationLeft;
    }

    public boolean canPickIrrigation() {
        return irrigationLeft > 0;
    }

    public boolean pickIrrigation() {
        if(!canPickIrrigation()) return false;
        irrigationLeft--;
        return true;
    }




}
