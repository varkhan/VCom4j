package net.varkhan.base.containers;

import net.varkhan.base.containers.map.Map;

import java.io.IOException;


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


    /*********************************************************************************
     **  String transformation
     **/

    /**
     * Appends as strings the elements of an array, wrapping each non-null
     * element with delimiters and separating them with a given string.
     *
     * @param buf   the buffer to append the composed string to
     * @param sep   the separator between elements
     * @param ldl   the left delimiter of elements
     * @param rdl   the right delimiter of element
     * @param array the array to join
     * @param <T>   the element type
     * @param <A>   the buffer type
     *
     * @return the original buffer, for chaining purposes
     *
     * @throws IOException if the output buffer raises this exception on {@code append()}
     */
    public static <T,A extends Appendable> A join(A buf, String sep, String ldl, String rdl, java.lang.Iterable<T> array) throws IOException {
        if(sep==null) {
            for(T elt: array) {
                if(elt!=null) {
                    if(ldl!=null) buf.append(ldl);
                    buf.append(elt.toString());
                    if(rdl!=null) buf.append(rdl);
                }
            }
        }
        else {
            boolean fst = true;
            for(T elt: array) {
                if(fst) fst=false;
                else buf.append(sep);
                if(elt!=null) {
                    if(ldl!=null) buf.append(ldl);
                    buf.append(elt.toString());
                    if(rdl!=null) buf.append(rdl);
                }
            }
        }
        return buf;
    }

    /**
     * Appends as strings the elements of an array, wrapping each non-null
     * element with delimiters and separating them with a given string.
     *
     * @param buf   the buffer to append the composed string to
     * @param sep   the separator between elements
     * @param ldl   the left delimiter of elements
     * @param rdl   the right delimiter of element
     * @param array the array to join
     * @param <T>   the element type
     *
     * @return the original buffer, for chaining purposes
     */
    public static <T> StringBuilder join(StringBuilder buf, String sep, String ldl, String rdl, java.lang.Iterable<T> array) {
        if(sep==null) {
            for(T elt: array) {
                if(elt!=null) {
                    if(ldl!=null) buf.append(ldl);
                    buf.append(elt.toString());
                    if(rdl!=null) buf.append(rdl);
                }
            }
        }
        else {
            boolean fst = true;
            for(T elt: array) {
                if(fst) fst=false;
                else buf.append(sep);
                if(elt!=null) {
                    if(ldl!=null) buf.append(ldl);
                    buf.append(elt.toString());
                    if(rdl!=null) buf.append(rdl);
                }
            }
        }
        return buf;
   }

    /**
     * Appends as strings the elements of an array, wrapping each non-null
     * element with delimiters and separating them with a given string.
     *
     * @param sep   the separator between elements
     * @param ldl   the left delimiter of elements
     * @param rdl   the right delimiter of element
     * @param array the array to join
     * @param <T>   the element type
     *
     * @return a concatenation of the elements of the array, as strings wrapped in the delimiters, and the separator
     */
    public static <T> String join(String sep, String ldl, String rdl, java.lang.Iterable<T> array) {
        return join(new StringBuilder(), sep, ldl, rdl, array).toString();
    }

    /**
     * Appends as strings the elements of an array, separating them with a given string.
     *
     * @param sep   the separator between elements
     * @param array the array to join
     * @param <T>   the element type
     *
     * @return a concatenation of the elements of the array, as strings, and the separator
     */
    public static <T> String join(String sep, java.lang.Iterable<T> array) {
        return join(new StringBuilder(), sep, null, null, array).toString();
    }

}
