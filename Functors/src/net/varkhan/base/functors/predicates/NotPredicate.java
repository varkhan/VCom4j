package net.varkhan.base.functors.predicates;

import net.varkhan.base.functors.Predicate;


/**
 * <b></b>.
 * <p/>
 *
 * @author varkhan
 * @date 2/11/12
 * @time 3:21 PM
 */
public class NotPredicate<A,C> implements Predicate<A,C> {

    private final Predicate<A,C> pred;

    public NotPredicate(Predicate<A, C> pred) {
        this.pred = pred;
    }

    public boolean invoke(A and, C ctx) {
        return !pred.invoke(and, ctx);
    }

}
