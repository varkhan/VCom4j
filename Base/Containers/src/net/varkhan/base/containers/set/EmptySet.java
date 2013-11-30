package net.varkhan.base.containers.set;

import net.varkhan.base.containers.Iterator;

import java.io.Serializable;


/**
 * <b>An empty, unmodifiable set</b>.
 * <p/>
 *
 * @author varkhan
 * @date 1/30/11
 * @time 11:16 PM
 */
public class EmptySet<K> implements Set<K>, Serializable, Cloneable {

    public static final long serialVersionUID=1L;

    /**
     * Returns the number of elements in this container.
     *
     * @return {@literal 0L}
     */
    public long size() { return 0; }

    /**
     * Indicates whether this container is empty.
     *
     * @return {@literal true}
     */
    public boolean isEmpty() { return true; }

    /**
     * Deletes all elements from this set.
     */
    public void clear() { }

    /**
     * Indicates whether a given element is present in this container.
     *
     * @param key the element
     *
     * @return {@literal false}
     */
    public boolean has(K key) { return false; }

    /**
     * Adds an element to this container (this method does not actually do anything).
     *
     * @param key the element to add
     *
     * @return {@literal false}
     */
    public boolean add(K key) { return false; }

    /**
     * Removes an element from this container (this method does not actually do anything).
     *
     * @param key the element to remove
     *
     * @return {@literal false}
     */
    public boolean del(K key) { return false; }

    /**
     * Iterates over all elements in the container.
     *
     * @return an empty iterable
     */
    public Iterator<? extends K> iterator() {
        return new Iterator<K>() {
            public boolean hasNext() { return false; }
            public K next() { return null; }
            public void remove() { }
        };
    }

    /**
     * Iterate over each element of the container, and pass it as argument to a
     * visitor's {@link net.varkhan.base.containers.Visitable.Visitor#invoke} method, until this method returns
     * a negative count.
     *
     * @param vis   the visitor
     * @param par   the control parameter
     * @param <Par> the type of the control parameter
     *
     * @return {@literal 0L}
     */
    public <Par> long visit(Visitor<K,Par> vis, Par par) { return 0; }

    /**
     * Returns a clone of this set.
     *
     * @return an identical, yet independent copy of this set
     */
    @SuppressWarnings("unchecked")
    public EmptySet clone() {
        try { return (EmptySet) super.clone(); }
        catch(CloneNotSupportedException cantHappen) { throw new InternalError(); }
    }

    public int hashCode() {
        return 0;
    }

    public boolean equals(Object obj) {
        return obj instanceof Set && ((Set)obj).isEmpty();
    }

    public String toString() {
        return "{ }";
    }

}
