/**
 *
 */
package net.varkhan.base.containers;

import net.varkhan.base.containers.Visitable.Visitor;


/**
 * <b>Applies a visitor to a subset of the objects in an indexed container.</b>
 * <p/>
 * Convenience object to allow applying an {@link IndexedVisitor} callback over
 * some subset of the elements of a container.
 * <p/>
 * Visitors can not mofify the value or object reference stored in the container.
 * <p/>
 * The value returned by a Visitor is used to signal early stops of the traversal,
 * and is aggregated into a final result available to the application.
 * <p/>
 *
 * @param <Type> the type of the objects in the container
 * @see Iterator
 * @see Iterable#iterator()
 * @see Indexable
 * @see Indexable#indexes()
 * @see IndexedOperable
 *
 * @author varkhan
 * @date Jan 29, 2010
 * @time 7:05:30 AM
 */
public interface IndexedVisitable<Type> {

    /**
     * <b>A callback, or procedure, used by the {@link IndexedVisitable#visit}.</b>
     * <p/>
     * An IndexedVisitor callback takes an index and single object of the type
     * supported by the container, processes that element, and returns a {@code long}
     * value that signifies the end of the iteration if negative, and is added to the
     * total visiting count otherwise.
     * <p/>
     *
     * @param <Type> the type of the objects in the container
     * @param <Par> the type of the control parameter
     *
     * @author varkhan
     * @date Jan 10, 2010
     * @time 4:04:20 AM
     */
    public static interface IndexedVisitor<Type,Par> {
        /**
         * Visit one element in the container.
         *
         * @param idx the index of the element
         * @param obj the object in the container
         * @param par the control parameter
         *
         * @return an integer count:
         *         <li/> if non-negative, it will be added to the visitor count,
         *         <li/> if negative it will trigger the termination of the visit.
        */
        public long invoke(long idx, Type obj, Par par);
    }

    /**
     * Iterate over each element of the container, and pass it as argument to a
     * visitor's {@link IndexedVisitor#invoke} method, until this method returns
     * a negative count.
     *
     * @param vis the visitor
     * @param par the control parameter
     * @param <Par> the type of the control parameter
     *
     * @return the sum of all positive return values from the visitor
     */
    public <Par> long visit(IndexedVisitor<Type,Par> vis, Par par);

}
