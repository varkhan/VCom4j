package net.varkhan.base.functor.ordinal;

import net.varkhan.base.functor.Mapper;
import net.varkhan.base.functor.Ordinal;


/**
 * <b></b>.
 * <p/>
 *
 * @author varkhan
 * @date 2/11/12
 * @time 3:21 PM
 */
public class ComposedOrdinal<A,C> implements Ordinal<A,C> {

    protected final Mapper<Object,A,C> mapr;
    protected final Ordinal<Object,C>  ordn;

    @SuppressWarnings({ "unchecked" })
    public <R> ComposedOrdinal(Ordinal<R,C> ordn, Mapper<R,A,C> mapr) {
        this.ordn=(Ordinal<Object,C>) ordn;
        this.mapr=(Mapper<Object,A,C>) mapr;
    }

    public Ordinal<?,C> left() { return ordn; }

    public Mapper<?,A,C> right() { return mapr; }

    public long cardinal() { return ordn.cardinal(); }

    public long invoke(A arg, C ctx) {
        return ordn.invoke(mapr.invoke(arg, ctx), ctx);
    }

    @Override
    public String toString() {
        String rs = right().toString();
        String ls = left().toString();
        if("$".equals(rs)) return ls;
        if("$".equals(ls)) return rs;
        StringBuilder buf = new StringBuilder();
        int p1 = 0, p2;
        while(p1<ls.length() && (p2=ls.indexOf('$',p1))>=0) {
            buf.append(ls.substring(p1,p2)).append(rs);
            p1 = p2+1;
        }
        buf.append(ls.substring(p1,ls.length()));
        return buf.toString();
    }

}
