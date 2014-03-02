package net.varkhan.base.functor.generator;

import net.varkhan.base.functor.Generator;
import net.varkhan.base.functor.Mapper;


/**
 * <b></b>.
 * <p/>
 *
 * @author varkhan
 * @date 2/11/12
 * @time 3:08 PM
 */
public class ComposedGenerator<R,C> implements Generator<R,C> {

    private final Mapper<R,Object,C> l;
    private final Generator<Object,C> r;

    @SuppressWarnings({ "unchecked" })
    public <T> ComposedGenerator(Mapper<R,? super T,C> l, Generator<? extends T,C> r) {
        this.l= (Mapper) l;
        this.r= (Generator) r;
    }

    public Mapper<R,?,C> left() { return l; }
    public Generator<?,C> right() { return r; }

    public R invoke(C ctx) {
        return l.invoke(r.invoke(ctx), ctx);
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
