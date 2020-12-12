package fr.matelots.polytech.core.game.goalcards.pattern;

import fr.matelots.polytech.core.game.Config;
import fr.matelots.polytech.core.game.parcels.BambooColor;
import fr.matelots.polytech.core.game.parcels.Side;
import fr.matelots.polytech.engine.util.Position;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author Alexandre Arcil
 * Cherche le motif dans des positions. Il va prendre une position de départ et calculer les positions à partir des offsets.
 * Il cherche toujours la position de départ qui nécessitera de placer le moins de parcelles possibles pour avoir le
 * motif. S'il l'étang est dans les positions pour avoir le motif, il va prendre un autre offset, d'où le double
 * tableau.
 */
public class PatternChecker {

    private static final PositionColored POND = new PositionColored(Config.POND_POSITION, null);
    private final Position[][] offsets;

    public PatternChecker(Position... offsets) {
        this.offsets = new Position[offsets.length + 1][offsets.length];
        this.offsets[0] = offsets;
        int k = 1;
        //Calcul les autres offsets, si jamais on tombe sur l'étang avec celui de base
        for(int i = 1; i < this.offsets.length; i++) {
            Position[] offsetSource = new Position[offsets.length];
            offsetSource[0] = this.offsets[0][i - 1].mul(-1);
            for(int j = 1; j < offsets.length; j++) {
                offsetSource[j] = offsetSource[0].add(this.offsets[0][k]);
                if(offsets.length == 2 || j + 1 != offsets.length)
                    k = k == offsets.length - 1 ? 0 : k + 1;
            }
            this.offsets[i] = offsetSource;
        }
    }

    /**
     * Check si le motif est contenue dans @param positionSet.
     *
     * @param positions Les positions
     * @param colors Les couleurs que doivent avoir les parcelles
     * @return La liste des positions manquantes pour compléter le motif. Vide s'il le trouve.
     * @throws IllegalArgumentException si @param positions est vide ou contient seulement l'étang
     */
    public Set<PositionColored> check(Set<PositionColored> positions, BambooColor[] colors) {
        //positions.removeIf(pos -> pos.getPosition().equals(Config.POND_POSITION)); //On enlève l'étang
        if (!positions.contains(POND))
            throw new IllegalArgumentException("Il manque l'étang !");
        if(colors.length - 1 != this.offsets[0].length)
            throw new IllegalArgumentException("Le nombre de couleurs est différents du nombre de parcelles du motif");

        Map<PositionColored, Integer> posDistance = this.positionDistances(positions);
        int maxRadius = this.maxDistance(positions) + 1;
        Set<PositionColored> patternPos = new HashSet<>();
        Set<PositionColored> results;
        Position cube = POND.getPosition().add(Side.BOTTOM_LEFT.getDirection());
        Side side = Side.BOTTOM_RIGHT;

        for(int radius = 1; radius <= maxRadius; radius++) { //algo "Spiral Rings" (redblobgames)
            Set<PositionColored> posColors = this.getPositionDistant(posDistance, radius);
            for (int i = 0; i < 6; i++) { //On tourne autour du centre à un certaine radius. Signe Ring sur redblobgames
                side = side.leftSide();
                for (int j = 0; j < radius; j++) {
                    PositionColored posColor = this.getPositionColoredAt(posColors, cube);
                    if(posColor != null)  //Une parcelle déjà posé
                        results = this.checkPattern(cube, posColor.getColor(), positions, colors);
                    else//Case libre, il suffit de vérifier qu'on peut avoir le motif
                        results = this.checkPattern(cube, null, positions, colors);
                    if(results != null) {
                        if(results.isEmpty())
                            return results; //motif trouvé !
                        if(patternPos.size() == 0 || results.size() < patternPos.size()) {
                            patternPos.clear();
                            patternPos.addAll(results);
                        } //meilleur position de départ trouvé
                    }
                    if(j + 1 != radius) //sauf la dernière execution de la boucle
                        cube = cube.add(side.getDirection());
                }
                cube = cube.add(side.getDirection());
            }
            cube = cube.add(Side.BOTTOM_LEFT.getDirection());
        }
        return patternPos.isEmpty() ? null : patternPos;
    }

    /**
     * Check si le motif peut être trouvé à la position <code>pos</code>, ou s'il peut l'être.
     * @param pos Le position de départ
     * @param posColor La couleur de la position de départ. Peut être null.
     * @param positions Les positions sur le plateau
     * @param colors Les couleurs que doivent avoir les parcelles du motif
     * @return Les position manquantes pour compléter le motif, ou null si impossible à cette position.
     */
    private Set<PositionColored> checkPattern(Position pos, BambooColor posColor, Set<PositionColored> positions, BambooColor[] colors) {
        Set<PositionColored> patternPos = new HashSet<>();
        boolean notFoundAlready = true;
        Set<PositionColored> posBuffer = new HashSet<>(); //Va contenir les positions manquantes par rapport à la position actuellement exécutés dans la boucle
        PositionColored[] offsetsBuffer = new PositionColored[this.offsets.length]; //Buffer des position après avoir appliqué un offset
        int offsetPos = 0;
        int currentColor = 0;
        int posStart = 0;
        for(Position[] offsetSource : this.offsets) {
            currentColor = posStart;
            posStart++;
            checkPossiblePattern: {
                if (posColor == null || posColor == colors[currentColor]) { //la parcelle de départ est de la bonne couleur
                    if(posColor == null)
                        posBuffer.add(new PositionColored(pos, colors[currentColor]));
                    currentColor = currentColor == offsetSource.length ? 0 : currentColor + 1;
                    for (Position offset : offsetSource) {
                        offsetsBuffer[offsetPos] = new PositionColored(pos.add(offset), colors[currentColor]);
                        PositionColored positionColoredAt = this.getPositionColoredAt(positions, offsetsBuffer[offsetPos].getPosition());
                        if (positionColoredAt == null) //la case est libre, on pourra placer une parcelle pour compléter le motif
                            posBuffer.add(offsetsBuffer[offsetPos]);
                        else if (positionColoredAt.getColor() != offsetsBuffer[offsetPos].getColor()) {
                            //la case contient une parcelle d'une autre couleur, on ne peut pas compléter le motif
                            break checkPossiblePattern;
                        }
                        offsetPos++;
                        currentColor = currentColor == offsetSource.length ? 0 : currentColor + 1;
                    }
                    //Si on est là, c'est que soit le motif est trouvé, soit il est incomplet
                    if (!posBuffer.contains(POND)) { //le motif ne doit pas contenir l'étang
                        if(posBuffer.isEmpty()) //motif trouvé !
                            return posBuffer;
                        else if(notFoundAlready) {//motif partiellement trouvé
                            patternPos.addAll(posBuffer);
                            notFoundAlready = false;
                        } else if(posBuffer.size() < patternPos.size()) {//moins de parcelles à posées
                            patternPos.clear();
                            patternPos.addAll(posBuffer);
                        }
                    }
                }
            }
            offsetPos = 0;
            posBuffer.clear();
        }
        return patternPos.isEmpty() ? null : patternPos;
    }

    private int maxDistance(Set<PositionColored> positions) {
        return positions.stream().mapToInt(this::positionDistance).max().orElse(0);
    }

    private PositionColored getPositionColoredAt(Set<PositionColored> posColors, Position pos) {
        for(PositionColored posColor : posColors) {
            if(posColor.getPosition().equals(pos))
                return posColor;
        }
        return null;
    }

    private Set<PositionColored> getPositionDistant(Map<PositionColored, Integer> posDist, int radius) {
        return posDist.entrySet().stream().filter(entry -> entry.getValue() == radius)
                .map(Map.Entry::getKey).collect(Collectors.toSet());
    }

    private Map<PositionColored, Integer> positionDistances(Set<PositionColored> positions) {
        Map<PositionColored, Integer> positionsDistance = new HashMap<>();
        for(PositionColored position : positions)
            positionsDistance.put(position, this.positionDistance(position));
        return positionsDistance;
    }

    private int positionDistance(PositionColored positionColored) {
        Position position = positionColored.getPosition();
        Position pondPosition = POND.getPosition();
        return (Math.abs(position.getX() - pondPosition.getX()) + Math.abs(position.getY() - pondPosition.getY()) +
                Math.abs(position.getZ() - pondPosition.getZ())) / 2;
    }

}
