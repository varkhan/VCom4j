package net.varkhan.data.learn;

import net.varkhan.base.functor.Functional;


/**
 * <b></b>.
 * <p/>
 *
 * @author varkhan
 * @date 11/30/13
 * @time 12:48 PM
 */
public interface Regressor<T,C> extends Functional<T,C> {

    public double invoke(T obs, C ctx);

    public double confidence(double val, T obs, C ctx);

}
