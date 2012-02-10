/**
 *
 */
package net.varkhan.base.containers.map;

import junit.framework.TestCase;
import net.varkhan.base.containers.Index;
import net.varkhan.base.containers.Iterator;

import java.io.*;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;


/**
 * @author varkhan
 * @date Nov 5, 2009
 * @time 6:09:05 PM
 */
public class IndexedObjectMapTest extends TestCase {
    long baseseed=1234567890987654321L;

    public void testArrayOpenHashIndexedMap() throws Exception {
        featureTest(10000, new ArrayOpenHashIndexedMap<String,String>(), 0);
    }

    public void testBlockOpenHashIndexedMap() throws Exception {
        featureTest(10000, new BlockOpenHashIndexedMap<String,String>(), 0);
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

    public void featureTest(int num, IndexedMap<String,String> imap, int verb) throws Exception {
        System.out.println("Test "+imap.getClass().getSimpleName());
        Random rand=new Random(baseseed);
        String[] keys=generateKeyStrings(rand, num, 2, 5, "abcdefghijklmnopqrstuvwxyz".toCharArray());
        String[] vals=generateValueStrings(rand, num, .1, 2, 5, "abcdefghijklmnopqrstuvwxyz".toCharArray());
        System.out.println("Test "+imap.getClass().getSimpleName()+" ["+baseseed+"]");
        featureTestAdd(rand, keys, vals, imap, verb);
        featureTestHas(rand, keys, vals, imap, verb);
        featureTestIdx(rand, keys, vals, imap, verb);
        featureTestGet(rand, keys, vals, imap, verb);
        featureTestDel(rand, keys, vals, imap, verb);
        featureTestClear(rand, keys, vals, imap, verb);
        featureTestIndexes(rand, keys, vals, imap, verb);
        featureTestIterate(rand, keys, vals, imap, verb);
        try { featureTestSerialize(rand, keys, vals, imap, verb); } catch(NotSerializableException e) { /* ignore */ }
        System.out.println();
    }

    private <K,V> void featureTestAdd(Random rand, K[] keys, V[] vals, IndexedMap<K,V> imap, int verb) throws Exception {
        // Testing key adding
        for(int i=0;i<keys.length;i++) {
            K k=keys[i];
            V v=vals[i];
            long x=imap.add(k, v);
            if(verb>0) System.err.println("Added "+v+": "+x);
            if(verb>1) System.err.println(imap);
            assertTrue("add("+v+") = "+x, x>=0);
        }
        if(verb>0) System.out.println("add(T) OK");
    }

    private <K,V> void featureTestHas(Random rand, K[] keys, V[] vals, IndexedMap<K,V> imap, int verb) throws Exception {
        long[] idx=new long[keys.length];
        imap.clear();
        for(int i=0;i<keys.length;i++) idx[i]=imap.add(keys[i],vals[i]);
        // Testing if keys are seen
        for(int i=0;i<keys.length;i++) {
            long x=idx[i];
            assertTrue("has("+x+")", imap.has(x));
            if(verb>1) System.err.println("Checked "+x);
            if(verb>2) System.err.println(imap);
        }
        System.out.println("has(long) OK");
    }

    private <K,V> void featureTestIdx(Random rand, K[] keys, V[] vals, IndexedMap<K,V> imap, int verb) throws Exception {
        long[] idx=new long[keys.length];
        imap.clear();
        for(int i=0;i<keys.length;i++) idx[i]=imap.add(keys[i],vals[i]);
        // Testing if indexes can be retrieved
        for(int i=0;i<keys.length;i++) {
            K k=keys[i];
            long x=imap.index(k);
            assertTrue("index("+k+") = "+idx[i]+" / "+x, x==idx[i]);
            if(verb>1) System.err.println("Indexed "+k+": "+x);
            if(verb>2) System.err.println(imap);
        }
        if(verb>0) System.out.println("index(T) OK");
    }

    private <K,V> void featureTestGet(Random rand, K[] keys, V[] vals, IndexedMap<K,V> imap, int verb) throws Exception {
        long[] idx=new long[keys.length];
        imap.clear();
        for(int i=0;i<keys.length;i++) idx[i]=imap.add(keys[i],vals[i]);
        // Testing if keys/values can be retrieved
        for(int i=0;i<keys.length;i++) {
            K k=keys[i];
            V v=vals[i];
            long x=idx[i];
            assertTrue("getKey("+x+") = "+k, k==imap.getKey(x));
            assertTrue("getValue("+x+") = "+v, v==imap.getValue(x));
            if(verb>1) System.err.println("Indexed "+k+": "+x);
            if(verb>2) System.err.println(imap);
        }
        if(verb>0) System.out.println("get{Key,Value}(long) OK");
    }

    private <K,V> void featureTestDel(Random rand, K[] keys, V[] vals, IndexedMap<K,V> imap, int verb) throws Exception {
        long[] idx=new long[keys.length];
        // Testing forward delete (no resize)
        for(int i=0;i<keys.length;i++) {
            K k=keys[i];
            long x=idx[i];
            // This may have been deleted before
//            assertTrue("has("+k+")",set.has(x));
            imap.del(x);
            if(verb>1) System.err.println("Deleted "+k+": "+x);
            if(verb>2) System.err.println(imap);
            assertFalse("has("+k+")", imap.has(x));
//            for(int j=i+1; j<keys.length; j++) {
//                String t = keys[i];
//                if(t.equals(k)) continue;
//                long xx = set.index(t);
//                assertTrue("index("+t+") = "+idx[i]+" / "+xx,xx==idx[i]);
//            }
//            assertTrue("index("+k+") = "+idx[i]+" / "+x, x==idx[i]);
        }
        if(verb>0) System.out.println("del(long) forward OK");
        // Testing backward delete (implies auto resize)
        for(int i=0;i<keys.length;i++) {
            idx[i]=imap.add(keys[i], vals[i]);
        }
        for(int i=keys.length-1;i>=0;i--) {
            K k=keys[i];
            long x=idx[i];
            // This may have been deleted before
//            assertTrue("has("+k+")",set.has(x));
            imap.del(x);
//            System.err.println("Deleted "+k+": ");
//            System.err.println(set);
            assertFalse("has("+k+")", imap.has(x));
//            for(int j=i+1; j<keys.length; j++) {
//                String t = keys[i];
//                if(t.equals(k)) continue;
//                long xx = set.index(t);
//                assertTrue("index("+t+") = "+idx[i]+" / "+xx,xx==idx[i]);
//            }
//            assertTrue("index("+k+") = "+idx[i]+" / "+x, x==idx[i]);
        }
        if(verb>0) System.out.println("del(long) backward OK");
        // Testing random delete (implies auto resize)
        Set<Integer> del=new HashSet<Integer>();
        while(del.size()<keys.length) {
            int i=rand.nextInt(vals.length);
            if(del.contains(i)) continue;
            K k=keys[i];
            V v=vals[i];
            long x=idx[i];
            imap.del(x);
            del.add(i);
            assertFalse("has("+k+")", imap.has(x));
//            for(int j=i+1; j<a.length; j++) {
//                String t = a[i];
//                if(t.equals(s)) continue;
//                long xx = set.index(t);
//                assertTrue("index("+t+") = "+idx[i]+" / "+xx,xx==idx[i]);
//            }
//            assertTrue("index("+k+") = "+idx[i]+" / "+x, x==idx[i]);
        }
        System.out.println("del(long) OK");
    }

    @SuppressWarnings("unchecked")
    public <K,V> void featureTestSerialize(Random rand, K[] keys, V[] vals, IndexedMap<K,V> imap, int verb) throws Exception {
        long[] idx=new long[keys.length];
        imap.clear();
        for(int i=0;i<keys.length;i++) idx[i]=imap.add(keys[i],vals[i]);
        File t=null;
        try {
            t=File.createTempFile("serial-", ".ser");
            ObjectOutputStream os=null;
            try {
                os=new ObjectOutputStream(new FileOutputStream(t));
                os.writeObject(imap);
            }
            finally {
                if(os!=null) os.close();
            }
            ObjectInputStream is=null;
            IndexedMap<K,V> smap=null;
            try {
                is=new ObjectInputStream(new FileInputStream(t));
                smap=(IndexedMap<K,V>) is.readObject();
            }
            finally {
                if(is!=null) is.close();
            }
//            assertTrue("serialize(lst)==lst",iset.equals(sset));
            for(int i=0;i<vals.length;i++) {
                K k=keys[i];
                V v=vals[i];
                long x=idx[i];
                assertTrue("index("+k+") = "+x, x==smap.index(k));
                assertEquals("getKey("+x+")", k, smap.getKey(x));
                assertEquals("getValue("+x+")", v, smap.getValue(x));
            }
            System.out.println("serial OK");
        }
        finally {
            if(t!=null) t.delete();
        }
    }

    public <K,V> void featureTestClear(Random rand, K[] keys, V[] vals, IndexedMap<K,V> imap, int verb) throws Exception {
        long[] idx=new long[keys.length];
        imap.clear();
        for(int i=0;i<keys.length;i++) idx[i]=imap.add(keys[i],vals[i]);
        imap.clear();
        assertTrue("isEmpty", imap.isEmpty());
        for(int i=0;i<vals.length;i++) {
            long x=idx[i];
            assertFalse("has("+x+")", imap.has(x));
        }
        System.out.println("clear() OK");
    }

    public <K,V> void featureTestIndexes(Random rand, K[] keys, V[] vals, IndexedMap<K,V> imap, int verb) throws Exception {
        long[] idx=new long[keys.length];
        imap.clear();
        for(int i=0;i<keys.length;i++) idx[i]=imap.add(keys[i],vals[i]);
        Index it=imap.indexes();
        assertFalse("!hasPrevious()", it.hasPrevious());
        boolean prev=false;
        for(int i=0;i<imap.size();i++) {
            assertTrue(i+": hasNext()", it.hasNext());
            long x=it.next();
            /*if(verb>0)*/ System.err.println(i+": next() = "+x);
            if(prev) assertTrue(i+": hasPrevious()", it.hasPrevious());
            else assertFalse(i+": !hasPrevious()", it.hasPrevious());
            prev=true;
        }
        assertFalse("!hasNext()", it.hasNext());
        System.out.println("indexes() OK");
    }

    public <K,V> void featureTestIterate(Random rand, K[] keys, V[] vals, IndexedMap<K,V> imap, int verb) throws Exception {
        long[] idx=new long[keys.length];
        imap.clear();
        for(int i=0;i<keys.length;i++) idx[i]=imap.add(keys[i],vals[i]);
        @SuppressWarnings("unchecked")
        Iterator<IndexedMap.Entry<K,V>> it=(Iterator<IndexedMap.Entry<K,V>>) imap.iterator();
        for(int i=0;i<imap.size();i++) {
            assertTrue(i+": hasNext()", it.hasNext());
            it.next();
        }
        assertFalse("!hasNext()", it.hasNext());
        System.out.println("iterate() OK");
    }

}
