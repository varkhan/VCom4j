package net.varkhan.base.functor.mapper;

import net.varkhan.base.functor.Functional;
import net.varkhan.base.functor.Mapper;


/**
* <b></b>.
* <p/>
*
* @author varkhan
* @date 11/24/13
* @time 7:06 PM
*/
class FunctionalMapper<A,C> implements Mapper<Double,A,C> {
    protected final Functional<A,C> func;

    public FunctionalMapper(Functional<A,C> func) {this.func=func;}

    @Override
    public Double invoke(A arg, C ctx) {
        return func.invoke(arg, ctx);
    }
}
