package net.varkhan.base.containers.array;

import junit.framework.TestCase;


/**
 * @author varkhan
 * @date Mar 12, 2009
 * @time 6:19:14 PM
 */
public class BitArraysTest extends TestCase {

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
        for(int i=0;i<256;i++) {
            assertEquals("csb("+Integer.toBinaryString(i)+")", csb(i), BitArrays.csb((byte) i));
        }
        System.out.println("BitArrays.csb(byte) OK");
    }

    public void testCsbShort() {
        for(int i=0;i<256*256;i++) {
            assertEquals("csb("+Integer.toBinaryString(i)+")", csb(i), BitArrays.csb((short) i));
        }
        System.out.println("BitArrays.csb(short) OK");
    }

    public void testCsbInt() {
        for(int i=0;i<256*256;i++) {
            assertEquals("csb("+Integer.toBinaryString(i)+")", csb(i), BitArrays.csb((int) i));
        }
        System.out.println("BitArrays.csb(int) OK");
    }

    public void testCsbLong() {
        for(int i=0;i<256*256;i++) {
            assertEquals("csb("+Integer.toBinaryString(i)+")", csb(i), BitArrays.csb((long) i));
        }
        System.out.println("BitArrays.csb(long) OK");
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
        System.out.println("BitArrays.lsb(byte) OK");
    }

    public void testLsbShort() {
        for(int i=0;i<256*256;i++) {
            assertEquals("lsb("+Integer.toBinaryString(i)+")", lsb(i), BitArrays.lsb((short) i));
        }
        System.out.println("BitArrays.csb(short) OK");
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
        System.out.println("BitArrays.rsb(byte) OK");
    }

    public void testRsbShort() {
        for(int i=0;i<256*256;i++) {
            assertEquals("rsb("+Integer.toBinaryString(i)+")", rsb(i), BitArrays.rsb((short) i));
        }
        System.out.println("BitArrays.rsb(short) OK");
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
        System.out.println("BitArrays.mp2(byte) OK");
    }

    public void testMp2Short() {
        for(int i=0;i<256*256;i++) {
            assertEquals("mp2("+Integer.toBinaryString(i)+")", mp2(i), BitArrays.mp2((short) i));
        }
        System.out.println("BitArrays.mp2(short) OK");
    }

}
