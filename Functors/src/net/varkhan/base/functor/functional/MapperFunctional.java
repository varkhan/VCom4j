package net.varkhan.base.functor.functional;

import net.varkhan.base.functor.Functional;
import net.varkhan.base.functor.Mapper;


/**
* <b></b>.
* <p/>
*
* @author varkhan
* @date 11/24/13
* @time 7:05 PM
*/
public class MapperFunctional<A,C> implements Functional<A,C> {
    protected final Mapper<Double,A,C> mapr;

    public MapperFunctional(Mapper<Double,A,C> mapr) {this.mapr=mapr;}

    public Mapper<Double,A,C> source() { return mapr; }

    @Override
    public double invoke(A arg, C ctx) {
        Double v=mapr.invoke(arg, ctx);
        return v==null ? Double.NaN : v;
    }
}
