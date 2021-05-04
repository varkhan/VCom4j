package net.varkhan.base.extensions;

import net.varkhan.base.extensions.type.Kind;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * <b>A type-checked {@link Function}</b>
 *
 * @param <T> the type of the input to the mapper
 * @param <R> the type of the result of the mapper
 *
 * @author varkhan
 * @date 4/5/19
 * @time 10:12 AM
 */
public interface Mapper<T,R> extends Function<T,R> {

    /**
     * A description of type of input accepted by the mapper.
     *
     * @return the Kind describing the logical type accepted by {@link #apply(Object)}
     */
    public Kind<T> from();

    /**
     * A description of type of result returned by the mapper.
     *
     * @return the Kind describing the logical type returned by {@link #apply(Object)}
     */
    public Kind<R> into();

    /**
     * Applies this mapper to the given input.
     * @param t the mapper's input
     * @return the mapper's result
     */
    @Override public R apply(T t);

    /**
     * A default implementation of a mapper
     *
     * @param <T>
     * @param <R>
     */
    public abstract static class Base<T,R> implements Mapper<T,R> {
        protected final Kind<T> from;
        protected final Kind<R> into;
        protected Base(Kind<T> from, Kind<R> into) { this.from = from; this.into = into; }
        @Override public Kind<T> from() { return from; }
        @Override public Kind<R> into() { return into; }
        @Override public abstract R apply(T t);
        @Override public String toString() { return this.getClass().getSimpleName()+"("+from().name()+"):"+into().name(); }
    }

    public static class Identity<T,R> extends Base<T,R> {
        protected Identity(Kind<T> fromKind, Kind<R> intoKind) { super(fromKind,intoKind); }
        @SuppressWarnings("unchecked")
        @Override public R apply(T t) { return (R) t; }
    }

    public static <T> Identity<T,T> identity(Kind<T> kind) {
        return new Identity<>(kind,kind);
    }

    public static <T extends R, R> Mapper<T[], R[]> array(Kind.ArrayKind<T,T[]> from, Kind.ArrayKind<R,R[]> into, final Mapper<T, R> mapper) {
        if(mapper == null) return new Identity<>(from, into);
        final Class<?> element =  (into instanceof Kind.PrimitiveArrayKind<?, ?>)
                ? ((Kind.PrimitiveArrayKind<?, ?>)into).element().primClass()
                : Object.class;
        return new Mapper.Base<T[], R[]>(from,into) {
            @SuppressWarnings("unchecked")
            @Override
            public R[] apply(T[] t) {
                // Silently support Nullable kinds
                if (t == null) return null;
                int l = java.lang.reflect.Array.getLength(t);
                R[] a = (R[]) java.lang.reflect.Array.newInstance(element,l);
                for(int i=0; i<l; i++) {
                    T e = (T) java.lang.reflect.Array.get(from, i);
                    java.lang.reflect.Array.set(a,i,mapper.apply(e));
                }
                return a;
            }
        };
    }

    public static <T,R> Mapper<List<T>, List<R>> list(Kind.ListKind<T> fromKind, Kind.ListKind<R> intoKind, final Mapper<T, R> mapper) {
        if(mapper == null) return new Identity<>(fromKind, intoKind);
        return new Mapper.Base<List<T>, List<R>>(fromKind, intoKind) {
            @Override
            public List<R> apply(List<T> t) {
                // Silently support Nullable kinds
                if (t == null) return null;
                return t.stream().map(mapper).collect(Collectors.toList());
            }
        };
    }

}
