package fr.matelots.polytech.core.game.goalcards.pattern;

import fr.matelots.polytech.engine.util.Position;

import java.util.HashSet;
import java.util.Set;

/**
 * @author Alexandre Arcil
 */
public class PatternChecker {

    private final Position[] offsets;

    public PatternChecker(Position... offsets) {
        this.offsets = offsets;
    }

    /**
     * Check si le motif est contenue dans @param positionSet.
     * @param positions Les positions
     * @return La liste des positions manquantes pour compléter le motif. Vide s'il le trouve.
     * @throws IllegalArgumentException si @param positions est vide
     */
    public Set<Position> check(Set<Position> positions) {
        positions.removeIf(pos -> pos.getX() == 0 && pos.getY() == 0 && pos.getZ() == 0); //On enlève l'étang
        if(positions.isEmpty())
            throw new IllegalArgumentException("Aucune position !");
        Set<Position> patternPos = new HashSet<>(); //Les positions manquantes pour compléter l'objectif. En contient le moins possible
        boolean firstLoop = true; //Pour initialiser patternPos lors de la première execution de la boucle
        Set<Position> posBuffer = new HashSet<>(); //Va contenir les positions manquantes par rapport à la position actuellement exécutés dans la boucle
        Position[] offsetsBuffer = new Position[this.offsets.length]; //Buffer des position après avoir appliqué un offset
        int offsetPos = 0;
        for(Position position : positions) {
            for(Position offset : offsets) {
                offsetsBuffer[offsetPos] = position.add(offset);
                if(!positions.contains(offsetsBuffer[offsetPos]))
                    posBuffer.add(offsetsBuffer[offsetPos]);
               offsetPos++;
            }
            if(firstLoop) {
                patternPos.addAll(posBuffer);
                firstLoop = false;
            } else if(posBuffer.size() < patternPos.size()) {
                patternPos.clear();
                patternPos.addAll(posBuffer);
            }
            posBuffer.clear();
            offsetPos = 0;
        }
        return patternPos;
    }

}
