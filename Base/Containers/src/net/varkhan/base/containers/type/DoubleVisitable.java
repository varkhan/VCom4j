/**
 *
 */
package net.varkhan.base.containers.type;

import net.varkhan.base.containers.Visitable;


/**
 * @author varkhan
 * @date Jan 10, 2010
 * @time 4:31:51 AM
 */
public interface DoubleVisitable extends Visitable<Double> {

    public interface DoubleVisitor<Par> {
        /**
         * Visit one element in the container.
         *
         * @param obj the object in the container
         * @param par the control parameter
         *
         * @return an integer count:
         *         <li/> if non-negative, it will be added to the visitor count,
         *         <li/> if negative it will trigger the termination of the visit.
         */
        public long invoke(double obj, Par par);
    }

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
    public <Par> long visit(DoubleVisitor<Par> vis, Par par);


}
