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
 * @time 5:57 AM
 */
public class StringSerializerTest extends TestCase {

    public void testString() {
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


}
