/**
 *
 */
package net.varkhan.base.containers.set;

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
public class ObjectSetTest extends TestCase {
    long baseseed=1234567890987654321L;

    public void testArrayOpenHashSet() throws Exception {
        featureTest(100000, new ArrayOpenHashSet<String>(), 0);
    }

    public void testBlockOpenHashSet() throws Exception {
        featureTest(100000, new BlockOpenHashSet<String>(), 0);
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

    public void featureTest(int num, Set<String> set, int verb) throws Exception {
        Random rand=new Random(baseseed);
        String[] vals=generateKeyStrings(rand, num, 2, 5, "abcdefghijklmnopqrstuvwxyz".toCharArray());
        System.out.println("Test "+set.getClass().getSimpleName()+" ["+baseseed+"]");
        featureTestAdd(rand, vals, set, verb);
        featureTestHas(rand, vals, set, verb);
//        featureTestIdx(rand,vals,set,verb);
//        featureTestGet(rand,vals,set,verb);
        featureTestDel(rand, vals, set, verb);
        featureTestClear(rand, vals, set, verb);
        featureTestIterate(rand, vals, set, verb);
        try { featureTestSerialize(rand, vals, set, verb); } catch(NotSerializableException e) { /* ignore */ }
        System.out.println();
    }

    public <T> void featureTestAdd(Random rand, T[] vals, Set<T> set, int verb) throws Exception {
        // Testing key adding
        for(int i=0;i<vals.length;i++) {
            T v=vals[i];
            set.add(v);
            if(verb>0) System.err.println("Added "+i+" "+v);
            if(verb>1) System.err.println(set);
            assertTrue("has("+v+")", set.has(v));
        }
        System.out.println("add(T) OK");
    }

    public <T> void featureTestHas(Random rand, T[] vals, Set<T> set, int verb) throws Exception {
        set.clear();
        for(int i=0;i<vals.length;i++) set.add(vals[i]);
        if(verb>0) System.err.println("Ready");
        // Testing if keys are seen
        for(int i=0;i<vals.length;i++) {
            T v=vals[i];
            assertTrue("has("+v+")", set.has(v));
            if(verb>0) System.err.println("Checked "+i+" "+v);
            if(verb>1) System.err.println(set);
        }
        System.out.println("has(T) OK");
    }

    public <T> void featureTestDel(Random rand, T[] vals, Set<T> set, int verb) throws Exception {
        set.clear();
        for(int i=0;i<vals.length;i++) set.add(vals[i]);
        if(verb>0) System.err.println("Ready");
        // Testing forward delete (no resize)
        for(int i=0;i<vals.length;i++) {
            T v=vals[i];
            // This may have been deleted before
//            assertTrue("has("+v+")",set.has(v));
            set.del(v);
            if(verb>0) System.err.println("Deleted "+i+" "+v);
            if(verb>1) System.err.println(set);
            assertFalse("has("+v+")", set.has(v));
        }
        System.out.println("del(T) forward OK");
        set.clear();
        for(int i=0;i<vals.length;i++) set.add(vals[i]);
        if(verb>0) System.err.println("Ready");
        // Testing backward delete (implies auto resize)
        for(int i=vals.length-1;i>=0;i--) {
            T v=vals[i];
            // This may have been deleted before
//            assertTrue("has("+v+")",set.has(v));
            set.del(v);
            if(verb>0) System.err.println("Deleted "+i+" "+v);
            if(verb>1) System.err.println(set);
            assertFalse("has("+v+")", set.has(v));
        }
        System.out.println("del() backward OK");
        set.clear();
        for(int i=0;i<vals.length;i++) set.add(vals[i]);
        if(verb>0) System.err.println("Ready");
        // Testing random delete (implies auto resize)
        java.util.Set<Integer> del=new HashSet<Integer>();
        while(del.size()<vals.length) {
            int i=rand.nextInt(vals.length);
            if(del.contains(i)) continue;
            T v=vals[i];
            set.del(v);
            del.add(i);
            assertFalse("has("+v+")", set.has(v));
        }
        System.out.println("del(long) OK");
    }

    public <T> void featureTestClear(Random rand, T[] vals, Set<T> set, int verb) throws Exception {
        set.clear();
        for(int i=0;i<vals.length;i++) set.add(vals[i]);
        set.clear();
        if(verb>0) System.err.println("Ready");
        assertTrue("isEmpty", set.isEmpty());
        for(int i=0;i<vals.length;i++) {
            T v=vals[i];
            assertFalse("has("+v+")", set.has(v));
        }
        System.out.println("clear() OK");
    }

    public <T> void featureTestIterate(Random rand, T[] vals, Set<T> set, int verb) throws Exception {
        set.clear();
        for(int i=0;i<vals.length;i++) set.add(vals[i]);
        @SuppressWarnings("unchecked")
        Iterator<T> it=(Iterator<T>) set.iterator();
        for(int i=0;i<set.size();i++) {
            assertTrue(i+": hasNext()", it.hasNext());
            it.next();
        }
        assertFalse("hasNext()", it.hasNext());
        System.out.println("iterate() OK");
    }

    @SuppressWarnings("unchecked")
    public <T> void featureTestSerialize(Random rand, T[] vals, Set<T> set, int verb) throws Exception {
        set.clear();
        for(int i=0;i<vals.length;i++) set.add(vals[i]);
        File t=null;
        try {
            t=File.createTempFile("serial-", ".ser");
            ObjectOutputStream os=null;
            try {
                os=new ObjectOutputStream(new FileOutputStream(t));
                os.writeObject(set);
            }
            finally {
                if(os!=null) os.close();
            }
            ObjectInputStream is=null;
            Set<T> sset=null;
            try {
                is=new ObjectInputStream(new FileInputStream(t));
                sset=(Set<T>) is.readObject();
            }
            finally {
                if(is!=null) is.close();
            }
//            assertTrue("serialize(lst)==lst",iset.equals(sset));
            for(int i=0;i<vals.length;i++) {
                T v=vals[i];
                assertTrue("has("+v+")", sset.has(v));
            }
            System.out.println("serial OK");
        }
        finally {
            if(t!=null) t.delete();
        }
    }

}
