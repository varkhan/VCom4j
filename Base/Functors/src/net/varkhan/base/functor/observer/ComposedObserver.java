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

}
