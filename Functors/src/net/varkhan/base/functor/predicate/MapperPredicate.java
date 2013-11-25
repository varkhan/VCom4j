package net.varkhan.base.functor.predicate;

import net.varkhan.base.functor.Mapper;
import net.varkhan.base.functor.Predicate;


/**
* <b></b>.
* <p/>
*
* @author varkhan
* @date 11/24/13
* @time 7:03 PM
*/
public class MapperPredicate<A,C> implements Predicate<A,C> {
    protected final Mapper<Boolean,A,C> mapr;

    public MapperPredicate(Mapper<Boolean,A,C> mapr) {this.mapr=mapr;}

    @Override
    public boolean invoke(A arg, C ctx) {
        Boolean v=mapr.invoke(arg, ctx);
        return v==null ? false : v;
    }
}
