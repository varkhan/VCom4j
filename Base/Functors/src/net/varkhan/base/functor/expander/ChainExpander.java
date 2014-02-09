package net.varkhan.base.functor.expander;

import net.varkhan.base.functor.Expander;

import java.util.Iterator;
import java.util.NoSuchElementException;


/**
 * <b></b>.
 * <p/>
 * @author varkhan
 * @date 11/5/13
 * @time 4:26 PM
 */
public class ChainExpander<T,S,C> implements Expander<T,S,C> {
    protected final Expander<T,/* U */ Object,C> l;
    protected final Expander</* U */ Object,S,C> r;

    @SuppressWarnings("unchecked")
    public <U> ChainExpander(Expander<T,U,C> l, Expander<U,S,C> r) {
        this.l=(Expander<T,Object,C>) l;
        this.r=(Expander<Object,S,C>) r;
    }

    public Expander<T,?,C> left() { return l; }
    public Expander<?,S,C> right() { return r; }

    @Override
    public Iterable<T> invoke(S src, C ctx) {
        return new ChainIterable<T>(l, r.invoke(src, ctx), ctx);
    }


    public static class ChainIterable<T> implements Iterable<T> {
        protected final Expander<T, /* U */ Object, /* C */ Object> exp;
        protected final Iterable</* U */ Object>                    itr;
        protected final /* C */ Object                              ctx;

        @SuppressWarnings("unchecked")
        public <U,C> ChainIterable(Expander<T,U,C> exp, Iterable<U> itr, C ctx) {
            this.exp=(Expander<T,Object,Object>) exp;
            this.itr=(Iterable<Object>) itr;
            this.ctx=ctx;
        }

        @Override
        public Iterator<T> iterator() {
            return new ChainIterator<T>(exp, itr.iterator(), ctx);
        }

    }


    public static class ChainIterator<T> implements Iterator<T> {
        protected final Expander<T, /* U */ Object, /* C */ Object> exp;
        protected final Iterator</* U */ Object>                    itr;
        protected final /* C */ Object                              ctx;
        protected volatile Iterator<T> next=null;

        @SuppressWarnings("unchecked")
        public <U,C> ChainIterator(Expander<T,U,C> exp, Iterator<U> itr, C ctx) {
            this.exp = (Expander<T, Object, Object>) exp;
            this.itr = (Iterator<Object>) itr;
            this.ctx = ctx;
        }

        @Override
        public boolean hasNext() {
            while(next==null || !next.hasNext()) {
                if(!itr.hasNext()) return false;
                Object val = itr.next();
                next = exp.invoke(val, ctx).iterator();
            }
            return true;
        }

        @Override
        public T next() {
            if(!hasNext()) throw new NoSuchElementException();
            return next.next();
        }

        @Override
        public void remove() { }
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
