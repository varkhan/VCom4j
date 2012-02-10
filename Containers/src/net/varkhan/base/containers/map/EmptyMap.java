package net.varkhan.base.containers.map;


import net.varkhan.base.containers.Container;
import net.varkhan.base.containers.Iterator;
import net.varkhan.base.containers.Visitable;
import net.varkhan.base.containers.set.EmptySet;


/**
 * <b>An empty, unmodifiable map</b>.
 * <p/>
 *
 * @author varkhan
 * @date 1/30/11
 * @time 11:10 PM
 */
public class EmptyMap<K,V> implements Map<K,V> {

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
     * Indicate whether a given key is present in this container.
     *
     * @param key the key part of the entry
     *
     * @return {@literal false}
     */
    public boolean has(K key) { return false; }

    /**
     * Retrieves a value for a given key in this map.
     *
     * @param key the key
     *
     * @return {@literal null}
     */
    public V get(K key) { return null; }

    /**
     * Adds an entry to this map (this method does not actually do anything).
     *
     * @param item the entry (key/value pair) to add
     *
     * @return {@literal false}
     */
    public boolean add(Entry<K,V> item) { return false; }

    /**
     * Adds an entry to the map (this method does not actually do anything).
     *
     * @param key the key part of the entry
     * @param val the value
     *
     * @return {@literal false}
     */
    public boolean add(K key, V val) { return false; }

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
    public Iterator<? extends Entry<K,V>> iterator() {
        return new Iterator<Entry<K,V>>() {
            public boolean hasNext() { return false; }
            public Entry<K,V> next() { return null; }
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
    public <Par> long visit(Visitor<Entry<K,V>,Par> vis, Par par) { return 0; }

    /**
     * A container of all the keys in this map
     *
     * @return a container, backed by the map, providing a view of the keys in the map
     */
    public Container<K> keys() { return new EmptySet<K>(); }

    /**
     * A container of all the values in this map
     *
     * @return a container, backed by the map, providing a view of the values in the map
     */
    public Container<V> values() { return new EmptySet<V>(); }
}
