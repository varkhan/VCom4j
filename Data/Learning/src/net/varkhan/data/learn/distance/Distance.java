package net.varkhan.data.learn.distance;

import net.varkhan.base.functor.Functional;
import net.varkhan.base.functor._;
import net.varkhan.base.functor.curry.Pair;


/**
 * <b></b>.
 * <p/>
 *
 * @author varkhan
 * @date 12/1/13
 * @time 5:56 PM
 */
public abstract class Distance<T,C> implements Functional<_<T,_<T,_>>,C> {

    public double invoke(_<T,_<T,_>> arg, C ctx) {
        return invoke(arg.lvalue(),arg._value().lvalue(),ctx);
    }

    public abstract double invoke(T lvalue, T rvalue, C ctx);


    public static <T,C> Distance<T,C> wrap(final Functional<_<T,_<T,_>>,C> dist) {
        if(dist instanceof Distance) return (Distance<T, C>) dist;
        return new Distance<T,C>() {
            public double invoke(T lvalue, T rvalue, C ctx) {
                return invoke(new Pair.Value<T,T>(lvalue, rvalue), ctx);
            }
        };
    }

}
