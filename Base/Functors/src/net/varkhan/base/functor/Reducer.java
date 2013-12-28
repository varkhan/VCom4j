package net.varkhan.base.functor;


/**
 * <b>A Mapper applicable to an Iterable</b>.
 * <p/>
 *
 * @param <R> the type of results of the reducer
 * @param <A> the type of elements in the argument
 * @param <C> the type of the context parameter
 *
 * @author varkhan
 * @date 11/5/13
 * @time 7:13 PM
 */
public interface Reducer<R,A,C> extends Mapper<R,Iterable<A>,C> {

    /**
     * Iterate over the arguments and compute a reduced value for a given context.
     *
     * @param arg an iterable over the arguments
     * @param ctx the context
     * @return the reduced value for this argument
     */
    public R invoke(Iterable<A> arg, C ctx);

}
