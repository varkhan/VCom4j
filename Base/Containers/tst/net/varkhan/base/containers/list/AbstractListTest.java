package net.varkhan.base.containers.list;

import junit.framework.TestCase;
import net.varkhan.base.containers.Visitable;

import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;


/**
 * <b></b>.
 * <p/>
 *
 * @author varkhan
 * @date 11/2/13
 * @time 3:47 PM
 */
public abstract class AbstractListTest extends TestCase {

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

    public <T> void featureTestAdd(Random rand, T[] vals, List<T> lst) throws Exception {
        Set<Integer> add=new HashSet<Integer>();
        int size=0;
        int[] pos = new int[vals.length];
        while(add.size()<vals.length) {
            int i=rand.nextInt(vals.length);
            if(add.contains(i)) continue;
            add.add(i);
            if(vals[i]==null) {
                continue;
            }
            pos[size++] = i;
            assertTrue("add("+i+")",lst.add(vals[i]));
            assertEquals("size()", size, lst.size());
        }
        assertEquals("size()", size, lst.size());
        for(int i=0; i<size; i++) {
            assertSame("get(i)",vals[pos[i]],lst.get(i));
        }
        System.out.println("add(T) OK");
    }

    public <T> void featureTestAddI(Random rand, T[] vals, List<T> lst) throws Exception {
        Set<Integer> add=new HashSet<Integer>();
        int size=0;
        int[] pos = new int[vals.length];
        while(add.size()<vals.length) {
            int i=rand.nextInt(vals.length);
            if(add.contains(i)) continue;
            add.add(i);
            if(vals[i]==null) {
                continue;
            }
            int j = rand.nextInt(size+1);
            System.arraycopy(pos,j,pos,j+1,size-j);
            pos[j] = i;
            size++;
            assertTrue("add("+j+","+i+")",lst.add(j,vals[i]));
            assertEquals("size()", size, lst.size());
        }
        assertEquals("size()", size, lst.size());
        for(int i=0; i<size; i++) {
            assertSame("get(i)",vals[pos[i]],lst.get(i));
        }
        System.out.println("add(i,T) OK");
    }

    public <T> void featureTestSet(Random rand, T[] vals, List<T> lst) throws Exception {
        Set<Integer> add=new HashSet<Integer>();
        int size=0;
        int[] pos = new int[vals.length];
        while(add.size()<vals.length) {
            int i=rand.nextInt(vals.length);
            if(add.contains(i)) continue;
            add.add(i);
            if(vals[i]==null) {
                continue;
            }
            pos[size++] = i;
            assertTrue("add("+i+")",lst.add(vals[i]));
            assertEquals("size()", size, lst.size());
        }
        assertEquals("size()", size, lst.size());
        for(int i=0; i<size; i++) {
            assertSame("get(i)",vals[pos[i]],lst.get(i));
        }
        Map<Integer,Integer> ref = new HashMap<Integer,Integer>();
        while(ref.size()<size/2) {
            int i=rand.nextInt(vals.length);
            int j=rand.nextInt(size);
            if(ref.containsKey(j)) continue;
            ref.put(j,i);
            if(vals[i]==null) {
                continue;
            }
            assertTrue("set("+j+")", lst.set(j, vals[i]));
        }
        for(Map.Entry<Integer,Integer> e: ref.entrySet()) {
            int j= e.getKey();
            int i= e.getValue();
            if(vals[i]!=null) assertSame("get("+j+")", vals[i], lst.get(j));
        }
        System.out.println("set(T) OK");
    }

    public <T> void featureTestDel(Random rand, T[] vals, List<T> lst) throws Exception {
        Set<Integer> add=new HashSet<Integer>();
        int size=0;
        int[] pos = new int[vals.length];
        while(add.size()<vals.length) {
            int i=rand.nextInt(vals.length);
            if(add.contains(i)) continue;
            add.add(i);
            if(vals[i]==null) {
                continue;
            }
            pos[size++] = i;
            assertTrue("add("+i+")", lst.add(vals[i]));
            assertEquals("size()", size, lst.size());
        }
        assertEquals("size()", size, lst.size());
        for(int i=0; i<size; i++) {
            assertSame("get(i)",vals[pos[i]],lst.get(i));
        }
        for(int c=0;c<=size/2;c++) {
            int i=rand.nextInt((int)lst.size());
            System.arraycopy(pos,i+1,pos,i,pos.length-i-1);
            size--;
            assertTrue("del("+i+")",lst.del(i));
            assertEquals("size()", size, lst.size());
        }
        assertEquals("size()", size, lst.size());
        for(int i=0; i<size; i++) {
            assertSame("get(i)",vals[pos[i]],lst.get(i));
        }
        System.out.println("del(T) OK");
    }

    public <T> void featureTestClear(Random rand, T[] vals, List<T> lst) throws Exception {
        for(int i=0;i<vals.length;i++) if(vals[i]!=null) lst.add(vals[i]);
        lst.clear();
        assertTrue("isEmpty", lst.isEmpty());
        assertEquals("size()", 0, lst.size());
        System.out.println("clear() OK");
    }

    public <T> void featureTestIterator(Random rand, T[] vals, List<T> lst) throws Exception {
        for(int i=0;i<vals.length;i++) if(vals[i]!=null) lst.add(vals[i]);
        Iterator<? extends T> it=lst.iterator();
        for(int i=0;i<vals.length;i++) {
            if(vals[i]!=null) {
                assertTrue(i+": hasNext()", it.hasNext());
                assertEquals(i+": next()", vals[i], it.next());
            }
        }
        assertFalse("hasNext()", it.hasNext());
        System.out.println("iterator() OK");
    }

    public <T> void featureTestVisit(Random rand, T[] vals, List<T> lst) throws Exception {
        java.util.Set<T> ref = new HashSet<T>();
        for(int i=0;i<vals.length;i++) if(vals[i]!=null) {
            lst.add(vals[i]);
            ref.add(vals[i]);
        }
        Visitable.Visitor<T,java.util.Set<T>> vv= new Visitable.Visitor<T,java.util.Set<T>>() {
            @Override
            public long invoke(T obj, java.util.Set<T> ts) {
                return ts.contains(obj)?1:-1;
            }
        };
        assertEquals("size()==visit()", lst.size(), lst.visit(vv, ref));
        System.out.println("visit(Visitor) OK");
    }

    public <T> void featureTestSublist(Random rand, T[] vals, T ext, List<T> lst, int beg, int end) {
        for(int i=0;i<vals.length;i++) lst.add(vals[i]);
        List<T> sl = lst.sublist(beg, end);
        assertEquals("size()==end-beg",end-beg,sl.size());
        assertEquals("isEmpty() iff beg==end",beg==end,sl.isEmpty());
        assertEquals("hashCode()",lst.sublist(beg, end).hashCode(),sl.hashCode());
        assertEquals("hashCode()",lst.hashCode(),lst.sublist(0, lst.size()).hashCode());
        assertTrue("equals()",lst.sublist(beg, end).equals(sl));
        assertTrue("equals()",lst.sublist(0, lst.size()).equals(lst));
        assertTrue("equals()",lst.equals(lst.sublist(0, lst.size())));
        Iterator<? extends T> it=sl.iterator();
        for(int i=0;i<end-beg;i++) {
//            if(vals[beg+i]!=null) {
                assertTrue(i+": hasNext()", it.hasNext());
                assertEquals(i+": next()", vals[beg+i], it.next());
//            }
        }
        assertFalse("hasNext()", it.hasNext());
        assertTrue("add()",sl.add(ext));
        assertEquals("add()?",ext,lst.get(end));
        assertTrue("add(i)",sl.add(0,ext));
        assertEquals("add(i)?",ext,lst.get(beg));
        System.out.println("sublist() OK");
    }

    public <T> void featureTestSerialize(Random rand, T[] vals, List<T> lst) throws Exception {
        for(int i=0;i<vals.length;i++) if(vals[i]!=null) lst.add(vals[i]);
        File t=null;
        try {
            t=File.createTempFile("serial-", ".ser");
            ObjectOutputStream os=null;
            try {
                os=new ObjectOutputStream(new FileOutputStream(t));
                os.writeObject(lst);
            }
            finally {
                if(os!=null) os.close();
            }
            ObjectInputStream is=null;
            List<T> slst=null;
            try {
                is=new ObjectInputStream(new FileInputStream(t));
                slst=(List<T>) is.readObject();
            }
            finally {
                if(is!=null) is.close();
            }
//            assertTrue("serialize(lst)==lst", lst.equals(slst));
            assertEquals("size(lst)==lst", lst.size(),slst.size());
            for(int i=0;i<lst.size();i++) {
                assertEquals("get("+i+")", lst.get(i), slst.get(i));
            }
            System.out.println("serial OK");
        }
        finally {
            if(t!=null) t.delete();
        }
    }

    public <T> void featureTestEquals(Random rand, T[] vals, List<T> lst, List<T> eql) throws Exception {
        for(int i=0;i<vals.length;i++) if(vals[i]!=null) lst.add(vals[i]);
        assertFalse("lst.equals(eql)", lst.equals(eql));
        for(int i=0;i<vals.length;i++) if(vals[i]!=null) eql.add(vals[i]);
        assertTrue("lst.equals(eql)", lst.equals(eql));
        assertEquals("lst.hashCode()", lst.hashCode(), eql.hashCode());
        for(int i=0; i<100; i++) {
            int p = rand.nextInt();
            if(lst.del(p)) assertFalse("lst.equals(eql)", lst.equals(eql));
            else assertTrue("lst.equals(eql)", lst.equals(eql));
            eql.del(p);
            assertTrue("lst.equals(eql)", lst.equals(eql));
            assertEquals("lst.hashCode()", lst.hashCode(), eql.hashCode());
        }
        System.out.println("equals OK");
    }

    public <T,L extends List<T> & Cloneable> void featureTestClone(Random rand, T[] vals, L lst) throws Exception {
        for(int i=0;i<vals.length;i++) if(vals[i]!=null) lst.add(vals[i]);
        List<T> cln = clone(lst);
        assertEquals("size(lst)==lst", lst.size(),cln.size());
        for(int i=0;i<lst.size();i++) {
            assertEquals("get("+i+")", lst.get(i), cln.get(i));
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