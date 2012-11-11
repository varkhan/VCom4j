package net.varkhan.base.functor.predicate;

import net.varkhan.base.functor.Predicate;


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
