/**
 *
 */
package net.varkhan.base.containers.list;

import net.varkhan.base.containers.Index;
import net.varkhan.base.containers.Indexable;
import net.varkhan.base.containers.type.DoubleIterable;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;


/**
 * <b>Constant-valued IndexedDoubleList.</b>
 * <p/>
 * An immutable IndexedList that returns a constant, predefined value for every
 * index.
 * <p/>
 *
 * @author varkhan
 * @date Mar 12, 2009
 * @time 6:48:14 PM
 */
public class ConstIndexedDoubleList implements IndexedDoubleList, Externalizable, Cloneable {

    /**
     * An immutable IndexedList that always return a {@literal 0} value for every index
     */
    public static final ConstIndexedDoubleList ConstZero=new ConstIndexedDoubleList();

    private static final long serialVersionUID=1L;

    /**
     * The constant return value
     */
    private double defVal;


    /**********************************************************************************
     **  List constructors
     **/

    /**
     * Builds a new constant IndexedDoubleList.
     *
     * @param def the constant entry value
     */
    public ConstIndexedDoubleList(double def) {
        defVal=def;
    }

    /**
     * Builds a new constant IndexedDoubleList, with a {@literal 0.0d} entry value.
     */
    public ConstIndexedDoubleList() {
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
    public double getDefaultValue() {
        return defVal;
    }

    /**
     * Sets the default value.
     *
     * @param def the default value, returned by {@link #get} every time it is called
     */
    public void setDefaultValue(double def) {
        defVal=def;
    }


    /**********************************************************************************
     **  List entries accessors
     **/

    /**
     * Indicates whether an index has an associated entry
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
    public Double get(long index) { return defVal; }

    /**
     * Extracts the element designated by an index.
     *
     * @param index a unique identifier for this entry
     *
     * @return the constant entry value
     */
    public double getDouble(long index) { return defVal; }

    /**
     * Adds an element at the end of the list (in this implementation,
     * this method does nothing).
     *
     * @param val the object to store in the list
     *
     * @return always {@code 0}
     */
    public long add(Double val) { return 0; }

    /**
     * Adds an element at the end of the list (in this implementation,
     * this method does nothing).
     *
     * @param val the object to store in the list
     *
     * @return always {@code 0}
     */
    public long add(double val) { return 0; }

    /**
     * Associates an element to a particular index (in this implementation,
     * this method does nothing).
     *
     * @param index a unique identifier for this entry
     * @param val   the object to store in the list
     *
     * @return always {@code index}
     */
    public long set(long index, Double val) { return index; }

    /**
     * Associates an element to a particular index (in this implementation,
     * this method does nothing).
     *
     * @param index a unique identifier for this entry
     * @param val   the object to store in the list
     *
     * @return always {@code index}
     */
    public long set(long index, double val) { return index; }

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
    public DoubleIterator iterator() {
        return new DoubleIterator() {
            public boolean hasNext() { return false; }
            public Double next() { return defVal; }
            public double nextValue() { return defVal; }
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
    public <Par> long visit(Visitor<Double,Par> vis, Par par) {
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
    public <Par> long visit(DoubleVisitor<Par> vis, Par par) {
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
    public <Par> long visit(IndexedVisitor<Double,Par> vis, Par par) {
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
    public <Par> long visit(IndexedDoubleVisitor<Par> vis, Par par) {
        return 0;
    }

    /**
     * Iterates over a set of elements designated by an array of indexes.
     *
     * @param indexes an array of identifiers
     *
     * @return a constant-valued iterator over as many elements as there are indexes
     */
    public DoubleIterable iterate(final long[] indexes) {
        return new DoubleIterable() {
            public DoubleIterator iterator() {
                return new DoubleIterator() {
                    private int i=0;

                    public boolean hasNext() {
                        return i<indexes.length;
                    }

                    public Double next() {
                        i++;
                        return defVal;
                    }

                    public double nextValue() {
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
    public DoubleIterable iterate(final java.lang.Iterable<Long> indexes) {
        return new DoubleIterable() {
            public DoubleIterator iterator() {
                return new DoubleIterator() {
                    private final java.util.Iterator<Long> iter=indexes.iterator();

                    public boolean hasNext() {
                        return iter.hasNext();
                    }

                    public Double next() {
                        iter.next();
                        return defVal;
                    }

                    public double nextValue() {
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
    public DoubleIterable iterate(final Indexable indexes) {
        return new DoubleIterable() {
            public DoubleIterator iterator() {
                return new DoubleIterator() {
                    private final Index iter=indexes.indexes();

                    public boolean hasNext() {
                        return iter.hasNext();
                    }

                    public Double next() {
                        iter.next();
                        return defVal;
                    }

                    public double nextValue() {
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
        out.writeDouble(defVal);
    }

    /**
     * Read a ConstIndexedList from a stream.
     *
     * @param in the stream to read the object from
     *
     * @throws IOException if I/O errors occur
     */
    public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
        defVal=in.readDouble();
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
        long bits=Double.doubleToLongBits(defVal);
        return (int) (bits^(bits>>>32));
    }

    /**
     * Test whether two constant lists are equal
     *
     * @return {@code true} iff {@code obj} is a ConstIndexedLongList and its default value is the same as this list
     */
    public boolean equals(Object obj) {
        if(!(obj instanceof ConstIndexedDoubleList)) return false;
        ConstIndexedDoubleList that=(ConstIndexedDoubleList) obj;
        return this.defVal==that.defVal;
    }

    /**
     * Return a clone of this list
     *
     * @return an independent constant list, sharing the same default value
     */
    public ConstIndexedDoubleList clone() {
        ConstIndexedDoubleList clone=null;
        try { clone=(ConstIndexedDoubleList) super.clone(); }
        catch(CloneNotSupportedException e) { /** never happens **/}
        return clone;
    }

    /**
     * Returns a string representation of the list.
     *
     * @return the value of {@code "[ ("+ getDefaultValue() + ") ]"}
     */
    public String toString() {
        return new StringBuilder().append("[ (").append(defVal).append(") ]").toString();
    }

}
