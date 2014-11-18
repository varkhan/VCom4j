package net.varkhan.base.conversion.serializer.primitives;

import junit.framework.TestCase;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.nio.ByteBuffer;
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

    public void testBooleanIS() {
        BooleanSerializer<Object> s=new BooleanSerializer<Object>();
        ByteArrayOutputStream os=new ByteArrayOutputStream();
        s.encode(true, os, null);
        s.encode(false, os, null);
        InputStream is=new ByteArrayInputStream(os.toByteArray());
        assertEquals("true", true, s.decode(is, null).booleanValue());
        assertEquals("false", false, s.decode(is, null).booleanValue());
    }

    public void testBooleanBB() {
        BooleanSerializer<Object> s=new BooleanSerializer<Object>();
        ByteBuffer bb = ByteBuffer.allocate(10);
        s.encode(true, bb, null);
        s.encode(false, bb, null);
        bb.flip();
        assertEquals("true", true, s.decode(bb, null).booleanValue());
        assertEquals("false", false, s.decode(bb, null).booleanValue());
    }

    public void testBooleanAR() {
        BooleanSerializer<Object> s=new BooleanSerializer<Object>();
        byte[] ar = new byte[10];
        long pos = 0;
        pos += s.encode(true, ar, pos, ar.length-pos, null);
        pos += s.encode(false, ar, pos, ar.length-pos, null);
        long len = pos;
        pos = 0;
        Boolean v;
        v=s.decode(ar, pos, len-pos, null);
        pos+=s.length(v, null);
        assertEquals("true", true, v.booleanValue());
        v=s.decode(ar, pos, len-pos, null);
        pos+=s.length(v, null);
        assertEquals("false", false, v.booleanValue());
    }


    public void testByteIS() {
        ByteSerializer<Object> s=new ByteSerializer<Object>();
        ByteArrayOutputStream os=new ByteArrayOutputStream();
        for(int i=0;i<256;i++) s.encode((byte) i, os, null);
        InputStream is=new ByteArrayInputStream(os.toByteArray());
        for(int i=0;i<256;i++) assertEquals(""+i, (byte) i, s.decode(is, null).byteValue());
    }

    public void testByteBB() {
        ByteSerializer<Object> s=new ByteSerializer<Object>();
        ByteBuffer bb = ByteBuffer.allocate(300);
        for(int i=0;i<256;i++) s.encode((byte) i, bb, null);
        bb.flip();
        for(int i=0;i<256;i++) assertEquals(""+i, (byte) i, s.decode(bb, null).byteValue());
    }

    public void testByteAR() {
        ByteSerializer<Object> s=new ByteSerializer<Object>();
        byte[] ar = new byte[300];
        long pos = 0;
        for(int i=0;i<256;i++) pos += s.encode((byte) i, ar, pos, ar.length-pos, null);
        long len = pos;
        pos = 0;
        for(int i=0;i<256;i++) {
            Byte v=s.decode(ar, pos, len-pos, null);
            pos += s.length(v, null);
            assertEquals(""+i, (byte) i, v.byteValue());
        }
    }

    public void testShortIS() {
        short[] vals=new short[1000];
        Random rand=new Random();
        for(int i=0;i<vals.length;i++) vals[i]=(short) rand.nextInt(Short.MAX_VALUE);
        ShortSerializer<Object> s=new ShortSerializer<Object>();
        ByteArrayOutputStream os=new ByteArrayOutputStream();
        for(short val : vals) s.encode(val, os, null);
        InputStream is=new ByteArrayInputStream(os.toByteArray());
        for(short val : vals) assertEquals(""+val, val, s.decode(is, null).shortValue());
    }

    public void testShortBB() {
        short[] vals=new short[1000];
        Random rand=new Random();
        for(int i=0;i<vals.length;i++) vals[i]=(short) rand.nextInt(Short.MAX_VALUE);
        ShortSerializer<Object> s=new ShortSerializer<Object>();
        ByteBuffer bb = ByteBuffer.allocate(2*vals.length);
        for(short val : vals) s.encode(val, bb, null);
        bb.flip();
        for(short val : vals) assertEquals(""+val, val, s.decode(bb, null).shortValue());
    }

    public void testShortAR() {
        short[] vals=new short[1000];
        Random rand=new Random();
        for(int i=0;i<vals.length;i++) vals[i]=(short) rand.nextInt(Short.MAX_VALUE);
        ShortSerializer<Object> s=new ShortSerializer<Object>();
        byte[] ar = new byte[2*vals.length];
        long pos = 0;
        for(short val : vals) pos += s.encode(val, ar, pos, ar.length-pos, null);
        long len = pos;
        pos = 0;
        for(short val : vals) {
            Short v=s.decode(ar, pos, len-pos, null);
            pos += s.length(v, null);
            assertEquals(""+val, val, v.shortValue());
        }
    }

    public void testIntIS() {
        int[] vals=new int[1000];
        Random rand=new Random();
        for(int i=0;i<vals.length;i++) vals[i]=rand.nextInt();
        IntSerializer<Object> s=new IntSerializer<Object>();
        ByteArrayOutputStream os=new ByteArrayOutputStream();
        for(int val : vals) s.encode(val, os, null);
        InputStream is=new ByteArrayInputStream(os.toByteArray());
        for(int val : vals) assertEquals(""+val, val, s.decode(is, null).intValue());
    }

    public void testIntBB() {
        int[] vals=new int[1000];
        Random rand=new Random();
        for(int i=0;i<vals.length;i++) vals[i]=rand.nextInt();
        IntSerializer<Object> s=new IntSerializer<Object>();
        ByteBuffer bb = ByteBuffer.allocate(4*vals.length);
        for(int val : vals) s.encode(val, bb, null);
        bb.flip();
        for(int val : vals) assertEquals(""+val, val, s.decode(bb, null).intValue());
    }

    public void testIntAR() {
        int[] vals=new int[1000];
        Random rand=new Random();
        for(int i=0;i<vals.length;i++) vals[i]=rand.nextInt();
        IntSerializer<Object> s=new IntSerializer<Object>();
        byte[] ar = new byte[4*vals.length];
        long pos = 0;
        for(int val : vals) pos += s.encode(val, ar, pos, ar.length-pos, null);
        long len = pos;
        pos = 0;
        for(int val : vals) {
            Integer v=s.decode(ar, pos, len-pos, null);
            pos += s.length(v, null);
            assertEquals(""+val, val, v.intValue());
        }
    }

    public void testLongIS() {
        long[] vals=new long[1000];
        Random rand=new Random();
        for(int i=0;i<vals.length;i++) vals[i]=rand.nextLong();
        LongSerializer<Object> s=new LongSerializer<Object>();
        ByteArrayOutputStream os=new ByteArrayOutputStream();
        for(long val : vals) s.encode(val, os, null);
        InputStream is=new ByteArrayInputStream(os.toByteArray());
        for(long val : vals) assertEquals(""+val, val, s.decode(is, null).longValue());
    }

    public void testLongBB() {
        long[] vals=new long[1000];
        Random rand=new Random();
        for(int i=0;i<vals.length;i++) vals[i]=rand.nextLong();
        LongSerializer<Object> s=new LongSerializer<Object>();
        ByteBuffer bb = ByteBuffer.allocate(8*vals.length);
        for(long val : vals) s.encode(val, bb, null);
        bb.flip();
        for(long val : vals) assertEquals(""+val, val, s.decode(bb, null).longValue());
    }

    public void testLongAR() {
        long[] vals=new long[1000];
        Random rand=new Random();
        for(int i=0;i<vals.length;i++) vals[i]=rand.nextLong();
        LongSerializer<Object> s=new LongSerializer<Object>();
        byte[] ar = new byte[8*vals.length];
        long pos = 0;
        for(long val : vals) pos += s.encode(val, ar, pos, ar.length-pos, null);
        long len = pos;
        pos = 0;
        for(long val : vals) {
            Long v=s.decode(ar, pos, len-pos, null);
            pos += s.length(v, null);
            assertEquals(""+val, val, v.longValue());
        }
    }

    public void testVariadicIS() {
        long[] vals=new long[1000];
        Random rand=new Random();
        for(int i=0;i<vals.length;i++) vals[i]=rand.nextLong();
        VariadicSerializer<Object> s=new VariadicSerializer<Object>();
        ByteArrayOutputStream os=new ByteArrayOutputStream();
        for(long val : vals) s.encode(val, os, null);
        InputStream is=new ByteArrayInputStream(os.toByteArray());
        for(long val : vals) assertEquals(""+val, val, s.decode(is, null).longValue());
    }

    public void testVariadicBB() {
        long[] vals=new long[1000];
        Random rand=new Random();
        for(int i=0;i<vals.length;i++) vals[i]=rand.nextLong();
        VariadicSerializer<Object> s=new VariadicSerializer<Object>();
        ByteBuffer bb = ByteBuffer.allocate(10*vals.length);
        for(long val : vals) s.encode(val, bb, null);
        bb.flip();
        for(long val : vals) assertEquals(""+val, val, s.decode(bb, null).longValue());
    }

    public void testVariadicAR() {
        long[] vals=new long[1000];
        Random rand=new Random();
        for(int i=0;i<vals.length;i++) vals[i]=rand.nextLong();
        VariadicSerializer<Object> s=new VariadicSerializer<Object>();
        byte[] ar = new byte[10*vals.length];
        long pos = 0;
        for(long val : vals) pos += s.encode(val, ar, pos, ar.length-pos, null);
        long len = pos;
        pos = 0;
        for(long val : vals) {
            Long v=s.decode(ar, pos, len-pos, null);
            pos += s.length(v, null);
            assertEquals(""+val, val, v.longValue());
        }
    }

}
