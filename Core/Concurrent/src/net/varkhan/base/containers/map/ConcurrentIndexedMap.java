/**
 *
 */
package net.varkhan.base.containers.map;

import net.varkhan.base.containers.*;
import net.varkhan.base.containers.Iterable;


/**
 * <b>A thread-safe searchable indexed collection, that associates keys to values, references each such pair by a unique index, and does not allow duplicate keys.</b>.
 * <p/>
 * A thread-safe indexed map of objects, that associates with each unique key an entry,
 * constituted of a unique identifier, and a modifiable value. Given a key that
 * is mapped to an entry, the associated index can be retrieved, which can be
 * used in concurrent reading, writing, and removal operations on the mapping value.
 * <p/>
 *
 * @param <Key>   the type of the keys used to search indexes
 * @param <Value> the type of the values stored in the container
 *
 * @author varkhan
 * @date Mar 19, 2009
 * @time 3:37:27 AM
 */
public interface ConcurrentIndexedMap<Key,Value> extends IndexedMap<Key,Value> {



    /**********************************************************************************
     **  Global information accessors
     **/

    /**
     * Returns the number of entries in this map.
     *
     * @return the number of entries (elements and related indexes) stored in this map
     */
    public long size();

    /**
     * Indicates whether this map is empty.
     *
     * @return {@literal true} if this map contains no entry,
     *         {@literal false} otherwise
     */
    public boolean isEmpty();

    /**
     * Returns the smallest position higher that any valid index in this map.
     *
     * @return the highest valid index plus one
     */
    public long head();

    /**
     * Deletes all entries from this map.
     */
    public void clear();


    /**********************************************************************************
     **  Direct mapping accessors
     **/

    /**
     * Search for a given key in this set, and returns the associated index
     *
     * @param key a key referring to an entry
     *
     * @return the index for this entry, or {@literal -1L} if the specified key is not in the set
     */
    public long index(Key key);

    /**
     * Indicates whether an index is valid (has an associated element).
     *
     * @param index a unique identifier
     *
     * @return {@literal true} if an element is available at this index,
     *         or {@literal false} if no element is available at this index
     */
    public boolean has(long index);

    /**
     * Obtains the entry designated by an index.
     *
     * @param index a unique identifier
     *
     * @return the requested entry, or {@literal null} if no element is available at this index
     */
    public IndexedMap.Entry<Key,Value> get(long index);

    /**
     * Gets the key designated by an index.
     *
     * @param index a unique identifier
     *
     * @return the requested key, or {@literal null} if no element is available at this index
     */
    public Key getKey(long index);

    /**
     * Gets the value designated by an index.
     *
     * @param index a unique identifier
     *
     * @return the requested value at this index
     */
    public Value getValue(long index);

    /**
     * Sets the value designated by an index.
     *
     * @param index a unique identifier
     * @param value the new value at this index
     */
    public void setValue(long index, Value value);

    /**
     * Replaces the value designated by a key, if the record exists and has the specified value.
     *
     * @param key a key referring to this record
     * @param oldval the old value at this index
     * @param newval the new value at this index
     *
     * @return {@literal true} if the record was updated, {@literal false} otherwise
     */
    public boolean repForKey(Key key, Value oldval, Value newval);

    /**
     * Replaces the value designated by an index, if the record exists and has the specified value.
     *
     * @param index a unique identifier
     * @param oldval the old value at this index
     * @param newval the new value at this index
     *
     * @return {@literal true} if the record was updated, {@literal false} otherwise
     */
    public boolean rep(long index, Value oldval, Value newval);

    /**
     * Allocates an index and associates it with an Entry record
     *
     * @param key a key referring to this record
     * @param val the value contained in this record
     *
     * @return the record's unique identifier, that would be subsequently returned
     *         by a call to {@code getIndex(getKey(data))}
     */
    public long add(Key key, Value val);

    /**
     * Allocates an index and associates it with an Entry record, if no record for this key exists.
     *
     * @param key a key referring to this record
     * @param val the value contained in this record
     *
     * @return the record's unique identifier, that would be subsequently returned
     *         by a call to {@code getIndex(getKey(data))}, or {@literal -1L} if the record was not added
     */
    public long addIfAbsent(Key key, Value val);

    /**
     * Allocates an index and associates it with an Entry record
     *
     * @param item the data contained in this record
     *
     * @return the record's unique identifier, that would be subsequently returned
     *         by a call to {@code getIndex(getKey(data))}
     */
    public long add(IndexedMap.Entry<Key,Value> item);

    /**
     * Deletes an Entry record, and invalidates the associated index mapping
     *
     * @param index a unique identifier for this record
     */
    public void del(long index);

    /**
     * Deletes an Entry record, and invalidates the associated index mapping,
     * if the record exists and has the specified value.
     *
     * @param key a key referring to this record
     * @param val the value contained in this record
     *
     * @return {@literal true} if the record was deleted, {@literal false} otherwise
     */
    public boolean delForKey(Key key, Value val);

    /**
     * Deletes an Entry record, and invalidates the associated index mapping,
     * if the record exists and has the specified value.
     *
     * @param index a unique identifier for this record
     * @param val the value contained in this record
     *
     * @return {@literal true} if the record was deleted, {@literal false} otherwise
     */
    public boolean del(long index, Value val);


    /**********************************************************************************
     **  Entry list iterators
     **/

    /**
     * Returns an {@link net.varkhan.base.containers.Index} over the indexes in the set.
     *
     * @return an {@code Index} enumerating all indexes in the set
     */
    public Index indexes();

    /**
     * Iterates over all indexes in the set.
     *
     * @return an iterable over all the indexes that designate elements in the set
     */
    public java.lang.Iterable<Long> iterateIndexes();

    /**
     * Iterates over all elements in the set.
     *
     * @return an iterator over all the elements stored in the set
     */
    public Iterator<? extends IndexedMap.Entry<Key,Value>> iterator();

    /**
     * Iterates over a set of elements designated by an array of indexes.
     *
     * @param indexes an array of indexes
     *
     * @return an iterable over all the elements associated to the identifiers
     */
    public Iterable<? extends IndexedMap.Entry<Key,Value>> iterate(long[] indexes);

    /**
     * Iterates over a set of elements designated by an iterator over indexes.
     *
     * @param indexes an iterable over indexes
     *
     * @return an iterable over all the elements associated to the identifiers
     */
    public Iterable<? extends IndexedMap.Entry<Key,Value>> iterate(java.lang.Iterable<Long> indexes);

    /**
     * Iterates over a set of elements designated by an iterator over indexes.
     *
     * @param indexes an iterable over indexes
     *
     * @return an iterable over all the elements associated to the identifiers
     */
    public Iterable<? extends IndexedMap.Entry<Key,Value>> iterate(Indexable indexes);

    /**
     * An indexed container of all the keys in this map
     *
     * @return a container, backed by the map, providing a view of the keys in the map
     */
    public IndexedContainer<Key> keys();

    /**
     * An indexed container of all the values in this map
     *
     * @return a container, backed by the map, providing a view of the values in the map
     */
    public IndexedContainer<Value> values();

}
