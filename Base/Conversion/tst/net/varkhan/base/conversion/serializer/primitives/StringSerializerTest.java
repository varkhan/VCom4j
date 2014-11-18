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
 * @time 5:57 AM
 */
public class StringSerializerTest extends TestCase {

    public void testStringIS() {
        StringSerializer<Object> s=new StringSerializer<Object>();
        Random rand=new Random();
        String[] as=new String[(int) (rand.nextFloat()*1000)];
        for(int i=0;i<as.length;i++) {
            char[] ac=new char[(int) (rand.nextFloat()*1000)];
            for(int j=0;j<ac.length;j++) ac[j]=(char) (rand.nextFloat()*Character.MAX_CODE_POINT);
            as[i]=new String(ac);
        }
        ByteArrayOutputStream os=new ByteArrayOutputStream();
        for(String v : as) {
//            System.out.println(v.length());
            s.encode(v, os, null);
        }
        InputStream is=new ByteArrayInputStream(os.toByteArray());
        for(String v : as) assertEquals("\""+v+"\"", v, s.decode(is, null));
    }

    public void testStringBB() {
        StringSerializer<Object> s=new StringSerializer<Object>();
        Random rand=new Random();
        String[] as=new String[(int) (rand.nextFloat()*1000)];
        for(int i=0;i<as.length;i++) {
            char[] ac=new char[(int) (rand.nextFloat()*1000)];
            for(int j=0;j<ac.length;j++) ac[j]=(char) (rand.nextFloat()*Character.MAX_CODE_POINT);
            as[i]=new String(ac);
        }
        ByteBuffer bb = ByteBuffer.allocate(10+4*1000*1000);
        for(String v : as) {
//            System.out.println(v.length());
            s.encode(v, bb, null);
        }
        bb.flip();
        for(String v : as) assertEquals("\""+v+"\"", v, s.decode(bb, null));
    }

    public void testStringAR() {
        StringSerializer<Object> s=new StringSerializer<Object>();
        Random rand=new Random();
        String[] as=new String[(int) (rand.nextFloat()*1000)];
        for(int i=0;i<as.length;i++) {
            char[] ac=new char[(int) (rand.nextFloat()*1000)];
            for(int j=0;j<ac.length;j++) ac[j]=(char) (rand.nextFloat()*Character.MAX_CODE_POINT);
            as[i]=new String(ac);
        }
        byte[] ba = new byte[10+4*1000*1000];
        long pos = 0;
        for(String v : as) {
//            System.out.println(v.length());
            pos += s.encode(v, ba, pos, ba.length-pos, null);
        }
        long len = pos;
        pos=0;
        for(String val : as) {
            String v=s.decode(ba, pos, len-pos, null);
            pos += s.length(val,null);
            assertEquals("\""+val+"\"", val, v);
        }
    }

}
