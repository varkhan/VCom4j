package net.varkhan.base.functor;

/**
 * <b>A functor mapping an argument to an ordinal</b>.
 * <p/>
 *
 * @param <A> the type of arguments to the ordinal
 * @param <C> the type of the context parameter
 *
 * @author varkhan
 * @date 12/26/13
 * @time 8:27 PM
 */
public interface Ordinal<A,C> {

    /**
     * The number of possible distinct values taken by this ordinal
     * (or, equivalently, the upper bound of the values taken by this ordinal).
     * <p/>
     * If the value {@literal 0} is returned, the ordinal is considered <em>empty</em>,
     * and should never be evaluated. In this case the result of {@link #invoke(Object, Object)}
     * is undefined (and invoking this method can possibly raise an exception).
     *
     * @return a non-negative number bounding (exclusively) the values of {@link #invoke(Object, Object)}
     */
    public long cardinal();

    /**
     * Compute the value of the ordinal for a given argument and context.
     * <p/>
     * If {@link #cardinal()} is {@literal 0}, this method is considered undefined,
     * and invoking it can possibly raise an exception.
     *
     * @param arg the argument
     * @param ctx the context
     * @return the value of the ordinal for this argument,
     *      between {@literal 0} (inclusive) and {@link #cardinal()} (exclusive)
     */
    public long invoke(A arg, C ctx);

}
