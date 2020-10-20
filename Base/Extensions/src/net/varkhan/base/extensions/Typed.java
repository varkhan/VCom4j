package net.varkhan.base.extensions;

/**
 * <b>An interface to denote "typed" objects possessing type information</b>.
 * <p/>
 *
 * @author varkhan
 * @date 1/8/11
 * @time 11:55 PM
 */
public interface Typed<T> {

    /**
     * The objet's type information
     *
     * @return the object type
     */
    public T type();


    /**
     * <b>A default implementation of {@link Typed}.</b>
     *
     * @param <T> the type of the type information
     */
    public abstract class Base<T> implements Typed<T> {
        protected final T type;
        protected Base(T type) { this.type = type; }
        @Override public T type() { return type; }
    }

}
