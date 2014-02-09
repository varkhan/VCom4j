package net.varkhan.base.functor.predicate;

import net.varkhan.base.functor.Predicate;


/**
 * <b></b>.
 * <p/>
 *
 * @author varkhan
 * @date 10/30/13
 * @time 3:53 PM
 */
public class IdentPredicate<C> implements Predicate<Boolean,C> {

    protected IdentPredicate() { }

    @Override
    public boolean invoke(Boolean arg, C ctx) {
        return arg;
    }

    protected static final IdentPredicate<?> ID=new IdentPredicate();

    @SuppressWarnings("unchecked")
    public static <C> IdentPredicate<C> as() { return (IdentPredicate<C>) ID; }

    @Override
    public String toString() {
        return "$";
    }

}
