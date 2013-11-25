package net.varkhan.base.functor.expander;

import net.varkhan.base.functor.Expander;
import net.varkhan.base.functor.Mapper;


/**
 * <b></b>.
 * <p/>
 *
 * @author varkhan
 * @date 4/7/12
 * @time 5:48 PM
 */
public class IdentExpander<A,C> implements Expander<A,Iterable<A>,C> {

    public IdentExpander() { }

    public Iterable<A> invoke(Iterable<A> arg, C ctx) { return arg; }

    protected static final IdentExpander<Object,Object> ID=new IdentExpander<Object,Object>();

    @SuppressWarnings({ "unchecked" })
    public static <A,C> Expander<A,Iterable<A>,C> as() { return (Expander) ID;}

    public static <R,A,C> Expander<R,A,C> as(final Mapper<Iterable<R>, A, C> mapr) {
        return new Expander<R,A,C>() {
            public Iterable<R> invoke(A arg, C ctx) { return mapr.invoke(arg, ctx); }
        };
    }

}
