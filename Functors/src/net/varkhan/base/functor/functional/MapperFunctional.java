package net.varkhan.base.functor.functional;

import net.varkhan.base.functor.Functional;
import net.varkhan.base.functor.Mapper;


/**
 * <b></b>.
 * <p/>
 *
 * @author varkhan
 * @date 2/11/12
 * @time 3:21 PM
 */
public class MapperFunctional<A,C> implements Functional<A,C> {

    private final Mapper<Object,A,C>   mapr;
    private final Functional<Object,C> func;

    @SuppressWarnings({ "unchecked" })
    public <R> MapperFunctional(Functional<R,C> func, Mapper<R,A,C> mapr) {
        this.func=(Functional<Object,C>) func;
        this.mapr=(Mapper<Object,A,C>) mapr;
    }

    public double invoke(A arg, C ctx) {
        return func.invoke(mapr.invoke(arg, ctx), ctx);
    }

}
