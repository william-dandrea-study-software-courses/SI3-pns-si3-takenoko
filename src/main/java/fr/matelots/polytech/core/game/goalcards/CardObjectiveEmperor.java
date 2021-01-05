package fr.matelots.polytech.core.game.goalcards;

import fr.matelots.polytech.core.game.Config;

/**
 * @author Gabriel Cogne
 */
public class CardObjectiveEmperor extends CardObjective {

    public CardObjectiveEmperor() {
        super (Config.EMPEROR_VALUE);
    }

    @Override
    public boolean verify() {
        return (completed = true);
    }
}
