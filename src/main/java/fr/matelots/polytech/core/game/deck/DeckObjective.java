package fr.matelots.polytech.core.game.deck;

import fr.matelots.polytech.core.PickDeckEmptyException;
import fr.matelots.polytech.core.game.Board;
import fr.matelots.polytech.core.game.Config;
import fr.matelots.polytech.core.game.goalcards.CardObjective;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author Alexandre Arcil
 */
public abstract class DeckObjective<T extends CardObjective> {

    protected final List<T> objectives;
    protected final Board board;

    public DeckObjective(Board board) {
        this.board = board;
        this.objectives = new ArrayList<>();
        this.fill();
        if(this.objectives.size() != Config.DECK_SIZE)
            throw new RuntimeException("La taille du paquet est de "+this.objectives.size()
                    + " alors qu'elle devrait être de "+Config.DECK_SIZE);
        Collections.shuffle(this.objectives);
    }

    /**
     * C'est dans cette méthode qu'il faut remplir {@link #objectives}
     */
    protected abstract void fill();

    /**
     * Tire la carte au sommet du paquet.
     * @return une carte
     * @throws PickDeckEmptyException Si le packet est vide
     */
    public T pick() {
        if(this.canPick())
            return this.objectives.remove(0);
        else
            throw new PickDeckEmptyException();
    }

    /**
     * Permet de savoir si le paquet contient des cartes. Il faut l'appeler avant d'appeler {@link #pick()}
     * @return true si le paquet n'est pas vide, false sinon
     */
    public boolean canPick() {
        return !this.objectives.isEmpty();
    }

}
