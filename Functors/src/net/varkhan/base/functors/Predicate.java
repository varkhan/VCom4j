package net.varkhan.base.functors;

/**
 * <b></b>.
 * <p/>
 *
 * @author varkhan
 * @date 2/11/12
 * @time 3:01 PM
 */
public interface Predicate<A,C> {

    public boolean invoke(A arg, C ctx);

}
