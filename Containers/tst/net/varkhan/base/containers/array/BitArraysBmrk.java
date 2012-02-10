package net.varkhan.base.containers.array;

import junit.framework.TestCase;


/**
 * @author varkhan
 * @date Mar 12, 2009
 * @time 6:19:14 PM
 */
public class BitArraysBmrk extends TestCase {

    private static int csb(int b) {
        if(b==0) return 0;
        int c=0;
        while(b!=0) {
            c+=b&0x1;
            b>>>=1;
        }
        return c;
    }

    public void testCsbByte() {
        int n=100000000;
        long t0=System.nanoTime();
        for(int j=0;j<n;j++) {
            for(int i=0;i<256;i++) {
                BitArrays.csb((byte) i);
            }
        }
        long t1=System.nanoTime();
        System.out.println("BitArrays.csb(byte) "+((t1-t0)/(double) n)+"ns");
    }

    public void testCsbShort() {
        int n=1000000;
        long t0=System.nanoTime();
        for(int j=0;j<n;j++) {
            for(int i=0;i<256*256;i++) {
                BitArrays.csb((short) i);
            }
        }
        long t1=System.nanoTime();
        System.out.println("BitArrays.csb(short) "+((t1-t0)/(double) n)+"ns");
    }

    public void testCsbInt() {
        int n=1000000;
        long t0=System.nanoTime();
        for(int j=0;j<n;j++) {
            for(int i=0;i<256*256;i++) {
                BitArrays.csb(i);
            }
        }
        long t1=System.nanoTime();
        System.out.println("BitArrays.csb(int) "+((t1-t0)/(double) n)+"ns");
    }

    public void testCsbLong() {
        int n=1000000;
        long t0=System.nanoTime();
        for(int j=0;j<n;j++) {
            for(int i=0;i<256*256;i++) {
                BitArrays.csb((long) i);
            }
        }
        long t1=System.nanoTime();
        System.out.println("BitArrays.csb(long) "+((t1-t0)/(double) n)+"ns");
    }


    private static int lsb(int b) {
        if(b==0) return 0;
        int c=0;
        while((b>>>c)!=0) c++;
        return c;
    }

    public void testLsbByte() {
        for(int i=0;i<256;i++) {
            assertEquals("lsb("+Integer.toBinaryString(i)+")", lsb(i), BitArrays.lsb((byte) i));
        }
    }

    public void testLsbShort() {
        for(int i=0;i<256*256;i++) {
            assertEquals("lsb("+Integer.toBinaryString(i)+")", lsb(i), BitArrays.lsb((short) i));
        }
    }


    private static int rsb(int b) {
        if(b==0) return -1;
        int c=0;
        while((b&(1<<c))==0) c++;
        return c;
    }

    public void testRsbByte() {
        for(int i=0;i<256;i++) {
            assertEquals("rsb("+Integer.toBinaryString(i)+")", rsb(i), BitArrays.rsb((byte) i));
        }
    }

    public void testRsbShort() {
        for(int i=0;i<256*256;i++) {
            assertEquals("rsb("+Integer.toBinaryString(i)+")", rsb(i), BitArrays.rsb((short) i));
        }
    }

    private static long mp2(int b) {
        long c=0;
        for(int p=0;p<32;p++) {
            // Compute the 2*p bit of c as the p-th bit i
            c|=(b&(1<<p))<<p;
        }
        return c;
    }

    public void testMp2Byte() {
        for(int i=0;i<256;i++) {
            assertEquals("mp2("+Integer.toBinaryString(i)+")", mp2(i), BitArrays.mp2((byte) i));
        }
    }

    public void testMp2Short() {
        for(int i=0;i<256*256;i++) {
            assertEquals("mp2("+Integer.toBinaryString(i)+")", mp2(i), BitArrays.mp2((short) i));
        }
    }

}
