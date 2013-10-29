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


    public static <A,C> AggregatePredicate<A,C> and(Predicate<A, C>... preds) {
        return new AggregatePredicate<A,C>(preds) {
            @Override
            public boolean invoke(A arg, C ctx) {
                for(Predicate<A, C> p: preds)
                    if(!p.invoke(arg, ctx)) return false;
                return true;
            }
        };
    }

    public static <A,C> AggregatePredicate<A,C> nand(Predicate<A, C>... preds) {
        return new AggregatePredicate<A,C>(preds) {
            @Override
            public boolean invoke(A arg, C ctx) {
                for(Predicate<A, C> p: preds)
                    if(!p.invoke(arg, ctx)) return true;
                return false;
            }
        };
    }

    public static <A,C> AggregatePredicate<A,C> or(Predicate<A, C>... preds) {
        return new AggregatePredicate<A,C>(preds) {
            @Override
            public boolean invoke(A arg, C ctx) {
                for(Predicate<A, C> p: preds)
                    if(p.invoke(arg, ctx)) return true;
                return false;
            }
        };
    }

    public static <A,C> AggregatePredicate<A,C> nor(Predicate<A, C>... preds) {
        return new AggregatePredicate<A,C>(preds) {
            @Override
            public boolean invoke(A arg, C ctx) {
                for(Predicate<A, C> p: preds)
                    if(p.invoke(arg, ctx)) return false;
                return true;
            }
        };
    }

 }
