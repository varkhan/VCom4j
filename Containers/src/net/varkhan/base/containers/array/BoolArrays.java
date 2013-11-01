/**
 *
 */
package net.varkhan.base.containers.array;

/**
 * <b>Static boolean arrays manipulation utilities.</b>
 * <p/>
 * Utilities for manipulating arrays of booleans.
 * <p/>
 *
 * @author varkhan
 * @date Nov 15, 2010
 * @time 12:08:48 AM
 */
public class BoolArrays {

    /**
     * Private empty constructor that forbids instantiation of this class.
     */
    protected BoolArrays() {
    }


    /*********************************************************************************
     **  Matching operations
     **/

    /**
     * Indicates whether two boolean arrays are equal.
     *
     * @param array1 the first array
     * @param array2 the second array
     *
     * @return {@code true} iff the arrays are either both {@code null} or
     *         have the same number of elements, and the values of elements in the
     *         same positions are equal
     */
    public static boolean equals(boolean[] array1, boolean[] array2) {
        if(array1==null) return array2==null;
        if(array1.length!=array2.length) return false;
        for(int i=0;i<array1.length;i++) {
            if(array1[i]!=array2[i]) return false;
        }
        return true;
    }

    /**
     * Returns the position of the first occurrence of a value inside an array.
     *
     * @param item  the value to search for
     * @param array the array to search
     *
     * @return {@literal -1} if {@code item} was not found in {@code array},
     * the position of the first element of the array equal to {@code item}
     */
    public static int indexOf(boolean item, boolean... array) {
        if(array==null) return -1;
        final int length = array.length;
        for(int i=0;i<length;i++) if(item==array[i]) return i;
        return -1;
    }

    /**
     * Returns the position of the first occurrence of an array as a subsequence
     * of an other array.
     *
     * @param array the array
     * @param pos   the starting point in the array
     * @param sub   the subsequence to find
     *
     * @return the smallest index {@code idx} greater than {@code pos} such that
     *         the elements of {@code array} starting at {@code idx} are exactly those of
     *         {@code sub}  ({@code -1} is returned if {@code sub} is not a subsequence
     *         of {@code array})
     */
    public static int indexOf(boolean[] array, int pos, boolean[] sub) {
        if(array==null) return sub==null ? 0 : -1;
        match:
        while(pos<array.length) {
            for(int i=0;i<sub.length;i++) {
                if(pos+i>array.length) return -1;
                if(array[pos+i]!=sub[i]) {
                    pos++;
                    continue match;
                }
            }
            return pos;
        }
        return -1;
    }


    /*********************************************************************************
     **  Copy and concatenation
     **/

    /**
     * Append elements to an array.
     *
     * @param array the array to append to
     * @param elems the elements to append
     *
     * @return an array of booleans containing all the elements of {@code array} followed by all the elements in {@code elems}
     */
    public static boolean[] append(boolean[] array, boolean... elems) {
        boolean[] concat=new boolean[array.length+elems.length];
        System.arraycopy(array, 0, concat, 0, array.length);
        System.arraycopy(elems, 0, concat, array.length, elems.length);
        return concat;
    }

    /**
     * Prepend elements to an array.
     *
     * @param array the array to prepend to
     * @param elems the elements to prepend
     *
     * @return an array of booleans containing all the elements in {@code elems} followed by all the elements of {@code array}
     */
    public static boolean[] prepend(boolean[] array, boolean... elems) {
        boolean[] concat=new boolean[array.length+elems.length];
        System.arraycopy(elems, 0, concat, 0, elems.length);
        System.arraycopy(array, 0, concat, elems.length, array.length);
        return concat;
    }

    /**
     * Concatenates several arrays.
     *
     * @param array  the first array to append to
     * @param arrays the arrays to append
     *
     * @return an array of booleans containing all the elements in the arrays, in order
     */
    public static boolean[] concat(boolean[] array, boolean[]... arrays) {
        int l=array.length;
        for(boolean[] t : arrays) { l+=t.length; }
        boolean[] concat=new boolean[l];
        System.arraycopy(array, 0, concat, 0, array.length);
        l=array.length;
        for(boolean[] t : arrays) {
            System.arraycopy(t, 0, concat, l, t.length);
            l+=t.length;
        }
        return concat;
    }

    /**
     * Returns a segment of an array.
     *
     * @param array the source array
     * @param beg   the begining position of the segment, inclusive
     * @param end   the ending position of the segment, exclusive
     *
     * @return an array of booleans, of length {@code end-beg}, and containing all the
     * elements between positions {@code beg}, inclusive, and {@code end}, exclusive,
     * of the original array
     */
    public static boolean[] subarray(boolean[] array, int beg, int end) {
        int l=array.length;
        if(beg<0 || beg>end || end>l) throw new ArrayIndexOutOfBoundsException("["+beg+":"+end+"] is not a valid range specifier");
        boolean[] subary=new boolean[end-beg];
        if(end>beg) System.arraycopy(array, beg, subary, 0, end-beg);
        return subary;
    }

}
