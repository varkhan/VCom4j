/**
 *
 */
package net.varkhan.base.containers.list;

import net.varkhan.base.containers.Index;
import net.varkhan.base.containers.Indexable;
import net.varkhan.base.containers.type.LongIterable;

import java.io.Serializable;
import java.util.ConcurrentModificationException;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantReadWriteLock;


/**
 * <b>Provide multithread safety over an unsynchronized IndexedList implementation</b>
 * <p/>
 * This class wraps an unsynchronized IndexedList implementation in a read/write
 * synchronized layer, provided all read operations on the underlying list are
 * state-less (i.e. do not modify any field data in the list).
 * <p/>
 * Individual read or write access to list items is thread safe, and iterators are
 * fail-fast, meaning they will throw a {@link ConcurrentModificationException}
 * whenever is detected a modification on the underlying list that is not the result
 * of the iterator's own methods.
 * <p/>
 *
 * @author varkhan
 * @date Mar 12, 2009
 * @time 7:18:56 PM
 */
public class SyncIndexedLongList implements IndexedLongList, Serializable {

    private static final long serialVersionUID=1L;

    private final Lock            rlock;
    private final Lock            wlock;
    private final IndexedLongList list;
    private volatile long opid=0;


    /**********************************************************************************
     **  List constructors
     **/

    /**
     * Wraps a synchronization layer on top of an IndexedList
     *
     * @param list the IndexedList to synchronize
     */
    public SyncIndexedLongList(IndexedLongList list) {
        this.list=list;
        ReentrantReadWriteLock rwl=new ReentrantReadWriteLock();
        this.rlock=rwl.readLock();
        this.wlock=rwl.writeLock();
    }

    /**
     * Wraps an IndexedList using predefined synchronization locks
     *
     * @param list  the IndexedList to synchronize
     * @param rlock the read lock
     * @param wlock the write lock
     */
    public SyncIndexedLongList(IndexedLongList list, Lock rlock, Lock wlock) {
        this.list=list;
        this.rlock=rlock;
        this.wlock=wlock;
    }


    /**********************************************************************************
     **  List statistics accessors
     **/

    /**
     * Returns the number of elements in this list.
     *
     * @return the number of entries (elements and related indexes) stored in this list
     */
    public long size() {
        rlock.lock();
        try { return list.size(); }
        finally { rlock.unlock(); }
    }

    /**
     * Indicates whether this list is empty.
     *
     * @return {@literal true} if this list contains no entry,
     *         {@literal false} otherwise
     */
    public boolean isEmpty() {
        rlock.lock();
        try { return list.isEmpty(); }
        finally { rlock.unlock(); }
    }

    /**
     * Returns the smallest position higher that any valid index in this list.
     *
     * @return the highest valid index plus one
     */
    public long head() {
        rlock.lock();
        try { return list.head(); }
        finally { rlock.unlock(); }
    }

    /**
     * Returns an index that has no associated element in this list.
     *
     * @return an unused index
     */
    public long free() {
        rlock.lock();
        try { return list.free(); }
        finally { rlock.unlock(); }
    }

    /**
     * Deletes all elements from this list
     */
    public void clear() {
        wlock.lock();
        try { list.clear(); }
        finally {
            ++opid;
            wlock.unlock();
        }
    }

    /**
     * Gets the default value
     *
     * @return the default value, returned by {@link #getLong} on empty entries
     */
    public long getDefaultValue() {
        rlock.lock();
        try { return list.getDefaultValue(); }
        finally { rlock.unlock(); }
    }

    /**
     * Sets the default value
     *
     * @param def the default value, returned by {@link #getLong} on empty entries
     */
    public void setDefaultValue(long def) {
        wlock.lock();
        try { list.setDefaultValue(def); }
        finally {
            ++opid;
            wlock.unlock();
        }
    }


    /**********************************************************************************
     **  List entries accessors
     **/

    /**
     * Indicates whether an index has an associated entry.
     *
     * @param index a unique identifier for this entry
     *
     * @return {@literal true} if an element is associated with this index,
     *         or {@literal false} if no element is associated with this index
     */
    public boolean has(long index) {
        rlock.lock();
        try { return list.has(index); }
        finally { rlock.unlock(); }
    }

    /**
     * Extracts the element designated by an index.
     *
     * @param index a unique identifier for this entry
     *
     * @return the requested element, or {@literal null} if no entry is associated to this index
     */
    public Long get(long index) {
        rlock.lock();
        try { return list.get(index); }
        finally { rlock.unlock(); }
    }

    /**
     * Extracts the element designated by an index.
     *
     * @param index a unique identifier for this entry
     *
     * @return the requested element, or {@literal null} if no entry is associated to this index
     */
    public long getLong(long index) {
        rlock.lock();
        try { return list.getLong(index); }
        finally { rlock.unlock(); }
    }

    /**
     * Adds an element at the end of the list.
     * <p/>
     * Note: this is equivalent to {@link #set}{@code (head(), val)}, and
     * the returned index is the value of {@link #head()} before the call.
     *
     * @param val the object to store in the list
     *
     * @return the entry's unique identifier, that will subsequently give access to the element
     */
    public long add(Long val) {
        wlock.lock();
        try { return list.add(val); }
        finally {
            ++opid;
            wlock.unlock();
        }
    }

    /**
     * Adds an element at the end of the list.
     * <p/>
     * Note: this is equivalent to {@link #set}{@code (head(), val)}, and
     * the returned index is the value of {@link #head()} before the call.
     *
     * @param val the object to store in the list
     *
     * @return the entry's unique identifier, that will subsequently give access to the element
     */
    public long add(long val) {
        wlock.lock();
        try { return list.add(val); }
        finally {
            ++opid;
            wlock.unlock();
        }
    }

    /**
     * Associates an element to a particular index (the index can refer to an
     * already existing entry, in which case that entry is overwritten).
     *
     * @param index a unique identifier for this entry
     * @param val   the object to store in the list
     *
     * @return the entry's unique identifier, equal to {@code index}, or {@literal -1L} on error
     */
    public long set(long index, Long val) {
        wlock.lock();
        try { return list.set(index, val); }
        finally {
            ++opid;
            wlock.unlock();
        }
    }

    /**
     * Associates an element to a particular index (the index can refer to an
     * already existing entry, in which case that entry is overwritten).
     *
     * @param index a unique identifier for this entry
     * @param val   the object to store in the list
     *
     * @return the entry's unique identifier, equal to {@code index}, or {@literal -1L} on error
     */
    public long set(long index, long val) {
        wlock.lock();
        try { return list.set(index, val); }
        finally {
            ++opid;
            wlock.unlock();
        }
    }

    /**
     * Deletes an element, and invalidates the related index.
     *
     * @param index a unique identifier for this entry
     */
    public void del(long index) {
        wlock.lock();
        try { list.del(index); }
        finally {
            ++opid;
            wlock.unlock();
        }
    }


    /**********************************************************************************
     **  List entries iterators
     **/

    /**
     * Iterates over all indexes in the list, using an {@link Index}.
     *
     * @return an iterator over all the indexes that designate elements in the list
     */
    public Index indexes() {
        rlock.lock();
        final Index iterator;
        try { iterator=list.indexes(); }
        finally { rlock.unlock(); }
        return new Index() {
            private long index=-1;
            private final long exid=opid;

            public long current() {
                return index;
            }

            public boolean hasNext() {
                rlock.lock();
                try {
                    if(exid!=opid) throw new ConcurrentModificationException();
                    return iterator.hasNext();
                }
                finally { rlock.unlock(); }
            }

            public long next() {
                rlock.lock();
                try {
                    if(exid!=opid) throw new ConcurrentModificationException();
                    return iterator.next();
                }
                finally { rlock.unlock(); }
            }

            public boolean hasPrevious() {
                rlock.lock();
                try {
                    if(exid!=opid) throw new ConcurrentModificationException();
                    return iterator.hasPrevious();
                }
                finally { rlock.unlock(); }
            }

            public long previous() {
                rlock.lock();
                try {
                    if(exid!=opid) throw new ConcurrentModificationException();
                    return index=iterator.previous();
                }
                finally { rlock.unlock(); }
            }
        };
    }

    /**
     * Iterates over all indexes in the collection.
     *
     * @return an iterable over all the indexes that designate elements in the list
     */
    public java.lang.Iterable<Long> iterateIndexes() {
        return new java.lang.Iterable<Long>() {
            public java.util.Iterator<Long> iterator() {
                rlock.lock();
                try {
                    return new java.util.Iterator<Long>() {
                        final java.util.Iterator<Long> iterator=list.iterateIndexes().iterator();
                        private long exid=opid;

                        public boolean hasNext() {
                            rlock.lock();
                            try {
                                if(exid!=opid) throw new ConcurrentModificationException();
                                return iterator.hasNext();
                            }
                            finally { rlock.unlock(); }
                        }

                        public Long next() {
                            rlock.lock();
                            try {
                                if(exid!=opid) throw new ConcurrentModificationException();
                                return iterator.next();
                            }
                            finally { rlock.unlock(); }
                        }

                        public void remove() {
                            wlock.lock();
                            try {
                                if(exid!=opid) throw new ConcurrentModificationException();
                                exid=++opid;
                                iterator.remove();
                            }
                            finally { wlock.unlock(); }
                        }
                    };
                }
                finally { rlock.unlock(); }
            }
        };
    }

    /**
     * Iterates over all elements in the list.
     *
     * @return an iterator over all the elements stored in the list
     */
    public LongIterator iterator() {
        rlock.lock();
        try {
            return new LongIterator() {
                final LongIterator iterator=list.iterator();
                private long exid=opid;

                public boolean hasNext() {
                    rlock.lock();
                    try {
                        if(exid!=opid) throw new ConcurrentModificationException();
                        return iterator.hasNext();
                    }
                    finally { rlock.unlock(); }
                }

                public Long next() {
                    rlock.lock();
                    try {
                        if(exid!=opid) throw new ConcurrentModificationException();
                        return iterator.next();
                    }
                    finally { rlock.unlock(); }
                }

                public long nextValue() {
                    rlock.lock();
                    try {
                        if(exid!=opid) throw new ConcurrentModificationException();
                        return iterator.nextValue();
                    }
                    finally { rlock.unlock(); }
                }

                public void remove() {
                    wlock.lock();
                    try {
                        if(exid!=opid) throw new ConcurrentModificationException();
                        exid=++opid;
                        iterator.remove();
                    }
                    finally { wlock.unlock(); }
                }
            };
        }
        finally { rlock.unlock(); }
    }

    /**
     * Iterate over each element of the list, and pass it as argument to a
     * visitor's {@link Visitor#invoke} method, until this method returns
     * a negative count.
     *
     * @param vis the visitor
     * @param par the control parameter
     * @param <Par> the type of the control parameter
     *
     * @return the sum of all positive return values from the visitor
     */
    public <Par> long visit(final Visitor<Long,Par> vis, Par par) {
        rlock.lock();
        try {
            return list.visit(new Visitor<Long,Par>() {
                private final long exid=opid;

                public long invoke(Long obj, Par par) {
                    if(exid!=opid) throw new ConcurrentModificationException();
                    return vis.invoke(obj, par);
                }
            }, par);
        }
        finally { rlock.unlock(); }
    }

    /**
     * Iterate over each element of the list, and pass it as argument to a
     * visitor's {@link Visitor#invoke} method, until this method returns
     * a negative count.
     *
     * @param vis the visitor
     * @param par the control parameter
     * @param <Par> the type of the control parameter
     *
     * @return the sum of all positive return values from the visitor
     */
    public <Par> long visit(final LongVisitor<Par> vis, Par par) {
        rlock.lock();
        try {
            return list.visit(new LongVisitor<Par>() {
                private final long exid=opid;

                public long invoke(long obj, Par par) {
                    if(exid!=opid) throw new ConcurrentModificationException();
                    return vis.invoke(obj, par);
                }
            }, par);
        }
        finally { rlock.unlock(); }
    }

    /**
     * Iterate over each element of the list, and pass it as argument to a
     * visitor's {@link IndexedVisitor#invoke} method, until this method returns
     * a negative count.
     *
     * @param vis the visitor
     * @param par the control parameter
     * @param <Par> the type of the control parameter
     *
     * @return the sum of all positive return values from the visitor
     */
    public <Par> long visit(final IndexedVisitor<Long,Par> vis, Par par) {
        rlock.lock();
        try {
            return list.visit(new IndexedVisitor<Long,Par>() {
                private final long exid=opid;

                public long invoke(long idx, Long obj, Par par) {
                    if(exid!=opid) throw new ConcurrentModificationException();
                    return vis.invoke(idx, obj, par);
                }
            }, par);
        }
        finally { rlock.unlock(); }
    }

    /**
     * Iterate over each element of the list, and pass it as argument to a
     * visitor's {@link IndexedVisitor#invoke} method, until this method returns
     * a negative count.
     *
     * @param vis the visitor
     * @param par the control parameter
     * @param <Par> the type of the control parameter
     *
     * @return the sum of all positive return values from the visitor
     */
    public <Par> long visit(final IndexedLongVisitor<Par> vis, Par par) {
        rlock.lock();
        try {
            return list.visit(new IndexedLongVisitor<Par>() {
                private final long exid=opid;

                public long invoke(long idx, long obj, Par par) {
                    if(exid!=opid) throw new ConcurrentModificationException();
                    return vis.invoke(idx, obj, par);
                }
            }, par);
        }
        finally { rlock.unlock(); }
    }

    /**
     * Iterates over a set of elements designated by an array of indexes.
     *
     * @param indexes an array of identifiers
     *
     * @return an iterable over all the elements indexed by the identifiers
     */
    public LongIterable iterate(final long[] indexes) {
        return new LongIterable() {
            public LongIterator iterator() {
                rlock.lock();
                try {
                    return new LongIterator() {
                        final LongIterator iterator=list.iterate(indexes).iterator();
                        private long exid=opid;

                        public boolean hasNext() {
                            rlock.lock();
                            try {
                                if(exid!=opid) throw new ConcurrentModificationException();
                                return iterator.hasNext();
                            }
                            finally { rlock.unlock(); }
                        }

                        public Long next() {
                            rlock.lock();
                            try {
                                if(exid!=opid) throw new ConcurrentModificationException();
                                return iterator.next();
                            }
                            finally { rlock.unlock(); }
                        }

                        public long nextValue() {
                            rlock.lock();
                            try {
                                if(exid!=opid) throw new ConcurrentModificationException();
                                return iterator.nextValue();
                            }
                            finally { rlock.unlock(); }
                        }

                        public void remove() {
                            wlock.lock();
                            try {
                                if(exid!=opid) throw new ConcurrentModificationException();
                                exid=++opid;
                                iterator.remove();
                            }
                            finally { wlock.unlock(); }
                        }
                    };
                }
                finally { rlock.unlock(); }
            }
        };
    }

    /**
     * Iterates over a set of elements designated by an iterator over indexes.
     *
     * @param indexes an iterable over identifiers
     *
     * @return an iterable over all the elements indexed by the identifiers
     */
    public LongIterable iterate(final java.lang.Iterable<Long> indexes) {
        return new LongIterable() {
            public LongIterator iterator() {
                rlock.lock();
                try {
                    return new LongIterator() {
                        final LongIterator iterator=list.iterate(indexes).iterator();
                        private long exid=opid;

                        public boolean hasNext() {
                            rlock.lock();
                            try {
                                if(exid!=opid) throw new ConcurrentModificationException();
                                return iterator.hasNext();
                            }
                            finally { rlock.unlock(); }
                        }

                        public Long next() {
                            rlock.lock();
                            try {
                                if(exid!=opid) throw new ConcurrentModificationException();
                                return iterator.next();
                            }
                            finally { rlock.unlock(); }
                        }

                        public long nextValue() {
                            rlock.lock();
                            try {
                                if(exid!=opid) throw new ConcurrentModificationException();
                                return iterator.nextValue();
                            }
                            finally { rlock.unlock(); }
                        }

                        public void remove() {
                            wlock.lock();
                            try {
                                if(exid!=opid) throw new ConcurrentModificationException();
                                exid=++opid;
                                iterator.remove();
                            }
                            finally { wlock.unlock(); }
                        }
                    };
                }
                finally { rlock.unlock(); }
            }
        };
    }

    /**
     * Iterates over a set of elements designated by an iterator over indexes
     *
     * @param indexes an iterable over identifiers
     *
     * @return an iterable over all the elements indexed by the identifiers
     */
    public LongIterable iterate(final Indexable indexes) {
        return new LongIterable() {
            public LongIterator iterator() {
                rlock.lock();
                try {
                    return new LongIterator() {
                        final LongIterator iterator=list.iterate(indexes).iterator();
                        private long exid=opid;

                        public boolean hasNext() {
                            rlock.lock();
                            try {
                                if(exid!=opid) throw new ConcurrentModificationException();
                                return iterator.hasNext();
                            }
                            finally { rlock.unlock(); }
                        }

                        public Long next() {
                            rlock.lock();
                            try {
                                if(exid!=opid) throw new ConcurrentModificationException();
                                return iterator.next();
                            }
                            finally { rlock.unlock(); }
                        }

                        public long nextValue() {
                            rlock.lock();
                            try {
                                if(exid!=opid) throw new ConcurrentModificationException();
                                return iterator.nextValue();
                            }
                            finally { rlock.unlock(); }
                        }

                        public void remove() {
                            wlock.lock();
                            try {
                                if(exid!=opid) throw new ConcurrentModificationException();
                                exid=++opid;
                                iterator.remove();
                            }
                            finally { wlock.unlock(); }
                        }
                    };
                }
                finally { rlock.unlock(); }
            }
        };
    }
}
