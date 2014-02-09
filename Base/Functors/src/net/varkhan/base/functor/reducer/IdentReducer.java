package net.varkhan.base.functor.reducer;

import net.varkhan.base.functor.Reducer;


/**
 * <b></b>.
 * <p/>
 *
 * @author varkhan
 * @date 11/24/13
 * @time 4:24 PM
 */
public class IdentReducer<A,C> implements Reducer<Iterable<A>,A,C> {

    public IdentReducer() { }

    public Iterable<A> invoke(Iterable<A> arg, C ctx) { return arg; }

    protected static final IdentReducer<Object,Object> ID=new IdentReducer<Object,Object>();

    @SuppressWarnings({ "unchecked" })
    public static <A,C> Reducer<Iterable<A>,A,C> as() { return (Reducer) ID;}

    @Override
    public String toString() {
        return "$";
    }

}
