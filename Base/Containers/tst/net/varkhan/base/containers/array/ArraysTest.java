package net.varkhan.base.containers.array;

import junit.framework.TestCase;
import net.varkhan.base.containers.Iterator;
import net.varkhan.base.containers.list.List;
import net.varkhan.base.containers.map.Map;

import java.util.Comparator;
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
        if(!Arrays.equals(expected,actual)) fail(message+";\n expected: ["+Arrays.join(",",expected)+"];\n   actual: ["+Arrays.join(",",actual)+"]");
    }

    public void testEquals() throws Exception {
        assertTrue("equals([],[])", Arrays.equals(new Object[] {}, new Object[] {}));
        assertTrue("equals([1,2,3,\"6\"],[1,2,3,\"6\"])", Arrays.equals(new Object[] { 1, 2, 3, "6" }, new Object[] { 1, 2, 3, "6" }));
        assertFalse("equals([1,2,3,\"6\"],[5,2,3,\"6\"])", Arrays.equals(new Object[] { 1, 2, 3, "6" }, new Object[] { 5, 2, 3, "6" }));
        assertFalse("equals([1,2,3,\"6\"],[1,2,3,null])", Arrays.equals(new Object[] { 1, 2, 3, "6" }, new Object[] { 5, 2, 3, null }));
        assertFalse("equals([1,2,3,\"6\"],[1,2,3,\"6\",10])", Arrays.equals(new Object[] { 1, 2, 3, "6" }, new Object[] { 5, 2, 3, "6", 10 }));
    }

    public void testIndexOf() throws Exception {
        assertEquals("indexOf(null,1,2,\"3\",null)", 3, Arrays.indexOf(null,1,2,"3",null));
        assertEquals("indexOf(null,null,2,\"3\",4)", 0, Arrays.indexOf(null, null, 2, "3", 4));
        assertEquals("indexOf(null,1,2,\"3\",4)", -1, Arrays.indexOf(null, 1, 2, "3", 4));
        assertEquals("indexOf(1,1,2,\"3\",null)", 0, Arrays.indexOf(1, 1, 2, "3", null));
        assertEquals("indexOf(4,1,2,\"3\",null)", -1, Arrays.indexOf(4, 1, 2, "3", null));
        assertEquals("indexOf(4,1,2,\"3\",4)", 3, Arrays.indexOf(4, 1, 2, "3", 4));
        assertEquals("indexOf(\"3\",1,2,\"3\",4)", 2, Arrays.indexOf("3", 1, 2, "3", 4));
    }

    public void testSort() throws Exception {
        Integer[] ary = {0};
        Arrays.sort(ary);
        assertArrayEquals("heapSort(0)", new Integer[] { 0 }, ary);
        ary = new Integer[]{3,2,1,1,4,4,6,5,7,2};
        Arrays.sort(ary);
        assertArrayEquals("heapSort(3,2,1,1,4,4,6,5,7,2)", new Object[] { 1, 1, 2, 2, 3, 4, 4, 5, 6, 7 }, ary);
        int N = 500; // Max number of objects that will fit in normal heap size: 16*500^2 = 4m
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
            c+=Arrays.sort(a1[i]);
        }
        long t1 = System.currentTimeMillis();
        for(int i=0; i<N; i++) {
            java.util.Arrays.sort(a2[i]);
        }
        long t2 = System.currentTimeMillis();
        for(int i=0; i<N; i++) {
            assertArrayEquals("sort("+Arrays.join(",",a[i])+")",a2[i],a1[i]);
        }
        System.out.println("Sorted "+N+" arrays of "+n+" elements in "+(t1-t0)+"ms, "+c+" operations ("+(t2-t1)+"ms for java.util.Arrays.sort)");
    }

    public void testSortCmp() throws Exception {
        Comparator<String> cmp = new Comparator<String>() {
            @Override
            public int compare(String o1, String o2) {
                return o1.compareTo(o2);
//                if (o1.equals(o2)) return 0;
//                if (o1.length() == 0 && o2.length() == 0) return 0;
//                if (o1.length() == 0) return -1;
//                if (o2.length() == 0) return +1;
//                return o1.charAt(0) - o2.charAt(0);
            }
        };
        String[] ary = {"0"};
        Arrays.sort(cmp, ary);
        assertArrayEquals("heapSort(0)", new String[] { "0" }, ary);
        ary = new String[]{"3","2","1","1","4","4","6","5","7","2"};
        Arrays.sort(cmp, ary);
        assertArrayEquals("heapSort(3,2,1,1,4,4,6,5,7,2)", new Object[] { "1", "1", "2", "2", "3", "4", "4", "5", "6", "7" }, ary);
        int N = 500; // Max number of objects that will fit in normal heap size: 16*500^2 = 4m
        String[][] a = new String[N][];
        String[][] a1 = new String[N][];
        String[][] a2 = new String[N][];
        Random rand = new Random();
        int n = 0;
        int c = 0;
        for(int i=0; i<N; i++) {
            int l = rand.nextInt(N);
            String[] s = new String[l];
            for(int j=0; j<l; j++) s[j]=""+(char)('a'+rand.nextInt(26));
            a[i] = s;
            a1[i] = s.clone();
            a2[i] = s.clone();
            n += l;
        }
        System.out.println("Sorting "+N+" arrays of "+n+" elements");
        long t0 = System.currentTimeMillis();
        for(int i=0; i<N; i++) {
            c+=Arrays.sort(cmp, a1[i]);
        }
        long t1 = System.currentTimeMillis();
        for(int i=0; i<N; i++) {
            java.util.Arrays.sort(a2[i],cmp);
        }
        long t2 = System.currentTimeMillis();
        for(int i=0; i<N; i++) {
            assertArrayEquals("sort("+Arrays.join(",",a[i])+")",a2[i],a1[i]);
        }
        System.out.println("Sorted "+N+" arrays of "+n+" elements in "+(t1-t0)+"ms, "+c+" operations ("+(t2-t1)+"ms for java.util.Arrays.sort)");
    }


    public void testSearch() throws Exception {
        String[] a1 = new String[] {};
        assertEquals("",-1,Arrays.search(a1,0,0,"1"));
        String[] a2 = new String[] { "2"};
        assertEquals("",-2,Arrays.search(a2,0,1,"3"));
        String[] a3 = new String[] { "2"};
        assertEquals("",-1,Arrays.search(a3,0,1,"1"));
        assertEquals("",0,Arrays.search(a3,0,1,"2"));
        String[] a4 = new String[]{"2", "3", "4", "7", "8"};
        assertEquals("",-4,Arrays.search(a4,0,5,"5"));
        assertEquals("",2,Arrays.search(a4,0,5,"4"));
    }

    public void testSearchCmp() throws Exception {
        Comparator<String> cmp = new Comparator<String>() {
            @Override
            public int compare(String o1, String o2) {
                return o1.compareTo(o2);
//                if (o1.equals(o2)) return 0;
//                if (o1.length() == 0 && o2.length() == 0) return 0;
//                if (o1.length() == 0) return -1;
//                if (o2.length() == 0) return +1;
//                return o1.charAt(0) - o2.charAt(0);
            }
        };
        String[] a1 = new String[] {};
        assertEquals("",-1,Arrays.search(a1,0,0,"1",cmp));
        String[] a2 = new String[] { "2"};
        assertEquals("",-2,Arrays.search(a2,0,1,"3",cmp));
        String[] a3 = new String[] { "2"};
        assertEquals("",-1,Arrays.search(a3,0,1,"1",cmp));
        assertEquals("",0,Arrays.search(a3,0,1,"2",cmp));
        String[] a4 = new String[]{"2", "3", "4", "7", "8"};
        assertEquals("",-4,Arrays.search(a4,0,5,"5",cmp));
        assertEquals("",2,Arrays.search(a4,0,5,"4",cmp));
    }

    public void testInsert() throws Exception {
        String[] a1 = new String[] {null};
        assertEquals("",0,Arrays.insert(a1,0,0,"1"));
        assertArrayEquals("",new String[]{"1"},a1);
        String[] a2 = new String[] { "2", null};
        assertEquals("",1,Arrays.insert(a2,0,1,"3"));
        assertArrayEquals("",new String[]{"2","3"},a2);
        String[] a3 = new String[] { "2", null};
        assertEquals("",0,Arrays.insert(a3,0,1,"1"));
        assertArrayEquals("",new String[]{"1","2"},a3);
        String[] a4 = new String[]{"2", "3", "4", "7", "8", null};
        assertEquals("",3,Arrays.insert(a4,0,5,"5"));
        assertArrayEquals("",new String[]{"2", "3", "4", "5", "7", "8"},a4);
    }

    public void testInsertCmp() throws Exception {
        Comparator<String> cmp = new Comparator<String>() {
            @Override
            public int compare(String o1, String o2) {
                return o1.compareTo(o2);
//                if (o1.equals(o2)) return 0;
//                if (o1.length() == 0 && o2.length() == 0) return 0;
//                if (o1.length() == 0) return -1;
//                if (o2.length() == 0) return +1;
//                return o1.charAt(0) - o2.charAt(0);
            }
        };
        String[] a1 = new String[] {null};
        assertEquals("",0,Arrays.insert(a1,0,0,"1",cmp));
        assertArrayEquals("",new String[]{"1"},a1);
        String[] a2 = new String[] { "2", null};
        assertEquals("",1,Arrays.insert(a2,0,1,"3",cmp));
        assertArrayEquals("",new String[]{"2","3"},a2);
        String[] a3 = new String[] { "2", null};
        assertEquals("",0,Arrays.insert(a3,0,1,"1",cmp));
        assertArrayEquals("",new String[]{"1","2"},a3);
        String[] a4 = new String[]{"2", "3", "4", "7", "8", null};
        assertEquals("",3,Arrays.insert(a4,0,5,"5",cmp));
        assertArrayEquals("",new String[]{"2", "3", "4", "5", "7", "8"},a4);
    }

    public void testUnion() throws Exception {
        assertArrayEquals("",
                new String[]{},
                Arrays.union(new String[]{}, 0, 0, new String[]{}, 0, 0));
        assertArrayEquals("",
                new String[]{"2", "3", "4", "5", "6"},
                Arrays.union(new String[]{}, 0, 0, new String[]{"2", "3", "4", "5", "6"}, 0, 5));
        assertArrayEquals("",
                new String[]{"1", "2", "3", "4", "5", "6", "7"},
                Arrays.union(new String[]{"1", "3", "7"}, 0, 3, new String[]{"2", "3", "4", "5", "6"}, 0, 5));
    }

    public void testUnionCmp() throws Exception {
        Comparator<String> cmp = new Comparator<String>() {
            @Override
            public int compare(String o1, String o2) {
                if (o1.equals(o2)) return 0;
                if (o1.length() == 0 && o2.length() == 0) return 0;
                if (o1.length() == 0) return -1;
                if (o2.length() == 0) return +1;
                return o1.charAt(0) - o2.charAt(0);
            }
        };
        assertArrayEquals("",
                new String[]{},
                Arrays.union(new String[]{}, 0, 0, new String[]{}, 0, 0, cmp));
        assertArrayEquals("",
                new String[]{"2", "3", "4", "5", "6"},
                Arrays.union(new String[]{}, 0, 0, new String[]{"2", "3", "4", "5", "6"}, 0, 5, cmp));
        assertArrayEquals("",
                new String[]{"1", "2", "3", "4", "5", "6", "7"},
                Arrays.union(new String[]{"1", "3", "7"}, 0, 3, new String[]{"2", "3", "4", "5", "6"}, 0, 5, cmp));
    }

    public void testInter() throws Exception {
        assertArrayEquals("",
                          new String[]{},
                          Arrays.inter(new String[]{}, 0, 0, new String[]{}, 0, 0));
        assertArrayEquals("",
                          new String[]{},
                          Arrays.inter(new String[]{}, 0, 0, new String[]{"2", "3", "4", "5", "6"}, 0, 5));
        assertArrayEquals("",
                          new String[]{"3"},
                          Arrays.inter(new String[]{"1", "3", "7"}, 0, 3, new String[]{"2", "3", "4", "5", "6"}, 0, 5));
        assertArrayEquals("",
                          new String[]{"3", "6"},
                          Arrays.inter(new String[]{"1", "3", "6"}, 0, 3, new String[]{"2", "3", "4", "5", "6"}, 0, 5));
    }

    public void testInterCmp() throws Exception {
        Comparator<String> cmp = new Comparator<String>() {
            @Override
            public int compare(String o1, String o2) {
                if (o1.equals(o2)) return 0;
                if (o1.length() == 0 && o2.length() == 0) return 0;
                if (o1.length() == 0) return -1;
                if (o2.length() == 0) return +1;
                return o1.charAt(0) - o2.charAt(0);
            }
        };
        assertArrayEquals("",
                          new String[]{},
                          Arrays.inter(new String[]{}, 0, 0, new String[]{}, 0, 0, cmp));
        assertArrayEquals("",
                          new String[]{},
                          Arrays.inter(new String[]{}, 0, 0, new String[]{"2", "3", "4", "5", "6"}, 0, 5, cmp));
        assertArrayEquals("",
                          new String[]{"3"},
                          Arrays.inter(new String[]{"1", "3", "7"}, 0, 3, new String[]{"2", "3", "4", "5", "6"}, 0, 5, cmp));
        assertArrayEquals("",
                          new String[]{"3", "6"},
                          Arrays.inter(new String[]{"1", "3", "6"}, 0, 3, new String[]{"2", "3", "4", "5", "6"}, 0, 5, cmp));
    }


    public void testAppend() throws Exception {
        assertArrayEquals("",
                          new String[]{"1", "2", "3", "4", "5", "6"},
                          Arrays.append(new String[]{"1", "2", "3"}, "4", "5", "6"));
        assertArrayEquals("",
                          new String[]{"1", "2", "3", "4", "5", "6"},
                          Arrays.append(new String[]{}, "1", "2", "3", "4", "5", "6"));
        assertArrayEquals("",
                          new String[]{},
                          Arrays.append(new String[]{}));
        assertArrayEquals("",
                          new String[]{"1", "2", "3", "4", "5"},
                          Arrays.append(new String[]{"1", "2", "3"}, "4", "5"));
        assertArrayEquals("",
                          new String[]{"1", "2", "3"},
                          Arrays.append(new String[]{"1", "2", "3"}));
    }

    public void testPrepend() throws Exception {
        assertArrayEquals("",
                          new String[]{"4", "5", "6", "1", "2", "3"},
                          Arrays.prepend(new String[]{"1", "2", "3"}, "4", "5", "6"));
        assertArrayEquals("",
                          new String[]{"1", "2", "3", "4", "5", "6"},
                          Arrays.prepend(new String[]{}, "1", "2", "3", "4", "5", "6"));
        assertArrayEquals("",
                          new String[]{},
                          Arrays.prepend(new String[]{}));
        assertArrayEquals("",
                          new String[]{"4", "5", "1", "2", "3"},
                          Arrays.prepend(new String[]{"1", "2", "3"}, "4", "5"));
        assertArrayEquals("",
                          new String[]{"1", "2", "3"},
                          Arrays.prepend(new String[]{"1", "2", "3"}));
    }

    public void testConcat() throws Exception {
        assertArrayEquals("",
                          new String[]{"1", "2", "3", "4", "5", "6"},
                          Arrays.concat(new String[]{"1", "2", "3"}, new String[]{"4", "5", "6"}));
        assertArrayEquals("",
                          new String[]{"1", "2", "3", "4", "5", "6"},
                          Arrays.concat(new String[]{}, new String[]{"1", "2", "3", "4", "5", "6"}));
        assertArrayEquals("",
                          new String[]{},
                          Arrays.concat(new String[]{}));
        assertArrayEquals("",
                          new String[]{"1", "2", "3", "4", "5", "6"},
                          Arrays.concat(new String[]{"1", "2", "3"}, new String[]{"4", "5"}, new String[]{"6"}));
        assertArrayEquals("",
                          new String[]{"1", "2", "3", "4", "5", "6"},
                          Arrays.concat(new String[]{"1", "2", "3"}, new String[]{}, new String[]{"4", "5"}, new String[]{"6"}));
        assertArrayEquals("",
                          new String[]{"1", "2", "3", "4", "5", "6"},
                          Arrays.concat(new String[]{}, new String[]{"1", "2", "3"}, new String[]{"4", "5"}, new String[]{"6"}));

    }

    public void testSubarray() throws Exception {
        assertArrayEquals("subarray([],0,0)", new Object[] { }, Arrays.subarray(new String[]{}, 0, 0));
        assertArrayEquals("subarray([1,2,3,4,5,6],0,0)", new Object[] { }, Arrays.subarray(new Object[] { 1, 2, 3, 4, 5, 6 }, 0, 0));
        assertArrayEquals("subarray([1,2,3,4,5,6],6,6)", new Object[] { }, Arrays.subarray(new Object[] { 1, 2, 3, 4, 5, 6 }, 6, 6));
        assertArrayEquals("subarray([1,2,3,4,5,6],1,4)", new Object[] { 2, 3, 4 }, Arrays.subarray(new Object[] { 1, 2, 3, 4, 5, 6 }, 1, 4));
        assertArrayEquals("subarray([1,2,3,4,5,6],2,3)", new Object[] { 3 }, Arrays.subarray(new Object[] { 1, 2, 3, 4, 5, 6 }, 2, 3));
        assertArrayEquals("subarray([1,2,3,4,5,6],0,6)", new Object[] { 1, 2, 3, 4, 5, 6 }, Arrays.subarray(new Object[] { 1, 2, 3, 4, 5, 6 }, 0, 6));
        assertArrayEquals("subarray([1,2,3,4,5,6],1,1)", new Object[] { }, Arrays.subarray(new Object[] { 1, 2, 3, 4, 5, 6 }, 1, 1));
        try {
            Arrays.subarray(new Object[] { }, 0, 1);
            fail("subarray([],0,1)");
        } catch (ArrayIndexOutOfBoundsException e) {
        }
        try {
            Arrays.subarray(new Object[] { }, 1, 0);
            fail("subarray([],1,0)");
        } catch (ArrayIndexOutOfBoundsException e) {
        }
        try {
            Arrays.subarray(new Object[] { 1, 2, 3, 4, 5, 6 }, 3, 7);
            fail("subarray([1,2,3,4,5,6],3,7)");
        } catch (ArrayIndexOutOfBoundsException e) {
        }
        assertArrayEquals("subarray(['1','2','3','4','5','6'],0,6)", new String[] { "1", "2", "3", "4", "5", "6" },
                          Arrays.subarray(new String[] { "1", "2", "3", "4", "5", "6" }, 0, 6));
    }

    public void testAsList() throws Exception {
        List<String> list = Arrays.asList("val1", "val2", "val3", "val4");
        assertEquals("asList(...).size()", 4, list.size());
        Iterator<? extends String> it=list.iterator();
        assertEquals("asList(...).iterator().next()", "val1", it.next());
        assertEquals("asList(...).iterator().next()2", "val2", it.next());
        assertEquals("asList(...).iterator().next()3", "val3", it.next());
        assertEquals("asList(...).iterator().next()4", "val4", it.next());
        assertFalse("asList(...).iterator().next()4.hasNext()", it.hasNext());
        List<String> slist = list.sublist(1,2);
        assertEquals("asList(...).sublist(1,2).size()", 1, slist.size());
        Iterator<? extends String> sit=slist.iterator();
        assertEquals("asList(...).sublist(1,2).iterator().next()", "val2", sit.next());
        assertFalse("asList(...).sublist(1,2).iterator().next().hasNext()", sit.hasNext());
        slist = list.sublist(1,4);
        assertEquals("asList(...).sublist(1,4).size()", 3, slist.size());
        sit=slist.iterator();
        assertEquals("asList(...).sublist(1,4).iterator().next()", "val2", sit.next());
        assertEquals("asList(...).sublist(1,4).iterator().next()2", "val3", sit.next());
        assertEquals("asList(...).sublist(1,4).iterator().next()3", "val4", sit.next());
        assertFalse("asList(...).sublist(1,4).iterator().next()3.hasNext()", sit.hasNext());
    }

    public void testAsMap() throws Exception {
        Map<String, String> map0 = Arrays.asMap(String.class, String.class);
        assertEquals("asMap().isEmpty()", true, map0.isEmpty());

        Map<String, String> map1 = Arrays.asMap(String.class, String.class, "key1", "val1", "key2", "val2");
        assertEquals("asMap(...).has(key1)", true, map1.has("key1"));
        assertEquals("asMap(...).get(key2) = val2", "val2", map1.get("key2"));

        try {
            Map<String, String> map2 = Arrays.asMap(String.class, String.class, "key1", "val1", "key2", "val2", "key3");
            fail("asMap(odd args) = IAE!");
        } catch (IllegalArgumentException e) {
        }

        try {
            Map<Integer, String> map3 = Arrays.asMap(Integer.class, String.class, "key1", "val1", "key2", "val2");
            fail("asMap(other key type) = IAE!");
        } catch (IllegalArgumentException e) {
        }

        try {
            Map<String, Long> map4 = Arrays.asMap(String.class, Long.class, "key1", "val1", "key2", "val2");
            fail("asMap(other val type) = IAE!");
        } catch (IllegalArgumentException e) {
        }
    }


    public void testStrg() throws Exception {
        assertEquals("[0|]",Arrays.toString(new Object[]{}));
        assertEquals("[1|0]",Arrays.toString(0));
        assertEquals("[1|1]",Arrays.toString(1));
        assertEquals("[1|]",Arrays.toString((Object)null));
        assertEquals("[1|247]",Arrays.toString(0xF7));
        assertEquals("[1|foo]",Arrays.toString("foo"));
        assertEquals("[5|0,true,2.2,,foo]",Arrays.toString(0,true,2.2,null,"foo"));
        assertEquals("[5|0,true,2.2,,foo]",Arrays.toString(new StringBuilder(),0,true,2.2,null,"foo").toString());
        assertEquals("[5|0,true,2.2,,foo]",Arrays.toString(new StringBuffer(),0,true,2.2,null,"foo").toString());

    }

    public void testJoin() throws Exception {
        assertEquals("",Arrays.join(":"));
        assertEquals("0",Arrays.join(":",0));
        assertEquals("1",Arrays.join(":",1));
        assertEquals("",Arrays.join(":",(Object)null));
        assertEquals("247",Arrays.join(":",0xF7));
        assertEquals("foo",Arrays.join(":","foo"));
        assertEquals("0:true:2.2::foo",Arrays.join(":",0,true,2.2,null,"foo"));
        assertEquals("<0>:<true>:<2.2>::<foo>",Arrays.join(new StringBuilder(),":","<",">",0,true,2.2,null,"foo").toString());
        assertEquals("<0>:<true>:<2.2>::<foo>",Arrays.join(new StringBuffer(),":","<",">",0,true,2.2,null,"foo").toString());
    }


}
