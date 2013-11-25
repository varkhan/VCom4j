package net.varkhan.base.functor.mapper;

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
class PredicateMapper<A,C> implements Mapper<Boolean,A,C> {
    protected final Predicate<A,C> pred;

    public PredicateMapper(Predicate<A,C> pred) {this.pred=pred;}

    @Override
    public Boolean invoke(A arg, C ctx) {
        return pred.invoke(arg, ctx);
    }
}
