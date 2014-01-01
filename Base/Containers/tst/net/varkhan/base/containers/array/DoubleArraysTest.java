package net.varkhan.base.containers.array;

import junit.framework.TestCase;
import net.varkhan.base.containers.type.DoubleContainer;
import net.varkhan.base.containers.type.DoubleIterable;

import java.util.Random;


/**
 * <b></b>.
 * <p/>
 *
 * @author varkhan
 * @date 3/24/12
 * @time 9:39 PM
 */
public class DoubleArraysTest extends TestCase {

    public static void assertArrayEquals(String message, double[] expected, double[] actual) {
        if(expected==null) { if(actual==null) return; }
        else if(expected.length==actual.length) {
            boolean same = true;
            for(int i=0; i<expected.length; i++) if(expected[i]!=actual[i]) { same=false; break; }
            if(same) return;
        }
        fail(message+";\n expected: ["+DoubleArrays.join(",", expected)+"];\n   actual: ["+DoubleArrays.join(",", actual)+"]");
    }

    public static double[] reverse(double[] a) {
        for(int i1=0, i2=a.length-1; i1<i2; i1++, i2--) {
            double t = a[i2];
            a[i2] = a[i1];
            a[i1] = t;
        }
        return a;
    }

    public void testEquals() throws Exception {
        assertTrue("equals([],[])", DoubleArrays.equals(new double[] { }, new double[] { }));
        assertTrue("equals([1,2,3,\"6\"],[1,2,3,\"6\"])", DoubleArrays.equals(new double[] { 1, 2, 3, 6.06 }, new double[] { 1, 2, 3, 6.06 }));
        assertFalse("equals([1,2,3,\"6\"],[5,2,3,\"6\"])", DoubleArrays.equals(new double[] { 1, 2, 3, 6.06 }, new double[] { 5, 2, 3, 6.06 }));
        assertFalse("equals([1,2,3,\"6\"],[1,2,3,Double.NaN])", DoubleArrays.equals(new double[] { 1, 2, 3, 6.06 }, new double[] { 5, 2, 3, Double.NaN }));
        assertFalse("equals([1,2,3,\"6\"],[1,2,3,\"6\",10])", DoubleArrays.equals(new double[] { 1, 2, 3, 6.06 }, new double[] { 5, 2, 3, 6.06, 10 }));
    }

    public void testIndexOf() throws Exception {
        assertEquals("indexOf(Double.NaN,1,2,\"3\",Double.NaN)", -1, DoubleArrays.indexOf(Double.NaN,1,2,3.03,Double.NaN));
        assertEquals("indexOf(Double.NaN,Double.NaN,2,\"3\",4)", -1, DoubleArrays.indexOf(Double.NaN, Double.NaN, 2, 3.03, 4));
        assertEquals("indexOf(Double.NaN,1,2,\"3\",4)", -1, DoubleArrays.indexOf(Double.NaN, 1, 2, 3.03, 4));
        assertEquals("indexOf(1,1,2,\"3\",Double.NaN)", 0, DoubleArrays.indexOf(1, 1, 2, 3.03, Double.NaN));
        assertEquals("indexOf(4,1,2,\"3\",Double.NaN)", -1, DoubleArrays.indexOf(4, 1, 2, 3.03, Double.NaN));
        assertEquals("indexOf(4,1,2,\"3\",4)", 3, DoubleArrays.indexOf(4, 1, 2, 3.03, 4));
        assertEquals("indexOf(\"3\",1,2,\"3\",4)", 2, DoubleArrays.indexOf(3.03, 1, 2, 3.03, 4));
    }

    public void testIndexOfArray() throws Exception {
        assertEquals("indexOf([], 0, [1,2,3,6])", 0, DoubleArrays.indexOf(new double[]{}, 0, new double[]{ 1, 2, 3.03, 6 }));
        assertEquals("indexOf([2], 0, [1,2,3,6])", 1, DoubleArrays.indexOf(new double[]{2}, 0, new double[]{ 1, 2, 3.03, 6 }));
        assertEquals("indexOf([2,3], 0, [1,2,3,6])", 1, DoubleArrays.indexOf(new double[]{2, 3.03}, 0, new double[]{ 1, 2, 3.03, 6 }));
        assertEquals("indexOf([2,3], 2, [1,2,3,6])", -1, DoubleArrays.indexOf(new double[]{2, 3.03}, 2, new double[]{ 1, 2, 3.03, 6 }));
        assertEquals("indexOf([2,6], 0, [1,2,3,6])", -1, DoubleArrays.indexOf(new double[]{2, 6}, 0, new double[]{ 1, 2, 3.03, 6 }));
    }

    public void testSortDec() throws Exception {
        double[] ary = {0};
        DoubleArrays.sortDec(ary);
        assertArrayEquals("heapSort(0)", new double[] { 0 }, ary);
        ary = new double[]{3,2,1,1,4,4,6,5,7,2};
        DoubleArrays.sortDec(ary);
        assertArrayEquals("heapSort(3,2,1,1,4,4,6,5,7,2)", reverse(new double[] { 1, 1, 2, 2, 3, 4, 4, 5, 6, 7 }), ary);
        int N = 500; // Max number of objects that will fit in normal heap size: 16*500^2 = 4m
        double[][] a = new double[N][];
        double[][] a1 = new double[N][];
        double[][] a2 = new double[N][];
        Random rand = new Random();
        int n = 0;
        int c = 0;
        for(int i=0; i<N; i++) {
            int l = rand.nextInt(N);
            double[] s = new double[l];
            for(int j=0; j<l; j++) s[j]=rand.nextDouble();
            a[i] = s;
            a1[i] = s.clone();
            a2[i] = s.clone();
            n += l;
        }
        System.out.println("Sorting "+N+" arrays of "+n+" elements");
        long t0 = System.currentTimeMillis();
        for(int i=0; i<N; i++) {
            c+=DoubleArrays.sortDec(a1[i]);
        }
        long t1 = System.currentTimeMillis();
        for(int i=0; i<N; i++) {
            java.util.Arrays.sort(a2[i]);
            reverse(a2[i]);
        }
        long t2 = System.currentTimeMillis();
        for(int i=0; i<N; i++) {
            assertArrayEquals("sort("+DoubleArrays.join(",",a[i])+")",a2[i],a1[i]);
        }
        System.out.println("Sorted "+N+" arrays of "+n+" elements in "+(t1-t0)+"ms, "+c+" operations ("+(t2-t1)+"ms for java.util.DoubleArrays.sort)");
    }

    public void testSortInc() throws Exception {
        double[] ary = {0};
        DoubleArrays.sortInc(ary);
        assertArrayEquals("heapSort(0)", new double[] { 0 }, ary);
        ary = new double[]{3,2,1,1,4,4,6,5,7,2};
        DoubleArrays.sortInc(ary);
        assertArrayEquals("heapSort(3,2,1,1,4,4,6,5,7,2)", new double[] { 1, 1, 2, 2, 3, 4, 4, 5, 6, 7 }, ary);
        int N = 500; // Max number of objects that will fit in normal heap size: 16*500^2 = 4m
        double[][] a = new double[N][];
        double[][] a1 = new double[N][];
        double[][] a2 = new double[N][];
        Random rand = new Random();
        int n = 0;
        int c = 0;
        for(int i=0; i<N; i++) {
            int l = rand.nextInt(N);
            double[] s = new double[l];
            for(int j=0; j<l; j++) s[j]=rand.nextDouble();
            a[i] = s;
            a1[i] = s.clone();
            a2[i] = s.clone();
            n += l;
        }
        System.out.println("Sorting "+N+" arrays of "+n+" elements");
        long t0 = System.currentTimeMillis();
        for(int i=0; i<N; i++) {
            c+=DoubleArrays.sortInc(a1[i]);
        }
        long t1 = System.currentTimeMillis();
        for(int i=0; i<N; i++) {
            java.util.Arrays.sort(a2[i]);
        }
        long t2 = System.currentTimeMillis();
        for(int i=0; i<N; i++) {
            assertArrayEquals("sort("+DoubleArrays.join(",",a[i])+")",a2[i],a1[i]);
        }
        System.out.println("Sorted "+N+" arrays of "+n+" elements in "+(t1-t0)+"ms, "+c+" operations ("+(t2-t1)+"ms for java.util.DoubleArrays.sort)");
    }

    public void testSearchDec() throws Exception {
        double[] a1 = reverse(new double[] {});
        assertEquals("",-1,DoubleArrays.searchDec(a1, 0, 0, 1.01));
        double[] a2 = reverse(new double[] { 2.02});
        assertEquals("",-2,DoubleArrays.searchDec(a2, 0, 1, 3.03));
        double[] a3 = reverse(new double[] { 2.02});
        assertEquals("",-1,DoubleArrays.searchDec(a3, 0, 1, 1.01));
        assertEquals("",0,DoubleArrays.searchDec(a3, 0, 1, 2.02));
        double[] a4 = reverse(new double[]{2.02, 3.03, 4.04, 7.07, 8.08});
        assertEquals("",-6,DoubleArrays.searchDec(a4, 0, 5, 5.05));
        assertEquals("",2,DoubleArrays.searchDec(a4, 0, 5, 4.04));
    }

    public void testSearchInc() throws Exception {
        double[] a1 = new double[] {};
        assertEquals("",-1,DoubleArrays.searchInc(a1, 0, 0, 1.01));
        double[] a2 = new double[] { 2.02};
        assertEquals("",-2,DoubleArrays.searchInc(a2, 0, 1, 3.03));
        double[] a3 = new double[] { 2.02};
        assertEquals("",-1,DoubleArrays.searchInc(a3, 0, 1, 1.01));
        assertEquals("",0,DoubleArrays.searchInc(a3, 0, 1, 2.02));
        double[] a4 = new double[]{2.02, 3.03, 4.04, 7.07, 8.08};
        assertEquals("",-4,DoubleArrays.searchInc(a4, 0, 5, 5.05));
        assertEquals("",2,DoubleArrays.searchInc(a4, 0, 5, 4.04));
    }

//    public void testInsert() throws Exception {
//        double[] a1 = new double[] {Double.NaN};
//        assertEquals("",0,DoubleArrays.insert(a1,0,0,1.01));
//        assertArrayEquals("",new double[]{1.01},a1);
//        double[] a2 = new double[] { 2.02, Double.NaN};
//        assertEquals("",1,DoubleArrays.insert(a2,0,1,3.03));
//        assertArrayEquals("",new double[]{2.02,3.03},a2);
//        double[] a3 = new double[] { 2.02, Double.NaN};
//        assertEquals("",0,DoubleArrays.insert(a3,0,1,1.01));
//        assertArrayEquals("",new double[]{1.01,2.02},a3);
//        double[] a4 = new double[]{2.02, 3.03, 4.04, 7.07, 8.08, Double.NaN};
//        assertEquals("",3,DoubleArrays.insert(a4,0,5,5.05));
//        assertArrayEquals("",new double[]{2.02, 3.03, 4.04, 5.05, 7.07, 8.08},a4);
//    }

    public void testUnionDec() throws Exception {
        assertArrayEquals("",
                reverse(new double[] { }),
                DoubleArrays.unionDec(reverse(new double[] { }), 0, 0, reverse(new double[] { }), 0, 0));
        assertArrayEquals("",
                reverse(new double[]{2.02, 3.03, 4.04, 5.05, 6.06}),
                DoubleArrays.unionDec(reverse(new double[] { }), 0, 0, reverse(new double[] { 2.02, 3.03, 4.04, 5.05, 6.06 }), 0, 5));
        assertArrayEquals("",
                reverse(new double[] { 1.01, 2.02, 3.03, 4.04, 5.05, 6.06, 7.07 }),
                DoubleArrays.unionDec(reverse(new double[] { 1.01, 3.03, 7.07 }), 0, 3, reverse(new double[] { 2.02, 3.03, 4.04, 5.05, 6.06 }), 0, 5));
    }

    public void testUnionInc() throws Exception {
        assertArrayEquals("",
                new double[]{},
                DoubleArrays.unionInc(new double[] { }, 0, 0, new double[] { }, 0, 0));
        assertArrayEquals("",
                new double[]{2.02, 3.03, 4.04, 5.05, 6.06},
                DoubleArrays.unionInc(new double[] { }, 0, 0, new double[] { 2.02, 3.03, 4.04, 5.05, 6.06 }, 0, 5));
        assertArrayEquals("",
                new double[]{1.01, 2.02, 3.03, 4.04, 5.05, 6.06, 7.07},
                DoubleArrays.unionInc(new double[] { 1.01, 3.03, 7.07 }, 0, 3, new double[] { 2.02, 3.03, 4.04, 5.05, 6.06 }, 0, 5));
    }

    public void testInterDec() throws Exception {
        assertArrayEquals("",
                          reverse(new double[]{}),
                          DoubleArrays.interDec(reverse(new double[] { }), 0, 0, reverse(new double[] { }), 0, 0));
        assertArrayEquals("",
                          reverse(new double[]{}),
                          DoubleArrays.interDec(reverse(new double[] { }), 0, 0, reverse(new double[] { 2.02, 3.03, 4.04, 5.05, 6.06 }), 0, 5));
        assertArrayEquals("",
                          reverse(new double[]{3.03}),
                          DoubleArrays.interDec(reverse(new double[] { 1.01, 3.03, 7.07 }), 0, 3, reverse(new double[] { 2.02, 3.03, 4.04, 5.05, 6.06 }), 0, 5));
        assertArrayEquals("",
                          reverse(new double[]{3.03, 6.06}),
                          DoubleArrays.interDec(reverse(new double[] { 1.01, 3.03, 6.06 }), 0, 3, reverse(new double[] { 2.02, 3.03, 4.04, 5.05, 6.06 }), 0, 5));
    }

    public void testInterInc() throws Exception {
        assertArrayEquals("",
                          new double[]{},
                          DoubleArrays.interInc(new double[] { }, 0, 0, new double[] { }, 0, 0));
        assertArrayEquals("",
                          new double[]{},
                          DoubleArrays.interInc(new double[] { }, 0, 0, new double[] { 2.02, 3.03, 4.04, 5.05, 6.06 }, 0, 5));
        assertArrayEquals("",
                          new double[]{3.03},
                          DoubleArrays.interInc(new double[] { 1.01, 3.03, 7.07 }, 0, 3, new double[] { 2.02, 3.03, 4.04, 5.05, 6.06 }, 0, 5));
        assertArrayEquals("",
                          new double[]{3.03, 6.06},
                          DoubleArrays.interInc(new double[] { 1.01, 3.03, 6.06 }, 0, 3, new double[] { 2.02, 3.03, 4.04, 5.05, 6.06 }, 0, 5));
    }

    public void testAppend() throws Exception {
        assertArrayEquals("",
                          new double[]{1.01, 2.02, 3.03, 4.04, 5.05, 6.06},
                          DoubleArrays.append(new double[]{1.01, 2.02, 3.03}, 4.04, 5.05, 6.06));
        assertArrayEquals("",
                          new double[]{1.01, 2.02, 3.03, 4.04, 5.05, 6.06},
                          DoubleArrays.append(new double[]{}, 1.01, 2.02, 3.03, 4.04, 5.05, 6.06));
        assertArrayEquals("",
                          new double[]{},
                          DoubleArrays.append(new double[]{}));
        assertArrayEquals("",
                          new double[]{1.01, 2.02, 3.03, 4.04, 5.05},
                          DoubleArrays.append(new double[]{1.01, 2.02, 3.03}, 4.04, 5.05));
        assertArrayEquals("",
                          new double[]{1.01, 2.02, 3.03},
                          DoubleArrays.append(new double[]{1.01, 2.02, 3.03}));
    }

    public void testPrepend() throws Exception {
        assertArrayEquals("",
                          new double[]{4.04, 5.05, 6.06, 1.01, 2.02, 3.03},
                          DoubleArrays.prepend(new double[]{1.01, 2.02, 3.03}, 4.04, 5.05, 6.06));
        assertArrayEquals("",
                          new double[]{1.01, 2.02, 3.03, 4.04, 5.05, 6.06},
                          DoubleArrays.prepend(new double[]{}, 1.01, 2.02, 3.03, 4.04, 5.05, 6.06));
        assertArrayEquals("",
                          new double[]{},
                          DoubleArrays.prepend(new double[]{}));
        assertArrayEquals("",
                          new double[]{4.04, 5.05, 1.01, 2.02, 3.03},
                          DoubleArrays.prepend(new double[]{1.01, 2.02, 3.03}, 4.04, 5.05));
        assertArrayEquals("",
                          new double[]{1.01, 2.02, 3.03},
                          DoubleArrays.prepend(new double[]{1.01, 2.02, 3.03}));
    }

    public void testConcat() throws Exception {
        assertArrayEquals("",
                          new double[]{1.01, 2.02, 3.03, 4.04, 5.05, 6.06},
                          DoubleArrays.concat(new double[]{1.01, 2.02, 3.03}, new double[]{4.04, 5.05, 6.06}));
        assertArrayEquals("",
                          new double[]{1.01, 2.02, 3.03, 4.04, 5.05, 6.06},
                          DoubleArrays.concat(new double[]{}, new double[]{1.01, 2.02, 3.03, 4.04, 5.05, 6.06}));
        assertArrayEquals("",
                          new double[]{},
                          DoubleArrays.concat(new double[]{}));
        assertArrayEquals("",
                          new double[]{1.01, 2.02, 3.03, 4.04, 5.05, 6.06},
                          DoubleArrays.concat(new double[]{1.01, 2.02, 3.03}, new double[]{4.04, 5.05}, new double[]{6.06}));
        assertArrayEquals("",
                          new double[]{1.01, 2.02, 3.03, 4.04, 5.05, 6.06},
                          DoubleArrays.concat(new double[]{1.01, 2.02, 3.03}, new double[]{}, new double[]{4.04, 5.05}, new double[]{6.06}));
        assertArrayEquals("",
                          new double[]{1.01, 2.02, 3.03, 4.04, 5.05, 6.06},
                          DoubleArrays.concat(new double[]{}, new double[]{1.01, 2.02, 3.03}, new double[]{4.04, 5.05}, new double[]{6.06}));

    }

    public void testSubarray() throws Exception {
        assertArrayEquals("subarray([],0,0)", new double[] { }, DoubleArrays.subarray(new double[]{}, 0, 0));
        assertArrayEquals("subarray([1,2,3,4,5,6],0,0)", new double[] { }, DoubleArrays.subarray(new double[] { 1, 2, 3, 4, 5, 6 }, 0, 0));
        assertArrayEquals("subarray([1,2,3,4,5,6],6,6)", new double[] { }, DoubleArrays.subarray(new double[] { 1, 2, 3, 4, 5, 6 }, 6, 6));
        assertArrayEquals("subarray([1,2,3,4,5,6],1,4)", new double[] { 2, 3, 4 }, DoubleArrays.subarray(new double[] { 1, 2, 3, 4, 5, 6 }, 1, 4));
        assertArrayEquals("subarray([1,2,3,4,5,6],2,3)", new double[] { 3 }, DoubleArrays.subarray(new double[] { 1, 2, 3, 4, 5, 6 }, 2, 3));
        assertArrayEquals("subarray([1,2,3,4,5,6],0,6)", new double[] { 1, 2, 3, 4, 5, 6 }, DoubleArrays.subarray(new double[] { 1, 2, 3, 4, 5, 6 }, 0, 6));
        assertArrayEquals("subarray([1,2,3,4,5,6],1,1)", new double[] { }, DoubleArrays.subarray(new double[] { 1, 2, 3, 4, 5, 6 }, 1, 1));
        try {
            DoubleArrays.subarray(new double[] { }, 0, 1);
            fail("subarray([],0,1)");
        } catch (ArrayIndexOutOfBoundsException e) {
        }
        try {
            DoubleArrays.subarray(new double[] { }, 1, 0);
            fail("subarray([],1,0)");
        } catch (ArrayIndexOutOfBoundsException e) {
        }
        try {
            DoubleArrays.subarray(new double[] { 1, 2, 3, 4, 5, 6 }, 3, 7);
            fail("subarray([1,2,3,4,5,6],3,7)");
        } catch (ArrayIndexOutOfBoundsException e) {
        }
        assertArrayEquals("subarray(['1','2','3','4','5','6'],0,6)", new double[] { 1.01, 2.02, 3.03, 4.04, 5.05, 6.06 },
                          DoubleArrays.subarray(new double[] { 1.01, 2.02, 3.03, 4.04, 5.05, 6.06 }, 0, 6));
    }

    public void testAsList() throws Exception {
        DoubleContainer list = DoubleArrays.container(1.01,2.02,3.03);
        assertEquals("asList(...).size()", 3, list.size());
        DoubleIterable.DoubleIterator it=list.iterator();
        assertEquals("asList(...).iterator().next()", 1.01, it.nextValue());
        assertEquals("asList(...).iterator().next().next()", 2.02, it.nextValue());
        assertEquals("asList(...).iterator().next().next().next()", 3.03, it.nextValue());
        assertFalse("asList(...).iterator().next().next().next().hasNext()", it.hasNext());
    }

    public void testStrgDouble() throws Exception {
        assertEquals("[0|]",DoubleArrays.toString(new double[]{}));
        assertEquals("[1|0.000000]",DoubleArrays.toString(new double[]{0}));
        assertEquals("[1|1.000000]",DoubleArrays.toString(new double[]{1}));
        assertEquals("[1|247.000000]",DoubleArrays.toString(new double[]{0xF7}));
        assertEquals("[5|0.000000,1.000000,2.200000,243.000000,4.000000]",DoubleArrays.toString(new double[]{0,1,2.2,0xF3,4}));
        assertEquals("[5|0.000000,1.000000,2.200000,243.000000,4.000000]",DoubleArrays.toString(new StringBuilder(),new double[]{0,1,2.2,0xF3,4}).toString());
        assertEquals("[5|0.000000,1.000000,2.200000,243.000000,4.000000]",DoubleArrays.toString(new StringBuffer(),new double[]{0,1,2.2,0xF3,4}).toString());
    }

    public void testJoinDouble() throws Exception {
        assertEquals("",DoubleArrays.join(":",new double[]{}));
        assertEquals("0.000000",DoubleArrays.join(":",new double[]{0}));
        assertEquals("1.000000",DoubleArrays.join(":",new double[]{1}));
        assertEquals("247.000000",DoubleArrays.join(":",new double[]{0xF7}));
        assertEquals("0.000000:1.000000:2.200000:243.000000:4.000000",DoubleArrays.join(":",new double[]{0,1,2.2,0xF3,4}));
        assertEquals("0.000000:1.000000:2.200000:243.000000:4.000000",DoubleArrays.join(new StringBuilder(),":",new double[]{0,1,2.2,0xF3,4}).toString());
        assertEquals("0.000000:1.000000:2.200000:243.000000:4.000000",DoubleArrays.join(new StringBuffer(),":",new double[]{0,1,2.2,0xF3,4}).toString());
        assertEquals("0.0000001.0000002.200000243.0000004.000000",DoubleArrays.join(null,new double[]{0,1,2.2,0xF3,4}));
        assertEquals("0.0000001.0000002.200000243.0000004.000000",DoubleArrays.join(new StringBuilder(),null,new double[]{0,1,2.2,0xF3,4}).toString());
        assertEquals("0.0000001.0000002.200000243.0000004.000000",DoubleArrays.join(new StringBuffer(),null,new double[]{0,1,2.2,0xF3,4}).toString());
    }

}
