package net.varkhan.base.functor;

/**
 * <b>A functor mapping an argument to a boolean</b>.
 * <p/>
 *
 * @param <A> the type of arguments to the predicate
 * @param <C> the type of the context parameter
 *
 * @author varkhan
 * @date 2/11/12
 * @time 3:01 PM
 */
public interface Predicate<A,C> {

    /**
     * Compute the value of the predicate for a given argument and context.
     *
     * @param arg the argument
     * @param ctx the context
     * @return the value of the predicate for this argument
     */
    public boolean invoke(A arg, C ctx);

}
