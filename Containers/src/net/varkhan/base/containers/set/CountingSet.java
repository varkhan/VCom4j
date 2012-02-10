/**
 *
 */
package net.varkhan.base.containers.set;

import net.varkhan.base.containers.Iterator;


/**
 * <b>A set that counts the number of times elements have been added or removed.</b>
 * <p/>
 *
 * @param <Key> the type of the objects stored in the set
 *
 * @author varkhan
 * @date Apr 9, 2010
 * @time 7:52:39 AM
 */
public interface CountingSet<Key> extends Set<Key> {


    /**********************************************************************************
     **  Container statistics accessors
     **/

    /**
     * Returns the number of distinct elements in this container.
     *
     * @return the number of distinct elements stored in this set
     */
    public long size();

    /**
     * Indicates whether this container is empty.
     *
     * @return {@literal true} if this container contains no element,
     *         {@literal false} otherwise
     */
    public boolean isEmpty();

    /**
     * Returns the number of times any element has been added to this container.
     *
     * @return the number of times any key has been added to the container,
     *         or {@literal 0L} if this container is empty
     */
    public long count();

    /**
     * Deletes all elements from this set.
     */
    public void clear();


    /**********************************************************************************
     **  Container values accessors
     **/

    /**
     * Indicates whether a given element is present in this container.
     *
     * @param key the element
     *
     * @return {@literal true} if the element is in the container,
     *         or {@literal false} if the element is absent
     */
    public boolean has(Key key);

    /**
     * Returns the number of times a given element has been added to this container.
     *
     * @param key the element
     *
     * @return the number of times the element has been added to the container,
     *         or {@literal 0L} if the element is absent
     */
    public long count(Key key);

    /**
     * Adds an element to this container.
     *
     * @param key the element to add
     *
     * @return {@literal true} if the container has been modified as a result of
     *         this operation, {@literal false} if the container remains unchanged
     */
    public boolean add(Key key);

    /**
     * Removes an element from this container.
     *
     * @param key the element to remove
     *
     * @return {@literal true} if the container has been modified as a result of
     *         this operation, {@literal false} if the container remains unchanged
     */
    public boolean del(Key key);


    /**********************************************************************************
     **  Container elements iterators
     **/

    /**
     * Iterates over all elements in the container.
     *
     * @return an iterable over all the elements stored in the container
     */
    public Iterator<? extends Key> iterator();

    /**
     * Iterate over each element of the container, and pass it as argument to a
     * visitor's {@link Visitor#invoke} method, until this method returns
     * a negative count.
     *
     * @param vis the visitor
     * @param par the control parameter
     * @param <Par> the type of the control parameter
     *
     * @return the sum of all positive return values from the visitor
     */
    public <Par> long visit(Visitor<Key,Par> vis, Par par);

}
