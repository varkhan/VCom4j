package net.varkhan.base.functors;

/**
 * <b></b>.
 * <p/>
 *
 * @author varkhan
 * @date 2/11/12
 * @time 3:00 PM
 */
public interface Mapper<R,A,C> {

    public R invoke(A arg, C ctx);

}
