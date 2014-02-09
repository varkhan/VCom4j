package net.varkhan.base.functor.expander;

import net.varkhan.base.functor.Expander;
import net.varkhan.base.functor.Mapper;

import java.util.Iterator;


/**
 * <b></b>.
 * <p/>
 *
 * @author varkhan
 * @date 2/11/12
 * @time 3:21 PM
 */
public class ComposedExpander<R,A,C> implements Expander<R,A,C> {

    protected final Mapper<R,Object,C> mapr;
    protected final Expander<Object,A,C> expr;

    @SuppressWarnings({ "unchecked" })
    public <U> ComposedExpander(Mapper<R,U,C> mapr, Expander<U,A,C> expr) {
        this.expr=(Expander<Object,A,C>) expr;
        this.mapr=(Mapper<R,Object,C>) mapr;
    }

    public Mapper<R,?,C> left() { return mapr; }
    public Expander<?,A,C> right() { return expr; }

    public Iterable<R> invoke(final A arg, final C ctx) {
        return new Iterable<R>() {
            @Override
            public Iterator<R> iterator() {
                final Iterator<Object> it = expr.invoke(arg, ctx).iterator();
                return new Iterator<R>() {
                    @Override
                    public boolean hasNext() { return it.hasNext(); }
                    @Override
                    public R next() { return mapr.invoke(it.next(),ctx); }
                    @Override
                    public void remove() { }
                };
            }
        };
    }

    @Override
    public String toString() {
        String rs = right().toString();
        String ls = left().toString();
        if("$".equals(rs)) return ls;
        if("$".equals(ls)) return rs;
        StringBuilder buf = new StringBuilder();
        int p1 = 0, p2;
        while(p1<rs.length() && (p2=rs.indexOf('$',p1))>=0) {
            buf.append(rs.substring(p1,p2)).append(ls);
            p1 = p2+1;
        }
        buf.append(rs.substring(p1,rs.length()));
        return buf.toString();
    }

}
