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
public abstract class WrapperPredicate<A,C> implements Predicate<A,C> {

    protected final Predicate<A,C> pred;

    public WrapperPredicate(Predicate<A,C> pred) {
        this.pred = pred;
    }

    public Predicate<A, C> component() {
        return pred;
    }

    public abstract boolean invoke(A arg, C ctx);

}
