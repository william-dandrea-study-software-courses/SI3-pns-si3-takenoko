package fr.matelots.polytech.engine.util;

import fr.matelots.polytech.core.game.Board;
import fr.matelots.polytech.core.game.Config;
import fr.matelots.polytech.core.game.parcels.Parcel;
import fr.matelots.polytech.core.game.parcels.Side;

import java.util.*;
import java.util.function.Predicate;

public class ParcelRouteFinder {
    private static List<Side> getPathInParcel(Side input, Side output) {
        if(input == null || output == null) throw new RuntimeException("Error");
        List<Side> toIrrigate = new ArrayList<>();
        // Evalute distance by left
        int distanceRight = 0;
        Side temp = input;
        while(temp != output) {
            temp = temp.rightSide();
            distanceRight++;
        }

        // Evalute distance by left
        temp = output;
        int distanceLeft = 0;
        while(temp != output) {
            temp = temp.leftSide();
            distanceLeft++;
        }

        // return the nearest
        if(distanceLeft < distanceRight) {
            temp = input;
            while(temp != output) {
                temp = temp.leftSide();
                toIrrigate.add(temp);
            }
        } else {
            temp = input;
            while(temp != output) {
                temp = temp.rightSide();
                toIrrigate.add(temp);
            }
        }
        toIrrigate.add(output);

        return toIrrigate;
    }
    private static List<Side> getPathInParcel(Parcel currentParcel, Side output) {
        if(currentParcel == null || output == null) throw new RuntimeException("Error");
        List<Side> toIrrigate = new ArrayList<>();
        // Evalute distance by left
        int distanceRight = 0;
        Side temp = output;
        while(!currentParcel.isIrrigate(temp)) {
            temp = temp.rightSide();
            distanceRight++;
        }

        // Evalute distance by left
        temp = output;
        int distanceLeft = 0;
        while(!currentParcel.isIrrigate(temp)) {
            temp = temp.leftSide();
            distanceLeft++;
        }

        // return the nearest
        if(distanceLeft < distanceRight) {
            temp = output;
            while(!currentParcel.isIrrigate(temp)) {
                toIrrigate.add(temp);
                temp = temp.leftSide();
            }
        } else {
            temp = output;
            while(!currentParcel.isIrrigate(temp)) {
                toIrrigate.add(temp);
                temp = temp.rightSide();
            }
        }

        return toIrrigate;
    }

    private static List<AbsolutePositionIrrigation> getPathInAParcelWithIrrigation(Board board, Position currentPosition, Side output, Side input) {
        var parcel = board.getParcel(currentPosition);
        List<AbsolutePositionIrrigation> list = new ArrayList<>();
        List<Side> result;
        if(input == null) {
            result = getPathInParcel(parcel, output);
        } else {
            result = getPathInParcel(input, output);
        }
        result.stream().forEach(s -> list.add(new AbsolutePositionIrrigation(currentPosition, s, board)));
        return list;
    }

    public static Optional<Set<AbsolutePositionIrrigation>> getRequiredIrrigation(Board board, Position start, Position end) {
        var positions = LineDrawer.getLine(start, end);
        Set<AbsolutePositionIrrigation> result = new HashSet<>();

        Side oldSide = null;
        for(int i = 0; i < positions.size() - 1; i++) {
            if(!board.getPositions().contains(positions.get(i))) return Optional.empty();

            Side output = Side.getTouchedSide(positions.get(i), positions.get(i + 1));
            if(output == null) throw new RuntimeException("Tiles are not neighbour");

            var pathInThisParcel = getPathInAParcelWithIrrigation(board, positions.get(i), output, oldSide);
            result.addAll(pathInThisParcel);
            oldSide = output.oppositeSide();
        }

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
                .filter(p -> !p.isEmpty())
                .min(Comparator.comparing(Set::size)); // on recupere le chemin le moins couteux
        return shortest;
    }

    public static Optional<AbsolutePositionIrrigation> getNextParcelToIrrigate(Set<AbsolutePositionIrrigation> path) {
        return path.stream().filter(api -> !api.isIrrigate() && api.canBeIrrigated()).findAny();
    }
}
