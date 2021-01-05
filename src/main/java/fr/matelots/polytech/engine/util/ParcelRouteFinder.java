package fr.matelots.polytech.engine.util;

import fr.matelots.polytech.core.game.Board;
import fr.matelots.polytech.core.game.parcels.Side;

import java.util.*;
import java.util.function.Predicate;

public class ParcelRouteFinder {
    private static List<Side> getPathInParcel(Side input, Side output) {
        List<Side> toIrrigate = new ArrayList<>();
        if(output != null) {
            if(output == input) return toIrrigate;
            else if(output == input.leftSide() || output == input.leftSide().leftSide()) {
                // By the left !
                toIrrigate.add(input.leftSide());
                if(output == input.leftSide().leftSide()) {
                    toIrrigate.add(input.leftSide().leftSide());
                }
            }
            else {
                // by the right !
                toIrrigate.add(input.rightSide());
                if(output != input.rightSide()) {
                    // output is (opposite or two to right)
                    toIrrigate.add(input.rightSide().rightSide());
                }
                if(output == input.oppositeSide()) {
                    toIrrigate.add(input.oppositeSide());
                }
            }
        }
        return toIrrigate;
    }
    private static List<AbsolutePositionIrrigation> encapsulate(Position position, List<Side> sides, Board board) {
        List<AbsolutePositionIrrigation> result = new ArrayList<>();
        sides.forEach(s -> result.add(new AbsolutePositionIrrigation(position, s, board)));
        return result;
    }
    private static List<AbsolutePositionIrrigation> getPathInAParcelWithIrrigation(Board board, Position positionParcel, Position outputPosition, Side inputSide) {
        if(outputPosition == null && inputSide == null) return new ArrayList<>(); // Final position

        Side input = null;
        if(inputSide != null) {
            input = inputSide;
        } else {
            var parcel = board.getParcel(positionParcel);
            var irrigatedSide = Arrays.stream(Side.values()).filter(parcel::isIrrigate).findAny();

            if(irrigatedSide.isPresent()) {
                input = irrigatedSide.get();
            } else {
                throw new RuntimeException("This parcel is not irrigated");
            }
        }

        Position delta = new Position(
                outputPosition.getX() - positionParcel.getX(),
                outputPosition.getY() - positionParcel.getY(),
                outputPosition.getZ() - positionParcel.getZ()
        );

        var output = Arrays.stream(Side.values()).filter(s -> s.getDirection().equals(delta)).findAny();
        if(output.isPresent()) {
            return encapsulate(positionParcel, getPathInParcel(input, output.get()), board);
        } else {
            throw new RuntimeException("Parcels are not neighbours");
        }
    }

    public static Optional<Set<AbsolutePositionIrrigation>> getRequiredIrrigation(Board board, Position start, Position end) {
        var positions = LineDrawer.getLine(start, end);
        Set<AbsolutePositionIrrigation> result = new HashSet<>();

        Side oldSide = null;
        for(int i = 0; i < positions.size() - 1; i++) {
            if(!board.getPositions().contains(positions.get(i))) return Optional.empty();

            var pathInThisParcel =
                    getPathInAParcelWithIrrigation(
                            board,
                            positions.get(i),
                            positions.get(i + 1),
                            oldSide == null ? null : oldSide.oppositeSide()
                    );
            result.addAll(pathInThisParcel);
            if(!pathInThisParcel.isEmpty())
                oldSide = pathInThisParcel.get(pathInThisParcel.size() - 1).getSide();
        }

        result.addAll(getPathInAParcelWithIrrigation(
                board,
                positions.get(positions.size() - 1),
                null, null)
        );

        return Optional.of(result);
    }

    /**
     * En Admettant la map convex.
     * Si elle ne l'est pas et que la ligne droite entre le debut et la fin passe par dessus un vide,
     * La fonction retournera un Set vide
     * @param board : Board where to search path
     * @param start : Starting position irrigated
     * @param end : End position to reached
     * @return Empty Set if no Path, Set of position else
     */
    public static Optional<Set<AbsolutePositionIrrigation>> getIrrigationToIrrigate(Board board, Position start, Position end) {
        Set<AbsolutePositionIrrigation> positions = new HashSet<>();

        var resultPath = getRequiredIrrigation(board, start, end);
        if(resultPath.isEmpty()) return  Optional.empty();

        resultPath.get().stream()
                .filter(Predicate.not(AbsolutePositionIrrigation::isIrrigate))
                .forEach(positions::add);

        return Optional.of(positions);
    }

    public static Optional<Set<AbsolutePositionIrrigation>> getBestPathToIrrigate(Board board, Position positionToIrrigate) {
        var shortest = board.getPositions().stream()
                .filter(p -> board.getParcel(p).isIrrigate()) // on recupere les cases irrigué
                .map(p -> getIrrigationToIrrigate(board, p, positionToIrrigate)) // On recherche tout les chemins
                .filter(Optional::isPresent)        // On retire les positions jugé sans chemin
                .map(Optional::get)                 // on recupere les valeur des optionnels
                .min(Comparator.comparing(Set::size)); // on recupere le chemin le moins couteux
        return shortest;
    }
}
