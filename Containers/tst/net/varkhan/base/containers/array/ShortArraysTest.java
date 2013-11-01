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
public class ShortArraysTest extends TestCase {

    public static void assertArrayEquals(String message, short[] expected, short[] actual) {
        if(expected==null) { if(actual==null) return; }
        else if(expected.length==actual.length) {
            boolean same = true;
            for(int i=0; i<expected.length; i++) if(expected[i]!=actual[i]) { same=false; break; }
            if(same) return;
        }
        fail(message+";\n expected: ["+StringArrays.join(",", expected)+"];\n   actual: ["+StringArrays.join(",", actual)+"]");
    }

    public static short[] reverse(short[] a) {
        for(int i1=0, i2=a.length-1; i1<i2; i1++, i2--) {
            short t = a[i2];
            a[i2] = a[i1];
            a[i1] = t;
        }
        return a;
    }

    public void testEquals() throws Exception {
        assertTrue("equals([],[])", ShortArrays.equals(new short[] { }, new short[] { }));
        assertTrue("equals([1,2,3,\"6\"],[1,2,3,\"6\"])", ShortArrays.equals(new short[] { 1, 2, 3, 6 }, new short[] { 1, 2, 3, 6 }));
        assertFalse("equals([1,2,3,\"6\"],[5,2,3,\"6\"])", ShortArrays.equals(new short[] { 1, 2, 3, 6 }, new short[] { 5, 2, 3, 6 }));
        assertFalse("equals([1,2,3,\"6\"],[1,2,3,256])", ShortArrays.equals(new short[] { 1, 2, 3, 6 }, new short[] { 5, 2, 3, 256 }));
        assertFalse("equals([1,2,3,\"6\"],[1,2,3,\"6\",10])", ShortArrays.equals(new short[] { 1, 2, 3, 6 }, new short[] { 5, 2, 3, 6, 10 }));
    }

    public void testIndexOf() throws Exception {
        assertEquals("indexOf(Short.NaN,1,2,\"3\",Short.NaN)", 3, ShortArrays.indexOf((short)256,(short)1,(short)2,(short)3,(short)256));
        assertEquals("indexOf(Short.NaN,Short.NaN,2,\"3\",4)", 0, ShortArrays.indexOf((short)256, (short)256, (short)2, (short)3, (short)4));
        assertEquals("indexOf(Short.NaN,1,2,\"3\",4)", -1, ShortArrays.indexOf((short)256, (short)1, (short)2, (short)3, (short)4));
        assertEquals("indexOf(1,1,2,\"3\",Short.NaN)", 0, ShortArrays.indexOf((short)1, (short)1, (short)2, (short)3, (short)256));
        assertEquals("indexOf(4,1,2,\"3\",Short.NaN)", -1, ShortArrays.indexOf((short)4, (short)1, (short)2, (short)3, (short)256));
        assertEquals("indexOf(4,1,2,\"3\",4)", 3, ShortArrays.indexOf((short)4, (short)1, (short)2, (short)3, (short)4));
        assertEquals("indexOf(\"3\",1,2,\"3\",4)", 2, ShortArrays.indexOf((short)3, (short)1, (short)2, (short)3, (short)4));
    }

    public void testSortDec() throws Exception {
        short[] ary = {0};
        ShortArrays.heapSortDec(ary);
        assertArrayEquals("heapSort(0)", new short[] { 0 }, ary);
        ary = new short[]{3,2,1,1,4,4,6,5,7,2};
        ShortArrays.heapSortDec(ary);
        assertArrayEquals("heapSort(3,2,1,1,4,4,6,5,7,2)", reverse(new short[] { 1, 1, 2, 2, 3, 4, 4, 5, 6, 7 }), ary);
        int N = 500; // Max number of objects that will fit in normal heap size: 16*500^2 = 4m
        short[][] a = new short[N][];
        short[][] a1 = new short[N][];
        short[][] a2 = new short[N][];
        Random rand = new Random();
        int n = 0;
        int c = 0;
        for(int i=0; i<N; i++) {
            int l = rand.nextInt(N);
            short[] s = new short[l];
            for(int j=0; j<l; j++) s[j]=(short)rand.nextInt(1<<15);
            a[i] = s;
            a1[i] = s.clone();
            a2[i] = s.clone();
            n += l;
        }
        System.out.println("Sorting "+N+" arrays of "+n+" elements");
        long t0 = System.currentTimeMillis();
        for(int i=0; i<N; i++) {
            c+=ShortArrays.heapSortDec(a1[i]);
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
        System.out.println("Sorted "+N+" arrays of "+n+" elements in "+(t1-t0)+"ms, "+c+" operations ("+(t2-t1)+"ms for java.util.ShortArrays.sort)");
    }

    public void testSortInc() throws Exception {
        short[] ary = {0};
        ShortArrays.heapSortInc(ary);
        assertArrayEquals("heapSort(0)", new short[] { 0 }, ary);
        ary = new short[]{3,2,1,1,4,4,6,5,7,2};
        ShortArrays.heapSortInc(ary);
        assertArrayEquals("heapSort(3,2,1,1,4,4,6,5,7,2)", new short[] { 1, 1, 2, 2, 3, 4, 4, 5, 6, 7 }, ary);
        int N = 500; // Max number of objects that will fit in normal heap size: 16*500^2 = 4m
        short[][] a = new short[N][];
        short[][] a1 = new short[N][];
        short[][] a2 = new short[N][];
        Random rand = new Random();
        int n = 0;
        int c = 0;
        for(int i=0; i<N; i++) {
            int l = rand.nextInt(N);
            short[] s = new short[l];
            for(int j=0; j<l; j++) s[j]=(short)rand.nextInt(1<<15);
            a[i] = s;
            a1[i] = s.clone();
            a2[i] = s.clone();
            n += l;
        }
        System.out.println("Sorting "+N+" arrays of "+n+" elements");
        long t0 = System.currentTimeMillis();
        for(int i=0; i<N; i++) {
            c+=ShortArrays.heapSortInc(a1[i]);
        }
        long t1 = System.currentTimeMillis();
        for(int i=0; i<N; i++) {
            java.util.Arrays.sort(a2[i]);
        }
        long t2 = System.currentTimeMillis();
        for(int i=0; i<N; i++) {
            assertArrayEquals("sort("+StringArrays.join(",",a[i])+")",a2[i],a1[i]);
        }
        System.out.println("Sorted "+N+" arrays of "+n+" elements in "+(t1-t0)+"ms, "+c+" operations ("+(t2-t1)+"ms for java.util.ShortArrays.sort)");
    }

    public void testSearchDec() throws Exception {
        short[] a1 = reverse(new short[] {});
        assertEquals("",-1,ShortArrays.searchDec(a1, 0, 0, (short)1));
        short[] a2 = reverse(new short[] { 2});
        assertEquals("",-2,ShortArrays.searchDec(a2, 0, 1, (short)3));
        short[] a3 = reverse(new short[] { 2});
        assertEquals("",-1,ShortArrays.searchDec(a3, 0, 1, (short)1));
        assertEquals("",0,ShortArrays.searchDec(a3, 0, 1, (short)2));
        short[] a4 = reverse(new short[]{2, 3, 4, 7, 8});
        assertEquals("",-6,ShortArrays.searchDec(a4, 0, 5, (short)5));
        assertEquals("",2,ShortArrays.searchDec(a4, 0, 5, (short)4));
    }

    public void testSearchInc() throws Exception {
        short[] a1 = new short[] {};
        assertEquals("",-1,ShortArrays.searchInc(a1, 0, 0, (short)1));
        short[] a2 = new short[] { 2};
        assertEquals("",-2,ShortArrays.searchInc(a2, 0, 1, (short)3));
        short[] a3 = new short[] { 2};
        assertEquals("",-1,ShortArrays.searchInc(a3, 0, 1, (short)1));
        assertEquals("",0,ShortArrays.searchInc(a3, 0, 1, (short)2));
        short[] a4 = new short[]{2, 3, 4, 7, 8};
        assertEquals("",-4,ShortArrays.searchInc(a4, 0, 5, (short)5));
        assertEquals("",2,ShortArrays.searchInc(a4, 0, 5, (short)4));
    }

//    public void testInsert() throws Exception {
//        short[] a1 = new short[] {Short.NaN};
//        assertEquals("",0,ShortArrays.insert(a1,0,0,1));
//        assertArrayEquals("",new short[]{1},a1);
//        short[] a2 = new short[] { 2, Short.NaN};
//        assertEquals("",1,ShortArrays.insert(a2,0,1,3));
//        assertArrayEquals("",new short[]{2,3},a2);
//        short[] a3 = new short[] { 2, Short.NaN};
//        assertEquals("",0,ShortArrays.insert(a3,0,1,1));
//        assertArrayEquals("",new short[]{1,2},a3);
//        short[] a4 = new short[]{2, 3, 4, 7, 8, Short.NaN};
//        assertEquals("",3,ShortArrays.insert(a4,0,5,5));
//        assertArrayEquals("",new short[]{2, 3, 4, 5, 7, 8},a4);
//    }

    public void testUnionDec() throws Exception {
        assertArrayEquals("",
                reverse(new short[] { }),
                ShortArrays.unionDec(reverse(new short[] { }), 0, 0, reverse(new short[] { }), 0, 0));
        assertArrayEquals("",
                reverse(new short[]{2, 3, 4, 5, 6}),
                ShortArrays.unionDec(reverse(new short[] { }), 0, 0, reverse(new short[] { 2, 3, 4, 5, 6 }), 0, 5));
        assertArrayEquals("",
                reverse(new short[] { 1, 2, 3, 4, 5, 6, 7 }),
                ShortArrays.unionDec(reverse(new short[] { 1, 3, 7 }), 0, 3, reverse(new short[] { 2, 3, 4, 5, 6 }), 0, 5));
    }

    public void testUnionInc() throws Exception {
        assertArrayEquals("",
                new short[]{},
                ShortArrays.unionInc(new short[] { }, 0, 0, new short[] { }, 0, 0));
        assertArrayEquals("",
                new short[]{2, 3, 4, 5, 6},
                ShortArrays.unionInc(new short[] { }, 0, 0, new short[] { 2, 3, 4, 5, 6 }, 0, 5));
        assertArrayEquals("",
                new short[]{1, 2, 3, 4, 5, 6, 7},
                ShortArrays.unionInc(new short[] { 1, 3, 7 }, 0, 3, new short[] { 2, 3, 4, 5, 6 }, 0, 5));
    }

    public void testInterDec() throws Exception {
        assertArrayEquals("",
                          reverse(new short[]{}),
                          ShortArrays.interDec(reverse(new short[] { }), 0, 0, reverse(new short[] { }), 0, 0));
        assertArrayEquals("",
                          reverse(new short[]{}),
                          ShortArrays.interDec(reverse(new short[] { }), 0, 0, reverse(new short[] { 2, 3, 4, 5, 6 }), 0, 5));
        assertArrayEquals("",
                          reverse(new short[]{3}),
                          ShortArrays.interDec(reverse(new short[] { 1, 3, 7 }), 0, 3, reverse(new short[] { 2, 3, 4, 5, 6 }), 0, 5));
        assertArrayEquals("",
                          reverse(new short[]{3, 6}),
                          ShortArrays.interDec(reverse(new short[] { 1, 3, 6 }), 0, 3, reverse(new short[] { 2, 3, 4, 5, 6 }), 0, 5));
    }

    public void testInterInc() throws Exception {
        assertArrayEquals("",
                          new short[]{},
                          ShortArrays.interInc(new short[] { }, 0, 0, new short[] { }, 0, 0));
        assertArrayEquals("",
                          new short[]{},
                          ShortArrays.interInc(new short[] { }, 0, 0, new short[] { 2, 3, 4, 5, 6 }, 0, 5));
        assertArrayEquals("",
                          new short[]{3},
                          ShortArrays.interInc(new short[] { 1, 3, 7 }, 0, 3, new short[] { 2, 3, 4, 5, 6 }, 0, 5));
        assertArrayEquals("",
                          new short[]{3, 6},
                          ShortArrays.interInc(new short[] { 1, 3, 6 }, 0, 3, new short[] { 2, 3, 4, 5, 6 }, 0, 5));
    }

    public void testAppend() throws Exception {
        assertArrayEquals("",
                          new short[]{1, 2, 3, 4, 5, 6},
                          ShortArrays.append(new short[]{1, 2, 3}, (short)4, (short)5, (short)6));
        assertArrayEquals("",
                          new short[]{1, 2, 3, 4, 5, 6},
                          ShortArrays.append(new short[]{}, (short)1, (short)2, (short)3, (short)4, (short)5, (short)6));
        assertArrayEquals("",
                          new short[]{},
                          ShortArrays.append(new short[]{}));
        assertArrayEquals("",
                          new short[]{1, 2, 3, 4, 5},
                          ShortArrays.append(new short[]{1, 2, 3}, (short)4, (short)5));
        assertArrayEquals("",
                          new short[]{1, 2, 3},
                          ShortArrays.append(new short[]{1, 2, 3}));
    }

    public void testPrepend() throws Exception {
        assertArrayEquals("",
                          new short[]{4, 5, 6, 1, 2, 3},
                          ShortArrays.prepend(new short[]{1, 2, 3}, (short)4, (short)5, (short)6));
        assertArrayEquals("",
                          new short[]{1, 2, 3, 4, 5, 6},
                          ShortArrays.prepend(new short[]{}, (short)1, (short)2, (short)3, (short)4, (short)5, (short)6));
        assertArrayEquals("",
                          new short[]{},
                          ShortArrays.prepend(new short[]{}));
        assertArrayEquals("",
                          new short[]{4, 5, 1, 2, 3},
                          ShortArrays.prepend(new short[]{1, 2, 3}, (short)4, (short)5));
        assertArrayEquals("",
                          new short[]{1, 2, 3},
                          ShortArrays.prepend(new short[]{1, 2, 3}));
    }

    public void testConcat() throws Exception {
        assertArrayEquals("",
                          new short[]{1, 2, 3, 4, 5, 6},
                          ShortArrays.concat(new short[]{1, 2, 3}, new short[]{4, 5, 6}));
        assertArrayEquals("",
                          new short[]{1, 2, 3, 4, 5, 6},
                          ShortArrays.concat(new short[]{}, new short[]{1, 2, 3, 4, 5, 6}));
        assertArrayEquals("",
                          new short[]{},
                          ShortArrays.concat(new short[]{}));
        assertArrayEquals("",
                          new short[]{1, 2, 3, 4, 5, 6},
                          ShortArrays.concat(new short[]{1, 2, 3}, new short[]{4, 5}, new short[]{6}));
        assertArrayEquals("",
                          new short[]{1, 2, 3, 4, 5, 6},
                          ShortArrays.concat(new short[]{1, 2, 3}, new short[]{}, new short[]{4, 5}, new short[]{6}));
        assertArrayEquals("",
                          new short[]{1, 2, 3, 4, 5, 6},
                          ShortArrays.concat(new short[]{}, new short[]{1, 2, 3}, new short[]{4, 5}, new short[]{6}));

    }

    public void testSubarray() throws Exception {
        assertArrayEquals("subarray([],0,0)", new short[] { }, ShortArrays.subarray(new short[]{}, 0, 0));
        assertArrayEquals("subarray([1,2,3,4,5,6],0,0)", new short[] { }, ShortArrays.subarray(new short[] { 1, 2, 3, 4, 5, 6 }, 0, 0));
        assertArrayEquals("subarray([1,2,3,4,5,6],6,6)", new short[] { }, ShortArrays.subarray(new short[] { 1, 2, 3, 4, 5, 6 }, 6, 6));
        assertArrayEquals("subarray([1,2,3,4,5,6],1,4)", new short[] { 2, 3, 4 }, ShortArrays.subarray(new short[] { 1, 2, 3, 4, 5, 6 }, 1, 4));
        assertArrayEquals("subarray([1,2,3,4,5,6],2,3)", new short[] { 3 }, ShortArrays.subarray(new short[] { 1, 2, 3, 4, 5, 6 }, 2, 3));
        assertArrayEquals("subarray([1,2,3,4,5,6],0,6)", new short[] { 1, 2, 3, 4, 5, 6 }, ShortArrays.subarray(new short[] { 1, 2, 3, 4, 5, 6 }, 0, 6));
        assertArrayEquals("subarray([1,2,3,4,5,6],1,1)", new short[] { }, ShortArrays.subarray(new short[] { 1, 2, 3, 4, 5, 6 }, 1, 1));
        try {
            ShortArrays.subarray(new short[] { }, 0, 1);
            fail("subarray([],0,1)");
        } catch (ArrayIndexOutOfBoundsException e) {
        }
        try {
            ShortArrays.subarray(new short[] { }, 1, 0);
            fail("subarray([],1,0)");
        } catch (ArrayIndexOutOfBoundsException e) {
        }
        try {
            ShortArrays.subarray(new short[] { 1, 2, 3, 4, 5, 6 }, 3, 7);
            fail("subarray([1,2,3,4,5,6],3,7)");
        } catch (ArrayIndexOutOfBoundsException e) {
        }
        assertArrayEquals("subarray(['1','2','3','4','5','6'],0,6)", new short[] { 1, 2, 3, 4, 5, 6 },
                          ShortArrays.subarray(new short[] { 1, 2, 3, 4, 5, 6 }, 0, 6));
    }


}
