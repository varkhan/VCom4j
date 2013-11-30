package net.varkhan.base.containers;

import junit.framework.TestCase;
import net.varkhan.base.containers.array.Arrays;
import net.varkhan.base.containers.map.Map;


/**
 * <b></b>.
 * <p/>
 *
 * @author varkhan
 * @date 11/1/13
 * @time 6:27 PM
 */
public class ContainersTest extends TestCase {

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

    public void testJoin() throws Exception {
        assertEquals("",Containers.join(":",Arrays.asList()));
        assertEquals("0",Containers.join(":",Arrays.asList(0)));
        assertEquals("1",Containers.join(":",Arrays.asList(1)));
        assertEquals("",Containers.join(":",Arrays.asList((Object)null)));
        assertEquals("247",Containers.join(":",Arrays.asList(0xF7)));
        assertEquals("foo",Containers.join(":",Arrays.asList("foo")));
        assertEquals("0:true:2.2::foo",Containers.join(":",Arrays.asList(0,true,2.2,null,"foo")));
        assertEquals("<0>:<true>:<2.2>::<foo>",Containers.join(new StringBuilder(),":","<",">",Arrays.asList(0,true,2.2,null,"foo")).toString());
        assertEquals("<0>:<true>:<2.2>::<foo>",Containers.join(new StringBuffer(),":","<",">",Arrays.asList(0,true,2.2,null,"foo")).toString());
    }


    protected static class ArrayContainer<T> implements Container<T> {
        final T[] a;
        ArrayContainer(T[] a) { this.a=a; }
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
    protected static class ArrayMap<K,V> implements Map<K,V> {
        final K[] keys;
        final V[] vals;
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
