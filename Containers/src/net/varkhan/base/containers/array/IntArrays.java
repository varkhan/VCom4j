/**
 *
 */
package net.varkhan.base.containers.array;

import net.varkhan.base.containers.Visitable;
import net.varkhan.base.containers.type.IntContainer;

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
     * Indicates whether a value is contained in an int array.
     *
     * @param item  the value to search for
     * @param array the array to search
     *
     * @return {@literal true} if {@code array} contains {@code item}
     */
    public static boolean isMember(int item, int[] array) {
        if(array==null) return false;
        for(int elem : array) if(item==elem) return true;
        return false;
    }

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
    public static int indexOf(int[] array, int pos, int[] sub) {
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
     * @param heap the sorted array
     * @param inf  the minimum index
     * @param sup  the maximum index
     * @param key  the value to search for
     *
     * @return the key position in the array, or {@code -(inspos+1)},
     *         where {@code inspos} is the index of the first value in the
     *         array bigger than {@code key}
     */
    public static int searchHeapInc(int[] heap, int inf, int sup, int key) {
        int min=inf;
        int max=sup-1;

        while(min<=max) {
            int med=(min+max)>>>1;
            int medVal=heap[med];

            if(medVal<key) min=med+1;
            else if(medVal>key) max=med-1;
            else return med; // key found
        }
        return -(min+1);  // key not found.
    }

    /**
     * Finds an object in a sorted array, in descending order.
     *
     * @param heap the sorted array
     * @param inf  the minimum index
     * @param sup  the maximum index
     * @param key  the value to search for
     *
     * @return the key position in the array, or {@code -(inspos+1)},
     *         where {@code inspos} is the index of the first value in the
     *         array bigger than {@code key}
     */
    public static int searchHeapDec(int[] heap, int inf, int sup, int key) {
        int min=inf;
        int max=sup-1;

        while(min<=max) {
            int med=(min+max)>>>1;
            int medVal=heap[med];

            if(medVal<key) min=med+1;
            else if(medVal>key) max=med-1;
            else return med; // key found
        }
        return -(min+1);  // key not found.
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
        if(beg<0 || beg>end || end>=l) throw new IndexOutOfBoundsException();
        int[] subary=new int[end-beg];
        System.arraycopy(array, beg, subary, 0, end-beg);
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

}
