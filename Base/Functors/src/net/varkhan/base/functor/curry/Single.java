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
public interface Single<L> extends Tuple<L, __<?,?>> {

    public L lvalue();
    public Object[] values();

    public static class Value<L> extends Tuple.Value<L, __<?,?>> implements Single<L> {
        public Value(__<L,? extends __<?,?>> t) { super(t); }
        public Value(L l) { super(l); }
    }

}
