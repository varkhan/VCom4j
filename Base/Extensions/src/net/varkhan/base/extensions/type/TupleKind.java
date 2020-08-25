package net.varkhan.base.extensions.type;

/**
 * <b>The logical type representation for a {@link Tuple}, as a Tuple of {@link Kind}s.</b>.
 * <p/>
 *
 * @param <V> The first (left-most) type of the sequence
 * @param <_T> The type of the remaining (right) Tuple (the remaining sequence of values)
 *
 * @author varkhan
 * @date 18/8/13
 * @time 06:47 PM
 */
public class TupleKind<V, _T extends Tuple<?,?>> extends Tuple.Chain<Kind<V>,TupleKind<?,?>> implements Kind<Tuple<V, _T>> {
    protected final String name;

    public TupleKind(String name, Kind<V> firstKind, TupleKind<?,?> nextKind) {
        super(firstKind,nextKind);
        StringBuilder buf = new StringBuilder(name);
        buf.append('(');
        boolean f = true;
        TupleKind<?,?> t = this;
        while(t!=null) {
            if(f) f = false;
            else buf.append(',');
            Kind<?> v = t.value();
            buf.append(v);
            t = t.next();
        }
        buf.append(')');
        this.name = buf.toString();
    }

    @Override
    public String name() { return name; }

    @Override
    public boolean isNullable() { return false; }

    @Override
    public <F> boolean isAssignableFrom(Kind<F> from) {
        if(!(from instanceof Tuple)) return !hasNext() && value().isAssignableFrom(from);
        Object thisVal = ((Tuple<?, ?>) from).value();
        Object nextVal = ((Tuple<?, ?>) from).next();
        if(nextVal==null) {
            return thisVal instanceof Kind && !((Tuple<?, ?>) from).hasNext() &&
                    value().isAssignableFrom((Kind<?>) thisVal);
        }
        return thisVal instanceof Kind && nextVal instanceof Kind &&
                value().isAssignableFrom((Kind<?>) thisVal) && next().isAssignableFrom((Kind<?>) nextVal);
    }

    @SuppressWarnings("unchecked")
    @Override
    public <F> Caster<F, Tuple<V, _T>> assignFrom(Kind<F> from) {
        if(!hasNext()) {
            final Caster<Object, V> thisCast;
            if(from instanceof Tuple)
                thisCast = (Caster<Object, V>) value().assignFrom((Kind<?>) ((Tuple<?, ?>) from).value());
            else
                thisCast = (Caster<Object, V>) value().assignFrom(from);
            return new BaseCaster<F, Tuple<V, _T>>(from, this) {
                @Override
                public Tuple<V, _T> apply(F f) {
                    return (Tuple<V, _T>) new Tuple.Single<V>(thisCast.apply(f));
                }
            };
        }
        if(!(from instanceof Tuple)) throw new ClassCastException("Cannot cast "+from.name()+" to "+name);
        Object thisVal = ((Tuple<?, ?>) from).value();
        Object nextVal = ((Tuple<?, ?>) from).next();
        if(!(thisVal instanceof Kind)) throw new ClassCastException("Cannot cast "+from.name()+" to "+name);
        final Caster<Object, V> thisCast = (Caster<Object, V>) value().assignFrom((Kind<V>) thisVal);
        final Caster<Object, _T> nextCast = (Caster<Object, _T>) (Caster<?, _T>) next().assignFrom((Kind<_T>) nextVal);
        return new BaseCaster<F, Tuple<V, _T>>(from, this) {
            @Override
            public Tuple<V, _T> apply(F f) {
                if(!(f instanceof Tuple)) throw new ClassCastException("Cannot cast "+from.name()+" to "+name);
                return new Chain<>(thisCast.apply(((Tuple<?,?>) f).value()), nextCast.apply(((Tuple<?,?>) f).next()));
            }
        };
    }

    @Override
    public String toString() { return name; }


    /**********************************************************************************
     ** Pair, Single and Void Tuple Kinds
     **/

    /**
     * <b>The logical type representation for a pair Tuple.</b>
     *
     * @param <V> the type of the first member
     * @param <U> the type of the second (and last) member
     */
    public static class Pair<V, U> extends TupleKind<V, Tuple.Single<U>> {
        public Pair(String name, Kind<V> kind, Single<U> nextKind) {
            super(name, kind, nextKind);
        }

        public Pair(String name, Kind<V> kind, Kind<U> lastKind) {
            this(name, kind, new TupleKind.Single<U>("",lastKind));
        }
    }

    /**
     * <b>The logical type representation for a singleton Tuple.</b>
     *
     * @param <V> the type of the only member
     */
    public static class Single<V> extends TupleKind<V, Tuple.Void> {
        public Single(String name, Kind<V> kind) {
            super(name, kind, null);
        }
    }

    /**
     * <b>A marker class for the terminal Tuple Kind with no members.</b>
     * <br/>
     * This class has no public constructor, and has only one instance.
     */
    public static final class Void extends TupleKind.Single<Tuple.Void> {
        protected Void() { super("void",null); }

        /**
         * <b>The only allowed concrete Kind for the terminal {@link Tuple.Void}.</b>
         */
        public static final Void KIND = new TupleKind.Void();
    }

}
