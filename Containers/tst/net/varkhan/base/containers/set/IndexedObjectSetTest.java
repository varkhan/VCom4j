/**
 *
 */
package net.varkhan.base.containers.set;

import junit.framework.TestCase;
import net.varkhan.base.containers.Index;
import net.varkhan.base.containers.Iterator;

import java.io.*;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;


/**
 * @author varkhan
 * @date May 21, 2009
 * @time 12:13:22 AM
 */
public class IndexedObjectSetTest extends TestCase {
    long baseseed=1234567890987654321L;


    public void testArrayOpenHashIndexedSet() throws Exception {
        featureTest(100000, new ArrayOpenHashIndexedSet<String>(), 0);
    }

    public void testAbstractArrayOpenHashIndexedSet() throws Exception {
        featureTest(100000, new AbstractArrayOpenHashIndexedSet<String>() {
            private static final long serialVersionUID=1L;
        }, 0);
    }

    public void testBlockOpenHashIndexedSet() throws Exception {
        featureTest(100000, new BlockOpenHashIndexedSet<String>(), 0);
    }

    public void testAbstractBlockOpenHashIndexedSet() throws Exception {
        featureTest(100000, new AbstractBlockOpenHashIndexedSet<String>() {
            private static final long serialVersionUID=1L;
        }, 0);
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

    public void featureTest(int num, IndexedSet<String> iset, int verb) throws Exception {
        Random rand=new Random(baseseed);
        String[] vals=generateKeyStrings(rand, num, 2, 5, "abcdefghijklmnopqrstuvwxyz".toCharArray());
        System.out.println("Test "+iset.getClass().getSimpleName()+" ["+baseseed+"]");
        featureTestAdd(rand, vals, iset, verb);
        featureTestHas(rand, vals, iset, verb);
        featureTestIdx(rand, vals, iset, verb);
        featureTestGet(rand, vals, iset, verb);
        featureTestDel(rand, vals, iset, verb);
        featureTestClear(rand, vals, iset, verb);
        featureTestIndexes(rand, vals, iset, verb);
        featureTestIterate(rand, vals, iset, verb);
        try { featureTestSerialize(rand, vals, iset, verb); } catch(NotSerializableException e) { /* ignore */ }
        System.out.println();
    }

    public <T> void featureTestAdd(Random rand, T[] vals, IndexedSet<T> iset, int verb) throws Exception {
        // Testing key adding
        for(int i=0;i<vals.length;i++) {
            T v=vals[i];
            long x=iset.add(v);
            if(verb>0) System.err.println("Added "+v+": "+x);
            if(verb>1) System.err.println(iset);
            assertTrue("add("+v+") = "+x, x>=0);
        }
        System.out.println("add(T) OK");
    }

    public <T> void featureTestHas(Random rand, T[] vals, IndexedSet<T> iset, int verb) throws Exception {
        long[] idx=new long[vals.length];
        iset.clear();
        for(int i=0;i<vals.length;i++) idx[i]=iset.add(vals[i]);
        // Testing if keys are seen
        for(int i=0;i<vals.length;i++) {
            long x=idx[i];
            assertTrue("has("+x+")", iset.has(x));
            if(verb>1) System.err.println("Checked "+x);
            if(verb>2) System.err.println(iset);
        }
        System.out.println("has(long) OK");
    }

    public <T> void featureTestIdx(Random rand, T[] vals, IndexedSet<T> iset, int verb) throws Exception {
        long[] idx=new long[vals.length];
        iset.clear();
        for(int i=0;i<vals.length;i++) idx[i]=iset.add(vals[i]);
        // Testing if indexes can be retrieved
        for(int i=0;i<vals.length;i++) {
            T v=vals[i];
            long x=iset.index(v);
            assertTrue("index("+v+") = "+idx[i]+" / "+x, x==idx[i]);
            if(verb>0) System.err.println("Indexed "+v+": "+x);
            if(verb>1) System.err.println(iset);
        }
        System.out.println("index(T) OK");
    }

    public <T> void featureTestGet(Random rand, T[] vals, IndexedSet<T> iset, int verb) throws Exception {
        long[] idx=new long[vals.length];
        iset.clear();
        for(int i=0;i<vals.length;i++) idx[i]=iset.add(vals[i]);
        // Testing if elements can be retrieved
        for(int i=0;i<vals.length;i++) {
            T v=vals[i];
            long x=idx[i];
            assertTrue("get("+x+") = "+v+" / "+iset.get(x), v==null ? iset.get(x)==null : v.equals(iset.get(x)));
            if(verb>0) System.err.println("Got "+v+": "+x);
            if(verb>1) System.err.println(iset);
        }
        System.out.println("get(long) OK");
    }

    public <T> void featureTestDel(Random rand, T[] vals, IndexedSet<T> iset, int verb) throws Exception {
        long[] idx=new long[vals.length];
        iset.clear();
        for(int i=0;i<vals.length;i++) idx[i]=iset.add(vals[i]);
        // Testing forward delete (no resize)
        for(int i=0;i<vals.length;i++) {
            T v=vals[i];
            long x=idx[i];
            // This may have been deleted before
//            assertTrue("has("+s+")",set.has(x));
            iset.del(x);
            if(verb>0) System.err.println("Deleted "+v+": "+x);
            if(verb>1) System.err.println(iset);
            assertFalse("has("+v+")", iset.has(x));
//            for(int j=i+1; j<a.length; j++) {
//                String t = a[i];
//                if(t.equals(s)) continue;
//                long xx = set.index(t);
//                assertTrue("index("+t+") = "+idx[i]+" / "+xx,xx==idx[i]);
//            }
//            assertTrue("index("+v+") = "+idx[i]+" / "+x, x==idx[i]);
        }
        System.out.println("del(long) forward OK");
        iset.clear();
        for(int i=0;i<vals.length;i++) idx[i]=iset.add(vals[i]);
        // Testing backward delete (implies auto resize)
        for(int i=vals.length-1;i>=0;i--) {
            T v=vals[i];
            long x=idx[i];
            // This may have been deleted before
//            assertTrue("has("+s+")",set.has(x));
            iset.del(x);
            assertFalse("has("+v+")", iset.has(x));
//            for(int j=i+1; j<a.length; j++) {
//                String t = a[i];
//                if(t.equals(s)) continue;
//                long xx = set.index(t);
//                assertTrue("index("+t+") = "+idx[i]+" / "+xx,xx==idx[i]);
//            }
//            assertTrue("index("+v+") = "+idx[i]+" / "+x, x==idx[i]);
        }
        System.out.println("del(long) backward OK");
        iset.clear();
        for(int i=0;i<vals.length;i++) idx[i]=iset.add(vals[i]);
        // Testing random delete (implies auto resize)
        Set<Integer> del=new HashSet<Integer>();
        while(del.size()<vals.length) {
            int i=rand.nextInt(vals.length);
            if(del.contains(i)) continue;
            T v=vals[i];
            long x=idx[i];
            iset.del(x);
            del.add(i);
            assertFalse("has("+v+")", iset.has(x));
//            for(int j=i+1; j<a.length; j++) {
//                String t = a[i];
//                if(t.equals(s)) continue;
//                long xx = set.index(t);
//                assertTrue("index("+t+") = "+idx[i]+" / "+xx,xx==idx[i]);
//            }
//            assertTrue("index("+v+") = "+idx[i]+" / "+x, x==idx[i]);
        }
        System.out.println("del(long) OK");
    }

    @SuppressWarnings("unchecked")
    public <T> void featureTestSerialize(Random rand, T[] vals, IndexedSet<T> iset, int verb) throws Exception {
        long[] idx=new long[vals.length];
        iset.clear();
        for(int i=0;i<vals.length;i++) idx[i]=iset.add(vals[i]);
        File t=null;
        try {
            t=File.createTempFile("serial-", ".ser");
            ObjectOutputStream os=null;
            try {
                os=new ObjectOutputStream(new FileOutputStream(t));
                os.writeObject(iset);
            }
            finally {
                if(os!=null) os.close();
            }
            ObjectInputStream is=null;
            IndexedSet<T> sset=null;
            try {
                is=new ObjectInputStream(new FileInputStream(t));
                sset=(IndexedSet<T>) is.readObject();
            }
            finally {
                if(is!=null) is.close();
            }
//            assertTrue("serialize(lst)==lst",iset.equals(sset));
            for(int i=0;i<vals.length;i++) {
                T v=vals[i];
                long x=idx[i];
                assertEquals("index("+v+") = "+x, x, sset.index(v));
                assertEquals("get("+x+")", v, sset.get(x));
            }
            System.out.println("serial OK");
        }
        finally {
            if(t!=null) t.delete();
        }
    }

    public <T> void featureTestClear(Random rand, T[] vals, IndexedSet<T> iset, int verb) throws Exception {
        long[] idx=new long[vals.length];
        iset.clear();
        for(int i=0;i<vals.length;i++) idx[i]=iset.add(vals[i]);
        iset.clear();
        assertTrue("isEmpty", iset.isEmpty());
        for(int i=0;i<vals.length;i++) {
            long x=idx[i];
            assertFalse("has("+x+")", iset.has(x));
        }
        System.out.println("clear() OK");
    }

    public <T> void featureTestIndexes(Random rand, T[] vals, IndexedSet<T> iset, int verb) throws Exception {
        long[] idx=new long[vals.length];
        iset.clear();
        for(int i=0;i<vals.length;i++) idx[i]=iset.add(vals[i]);
        Index it=iset.indexes();
        assertFalse("!hasPrevious()", it.hasPrevious());
        boolean prev=false;
        for(int i=0;i<iset.size();i++) {
            assertTrue(i+": hasNext()", it.hasNext());
            long x=it.next();
            if(verb>0) System.err.println(i+": next() = "+x);
            if(prev) assertTrue(i+": hasPrevious()", it.hasPrevious());
            else assertFalse(i+": !hasPrevious()", it.hasPrevious());
            prev=true;
        }
        assertFalse("!hasNext()", it.hasNext());
        System.out.println("indexes() OK");
    }

    public <T> void featureTestIterate(Random rand, T[] vals, IndexedSet<T> iset, int verb) throws Exception {
        long[] idx=new long[vals.length];
        iset.clear();
        for(int i=0;i<vals.length;i++) idx[i]=iset.add(vals[i]);
        @SuppressWarnings("unchecked")
        Iterator<T> it=(Iterator<T>) iset.iterator();
        for(int i=0;i<iset.size();i++) {
            assertTrue(i+": hasNext()", it.hasNext());
            it.next();
        }
        assertFalse("!hasNext()", it.hasNext());
        System.out.println("iterate() OK");
    }

}
