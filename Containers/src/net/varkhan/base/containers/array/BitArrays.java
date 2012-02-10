/**
 *
 */
package net.varkhan.base.containers.array;

/**
 * <b>Static bit manipulation utilities.</b>
 * <p/>
 * Utilities for manipulating integer types as bit arrays,
 * and performing conversions between representations.
 * <p/>
 *
 * @author varkhan
 * @date Mar 12, 2009
 * @time 3:07:18 AM
 */
public class BitArrays {

    /**
     * Private empty constructor that forbids instantiation of this class.
     */
    protected BitArrays() {
    }


    /*********************************************************************************
     **  Bit to boolean conversion
     **/

    /**
     * Flip the bits of a bit mask according to the values to a boolean array.
     *
     * @param mask  the original bit mask
     * @param flags the boolean array
     *
     * @return a bit mask whose bits are flipped compared to {@code mask} iff the corresponding boolean is {@literal true}
     */
    public static long flip(long mask, boolean[] flags) {
        for(int i=0;i<flags.length;i++) {
            mask^=1<<i;
        }
        return mask;
    }

    /**
     * Flip the values of a boolean array according to the bits of a bit mask.
     *
     * @param flags the original boolean array
     * @param mask  the bit mask
     *
     * @return a boolean array whose values are flipped if the corresponding bit is set
     */
    public static boolean[] flip(boolean[] flags, long mask) {
        for(int i=0;i<flags.length;i++) {
            flags[i]^=0!=(mask&(1<<i));
        }
        return flags;
    }


    /*********************************************************************************
     **  Bit counting
     **/

    /**
     * All the counts of set bits
     */
    private static final byte[] byteCSB=new byte[256];

    static {
        for(int i=0;i<256;i++) {
            int b=i;
            byte c=0;
            while(b!=0) {
                c+=b&0x1;
                b>>>=1;
            }
            byteCSB[i]=c;
        }
    }

    /**
     * Count the set bits of an integer value.
     *
     * @param val the integer
     *
     * @return the number of bits set to 1 in the argument
     */
    public static int csb(byte val) {
//        val = (byte) (val - ((val >>> 1) & 0x5555));
//        val = (byte) ((val & 0x3333) + ((val >>> 2) & 0x3333));
//        val = (byte) ((val + (val >>> 4)) & 0x0f);
//        return val & 0x3f;
        return 0xFF&byteCSB[val&0xFF];
    }

    /**
     * Count the set bits of an integer value.
     *
     * @param val the integer
     *
     * @return the number of bits set to 1 in the argument
     */
    public static int csb(short val) {
        val=(short) (val-((val>>>1)&0x5555));
        val=(short) ((val&0x3333)+((val>>>2)&0x3333));
        val=(short) ((val+(val>>>4))&0x0f0f);
        val=(short) (val+(val>>>8));
        return val&0x3f;
//        return (0xFF& byteCSB[val&0xFF]) +
//               (0xFF& byteCSB[(val>>>8)&0xFF]) ;
    }

    /**
     * Count the set bits of an integer value.
     *
     * @param val the integer
     *
     * @return the number of bits set to 1 in the argument
     */
    public static int csb(int val) {
        val=val-((val>>>1)&0x55555555);
        val=(val&0x33333333)+((val>>>2)&0x33333333);
        val=(val+(val>>>4))&0x0f0f0f0f;
        val=val+(val>>>8);
        val=val+(val>>>16);
        return val&0x3f;
//        return (0xFF& byteCSB[val&0xFF]) +
//               (0xFF& byteCSB[(val>>> 8)&0xFF]) +
//               (0xFF& byteCSB[(val>>>16)&0xFF]) +
//               (0xFF& byteCSB[(val>>>24)&0xFF]) ;
    }

    /**
     * Count the set bits of an integer value.
     *
     * @param val the integer
     *
     * @return the number of bits set to 1 in the argument
     */
    public static int csb(long val) {
        val=val-((val>>>1)&0x5555555555555555L);
        val=(val&0x3333333333333333L)+((val>>>2)&0x3333333333333333L);
        val=(val+(val>>>4))&0x0f0f0f0f0f0f0f0fL;
        val=val+(val>>>8);
        val=val+(val>>>16);
        val=val+(val>>>32);
        return (int) val&0x7f;
//        return (0xFF& byteCSB[(int)(val&0xFF)]) +
//               (0xFF& byteCSB[(int)((val>>> 8)&0xFF)]) +
//               (0xFF& byteCSB[(int)((val>>>16)&0xFF)]) +
//               (0xFF& byteCSB[(int)((val>>>24)&0xFF)]) +
//               (0xFF& byteCSB[(int)((val>>>32)&0xFF)]) +
//               (0xFF& byteCSB[(int)((val>>>40)&0xFF)]) +
//               (0xFF& byteCSB[(int)((val>>>48)&0xFF)]) +
//               (0xFF& byteCSB[(int)((val>>>56)&0xFF)]) ;
    }


    /*********************************************************************************
     **  Left set bit
     **/

    /**
     * All the left set bits for bytes
     */
    private static final byte[] byteLSB=new byte[256];

    static {
        byteLSB[0]=0;
        for(int i=1;i<256;i++) {
            byte c=0;
            while((i>>>c)!=0) c++;
            byteLSB[i]=c;
        }
    }

    /**
     * Return the position of the highest set bit of an integer value.
     *
     * @param val the integer
     *
     * @return the position of the highest bit set to 1 in the argument, starting at 1
     *         (or 0 if the argument is zero)
     */
    public static int lsb(byte val) {
        return 0xFF&byteLSB[val&0xFF];
    }

    /**
     * Return the position of the highest set bit of an integer value.
     *
     * @param val the integer
     *
     * @return the position of the highest bit set to 1 in the argument, starting at 1
     *         (or 0 if the argument is zero)
     */
    public static int lsb(short val) {
        if((val>>>8)==0) return 0xFF&byteLSB[val&0xFF];
        return 8+(0xFF&byteLSB[(val>>>8)&0xFF]);
    }

    /**
     * Return the position of the highest set bit of an integer value.
     *
     * @param val the integer
     *
     * @return the position of the highest bit set to 1 in the argument, starting at 1
     *         (or 0 if the argument is zero)
     */
    public static int lsb(int val) {
        if((val>>>8)==0) return 0xFF&byteLSB[val&0xFF];
        if((val>>>16)==0) return 8+(0xFF&byteLSB[(val>>>8)&0xFF]);
        if((val>>>24)==0) return 16+(0xFF&byteLSB[(val>>>16)&0xFF]);
        return 24+(0xFF&byteLSB[(val>>>24)&0xFF]);
    }

    /**
     * Return the position of the highest set bit of an integer value.
     *
     * @param val the integer
     *
     * @return the position of the highest bit set to 1 in the argument, starting at 1
     *         (or 0 if the argument is zero)
     */
    public static int lsb(long val) {
        if((val>>>8)==0) return 0xFF&byteLSB[(int) (val&0xFF)];
        if((val>>>16)==0) return 8+(0xFF&byteLSB[(int) ((val>>>8)&0xFF)]);
        if((val>>>24)==0) return 16+(0xFF&byteLSB[(int) ((val>>>16)&0xFF)]);
        if((val>>>32)==0) return 24+(0xFF&byteLSB[(int) ((val>>>24)&0xFF)]);
        if((val>>>40)==0) return 32+(0xFF&byteLSB[(int) ((val>>>32)&0xFF)]);
        if((val>>>48)==0) return 40+(0xFF&byteLSB[(int) ((val>>>40)&0xFF)]);
        if((val>>>56)==0) return 48+(0xFF&byteLSB[(int) ((val>>>48)&0xFF)]);
        return 56+(0xFF&byteLSB[(int) ((val>>>56)&0xFF)]);
    }


    /*********************************************************************************
     **  Right set bit
     **/

    /**
     * All the right set bits for bytes
     */
    private static final byte[] byteRSB=new byte[256];

    static {
        byteRSB[0]=-1;
        for(int i=1;i<256;i++) {
            byte c=0;
            while((i&(1<<c))==0) c++;
            byteRSB[i]=c;
        }
    }

    /**
     * Return the position of the lowest set bit of an integer value.
     *
     * @param val the integer
     *
     * @return the position of the lowest bit set to 1 in the argument, starting at 1
     *         (or 0 if the argument is zero)
     */
    public static int rsb(byte val) {
        if(val==0) return -1;
        return 0xFF&byteRSB[val&0xFF];
    }

    /**
     * Return the position of the lowest set bit of an integer value.
     *
     * @param val the integer
     *
     * @return the position of the lowest bit set to 1 in the argument, starting at 1
     *         (or 0 if the argument is zero)
     */
    public static int rsb(short val) {
        if(val==0) return -1;
        if((val&0xFF)!=0) return 0xFF&byteRSB[val&0xFF];
        return 8+(0xFF&byteRSB[(val>>>8)&0xFF]);
    }

    /**
     * Return the position of the lowest set bit of an integer value.
     *
     * @param val the integer
     *
     * @return the position of the lowest bit set to 1 in the argument, starting at 1
     *         (or 0 if the argument is zero)
     */
    public static int rsb(int val) {
        if(val==0) return -1;
        if((val&0x000000FF)!=0) return 0xFF&byteRSB[val&0xFF];
        if((val&0x0000FF00)!=0) return 8+(0xFF&byteRSB[(val>>>8)&0xFF]);
        if((val&0x00FF0000)!=0) return 16+(0xFF&byteRSB[(val>>>16)&0xFF]);
        return 24+(0xFF&byteRSB[(val>>>24)&0xFF]);
    }

    /**
     * Return the position of the lowest set bit of an integer value.
     *
     * @param val the integer
     *
     * @return the position of the lowest bit set to 1 in the argument, starting at 1
     *         (or 0 if the argument is zero)
     */
    public static int rsb(long val) {
        if(val==0) return -1;
        if((val&0x00000000000000FFL)!=0) return 0xFF&byteRSB[(int) (val&0xFF)];
        if((val&0x000000000000FF00L)!=0) return 8+(0xFF&byteRSB[(int) ((val>>>8)&0xFF)]);
        if((val&0x0000000000FF0000L)!=0) return 16+(0xFF&byteRSB[(int) ((val>>>16)&0xFF)]);
        if((val&0x00000000FF000000L)!=0) return 24+(0xFF&byteRSB[(int) ((val>>>24)&0xFF)]);
        if((val&0x000000FF00000000L)!=0) return 32+(0xFF&byteRSB[(int) ((val>>>32)&0xFF)]);
        if((val&0x0000FF0000000000L)!=0) return 40+(0xFF&byteRSB[(int) ((val>>>40)&0xFF)]);
        if((val&0x00FF000000000000L)!=0) return 48+(0xFF&byteRSB[(int) ((val>>>48)&0xFF)]);
        return 56+(0xFF&byteRSB[(int) ((val>>>56)&0xFF)]);
    }


    /*********************************************************************************
     **  Multiplexing
     **/

    /**
     * All the 2-multiplexings for bytes
     */
    private static final short[] byteMP2=new short[256];

    static {
        for(int i=0;i<256;i++) {
            short c=0;
            for(int p=0;p<8;p++) {
                // Compute the 2*p bit of c as the p-th bit of i
                c|=(i&(1<<p))<<p;
            }
            byteMP2[i]=c;
        }
    }

    /**
     * Multiplex an integer value 2-fold.
     *
     * @param val the integer
     *
     * @return the integer whose bit in position {@code 2*k} is the bit in position
     *         {@code k} in the argument, and whose bit in position {@code 2*k+1} is 0
     */
    public static short mp2(byte val) {
        return byteMP2[0xFF&val];
    }

    /**
     * Multiplex an integer value 2-fold.
     *
     * @param val the integer
     *
     * @return the integer whose bit in position {@code 2*k} is the bit in position
     *         {@code k} in the argument, and whose bit in position {@code 2*k+1} is 0
     */
    public static int mp2(short val) {
        return (0xFFFF&byteMP2[0xFF&val])|((0xFFFF&byteMP2[0xFF&(val>>>8)])<<16);
    }

    /**
     * Multiplex an integer value 2-fold.
     *
     * @param val the integer
     *
     * @return the integer whose bit in position {@code 2*k} is the bit in position
     *         {@code k} in the argument, and whose bit in position {@code 2*k+1} is 0
     */
    public static long mp2(int val) {
        return (0xFFFFL&byteMP2[0xFF&val])|((0xFFFFL&byteMP2[0xFF&(val>>>8)])<<16)|
               ((0xFFFFL&byteMP2[0xFF&(val>>>16)])<<32)|((0xFFFFL&byteMP2[0xFF&(val>>>24)])<<48);
    }


    /*********************************************************************************
     **  Reversing
     **/

    /**
     * All the reverses for bytes
     */
    private static final byte[] byteREV=new byte[256];

    static {
        for(int i=0;i<256;i++) {
            byte c=0;
            for(int p=0;p<8;p++) {
                // Compute the (8-p-1)th bit of c as the p-th bit of i
                c|=((i>>p)&1)<<(8-p-1);
            }
            byteREV[i]=c;
        }
    }

    /**
     * Reverses the bits of an integer value.
     *
     * @param val the integer
     *
     * @return the N-bits integer whose bit in position {@code N-k} is the bit in
     *         position {@code k} in the argument
     */
    public static byte rev(byte val) {
        return byteREV[val&0xFF];
    }

    /**
     * Reverses the bits of an integer value.
     *
     * @param val the integer
     *
     * @return the N-bits integer whose bit in position {@code N-k} is the bit in
     *         position {@code k} in the argument
     */
    public static short rev(short val) {
        return (short) (((0xFF&byteREV[val&0x00FF])<<8)|((0xFF&byteREV[(val&0xFF00)>>8])));
    }

    /**
     * Reverses the bits of an integer value.
     *
     * @param val the integer
     *
     * @return the N-bits integer whose bit in position {@code N-k} is the bit in
     *         position {@code k} in the argument
     */
    public static int rev(int val) {
//        val = (val & 0x55555555) << 1 | (val >>> 1) & 0x55555555;
//        val = (val & 0x33333333) << 2 | (val >>> 2) & 0x33333333;
//        val = (val & 0x0f0f0f0f) << 4 | (val >>> 4) & 0x0f0f0f0f;
//        val = (val << 24) | ((val & 0xff00) << 8) | ((val >>> 8) & 0xff00) | (val >>> 24);
//        return val;
        return ((0xFF&byteREV[(val&0x000000FF)])<<24)
               |((0xFF&byteREV[(val&0x0000FF00)>>8])<<16)
               |((0xFF&byteREV[(val&0x00FF0000)>>16])<<8)
               |((0xFF&byteREV[(val&0xFF000000)>>24]));
    }

    /**
     * Reverses the bits of an integer value.
     *
     * @param val the integer
     *
     * @return the N-bits integer whose bit in position {@code N-k} is the bit in
     *         position {@code k} in the argument
     */
    public static long rev(long val) {
        val=(val&0x5555555555555555L)<<1|(val>>>1)&0x5555555555555555L;
        val=(val&0x3333333333333333L)<<2|(val>>>2)&0x3333333333333333L;
        val=(val&0x0f0f0f0f0f0f0f0fL)<<4|(val>>>4)&0x0f0f0f0f0f0f0f0fL;
        val=(val&0x00ff00ff00ff00ffL)<<8|(val>>>8)&0x00ff00ff00ff00ffL;
        val=(val<<48)|((val&0xffff0000L)<<16)|((val>>>16)&0xffff0000L)|(val>>>48);
        return val;
//        return ((0xFFL&byteREV[(int) ((val&0x00000000000000FFL))])<<56)
//             | ((0xFFL&byteREV[(int) ((val&0x000000000000FF00L)>>8)])<<48)
//             | ((0xFFL&byteREV[(int) ((val&0x0000000000FF0000L)>>16)])<<40)
//             | ((0xFFL&byteREV[(int) ((val&0x00000000FF000000L)>>24)])<<32)
//             | ((0xFFL&byteREV[(int) ((val&0x000000FF00000000L)>>32)])<<24)
//             | ((0xFFL&byteREV[(int) ((val&0x0000FF0000000000L)>>40)])<<16)
//             | ((0xFFL&byteREV[(int) ((val&0x00FF000000000000L)>>48)])<<8)
//             | ((0xFFL&byteREV[(int) ((val&0xFF00000000000000L)>>56)]));
    }

}
