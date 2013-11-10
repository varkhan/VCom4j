package net.varkhan.base.functor.functional;

import net.varkhan.base.functor.Functional;
import net.varkhan.base.functor.Mapper;


/**
 * <b></b>.
 * <p/>
 *
 * @author varkhan
 * @date 10/30/13
 * @time 3:53 PM
 */
public final class IdentFunctional<C> implements Functional<Double,C> {

    protected IdentFunctional() { }

    @Override
    public double invoke(Double arg, C ctx) {
        return arg;
    }

    protected static final IdentFunctional<?> ID=new IdentFunctional();

    @SuppressWarnings("unchecked")
    public static <C> IdentFunctional<C> as() {
        return (IdentFunctional<C>) ID;
    }

    public static <A,C> Functional<A,C> as(final Mapper<Double,A,C> mapr) {
        return new Functional<A,C>() {
            @Override
            public double invoke(A arg, C ctx) {
                Double v=mapr.invoke(arg, ctx);
                return v==null?Double.NaN:v;
            }
        };
    }

    public static <A,C> Mapper<Double,A,C> as(final Functional<A,C> func) {
        return new Mapper<Double,A,C>() {
            @Override
            public Double invoke(A arg, C ctx) {
                return func.invoke(arg, ctx);
            }
        };
    }
}
