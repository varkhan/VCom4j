package net.varkhan.base.functor.curry;

import net.varkhan.base.functor._;


/**
 * <b></b>.
 * <p/>
 *
 * @author varkhan
 * @date 12/1/13
 * @time 12:48 PM
 */
public interface CSingle<L> extends Single<L>, CTuple<L,_>, _<L,_> {

    public Class<L> ltype();
    public L lvalue();

    public Class<?>[] types();
    public Object[] values();

    public static class Value<L> extends CTuple.Value<L,_> implements CSingle<L> {
        public Value(CSingle<L> t) { super(t); }
        public Value(Class<L> lc, L l) { super(new Class[]{lc}, new Object[]{l}); }
    }

}
