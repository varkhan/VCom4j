/**
 *
 */
package net.varkhan.base.containers.list;

import junit.framework.TestCase;
import net.varkhan.base.containers.*;
import net.varkhan.base.containers.Iterable;

import java.io.*;
import java.util.*;
import java.util.Iterator;


/**
 * @author varkhan
 * @date Mar 12, 2009
 * @time 6:20:13 PM
 */
public abstract class AbstractIndexedListTest extends TestCase {

    protected static Integer[] genIntegerList(Random rand, int size, double sparsityratio) {
        Integer[] lst=new Integer[size];
        for(int i=0;i<size;i++) {
            if(rand.nextFloat()<sparsityratio) lst[i]=null;
            else {
                lst[i]=i;
            }
        }
        return lst;
    }

    protected static String[] genStringList(Random rand, int size, double sparsityratio) {
        String[] lst=new String[size];
        for(int i=0;i<size;i++) {
            if(rand.nextFloat()<sparsityratio) lst[i]=null;
            else {
                lst[i]=genString(rand,rand.nextInt(10), "abcdefghijklmnopqrstuvwxyz01223456789".toCharArray())+"_"+i;
            }
        }
        return lst;
    }

    protected static String genString(Random rand, int len, char[] chr) {
        char[] buf = new char[len];
        for(int i=0; i<len; i++) buf[i] = chr[rand.nextInt(chr.length)];
        return new String(buf);
    }

    public <T> void featureTestAdd(Random rand, T[] vals, IndexedList<T> ilst) throws Exception {
        Set<Integer> add=new HashSet<Integer>();
        long size=0;
        long head=0;
        long[] pos = new long[vals.length];
        while(add.size()<vals.length) {
            int i=rand.nextInt(vals.length);
            if(add.contains(i)) continue;
            add.add(i);
            if(vals[i]==null) {
                pos[i] = -1;
                continue;
            }
            size++;
            long idx=ilst.add(vals[i]);
            if(idx>=head) head=idx+1;
            assertTrue("add("+i+")", idx>=0);
            assertEquals("size()", size, ilst.size());
            assertEquals("head()", head, ilst.head());
            pos[i] = idx;
        }
        assertEquals("size()", size, ilst.size());
        assertEquals("head()", head, ilst.head());
        for(int i=0; i<pos.length; i++) {
            if(pos[i]>=0) assertSame("get(i)",vals[i],ilst.get(pos[i]));
        }
        System.out.println("add(T) OK");
    }

    public <T> void featureTestSet(Random rand, T[] vals, IndexedList<T> ilst) throws Exception {
        Set<Integer> add=new HashSet<Integer>();
        long size=0;
        long head=0;
        while(add.size()<vals.length) {
            int i=rand.nextInt(vals.length);
            if(add.contains(i)) continue;
            add.add(i);
            if(vals[i]==null) {
                continue;
            }
            size++;
            if(i>=head) head=i+1;
            assertEquals("set("+i+")", i, ilst.set(i, vals[i]));
        }
        assertEquals("size()", ilst.size(), size);
        assertEquals("head()", ilst.head(), head);
        Map<Integer,Integer> ref = new HashMap<Integer,Integer>();
        while(ref.size()<size/2) {
            int i=rand.nextInt(vals.length);
            int j=rand.nextInt(vals.length);
            if(ref.containsKey(j)) continue;
            ref.put(j, i);
            if(vals[i]==null) {
                continue;
            }
            if(i>=head) head=i+1;
            assertEquals("set("+j+")", j, ilst.set(j, vals[i]));
        }
        for(Map.Entry<Integer,Integer> e: ref.entrySet()) {
            int j= e.getKey();
            int i= e.getValue();
            if(vals[i]!=null) assertSame("get("+j+")", vals[i], ilst.get(j));
        }
        System.out.println("set(long,T) OK");
    }

    public <T> void featureTestHas(Random rand, T[] vals, IndexedList<T> ilst) throws Exception {
        ilst.clear();
        for(int i=0;i<vals.length;i++) if(vals[i]!=null) ilst.set(i, vals[i]);
        for(int i=0;i<ilst.head();i++) {
            if(vals[i]==null) assertFalse("has("+i+")", ilst.has(i));
            else assertTrue("has("+i+")", ilst.has(i));
        }
        System.out.println("has(long) OK");
    }

    public <T> void featureTestGet(Random rand, T[] vals, IndexedList<T> ilst) throws Exception {
        ilst.clear();
        for(int i=0;i<vals.length;i++) if(vals[i]!=null) ilst.set(i, vals[i]);
        for(int i=0;i<ilst.head();i++) {
            assertEquals("get("+i+")", vals[i], ilst.get(i));
        }
        System.out.println("get(long) OK");
    }

    public <T> void featureTestDel(Random rand, T[] vals, IndexedList<T> ilst) throws Exception {
        ilst.clear();
        for(int i=0;i<vals.length;i++) if(vals[i]!=null) ilst.set(i, vals[i]);
        Set<Integer> del=new HashSet<Integer>();
        while(del.size()<vals.length) {
            int i=rand.nextInt(vals.length);
            if(del.contains(i)) continue;
            if(vals[i]==null) {
                del.add(i);
                assertFalse("has("+i+")", ilst.has(i));
                continue;
            }
            ilst.del(i);
            del.add(i);
            if(ilst.size()%10000==0) {
                for(int j=0;j<ilst.head();j++) {
                    if(del.contains(j)) continue;
                    assertEquals("get("+j+")", vals[j], ilst.get(j));
                }
            }
        }
        assertTrue("isEmpty()", ilst.isEmpty());
        System.out.println("del(long) OK");
    }

    public <T> void featureTestClear(Random rand, T[] vals, IndexedList<T> ilst) throws Exception {
        ilst.clear();
        for(int i=0;i<vals.length;i++) if(vals[i]!=null) ilst.set(i, vals[i]);
        ilst.clear();
        assertTrue("isEmpty()", ilst.isEmpty());
        for(int i=0;i<vals.length;i++) {
            assertFalse("has("+i+")", ilst.has(i));
        }
        assertEquals("size()", 0, ilst.size());
        assertEquals("head()", 0, ilst.head());
        System.out.println("clear() OK");
    }

    public <T> void featureTestFree(Random rand, T[] vals, IndexedList<T> ilst) throws Exception {
        ilst.clear();
        long maxidx=0;
        for(int i=0;i<vals.length;i++) if(vals[i]!=null) {
            long idx = ilst.set(i, vals[i]);
            if(maxidx<idx) maxidx=idx;
        }
        assertFalse("!has(free())",ilst.has(ilst.free()));
        assertTrue("free()<=head()",ilst.free()<=maxidx);
        System.out.println("free() OK");
    }

    public <T> void featureTestIndexes(Random rand, T[] vals, IndexedList<T> ilst) throws Exception {
        ilst.clear();
        for(int i=0;i<vals.length;i++) if(vals[i]!=null) ilst.set(i, vals[i]);
        Index it=ilst.indexes();
        assertFalse("hasPrevious()", it.hasPrevious());
        boolean prev=false;
        for(int i=0;i<vals.length;i++) {
            if(vals[i]!=null) {
                assertTrue(i+": hasNext()", it.hasNext());
                assertEquals(i+": next()", (long) i, it.next());
                if(prev) assertTrue(i+": hasPrevious()", it.hasPrevious());
                else assertFalse(i+": hasPrevious()", it.hasPrevious());
                prev=true;
            }
        }
        assertFalse("hasNext()", it.hasNext());
        System.out.println("indexes() OK");
    }

    public <T> void featureTestIterateIndexes(Random rand, T[] vals, IndexedList<T> ilst) throws Exception {
        ilst.clear();
        for(int i=0;i<vals.length;i++) if(vals[i]!=null) ilst.set(i, vals[i]);
        java.lang.Iterable<Long> it=ilst.iterateIndexes();
        java.util.Iterator<? extends Long> ii=it.iterator();
        for(int i=0;i<vals.length;i++) {
            if(vals[i]!=null) {
                assertTrue(i+": hasNext()", ii.hasNext());
                assertEquals(i+": next()", i, ii.next().intValue());
            }
        }
        assertFalse("hasNext()", ii.hasNext());
        System.out.println("indexes() OK");
    }

    public <T> void featureTestIterator(Random rand, T[] vals, IndexedList<T> ilst) throws Exception {
        ilst.clear();
        for(int i=0;i<vals.length;i++) if(vals[i]!=null) ilst.set(i, vals[i]);
        Iterator<? extends T> it=ilst.iterator();
        for(int i=0;i<vals.length;i++) {
            if(vals[i]!=null) {
                assertTrue(i+": hasNext()", it.hasNext());
                assertEquals(i+": next()", vals[i], it.next());
            }
        }
        assertFalse("hasNext()", it.hasNext());
        System.out.println("iterator() OK");
    }

    public <T> void featureTestVisit(Random rand, T[] vals, IndexedList<T> ilst) throws Exception {
        ilst.clear();
        java.util.Set<T> ref = new java.util.HashSet<T>();
        for(int i=0;i<vals.length;i++) if(vals[i]!=null) {
            ilst.set(i, vals[i]);
            ref.add(vals[i]);
        }
        Visitable.Visitor<T,java.util.Set<T>> vv= new Visitable.Visitor<T,java.util.Set<T>>() {
            @Override
            public long invoke(T obj, java.util.Set<T> ts) {
                return ts.contains(obj)?1:-1;
            }
        };
        assertEquals("size()==visit()", ilst.size(), ilst.visit(vv, ref));
        System.out.println("visit(Visitor) OK");
    }

    public <T> void featureTestVisitIndexed(Random rand, T[] vals, IndexedList<T> ilst) throws Exception {
        ilst.clear();
        java.util.Map<Long,T> ref = new java.util.HashMap<Long,T>();
        for(int i=0;i<vals.length;i++) if(vals[i]!=null) {
            ilst.set(i, vals[i]);
            ref.put((long)i, vals[i]);
        }
        IndexedVisitable.IndexedVisitor<T,java.util.Map<Long,T>> vv= new IndexedVisitable.IndexedVisitor<T,java.util.Map<Long,T>>() {
            @Override
            public long invoke(long idx, T obj, java.util.Map<Long,T> ts) {
                return ts.get(idx)==obj?1:-1;
            }
        };
        assertEquals("size()==visit()",ilst.size(),ilst.visit(vv, ref));
        System.out.println("visit(Visitor) OK");
    }

    public <T> void featureTestIterateIndex(Random rand, T[] vals, IndexedList<T> ilst) throws Exception {
        ilst.clear();
        for(int i=0;i<vals.length;i++) if(vals[i]!=null) ilst.set(i, vals[i]);
        long[] idx = new long[rand.nextInt((int)ilst.size())];
        for(int i=0; i<idx.length; i++) {
            int j = rand.nextInt((int)ilst.head());
            while(!ilst.has(j)) j = rand.nextInt((int)ilst.head());
            idx[i] = j;
        }
        Indexable iin = new Indexable.Enumerate(idx);
        Iterable<? extends T> it=ilst.iterate(iin);
        Index ix = iin.indexes();
        java.util.Iterator<? extends T> ii=it.iterator();
        while(ix.hasNext()) {
            long i = ix.next();
            assertTrue("hasNext()",ii.hasNext());
            assertSame("get(i)==next()",ilst.get(i),ii.next());
        }
        assertFalse("hasNext()", ii.hasNext());
        System.out.println("iterate(Indexable) OK");
    }

    public <T> void featureTestIterateIndexArray(Random rand, T[] vals, IndexedList<T> ilst) throws Exception {
        ilst.clear();
        for(int i=0;i<vals.length;i++) if(vals[i]!=null) ilst.set(i, vals[i]);
        long[] idx = new long[rand.nextInt((int)ilst.size())];
        for(int i=0; i<idx.length; i++) {
            int j = rand.nextInt((int)ilst.head());
            while(!ilst.has(j)) j = rand.nextInt((int)ilst.head());
            idx[i] = j;
        }
        Iterable<? extends T> it=ilst.iterate(idx);
        java.util.Iterator<? extends T> ii=it.iterator();
        for(long i : idx) {
            assertTrue("hasNext()", ii.hasNext());
            assertSame("get(i)==next()", ilst.get(i), ii.next());
        }
        assertFalse("hasNext()", ii.hasNext());
        System.out.println("iterate(long[]) OK");
    }

    public <T> void featureTestIterateIndexIterator(Random rand, T[] vals, IndexedList<T> ilst) throws Exception {
        ilst.clear();
        for(int i=0;i<vals.length;i++) if(vals[i]!=null) ilst.set(i, vals[i]);
        java.util.List<Long> iin = new java.util.ArrayList<Long>();
        int len = rand.nextInt((int)ilst.size());
        while(iin.size()<len) {
            long j = rand.nextInt((int)ilst.head());
            while(!ilst.has(j)) j = rand.nextInt((int)ilst.head());
            iin.add(j);
        }
        Iterable<? extends T> it=ilst.iterate(iin);
        java.util.Iterator<Long> ix = iin.iterator();
        java.util.Iterator<? extends T> ii=it.iterator();
        while(ix.hasNext()) {
            long i = ix.next();
            assertTrue("hasNext()",ii.hasNext());
            assertSame("get(i)==next()",ilst.get(i),ii.next());
        }
        assertFalse("hasNext()", ii.hasNext());
        System.out.println("iterate(Iterable<Long>) OK");
    }

    @SuppressWarnings("unchecked")
    public <T> void featureTestSerialize(Random rand, T[] vals, IndexedList<T> ilst) throws Exception {
        ilst.clear();
        for(int i=0;i<vals.length;i++) if(vals[i]!=null) ilst.set(i, vals[i]);
        File t=null;
        try {
            t=File.createTempFile("serial-", ".ser");
            ObjectOutputStream os=null;
            try {
                os=new ObjectOutputStream(new FileOutputStream(t));
                os.writeObject(ilst);
            }
            finally {
                if(os!=null) os.close();
            }
            ObjectInputStream is=null;
            IndexedList<T> slst=null;
            try {
                is=new ObjectInputStream(new FileInputStream(t));
                slst=(IndexedList<T>) is.readObject();
            }
            finally {
                if(is!=null) is.close();
            }
//            assertTrue("serialize(lst)==lst", ilst.equals(slst));
            assertEquals("size(lst)==lst", ilst.size(),slst.size());
            for(int i=0;i<ilst.head();i++) {
                assertEquals("get("+i+")", vals[i], slst.get(i));
            }
            System.out.println("serial OK");
        }
        finally {
            if(t!=null) t.delete();
        }
    }

    public <T> void featureTestEquals(Random rand, T[] vals, IndexedList<T> ilst, IndexedList<T> eql) throws Exception {
        for(int i=0;i<vals.length;i++) if(vals[i]!=null) ilst.add(vals[i]);
        assertFalse("lst.equals(eql)", ilst.equals(eql));
        for(int i=0;i<vals.length;i++) if(vals[i]!=null) eql.add(vals[i]);
        assertTrue("lst.equals(eql)", ilst.equals(eql));
        assertEquals("lst.hashCode()", ilst.hashCode(), eql.hashCode());
        for(int i=0; i<100; i++) {
            int p = rand.nextInt();
            if(!ilst.has(p)) continue;
            assertFalse("lst.equals(eql)", ilst.equals(eql));
            eql.del(p);
            assertTrue("lst.equals(eql)", ilst.equals(eql));
            assertEquals("lst.hashCode()", ilst.hashCode(), eql.hashCode());
        }
        System.out.println("equals OK");
    }

    public <T,L extends IndexedList<T> & Cloneable> void featureTestClone(Random rand, T[] vals, L ilst) throws Exception {
        for(int i=0;i<vals.length;i++) if(vals[i]!=null) ilst.add(vals[i]);
        IndexedList<T> cln = clone(ilst);
        assertEquals("size(lst)==lst", ilst.size(),cln.size());
        for(int i=0;i<ilst.size();i++) {
            assertEquals("get("+i+")", ilst.get(i), cln.get(i));
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

    public <T> void featureTestString(Random rand, T[] vals, IndexedList<T> ilst) throws Exception {
        ilst.clear();
        assertEquals("[].toString()","[ (null) ]",ilst.toString());
        StringBuilder b = new StringBuilder("[");
        boolean f=true;
        for(int i=0;i<vals.length && i<100;i++) {
            if(vals[i]!=null) {
                long idx= ilst.add(vals[i]);
                if(f) f=false;
                else b.append(",");
                b.append(" ").append(idx).append("@").append(vals[i]);
                assertEquals("toString() ["+i+"]",b.toString()+" (null) ]",ilst.toString());
            }
        }
    }

}
