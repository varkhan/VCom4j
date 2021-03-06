/**
 *
 */
package net.varkhan.base.containers.array;

import net.varkhan.base.containers.Visitable;
import net.varkhan.base.containers.type.IntContainer;

import java.io.IOException;
import java.util.NoSuchElementException;


/**
 * <b>Static int arrays manipulation utilities.</b>
 * <p/>
 * Utilities for manipulating arrays of ints.
 * <p/>
 *
 * @author varkhan
 * @date Nov 15, 2010
 * @time 12:06:52 AM
 */
public class IntArrays {

    /**
     * Private empty constructor that forbids instantiation of this class.
     */
    protected IntArrays() {
    }


    /*********************************************************************************
     **  Matching operations
     **/

    /**
     * Indicates whether two int arrays are equal.
     *
     * @param array1 the first array
     * @param array2 the second array
     *
     * @return {@code true} iff the arrays are either both {@code null} or
     *         have the same number of elements, and the values of elements in the
     *         same positions are equal
     */
    public static boolean equals(int[] array1, int[] array2) {
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
    public static int indexOf(int item, int... array) {
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
    public static int indexOf(int[] sub, int pos, int[] array) {
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
    public static int sortInc(int... ary) {
        return sortInc(ary, 0, ary.length-1);
    }

    /**
     * Sorts an array in place, in descending order.
     *
     * @param ary the array to sort
     * @return the number of swap operations required for the sorting
     */
    public static int sortDec(int... ary) {
        return sortDec(ary, 0, ary.length-1);
    }

    /**
     * Sorts an array segment in place, in ascending order.
     *
     * @param ary the array to sort
     * @param inf the minimum index
     * @param sup the maximum index
     * @return the number of swap operations required for the sorting
     */
    public static int sortInc(int[] ary, int inf, int sup) {
        int cnt = 0;
        int beg = ((inf+sup)>>1)+1; // inf + (sup-inf+1)/2 - 1 = (sup+inf)/2+1
        while(beg>inf) {
            beg --;
            cnt += heapDownInc(ary,beg,sup);
        }
        int end = sup;
        while(end>=inf) {
            int v = ary[end];
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
    public static int sortDec(int[] ary, int inf, int sup) {
        int cnt = 0;
        int beg = ((inf+sup)>>1)+1; // inf + (sup-inf+1)/2 - 1 = (sup+inf)/2+1
        while(beg>inf) {
            beg --;
            cnt += heapDownDec(ary,beg,sup);
        }
        int end = sup;
        while(end>=inf) {
            int v = ary[end];
            ary[end] = ary[inf];
            ary[inf] = v;
            end --;
            cnt += 1 + heapDownDec(ary,inf,end);
        }
        return cnt;
    }

    protected static int heapDownInc(int[] ary, int inf, int sup) {
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
            int v = ary[pos];
            ary[pos] = ary[swp];
            ary[swp] = v;
            cnt ++;
            pos = swp;
            cld = (pos<<1)+1;
        }
        return cnt;
    }

    protected static int heapDownDec(int[] ary, int inf, int sup) {
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
            int v = ary[pos];
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
    public static int searchInc(int[] ary, int inf, int sup, int key) {
        int min=inf;
        int max=sup-1;

        while(min<=max) {
            int med=(min+max)>>>1;
            int medVal=ary[med];

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
    public static int searchDec(int[] ary, int inf, int sup, int key) {
        int min=inf;
        int max=sup-1;

        while(min<=max) {
            int med=(min+max)>>>1;
            int medVal=ary[med];

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
    public static int[] unionInc(int[] ary1, int beg1, int len1, int[] ary2, int beg2, int len2) {
        int len = len1+len2;
        int[] union = new int[len];
        if(len==0) return union;
        int last = 0;
        len1+=beg1;
        len2+=beg2;
        int beg=0;
        while(beg1<len1 && beg2<len2) {
            int val1 = ary1[beg1];
            int val2 = ary2[beg2];
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
            int val1 = ary1[beg1];
            if(beg==0||last<val1) last = union[beg++] = val1;
            beg1++;
        }
        while(beg2<len2) {
            int val2 = ary2[beg2];
            if(beg==0||last<val2) last = union[beg++] = val2;
            beg2++;
        }
        if(beg>=len) return union;
        int[] copy = new int[beg];
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
    public static int[] unionDec(int[] ary1, int beg1, int len1, int[] ary2, int beg2, int len2) {
        int len = len1+len2;
        int[] union = new int[len];
        if(len==0) return union;
        int last = 0;
        len1+=beg1;
        len2+=beg2;
        int beg=0;
        while(beg1<len1 && beg2<len2) {
            int val1 = ary1[beg1];
            int val2 = ary2[beg2];
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
            int val1 = ary1[beg1];
            if(beg==0||last>val1) last = union[beg++] = val1;
            beg1++;
        }
        while(beg2<len2) {
            int val2 = ary2[beg2];
            if(beg==0||last>val2) last = union[beg++] = val2;
            beg2++;
        }
        if(beg>=len) return union;
        int[] copy = new int[beg];
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
    public static int[] interInc(int[] ary1, int beg1, int len1, int[] ary2, int beg2, int len2) {
        int len = (len1>len2)?len1:len2;
        int[] inter = new int[len];
        if(len==0) return inter;
        int last = 0;
        len1+=beg1;
        len2+=beg2;
        int beg=0;
        while(beg1<len1 && beg2<len2) {
            int val1 = ary1[beg1];
            int val2 = ary2[beg2];
            if(val1<val2) { beg1++; } else
            if(val1>val2) { beg2++; } else
            { if(beg==0||last<val1) last = inter[beg++] = val1; beg1++; beg2++; }
        }
        if(beg>=len) return inter;
        int[] copy = new int[beg];
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
    public static int[] interDec(int[] ary1, int beg1, int len1, int[] ary2, int beg2, int len2) {
        int len = (len1>len2)?len1:len2;
        int[] inter = new int[len];
        if(len==0) return inter;
        int last = 0;
        len1+=beg1;
        len2+=beg2;
        int beg=0;
        while(beg1<len1 && beg2<len2) {
            int val1 = ary1[beg1];
            int val2 = ary2[beg2];
            if(val1>val2) { beg1++; } else
            if(val1<val2) { beg2++; } else
            { if(beg==0||last>val1) last = inter[beg++] = val1; beg1++; beg2++; }
        }
        if(beg>=len) return inter;
        int[] copy = new int[beg];
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
     * @return an array of ints containing all the elements of {@code array} followed by all the elements in {@code elems}
     */
    public static int[] append(int[] array, int... elems) {
        int[] concat=new int[array.length+elems.length];
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
     * @return an array of ints containing all the elements in {@code elems} followed by all the elements of {@code array}
     */
    public static int[] prepend(int[] array, int... elems) {
        int[] concat=new int[array.length+elems.length];
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
     * @return an array of ints containing all the elements in the arrays, in order
     */
    public static int[] concat(int[] array, int[]... arrays) {
        int l=array.length;
        for(int[] t : arrays) { l+=t.length; }
        int[] concat=new int[l];
        System.arraycopy(array, 0, concat, 0, array.length);
        l=array.length;
        for(int[] t : arrays) {
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
     * @return an array of ints, of length {@code end-beg}, and containing all the
     * elements between positions {@code beg}, inclusive, and {@code end}, exclusive,
     * of the original array
     */
    public static int[] subarray(int[] array, int beg, int end) {
        int l=array.length;
        if(beg<0 || beg>end || end>l) throw new ArrayIndexOutOfBoundsException("["+beg+":"+end+"] is not a valid range specifier");
        int[] subary=new int[end-beg];
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
    public static IntContainer container(final int... values) {
        return new IntContainer() {
            public long size() { return values==null?0:values.length; }
            public boolean isEmpty() { return values==null||values.length==0; }
            public IntIterator iterator() {
                return new IntIterator() {
                    private volatile int pos = 0;
                    public boolean hasNext() { return pos<values.length; }
                    public Integer next() { return nextValue(); }
                    public int nextValue() {
                        if(pos>=values.length) throw new NoSuchElementException();
                        return values[pos++];
                    }
                    public void remove() { throw new UnsupportedOperationException(); }
                };

            }
            public <Par> long visit(IntVisitor<Par> vis, Par par) {
                long ret = 0;
                if(values!=null) for(int val: values) {
                    long r = vis.invoke(val, par);
                    if(r<0) return ret;
                    ret += r;
                }
                return ret;
            }
            public <Par> long visit(Visitable.Visitor<Integer,Par> vis, Par par) {
                long ret = 0;
                if(values!=null) for(int val: values) {
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
     * Builds a pretty string representation of an int array.
     *
     * @param buf   the buffer to append the composed string to
     * @param array the int array to stringify
     *
     * @return the original buffer, for chaining purposes
     *
     * @throws java.io.IOException if the output buffer raises this exception on {@code append()}
     */
    public static <A extends Appendable> A toString(A buf, int[] array) throws IOException {
        buf.append("[").append(Integer.toString(array.length)).append("|");
        for(int i=0;i<array.length;i++) {
            if(i>0) buf.append(",");
            buf.append(Integer.toString(array[i]));
        }
        buf.append("]");
        return buf;
    }

    /**
     * Builds a pretty string representation of an int array.
     *
     * @param buf   the buffer to append the composed string to
     * @param array the int array to stringify
     *
     * @return the original buffer, for chaining purposes
     */
    public static StringBuilder toString(StringBuilder buf, int[] array) {
        buf.append("[").append(array.length).append("|");
        for(int i=0;i<array.length;i++) {
            if(i>0) buf.append(",");
            buf.append(Integer.toString(array[i]));
        }
        buf.append("]");
        return buf;
    }

    /**
     * Returns a pretty string representation of an int array.
     *
     * @param array the int array to stringify
     *
     * @return a human-readable string exposing the contents of the array
     */
    public static String toString(int[] array) {
        return toString(new StringBuilder(), array).toString();
    }

    /**
     * Appends as strings the elements of an int array, separating them with a given string.
     *
     * @param buf   the buffer to append the composed string to
     * @param sep   the separator to use
     * @param array the int array to concatenate
     *
     * @return the original buffer, for chaining purposes
     *
     * @throws IOException if the output buffer raises this exception on {@code append()}
     */
    public static <A extends Appendable> A join(A buf, String sep, int[] array) throws IOException {
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
     * Appends as strings the elements of an int array, separating them with a given string.
     *
     * @param buf   the buffer to append the composed string to
     * @param sep   the separator to use
     * @param array the int array to concatenate
     *
     * @return the original buffer, for chaining purposes
     */
    public static StringBuilder join(StringBuilder buf, String sep, int[] array) {
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
     * Appends as strings the elements of an int array, separating them with a given string.
     *
     * @param sep   the separator to use
     * @param array the int array to concatenate
     *
     * @return a concatenation of the elements of the array, as string, and the separator
     */
    public static String join(String sep, int[] array) {
        return join(new StringBuilder(), sep, array).toString();
    }

}
