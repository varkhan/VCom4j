/**
 *
 */
package net.varkhan.base.containers.array;

import junit.framework.TestCase;

import java.util.Random;


/**
 * @author varkhan
 * @date Mar 12, 2009
 * @time 6:07:21 PM
 */
public class CharArraysTest extends TestCase {

    public static void assertArrayEquals(String message, char[] expected, char[] actual) {
        if(expected==null) { if(actual==null) return; }
        else if(expected.length==actual.length) {
            boolean same = true;
            for(int i=0; i<expected.length; i++) if(expected[i]!=actual[i]) { same=false; break; }
            if(same) return;
        }
        fail(message+";\n expected: ["+StringArrays.join(",", expected)+"];\n   actual: ["+StringArrays.join(",", actual)+"]");
    }

    private static void assertArrayEquals(String msg, String[] exp, String[] ret) {
        assertEquals(msg, toString(exp), toString(ret));
    }

    private static void assertArrayEquals(String msg, CharSequence[] exp, CharSequence[] ret) {
        assertEquals(msg, toString(exp), toString(ret));
    }

    public static char[] reverse(char[] a) {
        for(int i1=0, i2=a.length-1; i1<i2; i1++, i2--) {
            char t = a[i2];
            a[i2] = a[i1];
            a[i1] = t;
        }
        return a;
    }

    private static String[] toString(char[][] a) {
        if(a==null) return null;
        if(a.length==0) return new String[0];
        String[] s=new String[a.length];
        for(int i=0;i<a.length;i++) {
            s[i]=new String(a[i]);
        }
        return s;
    }

    private static String toString(CharSequence[] a) {
        if(a==null) return "null";
        if(a.length==0) return "[]";
        return "[\""+StringArrays.join("\",\"", a)+"\"]";
    }

    private static String toString(String[] a) {
        if(a==null) return "null";
        if(a.length==0) return "[]";
        return "[\""+StringArrays.join("\",\"", a)+"\"]";
    }

    public void testEquals() throws Exception {
        assertTrue("equals([],[])", CharArrays.equals(new char[] { }, new char[] { }));
        assertTrue("equals([1,2,3,\"6\"],[1,2,3,\"6\"])", CharArrays.equals(new char[] { 1, 2, 3, 6 }, new char[] { 1, 2, 3, 6 }));
        assertFalse("equals([1,2,3,\"6\"],[5,2,3,\"6\"])", CharArrays.equals(new char[] { 1, 2, 3, 6 }, new char[] { 5, 2, 3, 6 }));
        assertFalse("equals([1,2,3,\"6\"],[1,2,3,256])", CharArrays.equals(new char[] { 1, 2, 3, 6 }, new char[] { 5, 2, 3, 256 }));
        assertFalse("equals([1,2,3,\"6\"],[1,2,3,\"6\",10])", CharArrays.equals(new char[] { 1, 2, 3, 6 }, new char[] { 5, 2, 3, 6, 10 }));
    }

    public void testEqualsCharSequence() throws Exception {
        assertTrue("equals([],[])", CharArrays.equals("", ""));
        assertTrue("equals([1,2,3,\"6\"],[1,2,3,\"6\"])", CharArrays.equals("1236", "1236"));
        assertFalse("equals([1,2,3,\"6\"],[5,2,3,\"6\"])", CharArrays.equals("1236", "5236"));
        assertFalse("equals([1,2,3,\"6\"],[1,2,3])", CharArrays.equals("1236","123"));
        assertFalse("equals([1,2,3,\"6\"],[1,2,3,256])", CharArrays.equals("1236","123\0"));
    }

    public void testIndexOf() throws Exception {
        assertEquals("indexOf(Char.NaN,1,2,\"3\",Char.NaN)", 3, CharArrays.indexOf((char)256,(char)1,(char)2,(char)3,(char)256));
        assertEquals("indexOf(Char.NaN,Char.NaN,2,\"3\",4)", 0, CharArrays.indexOf((char)256, (char)256, (char)2, (char)3, (char)4));
        assertEquals("indexOf(Char.NaN,1,2,\"3\",4)", -1, CharArrays.indexOf((char)256, (char)1, (char)2, (char)3, (char)4));
        assertEquals("indexOf(1,1,2,\"3\",Char.NaN)", 0, CharArrays.indexOf((char)1, (char)1, (char)2, (char)3, (char)256));
        assertEquals("indexOf(4,1,2,\"3\",Char.NaN)", -1, CharArrays.indexOf((char)4, (char)1, (char)2, (char)3, (char)256));
        assertEquals("indexOf(4,1,2,\"3\",4)", 3, CharArrays.indexOf((char)4, (char)1, (char)2, (char)3, (char)4));
        assertEquals("indexOf(\"3\",1,2,\"3\",4)", 2, CharArrays.indexOf((char)3, (char)1, (char)2, (char)3, (char)4));
    }

    public void testSortDec() throws Exception {
        char[] ary = {0};
        CharArrays.heapSortDec(ary);
        assertArrayEquals("heapSort(0)", new char[] { 0 }, ary);
        ary = new char[]{3,2,1,1,4,4,6,5,7,2};
        CharArrays.heapSortDec(ary);
        assertArrayEquals("heapSort(3,2,1,1,4,4,6,5,7,2)", reverse(new char[] { 1, 1, 2, 2, 3, 4, 4, 5, 6, 7 }), ary);
        int N = 500; // Max number of objects that will fit in normal heap size: 16*500^2 = 4m
        char[][] a = new char[N][];
        char[][] a1 = new char[N][];
        char[][] a2 = new char[N][];
        Random rand = new Random();
        int n = 0;
        int c = 0;
        for(int i=0; i<N; i++) {
            int l = rand.nextInt(N);
            char[] s = new char[l];
            for(int j=0; j<l; j++) s[j]=(char)rand.nextInt(1<<15);
            a[i] = s;
            a1[i] = s.clone();
            a2[i] = s.clone();
            n += l;
        }
        System.out.println("Sorting "+N+" arrays of "+n+" elements");
        long t0 = System.currentTimeMillis();
        for(int i=0; i<N; i++) {
            c+=CharArrays.heapSortDec(a1[i]);
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
        System.out.println("Sorted "+N+" arrays of "+n+" elements in "+(t1-t0)+"ms, "+c+" operations ("+(t2-t1)+"ms for java.util.CharArrays.sort)");
    }

    public void testSortInc() throws Exception {
        char[] ary = {0};
        CharArrays.heapSortInc(ary);
        assertArrayEquals("heapSort(0)", new char[] { 0 }, ary);
        ary = new char[]{3,2,1,1,4,4,6,5,7,2};
        CharArrays.heapSortInc(ary);
        assertArrayEquals("heapSort(3,2,1,1,4,4,6,5,7,2)", new char[] { 1, 1, 2, 2, 3, 4, 4, 5, 6, 7 }, ary);
        int N = 500; // Max number of objects that will fit in normal heap size: 16*500^2 = 4m
        char[][] a = new char[N][];
        char[][] a1 = new char[N][];
        char[][] a2 = new char[N][];
        Random rand = new Random();
        int n = 0;
        int c = 0;
        for(int i=0; i<N; i++) {
            int l = rand.nextInt(N);
            char[] s = new char[l];
            for(int j=0; j<l; j++) s[j]=(char)rand.nextInt(1<<15);
            a[i] = s;
            a1[i] = s.clone();
            a2[i] = s.clone();
            n += l;
        }
        System.out.println("Sorting "+N+" arrays of "+n+" elements");
        long t0 = System.currentTimeMillis();
        for(int i=0; i<N; i++) {
            c+=CharArrays.heapSortInc(a1[i]);
        }
        long t1 = System.currentTimeMillis();
        for(int i=0; i<N; i++) {
            java.util.Arrays.sort(a2[i]);
        }
        long t2 = System.currentTimeMillis();
        for(int i=0; i<N; i++) {
            assertArrayEquals("sort("+StringArrays.join(",",a[i])+")",a2[i],a1[i]);
        }
        System.out.println("Sorted "+N+" arrays of "+n+" elements in "+(t1-t0)+"ms, "+c+" operations ("+(t2-t1)+"ms for java.util.CharArrays.sort)");
    }

    public void testSearchDec() throws Exception {
        char[] a1 = reverse(new char[] {});
        assertEquals("",-1,CharArrays.searchDec(a1, 0, 0, (char)1));
        char[] a2 = reverse(new char[] { 2});
        assertEquals("",-2,CharArrays.searchDec(a2, 0, 1, (char)3));
        char[] a3 = reverse(new char[] { 2});
        assertEquals("",-1,CharArrays.searchDec(a3, 0, 1, (char)1));
        assertEquals("",0,CharArrays.searchDec(a3, 0, 1, (char)2));
        char[] a4 = reverse(new char[]{2, 3, 4, 7, 8});
        assertEquals("",-6,CharArrays.searchDec(a4, 0, 5, (char)5));
        assertEquals("",2,CharArrays.searchDec(a4, 0, 5, (char)4));
    }

    public void testSearchInc() throws Exception {
        char[] a1 = new char[] {};
        assertEquals("",-1,CharArrays.searchInc(a1, 0, 0, (char)1));
        char[] a2 = new char[] { 2};
        assertEquals("",-2,CharArrays.searchInc(a2, 0, 1, (char)3));
        char[] a3 = new char[] { 2};
        assertEquals("",-1,CharArrays.searchInc(a3, 0, 1, (char)1));
        assertEquals("",0,CharArrays.searchInc(a3, 0, 1, (char)2));
        char[] a4 = new char[]{2, 3, 4, 7, 8};
        assertEquals("",-4,CharArrays.searchInc(a4, 0, 5, (char)5));
        assertEquals("",2,CharArrays.searchInc(a4, 0, 5, (char)4));
    }

//    public void testInsert() throws Exception {
//        char[] a1 = new char[] {Char.NaN};
//        assertEquals("",0,CharArrays.insert(a1,0,0,1));
//        assertArrayEquals("",new char[]{1},a1);
//        char[] a2 = new char[] { 2, Char.NaN};
//        assertEquals("",1,CharArrays.insert(a2,0,1,3));
//        assertArrayEquals("",new char[]{2,3},a2);
//        char[] a3 = new char[] { 2, Char.NaN};
//        assertEquals("",0,CharArrays.insert(a3,0,1,1));
//        assertArrayEquals("",new char[]{1,2},a3);
//        char[] a4 = new char[]{2, 3, 4, 7, 8, Char.NaN};
//        assertEquals("",3,CharArrays.insert(a4,0,5,5));
//        assertArrayEquals("",new char[]{2, 3, 4, 5, 7, 8},a4);
//    }

    public void testUnionDec() throws Exception {
        assertArrayEquals("",
                reverse(new char[] { }),
                CharArrays.unionDec(reverse(new char[] { }), 0, 0, reverse(new char[] { }), 0, 0));
        assertArrayEquals("",
                reverse(new char[]{2, 3, 4, 5, 6}),
                CharArrays.unionDec(reverse(new char[] { }), 0, 0, reverse(new char[] { 2, 3, 4, 5, 6 }), 0, 5));
        assertArrayEquals("",
                reverse(new char[] { 1, 2, 3, 4, 5, 6, 7 }),
                CharArrays.unionDec(reverse(new char[] { 1, 3, 7 }), 0, 3, reverse(new char[] { 2, 3, 4, 5, 6 }), 0, 5));
    }

    public void testUnionInc() throws Exception {
        assertArrayEquals("",
                new char[]{},
                CharArrays.unionInc(new char[] { }, 0, 0, new char[] { }, 0, 0));
        assertArrayEquals("",
                new char[]{2, 3, 4, 5, 6},
                CharArrays.unionInc(new char[] { }, 0, 0, new char[] { 2, 3, 4, 5, 6 }, 0, 5));
        assertArrayEquals("",
                new char[]{1, 2, 3, 4, 5, 6, 7},
                CharArrays.unionInc(new char[] { 1, 3, 7 }, 0, 3, new char[] { 2, 3, 4, 5, 6 }, 0, 5));
    }

    public void testInterDec() throws Exception {
        assertArrayEquals("",
                          reverse(new char[]{}),
                          CharArrays.interDec(reverse(new char[] { }), 0, 0, reverse(new char[] { }), 0, 0));
        assertArrayEquals("",
                          reverse(new char[]{}),
                          CharArrays.interDec(reverse(new char[] { }), 0, 0, reverse(new char[] { 2, 3, 4, 5, 6 }), 0, 5));
        assertArrayEquals("",
                          reverse(new char[]{3}),
                          CharArrays.interDec(reverse(new char[] { 1, 3, 7 }), 0, 3, reverse(new char[] { 2, 3, 4, 5, 6 }), 0, 5));
        assertArrayEquals("",
                          reverse(new char[]{3, 6}),
                          CharArrays.interDec(reverse(new char[] { 1, 3, 6 }), 0, 3, reverse(new char[] { 2, 3, 4, 5, 6 }), 0, 5));
    }

    public void testInterInc() throws Exception {
        assertArrayEquals("",
                          new char[]{},
                          CharArrays.interInc(new char[] { }, 0, 0, new char[] { }, 0, 0));
        assertArrayEquals("",
                          new char[]{},
                          CharArrays.interInc(new char[] { }, 0, 0, new char[] { 2, 3, 4, 5, 6 }, 0, 5));
        assertArrayEquals("",
                          new char[]{3},
                          CharArrays.interInc(new char[] { 1, 3, 7 }, 0, 3, new char[] { 2, 3, 4, 5, 6 }, 0, 5));
        assertArrayEquals("",
                          new char[]{3, 6},
                          CharArrays.interInc(new char[] { 1, 3, 6 }, 0, 3, new char[] { 2, 3, 4, 5, 6 }, 0, 5));
    }

    public void testAppend() throws Exception {
        assertArrayEquals("",
                          new char[]{1, 2, 3, 4, 5, 6},
                          CharArrays.append(new char[]{1, 2, 3}, (char)4, (char)5, (char)6));
        assertArrayEquals("",
                          new char[]{1, 2, 3, 4, 5, 6},
                          CharArrays.append(new char[]{}, (char)1, (char)2, (char)3, (char)4, (char)5, (char)6));
        assertArrayEquals("",
                          new char[]{},
                          CharArrays.append(new char[]{}));
        assertArrayEquals("",
                          new char[]{1, 2, 3, 4, 5},
                          CharArrays.append(new char[]{1, 2, 3}, (char)4, (char)5));
        assertArrayEquals("",
                          new char[]{1, 2, 3},
                          CharArrays.append(new char[]{1, 2, 3}));
    }

    public void testPrepend() throws Exception {
        assertArrayEquals("",
                          new char[]{4, 5, 6, 1, 2, 3},
                          CharArrays.prepend(new char[]{1, 2, 3}, (char)4, (char)5, (char)6));
        assertArrayEquals("",
                          new char[]{1, 2, 3, 4, 5, 6},
                          CharArrays.prepend(new char[]{}, (char)1, (char)2, (char)3, (char)4, (char)5, (char)6));
        assertArrayEquals("",
                          new char[]{},
                          CharArrays.prepend(new char[]{}));
        assertArrayEquals("",
                          new char[]{4, 5, 1, 2, 3},
                          CharArrays.prepend(new char[]{1, 2, 3}, (char)4, (char)5));
        assertArrayEquals("",
                          new char[]{1, 2, 3},
                          CharArrays.prepend(new char[]{1, 2, 3}));
    }

    public void testConcat() throws Exception {
        assertArrayEquals("",
                          new char[]{1, 2, 3, 4, 5, 6},
                          CharArrays.concat(new char[]{1, 2, 3}, new char[]{4, 5, 6}));
        assertArrayEquals("",
                          new char[]{1, 2, 3, 4, 5, 6},
                          CharArrays.concat(new char[]{}, new char[]{1, 2, 3, 4, 5, 6}));
        assertArrayEquals("",
                          new char[]{},
                          CharArrays.concat(new char[]{}));
        assertArrayEquals("",
                          new char[]{1, 2, 3, 4, 5, 6},
                          CharArrays.concat(new char[]{1, 2, 3}, new char[]{4, 5}, new char[]{6}));
        assertArrayEquals("",
                          new char[]{1, 2, 3, 4, 5, 6},
                          CharArrays.concat(new char[]{1, 2, 3}, new char[]{}, new char[]{4, 5}, new char[]{6}));
        assertArrayEquals("",
                          new char[]{1, 2, 3, 4, 5, 6},
                          CharArrays.concat(new char[]{}, new char[]{1, 2, 3}, new char[]{4, 5}, new char[]{6}));

    }

    public void testSubarray() throws Exception {
        assertArrayEquals("subarray([],0,0)", new char[] { }, CharArrays.subarray(new char[]{}, 0, 0));
        assertArrayEquals("subarray([1,2,3,4,5,6],0,0)", new char[] { }, CharArrays.subarray(new char[] { 1, 2, 3, 4, 5, 6 }, 0, 0));
        assertArrayEquals("subarray([1,2,3,4,5,6],6,6)", new char[] { }, CharArrays.subarray(new char[] { 1, 2, 3, 4, 5, 6 }, 6, 6));
        assertArrayEquals("subarray([1,2,3,4,5,6],1,4)", new char[] { 2, 3, 4 }, CharArrays.subarray(new char[] { 1, 2, 3, 4, 5, 6 }, 1, 4));
        assertArrayEquals("subarray([1,2,3,4,5,6],2,3)", new char[] { 3 }, CharArrays.subarray(new char[] { 1, 2, 3, 4, 5, 6 }, 2, 3));
        assertArrayEquals("subarray([1,2,3,4,5,6],0,6)", new char[] { 1, 2, 3, 4, 5, 6 }, CharArrays.subarray(new char[] { 1, 2, 3, 4, 5, 6 }, 0, 6));
        assertArrayEquals("subarray([1,2,3,4,5,6],1,1)", new char[] { }, CharArrays.subarray(new char[] { 1, 2, 3, 4, 5, 6 }, 1, 1));
        try {
            CharArrays.subarray(new char[] { }, 0, 1);
            fail("subarray([],0,1)");
        } catch (ArrayIndexOutOfBoundsException e) {
        }
        try {
            CharArrays.subarray(new char[] { }, 1, 0);
            fail("subarray([],1,0)");
        } catch (ArrayIndexOutOfBoundsException e) {
        }
        try {
            CharArrays.subarray(new char[] { 1, 2, 3, 4, 5, 6 }, 3, 7);
            fail("subarray([1,2,3,4,5,6],3,7)");
        } catch (ArrayIndexOutOfBoundsException e) {
        }
        assertArrayEquals("subarray(['1','2','3','4','5','6'],0,6)", new char[] { 1, 2, 3, 4, 5, 6 },
                          CharArrays.subarray(new char[] { 1, 2, 3, 4, 5, 6 }, 0, 6));
    }


    public void testTr() throws Exception {
        assertEquals("tr(\"abcdefghijkl\",45djlk\",\"67DJL\")", "abcDefghiJkL", CharArrays.tr("abcdefghijkl", "45djlk".toCharArray(), "67DJL".toCharArray()));
        assertEquals("tr(\"abcdefghijkl\",45djlk\",\"67DJL\")", "abcDefghiJkL", CharArrays.tr(new StringBuilder("abcdefghijkl"), "45djlk".toCharArray(), "67DJL".toCharArray()));
        assertEquals("tr(\"abcdefghijkl\",45djlk\",\"67DJL\")", 4, CharArrays.tr("abcdefghijkl".toCharArray(), "45djlk".toCharArray(), "67DJL".toCharArray()));
        assertEquals("tr(\"abcdefghijkl\"[5,10],45djlk\",\"67DJL\")", 1, CharArrays.tr("abcdefghijkl".toCharArray(), 5,5,"45djlk".toCharArray(), "67DJL".toCharArray()));
    }

    public void testRepl() throws Exception {
        assertEquals("totoghiatatiti", CharArrays.repl("tototaaatatiti", "taa", "ghi"));
        assertEquals("totoghaaghtiti", CharArrays.repl("tototaaatatiti", "ta", "gh"));
        assertEquals("totoghaaghtiti", CharArrays.repl(new StringBuilder(),"tototaaatatiti", "ta".toCharArray(), "gh".toCharArray()).toString());
        assertEquals("totoghaaghtiti", CharArrays.repl(new StringBuffer(),"tototaaatatiti", "ta".toCharArray(), "gh".toCharArray()).toString());
        assertEquals("totoghiaghtiti", CharArrays.repl("tototaaatatiti", new String[] { "taa", "ta" }, new String[] { "ghi", "gh" }));
        assertEquals("totoghaaghtiti", CharArrays.repl("tototaaatatiti", new String[] { "ta", "taa" }, new String[] { "gh", "ghi" }));
        assertEquals("totoghaaghffi", CharArrays.repl("tototaaatatiti", new String[] { "ta", "taa", "tit" }, new String[] { "gh", "ghi", "ff" }));
        assertEquals("totoghaaghffi", CharArrays.repl(new StringBuilder(),"tototaaatatiti", new String[] { "ta", "taa", "tit" }, new String[] { "gh", "ghi", "ff" }).toString());
        assertEquals("totoghaaghffi", CharArrays.repl(new StringBuffer(), "tototaaatatiti", new String[] { "ta", "taa", "tit" }, new String[] { "gh", "ghi", "ff" }).toString());
        assertEquals("totoghaaghtiti", CharArrays.repl("tototaaatatiti", new String[] { "ta", "taa", "tip" }, new String[] { "gh", "ghi", "ff" }));
        assertEquals("totoghaaghtiti", CharArrays.repl(new StringBuilder(),"tototaaatatiti", new String[] { "ta", "taa", "tip" }, new String[] { "gh", "ghi", "ff" }).toString());
        assertEquals("totoghaaghtiti", CharArrays.repl(new StringBuffer(), "tototaaatatiti", new String[] { "ta", "taa", "tip" }, new String[] { "gh", "ghi", "ff" }).toString());
        assertEquals("totoghaaghtiti", CharArrays.repl(new StringBuilder(),"tototaaatatiti", new char[][] { "ta".toCharArray(), "taa".toCharArray(), "tip".toCharArray() }, new char[][] { "gh".toCharArray(), "ghi".toCharArray(), "ff".toCharArray() }).toString());
        assertEquals("totoghaaghtiti", CharArrays.repl(new StringBuffer(),"tototaaatatiti", new char[][] { "ta".toCharArray(), "taa".toCharArray(), "tip".toCharArray() }, new char[][] { "gh".toCharArray(), "ghi".toCharArray(), "ff".toCharArray() }).toString());
    }

    public void testChop() throws Exception {
        assertEquals("chop(\"tata:titi:tutu\",:)", "tata", CharArrays.chop("tata:titi:tutu", ':'));
        assertEquals("chop(\"tata:titi:tutu\",:)", "tata", CharArrays.chop(new StringBuilder("tata:titi:tutu"), ':').toString());
        assertEquals("chop(\"tata:titi:tutu\",:)", "tata", new String(CharArrays.chop("tata:titi:tutu".toCharArray(), ':')));
        assertEquals("chop(\"tata:titi:tutu\"[6,13],:)", "iti", new String(CharArrays.chop("tata:titi:tutu".toCharArray(), 6, 7, ':')));
    }

    public void testSplit() throws Exception {
        assertArrayEquals("split(\"tata:titi:tutu\",:,2)", new String[] { "tata", "titi:tutu" }, CharArrays.split("tata:titi:tutu", ':', 2));
        assertArrayEquals("split(\"tata:titi:tutu\",:,2)", new CharSequence[] { new StringBuilder("tata"), new StringBuilder("titi:tutu") }, CharArrays.split(new StringBuilder("tata:titi:tutu"), ':', 2));
        assertArrayEquals("split(\"tata:titi:tutu\"[6,13],:,2)", new String[] { "iti", "tut" }, toString(CharArrays.split("tata:titi:tutu".toCharArray(), 6, 7, ':', 2)));
        assertArrayEquals("split(\"tata:titi:tutu\",:,+INF)", new String[] { "tata", "titi", "tutu" }, CharArrays.split("tata:titi:tutu", ':', Integer.MAX_VALUE));
        assertArrayEquals("split(\"tata:titi:tutu\",:,+INF)", new String[] { "tata", "titi", "tutu" }, toString(CharArrays.split("tata:titi:tutu".toCharArray(), ':', Integer.MAX_VALUE)));
        assertArrayEquals("split(\"tata:titi:tutu:\",:,+INF)", new String[] { "tata", "titi", "tutu", "" }, CharArrays.split("tata:titi:tutu:", ':', Integer.MAX_VALUE));
    }

    public void testJoin() throws Exception {
        assertEquals("join(:,\"tata\", \"titi\", \"tutu\")","tata:titi:tutu",CharArrays.join(":", new String[] {"tata", "titi", "tutu"}));
        assertEquals("join(:,\"tata\", \"titi\", \"tutu\")","tata:titi:tutu",CharArrays.join(new StringBuilder(),":",new String[] { "tata", "titi", "tutu" }).toString());
        assertEquals("join(:,\"tata\", \"titi\", \"tutu\")","tata:titi:tutu",CharArrays.join(new StringBuffer(),":",new String[] { "tata", "titi", "tutu" }).toString());
        assertEquals("join(<,>,:,\"tata\", \"titi\", \"tutu\")","<tata>:<titi>:<tutu>:<>",CharArrays.join("<",">",":", new String[] {"tata", "titi", "tutu", ""}));
        assertEquals("join(<,>,:,\"tata\", \"titi\", \"tutu\")","<tata>:<titi>:<tutu>:<>",CharArrays.join(new StringBuilder(),"<",">",":",new String[] { "tata", "titi", "tutu", "" }).toString());
        assertEquals("join(<,>,:,\"tata\", \"titi\", \"tutu\")","<tata>:<titi>:<tutu>:<>",CharArrays.join(new StringBuffer(),"<",">",":",new String[] { "tata", "titi", "tutu", "" }).toString());
    }

    public void testFormat() throws Exception {
//        * <li/> <b>'%'</b>: outputs a literal '%' character, <em>without consuming an argument</em>
//        * <li/> <b>'c'</b>: formats the argument (which must be of type {@link java.lang.Character}) as a single character
//        * <li/> <b>'s'</b>: formats the argument (which must be of type {@link java.lang.CharSequence}) as a character sequence
//        * <li/> <b>'S'</b>: formats the argument (which must be {@literal null}, or of type {@link java.lang.CharSequence}) as a character sequence, outputting {@code "null"} if it is {@literal null}
//        * <li/> <b>'d'</b>: formats the argument (which must be of type {@link java.lang.Number}) as a signed decimal integer
//        * <li/> <b>'u'</b>: formats the argument (which must be of type {@link java.lang.Number}) as an unsigned decimal integer
//        * <li/> <b>'x'</b>: formats the argument (which must be of type {@link java.lang.Number}) as an hexadecimal integer, using lower case letters
//        * <li/> <b>'X'</b>: formats the argument (which must be of type {@link java.lang.Number}) as an hexadecimal integer, using upper case letters
        assertEquals("format(\"%s\",\"toto\"", "toto", CharArrays.format("%s", "toto"));
        assertEquals("format(\"%<.12S\",\"toto\"", "\"toto\"......", CharArrays.format("%<.12S", "toto"));
        assertEquals("format(\"%<.12S\",null", "null........", CharArrays.format("%<.12S", (String)null));
        assertEquals("format(\"%<|.12S\",\"foobarbazandmore\"", "\"foobarbazan", CharArrays.format("%<|.12S", "foobarbazandmore"));
        assertEquals("format(\"%S\",\"toto\"", "\"toto\"", CharArrays.format("%S", "toto"));
        assertEquals("format(\"%c\",\'W\'", "W", CharArrays.format("%c", 'W'));
        assertEquals("format(\"%c\",(int)\'W\'", "W", CharArrays.format("%c", (int)'W'));
        assertEquals("format(\"%<.12s\",\"toto\"", "toto........", CharArrays.format("%<.12s", "toto"));
        assertEquals("format(\"%>.12s\",\"toto\"", "........toto", CharArrays.format("%>.12s", "toto"));
        assertEquals("format(\"% .2u\",7", "111", CharArrays.format("% .2u", 7));
        assertEquals("format(\"%04.2u\",7", "0111", CharArrays.format("%04.2u", 7));
        assertEquals("format(\"%<04.2u\",7", "1110", CharArrays.format("%<04.2u", 7));
        assertEquals("format(\"%>04.4x\",7", "07ff", CharArrays.format("%>04.4x", 0x7ff));
        assertEquals("format(\"%>04.4X\",7", "07FF", CharArrays.format("%>04.4X", 0x7ff));
        assertEquals("format(\"%>04.2X\",7", "133333", CharArrays.format("%>04.2X", 0x7ff));
        assertEquals("format(\"%>|04.2X\",7", "3333", CharArrays.format("%>|04.2X", 0x7ff));
    }

}
