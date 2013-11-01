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
        if(!Arrays.equals(expected,actual)) fail(message+";\n expected: ["+StringArrays.join(",",expected)+"];\n   actual: ["+StringArrays.join(",",actual)+"]");
    }

    public void testEquals() throws Exception {
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

    public void testInsert() throws Exception {
        String[] a1 = new String[] {null};
        assertEquals("",0,Arrays.insert(a1,0,1,"1"));
        assertArrayEquals("",new String[]{"1"},a1);
        String[] a2 = new String[] { "2", null};
        assertEquals("",1,Arrays.insert(a2,0,2,"3"));
        assertArrayEquals("",new String[]{"2","3"},a2);
        String[] a3 = new String[] { "2", null};
        assertEquals("",0,Arrays.insert(a3,0,2,"1"));
        assertArrayEquals("",new String[]{"1","2"},a3);
        String[] a4 = new String[]{"2", "3", "4", "7", "8", null};
        assertEquals("",3,Arrays.insert(a4,0,6,"5"));
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
        List<String> list = Arrays.asList("val1", "val2", "val3");
        assertEquals("asList(...).size()", 3, list.size());
        Iterator<? extends String> it=list.iterator();
        assertEquals("asList(...).iterator().next()", "val1", it.next());
        assertEquals("asList(...).iterator().next().next()", "val2", it.next());
        assertEquals("asList(...).iterator().next().next().next()", "val3", it.next());
        assertFalse("asList(...).iterator().next().next().next().hasNext()", it.hasNext());
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
}
