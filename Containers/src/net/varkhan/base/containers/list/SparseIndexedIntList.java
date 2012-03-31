/**
 *
 */
package net.varkhan.base.containers.list;

import net.varkhan.base.containers.Index;
import net.varkhan.base.containers.Indexable;
import net.varkhan.base.containers.Indexed;
import net.varkhan.base.containers.type.IntIterable;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.util.NoSuchElementException;


/**
 * <b>IndexedIntList using sparse segments.</b>.
 * <p/>
 * This list stores objects in arbitrary positions (their index, in the sense of
 * {@link Indexed}).
 * <p/>
 * The entries are kept in a resizable array of storage segments. Each segment
 * maintains occupancy statistics in a bit array, and a compact array of actual
 * elements.
 * <p/>
 * The segment structure bypasses the normal size limits implied by the {@code int}
 * subscript, and limits reallocation costs.
 * <p/>
 *
 * @author varkhan
 * @date Mar 12, 2009
 * @time 6:16:09 AM
 */
public class SparseIndexedIntList extends AbstractSparseIndexedList implements IndexedIntList, Externalizable {

    private static final long serialVersionUID=1L;


    /**
     * The list slot storage
     */
    private int[][] list=null;

    /**
     * The default return value
     */
    private int defVal=0;


    /**********************************************************************************
     **  List constructors
     **/

    /**
     * Creates a new SparseIndexedIntList, specifying the reallocation strategy.
     *
     * @param blockshift the node reference storage block size 2-logarithm
     * @param growthfact the node reference storage growth factor
     */
    public SparseIndexedIntList(int blockshift, double growthfact) {
        super(blockshift, growthfact);
    }

    /**
     * Creates a new SparseIndexedIntList
     */
    public SparseIndexedIntList() {
        this(10, 1.5);
    }

    /**
     * Copies an IndexedList of numbers, specifying the reallocation strategy.
     *
     * @param blockshift the node reference storage block size 2-logarithm
     * @param growthfact the node reference storage growth factor
     * @param list       the IndexedList to copy
     */
    public SparseIndexedIntList(int blockshift, double growthfact, IndexedList<? extends Number> list) {
        this(blockshift, growthfact);
        Index it=list.indexes();
        while(it.hasNext()) {
            long id=it.next();
            Number obj=list.get(id);
            if(obj==null) continue;
            int val=obj.intValue();
            if(val!=defVal) set(id, val);
        }
    }

    /**
     * Copies an IndexedList of numbers.
     *
     * @param list the IndexedList to copy
     */
    public SparseIndexedIntList(IndexedList<? extends Number> list) {
        this();
        Index it=list.indexes();
        while(it.hasNext()) {
            long id=it.next();
            Number obj=list.get(id);
            if(obj==null) continue;
            int val=obj.intValue();
            if(val!=defVal) set(id, val);
        }
    }

    /**
     * Copies an IndexedIntList.
     *
     * @param list the IndexedList to copy
     */
    public SparseIndexedIntList(IndexedIntList list) {
        this();
        Index it=list.indexes();
        while(it.hasNext()) {
            long id=it.next();
            int val=list.getInt(id);
            if(val!=defVal) set(id, val);
        }
    }

    /**
     * Builds a SparseIndexedIntList from an array of numbers.
     *
     * @param array the array to copy
     */
    public <N extends Number> SparseIndexedIntList(N... array) {
        this();
        for(int id=0;id<array.length;id++) {
            Number obj=array[id];
            if(obj==null) continue;
            int val=obj.intValue();
            if(val!=defVal) set(id, val);
        }
    }

    /**
     * Builds a SparseIndexedIntList from an array of ints.
     *
     * @param array the array to copy
     */
    public SparseIndexedIntList(int... array) {
        this();
        for(int id=0;id<array.length;id++) {
            int val=array[id];
            if(val!=defVal) set(id, val);
        }
    }


    /**********************************************************************************
     **  List statistics accessors
     **/


    /**
     * Deletes all elements from this list.
     */
    public void clear() {
        super.clear();
        list=null;
    }

    /**
     * Gets the default value.
     *
     * @return the default value, returned by {@link #get} on empty entries
     */
    public int getDefaultValue() {
        return defVal;
    }

    /**
     * Sets the default value.
     *
     * @param def the default value, returned by {@link #get} on empty entries
     */
    public void setDefaultValue(int def) {
        defVal=def;
    }


    /**********************************************************************************
     **  List entries accessors
     **/

    /**
     * Extracts the element designated by an index.
     *
     * @param index a unique identifier for this entry
     *
     * @return the requested element, or {@literal null} if no entry is associated to this index
     */
    public Integer get(long index) {
        return getInt(index);
    }

    /**
     * Extracts the element designated by an index.
     *
     * @param index a unique identifier for this entry
     *
     * @return the requested element, or {@literal null} if no entry is associated to this index
     */
    public int getInt(long index) {
        if(index<0||index>=head) return defVal;
        int blockpos=(int) (index>>>blockshift);
        byte[] mask=bits[blockpos];
        if(mask==null) return defVal;
        int pos=getSparsePos(mask, (int) (index&blockmask));
        if(pos<=0) return defVal;
        return list[blockpos][pos-1];
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
    public long add(Integer val) {
        if(val==null) return head;
        return add(val.intValue());
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
    public long add(int val) {
        long index=head;
        set(index, val);
        return index;
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
    public long set(long index, Integer val) {
        if(val==null) {
            // No actual storage required
            del(index);
            return index;
        }
        return set(index, val.intValue());
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
    public long set(long index, int val) {
        int blockpos=(int) (index>>>blockshift);
        if(bits==null||list==null) {
            bits=new byte[blocksize][];
            list=new int[blocksize][];
        }
        else if(blockpos>=list.length) {
            // We need to realloc
            int newBlockNum=(int) ((list.length+1)*growthfact);
            if(newBlockNum<=blockpos) newBlockNum=blockpos+1;
            byte[][] newbits=new byte[newBlockNum][];
            System.arraycopy(bits, 0, newbits, 0, bits.length);
            bits=newbits;
            int[][] newlist=new int[newBlockNum][];
            System.arraycopy(list, 0, newlist, 0, list.length);
            list=newlist;
        }
        byte[] mask=bits[blockpos];
        if(mask==null) {
            mask=new byte[blockhead];
            bits[blockpos]=mask;
        }
        int blockoff=(int) (index&blockmask);
        int[] block=list[blockpos];
        if(block==null) {
            block=new int[1];
            list[blockpos]=block;
            block[0]=val;
            setBitOn(mask, blockoff);
            size++;
            if(index>=head) head=index+1;
            return index;
        }
        int blockidx=getSparsePos(mask, blockoff);
        if(blockidx>0) {
            // We already have an object here... set it and bail out
            block[blockidx-1]=val;
            if(index>=head) head=index+1;
            return index;
        }
        blockidx=-blockidx;
        int len=getSparseLength(mask);
        if(len>=block.length) {
            int newlen=(int) ((block.length+1)*growthfact);
            if(len>newlen) newlen=len;
            if(newlen>blocksize) newlen=blocksize;
            int[] newblock=new int[newlen];
            System.arraycopy(block, 0, newblock, 0, blockidx);
            System.arraycopy(block, blockidx, newblock, blockidx+1, len-blockidx);
            newblock[blockidx]=val;
            list[blockpos]=newblock;
        }
        else {
            System.arraycopy(block, blockidx, block, blockidx+1, len-blockidx);
            block[blockidx]=val;
        }
        setBitOn(mask, blockoff);
        size++;
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
        int blockpos=(int) (index>>>blockshift);
        byte[] mask=bits[blockpos];
        if(mask==null) return;
        int blockoff=(int) (index&blockmask);
        int blockidx=getSparsePos(mask, blockoff);
        /// Do we have something to delete?
        if(blockidx<=0) return;
        int[] block=list[blockpos];
        if(block==null) return;
        setBitOff(mask, blockoff);
        // This should be the original length -1
        int len=getSparseLength(mask);
        if(len==0) {
            list[blockpos]=null;
            bits[blockpos]=null;
        }
        else {
            // Set blockidx to the actual zero-based position of the object
            blockidx--;
            // The original length of the section was len+1
            if(len*growthfact*growthfact<block.length) {
                int newlen=(int) ((block.length+1)/growthfact);
                if(len>newlen) newlen=len;
                if(newlen>blocksize) newlen=blocksize;
                int[] newblock=new int[newlen];
                // Copy the start section, up to before blockidx
                System.arraycopy(block, 0, newblock, 0, blockidx);
                // Copy the end section, deleting the object at blockidx
                System.arraycopy(block, blockidx+1, newblock, blockidx, len-blockidx);
                list[blockpos]=newblock;
            }
            else {
                // Copy the end section, deleting the object at blockidx
                System.arraycopy(block, blockidx+1, block, blockidx, len-blockidx);
            }
        }
        size--;
        int headpos=(int) ((head-1)>>>blockshift);
        while(headpos>=0&&isEmpty(bits[headpos])) headpos--;
        if(headpos<0) {
            head=0;
            if(blocksize*growthfact<list.length) {
                // We need to realloc
                bits=new byte[blocksize][];
                list=new int[blocksize][];
            }
        }
        else {
            head=(headpos<<blockshift)+getSparseHead(bits[headpos]);
            if(headpos*growthfact*growthfact<list.length) {
                // We need to realloc
                int newBlockNum=(int) ((list.length+1)/growthfact);
                byte[][] newbits=new byte[newBlockNum][];
                System.arraycopy(bits, 0, newbits, 0, headpos+1);
                bits=newbits;
                int[][] newlist=new int[newBlockNum][];
                System.arraycopy(list, 0, newlist, 0, headpos+1);
                list=newlist;
            }
        }
    }


    /**********************************************************************************
     **  List entries iterators
     **/

    /**
     * Iterates over all elements in the list.
     *
     * @return an iterator over all the elements stored in the list
     */
    public IntIterator iterator() {
        return new IntIterator() {
            private long index=-1;

            public boolean hasNext() {
                long i=index+1;
                while(i<head) {
                    int blockpos=(int) (i>>>blockshift);
                    byte[] mask=bits[blockpos];
                    if(mask==null) {
                        i=(blockpos+1)<<blockshift;
                        continue;
                    }
                    int pos=getSparsePos(mask, (int) (i&blockmask));
                    if(pos>0) return true;
                    i++;
                }
                return false;
            }

            public Integer next() { return nextValue(); }

            public int nextValue() {
                index++;
                while(index<head) {
                    int blockpos=(int) (index>>>blockshift);
                    byte[] mask=bits[blockpos];
                    if(mask==null) {
                        index=(blockpos+1)<<blockshift;
                        continue;
                    }
                    int pos=getSparsePos(mask, (int) (index&blockmask));
                    if(pos>0) return list[blockpos][pos-1];
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
    public <Par> long visit(Visitor<Integer,Par> vis, Par par) {
        long c=0;
        long i=0;
        while(i<head) {
            int blockpos=(int) (i>>>blockshift);
            byte[] mask=bits[blockpos];
            if(mask==null) {
                i=(blockpos+1)<<blockshift;
                continue;
            }
            int pos=getSparsePos(mask, (int) (i&blockmask));
            if(pos>0) {
                int obj=list[blockpos][pos-1];
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
     * visitor's {@link Visitor#invoke} method, until this method returns
     * a negative count.
     *
     * @param vis the visitor
     * @param par the control parameter
     * @param <Par> the type of the control parameter
     *
     * @return the sum of all positive return values from the visitor
     */
    public <Par> long visit(IntVisitor<Par> vis, Par par) {
        long c=0;
        long i=0;
        while(i<head) {
            int blockpos=(int) (i>>>blockshift);
            byte[] mask=bits[blockpos];
            if(mask==null) {
                i=(blockpos+1)<<blockshift;
                continue;
            }
            int pos=getSparsePos(mask, (int) (i&blockmask));
            if(pos>0) {
                int obj=list[blockpos][pos-1];
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
    public <Par> long visit(IndexedVisitor<Integer,Par> vis, Par par) {
        long c=0;
        long i=0;
        while(i<head) {
            int blockpos=(int) (i>>>blockshift);
            byte[] mask=bits[blockpos];
            if(mask==null) {
                i=(blockpos+1)<<blockshift;
                continue;
            }
            int pos=getSparsePos(mask, (int) (i&blockmask));
            if(pos>0) {
                int obj=list[blockpos][pos-1];
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
     * visitor's {@link IndexedVisitor#invoke} method, until this method returns
     * a negative count.
     *
     * @param vis the visitor
     * @param par the control parameter
     * @param <Par> the type of the control parameter
     *
     * @return the sum of all positive return values from the visitor
     */
    public <Par> long visit(IndexedIntVisitor<Par> vis, Par par) {
        long c=0;
        long i=0;
        while(i<head) {
            int blockpos=(int) (i>>>blockshift);
            byte[] mask=bits[blockpos];
            if(mask==null) {
                i=(blockpos+1)<<blockshift;
                continue;
            }
            int pos=getSparsePos(mask, (int) (i&blockmask));
            if(pos>0) {
                int obj=list[blockpos][pos-1];
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
     * @param indexes an array of identifiers
     *
     * @return an iterable over all the elements indexed by the identifiers
     */
    public IntIterable iterate(final long[] indexes) {
        return new IntIterable() {
            public IntIterator iterator() {
                return new IntIterator() {
                    private int i=0;

                    public boolean hasNext() {
                        return i<indexes.length;
                    }

                    public Integer next() {
                        long index=indexes[i++];
                        if(index<0||index>=head) return null;
                        int blockpos=(int) (index>>>blockshift);
                        byte[] mask=bits[blockpos];
                        if(mask==null) return null;
                        int pos=getSparsePos(mask, (int) (index&blockmask));
                        if(pos<=0) return null;
                        return list[blockpos][pos-1];
                    }

                    public int nextValue() {
                        long index=indexes[i++];
                        if(index<0||index>=head) return defVal;
                        int blockpos=(int) (index>>>blockshift);
                        byte[] mask=bits[blockpos];
                        if(mask==null) return defVal;
                        int pos=getSparsePos(mask, (int) (index&blockmask));
                        if(pos<=0) return defVal;
                        return list[blockpos][pos-1];
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
     * @param indexes an iterable over identifiers
     *
     * @return an iterable over all the elements indexed by the identifiers
     */
    public IntIterable iterate(final java.lang.Iterable<Long> indexes) {
        return new IntIterable() {
            public IntIterator iterator() {
                return new IntIterator() {
                    private final java.util.Iterator<Long> iter=indexes.iterator();

                    public boolean hasNext() {
                        return iter.hasNext();
                    }

                    public Integer next() {
                        long index=iter.next();
                        if(index<0||index>=head) return null;
                        int blockpos=(int) (index>>>blockshift);
                        byte[] mask=bits[blockpos];
                        if(mask==null) return null;
                        int pos=getSparsePos(mask, (int) (index&blockmask));
                        if(pos<=0) return null;
                        return list[blockpos][pos-1];
                    }

                    public int nextValue() {
                        long index=iter.next();
                        if(index<0||index>=head) return defVal;
                        int blockpos=(int) (index>>>blockshift);
                        byte[] mask=bits[blockpos];
                        if(mask==null) return defVal;
                        int pos=getSparsePos(mask, (int) (index&blockmask));
                        if(pos<=0) return defVal;
                        return list[blockpos][pos-1];
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
     * @param indexes an iterable over identifiers
     *
     * @return an iterable over all the elements indexed by the identifiers
     */
    public IntIterable iterate(final Indexable indexes) {
        return new IntIterable() {
            public IntIterator iterator() {
                return new IntIterator() {
                    private final Index iter=indexes.indexes();

                    public boolean hasNext() {
                        return iter.hasNext();
                    }

                    public Integer next() {
                        long index=iter.next();
                        if(index<0||index>=head) return null;
                        int blockpos=(int) (index>>>blockshift);
                        byte[] mask=bits[blockpos];
                        if(mask==null) return null;
                        int pos=getSparsePos(mask, (int) (index&blockmask));
                        if(pos<=0) return null;
                        return list[blockpos][pos-1];
                    }

                    public int nextValue() {
                        long index=iter.next();
                        if(index<0||index>=head) return defVal;
                        int blockpos=(int) (index>>>blockshift);
                        byte[] mask=bits[blockpos];
                        if(mask==null) return defVal;
                        int pos=getSparsePos(mask, (int) (index&blockmask));
                        if(pos<=0) return defVal;
                        return list[blockpos][pos-1];
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
     * Write a SparseIndexedList to a stream.
     *
     * @param out the stream to write the object to
     *
     * @throws IOException if I/O errors occur
     * @serialData <li/> {@code Object defVal}     - the default value
     * <li/> {@code byte blockshift}   - the block size 2-logarithm
     * <li/> {@code double growthfact} - the buffer growth factor
     * <li/> {@code long size}         - the number of set entries
     * <li/> {@code long head}         - the highest allocated index + 1
     * <li/> all the blocks, as a bit mask of (blocksize/8) {@code byte}s,
     * followed by the set values (a variable number of {@code int}s, as defined by the mask)
     */
    public void writeExternal(ObjectOutput out) throws IOException {
        super.writeExternal(out);
        out.writeInt(defVal);
        if(head>0) {
            int headlen=(int) (head>>>blockshift)+1;
            byte[] zero=new byte[blockhead];
            for(int i=0;i<headlen;i++) {
                final byte[] mask=bits[i];
                if(mask==null) {
                    out.write(zero, 0, blockhead);
                }
                else {
                    out.write(mask, 0, blockhead);
                    int len=getSparseLength(mask);
                    final int[] block=list[i];
                    for(int j=0;j<len;j++) out.writeInt(block[j]);
                }
            }
        }
    }

    /**
     * Read a SparseIndexedList from a stream.
     *
     * @param in the stream to read the object from
     *
     * @throws IOException if I/O errors occur
     */
    public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
        super.readExternal(in);
        defVal=in.readInt();
        if(head>0) {
            int headlen=(int) (head>>>blockshift)+1;
            bits=new byte[headlen][];
            list=new int[headlen][];
            for(int i=0;i<headlen;i++) {
                final byte[] mask=bits[i]=new byte[blockhead];
                in.readFully(mask, 0, blockhead);
                int len=getSparseLength(mask);
                if(len==0) {
                    bits[i]=null;
                    list[i]=null;
                }
                else {
                    final int[] block=list[i]=new int[len];
                    for(int j=0;j<len;j++) block[j]=in.readInt();
                }
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
        int hash=blockshift;
        hash^=size^(size>>>32);
        hash^=head^(head>>>32);
        if(head>0) {
            int headlen=(int) (head>>>blockshift)+1;
            for(int i=0;i<headlen;i++) {
                final byte[] mask=bits[i];
                if(mask!=null) {
                    for(int j=0;j<blockhead;) {
                        int f=(mask[j++]&0xFF)<<24;
                        if(j<blockhead) f+=(mask[j++]&0xFF)<<16;
                        if(j<blockhead) f+=(mask[j++]&0xFF)<<8;
                        if(j<blockhead) f+=(mask[j++]&0xFF)<<0;
                        hash^=f;
                    }
                    int len=getSparseLength(mask);
                    final int[] block=list[i];
                    for(int j=0;j<len;j++) {
                        hash^=block[j];
                    }
                }
            }
        }
        return hash;
    }

    public boolean equals(Object obj) {
        if(!(obj instanceof SparseIndexedIntList)) return false;
        SparseIndexedIntList that=(SparseIndexedIntList) obj;
        if(this.size!=that.size) return false;
        if(this.head!=that.head) return false;
        if(this.blockshift!=that.blockshift) return false;
        if(this.defVal!=that.defVal) return false;
        if(head>0) {
            int headlen=(int) (head>>>blockshift)+1;
            for(int i=0;i<headlen;i++) {
                final byte[] thismask=this.bits[i];
                final byte[] thatmask=that.bits[i];
                int len;
                if(thismask==null||(len=getSparseLength(thismask))==0) {
                    if(thatmask!=null&&getSparseLength(thatmask)>0) return false;
                    continue;
                }
                for(int j=0;j<blockhead;j++) if(thismask[j]!=thatmask[j]) return false;
                final int[] thisblock=this.list[i];
                final int[] thatblock=that.list[i];
                for(int j=0;j<len;j++) if(thisblock[j]!=thatblock[j]) return false;
            }
        }
        return true;
    }

    public SparseIndexedIntList clone() {
        SparseIndexedIntList clone;
        try { clone=(SparseIndexedIntList) super.clone(); }
        catch(CloneNotSupportedException e) { return null; }
        if(this.bits!=null) {
            clone.bits=new byte[this.bits.length][];
            for(int i=0;i<this.bits.length;i++) {
                byte[] mask=this.bits[i];
                if(mask!=null) {
                    byte[] m=new byte[mask.length];
                    System.arraycopy(mask, 0, m, 0, mask.length);
                    clone.bits[i]=m;
                }
            }
        }
        if(this.list!=null) {
            clone.list=new int[this.list.length][];
            for(int i=0;i<this.list.length;i++) {
                int[] block=this.list[i];
                if(block!=null) {
                    int[] b=new int[block.length];
                    System.arraycopy(block, 0, b, 0, block.length);
                    clone.list[i]=b;
                }
            }
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
        long i=0;
        while(i<head) {
            int blockpos=(int) (i>>>blockshift);
            byte[] mask=bits[blockpos];
            if(mask==null) {
                i=(blockpos+1)<<blockshift;
                continue;
            }
            int pos=getSparsePos(mask, (int) (i&blockmask));
            if(pos>0) buf.append(" ").append(i).append(":").append(list[blockpos][pos-1]);
            i++;
        }
        buf.append("}");
        return buf.toString();
    }

}
