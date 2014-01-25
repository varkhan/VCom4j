package net.varkhan.data.learn.ensemble;

import net.varkhan.data.learn.Regressor;


/**
 * <b></b>.
 * <p/>
 *
 * @author varkhan
 * @date 12/26/13
 * @time 6:57 PM
 */
public class AverageRegressor<T,C> implements Regressor<T,C> {
    protected final Regressor<T,C>[] components;

    public AverageRegressor(Regressor<T,C>... components) {
        this.components=components;
    }

    @Override
    public double invoke(T obs, C ctx) {
        double cnf=0;
        double avg=0;
        for(Regressor<T,C> c : components) {
            double k = c.invoke(obs, ctx);
            double p = c.confidence(k, obs, ctx);
            cnf += p;
            avg += k*p;
        }
        return avg/cnf;
    }

    @Override
    public double confidence(double val, T obs, C ctx) {
        double p = 0;
        for(Regressor<T,C> c: components) {
            p += c.confidence(val, obs, ctx);
        }
        return p/components.length;
    }

}
