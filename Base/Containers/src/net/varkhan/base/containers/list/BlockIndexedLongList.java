/**
 *
 */
package net.varkhan.base.containers.list;

import net.varkhan.base.containers.Index;
import net.varkhan.base.containers.Indexable;
import net.varkhan.base.containers.Indexed;
import net.varkhan.base.containers.type.LongIterable;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.util.NoSuchElementException;


/**
 * <b>IndexedLongList using resizable arrays of blocks</b>.
 * <p/>
 * This list stores objects in arbitrary positions (their index, in the sense of
 * {@link Indexed}).
 * <p/>
 * The entries are kept in a resizable array of storage blocks, thus bypassing
 * the normal size limits implied by the {@code int} subscript, enabling some
 * degree of sparseness when whole blocks are empty, and limiting reallocation
 * costs.
 * <p/>
 *
 * @author varkhan
 * @date Mar 12, 2009
 * @time 5:44:48 AM
 */
public class BlockIndexedLongList extends AbstractBlockIndexedList implements IndexedLongList, Externalizable, Cloneable {

    private static final long serialVersionUID=1L;

    /**
     * The list slot storage
     */
    private long[][] list=null;

    /**
     * The default return value
     */
    private long defVal=0;


    /**********************************************************************************
     **  List constructors
     **/

    /**
     * Creates a new BlockIndexedLongList, specifying the reallocation strategy.
     *
     * @param blockshift the node reference storage block size 2-logarithm
     * @param growthfact the node reference storage growth factor
     */
    public BlockIndexedLongList(int blockshift, double growthfact) {
        super(blockshift, growthfact);
    }

    /**
     * Creates a new BlockIndexedLongList.
     */
    public BlockIndexedLongList() {
        this(10, 1.5);
    }

    /**
     * Copies an IndexedList of numbers, specifying the reallocation strategy.
     *
     * @param blockshift the node reference storage block size 2-logarithm
     * @param growthfact the node reference storage growth factor
     * @param list       the IndexedList to copy
     */
    public BlockIndexedLongList(int blockshift, double growthfact, IndexedList<? extends Number> list) {
        this(blockshift, growthfact);
        Index it=list.indexes();
        while(it.hasNext()) {
            long id=it.next();
            Number obj=list.get(id);
            if(obj==null) continue;
            long val=obj.longValue();
            if(val!=defVal) set(id, val);
        }
    }

    /**
     * Copies an IndexedList of numbers.
     *
     * @param list the IndexedList to copy
     */
    public BlockIndexedLongList(IndexedList<? extends Number> list) {
        this();
        Index it=list.indexes();
        while(it.hasNext()) {
            long id=it.next();
            Number obj=list.get(id);
            if(obj==null) continue;
            long val=obj.longValue();
            if(val!=defVal) set(id, val);
        }
    }

    /**
     * Copies an IndexedLongList.
     *
     * @param list the IndexedList to copy
     */
    public BlockIndexedLongList(IndexedLongList list) {
        this();
        Index it=list.indexes();
        while(it.hasNext()) {
            long id=it.next();
            long val=list.getLong(id);
            if(val!=defVal) set(id, val);
        }
    }

    /**
     * Builds an BlockIndexedLongList from an array of numbers.
     *
     * @param array the array to copy
     */
    public <N extends Number> BlockIndexedLongList(N... array) {
        this();
        for(int id=0;id<array.length;id++) {
            Number obj=array[id];
            if(obj==null) continue;
            long val=obj.longValue();
            if(val!=defVal) set(id, val);
        }
    }

    /**
     * Builds an BlockIndexedLongList from an array of longs.
     *
     * @param array the array to copy
     */
    public BlockIndexedLongList(long... array) {
        this();
        for(int id=0;id<array.length;id++) {
            long val=array[id];
            if(val!=defVal) set(id, val);
        }
    }


    /**********************************************************************************
     **  List statistics accessors
     **/

    /**
     * Returns an index that has no associated element in this list.
     *
     * @return the smallest unused index in this list
     */
    public long free() {
        if(bits==null||list==null) return 0;
        if(head==size) return head;
        int blockpos=0;
        int blockmax=(int) (head>>>blockshift);
        while(blockpos<blockmax) {
            int blocknum=bits[blockpos];
            if(blocknum==0) return blockpos<<blockshift;
            if(blocknum<blocksize) {
                int blockoff=0;
                long[] block=list[blockoff];
                if(block==null) return blockpos<<blockshift;
                while(blockoff<block.length) {
                    if(block[blockoff]==defVal) return (blockpos<<blockshift)+blockoff;
                    blockoff++;
                }
            }
            blockpos++;
        }
        return head;
    }

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
     * @return the default value, returned by {@link #get} on indexes without an associated entry
     */
    public long getDefaultValue() {
        return defVal;
    }

    /**
     * Sets the default value.
     *
     * @param def the default value, returned by {@link #get} on indexes without an associated entry
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
     * @return {@literal true} if an element is associated with this index,
     *         or {@literal false} if no element is associated with this index
     */
    public boolean has(long index) {
        if(index<0||index>=head) return false;
        long[] block=list[(int) (index>>>blockshift)];
        if(block==null) return false;
        return block[(int) (index&blockmask)]!=defVal;
    }

    /**
     * Extracts the element designated by an index.
     *
     * @param index a unique identifier for this entry
     *
     * @return the requested element, or {@literal null} if no entry is associated to this index
     */
    public Long get(long index) {
        return getLong(index);
    }

    /**
     * Extracts the element designated by an index.
     *
     * @param index a unique identifier for this entry
     *
     * @return the requested element, or {@literal null} if no entry is associated to this index
     */
    public long getLong(long index) {
        if(index<0||index>=head) return defVal;
        long[] block=list[(int) (index>>>blockshift)];
        if(block==null) return defVal;
        return block[(int) (index&blockmask)];
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
    public long add(Long val) {
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
    public long add(long val) {
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
    public long set(long index, Long val) {
        if(val==null) {
            // No actual storage required
            del(index);
            return index;
        }
        return set(index, val.longValue());
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
    public long set(long index, long val) {
        if(val==defVal) {
            // No actual storage required
            del(index);
            return index;
        }
        int blockpos=(int) (index>>>blockshift);
        if(bits==null||list==null) {
            bits=new int[blocksize];
            list=new long[blocksize][];
        }
        else if(blockpos>=list.length) {
            // We need to realloc
            int newBlockNum=(int) ((list.length<<blockshift+1)*growthfact);
            if(newBlockNum<=blockpos) newBlockNum=blockpos+1;
            int[] newbits=new int[newBlockNum];
            System.arraycopy(bits, 0, newbits, 0, bits.length);
            bits=newbits;
            long[][] newlist=new long[newBlockNum][];
            System.arraycopy(list, 0, newlist, 0, list.length);
            list=newlist;
        }
        long[] block=list[blockpos];
        if(block==null) {
            block=new long[blocksize];
            for(int i=0;i<blocksize;i++) block[i]=defVal;
            list[blockpos]=block;
        }
        int blockoff=(int) (index&blockmask);
        if(block[blockoff]==defVal) {
            bits[blockpos]++;
            size++;
        }
        block[blockoff]=val;
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
        long[] block=list[blockpos];
        if(block==null) return;
        int blockoff=(int) (index&blockmask);
        if(block[blockoff]==defVal) return;
        block[blockoff]=defVal;
        bits[blockpos]--;
        size--;
        if(bits[blockpos]==0) {
            list[blockpos]=null;
        }
        int headpos=(int) ((head-1)>>>blockshift);
        while(headpos>0&&bits[headpos]==0) headpos--;
        block=list[headpos];
        int headoff;
        if(block==null) headoff=0;
        else {
            headoff=blocksize;
            while(headoff>0) {
                headoff--;
                if(block[headoff]!=defVal) break;
            }
        }
        head=(headpos<<blockshift)+headoff+1;
        if(headpos*growthfact*growthfact<list.length) {
            // We need to realloc
            int newBlockNum=(int) ((list.length+1)/growthfact);
            int[] newbits=new int[newBlockNum];
            System.arraycopy(bits, 0, newbits, 0, headpos+1);
            bits=newbits;
            long[][] newlist=new long[newBlockNum][];
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
            private long index=-1;

            public long current() {
                return index;
            }

            public boolean hasNext() {
                long i=index+1;
                while(i<head) {
                    int blockpos=(int) (i>>>blockshift);
                    if(bits[blockpos]==0) {
                        i=(blockpos+1)<<blockshift;
                        continue;
                    }
                    if(list[blockpos][(int) (i&blockmask)]!=defVal) return true;
                    i++;
                }
                return false;
            }

            public long next() {
                index++;
                while(index<head) {
                    int blockpos=(int) (index>>>blockshift);
                    if(bits[blockpos]==0) {
                        index=(blockpos+1)<<blockshift;
                        continue;
                    }
                    if(list[blockpos][(int) (index&blockmask)]!=defVal) return index;
                    index++;
                }
                throw new NoSuchElementException("No element at index "+index);
            }

            public boolean hasPrevious() {
                long i=index-1;
                while(i>0) {
                    int blockpos=(int) (i>>>blockshift);
                    if(bits[blockpos]==0) {
                        i=(blockpos<<blockshift)-1;
                        continue;
                    }
                    if(list[blockpos][(int) (i&blockmask)]!=defVal) return true;
                    i--;
                }
                return false;
            }

            public long previous() {
                index--;
                while(index>0) {
                    int blockpos=(int) (index>>>blockshift);
                    if(bits[blockpos]==0) {
                        index=(blockpos<<blockshift)-1;
                        continue;
                    }
                    if(list[blockpos][(int) (index&blockmask)]!=defVal) return index;
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
                    private long index=-1;

                    public boolean hasNext() {
                        long i=index+1;
                        while(i<head) {
                            int blockpos=(int) (i>>>blockshift);
                            if(bits[blockpos]==0) {
                                i=(blockpos+1)<<blockshift;
                                continue;
                            }
                            if(list[blockpos][(int) (i&blockmask)]!=defVal) return true;
                            i++;
                        }
                        return false;
                    }

                    public Long next() {
                        index++;
                        while(index<head) {
                            int blockpos=(int) (index>>>blockshift);
                            if(bits[blockpos]==0) {
                                index=(blockpos+1)<<blockshift;
                                continue;
                            }
                            if(list[blockpos][(int) (index&blockmask)]!=defVal) return index;
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
    public LongIterator iterator() {
        return new LongIterator() {
            private long index=-1;

            public boolean hasNext() {
                long i=index+1;
                while(i<head) {
                    int blockpos=(int) (i>>>blockshift);
                    if(bits[blockpos]==0) {
                        i=(blockpos+1)<<blockshift;
                        continue;
                    }
                    if(list[blockpos][(int) (i&blockmask)]!=defVal) return true;
                    i++;
                }
                return false;
            }

            public Long next() { return nextValue(); }

            public long nextValue() {
                index++;
                while(index<head) {
                    int blockpos=(int) (index>>>blockshift);
                    if(bits[blockpos]==0) {
                        index=(blockpos+1)<<blockshift;
                        continue;
                    }
                    long val=list[blockpos][(int) (index&blockmask)];
                    if(val!=defVal) return val;
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
    public <Par> long visit(Visitor<Long,Par> vis, Par par) {
        long c=0;
        long i=0;
        while(i<head) {
            int blockpos=(int) (i>>>blockshift);
            if(bits[blockpos]==0) {
                i=(blockpos+1)<<blockshift;
                continue;
            }
            long obj=list[blockpos][(int) (i&blockmask)];
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
     * visitor's {@link Visitor#invoke} method, until this method returns
     * a negative count.
     *
     * @param vis the visitor
     * @param par the control parameter
     * @param <Par> the type of the control parameter
     *
     * @return the sum of all positive return values from the visitor
     */
    public <Par> long visit(LongVisitor<Par> vis, Par par) {
        long c=0;
        long i=0;
        while(i<head) {
            int blockpos=(int) (i>>>blockshift);
            if(bits[blockpos]==0) {
                i=(blockpos+1)<<blockshift;
                continue;
            }
            long obj=list[blockpos][(int) (i&blockmask)];
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
    public <Par> long visit(IndexedVisitor<Long,Par> vis, Par par) {
        long c=0;
        long i=0;
        while(i<head) {
            int blockpos=(int) (i>>>blockshift);
            if(bits[blockpos]==0) {
                i=(blockpos+1)<<blockshift;
                continue;
            }
            long obj=list[blockpos][(int) (i&blockmask)];
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
     * visitor's {@link IndexedVisitor#invoke} method, until this method returns
     * a negative count.
     *
     * @param vis the visitor
     * @param par the control parameter
     * @param <Par> the type of the control parameter
     *
     * @return the sum of all positive return values from the visitor
     */
    public <Par> long visit(IndexedLongVisitor<Par> vis, Par par) {
        long c=0;
        long i=0;
        while(i<head) {
            int blockpos=(int) (i>>>blockshift);
            if(bits[blockpos]==0) {
                i=(blockpos+1)<<blockshift;
                continue;
            }
            long obj=list[blockpos][(int) (i&blockmask)];
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
    public LongIterable iterate(final long[] indexes) {
        return new LongIterable() {
            public LongIterator iterator() {
                return new LongIterator() {
                    private int i=0;

                    public boolean hasNext() {
                        return i<indexes.length;
                    }

                    public Long next() {
                        long index=indexes[i++];
                        return list[(int) (index>>>blockshift)][(int) (index&blockmask)];
                    }

                    public long nextValue() {
                        long index=indexes[i++];
                        return list[(int) (index>>>blockshift)][(int) (index&blockmask)];
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
    public LongIterable iterate(final java.lang.Iterable<Long> indexes) {
        return new LongIterable() {
            public LongIterator iterator() {
                return new LongIterator() {
                    private final java.util.Iterator<Long> iter=indexes.iterator();

                    public boolean hasNext() {
                        return iter.hasNext();
                    }

                    public Long next() {
                        long index=iter.next();
                        return list[(int) (index>>>blockshift)][(int) (index&blockmask)];
                    }

                    public long nextValue() {
                        long index=iter.next();
                        return list[(int) (index>>>blockshift)][(int) (index&blockmask)];
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
    public LongIterable iterate(final Indexable indexes) {
        return new LongIterable() {
            public LongIterator iterator() {
                return new LongIterator() {
                    private final Index iter=indexes.indexes();

                    public boolean hasNext() {
                        return iter.hasNext();
                    }

                    public Long next() {
                        long index=iter.next();
                        return list[(int) (index>>>blockshift)][(int) (index&blockmask)];
                    }

                    public long nextValue() {
                        long index=iter.next();
                        return list[(int) (index>>>blockshift)][(int) (index&blockmask)];
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
     * Write an BlockIndexedLongList to a stream.
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
        super.writeExternal(out);
        out.writeLong(defVal);
        if(head>0) {
            int headlen=(int) (head>>>blockshift)+1;
            for(int i=0;i<headlen;i++) {
                out.writeInt(bits[i]);
                final long[] block=list[i];
                if(block==null) for(int j=0;j<blocksize;j++) out.writeLong(defVal);
                else for(int j=0;j<blocksize;j++) out.writeLong(block[j]);
            }
        }
    }

    /**
     * Read an BlockIndexedList from a stream.
     *
     * @param in the stream to read the object from
     *
     * @throws IOException if I/O errors occur
     */
    public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
        super.readExternal(in);
        defVal=in.readLong();
        if(head>0) {
            int headlen=(int) (head>>>blockshift)+1;
            bits=new int[headlen];
            list=new long[headlen][];
            for(int i=0;i<headlen;i++) {
                bits[i]=in.readInt();
                final long[] block=list[i]=new long[blocksize];
                for(int j=0;j<blocksize;j++) block[j]=in.readLong();
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
                hash^=bits[i];
                final long[] block=list[i];
                if(block!=null) for(int j=0;j<blocksize;j++) {
                    long l=block[j];
                    hash^=l^(l>>>32);
                }
            }
        }
        return hash;
    }

    public boolean equals(Object obj) {
        if(!(obj instanceof BlockIndexedLongList)) return false;
        BlockIndexedLongList that=(BlockIndexedLongList) obj;
        if(this.size!=that.size) return false;
        if(this.head!=that.head) return false;
        if(this.blockshift!=that.blockshift) return false;
        if(this.defVal!=that.defVal) return false;
        if(head>0) {
            int headlen=(int) (head>>>blockshift)+1;
            for(int i=0;i<headlen;i++) {
                final int thisnum=this.bits[i];
                final int thatnum=that.bits[i];
                if(thisnum!=thatnum) return false;
                final long[] thisblock=this.list[i];
                final long[] thatblock=that.list[i];
                if(thisblock==null&&thatblock==null) continue;
                if(thisblock==null||thatblock==null) return false;
                for(int j=0;j<blocksize;j++) {
                    if(thisblock[j]!=thatblock[j]) return false;
                }
            }
        }
        return true;
    }

    public BlockIndexedLongList clone() {
        BlockIndexedLongList clone;
        try { clone=(BlockIndexedLongList) super.clone(); }
        catch(CloneNotSupportedException e) { return null; }
        if(this.bits!=null) {
            clone.bits=new int[this.bits.length];
            System.arraycopy(this.bits, 0, clone.bits, 0, this.bits.length);
        }
        if(this.list!=null) {
            clone.list=new long[this.list.length][];
            for(int i=0;i<this.list.length;i++) {
                long[] block=this.list[i];
                if(block!=null) {
                    long[] b=new long[block.length];
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
     * @return a string enclosing in square brackets the string representations
     *         of all the elements in the list, prefixed by their index
     */
    public String toString() {
        StringBuilder buf=new StringBuilder();
        buf.append('[');
        boolean first = true;
        long i=0;
        while(i<head) {
            int blockpos=(int) (i>>>blockshift);
            if(bits[blockpos]==0) {
                i=(blockpos+1)<<blockshift;
                continue;
            }
            long val=list[blockpos][(int) (i&blockmask)];
            if(val!=defVal) {
                if(first) first=false;
                else buf.append(',');
                buf.append(' ').append(i).append('@').append(val);
            }
            i++;
        }
        buf.append(' ').append('(').append(defVal).append(')').append(' ');
        buf.append(']');
        return buf.toString();
    }

}
