/**
 *
 */
package net.varkhan.base.containers;

/**
 * <b>A searchable container.</b>
 * <p/>
 * This container allows to test for the presence or absence of a given object.
 * <p/>
 * If combined with {@link Container} semantics on the same type, it is required
 * that the objects that are indicated as <em>present</em> are exactly those that
 * test equal to the objects returned by the container iterator.
 * <p/>
 *
 * @param <Key> the type of object that can be included
 *
 * @author varkhan
 * @date Mar 19, 2009
 * @time 1:39:19 AM
 */
public interface Searchable<Key> {


    /**********************************************************************************
     **  Container values accessors
     **/

    /**
     * Indicate whether a given key is present in this container.
     *
     * @param key a unique identifier
     *
     * @return {@literal true} if this key is in the container,
     *         or {@literal false} if this key is absent
     */
    public boolean has(Key key);


}
