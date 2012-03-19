package net.varkhan.base.functors.functionals;

import net.varkhan.base.functors.Functional;


/**
 * <b></b>.
 * <p/>
 *
 * @author varkhan
 * @date 2/11/12
 * @time 3:23 PM
 */
public abstract class AggregateFunctional<A,C> implements Functional<A,C> {

    protected final Functional<A, C>[] funcs;

    public AggregateFunctional(Functional<A, C>... funcs) {
        this.funcs = funcs;
    }

    public Functional<A, C>[] components() {
        return funcs.clone();
    }

    public abstract double invoke(A arg, C ctx);

}
