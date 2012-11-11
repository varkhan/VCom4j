package net.varkhan.base.functor.predicate;

import net.varkhan.base.functor.Predicate;


/**
 * <b></b>.
 * <p/>
 *
 * @author varkhan
 * @date 2/11/12
 * @time 3:13 PM
 */
public abstract class AggregatePredicate<A,C> implements Predicate<A,C> {

    protected final Predicate<A, C>[] preds;

    public AggregatePredicate(Predicate<A, C>... preds) {
        this.preds = preds;
    }

    public Predicate<A, C>[] components() {
        return preds.clone();
    }

    public abstract boolean invoke(A arg, C ctx);

 }
