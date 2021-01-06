package fr.matelots.polytech.core.players;

import fr.matelots.polytech.core.game.Board;
import fr.matelots.polytech.core.game.Config;
import fr.matelots.polytech.core.game.goalcards.CardObjective;
import fr.matelots.polytech.core.game.goalcards.CardObjectiveGardener;
import fr.matelots.polytech.core.game.goalcards.CardObjectiveParcel;
import fr.matelots.polytech.core.game.goalcards.pattern.PositionColored;
import fr.matelots.polytech.core.game.parcels.BambooPlantation;
import fr.matelots.polytech.core.game.parcels.Layout;
import fr.matelots.polytech.engine.util.Position;

import java.util.*;

import static java.lang.StrictMath.abs;


/**
 * The best IA of the world
 *
 * @author williamdandrea
 */
public class MarganIA {

    public static PositionColored findTheBestPlaceToPlaceAnParcel(CardObjectiveParcel objective, Board board) {

        switch (objective.getPattern()) {
            case TRIANGLE:
            case C:
            case LINE:
                return resolvePatternLine(objective, board);
            case RHOMBUS:
                return resolveRhombusLine(objective, board);
        }
        return null;
    }


    /**
     * If obj already resolve, it return null
     * @param objective
     * @param board
     * @return
     */
    public static PositionColored resolvePatternLine(CardObjectiveParcel objective, Board board) {
        Set<Position> actualGrid = board.getPositions();
        objective.verify();

        PositionColored positionFinalWithOneParcelAround = null;
        Map<PositionColored, Integer> candidateParcels = new HashMap<PositionColored, Integer>();
        Set<PositionColored> missingPositions = objective.getMissingPositionsToComplete();

        //System.out.println("missingPosition : " + missingPositions);
        for (PositionColored pos : missingPositions) {
            int numberOfPlaceValidAroundThePosition = 0;

            // We turn around the position
            for (int i = 1; i <= 6; i++) {
                Position newPosition = nextPositionIncrement(i, pos.getPosition());
                //if (board.isPlaceValid(newPosition) || newPosition.equals(Config.POND_POSITION)) {
                if (board.containTile(newPosition)) {
                    numberOfPlaceValidAroundThePosition++;
                }
            }

            //System.out.println("Position : " + pos);
            //System.out.println("Number place : " + numberOfPlaceValidAroundThePosition);


            if (numberOfPlaceValidAroundThePosition >= 2) {
                // We can place an parcel at the missing position because this position is valid
                candidateParcels.put(pos, numberOfPlaceValidAroundThePosition);

            } else {
                // We can't place an parcel because we don't have more than 2 parcels around,
                // So, we will put a parcel at the closest of the first parcel
                if (numberOfPlaceValidAroundThePosition != 0) {
                    for (int i = 1; i <= 6; i++) {
                        Position internalPosition = nextPositionIncrement(i, pos.getPosition());
                        //System.out.println(internalPosition);

                        if (board.containTile(internalPosition)) {

                            for (int u = 6; u >= 1; u--) {
                                Position internalPosition2 = nextPositionIncrement(u, internalPosition);
                                if (board.isPlaceValid(internalPosition2)) {
                                    positionFinalWithOneParcelAround = new PositionColored(internalPosition2, objective.getColors()[0]);
                                }
                            }
                        }
                    }
                }
            }

        }


        // If we have some parcels, we can return the parcel with the max of pos
        if (candidateParcels.size() != 0) {
            return Collections.max(candidateParcels.entrySet(), (entry1, entry2) -> entry1.getValue() - entry2.getValue()).getKey();
        } else {
            if (positionFinalWithOneParcelAround != null) {
                return positionFinalWithOneParcelAround;
            }
            return null;
        }
    }








    public static PositionColored resolveRhombusLine(CardObjectiveParcel objective, Board board) {

        objective.verify();

        PositionColored positionFinalWithOneParcelAround = null;
        Map<PositionColored, Integer> candidateParcels = new HashMap<PositionColored, Integer>();
        Set<PositionColored> missingPositions = objective.getMissingPositionsToComplete();



        for (PositionColored pos : missingPositions) {
            int numberOfPlaceValidAroundThePosition = 0;

            if (board.isPlaceValid(pos.getPosition()))
                return pos;


            // We turn around the position
            for (int i = 1; i <= 6; i++) {
                Position newPosition = nextPositionIncrement(i, pos.getPosition());
                if (board.containTile(newPosition)) {
                    numberOfPlaceValidAroundThePosition++;
                }
            }

            //System.out.println("Position : " + pos);
            //System.out.println("Number place : " + numberOfPlaceValidAroundThePosition);



            // We can't place an parcel because we don't have more than 2 parcels around,
            // So, we will put a parcel at the closest of the first parcel
            if (numberOfPlaceValidAroundThePosition != 0) {
                for (int i = 1; i <= 6; i++) {
                    Position internalPosition = nextPositionIncrement(i, pos.getPosition());
                    //System.out.println(internalPosition);

                    if (board.containTile(internalPosition)) {

                        for (int u = 6; u >= 1; u--) {
                            Position internalPosition2 = nextPositionIncrement(u, internalPosition);
                            if (board.isPlaceValid(internalPosition2)) {

                                for (PositionColored missingPos: missingPositions) {
                                    if (internalPosition2.equals(missingPos.getPosition())) {
                                        positionFinalWithOneParcelAround = new PositionColored(internalPosition2, missingPos.getColor());
                                    } else {
                                        positionFinalWithOneParcelAround = new PositionColored(internalPosition2, objective.getColors()[0]);
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }



        if (positionFinalWithOneParcelAround != null) {
            return positionFinalWithOneParcelAround;
        }
        return null;

    }



    public static Position nextPositionIncrement(int increment, Position initialPosition){
        switch (increment) {
            case 1: return new Position(initialPosition.getX(), initialPosition.getY()+1, initialPosition.getZ()-1);
            case 2: return new Position(initialPosition.getX()+1, initialPosition.getY(), initialPosition.getZ()-1);
            case 3: return new Position(initialPosition.getX()+1, initialPosition.getY()-1, initialPosition.getZ());
            case 4: return new Position(initialPosition.getX(), initialPosition.getY()-1, initialPosition.getZ()+1);
            case 5: return new Position(initialPosition.getX()-1, initialPosition.getY(), initialPosition.getZ()+1);
            case 6: return new Position(initialPosition.getX()-1, initialPosition.getY()+1, initialPosition.getZ());
            default: return null;
        }
    }



    /**
     * Cette méthode va rechercher l'emplacement optimum ou placer le jardinier afin de résoudre au plus vite son objectif
     * @param objective
     * @param board
     * @return hmap with the first parameter is the ideal place and the set is the place where we can move the gardener
     */

    public static Map<Position, Set<Position>> findTheBestPlaceToMoveTheGardener(CardObjectiveGardener objective, Board board) {

        Layout layout = objective.getLayout();
        int sizeLayout = objective.getSize();
        Position finalPosition = null;

        Set<Position> idealPositionWhereWeCanMoveTheGardener = new HashSet<Position>();

        if (layout != null) {
            int difference = 20;
            int min = 0;
            for (Position p: board.getPositions()) {
                BambooPlantation bambooPlantation = (BambooPlantation) board.getParcel(p);
                if (bambooPlantation.isIrrigate()) {

                    for (int i = 0; i < 6; i++) {
                        Position internalPosition2 = nextPositionIncrement(i, p);
                        if (bambooPlantation.getLayout().equals(layout)) {
                            // On regarde la différence entre le nombre de bambou actuel sur la parcelle et le nombre voulu par l'objectif
                            int sizeOfAroundBamboo = board.getParcel(internalPosition2).getBambooSize();
                            int var = sizeLayout - sizeOfAroundBamboo;

                            if (var <= difference) {
                                difference = var;
                                finalPosition = internalPosition2;
                            }
                            idealPositionWhereWeCanMoveTheGardener.add(p);
                        }

                    }
                }
            }
        } else {
            // Pas de layout, on peut se placer ou on veut

            int difference = 20;

            for (Position p: board.getPositions()) {
                BambooPlantation bambooPlantation = (BambooPlantation) board.getParcel(p);
                if (bambooPlantation.isIrrigate()) {

                    for (int i = 0; i < 6; i++) {
                        Position internalPosition2 = nextPositionIncrement(i, p);
                        // On regarde la différence entre le nombre de bambou actuel sur la parcelle et le nombre voulu par l'objectif
                        int sizeOfAroundBamboo = board.getParcel(internalPosition2).getBambooSize();
                        int var = sizeLayout - sizeOfAroundBamboo;

                        if (var <= difference) {
                            difference = var;
                            finalPosition = internalPosition2;
                        }
                        idealPositionWhereWeCanMoveTheGardener.add(p);
                    }
                }
            }
        }



        Map finalHmap = new HashMap();
        finalHmap.put(finalPosition, idealPositionWhereWeCanMoveTheGardener);

        return finalHmap;

    }


    public static Set<BambooPlantation> searchTheIrrigatesPositionsInTheBoard(Board board){
        Set<BambooPlantation> irrigatePositions = new HashSet<>();

        for (Position p: board.getPositions()) {
            BambooPlantation bambooPlantation = (BambooPlantation) board.getParcel(p);
            if(board.getParcel(p).isIrrigate()) {
                irrigatePositions.add(bambooPlantation);
            }
        }

        return irrigatePositions;
    }


    /**
     * Récuperer les parcelles autour d'une parcelle irrigué afin de voir si un bambou peut etre placé dessus
     *
     */

    public static Set<Position> searchTheParcelsAroundAnIrrigateParcel(Board board) {

        Set<Position> positionsOfGoodParcels = new HashSet<Position>();
        System.out.println(board.getPositions());
        for (Position initialPosition : board.getPositions()) {
            // We browse all the map

            if (!board.getParcel(initialPosition).isPond()) {
                BambooPlantation bambooPlantation = (BambooPlantation) board.getParcel(initialPosition);


                if (bambooPlantation.isIrrigate()) {
                    for (int i = 1; i <= 6; i++) {
                        // If a parcel is irrigate, we turn arround this parcel (because the bamboo grow arround an irrigate parcel, even if this parcel is not irrigate)
                        Position loopPositionArroud = nextPositionIncrement(i, initialPosition);

                        if ((board.getPositions().contains(loopPositionArroud)) && (!loopPositionArroud.equals(Config.POND_POSITION))) {
                            BambooPlantation internalBambooPlantation = (BambooPlantation) board.getParcel(loopPositionArroud);
                            if (!internalBambooPlantation.isIrrigate()) {
                                positionsOfGoodParcels.add(loopPositionArroud);
                            }

                        }
                    }
                }
            }

        }
        return positionsOfGoodParcels;
    }


    /**
     * Return the ideal position for move the gardener or the panda
     * @param initialPosition
     * @param finalPosition
     * @param board
     * @return
     */
    public static Position findTheBestPositionForMoovingTheGardenerInLine(Position initialPosition, Position finalPosition, Board board) {

        int finalX = initialPosition.getX();
        int finalY = initialPosition.getY();
        int finalZ = initialPosition.getZ();


        // Si la parcelle finale est a droite
        if (finalPosition.getY() < initialPosition.getY()) {
            // On bouge vers la droite jusqu'a ce qu'ils aient la même valeur de y
            while (finalPosition.getY() != finalY) {
                finalX+=1;
                finalY-=1;
            }
            return new Position(finalX,finalY,finalZ);
        } else if (finalPosition.getY() > initialPosition.getY()) {
            while (finalPosition.getY() != finalY) {
                finalX-=1;
                finalY+=1;
            }
            return new Position(finalX,finalY,finalZ);
        }

        return finalPosition;
    }


}
