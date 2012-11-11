package net.varkhan.base.functor;

/**
 * <b></b>.
 * <p/>
 *
 * @author varkhan
 * @date 2/21/12
 * @time 11:53 PM
 */
public interface Inspector<A,C> {

    public void invoke(A arg, C ctx);

}
