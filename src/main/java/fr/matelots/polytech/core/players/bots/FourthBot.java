package fr.matelots.polytech.core.players.bots;

import fr.matelots.polytech.core.game.Game;
import fr.matelots.polytech.core.players.Bot;

/**
 * Piocher 2/3 obj parcelles et 2/3 jardinier, je choisi a l’instant t l’objectif le plus intéressant
 * et je l’accompli (un objectif a la fois) puis j’en repioche un
 *
 * - [OK] Tirer 2 objectifs jardinier
 * - [OK] Tirer 2 objectifs parcelles 
 * 
 * - [ ] Analyser les objectifs parcelle
 *     - [ ] Prendre la carte objectif jardinier ayant le pattern le plus petit
 *     - [ ] Regarder si on peut le résoudre
 * - [ ] Analyser les objectifs jardinier
 *     - [ ] Prendre la carte objectif avec le moins de bambou
 *     - [ ] Regarder si on peu le résoudre  
 * - [ ] Regarder qui est le plus facile à résoudre entre les 2 objectifs choisies  
 * - [ ] Si objectif parcelle plus facile à résoudre
 *     - [ ] Placer une case au bon endroit
 *     - [ ] Vérifier si l’objectif est valider
 *         - [ ] VALIDE :
 *             - [ ] On tire un nouvel objectif
 *             - [ ] On analyse a nouveau lequel est le plus facile à résoudre
 *         - [ ] NON VALIDE :
 *             - [ ] On analyse quel objectif est le plus facile à résoudre parmi la liste
 *                 - [ ] Si c’est toujours celui la, on essaye de le résoudre en rajoutant des cases
 *                 - [ ] Sinon, on passe à l’objectif le plus simple à résoudre 
 * - [ ] Si objectif jardinier plus facile à résoudre
 *     - [ ] Placer le jardinier au bon endroit (verifier si la case est valide, et quand il arrive il peut faire pousser un bambou)
 *     - [ ] Vérifier si l’objectif est valider
 *         - [ ] VALIDE :
 *             - [ ] On tire un nouvel objectif
 *             - [ ] On analyse a nouveau lequel est le plus facile à résoudre
 *         - [ ] NON VALIDE :
 *             - [ ] On analyse quel objectif est le plus facile à résoudre parmi la liste
 *                 - [ ] Si c’est toujours celui la, on essaye de le résoudre en rajoutant des cases
 *                 - [ ] Sinon, on passe à l’objectif le plus simple à résoudre
 *
 * @author williamdandrea
 */
public class FourthBot extends Bot {

    private static final int NUMBER_OF_PARCEL_OBJECTIVES_AT_THE_START = 2;
    private static final int NUMBER_OF_GARDENER_OBJECTIVES_AT_THE_START = 2;
    private boolean firstLaunch = true;

    public FourthBot(Game game) {
        super(game);
    }

    @Override
    public void playTurn() {

        // If it is the first game launch, we pick 2 (number in parameters) parcels objectives and 2 gardener objectives
        // (number in parameters). After this brackets, we will be not in the first launch, so we will try to analyse
        // directly the objectives.
        if (firstLaunch) {
            firstLaunchPickObjectives();
            firstLaunch = false;
        } else {

        }

    }

    @Override
    public boolean canPlay() { return false; }

    /**
     * This function pick an new Parcel objective and add this objective to the player deck
     */
    void pickAnParcelObjectiveAndAddToPlayerBoard() {
        pickParcelObjective();
    }

    /**
     * This function pick an new Gardener objective and add this objective to the player deck
     */
    void pickAnGardenerObjectiveAndAddToPlayerBoard() {
        pickGardenerObjective();
    }

    /**
     * If it is the first game launch, we pick 2 (number in parameters) parcels objectives and 2 gardener objectives
     * (number in parameters)
     */
    void firstLaunchPickObjectives() {
        for (int i = 0; i < NUMBER_OF_PARCEL_OBJECTIVES_AT_THE_START ; i++) {
            pickAnParcelObjectiveAndAddToPlayerBoard();
        }
        for (int i = 0; i < NUMBER_OF_PARCEL_OBJECTIVES_AT_THE_START ; i++) {
            pickAnGardenerObjectiveAndAddToPlayerBoard();
        }
    }


}
