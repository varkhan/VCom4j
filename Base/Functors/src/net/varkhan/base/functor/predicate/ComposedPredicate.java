package net.varkhan.base.functor.predicate;

import net.varkhan.base.functor.Mapper;
import net.varkhan.base.functor.Predicate;


/**
 * <b></b>.
 * <p/>
 *
 * @author varkhan
 * @date 2/11/12
 * @time 3:21 PM
 */
public class ComposedPredicate<A,C> implements Predicate<A,C> {

    protected final Mapper<Object,A,C> mapr;
    protected final Predicate<Object,C> pred;

    @SuppressWarnings({ "unchecked" })
    public <R> ComposedPredicate(Predicate<R,C> pred, Mapper<R,A,C> mapr) {
        this.pred = (Predicate<Object, C>) pred;
        this.mapr = (Mapper<Object, A, C>) mapr;
    }

    public Predicate<?,C> left() { return pred; }
    public Mapper<?,A,C> right() { return mapr; }

    public boolean invoke(A arg, C ctx) {
        return pred.invoke(mapr.invoke(arg, ctx), ctx);
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
