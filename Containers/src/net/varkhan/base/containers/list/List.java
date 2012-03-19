package net.varkhan.base.containers.list;

import net.varkhan.base.containers.Collection;
import net.varkhan.base.containers.Iterator;


/**
 * <b>A modifiable list.</b>.
 * <p/>
 * Added elements are inserted at the end of the sequence of pre-existing elements.
 * <p/>
 * Removed elements are replaced by the following elements in the sequence, whose
 * positions (indices) are thus decreased by one.
 * <p/>
 *
 * @param <Type> the type of elements in the list
 *
 * @author varkhan
 * @date 3/18/12
 * @time 5:46 PM
 */
public interface List<Type> extends Collection<Type> {


    /**********************************************************************************
     **  List statistics accessors
     **/

    /**
     * Returns the number of elements in this list.
     *
     * @return the number of elements stored in this list
     */
    public long size();

    /**
     * Indicates whether this list is empty.
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
     **  List entries accessors
     **/

    /**
     * Adds an element to this list.
     *
     * @param elt the element to add
     *
     * @return {@literal true} if the list has been modified as a result of
     *         this operation, {@literal false} if the list remains unchanged
     */
    public boolean add(Type elt);

    /**
     * Gets an element from this list.
     *
     * @param idx the index of the element
     *
     * @return the element at position {@code idx}, or {@literal null} if no element exists
     */
    public Type get(long idx);

    /**
     * Gets an element in this list.
     *
     * @param idx the index of the element
     * @param elt the element to set
     *
     * @return {@literal true} if the list has been modified as a result of
     *         this operation, {@literal false} if the list remains unchanged (for
     *         instance, because the element was not initially in the list)
     */
    public boolean set(long idx, Type elt);

    /**
     * Removes an element from this list.
     *
     * @param idx the index of element to remove
     *
     * @return {@literal true} if the list has been modified as a result of
     *         this operation, {@literal false} if the list remains unchanged
     */
    public boolean del(long idx);

    /**
     * Removes an element from this list.
     *
     * @param elt the element to remove
     *
     * @return {@literal true} if the list has been modified as a result of
     *         this operation, {@literal false} if the list remains unchanged
     */
    public boolean del(Type elt);


    /**********************************************************************************
     **  List entries iterators
     **/

    /**
     * Iterates over all elements in the list.
     *
     * @return an iterable over all the elements stored in the list
     */
    public Iterator<? extends Type> iterator();

    /**
     * Iterate over each element of the list, and pass it as argument to a
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
