package net.varkhan.core.containers;

import net.varkhan.base.containers.Container;
import net.varkhan.base.containers.Iterator;

import java.io.Serializable;
import java.util.ConcurrentModificationException;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantReadWriteLock;


/**
 * <b>Provide multithread safety over an unsynchronized ontainer implementation</b>
 * <p/>
 * This class wraps an unsynchronized Container implementation in a read/write
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
 * Note that since a Container is read-only, no specific thread-safety is provided
 * by this implementation, unless it is subclassed to wrap writable container types
 * using the appropriate locking.
 * <p/>
 *
 * @param <Type> the type of elements in the container
 *
 * @author varkhan
 * @date 2/17/11
 * @time 10:36 PM
 */
public abstract class SyncContainer<Type> implements Container<Type>, Serializable {


    protected static final long serialVersionUID=1L;

    protected final Lock rlock;
    protected final Lock wlock;
    protected final Container<Type> container;
    protected volatile long opid=0;


    /**********************************************************************************
     **  Container constructors
     **/

    /**
     * Wraps a synchronization layer on top of a Container
     *
     * @param container the Container to synchronize
     */
    public SyncContainer(Container<Type> container) {
        this.container=container;
        ReentrantReadWriteLock rwl=new ReentrantReadWriteLock();
        this.rlock=rwl.readLock();
        this.wlock=rwl.writeLock();
    }

    /**
     * Wraps a Container using predefined synchronization locks
     *
     * @param container the Container to synchronize
     * @param rlock the read lock
     * @param wlock the write lock
     */
    public SyncContainer(Container<Type> container, Lock rlock, Lock wlock) {
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
     * @return the number of elements stored in this list
     */
    public long size() {
        rlock.lock();
        try { return container.size(); }
        finally { rlock.unlock(); }
    }

    /**
     * Indicates whether this container is empty.
     *
     * @return {@literal true} if this container contains no element,
     *         {@literal false} otherwise
     */
    public boolean isEmpty() {
        rlock.lock();
        try { return container.isEmpty(); }
        finally { rlock.unlock(); }
    }


    /**********************************************************************************
     **  Container elements iterators
     **/

    /**
     * Iterates over all elements in the container.
     *
     * @return an iterable over all the elements stored in the container
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

}
