package net.varkhan.base.functor;


/**
 * <b></b>.
 * <p/>
 *
 * @author varkhan
 * @date 11/5/13
 * @time 7:13 PM
 */
public interface Reducer<R,A,C> extends Mapper<R,Iterable<A>,C> {
}
