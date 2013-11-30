/**
 *
 */
package net.varkhan.base.containers.queue;

import net.varkhan.base.containers.Container;
import net.varkhan.base.containers.Iterator;


/**
 * <b>A modifiable set of elements maintaining an internal order</b>
 * <p/>
 * A queue provides insertions, inspection and extraction operations on
 * its elements, that follow the internal order, and may be subject to
 * implementation-specific restrictions (number of elements, time elapsed,
 * duplicates...).
 * <p/>
 *
 * @author varkhan
 * @date Mar 13, 2009
 * @time 5:24:34 AM
 */
public interface Queue<Type> extends Container<Type> {

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
     * Indicates whether this container is empty.
     *
     * @return {@literal true} if this container contains no entry,
     *         {@literal false} otherwise
     */
    public boolean isEmpty();

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
    public boolean add(Type obj);

    /**
     * Removes and return the object at the head of the queue.
     *
     * @return the first object on the queue
     */
    public Type poll();

    /**
     * Returns, without removing it, the object at the head of the queue.
     *
     * @return the first object on the queue
     */
    public Type peek();

    /**
     * Returns an element at a given position.
     *
     * @param pos the position in the queue
     *
     * @return the element at position {@code pos}
     */
    public Type get(int pos);


    /**********************************************************************************
     **  Iterator operations
     **/

    /**
     * Returns an iterator over the elements in the queue.
     *
     * @return an Iterator.
     */
    public Iterator<Type> iterator();

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
    public <Par> long visit(Visitor<Type,Par> vis, Par par);

}
