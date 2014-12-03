package net.varkhan.base.containers.map;

import junit.framework.TestCase;
import net.varkhan.base.containers.Iterator;
import net.varkhan.base.containers.Visitable;

import java.io.*;
import java.util.HashSet;
import java.util.Random;


/**
 * <b></b>.
 * <p/>
 *
 * @author varkhan
 * @date 11/29/13
 * @time 7:02 PM
 */
public abstract class AbstractLong2ObjMapTest extends TestCase {

    @SuppressWarnings("unchecked")
    public static <C extends Cloneable> C clone(C obj) {
        try {
            return (C) obj.getClass().getMethod("clone").invoke(obj);
        }
        catch(Exception e) {
            return null;
        }
    }

    protected long[] genKeyLongs(Random rand, int num) {
        java.util.Set<Long> keys=new HashSet<Long>(num);
        while(keys.size()<num) {
            long l=rand.nextLong();
            if(l!=0x8000000000000000L && l!=0x7FFFFFFFFFFFFFFFL) keys.add(l);
        }
        long[] a = new long[keys.size()];
        int i=0;
        for(Long v: keys) a[i++] = v;
        return a;
    }

    protected String[] genValueStrings(Random rand, int num, double sparse, int minl, int maxl, char[] characters) {
        String[] a=new String[num];
        for(int i=0;i<num;i++) {
            if(rand.nextFloat()<sparse) {
                a[i]=null;
                continue;
            }
            StringBuilder buf=new StringBuilder();
            int len=minl+rand.nextInt(maxl-minl);
            for(int j=0;j<len;j++) buf.append(characters[rand.nextInt(characters.length)]);
            a[i]=buf.toString();
        }
        return a;
    }

    public <T> void featureTestAdd(Random rand, long[] keys, T[] vals, Long2ObjMap<T> map, int verb) throws Exception {
        // Testing key adding
        for(int i=0;i<keys.length;i++) {
            long k=keys[i];
            T v=vals[i];
            map.add(k,v);
            if(verb>0) System.err.println("Added "+i+" "+k);
            if(verb>1) System.err.println(map);
            assertTrue("has("+k+")", map.has(k));
        }
        System.out.println("add(T) OK");
    }

    public <T> void featureTestHas(Random rand, long[] keys, T[] vals, Long2ObjMap<T> map, int verb) throws Exception {
        map.clear();
        for(int i=0;i<keys.length;i++) map.add(keys[i],vals[i]);
        if(verb>0) System.err.println("Ready");
        // Testing if keys are seen
        for(int i=0;i<keys.length;i++) {
            long k=keys[i];
            assertTrue("has("+k+")", map.has(k));
            if(verb>0) System.err.println("Checked "+i+" "+k);
            if(verb>1) System.err.println(map);
        }
        System.out.println("has(T) OK");
    }

    protected <T> void featureTestGet(Random rand, long[] keys, T[] vals, Long2ObjMap<T> map, int verb) throws Exception {
        map.clear();
        for(int i=0;i<keys.length;i++) map.add(keys[i],vals[i]);
        // Testing if keys/values can be retrieved
        for(int i=0;i<keys.length;i++) {
            long k=keys[i];
            T v=vals[i];
            assertTrue("get("+k+") = "+v, v==map.get(k));
            if(verb>1) System.err.println("Checked "+i+": "+k);
            if(verb>2) System.err.println(map);
        }
        if(verb>0) System.out.println("get{Key,Value}(long) OK");
    }

    public <T> void featureTestDel(Random rand, long[] keys, T[] vals, Long2ObjMap<T> map, int verb) throws Exception {
        map.clear();
        for(int i=0;i<keys.length;i++) map.add(keys[i],vals[i]);
        if(verb>0) System.err.println("Ready");
        // Testing forward delete (no resize)
        for(int i=0;i<keys.length;i++) {
            long k=keys[i];
            T v=vals[i];
            // This may have been deleted before
//            assertTrue("has("+v+")",map.has(v));
            map.del(k);
            if(verb>0) System.err.println("Deleted "+i+" "+k);
            if(verb>1) System.err.println(map);
            assertFalse("has("+k+")", map.has(k));
        }
        System.out.println("del(T) forward OK");
        map.clear();
        for(int i=0;i<keys.length;i++) map.add(keys[i],vals[i]);
        if(verb>0) System.err.println("Ready");
        // Testing backward delete (implies auto resize)
        for(int i=vals.length-1;i>=0;i--) {
            long k=keys[i];
            T v=vals[i];
            // This may have been deleted before
//            assertTrue("has("+v+")",map.has(v));
            map.del(k);
            if(verb>0) System.err.println("Deleted "+i+" "+k);
            if(verb>1) System.err.println(map);
            assertFalse("has("+k+")", map.has(k));
        }
        System.out.println("del() backward OK");
        map.clear();
        for(int i=0;i<keys.length;i++) map.add(keys[i],vals[i]);
        if(verb>0) System.err.println("Ready");
        // Testing random delete (implies auto resize)
        java.util.Set<Integer> del=new HashSet<Integer>();
        while(del.size()<keys.length) {
            int i=rand.nextInt(vals.length);
            if(del.contains(i)) continue;
            long k=keys[i];T v=vals[i];
            map.del(k);
            del.add(i);
            assertFalse("has("+k+")", map.has(k));
        }
        System.out.println("del(long) OK");
    }

    public <T> void featureTestClear(Random rand, long[] keys, T[] vals, Long2ObjMap<T> map, int verb) throws Exception {
        map.clear();
        for(int i=0;i<keys.length;i++) map.add(keys[i],vals[i]);
        map.clear();
        if(verb>0) System.err.println("Ready");
        assertTrue("isEmpty", map.isEmpty());
        for(int i=0;i<vals.length;i++) {
            long k=keys[i];
            T v=vals[i];
            assertFalse("has("+k+")", map.has(k));
        }
        System.out.println("clear() OK");
    }

    public <T> void featureTestIterate(Random rand, long[] keys, T[] vals, Long2ObjMap<T> map, int verb) throws Exception {
        map.clear();
        for(int i=0;i<keys.length;i++) map.add(keys[i],vals[i]);
        @SuppressWarnings("unchecked")
        Iterator<Long2ObjMap.Entry<T>> it=(Iterator<Long2ObjMap.Entry<T>>) map.iterator();
        for(int i=0;i<map.size();i++) {
            assertTrue(i+": hasNext()", it.hasNext());
            it.next();
        }
        assertFalse("hasNext()", it.hasNext());
        System.out.println("iterate() OK");
    }

    public <T> void featureTestVisit(Random rand, long[] keys, T[] vals, Long2ObjMap<T> map, int verb) throws Exception {
        map.clear();
        for(int i=0;i<keys.length;i++) map.add(keys[i],vals[i]);
        assertEquals("visit()", map.size(), map.visit(new Visitable.Visitor<Map.Entry<Long,T>,Map<Long,T>>() {
            @Override
            public long invoke(Map.Entry<Long,T> obj, Map<Long,T> set) {
                assertTrue(set.has(obj.getKey()));
                assertEquals(obj.getValue(), set.get(obj.getKey()));
                return 1;
            }
        }, map));
        System.out.println("visit() OK");
    }

    @SuppressWarnings("unchecked")
    public <T> void featureTestSerialize(Random rand, long[] keys, T[] vals, Long2ObjMap<T> map, int verb) throws Exception {
        map.clear();
        for(int i=0;i<keys.length;i++) map.add(keys[i],vals[i]);
        File t=null;
        try {
            t=File.createTempFile("serial-", ".ser");
            ObjectOutputStream os=null;
            try {
                os=new ObjectOutputStream(new FileOutputStream(t));
                os.writeObject(map);
            }
            finally {
                if(os!=null) os.close();
            }
            ObjectInputStream is=null;
            Long2ObjMap<T> smap=null;
            try {
                is=new ObjectInputStream(new FileInputStream(t));
                smap=(Long2ObjMap<T>) is.readObject();
            }
            finally {
                if(is!=null) is.close();
            }
//            assertTrue("serialize(lst)==lst",imap.equals(smap));
            assertEquals("size(map)==map", map.size(),smap.size());
            for(int i=0;i<keys.length;i++) {
                long k=keys[i];T v=vals[i];
                assertTrue("has("+k+")", smap.has(k));
                assertTrue("get("+k+")", smap.has(k));
            }
            System.out.println("serial OK");
        }
        finally {
            if(t!=null) t.delete();
        }
    }

    public <T> void featureTestEquals(Random rand, long[] keys, T[] vals, Long2ObjMap<T> map, Long2ObjMap<T> eql, int verb) throws Exception {
        map.clear();
        for(int i=0;i<keys.length;i++) map.add(keys[i],vals[i]);
        assertFalse("lst.equals(eql)", map.equals(eql));
        for(int i=0;i<keys.length;i++) eql.add(keys[i],vals[i]);
        assertTrue("lst.equals(eql)", map.equals(eql));
        assertEquals("lst.hashCode()", map.hashCode(), eql.hashCode());
        for(int i=0; i<100; i++) {
            int p = rand.nextInt(vals.length);
            if(map.del(keys[p])) assertFalse("lst.equals(eql)", map.equals(eql));
            else assertTrue("lst.equals(eql)", map.equals(eql));
            eql.del(keys[p]);
            assertTrue("lst.equals(eql)", map.equals(eql));
            assertEquals("lst.hashCode()", map.hashCode(), eql.hashCode());
        }
        System.out.println("equals OK");
    }

    public <T,S extends Long2ObjMap<T> & Cloneable> void featureTestClone(Random rand, long[] keys, T[] vals, S map, int verb) throws Exception {
        map.clear();
        for(int i=0;i<keys.length;i++) map.add(keys[i],vals[i]);
        Map<Long,T> cln = clone(map);
        assertEquals("size(set)==set", map.size(),cln.size());
        for(int i=0;i<keys.length;i++) {
            assertEquals("get("+i+")", map.get(keys[i]), cln.get(keys[i]));
        }
        System.out.println("clone OK");
    }
}
