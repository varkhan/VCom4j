/**
 *
 */
package net.varkhan.base.containers.array;

import net.varkhan.base.containers.Container;
import net.varkhan.base.containers.Iterator;
import net.varkhan.base.containers.map.ArrayOpenHashMap;
import net.varkhan.base.containers.map.Map;

import java.lang.reflect.Array;
import java.util.Comparator;
import java.util.NoSuchElementException;


/**
 * <b>Static array manipulation utilities.</b>
 * <p/>
 * Utilities for converting primitive type arrays to human-readable strings,
 * accessing, comparing and sorting array elements.
 * <p/>
 *
 * @author varkhan
 * @date Mar 12, 2009
 * @time 2:52:53 AM
 */
public class Arrays {

    /**
     * Private empty constructor that forbids instantiation of this class.
     */
    protected Arrays() {
    }


    /*********************************************************************************
     **  Matching operations
     **/

    /**
     * Indicates whether an object is contained in an array,
     * using the standard {@link #equals} method if the object
     * to find is not {@literal null}, or comparing to {@literal null}
     * otherwise.
     *
     * @param item  the object to search for
     * @param array the array to search
     * @param <T>   the element type
     *
     * @return {@literal true} if {@code array} contains an object equal to {@code item}
     */
    public static <T> boolean isMember(T item, T... array) {
        if(array==null) return false;
        if(item==null) {
            for(T elem : array) {
                if(elem==null) return true;
            }
            return false;
        }
        else {
            for(T elem : array) {
                if(item.equals(elem)) return true;
            }
            return false;
        }
    }

    /**
     * Indicates whether two byte arrays are equal.
     *
     * @param array1 the first array
     * @param array2 the second array
     * @param <T>    the element type
     *
     * @return {@code true} iff the arrays are either both {@code null} or
     *         have the same number of elements, and the values of elements in the
     *         same positions are equal
     */
    public static <T> boolean equals(T[] array1, T[] array2) {
        if(array1==null) return array2==null;
        if(array1.length!=array2.length) return false;
        for(int i=0;i<array1.length;i++) {
            if(array1[i]==array2[i]) continue;
            if(array1[i]==null&&array2[i]!=null) return false;
            if(array1[i]!=null&&array2[i]==null) return false;
            if(!array1[i].equals(array2[i])) return false;
        }
        return true;
    }

    /**
     * Returns the position of the first occurrence of an array as a subsequence
     * of an other array.
     *
     * @param array the array
     * @param pos   the starting point in the array
     * @param sub   the subsequence to find
     * @param <T>   the element type
     *
     * @return the smallest index {@code idx} greater than {@code pos} such that
     *         the elements of {@code array} starting at {@code idx} are exactly those of
     *         {@code sub}  ({@code -1} is returned if {@code sub} is not a subsequence
     *         of {@code array})
     */
    public static <T> int indexOf(T[] array, int pos, T[] sub) {
        if(array==null) return sub==null ? 0 : -1;
        match:
        while(pos<array.length) {
            for(int i=0;i<sub.length;i++) {
                if(pos+i>array.length) return -1;
                T a=array[pos+i];
                T s=sub[i];
                if(a==s) continue;
                if((a==null)||(s==null)||!a.equals(s)
                        ) {
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
     * Finds an object in a sorted array, using the natural order.
     *
     * @param ary  the sorted array
     * @param inf  the minimum index
     * @param sup  the maximum index
     * @param key  the object to search for
     * @param <T>  the element type
     *
     * @return the key position in the array, or {@code -(inspos+1)},
     *         where {@code inspos} is the index of the first object in the
     *         array bigger than {@code key}
     */
    public static <T extends Comparable<? super T>> int search(T[] ary, int inf, int sup, T key) {
        int min=inf;
        int max=sup-1;

        while(min<=max) {
            int med=(min+max)>>>1;
            T medVal=ary[med];
            int cmp=medVal.compareTo(key);

            if(cmp<0) min=med+1;
            else if(cmp>0) max=med-1;
            else return med; // key found
        }
        return -(min+1);  // key not found.
    }

    /**
     * Finds an object in a sorted array, using a comparator.
     *
     * @param ary  the sorted array
     * @param inf  the minimum index
     * @param sup  the maximum index
     * @param key  the object to search for
     * @param comp the comparator
     * @param <T>  the element type
     *
     * @return the key position in the array, or {@code -(inspos+1)},
     *         where {@code inspos} is the index of the first object in the
     *         array bigger than {@code key}
     */
    public static <T> int search(T[] ary, int inf, int sup, T key, Comparator<? super T> comp) {
//        if (comp == null)  return search(ary, inf, sup, key);
        int min=inf;
        int max=sup-1;

        while(min<=max) {
            int med=(min+max)>>>1;
            T medVal=ary[med];
            int cmp=comp.compare(medVal, key);

            if(cmp<0) min=med+1;
            else if(cmp>0) max=med-1;
            else return med; // key found
        }
        return -(min+1);  // key not found.
    }

    /**
     * Inserts an object in a sorted ary, using the natural order.
     *
     * @param ary  the sorted array
     * @param inf  the minimum index
     * @param sup  the maximum index
     * @param key  the object to insert
     * @param <T>  the element type
     *
     * @return the position of the inserted object
     */
    public static <T extends Comparable<? super T>> int insert(T[] ary, int inf, int sup, T key) {
        int min=inf;
        int max=sup-1;

        while(min<=max) {
            int med=(min+max)>>>1;
            T medVal=ary[med];
            int cmp=medVal.compareTo(key);

            if(cmp<0) min=med+1;
            else if(cmp>0) max=med-1;
            else {
                System.arraycopy(ary, med, ary, med+1, sup-med-1);
                ary[med]=key;
                return med;
            }
        }
        System.arraycopy(ary, min, ary, min+1, sup-min-1);
        ary[min]=key;
        return min;
    }

    /**
     * Inserts an object in a sorted ary, using a comparator.
     *
     * @param ary  the sorted array
     * @param inf  the minimum index
     * @param sup  the maximum index
     * @param key  the object to insert
     * @param comp the comparator
     * @param <T>  the element type
     *
     * @return the position of the inserted object
     */
    public static <T> int insert(T[] ary, int inf, int sup, T key, Comparator<? super T> comp) {
//        if (comp == null)  return insert(ary, inf, sup, key);
        int min=inf;
        int max=sup-1;

        while(min<=max) {
            int med=(min+max)>>>1;
            T medVal=ary[med];
            int cmp=comp.compare(medVal, key);

            if(cmp<0) min=med+1;
            else if(cmp>0) max=med-1;
            else {
                System.arraycopy(ary, med, ary, med+1, sup-med-1);
                ary[med]=key;
                return med;
            }
        }
        System.arraycopy(ary, min, ary, min+1, sup-min-1);
        ary[min]=key;
        return min;
    }

    /**
     * Computes the union of segments in two sorted arrays, using a comparator.
     *
     * @param ary1 the first sorted array
     * @param beg1 the start position of the first segment
     * @param len1 the length of the first segment
     * @param ary2 the second sorted array
     * @param beg2 the start position of the second segment
     * @param len2 the length of the second segment
     * @param comp the comparator
     * @param <T>  the element type
     * @return the union of the two segments, with duplicates removed
     */
    public static <T> T[] union(T[] ary1, int beg1, int len1, T[] ary2, int beg2, int len2, Comparator<? super T> comp) {
        int len = len1+len2;
        @SuppressWarnings("unchecked")
        T[] union=(ary1.getClass()==Object[].class)
                   ? (T[]) new Object[len]
                   : (T[]) Array.newInstance(ary1.getClass().getComponentType(), len);
        if(len==0) return union;
        T last = null;
        len1+=beg1;
        len2+=beg2;
        int beg=0;
        while(len1>0 && len2>0) {
            T val1 = ary1[beg1];
            T val2 = ary2[beg2];
            int cmp = comp.compare(val1, val2);
            if(cmp<0) { if(beg==0||comp.compare(val1,last)>0) last = union[beg++] = val1; beg1++; } else
            if(cmp>0) { if(beg==0||comp.compare(val2,last)>0) last = union[beg++] = val2; beg2++; } else
            { if(beg==0||comp.compare(val1,last)>0) last = union[beg++] = val1; beg1++; beg2++; }
        }
        while(len1>0) {
            T val1 = ary1[beg1];
            if(beg==0||comp.compare(val1,last)>0) last = union[beg++] = val1;
            beg1++;
        }
        while(len2>0) {
            T val2 = ary2[beg2];
            if(beg==0||comp.compare(val2,last)>0) last = union[beg++] = val2;
            beg2++;
        }
        if(beg==len) return union;
        T[] copy = (((Object) ary1.getClass())==Object[].class)
                            ? (T[]) new Object[len]
                            : (T[]) Array.newInstance(ary1.getClass().getComponentType(), beg);
        System.arraycopy(union, 0, copy, 0, beg);
        return copy;
    }

    /**
     * Computes the union of segments in two sorted arrays, using the natural order.
     *
     * @param ary1 the first sorted array
     * @param beg1 the start position of the first segment
     * @param len1 the length of the first segment
     * @param ary2 the second sorted array
     * @param beg2 the start position of the second segment
     * @param len2 the length of the second segment
     * @param <T>  the element type
     * @return the union of the two segments, with duplicates removed
     */
    public static <T extends Comparable<? super T>> T[] union(T[] ary1, int beg1, int len1, T[] ary2, int beg2, int len2) {
        int len = len1+len2;
        @SuppressWarnings("unchecked")
        T[] union=(((Object) ary1.getClass())==Object[].class)
                   ? (T[]) new Object[len]
                   : (T[]) Array.newInstance(ary1.getClass().getComponentType(), len);
        if(len==0) return union;
        T last = null;
        len1+=beg1;
        len2+=beg2;
        int beg=0;
        while(beg1<len1 && beg2<len2) {
            T val1 = ary1[beg1];
            T val2 = ary2[beg2];
            int cmp = val1.compareTo(val2);
            if(cmp<0) { if(beg==0||val1.compareTo(last)>0) last = union[beg++] = val1; beg1++; } else
            if(cmp>0) { if(beg==0||val2.compareTo(last)>0) last = union[beg++] = val2; beg2++; } else
            { if(beg==0||val1.compareTo(last)>0) last = union[beg++] = val1; beg1++; beg2++; }
        }
        while(len1>0) {
            T val1 = ary1[beg1];
            if(beg==0||val1.compareTo(last)>0) last = union[beg++] = val1;
            beg1++;
        }
        while(len2>0) {
            T val2 = ary2[beg2];
            if(beg==0||val2.compareTo(last)>0) last = union[beg++] = val2;
            beg2++;
        }
        if(beg==len) return union;
        T[] copy = (((Object) ary1.getClass())==Object[].class)
                            ? (T[]) new Object[len]
                            : (T[]) Array.newInstance(ary1.getClass().getComponentType(), beg);
        System.arraycopy(union, 0, copy, 0, beg);
        return copy;
    }

    /**
     * Computes the intersection of segments in two sorted arrays, using a comparator.
     *
     * @param ary1 the first sorted array
     * @param beg1 the start position of the first segment
     * @param len1 the length of the first segment
     * @param ary2 the second sorted array
     * @param beg2 the start position of the second segment
     * @param len2 the length of the second segment
     * @param comp the comparator
     * @param <T>  the element type
     * @return the intersection of the two segments, with duplicates removed
     */
    public static <T> T[] inter(T[] ary1, int beg1, int len1, T[] ary2, int beg2, int len2, Comparator<? super T> comp) {
        int len = (len1>len2)?len1:len2;
        @SuppressWarnings("unchecked")
        T[] inter=(ary1.getClass()==Object[].class)
                   ? (T[]) new Object[len]
                   : (T[]) Array.newInstance(ary1.getClass().getComponentType(), len);
        if(len==0) return inter;
        T last = null;
        len1+=beg1;
        len2+=beg2;
        int beg=0;
        while(beg1<len1 && beg2<len2) {
            T val1 = ary1[beg1];
            T val2 = ary2[beg2];
            int cmp = comp.compare(val1, val2);
            if(cmp<0) { beg1++; len1--; } else
            if(cmp>0) { beg2++; len2--; } else
            { if(beg==0||comp.compare(val1,last)>0) last = inter[beg++] = val1; beg1++; beg2++; }
        }
        if(beg==len) return inter;
        T[] copy = (((Object) ary1.getClass())==Object[].class)
                            ? (T[]) new Object[len]
                            : (T[]) Array.newInstance(ary1.getClass().getComponentType(), beg);
        System.arraycopy(inter, 0, copy, 0, beg);
        return copy;
    }

    /**
     * Computes the intersection of segments in two sorted arrays, using the natural order.
     *
     * @param ary1 the first sorted array
     * @param beg1 the start position of the first segment
     * @param len1 the length of the first segment
     * @param ary2 the second sorted array
     * @param beg2 the start position of the second segment
     * @param len2 the length of the second segment
     * @param <T>  the element type
     * @return the intersection of the two segments, with duplicates removed
     */
    public static <T extends Comparable<? super T>> T[] inter(T[] ary1, int beg1, int len1, T[] ary2, int beg2, int len2) {
        int len = (len1>len2)?len1:len2;
        @SuppressWarnings("unchecked")
        T[] inter=(((Object)ary1.getClass())==Object[].class)
                   ? (T[]) new Object[len]
                   : (T[]) Array.newInstance(ary1.getClass().getComponentType(), len);
        if(len==0) return inter;
        T last = null;
        len1+=beg1;
        len2+=beg2;
        int beg=0;
        while(beg1<len1 && beg2<len2) {
            T val1 = ary1[beg1];
            T val2 = ary2[beg2];
            int cmp = val1.compareTo(val2);
            if(cmp<0) { beg1++; len1--; } else
            if(cmp>0) { beg2++; len2--; } else
            { if(beg==0||val1.compareTo(last)>0) last = inter[beg++] = val1; beg1++; beg2++; }
        }
        if(beg==len) return inter;
        T[] copy = (((Object) ary1.getClass())==Object[].class)
                            ? (T[]) new Object[len]
                            : (T[]) Array.newInstance(ary1.getClass().getComponentType(), beg);
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
     * @param <T>   the element type
     *
     * @return an array containing all the elements of {@code array} followed by all the elements in {@code elems}
     */
    public static <T> T[] append(T[] array, T... elems) {
        @SuppressWarnings("unchecked")
        T[] concat=(array.getClass()==Object[].class)
                   ? (T[]) new Object[array.length+elems.length]
                   : (T[]) Array.newInstance(array.getClass().getComponentType(), array.length+elems.length);
        System.arraycopy(array, 0, concat, 0, array.length);
        System.arraycopy(elems, 0, concat, array.length, elems.length);
        return concat;
    }

    /**
     * Prepend elements to an array.
     *
     * @param array the array to prepend to
     * @param elems the elements to prepend
     * @param <T>   the element type
     *
     * @return an array containing all the elements in {@code elems} followed by all the elements of {@code array}
     */
    public static <T> T[] prepend(T[] array, T... elems) {
        @SuppressWarnings("unchecked")
        T[] concat=(array.getClass()==Object[].class)
                   ? (T[]) new Object[array.length+elems.length]
                   : (T[]) Array.newInstance(array.getClass().getComponentType(), array.length+elems.length);
        System.arraycopy(elems, 0, concat, 0, elems.length);
        System.arraycopy(array, 0, concat, elems.length, array.length);
        return concat;
    }

    /**
     * Concatenates several arrays.
     *
     * @param array  the first array to append to
     * @param arrays the arrays to append
     * @param <T>    the element type
     *
     * @return an array whose element type is the element type of the first array, and containing all the elements in the arrays, in order
     */
    public static <T> T[] concat(T[] array, T[]... arrays) {
        int l=array.length;
        for(T[] t : arrays) { l+=t.length; }
        java.util.Arrays.copyOf(array, 0);
        @SuppressWarnings("unchecked")
        T[] concat=(array.getClass()==Object[].class)
                   ? (T[]) new Object[l]
                   : (T[]) Array.newInstance(array.getClass().getComponentType(), l);
        System.arraycopy(array, 0, concat, 0, array.length);
        l=array.length;
        for(T[] t : arrays) {
            System.arraycopy(t, 0, concat, l, t.length);
            l+=t.length;
        }
        return concat;
    }

    /**
     * Returns a segment of an array.
     *
     * @param array the source array
     * @param beg   the beginning position of the segment, inclusive
     * @param end   the ending position of the segment, exclusive
     * @param <T>   the element type
     *
     * @return an array whose element type is the element type of the input array, of length {@code end-beg}, and
     * containing all the elements between positions {@code beg}, inclusive, and {@code end}, exclusive, of the original array
     */
    public static <T> T[] subarray(T[] array, int beg, int end) {
        int l=array.length;
        if(beg<0 || beg>end || end>l) throw new IndexOutOfBoundsException();
        @SuppressWarnings("unchecked")
        T[] subary=(array.getClass()==Object[].class)
                   ? (T[]) new Object[end-beg]
                   : (T[]) Array.newInstance(array.getClass().getComponentType(), end-beg);
        if(end>beg) System.arraycopy(array, beg, subary, 0, end-beg);
        return subary;
    }


    /*********************************************************************************
     **  Container wrapping
     **/

    /**
     * Returns an immutable container backed by an array.
     *
     * @param values the array of values
     * @param <T>    the type of the values
     * @return a container holding the elements of the array, in order
     */
    public static <T> Container<T> container(final T... values) {
        return new Container<T>() {
            public long size() { return values==null?0:values.length; }
            public boolean isEmpty() { return values==null||values.length==0; }
            public Iterator<? extends T> iterator() {
                return new Iterator<T>() {
                    private volatile int pos = 0;
                    public boolean hasNext() { return values!=null&&pos<values.length; }
                    public T next() {
                        if(values==null||pos>=values.length) throw new NoSuchElementException();
                        return values[pos++];
                    }
                    public void remove() { throw new UnsupportedOperationException(); }
                };
            }
            public <Par> long visit(Visitor<T,Par> vis, Par par) {
                long ret = 0;
                if(values!=null) for(T obj: values) {
                    long r = vis.invoke(obj, par);
                    if(r<0) return ret;
                    ret += r;
                }
                return ret;
            }
        };
    }

    /**
     * Returns an array of alternating keys and values as a map.
     *
     * @param values the array of keys and values
     * @param <K>    the type of the keys
     * @param <V>    the type of the values
     * @return a map associating the object at even indexes in the array to the immediately following (odd index) element
     */
    @SuppressWarnings({ "unchecked" })
    public static <K,V> Map<K,V> mapping(final Object... values) {
        ArrayOpenHashMap<K,V> map = new ArrayOpenHashMap<K,V>();
        if(values==null) return map;
        if((values.length&1)!=0) throw new IllegalArgumentException("Key/Value array must be of even size");
        for(int i=0; i<values.length; i+=2) {
            map.add((K) values[i],(V) values[i+1]);
        }
        return map;
    }
}
