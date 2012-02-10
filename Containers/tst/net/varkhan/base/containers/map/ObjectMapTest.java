/**
 *
 */
package net.varkhan.base.containers.map;

import junit.framework.TestCase;
import net.varkhan.base.containers.Iterator;

import java.io.*;
import java.util.HashSet;
import java.util.Random;


/**
 * @author varkhan
 * @date Feb 10, 2010
 * @time 6:24:10 AM
 */
public class ObjectMapTest extends TestCase {
    long baseseed=1234567890987654321L;

    public void testArrayOpenHashMap() throws Exception {
        featureTest(100000, new ArrayOpenHashMap<String,String>(), 0);
    }

    public void testBlockOpenHashMap() throws Exception {
        featureTest(100000, new BlockOpenHashMap<String,String>(), 0);
    }

    private String[] generateKeyStrings(Random rand, int num, int minl, int maxl, char[] characters) {
        java.util.Set<String> keys=new HashSet<String>(num);
        while(keys.size()<num) {
            StringBuilder buf=new StringBuilder();
            int len=minl+rand.nextInt(maxl-minl);
            for(int j=0;j<len;j++) buf.append(characters[rand.nextInt(characters.length)]);
            keys.add(buf.toString());
        }
        return keys.toArray(new String[num]);
    }

    private String[] generateValueStrings(Random rand, int num, double sparse, int minl, int maxl, char[] characters) {
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

    public void featureTest(int num, Map<String,String> map, int verb) throws Exception {
        Random rand=new Random(baseseed);
        String[] keys=generateKeyStrings(rand, num, 2, 5, "abcdefghijklmnopqrstuvwxyz".toCharArray());
        String[] vals=generateValueStrings(rand, num, .1, 2, 5, "abcdefghijklmnopqrstuvwxyz".toCharArray());
        System.out.println("Test "+map.getClass().getSimpleName()+" ["+baseseed+"]");
        featureTestAdd(rand, keys, vals, map, verb);
        featureTestHas(rand, keys, vals, map, verb);
//        featureTestIdx(rand,keys, vals,map,verb);
        featureTestGet(rand,keys, vals,map,verb);
        featureTestDel(rand, keys, vals, map, verb);
        featureTestClear(rand, keys, vals, map, verb);
        featureTestIterate(rand, keys, vals, map, verb);
        try { featureTestSerialize(rand, keys, vals, map, verb); } catch(NotSerializableException e) { /* ignore */ }
        System.out.println();
    }

    public <K,V> void featureTestAdd(Random rand, K[] keys, V[] vals, Map<K,V> map, int verb) throws Exception {
        // Testing key adding
        for(int i=0;i<vals.length;i++) {
            K k=keys[i];
            V v=vals[i];
            map.add(k,v);
            if(verb>0) System.err.println("Added "+i+" "+k);
            if(verb>1) System.err.println(map);
            assertTrue("has("+k+")", map.has(k));
        }
        System.out.println("add(T) OK");
    }

    public <K,V> void featureTestHas(Random rand, K[] keys, V[] vals, Map<K,V> map, int verb) throws Exception {
        map.clear();
        for(int i=0;i<vals.length;i++) map.add(keys[i],vals[i]);
        if(verb>0) System.err.println("Ready");
        // Testing if keys are seen
        for(int i=0;i<vals.length;i++) {
            K k=keys[i];
            assertTrue("has("+k+")", map.has(k));
            if(verb>0) System.err.println("Checked "+i+" "+k);
            if(verb>1) System.err.println(map);
        }
        System.out.println("has(T) OK");
    }

    private <K,V> void featureTestGet(Random rand, K[] keys, V[] vals, Map<K,V> map, int verb) throws Exception {
        map.clear();
        for(int i=0;i<keys.length;i++) map.add(keys[i],vals[i]);
        // Testing if keys/values can be retrieved
        for(int i=0;i<keys.length;i++) {
            K k=keys[i];
            V v=vals[i];
            assertTrue("get("+k+") = "+v, v==map.get(k));
            if(verb>1) System.err.println("Checked "+i+": "+k);
            if(verb>2) System.err.println(map);
        }
        if(verb>0) System.out.println("get{Key,Value}(long) OK");
    }

    public <K,V> void featureTestDel(Random rand, K[] keys, V[] vals, Map<K,V> map, int verb) throws Exception {
        map.clear();
        for(int i=0;i<vals.length;i++) map.add(keys[i],vals[i]);
        if(verb>0) System.err.println("Ready");
        // Testing forward delete (no resize)
        for(int i=0;i<vals.length;i++) {
            K k=keys[i];
            V v=vals[i];
            // This may have been deleted before
//            assertTrue("has("+v+")",map.has(v));
            map.del(k);
            if(verb>0) System.err.println("Deleted "+i+" "+k);
            if(verb>1) System.err.println(map);
            assertFalse("has("+k+")", map.has(k));
        }
        System.out.println("del(T) forward OK");
        map.clear();
        for(int i=0;i<vals.length;i++) map.add(keys[i],vals[i]);
        if(verb>0) System.err.println("Ready");
        // Testing backward delete (implies auto resize)
        for(int i=vals.length-1;i>=0;i--) {
            K k=keys[i];
            V v=vals[i];
            // This may have been deleted before
//            assertTrue("has("+v+")",map.has(v));
            map.del(k);
            if(verb>0) System.err.println("Deleted "+i+" "+k);
            if(verb>1) System.err.println(map);
            assertFalse("has("+k+")", map.has(k));
        }
        System.out.println("del() backward OK");
        map.clear();
        for(int i=0;i<vals.length;i++) map.add(keys[i],vals[i]);
        if(verb>0) System.err.println("Ready");
        // Testing random delete (implies auto resize)
        java.util.Set<Integer> del=new HashSet<Integer>();
        while(del.size()<vals.length) {
            int i=rand.nextInt(vals.length);
            if(del.contains(i)) continue;
            K k=keys[i];V v=vals[i];
            map.del(k);
            del.add(i);
            assertFalse("has("+k+")", map.has(k));
        }
        System.out.println("del(long) OK");
    }

    public <K,V> void featureTestClear(Random rand, K[] keys, V[] vals, Map<K,V> map, int verb) throws Exception {
        map.clear();
        for(int i=0;i<vals.length;i++) map.add(keys[i],vals[i]);
        map.clear();
        if(verb>0) System.err.println("Ready");
        assertTrue("isEmpty", map.isEmpty());
        for(int i=0;i<vals.length;i++) {
            K k=keys[i];
            V v=vals[i];
            assertFalse("has("+k+")", map.has(k));
        }
        System.out.println("clear() OK");
    }

    public <K,V> void featureTestIterate(Random rand, K[] keys, V[] vals, Map<K,V> map, int verb) throws Exception {
        map.clear();
        for(int i=0;i<vals.length;i++) map.add(keys[i],vals[i]);
        @SuppressWarnings("unchecked")
        Iterator<Map.Entry<K,V>> it=(Iterator<Map.Entry<K,V>>) map.iterator();
        for(int i=0;i<map.size();i++) {
            assertTrue(i+": hasNext()", it.hasNext());
            it.next();
        }
        assertFalse("hasNext()", it.hasNext());
        System.out.println("iterate() OK");
    }

    @SuppressWarnings("unchecked")
    public <K,V> void featureTestSerialize(Random rand, K[] keys, V[] vals, Map<K,V> map, int verb) throws Exception {
        map.clear();
        for(int i=0;i<vals.length;i++) map.add(keys[i],vals[i]);
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
            Map<K,V> smap=null;
            try {
                is=new ObjectInputStream(new FileInputStream(t));
                smap=(Map<K,V>) is.readObject();
            }
            finally {
                if(is!=null) is.close();
            }
//            assertTrue("serialize(lst)==lst",imap.equals(smap));
            for(int i=0;i<vals.length;i++) {
                K k=keys[i];V v=vals[i];
                assertTrue("has("+k+")", smap.has(k));
                assertTrue("get("+k+")", smap.has(k));
            }
            System.out.println("serial OK");
        }
        finally {
            if(t!=null) t.delete();
        }
    }

}
