/**
 *
 */
package net.varkhan.base.containers.set;

import junit.framework.TestCase;

import java.io.*;
import java.util.Random;


/**
 * @author varkhan
 * @date May 21, 2009
 * @time 12:13:22 AM
 */
public class IndexedObjectSetBmrk extends TestCase {
    long seed=1234567890987654321L;

    public void testArrayOpenHashIndexedSet() {
        for(int i=0;i<10;i++) benchmarkIndexedSet(1000000, new ArrayOpenHashIndexedSet<String>(), false);
        benchmarkIndexedSet(1000000, new ArrayOpenHashIndexedSet<String>(), true);
    }

    public void testAbstractArrayOpenHashIndexedSet() {
        for(int i=0;i<10;i++)
            benchmarkIndexedSet(1000000, new AbstractArrayOpenHashIndexedSet<String>() {
                private static final long serialVersionUID=1L;
            }, false);
        benchmarkIndexedSet(1000000, new AbstractArrayOpenHashIndexedSet<String>() {
            private static final long serialVersionUID=1L;
        }, true);
    }

    public void testBlockOpenHashIndexedSet() {
        for(int i=0;i<10;i++) benchmarkIndexedSet(1000000, new BlockOpenHashIndexedSet<String>(), false);
        benchmarkIndexedSet(1000000, new BlockOpenHashIndexedSet<String>(), true);
    }

    public void testAbstractBlockOpenHashIndexedSet() {
        for(int i=0;i<10;i++)
            benchmarkIndexedSet(1000000, new AbstractBlockOpenHashIndexedSet<String>() {
                private static final long serialVersionUID=1L;
            }, false);
        benchmarkIndexedSet(1000000, new AbstractBlockOpenHashIndexedSet<String>() {
            private static final long serialVersionUID=1L;
        }, true);
    }

    private String[] generateStrings(int num, int minl, int maxl, char[] characters) {
        Random rand=new Random(seed);
        String[] a=new String[num];
        for(int i=0;i<num;i++) {
            StringBuilder buf=new StringBuilder();
            int len=minl+rand.nextInt(maxl-minl);
            for(int j=0;j<len;j++) buf.append(characters[rand.nextInt(characters.length)]);
            a[i]=buf.toString();
        }
        return a;
    }

    public void benchmarkIndexedSet(int num, IndexedSet<String> set, boolean pre) {
        if(pre) System.out.println("Bmrk "+set.getClass().getSimpleName());
        String[] a=generateStrings(num, 2, 5, "abcdefghijklmnopqrstuvwxyz".toCharArray());
        long[] x=new long[a.length];
        boolean ok;
        long t0, t1;
        // Testing key adding
        t0=System.nanoTime();
        ok=true;
        for(int i=0;i<a.length;i++) {
            String s=a[i];
            long idx=set.add(s);
            x[i]=idx;
            ok&=idx>=0;
        }
        t1=System.nanoTime();
        if(pre) {
            assertTrue("add", ok);
            System.out.println("add() in "+((t1-t0)/1000)+"ms ("+((t1-t0)/a.length)+"ns/key)");
        }
        // Testing if indexes can be retrieved
        t0=System.nanoTime();
        ok=true;
        for(int i=0;i<a.length;i++) {
            String s=a[i];
            long idx=set.index(s);
            ok&=idx==x[i];
        }
        t1=System.nanoTime();
        if(pre) {
            assertTrue("index", ok);
            System.out.println("index() in "+((t1-t0)/1000)+"ms ("+((t1-t0)/a.length)+"ns/key)");
        }
        // Get serial size
        if(pre) {
            long s=getSerialSize(set);
            System.out.println("Size is "+s+" bytes ("+(s/(double)a.length)+" b/key)");
        }
        // Testing forward delete (no resize)
        t0=System.nanoTime();
        ok=true;
        for(int i=0;i<a.length;i++) {
            long idx=x[i];
            set.del(idx);
        }
        t1=System.nanoTime();
        if(pre) {
            assertTrue("del", ok);
            System.out.println("del() in "+((t1-t0)/1000)+"ms ("+((t1-t0)/a.length)+"ns/key)");
        }
        // Testing backward delete (implies auto resize)
        for(int i=0;i<a.length;i++) {
            x[i]=set.add(a[i]);
        }
        t0=System.nanoTime();
        ok=true;
        for(int i=a.length-1;i>=0;i--) {
            long idx=x[i];
            set.del(idx);
        }
        t1=System.nanoTime();
        if(pre) {
            assertTrue("del rev", ok);
            System.out.println("del() reverse in "+((t1-t0)/1000)+"ms ("+((t1-t0)/a.length)+"ns/key)");
        }
        if(pre) System.out.println();
    }

    private long getSerialSize(Object o) {
        if(!(o instanceof Serializable)) return -1;
        File t;
        try { t=File.createTempFile("/tmp", ".ser"); } catch(IOException e) { return -1; }
        ObjectOutputStream os=null;
        try {
            os=new ObjectOutputStream(new FileOutputStream(t));
            os.writeObject(o);
            return t.length();
        }
        catch(NotSerializableException e) {
            return -1;
        }
        catch(Exception e) {
            e.printStackTrace();
            return -1;
        }
        finally {
            if(os!=null) try {os.close();} catch(Throwable e) {}
            t.delete();
        }
    }

}
