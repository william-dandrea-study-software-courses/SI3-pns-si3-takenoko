package fr.matelots.polytech.core.game.goalcards.pattern;

import fr.matelots.polytech.engine.util.Position;

import java.util.HashSet;
import java.util.Set;

/**
 * @author Alexandre Arcil
 * Cherche le motif dans des positions. Il va prendre une position de départ et calculer les positions à partir des offsets.
 * Il cherche toujours la position de départ qui nécessitera de placer le moins de parcelles possibles pour avoir le
 * motif. S'il l'étang est dans les positions pour avoir le motif, il va prendre un autre offset, d'où le double
 * tableau.
 */
public class PatternChecker {

    private static final Position POND = new Position(0, 0, 0);
    private final Position[][] offsets;

    public PatternChecker(Position... offsets) {
        this.offsets = new Position[offsets.length + 1][offsets.length];
        this.offsets[0] = offsets;
        //Calcul les autres offsets, si jamais on tombe sur l'étang avec celui de base
        for(int i = 1; i < this.offsets.length; i++) {
            Position[] offsetSource = new Position[offsets.length];
            offsetSource[0] = this.offsets[0][i - 1].mul(-1);
            for(int j = 1; j < offsets.length + 1; j++) {
                if(j != i) {
                    int arrayPos = j > i ? j - 1 : j;
                    offsetSource[arrayPos] = this.offsets[0][arrayPos].add(offsetSource[0]);
                }
            }
            this.offsets[i] = offsetSource;
        }
    }

    /**
     * Check si le motif est contenue dans @param positionSet.
     *
     * @param positions Les positions
     * @return La liste des positions manquantes pour compléter le motif. Vide s'il le trouve.
     * @throws IllegalArgumentException si @param positions est vide ou contient seulement l'étang
     */
    public Set<Position> check(Set<Position> positions) {
        positions.removeIf(pos -> pos.equals(POND)); //On enlève l'étang
        if (positions.isEmpty())
            throw new IllegalArgumentException("Aucune position !");
        Set<Position> patternPos = new HashSet<>(); //Les positions manquantes pour compléter l'objectif. En contient le moins possible
        boolean firstLoop = true; //Pour initialiser patternPos lors de la première execution de la boucle
        Set<Position> posBuffer = new HashSet<>(); //Va contenir les positions manquantes par rapport à la position actuellement exécutés dans la boucle
        Position[] offsetsBuffer = new Position[this.offsets.length]; //Buffer des position après avoir appliqué un offset
        int offsetPos = 0;
        for (Position position : positions) {
            for(Position[] offsetSource : this.offsets) {
                for(Position offset : offsetSource) {
                    offsetsBuffer[offsetPos] = position.add(offset);
                    if (!positions.contains(offsetsBuffer[offsetPos]))
                        posBuffer.add(offsetsBuffer[offsetPos]);
                    offsetPos++;
                }
                if(!posBuffer.contains(POND))
                    break;
                offsetPos = 0;
                posBuffer.clear();
            }
            if (firstLoop) {
                patternPos.addAll(posBuffer);
                firstLoop = false;
            } else if (posBuffer.size() < patternPos.size()) {
                patternPos.clear();
                patternPos.addAll(posBuffer);
            }
            posBuffer.clear();
            offsetPos = 0;
        }
        return patternPos;
    }

}
