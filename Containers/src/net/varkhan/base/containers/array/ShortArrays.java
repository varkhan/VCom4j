/**
 *
 */
package net.varkhan.base.containers.array;

import java.io.IOException;


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
     * Returns the position of the first occurrence of a value inside an array.
     *
     * @param item  the value to search for
     * @param array the array to search
     *
     * @return {@literal -1} if {@code item} was not found in {@code array},
     * the position of the first element of the array equal to {@code item}
     */
    public static int indexOf(short item, short... array) {
        if(array==null) return -1;
        final int length = array.length;
        for(int i=0;i<length;i++) if(item==array[i]) return i;
        return -1;
    }

    /**
     * Returns the position of the first occurrence of an array as a subsequence
     * of an other array.
     *
     * @param sub   the subsequence to find
     * @param pos   the starting point in the array
     * @param array the array
     *
     * @return the smallest index {@code idx} greater than {@code pos} such that
     *         the elements of {@code array} starting at {@code idx} are exactly those of
     *         {@code sub}  ({@code -1} is returned if {@code sub} is not a subsequence
     *         of {@code array})
     */
    public static int indexOf(short[] sub, int pos, short[] array) {
        if(array==null) return sub==null ? 0 : -1;
        match:
        while(pos<array.length) {
            for(int i=0;i<sub.length;i++) {
                if(pos+i>=array.length) return -1;
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
     * Sorts an array in place, in ascending order.
     *
     * @param ary the array to sort
     * @return the number of swap operations required for the sorting
     */
    public static int heapSortInc(short... ary) {
        return heapSortInc(ary,0,ary.length-1);
    }

    /**
     * Sorts an array in place, in descending order.
     *
     * @param ary the array to sort
     * @return the number of swap operations required for the sorting
     */
    public static int heapSortDec(short... ary) {
        return heapSortDec(ary,0,ary.length-1);
    }

    /**
     * Sorts an array segment in place, in ascending order.
     *
     * @param ary the array to sort
     * @param inf the minimum index
     * @param sup the maximum index
     * @return the number of swap operations required for the sorting
     */
    public static int heapSortInc(short[] ary, int inf, int sup) {
        int cnt = 0;
        int beg = ((inf+sup)>>1)+1; // inf + (sup-inf+1)/2 - 1 = (sup+inf)/2+1
        while(beg>inf) {
            beg --;
            cnt += heapDownInc(ary,beg,sup);
        }
        int end = sup;
        while(end>=inf) {
            short v = ary[end];
            ary[end] = ary[inf];
            ary[inf] = v;
            end --;
            cnt += 1 + heapDownInc(ary,inf,end);
        }
        return cnt;
    }

    /**
     * Sorts an array segment in place, in descending order.
     *
     * @param ary the array to sort
     * @param inf the minimum index
     * @param sup the maximum index
     * @return the number of swap operations required for the sorting
     */
    public static int heapSortDec(short[] ary, int inf, int sup) {
        int cnt = 0;
        int beg = ((inf+sup)>>1)+1; // inf + (sup-inf+1)/2 - 1 = (sup+inf)/2+1
        while(beg>inf) {
            beg --;
            cnt += heapDownDec(ary,beg,sup);
        }
        int end = sup;
        while(end>=inf) {
            short v = ary[end];
            ary[end] = ary[inf];
            ary[inf] = v;
            end --;
            cnt += 1 + heapDownDec(ary,inf,end);
        }
        return cnt;
    }

    protected static int heapDownInc(short[] ary, int inf, int sup) {
        int cnt = 0;
        int pos = inf;
        int cld = (pos<<1)+1;
        while(cld<=sup) {
            int swp = pos;
            if(ary[swp]<ary[cld]) swp = cld;
            cld ++;
            if(cld<=sup) {
                if(ary[swp]<ary[cld]) swp = cld;
            }
            if(swp==pos) return cnt;
            short v = ary[pos];
            ary[pos] = ary[swp];
            ary[swp] = v;
            cnt ++;
            pos = swp;
            cld = (pos<<1)+1;
        }
        return cnt;
    }

    protected static int heapDownDec(short[] ary, int inf, int sup) {
        int cnt = 0;
        int pos = inf;
        int cld = (pos<<1)+1;
        while(cld<=sup) {
            int swp = pos;
            if(ary[swp]>ary[cld]) swp = cld;
            cld ++;
            if(cld<=sup) {
                if(ary[swp]>ary[cld]) swp = cld;
            }
            if(swp==pos) return cnt;
            short v = ary[pos];
            ary[pos] = ary[swp];
            ary[swp] = v;
            cnt ++;
            pos = swp;
            cld = (pos<<1)+1;
        }
        return cnt;
    }

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
        if(beg<0 || beg>end || end>l) throw new ArrayIndexOutOfBoundsException("["+beg+":"+end+"] is not a valid range specifier");
        short[] subary=new short[end-beg];
        if(end>beg) System.arraycopy(array, beg, subary, 0, end-beg);
        return subary;
    }


    /*********************************************************************************
     **  String transformation
     **/

    /**
     * Builds a pretty string representation of a short array.
     *
     * @param buf   the buffer to append the composed string to
     * @param array the short array to stringify
     *
     * @return the original buffer, for chaining purposes
     *
     * @throws java.io.IOException if the output buffer raises this exception on {@code append()}
     */
    public static <A extends Appendable> A toString(A buf, short[] array) throws IOException {
        buf.append("[").append(Integer.toString(array.length)).append("|");
        for(int i=0;i<array.length;i++) {
            if(i>0) buf.append(",");
            buf.append(Integer.toString(array[i]));
        }
        buf.append("]");
        return buf;
    }

    /**
     * Builds a pretty string representation of a short array.
     *
     * @param buf   the buffer to append the composed string to
     * @param array the short array to stringify
     *
     * @return the original buffer, for chaining purposes
     */
    public static StringBuilder toString(StringBuilder buf, short[] array) {
        buf.append("[").append(array.length).append("|");
        for(int i=0;i<array.length;i++) {
            if(i>0) buf.append(",");
            buf.append(Integer.toString(array[i]));
        }
        buf.append("]");
        return buf;
    }

    /**
     * Returns a pretty string representation of a short array.
     *
     * @param array the short array to stringify
     *
     * @return a human-readable string exposing the contents of the array
     */
    public static String toString(short[] array) {
        return toString(new StringBuilder(), array).toString();
    }

    /**
     * Appends as strings the elements of a short array, separating them with a given string.
     *
     * @param buf   the buffer to append the composed string to
     * @param sep   the separator to use
     * @param array the short array to concatenate
     *
     * @return the original buffer, for chaining purposes
     *
     * @throws IOException if the output buffer raises this exception on {@code append()}
     */
    public static <A extends Appendable> A join(A buf, String sep, short[] array) throws IOException {
        if(sep==null) for(int i=0;i<array.length;i++) {
            buf.append(Integer.toString(array[i]));
        }
        else for(int i=0;i<array.length;i++) {
            if(i>0) buf.append(sep);
            buf.append(Integer.toString(array[i]));
        }
        return buf;
    }

    /**
     * Appends as strings the elements of a short array, separating them with a given string.
     *
     * @param buf   the buffer to append the composed string to
     * @param sep   the separator to use
     * @param array the short array to concatenate
     *
     * @return the original buffer, for chaining purposes
     */
    public static StringBuilder join(StringBuilder buf, String sep, short[] array) {
        if(sep==null) for(int i=0;i<array.length;i++) {
            buf.append(Integer.toString(array[i]));
        }
        else for(int i=0;i<array.length;i++) {
            if(i>0) buf.append(sep);
            buf.append(Integer.toString(array[i]));
        }
        return buf;
    }

    /**
     * Appends as strings the elements of a short array, separating them with a given string.
     *
     * @param sep   the separator to use
     * @param array the short array to concatenate
     *
     * @return a concatenation of the elements of the array, as string, and the separator
     */
    public static String join(String sep, short[] array) {
        return join(new StringBuilder(), sep, array).toString();
    }

}
