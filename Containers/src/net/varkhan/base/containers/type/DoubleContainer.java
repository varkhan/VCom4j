/**
 *
 */
package net.varkhan.base.containers.type;

import net.varkhan.base.containers.Container;


/**
 * <b>A double container.</b>
 * <p/>
 * This container stores doubles, that can be retrieved through an {@link DoubleIterable.DoubleIterator}
 * giving access to all the elements of the container.
 * <p/>
 *
 * @author varkhan
 * @date Mar 13, 2009
 * @time 8:43:50 PM
 */
public interface DoubleContainer extends DoubleIterable, DoubleVisitable, Container<Double> {


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
    public DoubleIterator iterator();

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
    public <Par> long visit(DoubleVisitor<Par> vis, Par par);


}
