package fr.matelots.polytech.engine.util;

import fr.matelots.polytech.core.game.Board;
import fr.matelots.polytech.core.game.parcels.Side;

import java.util.*;
import java.util.function.Predicate;

/**
 * @author Yann Clodong
 */
public class ParcelRouteFinder {

    private static Optional<List<Side>> getPathInParcel(Side output, Predicate<Side> isGoodInput, Predicate<Side> isPraticable) {
        if(output == null) throw new RuntimeException("Error");
        if(Arrays.stream(Side.values()).noneMatch(isGoodInput))
            return Optional.empty();

        List<Side> toIrrigate = new ArrayList<>();
        // Evalute distance by left
        int distanceRight = 0;
        Side temp = output;
        boolean canUseLeft = true;
        while(canUseLeft && !isGoodInput.test(temp)) {
            temp = temp.leftSide();
            distanceRight++;
            if(!isPraticable.test(temp)) canUseLeft = false;
        }

        // Evalute distance by left
        temp = output;
        int distanceLeft = 0;
        boolean canUseRight = true;
        while(canUseRight && !isGoodInput.test(temp)) {
            temp = temp.rightSide();
            distanceLeft++;
            if(!isPraticable.test(temp)) canUseRight = false;
        }

        if(canUseLeft && canUseRight) {
            // return the nearest
            if (distanceLeft < distanceRight) {
                temp = output;
                while (!isGoodInput.test(temp)) {
                    toIrrigate.add(temp);
                    temp = temp.leftSide();
                }
                toIrrigate.add(temp);

            } else {
                temp = output;
                while (!isGoodInput.test(temp)) {
                    toIrrigate.add(temp);
                    temp = temp.rightSide();
                }
                toIrrigate.add(temp);

            }
        } else if(canUseLeft) {
            temp = output.leftSide();
            toIrrigate.add(output);
            while (!isGoodInput.test(temp) && temp != output) {
                toIrrigate.add(temp);
                temp = temp.leftSide();
            }
            toIrrigate.add(temp);

            if(temp == output)
                return Optional.empty();
        } else if(canUseRight) {
            temp = output.rightSide();
            toIrrigate.add(output);
            while (!isGoodInput.test(temp) && temp != output) {
                toIrrigate.add(temp);
                temp = temp.rightSide();
            }
            toIrrigate.add(temp);

            if(temp == output)
                return Optional.empty();
        } else
            return Optional.empty();

        return Optional.of(toIrrigate);
    }

    private static Optional<List<AbsolutePositionIrrigation>> getPathInAParcelWithIrrigation(Board board, Position currentPosition, Side output, Side input) {
        List<AbsolutePositionIrrigation> list = new ArrayList<>();
        List<Side> result;
        Optional<List<Side>> opt;
        if(input == null) {
            opt = getPathInParcel(output, side -> board.canPlaceIrrigation(currentPosition, side), side -> board.isInterstice(currentPosition, side));

        } else {

            opt = getPathInParcel(output, side -> side == input, side -> board.isInterstice(currentPosition, side));

        }
        if(opt.isPresent())
            result = opt.get();
        else
            return Optional.empty();
        result.forEach(s -> list.add(new AbsolutePositionIrrigation(currentPosition, s, board)));
        return Optional.of(list);
    }

    private static Optional<Set<AbsolutePositionIrrigation>> getRequiredIrrigation(Board board, Position start, Position end) {
        var positions = LineDrawer.getLine(start, end);
        Set<AbsolutePositionIrrigation> result = new HashSet<>();

        Side oldSide = null;
        for(int i = 0; i < positions.size() - 1; i++) {
            if(!board.getPositions().contains(positions.get(i))) return Optional.empty();

            Side output = Side.getTouchedSide(positions.get(i), positions.get(i + 1));
            if(output == null) throw new RuntimeException("Tiles are not neighbour");

            var pathInThisParcel = getPathInAParcelWithIrrigation(board, positions.get(i), output, oldSide);
            if(pathInThisParcel.isEmpty()) return Optional.empty();

            result.addAll(pathInThisParcel.get());
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

    /**
     * Récupère les irrigations nécessaires pour irriguer la parcelle positionToIrrigate
     * @param board la plateau
     * @param positionToIrrigate la parcelle à irriguer
     * @return Set de positions à irriguer pour irriguer la parcelle positionToIrrigate
     */
    public static Optional<Set<AbsolutePositionIrrigation>> getBestPathToIrrigate(Board board, Position positionToIrrigate) {
        var nearestPoint = board.getPositions().stream().filter(p -> board.getParcel(p).isIrrigate()).sorted(Comparator.comparing(o1 -> (Math.abs(o1.getX() - positionToIrrigate.getX()) +
                Math.abs(o1.getY() - positionToIrrigate.getY()) +
                Math.abs(o1.getZ() - positionToIrrigate.getZ())) / 2)).toArray(Position[]::new);
        Set<AbsolutePositionIrrigation> path = null;
        int i = 0;

        while(path == null && i < nearestPoint.length) {
            path = getIrrigationToIrrigate(board, nearestPoint[i], positionToIrrigate).orElse(null);
            i++;
        }

        if(path == null) return Optional.empty();
        else return Optional.of(path);
    }

    /**
     * Récupérer la première parcelle à irriguer dans le chemin donné
     * @param path le chemin vers la parcelle à irriguer
     * @return la position de la première parcelle à irriguer dans le chemin donné
     */
    public static Optional<AbsolutePositionIrrigation> getNextParcelToIrrigate(Set<AbsolutePositionIrrigation> path) {
        return path.stream().filter(api -> !api.isIrrigate() && api.canBeIrrigated()).findAny();
    }
}
