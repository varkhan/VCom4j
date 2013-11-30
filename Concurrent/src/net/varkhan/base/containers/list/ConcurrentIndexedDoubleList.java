/**
 *
 */
package net.varkhan.base.containers.list;

import net.varkhan.base.containers.Index;
import net.varkhan.base.containers.Indexable;
import net.varkhan.base.containers.type.DoubleIterable;
import net.varkhan.base.containers.type.IndexedDoubleCollection;


/**
 * <b>A thread-safe list of {@code double}s that allow for arbitrary element positions.</b>.
 * <p/>
 * This list stores {@code double}s in arbitrary positions (their index, in the
 * sense of {@link net.varkhan.base.containers.Indexed}).
 * <p/>
 * It can be used as a dense list, where added elements are inserted in the
 * first available position, or a sparse list, where the insertion point can
 * be any arbitrary value, potentially leaving gaps in the list.
 * <p/>
 *
 * @author varkhan
 * @date Mar 12, 2009
 * @time 4:39:05 AM
 */
public interface ConcurrentIndexedDoubleList extends ConcurrentIndexedList<Double>, IndexedDoubleCollection {

    /**********************************************************************************
     **  List statistics accessors
     **/

    /**
     * Returns the number of elements in this list.
     *
     * @return the number of entries (elements and related indexes) stored in this list
     */
    public long size();

    /**
     * Indicates whether this list is empty.
     *
     * @return {@literal true} if this list contains no entry,
     *         {@literal false} otherwise
     */
    public boolean isEmpty();

    /**
     * Returns the smallest position higher that any valid index in this list.
     *
     * @return the highest valid index plus one
     */
    public long head();

    /**
     * Returns an index that has no associated element in this list.
     *
     * @return an unused index
     */
    public long free();

    /**
     * Deletes all elements from this list.
     */
    public void clear();

    /**
     * Gets the default value
     *
     * @return the default value, returned by {@link #getDouble} on empty entries
     */
    public double getDefaultValue();

    /**
     * Sets the default value
     *
     * @param def the default value, returned by {@link #getDouble} on empty entries
     */
    public void setDefaultValue(double def);


    /**********************************************************************************
     **  List entries accessors
     **/

    /**
     * Indicates whether an index has an associated entry.
     *
     * @param index a unique identifier for this entry
     *
     * @return {@literal true} if an element is associated with this index,
     *         or {@literal false} if no element is associated with this index
     */
    public boolean has(long index);

    /**
     * Extracts the element designated by an index.
     *
     * @param index a unique identifier for this entry
     *
     * @return the requested element, or {@literal null} if no entry is associated to this index
     */
    public Double get(long index);

    /**
     * Extracts the element designated by an index.
     *
     * @param index a unique identifier for this entry
     *
     * @return the requested element, or {@literal null} if no entry is associated to this index
     */
    public double getDouble(long index);

    /**
     * Adds an element to the list, associating it to a previously unused index.
     *
     * @param val the object to store in the list
     *
     * @return the entry's unique identifier, that will subsequently give access to the element
     */
    public long add(Double val);

    /**
     * Adds an element to the list, associating it to a previously unused index.
     *
     * @param val the object to store in the list
     *
     * @return the entry's unique identifier, that will subsequently give access to the element
     */
    public long add(double val);

    /**
     * Associates an element to a particular index (the index can refer to an
     * already existing entry, in which case that entry is overwritten).
     *
     * @param index a unique identifier for this entry
     * @param val   the object to store in the list
     *
     * @return the entry's unique identifier, equal to {@code index}, or {@literal -1L} on error
     */
    public long set(long index, Double val);

    /**
     * Associates an element to a particular index (the index can refer to an
     * already existing entry, in which case that entry is overwritten).
     *
     * @param index a unique identifier for this entry
     * @param val   the object to store in the list
     *
     * @return the entry's unique identifier, equal to {@code index}, or {@literal -1L} on error
     */
    public long set(long index, double val);

    /**
     * Associates an element to a particular index, if no value was sstored at this index
     *
     * @param index a unique identifier for this entry
     * @param val   the object to store in the list
     *
     * @return the entry's unique identifier, equal to {@code index}, or {@literal -1L} if a value existed at that index
     */
    public long setIfAbsent(long index, double val);

    /**
     * Associates an element to a particular index, if a particular value existed at this index.
     *
     * @param index  a unique identifier for this entry
     * @param oldval the object at that index in the list
     * @param newval the object to store in the list
     *
     * @return the entry's unique identifier, equal to {@code index}, or {@literal -1L} if the element was not replaced
     */
    public long rep(long index, double oldval, double newval);

    /**
     * Deletes an element, and invalidates the related index.
     *
     * @param index a unique identifier for this entry
     */
    public void del(long index);

    /**
     * Deletes an element, and invalidates the related index.
     *
     * @param index a unique identifier for this entry
     * @param val   the object at this index in the list
     */
    public boolean del(long index, double val);


    /**********************************************************************************
     **  List entries iterators
     **/

    /**
     * Iterates over all indexes in the list, using an {@link net.varkhan.base.containers.Index}.
     *
     * @return an iterator over all the indexes that designate elements in the list
     */
    public Index indexes();

    /**
     * Iterates over all indexes in the list.
     *
     * @return an iterable over all the indexes that designate elements in the list
     */
    public Iterable<Long> iterateIndexes();

    /**
     * Iterates over all elements in the list.
     *
     * @return an iterator over all the elements stored in the list
     */
    public DoubleIterator iterator();

    /**
     * Iterate over each element of the list, and pass it as argument to a
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
     * Iterate over each element of the container, and pass it as argument to a
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
     * Iterate over each element of the list, and pass it as argument to a
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
     * Iterate over each element of the container, and pass it as argument to a
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
    public DoubleIterable iterate(Iterable<Long> indexes);

    /**
     * Iterates over a set of elements designated by an iterator over indexes.
     *
     * @param indexes an iterable over indexes
     *
     * @return an iterable over all the elements associated to the identifiers
     */
    public DoubleIterable iterate(Indexable indexes);

}
