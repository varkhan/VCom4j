package net.varkhan.core.containers;

import net.varkhan.base.containers.*;
import net.varkhan.base.containers.Iterable;

import java.io.Serializable;
import java.util.ConcurrentModificationException;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantReadWriteLock;


/**
 * <b>Provide multithread safety over an unsynchronized IndexedContainer implementation</b>
 * <p/>
 * This class wraps an unsynchronized IndexedContainer implementation in a read/write
 * synchronized layer, provided all read operations on the underlying container are
 * state-less (i.e. do not modify any field data in the container).
 * <p/>
 * Individual read access to container items is thread safe, and iterators are
 * fail-fast, meaning they will throw a {@link ConcurrentModificationException}
 * whenever is detected a modification on the underlying container that is not
 * the result of the iterator's own methods.
 * <p/>
 * A pair of read/write locks is provided to classes extending this implementation,
 * allowing them to protect write operations from concurrent access, and read operations
 * from inconsistent state during the writes.
 * <p/>
 * Note that since an IndexedContainer is read-only, no specific thread-safety is provided
 * by this implementation, unless it is subclassed to wrap writable container types
 * using the appropriate locking.
 * <p/>
 *
 * @param <Type> the type of elements in the container
 *
 * @author varkhan
 * @date 2/17/11
 * @time 10:01 PM
 */
public abstract class SyncIndexedContainer<Type> implements IndexedContainer<Type>, Serializable {

    protected static final long serialVersionUID=1L;

    protected final Lock rlock;
    protected final Lock wlock;
    protected final IndexedContainer<Type> container;
    protected volatile long opid=0;


    /**********************************************************************************
     **  Container constructors
     **/

    /**
     * Wraps a synchronization layer on top of an IndexedContainer
     *
     * @param container the IndexedContainer to synchronize
     */
    public SyncIndexedContainer(IndexedContainer<Type> container) {
        this.container=container;
        ReentrantReadWriteLock rwl=new ReentrantReadWriteLock();
        this.rlock=rwl.readLock();
        this.wlock=rwl.writeLock();
    }

    /**
     * Wraps an IndexedContainer using predefined synchronization locks
     *
     * @param container the IndexedContainer to synchronize
     * @param rlock the read lock
     * @param wlock the write lock
     */
    public SyncIndexedContainer(IndexedContainer<Type> container, Lock rlock, Lock wlock) {
        this.container=container;
        this.rlock=rlock;
        this.wlock=wlock;
    }


    /**********************************************************************************
     **  Container statistics accessors
     **/

    /**
     * Returns the number of elements in this container.
     *
     * @return the number of entries (elements and related indexes) stored in this container
     */
    public long size() {
        rlock.lock();
        try { return container.size(); }
        finally { rlock.unlock(); }
    }

    /**
     * Indicates whether this container is empty.
     *
     * @return {@literal true} if this container contains no entry,
     *         {@literal false} otherwise
     */
    public boolean isEmpty() {
        rlock.lock();
        try { return container.isEmpty(); }
        finally { rlock.unlock(); }
    }

    /**
     * Returns the smallest position higher that any valid index in this container.
     *
     * @return the highest valid index plus one
     */
    public long head() {
        rlock.lock();
        try { return container.head(); }
        finally { rlock.unlock(); }
    }


    /**********************************************************************************
     **  Container entries accessors
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
        try { return container.has(index); }
        finally { rlock.unlock(); }
    }

    /**
     * Extracts the element designated by an index.
     *
     * @param index a unique identifier for this entry
     *
     * @return the requested element, or {@literal null} if no entry is associated to this index
     */
    public Type get(long index) {
        rlock.lock();
        try { return container.get(index); }
        finally { rlock.unlock(); }
    }


    /**********************************************************************************
     **  Container entries iterators
     **/

    /**
     * Iterates over all indexes in the container, using an {@link net.varkhan.base.containers.Index}.
     *
     * @return an iterator over all the indexes that designate elements in the container
     */
    public Index indexes() {
        rlock.lock();
        try {
            return new Index() {
                final Index iterator=container.indexes();
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
     * @return an iterable over all the indexes that designate elements in the container
     */
    public java.lang.Iterable<Long> iterateIndexes() {
        return new java.lang.Iterable<Long>() {
            public java.util.Iterator<Long> iterator() {
                rlock.lock();
                try {
                    return new java.util.Iterator<Long>() {
                        final java.util.Iterator<Long> iterator=container.iterateIndexes().iterator();
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
     * Iterates over all elements in the container.
     *
     * @return an iterator over all the elements stored in the container
     */
    public Iterator<? extends Type> iterator() {
        rlock.lock();
        try {
            return new Iterator<Type>() {
                final Iterator<? extends Type> iterator=container.iterator();
                private long exid=opid;

                public boolean hasNext() {
                    rlock.lock();
                    try {
                        if(exid!=opid) throw new ConcurrentModificationException();
                        return iterator.hasNext();
                    }
                    finally { rlock.unlock(); }
                }

                public Type next() {
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
     * Iterate over each element of the container, and pass it as argument to a
     * visitor's {@link Visitor#invoke} method, until this method returns
     * a negative count.
     *
     * @param vis the visitor
     * @param par the control parameter
     * @param <Par> the type of the control parameter
     *
     * @return the sum of all positive return values from the visitor
     */
    public <Par> long visit(final Visitor<Type,Par> vis, Par par) {
        rlock.lock();
        try {
            return container.visit(new Visitor<Type,Par>() {
                private final long exid=opid;

                public long invoke(Type obj, Par par) {
                    if(exid!=opid) throw new ConcurrentModificationException();
                    return vis.invoke(obj, par);
                }
            }, par);
        }
        finally { rlock.unlock(); }
    }

    /**
     * Iterate over each element of the container, and pass it as argument to a
     * visitor's {@link net.varkhan.base.containers.IndexedVisitable.IndexedVisitor#invoke} method, until this method returns
     * a negative count.
     *
     * @param vis the visitor
     * @param par the control parameter
     * @param <Par> the type of the control parameter
     *
     * @return the sum of all positive return values from the visitor
     */
    public <Par> long visit(final IndexedVisitor<Type,Par> vis, Par par) {
        rlock.lock();
        try {
            return container.visit(new IndexedVisitor<Type,Par>() {
                private final long exid=opid;

                public long invoke(long idx, Type obj, Par par) {
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
    public net.varkhan.base.containers.Iterable<? extends Type> iterate(final long[] indexes) {
        return new Iterable<Type>() {
            public Iterator<Type> iterator() {
                rlock.lock();
                try {
                    return new Iterator<Type>() {
                        final Iterator<? extends Type> iterator=container.iterate(indexes).iterator();
                        private long exid=opid;

                        public boolean hasNext() {
                            rlock.lock();
                            try {
                                if(exid!=opid) throw new ConcurrentModificationException();
                                return iterator.hasNext();
                            }
                            finally { rlock.unlock(); }
                        }

                        public Type next() {
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
    public Iterable<? extends Type> iterate(final java.lang.Iterable<Long> indexes) {
        return new Iterable<Type>() {
            public Iterator<Type> iterator() {
                rlock.lock();
                try {
                    return new Iterator<Type>() {
                        final Iterator<? extends Type> iterator=container.iterate(indexes).iterator();
                        private long exid=opid;

                        public boolean hasNext() {
                            rlock.lock();
                            try {
                                if(exid!=opid) throw new ConcurrentModificationException();
                                return iterator.hasNext();
                            }
                            finally { rlock.unlock(); }
                        }

                        public Type next() {
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
    public Iterable<? extends Type> iterate(final Indexable indexes) {
        return new Iterable<Type>() {
            public Iterator<Type> iterator() {
                rlock.lock();
                try {
                    return new Iterator<Type>() {
                        final Iterator<? extends Type> iterator=container.iterate(indexes).iterator();
                        private long exid=opid;

                        public boolean hasNext() {
                            rlock.lock();
                            try {
                                if(exid!=opid) throw new ConcurrentModificationException();
                                return iterator.hasNext();
                            }
                            finally { rlock.unlock(); }
                        }

                        public Type next() {
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
    }}
