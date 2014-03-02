package net.varkhan.base.functor.functional;

import net.varkhan.base.functor.Functional;
import net.varkhan.base.functor.Mapper;


/**
 * <b></b>.
 * <p/>
 *
 * @author varkhan
 * @date 2/11/12
 * @time 3:21 PM
 */
public class ComposedFunctional<A,C> implements Functional<A,C> {

    protected final Mapper<Object,A,C>   mapr;
    protected final Functional<Object,C> func;

    @SuppressWarnings({ "unchecked" })
    public <R> ComposedFunctional(Functional<R,C> func, Mapper<R,A,C> mapr) {
        this.func=(Functional<Object,C>) func;
        this.mapr=(Mapper<Object,A,C>) mapr;
    }

    public Functional<?,C> left() { return func; }
    public Mapper<?,A,C> right() { return mapr; }

    public double invoke(A arg, C ctx) {
        return func.invoke(mapr.invoke(arg, ctx), ctx);
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
