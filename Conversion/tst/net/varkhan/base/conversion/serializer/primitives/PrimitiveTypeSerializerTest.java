package net.varkhan.base.conversion.serializer.primitives;

import junit.framework.TestCase;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.Random;


/**
 * <b></b>.
 * <p/>
 *
 * @author varkhan
 * @date 2/6/11
 * @time 4:02 AM
 */
public class PrimitiveTypeSerializerTest extends TestCase {

    public void testBoolean() {
        BooleanSerializer<Object> s=new BooleanSerializer<Object>();
        ByteArrayOutputStream os=new ByteArrayOutputStream();
        s.encode(true, os, null);
        s.encode(false, os, null);
        InputStream is=new ByteArrayInputStream(os.toByteArray());
        assertEquals("true", true, s.decode(is, null).booleanValue());
        assertEquals("false", false, s.decode(is, null).booleanValue());
    }

    public void testByte() {
        ByteSerializer<Object> s=new ByteSerializer<Object>();
        ByteArrayOutputStream os=new ByteArrayOutputStream();
        for(int i=0;i<256;i++) s.encode((byte) i, os, null);
        InputStream is=new ByteArrayInputStream(os.toByteArray());
        for(int i=0;i<256;i++) assertEquals(""+i, (byte) i, s.decode(is, null).byteValue());
    }

    public void testShort() {
        short[] vals=new short[1000];
        Random rand=new Random();
        for(int i=0;i<vals.length;i++) vals[i]=(short) rand.nextInt(Short.MAX_VALUE);
        ShortSerializer<Object> s=new ShortSerializer<Object>();
        ByteArrayOutputStream os=new ByteArrayOutputStream();
        for(short val : vals) s.encode(val, os, null);
        InputStream is=new ByteArrayInputStream(os.toByteArray());
        for(short val : vals) assertEquals(""+val, val, s.decode(is, null).shortValue());
    }

    public void testInt() {
        int[] vals=new int[1000];
        Random rand=new Random();
        for(int i=0;i<vals.length;i++) vals[i]=rand.nextInt();
        IntSerializer<Object> s=new IntSerializer<Object>();
        ByteArrayOutputStream os=new ByteArrayOutputStream();
        for(int val : vals) s.encode(val, os, null);
        InputStream is=new ByteArrayInputStream(os.toByteArray());
        for(int val : vals) assertEquals(""+val, val, s.decode(is, null).intValue());
    }

    public void testLong() {
        long[] vals=new long[1000];
        Random rand=new Random();
        for(int i=0;i<vals.length;i++) vals[i]=rand.nextLong();
        LongSerializer<Object> s=new LongSerializer<Object>();
        ByteArrayOutputStream os=new ByteArrayOutputStream();
        for(long val : vals) s.encode(val, os, null);
        InputStream is=new ByteArrayInputStream(os.toByteArray());
        for(long val : vals) assertEquals(""+val, val, s.decode(is, null).longValue());
    }

    public void testVariadic() {
        long[] vals=new long[1000];
        Random rand=new Random();
        for(int i=0;i<vals.length;i++) vals[i]=rand.nextLong();
        VariadicSerializer<Object> s=new VariadicSerializer<Object>();
        ByteArrayOutputStream os=new ByteArrayOutputStream();
        for(long val : vals) s.encode(val, os, null);
        InputStream is=new ByteArrayInputStream(os.toByteArray());
        for(long val : vals) assertEquals(""+val, val, s.decode(is, null).longValue());
    }

}
