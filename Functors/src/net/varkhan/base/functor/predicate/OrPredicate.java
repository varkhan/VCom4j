package net.varkhan.base.functor.predicate;

import net.varkhan.base.functor.Predicate;


/**
 * <b></b>.
 * <p/>
 *
 * @author varkhan
 * @date 2/11/12
 * @time 3:20 PM
 */
public class OrPredicate<A,C> extends AggregatePredicate<A,C> {

    public OrPredicate(Predicate<A,C>... preds) {
        super(preds);
    }

    public boolean invoke(A arg, C ctx) {
        boolean b = false;
        for(Predicate<A,C> p: preds) b |= p.invoke(arg, ctx);
        return b;
    }

}
