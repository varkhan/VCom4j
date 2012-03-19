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
public class MaxFunctional<A,C> extends AggregateFunctional<A,C> {

    public MaxFunctional(Functional<A,C>... funcs) {
        super(funcs);
    }

    public double invoke(A arg, C ctx) {
        double m = -Double.MAX_VALUE;
        for(Functional<A,C> f: funcs) {
            double v = f.invoke(arg, ctx);
            if(v>m) m = v;
        }
        return m;
    }

}
