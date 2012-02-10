package net.varkhan.base.containers.set;

import net.varkhan.base.containers.*;
import net.varkhan.base.containers.Iterable;

import java.io.Serializable;
import java.util.ConcurrentModificationException;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantReadWriteLock;


/**
 * <b>Provide multithread safety over an unsynchronized IndexedSet implementation</b>
 * <p/>
 * This class wraps an unsynchronized IndexedSet implementation in a read/write
 * synchronized layer, provided all read operations on the underlying set are
 * state-less (i.e. do not modify any field data in the set).
 * <p/>
 * Individual read or write access to set items is thread safe, and iterators are
 * fail-fast, meaning they will throw a {@link ConcurrentModificationException}
 * whenever is detected a modification on the underlying set that is not the result
 * of the iterator's own methods.
 * <p/>
 *
 * @param <Key> the type of elements in the set
 *
 * @author varkhan
 * @date 2/17/11
 * @time 10:00 PM
 */
public class SyncIndexedSet<Key> implements IndexedSet<Key>, Serializable {

    private static final long serialVersionUID=1L;

    private final Lock rlock;
    private final Lock wlock;
    private final IndexedSet<Key> set;
    private volatile long opid=0;


    /**********************************************************************************
     **  Set constructors
     **/

    /**
     * Wraps a synchronization layer on top of an IndexedSet
     *
     * @param set the IndexedSet to synchronize
     */
    public SyncIndexedSet(IndexedSet<Key> set) {
        this.set=set;
        ReentrantReadWriteLock rwl=new ReentrantReadWriteLock();
        this.rlock=rwl.readLock();
        this.wlock=rwl.writeLock();
    }

    /**
     * Wraps an IndexedSet using predefined synchronization locks
     *
     * @param set  the IndexedSet to synchronize
     * @param rlock the read lock
     * @param wlock the write lock
     */
    public SyncIndexedSet(IndexedSet<Key> set, Lock rlock, Lock wlock) {
        this.set=set;
        this.rlock=rlock;
        this.wlock=wlock;
    }


    /**********************************************************************************
     **  Set statistics accessors
     **/

    /**
     * Returns the number of elements in this set.
     *
     * @return the number of entries (elements and related indexes) stored in this set
     */
    public long size() {
        rlock.lock();
        try { return set.size(); }
        finally { rlock.unlock(); }
    }

    /**
     * Indicates whether this set is empty.
     *
     * @return {@literal true} if this set contains no entry,
     *         {@literal false} otherwise
     */
    public boolean isEmpty() {
        rlock.lock();
        try { return set.isEmpty(); }
        finally { rlock.unlock(); }
    }

    /**
     * Returns the smallest position higher that any valid index in this set.
     *
     * @return the highest valid index plus one
     */
    public long head() {
        rlock.lock();
        try { return set.head(); }
        finally { rlock.unlock(); }
    }

    /**
     * Deletes all elements from this set
     */
    public void clear() {
        wlock.lock();
        try { set.clear(); }
        finally {
            ++opid;
            wlock.unlock();
        }
    }


    /**********************************************************************************
     **  Set entries accessors
     **/

    /**
     * Search for a given key in this set, and returns the associated index
     *
     * @param key the object to find in the set
     *
     * @return the index for this entry, or {@literal -1L} if the specified object is not in the set
     */
    public long index(Key key) {
        rlock.lock();
        try { return set.index(key); }
        finally { rlock.unlock(); }
    }

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
        try { return set.has(index); }
        finally { rlock.unlock(); }
    }

    /**
     * Extracts the element designated by an index.
     *
     * @param index a unique identifier for this entry
     *
     * @return the requested element, or {@literal null} if no entry is associated to this index
     */
    public Key get(long index) {
        rlock.lock();
        try { return set.get(index); }
        finally { rlock.unlock(); }
    }

    /**
     * Adds an element at the end of the set.
     * <p/>
     * Note: this is equivalent to {@link #set}{@code (head(), val)}, and
     * the returned index is the value of {@link #head()} before the call.
     *
     * @param val the object to store in the set
     *
     * @return the entry's unique identifier, that will subsequently give access to the element
     */
    public long add(Key val) {
        wlock.lock();
        try { return set.add(val); }
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
        try { set.del(index); }
        finally {
            ++opid;
            wlock.unlock();
        }
    }


    /**********************************************************************************
     **  Set entries iterators
     **/

    /**
     * Iterates over all indexes in the set, using an {@link net.varkhan.base.containers.Index}.
     *
     * @return an iterator over all the indexes that designate elements in the set
     */
    public Index indexes() {
        rlock.lock();
        try {
            return new Index() {
                final Index iterator=set.indexes();
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
                        return index=iterator.next();
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
        finally { rlock.unlock(); }
    }

    /**
     * Iterates over all indexes in the collection.
     *
     * @return an iterable over all the indexes that designate elements in the set
     */
    public java.lang.Iterable<Long> iterateIndexes() {
        return new java.lang.Iterable<Long>() {
            public java.util.Iterator<Long> iterator() {
                rlock.lock();
                try {
                    return new java.util.Iterator<Long>() {
                        final java.util.Iterator<Long> iterator=set.iterateIndexes().iterator();
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
     * Iterates over all elements in the set.
     *
     * @return an iterator over all the elements stored in the set
     */
    public Iterator<? extends Key> iterator() {
        rlock.lock();
        try {
            return new Iterator<Key>() {
                final Iterator<? extends Key> iterator=set.iterator();
                private long exid=opid;

                public boolean hasNext() {
                    rlock.lock();
                    try {
                        if(exid!=opid) throw new ConcurrentModificationException();
                        return iterator.hasNext();
                    }
                    finally { rlock.unlock(); }
                }

                public Key next() {
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

    /**
     * Iterate over each element of the set, and pass it as argument to a
     * visitor's {@link Visitor#invoke} method, until this method returns
     * a negative count.
     *
     * @param vis the visitor
     * @param par the control parameter
     * @param <Par> the type of the control parameter
     *
     * @return the sum of all positive return values from the visitor
     */
    public <Par> long visit(final Visitor<Key,Par> vis, Par par) {
        rlock.lock();
        try {
            return set.visit(new Visitor<Key,Par>() {
                private final long exid=opid;

                public long invoke(Key obj, Par par) {
                    if(exid!=opid) throw new ConcurrentModificationException();
                    return vis.invoke(obj, par);
                }
            }, par);
        }
        finally { rlock.unlock(); }
    }

    /**
     * Iterate over each element of the set, and pass it as argument to a
     * visitor's {@link IndexedVisitor#invoke} method, until this method returns
     * a negative count.
     *
     * @param vis the visitor
     * @param par the control parameter
     * @param <Par> the type of the control parameter
     *
     * @return the sum of all positive return values from the visitor
     */
    public <Par> long visit(final IndexedVisitor<Key,Par> vis, Par par) {
        rlock.lock();
        try {
            return set.visit(new IndexedVisitor<Key,Par>() {
                private final long exid=opid;

                public long invoke(long idx, Key obj, Par par) {
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
    public net.varkhan.base.containers.Iterable<? extends Key> iterate(final long[] indexes) {
        return new Iterable<Key>() {
            public Iterator<Key> iterator() {
                rlock.lock();
                try {
                    return new Iterator<Key>() {
                        final Iterator<? extends Key> iterator=set.iterate(indexes).iterator();
                        private long exid=opid;

                        public boolean hasNext() {
                            rlock.lock();
                            try {
                                if(exid!=opid) throw new ConcurrentModificationException();
                                return iterator.hasNext();
                            }
                            finally { rlock.unlock(); }
                        }

                        public Key next() {
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
     * Iterates over a set of elements designated by an iterator over indexes.
     *
     * @param indexes an iterable over identifiers
     *
     * @return an iterable over all the elements indexed by the identifiers
     */
    public Iterable<? extends Key> iterate(final java.lang.Iterable<Long> indexes) {
        return new Iterable<Key>() {
            public Iterator<Key> iterator() {
                rlock.lock();
                try {
                    return new Iterator<Key>() {
                        final Iterator<? extends Key> iterator=set.iterate(indexes).iterator();
                        private long exid=opid;

                        public boolean hasNext() {
                            rlock.lock();
                            try {
                                if(exid!=opid) throw new ConcurrentModificationException();
                                return iterator.hasNext();
                            }
                            finally { rlock.unlock(); }
                        }

                        public Key next() {
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
     * Iterates over a set of elements designated by an iterator over indexes
     *
     * @param indexes an iterable over identifiers
     *
     * @return an iterable over all the elements indexed by the identifiers
     */
    public Iterable<? extends Key> iterate(final Indexable indexes) {
        return new Iterable<Key>() {
            public Iterator<Key> iterator() {
                rlock.lock();
                try {
                    return new Iterator<Key>() {
                        final Iterator<? extends Key> iterator=set.iterate(indexes).iterator();
                        private long exid=opid;

                        public boolean hasNext() {
                            rlock.lock();
                            try {
                                if(exid!=opid) throw new ConcurrentModificationException();
                                return iterator.hasNext();
                            }
                            finally { rlock.unlock(); }
                        }

                        public Key next() {
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


    /**********************************************************************************
     **  Set entry and iterators
     **/

    /**
     * Iterates over all entries in the set.
     *
     * @return an iterable over all the entries in the set
     */
    public Iterable<? extends IndexedSet.Entry<? extends Key>> entries() {
        return new Iterable<IndexedSet.Entry<? extends Key>>() {
            public Iterator<IndexedSet.Entry<? extends Key>> iterator() {
                rlock.lock();
                try {
                    return new Iterator<IndexedSet.Entry<? extends Key>>() {
                        @SuppressWarnings("unchecked")
                        final Iterator<IndexedSet.Entry<? extends Key>> iterator=(Iterator<Entry<? extends Key>>) set.entries();
                        private long exid=opid;

                        public boolean hasNext() {
                            rlock.lock();
                            try {
                                if(exid!=opid) throw new ConcurrentModificationException();
                                return iterator.hasNext();
                            }
                            finally { rlock.unlock(); }
                        }

                        public IndexedSet.Entry<? extends Key> next() {
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
     * Iterates over a set of entries designated by an array of indexes.
     *
     * @param indexes an array of indexes
     *
     * @return an iterable over all the entries designated by the identifiers
     */
    public Iterable<? extends IndexedSet.Entry<? extends Key>> entries(final long[] indexes) {
        return new Iterable<IndexedSet.Entry<? extends Key>>() {
            public Iterator<IndexedSet.Entry<? extends Key>> iterator() {
                rlock.lock();
                try {
                    return new Iterator<IndexedSet.Entry<? extends Key>>() {
                        @SuppressWarnings("unchecked")
                        final Iterator<IndexedSet.Entry<? extends Key>> iterator=(Iterator<Entry<? extends Key>>) set.entries(indexes);
                        private long exid=opid;

                        public boolean hasNext() {
                            rlock.lock();
                            try {
                                if(exid!=opid) throw new ConcurrentModificationException();
                                return iterator.hasNext();
                            }
                            finally { rlock.unlock(); }
                        }

                        public IndexedSet.Entry<? extends Key> next() {
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
     * Iterates over a set of entries designated by an iterator over indexes.
     *
     * @param indexes an iterable over indexes
     *
     * @return an iterable over all the entries designated by the identifiers
     */
    public Iterable<? extends IndexedSet.Entry<? extends Key>> entries(final Iterable<Long> indexes) {
        return new Iterable<IndexedSet.Entry<? extends Key>>() {
            public Iterator<IndexedSet.Entry<? extends Key>> iterator() {
                rlock.lock();
                try {
                    return new Iterator<IndexedSet.Entry<? extends Key>>() {
                        @SuppressWarnings("unchecked")
                        final Iterator<IndexedSet.Entry<? extends Key>> iterator=(Iterator<Entry<? extends Key>>) set.entries(indexes);
                        private long exid=opid;

                        public boolean hasNext() {
                            rlock.lock();
                            try {
                                if(exid!=opid) throw new ConcurrentModificationException();
                                return iterator.hasNext();
                            }
                            finally { rlock.unlock(); }
                        }

                        public IndexedSet.Entry<? extends Key> next() {
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
     * Iterates over a set of entries designated by an iterator over indexes.
     *
     * @param indexes an iterable over indexes
     *
     * @return an iterable over all the entries designated by the identifiers
     */
    public Iterable<? extends IndexedSet.Entry<? extends Key>> entries(final Indexable indexes) {
        return new Iterable<IndexedSet.Entry<? extends Key>>() {
            public Iterator<IndexedSet.Entry<? extends Key>> iterator() {
                rlock.lock();
                try {
                    return new Iterator<IndexedSet.Entry<? extends Key>>() {
                        @SuppressWarnings("unchecked")
                        final Iterator<IndexedSet.Entry<? extends Key>> iterator=(Iterator<Entry<? extends Key>>) set.entries(indexes);
                        private long exid=opid;

                        public boolean hasNext() {
                            rlock.lock();
                            try {
                                if(exid!=opid) throw new ConcurrentModificationException();
                                return iterator.hasNext();
                            }
                            finally { rlock.unlock(); }
                        }

                        public IndexedSet.Entry<? extends Key> next() {
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

}
