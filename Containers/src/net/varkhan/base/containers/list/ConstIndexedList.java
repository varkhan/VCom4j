/**
 *
 */
package net.varkhan.base.containers.list;

import net.varkhan.base.containers.Index;
import net.varkhan.base.containers.Indexable;
import net.varkhan.base.containers.Iterable;
import net.varkhan.base.containers.Iterator;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;


/**
 * <b>Constant-valued IndexedList.</b>
 * <p/>
 * An immutable IndexedList that returns a constant, predefined value for every
 * index.
 * <p/>
 *
 * @author varkhan
 * @date Mar 12, 2009
 * @time 6:48:14 PM
 */
public class ConstIndexedList<Type> implements IndexedList<Type>, Externalizable, Cloneable {

    /**
     * An immutable IndexedList that always return a {@literal null} value for every index
     */
    public static final ConstIndexedList<?> ConstNull=new ConstIndexedList<Object>();

    private static final long serialVersionUID=1L;

    /**
     * The constant return value
     */
    private Type defVal;


    /**********************************************************************************
     **  List constructors
     **/

    /**
     * Builds a new constant IndexedList.
     *
     * @param def the constant entry value
     */
    public ConstIndexedList(Type def) {
        defVal=def;
    }

    /**
     * Builds a new constant IndexedList, with a {@literal null} entry value.
     */
    public ConstIndexedList() {
        defVal=null;
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
     * Deletes all elements from this list
     */
    public void clear() { }

    /**
     * Gets the default value
     *
     * @return the default value, returned by {@link #get} every time it is called
     */
    public Type getDefaultValue() {
        return defVal;
    }

    /**
     * Sets the default value
     *
     * @param def the default value, returned by {@link #get} every time it is called
     */
    public void setDefaultValue(Type def) {
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
     * Extracts the element designated by an index
     *
     * @param index a unique identifier for this entry
     *
     * @return the constant entry value
     */
    public Type get(long index) { return defVal; }

    /**
     * Adds an element at the end of the list (in this implementation,
     * this method does nothing).
     *
     * @param val the object to store in the list
     *
     * @return always {@code 0}
     */
    public long add(Type val) { return 0; }

    /**
     * Associates an element to a particular index (in this implementation,
     * this method does nothing).
     *
     * @param index a unique identifier for this entry
     * @param val   the object to store in the list
     *
     * @return always {@code index}
     */
    public long set(long index, Type val) { return index; }

    /**
     * Deletes an element, and invalidates the related index
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
    public Iterator<? extends Type> iterator() {
        return new Iterator<Type>() {
            public boolean hasNext() { return false; }

            public Type next() { return defVal; }

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
    public <Par> long visit(Visitor<Type,Par> vis, Par par) {
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
    public <Par> long visit(IndexedVisitor<Type,Par> vis, Par par) {
        return 0;
    }

    /**
     * Iterates over a set of elements designated by an array of indexes.
     *
     * @param indexes an array of identifiers
     *
     * @return a constant-valued iterator over as many elements as there are indexes
     */
    public Iterable<? extends Type> iterate(final long[] indexes) {
        return new Iterable<Type>() {
            public Iterator<Type> iterator() {
                return new Iterator<Type>() {
                    private int i=0;

                    public boolean hasNext() {
                        return i<indexes.length;
                    }

                    public Type next() {
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
    public Iterable<? extends Type> iterate(final java.lang.Iterable<Long> indexes) {
        return new Iterable<Type>() {
            public Iterator<Type> iterator() {
                return new Iterator<Type>() {
                    private final java.util.Iterator<Long> iter=indexes.iterator();

                    public boolean hasNext() {
                        return iter.hasNext();
                    }

                    public Type next() {
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
    public Iterable<? extends Type> iterate(final Indexable indexes) {
        return new Iterable<Type>() {
            public Iterator<Type> iterator() {
                return new Iterator<Type>() {
                    private final Index iter=indexes.indexes();

                    public boolean hasNext() {
                        return iter.hasNext();
                    }

                    public Type next() {
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
        out.writeObject(defVal);
    }

    /**
     * Read a ConstIndexedList from a stream.
     *
     * @param in the stream to read the object from
     *
     * @throws IOException if I/O errors occur
     */
    @SuppressWarnings( { "unchecked" })
    public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
        defVal=(Type) in.readObject();
    }


    /**********************************************************************************
     **  Object method overrides
     **/


    /**
     * Return a hash code value for this list.
     *
     * @return the hash code of the default value, or {@code 0} if the default value is {@code null}
     */
    public int hashCode() {
        return this.defVal==null ? 0 : this.defVal.hashCode();
    }

    /**
     * Test whether two constant lists are equal.
     *
     * @return {@code true} iff {@code obj} is a ConstIndexedList and its default value is the same as this list
     */
    public boolean equals(Object obj) {
        if(!(obj instanceof ConstIndexedList)) return false;
        ConstIndexedList<?> that=(ConstIndexedList<?>) obj;
        if(this.defVal==that.defVal) return true;
        // This OR is actually an exclusion, since we just compared the ref values
        if(this.defVal==null||that.defVal==null) return false;
        return this.defVal.equals(that.defVal);
    }

    /**
     * Return a clone of this list.
     *
     * @return an independent constant list, sharing the same default value
     */
    @SuppressWarnings("unchecked")
    public ConstIndexedList<Type> clone() {
        ConstIndexedList<Type> clone=null;
        try { clone=(ConstIndexedList<Type>) super.clone(); }
        catch(CloneNotSupportedException e) { /** never happens **/}
        return clone;
    }

    /**
     * Return a string representation of the list.
     *
     * @return the value of {@code "{("+ getDefaultValue() + ")}"}
     */
    public String toString() {
        return new StringBuilder().append("{(").append(defVal.toString()).append(")}").toString();
    }

}
