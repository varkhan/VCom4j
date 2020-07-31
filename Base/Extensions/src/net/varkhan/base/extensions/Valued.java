package net.varkhan.base.extensions;


import org.omg.CORBA.Object;

import java.util.Objects;

/**
 * <b>An interface to denote "wrapped" values with added semantics.</b>
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
     * @return the wrapped value
     */
    public T value();


    /**
     * A base implementation of {@link Valued}
     *
     * @param <T> the type of the wrapped value
     */
    public abstract class Base<T> implements Valued<T> {
        private final T value;

        public Base(T value) { this.value = value; }

        @Override public T value() { return value; }
        @Override public String toString() { return Objects.toString(value()); }
        @Override public int hashCode() { return Objects.hashCode(value()); }
        public boolean equals(Object obj) {
            if (this == obj) return true;
            if (obj == null) return false;
            if (obj instanceof Valued) return Objects.equals(value(), ((Valued<?>) obj).value());
            return Objects.equals(value(),obj);
        }

    }

    /**
     * A default implementation of a {@link net.varkhan.base.extensions.Named Named} {@link Valued Valued}
     */
    public abstract class Named<T> extends net.varkhan.base.extensions.Named.Base implements Valued<T> {
        private final T val;

        protected Named(String name, T val) {
            super(name);
            this.val = val;
        }

        @Override public T value() { return val; }
        @Override public String toString() { return name() + "=" + value(); }

        @Override
        public boolean equals(java.lang.Object obj) {
            if(!(obj instanceof Valued.Named)) return false;
            Named<?> that = (Named<?>) obj;
            return Objects.equals(this.name(),that.name()) && Objects.equals(this.value(), that.value());
        }

        @Override public int hashCode() { return Objects.hash(name())*31+Objects.hash(value()); }
    }

}