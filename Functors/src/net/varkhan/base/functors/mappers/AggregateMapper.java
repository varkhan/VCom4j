package net.varkhan.base.functors.mappers;

import net.varkhan.base.functors.Mapper;


/**
 * <b></b>.
 * <p/>
 *
 * @author varkhan
 * @date 2/11/12
 * @time 3:06 PM
 */
public abstract class AggregateMapper<R,A,C> implements Mapper<R,A,C> {

    protected final Mapper<R, A, C>[] funcs;

    public AggregateMapper(Mapper<R, A, C>... funcs) {
        this.funcs = funcs;
    }

    @SuppressWarnings("unchecked")
    public Mapper<R, A, C>[] components() {
        return funcs.clone();
    }

    public abstract R invoke(A arg, C ctx);

}
