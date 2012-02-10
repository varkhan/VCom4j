package net.varkhan.base.conversion.serializer.composite;

import junit.framework.TestCase;
import net.varkhan.base.conversion.serializer.Serializer;
import net.varkhan.base.conversion.serializer.primitives.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;


/**
 * <b></b>.
 * <p/>
 *
 * @author varkhan
 * @date 5/28/11
 * @time 11:07 PM
 */
public class StructSerializerTest extends TestCase {


    public void testFlatStruct() {
        StructSerializer<Object> s = new StructSerializer<Object>();
        s.setSerializer("f_bool",new BooleanSerializer<Object>());
        s.setSerializer("f_int",new IntSerializer<Object>());
        s.setSerializer("f_string",new StringSerializer<Object>());
        Map<String,Object> m = new HashMap<String,Object>();
        m.put("f_bool",true);
        m.put("f_int",10);
        m.put("f_string","toto");
        ByteArrayOutputStream os=new ByteArrayOutputStream();
        s.encode(m,os,null);
        InputStream is=new ByteArrayInputStream(os.toByteArray());
        assertEquals("map", m, s.decode(is, null));
    }

    public void testHierStruct() {
        StructSerializer<Object> s2 = new StructSerializer<Object>();
        s2.setSerializer("x",new FloatSerializer<Object>());
        s2.setSerializer("y",new FloatSerializer<Object>());
        s2.setSerializer("n",new LongSerializer<Object>());
        Map<String,Object> pos = new HashMap<String,Object>();
        pos.put("x",4.2f);
        pos.put("y",5.7f);
        pos.put("n",100L);
        StructSerializer<Object> s1 = new StructSerializer<Object>();
        s1.setSerializer("flag",new BooleanSerializer<Object>());
        s1.setSerializer("count",new IntSerializer<Object>());
        s1.setSerializer("name",new StringSerializer<Object>());
        s1.setSerializer("pos",s2);
        Map<String,Object> m = new HashMap<String,Object>();
        m.put("flag", true);
        m.put("count", 10);
        m.put("name","toto");
        m.put("pos", pos);
        ByteArrayOutputStream os=new ByteArrayOutputStream();
        s1.encode(m, os, null);
        InputStream is=new ByteArrayInputStream(os.toByteArray());
        assertEquals("map", m, s1.decode(is, null));
    }

    public void testNonNullableStruct() {
        StructSerializer<Object> s2 = new StructSerializer<Object>();
        s2.setSerializer("x", new FloatSerializer<Object>());
        s2.setSerializer("y", new FloatSerializer<Object>());
        s2.setSerializer("n", new LongSerializer<Object>());
        StructSerializer<Object> s1 = new StructSerializer<Object>();
        s1.setSerializer("flag",new BooleanSerializer<Object>());
        s1.setSerializer("count",new IntSerializer<Object>());
        s1.setSerializer("name",new StringSerializer<Object>());
        s1.setSerializer("pos",s2);
        Map<String,Object> m = new HashMap<String,Object>();
        m.put("flag", true);
        m.put("count", 10);
        m.put("name","toto");
        m.put("pos", null);
        ByteArrayOutputStream os=new ByteArrayOutputStream();
        try {
            s1.encode(m, os, null);
            fail("Uncaught NULL reference for non-nullable");
        }
        catch(NullPointerException e) { /* success */ }
    }

    public void testNullableStruct() {
        StructSerializer<Object> s2 = new StructSerializer<Object>();
        s2.setSerializer("x",new FloatSerializer<Object>());
        s2.setSerializer("y",new FloatSerializer<Object>());
        s2.setSerializer("n",new LongSerializer<Object>());
        Map<String,Object> pos = new HashMap<String,Object>();
        pos.put("x",4.2f);
        pos.put("y",5.7f);
        pos.put("n",100L);
        UnionSerializer<Object,Object> s3 = new UnionSerializer<Object,Object>();
        s3.setSerializer(Void.class, new NullSerializer<Void,Object>());
        s3.setSerializer(Map.class, (Serializer) s2);
        StructSerializer<Object> s1 = new StructSerializer<Object>();
        s1.setSerializer("flag",new BooleanSerializer<Object>());
        s1.setSerializer("count",new IntSerializer<Object>());
        s1.setSerializer("name",new StringSerializer<Object>());
        s1.setSerializer("pos",s3);
        Map<String,Object> m = new HashMap<String,Object>();
        m.put("flag", true);
        m.put("count", 10);
        m.put("name","toto");
        m.put("pos", pos);
        ByteArrayOutputStream os=new ByteArrayOutputStream();
        s1.encode(m, os, null);
        InputStream is=new ByteArrayInputStream(os.toByteArray());
        assertEquals("map", m, s1.decode(is, null));
    }

}
