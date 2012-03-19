package net.varkhan.base.functors.predicates;

import net.varkhan.base.functors.Predicate;


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
