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
public class SamePredicate<A,C> implements Predicate<A,C> {
    protected final A obj;

    public SamePredicate(A obj) { this.obj=obj; }
    public boolean invoke(A arg, C ctx) { return arg==obj; }

    public static <A,C> SamePredicate<A,C> as(A obj) { return new SamePredicate<A,C>(obj);}

    @Override
    public String toString() {
        return "($=="+obj+')';
    }

}
