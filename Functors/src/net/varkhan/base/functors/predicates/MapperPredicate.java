package net.varkhan.base.functors.predicates;

import net.varkhan.base.functors.Mapper;
import net.varkhan.base.functors.Predicate;


/**
 * <b></b>.
 * <p/>
 *
 * @author varkhan
 * @date 2/11/12
 * @time 3:21 PM
 */
public class MapperPredicate<A,C> implements Predicate<A,C> {

    private final Mapper<Object,A,C> mapr;
    private final Predicate<Object,C> pred;

    @SuppressWarnings({ "unchecked" })
    public <R> MapperPredicate(Predicate<R,C> pred, Mapper<R,A,C> mapr) {
        this.pred = (Predicate<Object, C>) pred;
        this.mapr = (Mapper<Object, A, C>) mapr;
    }

    public boolean invoke(A arg, C ctx) {
        return !pred.invoke(mapr.invoke(arg, ctx), ctx);
    }

}
