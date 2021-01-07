package fr.matelots.polytech.core.players.bots;

import fr.matelots.polytech.core.game.Config;
import fr.matelots.polytech.core.game.Game;
import fr.matelots.polytech.core.game.Weather;
import fr.matelots.polytech.core.game.goalcards.CardObjective;
import fr.matelots.polytech.core.game.goalcards.CardObjectiveGardener;
import fr.matelots.polytech.core.game.goalcards.CardObjectiveParcel;
import fr.matelots.polytech.core.game.goalcards.pattern.PositionColored;
import fr.matelots.polytech.core.game.parcels.*;
import fr.matelots.polytech.core.players.Bot;
import fr.matelots.polytech.core.players.MarganIA;
import fr.matelots.polytech.core.players.bots.logger.BotActionType;
import fr.matelots.polytech.core.players.bots.logger.TurnLog;
import fr.matelots.polytech.engine.util.AbsolutePositionIrrigation;
import fr.matelots.polytech.engine.util.ParcelRouteFinder;
import fr.matelots.polytech.engine.util.Position;
import fr.matelots.polytech.engine.util.ShortestPathAlgorithm;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Piocher 2/3 obj parcelles et 2/3 jardinier, je choisi a l’instant t l’objectif le plus intéressant
 * et je l’accompli (un objectif a la fois) puis j’en repioche un
 *
 * - [OK] Tirer 2 objectifs jardinier       [TESTED]
 * - [OK] Tirer 2 objectifs parcelles      [TESTED]
 *
 * - [OK] Analyser les objectifs parcelle
 *     - [OK] Prendre la carte objectif jardinier ayant le pattern le plus petit
 *     - [OK] Regarder si on peut le résoudre
 * - [OK] Analyser les objectifs jardinier
 *     - [OK] Prendre la carte objectif avec le moins de bambou
 *     - [OK] Regarder si on peu le résoudre
 * - [OK] Regarder qui est le plus facile à résoudre entre les 2 objectifs choisies
 * - [OK] Si objectif parcelle plus facile à résoudre
 *     - [OK] Placer une case au bon endroit
 *     - [OK] Vérifier si l’objectif est valider
 *         - [OK] VALIDE :
 *             - [OK] On tire un nouvel objectif
 *             - [OK] On analyse a nouveau lequel est le plus facile à résoudre
 *         - [OK] NON VALIDE :
 *             - [OK] On analyse quel objectif est le plus facile à résoudre parmi la liste
 *                 - [OK] Si c’est toujours celui la, on essaye de le résoudre en rajoutant des cases
 *                 - [OK] Sinon, on passe à l’objectif le plus simple à résoudre
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
 * @author Alexandre Arcil
 */
public class FourthBot extends Bot {

    private final int MAX_NUMBER_OF_PARCEL_OBJECTIVES = 2;
    private final int MAX_NUMBER_OF_GARDENER_OBJECTIVES = 2;
    CardObjectiveParcel currentParcelObjective;
    CardObjectiveGardener currentGardenerObjective;
    int numberOfResolveObjective;
    private int trials = 0;

    public FourthBot(Game game) {
        super(game);
    }

    public FourthBot(Game game, String name) {
        super(game, name);
    }

    @Override
    public void playTurn(TurnLog log, Weather weather) {
        super.playTurn(log, weather);
        this.trials++;
        // If it is the first game launch, we pick 2 (number in parameters) parcels objectives and 2 gardener objectives
        // (number in parameters). After this brackets, we will be not in the first launch, so we will try to analyse
        // directly the objectives.
        /*if (this.firstLaunch) {
            this.pickObjectives(log);
            this.placeAnParcelAnywhere(log);
            this.firstLaunch = false;
        } else {*/
            // We pick cards in order to always have 2 objectives parcels and gardener objectives
        this.fillObjectiveCards(log);

        // Now we will analyse the parcels objectives and put the more easier to the currentParcelObjective variable
        this.analyzeParcelsObjectives(log);
        this.analyzeGardenerObjectives();

        // Now we need to compare the easiest objective to resolve
        CardObjective easiestObjective = this.easiestObjectiveToResolve();
       /* if(easiestObjective == null)
            System.out.println("easiestObjective null");*/

        //System.out.println(easiestObjective);
        // Now we will resolve the easiest objectives
        if (easiestObjective instanceof CardObjectiveParcel) {
            /*System.out.println("Trying to do a parcel objective ("+this.currentParcelObjective.getPattern()+" - "+ Arrays.toString(this.currentParcelObjective.getColors()) +")");
            System.out.println("completed: "+easiestObjective.isCompleted());*/
            // We try to resolve the objective
            if (!easiestObjective.isCompleted())
                this.tryToResolveParcelObjective(log);

            // Now we check if the objective is completed
            //System.out.println("completed: "+easiestObjective.isCompleted()+" - "+this.checkObjective(easiestObjective));
            if (this.checkObjective(easiestObjective)) {
                // We select a new parcel objective
                /*Optional<CardObjective> cardObjective = */
                this.pickParcelObjective(log);
                /*if(cardObjective.isEmpty())
                    System.out.println("NO PRESENT ?");*/
                //obj.ifPresent(cardObjectiveParcel -> log.addAction(BotActionType.PICK_PARCEL_GOAL, cardObjectiveParcel.toString()));
            }
        }

        if (easiestObjective instanceof CardObjectiveGardener) {
            //System.out.println("Trying to do a gardener objective ("+this.currentGardenerObjective.getColor()+" - "+this.currentGardenerObjective.getSize()+" - "+this.currentGardenerObjective.getCountMissing()+")");
            // We try to resolve the objective
            if (!easiestObjective.isCompleted())
                this.tryToResolveGardenerObjective(log);

            //System.out.println("completed: "+easiestObjective.isCompleted()+" - "+this.checkObjective(easiestObjective));
            // Now we check if the objective is completed
            if (this.checkObjective(easiestObjective)) {
                // We select a new parcel objective
                this.pickGardenerObjective(log);
                //obj.ifPresent(cardObjectiveGardener -> log.addAction(BotActionType.PICK_GARDENER_GOAL, cardObjectiveGardener.toString()));
            }

        }

        //}
        //log.addAction(action, actionParameter);
    }

    /**
     * This function will pick cards to always have {@link #MAX_NUMBER_OF_PARCEL_OBJECTIVES} parcel objectives and
     * {@link #MAX_NUMBER_OF_GARDENER_OBJECTIVES} gardener objectives
     */
    private void fillObjectiveCards(TurnLog log) {
        int unfinishedParcelObjectives = getIndividualBoard().countUnfinishedParcelObjectives();
        int unfinishedGardenerObjectives = getIndividualBoard().countUnfinishedGardenerObjectives();
        for(int i = unfinishedParcelObjectives; i < MAX_NUMBER_OF_PARCEL_OBJECTIVES; i++) {
            if(!this.hasAlreadyPickGoal())
                this.pickParcelObjective(log);
        }
        for(int i = unfinishedGardenerObjectives; i < MAX_NUMBER_OF_GARDENER_OBJECTIVES; i++) {
            if(!this.hasAlreadyPickGoal())
                this.pickGardenerObjective(log);
        }
    }

    private boolean hasAlreadyPickGoal() {
        return this.getLastAction() == BotActionType.PICK_GARDENER_GOAL || this.getLastAction() == BotActionType.PICK_PANDA_GOAL
                || this.getLastAction() == BotActionType.PICK_PARCEL_GOAL;
    }

    /**
     * This function check the current objective
     * @return true if the currentObjective is in progress or false if there is any currentObjective or if
     * the currentObjective is finish
     */
    private boolean checkObjective(CardObjective objective) {
        if(objective != null) {
            if(objective.isCompleted()) {
                this.numberOfResolveObjective++;
                return true;
            }
        }
        return false;
    }

    /**
     * This function will analyze all the parcels objectives and set the easier parcel objective
     * to {@link #currentParcelObjective}
     */
    private void analyzeParcelsObjectives(TurnLog log) {
        if(this.cantDoParcelObjectives()) {
            this.currentParcelObjective = null;
            return;
        }
        // We get the unfinished parcels objectives into the variable unfinishedParcelsObjectives in order to
        // choose the easiest objective
        List<CardObjectiveParcel> unfinishedParcelsObjectives = this.getIndividualBoard().getUnfinishedParcelObjectives();
        if(!unfinishedParcelsObjectives.isEmpty()) {
            // We verify them
            for (CardObjectiveParcel unfinishedParcelsObjective : unfinishedParcelsObjectives) {
                unfinishedParcelsObjective.verify();
                /*if(unfinishedParcelsObjective.isCompleted())
                    System.out.println("Objective parcel completed ! +"+unfinishedParcelsObjective.getScore());*/
            }
            // We keep only the objectives we can do
            List<CardObjectiveParcel> objectivesCompletable = unfinishedParcelsObjectives.stream().filter(this::canDoParcelObjective).collect(Collectors.toList());
            if(objectivesCompletable.isEmpty()) {
                for(int i = 0; i < Config.TOTAL_NUMBER_OF_ACTIONS; i++) {
                    Optional<CardObjective> cardObjective = this.pickParcelObjective(log);
                    if(cardObjective.isPresent()) {
                        if(this.canDoParcelObjective((CardObjectiveParcel) cardObjective.get()))
                            this.currentParcelObjective = (CardObjectiveParcel) cardObjective.get();
                    } else
                        this.currentParcelObjective = null;
                }
            } else {
                // To determine if a parcel objective is easy to resolve, we will count the number of parcels the objectives need
                // Less this number is, more easy the objective will be resolve
                objectivesCompletable.sort(Comparator.comparingInt(objective -> objective.getMissingPositionsToComplete().size()));
                this.currentParcelObjective = objectivesCompletable.get(0);
            }
        }
    }

    private boolean cantDoParcelObjectives() {
        return !this.canDoParcelObjectives() ||
                (this.getIndividualBoard().getUnfinishedParcelObjectives().stream().noneMatch(this::canDoParcelObjective) &&
                        !this.getBoard().getDeckParcelObjective().canPick());
    }

    private boolean canDoParcelObjective(CardObjectiveParcel card) {
        if(card.getMissingPositionsToComplete() == null)
            return true;
        EnumMap<BambooColor, Integer> colorCount = new EnumMap<>(BambooColor.class);
        card.getMissingPositionsToComplete().stream().map(PositionColored::getColor).forEach(color -> {
            if(colorCount.containsKey(color))
                colorCount.put(color, colorCount.get(color) + 1);
            else
                colorCount.put(color, 1);
        });
        return colorCount.entrySet().stream().noneMatch(entry -> this.board.getParcelLeftToPlace(entry.getKey()) < entry.getValue());
    }

    /**
     * This function will analyze all the gardeners objectives and set the easier gardener objective
     * to {@link #currentGardenerObjective}
     */
    private void analyzeGardenerObjectives() {
        List<CardObjectiveGardener> unfinishedGardenersObjectives = this.getIndividualBoard().getUnfinishedGardenerObjectives();
        if(!unfinishedGardenersObjectives.isEmpty()) {
            // pas de parcelle de la bonne couleur
            // parcelle bonne couleur mais pas irrigué
            for (CardObjectiveGardener unfinishedGardenersObjective : unfinishedGardenersObjectives) {
                unfinishedGardenersObjective.verify();
                /*if(unfinishedGardenersObjective.isCompleted())
                    System.out.println("Objective parcel completed ! +"+unfinishedGardenersObjective.getScore());*/
            }
            //List<CardObjectiveGardener> objectivesCompletable = unfinishedGardenersObjectives.stream().filter(this::canDoGardenerObjective).collect(Collectors.toList());
            unfinishedGardenersObjectives.sort(Comparator.comparingInt(CardObjectiveGardener::getCountMissing));
            this.currentGardenerObjective = unfinishedGardenersObjectives.get(0);
        }
    }

    /**
     * This function will compare {@link #currentParcelObjective} and {@link #currentGardenerObjective} to determine
     * witch is the easiest to resolve. The easier is the objective with the lowest score.
     */
    CardObjective easiestObjectiveToResolve() {
        if(this.currentParcelObjective == null || !this.canDoParcelObjectives())
            return this.currentGardenerObjective;
        else if(this.currentGardenerObjective == null)
            return this.currentParcelObjective;
        else if (currentParcelObjective.getScore() <= currentGardenerObjective.getScore())
            return currentParcelObjective;
        else
            return currentGardenerObjective;
    }

    /**
     * This function will try to resolve the parcel objective {@link #currentParcelObjective}. He will place a missing
     * parcel to complete the patterns, or, if he's not in a valid place, will place a parcel to make it valid
     */
    void tryToResolveParcelObjective(TurnLog log) {



        // We check if the game board is just composed of the pond (etang) or if we have more parcels
        /*if (board.getParcelCount() == 1) {
            // We need to place a parcel anywhere in the game board
            this.placeAnParcelAnywhere(this.currentParcelObjective.getMissingPositionsToComplete().iterator().next().getColor(), log);
        } else*//* if (this.currentParcelObjective != null)*/ //{ // We check the place where we can place a new parcel
        Set<PositionColored> missingPositions = this.currentParcelObjective.getMissingPositionsToComplete();
        ArrayList<PositionColored> positionsWeChoose = new ArrayList<>();
        PositionColored position;

        // We browse all the place where we can place a parcel and we add this positions to the ArrayList positionsWeChoose
        missingPositions.stream()
                .filter(posColor -> board.isPlaceValid(posColor.getPosition()))
                .forEach(positionsWeChoose::add);
        // We have an place to put the new parcel

        if (!positionsWeChoose.isEmpty()) {
            // We choose a random parcel in the potential list
            int index = random.nextInt(positionsWeChoose.size());
            position = positionsWeChoose.get(index);

            // We add the new parcel

        } else { // il manque une ou plusieurs parcelles pour pouvoir poser celle du motif
            position = MarganIA.findTheBestPlaceToPlaceAnParcel(this.currentParcelObjective, this.board);
                /*if(missingPos != null)
                    this.placeParcel(missingPos.getPosition(), missingPos.getColor(), log);
                else
                    System.out.println("MISSING POS NULL...");*/
        }
        if(position != null) //Arrive s'il ne peut plus poser de parcelles, ce qui n'arrivera jamais car cas déjà pris en compte
            this.placeParcel(position.getPosition(), position.getColor(), log);
        //} else {
            /*// We put a parcel anywhere
            this.placeAnParcelAnywhere(log);
            //position.ifPresent(position1 -> log.addAction(BotActionType.PLACE_PARCEL, position1.toString()));
        }*/
            /*Set<PositionColored> missingPositions = null;
            if (this.currentParcelObjective != null) {
                missingPositions = this.currentParcelObjective.getMissingPositionsToComplete();
            }
            ArrayList<PositionColored> positionsWeChoose = new ArrayList<>();


            // We browse all the place where we can place a parcel and we add this positions to the ArrayList positionsWeChoose
            if (missingPositions != null)
                missingPositions.stream()
                        .filter(posColor -> board.isPlaceValid(posColor.getPosition()))
                        .forEach(positionsWeChoose::add);

            if(positionsWeChoose.size() != 0) {
                // We have an place to put the new parcel

                // We choose a random parcel in the potential list
                Random randomNumber = new Random();
                int position = randomNumber.nextInt(positionsWeChoose.size());
                PositionColored positionColored = positionsWeChoose.get(position);

                // We add the new parcel
                board.addParcel(positionColored.getPosition(), new BambooPlantation(positionColored.getColor()));

                log.addAction(BotActionType.PLACE_PARCEL, positionsWeChoose.get(position).toString());
            } else {
                // We put a parcel anywhere
                var position = placeAnParcelAnywhere(log);

                position.ifPresent(position1 -> log.addAction(BotActionType.PLACE_PARCEL, position1.toString()));
            }*/
    }

    /**
     * This function will try to resolve the gardener objective {@link #currentGardenerObjective}.
     */
    void tryToResolveGardenerObjective(TurnLog log) {
        /*
        pas parcelle avec bamboo couleur -> place une parcelle de la bonne couleur en faisant en sorte de l'irrigué
        s'il trouve parcelle -> l'irrigué s'il besoin -> allez dessus pour augmenter de un la taille des bamboos
        s'il trouve mais qu'il est dessus -> deplacez le jardinier à côté
         */
        var positions = board.getPositions().stream()
                .filter(pos ->
                        !board.getParcel(pos).isPond() &&
                                board.getParcel(pos).getBambooColor().equals(currentGardenerObjective.getColor()) &&
                                board.getParcel(pos).getBambooSize() != currentGardenerObjective.getSize() &&
                                board.getParcel(pos).getBambooSize() >= 1)
                .sorted(Comparator.comparingInt( (Position p) -> board.getParcel(p).getBambooSize() ))
                .collect(Collectors.toList());

        //System.out.println("position empty: "+positions.isEmpty());
        //BoardDrawer drawer = new BoardDrawer(this.board);
        //drawer.print();
        if(positions.isEmpty()) {
            //Cas: aucune parcelle avec la bonne couleur, ou une parcelle sans bamboo car pas irrigué
            Position position = this.parcelWithNoBamboo(this.currentGardenerObjective.getColor());
            if(position == null)
                this.placeAnParcelAnywhere(currentGardenerObjective.getColor(), log);
            else {//Une parcelle de la bonne couleur existe, mais elle n'a pas de bamboo car pas irrigué
                var pathOpt = ParcelRouteFinder.getBestPathToIrrigate(this.board, position);
                if(pathOpt.isPresent()) { //Quel cas pas présent ?
                    Set<AbsolutePositionIrrigation> path = pathOpt.get();
                    var next = ParcelRouteFinder.getNextParcelToIrrigate(path);
                    while (next.isPresent()) {
                        this.individualBoard.addIrrigation();
                        boolean placed = this.irrigate(next.get(), log);
                        if(placed)
                            next = ParcelRouteFinder.getNextParcelToIrrigate(path);
                        else
                            return;
                    }
                    this.moveGardenerByStep(position, log); //Maintenant qu'elle est irrigué, déplaçons le jardinier
                } else {
                    //Il arrive pas à trouve de chemin car pas en ligne droite
                }
            }
        } else if(this.canDoAction()) {
            List<Position> tooSmall = positions.stream()
                    .filter(position -> board.getParcel(position).getBambooSize() < currentGardenerObjective.getSize())
                    .collect(Collectors.toList());
            List<Position> tooBig = positions.stream()
                    .filter(position -> board.getParcel(position).getBambooSize() > currentGardenerObjective.getSize() &&
                            ((BambooPlantation) board.getParcel(position)).getLayout() != Layout.ENCLOSURE)
                    .collect(Collectors.toList());
            if(!tooSmall.isEmpty()) {
                Position pos = tooSmall.get(0); // La parcelle avec le bamboo le plus grand
                if(this.getBoard().getGardener().getPosition().equals(pos)) { // Si le jardinier est déjà dessus
                    if(tooSmall.size() >= 2)
                        this.moveGardenerByStep(tooSmall.get(1), log); //on va au prochain
                    else
                        this.moveGardenerByStep(this.getNextPosition(pos), log);
                } else
                    this.moveGardenerByStep(pos, log);
            }
            if(!tooBig.isEmpty()) {
                Position pos = tooBig.get(0); // La parcelle a un bamboo trop grande
                if(this.getBoard().getPanda().getPosition().equals(pos)) { // Si le panda est déjà dessus
                    if(tooBig.size() >= 2)
                        this.movePandaByStep(log, tooBig.get(1));
                    else {
                        List<Position> positionsToPond = ShortestPathAlgorithm.shortestPath(this.board.getPanda().getPosition(), Config.POND_POSITION, this.board);
                        if(ShortestPathAlgorithm.isLine(positionsToPond))
                            this.movePandaByStep(log, Config.POND_POSITION);
                        else
                            this.movePandaByStep(log, this.getNextPosition(pos));
                    }
                } else
                    this.movePandaByStep(log, pos);
            }
            if(tooBig.isEmpty() && tooSmall.isEmpty())
                this.placeAnParcelAnywhere(this.currentGardenerObjective.getColor(), log);
        }
    }

    public boolean moveGardenerByStep(Position position, TurnLog log) {
        List<Position> movePositions = this.getMovePositions(this.board.getGardener().getPosition(), position);
        for (Position movePosition : movePositions) {
            boolean success = this.moveGardener(movePosition, log);
            if(!success)
                return false;
        }
        return true;
/*

        List<Position> positions = ShortestPathAlgorithm.shortestPath(this.board.getGardener().getPosition(), position, this.board);
        if(positions.size() == 1)
            return false; //Bouger au même endroit !?
        else {
            Side side = Side.getTouchedSide(positions.get(0), positions.get(1));
            for(int i = 2; i < positions.size(); i++) {
                if(Side.getTouchedSide(positions.get(i - 1), positions.get(i)) != side)
                    return super.moveGardener(positions.get(i - 1), log);
            }
            return super.moveGardener(position, log);
        }*/
    }

    protected boolean movePandaByStep(TurnLog log, Position position) {
        List<Position> movePositions = this.getMovePositions(this.board.getPanda().getPosition(), position);
        for (Position movePosition : movePositions) {
            boolean success = this.movePanda(log, movePosition);
            if(!success)
                return false;
        }
        return true;
    }

    private List<Position> getMovePositions(Position start, Position goal) {
        List<Position> path = ShortestPathAlgorithm.shortestPath(start, goal, this.board);
        if(path.size() == 1 || path.size() == 2) {
            if(path.size() == 2)
                path.remove(start);
            return path;
        }
        List<Position> points = new ArrayList<>();
        Side sideDirection = Side.getTouchedSide(path.get(0), path.get(1));
        for(int i = 2; i < path.size(); i++) {
            Side side = Side.getTouchedSide(path.get(i - 1), path.get(i));
            if(side != sideDirection) {
                points.add(path.get(i - 1));
                sideDirection = side;
            }
        }
        points.add(goal);
        return points;
    }

    private Position getNextPosition(Position position) {
        Position posTmp;
        for(Side side : Side.values()) {
            posTmp = position.add(side.getDirection());
            if(this.board.containTile(posTmp))
                return posTmp;
        }
        return null;
    }

    private Position parcelWithNoBamboo(BambooColor color) {
        Parcel parcel;
        for(Position position : this.board.getPositions()) {
            parcel = this.board.getParcel(position);
            if(parcel.getBambooSize() == 0 && parcel.getBambooColor() == color)
                return position;
        }
        return null;
    }

    @Override
    public boolean canPlay() {
        return (this.canDoParcelObjectives() && this.canDoGardenerObjectives()) && this.trials <= 100;
    }

    private boolean canDoParcelObjectives() {
        return this.board.getParcelLeftToPlace() != 0;
    }

    private boolean canDoGardenerObjectives() {
        return true;
    }

    public int getNumberOfParcelObjectivesAtTheStart() {
        return MAX_NUMBER_OF_PARCEL_OBJECTIVES;
    }

    public int getNumberOfGardenerObjectivesAtTheStart() {
        return MAX_NUMBER_OF_GARDENER_OBJECTIVES;
    }

    @Override
    public String toString() {
        return "Bot 4";
    }

    public int getTrials() {
        return trials;
    }
}