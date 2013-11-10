package net.varkhan.base.containers.array;

import junit.framework.TestCase;
import net.varkhan.base.containers.type.IntContainer;
import net.varkhan.base.containers.type.IntIterable;

import java.util.Random;


/**
 * <b></b>.
 * <p/>
 *
 * @author varkhan
 * @date 3/24/12
 * @time 9:39 PM
 */
public class IntArraysTest extends TestCase {

    public static void assertArrayEquals(String message, int[] expected, int[] actual) {
        if(expected==null) { if(actual==null) return; }
        else if(expected.length==actual.length) {
            boolean same = true;
            for(int i=0; i<expected.length; i++) if(expected[i]!=actual[i]) { same=false; break; }
            if(same) return;
        }
        fail(message+";\n expected: ["+IntArrays.join(",", expected)+"];\n   actual: ["+IntArrays.join(",", actual)+"]");
    }

    public static int[] reverse(int[] a) {
        for(int i1=0, i2=a.length-1; i1<i2; i1++, i2--) {
            int t = a[i2];
            a[i2] = a[i1];
            a[i1] = t;
        }
        return a;
    }

    public void testEquals() throws Exception {
        assertTrue("equals([],[])", IntArrays.equals(new int[] { }, new int[] { }));
        assertTrue("equals([1,2,3,\"6\"],[1,2,3,\"6\"])", IntArrays.equals(new int[] { 1, 2, 3, 6 }, new int[] { 1, 2, 3, 6 }));
        assertFalse("equals([1,2,3,\"6\"],[5,2,3,\"6\"])", IntArrays.equals(new int[] { 1, 2, 3, 6 }, new int[] { 5, 2, 3, 6 }));
        assertFalse("equals([1,2,3,\"6\"],[1,2,3,Integer.NaN])", IntArrays.equals(new int[] { 1, 2, 3, 6 }, new int[] { 5, 2, 3, 999999 }));
        assertFalse("equals([1,2,3,\"6\"],[1,2,3,\"6\",10])", IntArrays.equals(new int[] { 1, 2, 3, 6 }, new int[] { 5, 2, 3, 6, 10 }));
    }

    public void testIndexOf() throws Exception {
        assertEquals("indexOf(Integer.NaN,1,2,\"3\",Integer.NaN)", 3, IntArrays.indexOf(999999,1,2,3,999999));
        assertEquals("indexOf(Integer.NaN,Integer.NaN,2,\"3\",4)", 0, IntArrays.indexOf(999999, 999999, 2, 3, 4));
        assertEquals("indexOf(Integer.NaN,1,2,\"3\",4)", -1, IntArrays.indexOf(999999, 1, 2, 3, 4));
        assertEquals("indexOf(1,1,2,\"3\",Integer.NaN)", 0, IntArrays.indexOf(1, 1, 2, 3, 999999));
        assertEquals("indexOf(4,1,2,\"3\",Integer.NaN)", -1, IntArrays.indexOf(4, 1, 2, 3, 999999));
        assertEquals("indexOf(4,1,2,\"3\",4)", 3, IntArrays.indexOf(4, 1, 2, 3, 4));
        assertEquals("indexOf(\"3\",1,2,\"3\",4)", 2, IntArrays.indexOf(3, 1, 2, 3, 4));
    }

    public void testIndexOfArray() throws Exception {
        assertEquals("indexOf([], 0, [1,2,3,6])", 0, IntArrays.indexOf(new int[]{}, 0, new int[]{ 1, 2, 3, 6 }));
        assertEquals("indexOf([2], 0, [1,2,3,6])", 1, IntArrays.indexOf(new int[]{2}, 0, new int[]{ 1, 2, 3, 6 }));
        assertEquals("indexOf([2,3], 0, [1,2,3,6])", 1, IntArrays.indexOf(new int[]{2, 3}, 0, new int[]{ 1, 2, 3, 6 }));
        assertEquals("indexOf([2,3], 2, [1,2,3,6])", -1, IntArrays.indexOf(new int[]{2, 3}, 2, new int[]{ 1, 2, 3, 6 }));
        assertEquals("indexOf([2,6], 0, [1,2,3,6])", -1, IntArrays.indexOf(new int[]{2, 6}, 0, new int[]{ 1, 2, 3, 6 }));
    }

    public void testSortDec() throws Exception {
        int[] ary = {0};
        IntArrays.heapSortDec(ary);
        assertArrayEquals("heapSort(0)", new int[] { 0 }, ary);
        ary = new int[]{3,2,1,1,4,4,6,5,7,2};
        IntArrays.heapSortDec(ary);
        assertArrayEquals("heapSort(3,2,1,1,4,4,6,5,7,2)", reverse(new int[] { 1, 1, 2, 2, 3, 4, 4, 5, 6, 7 }), ary);
        int N = 500; // Max number of objects that will fit in normal heap size: 16*500^2 = 4m
        int[][] a = new int[N][];
        int[][] a1 = new int[N][];
        int[][] a2 = new int[N][];
        Random rand = new Random();
        int n = 0;
        int c = 0;
        for(int i=0; i<N; i++) {
            int l = rand.nextInt(N);
            int[] s = new int[l];
            for(int j=0; j<l; j++) s[j]=rand.nextInt();
            a[i] = s;
            a1[i] = s.clone();
            a2[i] = s.clone();
            n += l;
        }
        System.out.println("Sorting "+N+" arrays of "+n+" elements");
        long t0 = System.currentTimeMillis();
        for(int i=0; i<N; i++) {
            c+=IntArrays.heapSortDec(a1[i]);
        }
        long t1 = System.currentTimeMillis();
        for(int i=0; i<N; i++) {
            java.util.Arrays.sort(a2[i]);
            reverse(a2[i]);
        }
        long t2 = System.currentTimeMillis();
        for(int i=0; i<N; i++) {
            assertArrayEquals("sort("+IntArrays.join(",",a[i])+")",a2[i],a1[i]);
        }
        System.out.println("Sorted "+N+" arrays of "+n+" elements in "+(t1-t0)+"ms, "+c+" operations ("+(t2-t1)+"ms for java.util.IntArrays.sort)");
    }

    public void testSortInc() throws Exception {
        int[] ary = {0};
        IntArrays.heapSortInc(ary);
        assertArrayEquals("heapSort(0)", new int[] { 0 }, ary);
        ary = new int[]{3,2,1,1,4,4,6,5,7,2};
        IntArrays.heapSortInc(ary);
        assertArrayEquals("heapSort(3,2,1,1,4,4,6,5,7,2)", new int[] { 1, 1, 2, 2, 3, 4, 4, 5, 6, 7 }, ary);
        int N = 500; // Max number of objects that will fit in normal heap size: 16*500^2 = 4m
        int[][] a = new int[N][];
        int[][] a1 = new int[N][];
        int[][] a2 = new int[N][];
        Random rand = new Random();
        int n = 0;
        int c = 0;
        for(int i=0; i<N; i++) {
            int l = rand.nextInt(N);
            int[] s = new int[l];
            for(int j=0; j<l; j++) s[j]=rand.nextInt();
            a[i] = s;
            a1[i] = s.clone();
            a2[i] = s.clone();
            n += l;
        }
        System.out.println("Sorting "+N+" arrays of "+n+" elements");
        long t0 = System.currentTimeMillis();
        for(int i=0; i<N; i++) {
            c+=IntArrays.heapSortInc(a1[i]);
        }
        long t1 = System.currentTimeMillis();
        for(int i=0; i<N; i++) {
            java.util.Arrays.sort(a2[i]);
        }
        long t2 = System.currentTimeMillis();
        for(int i=0; i<N; i++) {
            assertArrayEquals("sort("+IntArrays.join(",",a[i])+")",a2[i],a1[i]);
        }
        System.out.println("Sorted "+N+" arrays of "+n+" elements in "+(t1-t0)+"ms, "+c+" operations ("+(t2-t1)+"ms for java.util.IntArrays.sort)");
    }

    public void testSearchDec() throws Exception {
        int[] a1 = reverse(new int[] {});
        assertEquals("",-1,IntArrays.searchDec(a1, 0, 0, 1));
        int[] a2 = reverse(new int[] { 2});
        assertEquals("",-2,IntArrays.searchDec(a2, 0, 1, 3));
        int[] a3 = reverse(new int[] { 2});
        assertEquals("",-1,IntArrays.searchDec(a3, 0, 1, 1));
        assertEquals("",0,IntArrays.searchDec(a3, 0, 1, 2));
        int[] a4 = reverse(new int[]{2, 3, 4, 7, 8});
        assertEquals("",-6,IntArrays.searchDec(a4, 0, 5, 5));
        assertEquals("",2,IntArrays.searchDec(a4, 0, 5, 4));
    }

    public void testSearchInc() throws Exception {
        int[] a1 = new int[] {};
        assertEquals("",-1,IntArrays.searchInc(a1, 0, 0, 1));
        int[] a2 = new int[] { 2};
        assertEquals("",-2,IntArrays.searchInc(a2, 0, 1, 3));
        int[] a3 = new int[] { 2};
        assertEquals("",-1,IntArrays.searchInc(a3, 0, 1, 1));
        assertEquals("",0,IntArrays.searchInc(a3, 0, 1, 2));
        int[] a4 = new int[]{2, 3, 4, 7, 8};
        assertEquals("",-4,IntArrays.searchInc(a4, 0, 5, 5));
        assertEquals("",2,IntArrays.searchInc(a4, 0, 5, 4));
    }

//    public void testInsert() throws Exception {
//        int[] a1 = new int[] {Integer.NaN};
//        assertEquals("",0,IntArrays.insert(a1,0,0,1));
//        assertArrayEquals("",new int[]{1},a1);
//        int[] a2 = new int[] { 2, Integer.NaN};
//        assertEquals("",1,IntArrays.insert(a2,0,1,3));
//        assertArrayEquals("",new int[]{2,3},a2);
//        int[] a3 = new int[] { 2, Integer.NaN};
//        assertEquals("",0,IntArrays.insert(a3,0,1,1));
//        assertArrayEquals("",new int[]{1,2},a3);
//        int[] a4 = new int[]{2, 3, 4, 7, 8, Integer.NaN};
//        assertEquals("",3,IntArrays.insert(a4,0,5,5));
//        assertArrayEquals("",new int[]{2, 3, 4, 5, 7, 8},a4);
//    }

    public void testUnionDec() throws Exception {
        assertArrayEquals("",
                reverse(new int[] { }),
                IntArrays.unionDec(reverse(new int[] { }), 0, 0, reverse(new int[] { }), 0, 0));
        assertArrayEquals("",
                reverse(new int[]{2, 3, 4, 5, 6}),
                IntArrays.unionDec(reverse(new int[] { }), 0, 0, reverse(new int[] { 2, 3, 4, 5, 6 }), 0, 5));
        assertArrayEquals("",
                reverse(new int[] { 1, 2, 3, 4, 5, 6, 7 }),
                IntArrays.unionDec(reverse(new int[] { 1, 3, 7 }), 0, 3, reverse(new int[] { 2, 3, 4, 5, 6 }), 0, 5));
    }

    public void testUnionInc() throws Exception {
        assertArrayEquals("",
                new int[]{},
                IntArrays.unionInc(new int[] { }, 0, 0, new int[] { }, 0, 0));
        assertArrayEquals("",
                new int[]{2, 3, 4, 5, 6},
                IntArrays.unionInc(new int[] { }, 0, 0, new int[] { 2, 3, 4, 5, 6 }, 0, 5));
        assertArrayEquals("",
                new int[]{1, 2, 3, 4, 5, 6, 7},
                IntArrays.unionInc(new int[] { 1, 3, 7 }, 0, 3, new int[] { 2, 3, 4, 5, 6 }, 0, 5));
    }

    public void testInterDec() throws Exception {
        assertArrayEquals("",
                          reverse(new int[]{}),
                          IntArrays.interDec(reverse(new int[] { }), 0, 0, reverse(new int[] { }), 0, 0));
        assertArrayEquals("",
                          reverse(new int[]{}),
                          IntArrays.interDec(reverse(new int[] { }), 0, 0, reverse(new int[] { 2, 3, 4, 5, 6 }), 0, 5));
        assertArrayEquals("",
                          reverse(new int[]{3}),
                          IntArrays.interDec(reverse(new int[] { 1, 3, 7 }), 0, 3, reverse(new int[] { 2, 3, 4, 5, 6 }), 0, 5));
        assertArrayEquals("",
                          reverse(new int[]{3, 6}),
                          IntArrays.interDec(reverse(new int[] { 1, 3, 6 }), 0, 3, reverse(new int[] { 2, 3, 4, 5, 6 }), 0, 5));
    }

    public void testInterInc() throws Exception {
        assertArrayEquals("",
                          new int[]{},
                          IntArrays.interInc(new int[] { }, 0, 0, new int[] { }, 0, 0));
        assertArrayEquals("",
                          new int[]{},
                          IntArrays.interInc(new int[] { }, 0, 0, new int[] { 2, 3, 4, 5, 6 }, 0, 5));
        assertArrayEquals("",
                          new int[]{3},
                          IntArrays.interInc(new int[] { 1, 3, 7 }, 0, 3, new int[] { 2, 3, 4, 5, 6 }, 0, 5));
        assertArrayEquals("",
                          new int[]{3, 6},
                          IntArrays.interInc(new int[] { 1, 3, 6 }, 0, 3, new int[] { 2, 3, 4, 5, 6 }, 0, 5));
    }

    public void testAppend() throws Exception {
        assertArrayEquals("",
                          new int[]{1, 2, 3, 4, 5, 6},
                          IntArrays.append(new int[]{1, 2, 3}, 4, 5, 6));
        assertArrayEquals("",
                          new int[]{1, 2, 3, 4, 5, 6},
                          IntArrays.append(new int[]{}, 1, 2, 3, 4, 5, 6));
        assertArrayEquals("",
                          new int[]{},
                          IntArrays.append(new int[]{}));
        assertArrayEquals("",
                          new int[]{1, 2, 3, 4, 5},
                          IntArrays.append(new int[]{1, 2, 3}, 4, 5));
        assertArrayEquals("",
                          new int[]{1, 2, 3},
                          IntArrays.append(new int[]{1, 2, 3}));
    }

    public void testPrepend() throws Exception {
        assertArrayEquals("",
                          new int[]{4, 5, 6, 1, 2, 3},
                          IntArrays.prepend(new int[]{1, 2, 3}, 4, 5, 6));
        assertArrayEquals("",
                          new int[]{1, 2, 3, 4, 5, 6},
                          IntArrays.prepend(new int[]{}, 1, 2, 3, 4, 5, 6));
        assertArrayEquals("",
                          new int[]{},
                          IntArrays.prepend(new int[]{}));
        assertArrayEquals("",
                          new int[]{4, 5, 1, 2, 3},
                          IntArrays.prepend(new int[]{1, 2, 3}, 4, 5));
        assertArrayEquals("",
                          new int[]{1, 2, 3},
                          IntArrays.prepend(new int[]{1, 2, 3}));
    }

    public void testConcat() throws Exception {
        assertArrayEquals("",
                          new int[]{1, 2, 3, 4, 5, 6},
                          IntArrays.concat(new int[]{1, 2, 3}, new int[]{4, 5, 6}));
        assertArrayEquals("",
                          new int[]{1, 2, 3, 4, 5, 6},
                          IntArrays.concat(new int[]{}, new int[]{1, 2, 3, 4, 5, 6}));
        assertArrayEquals("",
                          new int[]{},
                          IntArrays.concat(new int[]{}));
        assertArrayEquals("",
                          new int[]{1, 2, 3, 4, 5, 6},
                          IntArrays.concat(new int[]{1, 2, 3}, new int[]{4, 5}, new int[]{6}));
        assertArrayEquals("",
                          new int[]{1, 2, 3, 4, 5, 6},
                          IntArrays.concat(new int[]{1, 2, 3}, new int[]{}, new int[]{4, 5}, new int[]{6}));
        assertArrayEquals("",
                          new int[]{1, 2, 3, 4, 5, 6},
                          IntArrays.concat(new int[]{}, new int[]{1, 2, 3}, new int[]{4, 5}, new int[]{6}));

    }

    public void testSubarray() throws Exception {
        assertArrayEquals("subarray([],0,0)", new int[] { }, IntArrays.subarray(new int[]{}, 0, 0));
        assertArrayEquals("subarray([1,2,3,4,5,6],0,0)", new int[] { }, IntArrays.subarray(new int[] { 1, 2, 3, 4, 5, 6 }, 0, 0));
        assertArrayEquals("subarray([1,2,3,4,5,6],6,6)", new int[] { }, IntArrays.subarray(new int[] { 1, 2, 3, 4, 5, 6 }, 6, 6));
        assertArrayEquals("subarray([1,2,3,4,5,6],1,4)", new int[] { 2, 3, 4 }, IntArrays.subarray(new int[] { 1, 2, 3, 4, 5, 6 }, 1, 4));
        assertArrayEquals("subarray([1,2,3,4,5,6],2,3)", new int[] { 3 }, IntArrays.subarray(new int[] { 1, 2, 3, 4, 5, 6 }, 2, 3));
        assertArrayEquals("subarray([1,2,3,4,5,6],0,6)", new int[] { 1, 2, 3, 4, 5, 6 }, IntArrays.subarray(new int[] { 1, 2, 3, 4, 5, 6 }, 0, 6));
        assertArrayEquals("subarray([1,2,3,4,5,6],1,1)", new int[] { }, IntArrays.subarray(new int[] { 1, 2, 3, 4, 5, 6 }, 1, 1));
        try {
            IntArrays.subarray(new int[] { }, 0, 1);
            fail("subarray([],0,1)");
        } catch (ArrayIndexOutOfBoundsException e) {
        }
        try {
            IntArrays.subarray(new int[] { }, 1, 0);
            fail("subarray([],1,0)");
        } catch (ArrayIndexOutOfBoundsException e) {
        }
        try {
            IntArrays.subarray(new int[] { 1, 2, 3, 4, 5, 6 }, 3, 7);
            fail("subarray([1,2,3,4,5,6],3,7)");
        } catch (ArrayIndexOutOfBoundsException e) {
        }
        assertArrayEquals("subarray(['1','2','3','4','5','6'],0,6)", new int[] { 1, 2, 3, 4, 5, 6 },
                          IntArrays.subarray(new int[] { 1, 2, 3, 4, 5, 6 }, 0, 6));
    }

    public void testAsList() throws Exception {
        IntContainer list = IntArrays.container(1,2,3);
        assertEquals("asList(...).size()", 3, list.size());
        IntIterable.IntIterator it=list.iterator();
        assertEquals("asList(...).iterator().next()", 1, it.nextValue());
        assertEquals("asList(...).iterator().next().next()", 2, it.nextValue());
        assertEquals("asList(...).iterator().next().next().next()", 3, it.nextValue());
        assertFalse("asList(...).iterator().next().next().next().hasNext()", it.hasNext());
    }

    public void testStrgInt() throws Exception {
        assertEquals("[0|]",IntArrays.toString(new int[]{}));
        assertEquals("[1|0]",IntArrays.toString(new int[]{0}));
        assertEquals("[1|1]",IntArrays.toString(new int[]{1}));
        assertEquals("[1|247]",IntArrays.toString(new int[]{0xF7}));
        assertEquals("[5|0,1,2,243,4]",IntArrays.toString(new int[]{0,1,2,0xF3,4}));
        assertEquals("[5|0,1,2,243,4]",IntArrays.toString(new StringBuilder(),new int[]{0,1,2,0xF3,4}).toString());
        assertEquals("[5|0,1,2,243,4]",IntArrays.toString(new StringBuffer(),new int[]{0,1,2,0xF3,4}).toString());
    }

    public void testJoinInt() throws Exception {
        assertEquals("",IntArrays.join(":",new int[]{}));
        assertEquals("0",IntArrays.join(":",new int[]{0}));
        assertEquals("1",IntArrays.join(":",new int[]{1}));
        assertEquals("247",IntArrays.join(":",new int[]{0xF7}));
        assertEquals("0:1:2:243:4",IntArrays.join(":",new int[]{0,1,2,0xF3,4}));
        assertEquals("0:1:2:243:4",IntArrays.join(new StringBuilder(),":",new int[]{0,1,2,0xF3,4}).toString());
        assertEquals("0:1:2:243:4",IntArrays.join(new StringBuffer(),":",new int[]{0,1,2,0xF3,4}).toString());
        assertEquals("0122434",IntArrays.join(null,new int[]{0,1,2,0xF3,4}));
        assertEquals("0122434",IntArrays.join(new StringBuilder(),null,new int[]{0,1,2,0xF3,4}).toString());
        assertEquals("0122434",IntArrays.join(new StringBuffer(),null,new int[]{0,1,2,0xF3,4}).toString());
    }

}
