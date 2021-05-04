package net.varkhan.base.extensions;

import java.util.Objects;

/**
 * <b>An interface to denote "wrapped" values with added semantics</b>
 * <br/>
 *
 * @param <T> the type of the wrapped value
 *
 * @author varkhan
 * @date 2/8/11
 * @time 9:37 PM
 */
public interface Valued<T> {

    /**
     * The actual value contained in this wrapper.
     *
     * @return the wrapped value
     */
    public T value();


    /**
     * <b>A default implementation of {@link Valued}.</b>
     *
     * @param <T> the type of the wrapped value
     */
    public abstract class Base<T> implements Valued<T> {
        protected final T value;

        protected Base(T value) { this.value = value; }

        @Override public T value() { return value; }
        @Override public String toString() { return Objects.toString(value()); }
        @Override public int hashCode() { return Objects.hashCode(value()); }
        @Override public boolean equals(Object obj) {
            if (this == obj) return true;
            if (obj instanceof Valued) return Objects.equals(value(), ((Valued<?>) obj).value());
            return Objects.equals(value(),obj);
        }

    }

    /**
     * <b>A default implementation of a {@link net.varkhan.base.extensions.Named Named} {@link Valued Valued}.</b>
     */
    public abstract class Named<T> extends net.varkhan.base.extensions.Named.Base implements Valued<T> {
        protected final T value;

        protected Named(String name, T value) {
            super(name);
            this.value = value;
        }

        @Override public T value() { return value; }
        @Override public String toString() { return name() + "=" + value(); }

        @Override public boolean equals(Object obj) {
            if(!(obj instanceof Valued.Named)) return false;
            Named<?> that = (Named<?>) obj;
            return Objects.equals(this.name(),that.name()) && Objects.equals(this.value(), that.value());
        }

        @Override public int hashCode() { return Objects.hash(name())*31+Objects.hash(value()); }
    }

}