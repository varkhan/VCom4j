package net.varkhan.base.functor.predicate;

import net.varkhan.base.functor.Predicate;


/**
 * <b></b>.
 * <p/>
 *
 * @author varkhan
 * @date 4/7/12
 * @time 5:12 PM
 */
public class ConstPredicate<A,C> implements Predicate<A,C> {

    protected final boolean val;
    public ConstPredicate(boolean val) { this.val=val; }
    public boolean invoke(A arg, C ctx) { return val; }

    protected static final Predicate<?,?> TRUE = new ConstPredicate(true);
    protected static final Predicate<?,?> FALSE = new ConstPredicate(false);

    @SuppressWarnings({ "unchecked" })
    public static <A,C> Predicate<A,C> TRUE() { return (Predicate<A, C>) TRUE; }

    @SuppressWarnings({ "unchecked" })
    public static <A,C> Predicate<A,C> FALSE() { return (Predicate<A, C>) FALSE; }

    @SuppressWarnings({ "unchecked" })
    public static <A,C> Predicate<A,C> as(boolean val) { return (Predicate<A, C>) (val?TRUE:FALSE); }

    @Override
    public String toString() {
        if(val) return "true";
        else return "false";
    }

}
