package net.varkhan.base.conversion.serializer.composite;

import junit.framework.TestCase;
import net.varkhan.base.conversion.serializer.Serializer;
import net.varkhan.base.conversion.serializer.primitives.StringSerializer;

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
 * @date 5/28/11
 * @time 11:27 PM
 */
public class ArraySerializerTest extends TestCase {

    public void testStringIS() {
        String[][] vals=new String[1000][];
        Random rand=new Random();
        for(int i=0;i<vals.length;i++) {
            vals[i]=new String[rand.nextInt(200)];
            for(int j=0;j<vals[i].length;j++) vals[i][j]=randString(rand, 30, "abcdefghijklmnopqrstuvwxyz".toCharArray());
        }
        @SuppressWarnings( { "unchecked" })
        ArraySerializer<String,Object> s=new ArraySerializer<String,Object>(String.class, (Serializer) new StringSerializer<Object>());
        ByteArrayOutputStream os=new ByteArrayOutputStream();
        for(String[] val : vals) s.encode(val, os, null);
        InputStream is=new ByteArrayInputStream(os.toByteArray());
        for(String[] val : vals) assertArrayEquals(""+Arrays.toString(val), val, s.decode(is, null));
    }

    public void testStringBB() {
        String[][] vals=new String[1000][];
        Random rand=new Random();
        for(int i=0;i<vals.length;i++) {
            vals[i]=new String[rand.nextInt(200)];
            for(int j=0;j<vals[i].length;j++) vals[i][j]=randString(rand, 30, "abcdefghijklmnopqrstuvwxyz".toCharArray());
        }
        @SuppressWarnings( { "unchecked" })
        ArraySerializer<String,Object> s=new ArraySerializer<String,Object>(String.class, (Serializer) new StringSerializer<Object>());
        ByteBuffer bb = ByteBuffer.allocate(10+(10+(10+4*30)*200)*vals.length);
        for(String[] val : vals) s.encode(val, bb, null);
        bb.flip();
        for(String[] val : vals) assertArrayEquals(""+Arrays.toString(val), val, s.decode(bb, null));
    }

    public void testStringAR() {
        String[][] vals=new String[1000][];
        Random rand=new Random();
        for(int i=0;i<vals.length;i++) {
            vals[i]=new String[rand.nextInt(200)];
            for(int j=0;j<vals[i].length;j++) vals[i][j]=randString(rand, 30, "abcdefghijklmnopqrstuvwxyz".toCharArray());
        }
        @SuppressWarnings( { "unchecked" })
        ArraySerializer<String,Object> s=new ArraySerializer<String,Object>(String.class, (Serializer) new StringSerializer<Object>());
        byte[] ar = new byte[10+(10+(10+6*30)*200)*vals.length];
        long pos = 0;
        for(String[] val : vals) pos += s.encode(val, ar, pos, ar.length-pos, null);
        long len = 0;
        for(String[] val : vals) len += s.length(val,null);
        assertEquals("len",len,pos);
        len = pos;
        pos = 0;
        for(String[] val : vals) {
            String[] v=s.decode(ar, pos, len-pos, null);
            pos += s.length(v, null);
            assertArrayEquals(""+Arrays.toString(val), val, v);
        }
        assertEquals("len",len,pos);
    }

    private static String randString(Random rand, int size, char[] chars) {
        int len = rand.nextInt(size);
        char[] c = new char[len];
        for(int i=0; i<len; i++) {
            c[i] = chars[rand.nextInt(chars.length)];
        }
        return new String(c);
    }


    public static void assertArrayEquals(String message, Object[] expected, Object[] actual) {
        assertEquals(message+" (length)", expected.length, actual.length);
        for(int i=0;i<expected.length;i++) assertEquals(message+" ["+i+"]", expected[i], actual[i]);
    }

}
