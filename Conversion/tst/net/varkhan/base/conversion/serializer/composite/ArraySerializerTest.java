package net.varkhan.base.conversion.serializer.composite;

import junit.framework.TestCase;
import net.varkhan.base.conversion.serializer.Serializer;
import net.varkhan.base.conversion.serializer.primitives.StringSerializer;

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
 * @date 5/28/11
 * @time 11:27 PM
 */
public class ArraySerializerTest extends TestCase {

    public void testString() {
        String[][] vals=new String[1000][];
        Random rand=new Random();
        for(int i=0;i<vals.length;i++) {
            vals[i]=new String[rand.nextInt(200)];
            for(int j=0;j<vals[i].length;j++) vals[i][j]=randString(rand, 30, "abcdefghijklmnopqrstuvwxyz".toCharArray());
        }
        @SuppressWarnings( { "unchecked" })
        ArraySerializer<String,Object> s=new ArraySerializer<String,Object>((Serializer) new StringSerializer<Object>());
        ByteArrayOutputStream os=new ByteArrayOutputStream();
        for(String[] val : vals) s.encode(val, os, null);
        InputStream is=new ByteArrayInputStream(os.toByteArray());
        for(String[] val : vals) assertArrayEquals(""+Arrays.toString(val), val, s.decode(is, null));
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
