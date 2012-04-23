package net.varkhan.base.containers.array;

import junit.framework.TestCase;

import java.util.Random;


/**
 * <b></b>.
 * <p/>
 *
 * @author varkhan
 * @date 3/24/12
 * @time 9:39 PM
 */
public class ArraysTest extends TestCase {

    public static <T> void assertArrayEquals(String message, T[] expected, T[] actual) {
        if(!Arrays.equals(expected,actual)) fail(message+";\n expected: ["+StringArrays.join(",",expected)+"];\n   actual: ["+StringArrays.join(",",actual)+"]");
    }

    public void testEquals() throws Exception {
        assertTrue("equals([1,2,3,\"f\"],[1,2,3,\"f\"])",Arrays.equals(new Object[]{1, 2, 3, "f"}, new Object[]{1, 2, 3, "f"}));
        assertFalse("equals([1,2,3,\"f\"],[5,2,3,\"f\"])", Arrays.equals(new Object[] { 1, 2, 3, "f" }, new Object[] { 5, 2, 3, "f" }));
        assertFalse("equals([1,2,3,\"f\"],[1,2,3,null])", Arrays.equals(new Object[] { 1, 2, 3, "f" }, new Object[] { 5, 2, 3, null }));
        assertFalse("equals([1,2,3,\"f\"],[1,2,3,\"f\",10])", Arrays.equals(new Object[] { 1, 2, 3, "f" }, new Object[] { 5, 2, 3, "f", 10 }));
    }

    public void testIndexOf() throws Exception {
        assertEquals("isMember(null,1,2,3,null)", 3, Arrays.indexOf(null,1,2,3,null));
        assertEquals("isMember(null,null,2,3,4)", 0, Arrays.indexOf(null, null, 2, 3, 4));
        assertEquals("isMember(null,1,2,3,4)", -1, Arrays.indexOf(null, 1, 2, 3, 4));
        assertEquals("isMember(1,1,2,3,null)", 0, Arrays.indexOf(1, 1, 2, 3, null));
        assertEquals("isMember(4,1,2,3,null)", -1, Arrays.indexOf(4, 1, 2, 3, null));
        assertEquals("isMember(4,1,2,3,4)", 3, Arrays.indexOf(4, 1, 2, 3, 4));
    }

    public void testArraySort() throws Exception {
        Integer[] ary = {0};
        Arrays.heapSort(ary);
        assertArrayEquals("heapSort(0)", new Integer[] { 0 }, ary);
        ary = new Integer[]{3,2,1,1,4,4,6,5,7,2};
        Arrays.heapSort(ary);
        assertArrayEquals("heapSort(3,2,1,1,4,4,6,5,7,2)", new Object[] { 1, 1, 2, 2, 3, 4, 4, 5, 6, 7 }, ary);
        int N = 5000; // Max number of objects that will fit in normal heap size: 16*5k^2 = 400m
        Integer[][] a = new Integer[N][];
        Integer[][] a1 = new Integer[N][];
        Integer[][] a2 = new Integer[N][];
        Random rand = new Random();
        int n = 0;
        int c = 0;
        for(int i=0; i<N; i++) {
            int l = rand.nextInt(N);
            Integer[] s = new Integer[l];
            for(int j=0; j<l; j++) s[j]=rand.nextInt();
            a[i] = s;
            a1[i] = s.clone();
            a2[i] = s.clone();
            n += l;
        }
        System.out.println("Sorting "+N+" arrays of "+n+" elements");
        long t0 = System.currentTimeMillis();
        for(int i=0; i<N; i++) {
            c+=Arrays.heapSort(a1[i]);
        }
        long t1 = System.currentTimeMillis();
        for(int i=0; i<N; i++) {
            java.util.Arrays.sort(a2[i]);
        }
        long t2 = System.currentTimeMillis();
        for(int i=0; i<N; i++) {
            assertArrayEquals("sort("+StringArrays.join(",",a[i])+")",a2[i],a1[i]);
        }
        System.out.println("Sorted "+N+" arrays of "+n+" elements in "+(t1-t0)+"ms, "+c+" operations ("+(t2-t1)+"ms for java.util.Arrays.sort)");
    }

    public void testArrayIndexOf() throws Exception {

    }

    public void testSearch() throws Exception {

    }

    public void testInsert() throws Exception {

    }

    public void testAppend() throws Exception {

    }

    public void testPrepend() throws Exception {

    }

    public void testConcat() throws Exception {

    }

    public void testSubarray() throws Exception {
        assertArrayEquals("subarray([1,2,3,4,5,6],2,3)", new Object[] { 3 }, Arrays.subarray(new Object[] { 1, 2, 3, 4, 5, 6 }, 2, 3));
        assertArrayEquals("subarray([1,2,3,4,5,6],0,6)", new Object[] { 1, 2, 3, 4, 5, 6 }, Arrays.subarray(new Object[] { 1, 2, 3, 4, 5, 6 }, 0, 6));
        assertArrayEquals("subarray([1,2,3,4,5,6],1,1)", new Object[] { }, Arrays.subarray(new Object[] { 1, 2, 3, 4, 5, 6 }, 1, 1));
        assertArrayEquals("subarray([1,2,3,4,5,6],6,6)", new Object[] { }, Arrays.subarray(new Object[] { 1, 2, 3, 4, 5, 6 }, 6, 6));
    }

    public void testContainer() throws Exception {

    }

    public void testMapping() throws Exception {

    }
}
