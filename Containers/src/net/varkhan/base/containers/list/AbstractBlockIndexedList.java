/**
 *
 */
package net.varkhan.base.containers.list;

import net.varkhan.base.containers.Indexed;

import java.io.IOException;
import java.io.InvalidClassException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.lang.reflect.Field;
import java.security.PrivilegedActionException;


/**
 * <b>Abstract IndexedList using resizable arrays of blocks</b>.
 * <p/>
 * This abstract list framework is used as a common base for type-specific
 * concrete implementations, that store objects in arbitrary positions (their
 * index, in the sense of {@link Indexed}).
 * <p/>
 * The entries are kept in a resizable array of storage blocks, thus bypassing
 * the normal size limits implied by the {@code int} subscript, enabling some
 * degree of sparseness when whole blocks are empty, and limiting reallocation
 * costs.
 * <p/>
 *
 * @author varkhan
 * @date Mar 23, 2009
 * @time 9:38:09 PM
 */
abstract class AbstractBlockIndexedList {

    /**
     * The list block size 2-logarithm
     */
    protected final int blockshift;

    /**
     * The list block size
     */
    protected final int blocksize;

    /**
     * The list block mask ({@code blocksize - 1})
     */
    protected final int blockmask;

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
     * The list occupancy storage
     */
    protected int[] bits=null;


    /**********************************************************************************
     **  List constructors
     **/

    /**
     * Creates a new BlockIndexedList, specifying the reallocation strategy.
     *
     * @param blockshift the node reference storage block size 2-logarithm
     * @param growthfact the node reference storage growth factor
     */
    public AbstractBlockIndexedList(int blockshift, double growthfact) {
        if(blockshift<=3) blockshift=3;
        if(growthfact<=1) growthfact=1.5;
        this.growthfact=growthfact;
        this.blockshift=blockshift;
        this.blocksize=(1<<blockshift);
        this.blockmask=(1<<blockshift)-1;
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
     * Deletes all elements from this list.
     */
    public void clear() {
        size=0;
        head=0;
        bits=null;
    }


    /**********************************************************************************
     **  Externalization
     **/

    /**
     * Write an BlockIndexedList to a stream.
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
        out.writeByte(blockshift);
        out.writeDouble(growthfact);
        out.writeLong(size);
        out.writeLong(head);
    }

    /**
     * Read an BlockIndexedList from a stream.
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
        final Class<?> klass=AbstractBlockIndexedList.class;
        final Object target=this;
        // Set block* fields, bypassing final protection
        try {
            final Field field_BS=klass.getDeclaredField("blockshift");
            final int value_BS=block_shift;
            final Field field_BZ=klass.getDeclaredField("blocksize");
            final int value_BZ=1<<block_shift;
            final Field field_BM=klass.getDeclaredField("blockmask");
            final int value_BM=(1<<block_shift)-1;
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
            throw new InvalidClassException(BlockIndexedList.class.getSimpleName(), "Invalid class structure");
        }
        catch(PrivilegedActionException e) {
            InvalidClassException t=new InvalidClassException(
                    BlockIndexedList.class.getSimpleName(),
                    "Invalid class structure"
            );
            t.initCause(e.getCause());
            throw t;
        }
        catch(SecurityException e) {
            InvalidClassException t=new InvalidClassException(
                    BlockIndexedList.class.getSimpleName(),
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
         * but bypasses the final keyword
         */
        size=in.readLong();
        head=in.readLong();
    }


    /**
     * *******************************************************************************
     * *  Object method overrides
     */


    @Override
    public int hashCode() {
        int hash=blockshift;
        hash^=size^(size>>>32);
        hash^=head^(head>>>32);
        return hash;
    }

    protected AbstractBlockIndexedList clone() throws CloneNotSupportedException {
        AbstractBlockIndexedList clone=(AbstractBlockIndexedList) super.clone();
        if(this.bits!=null) {
            clone.bits=new int[this.bits.length];
            System.arraycopy(this.bits, 0, clone.bits, 0, this.bits.length);
        }
        return clone;
    }

}
