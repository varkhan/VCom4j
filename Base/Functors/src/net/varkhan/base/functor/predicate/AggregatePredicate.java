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
            @Override
            public String toString() {
                StringBuilder buf = new StringBuilder();
                boolean f = true;
                for(Predicate<A,C> p: preds) {
                    if(f) f = false;
                    else buf.append('&');
                    buf.append(p.toString());
                }
                return buf.toString();
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
            @Override
            public String toString() {
                StringBuilder buf = new StringBuilder("!");
                buf.append('(');
                boolean f = true;
                for(Predicate<A,C> p: preds) {
                    if(f) f = false;
                    else buf.append('&');
                    buf.append(p.toString());
                }
                buf.append(')');
                return buf.toString();
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
            @Override
            public String toString() {
                StringBuilder buf = new StringBuilder();
                boolean f = true;
                for(Predicate<A,C> p: preds) {
                    if(f) f = false;
                    else buf.append('|');
                    buf.append(p.toString());
                }
                return buf.toString();
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
            @Override
            public String toString() {
                StringBuilder buf = new StringBuilder("!");
                buf.append('(');
                boolean f = true;
                for(Predicate<A,C> p: preds) {
                    if(f) f = false;
                    else buf.append('|');
                    buf.append(p.toString());
                }
                buf.append(')');
                return buf.toString();
            }
        };
    }

    protected String toString(String op) {
        StringBuilder buf = new StringBuilder(op);
        buf.append('(');
        boolean f = true;
        for(Predicate<A,C> p: preds) {
            if(f) f = false;
            else buf.append(',');
            buf.append(p.toString());
        }
        buf.append(')');
        return buf.toString();
    }

    @Override
    public String toString() {
        return toString(this.getClass().getSimpleName());
    }

}
