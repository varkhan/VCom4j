package net.varkhan.base.containers.array;

import junit.framework.TestCase;
import net.varkhan.base.containers.type.LongContainer;
import net.varkhan.base.containers.type.LongIterable;

import java.util.Random;


/**
 * <b></b>.
 * <p/>
 *
 * @author varkhan
 * @date 3/24/12
 * @time 9:39 PM
 */
public class LongArraysTest extends TestCase {

    public static void assertArrayEquals(String message, long[] expected, long[] actual) {
        if(expected==null) { if(actual==null) return; }
        else if(expected.length==actual.length) {
            boolean same = true;
            for(int i=0; i<expected.length; i++) if(expected[i]!=actual[i]) { same=false; break; }
            if(same) return;
        }
        fail(message+";\n expected: ["+StringArrays.join(",", expected)+"];\n   actual: ["+StringArrays.join(",", actual)+"]");
    }

    public static long[] reverse(long[] a) {
        for(int i1=0, i2=a.length-1; i1<i2; i1++, i2--) {
            long t = a[i2];
            a[i2] = a[i1];
            a[i1] = t;
        }
        return a;
    }

    public void testEquals() throws Exception {
        assertTrue("equals([],[])", LongArrays.equals(new long[] { }, new long[] { }));
        assertTrue("equals([1,2,3,\"6\"],[1,2,3,\"6\"])", LongArrays.equals(new long[] { 1, 2, 3, 6L }, new long[] { 1, 2, 3, 6L }));
        assertFalse("equals([1,2,3,\"6\"],[5,2,3,\"6\"])", LongArrays.equals(new long[] { 1, 2, 3, 6L }, new long[] { 5, 2, 3, 6L }));
        assertFalse("equals([1,2,3,\"6\"],[1,2,3,Long.NaN])", LongArrays.equals(new long[] { 1, 2, 3, 6L }, new long[] { 5, 2, 3, 9999999L }));
        assertFalse("equals([1,2,3,\"6\"],[1,2,3,\"6\",10])", LongArrays.equals(new long[] { 1, 2, 3, 6L }, new long[] { 5, 2, 3, 6L, 10 }));
    }

    public void testIndexOf() throws Exception {
        assertEquals("indexOf(Long.NaN,1,2,\"3\",Long.NaN)", 3, LongArrays.indexOf(9999999L,1,2,3L,9999999L));
        assertEquals("indexOf(Long.NaN,Long.NaN,2,\"3\",4)", 0, LongArrays.indexOf(9999999L, 9999999L, 2, 3L, 4));
        assertEquals("indexOf(Long.NaN,1,2,\"3\",4)", -1, LongArrays.indexOf(9999999L, 1, 2, 3L, 4));
        assertEquals("indexOf(1,1,2,\"3\",Long.NaN)", 0, LongArrays.indexOf(1, 1, 2, 3L, 9999999L));
        assertEquals("indexOf(4,1,2,\"3\",Long.NaN)", -1, LongArrays.indexOf(4, 1, 2, 3L, 9999999L));
        assertEquals("indexOf(4,1,2,\"3\",4)", 3, LongArrays.indexOf(4, 1, 2, 3L, 4));
        assertEquals("indexOf(\"3\",1,2,\"3\",4)", 2, LongArrays.indexOf(3L, 1, 2, 3L, 4));
    }

    public void testIndexOfArray() throws Exception {
        assertEquals("indexOf([], 0, [1,2,3,6])", 0, LongArrays.indexOf(new long[]{}, 0, new long[]{ 1, 2, 3, 6 }));
        assertEquals("indexOf([2], 0, [1,2,3,6])", 1, LongArrays.indexOf(new long[]{2}, 0, new long[]{ 1, 2, 3, 6 }));
        assertEquals("indexOf([2,3], 0, [1,2,3,6])", 1, LongArrays.indexOf(new long[]{2, 3}, 0, new long[]{ 1, 2, 3, 6 }));
        assertEquals("indexOf([2,3], 2, [1,2,3,6])", -1, LongArrays.indexOf(new long[]{2, 3}, 2, new long[]{ 1, 2, 3, 6 }));
        assertEquals("indexOf([2,6], 0, [1,2,3,6])", -1, LongArrays.indexOf(new long[]{2, 6}, 0, new long[]{ 1, 2, 3, 6 }));
    }

    public void testSortDec() throws Exception {
        long[] ary = {0};
        LongArrays.heapSortDec(ary);
        assertArrayEquals("heapSort(0)", new long[] { 0 }, ary);
        ary = new long[]{3,2,1,1,4,4,6,5,7,2};
        LongArrays.heapSortDec(ary);
        assertArrayEquals("heapSort(3,2,1,1,4,4,6,5,7,2)", reverse(new long[] { 1, 1, 2, 2, 3, 4, 4, 5, 6, 7 }), ary);
        int N = 500; // Max number of objects that will fit in normal heap size: 16*500^2 = 4m
        long[][] a = new long[N][];
        long[][] a1 = new long[N][];
        long[][] a2 = new long[N][];
        Random rand = new Random();
        int n = 0;
        int c = 0;
        for(int i=0; i<N; i++) {
            int l = rand.nextInt(N);
            long[] s = new long[l];
            for(int j=0; j<l; j++) s[j]=rand.nextLong();
            a[i] = s;
            a1[i] = s.clone();
            a2[i] = s.clone();
            n += l;
        }
        System.out.println("Sorting "+N+" arrays of "+n+" elements");
        long t0 = System.currentTimeMillis();
        for(int i=0; i<N; i++) {
            c+=LongArrays.heapSortDec(a1[i]);
        }
        long t1 = System.currentTimeMillis();
        for(int i=0; i<N; i++) {
            java.util.Arrays.sort(a2[i]);
            reverse(a2[i]);
        }
        long t2 = System.currentTimeMillis();
        for(int i=0; i<N; i++) {
            assertArrayEquals("sort("+StringArrays.join(",",a[i])+")",a2[i],a1[i]);
        }
        System.out.println("Sorted "+N+" arrays of "+n+" elements in "+(t1-t0)+"ms, "+c+" operations ("+(t2-t1)+"ms for java.util.LongArrays.sort)");
    }

    public void testSortInc() throws Exception {
        long[] ary = {0};
        LongArrays.heapSortInc(ary);
        assertArrayEquals("heapSort(0)", new long[] { 0 }, ary);
        ary = new long[]{3,2,1,1,4,4,6,5,7,2};
        LongArrays.heapSortInc(ary);
        assertArrayEquals("heapSort(3,2,1,1,4,4,6,5,7,2)", new long[] { 1, 1, 2, 2, 3, 4, 4, 5, 6, 7 }, ary);
        int N = 500; // Max number of objects that will fit in normal heap size: 16*500^2 = 4m
        long[][] a = new long[N][];
        long[][] a1 = new long[N][];
        long[][] a2 = new long[N][];
        Random rand = new Random();
        int n = 0;
        int c = 0;
        for(int i=0; i<N; i++) {
            int l = rand.nextInt(N);
            long[] s = new long[l];
            for(int j=0; j<l; j++) s[j]=rand.nextLong();
            a[i] = s;
            a1[i] = s.clone();
            a2[i] = s.clone();
            n += l;
        }
        System.out.println("Sorting "+N+" arrays of "+n+" elements");
        long t0 = System.currentTimeMillis();
        for(int i=0; i<N; i++) {
            c+=LongArrays.heapSortInc(a1[i]);
        }
        long t1 = System.currentTimeMillis();
        for(int i=0; i<N; i++) {
            java.util.Arrays.sort(a2[i]);
        }
        long t2 = System.currentTimeMillis();
        for(int i=0; i<N; i++) {
            assertArrayEquals("sort("+StringArrays.join(",",a[i])+")",a2[i],a1[i]);
        }
        System.out.println("Sorted "+N+" arrays of "+n+" elements in "+(t1-t0)+"ms, "+c+" operations ("+(t2-t1)+"ms for java.util.LongArrays.sort)");
    }

    public void testSearchDec() throws Exception {
        long[] a1 = reverse(new long[] {});
        assertEquals("",-1,LongArrays.searchDec(a1, 0, 0, 1L));
        long[] a2 = reverse(new long[] { 2L});
        assertEquals("",-2,LongArrays.searchDec(a2, 0, 1, 3L));
        long[] a3 = reverse(new long[] { 2L});
        assertEquals("",-1,LongArrays.searchDec(a3, 0, 1, 1L));
        assertEquals("",0,LongArrays.searchDec(a3, 0, 1, 2L));
        long[] a4 = reverse(new long[]{2L, 3L, 4L, 7L, 8L});
        assertEquals("",-6,LongArrays.searchDec(a4, 0, 5, 5L));
        assertEquals("",2,LongArrays.searchDec(a4, 0, 5, 4L));
    }

    public void testSearchInc() throws Exception {
        long[] a1 = new long[] {};
        assertEquals("",-1,LongArrays.searchInc(a1, 0, 0, 1L));
        long[] a2 = new long[] { 2L};
        assertEquals("",-2,LongArrays.searchInc(a2, 0, 1, 3L));
        long[] a3 = new long[] { 2L};
        assertEquals("",-1,LongArrays.searchInc(a3, 0, 1, 1L));
        assertEquals("",0,LongArrays.searchInc(a3, 0, 1, 2L));
        long[] a4 = new long[]{2L, 3L, 4L, 7L, 8L};
        assertEquals("",-4,LongArrays.searchInc(a4, 0, 5, 5L));
        assertEquals("",2,LongArrays.searchInc(a4, 0, 5, 4L));
    }

//    public void testInsert() throws Exception {
//        long[] a1 = new long[] {Long.NaN};
//        assertEquals("",0,LongArrays.insert(a1,0,0,1L));
//        assertArrayEquals("",new long[]{1L},a1);
//        long[] a2 = new long[] { 2L, Long.NaN};
//        assertEquals("",1,LongArrays.insert(a2,0,1,3L));
//        assertArrayEquals("",new long[]{2L,3L},a2);
//        long[] a3 = new long[] { 2L, Long.NaN};
//        assertEquals("",0,LongArrays.insert(a3,0,1,1L));
//        assertArrayEquals("",new long[]{1L,2L},a3);
//        long[] a4 = new long[]{2L, 3L, 4L, 7L, 8L, Long.NaN};
//        assertEquals("",3,LongArrays.insert(a4,0,5,5L));
//        assertArrayEquals("",new long[]{2L, 3L, 4L, 5L, 7L, 8L},a4);
//    }

    public void testUnionDec() throws Exception {
        assertArrayEquals("",
                reverse(new long[] { }),
                LongArrays.unionDec(reverse(new long[] { }), 0, 0, reverse(new long[] { }), 0, 0));
        assertArrayEquals("",
                reverse(new long[]{2L, 3L, 4L, 5L, 6L}),
                LongArrays.unionDec(reverse(new long[] { }), 0, 0, reverse(new long[] { 2L, 3L, 4L, 5L, 6L }), 0, 5));
        assertArrayEquals("",
                reverse(new long[] { 1L, 2L, 3L, 4L, 5L, 6L, 7L }),
                LongArrays.unionDec(reverse(new long[] { 1L, 3L, 7L }), 0, 3, reverse(new long[] { 2L, 3L, 4L, 5L, 6L }), 0, 5));
    }

    public void testUnionInc() throws Exception {
        assertArrayEquals("",
                new long[]{},
                LongArrays.unionInc(new long[] { }, 0, 0, new long[] { }, 0, 0));
        assertArrayEquals("",
                new long[]{2L, 3L, 4L, 5L, 6L},
                LongArrays.unionInc(new long[] { }, 0, 0, new long[] { 2L, 3L, 4L, 5L, 6L }, 0, 5));
        assertArrayEquals("",
                new long[]{1L, 2L, 3L, 4L, 5L, 6L, 7L},
                LongArrays.unionInc(new long[] { 1L, 3L, 7L }, 0, 3, new long[] { 2L, 3L, 4L, 5L, 6L }, 0, 5));
    }

    public void testInterDec() throws Exception {
        assertArrayEquals("",
                          reverse(new long[]{}),
                          LongArrays.interDec(reverse(new long[] { }), 0, 0, reverse(new long[] { }), 0, 0));
        assertArrayEquals("",
                          reverse(new long[]{}),
                          LongArrays.interDec(reverse(new long[] { }), 0, 0, reverse(new long[] { 2L, 3L, 4L, 5L, 6L }), 0, 5));
        assertArrayEquals("",
                          reverse(new long[]{3L}),
                          LongArrays.interDec(reverse(new long[] { 1L, 3L, 7L }), 0, 3, reverse(new long[] { 2L, 3L, 4L, 5L, 6L }), 0, 5));
        assertArrayEquals("",
                          reverse(new long[]{3L, 6L}),
                          LongArrays.interDec(reverse(new long[] { 1L, 3L, 6L }), 0, 3, reverse(new long[] { 2L, 3L, 4L, 5L, 6L }), 0, 5));
    }

    public void testInterInc() throws Exception {
        assertArrayEquals("",
                          new long[]{},
                          LongArrays.interInc(new long[] { }, 0, 0, new long[] { }, 0, 0));
        assertArrayEquals("",
                          new long[]{},
                          LongArrays.interInc(new long[] { }, 0, 0, new long[] { 2L, 3L, 4L, 5L, 6L }, 0, 5));
        assertArrayEquals("",
                          new long[]{3L},
                          LongArrays.interInc(new long[] { 1L, 3L, 7L }, 0, 3, new long[] { 2L, 3L, 4L, 5L, 6L }, 0, 5));
        assertArrayEquals("",
                          new long[]{3L, 6L},
                          LongArrays.interInc(new long[] { 1L, 3L, 6L }, 0, 3, new long[] { 2L, 3L, 4L, 5L, 6L }, 0, 5));
    }

    public void testAppend() throws Exception {
        assertArrayEquals("",
                          new long[]{1L, 2L, 3L, 4L, 5L, 6L},
                          LongArrays.append(new long[]{1L, 2L, 3L}, 4L, 5L, 6L));
        assertArrayEquals("",
                          new long[]{1L, 2L, 3L, 4L, 5L, 6L},
                          LongArrays.append(new long[]{}, 1L, 2L, 3L, 4L, 5L, 6L));
        assertArrayEquals("",
                          new long[]{},
                          LongArrays.append(new long[]{}));
        assertArrayEquals("",
                          new long[]{1L, 2L, 3L, 4L, 5L},
                          LongArrays.append(new long[]{1L, 2L, 3L}, 4L, 5L));
        assertArrayEquals("",
                          new long[]{1L, 2L, 3L},
                          LongArrays.append(new long[]{1L, 2L, 3L}));
    }

    public void testPrepend() throws Exception {
        assertArrayEquals("",
                          new long[]{4L, 5L, 6L, 1L, 2L, 3L},
                          LongArrays.prepend(new long[]{1L, 2L, 3L}, 4L, 5L, 6L));
        assertArrayEquals("",
                          new long[]{1L, 2L, 3L, 4L, 5L, 6L},
                          LongArrays.prepend(new long[]{}, 1L, 2L, 3L, 4L, 5L, 6L));
        assertArrayEquals("",
                          new long[]{},
                          LongArrays.prepend(new long[]{}));
        assertArrayEquals("",
                          new long[]{4L, 5L, 1L, 2L, 3L},
                          LongArrays.prepend(new long[]{1L, 2L, 3L}, 4L, 5L));
        assertArrayEquals("",
                          new long[]{1L, 2L, 3L},
                          LongArrays.prepend(new long[]{1L, 2L, 3L}));
    }

    public void testConcat() throws Exception {
        assertArrayEquals("",
                          new long[]{1L, 2L, 3L, 4L, 5L, 6L},
                          LongArrays.concat(new long[]{1L, 2L, 3L}, new long[]{4L, 5L, 6L}));
        assertArrayEquals("",
                          new long[]{1L, 2L, 3L, 4L, 5L, 6L},
                          LongArrays.concat(new long[]{}, new long[]{1L, 2L, 3L, 4L, 5L, 6L}));
        assertArrayEquals("",
                          new long[]{},
                          LongArrays.concat(new long[]{}));
        assertArrayEquals("",
                          new long[]{1L, 2L, 3L, 4L, 5L, 6L},
                          LongArrays.concat(new long[]{1L, 2L, 3L}, new long[]{4L, 5L}, new long[]{6L}));
        assertArrayEquals("",
                          new long[]{1L, 2L, 3L, 4L, 5L, 6L},
                          LongArrays.concat(new long[]{1L, 2L, 3L}, new long[]{}, new long[]{4L, 5L}, new long[]{6L}));
        assertArrayEquals("",
                          new long[]{1L, 2L, 3L, 4L, 5L, 6L},
                          LongArrays.concat(new long[]{}, new long[]{1L, 2L, 3L}, new long[]{4L, 5L}, new long[]{6L}));

    }

    public void testSubarray() throws Exception {
        assertArrayEquals("subarray([],0,0)", new long[] { }, LongArrays.subarray(new long[]{}, 0, 0));
        assertArrayEquals("subarray([1,2,3,4,5,6],0,0)", new long[] { }, LongArrays.subarray(new long[] { 1L, 2L, 3L, 4L, 5L, 6L }, 0, 0));
        assertArrayEquals("subarray([1,2,3,4,5,6],6,6)", new long[] { }, LongArrays.subarray(new long[] { 1L, 2L, 3, 4, 5, 6 }, 6, 6));
        assertArrayEquals("subarray([1,2,3,4,5,6],1,4)", new long[] { 2, 3, 4 }, LongArrays.subarray(new long[] { 1, 2, 3, 4, 5, 6 }, 1, 4));
        assertArrayEquals("subarray([1,2,3,4,5,6],2,3)", new long[] { 3 }, LongArrays.subarray(new long[] { 1, 2, 3, 4, 5, 6 }, 2, 3));
        assertArrayEquals("subarray([1,2,3,4,5,6],0,6)", new long[] { 1, 2, 3, 4, 5, 6 }, LongArrays.subarray(new long[] { 1, 2, 3, 4, 5, 6 }, 0, 6));
        assertArrayEquals("subarray([1,2,3,4,5,6],1,1)", new long[] { }, LongArrays.subarray(new long[] { 1, 2, 3, 4, 5, 6 }, 1, 1));
        try {
            LongArrays.subarray(new long[] { }, 0, 1);
            fail("subarray([],0,1)");
        } catch (ArrayIndexOutOfBoundsException e) {
        }
        try {
            LongArrays.subarray(new long[] { }, 1, 0);
            fail("subarray([],1,0)");
        } catch (ArrayIndexOutOfBoundsException e) {
        }
        try {
            LongArrays.subarray(new long[] { 1, 2, 3, 4, 5, 6 }, 3, 7);
            fail("subarray([1,2,3,4,5,6],3,7)");
        } catch (ArrayIndexOutOfBoundsException e) {
        }
        assertArrayEquals("subarray(['1','2','3','4','5','6'],0,6)", new long[] { 1L, 2L, 3L, 4L, 5L, 6L },
                          LongArrays.subarray(new long[] { 1L, 2L, 3L, 4L, 5L, 6L }, 0, 6));
    }

    public void testAsList() throws Exception {
        LongContainer list = LongArrays.container(1L,2L,3L);
        assertEquals("asList(...).size()", 3, list.size());
        LongIterable.LongIterator it=list.iterator();
        assertEquals("asList(...).iterator().next()", 1L, it.nextValue());
        assertEquals("asList(...).iterator().next().next()", 2L, it.nextValue());
        assertEquals("asList(...).iterator().next().next().next()", 3L, it.nextValue());
        assertFalse("asList(...).iterator().next().next().next().hasNext()", it.hasNext());
    }

}
