package net.varkhan.base.functor.functional;

import net.varkhan.base.functor.Functional;


/**
 * <b></b>.
 * <p/>
 *
 * @author varkhan
 * @date 10/30/13
 * @time 3:53 PM
 */
public class IdentFunctional<C> implements Functional<Double,C> {

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


}
