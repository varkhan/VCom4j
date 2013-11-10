/**
 *
 */
package net.varkhan.base.containers.array;

import net.varkhan.base.containers.Visitable;
import net.varkhan.base.containers.type.LongContainer;

import java.io.IOException;
import java.util.NoSuchElementException;


/**
 * <b>Static long arrays manipulation utilities.</b>
 * <p/>
 * Utilities for manipulating arrays of longs.
 * <p/>
 *
 * @author varkhan
 * @date Nov 15, 2010
 * @time 12:07:00 AM
 */
public class LongArrays {

    /**
     * Private empty constructor that forbids instantiation of this class.
     */
    protected LongArrays() {
    }


    /*********************************************************************************
     **  Matching operations
     **/

    /**
     * Indicates whether two long arrays are equal.
     *
     * @param array1 the first array
     * @param array2 the second array
     *
     * @return {@code true} iff the arrays are either both {@code null} or
     *         have the same number of elements, and the values of elements in the
     *         same positions are equal
     */
    public static boolean equals(long[] array1, long[] array2) {
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
    public static int indexOf(long item, long... array) {
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
    public static int indexOf(long[] sub, int pos, long[] array) {
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
    public static int heapSortInc(long... ary) {
        return heapSortInc(ary,0,ary.length-1);
    }

    /**
     * Sorts an array in place, in descending order.
     *
     * @param ary the array to sort
     * @return the number of swap operations required for the sorting
     */
    public static int heapSortDec(long... ary) {
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
    public static int heapSortInc(long[] ary, int inf, int sup) {
        int cnt = 0;
        int beg = ((inf+sup)>>1)+1; // inf + (sup-inf+1)/2 - 1 = (sup+inf)/2+1
        while(beg>inf) {
            beg --;
            cnt += heapDownInc(ary,beg,sup);
        }
        int end = sup;
        while(end>=inf) {
            long v = ary[end];
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
    public static int heapSortDec(long[] ary, int inf, int sup) {
        int cnt = 0;
        int beg = ((inf+sup)>>1)+1; // inf + (sup-inf+1)/2 - 1 = (sup+inf)/2+1
        while(beg>inf) {
            beg --;
            cnt += heapDownDec(ary,beg,sup);
        }
        int end = sup;
        while(end>=inf) {
            long v = ary[end];
            ary[end] = ary[inf];
            ary[inf] = v;
            end --;
            cnt += 1 + heapDownDec(ary,inf,end);
        }
        return cnt;
    }

    protected static int heapDownInc(long[] ary, int inf, int sup) {
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
            long v = ary[pos];
            ary[pos] = ary[swp];
            ary[swp] = v;
            cnt ++;
            pos = swp;
            cld = (pos<<1)+1;
        }
        return cnt;
    }

    protected static int heapDownDec(long[] ary, int inf, int sup) {
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
            long v = ary[pos];
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
    public static int searchInc(long[] ary, int inf, int sup, long key) {
        int min=inf;
        int max=sup-1;

        while(min<=max) {
            int med=(min+max)>>>1;
            long medVal=ary[med];

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
    public static int searchDec(long[] ary, int inf, int sup, long key) {
        int min=inf;
        int max=sup-1;

        while(min<=max) {
            int med=(min+max)>>>1;
            long medVal=ary[med];

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
    public static long[] unionInc(long[] ary1, int beg1, int len1, long[] ary2, int beg2, int len2) {
        int len = len1+len2;
        long[] union = new long[len];
        if(len==0) return union;
        long last = 0;
        len1+=beg1;
        len2+=beg2;
        int beg=0;
        while(beg1<len1 && beg2<len2) {
            long val1 = ary1[beg1];
            long val2 = ary2[beg2];
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
            long val1 = ary1[beg1];
            if(beg==0||last<val1) last = union[beg++] = val1;
            beg1++;
        }
        while(beg2<len2) {
            long val2 = ary2[beg2];
            if(beg==0||last<val2) last = union[beg++] = val2;
            beg2++;
        }
        if(beg>=len) return union;
        long[] copy = new long[beg];
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
    public static long[] unionDec(long[] ary1, int beg1, int len1, long[] ary2, int beg2, int len2) {
        int len = len1+len2;
        long[] union = new long[len];
        if(len==0) return union;
        long last = 0;
        len1+=beg1;
        len2+=beg2;
        int beg=0;
        while(beg1<len1 && beg2<len2) {
            long val1 = ary1[beg1];
            long val2 = ary2[beg2];
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
            long val1 = ary1[beg1];
            if(beg==0||last>val1) last = union[beg++] = val1;
            beg1++;
        }
        while(beg2<len2) {
            long val2 = ary2[beg2];
            if(beg==0||last>val2) last = union[beg++] = val2;
            beg2++;
        }
        if(beg>=len) return union;
        long[] copy = new long[beg];
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
    public static long[] interInc(long[] ary1, int beg1, int len1, long[] ary2, int beg2, int len2) {
        int len = (len1>len2)?len1:len2;
        long[] inter = new long[len];
        if(len==0) return inter;
        long last = 0;
        len1+=beg1;
        len2+=beg2;
        int beg=0;
        while(beg1<len1 && beg2<len2) {
            long val1 = ary1[beg1];
            long val2 = ary2[beg2];
            if(val1<val2) { beg1++; } else
            if(val1>val2) { beg2++; } else
            { if(beg==0||last<val1) last = inter[beg++] = val1; beg1++; beg2++; }
        }
        if(beg>=len) return inter;
        long[] copy = new long[beg];
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
    public static long[] interDec(long[] ary1, int beg1, int len1, long[] ary2, int beg2, int len2) {
        int len = (len1>len2)?len1:len2;
        long[] inter = new long[len];
        if(len==0) return inter;
        long last = 0;
        len1+=beg1;
        len2+=beg2;
        int beg=0;
        while(beg1<len1 && beg2<len2) {
            long val1 = ary1[beg1];
            long val2 = ary2[beg2];
            if(val1>val2) { beg1++; } else
            if(val1<val2) { beg2++; } else
            { if(beg==0||last>val1) last = inter[beg++] = val1; beg1++; beg2++; }
        }
        if(beg>=len) return inter;
        long[] copy = new long[beg];
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
     * @return an array of longs containing all the elements of {@code array} followed by all the elements in {@code elems}
     */
    public static long[] append(long[] array, long... elems) {
        long[] concat=new long[array.length+elems.length];
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
     * @return an array of longs containing all the elements in {@code elems} followed by all the elements of {@code array}
     */
    public static long[] prepend(long[] array, long... elems) {
        long[] concat=new long[array.length+elems.length];
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
     * @return an array of longs containing all the elements in the arrays, in order
     */
    public static long[] concat(long[] array, long[]... arrays) {
        int l=array.length;
        for(long[] t : arrays) { l+=t.length; }
        long[] concat=new long[l];
        System.arraycopy(array, 0, concat, 0, array.length);
        l=array.length;
        for(long[] t : arrays) {
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
     * @return an array of longs, of length {@code end-beg}, and containing all the
     * elements between positions {@code beg}, inclusive, and {@code end}, exclusive,
     * of the original array
     */
    public static long[] subarray(long[] array, int beg, int end) {
        int l=array.length;
        if(beg<0 || beg>end || end>l) throw new ArrayIndexOutOfBoundsException("["+beg+":"+end+"] is not a valid range specifier");
        long[] subary=new long[end-beg];
        if(end>beg) System.arraycopy(array, beg, subary, 0, end-beg);
        return subary;
    }


    /*********************************************************************************
     **  Container wrapping
     **/

    /**
     * Returns an immutable container backed by an array.
     *
     * @param values the array of values for the container
     * @return a container holding the elements of the array, in order
     */
    public static LongContainer container(final long... values) {
        return new LongContainer() {
            public long size() { return values==null?0:values.length; }
            public boolean isEmpty() { return values==null||values.length==0; }
            public LongIterator iterator() {
                return new LongIterator() {
                    private volatile int pos = 0;
                    public boolean hasNext() { return pos<values.length; }
                    public Long next() { return nextValue(); }
                    public long nextValue() {
                        if(pos>=values.length) throw new NoSuchElementException();
                        return values[pos++];
                    }
                    public void remove() { throw new UnsupportedOperationException(); }
                };

            }
            public <Par> long visit(LongVisitor<Par> vis, Par par) {
                long ret = 0;
                if(values!=null) for(long val: values) {
                    long r = vis.invoke(val, par);
                    if(r<0) return ret;
                    ret += r;
                }
                return ret;
            }
            public <Par> long visit(Visitable.Visitor<Long,Par> vis, Par par) {
                long ret = 0;
                if(values!=null) for(long val: values) {
                    long r = vis.invoke(val, par);
                    if(r<0) return ret;
                    ret += r;
                }
                return ret;
            }
        };
    }


    /*********************************************************************************
     **  String transformation
     **/

    /**
     * Builds a pretty string representation of a long array.
     *
     * @param buf   the buffer to append the composed string to
     * @param array the long array to stringify
     *
     * @return the original buffer, for chaining purposes
     *
     * @throws java.io.IOException if the output buffer raises this exception on {@code append()}
     */
    public static <A extends Appendable> A toString(A buf, long[] array) throws IOException {
        buf.append("[").append(Integer.toString(array.length)).append("|");
        for(int i=0;i<array.length;i++) {
            if(i>0) buf.append(",");
            buf.append(Long.toString(array[i]));
        }
        buf.append("]");
        return buf;
    }

    /**
     * Builds a pretty string representation of a long array.
     *
     * @param buf   the buffer to compose the string into
     * @param array the long array to stringify
     *
     * @return the original buffer, for chaining purposes
     */
    public static StringBuilder toString(StringBuilder buf, long[] array) {
        buf.append("[").append(array.length).append("|");
        for(int i=0;i<array.length;i++) {
            if(i>0) buf.append(",");
            buf.append(Long.toString(array[i]));
        }
        buf.append("]");
        return buf;
    }

    /**
     * Returns a pretty string representation of a long array.
     *
     * @param array the long array to stringify
     *
     * @return a human-readable string exposing the contents of the array
     */
    public static String toString(long[] array) {
        return toString(new StringBuilder(), array).toString();
    }

    /**
     * Appends as strings the elements of a long array, separating them with a given string.
     *
     * @param buf   the buffer to append the composed string to
     * @param sep   the separator to use
     * @param array the long array to concatenate
     *
     * @return the original buffer, for chaining purposes
     *
     * @throws IOException if the output buffer raises this exception on {@code append()}
     */
    public static <A extends Appendable> A join(A buf, String sep, long[] array) throws IOException {
        if(sep==null) for(int i=0;i<array.length;i++) {
            buf.append(Long.toString(array[i]));
        }
        else for(int i=0;i<array.length;i++) {
            if(i>0) buf.append(sep);
            buf.append(Long.toString(array[i]));
        }
        return buf;
    }

    /**
     * Appends as strings the elements of a long array, separating them with a given string.
     *
     * @param buf   the buffer to append the composed string to
     * @param sep   the separator to use
     * @param array the long array to concatenate
     *
     * @return the original buffer, for chaining purposes
     */
    public static StringBuilder join(StringBuilder buf, String sep, long[] array) {
        if(sep==null) for(int i=0;i<array.length;i++) {
            buf.append(Long.toString(array[i]));
        }
        else for(int i=0;i<array.length;i++) {
            if(i>0) buf.append(sep);
            buf.append(Long.toString(array[i]));
        }
        return buf;
    }

    /**
     * Appends as strings the elements of a long array, separating them with a given string.
     *
     * @param sep   the separator to use
     * @param array the long array to concatenate
     *
     * @return a concatenation of the elements of the array, as string, and the separator
     */
    public static String join(String sep, long[] array) {
        return join(new StringBuilder(), sep, array).toString();
    }

}
