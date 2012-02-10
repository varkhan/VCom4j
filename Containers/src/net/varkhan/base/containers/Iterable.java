/**
 *
 */
package net.varkhan.base.containers;


/**
 * <b>A view of the objects in a content container.</b>
 * <p/>
 * Convenience object to access an {@link Iterator} over some subset
 * of indexes from a container, allowing overriding classes to return more
 * specific element types.
 * <p/>
 * It provides an API equivalent to {@link java.lang.Iterable#iterator()}, which is defined as
 * returning an Iterator on its exact generic type, preventing overriding classes to return an
 * Iterator over a more specific type, and effectively preventing subclassing of Iterator by an
 * other generic interface.
 * <p/>
 *
 * @param <Type> the type of the objects in the container
 *
 * @see java.util.Iterator
 * @see java.lang.Iterable#iterator()
 *
 * @author varkhan
 * @date Mar 13, 2009
 * @time 8:50:55 PM
 */
public interface Iterable<Type> extends java.lang.Iterable/*<Type>*/  {

    /**
     * Iterates over all elements in the container.
     *
     * @return an iterator over all the elements stored in the container
     */
    public Iterator<? extends Type> iterator();

}
