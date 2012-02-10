/**
 *
 */
package net.varkhan.base.containers.set;

import net.varkhan.base.containers.Index;
import net.varkhan.base.containers.Indexable;
import net.varkhan.base.containers.Iterable;
import net.varkhan.base.containers.type.IndexedIntCollection;
import net.varkhan.base.containers.type.IntIterable;


/**
 * <b>A searchable indexed collection, that contains no duplicate elements.</b>.
 * <p/>
 * An indexed set of integers, that associates with each unique integer (element,
 * or key) inserted in the set a unique identifier (index), in an injective
 * mapping: given an element value present in the set, the associated index can
 * be obtained, and conversely, given a valid index, the associated element can
 * be retrieved or removed.
 * <p/>
 *
 * @author varkhan
 * @date Mar 19, 2009
 * @time 1:39:19 AM
 */
public interface IndexedIntSet extends IndexedSet<Integer>, IndexedIntCollection {


    /**********************************************************************************
     **  Global information accessors
     **/

    /**
     * Returns the number of elements in this set.
     *
     * @return the number of entries (elements and related indexes) stored in this set
     */
    public long size();

    /**
     * Indicates whether this set is empty.
     *
     * @return {@literal true} if this set contains no element,
     *         {@literal false} otherwise
     */
    public boolean isEmpty();

    /**
     * Returns the smallest position higher that any valid index in this set.
     *
     * @return the highest valid index plus one
     */
    public long head();

    /**
     * Deletes all elements from this set.
     */
    public void clear();

    /**
     * Gets the default key
     *
     * @return the value retrieved for indexes that have no associated key
     */
    public int getDefaultValue();

    /**
     * Sets the default key
     *
     * @param def the new default key
     */
    public void setDefaultValue(int def);


    /**********************************************************************************
     **  Set elements accessors
     **/

    /**
     * Search for a given key in this set, and returns the associated index
     *
     * @param key the object to find in the set
     *
     * @return the index for this entry, or {@literal -1L} if the specified object is not in the set
     */
    public long index(int key);

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
     * @return the requested object, or the default value if no element is available at this index
     */
    public int getInt(long index);

    /**
     * Allocates an entry with a unique index and associates it with an element,
     * or returns the elements identifier if it is already in the set.
     *
     * @param key the object to store in the set
     *
     * @return the entry's unique identifier, that can be used in
     *         {@link #get(long)} to obtain the original object
     */
    public long add(int key);

    /**
     * Deletes an element, and invalidates the related index.
     *
     * @param index a unique identifier for this element
     */
    public void del(long index);


    /**********************************************************************************
     **  Set elements iterators
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
    public IntIterator iterator();

    /**
     * Iterate over each element of the collection, and pass it as argument to a
     * visitor's {@link Visitor#invoke} method, until this method returns
     * a negative count.
     *
     * @param vis the visitor
     * @param par the control parameter
     * @param <Par> the type of the control parameter
     *
     * @return the sum of all positive return values from the visitor
     */
    public <Par> long visit(Visitor<Integer,Par> vis, Par par);

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
    public <Par> long visit(IndexedVisitor<Integer,Par> vis, Par par);

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
    public <Par> long visit(IntVisitor<Par> vis, Par par);

    /**
     * Iterate over each element of the container, and pass it as argument to a
     * visitor's {@link IndexedVisitor#invoke} method, until this method returns
     * a negative count.
     *
     * @param vis the visitor
     * @param par the control parameter
     * @param <Par> the type of the control parameter
     *
     * @return the sum of all positive return values from the visitor
     */
    public <Par> long visit(IndexedIntVisitor<Par> vis, Par par);

    /**
     * Iterates over a set of elements designated by an array of indexes.
     *
     * @param indexes an array of indexes
     *
     * @return an iterable over all the elements associated to the identifiers
     */
    public IntIterable iterate(long[] indexes);

    /**
     * Iterates over a set of elements designated by an iterator over indexes.
     *
     * @param indexes an iterable over indexes
     *
     * @return an iterable over all the elements associated to the identifiers
     */
    public IntIterable iterate(java.lang.Iterable<Long> indexes);

    /**
     * Iterates over a set of elements designated by an iterator over indexes.
     *
     * @param indexes an iterable over indexes
     *
     * @return an iterable over all the elements associated to the identifiers
     */
    public IntIterable iterate(Indexable indexes);


    /**********************************************************************************
     **  Set entry and iterators
     **/

    /**
     * <b>A generic Entry for an IndexedSet.</b>
     * <p/>
     * This interface materializes the association between an element contained
     * in the set, and its unique identifier.
     */
    public static interface IntEntry extends IndexedSet.Entry<Integer> {

        /**
         * Returns the element contained in the set
         *
         * @return the entry's element
         */
        public int getIntKey();

        /**
         * Returns the unique identifier associated to the element in the set
         *
         * @return the entry's index
         */
        public long index();
    }

    /**
     * Iterates over all entries in the set.
     *
     * @return an iterable over all the entries in the set
     */
    public Iterable<? extends IntEntry> entries();

    /**
     * Iterates over a set of entries designated by an array of indexes.
     *
     * @param indexes an array of indexes
     *
     * @return an iterable over all the entries designated by the identifiers
     */
    public Iterable<? extends IntEntry> entries(long[] indexes);

    /**
     * Iterates over a set of entries designated by an iterator over indexes.
     *
     * @param indexes an iterable over indexes
     *
     * @return an iterable over all the entries designated by the identifiers
     */
    public Iterable<? extends IntEntry> entries(Iterable<Long> indexes);

    /**
     * Iterates over a set of entries designated by an iterator over indexes.
     *
     * @param indexes an iterable over indexes
     *
     * @return an iterable over all the entries designated by the identifiers
     */
    public Iterable<? extends IntEntry> entries(Indexable indexes);

}
