package net.varkhan.base.functor.partition;

import net.varkhan.base.functor.Mapper;
import net.varkhan.base.functor.Ordinal;


/**
 * <b></b>.
 * <p/>
 *
 * @author varkhan
 * @date 2/11/12
 * @time 3:21 PM
 */
public class ComposedOrdinal<A,C> implements Ordinal<A,C> {

    private final Mapper<Object,A,C> mapr;
    private final Ordinal<Object,C>  ordn;

    @SuppressWarnings({ "unchecked" })
    public <R> ComposedOrdinal(Ordinal<R,C> ordn, Mapper<R,A,C> mapr) {
        this.ordn=(Ordinal<Object,C>) ordn;
        this.mapr=(Mapper<Object,A,C>) mapr;
    }

    public Ordinal<?,C> left() { return ordn; }

    public Mapper<?,A,C> right() { return mapr; }

    public long cardinal() { return ordn.cardinal(); }

    public long invoke(A arg, C ctx) {
        return ordn.invoke(mapr.invoke(arg, ctx), ctx);
    }

}
