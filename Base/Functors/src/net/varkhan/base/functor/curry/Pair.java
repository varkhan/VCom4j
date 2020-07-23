package net.varkhan.base.functor.curry;

import net.varkhan.base.functor.__;


/**
 * <b></b>.
 * <p/>
 *
 * @author varkhan
 * @date 12/1/13
 * @time 12:40 PM
 */
public interface Pair<L,R> extends Tuple<L, __<R, __<?,?>>> {

    public L lvalue();
    public R rvalue();
    public Object[] values();

    public static class Value<L,R> extends Tuple.Value<L, __<R, __<?,?>>> implements Pair<L,R> {
        public Value(__<L,? extends __<R, __<?,?>>> t) { super(t); }
        public Value(L l, R r) { super(l,r); }
        @SuppressWarnings("unchecked")
        public R rvalue() { return (R) values[1]; }
    }

}
