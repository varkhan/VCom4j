/**
 *
 */
package net.varkhan.base.containers.set;

import net.varkhan.base.containers.type.IntContainer;


/**
 * <b>A searchable collection that contains no duplicate elements.</b>
 * <p/>
 *
 * @author varkhan
 * @date Jun 30, 2009
 * @time 3:24:43 AM
 */
public interface IntSet extends Set<Integer>, IntContainer {


    /**********************************************************************************
     **  Container statistics accessors
     **/

    /**
     * Returns the number of elements in this container.
     *
     * @return the number of elements stored in this list
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
     * @return {@literal true} if this key is in the container,
     *         or {@literal false} if this key is absent
     */
    public boolean has(int key);

    /**
     * Adds an element to this container.
     *
     * @param key the element to add
     *
     * @return {@literal true} if the container has been modified as a result of
     *         this operation, {@literal false} if the container remains unchanged (for
     *         instance, because the element is already present)
     */
    public boolean add(int key);

    /**
     * Removes an element from this container.
     *
     * @param key the element to remove
     *
     * @return {@literal true} if the container has been modified as a result of
     *         this operation, {@literal false} if the container remains unchanged (for
     *         instance, because the element was not initially in the container)
     */
    public boolean del(int key);


    /**********************************************************************************
     **  Container elements iterators
     **/

    /**
     * Iterates over all elements in the container.
     *
     * @return an iterable over all the elements stored in the container
     */
    public IntIterator iterator();

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
    public <Par> long visit(IntVisitor<Par> vis, Par par);

}
