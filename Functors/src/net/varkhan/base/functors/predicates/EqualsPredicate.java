package net.varkhan.base.functors.predicates;

import net.varkhan.base.functors.Predicate;


/**
 * <b></b>.
 * <p/>
 *
 * @author varkhan
 * @date 4/7/12
 * @time 5:14 PM
 */
public class EqualsPredicate<A,C> implements Predicate<A,C> {
    protected final A obj;

    public EqualsPredicate(A obj) { this.obj=obj; }
    public boolean invoke(A arg, C ctx) { return (obj==null)?arg==null:obj.equals(arg); }

    public static <A,C> EqualsPredicate<A,C> as(A obj) { return new EqualsPredicate<A,C>(obj);}

}
