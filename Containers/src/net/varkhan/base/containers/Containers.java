package net.varkhan.base.containers;

import net.varkhan.base.containers.map.Map;


/**
 * <b>Static {@link Container} manipulation utilities.</b>.
 * <p/>
 * Utilities for converting Container types from and into other types,
 * searching and processing their contents, and comparing instances.
 * <p/>
 *
 * @author varkhan
 * @date 3/18/12
 * @time 2:49 PM
 */
public class Containers {

    /*********************************************************************************
     **  Equality
     **/


    /*********************************************************************************
     **  Deep inspection
     **/

    /**
     * Deep equality of containers.
     * <p/>
     * Note that two containers that contain the same set of elements but
     * <em>iterate</em> through them in a different order will <em>not</em>
     * be considered equal by this method.
     *
     * @param obj1 the left containers to inspect
     * @param obj2 the right containers to inspect
     * @param <E>  the type of the container elements
     *
     * @return {@literal true} if the two containers have the same size, and the
     *      element returned by their iterators at each iteration step are either
     *      {@see Object#equals} to each other or both {@literal null}.
     */
    public static <E> boolean equals(Container<E> obj1, Container<E> obj2) {
        if(obj1==obj2) return true;
        // We don't compare sizes, as this can be a slow operation for some containers,
        // and we have to check elements one by one anyways
        Iterator<? extends E> it1 = obj1.iterator();
        Iterator<? extends E> it2 = obj2.iterator();
        // As long as both collection have elements, check for equality
        while(it1.hasNext() && it2.hasNext()) {
            E el1 = it1.next();
            E el2 = it2.next();
            if(el1!=el2&&(el1==null||el2==null||!el1.equals(el2))) return false;
        }
        // If one of the containers has remaining elements, not equal
        return !(it1.hasNext() || it2.hasNext());
    }

    /**
     * Deep equality of maps.
     * <p/>
     * Note that two maps that contain the same set of key-value pairs but
     * <em>iterate</em> through them in a different order will <em>not</em>
     * be considered equal by this method.
     *
     * @param obj1 the left map to inspect
     * @param obj2 the right map to inspect
     * @param <K>  the type of the map keys
     * @param <V>  the type of the map values
     *
     * @return {@literal true} if the two maps have the same size, and the
     *      entries returned by their iterators at each iteration step contain
     *      keys and values that are respectively either {@see Object#equals}
     *      to each other or both {@literal null}.
     */
    public static <K,V> boolean equals(Map<K,V> obj1, Map<K,V> obj2) {
        if(obj1==obj2) return true;
        // We don't compare sizes, as this can be a slow operation for some maps,
        // and we have to check entries one by one anyways
        Iterator<? extends Map.Entry<K,V>> it1 = obj1.iterator();
        Iterator<? extends Map.Entry<K,V>> it2 = obj2.iterator();
        // As long as both collection have elements, check for equality
        while(it1.hasNext() && it2.hasNext()) {
            Map.Entry<K,V> e1 = it1.next();
            Map.Entry<K,V> e2 = it2.next();
            K k1 = e1.getKey();
            K k2 = e2.getKey();
            if(k1!=k2&&(k1==null||k2==null||!k1.equals(k2))) return false;
            V v1 = e1.getValue();
            V v2 = e2.getValue();
            if(v1!=v2&&(v1==null||v2==null||!v1.equals(v2))) return false;
        }
        // If one of the maps has remaining entries, not equal
        return !(it1.hasNext() || it2.hasNext());
    }

}
