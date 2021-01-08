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
 * @author williamdandrea
 * @author Alexandre Arcil
 */
public class FourthBot extends Bot {

    private final int MAX_NUMBER_OF_PARCEL_OBJECTIVES = 2;
    private final int MAX_NUMBER_OF_GARDENER_OBJECTIVES = 2;
    private int trials = 0;
    int irrigationNeeded;
    private boolean blocked;
    private TurnLog turnLog;

    public FourthBot(Game game) {
        super(game);
    }

    public FourthBot(Game game, String name) {
        super(game, name);
    }

    @Override
    public void playTurn(TurnLog log, Weather weather) {
        super.playTurn(log, weather);
        turnLog = log;
        this.trials++;
        // We pick cards in order to always have 2 objectives parcels and gardener objectives
        this.fillObjectiveCards(log);
        this.fillIrrigations(log);
        this.checkAllObjectives();
        if(!this.canDoAction())
            return;
        // Now we will analyse the parcels objectives and put the more easier to the currentParcelObjective variable
        List<CardObjectiveParcel> objectiveParcelsCompletable = this.getObjectiveParcelsCompletable(log);
        List<CardObjectiveGardener> objectiveGardenersCompletable = this.getObjectiveGardenersCompletable();
        if(objectiveGardenersCompletable.isEmpty()) {
            CardObjectiveGardener cardObjectiveGardener = this.pickCompletableGardenerCard(log);
            if(cardObjectiveGardener != null)
                objectiveGardenersCompletable.add(cardObjectiveGardener);
        }
        ArrayList<CardObjective> playOrder = new ArrayList<>();
        playOrder.addAll(objectiveParcelsCompletable);
        playOrder.addAll(objectiveGardenersCompletable);
        playOrder.sort(Comparator.comparingInt(CardObjective::getScore));

        for (CardObjective cardObjective : playOrder) {
            boolean doneSomething = false;
            if(cardObjective instanceof CardObjectiveParcel)
                doneSomething = this.tryToResolveParcelObjective((CardObjectiveParcel) cardObjective, log);
            else if(cardObjective instanceof CardObjectiveGardener)
                doneSomething = this.tryToResolveGardenerObjective((CardObjectiveGardener) cardObjective, log);
            if(doneSomething) {
                this.checkAllObjectives();
                return;
            }
        }
        //Aucun objectif n'a pu être avancé
        if(this.individualBoard.countUnfinishedObjectives() == Config.MAX_NUMBER_OF_OBJECTIVES_CARD_IN_HAND &&
                playOrder.size() == 0)
            this.blocked = true;
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

    void fillIrrigations(TurnLog log) {
        if(this.irrigationNeeded > 0) {
            this.pickIrrigation(log);
            this.irrigationNeeded--;
        }
    }

    boolean hasAlreadyPickGoal() {
        return this.getLastAction() == BotActionType.PICK_GARDENER_GOAL || this.getLastAction() == BotActionType.PICK_PANDA_GOAL
                || this.getLastAction() == BotActionType.PICK_PARCEL_GOAL;
    }

    /**
     * This function will analyze all the parcels objectives and set the easier parcel objective
     */
    List<CardObjectiveParcel> getObjectiveParcelsCompletable(TurnLog log) {
        if(this.cantDoParcelObjectives())
            return Collections.emptyList();
        // We get the unfinished parcels objectives into the variable unfinishedParcelsObjectives in order to
        // choose the easiest objective
        List<CardObjectiveParcel> unfinishedParcelsObjectives = this.getIndividualBoard().getUnfinishedParcelObjectives();
        if(!unfinishedParcelsObjectives.isEmpty()) {
            // We keep only the objectives we can do
            List<CardObjectiveParcel> objectivesCompletable = unfinishedParcelsObjectives.stream().filter(this::canDoParcelObjective).collect(Collectors.toList());
            if(objectivesCompletable.isEmpty()) {
                if(!Config.isPickAction(this.getLastAction())) {
                    Optional<CardObjectiveParcel> cardObjective = this.pickParcelObjective(log);
                    if(cardObjective.isPresent()) {
                        CardObjectiveParcel cardObjectiveParcel = cardObjective.get();
                        cardObjectiveParcel.verify();
                        if(this.canDoParcelObjective(cardObjectiveParcel))
                            return Collections.singletonList(cardObjectiveParcel);
                    } //Il a déjà 5 objectifs dans son individual board
                }
            } else
                return objectivesCompletable;
        }
        return Collections.emptyList();
    }

    boolean cantDoParcelObjectives() {
        return this.board.getParcelLeftToPlace() == 0 ||
                (this.getIndividualBoard().getUnfinishedParcelObjectives().stream().noneMatch(this::canDoParcelObjective) &&
                        !this.getBoard().getDeckParcelObjective().canPick());
    }

    boolean canDoParcelObjective(CardObjectiveParcel card) {
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
     */
    List<CardObjectiveGardener> getObjectiveGardenersCompletable() {
        List<CardObjectiveGardener> unfinishedGardenersObjectives = this.getIndividualBoard().getUnfinishedGardenerObjectives();
        if(!unfinishedGardenersObjectives.isEmpty())
            return unfinishedGardenersObjectives.stream().filter(this::canDoObjectiveGardener).collect(Collectors.toList());
        return new ArrayList<>();
    }

    boolean canDoObjectiveGardener(CardObjectiveGardener card) {
        if(card.isCompleted())
            return false;
        //TODO prendre en compte les layout
        int count = 0;
        for (Position position : this.board.getPositions()) {
            Parcel parcel = this.board.getParcel(position);
            if(parcel.getBambooColor() == card.getColor()) {
                if(parcel.isIrrigate())
                    count++;
                else {
                    var path = ParcelRouteFinder.getBestPathToIrrigate(board, position);
                    if(path.isPresent()) {
                        if(path.get().size() <= this.board.getIrrigationLeft() + this.individualBoard.getNumberOfIrrigations())
                            count++;
                    }
                }
            }
        }
        count += this.board.getParcelLeftToPlace(card.getColor());
        return count >= card.getCount();
    }

    private CardObjectiveGardener pickCompletableGardenerCard(TurnLog log) {
        if(!Config.isPickAction(this.getLastAction())) {
            Optional<CardObjectiveGardener> cardObjective = this.pickGardenerObjective(log);
            if(cardObjective.isPresent()) {
                CardObjectiveGardener cardObjectiveGardener = cardObjective.get();
                cardObjectiveGardener.verify();
                if(this.canDoObjectiveGardener(cardObjectiveGardener))
                    return cardObjectiveGardener;
            } //Il a déjà 5 objectifs dans son individual board
        }
        return null;
    }

    /**
     * This function will try to resolve a parcel objective. He will place a missing
     * parcel to complete the patterns, or, if he's not in a valid place, will place a parcel to make it valid
     */
    boolean tryToResolveParcelObjective(CardObjectiveParcel card, TurnLog log) {
        Set<PositionColored> missingPositions = card.getMissingPositionsToComplete();
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
        } else  // il manque une ou plusieurs parcelles pour pouvoir poser celle du motif
            position = MarganIA.findTheBestPlaceToPlaceAnParcel(card, this.board);
        if(position != null) {//Arrive s'il ne peut plus poser de parcelles, ce qui n'arrivera jamais car cas déjà pris en compte
            List<Parcel> parcelsPicked = this.board.pickParcels(); //jamais vide (cf cantDoParcelObjectives)
            if(card.getMissingPositionsToComplete().contains(position)) { //couleur cruciale
                for (Parcel parcel : parcelsPicked) {
                    if (parcel.getBambooColor() == position.getColor())
                        return this.placeParcel(log, position.getPosition(), parcel);
                }
                return this.placeAnParcelAnywhere(log, parcelsPicked.get(0)).isPresent();
                // On a pioché aucune parcelle de la bonne couleur -> on tente un autre objectif
                //TODO choisir la couleur par rapport aux autre objectifs
            } else //on s'en fout de la couleur
                return this.placeParcel(log, position.getPosition(), parcelsPicked.get(0));
        } else
            return false;
    }

    /**
     * This function will try to resolve a gardener objective.
     */
    boolean tryToResolveGardenerObjective(CardObjectiveGardener card, TurnLog log) {
        /*
        pas parcelle avec bamboo couleur -> place une parcelle de la bonne couleur en faisant en sorte de l'irrigué
        s'il trouve parcelle -> l'irrigué s'il besoin -> allez dessus pour augmenter de un la taille des bamboos
        s'il trouve mais qu'il est dessus -> deplacez le jardinier à côté
         */
        var goodParcels = board.getPositions().stream()
                .filter(position -> {
                    Parcel parcel = this.board.getParcel(position);
                    return parcel instanceof BambooPlantation && parcel.getBambooColor().equals(card.getColor());
                })
                .collect(Collectors.toList());

        if(card.getLayout() != null && this.individualBoard.getLayouts().contains(card.getLayout())) {
            var missingLayoutParcels = goodParcels.stream()
                    .filter(position -> {
                                Parcel parcel = this.board.getParcel(position);
                                return parcel.getBambooSize() == 0 && !parcel.hasLayout();
                            }
                    ).sorted(Comparator.comparingInt(this::distanceToPond))
                    .collect(Collectors.toList());
            if(!missingLayoutParcels.isEmpty())
                this.placeLayout(log, (BambooPlantation) this.board.getParcel(missingLayoutParcels.get(0)), card.getLayout());
        }

        var incompleteBamboo = goodParcels.stream()
                .filter(position -> {
                    Parcel parcel = this.board.getParcel(position);
                    return parcel.getBambooSize() != card.getSize() && parcel.getBambooSize() >= 1
                            && parcel.getLayout() == card.getLayout();
                })
                .sorted(Comparator.comparingInt( (Position position) -> this.board.getParcel(position).getBambooSize() ))
                .collect(Collectors.toList());

        if(incompleteBamboo.isEmpty()) {
            //Cas: il manque une/aucune parcelle avec la bonne couleur, une parcelle sans bamboo car pas irrigué, ou pas de aménagement
            Position position = this.parcelWithNoBamboo(card.getColor());
            if(position == null) {
                if(this.board.getParcelCount(card.getColor()) < card.getCount()) {
                    List<Parcel> parcelsPicked = this.board.pickParcels();
                    for (Parcel parcel : parcelsPicked) {
                        if (parcel.getBambooColor() == card.getColor())
                            return this.placeParcelAnywhereAndLayout(parcel, card.getLayout(), log);//TODO placeParcelNearestPond
                    }
                    //TODO choisir la couleur par rapport aux autre objectifs
                    return this.placeParcelAnywhereAndLayout(parcelsPicked.get(0), card.getLayout(), log);
                    //On a pioché aucune parcelle de la bonne couleur -> on tente un autre objectif
                } else //Toutes les parcelles bamboo sont posées et toute on au moins 1 bamboo sur eux
                    return false;
            } else {//Une parcelle de la bonne couleur existe, mais elle n'a pas de bamboo car pas irrigué
                var pathOpt = ParcelRouteFinder.getBestPathToIrrigate(this.board, position);
                if(pathOpt.isPresent()) { //Quel cas pas présent ?
                    Set<AbsolutePositionIrrigation> path = pathOpt.get();
                    if(path.size() - this.individualBoard.getNumberOfIrrigations() == 1) {
                        if(this.getLastAction() != BotActionType.PICK_IRRIGATION && this.canDoAction())
                            this.pickIrrigation(log);
                    }
                    if(this.individualBoard.getNumberOfIrrigations() >= path.size()) {
                        var next = ParcelRouteFinder.getNextParcelToIrrigate(path);
                        while (next.isPresent()) {
                            boolean placed = this.irrigate(next.get(), log);
                            if (placed)
                                next = ParcelRouteFinder.getNextParcelToIrrigate(path);
                            else
                                return false; //placer une irrigation n'est pas compter comme une action
                        }
                        //Maintenant qu'elle est irrigué, déplaçons le jardinier
                        return this.moveGardener(this.getStepMovePosition(this.board.getGardener().getPosition(), position), log);
                    } else {
                        if(this.getLastAction() != BotActionType.PICK_IRRIGATION && this.canDoAction())
                            return this.pickIrrigation(log);
                        else {
                            this.irrigationNeeded = path.size();
                            return false;
                        }
                    }
                } else
                    return false;
            }
        } else {
            List<Position> tooSmall = incompleteBamboo.stream()
                    .filter(position -> board.getParcel(position).getBambooSize() < card.getSize())
                    .collect(Collectors.toList());
            List<Position> tooBig = incompleteBamboo.stream()
                    .filter(position -> board.getParcel(position).getBambooSize() > card.getSize() &&
                            board.getParcel(position).getLayout() != Layout.ENCLOSURE)
                    .collect(Collectors.toList());
            if(!tooSmall.isEmpty()) {
                Position pos = tooSmall.get(0); // La parcelle avec le bamboo le plus grand
                if(this.getBoard().getGardener().getPosition().equals(pos)) { // Si le jardinier est déjà dessus
                    if(tooSmall.size() >= 2)
                        this.moveGardener(this.getStepMovePosition(this.board.getGardener().getPosition(), tooSmall.get(1)), log); //on va au prochain
                    else
                        this.moveGardener(this.getStepMovePosition(this.board.getGardener().getPosition(), this.getNextPosition(pos)), log);
                } else
                    this.moveGardener(this.getStepMovePosition(this.board.getGardener().getPosition(), pos), log);
            }
            if(!tooBig.isEmpty()) {
                Position pos = tooBig.get(0); // La parcelle a un bamboo trop grande
                if(this.getBoard().getPanda().getPosition().equals(pos)) { // Si le panda est déjà dessus
                    if(tooBig.size() >= 2)
                        this.movePanda(log, this.getStepMovePosition(this.board.getPanda().getPosition(), tooBig.get(1)));
                    else {
                        List<Position> positionsToPond = ShortestPathAlgorithm.shortestPath(this.board.getPanda().getPosition(), Config.POND_POSITION, this.board);
                        if(ShortestPathAlgorithm.isLine(positionsToPond))
                            this.movePanda(log, this.getStepMovePosition(this.board.getPanda().getPosition(), Config.POND_POSITION));
                        else
                            this.movePanda(log, this.getStepMovePosition(this.board.getPanda().getPosition(), this.getNextPosition(pos)));
                    }
                } else
                    this.movePanda(log, this.getStepMovePosition(this.board.getPanda().getPosition(), pos));
            }
            if(this.canDoAction() && this.board.getParcelLeftToPlace(card.getColor()) > 0) {
                List<Parcel> parcelsPicked = this.board.pickParcels();
                for (Parcel parcel : parcelsPicked) {
                    if (parcel.getBambooColor() == card.getColor())
                        return this.placeParcelAnywhereAndLayout(parcel, card.getLayout(), log); //TODO placeParcelNearestPond
                }
                //TODO choisir la couleur par rapport aux autre objectifs
                return this.placeParcelAnywhereAndLayout(parcelsPicked.get(0), card.getLayout(), log);
            } else
                return true;
        }
    }

    private int distanceToPond(Position pos) {
        return (Math.abs(pos.getX() - Config.POND_POSITION.getX()) +
                Math.abs(pos.getY() - Config.POND_POSITION.getY()) +
                Math.abs(pos.getZ() - Config.POND_POSITION.getZ())) / 2;
    }

    private boolean placeParcelAnywhereAndLayout(Parcel parcel, Layout layout, TurnLog log) {
        Optional<Position> present = this.placeAnParcelAnywhere(log, parcel);
        if(present.isPresent()) {
            if(layout != null && !this.board.getParcel(present.get()).isIrrigate())
                this.placeLayout(log, (BambooPlantation) parcel, layout);
            return true;
        } else
            return false;
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
        return !this.blocked &&
                !this.cantDoObjectives() &&
                this.trials <= 100;
    }

    private boolean cantDoObjectives() {
        return this.individualBoard.countUnfinishedObjectives() == Config.MAX_NUMBER_OF_OBJECTIVES_CARD_IN_HAND &&
                this.individualBoard.getUnfinishedParcelObjectives().stream().noneMatch(this::canDoParcelObjective) &&
                this.individualBoard.getUnfinishedGardenerObjectives().stream().noneMatch(this::canDoObjectiveGardener);
    }

    @Override
    protected void onCloudy() {
        List<CardObjectiveGardener> objectiveGardenersCompletable = this.getObjectiveGardenersCompletable();
        objectiveGardenersCompletable.sort(Comparator.comparingInt(CardObjective::getScore));
        EnumMap<Layout, Integer> layoutNeeded = new EnumMap<>(Layout.class);
        for (CardObjectiveGardener card : objectiveGardenersCompletable) {
            Layout layout = card.getLayout();
            if(layout != null) {
                if(layoutNeeded.containsKey(layout))
                    layoutNeeded.put(layout, layoutNeeded.get(layout) + 1);
                else
                    layoutNeeded.put(layout, 1);
            }
        }
        for (Layout layout : this.individualBoard.getLayouts()) {
            if(layoutNeeded.containsKey(layout))
                layoutNeeded.put(layout, layoutNeeded.get(layout) - 1);
        }
        layoutNeeded.entrySet().removeIf(entry -> entry.getValue() <= 0);
        for (CardObjectiveGardener card : objectiveGardenersCompletable) {
            Layout layout = card.getLayout();
            if(layout != null && layoutNeeded.containsKey(layout)) {
                Optional<Layout> layoutInDeck = this.getLayoutInDeck(layout);
                if(layoutInDeck.isPresent()) {
                    this.individualBoard.addLayouts(layoutInDeck.get());
                    return;
                }
            }
        }
        this.stockRandomLayout();
    }

    Optional<Layout> getLayoutInDeck(Layout layout) {
        switch (layout) {
            case BASIN:
                return this.pickBasinLayout(turnLog);
            case FERTILIZER:
                return this.pickFertilizerLayout(turnLog);
            case ENCLOSURE:
                return this.pickEnclosureLayout(turnLog);
        }
        return Optional.empty();
    }

    void stockRandomLayout() {
        List<Layout> layouts = Arrays.asList(Layout.values());
        Collections.shuffle(layouts);
        for (Layout layout : layouts) {
            Optional<Layout> layoutInDeck = this.getLayoutInDeck(layout);
            if(layoutInDeck.isPresent()) {
                this.individualBoard.addLayouts(layoutInDeck.get());
                return;
            }
        }
        //Plus de la layout, on fait rien, même s'il faudrait choisir la météo à appliquer
    }

    @Override
    public String toString() {
        return "Bot 4";
    }

    @Override
    protected void onThunderstorm(TurnLog log) { }
}