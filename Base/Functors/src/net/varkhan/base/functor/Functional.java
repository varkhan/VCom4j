package net.varkhan.base.functor;

/**
 * <b>A functor mapping an argument to a real number</b>.
 * <p/>
 *
 * @param <A> the type of arguments to the functional
 * @param <C> the type of the context parameter
 *
 * @author varkhan
 * @date 2/11/12
 * @time 3:03 PM
 */
public interface Functional<A,C> {

    /**
     * Compute the value of the functional for a given argument and context.
     *
     * @param arg the argument
     * @param ctx the context
     * @return the value of the functional for this argument
     */
    public double invoke(A arg, C ctx);

}
