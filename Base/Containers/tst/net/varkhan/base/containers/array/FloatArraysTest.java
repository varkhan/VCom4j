package net.varkhan.base.containers.array;

import junit.framework.TestCase;
import net.varkhan.base.containers.type.FloatContainer;
import net.varkhan.base.containers.type.FloatIterable;

import java.util.Random;


/**
 * <b></b>.
 * <p/>
 *
 * @author varkhan
 * @date 3/24/12
 * @time 9:39 PM
 */
public class FloatArraysTest extends TestCase {

    public static void assertArrayEquals(String message, float[] expected, float[] actual) {
        if(expected==null) { if(actual==null) return; }
        else if(expected.length==actual.length) {
            boolean same = true;
            for(int i=0; i<expected.length; i++) if(expected[i]!=actual[i]) { same=false; break; }
            if(same) return;
        }
        fail(message+";\n expected: ["+FloatArrays.join(",", expected)+"];\n   actual: ["+FloatArrays.join(",", actual)+"]");
    }

    public static float[] reverse(float[] a) {
        for(int i1=0, i2=a.length-1; i1<i2; i1++, i2--) {
            float t = a[i2];
            a[i2] = a[i1];
            a[i1] = t;
        }
        return a;
    }

    public void testEquals() throws Exception {
        assertTrue("equals([],[])", FloatArrays.equals(new float[] { }, new float[] { }));
        assertTrue("equals([1,2,3,\"6\"],[1,2,3,\"6\"])", FloatArrays.equals(new float[] { 1, 2, 3, 6.06f }, new float[] { 1, 2, 3, 6.06f }));
        assertFalse("equals([1,2,3,\"6\"],[5,2,3,\"6\"])", FloatArrays.equals(new float[] { 1, 2, 3, 6.06f }, new float[] { 5, 2, 3, 6.06f }));
        assertFalse("equals([1,2,3,\"6\"],[1,2,3,Float.NaN])", FloatArrays.equals(new float[] { 1, 2, 3, 6.06f }, new float[] { 5, 2, 3, Float.NaN }));
        assertFalse("equals([1,2,3,\"6\"],[1,2,3,\"6\",10])", FloatArrays.equals(new float[] { 1, 2, 3, 6.06f }, new float[] { 5, 2, 3, 6.06f, 10 }));
    }

    public void testIndexOf() throws Exception {
        assertEquals("indexOf(Float.NaN,1,2,\"3\",Float.NaN)", -1, FloatArrays.indexOf(Float.NaN,1,2,3.03f,Float.NaN));
        assertEquals("indexOf(Float.NaN,Float.NaN,2,\"3\",4)", -1, FloatArrays.indexOf(Float.NaN, Float.NaN, 2, 3.03f, 4));
        assertEquals("indexOf(Float.NaN,1,2,\"3\",4)", -1, FloatArrays.indexOf(Float.NaN, 1, 2, 3.03f, 4));
        assertEquals("indexOf(1,1,2,\"3\",Float.NaN)", 0, FloatArrays.indexOf(1, 1, 2, 3.03f, Float.NaN));
        assertEquals("indexOf(4,1,2,\"3\",Float.NaN)", -1, FloatArrays.indexOf(4, 1, 2, 3.03f, Float.NaN));
        assertEquals("indexOf(4,1,2,\"3\",4)", 3, FloatArrays.indexOf(4, 1, 2, 3.03f, 4));
        assertEquals("indexOf(\"3\",1,2,\"3\",4)", 2, FloatArrays.indexOf(3.03f, 1, 2, 3.03f, 4));
    }

    public void testIndexOfArray() throws Exception {
        assertEquals("indexOf([], 0, [1,2,3,6])", 0, FloatArrays.indexOf(new float[]{}, 0, new float[]{ 1, 2, 3.03f, 6 }));
        assertEquals("indexOf([2], 0, [1,2,3,6])", 1, FloatArrays.indexOf(new float[]{2}, 0, new float[]{ 1, 2, 3.03f, 6 }));
        assertEquals("indexOf([2,3], 0, [1,2,3,6])", 1, FloatArrays.indexOf(new float[]{2, 3.03f}, 0, new float[]{ 1, 2, 3.03f, 6 }));
        assertEquals("indexOf([2,3], 2, [1,2,3,6])", -1, FloatArrays.indexOf(new float[]{2, 3.03f}, 2, new float[]{ 1, 2, 3.03f, 6 }));
        assertEquals("indexOf([2,6], 0, [1,2,3,6])", -1, FloatArrays.indexOf(new float[]{2, 6}, 0, new float[]{ 1, 2, 3.03f, 6 }));
    }

    public void testSortDec() throws Exception {
        float[] ary = {0};
        FloatArrays.heapSortDec(ary);
        assertArrayEquals("heapSort(0)", new float[] { 0 }, ary);
        ary = new float[]{3,2,1,1,4,4,6,5,7,2};
        FloatArrays.heapSortDec(ary);
        assertArrayEquals("heapSort(3,2,1,1,4,4,6,5,7,2)", reverse(new float[] { 1, 1, 2, 2, 3, 4, 4, 5, 6, 7 }), ary);
        int N = 500; // Max number of objects that will fit in normal heap size: 16*500^2 = 4m
        float[][] a = new float[N][];
        float[][] a1 = new float[N][];
        float[][] a2 = new float[N][];
        Random rand = new Random();
        int n = 0;
        int c = 0;
        for(int i=0; i<N; i++) {
            int l = rand.nextInt(N);
            float[] s = new float[l];
            for(int j=0; j<l; j++) s[j]=rand.nextFloat();
            a[i] = s;
            a1[i] = s.clone();
            a2[i] = s.clone();
            n += l;
        }
        System.out.println("Sorting "+N+" arrays of "+n+" elements");
        long t0 = System.currentTimeMillis();
        for(int i=0; i<N; i++) {
            c+=FloatArrays.heapSortDec(a1[i]);
        }
        long t1 = System.currentTimeMillis();
        for(int i=0; i<N; i++) {
            java.util.Arrays.sort(a2[i]);
            reverse(a2[i]);
        }
        long t2 = System.currentTimeMillis();
        for(int i=0; i<N; i++) {
            assertArrayEquals("sort("+FloatArrays.join(",",a[i])+")",a2[i],a1[i]);
        }
        System.out.println("Sorted "+N+" arrays of "+n+" elements in "+(t1-t0)+"ms, "+c+" operations ("+(t2-t1)+"ms for java.util.FloatArrays.sort)");
    }

    public void testSortInc() throws Exception {
        float[] ary = {0};
        FloatArrays.heapSortInc(ary);
        assertArrayEquals("heapSort(0)", new float[] { 0 }, ary);
        ary = new float[]{3,2,1,1,4,4,6,5,7,2};
        FloatArrays.heapSortInc(ary);
        assertArrayEquals("heapSort(3,2,1,1,4,4,6,5,7,2)", new float[] { 1, 1, 2, 2, 3, 4, 4, 5, 6, 7 }, ary);
        int N = 500; // Max number of objects that will fit in normal heap size: 16*500^2 = 4m
        float[][] a = new float[N][];
        float[][] a1 = new float[N][];
        float[][] a2 = new float[N][];
        Random rand = new Random();
        int n = 0;
        int c = 0;
        for(int i=0; i<N; i++) {
            int l = rand.nextInt(N);
            float[] s = new float[l];
            for(int j=0; j<l; j++) s[j]=rand.nextFloat();
            a[i] = s;
            a1[i] = s.clone();
            a2[i] = s.clone();
            n += l;
        }
        System.out.println("Sorting "+N+" arrays of "+n+" elements");
        long t0 = System.currentTimeMillis();
        for(int i=0; i<N; i++) {
            c+=FloatArrays.heapSortInc(a1[i]);
        }
        long t1 = System.currentTimeMillis();
        for(int i=0; i<N; i++) {
            java.util.Arrays.sort(a2[i]);
        }
        long t2 = System.currentTimeMillis();
        for(int i=0; i<N; i++) {
            assertArrayEquals("sort("+FloatArrays.join(",",a[i])+")",a2[i],a1[i]);
        }
        System.out.println("Sorted "+N+" arrays of "+n+" elements in "+(t1-t0)+"ms, "+c+" operations ("+(t2-t1)+"ms for java.util.FloatArrays.sort)");
    }

    public void testSearchDec() throws Exception {
        float[] a1 = reverse(new float[] {});
        assertEquals("",-1,FloatArrays.searchDec(a1, 0, 0, 1.01f));
        float[] a2 = reverse(new float[] { 2.02f});
        assertEquals("",-2,FloatArrays.searchDec(a2, 0, 1, 3.03f));
        float[] a3 = reverse(new float[] { 2.02f});
        assertEquals("",-1,FloatArrays.searchDec(a3, 0, 1, 1.01f));
        assertEquals("",0,FloatArrays.searchDec(a3, 0, 1, 2.02f));
        float[] a4 = reverse(new float[]{2.02f, 3.03f, 4.04f, 7.07f, 8.08f});
        assertEquals("",-6,FloatArrays.searchDec(a4, 0, 5, 5.05f));
        assertEquals("",2,FloatArrays.searchDec(a4, 0, 5, 4.04f));
    }

    public void testSearchInc() throws Exception {
        float[] a1 = new float[] {};
        assertEquals("",-1,FloatArrays.searchInc(a1, 0, 0, 1.01f));
        float[] a2 = new float[] { 2.02f};
        assertEquals("",-2,FloatArrays.searchInc(a2, 0, 1, 3.03f));
        float[] a3 = new float[] { 2.02f};
        assertEquals("",-1,FloatArrays.searchInc(a3, 0, 1, 1.01f));
        assertEquals("",0,FloatArrays.searchInc(a3, 0, 1, 2.02f));
        float[] a4 = new float[]{2.02f, 3.03f, 4.04f, 7.07f, 8.08f};
        assertEquals("",-4,FloatArrays.searchInc(a4, 0, 5, 5.05f));
        assertEquals("",2,FloatArrays.searchInc(a4, 0, 5, 4.04f));
    }

//    public void testInsert() throws Exception {
//        float[] a1 = new float[] {Float.NaN};
//        assertEquals("",0,FloatArrays.insert(a1,0,0,1.01f));
//        assertArrayEquals("",new float[]{1.01f},a1);
//        float[] a2 = new float[] { 2.02f, Float.NaN};
//        assertEquals("",1,FloatArrays.insert(a2,0,1,3.03f));
//        assertArrayEquals("",new float[]{2.02f,3.03f},a2);
//        float[] a3 = new float[] { 2.02f, Float.NaN};
//        assertEquals("",0,FloatArrays.insert(a3,0,1,1.01f));
//        assertArrayEquals("",new float[]{1.01f,2.02f},a3);
//        float[] a4 = new float[]{2.02f, 3.03f, 4.04f, 7.07f, 8.08f, Float.NaN};
//        assertEquals("",3,FloatArrays.insert(a4,0,5,5.05f));
//        assertArrayEquals("",new float[]{2.02f, 3.03f, 4.04f, 5.05f, 7.07f, 8.08f},a4);
//    }

    public void testUnionDec() throws Exception {
        assertArrayEquals("",
                reverse(new float[]{}),
                FloatArrays.unionDec(reverse(new float[] { }), 0, 0, reverse(new float[] { }), 0, 0));
        assertArrayEquals("",
                reverse(new float[]{2.02f, 3.03f, 4.04f, 5.05f, 6.06f}),
                FloatArrays.unionDec(reverse(new float[] { }), 0, 0, reverse(new float[] { 2.02f, 3.03f, 4.04f, 5.05f, 6.06f }), 0, 5));
        assertArrayEquals("",
                reverse(new float[]{1.01f, 2.02f, 3.03f, 4.04f, 5.05f, 6.06f, 7.07f}),
                FloatArrays.unionDec(reverse(new float[] { 1.01f, 3.03f, 7.07f }), 0, 3, reverse(new float[] { 2.02f, 3.03f, 4.04f, 5.05f, 6.06f }), 0, 5));
    }

    public void testUnionInc() throws Exception {
        assertArrayEquals("",
                new float[]{},
                FloatArrays.unionInc(new float[] { }, 0, 0, new float[] { }, 0, 0));
        assertArrayEquals("",
                new float[]{2.02f, 3.03f, 4.04f, 5.05f, 6.06f},
                FloatArrays.unionInc(new float[] { }, 0, 0, new float[] { 2.02f, 3.03f, 4.04f, 5.05f, 6.06f }, 0, 5));
        assertArrayEquals("",
                new float[]{1.01f, 2.02f, 3.03f, 4.04f, 5.05f, 6.06f, 7.07f},
                FloatArrays.unionInc(new float[] { 1.01f, 3.03f, 7.07f }, 0, 3, new float[] { 2.02f, 3.03f, 4.04f, 5.05f, 6.06f }, 0, 5));
    }

    public void testInterDec() throws Exception {
        assertArrayEquals("",
                          reverse(new float[]{}),
                          FloatArrays.interDec(reverse(new float[] { }), 0, 0, reverse(new float[] { }), 0, 0));
        assertArrayEquals("",
                          reverse(new float[]{}),
                          FloatArrays.interDec(reverse(new float[] { }), 0, 0, reverse(new float[] { 2.02f, 3.03f, 4.04f, 5.05f, 6.06f }), 0, 5));
        assertArrayEquals("",
                          reverse(new float[]{3.03f}),
                          FloatArrays.interDec(reverse(new float[] { 1.01f, 3.03f, 7.07f }), 0, 3, reverse(new float[] { 2.02f, 3.03f, 4.04f, 5.05f, 6.06f }), 0, 5));
        assertArrayEquals("",
                          reverse(new float[]{3.03f, 6.06f}),
                          FloatArrays.interDec(reverse(new float[] { 1.01f, 3.03f, 6.06f }), 0, 3, reverse(new float[] { 2.02f, 3.03f, 4.04f, 5.05f, 6.06f }), 0, 5));
    }

    public void testInterInc() throws Exception {
        assertArrayEquals("",
                          new float[]{},
                          FloatArrays.interInc(new float[] { }, 0, 0, new float[] { }, 0, 0));
        assertArrayEquals("",
                          new float[]{},
                          FloatArrays.interInc(new float[] { }, 0, 0, new float[] { 2.02f, 3.03f, 4.04f, 5.05f, 6.06f }, 0, 5));
        assertArrayEquals("",
                          new float[]{3.03f},
                          FloatArrays.interInc(new float[] { 1.01f, 3.03f, 7.07f }, 0, 3, new float[] { 2.02f, 3.03f, 4.04f, 5.05f, 6.06f }, 0, 5));
        assertArrayEquals("",
                          new float[]{3.03f, 6.06f},
                          FloatArrays.interInc(new float[] { 1.01f, 3.03f, 6.06f }, 0, 3, new float[] { 2.02f, 3.03f, 4.04f, 5.05f, 6.06f }, 0, 5));
    }

    public void testAppend() throws Exception {
        assertArrayEquals("",
                          new float[]{1.01f, 2.02f, 3.03f, 4.04f, 5.05f, 6.06f},
                          FloatArrays.append(new float[]{1.01f, 2.02f, 3.03f}, 4.04f, 5.05f, 6.06f));
        assertArrayEquals("",
                          new float[]{1.01f, 2.02f, 3.03f, 4.04f, 5.05f, 6.06f},
                          FloatArrays.append(new float[]{}, 1.01f, 2.02f, 3.03f, 4.04f, 5.05f, 6.06f));
        assertArrayEquals("",
                          new float[]{},
                          FloatArrays.append(new float[]{}));
        assertArrayEquals("",
                          new float[]{1.01f, 2.02f, 3.03f, 4.04f, 5.05f},
                          FloatArrays.append(new float[]{1.01f, 2.02f, 3.03f}, 4.04f, 5.05f));
        assertArrayEquals("",
                          new float[]{1.01f, 2.02f, 3.03f},
                          FloatArrays.append(new float[]{1.01f, 2.02f, 3.03f}));
    }

    public void testPrepend() throws Exception {
        assertArrayEquals("",
                          new float[]{4.04f, 5.05f, 6.06f, 1.01f, 2.02f, 3.03f},
                          FloatArrays.prepend(new float[]{1.01f, 2.02f, 3.03f}, 4.04f, 5.05f, 6.06f));
        assertArrayEquals("",
                          new float[]{1.01f, 2.02f, 3.03f, 4.04f, 5.05f, 6.06f},
                          FloatArrays.prepend(new float[]{}, 1.01f, 2.02f, 3.03f, 4.04f, 5.05f, 6.06f));
        assertArrayEquals("",
                          new float[]{},
                          FloatArrays.prepend(new float[]{}));
        assertArrayEquals("",
                          new float[]{4.04f, 5.05f, 1.01f, 2.02f, 3.03f},
                          FloatArrays.prepend(new float[]{1.01f, 2.02f, 3.03f}, 4.04f, 5.05f));
        assertArrayEquals("",
                          new float[]{1.01f, 2.02f, 3.03f},
                          FloatArrays.prepend(new float[]{1.01f, 2.02f, 3.03f}));
    }

    public void testConcat() throws Exception {
        assertArrayEquals("",
                          new float[]{1.01f, 2.02f, 3.03f, 4.04f, 5.05f, 6.06f},
                          FloatArrays.concat(new float[]{1.01f, 2.02f, 3.03f}, new float[]{4.04f, 5.05f, 6.06f}));
        assertArrayEquals("",
                          new float[]{1.01f, 2.02f, 3.03f, 4.04f, 5.05f, 6.06f},
                          FloatArrays.concat(new float[]{}, new float[]{1.01f, 2.02f, 3.03f, 4.04f, 5.05f, 6.06f}));
        assertArrayEquals("",
                          new float[]{},
                          FloatArrays.concat(new float[]{}));
        assertArrayEquals("",
                          new float[]{1.01f, 2.02f, 3.03f, 4.04f, 5.05f, 6.06f},
                          FloatArrays.concat(new float[]{1.01f, 2.02f, 3.03f}, new float[]{4.04f, 5.05f}, new float[]{6.06f}));
        assertArrayEquals("",
                          new float[]{1.01f, 2.02f, 3.03f, 4.04f, 5.05f, 6.06f},
                          FloatArrays.concat(new float[]{1.01f, 2.02f, 3.03f}, new float[]{}, new float[]{4.04f, 5.05f}, new float[]{6.06f}));
        assertArrayEquals("",
                          new float[]{1.01f, 2.02f, 3.03f, 4.04f, 5.05f, 6.06f},
                          FloatArrays.concat(new float[]{}, new float[]{1.01f, 2.02f, 3.03f}, new float[]{4.04f, 5.05f}, new float[]{6.06f}));

    }

    public void testSubarray() throws Exception {
        assertArrayEquals("subarray([],0,0)", new float[] { }, FloatArrays.subarray(new float[]{}, 0, 0));
        assertArrayEquals("subarray([1,2,3,4,5,6],0,0)", new float[] { }, FloatArrays.subarray(new float[] { 1, 2, 3, 4, 5, 6 }, 0, 0));
        assertArrayEquals("subarray([1,2,3,4,5,6],6,6)", new float[] { }, FloatArrays.subarray(new float[] { 1, 2, 3, 4, 5, 6 }, 6, 6));
        assertArrayEquals("subarray([1,2,3,4,5,6],1,4)", new float[] { 2, 3, 4 }, FloatArrays.subarray(new float[] { 1, 2, 3, 4, 5, 6 }, 1, 4));
        assertArrayEquals("subarray([1,2,3,4,5,6],2,3)", new float[] { 3 }, FloatArrays.subarray(new float[] { 1, 2, 3, 4, 5, 6 }, 2, 3));
        assertArrayEquals("subarray([1,2,3,4,5,6],0,6)", new float[] { 1, 2, 3, 4, 5, 6 }, FloatArrays.subarray(new float[] { 1, 2, 3, 4, 5, 6 }, 0, 6));
        assertArrayEquals("subarray([1,2,3,4,5,6],1,1)", new float[] { }, FloatArrays.subarray(new float[] { 1, 2, 3, 4, 5, 6 }, 1, 1));
        try {
            FloatArrays.subarray(new float[] { }, 0, 1);
            fail("subarray([],0,1)");
        } catch (ArrayIndexOutOfBoundsException e) {
        }
        try {
            FloatArrays.subarray(new float[] { }, 1, 0);
            fail("subarray([],1,0)");
        } catch (ArrayIndexOutOfBoundsException e) {
        }
        try {
            FloatArrays.subarray(new float[] { 1, 2, 3, 4, 5, 6 }, 3, 7);
            fail("subarray([1,2,3,4,5,6],3,7)");
        } catch (ArrayIndexOutOfBoundsException e) {
        }
        assertArrayEquals("subarray(['1','2','3','4','5','6'],0,6)", new float[] { 1.01f, 2.02f, 3.03f, 4.04f, 5.05f, 6.06f },
                          FloatArrays.subarray(new float[] { 1.01f, 2.02f, 3.03f, 4.04f, 5.05f, 6.06f }, 0, 6));
    }

    public void testAsList() throws Exception {
        FloatContainer list = FloatArrays.container(1.01f,2.02f,3.03f);
        assertEquals("asList(...).size()", 3, list.size());
        FloatIterable.FloatIterator it=list.iterator();
        assertEquals("asList(...).iterator().next()", 1.01f, it.nextValue());
        assertEquals("asList(...).iterator().next().next()", 2.02f, it.nextValue());
        assertEquals("asList(...).iterator().next().next().next()", 3.03f, it.nextValue());
        assertFalse("asList(...).iterator().next().next().next().hasNext()", it.hasNext());
    }

    public void testStrgFloat() throws Exception {
        assertEquals("[0|]",FloatArrays.toString(new float[]{}));
        assertEquals("[1|0.000000]",FloatArrays.toString(new float[]{0}));
        assertEquals("[1|1.000000]",FloatArrays.toString(new float[]{1}));
        assertEquals("[1|247.000000]",FloatArrays.toString(new float[]{0xF7}));
        assertEquals("[5|0.000000,1.000000,2.200000,243.000000,4.000000]",FloatArrays.toString(new float[]{0,1,2.2f,0xF3,4}));
        assertEquals("[5|0.000000,1.000000,2.200000,243.000000,4.000000]",FloatArrays.toString(new StringBuilder(),new float[]{0,1,2.2f,0xF3,4}).toString());
        assertEquals("[5|0.000000,1.000000,2.200000,243.000000,4.000000]",FloatArrays.toString(new StringBuffer(),new float[]{0,1,2.2f,0xF3,4}).toString());
    }

    public void testJoinFloat() throws Exception {
        assertEquals("",FloatArrays.join(":",new float[]{}));
        assertEquals("0.000000",FloatArrays.join(":",new float[]{0}));
        assertEquals("1.000000",FloatArrays.join(":",new float[]{1}));
        assertEquals("247.000000",FloatArrays.join(":",new float[]{0xF7}));
        assertEquals("0.000000:1.000000:2.200000:243.000000:4.000000",FloatArrays.join(":",new float[]{0,1,2.2f,0xF3,4}));
        assertEquals("0.000000:1.000000:2.200000:243.000000:4.000000",FloatArrays.join(new StringBuilder(),":",new float[]{0,1,2.2f,0xF3,4}).toString());
        assertEquals("0.000000:1.000000:2.200000:243.000000:4.000000",FloatArrays.join(new StringBuffer(),":",new float[]{0,1,2.2f,0xF3,4}).toString());
        assertEquals("0.0000001.0000002.200000243.0000004.000000",FloatArrays.join(null,new float[]{0,1,2.2f,0xF3,4}));
        assertEquals("0.0000001.0000002.200000243.0000004.000000",FloatArrays.join(new StringBuilder(),null,new float[]{0,1,2.2f,0xF3,4}).toString());
        assertEquals("0.0000001.0000002.200000243.0000004.000000",FloatArrays.join(new StringBuffer(),null,new float[]{0,1,2.2f,0xF3,4}).toString());
    }


}
