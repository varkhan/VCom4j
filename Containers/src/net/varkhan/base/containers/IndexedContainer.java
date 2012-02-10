/**
 *
 */
package net.varkhan.base.containers;

/**
 * <b>A container that associates a {@code long} index to each element.</b>
 * <p/>
 * This container stores objects, that are mapped to a unique index through
 * which they can be retrieved. An {@link Index} over those indexes, and
 * several {@link Iterator}s allow access to all the elements of the container.
 * <p/>
 *
 * @param <Type> the type of the objects stored in the container
 *
 * @author varkhan
 * @date Mar 11, 2009
 * @time 1:19:16 AM
 */
public interface IndexedContainer<Type> extends Indexed<Type>, IndexedVisitable<Type>, Container<Type> {


    /**********************************************************************************
     **  Container statistics accessors
     **/

    /**
     * Returns the number of elements in this container.
     *
     * @return the number of entries (elements and related indexes) stored in this container
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
     * Returns an upper bound of valid indexes in this container.
     * <p/>
     * Implementors should return a value that is not unnecessarily big,
     * such as one above the actual greatest used index.
     *
     * @return an integer strictly greater than any valid index
     *         associated to a stored object in the container
     */
    public long head();


    /**********************************************************************************
     **  Container elements accessors
     **/

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
     * Obtains the element designated by an index.
     *
     * @param index a unique identifier
     *
     * @return the requested object, or {@literal null} if no element is available at this index
     */
    public Type get(long index);


    /**********************************************************************************
     **  Container elements iterators
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
     * @return an iterable over all the indexes that designate elements in the container
     */
    public java.lang.Iterable<Long> iterateIndexes();

    /**
     * Iterates over all elements in the container.
     *
     * @return an iterator over all the elements stored in the container
     */
    public Iterator<? extends Type> iterator();

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
    public <Par> long visit(Visitor<Type,Par> vis, Par par);

    /**
     * Iterate over each element of the collection, and pass it as argument to a
     * visitor's {@link IndexedVisitor#invoke} method, until this method returns
     * a negative count.
     *
     * @param vis the visitor
     * @param par the control parameter
     * @param <Par> the type of the control parameter
     *
     * @return the sum of all positive return values from the visitor
     */
    public <Par> long visit(IndexedVisitor<Type,Par> vis, Par par);

    /**
     * Iterates over a set of elements designated by an array of indexes.
     *
     * @param indexes an array of indexes
     *
     * @return an iterable over all the elements associated to the identifiers
     */
    public Iterable<? extends Type> iterate(long[] indexes);

    /**
     * Iterates over a set of elements designated by an iterator over indexes.
     *
     * @param indexes an iterable over indexes
     *
     * @return an iterable over all the elements associated to the identifiers
     */
    public Iterable<? extends Type> iterate(java.lang.Iterable<Long> indexes);

    /**
     * Iterates over a set of elements designated by an iterator over indexes.
     *
     * @param indexes an iterable over indexes
     *
     * @return an iterable over all the elements associated to the identifiers
     */
    public Iterable<? extends Type> iterate(Indexable indexes);

}
