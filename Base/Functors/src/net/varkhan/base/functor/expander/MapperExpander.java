package net.varkhan.base.functor.expander;

import net.varkhan.base.functor.Expander;
import net.varkhan.base.functor.Mapper;


/**
* <b></b>.
* <p/>
*
* @author varkhan
* @date 11/24/13
* @time 7:07 PM
*/
public class MapperExpander<R,A,C> implements Expander<R,A,C> {
    protected final Mapper<Iterable<R>,A,C> mapr;

    public MapperExpander(Mapper<Iterable<R>,A,C> mapr) {this.mapr=mapr;}

    public Mapper<Iterable<R>,A,C> source() { return mapr; }

    public Iterable<R> invoke(A arg, C ctx) { return mapr.invoke(arg, ctx); }
}
