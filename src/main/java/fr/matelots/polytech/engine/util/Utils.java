package fr.matelots.polytech.engine.util;

import fr.matelots.polytech.core.game.Config;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

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

    /**
     * Prend un élément au hasard d'un Set.
     */
    public static <E> E getRandomElement(Set<E> set) {
        return getRandomElement(new ArrayList<>(set));
    }

}
