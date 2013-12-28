package net.varkhan.base.functor.mapper;

import net.varkhan.base.functor.Mapper;
import net.varkhan.base.functor.Ordinal;


/**
* <b></b>.
* <p/>
*
* @author varkhan
* @date 11/24/13
* @time 7:03 PM
*/
public class OrdinalMapper<A,C> implements Mapper<Long,A,C> {
    protected final Ordinal<A,C> ordn;

    public OrdinalMapper(Ordinal<A,C> ordn) {this.ordn=ordn;}

    public Ordinal<A,C> source() { return ordn; }

    public Long cardinal() { return ordn.cardinal(); }

    @Override
    public Long invoke(A arg, C ctx) {
        return ordn.invoke(arg, ctx);
    }
}
