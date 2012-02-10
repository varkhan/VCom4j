/**
 *
 */
package net.varkhan.base.containers.map;

import net.varkhan.base.containers.Iterator;
import net.varkhan.base.containers.type.FloatContainer;
import net.varkhan.base.containers.type.LongContainer;


/**
 * <b>A searchable container that associates keys to values, and does not allow duplicate keys.</b>
 * <p/>
 * An indexed map of objects, that associates with each unique key an entry,
 * constituted of a modifiable value.
 * <p/>
 *
 * @author varkhan
 * @date Jun 30, 2009
 * @time 3:24:43 AM
 */
public interface Long2FloatMap extends Map<Long,Float>/*, Obj2FloatMap<Long>, Long2ObjMap<Float>*/ {


    /**********************************************************************************
     **  Map.Entry interface
     **/

    /**
     * <b>An indexed map entry (key-index-value triplet)</b>
     */
    public interface Entry extends Map.Entry<Long,Float>, Obj2FloatMap.Entry<Long>, Long2ObjMap.Entry<Float> {

        /**
         * Returns the key corresponding to this entry.
         *
         * @return the key corresponding to this entry
         */
        public long getLongKey();

        /**
         * Returns the value corresponding to this entry.
         *
         * @return the value corresponding to this entry
         */
        public float getFloatValue();

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
        public float setFloatValue(float val);
    }

    /**********************************************************************************
     **  Container statistics accessors
     **/

    /**
     * Returns the number of elements in this container.
     *
     * @return the number of elements stored in this list
     */
    public long size();

    /**
     * Indicates whether this container is empty.
     *
     * @return {@literal true} if this container contains no element,
     *         {@literal false} otherwise
     */
    public boolean isEmpty();

    /**
     * Deletes all elements from this set.
     */
    public void clear();


    /**********************************************************************************
     **  Container values accessors
     **/

    /**
     * Indicates whether a given key is present in this container.
     *
     * @param key the key part of the entry
     *
     * @return {@literal true} if this key is in the container,
     *         or {@literal false} if this key is absent
     */
    public boolean has(long key);

    /**
     * Retrieves a value for a given key in this map.
     *
     * @param key the key
     * @return the value matching the key in the map,
     *         or {@literal null} if the key is not present in the map
     */
    public Float get(Long key);

    /**
     * Retrieves a value for a given key in this map.
     *
     * @param key the key
     * @return the value matching the key in the map,
     *         or {@literal null} if the key is not present in the map
     */
    public Float get(long key);

    /**
     * Retrieves a value for a given key in this map.
     *
     * @param key the key
     * @return the value matching the key in the map,
     */
    public float getFloat(Long key);

    /**
     * Retrieves a value for a given key in this map.
     *
     * @param key the key
     * @return the value matching the key in the map,
     */
    public float getFloat(long key);

    /**
     * Adds an entry to this map.
     *
     * @param item the entry (key/value pair) to add
     *
     * @return {@literal true} if the container has been modified as a result of
     *         this operation, {@literal false} if the container remains unchanged (for
     *         instance, because the key is already present, with the same value)
     */
    public boolean add(Long2FloatMap.Entry item);

    /**
     * Adds an entry to the map.
     *
     * @param key the key part of the entry
     * @param val the value
     *
     * @return {@literal true} if the container has been modified as a result of
     *         this operation, {@literal false} if the container remains unchanged (for
     *         instance, because the key is already present, with the same value)
     */
    public boolean add(long key, float val);

    /**
     * Removes an element from this container.
     *
     * @param key the element to remove
     *
     * @return {@literal true} if the container has been modified as a result of
     *         this operation, {@literal false} if the container remains unchanged (for
     *         instance, because the element was not initially in the container)
     */
    public boolean del(long key);


    /**********************************************************************************
     **  Map elements iterators
     **/

    /**
     * Iterates over all elements in the container.
     *
     * @return an iterable over all the elements stored in the container
     */
    public Iterator<? extends Long2FloatMap.Entry> iterator();

    /**
     * A container of all the keys in this map
     *
     * @return a container, backed by the map, providing a view of the keys in the map
     */
    public LongContainer keys();

    /**
     * A container of all the values in this map
     *
     * @return a container, backed by the map, providing a view of the values in the map
     */
    public FloatContainer values();

}
