package net.varkhan.base.containers.list;

import net.varkhan.base.containers.Iterator;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.util.NoSuchElementException;


/**
 * <b></b>.
 * <p/>
 *
 * @author varkhan
 * @date 3/18/12
 * @time 6:27 PM
 */
public class ArrayList<Type> implements List<Type>, Externalizable, Cloneable {

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
     * The list slot storage
     */
    private Object[] list=null;



    /**********************************************************************************
     **  List constructors
     **/

    /**
     * Creates a new ArrayList, specifying the reallocation strategy.
     *
     * @param growthfact the node reference storage growth factor
     */
    public ArrayList(double growthfact) {
        if(growthfact<=1) growthfact=1.5;
        this.growthfact=growthfact;
        clear();
    }

    /**
     * Creates a new ArrayList.
     */
    public ArrayList() {
        this(1.5);
    }

    /**
     * Copies an ArrayList, specifying the reallocation strategy.
     *
     * @param growthfact the node reference storage growth factor
     * @param list       the IndexedList to copy
     */
    public ArrayList(double growthfact, List<Type> list) {
        this(growthfact);
        Iterator<? extends Type> it=list.iterator();
        while(it.hasNext()) {
            Type obj=it.next();
            add(obj);
        }
    }

    /**
     * Copies an ArrayList.
     *
     * @param list the IndexedList to copy
     */
    public ArrayList(List<Type> list) {
        this();
        Iterator<? extends Type> it=list.iterator();
        while(it.hasNext()) {
            Type obj=it.next();
            add(obj);
        }
    }

    /**
     * Builds an ArrayList from an array.
     *
     * @param array the array to copy
     */
    public ArrayList(Type... array) {
        this();
        for(int id=0;id<array.length;id++) {
            Type obj=array[id];
            add(obj);
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
     * Deletes all elements from this list.
     */
    public void clear() {
        size=0;
        list=null;
    }


    /**********************************************************************************
     **  List entries accessors
     **/

    /**
     * Gets an element from this list.
     *
     * @param idx the index of the element
     *
     * @return the element at position {@code idx}, or {@literal null} if no element exists
     */
    @SuppressWarnings( { "unchecked" })
    public Type get(long idx) {
        if(idx<0||idx>=size) return null;
        final Object val=list[(int) idx];
        return (Type) val;
    }

    /**
     * Adds an element to this list.
     *
     * @param elt the element to add
     *
     * @return {@literal true} if the list has been modified as a result of
     *         this operation, {@literal false} if the list remains unchanged
     */
    public boolean add(Type elt) {
        return set(size, elt);
    }

    /**
     * Gets an element in this list.
     *
     * @param idx the index of the element
     * @param elt the element to set
     *
     * @return {@literal true} if the list has been modified as a result of
     *         this operation, {@literal false} if the list remains unchanged (for
     *         instance, because the element was not initially in the list)
     */
    public boolean set(long idx, Type elt) {
        if(idx<0 || idx>size) return false;
        if(list==null) {
            list=new Object[(int) (idx+1)];
        }
        else if(idx>=list.length) {
            // We need to realloc
            int newLen=(int) ((list.length+1)*growthfact);
            if(newLen<=idx) newLen=(int) (idx+1);
            Object[] newlist=new Object[newLen];
            System.arraycopy(list, 0, newlist, 0, list.length);
            list=newlist;
        }
        if(idx==size) size++;
        list[(int) idx]=elt;
        return true;
    }

    /**
     * Removes an element from this list.
     *
     * @param idx the index of element to remove
     *
     * @return {@literal true} if the list has been modified as a result of
     *         this operation, {@literal false} if the list remains unchanged
     */
    public boolean del(long idx){
        if(idx<0||idx>=size) return false;
        size--;
        System.arraycopy(list,(int)(idx+1),list,(int)idx,(int)(size-idx));
        if(size*growthfact*growthfact<list.length) {
            // We need to realloc
            int newLen=(int) ((list.length+1)/growthfact);
            Object[] newlist=new Object[newLen];
            System.arraycopy(list, 0, newlist, 0, (int)size+1);
            list=newlist;
        }
        return true;
    }

    /**
     * Removes an element from this list.
     *
     * @param elt the element to remove
     *
     * @return {@literal true} if the container has been modified as a result of
     *         this operation, {@literal false} if the container remains unchanged
     */
    public boolean del(Type elt) {
        long idx=0;
        while(idx<size) {
            Object obj=list[(int) idx];
            if(elt==obj|| (elt!=null&&elt.equals(obj))) {
                size--;
                System.arraycopy(list,(int)(idx+1),list,(int)idx,(int)(size-idx));
                if(size*growthfact*growthfact<list.length) {
                    // We need to realloc
                    int newLen=(int) ((list.length+1)/growthfact);
                    Object[] newlist=new Object[newLen];
                    System.arraycopy(list, 0, newlist, 0, (int)size+1);
                    list=newlist;
                }
                return true;
            }
        }
        return false;
    }


    /**********************************************************************************
     **  List entries iterators
     **/

    /**
     * Iterates over all elements in the list.
     *
     * @return an iterator over all the elements stored in the list
     */
    public Iterator<? extends Type> iterator() {
        return new Iterator<Type>() {
            private int pos=0;

            public boolean hasNext() {
                return pos<size;
            }

            @SuppressWarnings("unchecked")
            public Type next() {
                if(pos<size) return (Type) list[pos++];
                throw new NoSuchElementException("No element at index "+pos);
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
    public <Par> long visit(Visitor<Type,Par> vis, Par par) {
        long c=0;
        int i=0;
        while(i<size) {
            @SuppressWarnings("unchecked")
            Type obj=(Type) list[i];
            long r=vis.invoke(obj, par);
            if(r<0) return c;
            c+=r;
            i++;
        }
        return c;
    }

    /**********************************************************************************
     **  Externalization
     **/

    /**
     * Write an ArrayList to a stream.
     *
     * @param out the stream to write the object to
     *
     * @throws java.io.IOException if I/O errors occur
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
        out.writeLong(size);
        out.writeObject(null);
        if(size>0) {
            for(int i=0;i<size;i++) {
                out.writeObject(list[i]);
            }
        }
    }

    /**
     * Read an ArrayList from a stream.
     *
     * @param in the stream to read the object from
     *
     * @throws IOException if I/O errors occur
     */
    @SuppressWarnings( { "unchecked" })
    public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
        size=in.readLong();
        in.readLong();   // discard
        in.readObject(); // discard
        if(size>0) {
            list=new Object[(int) size];
            for(int i=0;i<size;i++) {
                list[i]=in.readObject();
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
        if(size>0) {
            for(int i=0;i<size;i++) {
                hash^=list[i].hashCode();
            }
        }
        return hash;
    }

    public boolean equals(Object obj) {
        if(!(obj instanceof ArrayList)) return false;
        ArrayList<?> that=(ArrayList<?>) obj;
        if(this.size!=that.size) return false;
        if(size>0) {
            for(int i=0;i<size;i++) {
                if(this.list[i]==that.list[i]) continue;
                if(this.list[i]==null||that.list[i]==null) return false;
                if(!this.list[i].equals(that.list[i])) return false;
            }
        }
        return true;
    }

    @SuppressWarnings("unchecked")
    public ArrayList<Type> clone() {
        ArrayList<Type> clone;
        try { clone=(ArrayList<Type>) super.clone(); }
        catch(CloneNotSupportedException e) { return null; }
        if(this.list!=null) {
            clone.list=new Object[this.list.length];
            System.arraycopy(this.list, 0, clone.list, 0, this.list.length);
        }
        return clone;
    }

    /**
     * Returns a string representation of the list.
     *
     * @return a string enclosing in square brackets the string representations
     *         of all the elements in the list
     */
    public String toString() {
        StringBuilder buf=new StringBuilder();
        buf.append('[').append(' ');
        boolean first = true;
        for(int i=0;i<size;i++) {
            @SuppressWarnings("unchecked")
            Type obj=(Type) list[i];
            if(first) first=false;
            else buf.append(',');
            buf.append(obj).append(' ');
            i++;
        }
        buf.append(']');
        return buf.toString();
    }

}
