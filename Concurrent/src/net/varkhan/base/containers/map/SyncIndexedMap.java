package net.varkhan.base.containers.map;

import net.varkhan.base.containers.*;
import net.varkhan.base.containers.Iterable;

import java.io.Serializable;
import java.util.ConcurrentModificationException;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantReadWriteLock;


/**
 * <b></b>.
 * <p/>
 *
 * @author varkhan
 * @date 2/17/11
 * @time 9:59 PM
 */
public class SyncIndexedMap<Key,Value> implements ConcurrentIndexedMap<Key,Value>, Serializable {

    private static final long serialVersionUID=1L;

    private final Lock rlock;
    private final Lock wlock;
    private final IndexedMap<Key,Value> map;
    private volatile long opid=0;


    /**********************************************************************************
     **  Map constructors
     **/

    /**
     * Wraps a synchronization layer on top of an IndexedMap
     *
     * @param map the IndexedMap to synchronize
     */
    public SyncIndexedMap(IndexedMap<Key,Value> map) {
        this.map=map;
        ReentrantReadWriteLock rwl=new ReentrantReadWriteLock();
        this.rlock=rwl.readLock();
        this.wlock=rwl.writeLock();
    }

    /**
     * Wraps an IndexedMap using predefined synchronization locks
     *
     * @param map  the IndexedMap to synchronize
     * @param rlock the read lock
     * @param wlock the write lock
     */
    public SyncIndexedMap(IndexedMap<Key,Value> map, Lock rlock, Lock wlock) {
        this.map=map;
        this.rlock=rlock;
        this.wlock=wlock;
    }


    /**********************************************************************************
     **  Map statistics accessors
     **/

    /**
     * Returns the number of elements in this map.
     *
     * @return the number of entries (elements and related indexes) stored in this map
     */
    public long size() {
        rlock.lock();
        try { return map.size(); }
        finally { rlock.unlock(); }
    }

    /**
     * Indicates whether this map is empty.
     *
     * @return {@literal true} if this map contains no entry,
     *         {@literal false} otherwise
     */
    public boolean isEmpty() {
        rlock.lock();
        try { return map.isEmpty(); }
        finally { rlock.unlock(); }
    }

    /**
     * Returns the smallest position higher that any valid index in this map.
     *
     * @return the highest valid index plus one
     */
    public long head() {
        rlock.lock();
        try { return map.head(); }
        finally { rlock.unlock(); }
    }

    /**
     * Deletes all elements from this map
     */
    public void clear() {
        wlock.lock();
        try { map.clear(); }
        finally {
            ++opid;
            wlock.unlock();
        }
    }


    /**********************************************************************************
     **  Map entries accessors
     **/

    /**
     * Search for a given key in this map, and returns the associated index
     *
     * @param key the object to find in the map
     *
     * @return the index for this entry, or {@literal -1L} if the specified object is not in the map
     */
    public long index(Key key) {
        rlock.lock();
        try { return map.index(key); }
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
        try { return map.has(index); }
        finally { rlock.unlock(); }
    }

    /**
     * Extracts the element designated by an index.
     *
     * @param index a unique identifier for this entry
     *
     * @return the requested element, or {@literal null} if no entry is associated to this index
     */
    public Entry<Key,Value> get(long index) {
        rlock.lock();
        try { return map.get(index); }
        finally { rlock.unlock(); }
    }

    /**
     * Gets the key designated by an index.
     *
     * @param index a unique identifier
     *
     * @return the requested key, or {@literal null} if no element is available at this index
     */
    public Key getKey(long index) {
        rlock.lock();
        try { return map.getKey(index); }
        finally { rlock.unlock(); }
    }

    /**
     * Gets the value designated by an index.
     *
     * @param index a unique identifier
     *
     * @return the requested value at this index
     */
    public Value getValue(long index) {
        rlock.lock();
        try { return map.getValue(index); }
        finally { rlock.unlock(); }
    }

    /**
     * Sets the value designated by an index.
     *
     * @param index a unique identifier
     * @param value the new value at this index
     */
    public void setValue(long index, Value value) {
        wlock.lock();
        try { map.setValue(index,value); }
        finally {
            ++opid;
            wlock.unlock();
        }
    }

    /**
     * Replaces the value designated by a key, if the record exists and has the specified value.
     *
     * @param key a key referring to this record
     * @param oldval the old value at this index
     * @param newval the new value at this index
     *
     * @return {@literal true} if the record was updated, {@literal false} otherwise
     */
    public boolean repForKey(Key key, Value oldval, Value newval) {
        wlock.lock();
        try {
            long idx = map.index(key);
            if(idx>=0 && map.get(idx)==oldval) {
                ++opid;
                map.setValue(idx,newval);
                return true;
            }
            else return false;
        }
        finally {
            ++opid;
            wlock.unlock();
        }
    }

    /**
     * Replaces the value designated by an index, if the record exists and has the specified value.
     *
     * @param index a unique identifier
     * @param oldval the old value at this index
     * @param newval the new value at this index
     *
     * @return {@literal true} if the record was updated, {@literal false} otherwise
     */
    public boolean rep(long index, Value oldval, Value newval) {
        wlock.lock();
        try {
            if(map.get(index)==oldval) {
                ++opid;
                map.setValue(index,newval);
                return true;
            }
            else return false;
        }
        finally {
            ++opid;
            wlock.unlock();
        }
    }

    /**
     * Allocates an index and associates it with an Entry record
     *
     * @param key a key referring to this record
     * @param val the value contained in this record
     *
     * @return the record's unique identifier, that would be subsequently returned
     *         by a call to {@code getIndex(getKey(data))}
     */
    public long add(Key key, Value val) {
        wlock.lock();
        try { return map.add(key,val); }
        finally {
            ++opid;
            wlock.unlock();
        }
    }

    /**
     * Allocates an index and associates it with an Entry record, if no record for this key exists.
     *
     * @param key a key referring to this record
     * @param val the value contained in this record
     *
     * @return the record's unique identifier, that would be subsequently returned
     *         by a call to {@code getIndex(getKey(data))}, or {@literal -1L} if the record was not added
     */
    public long addIfAbsent(Key key, Value val) {
        wlock.lock();
        try {
            long idx = map.index(key);
            if(idx<0) {
                ++opid;
                return map.add(key,val);
            }
            else return -1L;
        }
        finally {
            wlock.unlock();
        }
    }

    /**
     * Allocates an index and associates it with an Entry record
     *
     * @param item the data contained in this record
     *
     * @return the record's unique identifier, that would be subsequently returned
     *         by a call to {@code getIndex(getKey(data))}
     */
    public long add(Entry<Key,Value> item) {
        wlock.lock();
        try { return map.add(item); }
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
        try { map.del(index); }
        finally {
            ++opid;
            wlock.unlock();
        }
    }

    /**
     * Deletes an Entry record, and invalidates the associated index mapping,
     * if the record exists and has the specified value.
     *
     * @param key a key referring to this record
     * @param val the value contained in this record
     *
     * @return {@literal true} if the record was deleted, {@literal false} otherwise
     */
    public boolean delForKey(Key key, Value val) {
        wlock.lock();
        try {
            long idx = map.index(key);
            if(idx>=0 && map.getValue(idx)==val) {
                ++opid;
                map.del(idx);
                return true;
            }
            else return false;
        }
        finally {
            wlock.unlock();
        }
    }

    /**
     * Deletes an Entry record, and invalidates the associated index mapping,
     * if the record exists and has the specified value.
     *
     * @param index a unique identifier for this record
     * @param val the value contained in this record
     *
     * @return {@literal true} if the record was deleted, {@literal false} otherwise
     */
    public boolean del(long index, Value val) {
        wlock.lock();
        try {
            if(map.getValue(index)==val) {
                ++opid;
                map.del(index);
                return true;
            }
            else return false;
        }
        finally {
            wlock.unlock();
        }
    }


    /**********************************************************************************
     **  Map entries iterators
     **/

    /**
     * Iterates over all indexes in the map, using an {@link net.varkhan.base.containers.Index}.
     *
     * @return an iterator over all the indexes that designate elements in the map
     */
    public Index indexes() {
        rlock.lock();
        try {
            return new Index() {
                final Index iterator=map.indexes();
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
     * @return an iterable over all the indexes that designate elements in the map
     */
    public java.lang.Iterable<Long> iterateIndexes() {
        return new java.lang.Iterable<Long>() {
            public java.util.Iterator<Long> iterator() {
                rlock.lock();
                try {
                    return new java.util.Iterator<Long>() {
                        final java.util.Iterator<Long> iterator=map.iterateIndexes().iterator();
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


    private class SyncEntry implements Entry<Key,Value> {
        private final Entry<Key,Value> entry;
        private long exid=opid;

        public SyncEntry(Entry<Key,Value> entry) { this.entry=entry; }

        /**
         * Returns the index corresponding to this entry.
         *
         * @return the index corresponding to this entry
         */
        public long index() {
            rlock.lock();
            try {
                return entry.index();
            }
            finally { rlock.unlock(); }
        }

        /**
         * Returns the key corresponding to this entry.
         *
         * @return the key corresponding to this entry
         */
        public Key getKey() {
            rlock.lock();
            try {
                return entry.getKey();
            }
            finally { rlock.unlock(); }
        }

        /**
         * Returns the value corresponding to this entry.
         *
         * @return the value corresponding to this entry
         */
        public Value getValue() {
            rlock.lock();
            try {
                return entry.getValue();
            }
            finally { rlock.unlock(); }
        }

        /**
         * Replaces the value corresponding to this entry with the specified
         * value, in the entry and in the backing map.
         *
         * @param val the new value to be stored in this entry
         *
         * @return the old value corresponding to the entry
         *
         * @throws UnsupportedOperationException if the backing map is read-only
         */
        public Value setValue(Value val) {
            wlock.lock();
            try {
                if(exid!=opid) throw new ConcurrentModificationException();
                exid=++opid;
                return entry.setValue(val);
            }
            finally { wlock.unlock(); }
        }
    }


    /**
     * Iterates over all elements in the map.
     *
     * @return an iterator over all the elements stored in the map
     */
    public Iterator<? extends Entry<Key,Value>> iterator() {
        rlock.lock();
        try {
            return new Iterator<Entry<Key,Value>>() {
                final Iterator<? extends Entry<Key,Value>> iterator=map.iterator();
                private long exid=opid;

                public boolean hasNext() {
                    rlock.lock();
                    try {
                        if(exid!=opid) throw new ConcurrentModificationException();
                        return iterator.hasNext();
                    }
                    finally { rlock.unlock(); }
                }

                public Entry<Key,Value> next() {
                    rlock.lock();
                    try {
                        if(exid!=opid) throw new ConcurrentModificationException();
                        return new SyncEntry(iterator.next());
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
     * Iterate over each element of the map, and pass it as argument to a
     * visitor's {@link Visitor#invoke} method, until this method returns
     * a negative count.
     *
     * @param vis the visitor
     * @param par the control parameter
     * @param <Par> the type of the control parameter
     *
     * @return the sum of all positive return values from the visitor
     */
    public <Par> long visit(final Visitor<Entry<Key,Value>,Par> vis, Par par) {
        rlock.lock();
        try {
            return map.visit(new Visitor<Entry<Key,Value>,Par>() {
                private final long exid=opid;

                public long invoke(Entry<Key,Value> obj, Par par) {
                    if(exid!=opid) throw new ConcurrentModificationException();
                    return vis.invoke(obj, par);
                }
            }, par);
        }
        finally { rlock.unlock(); }
    }

    /**
     * Iterate over each element of the map, and pass it as argument to a
     * visitor's {@link Visitor#invoke} method, until this method returns
     * a negative count.
     *
     * @param vis the visitor
     * @param par the control parameter
     * @param <Par> the type of the control parameter
     *
     * @return the sum of all positive return values from the visitor
     */
    public <Par> long visit(final IndexedMapVisitor<Key,Value,Par> vis, Par par) {
        rlock.lock();
        try {
            return map.visit(new IndexedMapVisitor<Key,Value,Par>() {
                private final long exid=opid;

                public long invoke(long index, Key key, Value val, Par par) {
                    if(exid!=opid) throw new ConcurrentModificationException();
                    return vis.invoke(index, key, val, par);
                }
            }, par);
        }
        finally { rlock.unlock(); }
    }

    /**
     * Iterate over each element of the map, and pass it as argument to a
     * visitor's {@link IndexedVisitor#invoke} method, until this method returns
     * a negative count.
     *
     * @param vis the visitor
     * @param par the control parameter
     * @param <Par> the type of the control parameter
     *
     * @return the sum of all positive return values from the visitor
     */
    public <Par> long visit(final IndexedVisitor<Entry<Key,Value>,Par> vis, Par par) {
        rlock.lock();
        try {
            return map.visit(new IndexedVisitor<Entry<Key,Value>,Par>() {
                private final long exid=opid;

                public long invoke(long idx, Entry<Key,Value> obj, Par par) {
                    if(exid!=opid) throw new ConcurrentModificationException();
                    return vis.invoke(idx, obj, par);
                }
            }, par);
        }
        finally { rlock.unlock(); }
    }

    /**
     * Iterates over a map of elements designated by an array of indexes.
     *
     * @param indexes an array of identifiers
     *
     * @return an iterable over all the elements indexed by the identifiers
     */
    public Iterable<? extends Entry<Key,Value>> iterate(final long[] indexes) {
        return new net.varkhan.base.containers.Iterable<Entry<Key,Value>>() {
            public Iterator<Entry<Key,Value>> iterator() {
                rlock.lock();
                try {
                    return new Iterator<Entry<Key,Value>>() {
                        final Iterator<? extends Entry<Key,Value>> iterator=map.iterate(indexes).iterator();
                        private long exid=opid;

                        public boolean hasNext() {
                            rlock.lock();
                            try {
                                if(exid!=opid) throw new ConcurrentModificationException();
                                return iterator.hasNext();
                            }
                            finally { rlock.unlock(); }
                        }

                        public Entry<Key,Value> next() {
                            rlock.lock();
                            try {
                                if(exid!=opid) throw new ConcurrentModificationException();
                                return new SyncEntry(iterator.next());
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
     * Iterates over a map of elements designated by an iterator over indexes.
     *
     * @param indexes an iterable over identifiers
     *
     * @return an iterable over all the elements indexed by the identifiers
     */
    public Iterable<? extends Entry<Key,Value>> iterate(final java.lang.Iterable<Long> indexes) {
        return new Iterable<Entry<Key,Value>>() {
            public Iterator<Entry<Key,Value>> iterator() {
                rlock.lock();
                try {
                    return new Iterator<Entry<Key,Value>>() {
                        final Iterator<? extends Entry<Key,Value>> iterator=map.iterate(indexes).iterator();
                        private long exid=opid;

                        public boolean hasNext() {
                            rlock.lock();
                            try {
                                if(exid!=opid) throw new ConcurrentModificationException();
                                return iterator.hasNext();
                            }
                            finally { rlock.unlock(); }
                        }

                        public Entry<Key,Value> next() {
                            rlock.lock();
                            try {
                                if(exid!=opid) throw new ConcurrentModificationException();
                                return new SyncEntry(iterator.next());
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
     * Iterates over a map of elements designated by an iterator over indexes
     *
     * @param indexes an iterable over identifiers
     *
     * @return an iterable over all the elements indexed by the identifiers
     */
    public Iterable<? extends Entry<Key,Value>> iterate(final Indexable indexes) {
        return new Iterable<Entry<Key,Value>>() {
            public Iterator<Entry<Key,Value>> iterator() {
                rlock.lock();
                try {
                    return new Iterator<Entry<Key,Value>>() {
                        final Iterator<? extends Entry<Key,Value>> iterator=map.iterate(indexes).iterator();
                        private long exid=opid;

                        public boolean hasNext() {
                            rlock.lock();
                            try {
                                if(exid!=opid) throw new ConcurrentModificationException();
                                return iterator.hasNext();
                            }
                            finally { rlock.unlock(); }
                        }

                        public Entry<Key,Value> next() {
                            rlock.lock();
                            try {
                                if(exid!=opid) throw new ConcurrentModificationException();
                                return new SyncEntry(iterator.next());
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
     **  Map entry and iterators
     **/

    /**
     * An indexed container of all the keys in this map
     *
     * @return a container, backed by the map, providing a view of the keys in the map
     */
    public IndexedContainer<Key> keys() {
        return new SyncIndexedContainer<Key>(map.keys(),rlock,wlock) { };
    }

    /**
     * An indexed container of all the values in this map
     *
     * @return a container, backed by the map, providing a view of the values in the map
     */
    public IndexedContainer<Value> values() {
        return new SyncIndexedContainer<Value>(map.values(),rlock,wlock) { };
    }

}
