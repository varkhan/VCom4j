package net.varkhan.base.functor;

/**
 * <b></b>.
 * <p/>
 *
 * @author varkhan
 * @date 11/10/12
 * @time 6:22 PM
 */
public interface Operator<R,A,B,C> {

    public R invoke(A left, B right, C ctx);

}
