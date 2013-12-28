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

}
