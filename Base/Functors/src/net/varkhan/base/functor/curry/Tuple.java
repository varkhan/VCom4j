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
public interface Tuple<L, _T extends _> extends _<L,_T> {

    public L lvalue();
    public _T _value();

    public Object[] values();

    public static class Value<L,_T extends _> implements Tuple<L,_T> {
        protected final Object[] values;
        public Value(L l, Object... values) { this.values = uncurry(l, values); }
        protected Value(Object[] values) { this.values = values; }
        @SuppressWarnings("unchecked")
        public L lvalue() { return (L) values[0]; }
        @SuppressWarnings("unchecked")
        public _T _value() { return (values==null||values.length<=1)?null:(_T) new Value(rcurry(values)); }
        public Object[] values() { return values; }
        protected static <L> Object[] uncurry(L l, Object[] values) {
            if(values==null||values.length==0) return new Object[]{l};
            Object[] v = new Object[1+values.length];
            v[0] = l;
            System.arraycopy(values,0,v,1,values.length);
            return v;
        }
        @SuppressWarnings("unchecked")
        protected static <L> L lcurry(Object[] values) {
            if(values==null||values.length==0) return null;
            return (L) values[0];
        }
        @SuppressWarnings("unchecked")
        protected static Object[] rcurry(Object[] values) {
            if(values==null||values.length<=1) return new Object[0];
            Object[] v = new Object[values.length-1];
            System.arraycopy(values,1,v,0,values.length-1);
            return v;
        }

        @Override
        public boolean equals(Object o) {
            if(this==o) return true;
            if(!(o instanceof Tuple)) return false;
            Tuple that=(Tuple) o;
            final int l = values.length;
            Object[] thisV = this.values();
            Object[] thatV = that.values();
            if (thatV.length!=l) return false;
            for (int i=0; i<l; i++) {
                Object thisO = thisV[i];
                Object thatO = thatV[i];
                if (!(thisO==null ? thatO==null : thisO.equals(thatO))) return false;
            }
            return true;
        }

        @Override
        public int hashCode() {
            if (values==null) return 0;
            int h = 1;
            for (Object v: values) h = 31*h + (v==null?0:v.hashCode());
            return h;
        }

        @Override
        public String toString() {
            StringBuilder buf = new StringBuilder();
            buf.append('(');
            boolean f = false;
            for(Object v: values) {
                if(f) f = false;
                else buf.append(',');
                buf.append(v);
            }
            buf.append(')');
            return buf.toString();
        }
    }

}
