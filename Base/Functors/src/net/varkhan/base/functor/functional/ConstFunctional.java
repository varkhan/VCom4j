package net.varkhan.base.functor.functional;

import net.varkhan.base.functor.Functional;


/**
 * <b></b>.
 * <p/>
 *
 * @author varkhan
 * @date 10/29/13
 * @time 12:15 PM
 */
public class ConstFunctional<A,C> implements Functional<A,C> {

    protected final double val;

    public ConstFunctional(double val) {
        this.val = val;
    }

    @Override
    public double invoke(A arg, C ctx) {
        return val;
    }

    public static <A,C> ConstFunctional<A,C> as(double val) { return new ConstFunctional<A,C>(val); }

    @Override
    public String toString() {
        return Double.toString(val);
    }

}
