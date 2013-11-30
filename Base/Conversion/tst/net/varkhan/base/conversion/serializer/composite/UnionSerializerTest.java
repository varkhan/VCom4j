package net.varkhan.base.conversion.serializer.composite;

import junit.framework.TestCase;
import net.varkhan.base.conversion.serializer.DecodingException;
import net.varkhan.base.conversion.serializer.EncodingException;
import net.varkhan.base.conversion.serializer.primitives.BooleanSerializer;
import net.varkhan.base.conversion.serializer.primitives.IntSerializer;
import net.varkhan.base.conversion.serializer.primitives.NullSerializer;
import net.varkhan.base.conversion.serializer.primitives.StringSerializer;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;


/**
 * <b></b>.
 * <p/>
 *
 * @author varkhan
 * @date 5/28/11
 * @time 10:48 PM
 */
public class UnionSerializerTest extends TestCase {

    public void testConcrete() {
        UnionSerializer<Object,Object> s = new UnionSerializer<Object,Object>();
        s.setSerializer(Boolean.class,new BooleanSerializer<Object>());
        s.setSerializer(Integer.class,new IntSerializer<Object>());
        s.setSerializer(CharSequence.class, new StringSerializer<Object>());
        ByteArrayOutputStream os=new ByteArrayOutputStream();
        s.encode(true,os,null);
        s.encode("toto",os,null);
        s.encode(10,os,null);
        InputStream is=new ByteArrayInputStream(os.toByteArray());

        assertEquals("bool", true, s.decode(is, null));
        assertEquals("string", "toto", s.decode(is, null));
        assertEquals("int", 10, s.decode(is, null));
    }

    public void testWithNull() {
        UnionSerializer<Object,Object> s = new UnionSerializer<Object,Object>();
        s.setSerializer(Void.class,new NullSerializer<Void,Object>());
        s.setSerializer(Boolean.class,new BooleanSerializer<Object>());
        s.setSerializer(Integer.class,new IntSerializer<Object>());
        s.setSerializer(CharSequence.class, new StringSerializer<Object>());
        ByteArrayOutputStream os=new ByteArrayOutputStream();
        s.encode(true,os,null);
        s.encode("toto",os,null);
        s.encode(10,os,null);
        s.encode(null,os,null);
        InputStream is=new ByteArrayInputStream(os.toByteArray());

        assertEquals("bool", true, s.decode(is, null));
        assertEquals("string", "toto", s.decode(is, null));
        assertEquals("int", 10, s.decode(is, null));
        assertEquals("null", null, s.decode(is, null));
    }

    public void testUnknownClass() {
        UnionSerializer<Object,Object> s = new UnionSerializer<Object,Object>();
        s.setSerializer(Boolean.class,new BooleanSerializer<Object>());
        s.setSerializer(Integer.class,new IntSerializer<Object>());
        s.setSerializer(CharSequence.class, new StringSerializer<Object>());
        ByteArrayOutputStream os=new ByteArrayOutputStream();
        s.encode(true,os,null);
        s.encode("toto", os, null);
        s.encode(10, os, null);
        try {
            s.encode(1.0,os,null);
            fail("Unknown class not caught");
        }
        catch(EncodingException e) { /* success */ }
        InputStream is=new ByteArrayInputStream(os.toByteArray());

        assertEquals("bool", true, s.decode(is, null));
        assertEquals("string", "toto", s.decode(is, null));
        assertEquals("int", 10, s.decode(is, null));
    }

    public void testUnknownId() {
        UnionSerializer<Object,Object> s1 = new UnionSerializer<Object,Object>();
        s1.setSerializer(Boolean.class,new BooleanSerializer<Object>());
        s1.setSerializer(Integer.class,new IntSerializer<Object>());
        s1.setSerializer(CharSequence.class, new StringSerializer<Object>());
        UnionSerializer<Object,Object> s2 = new UnionSerializer<Object,Object>();
        s2.setSerializer(Boolean.class,new BooleanSerializer<Object>());
        s2.setSerializer(Integer.class,new IntSerializer<Object>());

        ByteArrayOutputStream os=new ByteArrayOutputStream();
        s1.encode(true,os,null);
        s1.encode("toto",os,null);
        s1.encode(10,os,null);
        InputStream is=new ByteArrayInputStream(os.toByteArray());

        assertEquals("bool", true, s2.decode(is, null));
        try {
            s2.decode(is, null);
            fail("Unknown ID not caught");
        }
        catch(DecodingException e) { /* success */ }
    }

}
