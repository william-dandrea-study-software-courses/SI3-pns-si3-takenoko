package fr.matelots.polytech.core.game.deck;

import fr.matelots.polytech.core.PickDeckEmptyException;
import fr.matelots.polytech.core.game.Board;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author Alexandre Arcil
 */
public abstract class Deck<T> {

    protected final List<T> cards;
    protected final Board board;

    public Deck(Board board) {
        this.board = board;
        this.cards = new ArrayList<>();
        this.fill();
        /*if(this.cards.size() != Config.DECK_OBJECTIVE_SIZE) Plus utilisable depuis l'ajout de DeckParcel
            throw new RuntimeException("La taille du paquet est de "+this.cards.size()
                    + " alors qu'elle devrait être de "+Config.DECK_OBJECTIVE_SIZE);*/
        Collections.shuffle(this.cards);
    }

    /**
     * C'est dans cette méthode qu'il faut remplir {@link #cards}
     */
    protected abstract void fill();

    /**
     * Tire la carte au sommet du paquet.
     * @return une carte
     * @throws PickDeckEmptyException Si le packet est vide
     */
    public T pick() {
        if(this.canPick())
            return this.cards.remove(0);
        else
            throw new PickDeckEmptyException();
    }

    /**
     * Permet de savoir si le paquet contient des cartes. Il faut l'appeler avant d'appeler {@link #pick()}
     * @return true si le paquet n'est pas vide, false sinon
     */
    public boolean canPick() {
        return !this.cards.isEmpty();
    }

}
