package fr.matelots.polytech.core.game.deck;

import fr.matelots.polytech.core.game.Board;
import fr.matelots.polytech.core.game.Config;
import fr.matelots.polytech.core.game.goalcards.CardObjectiveGardener;
import fr.matelots.polytech.core.game.parcels.BambooColor;

/**
 * @author Alexandre Arcil
 */
public class DeckGardenerObjective extends DeckObjective<CardObjectiveGardener> {

    public DeckGardenerObjective(Board board) {
        super(board);
        for(BambooColor bambooColor : BambooColor.values()) {
            for(int i = 0; i < 4; i++)
                this.objectives.add(new CardObjectiveGardener(board, 5, bambooColor, 4, 1));
        }
        this.objectives.add(new CardObjectiveGardener(board, 6, BambooColor.pink, 3, 2));
        this.objectives.add(new CardObjectiveGardener(board, 7, BambooColor.yellow, 3, 3));
        this.objectives.add(new CardObjectiveGardener(board, 8, BambooColor.green, 3, 4));
        if(this.objectives.size() != Config.DECK_SIZE)
            throw new RuntimeException("La taille du paquet est de "+this.objectives.size()
                    + " alors qu'elle devrait être de "+Config.DECK_SIZE);
    }
}
