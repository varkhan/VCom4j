package net.varkhan.base.functor.curry;

import net.varkhan.base.functor.__;


/**
 * <b></b>.
 * <p/>
 *
 * @author varkhan
 * @date 12/1/13
 * @time 12:48 PM
 */
public interface CPair<L,R> extends Pair<L,R>, CTuple<L, __<R, __<?,?>>>, __<L, __<R, __<?,?>>> {

    public Class<L> ltype();
    public L lvalue();

    public Class<R> rtype();
    public R rvalue();

    public Class<?>[] types();
    public Object[] values();

    public static class Value<L,R> extends CTuple.Value<L, __<R, __<?,?>>> implements CPair<L,R> {
        public Value(CPair<L,R> t) { super(t); }
        public Value(Class<L> lc, Class<R> rc, L l, R r) { super(new Class[]{lc,rc}, new Object[]{l,r}); }
        @SuppressWarnings("unchecked")
        public Class<R> rtype() { return (Class<R>) types[1]; }
        @SuppressWarnings("unchecked")
        public R rvalue() { return (R) values[1]; }
    }

}
