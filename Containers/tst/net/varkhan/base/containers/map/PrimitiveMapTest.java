package net.varkhan.base.containers.map;

import junit.framework.TestCase;
import net.varkhan.base.containers.Iterator;

import java.io.*;
import java.util.HashSet;
import java.util.Random;


/**
 * <b></b>.
 * <p/>
 *
 * @author varkhan
 * @date 2/9/11
 * @time 8:43 PM
 */
public class PrimitiveMapTest extends TestCase {

    long baseseed=1234567890987654321L;

//    public void testValues() {
//        System.err.println(0x7fffffffffffffffL);
//        System.err.println(0x8000000000000000L);
//    }

    public void testArrayOpenHashMap() throws Exception {
        featureTest(100000, new ArrayOpenHashLong2FloatMap(), 0);
    }

    public void testBlockOpenHashMap() throws Exception {
        featureTest(100000, new BlockOpenHashLong2FloatMap(), 0);
    }

    private long[] generateKeyLongs(Random rand, int num) {
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

    private float[] generateValueFloats(Random rand, int num, double sparse, float minVal, float maxVal) {
        float[] a=new float[num];
        for(int i=0;i<num;i++) {
            if(rand.nextFloat()<sparse) {
                a[i]=0f;
                continue;
            }
            a[i] = minVal + (maxVal-minVal)*rand.nextFloat();
        }
        return a;
    }

    public void featureTest(int num, Long2FloatMap map, int verb) throws Exception {
        Random rand=new Random(baseseed);
        long[] keys=generateKeyLongs(rand, num);
        float[] vals=generateValueFloats(rand, num, .1, 1f, 10000f);
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

    public void featureTestAdd(Random rand, long[] keys, float[] vals, Long2FloatMap map, int verb) throws Exception {
        // Testing key adding
        for(int i=0;i<vals.length;i++) {
            long k=keys[i];
            float v=vals[i];
            map.add(k,v);
            if(verb>0) System.err.println("Added "+i+" "+k);
            if(verb>1) System.err.println(map);
            assertTrue("has("+k+")", map.has(k));
        }
        System.out.println("add(T) OK");
    }

    public void featureTestHas(Random rand, long[] keys, float[] vals, Long2FloatMap map, int verb) throws Exception {
        map.clear();
        for(int i=0;i<vals.length;i++) map.add(keys[i],vals[i]);
        if(verb>0) System.err.println("Ready");
        // Testing if keys are seen
        for(int i=0;i<vals.length;i++) {
            long k=keys[i];
            assertTrue("has("+k+")", map.has(k));
            if(verb>0) System.err.println("Checked "+i+" "+k);
            if(verb>1) System.err.println(map);
        }
        System.out.println("has(T) OK");
    }

    private void featureTestGet(Random rand, long[] keys, float[] vals, Long2FloatMap map, int verb) throws Exception {
        map.clear();
        for(int i=0;i<keys.length;i++) map.add(keys[i],vals[i]);
        // Testing if keys/values can be retrieved
        for(int i=0;i<keys.length;i++) {
            long k=keys[i];
            float v=vals[i];
            assertTrue("get("+k+") = "+v, v==map.get(k));
            if(verb>1) System.err.println("Checked "+i+": "+k);
            if(verb>2) System.err.println(map);
        }
        if(verb>0) System.out.println("get{Key,Value}(long) OK");
    }

    public void featureTestDel(Random rand, long[] keys, float[] vals, Long2FloatMap map, int verb) throws Exception {
        map.clear();
        for(int i=0;i<vals.length;i++) map.add(keys[i],vals[i]);
        if(verb>0) System.err.println("Ready");
        // Testing forward delete (no resize)
        for(int i=0;i<vals.length;i++) {
            long k=keys[i];
            float v=vals[i];
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
            long k=keys[i];
            float v=vals[i];
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
            long k=keys[i];float v=vals[i];
            map.del(k);
            del.add(i);
            assertFalse("has("+k+")", map.has(k));
        }
        System.out.println("del(long) OK");
    }

    public void featureTestClear(Random rand, long[] keys, float[] vals, Long2FloatMap map, int verb) throws Exception {
        map.clear();
        for(int i=0;i<vals.length;i++) map.add(keys[i],vals[i]);
        map.clear();
        if(verb>0) System.err.println("Ready");
        assertTrue("isEmpty", map.isEmpty());
        for(int i=0;i<vals.length;i++) {
            long k=keys[i];
            float v=vals[i];
            assertFalse("has("+k+")", map.has(k));
        }
        System.out.println("clear() OK");
    }

    public void featureTestIterate(Random rand, long[] keys, float[] vals, Long2FloatMap map, int verb) throws Exception {
        map.clear();
        for(int i=0;i<vals.length;i++) map.add(keys[i],vals[i]);
        @SuppressWarnings("unchecked")
        Iterator<Long2FloatMap.Entry> it=(Iterator<Long2FloatMap.Entry>) map.iterator();
        for(int i=0;i<map.size();i++) {
            assertTrue(i+": hasNext()", it.hasNext());
            it.next();
        }
        assertFalse("hasNext()", it.hasNext());
        System.out.println("iterate() OK");
    }

    @SuppressWarnings("unchecked")
    public void featureTestSerialize(Random rand, long[] keys, float[] vals, Long2FloatMap map, int verb) throws Exception {
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
            Long2FloatMap smap=null;
            try {
                is=new ObjectInputStream(new FileInputStream(t));
                smap=(Long2FloatMap) is.readObject();
            }
            finally {
                if(is!=null) is.close();
            }
//            assertTrue("serialize(lst)==lst",imap.equals(smap));
            for(int i=0;i<vals.length;i++) {
                long k=keys[i];float v=vals[i];
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
