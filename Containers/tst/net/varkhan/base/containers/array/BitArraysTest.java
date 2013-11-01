package net.varkhan.base.containers.array;

import junit.framework.TestCase;


/**
 * @author varkhan
 * @date Mar 12, 2009
 * @time 6:19:14 PM
 */
public class BitArraysTest extends TestCase {

    public static void assertArrayEquals(String message, boolean[] expected, boolean[] actual) {
        if(expected==null) { if(actual==null) return; }
        else if(expected.length==actual.length) {
            boolean same = true;
            for(int i=0; i<expected.length; i++) if(expected[i]!=actual[i]) { same=false; break; }
            if(same) return;
        }
        fail(message+";\n expected: ["+StringArrays.join(",",expected)+"];\n   actual: ["+StringArrays.join(",",actual)+"]");
    }

    public void testFlipBoolean() {
        assertEquals("flip(00000000b,true,true,false,true)","1011",Long.toBinaryString(BitArrays.flip(0,new boolean[]{true,true,false,true})));
        assertEquals("flip(00000000b,true,true,false,true,false)","1011",Long.toBinaryString(BitArrays.flip(0,new boolean[]{true,true,false,true,false})));
        assertEquals("flip(00000010b,true,true,false,true,false)","1001",Long.toBinaryString(BitArrays.flip(2,new boolean[]{true,true,false,true,false})));
    }

    public void testFlipMask() {
        assertArrayEquals("flip(00000000b,true,true,false,true)", new boolean[] { true, true, false, true }, BitArrays.flip(new boolean[] { true, true, false, true }, 0));
        assertArrayEquals("flip(00000000b,true,true,false,true,false)", new boolean[] { true, false, false, true, false }, BitArrays.flip(new boolean[] { true, true, false, true, false }, 2));
    }

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

    public void testLsbInt() {
        for(int i=0;i<256*256;i++) {
            assertEquals("lsb("+Integer.toBinaryString(i)+")", lsb(i), BitArrays.lsb((int) i));
        }
        System.out.println("BitArrays.csb(int) OK");
    }

    public void testLsbLong() {
        for(int i=0;i<256*256;i++) {
            assertEquals("lsb("+Integer.toBinaryString(i)+")", lsb(i), BitArrays.lsb((long) i));
        }
        System.out.println("BitArrays.csb(long) OK");
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

    public void testRsbInt() {
        for(int i=0;i<256*256;i++) {
            assertEquals("rsb("+Integer.toBinaryString(i)+")", rsb(i), BitArrays.rsb((int) i));
        }
        System.out.println("BitArrays.rsb(int) OK");
    }

    public void testRsbLong() {
        for(int i=0;i<256*256;i++) {
            assertEquals("rsb("+Integer.toBinaryString(i)+")", rsb(i), BitArrays.rsb((long) i));
        }
        System.out.println("BitArrays.rsb(long) OK");
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

    public void testMp2Int() {
        for(int i=0;i<256*256;i++) {
            assertEquals("mp2("+Integer.toBinaryString(i)+")", mp2(i), BitArrays.mp2((int) i));
        }
        System.out.println("BitArrays.mp2(int) OK");
    }


    private static long rev(long b, int w) {
        long c=0;
        for(int p=0;p<w;p++) {
            // Compute the (w-p-1)-th bit of c as the p-th bit of b
//            System.out.println(Long.toBinaryString(b)+" => "+p+":"+Long.toBinaryString(((b&(1<<p))==0?0:1)<<(w-p-1)));
            c|=((b&(1L<<p))==0?0L:1L)<<(w-p-1);
        }
        return c;
    }

    public void testRevByte() {
        for(int i=0;i<256;i++) {
            assertEquals("rev("+Integer.toBinaryString(i)+")", rev(i,8), 0xFF&BitArrays.rev((byte) i));
        }
        System.out.println("BitArrays.rev(byte) OK");
    }

    public void testRevShort() {
        for(int i=0;i<256*256;i++) {
            assertEquals("rev("+Integer.toBinaryString(i)+")", rev(i,16), 0xFFFF&BitArrays.rev((short) i));
        }
        System.out.println("BitArrays.rev(byte) OK");
    }

    public void testRevInt() {
        for(int i=0;i<256*256;i++) {
            assertEquals("rev("+Integer.toBinaryString(i)+")", rev(i,32), 0xFFFFFFFFL&BitArrays.rev((int) i));
        }
        System.out.println("BitArrays.rev(byte) OK");
    }

    public void testRevLong() {
        for(int i=0;i<256*256;i++) {
            assertEquals("rev("+Integer.toBinaryString(i)+")", rev((long) i, 64), BitArrays.rev((long) i));
        }
        System.out.println("BitArrays.rev(byte) OK");
    }

}
