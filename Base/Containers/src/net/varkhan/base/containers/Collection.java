/**
 *
 */
package net.varkhan.base.containers;

/**
 * <b>A modifiable object container.</b>
 * <p/>
 * This container stores objects, that can be retrieved through an {@link Iterator}
 * giving access to all the elements of the container.
 * <p/>
 * The contents of this collection can be updated by adding or deleting elements,
 * through a mechanism defined by sub-interfaces, and clearing all the elements.
 * <p/>
 *
 * @param <Type> the type of the objects stored in the collection
 *
 * @author varkhan
 * @date Mar 13, 2009
 * @time 8:43:50 PM
 */
public interface Collection<Type> extends Container<Type> {


    /**********************************************************************************
     **  Collection statistics accessors
     **/

    /**
     * Returns the number of elements in this container.
     *
     * @return the number of elements stored in this container
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
     * Deletes all elements from this container.
     */
    public void clear();


    /**********************************************************************************
     **  Container values accessors
     **/

    /**
     * Adds an element to this container.
     *
     * @param elt the element to add
     *
     * @return {@literal true} if the container has been modified as a result of
     *         this operation, {@literal false} if the container remains unchanged (for
     *         instance, because the element is already present)
     */
    public boolean add(Type elt);

    /**
     * Removes an element from this container.
     *
     * @param elt the element to remove
     *
     * @return {@literal true} if the container has been modified as a result of
     *         this operation, {@literal false} if the container remains unchanged (for
     *         instance, because the element was not initially in the container)
     */
    public boolean del(Type elt);


    /**********************************************************************************
     **  Collection elements iterators
     **/

    /**
     * Iterates over all elements in the container.
     *
     * @return an iterable over all the elements stored in the container
     */
    public Iterator<? extends Type> iterator();

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
    public <Par> long visit(Visitor<Type,Par> vis, Par par);


}
