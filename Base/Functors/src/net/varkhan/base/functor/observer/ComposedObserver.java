package net.varkhan.base.functor.observer;

import net.varkhan.base.functor.Mapper;
import net.varkhan.base.functor.Observer;


/**
 * <b></b>.
 * <p/>
 *
 * @author varkhan
 * @date 2/11/12
 * @time 3:08 PM
 */
public class ComposedObserver<A,C> implements Observer<A,C> {

    private final Observer<Object,C> l;
    private final Mapper<Object,A,C> r;

    @SuppressWarnings({ "unchecked" })
    public <T> ComposedObserver(Observer<? super T,C> l, Mapper<? extends T,A,C> r) {
        this.l=(Observer) l;
        this.r=(Mapper) r;
    }

    public Observer<?,C> left() { return l; }

    public Mapper<?,A,C> right() { return r; }

    public void invoke(A arg, C ctx) {
        l.invoke(r.invoke(arg, ctx), ctx);
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
