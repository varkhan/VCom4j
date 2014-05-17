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
public final class Void implements _ {

    public static final Void VOID = new Void();

    protected static final Object[] EMPTY = {};

    protected Void() { }

    public Object lvalue() { return null; }
    public _ _value() { return null; }
    public Object rvalue() { return null; }
    public Object[] values() { return EMPTY; }

}
