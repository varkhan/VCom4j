/**
 *
 */
package net.varkhan.base.containers.type;

import net.varkhan.base.containers.IndexedVisitable;


/**
 * @author varkhan
 * @date Jan 10, 2010
 * @time 4:31:51 AM
 */
public interface IndexedLongVisitable extends IndexedVisitable<Long> {

    public interface IndexedLongVisitor<Par> {
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
        public long invoke(long idx, long obj, Par par);
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
    public <Par> long visit(IndexedLongVisitor<Par> vis, Par par);


}
