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
public interface Single<L> extends Tuple<L,_> {

    public L lvalue();
    public Object[] values();

    public static class Value<L> extends Tuple.Value<L,_> implements Single<L> {
        public Value(_<L,? extends _> t) { super(t); }
        public Value(L l) { super(l); }
    }

}
