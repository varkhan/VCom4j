package net.varkhan.base.functor.curry;

import net.varkhan.base.functor._;


/**
 * <b></b>.
 * <p/>
 *
 * @author varkhan
 * @date 12/1/13
 * @time 12:33 PM
 */
public interface CTuple<L, _T extends _> extends Tuple<L,_T> {

    public Class<L> ltype();
    public L lvalue();

    public _T _value();

    public Class<?>[] types();
    public Object[] values();

    public static class Value<L,_T extends _> extends Tuple.Value<L,_T> implements CTuple<L,_T> {
        protected final Class<?>[] types;
        public Value(Class<L> lc, Class<?>[] types, L l, Object[] values) {
            super(uncurry(l,values));
            this.types=uncurry(lc,types);
        }
        protected Value(Class<?>[] types, Object[] values) {
            super(values);
            this.types=types;
        }
        protected static <T> Class<?>[] uncurry(Class<T> l, Class<?>[] values) {
            if(values==null||values.length==0) return new Class[]{l};
            Class[] v = new Class[1+values.length];
            v[0] = l;
            System.arraycopy(values,0,v,1,values.length);
            return v;
        }
        @SuppressWarnings("unchecked")
        protected static <T> Class<T>[] rcurry(Class<T>[] values) {
            if(values==null||values.length<=1) return new Class[0];
            Class[] v = new Class[values.length-1];
            System.arraycopy(values,1,v,0,values.length-1);
            return v;
        }
        @SuppressWarnings("unchecked")
        public _T _value() { return (values==null||values.length<=1)?null:(_T) new CTuple.Value(rcurry(types),rcurry(values)); }
        @SuppressWarnings("unchecked")
        public Class<L> ltype() { return (Class<L>) types[0]; }
        public Class<?>[] types() { return types; }
    }

}
