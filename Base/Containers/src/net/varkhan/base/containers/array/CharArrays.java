/**
 *
 */
package net.varkhan.base.containers.array;

import java.io.IOException;


/**
 * <b>Static char arrays manipulation utilities.</b>
 * <p/>
 * Utilities for manipulating arrays of chars and character sequences.
 * <p/>
 *
 * @author varkhan
 * @date Mar 12, 2009
 * @time 3:09:04 AM
 */
public class CharArrays {

    /**
     * Private empty constructor that forbids instantiation of this class.
     */
    protected CharArrays() {
    }


    /*********************************************************************************
     **  Matching operations
     **/

    /**
     * Indicates whether two char arrays are equal.
     *
     * @param array1 the first array
     * @param array2 the second array
     *
     * @return {@code true} iff the arrays are either both {@code null} or
     *         have the same number of elements, and the values of elements in the
     *         same positions are equal
     */
    public static boolean equals(char[] array1, char[] array2) {
        if(array1==null) return array2==null;
        if(array1.length!=array2.length) return false;
        for(int i=0;i<array1.length;i++) {
            if(array1[i]!=array2[i]) return false;
        }
        return true;
    }

    /**
     * Indicates whether two CharSequence are equal.
     *
     * @param seq1 the first array
     * @param seq2 the second array
     *
     * @return {@code true} iff the CharSequences are either both {@code null} or
     *         have the same number of characters, and the same characters in the
     *         same positions
     */
    public static boolean equals(CharSequence seq1, CharSequence seq2) {
        if(seq1==null) return seq2==null;
        if(seq1.length()!=seq2.length()) return false;
        for(int i=0;i<seq1.length();i++) {
            if(seq1.charAt(i)!=seq2.charAt(i)) return false;
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
    public static int indexOf(char item, char... array) {
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
    public static int indexOf(char[] sub, int pos, char[] array) {
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
    public static int indexOf(CharSequence sub, int pos, CharSequence array) {
        if(array==null) return sub==null ? 0 : -1;
        int al=array.length();
        match:
        while(pos<al) {
            for(int i=0;i<sub.length();i++) {
                if(pos+i>=al) return -1;
                if(array.charAt(pos+i)!=sub.charAt(i)) {
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
    public static int heapSortInc(char... ary) {
        return heapSortInc(ary,0,ary.length-1);
    }

    /**
     * Sorts an array in place, in descending order.
     *
     * @param ary the array to sort
     * @return the number of swap operations required for the sorting
     */
    public static int heapSortDec(char... ary) {
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
    public static int heapSortInc(char[] ary, int inf, int sup) {
        int cnt = 0;
        int beg = ((inf+sup)>>1)+1; // inf + (sup-inf+1)/2 - 1 = (sup+inf)/2+1
        while(beg>inf) {
            beg --;
            cnt += heapDownInc(ary,beg,sup);
        }
        int end = sup;
        while(end>=inf) {
            char v = ary[end];
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
    public static int heapSortDec(char[] ary, int inf, int sup) {
        int cnt = 0;
        int beg = ((inf+sup)>>1)+1; // inf + (sup-inf+1)/2 - 1 = (sup+inf)/2+1
        while(beg>inf) {
            beg --;
            cnt += heapDownDec(ary,beg,sup);
        }
        int end = sup;
        while(end>=inf) {
            char v = ary[end];
            ary[end] = ary[inf];
            ary[inf] = v;
            end --;
            cnt += 1 + heapDownDec(ary,inf,end);
        }
        return cnt;
    }

    protected static int heapDownInc(char[] ary, int inf, int sup) {
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
            char v = ary[pos];
            ary[pos] = ary[swp];
            ary[swp] = v;
            cnt ++;
            pos = swp;
            cld = (pos<<1)+1;
        }
        return cnt;
    }

    protected static int heapDownDec(char[] ary, int inf, int sup) {
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
            char v = ary[pos];
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
    public static int searchInc(char[] ary, int inf, int sup, char key) {
        int min=inf;
        int max=sup-1;

        while(min<=max) {
            int med=(min+max)>>>1;
            char medVal=ary[med];

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
    public static int searchDec(char[] ary, int inf, int sup, char key) {
        int min=inf;
        int max=sup-1;

        while(min<=max) {
            int med=(min+max)>>>1;
            char medVal=ary[med];

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
    public static char[] unionInc(char[] ary1, int beg1, int len1, char[] ary2, int beg2, int len2) {
        int len = len1+len2;
        char[] union = new char[len];
        if(len==0) return union;
        char last = 0;
        len1+=beg1;
        len2+=beg2;
        int beg=0;
        while(beg1<len1 && beg2<len2) {
            char val1 = ary1[beg1];
            char val2 = ary2[beg2];
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
            char val1 = ary1[beg1];
            if(beg==0||last<val1) last = union[beg++] = val1;
            beg1++;
        }
        while(beg2<len2) {
            char val2 = ary2[beg2];
            if(beg==0||last<val2) last = union[beg++] = val2;
            beg2++;
        }
        if(beg>=len) return union;
        char[] copy = new char[beg];
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
    public static char[] unionDec(char[] ary1, int beg1, int len1, char[] ary2, int beg2, int len2) {
        int len = len1+len2;
        char[] union = new char[len];
        if(len==0) return union;
        char last = 0;
        len1+=beg1;
        len2+=beg2;
        int beg=0;
        while(beg1<len1 && beg2<len2) {
            char val1 = ary1[beg1];
            char val2 = ary2[beg2];
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
            char val1 = ary1[beg1];
            if(beg==0||last>val1) last = union[beg++] = val1;
            beg1++;
        }
        while(beg2<len2) {
            char val2 = ary2[beg2];
            if(beg==0||last>val2) last = union[beg++] = val2;
            beg2++;
        }
        if(beg>=len) return union;
        char[] copy = new char[beg];
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
    public static char[] interInc(char[] ary1, int beg1, int len1, char[] ary2, int beg2, int len2) {
        int len = (len1>len2)?len1:len2;
        char[] inter = new char[len];
        if(len==0) return inter;
        char last = 0;
        len1+=beg1;
        len2+=beg2;
        int beg=0;
        while(beg1<len1 && beg2<len2) {
            char val1 = ary1[beg1];
            char val2 = ary2[beg2];
            if(val1<val2) { beg1++; } else
            if(val1>val2) { beg2++; } else
            { if(beg==0||last<val1) last = inter[beg++] = val1; beg1++; beg2++; }
        }
        if(beg>=len) return inter;
        char[] copy = new char[beg];
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
    public static char[] interDec(char[] ary1, int beg1, int len1, char[] ary2, int beg2, int len2) {
        int len = (len1>len2)?len1:len2;
        char[] inter = new char[len];
        if(len==0) return inter;
        char last = 0;
        len1+=beg1;
        len2+=beg2;
        int beg=0;
        while(beg1<len1 && beg2<len2) {
            char val1 = ary1[beg1];
            char val2 = ary2[beg2];
            if(val1>val2) { beg1++; } else
            if(val1<val2) { beg2++; } else
            { if(beg==0||last>val1) last = inter[beg++] = val1; beg1++; beg2++; }
        }
        if(beg>=len) return inter;
        char[] copy = new char[beg];
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
     * @return an array of chars containing all the elements of {@code array} followed by all the elements in {@code elems}
     */
    public static char[] append(char[] array, char... elems) {
        char[] concat=new char[array.length+elems.length];
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
     * @return an array of chars containing all the elements in {@code elems} followed by all the elements of {@code array}
     */
    public static char[] prepend(char[] array, char... elems) {
        char[] concat=new char[array.length+elems.length];
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
     * @return an array of chars containing all the elements in the arrays, in order
     */
    public static char[] concat(char[] array, char[]... arrays) {
        int l=array.length;
        for(char[] t : arrays) { l+=t.length; }
        char[] concat=new char[l];
        System.arraycopy(array, 0, concat, 0, array.length);
        l=array.length;
        for(char[] t : arrays) {
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
     * @return an array of chars, of length {@code end-beg}, and containing all the
     * elements between positions {@code beg}, inclusive, and {@code end}, exclusive,
     * of the original array
     */
    public static char[] subarray(char[] array, int beg, int end) {
        int l=array.length;
        if(beg<0 || beg>end || end>l) throw new ArrayIndexOutOfBoundsException("["+beg+":"+end+"] is not a valid range specifier");
        char[] subary=new char[end-beg];
        if(end>beg) System.arraycopy(array, beg, subary, 0, end-beg);
        return subary;
    }


    /*********************************************************************************
     **  Character level manipulation
     **/

    /**
     * Transliterates (replaces single characters in) a segment of a char array.
     * <p/>
     * Characters that are contained in the matching set are counted, and replaced
     * by the character at the same position in the replacement set as the matched
     * character in the matching set. Duplicate characters in the matching set are
     * ignored, and only the first occurrence is taken into account. If the replacement
     * set is shorter than the matching set, the remaining matched characters are
     * counted, but remain unchanged. If the replacement set is longer, the remaining
     * characters are ignored.
     *
     * @param str a char array containing the segment
     * @param pos the position of the first character of the segment
     * @param len the length of (number of characters in) the segment
     * @param pat the matching character set (the set of characters to replace)
     * @param rep the replacement character set (the characters to replace with)
     *
     * @return the number of characters that have been matched in the segment
     */
    public static int tr(char[] str, int pos, int len, char[] pat, char[] rep) {
        if(pos>=str.length) throw new ArrayIndexOutOfBoundsException(pos);
        len+=pos;
        if(len>str.length) throw new ArrayIndexOutOfBoundsException(pos);
        int m=0;
        for(;pos<len;pos++) {
            char c=str[pos];
            for(int j=0;j<pat.length;j++) {
                if(pat[j]==c) {
                    m++;
                    if(rep!=null&&j<rep.length) str[pos]=rep[j];
                    break;
                }
            }
        }
        return m;
    }

    /**
     * Transliterates (replaces single characters in) a char array.
     * <p/>
     * Characters that are contained in the matching set are counted, and replaced
     * by the character at the same position in the replacement set as the matched
     * character in the matching set. Duplicate characters in the matching set are
     * ignored, and only the first occurrence is taken into account. If the replacement
     * set is shorter than the matching set, the remaining matched characters are
     * counted, but remain unchanged. If the replacement set is longer, the remaining
     * characters are ignored.
     *
     * @param str a char array
     * @param pat the matching character set (the set of characters to replace)
     * @param rep the replacement character set (the characters to replace with)
     *
     * @return the number of characters that have been matched in the segment
     */
    public static int tr(char[] str, char[] pat, char[] rep) {
        int m=0;
        for(int i=0;i<str.length;i++) {
            char c=str[i];
            for(int j=0;j<pat.length;j++) {
                if(pat[j]==c) {
                    m++;
                    if(rep!=null&&j<rep.length) str[i]=rep[j];
                    break;
                }
            }
        }
        return m;
    }

    /**
     * Transliterates (replaces single characters in) a CharSequence.
     * <p/>
     * Characters that are contained in the matching set are counted, and replaced
     * by the character at the same position in the replacement set as the matched
     * character in the matching set. Duplicate characters in the matching set are
     * ignored, and only the first occurrence is taken into account. If the replacement
     * set is shorter than the matching set, the remaining matched characters are
     * counted, but remain unchanged. If the replacement set is longer, the remaining
     * characters are ignored.
     *
     * @param str a CharSequence
     * @param pat the matching character set (the set of characters to replace)
     * @param rep the replacement character set (the characters to replace with)
     *
     * @return a new CharSequence with the replaced characters
     */
    public static CharSequence tr(CharSequence str, char[] pat, char[] rep) {
        char[] ss=new char[str.length()];
        for(int i=0;i<str.length();i++) {
            char c=str.charAt(i);
            ss[i]=c;
            for(int j=0;j<pat.length;j++) {
                if(pat[j]==c) {
                    if(rep!=null&&j<rep.length) ss[i]=rep[j];
                    break;
                }
            }
        }
        return new String(ss);
    }

    /**
     * Transliterates (replaces single characters in) a String.
     * <p/>
     * Characters that are contained in the matching set are counted, and replaced
     * by the character at the same position in the replacement set as the matched
     * character in the matching set. Duplicate characters in the matching set are
     * ignored, and only the first occurrence is taken into account. If the replacement
     * set is shorter than the matching set, the remaining matched characters are
     * counted, but remain unchanged. If the replacement set is longer, the remaining
     * characters are ignored.
     *
     * @param sep a String
     * @param pat the matching character set (the set of characters to replace)
     * @param rep the replacement character set (the characters to replace with)
     *
     * @return a new String with the replaced characters
     */
    public static String tr(String sep, char[] pat, char[] rep) {
        char[] ss=sep.toCharArray();
        for(int i=0;i<ss.length;i++) {
            char c=ss[i];
            for(int j=0;j<pat.length;j++) {
                if(pat[j]==c) {
                    if(rep!=null&&j<rep.length) ss[i]=rep[j];
                    break;
                }
            }
        }
        return new String(ss);
    }


    /*********************************************************************************
     **  Subsequence level manipulations
     **/

    /**
     * Replace a character by a CharSequence.
     * <p/>
     * Appends each character of the source sequence to the buffer,
     * except for segments that match the pattern, for which the
     * replacement is appended instead.
     *
     * @param buf the buffer to append the result to
     * @param str the source CharSequence
     * @param pat the character to replace
     * @param rep the CharSequence to replace matches with
     *
     * @return the original buffer, for chaining purposes
     */
    public static StringBuilder repl(StringBuilder buf, CharSequence str, char pat, CharSequence rep) {
        final int ls=str.length();
        // Pattern finding loop
        find:
        for(int i=0;i<ls;i++) {
            char c=str.charAt(i);
            // Look for a local match starting at i
            if(c!=pat) {
                // No match => add current char, restart match
                buf.append(c);
                continue find;
            }
            // Match => add replacement
            buf.append(rep);
        }
        return buf;
    }

    /**
     * Replace a character by a CharSequence.
     * <p/>
     * Appends each character of the source sequence to the buffer,
     * except for segments that match the pattern, for which the
     * replacement is appended instead.
     *
     * @param buf the buffer to append the result to
     * @param str the source CharSequence
     * @param pat the character to replace
     * @param rep the CharSequence to replace matches with
     *
     * @return the original buffer, for chaining purposes
     *
     * @throws IOException if the output buffer raises this exception on {@code append()}
     */
    public static <A extends Appendable> A repl(A buf, CharSequence str, char pat, CharSequence rep) throws IOException {
        final int ls=str.length();
        // Pattern finding loop
        find:
        for(int i=0;i<ls;i++) {
            char c=str.charAt(i);
            // Look for a local match starting at i
            if(c!=pat) {
                // No match => add current char, restart match
                buf.append(c);
                continue find;
            }
            // Match => add replacement
            buf.append(rep);
        }
        return buf;
    }

    /**
     * Replace a CharSequence by a character.
     * <p/>
     * Appends each character of the source sequence to the buffer,
     * except for segments that match the pattern, for which the
     * replacement is appended instead.
     *
     * @param buf the buffer to append the result to
     * @param str the source CharSequence
     * @param pat the CharSequence to replace
     * @param rep the character to replace matches with
     *
     * @return the original buffer, for chaining purposes
     */
    public static StringBuilder repl(StringBuilder buf, CharSequence str, CharSequence pat, char rep) {
        final int lp=pat.length();
        final int ls=str.length();
        // Pattern finding loop
        find:
        for(int i=0;i<ls;) {
            // Look for a local match starting at i
            for(int j=0;j<lp;j++) {
                if(i+j>=ls||str.charAt(i+j)!=pat.charAt(j)) {
                    // No match => add current char, restart match
                    buf.append(str.charAt(i++));
                    continue find;
                }
            }
            // Match => add replacement
            buf.append(rep);
            i+=lp;
        }
        return buf;
    }

    /**
     * Replace a CharSequence by a character.
     * <p/>
     * Appends each character of the source sequence to the buffer,
     * except for segments that match the pattern, for which the
     * replacement is appended instead.
     *
     * @param buf the buffer to append the result to
     * @param str the source CharSequence
     * @param pat the CharSequence to replace
     * @param rep the character to replace matches with
     *
     * @return the original buffer, for chaining purposes
     *
     * @throws IOException if the output buffer raises this exception on {@code append()}
     */
    public static <A extends Appendable> A repl(A buf, CharSequence str, CharSequence pat, char rep) throws IOException {
        final int lp=pat.length();
        final int ls=str.length();
        // Pattern finding loop
        find:
        for(int i=0;i<ls;) {
            // Look for a local match starting at i
            for(int j=0;j<lp;j++) {
                if(i+j>=ls||str.charAt(i+j)!=pat.charAt(j)) {
                    // No match => add current char, restart match
                    buf.append(str.charAt(i++));
                    continue find;
                }
            }
            // Match => add replacement
            buf.append(rep);
            i+=lp;
        }
        return buf;
    }

    /**
     * Replace a subsequence by an other in a CharSequence.
     * <p/>
     * Appends each character of the source sequence to the buffer,
     * except for segments that match the pattern, for which the
     * replacement is appended instead.
     *
     * @param buf the buffer to append the result to
     * @param str the source CharSequence
     * @param pat the CharSequence to replace in the source
     * @param rep the CharSequence to replace matches with
     *
     * @return the original buffer, for chaining purposes
     */
    public static StringBuilder repl(StringBuilder buf, CharSequence str, CharSequence pat, CharSequence rep) {
        final int lp=pat.length();
        final int ls=str.length();
        // Pattern finding loop
        find:
        for(int i=0;i<ls;) {
            // Look for a local match starting at i
            for(int j=0;j<lp;j++) {
                if(i+j>=ls||str.charAt(i+j)!=pat.charAt(j)) {
                    // No match => add current char, restart match
                    buf.append(str.charAt(i++));
                    continue find;
                }
            }
            // Match => add replacement
            if(rep!=null) buf.append(rep);
            i+=lp;
        }
        return buf;
    }

    /**
     * Replace a subsequence by an other in a CharSequence.
     * <p/>
     * Appends each character of the source sequence to the buffer,
     * except for segments that match the pattern, for which the
     * replacement is appended instead.
     *
     * @param buf the buffer to append the result to
     * @param str the source CharSequence
     * @param pat the CharSequence to replace in the source
     * @param rep the CharSequence to replace matches with
     *
     * @return the original buffer, for chaining purposes
     *
     * @throws IOException if the output buffer raises this exception on {@code append()}
     */
    public static <A extends Appendable> A repl(A buf, CharSequence str, CharSequence pat, CharSequence rep) throws IOException {
        final int lp=pat.length();
        final int ls=str.length();
        // Pattern finding loop
        find:
        for(int i=0;i<ls;) {
            // Look for a local match starting at i
            for(int j=0;j<lp;j++) {
                if(i+j>=ls||str.charAt(i+j)!=pat.charAt(j)) {
                    // No match => add current char, restart match
                    buf.append(str.charAt(i++));
                    continue find;
                }
            }
            // Match => add replacement
            if(rep!=null) buf.append(rep);
            i+=lp;
        }
        return buf;
    }

    /**
     * Replace a subsequence by an other in a CharSequence.
     * <p/>
     * Appends each character of the source sequence to the buffer,
     * except for segments that match the pattern, for which the
     * replacement is appended instead.
     *
     * @param buf the buffer to append the result to
     * @param str the source CharSequence
     * @param pat the subsequence to replace in the source
     * @param rep the subsequence to replace matches with
     *
     * @return the original buffer, for chaining purposes
     */
    public static StringBuilder repl(StringBuilder buf, CharSequence str, char[] pat, char[] rep) {
        final int lp=pat.length;
        final int lr=rep.length;
        final int ls=str.length();
        // Pattern finding loop
        find:
        for(int i=0;i<ls;) {
            // Look for a local match starting at i
            for(int j=0;j<lp;j++) {
                if(i+j>=ls||str.charAt(i+j)!=pat[j]) {
                    // No match => add current char, restart match
                    buf.append(str.charAt(i++));
                    continue find;
                }
            }
            // Match => add replacement
            if(rep!=null) for(int j=0;j<lr;j++) buf.append(rep[j]);
            i+=lp;
        }
        return buf;
    }

    /**
     * Replace a subsequence by an other in a CharSequence.
     * <p/>
     * Appends each character of the source sequence to the buffer,
     * except for segments that match the pattern, for which the
     * replacement is appended instead.
     *
     * @param buf the buffer to append the result to
     * @param str the source CharSequence
     * @param pat the subsequence to replace in the source
     * @param rep the subsequence to replace matches with
     *
     * @return the original buffer, for chaining purposes
     *
     * @throws IOException if the output buffer raises this exception on {@code append()}
     */
    public static <A extends Appendable> A repl(A buf, CharSequence str, char[] pat, char[] rep) throws IOException {
        final int lp=pat.length;
        final int lr=rep.length;
        final int ls=str.length();
        // Pattern finding loop
        find:
        for(int i=0;i<ls;) {
            // Look for a local match starting at i
            for(int j=0;j<lp;j++) {
                if(i+j>=ls||str.charAt(i+j)!=pat[j]) {
                    // No match => add current char, restart match
                    buf.append(str.charAt(i++));
                    continue find;
                }
            }
            // Match => add replacement
            if(rep!=null) for(int j=0;j<lr;j++) buf.append(rep[j]);
            i+=lp;
        }
        return buf;
    }

    /**
     * Replace a subsequence by an other in a segment of a character array.
     * <p/>
     * Appends each character of the source segment to the buffer,
     * except for segments that match the pattern, for which the
     * replacement is appended instead.
     *
     * @param buf the buffer to append the result to
     * @param str the source character array
     * @param pos the position of the segment in the array
     * @param len the length of the segment
     * @param pat the subsequence to replace in the segment
     * @param rep the subsequence to replace matches with
     *
     * @return the original buffer, for chaining purposes
     */
    public static StringBuilder repl(StringBuilder buf, char[] str, int pos, int len, char[] pat, char[] rep) {
        if(pos>=str.length) throw new ArrayIndexOutOfBoundsException(pos);
        len+=pos;
        if(len>str.length) throw new ArrayIndexOutOfBoundsException(pos);
        final int lp=pat.length;
        // Pattern finding loop
        find:
        for(;pos<len;) {
            // Look for a local match starting at i
            for(int j=0;j<lp;j++) {
                if(pos+j>=len||str[pos+j]!=pat[j]) {
                    // No match => add current char, restart match
                    buf.append(str[pos++]);
                    continue find;
                }
            }
            // Match => add replacement
            if(rep!=null) for(int j=0;j<rep.length;j++) buf.append(rep[j]);
            pos+=lp;
        }
        return buf;
    }

    /**
     * Replace a subsequence by an other in a segment of a character array.
     * <p/>
     * Appends each character of the source segment to the buffer,
     * except for segments that match the pattern, for which the
     * replacement is appended instead.
     *
     * @param buf the buffer to append the result to
     * @param str the source character array
     * @param pos the position of the segment in the array
     * @param len the length of the segment
     * @param pat the subsequence to replace in the segment
     * @param rep the subsequence to replace matches with
     *
     * @return the original buffer, for chaining purposes
     *
     * @throws IOException if the output buffer raises this exception on {@code append()}
     */
    public static <A extends Appendable> A repl(A buf, char[] str, int pos, int len, char[] pat, char[] rep) throws IOException {
        if(pos>=str.length) throw new ArrayIndexOutOfBoundsException(pos);
        len+=pos;
        if(len>str.length) throw new ArrayIndexOutOfBoundsException(pos);
        final int lp=pat.length;
        // Pattern finding loop
        find:
        for(;pos<len;) {
            // Look for a local match starting at i
            for(int j=0;j<lp;j++) {
                if(pos+j>=len||str[pos+j]!=pat[j]) {
                    // No match => add current char, restart match
                    buf.append(str[pos++]);
                    continue find;
                }
            }
            // Match => add replacement
            if(rep!=null) for(int j=0;j<rep.length;j++) buf.append(rep[j]);
            pos+=lp;
        }
        return buf;
    }

    /**
     * Replace multiple subsequences in a CharSequence.
     * <p/>
     * Appends each character of the source sequence to the buffer,
     * except for segments that match one of the pattern, for which the
     * sequence at the same position in the replacement array is appended instead.
     *
     * @param buf the buffer to append the result to
     * @param str the source CharSequence
     * @param pat the array of CharSequence to replace in the source
     * @param rep the array of CharSequence to replace corresponding matches with
     *
     * @return the original buffer, for chaining purposes
     */
    public static StringBuilder repl(StringBuilder buf, CharSequence str, CharSequence[] pat, CharSequence[] rep) {
        final int np=pat.length;
        final int ls=str.length();
        // Pattern finding loop
        find:
        for(int i=0;i<ls;) {
            match:
            for(int k=0;k<np;k++) {
                CharSequence sp=pat[k];
                final int lp=sp.length();
                // Look for a local match starting at i
                for(int j=0;j<lp;j++) {
                    if(i+j>=ls||str.charAt(i+j)!=sp.charAt(j)) {
                        // No match for pattern => go to next pattern
                        continue match;
                    }
                }
                // Match => add replacement, skip pattern
                if(rep!=null) buf.append(rep[k]);
                i+=lp;
                continue find;
            }
            // No match => add current char, restart match
            buf.append(str.charAt(i++));
        }
        return buf;
    }

    /**
     * Replace multiple subsequences in a CharSequence.
     * <p/>
     * Appends each character of the source sequence to the buffer,
     * except for segments that match one of the pattern, for which the
     * sequence at the same position in the replacement array is appended instead.
     *
     * @param buf the buffer to append the result to
     * @param str the source CharSequence
     * @param pat the array of CharSequence to replace in the source
     * @param rep the array of CharSequence to replace corresponding matches with
     *
     * @return the original buffer, for chaining purposes
     *
     * @throws IOException if the output buffer raises this exception on {@code append()}
     */
    public static <A extends Appendable> A repl(A buf, CharSequence str, CharSequence[] pat, CharSequence[] rep) throws IOException {
        final int np=pat.length;
        final int ls=str.length();
        // Pattern finding loop
        find:
        for(int i=0;i<ls;) {
            match:
            for(int k=0;k<np;k++) {
                CharSequence sp=pat[k];
                final int lp=sp.length();
                // Look for a local match starting at i
                for(int j=0;j<lp;j++) {
                    if(i+j>=ls||str.charAt(i+j)!=sp.charAt(j)) {
                        // No match for pattern => go to next pattern
                        continue match;
                    }
                }
                // Match => add replacement, skip pattern
                if(rep!=null) buf.append(rep[k]);
                i+=lp;
                continue find;
            }
            // No match => add current char, restart match
            buf.append(str.charAt(i++));
        }
        return buf;
    }

    /**
     * Replace multiple subsequences in a CharSequence.
     * <p/>
     * Appends each character of the source sequence to the buffer,
     * except for segments that match one of the pattern, for which the
     * sequence at the same position in the replacement array is appended instead.
     *
     * @param buf the buffer to append the result to
     * @param str the source CharSequence
     * @param pat the array of subsequences to replace in the source
     * @param rep the array of subsequences to replace corresponding matches with
     *
     * @return the original buffer, for chaining purposes
     */
    public static StringBuilder repl(StringBuilder buf, CharSequence str, char[][] pat, char[][] rep) {
        final int np=pat.length;
        final int ls=str.length();
        // Pattern finding loop
        find:
        for(int i=0;i<ls;) {
            match:
            for(int k=0;k<np;k++) {
                char[] sp=pat[k];
                final int lp=sp.length;
                // Look for a local match starting at i
                for(int j=0;j<lp;j++) {
                    if(i+j>=ls||str.charAt(i+j)!=sp[j]) {
                        // No match for pattern => go to next pattern
                        continue match;
                    }
                }
                // Match => add replacement (if any), skip pattern
                if(rep!=null&&k<rep.length) {
                    char[] sr=rep[k];
                    final int lr=sr.length;
                    for(int j=0;j<lr;j++) buf.append(sr[j]);
                }
                i+=lp;
                continue find;
            }
            // No match => add current char, restart match
            buf.append(str.charAt(i++));
        }
        return buf;
    }

    /**
     * Replace multiple subsequences in a CharSequence.
     * <p/>
     * Appends each character of the source sequence to the buffer,
     * except for segments that match one of the pattern, for which the
     * sequence at the same position in the replacement array is appended instead.
     *
     * @param buf the buffer to append the result to
     * @param str the source CharSequence
     * @param pat the array of subsequences to replace in the source
     * @param rep the array of subsequences to replace corresponding matches with
     *
     * @return the original buffer, for chaining purposes
     *
     * @throws IOException if the output buffer raises this exception on {@code append()}
     */
    public static <A extends Appendable> A repl(A buf, CharSequence str, char[][] pat, char[][] rep) throws IOException {
        final int np=pat.length;
        final int ls=str.length();
        // Pattern finding loop
        find:
        for(int i=0;i<ls;) {
            match:
            for(int k=0;k<np;k++) {
                char[] sp=pat[k];
                final int lp=sp.length;
                // Look for a local match starting at i
                for(int j=0;j<lp;j++) {
                    if(i+j>=ls||str.charAt(i+j)!=sp[j]) {
                        // No match for pattern => go to next pattern
                        continue match;
                    }
                }
                // Match => add replacement (if any), skip pattern
                if(rep!=null&&k<rep.length) {
                    char[] sr=rep[k];
                    final int lr=sr.length;
                    for(int j=0;j<lr;j++) buf.append(sr[j]);
                }
                i+=lp;
                continue find;
            }
            // No match => add current char, restart match
            buf.append(str.charAt(i++));
        }
        return buf;
    }

    /**
     * Replace multiple subsequences in a segment of a character array.
     * <p/>
     * Appends each character of the source segment to the buffer,
     * except for segments that match one of the pattern, for which the
     * sequence at the same position in the replacement array is appended instead.
     *
     * @param buf the buffer to append the result to
     * @param str the source character array
     * @param pos the position of the segment in the array
     * @param len the length of the segment
     * @param pat the array of subsequences to replace in the source
     * @param rep the array of subsequences to replace corresponding matches with
     *
     * @return the original buffer, for chaining purposes
     */
    public static StringBuilder repl(StringBuilder buf, char[] str, int pos, int len, char[][] pat, char[][] rep) {
        if(pos>=str.length) throw new ArrayIndexOutOfBoundsException(pos);
        len+=pos;
        if(len>str.length) throw new ArrayIndexOutOfBoundsException(pos);
        final int np=pat.length;
        // Pattern finding loop
        find:
        for(;pos<len;) {
            match:
            for(int k=0;k<np;k++) {
                char[] sp=pat[k];
                final int lp=sp.length;
                // Look for a local match starting at i
                for(int j=0;j<lp;j++) {
                    if(pos+j>=len||str[pos+j]!=sp[j]) {
                        // No match for pattern => go to next pattern
                        continue match;
                    }
                }
                // Match => add replacement (if any), skip pattern
                if(rep!=null&&k<rep.length) {
                    char[] sr=rep[k];
                    final int lr=sr.length;
                    for(int j=0;j<lr;j++) buf.append(sr[j]);
                }
                pos+=lp;
                continue find;
            }
            // No match => add current char, restart match
            buf.append(str[pos++]);
        }
        return buf;
    }

    /**
     * Replace multiple subsequences in a segment of a character array.
     * <p/>
     * Appends each character of the source segment to the buffer,
     * except for segments that match one of the pattern, for which the
     * sequence at the same position in the replacement array is appended instead.
     *
     * @param buf the buffer to append the result to
     * @param str the source character array
     * @param pos the position of the segment in the array
     * @param len the length of the segment
     * @param pat the array of subsequences to replace in the source
     * @param rep the array of subsequences to replace corresponding matches with
     *
     * @return the original buffer, for chaining purposes
     *
     * @throws IOException if the output buffer raises this exception on {@code append()}
     */
    public static <A extends Appendable> A repl(A buf, char[] str, int pos, int len, char[][] pat, char[][] rep) throws IOException {
        if(pos>=str.length) throw new ArrayIndexOutOfBoundsException(pos);
        len+=pos;
        if(len>str.length) throw new ArrayIndexOutOfBoundsException(pos);
        final int np=pat.length;
        // Pattern finding loop
        find:
        for(;pos<len;) {
            match:
            for(int k=0;k<np;k++) {
                char[] sp=pat[k];
                final int lp=sp.length;
                // Look for a local match starting at i
                for(int j=0;j<lp;j++) {
                    if(pos+j>=len||str[pos+j]!=sp[j]) {
                        // No match for pattern => go to next pattern
                        continue match;
                    }
                }
                // Match => add replacement (if any), skip pattern
                if(rep!=null&&k<rep.length) {
                    char[] sr=rep[k];
                    final int lr=sr.length;
                    for(int j=0;j<lr;j++) buf.append(sr[j]);
                }
                pos+=lp;
                continue find;
            }
            // No match => add current char, restart match
            buf.append(str[pos++]);
        }
        return buf;
    }

    public static String repl(String str, String pat, String rep) {
        return repl(new StringBuilder(), str, pat, rep).toString();
    }

    public static String repl(String str, String[] pat, String[] rep) {
        return repl(new StringBuilder(), str, pat, rep).toString();
    }


    /*********************************************************************************
     **  Field level manipulations
     **/


    /************************
     **  Single field extraction
     **/

    /**
     * Cuts a segment of a char array at the first occurrence of a delimiter.
     *
     * @param s a char array containing the segment
     * @param p the position in the array of the first character of the segment
     * @param l the length of (number of characters in) the segment
     * @param c the delimiter character
     *
     * @return a copy of the initial portion of the segment, ending at the first
     *         Occurrence of the delimiter, or a copy of the whole segment if the delimiter
     *         does not occur
     */
    public static char[] chop(char[] s, int p, int l, char c) {
        for(int i=0;i<l;i++) {
            if(s[p+i]==c) {
                char[] t=new char[i];
                System.arraycopy(s, p, t, 0, i);
                return t;
            }
        }
        char[] t=new char[l];
        System.arraycopy(s, p, t, 0, l);
        return t;
    }

    /**
     * Cuts a char array at the first occurrence of a delimiter.
     *
     * @param s a char array
     * @param c the delimiter character
     *
     * @return a copy of the initial portion of the char array ending at the first
     *         Occurrence of the delimiter, or a copy of the whole array if the delimiter
     *         does not occur
     */
    public static char[] chop(char[] s, char c) {
        for(int i=0;i<s.length;i++) {
            if(s[i]==c) {
                char[] t=new char[i];
                System.arraycopy(s, 0, t, 0, i);
                return t;
            }
        }
        char[] t=new char[s.length];
        System.arraycopy(s, 0, t, 0, s.length);
        return t;
    }

    /**
     * Cuts a character sequence at the first occurrence of a delimiter.
     *
     * @param s a character sequence
     * @param c the delimiter character
     *
     * @return the initial portion of the character sequence ending at the first
     *         Occurrence of the delimiter, or the whole character sequence if the delimiter
     *         does not occur
     */
    public static CharSequence chop(CharSequence s, char c) {
        for(int i=0;i<s.length();i++) {
            if(s.charAt(i)==c) return s.subSequence(0, i);
        }
        return s;
    }

    /**
     * Cuts a string at the first occurrence of a delimiter.
     *
     * @param s a string
     * @param c the delimiter character
     *
     * @return the initial portion of the string ending at the first occurrence
     *         of the delimiter, or the whole string if the delimiter does not occur
     */
    public static String chop(String s, char c) {
        int p=s.indexOf(c);
        if(p>=0) return s.substring(0, p);
        return s;
    }


    /************************
     **  Field array extraction
     **/

    /**
     * Splits a segment of a char array into fields, at occurrences of a delimiter.
     *
     * @param s a char array containing the segment
     * @param p the position in the array of the first character of the segment
     * @param l the length of (number of characters in) the segment
     * @param c the delimiter character
     * @param n the maximum number of fields to extract
     *
     * @return an array containing at most {@code n} elements, which are the portions of the
     *         segment between occurrences of the delimiter. If there are more than {@code n} of those,
     *         the last element of the array contains the remaining characters of the segment after the
     *         {@code n}-th occurrence of the delimiter.
     */
    public static char[][] split(char[] s, final int p, final int l, char c, int n) {
        if(n<0) return null;
        if(p>=s.length) throw new ArrayIndexOutOfBoundsException(p);
        if(p+l>s.length) throw new ArrayIndexOutOfBoundsException(p);
        if(n==1) {
            char[] x=new char[l];
            System.arraycopy(s, p, x, 0, l);
            return new char[][] { x };
        }
        int m=1;
        for(int i=0;i<l;i++) if(s[p+i]==c) m++;
        if(m<n) n=m;
        char[][] b=new char[n][];
        n--;
        m=0;
        int j=-1;         // Start at -1 (a virtual first delimiter is before the first char)
        for(int i=0;m<n&&i<l;i++)
            if(s[p+i]==c) {
                j++;            // Skip last delimiter (or get to 0 from -1)
                char[] x=new char[i-j];
                System.arraycopy(s, p+j, x, 0, i-j);
                b[m++]=x;
                j=i;
            }
        if(j<l) {
            j++;            // Skip last delimiter (or get to 0 from -1)
            char[] x=new char[l-j];
            System.arraycopy(s, p+j, x, 0, l-j);
            b[m]=x;
        }
        else b[m]=new char[0];
        return b;
    }

    /**
     * Splits a char array into fields, at occurrences of a delimiter.
     *
     * @param s a char array
     * @param c the delimiter character
     * @param n the maximum number of fields to extract
     *
     * @return an array containing at most {@code n} elements, which are the portions of the
     *         char array between occurrences of the delimiter. If there are more than {@code n} of those,
     *         the last element of the array contains the remaining characters of the array after the
     *         {@code n}-th occurrence of the delimiter.
     */
    public static char[][] split(char[] s, char c, int n) {
        if(n<0) return null;
        final int l=s.length;
        if(n==1) {
            char[] x=new char[l];
            System.arraycopy(s, 0, x, 0, l);
            return new char[][] { x };
        }
        int m=1;
        for(int i=0;i<l;i++) if(s[i]==c) m++;
        if(m<n) n=m;
        char[][] b=new char[n][];
        n--;
        m=0;
        int j=-1;         // Start at -1 (a virtual first delimiter is before the first char)
        for(int i=0;m<n&&i<l;i++)
            if(s[i]==c) {
                j++;            // Skip last delimiter (or get to 0 from -1)
                char[] x=new char[i-j];
                System.arraycopy(s, j, x, 0, i-j);
                b[m++]=x;
                j=i;
            }
        if(j<l) {
            j++;            // Skip last delimiter (or get to 0 from -1)
            char[] x=new char[l-j];
            System.arraycopy(s, j, x, 0, l-j);
            b[m]=x;
        }
        else b[m]=new char[0];
        return b;
    }

    /**
     * Splits a character sequence into fields, at occurrences of a delimiter.
     *
     * @param s a character sequence
     * @param c the delimiter character
     * @param n the maximum number of fields to extract
     *
     * @return an array containing at most {@code n} elements, which are the portions of the
     *         argument character sequence between occurrences of the delimiter. If there are more than
     *         {@code n} of those, the last element of the array contains the remaining of the character
     *         sequence after the {@code n}-th occurrence of the delimiter.
     */
    public static CharSequence[] split(CharSequence s, char c, int n) {
        if(n<0) return null;
        if(n==1) return new CharSequence[] { s };
        int m=1;
        int l=s.length();
        for(int i=0;i<l;i++) if(s.charAt(i)==c) m++;
        if(m<n) n=m;
        CharSequence[] b=new CharSequence[n];
        n--;
        m=0;
        int j=-1;
        for(int i=0;m<n&&i<l;i++)
            if(s.charAt(i)==c) {
                b[m++]=s.subSequence(j+1, i);
                j=i;
            }
        if(j<l) b[m]=s.subSequence(j+1, l);
        else b[m]="";
        return b;
    }

    /**
     * Splits a string into fields, at occurrences of a delimiter.
     *
     * @param s a string
     * @param c the delimiter character
     * @param n the maximum number of fields to extract
     *
     * @return an array containing at most {@code n} elements, which are the portions of the
     *         argument string between occurrences of the delimiter. If there are more than {@code n}
     *         of those, the last element of the array contains the remaining of the string after
     *         the {@code n}-th occurrence of the delimiter.
     */
    public static String[] split(String s, char c, int n) {
        if(n<0) return null;
        if(n==1) return new String[] { s };
        int m=1;
        for(int i=0;i<s.length();i++) if(s.charAt(i)==c) m++;
        if(m<n) n=m;
        String[] b=new String[n];
        n--;
        m=0;
        int j=-1;
        for(int i=0;m<n&&i<s.length();i++)
            if(s.charAt(i)==c) {
                b[m++]=s.substring(j+1, i);
                j=i;
            }
        if(j<s.length()) b[m]=s.substring(j+1);
        else b[m]="";
        return b;
    }


    /************************
     **  Field array merging
     **/

    /**
     * Appends as strings the elements of an array, separating them with a given string.
     *
     * @param buf   the buffer to append the composed string to
     * @param pre   a sequence of characters to prepend to each field
     * @param app   a sequence of characters to append to each field
     * @param sep   the separator to use
     * @param array the array to concatenate
     *
     * @return a concatenation of the elements of the array, as string, and the separator
     */
    public static <T> StringBuilder join(StringBuilder buf, String pre, String app, String sep, T... array) {
        if(sep==null) for(int i=0;i<array.length;i++) {
            T t=array[i];
            if(t!=null) {
                if(pre!=null) buf.append(pre);
                buf.append(t.toString());
                if(app!=null) buf.append(app);
            }
        }
        else for(int i=0;i<array.length;i++) {
            if(i>0) buf.append(sep);
            T t=array[i];
            if(t!=null) {
                if(pre!=null) buf.append(pre);
                buf.append(t.toString());
                if(app!=null) buf.append(app);
            }
        }
        return buf;
    }

    /**
     * Appends as strings the elements of an array, separating them with a given string.
     *
     * @param buf   the buffer to append the composed string to
     * @param pre   a sequence of characters to prepend to each field
     * @param app   a sequence of characters to append to each field
     * @param sep   the separator to use
     * @param array the array to concatenate
     *
     * @return the original buffer, for chaining purposes
     *
     * @throws IOException if the output buffer raises this exception on {@code append()}
     */
    public static <T,A extends Appendable> A join(A buf, String pre, String app, String sep, T... array) throws IOException {
        if(sep==null) for(int i=0;i<array.length;i++) {
            T t=array[i];
            if(t!=null) {
                if(pre!=null) buf.append(pre);
                buf.append(t.toString());
                if(app!=null) buf.append(app);
            }
        }
        else for(int i=0;i<array.length;i++) {
            if(i>0) buf.append(sep);
            T t=array[i];
            if(t!=null) {
                if(pre!=null) buf.append(pre);
                buf.append(t.toString());
                if(app!=null) buf.append(app);
            }
        }
        return buf;
    }

    /**
     * Appends as strings the elements of an array, separating them with a given string.
     *
     * @param buf   the buffer to append the composed string to
     * @param sep   the separator to use
     * @param array the array to concatenate
     *
     * @return a concatenation of the elements of the array, as string, and the separator
     */
    public static <T> StringBuilder join(StringBuilder buf, String sep, T... array) {
        if(sep==null) for(int i=0;i<array.length;i++) {
            T t=array[i];
            if(t!=null) buf.append(t.toString());
        }
        else for(int i=0;i<array.length;i++) {
            if(i>0) buf.append(sep);
            T t=array[i];
            if(t!=null) buf.append(t.toString());
        }
        return buf;
    }

    /**
     * Appends as strings the elements of an array, separating them with a given string.
     *
     * @param buf   the buffer to append the composed string to
     * @param sep   the separator to use
     * @param array the array to concatenate
     *
     * @return the original buffer, for chaining purposes
     *
     * @throws IOException if the output buffer raises this exception on {@code append()}
     */
    public static <T,A extends Appendable> A join(A buf, String sep, T... array) throws IOException {
        if(sep==null) for(int i=0;i<array.length;i++) {
            T t=array[i];
            if(t!=null) buf.append(t.toString());
        }
        else for(int i=0;i<array.length;i++) {
            if(i>0) buf.append(sep);
            T t=array[i];
            if(t!=null) buf.append(t.toString());
        }
        return buf;
    }

    /**
     * Appends as strings the elements of an array, separating them with a given string.
     *
     * @param pre   a sequence of characters to prepend to each field
     * @param app   a sequence of characters to append to each field
     * @param sep   the separator to use
     * @param array the array to concatenate
     *
     * @return a human-readable string exposing the contents of the array
     */
    public static <T> String join(String pre, String app, String sep, T... array) {
        return join(new StringBuilder(), pre, app, sep, array).toString();
    }

    /**
     * Appends as strings the elements of an array, separating them with a given string.
     *
     * @param sep   the separator to use
     * @param array the array to concatenate
     *
     * @return a human-readable string exposing the contents of the array
     */
    public static <T> String join(String sep, T... array) {
        return join(new StringBuilder(), sep, array).toString();
    }


    /************************
     **  Complex field extraction
     **/

    /**
     * Splits a string into "command-line" fields.
     * <p/>
     * The string is cut in separate substrings at every white-space, except when those
     * spaces are escaped, or are contained within literal sequences.
     * <p/>
     * "Escaped" characters, i.e. characters prefixed by an (unescaped) '\' backslash
     * character, are "unescaped" (the backslash is removed).
     * <p/>
     * "Literal" sequences are sequences starting and ending with the same (unescaped)
     * delimiter (one of '\'' or '\"'), in which all occurrences of that same delimiter
     * are escaped.
     *
     * @param s the String containing the concatenated fields
     *
     * @return an array containing the command-line fields
     */
    public static String[] splitCmd(String s) {
        int l=s.length();
        while(l>0) {
            if(Character.isSpaceChar(s.charAt(l-1))) l--;
            else break;
        }
        int n=1;
        boolean isEscape=false;
        char sepChar=' ';
        for(int i=0;i<l;i++) {
            char c=s.charAt(i);
            if(c=='\\') isEscape=!isEscape;
            else {
                if(isEscape) isEscape=false;
                else {
                    if(sepChar=='\''||sepChar=='\"') {
                        if(c==sepChar) sepChar=' ';
                    }
                    else if(Character.isSpaceChar(c)) {
                        n++;
                    }
                    else if(c=='\''||c=='\"') {
                        sepChar=c;
                    }
                }
            }
        }
        String[] b=new String[n];
        int p=0;
        int j=0;
        isEscape=false;
        sepChar=' ';
        for(int i=0;i<l;i++) {
            char c=s.charAt(i);
            if(c=='\\') isEscape=!isEscape;
            else {
                if(isEscape) {
                    if(b[p]==null) b[p]=s.substring(j, i-1);
                    else b[p]+=s.substring(j, i-1);
                    j=i;
                    isEscape=false;
                }
                else {
                    if(sepChar=='\''||sepChar=='\"') {
                        if(c==sepChar) {
                            sepChar=' ';
                            if(b[p]==null) b[p]=s.substring(j, i);
                            else b[p]+=s.substring(j, i);
                            j=i+1;
                        }
                    }
                    else if(Character.isSpaceChar(c)) {
                        if(i>j) {
                            if(b[p]==null) b[p++]=s.substring(j, i);
                            else b[p++]+=s.substring(j, i);
                        }
                        else if(b[p]!=null) p++;
                        j=i+1;
                    }
                    else if(c=='\''||c=='\"') {
                        if(i>j) {
                            if(b[p]==null) b[p]=s.substring(j, i);
                            else b[p]+=s.substring(j, i);
                        }
                        sepChar=c;
                        j=i+1;
                    }
                }
            }
        }
        if(p<b.length) {
            if(j<l) {
                if(b[p]==null) b[p]=s.substring(j, l);
                else b[p]+=s.substring(j, l);
            }
            else if(b[p]==null) b[p]="";
        }
        return b;
    }


    /************************
     **  Character sequence formatting
     **/

    /**
     * All possible lower-case chars for representing a number as a String
     */
    private final static char[] digits={
            '0', '1', '2', '3', '4', '5',
            '6', '7', '8', '9', 'a', 'b',
            'c', 'd', 'e', 'f', 'g', 'h',
            'i', 'j', 'k', 'l', 'm', 'n',
            'o', 'p', 'q', 'r', 's', 't',
            'u', 'v', 'w', 'x', 'y', 'z',
    };
    /**
     * All possible upper-case chars for representing a number as a String
     */
    private final static char[] DIGITS={
            '0', '1', '2', '3', '4', '5',
            '6', '7', '8', '9', 'A', 'B',
            'C', 'D', 'E', 'F', 'G', 'H',
            'I', 'J', 'K', 'L', 'M', 'N',
            'O', 'P', 'Q', 'R', 'S', 'T',
            'U', 'V', 'W', 'X', 'Y', 'Z',
    };

    /**
     * Formats a character sequence and a variadic array of arguments into a buffer.
     * <p/>
     * All characters from the format are added to the buffer, except those within an escape sequence.
     * Escape sequences start with a '%' character, can specify several optional parameters, and end
     * with a format type character. Each escape sequence consumes one argument from the array, and
     * transforms that argument into a format item, appended to the buffer, according to the type and
     * parameters of the escape sequence.
     * <p/>
     * Accepted format types are:
     * <ul>
     * <li/> <b>'%'</b>: outputs a literal '%' character, <em>without consuming an argument</em>
     * <li/> <b>'c'</b>: formats the argument (which must be of type {@link java.lang.Character}) as a single character
     * <li/> <b>'s'</b>: formats the argument (which must be of type {@link java.lang.CharSequence}) as a character sequence
     * <li/> <b>'S'</b>: formats the argument (which must be {@literal null}, or of type {@link java.lang.CharSequence}) as a character sequence, outputting {@code "null"} if it is {@literal null}
     * <li/> <b>'d'</b>: formats the argument (which must be of type {@link java.lang.Number}) as a signed decimal integer
     * <li/> <b>'u'</b>: formats the argument (which must be of type {@link java.lang.Number}) as an unsigned decimal integer
     * <li/> <b>'x'</b>: formats the argument (which must be of type {@link java.lang.Number}) as an hexadecimal integer, using lower case letters
     * <li/> <b>'X'</b>: formats the argument (which must be of type {@link java.lang.Number}) as an hexadecimal integer, using upper case letters
     * </ul>
     * All other format types result in the argument being converted to a String using {@link java.lang.Object#toString()}, and the result being
     * formatted as though an 'S' format had been specified.
     * <p/>
     * Parameters in order, any of the optional following:
     * <ul>
     * <li/> <b>'&lt;'</b> or <b>'&gt;'</b>: an orientation specifier, defining the alignment direction of the formatting: respectively left, or right (default: right)
     * <li/> <b>'|'</b>: a flag indicating that if the formatting results in more characters than the specified size, it should be truncated (default: none)
     * <li/> <b>'+'</b> or <b>'-'</b>: specifies that signed formatting of a positive number should respectively include or omit a leading '+' character (default: omit)
     * <li/> a <b>decimal integer</b> indicating the size (number of characters) of the formatted result
     * <li/> <b>'.'</b> (a dot) followed by a <b>decimal integer</b> indicating:
     * <ul>
     * <li/> for unsigned decimal integers: the radix (default: 10),
     * <li/> for hexadecimal integers: the shift (the number of bits that each digit represents, i.e. the base-2 logarithm of the radix)
     * </ul>
     * </ul>
     * <p/>
     *
     * @param buf the output buffer
     * @param fmt the format defining how the arguments should be written to the buffer
     * @param var the variadic array of arguments
     *
     * @throws IOException if appending to the buffer produced an I/O error
     */
    public static <A extends Appendable> A format(A buf, CharSequence fmt, Object... var) throws IOException {
        int a=0;
        final int n=fmt.length();
        char[] nbf=null;
        // Iterate on single characters / escapes
        for(int pos=0;pos<n;pos++) {
            char chr=fmt.charAt(pos);
            // If not escape, simply add char
            if(chr!='%') {
                buf.append(chr);
                continue;
            }
            if(++pos>=n) throw new IllegalArgumentException("Invalid format sequence in '"+fmt+"' at char "+pos);
            chr=fmt.charAt(pos);
            boolean sign=false;    // Whether a plus sign should be appended to positive numeric items
            boolean left=false;    // Whether item should be aligned left instead of right
            boolean trnc=false;    // Whether oversized items should be truncated
            if(chr=='<') {
                left=true;
                if(++pos>=n) throw new IllegalArgumentException("Invalid format sequence in '"+fmt+"' at char "+pos);
                chr=fmt.charAt(pos);
            }
            else if(chr=='>') {
                left=false;
                if(++pos>=n) throw new IllegalArgumentException("Invalid format sequence in '"+fmt+"' at char "+pos);
                chr=fmt.charAt(pos);
            }
            if(chr=='|') {
                trnc=true;
                if(++pos>=n) throw new IllegalArgumentException("Invalid format sequence in '"+fmt+"' at char "+pos);
                chr=fmt.charAt(pos);
            }
            if(chr=='+') {
                sign=true;
                if(++pos>=n) throw new IllegalArgumentException("Invalid format sequence in '"+fmt+"' at char "+pos);
                chr=fmt.charAt(pos);
            }
            else if(chr=='-') {
                sign=false;
                if(++pos>=n) throw new IllegalArgumentException("Invalid format sequence in '"+fmt+"' at char "+pos);
                chr=fmt.charAt(pos);
            }
            char pad='\0';
            // Look for a pad char -> 'c'
            if(((chr<='0')||(chr>'9'))
               &&((chr<'a')||(chr>'z'))
               &&((chr<'A')||(chr>'Z'))
               &&(chr!='%')) {
                pad=chr;
                if(++pos>=n) throw new IllegalArgumentException("Invalid format sequence in '"+fmt+"' at char "+pos);
                chr=fmt.charAt(pos);
            }
            // Look for display width
            int dsp=-1;
            if(chr=='*') {
                if(a>=var.length)
                    throw new IllegalArgumentException("Too few arguments for format * in '"+fmt+"' at char "+pos+", arg "+a);
                Object o=var[a++];
                if(!(o instanceof Number))
                    throw new IllegalArgumentException("Invalid argument for format * in '"+fmt+"' at char "+pos+", arg "+a);
                dsp=((Number) o).intValue();
                if(++pos>=n) throw new IllegalArgumentException("Invalid format sequence in '"+fmt+"' at char "+pos);
                chr=fmt.charAt(pos);
            }
            else if((chr>'0')&&(chr<='9')) {
                dsp=0;
                while((chr>='0')&&(chr<='9')) {
                    dsp*=10;
                    dsp+=chr-'0';
                    if(++pos>=n)
                        throw new IllegalArgumentException("Invalid format sequence in '"+fmt+"' at char "+pos);
                    chr=fmt.charAt(pos);
                }
            }
            // Look for display precision / radix / shift
            int pre=0;
            if(chr=='.') {
                if(++pos>=n) throw new IllegalArgumentException("Invalid format sequence in '"+fmt+"' at char "+pos);
                chr=fmt.charAt(pos);
                if(chr=='*') {
                    if(a>=var.length)
                        throw new IllegalArgumentException("Too few arguments for format * in '"+fmt+"' at char "+pos+", arg "+a);
                    Object o=var[a++];
                    if(!(o instanceof Number))
                        throw new IllegalArgumentException("Invalid argument for format * in '"+fmt+"' at char "+pos+", arg "+a);
                    dsp=((Number) o).intValue();
                    if(++pos>=n)
                        throw new IllegalArgumentException("Invalid format sequence in '"+fmt+"' at char "+pos);
                    chr=fmt.charAt(pos);
                }
                else if((chr>'0')&&(chr<='9')) {
                    while((chr>='0')&&(chr<='9')) {
                        pre*=10;
                        pre+=chr-'0';
                        if(++pos>=n)
                            throw new IllegalArgumentException("Invalid format sequence in '"+fmt+"' at char "+pos);
                        chr=fmt.charAt(pos);
                    }
                }
            }
            // Switch on format char
            switch(chr) {
                case '%': {     // Literal %
                    buf.append('%');
                }
                break;
                case 'c': {     // Char format
                    if(a>=var.length)
                        throw new IllegalArgumentException("Too few arguments for format %c in '"+fmt+"' at char "+pos+", arg "+a);
                    Object o=var[a++];
                    // Ignore padding and orientation
                    if(o==null)
                        throw new IllegalArgumentException("Null argument for format %c in '"+fmt+"' at char "+pos+", arg "+a);
                    if(o instanceof Character) buf.append((Character) o);
                    else if(o instanceof Integer) buf.append((char)((Integer) o).intValue());
                    else throw new IllegalArgumentException("Invalid argument for format %c in '"+fmt+"' at char "+pos+", arg "+a);
                }
                break;
                case 's':
                case 'S': {     // String format
                    if(a>=var.length)
                        throw new IllegalArgumentException("Too few arguments for format %s in '"+fmt+"' at char "+pos+", arg "+a);
                    Object o=var[a++];
                    if(o==null) {
                        if(chr!='S')
                            throw new IllegalArgumentException("Null argument for format %s in '"+fmt+"' at char "+pos+", arg "+a);
//                        else o="null";
                    }
                    else if(!(o instanceof CharSequence))
                        throw new IllegalArgumentException("Invalid argument for format %s in '"+fmt+"' at char "+pos+", arg "+a);
                    CharSequence s;
                    if(chr=='S') s = o==null?"null":("\""+o+"\"");
                    else s = (CharSequence) o;
                    // Pad with ' ' by default
                    if(pad=='\0') pad=' ';
                    if(dsp<0) {
                        buf.append(s);
                    }
                    else if(dsp>s.length()) {
                        if(left) {
                            dsp-=s.length();
                            buf.append(s);
                            while(dsp>0) {
                                buf.append(pad);
                                dsp--;
                            }
                        }
                        else {
                            dsp-=s.length();
                            while(dsp>0) {
                                buf.append(pad);
                                dsp--;
                            }
                            buf.append(s);
                        }
                    }
                    else if(trnc) {
                        if(left) buf.append(s, 0, dsp);
                        else buf.append(s, s.length()-dsp, s.length());
                    }
                    else buf.append(s);
                }
                break;
                case 'd': {     // Decimal signed integer
                    if(a>=var.length)
                        throw new IllegalArgumentException("Too few arguments for format %d in '"+fmt+"' at char "+pos+", arg "+a);
                    Object o=var[a++];
                    if(!(o instanceof Number))
                        throw new IllegalArgumentException("Invalid argument for format %d in '"+fmt+"' at char "+pos+", arg "+a);
                    long v=((Number) o).longValue();
                    long u; // Unsigned value
                    if(v<0) u=-v;
                    else u=v;
                    // Pad with ' ' by default
                    if(pad=='\0') pad=' ';
                    // Base is 10 by default
                    if(pre==0) pre=10;
                    if(nbf==null) nbf=new char[65];
                    int j=0;
                    while(j<65&&u!=0) {
                        nbf[j++]=(char) (u%pre+'0');
                        u/=pre;
                    }
                    if(dsp<0) {
                        // Append digits
                        while(j>0) buf.append(nbf[--j]);
                    }
                    else if(pad=='0') {    // '0' implies right align
                        // Append sign
                        if(v<0) {
                            buf.append('-');
                            dsp--;
                        }
                        else if(v>0&&sign) {
                            buf.append('+');
                            dsp--;
                        }
                        // Append padding
                        while(dsp>j) {
                            buf.append(pad);
                            dsp--;
                        }
                        // Append digits
                        while(j>0) buf.append(nbf[--j]);
                    }
                    else if(left) {
                        // Append sign
                        if(v<0) {
                            buf.append('-');
                            dsp--;
                        }
                        else if(v>0&&sign) {
                            buf.append('+');
                            dsp--;
                        }
                        // Append digits
                        dsp-=j;
                        while(j>0) buf.append(nbf[--j]);
                        // Append padding
                        while(dsp>0) {
                            buf.append(pad);
                            dsp--;
                        }
                    }
                    else {
                        // Reserve space for sign
                        if(v<0) { dsp--; }
                        else if(v>0&&sign) { dsp--; }
                        // Append padding
                        while(dsp>j) {
                            buf.append(pad);
                            dsp--;
                        }
                        // Append sign
                        if(v<0) { buf.append('-'); }
                        else if(v>0&&sign) { buf.append('+'); }
                        // Append digits
                        while(j>0) buf.append(nbf[--j]);
                    }
                }
                break;
                case 'u': {      // Decimal unsigned integer
                    if(a>=var.length)
                        throw new IllegalArgumentException("Too few arguments for format %u in '"+fmt+"' at char "+pos+", arg "+a);
                    Object o=var[a++];
                    if(!(o instanceof Number))
                        throw new IllegalArgumentException("Invalid argument for format %u in '"+fmt+"' at char "+pos+", arg "+a);
                    long u=((Number) o).longValue();
                    if(u<0) u=-u;
                    // Pad with ' ' by default
                    if(pad=='\0') pad=' ';
                    // Base is 10 by default
                    if(pre==0) pre=10;
                    if(nbf==null) nbf=new char[65];
                    int j=0;
                    while(j<65&&u!=0) {
                        nbf[j++]=(char) (u%pre+'0');
                        u/=pre;
                    }
                    if(dsp<0) {
                        // Append digits
                        while(j>0) buf.append(nbf[--j]);
                    }
                    else if(left) {
                        // Append digits
                        dsp-=j;
                        while(j>0) buf.append(nbf[--j]);
                        // Append padding
                        while(dsp>0) {
                            buf.append(pad);
                            dsp--;
                        }
                    }
                    else {
                        // Append padding
                        while(dsp>j) {
                            buf.append(pad);
                            dsp--;
                        }
                        // Append digits
                        while(j>0) buf.append(nbf[--j]);
                    }
                }
                break;
                case 'x':
                case 'X': {     // Hexadecimal unsigned integer
                    if(a>=var.length)
                        throw new IllegalArgumentException("Too few arguments for format %x in '"+fmt+"' at char "+pos+", arg "+a);
                    Object o=var[a++];
                    if(!(o instanceof Number))
                        throw new IllegalArgumentException("Invalid argument for format %x in '"+fmt+"' at char "+pos+", arg "+a);
                    long u=((Number) o).longValue();
                    if(u<0) u=-u;
                    // Pad with ' ' by default
                    if(pad=='\0') pad=' ';
                    // Shift is 4 by default
                    if(pre==0) pre=4;
                    if(pre>=6) pre=6;
                    if(nbf==null) nbf=new char[65];
                    int j=0;
                    int msk=(1<<pre)-1;
                    if(chr=='X') while(j<65&&u!=0) {
                        nbf[j++]=DIGITS[(int) (u&msk)];
                        u>>>=pre;
                    }
                    else while(j<65&&u!=0) {
                        nbf[j++]=digits[(int) (u&msk)];
                        u>>>=pre;
                    }
                    if(dsp<0) {
                        // Append digits
                        while(j>0) buf.append(nbf[--j]);
                    }
                    else if(left) {
                        // Append digits
                        while(j>0&&dsp>0) {
                            buf.append(nbf[--j]);
                            dsp--;
                        }
                        // If not truncating, write overflowing lower order digits
                        if(!trnc) while(j>0) buf.append(nbf[--j]);
                        // Append padding
                        while(dsp>0) {
                            buf.append(pad);
                            dsp--;
                        }
                    }
                    else {
                        // Append padding
                        while(dsp>j) {
                            buf.append(pad);
                            dsp--;
                        }
                        // If truncating and overflow, keep only lower order digits
                        if(trnc&&j>dsp) j=dsp;
                        // Append digits
                        while(j>0) buf.append(nbf[--j]);
                    }
                }
                break;
                default: {
                    if(a>=var.length)
                        throw new IllegalArgumentException("Too few arguments for format %s in '"+fmt+"' at char "+pos+", arg "+a);
                    Object o=var[a++];
                    String s=o==null ? "null" : o.toString();
                    // Pad with ' ' by default
                    if(pad=='\0') pad=' ';
                    if(dsp<0) {
                        buf.append(s);
                    }
                    else if(dsp>s.length()) {
                        if(left) {
                            dsp-=s.length();
                            buf.append(s);
                            while(dsp>0) {
                                buf.append(pad);
                                dsp--;
                            }
                        }
                        else {
                            dsp-=s.length();
                            while(dsp>0) {
                                buf.append(pad);
                                dsp--;
                            }
                            buf.append(s);
                        }
                    }
                    else if(trnc) {
                        if(left) buf.append(s, 0, dsp);
                        else buf.append(s, s.length()-dsp, s.length());
                    }
                    else buf.append(s);
                }
            } // End format character switch
        } // End item loop
        return buf;
    }

    /**
     * Formats character sequence and arguments into a buffer.
     * <p/>
     *
     * @param buf the output buffer
     * @param fmt the format defining how the arguments should be written to the buffer
     * @param var the variadic array of arguments
     *
     * @see {@link #format(Appendable, CharSequence, Object...)} for a complete description of syntax of the format sequence
     */
    public static StringBuilder format(StringBuilder buf, CharSequence fmt, Object... var) {
        try {
            format((Appendable) buf, fmt, var);
        }
        catch(IOException e) {
            /* That exception is never thrown by StringBuilder */
        }
        return buf;
    }

    /**
     * Formats character sequence and arguments into a string.
     * <p/>
     *
     * @param fmt the format defining how the arguments should be written to the buffer
     * @param var the variadic array of arguments
     *
     * @return the formatted string
     *
     * @see {@link #format(Appendable, CharSequence, Object...)} for a complete description of syntax of the format sequence
     */
    public static String format(CharSequence fmt, Object... var) {
        StringBuilder buf=new StringBuilder();
        try {
            format((Appendable) buf, fmt, var);
        }
        catch(IOException e) {
            /* That exception is never thrown by StringBuilder */
        }
        return buf.toString();
    }

}
