package net.varkhan.base.functor.predicate;

import net.varkhan.base.functor.Mapper;
import net.varkhan.base.functor.Predicate;


/**
 * <b></b>.
 * <p/>
 *
 * @author varkhan
 * @date 2/11/12
 * @time 3:21 PM
 */
public class MappedPredicate<A,C> implements Predicate<A,C> {

    private final Mapper<Object,A,C> mapr;
    private final Predicate<Object,C> pred;

    @SuppressWarnings({ "unchecked" })
    public <R> MappedPredicate(Predicate<R,C> pred, Mapper<R,A,C> mapr) {
        this.pred = (Predicate<Object, C>) pred;
        this.mapr = (Mapper<Object, A, C>) mapr;
    }

    public boolean invoke(A arg, C ctx) {
        return pred.invoke(mapr.invoke(arg, ctx), ctx);
    }

}
