/**
 *
 */
package net.varkhan.base.containers;

/**
 * <b>A view of the indexes in an Indexed content container.</b>
 * <p/>
 * Convenience object to access an {@link Index} over some
 * subset of indexes from an {@link Indexed} container.
 * <p/>
 *
 * @author varkhan
 * @date Mar 11, 2009
 * @time 1:13:49 AM
 */
public interface Indexable {

    /**
     * The {@link Index} over the contents of this view.
     *
     * @return an Index allowing forward and backward traversal of this view
     */
    public Index indexes();

    /**
     * Returns the number of elements in this container.
     *
     * @return the number of entries (elements and related indexes) stored in this list
     */
    public long size();

    /**
     * Indicates whether this container is empty.
     *
     * @return {@literal true} if this container contains no element,
     *         {@literal false} otherwise
     */
    public boolean isEmpty();

    /**
     * Indicates whether an index is valid (contained in this view).
     *
     * @param index a unique identifier
     *
     * @return {@literal true} if an element is available at this index,
     *         or {@literal false} if no element is available at this index
     */
    public boolean has(long index);


    /**********************************************************************************
     **  Static predefined iterators
     **/

    /**
     * An empty Indexable, that always yield an empty iterator.
     *
     * @see Index.Empty
     */
    public static final Indexable EMPTY = new Empty();

    /**
     * An empty Indexable, that always yield an empty iterator.
     *
     * @see Index.Empty
     */
    public static final class Empty implements Indexable {
        public Index.Empty indexes() { return new Index.Empty(); }
        public long size() { return 0; }
        public boolean isEmpty() { return true; }
        public boolean has(long index) { return false; }
    }


    /**
     * A singleton iterable, that returns a Singleton iterator,
     * iterating on a single index.
     *
     * @see Index.Singleton
     */
    public static final class Singleton implements Indexable {
        private final long element;

        /**
         * Create a singleton iterable.
         *
         * @param e the single index this iterable provides
         */
        public Singleton(long e) { element=e; }

        public Index.Singleton indexes() { return new Index.Singleton(element); }
        public long size() { return 1; }
        public boolean isEmpty() { return false; }
        public boolean has(long index) { return index==element; }
    }

    /**
     * A range iterable, that returns a Range iterator,
     * iterating on a range of index.
     *
     * @see Index.Range
     */
    public static final class Range implements Indexable {
        private final long min;
        private final long max;

        /**
         * Create a range iterable.
         *
         * @param min the lowest index to return
         * @param max the highest index to return
         */
        public Range(long min, long max) { this.min = min; this.max = max; }

        public Index.Range indexes() { return new Index.Range(min,max); }
        public long size() { return max-min+1; }
        public boolean isEmpty() { return min>max; }
        public boolean has(long index) { return index>=min && index<=max; }
    }

    /**
     * An enumeration iterable, that returns an Enumeration iterator,
     * iterating on the indexes in an array.
     *
     * @see Index.Enumerate
     */
    public static final class Enumerate implements Indexable {
        private final long[] elements;

        /**
         * Create an enumerated iterable.
         *
         * @param e the array of elements this iterable provides
         */
        public Enumerate(long... e) { elements=e; }

        public Index indexes() {
            if(elements==null || elements.length==0) return new Index.Empty();
            else return new Index.Enumerate(elements);
        }
        public long size() {
            return elements==null ? 0 : elements.length;
        }
        public boolean isEmpty() {
            return elements==null || elements.length==0;
        }
        public boolean has(long index) {
            if(elements!=null) for(long e: elements) { if(index==e) return true; }
            return false;
        }
    }

    /**
     * A sequence iterable, that returns a Sequence iterator,
     * iterating on the indexes in an array.
     *
     * @see Index.Enumerate
     */
    public static final class Sequence implements Indexable {
        private final Indexable[] segments;

        /**
         * Create a sequence iterable.
         *
         * @param s the array of Indexables
         */
        public Sequence(Indexable... s) { segments=s; }

        public Index indexes() {
            if(segments==null || segments.length==0) return new Index.Empty();
            Index[] it = new Index[segments.length];
            for(int i=0; i<segments.length; i++) it[i] = segments[i].indexes();
            return new Index.Sequence(it);
        }
        public long size() {
            if(segments==null) return 0;
            long s = 0;
            for(Indexable segment : segments) s+=segment.size();
            return s;
        }
        public boolean isEmpty() {
            if(segments==null) return true;
            for(Indexable segment : segments) if(!segment.isEmpty()) return false;
            return true;
        }
        public boolean has(long index) {
            if(segments!=null) for(Indexable s: segments) { if(s.has(index)) return true; }
            return false;
        }
    }

}
