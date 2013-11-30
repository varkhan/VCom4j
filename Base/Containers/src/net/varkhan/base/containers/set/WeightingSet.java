/**
 *
 */
package net.varkhan.base.containers.set;

import net.varkhan.base.containers.Iterator;


/**
 * <b>A set that keeps track of the weights of added elements.</b>
 * <p/>
 *
 * @param <Key> the type of the objects stored in the set
 *
 * @author varkhan
 * @date Apr 9, 2010
 * @time 7:52:39 AM
 */
public interface WeightingSet<Key> extends Set<Key> {


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
     * Returns the total weight of entries in the set.
     *
     * @return the sum of all the weights of elements in this set,
     *         or {@literal 0.0} if this container is empty
     */
    public double weight();

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
     * @return the sum of all the weights the element has been added with in the set,
     *         or {@literal 0.0} if the element is absent
     */
    public double weight(Key key);

    /**
     * Adds weight to an element in this container.
     * <p/>
     * If the resulting weight of the entry is non-zero, an entry will be created if it did not exist.
     * If the resulting weight is zero, an existing entry will be removed (see {@link #del(Object)}).
     *
     * @param key the element to add weight to
     * @param wgh the weight to be added to the element
     *
     * @return {@literal true} if the container has been modified as a result of
     *         this operation, {@literal false} if the container remains unchanged
     */
    public boolean add(Key key, double wgh);

    /**
     * Removes an element from this container.
     * <p/>
     * This is semantically equivalent to {@code add(key,-weight(key))}.
     *
     * @param key the element to remove weight from
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
     * visitor's {@link net.varkhan.base.containers.Visitable.Visitor#invoke} method, until this method returns
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
