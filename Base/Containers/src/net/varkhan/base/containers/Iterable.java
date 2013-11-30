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


    /**********************************************************************************
     **  Static predefined Iterables
     **/

    /**
     * An empty Iterable, that always yield an empty iterator.
     *
     * @see Iterator.Empty
     */
    public static final class Empty<Type> implements Iterable<Type> {
        public Iterator<Type> iterator() { return new Iterator.Empty<Type>(); }
    }


    /**
     * A singleton Iterable, that returns a Singleton iterator,
     * iterating on a single index.
     *
     * @see Iterator.Singleton
     */
    public static final class Singleton<Type> implements Iterable<Type> {
        private final Type element;

        /**
         * Create a singleton Iterable.
         *
         * @param e the single index this Iterable provides
         */
        public Singleton(Type e) { element=e; }

        public Iterator<Type> iterator() { return new Iterator.Singleton<Type>(element); }
    }

    /**
     * An enumeration Iterable, that returns an Enumeration iterator,
     * iterating on the indexes in an array.
     *
     * @see Iterator.Enumerate
     */
    public static final class Enumerate<Type> implements Iterable<Type> {
        private final Type[] elements;

        /**
         * Create an enumerated Iterable.
         *
         * @param e the array of elements this Iterable provides
         */
        public Enumerate(Type[] e) { elements=e; }

        public Iterator<Type> iterator() { return new Iterator.Enumerate<Type>(elements); }
    }

    /**
     * A sequence Iterable, that returns a Sequence iterator,
     * iterating on the indexes in an array.
     *
     * @see Iterator.Enumerate
     */
    public static final class Sequence<Type> implements Iterable<Type> {
        private final Iterable<Type>[] segments;

        /**
         * Create a sequence Iterable.
         *
         * @param s the array of Iterables
         */
        public Sequence(Iterable<Type>... s) { segments=s; }

        public Iterator<Type> iterator() {
            if(segments==null || segments.length==0) return new Iterator.Empty<Type>();
            @SuppressWarnings("unchecked")
            Iterator<? extends Type>[] it = new Iterator[segments.length];
            for(int i=0; i<segments.length; i++) it[i] = segments[i].iterator();
            return new Iterator.Sequence<Type>(it);
        }
    }

}
