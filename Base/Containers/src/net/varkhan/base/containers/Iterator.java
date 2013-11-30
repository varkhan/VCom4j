package net.varkhan.base.containers;

import java.util.NoSuchElementException;


/**
 * <b>An iterator over the objects in a content container.</b>
 * <p/>
 * This interface has been copied from {@link java.util.Iterator} (which it extends),
 * for convenience in proximity of {@link Iterable}.
 * <p/>
 *
 * @param <Type> the type of the objects in the container
 *
 * @see java.util.Iterator
 * @see java.lang.Iterable#iterator()
 *
 * @author varkhan
 * @date 1/1/11
 * @time 3:50 AM
 */
public interface Iterator<Type> extends java.util.Iterator<Type> {

    /**
     * Indicates whether the iteration has more elements.
     * <p/>
     * (In other words, returns {@literal true} if {@link #next} would return an element
     * rather than throwing an exception.)
     *
     * @return {@literal true} if the iterator has more elements
     */
    public boolean hasNext();

    /**
     * Returns the next element in the iteration.
     *
     * @return the next element in the iteration
     *
     * @throws java.util.NoSuchElementException if the iteration has no more elements
     */
    public Type next();

    /**
     *
     * Removes from the underlying collection the last element returned by the iterator
     * (optional operation).
     * <p/>
     * This method can be called only once per call to {@link #next}. The behavior of an
     * iterator is unspecified if the underlying collection is modified while the iteration
     * is in progress in any way other than by calling this method.
     *
     * @throws java.lang.UnsupportedOperationException if the operation is not supported by this Iterator

     * @throws java.lang.IllegalStateException if the {@link #next} method has not yet been called, or the
     *         {@link #remove} method has already been called after the last call to the {@link #next} method
     */
    public void remove();


    /**********************************************************************************
     **  Static predefined iterators
     **/

    /**
     * An empty index iterator (whose {@code hasNext()} and {@code hasPrevious()}
     * methods always return {@literal false}, and {@code next()} returns {@literal null}.
     */
    public static final Iterator EMPTY=new Empty();

    /**
     * An empty index iterator (whose {@code hasNext()} and {@code hasPrevious()}
     * methods always return {@literal false}, and {@code next()} returns {@literal null}.
     */
    public static final class Empty<Type> implements Iterator<Type> {
        public boolean hasNext() { return false; }
        public Type next() { return null; }
        public void remove() { }
    }


    /**
     * A singleton iterator, that returns a single element.
     */
    public static final class Singleton<Type> implements Iterator<Type> {
        private final Type element;
        private volatile boolean available=true;

        /**
         * Create a singleton index iterator.
         *
         * @param e the single element this iterator returns
         */
        public Singleton(Type e) { element=e; }

        public boolean hasNext() { return available; }

        public Type next() {
            if(!available) throw new NoSuchElementException();
            available=false;
            return element;
        }

        public boolean hasPrevious() { return !available; }

        public void remove() { throw new UnsupportedOperationException(); }

    }

    /**
     * An enumeration iterator, that returns all elements in an array, in order.
     */
    public static final class Enumerate<Type> implements Iterator<Type> {
        private final Type[] elements;
        private volatile int pos = 0;

        /**
         * Create a range index iterator.
         *
         * @param e the array of all the indexes to return
         */
        public Enumerate(Type[] e) { elements = e; }

        public boolean hasNext() { return pos<elements.length; }

        public Type next() {
            if(pos>=elements.length) throw new NoSuchElementException();
            return elements[pos++];
        }

        public void remove() { throw new UnsupportedOperationException(); }

    }


    /**
     * A sequence iterator, that returns all the elements of a series of iterators, in order.
     */
    public static final class Sequence<Type> implements Iterator<Type> {
        private final Iterator<? extends Type>[] segments;
        private volatile int pos = -1;

        /**
         * Create a sequence iterator.
         *
         * @param s the array of all the subsequence iterators
         */
        public Sequence(Iterator<? extends Type>[] s) { segments = s; }

        public boolean hasNext() {
            int pos = this.pos;
            if(pos<0) pos = 0;
            while(pos<segments.length && !segments[pos].hasNext()) pos ++;
            return pos<segments.length && segments[pos].hasNext();
        }

        public Type next() {
            if(pos<0) pos = 0;
            while(pos<segments.length && !segments[pos].hasNext()) pos ++;
            if(pos>=segments.length) throw new NoSuchElementException();
            return segments[pos].next();
        }

        public void remove() {
            if(pos<0 || pos>=segments.length) throw new IllegalStateException();
            segments[pos].remove();
        }

    }

}
