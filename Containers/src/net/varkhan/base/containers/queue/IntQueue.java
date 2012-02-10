/**
 *
 */
package net.varkhan.base.containers.queue;

import net.varkhan.base.containers.Iterator;
import net.varkhan.base.containers.type.IntVisitable;


/**
 * <b>A modifiable set of elements maintaining an internal order</b>
 * <p/>
 * A queue provides insertions, inspection and extraction operations
 * on its elements, that follow the internal order, and may be subject to
 * implementation-specific restrictions (bounds, delays, unicity...).
 * <p/>
 *
 * @author varkhan
 * @date Mar 13, 2009
 * @time 5:24:34 AM
 */
public interface IntQueue extends Queue<Integer>, IntVisitable {

    /**********************************************************************************
     **  Queue statistics accessors
     **/

    /**
     * Return the number of entries in the queue
     *
     * @return the number of objects stored in the queue
     */
    public long size();


    /**
     * Removes all objects from the queue
     */
    public void clear();


    /**********************************************************************************
     **  Queue element operations
     **/

    /**
     * Adds an object on the queue.
     *
     * @param obj the object to add
     *
     * @return {@literal true} if the object was added, {@literal false} if
     *         the object could not be added (for instance, because a limit on the
     *         queue content has been reached)
     */
    public boolean add(Integer obj);

    /**
     * Adds an object on the queue.
     *
     * @param obj the object to add
     *
     * @return {@literal true} if the object was added, {@literal false} if
     *         the object could not be added (for instance, because a limit on the
     *         queue content has been reached)
     */
    public boolean add(int obj);

    /**
     * Removes and return the object at the beginning of the queue
     *
     * @return the biggest object on the queue
     */
    public Integer poll();

    /**
     * Removes and return the object at the beginning of the queue
     *
     * @return the biggest object on the queue
     */
    public int dequeueInt();

    /**
     * Returns, without removing it, the object at the beginning of the queue
     *
     * @return the biggest object on the queue
     */
    public Integer peek();

    /**
     * Returns, without removing it, the object at the beginning of the queue
     *
     * @return the biggest object on the queue
     */
    public int getInt();

    /**
     * Returns an element at a given position
     *
     * @param pos the position in the queue
     *
     * @return the element at position {@code pos}
     */
    public Integer get(int pos);

    /**
     * Returns an element at a given position
     *
     * @param pos the position in the queue
     *
     * @return the element at position {@code pos}
     */
    public int getInt(int pos);


    /**********************************************************************************
     **  Iterator operations
     **/

    /**
     * Returns an iterator over the elements in the queue.
     *
     * @return an Iterator.
     */
    public Iterator<Integer> iterator();

    /**
     * Iterate over each element of the queue, and pass it as argument to a
     * visitor's {@link Visitor#invoke} method, until this method returns
     * a negative count.
     *
     * @param vis the visitor
     * @param par the control parameter
     * @param <Par> the type of the control parameter
     *
     * @return the sum of all positive return values from the visitor
     */
    public <Par> long visit(Visitor<Integer,Par> vis, Par par);

    /**
     * Iterate over each element of the queue, and pass it as argument to a
     * visitor's {@link Visitor#invoke} method, until this method returns
     * a negative count.
     *
     * @param vis the visitor
     * @param par the control parameter
     * @param <Par> the type of the control parameter
     *
     * @return the sum of all positive return values from the visitor
     */
    public <Par> long visit(IntVisitor<Par> vis, Par par);

}
