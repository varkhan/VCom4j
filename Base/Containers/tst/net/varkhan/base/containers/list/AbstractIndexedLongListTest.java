package net.varkhan.base.containers.list;

import junit.framework.TestCase;
import net.varkhan.base.containers.Index;
import net.varkhan.base.containers.Indexable;
import net.varkhan.base.containers.IndexedVisitable;
import net.varkhan.base.containers.Visitable;
import net.varkhan.base.containers.type.IndexedLongVisitable;
import net.varkhan.base.containers.type.LongIterable;
import net.varkhan.base.containers.type.LongVisitable;

import java.io.*;
import java.util.*;


/**
 * <b></b>.
 * <p/>
 *
 * @author varkhan
 * @date 11/3/13
 * @time 12:36 PM
 */
public abstract class AbstractIndexedLongListTest extends TestCase {

    protected static long[] genLongList(Random rand, int size, double sparsityratio, long defVal) {
        long[] lst=new long[size];
        for(int i=0;i<size;i++) {
            if(rand.nextFloat()<sparsityratio) lst[i]=defVal;
            else {
                lst[i]=rand.nextLong();
            }
        }
        return lst;
    }

    public void featureTestAdd(Random rand, long[] vals, IndexedLongList ilst, long defVal) throws Exception {
        Set<Integer> add=new HashSet<Integer>();
        long size=0;
        long head=0;
        long[] pos = new long[vals.length];
        while(add.size()<vals.length) {
            int i=rand.nextInt(vals.length);
            if(add.contains(i)) continue;
            add.add(i);
            if(vals[i]==defVal) {
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
            if(pos[i]>=0) assertEquals("get(i)", vals[i], ilst.getLong(pos[i]));
        }
        System.out.println("add(T) OK");
    }

    public void featureTestSet(Random rand, long[] vals, IndexedLongList ilst, long defVal) throws Exception {
        Set<Integer> add=new HashSet<Integer>();
        long size=0;
        long head=0;
        while(add.size()<vals.length) {
            int i=rand.nextInt(vals.length);
            if(add.contains(i)) continue;
            add.add(i);
            if(vals[i]==defVal) {
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
            ref.put(j,i);
            if(vals[i]==defVal) {
                continue;
            }
            if(i>=head) head=i+1;
            assertEquals("set("+j+")", j, ilst.set(j, vals[i]));
        }
        for(Map.Entry<Integer,Integer> e: ref.entrySet()) {
            int j= e.getKey();
            int i= e.getValue();
            if(vals[i]!=defVal) assertEquals("get("+j+")", vals[i], ilst.getLong(j));
        }
        System.out.println("set(long,T) OK");
    }

    public void featureTestAddO(Random rand, long[] vals, IndexedLongList ilst, long defVal) throws Exception {
        Set<Integer> add=new HashSet<Integer>();
        long size=0;
        long head=0;
        long[] pos = new long[vals.length];
        while(add.size()<vals.length) {
            int i=rand.nextInt(vals.length);
            if(add.contains(i)) continue;
            add.add(i);
            if(vals[i]==defVal) {
                pos[i] = -1;
                continue;
            }
            size++;
            long idx=ilst.add(Long.valueOf(vals[i]));
            if(idx>=head) head=idx+1;
            assertTrue("add("+i+")", idx>=0);
            assertEquals("size()", size, ilst.size());
            assertEquals("head()", head, ilst.head());
            pos[i] = idx;
        }
        assertEquals("size()", size, ilst.size());
        assertEquals("head()", head, ilst.head());
        for(int i=0; i<pos.length; i++) {
            if(pos[i]>=0) assertEquals("get(i)",Long.valueOf(vals[i]),ilst.get(pos[i]));
        }
        System.out.println("add(T) OK");
    }

    public <T> void featureTestSetO(Random rand, long[] vals, IndexedLongList ilst, long defVal) throws Exception {
        Set<Integer> add=new HashSet<Integer>();
        long size=0;
        long head=0;
        while(add.size()<vals.length) {
            int i=rand.nextInt(vals.length);
            if(add.contains(i)) continue;
            add.add(i);
            if(vals[i]==defVal) {
                continue;
            }
            size++;
            if(i>=head) head=i+1;
            assertEquals("set("+i+")", i, ilst.set(i, Long.valueOf(vals[i])));
        }
        assertEquals("size()", ilst.size(), size);
        assertEquals("head()", ilst.head(), head);
        Map<Integer,Integer> ref = new HashMap<Integer,Integer>();
        while(ref.size()<size/2) {
            int i=rand.nextInt(vals.length);
            int j=rand.nextInt(vals.length);
            if(ref.containsKey(j)) continue;
            ref.put(j, i);
            if(vals[i]==defVal) {
                continue;
            }
            if(i>=head) head=i+1;
            assertEquals("set("+j+")", j, ilst.set(j, vals[i]));
        }
        for(Map.Entry<Integer,Integer> e: ref.entrySet()) {
            int j= e.getKey();
            int i= e.getValue();
            if(vals[i]!=defVal) assertEquals("get("+j+")", Long.valueOf(vals[i]), ilst.get(j));
        }
        System.out.println("set(long,T) OK");
    }

    public void featureTestHas(Random rand, long[] vals, IndexedLongList ilst, long defVal) throws Exception {
        ilst.clear();
        for(int i=0;i<vals.length;i++) if(vals[i]!=defVal) ilst.set(i, vals[i]);
        for(int i=0;i<ilst.head();i++) {
            if(vals[i]==defVal) assertFalse("has("+i+")", ilst.has(i));
            else assertTrue("has("+i+")", ilst.has(i));
        }
        System.out.println("has(long) OK");
    }

    public void featureTestGet(Random rand, long[] vals, IndexedLongList ilst, long defVal) throws Exception {
        ilst.clear();
        for(int i=0;i<vals.length;i++) if(vals[i]!=defVal) ilst.set(i, vals[i]);
        for(int i=0;i<ilst.head();i++) {
            if(vals[i]==defVal) assertEquals("get("+i+")", vals[i], ilst.getLong(i));
            else assertEquals("get("+i+")", vals[i], ilst.getLong(i));
        }
        System.out.println("get(long) OK");
    }

    public void featureTestDel(Random rand, long[] vals, IndexedLongList ilst, long defVal) throws Exception {
        ilst.clear();
        for(int i=0;i<vals.length;i++) if(vals[i]!=defVal) ilst.set(i, vals[i]);
        Set<Integer> del=new HashSet<Integer>();
        while(del.size()<vals.length) {
            int i=rand.nextInt(vals.length);
            if(del.contains(i)) continue;
            if(vals[i]==defVal) {
                del.add(i);
                assertFalse("has("+i+")", ilst.has(i));
                continue;
            }
            ilst.del(i);
            del.add(i);
            if(ilst.size()%10000==0) {
                for(int j=0;j<ilst.head();j++) {
                    if(del.contains(j)) continue;
                    assertEquals("get("+j+")", vals[j], ilst.getLong(j));
                }
            }
        }
        assertTrue("isEmpty()", ilst.isEmpty());
        System.out.println("del(long) OK");
    }

    public void featureTestClear(Random rand, long[] vals, IndexedLongList ilst, long defVal) throws Exception {
        ilst.clear();
        for(int i=0;i<vals.length;i++) if(vals[i]!=defVal) ilst.set(i, vals[i]);
        ilst.clear();
        assertTrue("isEmpty90", ilst.isEmpty());
        for(int i=0;i<vals.length;i++) {
            assertFalse("has("+i+")", ilst.has(i));
        }
        assertEquals("size()", 0, ilst.size());
        assertEquals("head()", 0, ilst.head());
        System.out.println("clear() OK");
    }

    public void featureTestFree(Random rand, long[] vals, IndexedLongList ilst, long defVal) throws Exception {
        ilst.clear();
        long maxidx = 0;
        for(int i=0;i<vals.length;i++) if(vals[i]!=defVal) {
            long idx = ilst.set(i, vals[i]);
            if(maxidx<idx) maxidx=idx;
        }
        assertFalse("!has(free())", ilst.has(ilst.free()));
        assertTrue("free()<=head()", ilst.free()<=maxidx);
        System.out.println("free() OK");
    }

    public void featureTestDefault(Random rand, long[] vals, IndexedLongList ilst, long defVal, boolean set) throws Exception {
        ilst.clear();
        long maxidx = 0;
        for(int i=0;i<vals.length;i++) if(vals[i]!=defVal) {
            long idx = ilst.set(i, vals[i]);
            if(maxidx<idx) maxidx=idx;
        }
        maxidx++;
        assertEquals("getDefault()", defVal, ilst.getDefaultValue());
        assertEquals("get(!)", defVal, ilst.getLong(maxidx));
        if(set) {
            ilst.set(maxidx, defVal);
            assertEquals("get(!)", defVal, ilst.getLong(maxidx));
            assertFalse("has(def)", ilst.has(maxidx));
        }
        ilst.setDefaultValue(defVal+1);
        assertEquals("getDefault()",defVal+1,ilst.getDefaultValue());
        assertEquals("get(!)",defVal+1,ilst.getLong(maxidx));
        System.out.println("default*() OK");
    }

    public void featureTestIndexes(Random rand, long[] vals, IndexedLongList ilst, long defVal) throws Exception {
        ilst.clear();
        for(int i=0;i<vals.length;i++) if(vals[i]!=defVal) ilst.set(i, vals[i]);
        Index it=ilst.indexes();
        assertFalse("hasPrevious()", it.hasPrevious());
        boolean prev=false;
        for(int i=0;i<vals.length;i++) {
            if(vals[i]!=defVal) {
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

    public void featureTestIterateIndexes(Random rand, long[] vals, IndexedLongList ilst, long defVal) throws Exception {
        ilst.clear();
        for(int i=0;i<vals.length;i++) if(vals[i]!=defVal) ilst.set(i, vals[i]);
        Iterable<Long> it=ilst.iterateIndexes();
        Iterator<? extends Long> ii=it.iterator();
        for(int i=0;i<vals.length;i++) {
            if(vals[i]!=defVal) {
                assertTrue(i+": hasNext()", ii.hasNext());
                assertEquals(i+": next()", i, ii.next().intValue());
            }
        }
        assertFalse("hasNext()", ii.hasNext());
        System.out.println("indexes() OK");
    }

    public void featureTestIterator(Random rand, long[] vals, IndexedLongList ilst, long defVal) throws Exception {
        ilst.clear();
        for(int i=0;i<vals.length;i++) if(vals[i]!=defVal) ilst.set(i, vals[i]);
        LongIterable.LongIterator it=ilst.iterator();
        for(int i=0;i<vals.length;i++) {
            if(vals[i]!=defVal) {
                assertTrue(i+": hasNext()", it.hasNext());
                assertEquals(i+": next()", vals[i], it.nextValue());
            }
        }
        assertFalse("hasNext()", it.hasNext());
        System.out.println("iterator() OK");
    }

    public void featureTestVisit(Random rand, long[] vals, IndexedLongList ilst, long defVal) throws Exception {
        ilst.clear();
        Set<Long> ref = new HashSet<Long>();
        for(int i=0;i<vals.length;i++) if(vals[i]!=defVal) {
            ilst.set(i, vals[i]);
            ref.add(vals[i]);
        }
        LongVisitable.LongVisitor<Set<Long>> vv= new LongVisitable.LongVisitor<Set<Long>>() {
            @Override
            public long invoke(long obj, Set<Long> ts) {
                return ts.contains(obj)?1:-1;
            }
        };
        assertEquals("size()==visit()", ilst.size(), ilst.visit(vv, ref));
        Visitable.Visitor<Long,java.util.Set<Long>> v= new Visitable.Visitor<Long,java.util.Set<Long>>() {
            @Override
            public long invoke(Long obj, java.util.Set<Long> ts) {
                return ts.contains(obj)?1:-1;
            }
        };
        assertEquals("size()==visit()", ilst.size(), ilst.visit(v, ref));
        System.out.println("visit(Visitor) OK");
    }

    public void featureTestVisitIndexed(Random rand, long[] vals, IndexedLongList ilst, long defVal) throws Exception {
        ilst.clear();
        Map<Long,Long> ref = new HashMap<Long,Long>();
        for(int i=0;i<vals.length;i++) if(vals[i]!=defVal) {
            ilst.set(i, vals[i]);
            ref.put((long)i, vals[i]);
        }
        IndexedLongVisitable.IndexedLongVisitor<Map<Long,Long>> vv= new IndexedLongVisitable.IndexedLongVisitor<Map<Long,Long>>() {
            @Override
            public long invoke(long idx, long obj, Map<Long,Long> ts) {
                return ts.get(idx)==obj?1:-1;
            }
        };
        assertEquals("size()==visit()",ilst.size(),ilst.visit(vv, ref));
        IndexedVisitable.IndexedVisitor<Long,java.util.Map<Long,Long>> v= new IndexedVisitable.IndexedVisitor<Long,java.util.Map<Long,Long>>() {
            @Override
            public long invoke(long idx, Long obj, java.util.Map<Long,Long> ts) {
                return obj.equals(ts.get(idx))?1:-1;
            }
        };
        assertEquals("size()==visit()",ilst.size(),ilst.visit(v, ref));
        System.out.println("visit(Visitor) OK");
    }

    public void featureTestIterateIndex(Random rand, long[] vals, IndexedLongList ilst, long defVal) throws Exception {
        ilst.clear();
        for(int i=0;i<vals.length;i++) if(vals[i]!=defVal) ilst.set(i, vals[i]);
        long[] idx = new long[rand.nextInt((int)ilst.size())];
        for(int i=0; i<idx.length; i++) {
            int j = rand.nextInt((int)ilst.head());
            while(!ilst.has(j)) j = rand.nextInt((int)ilst.head());
            idx[i] = j;
        }
        Indexable iin = new Indexable.Enumerate(idx);
        Iterable<? extends Long> it=ilst.iterate(iin);
        Index ix = iin.indexes();
        Iterator<? extends Long> ii=it.iterator();
        while(ix.hasNext()) {
            long i = ix.next();
            assertTrue("hasNext()",ii.hasNext());
            assertEquals("get(i)==next()", ilst.get(i), ii.next());
        }
        assertFalse("hasNext()", ii.hasNext());
        System.out.println("iterate(Indexable) OK");
    }

    public void featureTestIterateIndexArray(Random rand, long[] vals, IndexedLongList ilst, long defVal) throws Exception {
        ilst.clear();
        for(int i=0;i<vals.length;i++) if(vals[i]!=defVal) ilst.set(i, vals[i]);
        long[] idx = new long[rand.nextInt((int)ilst.size())];
        for(int i=0; i<idx.length; i++) {
            int j = rand.nextInt((int)ilst.head());
            while(!ilst.has(j)) j = rand.nextInt((int)ilst.head());
            idx[i] = j;
        }
        LongIterable it=ilst.iterate(idx);
        LongIterable.LongIterator ii=it.iterator();
        for(long i : idx) {
            assertTrue("hasNext()", ii.hasNext());
            assertEquals("get(i)==next()", ilst.getLong(i), ii.nextValue());
        }
        assertFalse("hasNext()", ii.hasNext());
        System.out.println("iterate(long[]) OK");
    }

    public void featureTestIterateIndexIterator(Random rand, long[] vals, IndexedLongList ilst, long defVal) throws Exception {
        ilst.clear();
        for(int i=0;i<vals.length;i++) if(vals[i]!=defVal) ilst.set(i, vals[i]);
        java.util.List<Long> iin = new java.util.ArrayList<Long>();
        int len = rand.nextInt((int)ilst.size());
        while(iin.size()<len) {
            long j = rand.nextInt((int)ilst.head());
            while(!ilst.has(j)) j = rand.nextInt((int)ilst.head());
            iin.add(j);
        }
        LongIterable it=ilst.iterate(iin);
        Iterator<Long> ix = iin.iterator();
        LongIterable.LongIterator ii=it.iterator();
        while(ix.hasNext()) {
            long i = ix.next();
            assertTrue("hasNext()",ii.hasNext());
            assertEquals("get(i)==next()", ilst.getLong(i), ii.nextValue());
        }
        assertFalse("hasNext()", ii.hasNext());
        System.out.println("iterate(Iterable<Long>) OK");
    }

    @SuppressWarnings("unchecked")
    public void featureTestSerialize(Random rand, long[] vals, IndexedLongList ilst, long defVal) throws Exception {
        ilst.clear();
        for(int i=0;i<vals.length;i++) if(vals[i]!=defVal) ilst.set(i, vals[i]);
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
            IndexedLongList slst=null;
            try {
                is=new ObjectInputStream(new FileInputStream(t));
                slst=(IndexedLongList) is.readObject();
            }
            finally {
                if(is!=null) is.close();
            }
//            assertTrue("serialize(lst)==lst", ilst.equals(slst));
            assertEquals("size(lst)==lst", ilst.size(),slst.size());
            for(int i=0;i<ilst.head();i++) {
                assertEquals("get("+i+")", vals[i], slst.getLong(i));
            }
            System.out.println("serial OK");
        }
        finally {
            if(t!=null) t.delete();
        }
    }

    public void featureTestEquals(Random rand, long[] vals, IndexedLongList ilst, IndexedLongList eql, float defVal) throws Exception {
        for(int i=0;i<vals.length;i++) if(vals[i]!=defVal) ilst.set(i, vals[i]);
        assertFalse("lst.equals(eql)", ilst.equals(eql));
        for(int i=0;i<vals.length;i++) if(vals[i]!=defVal) eql.set(i, vals[i]);
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

    public <L extends IndexedLongList & Cloneable> void featureTestClone(Random rand, long[] vals, L ilst, float defVal) throws Exception {
        for(int i=0;i<vals.length;i++) if(vals[i]!=defVal) ilst.set(i, vals[i]);
        IndexedLongList cln = clone(ilst);
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

}
