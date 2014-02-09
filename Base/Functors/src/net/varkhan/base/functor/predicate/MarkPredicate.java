package net.varkhan.base.functor.predicate;

import net.varkhan.base.functor.Predicate;


/**
 * <b></b>.
 * <p/>
 *
 * @author varkhan
 * @date 4/7/12
 * @time 5:14 PM
 */
public class MarkPredicate<A,C> implements Predicate<A,C> {

    public boolean invoke(A arg, C ctx) { return arg==ctx; }

    protected static final MarkPredicate<?,?> MARK = new MarkPredicate<Object,Object>();

    @SuppressWarnings("unchecked")
    public static <A,C> MarkPredicate<A,C> as() { return (MarkPredicate<A,C>) MARK;}

    @Override
    public String toString() {
        return "($==%)";
    }

}
