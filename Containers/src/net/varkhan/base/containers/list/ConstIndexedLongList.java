/**
 *
 */
package net.varkhan.base.containers.list;

import net.varkhan.base.containers.Index;
import net.varkhan.base.containers.Indexable;
import net.varkhan.base.containers.type.LongIterable;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;


/**
 * <b>Constant-valued IndexedLongList.</b>
 * <p/>
 * An immutable IndexedList that returns a constant, predefined value for every
 * index.
 * <p/>
 *
 * @author varkhan
 * @date Mar 12, 2009
 * @time 6:48:14 PM
 */
public class ConstIndexedLongList implements IndexedLongList, Externalizable, Cloneable {

    /**
     * An immutable IndexedList that always return a {@literal 0} value for every index
     */
    public static final ConstIndexedLongList ConstZero=new ConstIndexedLongList();

    private static final long serialVersionUID=1L;

    /**
     * The constant return value
     */
    private long defVal;


    /**********************************************************************************
     **  List constructors
     **/

    /**
     * Builds a new constant IndexedLongList.
     *
     * @param def the constant entry value
     */
    public ConstIndexedLongList(long def) {
        defVal=def;
    }

    /**
     * Builds a new constant IndexedLongList, with a {@literal 0L} entry value.
     */
    public ConstIndexedLongList() {
        defVal=0;
    }


    /**********************************************************************************
     **  List statistics accessors
     **/

    /**
     * Returns the number of elements in this list.
     *
     * @return always 0
     */
    public long size() { return 0; }

    /**
     * Indicates whether this list is empty.
     *
     * @return {@literal true}
     */
    public boolean isEmpty() { return true; }

    /**
     * Returns the smallest position higher that any valid index in this list.
     *
     * @return always 0
     */
    public long head() { return 0; }

    /**
     * Returns an index that has no associated element in this list.
     *
     * @return always 0
     */
    public long free() { return 0; }

    /**
     * Deletes all elements from this list.
     */
    public void clear() { }

    /**
     * Gets the default value.
     *
     * @return the default value, returned by {@link #get} every time it is called
     */
    public long getDefaultValue() {
        return defVal;
    }

    /**
     * Sets the default value.
     *
     * @param def the default value, returned by {@link #get} every time it is called
     */
    public void setDefaultValue(long def) {
        defVal=def;
    }


    /**********************************************************************************
     **  List entries accessors
     **/

    /**
     * Indicates whether an index has an associated entry.
     *
     * @param index a unique identifier for this entry
     *
     * @return always {@literal true}
     */
    public boolean has(long index) { return true; }

    /**
     * Extracts the element designated by an index.
     *
     * @param index a unique identifier for this entry
     *
     * @return the constant entry value
     */
    public Long get(long index) { return defVal; }

    /**
     * Extracts the element designated by an index.
     *
     * @param index a unique identifier for this entry
     *
     * @return the constant entry value
     */
    public long getLong(long index) { return defVal; }

    /**
     * Adds an element at the end of the list (in this implementation,
     * this method does nothing).
     *
     * @param val the object to store in the list
     *
     * @return always {@code 0}
     */
    public long add(Long val) { return 0; }

    /**
     * Adds an element at the end of the list (in this implementation,
     * this method does nothing).
     *
     * @param val the object to store in the list
     *
     * @return always {@code 0}
     */
    public long add(long val) { return 0; }

    /**
     * Associates an element to a particular index (in this implementation,
     * this method does nothing).
     *
     * @param index a unique identifier for this entry
     * @param val   the object to store in the list
     *
     * @return always {@code index}
     */
    public long set(long index, Long val) { return index; }

    /**
     * Associates an element to a particular index (in this implementation,
     * this method does nothing).
     *
     * @param index a unique identifier for this entry
     * @param val   the object to store in the list
     *
     * @return always {@code index}
     */
    public long set(long index, long val) { return index; }

    /**
     * Deletes an element, and invalidates the related index.
     *
     * @param index a unique identifier for this entry
     */
    public void del(long index) { }


    /**********************************************************************************
     **  List entries iterators
     **/

    /**
     * Iterates over all indexes in the list, using an {@link Index}.
     *
     * @return an iterator over all the indexes that designate elements in the list
     */
    public Index indexes() {
        return new Index() {
            public long current() { return 0; }
            public boolean hasNext() { return false; }
            public long next() { return 0; }
            public boolean hasPrevious() { return false; }
            public long previous() { return 0; }
        };
    }

    /**
     * Iterates over all indexes in the collection.
     *
     * @return an empty iterable
     */
    public java.lang.Iterable<Long> iterateIndexes() {
        return new java.lang.Iterable<Long>() {
            public java.util.Iterator<Long> iterator() {
                return new java.util.Iterator<Long>() {
                    public boolean hasNext() { return false; }
                    public Long next() { return 0L; }
                    public void remove() { }
                };
            }
        };
    }

    /**
     * Iterates over all elements in the list.
     *
     * @return an empty iterator
     */
    public LongIterator iterator() {
        return new LongIterator() {
            public boolean hasNext() { return false; }
            public Long next() { return defVal; }
            public long nextValue() { return defVal; }
            public void remove() { }
        };
    }

    /**
     * Iterate over each element of the list.
     *
     * @param vis the visitor
     * @param par the control parameter
     * @param <Par> the type of the control parameter
     *
     * @return {@code 0}
     */
    public <Par> long visit(Visitor<Long,Par> vis, Par par) {
        return 0;
    }

    /**
     * Iterate over each element of the list.
     *
     * @param vis the visitor
     * @param par the control parameter
     * @param <Par> the type of the control parameter
     *
     * @return {@code 0}
     */
    public <Par> long visit(LongVisitor<Par> vis, Par par) {
        return 0;
    }

    /**
     * Iterate over each element of the list.
     *
     * @param vis the visitor
     * @param par the control parameter
     * @param <Par> the type of the control parameter
     *
     * @return {@code 0}
     */
    public <Par> long visit(IndexedVisitor<Long,Par> vis, Par par) {
        return 0;
    }

    /**
     * Iterate over each element of the list.
     *
     * @param vis the visitor
     * @param par the control parameter
     * @param <Par> the type of the control parameter
     *
     * @return {@code 0}
     */
    public <Par> long visit(IndexedLongVisitor<Par> vis, Par par) {
        return 0;
    }

    /**
     * Iterates over a set of elements designated by an array of indexes.
     *
     * @param indexes an array of identifiers
     *
     * @return a constant-valued iterator over as many elements as there are indexes
     */
    public LongIterable iterate(final long[] indexes) {
        return new LongIterable() {
            public LongIterator iterator() {
                return new LongIterator() {
                    private int i=0;

                    public boolean hasNext() {
                        return i<indexes.length;
                    }

                    public Long next() {
                        i++;
                        return defVal;
                    }

                    public long nextValue() {
                        i++;
                        return defVal;
                    }

                    public void remove() { }
                };
            }
        };
    }

    /**
     * Iterates over a set of elements designated by an iterator over indexes.
     *
     * @param indexes an iterable over identifiers
     *
     * @return a constant-valued iterator over as many elements as there are indexes
     */
    public LongIterable iterate(final java.lang.Iterable<Long> indexes) {
        return new LongIterable() {
            public LongIterator iterator() {
                return new LongIterator() {
                    private final java.util.Iterator<Long> iter=indexes.iterator();

                    public boolean hasNext() {
                        return iter.hasNext();
                    }

                    public Long next() {
                        iter.next();
                        return defVal;
                    }

                    public long nextValue() {
                        iter.next();
                        return defVal;
                    }

                    public void remove() { }
                };
            }
        };
    }

    /**
     * Iterates over a set of elements designated by an iterator over indexes.
     *
     * @param indexes an iterable over identifiers
     *
     * @return a constant-valued iterator over as many elements as there are indexes
     */
    public LongIterable iterate(final Indexable indexes) {
        return new LongIterable() {
            public LongIterator iterator() {
                return new LongIterator() {
                    private final Index iter=indexes.indexes();

                    public boolean hasNext() {
                        return iter.hasNext();
                    }

                    public Long next() {
                        iter.next();
                        return defVal;
                    }

                    public long nextValue() {
                        iter.next();
                        return defVal;
                    }

                    public void remove() { }
                };
            }
        };
    }


    /**********************************************************************************
     **  Externalization
     **/

    /**
     * Write a ConstIndexedList to a stream.
     *
     * @param out the stream to write the object to
     *
     * @throws IOException if I/O errors occur
     * @serialData {@code Object defVal} - the default value
     */
    public void writeExternal(ObjectOutput out) throws IOException {
        out.writeLong(defVal);
    }

    /**
     * Read a ConstIndexedList from a stream.
     *
     * @param in the stream to read the object from
     *
     * @throws IOException if I/O errors occur
     */
    public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
        defVal=in.readLong();
    }


    /**********************************************************************************
     **  Object method overrides
     **/


    /**
     * Return a hash code value for this list.
     *
     * @return the hash code of the default value
     */
    public int hashCode() {
        return (int) (defVal^(defVal>>>32));
    }

    /**
     * Test whether two constant lists are equal
     *
     * @return {@code true} iff {@code obj} is a ConstIndexedLongList and its default value is the same as this list
     */
    public boolean equals(Object obj) {
        if(!(obj instanceof ConstIndexedLongList)) return false;
        ConstIndexedLongList that=(ConstIndexedLongList) obj;
        return this.defVal==that.defVal;
    }

    /**
     * Return a clone of this list
     *
     * @return an independent constant list, sharing the same default value
     */
    public ConstIndexedLongList clone() {
        ConstIndexedLongList clone=null;
        try { clone=(ConstIndexedLongList) super.clone(); }
        catch(CloneNotSupportedException e) { /** never happens **/}
        return clone;
    }

    /**
     * Returns a string representation of the list.
     *
     * @return the value of {@code "{("+ getDefaultValue() + ")}"}
     */
    public String toString() {
        return new StringBuilder().append("{(").append(defVal).append(")}").toString();
    }

}
