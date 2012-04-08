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
public class NotPredicate<A,C> extends WrapperPredicate<A,C> {

    public NotPredicate(Predicate<A, C> pred) {
        super(pred);
    }

    public boolean invoke(A arg, C ctx) {
        return !pred.invoke(arg, ctx);
    }

}
