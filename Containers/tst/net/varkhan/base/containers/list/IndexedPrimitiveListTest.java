/**
 *
 */
package net.varkhan.base.containers.list;

import junit.framework.TestCase;
import net.varkhan.base.containers.Index;
import net.varkhan.base.containers.Iterator;
import net.varkhan.base.containers.type.IntIterable;

import java.io.*;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;


/**
 * @author varkhan
 * @date Mar 12, 2009
 * @time 6:20:13 PM
 */
public class IndexedPrimitiveListTest extends TestCase {

    //  long baseseed = new Random().nextLong();
    long baseseed=3553712899943009546L;
    //  long baseseed = -9144058084295631281L;


    public void testSparseIndexedList() throws Exception {
        featureTest(1000000, 0.9, new SparseIndexedIntList());
    }

    public void testBlockIndexedList() throws Exception {
        featureTest(1000000, 0.9, new BlockIndexedIntList());
    }

    public void testArrayIndexedList() throws Exception {
        featureTest(1000000, 0.9, new ArrayIndexedIntList());
    }

    public void featureTest(int size, double sparse, IndexedIntList ilst) throws Exception {
        Random rand=new Random(baseseed);
        int[] vals=genIntegerList(rand, size, sparse);
        System.out.println("Test "+ilst.getClass().getSimpleName()+" ["+baseseed+"]");
        featureTestSet(rand, vals, ilst);
        featureTestHas(rand, vals, ilst);
        featureTestGet(rand, vals, ilst);
        featureTestDel(rand, vals, ilst);
        featureTestClear(rand, vals, ilst);
        featureTestIndexes(rand, vals, ilst);
        featureTestIterate(rand, vals, ilst);
        featureTestSerialize(rand, vals, ilst);
        System.out.println();
    }

    private static int[] genIntegerList(Random rand, int size, double sparsityratio) {
        int[] lst=new int[size];
        for(int i=0;i<size;i++) {
            if(rand.nextFloat()<sparsityratio) lst[i]=0;
            else {
                lst[i]=i;
            }
        }
        return lst;
    }

    public void featureTestSet(Random rand, int[] vals, IndexedIntList ilst) throws Exception {
        Set<Integer> add=new HashSet<Integer>();
        int size=0;
        int head=0;
        while(add.size()<vals.length) {
            int i=rand.nextInt(vals.length);
            if(add.contains(i)) continue;
            add.add(i);
            if(vals[i]==0) {
                continue;
            }
            size++;
            if(i>=head) head=i+1;
            assertEquals("set("+i+")", i, ilst.set(i, vals[i]));
        }
        assertEquals("size()", ilst.size(), size);
        assertEquals("head()", ilst.head(), head);
        System.out.println("set(long,T) OK");
    }

    public void featureTestHas(Random rand, int[] vals, IndexedIntList ilst) throws Exception {
        ilst.clear();
        for(int i=0;i<vals.length;i++) if(vals[i]!=0) ilst.set(i, vals[i]);
        for(int i=0;i<ilst.head();i++) {
            if(vals[i]==0) assertFalse("has("+i+")", ilst.has(i));
            else assertTrue("has("+i+")", ilst.has(i));
        }
        System.out.println("has(long) OK");
    }

    public void featureTestGet(Random rand, int[] vals, IndexedIntList ilst) throws Exception {
        ilst.clear();
        for(int i=0;i<vals.length;i++) if(vals[i]!=0) ilst.set(i, vals[i]);
        for(int i=0;i<ilst.head();i++) {
            assertEquals("get("+i+")", vals[i], ilst.getInt(i));
        }
        System.out.println("get(long) OK");
    }

    public void featureTestDel(Random rand, int[] vals, IndexedIntList ilst) throws Exception {
        ilst.clear();
        for(int i=0;i<vals.length;i++) if(vals[i]!=0) ilst.set(i, vals[i]);
        Set<Integer> del=new HashSet<Integer>();
        while(del.size()<vals.length) {
            int i=rand.nextInt(vals.length);
            if(del.contains(i)) continue;
            if(vals[i]==0) {
                del.add(i);
                assertFalse("has("+i+")", ilst.has(i));
                continue;
            }
            ilst.del(i);
            del.add(i);
            if(ilst.size()%10000==0) {
                for(int j=0;j<ilst.head();j++) {
                    if(del.contains(j)) continue;
                    assertEquals("get("+j+")", vals[j], ilst.getInt(j));
                }
            }
        }
        assertTrue("isEmpty()", ilst.isEmpty());
        System.out.println("del(long) OK");
    }

    @SuppressWarnings("unchecked")
    public void featureTestSerialize(Random rand, int[] vals, IndexedIntList ilst) throws Exception {
        ilst.clear();
        for(int i=0;i<vals.length;i++) if(vals[i]!=0) ilst.set(i, vals[i]);
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
            IndexedIntList slst=null;
            try {
                is=new ObjectInputStream(new FileInputStream(t));
                slst=(IndexedIntList) is.readObject();
            }
            finally {
                if(is!=null) is.close();
            }
            assertTrue("serialize(lst)==lst", ilst.equals(slst));
            for(int i=0;i<ilst.head();i++) {
                assertEquals("get("+i+")", vals[i], slst.getInt(i));
            }
            System.out.println("serial OK");
        }
        finally {
            if(t!=null) t.delete();
        }
    }

    public void featureTestClear(Random rand, int[] vals, IndexedIntList ilst) throws Exception {
        ilst.clear();
        for(int i=0;i<vals.length;i++) if(vals[i]!=0) ilst.set(i, vals[i]);
        ilst.clear();
        assertTrue("isEmpty", ilst.isEmpty());
        for(int i=0;i<vals.length;i++) {
            assertFalse("has("+i+")", ilst.has(i));
        }
        System.out.println("clear() OK");
    }

    public void featureTestIndexes(Random rand, int[] vals, IndexedIntList ilst) throws Exception {
        ilst.clear();
        for(int i=0;i<vals.length;i++) if(vals[i]!=0) ilst.set(i, vals[i]);
        Index it=ilst.indexes();
        assertFalse("hasPrevious()", it.hasPrevious());
        boolean prev=false;
        for(int i=0;i<vals.length;i++) {
            if(vals[i]!=0) {
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

    public void featureTestIterate(Random rand, int[] vals, IndexedIntList ilst) throws Exception {
        ilst.clear();
        for(int i=0;i<vals.length;i++) if(vals[i]!=0) ilst.set(i, vals[i]);
        @SuppressWarnings("unchecked")
        IntIterable.IntIterator it= ilst.iterator();
        for(int i=0;i<vals.length;i++) {
            if(vals[i]!=0) {
                assertTrue(i+": hasNext()", it.hasNext());
                assertEquals(i+": next()", vals[i], it.nextValue());
            }
        }
        assertFalse("hasNext()", it.hasNext());
        System.out.println("iterate() OK");
    }

}
