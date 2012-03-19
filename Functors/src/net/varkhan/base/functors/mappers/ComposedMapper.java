package net.varkhan.base.functors.mappers;

import net.varkhan.base.functors.Mapper;


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
    public <T> ComposedMapper(Mapper<R, T, C> l, Mapper<T, A, C> r) {
        this.l= (Mapper<R, Object, C>) l;
        this.r= (Mapper<Object, A, C>) r;
    }

    public R invoke(A arg, C ctx) {
        return l.invoke(r.invoke(arg, ctx), ctx);
    }

}
