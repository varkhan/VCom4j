package net.varkhan.base.functor.predicate;

import net.varkhan.base.functor.Predicate;


/**
 * <b></b>.
 * <p/>
 *
 * @author varkhan
 * @date 10/29/13
 * @time 12:10 PM
 */
public abstract class TransformPredicate<A,C> implements Predicate<A,C> {

    protected final Predicate<A,C> pred;

    public TransformPredicate(Predicate<A,C> pred) {
        this.pred = pred;
    }

    public Predicate<A, C> component() {
        return pred;
    }

    public abstract boolean invoke(A arg, C ctx);

    public static <A,C> TransformPredicate<A,C> not(final Predicate<A,C> pred) {
        return new TransformPredicate<A,C>(pred) {
            @Override
            public boolean invoke(A arg, C ctx) {
                return !pred.invoke(arg, ctx);
            }
            @Override
            public String toString() { return toString("!"); }
        };
    }

    protected String toString(String op) {
        StringBuilder buf = new StringBuilder(op);
        buf.append('(');
        buf.append(component().toString());
        buf.append(')');
        return buf.toString();
    }

    @Override
    public String toString() {
        return toString(this.getClass().getSimpleName());
    }

}
