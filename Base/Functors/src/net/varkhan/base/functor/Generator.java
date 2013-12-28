package net.varkhan.base.functor;

/**
 * <b>A generator of a given type of values</b>.
 * <p/>
 * This interface can also represent a factory, or a static enumeration.
 *
 * @param <R> the type of values generated
 * @param <C> the type of the context parameter
 *
 * @author varkhan
 * @date 12/28/13
 * @time 1:05 PM
 */
public interface Generator<R,C> {

    /**
     * Generate a new value for a given context.
     *
     * @param ctx the context
     * @return a new generated value
     */
    public R invoke(C ctx);

}
