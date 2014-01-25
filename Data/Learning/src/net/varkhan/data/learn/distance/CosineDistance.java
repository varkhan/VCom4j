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
public class CosineDistance<T,C> extends Distance<WeightingSet<T>,C> {


    @Override
    @SuppressWarnings("unchecked")
    public double invoke(WeightingSet<T> lvalue, WeightingSet<T> rvalue, C ctx) {
        double nl=lvalue.weight();
        double nr=rvalue.weight();
        if(nl==0&&nr==0) return 0;
        if(nl==0||nr==0) return Double.POSITIVE_INFINITY;
        double dd = 0;
        double dl = 0;
        double dr = 0;
        for (T v : (Iterable<T>)lvalue) {
            double pl = dl * lvalue.weight(v);
            double pr = dr * rvalue.weight(v);
            dl += pl * pl;
            dd += pl * pr;
        }
        for (T v : (Iterable<T>)rvalue) {
            double pl = dl * lvalue.weight(v);
            double pr = dr * rvalue.weight(v);
            dr += pr * pr;
            dd += pl * pr;
        }
        double cos =  0.5*dd/(dl*dr);
        return Math.acos(cos);
    }

}
