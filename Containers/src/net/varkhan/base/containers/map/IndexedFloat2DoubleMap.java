/**
 *
 */
package net.varkhan.base.containers.map;

import net.varkhan.base.containers.Index;
import net.varkhan.base.containers.Indexable;
import net.varkhan.base.containers.Iterable;
import net.varkhan.base.containers.Iterator;
import net.varkhan.base.containers.type.IndexedDoubleContainer;
import net.varkhan.base.containers.type.IndexedFloatContainer;


/**
 * <b>A searchable indexed collection, that associates keys to values, references each such pair by a unique index, and does not allow duplicate keys.</b>.
 * <p/>
 * An indexed map of objects, that associates with each unique key an entry,
 * constituted of a unique identifier, and a modifiable value. Given a key that
 * is mapped to an entry, the associated index can be retrieved, which can be
 * used in reading, writing, and removal operations on the mapping value.
 * <p/>
 *
 * @author varkhan
 * @date Mar 19, 2009
 * @time 3:37:27 AM
 */
public interface IndexedFloat2DoubleMap extends IndexedMap<Float,Double>/*, IndexedObj2DoubleMap<Float>, IndexedFloat2ObjMap<Double>*/ {


    /**********************************************************************************
     **  IndexedMap.Entry interface
     **/

    /**
     * <b>An indexed map entry (key-index-value triplet)</b>
     */
    public interface Entry extends IndexedMap.Entry<Float,Double>, IndexedObj2DoubleMap.Entry<Float>, IndexedFloat2ObjMap.Entry<Double> {

        /**
         * Returns the index corresponding to this entry.
         *
         * @return the index corresponding to this entry
         */
        public long index();

        /**
         * Returns the key corresponding to this entry.
         *
         * @return the key corresponding to this entry
         */
        public float getFloatKey();

        /**
         * Returns the value corresponding to this entry.
         *
         * @return the value corresponding to this entry
         */
        public double getDoubleValue();

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
        public double setDoubleValue(double val);
    }

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


    /**
     * Gets the default key
     *
     * @return the value retrieved for indexes that have no associated key
     */
    public float getDefaultKey();

    /**
     * Sets the default key
     *
     * @param def the new default key
     */
    public void setDefaultKey(float def);

    /**
     * Gets the default value
     *
     * @return the value retrieved for indexes that have no associated key
     */
    public double getDefaultValue();

    /**
     * Sets the default value
     *
     * @param def the new default value
     */
    public void setDefaultValue(double def);


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
    public long index(float key);

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
    public IndexedFloat2DoubleMap.Entry get(long index);

    /**
     * Gets the key designated by an index.
     *
     * @param index a unique identifier
     *
     * @return the requested key, or {@literal null} if no element is available at this index
     */
    public float getFloatKey(long index);

    /**
     * Gets the value designated by an index.
     *
     * @param index a unique identifier
     *
     * @return the requested value at this index
     */
    public double getDoubleValue(long index);

    /**
     * Sets the value designated by an index.
     *
     * @param index a unique identifier
     * @param value the new value at this index
     */
    public void setDoubleValue(long index, double value);

    /**
     * Allocates an index and associates it with an Entry record
     *
     * @param key a key referring to this record
     * @param val the value contained in this record
     *
     * @return the record's unique identifier, that would be subsequently returned
     *         by a call to {@code getIndex(getKey(data))}
     */
    public long add(float key, double val);

    /**
     * Allocates an index and associates it with an Entry record
     *
     * @param item the data contained in this record
     *
     * @return the record's unique identifier, that would be subsequently returned
     *         by a call to {@code getIndex(getKey(data))}
     */
    public long add(IndexedFloat2DoubleMap.Entry item);

    /**
     * Deletes an Entry record, and invalidates the associated index mapping
     *
     * @param index a unique identifier for this record
     */
    public void del(long index);


    /**********************************************************************************
     **  Entry list iterators
     **/

    /**
     * Returns an {@link Index} over the indexes in the set.
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
    public Iterator<? extends IndexedFloat2DoubleMap.Entry> iterator();

    /**
     * Iterates over a set of elements designated by an array of indexes.
     *
     * @param indexes an array of indexes
     *
     * @return an iterable over all the elements associated to the identifiers
     */
    public Iterable<? extends IndexedFloat2DoubleMap.Entry> iterate(long[] indexes);

    /**
     * Iterates over a set of elements designated by an iterator over indexes.
     *
     * @param indexes an iterable over indexes
     *
     * @return an iterable over all the elements associated to the identifiers
     */
    public Iterable<? extends IndexedFloat2DoubleMap.Entry> iterate(java.lang.Iterable<Long> indexes);

    /**
     * Iterates over a set of elements designated by an iterator over indexes.
     *
     * @param indexes an iterable over indexes
     *
     * @return an iterable over all the elements associated to the identifiers
     */
    public Iterable<? extends IndexedFloat2DoubleMap.Entry> iterate(Indexable indexes);

    /**
     * An indexed container of all the keys in this map
     *
     * @return a container, backed by the map, providing a view of the keys in the map
     */
    public IndexedFloatContainer keys();

    /**
     * An indexed container of all the values in this map
     *
     * @return a container, backed by the map, providing a view of the values in the map
     */
    public IndexedDoubleContainer values();

}
