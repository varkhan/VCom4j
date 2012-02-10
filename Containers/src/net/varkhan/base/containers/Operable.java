/**
 *
 */
package net.varkhan.base.containers;

/**
 * <b>Applies an operator to a subset of the objects in a container.</b>
 * <p/>
 * Convenience object to allow applying an {@link Operator} callback over
 * some subset of the elements of a container.
 * <p/>
 * The return value of operators replaces the original value in the container
 * <p/>
 *
 * @param <Type> the type of the objects in the container
 * @see Iterator
 * @see Iterable#iterator()
 * @see Visitable
 *
 * @author varkhan
 * @date Jan 10, 2010
 * @time 7:16:41 AM
 */
public interface Operable<Type> {

    /**
     * <b>A callback, or procedure, used by the {@link Operable#operate}.</b>
     * <p/>
     * A Visitor callback takes a single object of the type supported by the
     * container, processes that element, and returns a new element value that
     * replaces the original argument.
     * <p/>
     *
     * @param <Type> the type of the objects in the container
     * @param <Par> the type of the control parameter
     */
    public static interface Operator<Type,Par> {
        /**
         * Operates on one element in the container.
         *
         * @param obj the operator's argument (the original object in the container)
         * @param par the control parameter
         *
         * @return the operator's result (that will replace the argument in the container)
         */
        public Type invoke(Type obj, Par par);
    }

    /**
     * Iterate over each element of the container, and pass it as argument to a
     * operator's {@link Operator#invoke} method, until this method returns
     * a negative count.
     *
     * @param operator the operator
     *
     * @return {@literal true} if the container was modified as a result of the operation
     * (i.e. at least one of the return values of the operator was different from the corresponding argument)
     */
    public <Par> boolean operate(Operator<Type,Par> operator);
}
