package net.varkhan.base.containers;

import junit.framework.TestCase;

import java.util.Random;


/**
 * <b></b>.
 * <p/>
 *
 * @author varkhan
 * @date 11/1/13
 * @time 7:13 PM
 */
public class HashesTest extends TestCase {

    private static int csb(int b) {
        if(b==0) return 0;
        int c=0;
        while(b!=0) {
            c+=b&0x1;
            b>>>=1;
        }
        return c;
    }

    private static int csb(long b) {
        if(b==0) return 0;
        int c=0;
        while(b!=0) {
            c+=b&0x1L;
            b>>>=1;
        }
        return c;
    }
    public void testMix32() throws Exception {
        Random r = new Random();
        long n = 10000;
        long c = 0;
        for(int i=0; i<n; i++) {
            int p = r.nextInt();
            int m1 = Hashes.mix(p);
            int b = r.nextInt(32);
            int m2 = Hashes.mix(p^(1<<b));
//            System.out.println("mix32: "+p+" "+b+" "+csb(m1^m2));
            c += csb(m1^m2);
        }
        System.out.println("mix32: "+(c/(32.0*n)));
        assertTrue(c>15.5*n);
        assertTrue(c<16.5*n);
    }

    public void testMix64() throws Exception {
        Random r = new Random();
        long n= 10000;
        long c = 0;
        for(int i=0; i<n; i++) {
            long p = r.nextLong();
            long m1 = Hashes.mix(p);
            int b = r.nextInt(64);
            long m2 = Hashes.mix(p^(1<<b));
//            System.out.println("mix64: "+p+" "+b+" "+csb(m1^m2));
            c += csb(m1^m2);
        }
        System.out.println("mix64: "+(c/(64.0*n)));
        assertTrue(c>31.5*n);
        assertTrue(c<32.5*n);
    }

    public void testPrime() throws Exception {
        int len = 1000;
        for(int i=0;i<len;i++) {
            long num = Hashes.prime(i);
            for(int j=0;j<i;j++) {
                long p=Hashes.prime(j);
                assertFalse(""+num+" / "+p,num%p==0);
            }
        }
    }

    @SuppressWarnings({ "UnnecessaryBoxing", "RedundantStringConstructorCall" })
    public void testDefaultHash() throws Exception {
        assertTrue("equals(null,null)",Hashes.ReferenceHashingStrategy.equal(null, null));
        assertEquals("hash(null)==0",0,Hashes.DefaultHashingStrategy.hash(null));
        Integer i10=new Integer(1);
        Integer i11=new Integer(1);
        Integer i20=new Integer(2);
        assertTrue("equals(1,-)",Hashes.DefaultHashingStrategy.equal(i10, i10));
        assertTrue("equals(1,1)",Hashes.DefaultHashingStrategy.equal(i10, i11));
        assertTrue("hash(1)==hash(1)", Hashes.DefaultHashingStrategy.hash(i10)==Hashes.DefaultHashingStrategy.hash(i11));
        assertFalse("!equals(1,2)",Hashes.DefaultHashingStrategy.equal(i10, i20));
        String s10=new String("1");
        String s11=new String("1");
        String s20=new String("2");
        assertTrue("equals(\"1\",-)",Hashes.DefaultHashingStrategy.equal(s10, s10));
        assertTrue("equals(\"1\",\"1\")",Hashes.DefaultHashingStrategy.equal(s10, s11));
        assertTrue("hash(\"1\")==hash(\"1\")", Hashes.DefaultHashingStrategy.hash(s10)==Hashes.DefaultHashingStrategy.hash(s11));
        assertFalse("!equals(\"1\",\"2\")",Hashes.DefaultHashingStrategy.equal(s10, s20));
        class O {
            int i;
            O(int i) { this.i=i; }
            public boolean equals(Object o) {
                if(this==o) return true;
                if(o==null||getClass()!=o.getClass()) return false;
                return !(i==0&&((O) o).i==0)&&i==((O) o).i;
            }
            public int hashCode() {
                return i;
            }
        }
        O o00=new O(0);
        O o01=new O(0);
        O o10=new O(1);
        O o11=new O(1);
        O o20=new O(2);
        assertFalse("equals(#1,null)",Hashes.DefaultHashingStrategy.equal(o10, null));
        assertTrue("equals(#1,-)",Hashes.DefaultHashingStrategy.equal(o10, o10));
        assertTrue("equals(#1,#1)",Hashes.DefaultHashingStrategy.equal(o10, o11));
        assertTrue("hash(#1)==hash(#1)", Hashes.DefaultHashingStrategy.hash(o10)==Hashes.DefaultHashingStrategy.hash(o11));
        assertFalse("!equals(#1,#2)", Hashes.DefaultHashingStrategy.equal(o10, o20));
        assertTrue("equals(#0,-)", Hashes.DefaultHashingStrategy.equal(o00, o00));
        assertFalse("!equals(#0,#0)", Hashes.DefaultHashingStrategy.equal(o00, o01));
        assertTrue("hash(#0)==hash(#0)", Hashes.DefaultHashingStrategy.hash(o00)==Hashes.DefaultHashingStrategy.hash(o01));
    }

    @SuppressWarnings({ "UnnecessaryBoxing", "RedundantStringConstructorCall" })
    public void testReferenceHash() throws Exception {
        assertTrue("equals(null,null)",Hashes.ReferenceHashingStrategy.equal(null, null));
        assertEquals("hash(null)==0",0,Hashes.ReferenceHashingStrategy.hash(null));
        Integer i10=new Integer(1);
        Integer i11=new Integer(1);
        Integer i20=new Integer(2);
        assertTrue("equals(1,-)",Hashes.ReferenceHashingStrategy.equal(i10, i10));
        assertFalse("!equals(1,1)",Hashes.ReferenceHashingStrategy.equal(i10, i11));
        assertFalse("hash(1)!=hash(1)", Hashes.ReferenceHashingStrategy.hash(i10)==Hashes.ReferenceHashingStrategy.hash(i11));
        assertFalse("!equals(1,2)",Hashes.ReferenceHashingStrategy.equal(i10, i20));
        String s10=new String("1");
        String s11=new String("1");
        String s20=new String("2");
        assertTrue("equals(\"1\",-)",Hashes.ReferenceHashingStrategy.equal(s10, s10));
        assertFalse("!equals(\"1\",\"1\")",Hashes.ReferenceHashingStrategy.equal(s10, s11));
        assertFalse("hash(\"1\")!=hash(\"1\")", Hashes.ReferenceHashingStrategy.hash(s10)==Hashes.ReferenceHashingStrategy.hash(s11));
        assertFalse("!equals(\"1\",\"2\")",Hashes.ReferenceHashingStrategy.equal(s10, s20));
        class O {
            int i;
            O(int i) { this.i=i; }
            public boolean equals(Object o) {
                if(this==o) return true;
                if(o==null||getClass()!=o.getClass()) return false;
                return !(i==0&&((O) o).i==0)&&i==((O) o).i;
            }
            public int hashCode() {
                return i;
            }
        }
        O o00=new O(0);
        O o01=new O(0);
        O o10=new O(1);
        O o11=new O(1);
        O o20=new O(2);
        assertFalse("equals(#1,null)",Hashes.ReferenceHashingStrategy.equal(o10, null));
        assertTrue("equals(#1,-)",Hashes.ReferenceHashingStrategy.equal(o10, o10));
        assertFalse("equals(#1,#1)",Hashes.ReferenceHashingStrategy.equal(o10, o11));
        assertFalse("hash(#1)!=hash(#1)", Hashes.ReferenceHashingStrategy.hash(o10)==Hashes.ReferenceHashingStrategy.hash(o11));
        assertFalse("!equals(#1,#2)", Hashes.ReferenceHashingStrategy.equal(o10, o20));
        assertTrue("equals(#0,-)", Hashes.ReferenceHashingStrategy.equal(o00, o00));
        assertFalse("!equals(#0,#0)", Hashes.ReferenceHashingStrategy.equal(o00, o01));
        assertFalse("hash(#0)!=hash(#0)", Hashes.ReferenceHashingStrategy.hash(o00)==Hashes.ReferenceHashingStrategy.hash(o01));
    }

    public void testByteArrayHash() throws Exception {
        assertTrue("equals(null,null)",Hashes.ByteArrayHashingStrategy.equal(null, null));
        assertEquals("hash(null)==0",0,Hashes.ByteArrayHashingStrategy.hash(null));
        byte[] a00 = new byte[]{};
        byte[] a01 = new byte[]{};
        byte[] a10 = new byte[]{1};
        byte[] a11 = new byte[]{1};
        byte[] a12 = new byte[]{2};
        byte[] a60 = new byte[]{1,2,3,4,5,6};
        byte[] a61 = new byte[]{1,2,3,4,5,6};
        byte[] a62 = new byte[]{1,2,3,4,7,6};
        assertFalse("equals([],null)",Hashes.ByteArrayHashingStrategy.equal(a00,null));
        assertTrue("equals([],-)",Hashes.ByteArrayHashingStrategy.equal(a00,a00));
        assertTrue("equals([],[])",Hashes.ByteArrayHashingStrategy.equal(a00,a01));
        assertTrue("hash([])==hash([])",Hashes.ByteArrayHashingStrategy.hash(a00)==Hashes.ByteArrayHashingStrategy.hash(a01));
        assertFalse("!equals([],[1])",Hashes.ByteArrayHashingStrategy.equal(a00,a10));
        assertTrue("equals([1],[1])",Hashes.ByteArrayHashingStrategy.equal(a10,a11));
        assertTrue("hash([1])==hash([1])",Hashes.ByteArrayHashingStrategy.hash(a10)==Hashes.ByteArrayHashingStrategy.hash(a11));
        assertFalse("!equals([1],[2])",Hashes.ByteArrayHashingStrategy.equal(a10,a12));
        assertFalse("hash([1])!=hash([2])",Hashes.ByteArrayHashingStrategy.hash(a10)==Hashes.ByteArrayHashingStrategy.hash(a12));
        assertTrue("equals([1,2,3,4,5,6],[1,2,3,4,5,6])",Hashes.ByteArrayHashingStrategy.equal(a60,a61));
        assertTrue("hash([1,2,3,4,5,6])==hash([1,2,3,4,5,6])",Hashes.ByteArrayHashingStrategy.hash(a60)==Hashes.ByteArrayHashingStrategy.hash(a61));
        assertFalse("!equals([1,2,3,4,5,6],[1,2,3,4,7,6])",Hashes.ByteArrayHashingStrategy.equal(a60,a62));
        assertFalse("hash([1,2,3,4,5,6])!=hash([1,2,3,4,7,6])",Hashes.ByteArrayHashingStrategy.hash(a60)==Hashes.ByteArrayHashingStrategy.hash(a62));
    }

    public void testCharArrayHash() throws Exception {
        assertTrue("equals(null,null)",Hashes.CharArrayHashingStrategy.equal(null, null));
        assertEquals("hash(null)==0",0,Hashes.CharArrayHashingStrategy.hash(null));
        char[] a00 = new char[]{};
        char[] a01 = new char[]{};
        char[] a10 = new char[]{'1'};
        char[] a11 = new char[]{'1'};
        char[] a12 = new char[]{'2'};
        char[] a60 = new char[]{'1','2','3','4','5','6'};
        char[] a61 = new char[]{'1','2','3','4','5','6'};
        char[] a62 = new char[]{'1','2','3','4','7','6'};
        assertFalse("equals([],null)",Hashes.CharArrayHashingStrategy.equal(a00,null));
        assertTrue("equals([],-)",Hashes.CharArrayHashingStrategy.equal(a00,a00));
        assertTrue("equals([],[])",Hashes.CharArrayHashingStrategy.equal(a00,a01));
        assertTrue("hash([])==hash([])",Hashes.CharArrayHashingStrategy.hash(a00)==Hashes.CharArrayHashingStrategy.hash(a01));
        assertFalse("!equals([],[1])",Hashes.CharArrayHashingStrategy.equal(a00,a10));
        assertTrue("equals([1],[1])",Hashes.CharArrayHashingStrategy.equal(a10,a11));
        assertTrue("hash([1])==hash([1])",Hashes.CharArrayHashingStrategy.hash(a10)==Hashes.CharArrayHashingStrategy.hash(a11));
        assertFalse("!equals([1],[2])",Hashes.CharArrayHashingStrategy.equal(a10,a12));
        assertFalse("hash([1])!=hash([2])",Hashes.CharArrayHashingStrategy.hash(a10)==Hashes.CharArrayHashingStrategy.hash(a12));
        assertTrue("equals([1,2,3,4,5,6],[1,2,3,4,5,6])",Hashes.CharArrayHashingStrategy.equal(a60,a61));
        assertTrue("hash([1,2,3,4,5,6])==hash([1,2,3,4,5,6])",Hashes.CharArrayHashingStrategy.hash(a60)==Hashes.CharArrayHashingStrategy.hash(a61));
        assertFalse("!equals([1,2,3,4,5,6],[1,2,3,4,7,6])",Hashes.CharArrayHashingStrategy.equal(a60,a62));
        assertFalse("hash([1,2,3,4,5,6])!=hash([1,2,3,4,7,6])",Hashes.CharArrayHashingStrategy.hash(a60)==Hashes.CharArrayHashingStrategy.hash(a62));
    }

    public void testShortArrayHash() throws Exception {
        assertTrue("equals(null,null)",Hashes.ShortArrayHashingStrategy.equal(null, null));
        assertEquals("hash(null)==0",0,Hashes.ShortArrayHashingStrategy.hash(null));
        short[] a00 = new short[]{};
        short[] a01 = new short[]{};
        short[] a10 = new short[]{1};
        short[] a11 = new short[]{1};
        short[] a12 = new short[]{2};
        short[] a60 = new short[]{1,2,3,4,5,6};
        short[] a61 = new short[]{1,2,3,4,5,6};
        short[] a62 = new short[]{1,2,3,4,7,6};
        assertFalse("equals([],null)",Hashes.ShortArrayHashingStrategy.equal(a00,null));
        assertTrue("equals([],-)",Hashes.ShortArrayHashingStrategy.equal(a00,a00));
        assertTrue("equals([],[])",Hashes.ShortArrayHashingStrategy.equal(a00,a01));
        assertTrue("hash([])==hash([])",Hashes.ShortArrayHashingStrategy.hash(a00)==Hashes.ShortArrayHashingStrategy.hash(a01));
        assertFalse("!equals([],[1])",Hashes.ShortArrayHashingStrategy.equal(a00,a10));
        assertTrue("equals([1],[1])",Hashes.ShortArrayHashingStrategy.equal(a10,a11));
        assertTrue("hash([1])==hash([1])",Hashes.ShortArrayHashingStrategy.hash(a10)==Hashes.ShortArrayHashingStrategy.hash(a11));
        assertFalse("!equals([1],[2])",Hashes.ShortArrayHashingStrategy.equal(a10,a12));
        assertFalse("hash([1])!=hash([2])",Hashes.ShortArrayHashingStrategy.hash(a10)==Hashes.ShortArrayHashingStrategy.hash(a12));
        assertTrue("equals([1,2,3,4,5,6],[1,2,3,4,5,6])",Hashes.ShortArrayHashingStrategy.equal(a60,a61));
        assertTrue("hash([1,2,3,4,5,6])==hash([1,2,3,4,5,6])",Hashes.ShortArrayHashingStrategy.hash(a60)==Hashes.ShortArrayHashingStrategy.hash(a61));
        assertFalse("!equals([1,2,3,4,5,6],[1,2,3,4,7,6])",Hashes.ShortArrayHashingStrategy.equal(a60,a62));
        assertFalse("hash([1,2,3,4,5,6])!=hash([1,2,3,4,7,6])",Hashes.ShortArrayHashingStrategy.hash(a60)==Hashes.ShortArrayHashingStrategy.hash(a62));
    }

    public void testIntArrayHash() throws Exception {
        assertTrue("equals(null,null)",Hashes.IntArrayHashingStrategy.equal(null, null));
        assertEquals("hash(null)==0",0,Hashes.IntArrayHashingStrategy.hash(null));
        int[] a00 = new int[]{};
        int[] a01 = new int[]{};
        int[] a10 = new int[]{1};
        int[] a11 = new int[]{1};
        int[] a12 = new int[]{2};
        int[] a60 = new int[]{1,2,3,4,5,6};
        int[] a61 = new int[]{1,2,3,4,5,6};
        int[] a62 = new int[]{1,2,3,4,7,6};
        assertFalse("equals([],null)",Hashes.IntArrayHashingStrategy.equal(a00,null));
        assertTrue("equals([],-)",Hashes.IntArrayHashingStrategy.equal(a00,a00));
        assertTrue("equals([],[])",Hashes.IntArrayHashingStrategy.equal(a00,a01));
        assertTrue("hash([])==hash([])",Hashes.IntArrayHashingStrategy.hash(a00)==Hashes.IntArrayHashingStrategy.hash(a01));
        assertFalse("!equals([],[1])",Hashes.IntArrayHashingStrategy.equal(a00,a10));
        assertTrue("equals([1],[1])",Hashes.IntArrayHashingStrategy.equal(a10,a11));
        assertTrue("hash([1])==hash([1])",Hashes.IntArrayHashingStrategy.hash(a10)==Hashes.IntArrayHashingStrategy.hash(a11));
        assertFalse("!equals([1],[2])",Hashes.IntArrayHashingStrategy.equal(a10,a12));
        assertFalse("hash([1])!=hash([2])",Hashes.IntArrayHashingStrategy.hash(a10)==Hashes.IntArrayHashingStrategy.hash(a12));
        assertTrue("equals([1,2,3,4,5,6],[1,2,3,4,5,6])",Hashes.IntArrayHashingStrategy.equal(a60,a61));
        assertTrue("hash([1,2,3,4,5,6])==hash([1,2,3,4,5,6])",Hashes.IntArrayHashingStrategy.hash(a60)==Hashes.IntArrayHashingStrategy.hash(a61));
        assertFalse("!equals([1,2,3,4,5,6],[1,2,3,4,7,6])",Hashes.IntArrayHashingStrategy.equal(a60,a62));
        assertFalse("hash([1,2,3,4,5,6])!=hash([1,2,3,4,7,6])",Hashes.IntArrayHashingStrategy.hash(a60)==Hashes.IntArrayHashingStrategy.hash(a62));
    }

    public void testLongArrayHash() throws Exception {
        assertTrue("equals(null,null)",Hashes.LongArrayHashingStrategy.equal(null, null));
        assertEquals("hash(null)==0",0,Hashes.LongArrayHashingStrategy.hash(null));
        long[] a00 = new long[]{};
        long[] a01 = new long[]{};
        long[] a10 = new long[]{1};
        long[] a11 = new long[]{1};
        long[] a12 = new long[]{2};
        long[] a60 = new long[]{1,2,3,4,5,6};
        long[] a61 = new long[]{1,2,3,4,5,6};
        long[] a62 = new long[]{1,2,3,4,7,6};
        assertFalse("equals([],null)",Hashes.LongArrayHashingStrategy.equal(a00,null));
        assertTrue("equals([],-)",Hashes.LongArrayHashingStrategy.equal(a00,a00));
        assertTrue("equals([],[])",Hashes.LongArrayHashingStrategy.equal(a00,a01));
        assertTrue("hash([])==hash([])",Hashes.LongArrayHashingStrategy.hash(a00)==Hashes.LongArrayHashingStrategy.hash(a01));
        assertFalse("!equals([],[1])",Hashes.LongArrayHashingStrategy.equal(a00,a10));
        assertTrue("equals([1],[1])",Hashes.LongArrayHashingStrategy.equal(a10,a11));
        assertTrue("hash([1])==hash([1])",Hashes.LongArrayHashingStrategy.hash(a10)==Hashes.LongArrayHashingStrategy.hash(a11));
        assertFalse("!equals([1],[2])",Hashes.LongArrayHashingStrategy.equal(a10,a12));
        assertFalse("hash([1])!=hash([2])",Hashes.LongArrayHashingStrategy.hash(a10)==Hashes.LongArrayHashingStrategy.hash(a12));
        assertTrue("equals([1,2,3,4,5,6],[1,2,3,4,5,6])",Hashes.LongArrayHashingStrategy.equal(a60,a61));
        assertTrue("hash([1,2,3,4,5,6])==hash([1,2,3,4,5,6])",Hashes.LongArrayHashingStrategy.hash(a60)==Hashes.LongArrayHashingStrategy.hash(a61));
        assertFalse("!equals([1,2,3,4,5,6],[1,2,3,4,7,6])",Hashes.LongArrayHashingStrategy.equal(a60,a62));
        assertFalse("hash([1,2,3,4,5,6])!=hash([1,2,3,4,7,6])",Hashes.LongArrayHashingStrategy.hash(a60)==Hashes.LongArrayHashingStrategy.hash(a62));
    }

    public void testFloatArrayHash() throws Exception {
        assertTrue("equals(null,null)",Hashes.FloatArrayHashingStrategy.equal(null, null));
        assertEquals("hash(null)==0",0,Hashes.FloatArrayHashingStrategy.hash(null));
        float[] a00 = new float[]{};
        float[] a01 = new float[]{};
        float[] a10 = new float[]{1};
        float[] a11 = new float[]{1};
        float[] a12 = new float[]{2};
        float[] a60 = new float[]{1,2,3,4,5,6};
        float[] a61 = new float[]{1,2,3,4,5,6};
        float[] a62 = new float[]{1,2,3,4,7,6};
        assertFalse("equals([],null)",Hashes.FloatArrayHashingStrategy.equal(a00,null));
        assertTrue("equals([],-)",Hashes.FloatArrayHashingStrategy.equal(a00,a00));
        assertTrue("equals([],[])",Hashes.FloatArrayHashingStrategy.equal(a00,a01));
        assertTrue("hash([])==hash([])",Hashes.FloatArrayHashingStrategy.hash(a00)==Hashes.FloatArrayHashingStrategy.hash(a01));
        assertFalse("!equals([],[1])",Hashes.FloatArrayHashingStrategy.equal(a00,a10));
        assertTrue("equals([1],[1])",Hashes.FloatArrayHashingStrategy.equal(a10,a11));
        assertTrue("hash([1])==hash([1])",Hashes.FloatArrayHashingStrategy.hash(a10)==Hashes.FloatArrayHashingStrategy.hash(a11));
        assertFalse("!equals([1],[2])",Hashes.FloatArrayHashingStrategy.equal(a10,a12));
        assertFalse("hash([1])!=hash([2])",Hashes.FloatArrayHashingStrategy.hash(a10)==Hashes.FloatArrayHashingStrategy.hash(a12));
        assertTrue("equals([1,2,3,4,5,6],[1,2,3,4,5,6])",Hashes.FloatArrayHashingStrategy.equal(a60,a61));
        assertTrue("hash([1,2,3,4,5,6])==hash([1,2,3,4,5,6])",Hashes.FloatArrayHashingStrategy.hash(a60)==Hashes.FloatArrayHashingStrategy.hash(a61));
        assertFalse("!equals([1,2,3,4,5,6],[1,2,3,4,7,6])",Hashes.FloatArrayHashingStrategy.equal(a60,a62));
        assertFalse("hash([1,2,3,4,5,6])!=hash([1,2,3,4,7,6])",Hashes.FloatArrayHashingStrategy.hash(a60)==Hashes.FloatArrayHashingStrategy.hash(a62));
    }

    public void testDoubleArrayHash() throws Exception {
        assertTrue("equals(null,null)",Hashes.DoubleArrayHashingStrategy.equal(null, null));
        assertEquals("hash(null)==0",0,Hashes.DoubleArrayHashingStrategy.hash(null));
        double[] a00 = new double[]{};
        double[] a01 = new double[]{};
        double[] a10 = new double[]{1};
        double[] a11 = new double[]{1};
        double[] a12 = new double[]{2};
        double[] a60 = new double[]{1,2,3,4,5,6};
        double[] a61 = new double[]{1,2,3,4,5,6};
        double[] a62 = new double[]{1,2,3,4,7,6};
        assertFalse("equals([],null)",Hashes.DoubleArrayHashingStrategy.equal(a00,null));
        assertTrue("equals([],-)",Hashes.DoubleArrayHashingStrategy.equal(a00,a00));
        assertTrue("equals([],[])",Hashes.DoubleArrayHashingStrategy.equal(a00,a01));
        assertTrue("hash([])==hash([])",Hashes.DoubleArrayHashingStrategy.hash(a00)==Hashes.DoubleArrayHashingStrategy.hash(a01));
        assertFalse("!equals([],[1])",Hashes.DoubleArrayHashingStrategy.equal(a00,a10));
        assertTrue("equals([1],[1])",Hashes.DoubleArrayHashingStrategy.equal(a10,a11));
        assertTrue("hash([1])==hash([1])",Hashes.DoubleArrayHashingStrategy.hash(a10)==Hashes.DoubleArrayHashingStrategy.hash(a11));
        assertFalse("!equals([1],[2])",Hashes.DoubleArrayHashingStrategy.equal(a10,a12));
        assertFalse("hash([1])!=hash([2])",Hashes.DoubleArrayHashingStrategy.hash(a10)==Hashes.DoubleArrayHashingStrategy.hash(a12));
        assertTrue("equals([1,2,3,4,5,6],[1,2,3,4,5,6])",Hashes.DoubleArrayHashingStrategy.equal(a60,a61));
        assertTrue("hash([1,2,3,4,5,6])==hash([1,2,3,4,5,6])",Hashes.DoubleArrayHashingStrategy.hash(a60)==Hashes.DoubleArrayHashingStrategy.hash(a61));
        assertFalse("!equals([1,2,3,4,5,6],[1,2,3,4,7,6])",Hashes.DoubleArrayHashingStrategy.equal(a60,a62));
        assertFalse("hash([1,2,3,4,5,6])!=hash([1,2,3,4,7,6])",Hashes.DoubleArrayHashingStrategy.hash(a60)==Hashes.DoubleArrayHashingStrategy.hash(a62));
    }

    public void testArrayHash() throws Exception {
        HashingStrategy<Object[]> H = Hashes.ObjArrayHashingStrategy(Hashes.DefaultHashingStrategy);
        assertTrue("equals(null,null)",H.equal(null, null));
        assertEquals("hash(null)==0",0,H.hash(null));
        Object[] a00 = new Object[]{};
        Object[] a01 = new Object[]{};
        Object[] a10 = new Object[]{1};
        Object[] a11 = new Object[]{1};
        Object[] a12 = new Object[]{"2"};
        Object[] a60 = new Object[]{1,2,3,4,5,6};
        Object[] a61 = new Object[]{1,2,3,4,5,6};
        Object[] a62 = new Object[]{1,2,3,4,"5",6};
        assertFalse("equals([],null)",H.equal(a00,null));
        assertTrue("equals([],-)",H.equal(a00,a00));
        assertTrue("equals([],[])",H.equal(a00,a01));
        assertTrue("hash([])==hash([])",H.hash(a00)==H.hash(a01));
        assertFalse("!equals([],[1])",H.equal(a00,a10));
        assertTrue("equals([1],[1])",H.equal(a10,a11));
        assertTrue("hash([1])==hash([1])",H.hash(a10)==H.hash(a11));
        assertFalse("!equals([1],[2])",H.equal(a10,a12));
        assertFalse("hash([1])!=hash([2])",H.hash(a10)==H.hash(a12));
        assertTrue("equals([1,2,3,4,5,6],[1,2,3,4,5,6])",H.equal(a60,a61));
        assertTrue("hash([1,2,3,4,5,6])==hash([1,2,3,4,5,6])",H.hash(a60)==H.hash(a61));
        assertFalse("!equals([1,2,3,4,5,6],[1,2,3,4,7,6])",H.equal(a60,a62));
        assertFalse("hash([1,2,3,4,5,6])!=hash([1,2,3,4,7,6])",H.hash(a60)==H.hash(a62));
        HashingStrategy<byte[][]> Ha = Hashes.ObjArrayHashingStrategy(Hashes.ByteArrayHashingStrategy);
        byte[] b10 = new byte[]{1};
        byte[] b11 = new byte[]{1};
        byte[] b12 = new byte[]{2};
        byte[] b60 = new byte[]{1,2,3,4,5,6};
        byte[] b61 = new byte[]{1,2,3,4,5,6};
        byte[] b62 = new byte[]{1,2,3,4,7,6};
        byte[][] bb00 = new byte[][] { };
        byte[][] bb01 = new byte[][] { };
        byte[][] bb10 = new byte[][] { b10 };
        byte[][] bb11 = new byte[][] { b10 };
        byte[][] bb12 = new byte[][] { b11 };
        byte[][] bb13 = new byte[][] { b12 };
        byte[][] bb20 = new byte[][] { b10,b60 };
        byte[][] bb21 = new byte[][] { b10,b61 };
        byte[][] bb22 = new byte[][] { b10,b62 };
        assertTrue(Ha.equal(bb00,bb00));
        assertTrue(Ha.equal(bb00,bb01));
        assertTrue(Ha.hash(bb00)==Ha.hash(bb01));
        assertTrue(Ha.equal(bb10,bb11));
        assertTrue(Ha.hash(bb10)==Ha.hash(bb11));
        assertTrue(Ha.equal(bb10,bb12));
        assertTrue(Ha.hash(bb10)==Ha.hash(bb12));
        assertFalse(Ha.equal(bb10,bb13));
        assertFalse(Ha.hash(bb10)==Ha.hash(bb13));
        assertTrue(Ha.equal(bb20,bb21));
        assertTrue(Ha.hash(bb20)==Ha.hash(bb21));
        assertFalse(Ha.equal(bb20,bb22));
        assertFalse(Ha.hash(bb20)==Ha.hash(bb22));
    }
}
