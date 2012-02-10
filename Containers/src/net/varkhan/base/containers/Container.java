/**
 *
 */
package net.varkhan.base.containers;

/**
 * <b>An object container.</b>
 * <p/>
 * This container stores objects, that can be retrieved through an {@link Iterator}
 * giving access to all the elements of the container.
 * <p/>
 *
 * @param <Type> the type of the objects stored in the container
 *
 * @author varkhan
 * @date Mar 13, 2009
 * @time 8:43:50 PM
 */
public interface Container<Type> extends Iterable<Type>, Visitable<Type> {


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


    /**********************************************************************************
     **  Container elements iterators
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
