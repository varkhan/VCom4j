package net.varkhan.base.containers.set;

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
 * @date 11/28/13
 * @time 1:05 PM
 */
public abstract class AbstractSetTest extends TestCase {

    protected String[] genKeyStrings(Random rand, int num, int minl, int maxl, char[] characters) {
        java.util.Set<String> keys=new HashSet<String>(num);
        while(keys.size()<num) {
            StringBuilder buf=new StringBuilder();
            int len=minl+rand.nextInt(maxl-minl);
            for(int j=0;j<len;j++) buf.append(characters[rand.nextInt(characters.length)]);
            keys.add(buf.toString());
        }
        return keys.toArray(new String[num]);
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

    public <T> void featureTestSize(Random rand, T[] vals, Set<T> set, int verb, boolean exact) throws Exception {
        long s=0;
        // Testing key adding
        for(int i=0;i<vals.length;i++) {
            T v=vals[i];
            if(set.add(v)) s++;
            if(verb>0) System.err.println("Added "+i+" "+v);
            if(verb>1) System.err.println(set);
        }
        assertEquals("size()",s,set.size());
        System.out.println("size(T) OK");
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
//            assertTrue("serialize(lst)==lst",sset.equals(sset));
            assertEquals("size(set)==set", set.size(),sset.size());
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

    public <T> void featureTestEquals(Random rand, T[] vals, Set<T> set, Set<T> eql, int verb) throws Exception {
        for(int i=0;i<vals.length;i++) if(vals[i]!=null) set.add(vals[i]);
        assertFalse("lst.equals(eql)", set.equals(eql));
        for(int i=0;i<vals.length;i++) if(vals[i]!=null) eql.add(vals[i]);
        assertTrue("lst.equals(eql)", set.equals(eql));
        assertEquals("lst.hashCode()", set.hashCode(), eql.hashCode());
        for(int i=0; i<100; i++) {
            int p = rand.nextInt(vals.length);
            if(set.del(vals[p])) assertFalse("lst.equals(eql)", set.equals(eql));
            else assertTrue("lst.equals(eql)", set.equals(eql));
            eql.del(vals[p]);
            assertTrue("lst.equals(eql)", set.equals(eql));
            assertEquals("lst.hashCode()", set.hashCode(), eql.hashCode());
        }
        System.out.println("equals OK");
    }

    public <T,S extends Set<T> & Cloneable> void featureTestClone(Random rand, T[] vals, S set, int verb) throws Exception {
        for(int i=0;i<vals.length;i++) if(vals[i]!=null) set.add(vals[i]);
        Set<T> cln = clone(set);
        assertEquals("size(set)==set", set.size(),cln.size());
        for(int i=0;i<vals.length;i++) {
            assertEquals("has("+i+")", set.has(vals[i]), cln.has(vals[i]));
        }
        System.out.println("clone OK");
    }

    @SuppressWarnings("unchecked")
    public static <C extends Cloneable> C clone(C obj) {
        try {
            return (C) obj.getClass().getMethod("clone").invoke(obj);
        }
        catch(Exception e) {
            return null;
        }
    }

}
