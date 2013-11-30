/**
 *
 */
package net.varkhan.base.containers.list;

import junit.framework.TestCase;

import java.io.File;
import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Random;


/**
 * @author varkhan
 * @date Mar 23, 2009
 * @time 10:42:42 PM
 */
public class IndexedObjectListBmrk extends TestCase {

    //  long baseseed = new Random().nextLong();
    long baseseed=3553712899943009546L;
    //  long baseseed = -9144058084295631281L;

    public void testArrayIndexedList() {
        benchmarkTest(1000000, 0.5, new ArrayIndexedFloatList());
    }

    public void testBlockIndexedList() {
        benchmarkTest(1000000, 0.5, new BlockIndexedFloatList());
    }

    public void testSparseIndexedList() {
        benchmarkTest(1000000, 0.5, new SparseIndexedFloatList());
    }

    public void testComparisonFull() {
        comparisonTest(1000000, 0.0);
    }

    public void testComparisonSparse() {
        comparisonTest(1000000, 0.9);
    }

    public void testComparisonRarefied() {
        comparisonTest(1000000, 0.9999);
    }

    private static float[] genFloatList(Random rand, int size, double sparsityratio) {
        float[] lst=new float[size];
        for(int i=0;i<size;i++) {
            if(rand.nextFloat()<sparsityratio) lst[i]=0.0f;
            else {
                lst[i]=i;
            }
        }
        return lst;
    }

    private long getSize(Object o) {
        File t=null;
        try {
            t=File.createTempFile("serial-", ".ser");
            ObjectOutputStream os=null;
            try {
                os=new ObjectOutputStream(new FileOutputStream(t));
                os.writeObject(o);
                os.close();
                return t.length();
            }
            finally {
                if(os!=null) os.close();
            }
        }
        catch(Exception e) {
            e.printStackTrace();
        }
        finally {
            if(t!=null) t.delete();
        }
        return -1;
    }

    private void comparisonTest(int size, double sparsityratio) {
        long t0, t1;
        System.err.println("BaseSeed: "+baseseed);
        Random rand=new Random(baseseed);
        t0=System.nanoTime();
        float[] lst=genFloatList(rand, size, sparsityratio);
        t1=System.nanoTime();
        System.out.println("Generated "+lst.length+" floats in "+((t1-t0)/1000000)+"ms, "+(sparsityratio*100)+"% sparse");
        ArrayList<Float> clst=new ArrayList<Float>();
        t0=System.nanoTime();
        for(int i=0;i<lst.length;i++) {
            float s=lst[i];
            clst.add(i, s);
        }
        t1=System.nanoTime();
        System.out.println("ArrayList: "+((t1-t0)/1000000)+"ms for "+lst.length+"elts ("+((t1-t0)/lst.length)+"ns/elt), "+getSize(clst)+"b");

        IndexedFloatList alst=new ArrayIndexedFloatList();
        t0=System.nanoTime();
        for(int i=0;i<lst.length;i++) {
            float s=lst[i];
            if(s!=0) alst.set(i, s);
        }
        t1=System.nanoTime();
        System.out.println("IndexedArrayList: "+((t1-t0)/1000000)+"ms for "+lst.length+"elts ("+((t1-t0)/lst.length)+"ns/elt), "+getSize(alst)+"b");

        IndexedFloatList blst=new BlockIndexedFloatList();
        t0=System.nanoTime();
        for(int i=0;i<lst.length;i++) {
            float s=lst[i];
            if(s!=0) blst.set(i, s);
        }
        t1=System.nanoTime();
        System.out.println("IndexedBlockList: "+((t1-t0)/1000000)+"ms for "+lst.length+"elts ("+((t1-t0)/lst.length)+"ns/elt), "+getSize(alst)+"b");

        IndexedFloatList ilst=new SparseIndexedFloatList(/*13,1.2*/);
        t0=System.nanoTime();
        for(int i=0;i<lst.length;i++) {
            float s=lst[i];
            if(s!=0) ilst.set(i, s);
        }
        t1=System.nanoTime();
        System.out.println("IndexedSparseList: "+((t1-t0)/1000000)+"ms for "+lst.length+"elts ("+((t1-t0)/lst.length)+"ns/elt), "+getSize(ilst)+"b");
    }


    public void benchmarkTest(int size, double sparse, IndexedFloatList ilst) {
        float defVal=ilst.getDefaultValue();
        long t0, t1;
        System.out.println("Benchmark "+ilst.getClass().getSimpleName()+" ["+baseseed+"]");
        Random rand=new Random(baseseed);
        float[] lst=genFloatList(rand, size, sparse);
        t0=System.nanoTime();
        for(int i=0;i<size;i++) {
            float s=lst[i];
            if(s!=defVal) ilst.set(i, s);
        }
        t1=System.nanoTime();
        System.out.println("set: "+((t1-t0)/1000000)+"ms for "+size+"elts ("+((t1-t0)/size)+"ns/elt)");
        t0=System.nanoTime();
        int s=0;
        for(int i=0;i<size;i++) {
            float v=ilst.get(i);
            if(v!=defVal) {
                s+=v;
                if(s<0) s=0;
            }
        }
        t1=System.nanoTime();
        assertTrue(s>=0);
        System.out.println("get: "+((t1-t0)/1000000)+"ms for "+size+"elts ("+((t1-t0)/size)+"ns/elt)");
        t0=System.nanoTime();
        for(int i=0;i<size;i++) {
            ilst.del(i);
        }
        t1=System.nanoTime();
        System.out.println("del: "+((t1-t0)/1000000)+"ms for "+size+"elts ("+((t1-t0)/size)+"ns/elt)");
        System.out.println();
    }

}
