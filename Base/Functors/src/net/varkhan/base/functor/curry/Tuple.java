package net.varkhan.base.functor.curry;

import net.varkhan.base.functor.__;

import java.util.function.Function;

/**
 * <b>An arbitrary sequence of values of arbitrary types.</b>.
 * <p/>
 *
 * @param <V> The first (left-most) type of the sequence
 * @param <_T> The type of the remaining (right) Tuple (the remaining sequence of values)
 *
 * @author varkhan
 * @date 12/1/13
 * @time 12:33 PM
 */
public interface Tuple<V, _T extends __<?,?>> extends __<V,_T> {

    public default int size() { return isLast() ? 1 : next().size()+1; }

    @SuppressWarnings("unchecked")
    public default <U> U value(int i) {
        __<?,?> t = this;
        while(i>0) {
            t = t.next();
            i --;
            if(t==null) throw new IndexOutOfBoundsException("Member index "+i+" is larger than this tuple size");
        }
        return (U) t.value();
    }


    /**
     * The first (left-most) value of the Tuple.
     *
     * @return a single value that is the first in the Tuple sequence
     */
    public V value();

    public default boolean isLast() { return next() == null; }

    /**
     * The Tuple containing the remaining (right of the left-most) values of the Tuple.
     *
     * @return a Tuple containing all the values in the Tuple minus the left-most
     */
    public _T next();

    public Object[] values();

    public static class Vector<L,_T extends __<?,?>> implements Tuple<L,_T> {
        protected final Object[] values;
        public Vector(__<L,? extends _T> t) { this.values = t.values(); }
        public Vector(L l, Object... values) { this.values = uncurry(l, values); }
        protected Vector(Object[] values) { this.values = values; }
        @SuppressWarnings("unchecked")
        public L value() { return (L) values[0]; }
        @SuppressWarnings("unchecked")
        public _T next() { return (values==null||values.length<=1)?null:(_T) new Vector<>(rcurry(values)); }
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
        protected static Object[] rcurry(Object[] values) {
            if(values==null||values.length<=1) return new Object[0];
            Object[] v = new Object[values.length-1];
            System.arraycopy(values,1,v,0,values.length-1);
            return v;
        }

        @Override
        public boolean equals(Object o) {
            if(this==o) return true;
            if(!(o instanceof Tuple<?,?>)) return false;
            Tuple<?,?> that=(Tuple<?,?>) o;
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
            boolean f = true;
            for(Object v: values) {
                if(f) f = false;
                else buf.append(',');
                buf.append(v);
            }
            buf.append(')');
            return buf.toString();
        }
    }


    /**********************************************************************************
     ** Chain Tuple
     **/

    /**
     * An n-ary Tuple implementation based on member recursion
     *
     * @param <V> the type of the first member
     * @param <_T> the type definition of the trailing Tuple
     */
    public static class Chain<V, _T extends __<?,?>> implements Tuple<V, _T> {
        private final V val;
        private final _T next;

        public Chain(V val, _T next) {
            this.val = val;
            this.next = next;
        }

        public Chain(__<V, _T> t) {
            this.val = t.value();
            this.next = next();
        }

        @Override public int size() { return (next==null) ? 1 : next.size()+1; }

        @SuppressWarnings("unchecked")
        @Override
        public <U> U value(int i) {
            __<?,?> t = this;
            int p = 0;
            while(t!=null && p<i) {
                p++;
                t = t.next();
            }
            if(t==null) throw new IllegalArgumentException("Member index "+i+" is larger than this tuple size");
            return (U) t.value();
        }

        @Override public V value() { return val; }
        @Override public _T next() { return next; }
        @Override public boolean isLast() { return next==null; }

        @Override
        public Object[] values() {
            Object[] vs = new Object[size()];
            __<?,?> t = this;
            int p = 0;
            while(t!=null) {
                vs[p++] = t.value();
                t = t.next();
            }
            return vs;
        }

        @Override
        public boolean equals(Object o) {
            if(this==o) return true;
            if(!(o instanceof Tuple<?,?>)) return false;
            __<?,?> tt = this;
            __<?,?> to = (Tuple<?,?>) o;
            while(tt!=null && to!=null) {
                if(tt==to) return true;
                Object vt = tt.value();
                Object vo = to.value();
                if (!(vt==null ? vo==null : vt.equals(vo))) return false;
                tt = tt.next();
                to = to.next();
            }
            return tt==null && to==null;
        }

        @Override
        public int hashCode() {
            int h = 1;
            __<?,?> t = this;
            while(t!=null) {
                Object v = t.value();
                h = 31*h + (v==null?0:v.hashCode());
                t = t.next();
            }
            return h;
        }

        @Override
        public String toString() {
            StringBuilder buf = new StringBuilder();
            buf.append('(');
            boolean f = true;
            __<?,?> t = this;
            while(t!=null) {
                if(f) f = false;
                else buf.append(',');
                Object v = t.value();
                buf.append(v);
                t = t.next();
            }
            buf.append(')');
            return buf.toString();
        }

        public static <A,R,C extends Tuple<?,?>> Function<Tuple<A,C>,R> wrapA(Function<A,R> fn, C c) {
            return t -> fn.apply(t.value());
        }
        public static <A,R,C extends Tuple<?,?>> Function<A,Tuple<R,C>> wrapR(Function<A,R> fn, C c) {
            return t -> new Chain<>(fn.apply(t),c);
        }
        public static <A,R,C extends Tuple<?,?>> Function<Tuple<A,C>,Tuple<R,C>> wrapAR(Function<A,R> fn, C c) {
            return t -> new Chain<>(fn.apply(t.value()),c);
        }
        public static <A,R,C extends Tuple<?,?>> Function<Tuple<A,C>,Tuple<R,C>> wrapARC(Function<A,R> fn) {
            return t -> new Chain<>(fn.apply(t.value()),t.next());
        }
        public static <A,R,C extends Tuple<?,?>> Function<A,R> unwrapA(Function<Tuple<A,C>,R> fn, C c) {
            return t -> fn.apply(new Chain<>(t,c));
        }
        public static <A,R,C extends Tuple<?,?>> Function<A,R> unwrapR(Function<A,Tuple<R,C>> fn, C c) {
            return t -> fn.apply(t).value();
        }
        public static <A,R,C extends Tuple<?,?>> Function<A,R> unwrapAR(Function<Tuple<A,C>,Tuple<R,C>> fn, C c) {
            return t -> fn.apply(new Chain<>(t,c)).value();
        }
    }

    /**
     * A pair Tuple implementation
     *
     * @param <V> the type of the first member
     * @param <U> the type of the second (and last) member
     */
    public static class Pair<V,U> extends Chain<V, Single<U>> {
        public Pair(V val, Single<U> next) { super(val, next); }
        public Pair(V val, U next) { super(val, new Single<>(next)); }
    }


    /**********************************************************************************
     ** Single and Void Tuple
     **/

    /**
     * A singleton Tuple implementation
     *
     * @param <V> the type of the only member
     */
    public static class Single<V> extends Chain<V,Void> {
        public Single(V val) { super(val,null); }
        public Single(__<V,? extends __<?,?>> t) { this(t.value()); }

        @Override public int size() { return 1; }

        @SuppressWarnings("unchecked")
        @Override public <U> U value(int i) {
            if(i>0) throw new IndexOutOfBoundsException("Member index "+i+" is larger than this tuple size");
            return (U) value();
        }

        @Override public Void next() { return null; }
        @Override public boolean isLast() { return false; }

        public static <A,R> Function<Tuple<A,Void>,R> wrapA(Function<A,R> fn) {
            return t -> fn.apply(t.value());
        }
        public static <A,R> Function<A,Tuple<R,Void>> wrapR(Function<A,R> fn) {
            return t -> new Single<R>(fn.apply(t));
        }
        public static <A,R> Function<Tuple<A,?>,Tuple<R,Void>> wrapAR(Function<A,R> fn) {
            return t -> new Single<>(fn.apply(t.value()));
        }
        public static <A,R> Function<A,R> unwrapA(Function<Tuple<A,Void>,R> fn) {
            return t -> fn.apply(new Single<>(t));
        }
        public static <A,R> Function<A,R> unwrapR(Function<A,Tuple<R,Void>> fn) {
            return t -> fn.apply(t).value();
        }
        public static <A,R> Function<A,R> unwrapAR(Function<Tuple<A,Void>,Tuple<R,Void>> fn) {
            return t -> fn.apply(new Single<>(t)).value();
        }
    }

    /**
     * A marker class for the terminal Tuple with no members
     */
    public static final class Void extends Single<java.lang.Void> {
        private Void() { super((java.lang.Void) null); }
        @Override public int size() { return 0; }
        @Override public java.lang.Void value() { return null; }
        @Override public <U> U value(int i) {
            throw new IllegalArgumentException("Member index "+i+" is larger than this tuple size");
        }
        @Override public Void next() { return null; }
        @Override public boolean isLast() { return false; }
    }

    /**
     * The only allowed concrete value of the terminal Tuple
     */
    public static final Void VOID = null; //new Void();

}
