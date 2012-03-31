/**
 *
 */
package net.varkhan.base.containers.array;

/**
 * <b>Static short arrays manipulation utilities.</b>
 * <p/>
 * Utilities for manipulating arrays of shorts.
 * <p/>
 *
 * @author varkhan
 * @date Nov 15, 2010
 * @time 12:06:41 AM
 */
public class ShortArrays {


    /**
     * Private empty constructor that forbids instantiation of this class.
     */
    protected ShortArrays() {
    }


    /*********************************************************************************
     **  Matching operations
     **/

    /**
     * Indicates whether a value is contained in a short array.
     *
     * @param item  the value to search for
     * @param array the array to search
     *
     * @return {@literal true} if {@code array} contains {@code item}
     */
    public static boolean isMember(short item, short[] array) {
        if(array==null) return false;
        for(short elem : array) if(item==elem) return true;
        return false;
    }

    /**
     * Indicates whether two short arrays are equal.
     *
     * @param array1 the first array
     * @param array2 the second array
     *
     * @return {@code true} iff the arrays are either both {@code null} or
     *         have the same number of elements, and the values of elements in the
     *         same positions are equal
     */
    public static boolean equals(short[] array1, short[] array2) {
        if(array1==null) return array2==null;
        if(array1.length!=array2.length) return false;
        for(int i=0;i<array1.length;i++) {
            if(array1[i]!=array2[i]) return false;
        }
        return true;
    }

    /**
     * Returns the position of the first occurrence of an array as a subsequence
     * of an other array.
     *
     * @param array the array
     * @param sub   the subsequence to find
     *
     * @return the smallest index {@code idx} greater than {@code pos} such that
     *         the elements of {@code array} starting at {@code idx} are exactly those of
     *         {@code sub}  ({@code -1} is returned if {@code sub} is not a subsequence
     *         of {@code array})
     */
    public static int indexOf(short[] array, int pos, short[] sub) {
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
     **  Sorting and search
     **/

    /**
     * Finds an object in a sorted array, in ascending order.
     *
     * @param ary  the sorted array
     * @param inf  the minimum index
     * @param sup  the maximum index
     * @param key  the value to search for
     *
     * @return the key position in the array, or {@code -(inspos+1)},
     *         where {@code inspos} is the index of the first value in the
     *         array bigger than {@code key}
     */
    public static int searchInc(short[] ary, int inf, int sup, short key) {
        int min=inf;
        int max=sup-1;

        while(min<=max) {
            int med=(min+max)>>>1;
            short medVal=ary[med];

            if(medVal<key) min=med+1;
            else if(medVal>key) max=med-1;
            else return med; // key found
        }
        return -(min+1);  // key not found.
    }

    /**
     * Finds an object in a sorted array, in descending order.
     *
     * @param ary  the sorted array
     * @param inf  the minimum index
     * @param sup  the maximum index
     * @param key  the value to search for
     *
     * @return the key position in the array, or {@code -(inspos+1)},
     *         where {@code inspos} is the index of the first value in the
     *         array bigger than {@code key}
     */
    public static int searchDec(short[] ary, int inf, int sup, short key) {
        int min=inf;
        int max=sup-1;

        while(min<=max) {
            int med=(min+max)>>>1;
            short medVal=ary[med];

            if(medVal<key) min=med+1;
            else if(medVal>key) max=med-1;
            else return med; // key found
        }
        return -(min+1);  // key not found.
    }

    /**
     * Computes the union of segments in two sorted arrays, in ascending order.
     *
     * @param ary1 the first sorted array
     * @param beg1 the start position of the first segment
     * @param len1 the length of the first segment
     * @param ary2 the second sorted array
     * @param beg2 the start position of the second segment
     * @param len2 the length of the second segment
     * @return the union of the two segments, with duplicates removed
     */
    public static short[] unionInc(short[] ary1, int beg1, int len1, short[] ary2, int beg2, int len2) {
        int len = len1+len2;
        short[] union = new short[len];
        if(len==0) return union;
        short last = 0;
        len1+=beg1;
        len2+=beg2;
        int beg=0;
        while(beg1<len1 && beg2<len2) {
            short val1 = ary1[beg1];
            short val2 = ary2[beg2];
            if(val1<val2) {
                if(beg==0||last<val1) last = union[beg++] = val1;
                beg1++;
            } else
            if(val1>val2) {
                if(beg==0||last<val2) last = union[beg++] = val2;
                beg2++;
            } else {
                if(beg==0||last<val1) last = union[beg++] = val1;
                beg1++; beg2++;
            }
        }
        while(beg1<len1) {
            short val1 = ary1[beg1];
            if(beg==0||last<val1) last = union[beg++] = val1;
            beg1++;
        }
        while(beg2<len2) {
            short val2 = ary2[beg2];
            if(beg==0||last<val2) last = union[beg++] = val2;
            beg2++;
        }
        if(beg>=len) return union;
        short[] copy = new short[beg];
        System.arraycopy(union, 0, copy, 0, beg);
        return copy;
    }

    /**
     * Computes the union of segments in two sorted arrays, in descending order.
     *
     * @param ary1 the first sorted array
     * @param beg1 the start position of the first segment
     * @param len1 the length of the first segment
     * @param ary2 the second sorted array
     * @param beg2 the start position of the second segment
     * @param len2 the length of the second segment
     * @return the union of the two segments, with duplicates removed
     */
    public static short[] unionDec(short[] ary1, int beg1, int len1, short[] ary2, int beg2, int len2) {
        int len = len1+len2;
        short[] union = new short[len];
        if(len==0) return union;
        short last = 0;
        len1+=beg1;
        len2+=beg2;
        int beg=0;
        while(beg1<len1 && beg2<len2) {
            short val1 = ary1[beg1];
            short val2 = ary2[beg2];
            if(val1>val2) {
                if(beg==0||last>val1) last = union[beg++] = val1;
                beg1++;
            } else
            if(val1<val2) {
                if(beg==0||last>val1) last = union[beg++] = val2;
                beg2++;
            } else {
                if(beg==0||last>val1) last = union[beg++] = val1;
                beg1++; beg2++;
            }
        }
        while(beg1<len1) {
            short val1 = ary1[beg1];
            if(beg==0||last>val1) last = union[beg++] = val1;
            beg1++;
        }
        while(beg2<len2) {
            short val2 = ary2[beg2];
            if(beg==0||last>val2) last = union[beg++] = val2;
            beg2++;
        }
        if(beg>=len) return union;
        short[] copy = new short[beg];
        System.arraycopy(union, 0, copy, 0, beg);
        return copy;
    }

    /**
     * Computes the intersection of segments in two sorted arrays, in ascending order.
     *
     * @param ary1 the first sorted array
     * @param beg1 the start position of the first segment
     * @param len1 the length of the first segment
     * @param ary2 the second sorted array
     * @param beg2 the start position of the second segment
     * @param len2 the length of the second segment
     * @return the intersection of the two segments, with duplicates removed
     */
    public static short[] interInc(short[] ary1, int beg1, int len1, short[] ary2, int beg2, int len2) {
        int len = (len1>len2)?len1:len2;
        short[] inter = new short[len];
        if(len==0) return inter;
        short last = 0;
        len1+=beg1;
        len2+=beg2;
        int beg=0;
        while(beg1<len1 && beg2<len2) {
            short val1 = ary1[beg1];
            short val2 = ary2[beg2];
            if(val1<val2) { beg1++; } else
            if(val1>val2) { beg2++; } else
            { if(beg==0||last<val1) last = inter[beg++] = val1; beg1++; beg2++; }
        }
        if(beg>=len) return inter;
        short[] copy = new short[beg];
        System.arraycopy(inter, 0, copy, 0, beg);
        return copy;
    }

    /**
     * Computes the intersection of segments in two sorted arrays, in descending order.
     *
     * @param ary1 the first sorted array
     * @param beg1 the start position of the first segment
     * @param len1 the length of the first segment
     * @param ary2 the second sorted array
     * @param beg2 the start position of the second segment
     * @param len2 the length of the second segment
     * @return the intersection of the two segments, with duplicates removed
     */
    public static short[] interDec(short[] ary1, int beg1, int len1, short[] ary2, int beg2, int len2) {
        int len = (len1>len2)?len1:len2;
        short[] inter = new short[len];
        if(len==0) return inter;
        short last = 0;
        len1+=beg1;
        len2+=beg2;
        int beg=0;
        while(beg1<len1 && beg2<len2) {
            short val1 = ary1[beg1];
            short val2 = ary2[beg2];
            if(val1>val2) { beg1++; } else
            if(val1<val2) { beg2++; } else
            { if(beg==0||last>val1) last = inter[beg++] = val1; beg1++; beg2++; }
        }
        if(beg>=len) return inter;
        short[] copy = new short[beg];
        System.arraycopy(inter, 0, copy, 0, beg);
        return copy;
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
     * @return an array of shorts containing all the elements of {@code array} followed by all the elements in {@code elems}
     */
    public static short[] append(short[] array, short... elems) {
        short[] concat=new short[array.length+elems.length];
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
     * @return an array of shorts containing all the elements in {@code elems} followed by all the elements of {@code array}
     */
    public static short[] prepend(short[] array, short... elems) {
        short[] concat=new short[array.length+elems.length];
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
     * @return an array of shorts containing all the elements in the arrays, in order
     */
    public static short[] concat(short[] array, short[]... arrays) {
        int l=array.length;
        for(short[] t : arrays) { l+=t.length; }
        short[] concat=new short[l];
        System.arraycopy(array, 0, concat, 0, array.length);
        l=array.length;
        for(short[] t : arrays) {
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
     * @return an array of shorts, of length {@code end-beg}, and containing all the
     * elements between positions {@code beg}, inclusive, and {@code end}, exclusive,
     * of the original array
     */
    public static short[] subarray(short[] array, int beg, int end) {
        int l=array.length;
        if(beg<0 || beg>end || end>l) throw new IndexOutOfBoundsException();
        short[] subary=new short[end-beg];
        if(end>beg) System.arraycopy(array, beg, subary, 0, end-beg);
        return subary;
    }

}
