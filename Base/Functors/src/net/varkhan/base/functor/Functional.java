package net.varkhan.base.functor;

/**
 * <b></b>.
 * <p/>
 *
 * @author varkhan
 * @date 2/11/12
 * @time 3:03 PM
 */
public interface Functional<A,C> {

    public double invoke(A arg, C ctx);

}
