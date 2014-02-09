package net.varkhan.base.functor.reducer;

import net.varkhan.base.functor.Mapper;
import net.varkhan.base.functor.Reducer;


/**
* <b></b>.
* <p/>
*
* @author varkhan
* @date 11/24/13
* @time 7:07 PM
*/
public class MapperReducer<R,A,C> implements Reducer<R,A,C> {
    protected final Mapper<R,Iterable<A>,C> mapr;

    public MapperReducer(Mapper<R,Iterable<A>,C> mapr) {this.mapr=mapr;}

    public Mapper<R,Iterable<A>,C> source() { return mapr; }

    public R invoke(Iterable<A> arg, C ctx) { return mapr.invoke(arg, ctx); }

    @Override
    public String toString() { return mapr.toString(); }

}
