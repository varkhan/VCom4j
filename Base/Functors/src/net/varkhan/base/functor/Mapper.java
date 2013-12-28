package net.varkhan.base.functor;

/**
 * <b>A functor mapping an argument to a result within a given context</b>.
 * <p/>
 *
 * @param <R> the type of results of the mapper
 * @param <A> the type of arguments to the mapper
 * @param <C> the type of the context parameter
 *
 * @author varkhan
 * @date 2/11/12
 * @time 3:00 PM
 */
public interface Mapper<R,A,C> {

    /**
     * Compute the value of the mapping for a given argument and context.
     *
     * @param arg the argument
     * @param ctx the context
     * @return the value of the mapping for this argument
     */
    public R invoke(A arg, C ctx);

}
