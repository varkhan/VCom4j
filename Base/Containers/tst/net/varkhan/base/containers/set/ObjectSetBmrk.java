/**
 *
 */
package net.varkhan.base.containers.set;

import junit.framework.TestCase;

import java.io.*;
import java.util.Random;


/**
 * @author varkhan
 * @date Feb 26, 2010
 * @time 8:14:25 PM
 */
public class ObjectSetBmrk extends TestCase {
    long seed=1234567890987654321L;

    public void testOpenHashSet() {
        for(int i=0;i<10;i++) benchmarkJavaSet(1000000, new java.util.HashSet<String>(), false);
        benchmarkJavaSet(1000000, new java.util.HashSet<String>(), true);
    }

    public void testArrayOpenHashSet() {
        for(int i=0;i<10;i++) benchmarkSet(1000000, new BlockOpenHashSet<String>(), false);
        benchmarkSet(1000000, new ArrayOpenHashSet<String>(), true);
    }

    public void testBlockOpenHashSet() {
        for(int i=0;i<10;i++) benchmarkSet(1000000, new BlockOpenHashSet<String>(), false);
        benchmarkSet(1000000, new BlockOpenHashSet<String>(), true);
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

    public void benchmarkSet(int num, Set<String> set, boolean pre) {
        if(pre) System.out.println("Bmrk "+set.getClass().getSimpleName());
        String[] a=generateStrings(num, 2, 5, "abcdefghijklmnopqrstuvwxyz".toCharArray());
        boolean ok;
        long t0, t1;
        // Testing key adding
        t0=System.nanoTime();
        ok=true;
        for(int i=0;i<a.length;i++) {
            set.add(a[i]);
        }
        t1=System.nanoTime();
        if(pre) {
            assertTrue("add", ok);
            System.out.println("add() OK in "+((t1-t0)/1000)+"ms ("+((t1-t0)/a.length)+"ns/key)");
        }
        // Testing if keys are present
        t0=System.nanoTime();
        ok=true;
        for(int i=0;i<a.length;i++) {
            String s=a[i];
            ok&=set.has(s);
        }
        t1=System.nanoTime();
        if(pre) {
            assertTrue("has", ok);
            System.out.println("has() OK in "+((t1-t0)/1000)+"ms ("+((t1-t0)/a.length)+"ns/key)");
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
            set.del(a[i]);
        }
        t1=System.nanoTime();
        if(pre) {
            assertTrue("del", ok);
            System.out.println("del() OK in "+((t1-t0)/1000)+"ms ("+((t1-t0)/a.length)+"ns/key)");
        }
        // Testing backward delete (implies auto resize)
        for(int i=0;i<a.length;i++) {
            set.add(a[i]);
        }
        t0=System.nanoTime();
        ok=true;
        for(int i=a.length-1;i>=0;i--) {
            set.del(a[i]);
        }
        t1=System.nanoTime();
        if(pre) {
            assertTrue("del rev", ok);
            System.out.println("del() reverse OK in "+((t1-t0)/1000)+"ms ("+((t1-t0)/a.length)+"ns/key)");
        }
        if(pre) System.out.println();
    }

    public void benchmarkJavaSet(int num, java.util.Set<String> set, boolean pre) {
        if(pre) System.out.println("Bmrk "+set.getClass().getSimpleName());
        String[] a=generateStrings(num, 2, 5, "abcdefghijklmnopqrstuvwxyz".toCharArray());
        boolean ok;
        long t0, t1;
        // Testing key adding
        t0=System.nanoTime();
        ok=true;
        for(int i=0;i<a.length;i++) {
            set.add(a[i]);
        }
        t1=System.nanoTime();
        if(pre) {
            assertTrue("add", ok);
            System.out.println("add() OK in "+((t1-t0)/1000)+"ms ("+((t1-t0)/a.length)+"ns/key)");
        }
        // Testing if keys are present
        t0=System.nanoTime();
        ok=true;
        for(int i=0;i<a.length;i++) {
            String s=a[i];
            ok&=set.contains(s);
        }
        t1=System.nanoTime();
        if(pre) {
            assertTrue("contains", ok);
            System.out.println("contains() OK in "+((t1-t0)/1000)+"ms ("+((t1-t0)/a.length)+"ns/key)");
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
            set.remove(a[i]);
        }
        t1=System.nanoTime();
        if(pre) {
            assertTrue("del", ok);
            System.out.println("del() OK in "+((t1-t0)/1000)+"ms ("+((t1-t0)/a.length)+"ns/key)");
        }
        // Testing backward delete (implies auto resize)
        for(int i=0;i<a.length;i++) {
            set.add(a[i]);
        }
        t0=System.nanoTime();
        ok=true;
        for(int i=a.length-1;i>=0;i--) {
            set.remove(a[i]);
        }
        t1=System.nanoTime();
        if(pre) {
            assertTrue("del rev", ok);
            System.out.println("del() reverse OK in "+((t1-t0)/1000)+"ms ("+((t1-t0)/a.length)+"ns/key)");
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
