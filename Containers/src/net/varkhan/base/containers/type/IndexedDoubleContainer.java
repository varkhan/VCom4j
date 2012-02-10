/**
 *
 */
package net.varkhan.base.containers.type;

import net.varkhan.base.containers.Index;
import net.varkhan.base.containers.Indexable;
import net.varkhan.base.containers.IndexedContainer;


/**
 * <b>A modifiable container that associates a {@code long} index to each element.</b>
 * <p/>
 * This container stores doubles, that are mapped to a unique index through
 * which they can be retrieved. An {@link Index} over those indexes, and
 * several {@link DoubleIterable.DoubleIterator}s allow access to all the elements of the collection.
 * <p/>
 *
 * @author varkhan
 * @date Mar 11, 2009
 * @time 1:19:16 AM
 */
public interface IndexedDoubleContainer extends IndexedContainer<Double>, DoubleIterable, DoubleVisitable, IndexedDoubleVisitable {


    /**********************************************************************************
     **  Collection statistics accessors
     **/

    /**
     * Returns the number of elements in this collection.
     *
     * @return the number of entries (elements and related indexes) stored in this collection
     */
    public long size();

    /**
     * Indicates whether this collection is empty.
     *
     * @return {@literal true} if this collection contains no element,
     *         {@literal false} otherwise
     */
    public boolean isEmpty();

    /**
     * Returns the smallest upper bound of valid indexes in this collection.
     *
     * @return the smallest index strictly greater than any valid index
     *         associated to a stored object in the collection, or equivalently
     *         the highest such valid index plus one
     */
    public long head();

    /**
     * Gets the default value
     *
     * @return the default value, returned by {@link #getDouble} on empty entries
     */
    public double getDefaultValue();


    /**********************************************************************************
     **  Collection elements accessors
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
    public Double get(long index);

    /**
     * Obtains the element designated by an index.
     *
     * @param index a unique identifier
     *
     * @return the requested object, or {@literal null} if no element is available at this index
     */
    public double getDouble(long index);


    /**********************************************************************************
     **  Collection elements iterators
     **/

    /**
     * Returns an {@link net.varkhan.base.containers.Index} over the indexes in the collection.
     *
     * @return an {@code Index} enumerating all indexes in the collection
     */
    public Index indexes();

    /**
     * Iterates over all indexes in the collection.
     *
     * @return an iterable over all the indexes that designate elements in the collection
     */
    public java.lang.Iterable<Long> iterateIndexes();

    /**
     * Iterates over all elements in the collection.
     *
     * @return an iterator over all the elements stored in the collection
     */
    public DoubleIterator iterator();

    /**
     * Iterate over each element of the collection, and pass it as argument to a
     * visitor's {@link net.varkhan.base.containers.Visitable.Visitor#invoke} method, until this method returns
     * a negative count.
     *
     * @param vis the visitor
     * @param par the control parameter
     * @param <Par> the type of the control parameter
     *
     * @return the sum of all positive return values from the visitor
     */
    public <Par> long visit(Visitor<Double,Par> vis, Par par);

    /**
     * Iterate over each element of the collection, and pass it as argument to a
     * visitor's {@link net.varkhan.base.containers.IndexedVisitable.IndexedVisitor#invoke} method, until this method returns
     * a negative count.
     *
     * @param vis the visitor
     * @param par the control parameter
     * @param <Par> the type of the control parameter
     *
     * @return the sum of all positive return values from the visitor
     */
    public <Par> long visit(IndexedVisitor<Double,Par> vis, Par par);

    /**
     * Iterate over each element of the collection, and pass it as argument to a
     * visitor's {@link net.varkhan.base.containers.Visitable.Visitor#invoke} method, until this method returns
     * a negative count.
     *
     * @param vis the visitor
     * @param par the control parameter
     * @param <Par> the type of the control parameter
     *
     * @return the sum of all positive return values from the visitor
     */
    public <Par> long visit(DoubleVisitor<Par> vis, Par par);

    /**
     * Iterate over each element of the collection, and pass it as argument to a
     * visitor's {@link net.varkhan.base.containers.IndexedVisitable.IndexedVisitor#invoke} method, until this method returns
     * a negative count.
     *
     * @param vis the visitor
     * @param par the control parameter
     * @param <Par> the type of the control parameter
     *
     * @return the sum of all positive return values from the visitor
     */
    public <Par> long visit(IndexedDoubleVisitor<Par> vis, Par par);

    /**
     * Iterates over a set of elements designated by an array of indexes.
     *
     * @param indexes an array of indexes
     *
     * @return an iterable over all the elements associated to the identifiers
     */
    public DoubleIterable iterate(long[] indexes);

    /**
     * Iterates over a set of elements designated by an iterator over indexes.
     *
     * @param indexes an iterable over indexes
     *
     * @return an iterable over all the elements associated to the identifiers
     */
    public DoubleIterable iterate(java.lang.Iterable<Long> indexes);

    /**
     * Iterates over a set of elements designated by an iterator over indexes.
     *
     * @param indexes an iterable over indexes
     *
     * @return an iterable over all the elements associated to the identifiers
     */
    public DoubleIterable iterate(Indexable indexes);

}
