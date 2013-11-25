package net.varkhan.base.functor.mapper;

import net.varkhan.base.functor.Mapper;


/**
 * <b></b>.
 * <p/>
 *
 * @author varkhan
 * @date 2/11/12
 * @time 3:08 PM
 */
public class ComposedMapper<R,A,C> implements Mapper<R,A,C> {

    private final Mapper<R,Object,C> l;
    private final Mapper<Object,A,C> r;

    @SuppressWarnings({ "unchecked" })
    public <T> ComposedMapper(Mapper<R, ? super T, C> l, Mapper<? extends T, A, C> r) {
        this.l= (Mapper) l;
        this.r= (Mapper) r;
    }

    public Mapper<R,?,C> left() { return l; }
    public Mapper<?,A,C> right() { return r; }

    public R invoke(A arg, C ctx) {
        return l.invoke(r.invoke(arg, ctx), ctx);
    }

}
