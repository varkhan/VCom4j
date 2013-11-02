/**
 *
 */
package net.varkhan.base.containers.array;

import junit.framework.TestCase;

import java.util.Random;


/**
 * @author varkhan
 * @date Feb 26, 2010
 * @time 7:12:39 PM
 */
public class ByteArraysTest extends TestCase {

    public void testVariadic() {
        long[] vals={
                0L,
                1L,
                2L,
                127L,
                128L,
                129L,
                128L*128L-1,
                128L*128L,
                128L*128L+1,
                -1L,
                1234567890123456789L,
        };
        for(int i=0;i<vals.length;i++) {
            long val=vals[i];
            int len=ByteArrays.lenVariadic(val);
            byte[] a=new byte[20];
            ByteArrays.setVariadic(a, 0, val);
            System.out.println(val+" "+len+" "+StringArrays.toString(a));
            assertEquals("setVariadic().len", 0, a[len]);
            assertTrue("setVariadic()[len]", a[len-1]>=0);
            assertTrue("setVariadic()[0]", len==1||a[0]<0);
            long var=ByteArrays.getVariadic(a, 0);
            assertEquals("getVariadic()", val, var);
        }
    }

    public static void assertArrayEquals(String message, byte[] expected, byte[] actual) {
        if(expected==null) { if(actual==null) return; }
        else if(expected.length==actual.length) {
            boolean same = true;
            for(int i=0; i<expected.length; i++) if(expected[i]!=actual[i]) { same=false; break; }
            if(same) return;
        }
        fail(message+";\n expected: ["+StringArrays.join(",",expected)+"];\n   actual: ["+StringArrays.join(",",actual)+"]");
    }

    public static byte[] reverse(byte[] a) {
        for(int i1=0, i2=a.length-1; i1<i2; i1++, i2--) {
            byte t = a[i2];
            a[i2] = a[i1];
            a[i1] = t;
        }
        return a;
    }

    public void testEquals() throws Exception {
        assertTrue("equals([],[])", ByteArrays.equals(new byte[] { }, new byte[] { }));
        assertTrue("equals([1,2,3,\"6\"],[1,2,3,\"6\"])", ByteArrays.equals(new byte[] { 1, 2, 3, 6 }, new byte[] { 1, 2, 3, 6 }));
        assertFalse("equals([1,2,3,\"6\"],[5,2,3,\"6\"])", ByteArrays.equals(new byte[] { 1, 2, 3, 6 }, new byte[] { 5, 2, 3, 6 }));
        assertFalse("equals([1,2,3,\"6\"],[1,2,3,0xFF])", ByteArrays.equals(new byte[] { 1, 2, 3, 6 }, new byte[] { 5, 2, 3, (byte)0xFF }));
        assertFalse("equals([1,2,3,\"6\"],[1,2,3,\"6\",10])", ByteArrays.equals(new byte[] { 1, 2, 3, 6 }, new byte[] { 5, 2, 3, 6, 10 }));
    }

    public void testIndexOf() throws Exception {
        assertEquals("indexOf(0xFF,1,2,\"3\",0xFF)", 3, ByteArrays.indexOf((byte)0xFF,(byte)1,(byte)2,(byte)3,(byte)0xFF));
        assertEquals("indexOf(0xFF,0xFF,2,\"3\",4)", 0, ByteArrays.indexOf((byte)0xFF, (byte)0xFF, (byte)2, (byte)3, (byte)4));
        assertEquals("indexOf(0xFF,1,2,\"3\",4)", -1, ByteArrays.indexOf((byte)0xFF, (byte)1, (byte)2, (byte)3, (byte)4));
        assertEquals("indexOf(1,1,2,\"3\",0xFF)", 0, ByteArrays.indexOf((byte)1, (byte)1, (byte)2, (byte)3, (byte)0xFF));
        assertEquals("indexOf(4,1,2,\"3\",0xFF)", -1, ByteArrays.indexOf((byte)4, (byte)1, (byte)2, (byte)3, (byte)0xFF));
        assertEquals("indexOf(4,1,2,\"3\",4)", 3, ByteArrays.indexOf((byte)4, (byte)1, (byte)2, (byte)3, (byte)4));
        assertEquals("indexOf(\"3\",1,2,\"3\",4)", 2, ByteArrays.indexOf((byte)3, (byte)1, (byte)2, (byte)3, (byte)4));
    }

    public void testSortDec() throws Exception {
        byte[] ary = {0};
        ByteArrays.heapSortDec(ary);
        assertArrayEquals("heapSort(0)", new byte[] { 0 }, ary);
        ary = new byte[]{3,2,1,1,4,4,6,5,7,2};
        ByteArrays.heapSortDec(ary);
        assertArrayEquals("heapSort(3,2,1,1,4,4,6,5,7,2)", reverse(new byte[] { 1, 1, 2, 2, 3, 4, 4, 5, 6, 7 }), ary);
        int N = 500; // Max number of objects that will fit in normal heap size: 16*500^2 = 4m
        byte[][] a = new byte[N][];
        byte[][] a1 = new byte[N][];
        byte[][] a2 = new byte[N][];
        Random rand = new Random();
        int n = 0;
        int c = 0;
        for(int i=0; i<N; i++) {
            int l = rand.nextInt(N);
            byte[] s = new byte[l];
            for(int j=0; j<l; j++) s[j]=(byte)rand.nextInt(256);
            a[i] = s;
            a1[i] = s.clone();
            a2[i] = s.clone();
            n += l;
        }
        System.out.println("Sorting "+N+" arrays of "+n+" elements");
        long t0 = System.currentTimeMillis();
        for(int i=0; i<N; i++) {
            c+=ByteArrays.heapSortDec(a1[i]);
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
        System.out.println("Sorted "+N+" arrays of "+n+" elements in "+(t1-t0)+"ms, "+c+" operations ("+(t2-t1)+"ms for java.util.ByteArrays.sort)");
    }

    public void testSortInc() throws Exception {
        byte[] ary = {0};
        ByteArrays.heapSortInc(ary);
        assertArrayEquals("heapSort(0)", new byte[] { 0 }, ary);
        ary = new byte[]{3,2,1,1,4,4,6,5,7,2};
        ByteArrays.heapSortInc(ary);
        assertArrayEquals("heapSort(3,2,1,1,4,4,6,5,7,2)", new byte[] { 1, 1, 2, 2, 3, 4, 4, 5, 6, 7 }, ary);
        int N = 500; // Max number of objects that will fit in normal heap size: 16*500^2 = 4m
        byte[][] a = new byte[N][];
        byte[][] a1 = new byte[N][];
        byte[][] a2 = new byte[N][];
        Random rand = new Random();
        int n = 0;
        int c = 0;
        for(int i=0; i<N; i++) {
            int l = rand.nextInt(N);
            byte[] s = new byte[l];
            for(int j=0; j<l; j++) s[j]=(byte)rand.nextInt(256);
            a[i] = s;
            a1[i] = s.clone();
            a2[i] = s.clone();
            n += l;
        }
        System.out.println("Sorting "+N+" arrays of "+n+" elements");
        long t0 = System.currentTimeMillis();
        for(int i=0; i<N; i++) {
            c+=ByteArrays.heapSortInc(a1[i]);
        }
        long t1 = System.currentTimeMillis();
        for(int i=0; i<N; i++) {
            java.util.Arrays.sort(a2[i]);
        }
        long t2 = System.currentTimeMillis();
        for(int i=0; i<N; i++) {
            assertArrayEquals("sort("+StringArrays.join(",",a[i])+")",a2[i],a1[i]);
        }
        System.out.println("Sorted "+N+" arrays of "+n+" elements in "+(t1-t0)+"ms, "+c+" operations ("+(t2-t1)+"ms for java.util.ByteArrays.sort)");
    }

    public void testSearchDec() throws Exception {
        byte[] a1 = reverse(new byte[] {});
        assertEquals("",-1,ByteArrays.searchDec(a1, 0, 0, (byte)1));
        byte[] a2 = reverse(new byte[] { 2});
        assertEquals("",-2,ByteArrays.searchDec(a2, 0, 1, (byte)3));
        byte[] a3 = reverse(new byte[] { 2});
        assertEquals("",-1,ByteArrays.searchDec(a3, 0, 1, (byte)1));
        assertEquals("",0,ByteArrays.searchDec(a3, 0, 1, (byte)2));
        byte[] a4 = reverse(new byte[]{2, 3, 4, 7, 8});
        assertEquals("",-6,ByteArrays.searchDec(a4, 0, 5, (byte)5));
        assertEquals("",2,ByteArrays.searchDec(a4, 0, 5, (byte)4));
    }

    public void testSearchInc() throws Exception {
        byte[] a1 = new byte[] {};
        assertEquals("",-1,ByteArrays.searchInc(a1, 0, 0, (byte)1));
        byte[] a2 = new byte[] { 2};
        assertEquals("",-2,ByteArrays.searchInc(a2, 0, 1, (byte)3));
        byte[] a3 = new byte[] { 2};
        assertEquals("",-1,ByteArrays.searchInc(a3, 0, 1, (byte)1));
        assertEquals("",0,ByteArrays.searchInc(a3, 0, 1, (byte)2));
        byte[] a4 = new byte[]{2, 3, 4, 7, 8};
        assertEquals("",-4,ByteArrays.searchInc(a4, 0, 5, (byte)5));
        assertEquals("",2,ByteArrays.searchInc(a4, 0, 5, (byte)4));
    }

//    public void testInsert() throws Exception {
//        byte[] a1 = new byte[] {0xFF};
//        assertEquals("",0,ByteArrays.insert(a1,0,0,1));
//        assertArrayEquals("",new byte[]{1},a1);
//        byte[] a2 = new byte[] { 2, 0xFF};
//        assertEquals("",1,ByteArrays.insert(a2,0,1,3));
//        assertArrayEquals("",new byte[]{2,3},a2);
//        byte[] a3 = new byte[] { 2, 0xFF};
//        assertEquals("",0,ByteArrays.insert(a3,0,1,1));
//        assertArrayEquals("",new byte[]{1,2},a3);
//        byte[] a4 = new byte[]{2, 3, 4, 7, 8, 0xFF};
//        assertEquals("",3,ByteArrays.insert(a4,0,5,5));
//        assertArrayEquals("",new byte[]{2, 3, 4, 5, 7, 8},a4);
//    }

    public void testUnionDec() throws Exception {
        assertArrayEquals("",
                reverse(new byte[]{}),
                ByteArrays.unionDec(reverse(new byte[] { }), 0, 0, reverse(new byte[] { }), 0, 0));
        assertArrayEquals("",
                reverse(new byte[]{2, 3, 4, 5, 6}),
                ByteArrays.unionDec(reverse(new byte[] { }), 0, 0, reverse(new byte[] { 2, 3, 4, 5, 6 }), 0, 5));
        assertArrayEquals("",
                reverse(new byte[]{1, 2, 3, 4, 5, 6, 7}),
                ByteArrays.unionDec(reverse(new byte[] { 1, 3, 7 }), 0, 3, reverse(new byte[] { 2, 3, 4, 5, 6 }), 0, 5));
    }

    public void testUnionInc() throws Exception {
        assertArrayEquals("",
                new byte[]{},
                ByteArrays.unionInc(new byte[] { }, 0, 0, new byte[] { }, 0, 0));
        assertArrayEquals("",
                new byte[]{2, 3, 4, 5, 6},
                ByteArrays.unionInc(new byte[] { }, 0, 0, new byte[] { 2, 3, 4, 5, 6 }, 0, 5));
        assertArrayEquals("",
                new byte[]{1, 2, 3, 4, 5, 6, 7},
                ByteArrays.unionInc(new byte[] { 1, 3, 7 }, 0, 3, new byte[] { 2, 3, 4, 5, 6 }, 0, 5));
    }

    public void testInterDec() throws Exception {
        assertArrayEquals("",
                          reverse(new byte[]{}),
                          ByteArrays.interDec(reverse(new byte[] { }), 0, 0, reverse(new byte[] { }), 0, 0));
        assertArrayEquals("",
                          reverse(new byte[]{}),
                          ByteArrays.interDec(reverse(new byte[] { }), 0, 0, reverse(new byte[] { 2, 3, 4, 5, 6 }), 0, 5));
        assertArrayEquals("",
                          reverse(new byte[]{3}),
                          ByteArrays.interDec(reverse(new byte[] { 1, 3, 7 }), 0, 3, reverse(new byte[] { 2, 3, 4, 5, 6 }), 0, 5));
        assertArrayEquals("",
                          reverse(new byte[]{3, 6}),
                          ByteArrays.interDec(reverse(new byte[] { 1, 3, 6 }), 0, 3, reverse(new byte[] { 2, 3, 4, 5, 6 }), 0, 5));
    }

    public void testInterInc() throws Exception {
        assertArrayEquals("",
                          new byte[]{},
                          ByteArrays.interInc(new byte[] { }, 0, 0, new byte[] { }, 0, 0));
        assertArrayEquals("",
                          new byte[]{},
                          ByteArrays.interInc(new byte[] { }, 0, 0, new byte[] { 2, 3, 4, 5, 6 }, 0, 5));
        assertArrayEquals("",
                          new byte[]{3},
                          ByteArrays.interInc(new byte[] { 1, 3, 7 }, 0, 3, new byte[] { 2, 3, 4, 5, 6 }, 0, 5));
        assertArrayEquals("",
                          new byte[]{3, 6},
                          ByteArrays.interInc(new byte[] { 1, 3, 6 }, 0, 3, new byte[] { 2, 3, 4, 5, 6 }, 0, 5));
    }

    public void testAppend() throws Exception {
        assertArrayEquals("",
                          new byte[]{1, 2, 3, 4, 5, 6},
                          ByteArrays.append(new byte[]{1, 2, 3}, (byte)4, (byte)5, (byte)6));
        assertArrayEquals("",
                          new byte[]{1, 2, 3, 4, 5, 6},
                          ByteArrays.append(new byte[]{}, (byte)1, (byte)2, (byte)3, (byte)4, (byte)5, (byte)6));
        assertArrayEquals("",
                          new byte[]{},
                          ByteArrays.append(new byte[]{}));
        assertArrayEquals("",
                          new byte[]{1, 2, 3, 4, 5},
                          ByteArrays.append(new byte[]{1, 2, 3}, (byte)4, (byte)5));
        assertArrayEquals("",
                          new byte[]{1, 2, 3},
                          ByteArrays.append(new byte[]{1, 2, 3}));
    }

    public void testPrepend() throws Exception {
        assertArrayEquals("",
                          new byte[]{4, 5, 6, 1, 2, 3},
                          ByteArrays.prepend(new byte[]{1, 2, 3}, (byte)4, (byte)5, (byte)6));
        assertArrayEquals("",
                          new byte[]{1, 2, 3, 4, 5, 6},
                          ByteArrays.prepend(new byte[]{}, (byte)1, (byte)2, (byte)3, (byte)4, (byte)5, (byte)6));
        assertArrayEquals("",
                          new byte[]{},
                          ByteArrays.prepend(new byte[]{}));
        assertArrayEquals("",
                          new byte[]{4, 5, 1, 2, 3},
                          ByteArrays.prepend(new byte[]{1, 2, 3}, (byte)4, (byte)5));
        assertArrayEquals("",
                          new byte[]{1, 2, 3},
                          ByteArrays.prepend(new byte[]{1, 2, 3}));
    }

    public void testConcat() throws Exception {
        assertArrayEquals("",
                          new byte[]{1, 2, 3, 4, 5, 6},
                          ByteArrays.concat(new byte[]{1, 2, 3}, new byte[]{4, 5, 6}));
        assertArrayEquals("",
                          new byte[]{1, 2, 3, 4, 5, 6},
                          ByteArrays.concat(new byte[]{}, new byte[]{1, 2, 3, 4, 5, 6}));
        assertArrayEquals("",
                          new byte[]{},
                          ByteArrays.concat(new byte[]{}));
        assertArrayEquals("",
                          new byte[]{1, 2, 3, 4, 5, 6},
                          ByteArrays.concat(new byte[]{1, 2, 3}, new byte[]{4, 5}, new byte[]{6}));
        assertArrayEquals("",
                          new byte[]{1, 2, 3, 4, 5, 6},
                          ByteArrays.concat(new byte[]{1, 2, 3}, new byte[]{}, new byte[]{4, 5}, new byte[]{6}));
        assertArrayEquals("",
                          new byte[]{1, 2, 3, 4, 5, 6},
                          ByteArrays.concat(new byte[]{}, new byte[]{1, 2, 3}, new byte[]{4, 5}, new byte[]{6}));

    }

    public void testSubarray() throws Exception {
        assertArrayEquals("subarray([],0,0)", new byte[] { }, ByteArrays.subarray(new byte[]{}, 0, 0));
        assertArrayEquals("subarray([1,2,3,4,5,6],0,0)", new byte[] { }, ByteArrays.subarray(new byte[] { 1, 2, 3, 4, 5, 6 }, 0, 0));
        assertArrayEquals("subarray([1,2,3,4,5,6],6,6)", new byte[] { }, ByteArrays.subarray(new byte[] { 1, 2, 3, 4, 5, 6 }, 6, 6));
        assertArrayEquals("subarray([1,2,3,4,5,6],1,4)", new byte[] { 2, 3, 4 }, ByteArrays.subarray(new byte[] { 1, 2, 3, 4, 5, 6 }, 1, 4));
        assertArrayEquals("subarray([1,2,3,4,5,6],2,3)", new byte[] { 3 }, ByteArrays.subarray(new byte[] { 1, 2, 3, 4, 5, 6 }, 2, 3));
        assertArrayEquals("subarray([1,2,3,4,5,6],0,6)", new byte[] { 1, 2, 3, 4, 5, 6 }, ByteArrays.subarray(new byte[] { 1, 2, 3, 4, 5, 6 }, 0, 6));
        assertArrayEquals("subarray([1,2,3,4,5,6],1,1)", new byte[] { }, ByteArrays.subarray(new byte[] { 1, 2, 3, 4, 5, 6 }, 1, 1));
        try {
            ByteArrays.subarray(new byte[] { }, 0, 1);
            fail("subarray([],0,1)");
        } catch (ArrayIndexOutOfBoundsException e) {
        }
        try {
            ByteArrays.subarray(new byte[] { }, 1, 0);
            fail("subarray([],1,0)");
        } catch (ArrayIndexOutOfBoundsException e) {
        }
        try {
            ByteArrays.subarray(new byte[] { 1, 2, 3, 4, 5, 6 }, 3, 7);
            fail("subarray([1,2,3,4,5,6],3,7)");
        } catch (ArrayIndexOutOfBoundsException e) {
        }
        assertArrayEquals("subarray(['1','2','3','4','5','6'],0,6)", new byte[] { 1, 2, 3, 4, 5, 6 },
                          ByteArrays.subarray(new byte[] { 1, 2, 3, 4, 5, 6 }, 0, 6));
    }

}
