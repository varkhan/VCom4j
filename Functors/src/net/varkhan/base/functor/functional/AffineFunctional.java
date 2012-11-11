package net.varkhan.base.functor.functional;

import net.varkhan.base.functor.Functional;


/**
 * <b></b>.
 * <p/>
 *
 * @author varkhan
 * @date 2/11/12
 * @time 3:28 PM
 */
public class AffineFunctional<A,C> extends TransformFunctional<A,C> {

    protected double offset;
    protected double scale;

    public AffineFunctional(Functional<A, C> func, double offset, double scale) {
        super(func);
        this.offset = offset;
        this.scale = scale;
    }

    public double invoke(A arg, C ctx) {
        if(func==null) return offset;
        return offset+scale*func.invoke(arg, ctx);
    }
}
