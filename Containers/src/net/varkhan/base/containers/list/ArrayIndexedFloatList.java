/**
 *
 */
package net.varkhan.base.containers.list;

import net.varkhan.base.containers.Index;
import net.varkhan.base.containers.Indexable;
import net.varkhan.base.containers.Indexed;
import net.varkhan.base.containers.type.FloatIterable;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.util.NoSuchElementException;


/**
 * <b>IndexedFloatList using resizable arrays of blocks</b>.
 * <p/>
 * This list stores objects in arbitrary positions (their index, in the sense of
 * {@link Indexed}).
 * <p/>
 *
 * @author varkhan
 * @date Mar 12, 2009
 * @time 5:44:48 AM
 */
public class ArrayIndexedFloatList implements IndexedFloatList, Externalizable, Cloneable {

    private static final long serialVersionUID=1L;

    /**
     * The list growth factor > 1.0
     */
    protected final double growthfact;

    /**
     * The number of elements contained in the list
     */
    protected long size=0;

    /**
     * The index of the last slot used + 1
     */
    protected long head=0;

    /**
     * The list slot storage
     */
    private float[] list=null;

    /**
     * The default return value
     */
    private float defVal=0;


    /**********************************************************************************
     **  List constructors
     **/

    /**
     * Creates a new ArrayIndexedFloatList, specifying the reallocation strategy.
     *
     * @param growthfact the node reference storage growth factor
     */
    public ArrayIndexedFloatList(double growthfact) {
        if(growthfact<=1) growthfact=1.5;
        this.growthfact=growthfact;
        clear();
    }

    /**
     * Creates a new ArrayIndexedFloatList.
     */
    public ArrayIndexedFloatList() {
        this(1.5);
    }

    /**
     * Copies an IndexedList, specifying the reallocation strategy.
     *
     * @param growthfact the node reference storage growth factor
     * @param list       the IndexedList to copy
     */
    public ArrayIndexedFloatList(double growthfact, IndexedFloatList list) {
        this(growthfact);
        Index it=list.indexes();
        while(it.hasNext()) {
            long id=it.next();
            float obj=list.getFloat(id);
            if(obj!=defVal) set(id, obj);
        }
    }

    /**
     * Copies an IndexedList.
     *
     * @param list the IndexedList to copy
     */
    public ArrayIndexedFloatList(IndexedFloatList list) {
        this();
        Index it=list.indexes();
        while(it.hasNext()) {
            long id=it.next();
            float obj=list.getFloat(id);
            if(obj!=defVal) set(id, obj);
        }
    }

    /**
     * Builds an ArrayIndexedFloatList from an array of numbers.
     *
     * @param array the array to copy
     */
    public <N extends Number> ArrayIndexedFloatList(N... array) {
        this();
        for(int id=0;id<array.length;id++) {
            Number obj=array[id];
            if(obj==null) continue;
            float val=obj.floatValue();
            if(val!=defVal) set(id, val);
        }
    }

    /**
     * Builds an ArrayIndexedFloatList from an array.
     *
     * @param array the array to copy
     */
    public ArrayIndexedFloatList(float... array) {
        this();
        for(int id=0;id<array.length;id++) {
            float obj=array[id];
            if(obj!=defVal) set(id, obj);
        }
    }


    /**********************************************************************************
     **  List statistics accessors
     **/

    /**
     * Returns the number of elements in this list.
     *
     * @return the number of entries (objects and related index) stored in this list
     */
    public final long size() {
        return size;
    }

    /**
     * Indicates whether this list is empty.
     *
     * @return {@literal true} if this list contains no entry,
     *         {@literal false} otherwise
     */
    public final boolean isEmpty() {
        return size<=0;
    }

    /**
     * Returns the smallest position higher that any valid index in this list.
     *
     * @return the highest valid index plus one
     */
    public final long head() {
        return head;
    }

    /**
     * Returns an index that has no associated element in this list.
     *
     * @return the smallest unused index in this list
     */
    public long free() {
        if(list==null) return 0;
        if(head==size) return head;
        int pos=0;
        while(pos<head) {
            if(list[pos]==defVal) return pos;
            pos++;
        }
        return head;
    }

    /**
     * Deletes all elements from this list.
     */
    public void clear() {
        size=0;
        head=0;
        list=null;
    }

    /**
     * Gets the default value.
     *
     * @return the default value, returned by {@link #get} on indexes without an associated entry
     */
    public float getDefaultValue() {
        return defVal;
    }

    /**
     * Sets the default value.
     *
     * @param def the default value, returned by {@link #get} on indexes without an associated entry
     */
    public void setDefaultValue(float def) {
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
     * @return {@literal true} if an element is associated with this index,
     *         or {@literal false} if no element is associated with this index
     */
    public boolean has(long index) {
        if(index<0||index>=head) return false;
        return list[(int) index]!=defVal;
    }

    /**
     * Extracts the element designated by an index.
     *
     * @param index a unique identifier for this entry
     *
     * @return the requested element, or {@literal null} if no entry is associated to this index
     */
    public Float get(long index) {
        if(index<0||index>=head) return defVal;
        final float val=list[(int) index];
        if(val==defVal) return defVal;
        return val;
    }

    /**
     * Extracts the element designated by an index.
     *
     * @param index a unique identifier for this entry
     *
     * @return the requested element, or {@literal null} if no entry is associated to this index
     */
    public float getFloat(long index) {
        if(index<0||index>=head) return defVal;
        return list[(int) index];
    }

    /**
     * Adds an element at the end of the list.
     * <p/>
     * Note: this is equivalent to {@link #set}{@code (head(), val)}, and
     * the returned index is the value of {@link #head()} before the call.
     *
     * @param val the object to store in the list
     *
     * @return the entry's unique identifier, that will subsequently give access to the element
     */
    public long add(Float val) {
        return set(head, val);
    }

    /**
     * Adds an element at the end of the list.
     * <p/>
     * Note: this is equivalent to {@link #set}{@code (head(), val)}, and
     * the returned index is the value of {@link #head()} before the call.
     *
     * @param val the object to store in the list
     *
     * @return the entry's unique identifier, that will subsequently give access to the element
     */
    public long add(float val) {
        return set(head, val);
    }

    /**
     * Associates an element to a particular index (the index can refer to an
     * already existing entry, in which case that entry is overwritten).
     *
     * @param index a unique identifier for this entry
     * @param val   the object to store in the list
     *
     * @return the entry's unique identifier, equal to {@code index}, or {@literal -1L} on error
     */
    public long set(long index, Float val) {
        if(val==null) {
            // No actual storage required
            del(index);
            return index;
        }
        return set(index, val.floatValue());
    }

    /**
     * Associates an element to a particular index (the index can refer to an
     * already existing entry, in which case that entry is overwritten).
     *
     * @param index a unique identifier for this entry
     * @param val   the object to store in the list
     *
     * @return the entry's unique identifier, equal to {@code index}, or {@literal -1L} on error
     */
    public long set(long index, float val) {
        if(val==defVal) {
            // No actual storage required
            del(index);
            return index;
        }
        if(list==null) {
            list=new float[(int) (index+1)];
        }
        else if(index>=list.length) {
            // We need to realloc
            int newLen=(int) (list.length*growthfact);
            if(newLen<=index) newLen=(int) (index+1);
            float[] newlist=new float[newLen];
            System.arraycopy(list, 0, newlist, 0, list.length);
            list=newlist;
        }
        if(list[(int) index]==defVal) {
            size++;
        }
        list[(int) index]=val;
        if(index>=head) head=index+1;
        return index;
    }

    /**
     * Deletes an element, and invalidates the related index.
     *
     * @param index a unique identifier for this entry
     */
    public void del(long index) {
        if(index<0||index>=head) return;
        if(list[(int) index]==defVal) return;
        list[(int) index]=defVal;
        size--;
        int headpos=(int) (head-1);
        while(headpos>0&&list[headpos]==defVal) headpos--;
        head=headpos+1;
        if(headpos*growthfact*growthfact<list.length) {
            // We need to realloc
            int newLen=(int) ((list.length+1)/growthfact);
            float[] newlist=new float[newLen];
            System.arraycopy(list, 0, newlist, 0, headpos+1);
            list=newlist;
        }
    }


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
            private int index=-1;

            public long current() {
                return index;
            }

            public boolean hasNext() {
                int i=index+1;
                while(i<head) {
                    if(list[i]!=defVal) return true;
                    i++;
                }
                return false;
            }

            public long next() {
                index++;
                while(index<head) {
                    if(list[index]!=defVal) return index;
                    index++;
                }
                throw new NoSuchElementException("No element at index "+index);
            }

            public boolean hasPrevious() {
                int i=index-1;
                while(i>0) {
                    if(list[i]!=defVal) return true;
                    i--;
                }
                return false;
            }

            public long previous() {
                index--;
                while(index>0) {
                    if(list[index]!=defVal) return index;
                    index--;
                }
                throw new NoSuchElementException("No element at index "+index);
            }
        };
    }

    /**
     * Iterates over all indexes in the list.
     *
     * @return an iterable over all the indexes associated to elements in the list
     */
    public java.lang.Iterable<Long> iterateIndexes() {
        return new java.lang.Iterable<Long>() {
            public java.util.Iterator<Long> iterator() {
                return new java.util.Iterator<Long>() {
                    private int index=-1;

                    public boolean hasNext() {
                        int i=index+1;
                        while(i<head) {
                            if(list[i]!=defVal) return true;
                            i++;
                        }
                        return false;
                    }

                    public Long next() {
                        index++;
                        while(index<head) {
                            if(list[index]!=defVal) return (long) index;
                            index++;
                        }
                        throw new NoSuchElementException("No element at index "+index);
                    }

                    public void remove() {
                        throw new UnsupportedOperationException();
                    }
                };
            }
        };
    }

    /**
     * Iterates over all elements in the list.
     *
     * @return an iterator over all the elements stored in the list
     */
    public FloatIterator iterator() {
        return new FloatIterator() {
            private int index=-1;

            public boolean hasNext() {
                int i=index+1;
                while(i<head) {
                    if(list[i]!=defVal) return true;
                    i++;
                }
                return false;
            }

            public Float next() { return nextValue(); }

            public float nextValue() {
                index++;
                while(index<head) {
                    float obj=list[index];
                    if(obj!=defVal) return obj;
                    index++;
                }
                throw new NoSuchElementException("No element at index "+index);
            }

            public void remove() {
                throw new UnsupportedOperationException();
            }
        };
    }

    /**
     * Iterate over each element of the list, and pass it as argument to a
     * visitor's {@link Visitor#invoke} method, until this method returns
     * a negative count.
     *
     * @param vis the visitor
     * @param par the control parameter
     * @param <Par> the type of the control parameter
     *
     * @return the sum of all positive return values from the visitor
     */
    public <Par> long visit(Visitor<Float,Par> vis, Par par) {
        long c=0;
        int i=0;
        while(i<head) {
            float obj=list[i];
            if(obj!=defVal) {
                long r=vis.invoke(obj, par);
                if(r<0) return c;
                c+=r;
            }
            i++;
        }
        return c;
    }

    /**
     * Iterate over each element of the list, and pass it as argument to a
     * visitor's {@link IndexedVisitor#invoke} method, until this method returns
     * a negative count.
     *
     * @param vis the visitor
     * @param par the control parameter
     * @param <Par> the type of the control parameter
     *
     * @return the sum of all positive return values from the visitor
     */
    public <Par> long visit(IndexedVisitor<Float,Par> vis, Par par) {
        long c=0;
        int i=0;
        while(i<head) {
            float obj=list[i];
            if(obj!=defVal) {
                long r=vis.invoke(i, obj, par);
                if(r<0) return c;
                c+=r;
            }
            i++;
        }
        return c;
    }

    /**
     * Iterate over each element of the list, and pass it as argument to a
     * visitor's {@link Visitor#invoke} method, until this method returns
     * a negative count.
     *
     * @param vis the visitor
     * @param par the control parameter
     * @param <Par> the type of the control parameter
     *
     * @return the sum of all positive return values from the visitor
     */
    public <Par> long visit(FloatVisitor<Par> vis, Par par) {
        long c=0;
        int i=0;
        while(i<head) {
            float obj=list[i];
            if(obj!=defVal) {
                long r=vis.invoke(obj, par);
                if(r<0) return c;
                c+=r;
            }
            i++;
        }
        return c;
    }

    /**
     * Iterate over each element of the list, and pass it as argument to a
     * visitor's {@link IndexedVisitor#invoke} method, until this method returns
     * a negative count.
     *
     * @param vis the visitor
     * @param par the control parameter
     * @param <Par> the type of the control parameter
     *
     * @return the sum of all positive return values from the visitor
     */
    public <Par> long visit(IndexedFloatVisitor<Par> vis, Par par) {
        long c=0;
        int i=0;
        while(i<head) {
            float obj=list[i];
            if(obj!=defVal) {
                long r=vis.invoke(i, obj, par);
                if(r<0) return c;
                c+=r;
            }
            i++;
        }
        return c;
    }

    /**
     * Iterates over a set of elements designated by an array of indexes.
     *
     * @param indexes an array of indexes
     *
     * @return an iterable over all the elements associated to the identifiers
     */
    public FloatIterable iterate(final long[] indexes) {
        return new FloatIterable() {
            public FloatIterator iterator() {
                return new FloatIterator() {
                    private int i=0;

                    public boolean hasNext() {
                        return i<indexes.length;
                    }

                    public Float next() {
                        long index=indexes[i++];
                        final float val=list[(int) (index)];
                        return val;
                    }

                    public float nextValue() {
                        long index=indexes[i++];
                        final float val=list[(int) (index)];
                        return val;
                    }

                    public void remove() {
                        throw new UnsupportedOperationException();
                    }
                };
            }
        };
    }


    /**
     * Iterates over a set of elements designated by an iterator over indexes.
     *
     * @param indexes an iterable over indexes
     *
     * @return an iterable over all the elements associated to the identifiers
     */
    public FloatIterable iterate(final java.lang.Iterable<Long> indexes) {
        return new FloatIterable() {
            public FloatIterator iterator() {
                return new FloatIterator() {
                    private final java.util.Iterator<Long> iter=indexes.iterator();

                    public boolean hasNext() {
                        return iter.hasNext();
                    }

                    public Float next() {
                        long index=iter.next();
                        final float val=list[(int) (index)];
                        return val;
                    }

                    public float nextValue() {
                        long index=iter.next();
                        final float val=list[(int) (index)];
                        return val;
                    }

                    public void remove() {
                        throw new UnsupportedOperationException();
                    }
                };
            }
        };
    }

    /**
     * Iterates over a set of elements designated by an iterator over indexes.
     *
     * @param indexes an iterable over indexes
     *
     * @return an iterable over all the elements associated to the identifiers
     */
    public FloatIterable iterate(final Indexable indexes) {
        return new FloatIterable() {
            public FloatIterator iterator() {
                return new FloatIterator() {
                    private final Index iter=indexes.indexes();

                    public boolean hasNext() {
                        return iter.hasNext();
                    }

                    public Float next() {
                        long index=iter.next();
                        final float val=list[(int) (index)];
                        return val;
                    }

                    public float nextValue() {
                        long index=iter.next();
                        final float val=list[(int) (index)];
                        return val;
                    }

                    public void remove() {
                        throw new UnsupportedOperationException();
                    }
                };
            }
        };
    }


    /**********************************************************************************
     **  Externalization
     **/

    /**
     * Write an ArrayIndexedList to a stream.
     *
     * @param out the stream to write the object to
     *
     * @throws IOException if I/O errors occur
     * @serialData <li/> {@code Object defVal}     - the default value
     * <li/> {@code byte blockshift}   - the block size 2-logarithm
     * <li/> {@code double growthfact} - the buffer growth factor
     * <li/> {@code long size}         - the number of set entries
     * <li/> {@code long head}         - the highest allocated index + 1
     * <li/> all the data block, as an occupancy count on one {@code int},
     * followed by the values ({@code blocksize} {@code Object}s)
     */
    public void writeExternal(ObjectOutput out) throws IOException {
        out.writeLong(size);
        out.writeLong(head);
        out.writeFloat(defVal);
        if(head>0) {
            for(int i=0;i<head;i++) {
                out.writeFloat(list[i]);
            }
        }
    }

    /**
     * Read an ArrayIndexedList from a stream.
     *
     * @param in the stream to read the object from
     *
     * @throws IOException if I/O errors occur
     */
    public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
        size=in.readLong();
        head=in.readLong();
        defVal=in.readFloat();
        if(head>0) {
            list=new float[(int) head];
            for(int i=0;i<head;i++) {
                list[i]=in.readFloat();
            }
        }
    }


    /**********************************************************************************
     **  Object method overrides
     **/


    /**
     * Return a hash code value for this list.
     *
     * @return a hash code
     */
    public int hashCode() {
        int hash=0;
        hash^=size^(size>>>32);
        hash^=head^(head>>>32);
        if(head>0) {
            for(int i=0;i<head;i++) {
                hash^=Float.floatToIntBits(list[i]);
            }
        }
        return hash;
    }

    public boolean equals(Object obj) {
        if(!(obj instanceof ArrayIndexedFloatList)) return false;
        ArrayIndexedFloatList that=(ArrayIndexedFloatList) obj;
        if(this.size!=that.size) return false;
        if(this.head!=that.head) return false;
        if(this.defVal!=that.defVal) return false;
        if(head>0) {
            for(int i=0;i<head;i++) {
                if(this.list[i]!=that.list[i]) return false;
            }
        }
        return true;
    }

    public ArrayIndexedFloatList clone() {
        ArrayIndexedFloatList clone;
        try { clone=(ArrayIndexedFloatList) super.clone(); }
        catch(CloneNotSupportedException e) { return null; }
        if(this.list!=null) {
            clone.list=new float[this.list.length];
            System.arraycopy(this.list, 0, clone.list, 0, this.list.length);
        }
        return clone;
    }

    /**
     * Returns a string representation of the IndexedList.
     *
     * @return a string enclosing in curly brackets the string representations
     *         of all the elements in the list, prefixed by their index
     */
    public String toString() {
        StringBuilder buf=new StringBuilder();
        buf.append("{(null)");
        for(int i=0;i<head;i++) {
            float obj=list[i];
            if(obj!=defVal) buf.append(" ").append(i).append(":").append(obj);
            i++;
        }
        buf.append("}");
        return buf.toString();
    }

}
