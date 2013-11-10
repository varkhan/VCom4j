/**
 *
 */
package net.varkhan.base.containers;

/**
 * <b>A bidirectional iterator over an Indexed content container.</b>
 * <p/>
 * IndexIterators enumerate valid {@code long} indexes for the contents
 * of an {@link Indexed} container, allowing both forward and backward
 * traversal.
 * <p/>
 *
 * @author varkhan
 * @date Mar 11, 2009
 * @time 12:55:58 AM
 */
public interface Index {

    /**
     * Returns the current identifier this iterators points to.
     *
     * @return the current identifier
     */
    public long current();

    /**
     * Returns true if the iteration has identifiers after this one.
     *
     * @return true if the iteration has identifiers after this one
     */
    public boolean hasNext();

    /**
     * Returns the next identifier in the iteration.
     *
     * @return the next identifier in the iteration
     */
    public long next();

    /**
     * Returns true if the iteration has identifiers before this one.
     *
     * @return true if the iteration has identifiers before this one
     */
    public boolean hasPrevious();

    /**
     * Returns the previous identifier in the iteration.
     *
     * @return the previous identifier in the iteration
     */
    public long previous();


    /**********************************************************************************
     **  Static predefined iterators
     **/

    /**
     * An empty index iterator (whose {@code hasNext()} and {@code hasPrevious()}
     * methods always return {@literal false}, and {@code current()}, {@code next()}
     * and {@code previous()} methods throw an {@link InvalidIndexException}).
     */
    public static final Index EMPTY=new Empty();

    /**
     * An empty index iterator (whose {@code hasNext()} and {@code hasPrevious()}
     * methods always return {@literal false}, and {@code current()}, {@code next()}
     * and {@code previous()} methods throw an {@link InvalidIndexException}).
     */
    public static final class Empty implements Index {
        public long current() { throw new InvalidIndexException(); }
        public boolean hasNext() { return false; }
        public long next() { throw new InvalidIndexException(); }
        public boolean hasPrevious() { return false; }
        public long previous() { throw new InvalidIndexException(); }
    }


    /**
     * A singleton iterator, that returns a single element.
     */
    public static final class Singleton implements Index {
        private final long element;
        private volatile boolean available=true;

        /**
         * Create a singleton index iterator.
         *
         * @param e the single element this iterator returns
         */
        public Singleton(long e) { element=e; }

        public long current() { return element; }

        public boolean hasNext() { return available; }

        public long next() {
            if(!available) throw new InvalidIndexException();
            available=false;
            return element;
        }

        public boolean hasPrevious() { return false; }

        public long previous() {
            throw new InvalidIndexException();
        }
    }

    /**
     * A range iterator, that returns all indexes in a range.
     */
    public static final class Range implements Index {
        private final long min;
        private final long max;
        private volatile long pos;

        /**
         * Create a range index iterator.
         *
         * @param min the lowest index to return
         * @param max the highest index to return
         */
        public Range(long min, long max) { this.min = min; this.max = max; this.pos = min-1; }

        public long current() { return pos; }

        public boolean hasNext() { return pos<max; }

        public long next() {
            if(pos>=max) throw new InvalidIndexException();
            return ++pos;
        }

        public boolean hasPrevious() { return pos>min; }

        public long previous() {
            if(pos<=min) throw new InvalidIndexException();
            return --pos;
        }
    }

    /**
     * An enumeration iterator, that returns all indexes in an array, in order.
     */
    public static final class Enumerate implements Index {
        private final long[] elements;
        private volatile int pos = -1;

        /**
         * Create a range index iterator.
         *
         * @param e the array of all the indexes to return
         */
        public Enumerate(long... e) { elements = e; }

        public long current() { return elements[pos]; }

        public boolean hasNext() { return pos+1<elements.length; }

        public long next() {
            if(pos+1>=elements.length) throw new Sequence.InvalidIndexException();
            return elements[++pos];
        }

        public boolean hasPrevious() { return pos>0; }

        public long previous() {
            if(pos<=0) throw new Sequence.InvalidIndexException();
            return elements[--pos];
        }
    }


    /**
     * A sequence iterator, that returns all the indexes of a series of Indexes, in order.
     */
    public static final class Sequence implements Index {
        private final Index[] segments;
        private volatile int pos = 0;

        /**
         * Create a sequence iterator.
         *
         * @param s the array of all the subsequence iterators
         */
        public Sequence(Index... s) { segments = s; }

        public long current() { return segments[pos].current(); }

        public boolean hasNext() {
            int pos = this.pos;
            if(pos<0) pos = 0;
            while(pos<segments.length && !segments[pos].hasNext()) pos ++;
            return pos<segments.length;
        }

        public long next() {
            if(pos<0) pos = 0;
            while(pos<segments.length && !segments[pos].hasNext()) pos ++;
            if(pos>=segments.length) throw new InvalidIndexException();
            return segments[pos].next();
        }

        public boolean hasPrevious() {
            int pos = this.pos;
            if(pos>=segments.length) pos = segments.length-1;
            while(pos>=0 && !segments[pos].hasPrevious()) pos --;
            return pos>=0;
        }

        public long previous() {
            if(pos>=segments.length) pos = segments.length-1;
            while(pos>=0 && !segments[pos].hasPrevious()) pos --;
            if(pos<0) throw new InvalidIndexException();
            return segments[pos].next();
        }
    }


    /**********************************************************************************
     **  Exceptions
     **/

    /**
     * <b>An exception thrown upon lookup of a non-existing index.</b>
     * <p/>
     * An {@link Index} or an {@link Indexed} content container may
     * throw this exception when asked to retrieve an object at an invalid
     * index, or an index that does not have associated content.
     */
    public static class InvalidIndexException extends RuntimeException {

        private static final long serialVersionUID=1L;
        /**
         * The offending index
         */
        private final long index;


        /**
         * Constructs a new runtime exception with {@literal null} as its
         * detail message.  The cause is not initialized, and may subsequently be
         * initialized by a call to {@link #initCause}.
         */
        public InvalidIndexException() {
            this.index=-1L;
        }

        /**
         * Constructs a new runtime exception with the specified detail message.
         * The cause is not initialized, and may subsequently be initialized by a
         * call to {@link #initCause}.
         *
         * @param message the detail message. The detail message is saved for
         *                later retrieval by the {@link #getMessage()} method.
         */
        public InvalidIndexException(String message) {
            super(message);
            this.index=-1L;
        }

        /**
         * Constructs a new runtime exception with the specified detail message and
         * cause.
         *
         * @param message the detail message (which is saved for later retrieval
         *                by the {@link #getMessage()} method).
         * @param cause   the cause (which is saved for later retrieval by the
         *                {@link #getCause()} method).  (A {@literal null} value is
         *                permitted, and indicates that the cause is nonexistent or
         *                unknown.)
         */
        public InvalidIndexException(String message, Throwable cause) {
            super(message, cause);
            this.index=-1L;
        }

        /**
         * Constructs a new runtime exception with {@literal null} as its
         * detail message.
         *
         * @param index the invalid index
         */
        public InvalidIndexException(long index) {
            this.index=index;
        }

        /**
         * Constructs a new runtime exception with the specified detail message.
         * The cause is not initialized, and may subsequently be initialized by a
         * call to {@link #initCause}.
         *
         * @param message the detail message. The detail message is saved for
         *                later retrieval by the {@link #getMessage()} method.
         * @param index   the invalid index
         */
        public InvalidIndexException(String message, long index) {
            super(message);
            this.index=index;
        }

        /**
         * Gets the value of the index that caused this exception to be raised
         *
         * @return the invalid index
         */
        public long getIndex() {
            return index;
        }

    }

}
