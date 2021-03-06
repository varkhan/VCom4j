/**
 *
 */
package net.varkhan.base.containers.array;

import net.varkhan.base.containers.Iterator;
import net.varkhan.base.containers.list.List;
import net.varkhan.base.containers.map.ArrayOpenHashMap;
import net.varkhan.base.containers.map.Map;

import java.io.IOException;
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
     * Returns the position of the first occurrence of an object inside an array.
     *
     * @param item  the object to search for
     * @param array the array to search
     * @param <T>   the element type
     *
     * @return {@literal -1} if {@code item} was not found in {@code array},
     * the position of the first element of the array for which {@code item}.{@link Object#equals(Object) equals}() is true.
     */
    public static <T> int indexOf(T item, T... array) {
        if(array==null) return -1;
        final int length=array.length;
        if(item==null) {
            for(int i=0;i<length;i++) {
                if(array[i]==null) return i;
            }
            return -1;
        }
        else {
            for(int i=0;i<length;i++) {
                if(item.equals(array[i])) return i;
            }
            return -1;
        }
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
     * Sorts an array in place using the natural order.
     *
     * @param ary the array to sort
     * @param <T> the element type
     * @return the number of swap operations required for the sorting
     */
    public static <T extends Comparable<? super T>> int sort(T... ary) {
        return sort(ary, 0, ary.length-1);
    }

    /**
     * Sorts an array segment in place using the natural order.
     *
     * @param ary the array to sort
     * @param inf the minimum index
     * @param sup the maximum index
     * @param <T> the element type
     * @return the number of swap operations required for the sorting
     */
    public static <T extends Comparable<? super T>> int sort(T[] ary, int inf, int sup) {
        int cnt = 0;
        int beg = ((inf+sup)>>1)+1; // inf + (sup-inf+1)/2 - 1 = (sup+inf)/2+1
        while(beg>inf) {
            beg --;
            cnt += heapDown(ary,beg,sup);
        }
        int end = sup;
        while(end>=inf) {
            T v = ary[end];
            ary[end] = ary[inf];
            ary[inf] = v;
            end --;
            cnt += 1 + heapDown(ary,inf,end);
        }
        return cnt;
    }

    protected static <T extends Comparable<? super T>> int heapDown(T[] ary, int inf, int sup) {
        int cnt = 0;
        int pos = inf;
        int cld = (pos<<1)+1;
        while(cld<=sup) {
            int swp = pos;
            int cmp = ary[swp].compareTo(ary[cld]);
            if(cmp<0) swp = cld;
            cld ++;
            if(cld<=sup) {
                cmp = ary[swp].compareTo(ary[cld]);
                if(cmp<0) swp = cld;
            }
            if(swp==pos) return cnt;
            T v = ary[pos];
            ary[pos] = ary[swp];
            ary[swp] = v;
            cnt ++;
            pos = swp;
            cld = (pos<<1)+1;
        }
        return cnt;
    }

    /**
     * Sorts an array in place using a comparator.
     *
     *
     * @param comp the comparator
     * @param ary  the array to sort
     * @return the number of swap operations required for the sorting
     */
    public static <T> int sort(Comparator<? super T> comp, T... ary) {
        return sort(comp, ary, 0, ary.length-1);
    }

    /**
     * Sorts an array segment in place using a comparator.
     *
     *
     * @param comp the comparator
     * @param ary  the array to sort
     * @param inf  the minimum index
     * @param sup  the maximum index
     * @param <T> the element type
     * @return the number of swap operations required for the sorting
     */
    public static <T> int sort(Comparator<? super T> comp, T[] ary, int inf, int sup) {
        int cnt = 0;
        int beg = ((inf+sup)>>1)+1; // inf + (sup-inf+1)/2 - 1 = (sup+inf)/2+1
        while(beg>inf) {
            beg --;
            cnt += heapDown(comp, ary,beg,sup);
        }
        int end = sup;
        while(end>=inf) {
            T v = ary[end];
            ary[end] = ary[inf];
            ary[inf] = v;
            end --;
            cnt += 1 + heapDown(comp, ary,inf,end);
        }
        return cnt;
    }

    protected static <T> int heapDown(Comparator<? super T> comp, T[] ary, int inf, int sup) {
        int cnt = 0;
        int pos = inf;
        int cld = (pos<<1)+1;
        while(cld<=sup) {
            int swp = pos;
            int cmp = comp.compare(ary[swp], ary[cld]);
            if(cmp<0) swp = cld;
            cld ++;
            if(cld<=sup) {
                cmp = comp.compare(ary[swp], ary[cld]);
                if(cmp<0) swp = cld;
            }
            if(swp==pos) return cnt;
            T v = ary[pos];
            ary[pos] = ary[swp];
            ary[swp] = v;
            cnt ++;
            pos = swp;
            cld = (pos<<1)+1;
        }
        return cnt;
    }

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
        int max=sup;

        while(min<max) {
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
        int max=sup;

        while(min<max) {
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
     * Inserts an object in a sorted array, using the natural order.
     * The array <em>must</em> have space for an extra element, i.e.
     * the length of the array must be larger than the specified
     * maximum index.
     *
     * @param ary  the sorted array
     * @param inf  the minimum index
     * @param sup  the maximum index
     * @param key  the object to insert
     * @param <T>  the element type
     *
     * @return the position of the inserted object
     * @throws ArrayIndexOutOfBoundsException if sup <= ary.length
     */
    public static <T extends Comparable<? super T>> int insert(T[] ary, int inf, int sup, T key) {
        int min=inf;
        int max=sup;

        while(min<max) {
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
        if(min<sup) System.arraycopy(ary, min, ary, min+1, sup-min);
        ary[min]=key;
        return min;
    }

    /**
     * Inserts an object in a sorted array, using a comparator.
     * The array <em>must</em> have space for an extra element, i.e.
     * the length of the array must be larger than the specified
     * maximum index.
     *
     * @param ary  the sorted array
     * @param inf  the minimum index
     * @param sup  the maximum index
     * @param key  the object to insert
     * @param comp the comparator
     * @param <T>  the element type
     *
     * @return the position of the inserted object
     * @throws ArrayIndexOutOfBoundsException if sup <= ary.length
     */
    public static <T> int insert(T[] ary, int inf, int sup, T key, Comparator<? super T> comp) {
//        if (comp == null)  return insert(ary, inf, sup, key);
        int min=inf;
        int max=sup;

        while(min<max) {
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
        if(min<sup) System.arraycopy(ary, min, ary, min+1, sup-min);
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
        while(beg1<len1 && beg2<len2) {
            T val1 = ary1[beg1];
            T val2 = ary2[beg2];
            int cmp = comp.compare(val1, val2);
            if(cmp<0) { if(beg==0||comp.compare(val1,last)>0) last = union[beg++] = val1; beg1++; } else
            if(cmp>0) { if(beg==0||comp.compare(val2,last)>0) last = union[beg++] = val2; beg2++; } else
            { if(beg==0||comp.compare(val1,last)>0) last = union[beg++] = val1; beg1++; beg2++; }
        }
        while(beg1<len1) {
            T val1 = ary1[beg1];
            if(beg==0||comp.compare(val1,last)>0) last = union[beg++] = val1;
            beg1++;
        }
        while(beg2<len2) {
            T val2 = ary2[beg2];
            if(beg==0||comp.compare(val2,last)>0) last = union[beg++] = val2;
            beg2++;
        }
        if(beg==len) return union;
        @SuppressWarnings("unchecked")
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
        while(beg1<len1) {
            T val1 = ary1[beg1];
            if(beg==0||val1.compareTo(last)>0) last = union[beg++] = val1;
            beg1++;
        }
        while(beg2<len2) {
            T val2 = ary2[beg2];
            if(beg==0||val2.compareTo(last)>0) last = union[beg++] = val2;
            beg2++;
        }
        if(beg==len) return union;
        @SuppressWarnings("unchecked")
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
            if(cmp<0) { beg1++; } else
            if(cmp>0) { beg2++; } else
            { beg1++; beg2++;  if(beg==0||comp.compare(val1,last)>0) last = inter[beg++] = val1;}
        }
        if(beg==len) return inter;
        @SuppressWarnings("unchecked")
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
            if(cmp<0) { beg1++; } else
            if(cmp>0) { beg2++; } else
            { beg1++; beg2++;  if(beg==0||val1.compareTo(last)>0) last = inter[beg++] = val1;}
        }
        if(beg==len) return inter;
        @SuppressWarnings("unchecked")
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
        if(beg<0 || beg>end || end>l) throw new ArrayIndexOutOfBoundsException("["+beg+":"+end+"] is not a valid range specifier");
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
     * Returns an immutable list backed by an array.
     *
     * @param values the array of values
     * @param <T>    the type of the values
     * @return a list holding the elements of the array, in order
     */
    public static <T> List<T> asList(final T... values) {
        if(values==null) return new _List<T>(null,0,0);
        return new _List<T>(values.clone(),0,values.length);
    }

    protected static class _List<T> implements List<T> {
        private final int beg;
        private final int len;
        private final T[] vals;

        public _List(T[] vals, int beg, int len) {
            this.vals=vals;
            this.beg=beg;
            this.len=len;
        }

        public long size() { return vals==null?0:len; }

        public boolean isEmpty() { return vals==null||len==0; }

        public void clear() { }

        public boolean add(T elt) { return false; }

        public boolean add(long idx, T elt) { return false; }

        public T get(long idx) { return vals==null||idx<0||idx>=len ? null : vals[beg+(int)idx]; }

        public boolean set(long idx, T elt) {
            if(vals==null) return false;
            if(0<=idx && idx<len && vals[beg+(int)idx]!=elt) {
                vals[beg+(int)idx]=elt;
                return true;
            }
            else return false;
        }

        public boolean del(long idx) { return false; }

        public boolean del(T elt) { return false; }

        public Iterator<? extends T> iterator() {
            return new Iterator<T>() {
                private volatile int pos=0;

                public boolean hasNext() { return vals!=null&&pos<len; }

                public T next() {
                    if(vals==null||pos>=len) throw new NoSuchElementException();
                    return vals[beg+pos++];
                }

                public void remove() { throw new UnsupportedOperationException(); }
            };
        }

        public <Par> long visit(Visitor<T,Par> vis, Par par) {
            long ret=0;
            if(vals!=null) for(int i=0;i<len;i++) {
                T obj=vals[beg+i];
                long r=vis.invoke(obj, par);
                if(r<0) return ret;
                ret+=r;
            }
            return ret;
        }

        public List<T> sublist(long beg, long end) {
            return new _List<T>(vals,(int)(this.beg+beg),(int)(end-beg));
        }

        public String toString() {
            StringBuilder buf=new StringBuilder();
            buf.append('[');
            boolean first = true;
            for(int i=0;i<len;i++) {
                T obj=vals[beg+i];
                if(first) first=false;
                else buf.append(',');
                buf.append(' ').append(obj);
                i++;
            }
            buf.append(' ').append(']');
            return buf.toString();
        }

        public int hashCode() {
            int hash=0;
            hash^=len;
            if(len>0) {
                for(int i=0;i<len;i++) {
                    Object o=vals[beg+i];
                    if(o!=null) hash^=o.hashCode();
                }
            }
            return hash;
        }

        public boolean equals(Object obj) {
            if(!(obj instanceof List)) return false;
            List<?> that=(List<?>) obj;
            if(vals==null) return that.size()==0;
            if(len!=that.size()) return false;
            if(len>0) {
                for(int i=0;i<len;i++) {
                    Object thiso=vals[beg+i];
                    Object thato=that.get(i);
                    if(thiso==thato) continue;
                    if(thiso==null||thato==null) return false;
                    if(!thiso.equals(thato)) return false;
                }
            }
            return true;
        }
    }


    /**
     * Returns an array of alternating keys and values as a map.
     *
     * @param kclass the class of the keys
     * @param vclass the class of the values
     * @param values the array of keys and values
     * @param <K>    the type of the keys
     * @param <V>    the type of the values
     * @return a map associating the object at even indexes in the array to the immediately following (odd index) element
     */
    @SuppressWarnings({ "unchecked" })
    public static <K, V> Map<K,V> asMap(final Class<K> kclass, final Class<V> vclass, final Object... values) {
        ArrayOpenHashMap<K,V> map=new ArrayOpenHashMap<K,V>();
        if(values==null) return map;
        if((values.length&1)!=0) throw new IllegalArgumentException("Key/Value array must be of even size");
        for(int i=0;i<values.length;i+=2) {
            Object key=values[i];
            if(key!=null&&!kclass.isAssignableFrom(key.getClass()))
                throw new IllegalArgumentException("Invalid key type at "+i);
            Object val=values[i+1];
            if(val!=null&&!vclass.isAssignableFrom(val.getClass()))
                throw new IllegalArgumentException("Invalid value type at "+(i+1));
            map.add((K) key, (V) val);
        }
        return map;
    }


    /*********************************************************************************
     **  String transformation
     **/

    /**
     * Builds a pretty string representation of an array.
     *
     * @param buf   the buffer to append the composed string to
     * @param array the array to stringify
     * @param <T>   the element type
     * @param <A>   the buffer type
     *
     * @return the original buffer, for chaining purposes
     *
     * @throws java.io.IOException if the output buffer raises this exception on {@code append()}
     */
    public static <T, A extends Appendable> A toString(A buf, T... array) throws IOException {
        buf.append("[").append(Integer.toString(array.length)).append("|");
        for(int i=0;i<array.length;i++) {
            if(i>0) buf.append(",");
            T t=array[i];
            if(t!=null) buf.append(t.toString());
        }
        buf.append("]");
        return buf;
    }

    /**
     * Builds a pretty string representation of an array.
     *
     * @param buf   the buffer to append the composed string to
     * @param array the array to stringify
     * @param <T>   the element type
     *
     * @return the original buffer, for chaining purposes
     */
    public static <T> StringBuilder toString(StringBuilder buf, T... array) {
        buf.append("[").append(array.length).append("|");
        for(int i=0;i<array.length;i++) {
            if(i>0) buf.append(",");
            T t=array[i];
            if(t!=null) buf.append(t.toString());
        }
        buf.append("]");
        return buf;
    }

    /**
     * Returns a pretty string representation of an array.
     *
     * @param array the array to stringify
     * @param <T>   the element type
     *
     * @return a human-readable string exposing the contents of the array
     */
    public static <T> String toString(T... array) {
        StringBuilder buf=new StringBuilder();
        return toString(buf, array).toString();
    }

    /**
     * Appends as strings the elements of an array, wrapping each non-null
     * element with delimiters and separating them with a given string.
     *
     * @param buf   the buffer to append the composed string to
     * @param sep   the separator between elements
     * @param ldl   the left delimiter of elements
     * @param rdl   the right delimiter of element
     * @param array the array to join
     * @param <T>   the element type
     * @param <A>   the buffer type
     *
     * @return the original buffer, for chaining purposes
     *
     * @throws IOException if the output buffer raises this exception on {@code append()}
     */
    public static <T, A extends Appendable> A join(A buf, String sep, String ldl, String rdl, T... array) throws IOException {
        if(sep==null) for(int i=0;i<array.length;i++) {
            T elt=array[i];
            if(elt!=null) {
                if(ldl!=null) buf.append(ldl);
                buf.append(elt.toString());
                if(rdl!=null) buf.append(rdl);
            }
        }
        else for(int i=0;i<array.length;i++) {
            if(i>0) buf.append(sep);
            T elt=array[i];
            if(elt!=null) {
                if(ldl!=null) buf.append(ldl);
                buf.append(elt.toString());
                if(rdl!=null) buf.append(rdl);
            }
        }
        return buf;
    }

    /**
     * Appends as strings the elements of an array, wrapping each non-null
     * element with delimiters and separating them with a given string.
     *
     * @param buf   the buffer to append the composed string to
     * @param sep   the separator between elements
     * @param ldl   the left delimiter of elements
     * @param rdl   the right delimiter of element
     * @param array the array to join
     * @param <T>   the element type
     *
     * @return the original buffer, for chaining purposes
     */
    public static <T> StringBuilder join(StringBuilder buf, String sep, String ldl, String rdl, T... array) {
        if(sep==null) for(int i=0;i<array.length;i++) {
            T elt=array[i];
            if(elt!=null) {
                if(ldl!=null) buf.append(ldl);
                buf.append(elt.toString());
                if(rdl!=null) buf.append(rdl);
            }
        }
        else for(int i=0;i<array.length;i++) {
            if(i>0) buf.append(sep);
            T elt=array[i];
            if(elt!=null) {
                if(ldl!=null) buf.append(ldl);
                buf.append(elt.toString());
                if(rdl!=null) buf.append(rdl);
            }
        }
        return buf;
    }

    /**
     * Appends as strings the elements of an array, wrapping each non-null
     * element with delimiters and separating them with a given string.
     *
     * @param sep   the separator between elements
     * @param ldl   the left delimiter of elements
     * @param rdl   the right delimiter of element
     * @param array the array to join
     * @param <T>   the element type
     *
     * @return a concatenation of the elements of the array, as strings wrapped in the delimiters, and the separator
     */
    public static <T> String join(String sep, String ldl, String rdl, T... array) {
        return join(new StringBuilder(), sep, ldl, rdl, array).toString();
    }

    /**
     * Appends as strings the elements of an array, separating them with a given string.
     *
     * @param sep   the separator between elements
     * @param array the array to join
     * @param <T>   the element type
     *
     * @return a concatenation of the elements of the array, as strings, and the separator
     */
    public static <T> String join(String sep, T... array) {
        return join(new StringBuilder(), sep, null, null, array).toString();
    }

}
