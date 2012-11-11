package net.varkhan.base.functor;

/**
 * <b></b>.
 * <p/>
 *
 * @author varkhan
 * @date 2/22/12
 * @time 1:01 AM
 */
public interface Generator<R,C> {

    public R invoke(C ctx);

}