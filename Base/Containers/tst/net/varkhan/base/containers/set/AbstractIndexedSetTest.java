package net.varkhan.base.containers.set;

import junit.framework.TestCase;
import net.varkhan.base.containers.Index;
import net.varkhan.base.containers.IndexedVisitable;
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
 * @time 5:27 PM
 */
public abstract class AbstractIndexedSetTest extends TestCase {
    @SuppressWarnings("unchecked")
    public static <C extends Cloneable> C clone(C obj) {
        try {
            return (C) obj.getClass().getMethod("clone").invoke(obj);
        }
        catch(Exception e) {
            return null;
        }
    }

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
        java.util.Set<Integer> del=new HashSet<Integer>();
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
//            assertTrue("serialize(set)==set",iset.equals(sset));
            assertEquals("size(set)==set", iset.size(),sset.size());
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
        net.varkhan.base.containers.Iterator<T> it=(net.varkhan.base.containers.Iterator<T>) iset.iterator();
        for(int i=0;i<iset.size();i++) {
            assertTrue(i+": hasNext()", it.hasNext());
            it.next();
        }
        assertFalse("!hasNext()", it.hasNext());
        System.out.println("iterate() OK");
    }

    public <T> void featureTestVisit(Random rand, T[] vals, IndexedSet<T> iset, int verb) throws Exception {
        long[] idx=new long[vals.length];
        iset.clear();
        for(int i=0;i<vals.length;i++) idx[i]=iset.add(vals[i]);
        assertEquals("visit()", iset.size(), iset.visit(new Visitable.Visitor<T,IndexedSet<T>>() {
            @Override
            public long invoke(T obj, IndexedSet<T> set) {
                assertTrue(set.index(obj)>=0);
                return 1;
            }
        }, iset));
        assertEquals("visit()", iset.size(), iset.visit(new IndexedVisitable.IndexedVisitor<T,IndexedSet<T>>() {
            @Override
            public long invoke(long idx, T obj, IndexedSet<T> set) {
                assertTrue(set.has(idx));
                assertEquals(idx, set.index(obj));
                return 1;
            }
        }, iset));
        System.out.println("visit() OK");
    }

    public <T> void featureTestEquals(Random rand, T[] vals, IndexedSet<T> iset, IndexedSet<T> eql, int verb) throws Exception {
        long[] idx=new long[vals.length];
        iset.clear();
        for(int i=0;i<vals.length;i++) idx[i]=iset.add(vals[i]);
        assertFalse("lst.equals(eql)", iset.equals(eql));
        for(int i=0;i<vals.length;i++) if(vals[i]!=null) eql.add(vals[i]);
        assertTrue("lst.equals(eql)", iset.equals(eql));
        assertEquals("lst.hashCode()", iset.hashCode(), eql.hashCode());
        for(int i=0; i<100; i++) {
            int p = rand.nextInt(vals.length);
            if(!iset.has(p)) continue;
            iset.del(p);
            assertFalse("lst.equals(eql)", iset.equals(eql));
            eql.del(p);
            assertTrue("lst.equals(eql)", iset.equals(eql));
            assertEquals("lst.hashCode()", iset.hashCode(), eql.hashCode());
        }
        System.out.println("equals OK");
    }

    public <T,S extends IndexedSet<T> & Cloneable> void featureTestClone(Random rand, T[] vals, S iset, int verb) throws Exception {
        long[] idx=new long[vals.length];
        iset.clear();
        for(int i=0;i<vals.length;i++) idx[i]=iset.add(vals[i]);
        IndexedSet<T> cln = clone(iset);
        assertEquals("size(set)==set", iset.size(),cln.size());
        for(int i=0;i<vals.length;i++) {
            T v=vals[i];
            long x=idx[i];
            assertEquals("index("+v+") = "+x, x, iset.index(v));
            assertEquals("get("+x+")", v, iset.get(x));
        }
        System.out.println("clone OK");
    }

    public <T> void featureTestString(Random rand, T[] vals, IndexedSet<T> iset, int verb) throws Exception {
        long[] idx=new long[vals.length];
        iset.clear();
        for(int i=0;i<vals.length;i++) idx[i]=iset.add(vals[i]);
        String s = iset.toString();
        assertTrue("toString() : { ",s.startsWith("{ "));
        assertTrue("toString() : { ",s.endsWith(" }"));
        for(int i=0;i<vals.length;i++) {
            String k=iset.index(vals[i])+"@"+vals[i];
            assertTrue("toString() : "+k,s.contains(k+", ")||s.contains(k+" }"));
        }
        System.out.println("toString OK");
    }

}
