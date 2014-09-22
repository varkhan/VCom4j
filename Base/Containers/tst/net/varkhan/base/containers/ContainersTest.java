package net.varkhan.base.containers;

import junit.framework.TestCase;
import net.varkhan.base.containers.array.Arrays;
import net.varkhan.base.containers.list.List;
import net.varkhan.base.containers.map.Map;

import java.util.NoSuchElementException;
import java.util.Random;


/**
 * <b></b>.
 * <p/>
 *
 * @author varkhan
 * @date 11/1/13
 * @time 6:27 PM
 */
public class ContainersTest extends TestCase {

    public static <T> void assertListEquals(String message, List<T> expected, List<T> actual) {
        if(!Containers.equals(expected,actual)) fail(message+";\n expected: ["+Containers.join(",",expected)+"];\n   actual: ["+Containers.join(",",actual)+"]");
    }

    public void testEqualsContainer() throws Exception {
        Container<String> c00 = new Container<String>() {
            public long size() { return 0; }
            public boolean isEmpty() { return true; }
            public Iterator<? extends String> iterator() { return new Iterator.Empty<String>(); }
            public <Par> long visit(Visitor<String,Par> vis, Par par) { return 0; }
        };
        Container<String> c01 = new Container<String>() {
            public long size() { return 0; }
            public boolean isEmpty() { return true; }
            public Iterator<? extends String> iterator() { return new Iterator.Empty<String>(); }
            public <Par> long visit(Visitor<String,Par> vis, Par par) { return 0; }
        };
        Container<String> c02 =new ArrayContainer<String>(new String[]{});
        Container<String> c10 =new ArrayContainer<String>(new String[]{ "a" });
        Container<String> c11 =new ArrayContainer<String>(new String[]{ "a" });
        Container<String> c12 =new ArrayContainer<String>(new String[]{ "b" });
        Container<String> c21 =new ArrayContainer<String>(new String[]{ "a", "c" });
        Container<String> c22 =new ArrayContainer<String>(new String[]{ "a", "c" });
        Container<String> c23 =new ArrayContainer<String>(new String[]{ "a", "d" });
        assertTrue("equals([],-)",Containers.equals(c00, c00));
        assertTrue("equals([],[])",Containers.equals(c00, c01));
        assertTrue("equals([],[[]])",Containers.equals(c00, c02));
        assertFalse("!equals([],[a])", Containers.equals(c00, c10));
        assertTrue("equals([a],-)", Containers.equals(c10, c10));
        assertTrue("equals([a],[a])", Containers.equals(c10, c11));
        assertFalse("!equals([a],[b])", Containers.equals(c10, c12));
        assertFalse("!equals([a],[a,c])", Containers.equals(c10, c21));
        assertTrue("equals([a,c],[a,c])", Containers.equals(c22, c21));
        assertFalse("!equals([a,d],[a,c])", Containers.equals(c23, c21));
    }

    public void testEqualsMap() throws Exception {
        Map<String,Integer> m00 =new ArrayMap<String,Integer>(new String[]{},new Integer[]{});
        Map<String,Integer> m01 =new ArrayMap<String,Integer>(new String[]{},new Integer[]{});
        Map<String,Integer> m10 =new ArrayMap<String,Integer>(new String[]{"a"},new Integer[]{1});
        Map<String,Integer> m11 =new ArrayMap<String,Integer>(new String[]{"a"},new Integer[]{1});
        Map<String,Integer> m12 =new ArrayMap<String,Integer>(new String[]{"b"},new Integer[]{2});
        Map<String,Integer> m13 =new ArrayMap<String,Integer>(new String[]{"a"},new Integer[]{2});
        Map<String,Integer> m20 =new ArrayMap<String,Integer>(new String[]{"a","b"},new Integer[]{1,2});
        Map<String,Integer> m21 =new ArrayMap<String,Integer>(new String[]{"a","b"},new Integer[]{1,2});
        Map<String,Integer> m22 =new ArrayMap<String,Integer>(new String[]{"a","b"},new Integer[]{1,3});
        Map<String,Integer> m23 =new ArrayMap<String,Integer>(new String[]{"a","c"},new Integer[]{1,3});
        assertTrue("equals({},-)",Containers.equals(m00,m00));
        assertTrue("equals({},{})",Containers.equals(m00,m01));
        assertFalse("!equals({a=>1},{})",Containers.equals(m10,m01));
        assertTrue("equals({a=>1},{a=>1})",Containers.equals(m10,m11));
        assertFalse("!equals({b=>2},{a=>1})",Containers.equals(m12,m11));
        assertFalse("!equals({b=>2},{a=>2})",Containers.equals(m12,m13));
        assertFalse("!equals({a=>1},{a=>2})",Containers.equals(m11,m13));
        assertTrue("equals({a=>1,b=>2},{a=>1,b=>2})",Containers.equals(m20,m21));
        assertFalse("!equals({a=>1,b=>3},{a=>1,b=>2})",Containers.equals(m22,m21));
        assertFalse("!equals({a=>1,b=>3},{a=>1,c=>3})",Containers.equals(m22,m23));
    }

    public void testJoinA() throws Exception {
        assertEquals("",Containers.join(":",Arrays.asList()));
        assertEquals("0",Containers.join(":",Arrays.asList(0)));
        assertEquals("1",Containers.join(":",Arrays.asList(1)));
        assertEquals("null",Containers.join(":",Arrays.asList((Object)null)));
        assertEquals("247",Containers.join(":",Arrays.asList(0xF7)));
        assertEquals("foo",Containers.join(":",Arrays.asList("foo")));
        assertEquals("0:true:2.2:null:foo",Containers.join(":",Arrays.asList(0,true,2.2,null,"foo")));
        assertEquals("<0>:<true>:<2.2>:null:<foo>",Containers.join(new StringBuilder(), "<", ">", "null", ":", Arrays.asList(0,true,2.2,null,"foo")).toString());
        assertEquals("<0>:<true>:<2.2>:null:<foo>",Containers.join(new StringBuffer(), "<", ">", "null", ":", Arrays.asList(0,true,2.2,null,"foo")).toString());
    }

    public void testJoinM() throws Exception {
        assertEquals("",Containers.join((String)null,"=",null,"null",":",Arrays.asMap(String.class, Integer.class)));
        assertEquals("a=0",Containers.join((String)null,"=",null,"null",":",Arrays.asMap(String.class, Integer.class, "a", 0)));
        assertEquals("b=1",Containers.join((String)null,"=",null,"null",":",Arrays.asMap(String.class,Integer.class,"b",1)));
        assertEquals("c=null",Containers.join((String)null,"=",null,"null",":",Arrays.asMap(String.class,Integer.class,"c",null)));
        assertEquals("s=foo:b=true:d=2.2:i=0:n=null",Containers.join((String)null,"=",null,"null",":",Arrays.asMap(String.class,Object.class,"i",0,"b",true,"d",2.2,"n",null,"s","foo")));
        assertEquals("<s=foo>:<b=true>:<d=2.2>:<i=0>:<n=null>",Containers.join(new StringBuilder(), "<", "=", ">", "null", ":", Arrays.asMap(String.class,Object.class,"i",0,"b",true,"d",2.2,"n",null,"s","foo")).toString());
        assertEquals("<s=foo>:<b=true>:<d=2.2>:<i=0>:<n=null>",Containers.join(new StringBuffer(), "<", "=", ">", "null", ":", Arrays.asMap(String.class, Object.class, "i", 0, "b", true, "d", 2.2, "n", null, "s", "foo")).toString());
    }

    public void testSort() throws Exception {
        List<Integer> ary = new ArrayList<Integer>(0);
        Containers.sort(ary);
        assertListEquals("heapSort(0)", new ArrayList<Integer>( 0 ), ary);
        ary = new ArrayList<Integer>(3,2,1,1,4,4,6,5,7,2);
        Containers.sort(ary);
        assertListEquals("heapSort(3,2,1,1,4,4,6,5,7,2)", new ArrayList<Integer>( 1, 1, 2, 2, 3, 4, 4, 5, 6, 7 ), ary);
        int N = 500; // Max number of objects that will fit in normal heap size: 16*500^2 = 4m
        Integer[][] a = new Integer[N][];
        List<Integer>[] a1 = new List[N];
        List<Integer>[] a2 = new List[N];
        Random rand = new Random();
        int n = 0;
        int c = 0;
        for(int i=0; i<N; i++) {
            int l = rand.nextInt(N);
            Integer[] s = new Integer[l];
            for(int j=0; j<l; j++) s[j]=rand.nextInt();
            a[i] = s;
            a1[i] = new ArrayList<Integer>(s.clone());
            a2[i] = new ArrayList<Integer>(s.clone());
            n += l;
        }
        System.out.println("Sorting "+N+" arrays of "+n+" elements");
        long t0 = System.currentTimeMillis();
        for(int i=0; i<N; i++) {
            c+=Containers.sort(a1[i]);
        }
        long t1 = System.currentTimeMillis();
        for(int i=0; i<N; i++) {
            java.util.Arrays.sort(a[i]);
        }
        long t2 = System.currentTimeMillis();
        for(int i=0; i<N; i++) {
            assertListEquals("sort("+Arrays.join(",",a[i])+")",new ArrayList<Integer>(a[i]),a1[i]);
        }
        System.out.println("Sorted "+N+" arrays of "+n+" elements in "+(t1-t0)+"ms, "+c+" operations ("+(t2-t1)+"ms for java.util.Arrays.sort)");
    }

    protected static class ArrayContainer<T> implements Container<T> {
        private final T[] a;
        ArrayContainer(T... a) { this.a=a; }
        public long size() { return a.length; }
        public boolean isEmpty() { return a.length==0; }
        public Iterator<? extends T> iterator() {
            return new Iterator<T>() {
                int i=0;
                public boolean hasNext() { return i<a.length; }
                public T next() { return a[i++]; }
                public void remove() { }
            };
        }
        public <Par> long visit(Visitor<T,Par> vis, Par par) {
            long c=0;
            for(T s: a) {
                long p = vis.invoke(s, par);
                if(p<0) return c;
                c += p;
            }
            return c;
        }
    }

    protected static class ArrayList<T> implements List<T> {
        private final T[] array;
        public ArrayList(T... array) { this.array=array; }
        public long size() { return array==null?0:array.length; }
        public boolean isEmpty() { return array==null||array.length==0; }
        public void clear() { }
        public boolean add(T elt) { return false; }
        public boolean add(long idx, T elt) { return false; }
        public T get(long idx) { return array[(int) idx]; }
        public boolean set(long idx, T elt) { array[(int) idx] = elt; return true; }
        public boolean del(long idx) { return false; }
        public boolean del(T elt) { return false; }
        public Iterator<? extends T> iterator() {
            return new Iterator<T>() {
                private volatile int pos = 0;
                public boolean hasNext() { return array!=null&&pos<array.length; }
                public T next() {
                    if(array==null||pos>=array.length) throw new NoSuchElementException();
                    return array[pos++];
                }
                public void remove() { throw new UnsupportedOperationException(); }
            };
        }
        public <Par> long visit(Visitor<T,Par> vis, Par par) {
            long ret = 0;
            if(array!=null) for(T obj: array) {
                long r = vis.invoke(obj, par);
                if(r<0) return ret;
                ret += r;
            }
            return ret;
        }
        public List<T> sublist(long beg, long len) {
            return null;
        }
    }

    protected static class ArrayMap<K,V> implements Map<K,V> {
        private final K[] keys;
        private final V[] vals;
        ArrayMap(K[] keys, V[] vals) {
            this.keys=keys;
            this.vals=vals;
        }
        public long size() { return keys.length; }
        public boolean isEmpty() { return keys.length!=0; }
        public void clear() { }
        public boolean has(K k) { return Arrays.indexOf((Object)k,keys)>=0; }
        public V get(K k) {
            int p = Arrays.indexOf((Object)k,keys);
            if(p>=0) return vals[p];
            return null;
        }
        public boolean add(Entry<K,V> item) { return false; }
        public boolean add(K s, V val) { return false; }
        public boolean del(K s) { return false; }
        public Iterator<? extends Entry<K,V>> iterator() {
            return new Iterator<Entry<K,V>>() {
                int i=0;
                public boolean hasNext() { return i<keys.length; }
                public Entry<K,V> next() {
                    final K k = keys[i];
                    final V v = vals[i];
                    i++;
                    return new E<K,V>(k, v);
                }
                public void remove() { }
            };
        }
        public Container<K> keys() { return new ArrayContainer<K>(keys); }
        public Container<V> values() { return new ArrayContainer<V>(vals); }
        public <Par> long visit(MapVisitor<K,V,Par> vis, Par par) {
            long c=0;
            for(int i=0; i<keys.length; i++) {
                long p=vis.invoke(keys[i],vals[i],par);
                if(p<0) return c;
                c += p;
            }
            return c;
        }
        public <Par> long visit(Visitor<Entry<K,V>,Par> vis, Par par) {
            long c=0;
            for(int i=0; i<keys.length; i++) {
                long p=vis.invoke(new E<K,V>(keys[i],vals[i]), par);
                if(p<0) return c;
                c += p;
            }
            return c;
        }
        private static class E<K,V> implements Map.Entry<K,V> {
            private final K k;
            private final V v;
            public E(K k, V v) {
                this.k=k;
                this.v=v;
            }
            public K getKey() { return k; }
            public V getValue() { return v; }
            public V setValue(V val) { return null; }
        }
    }
}
