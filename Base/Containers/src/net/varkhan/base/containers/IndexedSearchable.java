/**
 *
 */
package net.varkhan.base.containers;

/**
 * <b>A searchable indexed container.</b>
 * <p/>
 * This container stores objects, that are mapped to a unique index through
 * which they can be retrieved. An {@link Index} over those indexes, and
 * several {@link Iterator}s allow access to all the elements of the container.
 * <p/>
 *
 * @param <Key>   the type of the keys used to search indexes
 * @param <Value> the type of the values stored in the container
 *
 * @author varkhan
 * @date Mar 19, 2009
 * @time 1:39:19 AM
 */
public interface IndexedSearchable<Key,Value> extends IndexedContainer<Value> {


    /**********************************************************************************
     **  Global information accessors
     **/

    /**
     * Returns the number of keys / values in this container.
     *
     * @return the number of entries (keys, values and related indexes) stored in this container
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
     * Returns the smallest position higher that any valid index in this container.
     *
     * @return the highest valid index plus one
     */
    public long head();


    /**********************************************************************************
     **  Container values accessors
     **/

    /**
     * Search for a given key in this container, and returns the associated index.
     *
     * @param key a key referring to an entry
     *
     * @return the index mapped to this key, or {@literal -1L} if the specified key does not map to an index
     */
    public long index(Key key);

    /**
     * Indicates whether an index is valid (has an associated value).
     *
     * @param index a unique identifier
     *
     * @return {@literal true} if a value is available at this index,
     *         or {@literal false} if no value is available at this index
     */
    public boolean has(long index);

    /**
     * Obtains the value designated by an index.
     *
     * @param index a unique identifier
     *
     * @return the requested object, or {@literal null} if no value is available at this index
     */
    public Value get(long index);


    /**********************************************************************************
     **  Container values iterators
     **/

    /**
     * Returns an {@link Index} over the indexes in the container.
     *
     * @return an {@code Index} enumerating all indexes in the container
     */
    public Index indexes();

    /**
     * Iterates over all indexes in the container.
     *
     * @return an iterable over all the indexes that designate values in the container
     */
    public java.lang.Iterable<Long> iterateIndexes();

    /**
     * Iterates over all values in the container.
     *
     * @return an iterator over all the values stored in the container
     */
    public Iterator<? extends Value> iterator();

    /**
     * Iterate over each element of the container, and pass it as argument to a
     * visitor's {@link Visitor#invoke(Object, Object)} method, until this method returns
     * a negative count.
     *
     *
     * @param vis the visitor
     *
     * @param par
     * @return the sum of all positive return values from the visitor
     */
    public <Par> long visit(Visitor<Value,Par> vis, Par par);

    /**
     * Iterates over a set of values designated by an array of indexes.
     *
     * @param indexes an array of indexes
     *
     * @return an iterable over all the values associated to the identifiers
     */
    public Iterable<? extends Value> iterate(long[] indexes);

    /**
     * Iterates over a set of values designated by an iterator over indexes.
     *
     * @param indexes an iterable over indexes
     *
     * @return an iterable over all the values associated to the identifiers
     */
    public Iterable<? extends Value> iterate(java.lang.Iterable<Long> indexes);

    /**
     * Iterates over a set of values designated by an iterator over indexes.
     *
     * @param indexes an iterable over indexes
     *
     * @return an iterable over all the values associated to the identifiers
     */
    public Iterable<? extends Value> iterate(Indexable indexes);

}
