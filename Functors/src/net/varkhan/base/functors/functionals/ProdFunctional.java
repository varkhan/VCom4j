package net.varkhan.base.functors.functionals;

import net.varkhan.base.functors.Functional;


/**
 * <b></b>.
 * <p/>
 *
 * @author varkhan
 * @date 2/11/12
 * @time 3:25 PM
 */
public class ProdFunctional<A,C> extends AggregateFunctional<A,C> {

    public ProdFunctional(Functional<A,C>... funcs) {
        super(funcs);
    }

    public double invoke(A arg, C ctx) {
        double s = 1;
        for(Functional<A,C> f: funcs) s *= f.invoke(arg, ctx);
        return s;
    }

}
