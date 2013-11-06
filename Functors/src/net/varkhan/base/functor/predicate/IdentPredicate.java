package net.varkhan.base.functor.predicate;

import net.varkhan.base.functor.Mapper;
import net.varkhan.base.functor.Predicate;


/**
 * <b></b>.
 * <p/>
 *
 * @author varkhan
 * @date 10/30/13
 * @time 3:53 PM
 */
public final class IdentPredicate<C> implements Predicate<Boolean,C> {

    protected IdentPredicate() { }

    @Override
    public boolean invoke(Boolean arg, C ctx) {
        return arg;
    }

    protected static final IdentPredicate<?> ID=new IdentPredicate();

    @SuppressWarnings("unchecked")
    public static <C> IdentPredicate<C> identity() {
        return (IdentPredicate<C>) ID;
    }

    public static <A,C> Predicate<A,C> asPredicate(final Mapper<Boolean,A,C> mapr) {
        return new Predicate<A,C>() {
            @Override
            public boolean invoke(A arg, C ctx) {
                Boolean v=mapr.invoke(arg, ctx);
                return v==null ? false : v;
            }
        };
    }

    public static <A,C> Mapper<Boolean,A,C> asMapper(final Predicate<A,C> pred) {
        return new Mapper<Boolean,A,C>() {
            @Override
            public Boolean invoke(A arg, C ctx) {
                return pred.invoke(arg, ctx);
            }
        };
    }
}
