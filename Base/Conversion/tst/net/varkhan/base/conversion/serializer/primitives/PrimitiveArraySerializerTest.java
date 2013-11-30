package net.varkhan.base.conversion.serializer.primitives;

import junit.framework.TestCase;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
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

    public void testBoolean() {
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

    public void testByte() {
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

    public void testShort() {
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

    public void testInt() {
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

    public void testLong() {
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

}
