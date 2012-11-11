package net.varkhan.base.functor;

/**
 * <b></b>.
 * <p/>
 *
 * @author varkhan
 * @date 2/21/12
 * @time 11:40 PM
 */
public interface Reducer<A,C> {

    public A invoke(A larg, A rarg, C ctx);

}
