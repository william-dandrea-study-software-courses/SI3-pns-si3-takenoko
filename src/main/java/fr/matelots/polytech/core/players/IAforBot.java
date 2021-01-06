package fr.matelots.polytech.core.players;

import fr.matelots.polytech.core.game.Board;
import fr.matelots.polytech.core.game.Config;
import fr.matelots.polytech.core.game.goalcards.CardObjectiveParcel;
import fr.matelots.polytech.core.game.goalcards.pattern.PositionColored;
import fr.matelots.polytech.engine.util.Position;

import java.util.*;

public class IAforBot {

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
        Set<Position> actualGrid = board.getPositions();
        objective.verify();

        PositionColored positionFinalWithOneParcelAround = null;
        Map<PositionColored, Integer> candidateParcels = new HashMap<PositionColored, Integer>();
        Set<PositionColored> missingPositions = objective.getMissingPositionsToComplete();
        //System.out.println("missingPosition : " + missingPositions);


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



}
