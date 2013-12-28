package net.varkhan.base.functor;

/**
 * <b>An observer of a given type of values</b>.
 * <p/>
 *
 * @param <A> the type of values observed
 * @param <C> the type of the context parameter
 *
 * @author varkhan
 * @date 12/28/13
 * @time 1:08 PM
 */
public interface Observer<A,C> {

    /**
     * Observe a value in a given context.
     *
     * @param arg an observed value
     * @param ctx the context
     */
    public void invoke(A arg, C ctx);

}
