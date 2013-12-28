package net.varkhan.base.functor;


/**
 * <b>A Mapper returning an Iterable</b>.
 * <p/>
 *
 * @param <R> the type of elements in the result iterable
 * @param <A> the type of arguments to the expander
 * @param <C> the type of the context parameter
 *
 * @author varkhan
 * @date 11/5/13
 * @time 7:13 PM
 */
public interface Expander<R,A,C> extends Mapper<Iterable<R>,A,C> {

    /**
     * Expand the argument into a sequence of values.
     *
     * @param arg the argument
     * @param ctx the context
     * @return the expanded values for this argument
     */
    public Iterable<R> invoke(A arg, C ctx);

}
