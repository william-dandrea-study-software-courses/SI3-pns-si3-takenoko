package fr.matelots.polytech.engine.util;

import fr.matelots.polytech.core.game.Config;

import java.util.List;

/**
 * @author Alexandre Arcil
 */
public class Utils {

    /**
     * Prend un élément au hasard d'une liste.
     */
    public static <E> E getRandomElement(List<E> list) {
        int index = Config.RANDOM.nextInt(list.size());
        return list.get(index);
    }

}
