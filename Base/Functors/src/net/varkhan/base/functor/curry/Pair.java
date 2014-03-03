package net.varkhan.base.functor.curry;

import net.varkhan.base.functor._;


/**
 * <b></b>.
 * <p/>
 *
 * @author varkhan
 * @date 12/1/13
 * @time 12:40 PM
 */
public interface Pair<L,R> extends Tuple<L,_<R,_>> {

    public L lvalue();
    public R rvalue();
    public Object[] values();

    public static class Value<L,R> extends Tuple.Value<L,_<R,_>> implements Pair<L,R> {
        public Value(_<L,? extends _<R,_>> t) { super(t); }
        public Value(L l, R r) { super(l,r); }
        @SuppressWarnings("unchecked")
        public R rvalue() { return (R) values[1]; }
    }

}
