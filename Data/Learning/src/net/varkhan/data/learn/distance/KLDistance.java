package net.varkhan.data.learn.distance;

import net.varkhan.base.containers.set.WeightingSet;


/**
 * <b></b>.
 * <p/>
 *
 * @author varkhan
 * @date 12/7/13
 * @time 5:13 PM
 */
public class KLDistance<T,C> extends Distance<WeightingSet<T>,C> {


    @Override
    @SuppressWarnings("unchecked")
    public double invoke(WeightingSet<T> lvalue, WeightingSet<T> rvalue, C ctx) {
        double nl = lvalue.weight();
        double nr = rvalue.weight();
        if(nl==0&&nr==0) return 0;
        if(nl==0||nr==0) return Double.POSITIVE_INFINITY;
        nl = 1.0/nl;
        nr = 1.0/nr;
        double kl = 0;
        for (T v : (Iterable<T>)lvalue) {
            double pl = nl * lvalue.weight(v);
            double pr = nr * rvalue.weight(v);
            if (pl == 0 || pr == 0) return Double.POSITIVE_INFINITY;
            else kl += (pl - pr) * Math.log(pl / pr);
        }
        for (T v : (Iterable<T>)rvalue) {
            double pl = nl * lvalue.weight(v);
            double pr = nr * rvalue.weight(v);
            if (pl == 0 || pr == 0) return Double.POSITIVE_INFINITY;
            else kl += (pl - pr) * Math.log(pl / pr);
        }
        return 0.5 * kl;
    }

}
