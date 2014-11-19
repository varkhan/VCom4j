package net.varkhan.base.conversion.serializer.primitives;

import junit.framework.TestCase;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.Random;


/**
 * <b></b>.
 * <p/>
 *
 * @author varkhan
 * @date 2/6/11
 * @time 5:43 AM
 */
public class PrimitiveArraySerializerTest extends TestCase {

    public void testBooleanIS() {
        boolean[][] vals=new boolean[1000][];
        Random rand=new Random();
        for(int i=0;i<vals.length;i++) {
            vals[i]=new boolean[rand.nextInt(200)];
            for(int j=0;j<vals[i].length;j++) vals[i][j]=rand.nextBoolean();
        }
        BooleanArraySerializer<Object> s=new BooleanArraySerializer<Object>();
        ByteArrayOutputStream os=new ByteArrayOutputStream();
        for(boolean[] val : vals) s.encode(val, os, null);
        InputStream is=new ByteArrayInputStream(os.toByteArray());
        for(boolean[] val : vals) assertArrayEquals(""+Arrays.toString(val), val, s.decode(is, null));
    }

    public void testBooleanBB() {
        boolean[][] vals=new boolean[1000][];
        Random rand=new Random();
        for(int i=0;i<vals.length;i++) {
            vals[i]=new boolean[rand.nextInt(200)];
            for(int j=0;j<vals[i].length;j++) vals[i][j]=rand.nextBoolean();
        }
        BooleanArraySerializer<Object> s=new BooleanArraySerializer<Object>();
        ByteBuffer bb = ByteBuffer.allocate(10+200*vals.length);
        for(boolean[] val : vals) s.encode(val, bb, null);
        bb.flip();
        for(boolean[] val : vals) assertArrayEquals(""+Arrays.toString(val), val, s.decode(bb, null));
    }

    public void testBooleanAR() {
        boolean[][] vals=new boolean[1000][];
        Random rand=new Random();
        for(int i=0;i<vals.length;i++) {
            vals[i]=new boolean[rand.nextInt(200)];
            for(int j=0;j<vals[i].length;j++) vals[i][j]=rand.nextBoolean();
        }
        BooleanArraySerializer<Object> s=new BooleanArraySerializer<Object>();
        byte[] ar = new byte[10+200*vals.length];
        long pos = 0;
        for(boolean[] val : vals) pos += s.encode(val, ar, pos, ar.length-pos, null);
        long len = pos;
        pos = 0;
        for(boolean[] val : vals) {
            boolean[] v=s.decode(ar, pos, len-pos, null);
            pos += s.length(v, null);
            assertArrayEquals(""+Arrays.toString(val), val, v);
        }
    }


    public void testByteIS() {
        byte[][] vals=new byte[1000][];
        Random rand=new Random();
        for(int i=0;i<vals.length;i++) {
            vals[i]=new byte[rand.nextInt(200)];
            for(int j=0;j<vals[i].length;j++) vals[i][j]=(byte) rand.nextInt(Byte.MAX_VALUE);
        }
        ByteArraySerializer<Object> s=new ByteArraySerializer<Object>();
        ByteArrayOutputStream os=new ByteArrayOutputStream();
        for(byte[] val : vals) s.encode(val, os, null);
        InputStream is=new ByteArrayInputStream(os.toByteArray());
        for(byte[] val : vals) assertArrayEquals(""+Arrays.toString(val), val, s.decode(is, null));
    }

    public void testByteBB() {
        byte[][] vals=new byte[1000][];
        Random rand=new Random();
        for(int i=0;i<vals.length;i++) {
            vals[i]=new byte[rand.nextInt(200)];
            for(int j=0;j<vals[i].length;j++) vals[i][j]=(byte) rand.nextInt(Byte.MAX_VALUE);
        }
        ByteArraySerializer<Object> s=new ByteArraySerializer<Object>();
        ByteBuffer bb = ByteBuffer.allocate(10+200*vals.length);
        for(byte[] val : vals) s.encode(val, bb, null);
        bb.flip();
        for(byte[] val : vals) assertArrayEquals(""+Arrays.toString(val), val, s.decode(bb, null));
    }

    public void testByteAR() {
        byte[][] vals=new byte[1000][];
        Random rand=new Random();
        for(int i=0;i<vals.length;i++) {
            vals[i]=new byte[rand.nextInt(200)];
            for(int j=0;j<vals[i].length;j++) vals[i][j]=(byte) rand.nextInt(Byte.MAX_VALUE);
        }
        ByteArraySerializer<Object> s=new ByteArraySerializer<Object>();
        byte[] ar = new byte[10+200*vals.length];
        long pos = 0;
        for(byte[] val : vals) pos += s.encode(val, ar, pos, ar.length-pos, null);
        long len = pos;
        pos = 0;
        for(byte[] val : vals) {
            byte[] v=s.decode(ar, pos, len-pos, null);
            pos += s.length(v, null);
            assertArrayEquals(""+Arrays.toString(val), val, v);
        }
    }


    public void testShortIS() {
        short[][] vals=new short[1000][];
        Random rand=new Random();
        for(int i=0;i<vals.length;i++) {
            vals[i]=new short[rand.nextInt(200)];
            for(int j=0;j<vals[i].length;j++) vals[i][j]=(short) rand.nextInt(Short.MAX_VALUE);
        }
        ShortArraySerializer<Object> s=new ShortArraySerializer<Object>();
        ByteArrayOutputStream os=new ByteArrayOutputStream();
        for(short[] val : vals) s.encode(val, os, null);
        InputStream is=new ByteArrayInputStream(os.toByteArray());
        for(short[] val : vals) assertArrayEquals(""+Arrays.toString(val), val, s.decode(is, null));
    }

    public void testShortBB() {
        short[][] vals=new short[1000][];
        Random rand=new Random();
        for(int i=0;i<vals.length;i++) {
            vals[i]=new short[rand.nextInt(200)];
            for(int j=0;j<vals[i].length;j++) vals[i][j]=(short) rand.nextInt(Short.MAX_VALUE);
        }
        ShortArraySerializer<Object> s=new ShortArraySerializer<Object>();
        ByteBuffer bb = ByteBuffer.allocate(10+2*200*vals.length);
        for(short[] val : vals) s.encode(val, bb, null);
        bb.flip();
        for(short[] val : vals) assertArrayEquals(""+Arrays.toString(val), val, s.decode(bb, null));
    }

    public void testShortAR() {
        short[][] vals=new short[1000][];
        Random rand=new Random();
        for(int i=0;i<vals.length;i++) {
            vals[i]=new short[rand.nextInt(200)];
            for(int j=0;j<vals[i].length;j++) vals[i][j]=(short) rand.nextInt(Short.MAX_VALUE);
        }
        ShortArraySerializer<Object> s=new ShortArraySerializer<Object>();
        byte[] ar = new byte[10+2*200*vals.length];
        long pos = 0;
        for(short[] val : vals) pos += s.encode(val, ar, pos, ar.length-pos, null);
        long len = pos;
        pos = 0;
        for(short[] val : vals) {
            short[] v=s.decode(ar, pos, len-pos, null);
            pos += s.length(v, null);
            assertArrayEquals(""+Arrays.toString(val), val, v);
        }
    }


    public void testIntIS() {
        int[][] vals=new int[1000][];
        Random rand=new Random();
        for(int i=0;i<vals.length;i++) {
            vals[i]=new int[rand.nextInt(200)];
            for(int j=0;j<vals[i].length;j++) vals[i][j]=rand.nextInt();
        }
        IntArraySerializer<Object> s=new IntArraySerializer<Object>();
        ByteArrayOutputStream os=new ByteArrayOutputStream();
        for(int[] val : vals) s.encode(val, os, null);
        InputStream is=new ByteArrayInputStream(os.toByteArray());
        for(int[] val : vals) assertArrayEquals(""+Arrays.toString(val), val, s.decode(is, null));
    }

    public void testIntBB() {
        int[][] vals=new int[1000][];
        Random rand=new Random();
        for(int i=0;i<vals.length;i++) {
            vals[i]=new int[rand.nextInt(200)];
            for(int j=0;j<vals[i].length;j++) vals[i][j]=rand.nextInt();
        }
        IntArraySerializer<Object> s=new IntArraySerializer<Object>();
        ByteBuffer bb = ByteBuffer.allocate(10+4*200*vals.length);
        for(int[] val : vals) s.encode(val, bb, null);
        bb.flip();
        for(int[] val : vals) assertArrayEquals(""+Arrays.toString(val), val, s.decode(bb, null));
    }

    public void testIntAR() {
        int[][] vals=new int[1000][];
        Random rand=new Random();
        for(int i=0;i<vals.length;i++) {
            vals[i]=new int[rand.nextInt(200)];
            for(int j=0;j<vals[i].length;j++) vals[i][j]=rand.nextInt();
        }
        IntArraySerializer<Object> s=new IntArraySerializer<Object>();
        byte[] ar = new byte[10+4*200*vals.length];
        long pos = 0;
        for(int[] val : vals) pos += s.encode(val, ar, pos, ar.length-pos, null);
        long len = pos;
        pos = 0;
        for(int[] val : vals) {
            int[] v=s.decode(ar, pos, len-pos, null);
            pos += s.length(v, null);
            assertArrayEquals(""+Arrays.toString(val), val, v);
        }
    }


    public void testLongIS() {
        long[][] vals=new long[1000][];
        Random rand=new Random();
        for(int i=0;i<vals.length;i++) {
            vals[i]=new long[rand.nextInt(200)];
            for(int j=0;j<vals[i].length;j++) vals[i][j]=rand.nextLong();
        }
        LongArraySerializer<Object> s=new LongArraySerializer<Object>();
        ByteArrayOutputStream os=new ByteArrayOutputStream();
        for(long[] val : vals) s.encode(val, os, null);
        InputStream is=new ByteArrayInputStream(os.toByteArray());
        for(long[] val : vals) assertArrayEquals(""+Arrays.toString(val), val, s.decode(is, null));
    }

    public void testLongBB() {
        long[][] vals=new long[1000][];
        Random rand=new Random();
        for(int i=0;i<vals.length;i++) {
            vals[i]=new long[rand.nextInt(200)];
            for(int j=0;j<vals[i].length;j++) vals[i][j]=rand.nextLong();
        }
        LongArraySerializer<Object> s=new LongArraySerializer<Object>();
        ByteBuffer bb = ByteBuffer.allocate(10+8*200*vals.length);
        for(long[] val : vals) s.encode(val, bb, null);
        bb.flip();
        for(long[] val : vals) assertArrayEquals(""+Arrays.toString(val), val, s.decode(bb, null));
    }

    public void testLongAR() {
        long[][] vals=new long[1000][];
        Random rand=new Random();
        for(int i=0;i<vals.length;i++) {
            vals[i]=new long[rand.nextInt(200)];
            for(int j=0;j<vals[i].length;j++) vals[i][j]=rand.nextLong();
        }
        LongArraySerializer<Object> s=new LongArraySerializer<Object>();
        byte[] ar = new byte[10+8*200*vals.length];
        long pos = 0;
        for(long[] val : vals) pos += s.encode(val, ar, pos, ar.length-pos, null);
        long len = pos;
        pos = 0;
        for(long[] val : vals) {
            long[] v=s.decode(ar, pos, len-pos, null);
            pos += s.length(v, null);
            assertArrayEquals(""+Arrays.toString(val), val, v);
        }
    }


    public void testFloatIS() {
        float[][] vals=new float[1000][];
        Random rand=new Random();
        for(int i=0;i<vals.length;i++) {
            vals[i]=new float[rand.nextInt(200)];
            for(int j=0;j<vals[i].length;j++) vals[i][j]=Float.MAX_VALUE*rand.nextFloat();
        }
        FloatArraySerializer<Object> s=new FloatArraySerializer<Object>();
        ByteArrayOutputStream os=new ByteArrayOutputStream();
        for(float[] val : vals) s.encode(val, os, null);
        InputStream is=new ByteArrayInputStream(os.toByteArray());
        for(float[] val : vals) assertArrayEquals(""+Arrays.toString(val), val, s.decode(is, null));
    }

    public void testFloatBB() {
        float[][] vals=new float[1000][];
        Random rand=new Random();
        for(int i=0;i<vals.length;i++) {
            vals[i]=new float[rand.nextInt(200)];
            for(int j=0;j<vals[i].length;j++) vals[i][j]=Float.MAX_VALUE*rand.nextFloat();
        }
        FloatArraySerializer<Object> s=new FloatArraySerializer<Object>();
        ByteBuffer bb = ByteBuffer.allocate(10+4*200*vals.length);
        for(float[] val : vals) s.encode(val, bb, null);
        bb.flip();
        for(float[] val : vals) assertArrayEquals(""+Arrays.toString(val), val, s.decode(bb, null));
    }

    public void testFloatAR() {
        float[][] vals=new float[1000][];
        Random rand=new Random();
        for(int i=0;i<vals.length;i++) {
            vals[i]=new float[rand.nextInt(200)];
            for(int j=0;j<vals[i].length;j++) vals[i][j]=Float.MAX_VALUE*rand.nextFloat();
        }
        FloatArraySerializer<Object> s=new FloatArraySerializer<Object>();
        byte[] ar = new byte[10+4*200*vals.length];
        long pos = 0;
        for(float[] val : vals) pos += s.encode(val, ar, pos, ar.length-pos, null);
        long len = pos;
        pos = 0;
        for(float[] val : vals) {
            float[] v=s.decode(ar, pos, len-pos, null);
            pos += s.length(v, null);
            assertArrayEquals(""+Arrays.toString(val), val, v);
        }
    }


    public void testDoubleIS() {
        double[][] vals=new double[1000][];
        Random rand=new Random();
        for(int i=0;i<vals.length;i++) {
            vals[i]=new double[rand.nextInt(200)];
            for(int j=0;j<vals[i].length;j++) vals[i][j]=Double.MAX_VALUE*rand.nextDouble();
        }
        DoubleArraySerializer<Object> s=new DoubleArraySerializer<Object>();
        ByteArrayOutputStream os=new ByteArrayOutputStream();
        for(double[] val : vals) s.encode(val, os, null);
        InputStream is=new ByteArrayInputStream(os.toByteArray());
        for(double[] val : vals) assertArrayEquals(""+Arrays.toString(val), val, s.decode(is, null));
    }

    public void testDoubleBB() {
        double[][] vals=new double[1000][];
        Random rand=new Random();
        for(int i=0;i<vals.length;i++) {
            vals[i]=new double[rand.nextInt(200)];
            for(int j=0;j<vals[i].length;j++) vals[i][j]=Double.MAX_VALUE*rand.nextDouble();
        }
        DoubleArraySerializer<Object> s=new DoubleArraySerializer<Object>();
        ByteBuffer bb = ByteBuffer.allocate(10+8*200*vals.length);
        for(double[] val : vals) s.encode(val, bb, null);
        bb.flip();
        for(double[] val : vals) assertArrayEquals(""+Arrays.toString(val), val, s.decode(bb, null));
    }

    public void testDoubleAR() {
        double[][] vals=new double[1000][];
        Random rand=new Random();
        for(int i=0;i<vals.length;i++) {
            vals[i]=new double[rand.nextInt(200)];
            for(int j=0;j<vals[i].length;j++) vals[i][j]=Double.MAX_VALUE*rand.nextDouble();
        }
        DoubleArraySerializer<Object> s=new DoubleArraySerializer<Object>();
        byte[] ar = new byte[10+8*200*vals.length];
        long pos = 0;
        for(double[] val : vals) pos += s.encode(val, ar, pos, ar.length-pos, null);
        long len = pos;
        pos = 0;
        for(double[] val : vals) {
            double[] v=s.decode(ar, pos, len-pos, null);
            pos += s.length(v, null);
            assertArrayEquals(""+Arrays.toString(val), val, v);
        }
    }


    public static void assertArrayEquals(String message, boolean[] expected, boolean[] actual) {
        assertEquals(message+" (length)", expected.length, actual.length);
        for(int i=0;i<expected.length;i++) assertEquals(message+" ["+i+"]", expected[i], actual[i]);
    }

    public static void assertArrayEquals(String message, byte[] expected, byte[] actual) {
        assertEquals(message+" (length)", expected.length, actual.length);
        for(int i=0;i<expected.length;i++) assertEquals(message+" ["+i+"]", expected[i], actual[i]);
    }

    public static void assertArrayEquals(String message, short[] expected, short[] actual) {
        assertEquals(message+" (length)", expected.length, actual.length);
        for(int i=0;i<expected.length;i++) assertEquals(message+" ["+i+"]", expected[i], actual[i]);
    }

    public static void assertArrayEquals(String message, int[] expected, int[] actual) {
        assertEquals(message+" (length)", expected.length, actual.length);
        for(int i=0;i<expected.length;i++) assertEquals(message+" ["+i+"]", expected[i], actual[i]);
    }

    public static void assertArrayEquals(String message, long[] expected, long[] actual) {
        assertEquals(message+" (length)", expected.length, actual.length);
        for(int i=0;i<expected.length;i++) assertEquals(message+" ["+i+"]", expected[i], actual[i]);
    }

    public static void assertArrayEquals(String message, float[] expected, float[] actual) {
        assertEquals(message+" (length)", expected.length, actual.length);
        for(int i=0;i<expected.length;i++) assertEquals(message+" ["+i+"]", expected[i], actual[i]);
    }

    public static void assertArrayEquals(String message, double[] expected, double[] actual) {
        assertEquals(message+" (length)", expected.length, actual.length);
        for(int i=0;i<expected.length;i++) assertEquals(message+" ["+i+"]", expected[i], actual[i]);
    }

}
