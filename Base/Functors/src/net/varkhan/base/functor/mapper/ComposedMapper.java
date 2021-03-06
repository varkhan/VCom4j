package net.varkhan.base.functor.mapper;

import net.varkhan.base.functor.Mapper;


/**
 * <b></b>.
 * <p/>
 *
 * @author varkhan
 * @date 2/11/12
 * @time 3:08 PM
 */
public class ComposedMapper<R,A,C> implements Mapper<R,A,C> {

    protected final Mapper<R,Object,C> l;
    protected final Mapper<Object,A,C> r;

    @SuppressWarnings({ "unchecked" })
    public <T> ComposedMapper(Mapper<R, ? super T, C> l, Mapper<? extends T, A, C> r) {
        this.l= (Mapper) l;
        this.r= (Mapper) r;
    }

    public Mapper<R,?,C> left() { return l; }
    public Mapper<?,A,C> right() { return r; }

    public R invoke(A arg, C ctx) {
        return l.invoke(r.invoke(arg, ctx), ctx);
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
