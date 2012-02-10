/**
 *
 */
package net.varkhan.base.containers.list;

import net.varkhan.base.containers.Index;
import net.varkhan.base.containers.Indexed;

import java.io.IOException;
import java.io.InvalidClassException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.lang.reflect.Field;
import java.security.PrivilegedActionException;
import java.util.NoSuchElementException;


/**
 * <b>Abstract IndexedList using sparse segments</b>.
 * <p/>
 * This abstract list framework is used as a common base for type-specific
 * concrete implementations, that store objects in arbitrary positions (their
 * index, in the sense of {@link Indexed}).
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
abstract class AbstractSparseIndexedList {

    /**
     * The segment size 2-logarithm
     */
    protected final int blockshift;

    /**
     * The segment size
     */
    protected final int blocksize;

    /**
     * The segment subscript mask ({@code blocksize - 1})
     */
    protected final int blockmask;

    /**
     * The segment header size ({@code blocksize/8})
     */
    protected final int blockhead;

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
     * The segment occupancy storage
     */
    protected byte[][] bits=null;


    /**********************************************************************************
     **  List constructors
     **/

    /**
     * Creates a new SparseIndexedList, specifying the reallocation strategy.
     *
     * @param blockshift the node reference storage block size 2-logarithm
     * @param growthfact the node reference storage growth factor
     */
    public AbstractSparseIndexedList(int blockshift, double growthfact) {
        if(blockshift<=3) blockshift=3;
        if(growthfact<=1) growthfact=1.5;
        this.growthfact=growthfact;
        this.blockshift=blockshift;
        this.blocksize=(1<<blockshift);
        this.blockmask=(1<<blockshift)-1;
        this.blockhead=1<<(blockshift-3);
        clear();
    }


    /**********************************************************************************
     **  List statistics accessors
     **/

    /**
     * Returns the number of elements in this list.
     *
     * @return the number of entries (elements and related indexes) stored in this list
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
    public final long free() {
        if(bits==null) return 0;
        if(head==size) return head;
        int blockpos=0;
        int blockmax=(int) (head>>>blockshift);
        while(blockpos<blockmax) {
            byte[] mask=bits[blockpos];
            if(mask==null) return blockpos<<blockshift;
            int pos=getSparseTail(mask);
            if(pos<blocksize) return (blockpos<<blockshift)+pos;
            blockpos++;
        }
        return head;
    }

    /**
     * Deletes all elements from this list.
     */
    public void clear() {
        size=0;
        head=0;
        bits=null;
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
    public final boolean has(long index) {
        if(index<0||index>=head) return false;
        int blockpos=(int) (index>>>blockshift);
        byte[] mask=bits[blockpos];
        if(mask==null) return false;
        int pos=getSparsePos(mask, (int) (index&blockmask));
        return pos>0;
    }


    /**********************
     **  Bit mask operators
     **/

    /**
     * All the bit numbers for bytes
     */
    private static final byte[] bitNum=new byte[256];

    static {
        for(int i=0;i<256;i++) {
            int b=i;
            byte c=0;
            while(b!=0) {
                c+=b&0x1;
                b>>>=1;
            }
            bitNum[i]=c;
        }
    }

    /**
     * All the top set bit positions for bytes
     */
    private static final byte[] bitHead=new byte[256];

    static {
        for(int i=0;i<256;i++) {
            int b=i;
            byte c=0;
            while(b!=0) {
                c++;
                b>>>=1;
            }
            bitHead[i]=c;
        }
    }

    /**
     * All the bottom unset bit positions for bytes
     */
    private static final byte[] bitTail=new byte[256];

    static {
        for(int i=0;i<256;i++) {
            int b=i;
            byte c=0;
            while((b&0x1)!=0) {
                c++;
                b>>>=1;
            }
            bitTail[i]=c;
        }
    }

    /**
     * Indicates whether all bytes of a mask are zero.
     *
     * @param mask the mask
     *
     * @return {@literal true} if all bytes of {@code mask} are 0
     */
    protected static boolean isEmpty(byte[] mask) {
        if(mask==null) return true;
        for(byte b : mask) if(b!=0) return false;
        return true;
    }

    /**
     * Gets the number of set bits in a mask.
     *
     * @param mask the mask
     *
     * @return the total number of bits set to 1 in the mask bytes
     */
    protected static int getSparseLength(byte[] mask) {
        if(mask==null) return 0;
        int len=0;
        for(byte b : mask) len+=0xFF&bitNum[b&0xFF];
        return len;
    }

    /**
     * The number of set bits in a starting segment of a mask.
     *
     * @param mask the mask
     * @param off  the end position of the segment
     *
     * @return the number of bits set to 1 between the bit {@code 0} and the bit {@code off} i the mask,
     *         as a positive number if the bit {@code off} is set to 1, or as a negative number if it is 0
     */
    protected static int getSparsePos(byte[] mask, int off) {
        if(mask==null) return 0;
        int bitpos=off>>>3;
        int objpos=0;
        for(int i=0;i<bitpos;i++) {
            objpos+=0xFF&bitNum[mask[i]&0xFF];
        }
        int bitoff=off&0x7;
        int bitmsk=1<<bitoff;
        byte b=mask[bitpos];
        objpos+=0xFF&bitNum[b&(bitmsk-1)];
        if((b&bitmsk)==0) return -objpos;
        return 1+objpos;
    }

    /**
     * The length of the smallest segment containing all set bits in a mask.
     *
     * @param mask the mask
     *
     * @return the number of bits between the start and the highest set bit of a mask
     *         (or 0 if no bit is set in the mask)
     */
    protected static int getSparseHead(byte[] mask) {
        if(mask==null) return 0;
        int bitpos=mask.length;
        while(bitpos>0) {
            bitpos--;
            if(mask[bitpos]!=0) break;
        }
        return (bitpos<<3)+(0xFF&bitHead[mask[bitpos]&0xFF]);
    }

    /**
     * The length of the biggest segment whose bits are all set in a mask.
     *
     * @param mask the mask
     *
     * @return the number of bits between the start and the highest set bit of a mask
     *         (or 0 if no bit is set in the mask)
     */
    protected static int getSparseTail(byte[] mask) {
        if(mask==null) return 0;
        int bitpos=0;
        while(bitpos<mask.length) {
            if(mask[bitpos]!=0xFF) break;
            bitpos++;
        }
        return (bitpos<<3)+(0xFF&bitTail[mask[bitpos]&0xFF]);
    }

    /**
     * Sets a bit in a mask to 1.
     *
     * @param mask the mask
     * @param off  the offset of the bit in the mask
     */
    protected static void setBitOn(byte[] mask, int off) {
        int bitpos=off>>>3;
        int bitoff=off&0x7;
        mask[bitpos]|=1<<bitoff;
    }

    /**
     * Unsets a bit in a mask to 0.
     *
     * @param mask the mask
     * @param off  the offset of the bit in the mask
     */
    protected static void setBitOff(byte[] mask, int off) {
        int bitpos=off>>>3;
        int bitoff=off&0x7;
        mask[bitpos]&=~(1<<bitoff);
    }


    /**********************************************************************************
     **  List entries iterators
     **/

    /**
     * Iterates over all indexes in the list, using an {@link Index}.
     *
     * @return an iterator over all the indexes that designate elements in the list
     */
    public final Index indexes() {
        return new Index() {
            private long index=-1;

            public long current() {
                return index;
            }

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

            public long next() {
                index++;
                while(index<head) {
                    int blockpos=(int) (index>>>blockshift);
                    byte[] mask=bits[blockpos];
                    if(mask==null) {
                        index=(blockpos+1)<<blockshift;
                        continue;
                    }
                    int pos=getSparsePos(mask, (int) (index&blockmask));
                    if(pos>0) return index;
                    index++;
                }
                throw new NoSuchElementException("No element at index "+index);
            }

            public boolean hasPrevious() {
                long i=index-1;
                while(i>0) {
                    int blockpos=(int) (i>>>blockshift);
                    byte[] mask=bits[blockpos];
                    if(mask==null) {
                        i=(blockpos<<blockshift)-1;
                        continue;
                    }
                    int pos=getSparsePos(mask, (int) (i&blockmask));
                    if(pos>0) return true;
                    i--;
                }
                return false;
            }

            public long previous() {
                index--;
                while(index>0) {
                    int blockpos=(int) (index>>>blockshift);
                    byte[] mask=bits[blockpos];
                    if(mask==null) {
                        index=(blockpos<<blockshift)-1;
                        continue;
                    }
                    int pos=getSparsePos(mask, (int) (index&blockmask));
                    if(pos>0) return index;
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
    public final java.lang.Iterable<Long> iterateIndexes() {
        return new java.lang.Iterable<Long>() {
            public java.util.Iterator<Long> iterator() {
                return new java.util.Iterator<Long>() {
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

                    public Long next() {
                        index++;
                        while(index<head) {
                            int blockpos=(int) (index>>>blockshift);
                            byte[] mask=bits[blockpos];
                            if(mask==null) {
                                index=(blockpos+1)<<blockshift;
                                continue;
                            }
                            int pos=getSparsePos(mask, (int) (index&blockmask));
                            if(pos>0) return index;
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
     * followed by the set values (a variable number of {@code float}s, as defined by the mask)
     */
    public void writeExternal(ObjectOutput out) throws IOException {
        out.writeByte(blockshift);
        out.writeDouble(growthfact);
        out.writeLong(size);
        out.writeLong(head);
    }

    /**
     * Read a SparseIndexedList from a stream.
     *
     * @param in the stream to read the object from
     *
     * @throws IOException if I/O errors occur
     */
    public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
        int block_shift=in.readByte();
        double growth_fact=in.readDouble();
        if(block_shift<=3) block_shift=3;
        if(growth_fact<=1) growth_fact=1.5;
        final Class<?> klass=AbstractSparseIndexedList.class;
        final Object target=this;
        // Set block* fields, bypassing final protection
        try {
            final Field field_BS=klass.getDeclaredField("blockshift");
            final int value_BS=block_shift;
            final Field field_BZ=klass.getDeclaredField("blocksize");
            final int value_BZ=1<<block_shift;
            final Field field_BM=klass.getDeclaredField("blockmask");
            final int value_BM=(1<<block_shift)-1;
            final Field field_BH=klass.getDeclaredField("blockhead");
            final int value_BH=1<<(block_shift-3);
            final Field field_GF=klass.getDeclaredField("growthfact");
            final double value_GF=growth_fact;
            java.security.AccessController.doPrivileged(
                    new java.security.PrivilegedExceptionAction<Object>() {
                        public Object run() throws IllegalAccessException {
                            field_BS.setAccessible(true);
                            field_BS.setInt(target, value_BS);
                            field_BS.setAccessible(false);
                            field_BZ.setAccessible(true);
                            field_BZ.setInt(target, value_BZ);
                            field_BZ.setAccessible(false);
                            field_BM.setAccessible(true);
                            field_BM.setInt(target, value_BM);
                            field_BM.setAccessible(false);
                            field_BH.setAccessible(true);
                            field_BH.setInt(target, value_BH);
                            field_BH.setAccessible(false);
                            field_GF.setAccessible(true);
                            field_GF.setDouble(target, value_GF);
                            field_GF.setAccessible(false);
                            return null;
                        }
                    }
                                                       );
        }
        catch(NoSuchFieldException e) {
            // This can never happen
            throw new InvalidClassException(klass.getSimpleName(), "Invalid class structure, field "+e.getMessage()+" not found");
        }
        catch(PrivilegedActionException e) {
            InvalidClassException t=new InvalidClassException(
                    SparseIndexedList.class.getSimpleName(),
                    "Invalid class structure"
            );
            t.initCause(e.getCause());
            throw t;
        }
        catch(SecurityException e) {
            InvalidClassException t=new InvalidClassException(
                    SparseIndexedList.class.getSimpleName(),
                    "Invalid class structure"
            );
            t.initCause(e.getCause());
            throw t;
        }
        /**
         * This is equivalent to:
         *
         blockshift = in.readByte();
         blocksize = 1 << blockshift;
         blockmask = (1 << blockshift) - 1;
         blockhead = 1 << (blockshift-3);
         * but bypasses the final keyword
         */
        size=in.readLong();
        head=in.readLong();
    }

    public int hashCode() {
        int hash=blockshift;
        hash^=size^(size>>>32);
        hash^=head^(head>>>32);
        return hash;
    }

    protected AbstractSparseIndexedList clone() throws CloneNotSupportedException {
        AbstractSparseIndexedList clone=(AbstractSparseIndexedList) super.clone();
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
        return clone;
    }

}
