package net.varkhan.base.conversion.formats;

import junit.framework.TestCase;

import java.io.*;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;


/**
 * <b></b>.
 * <p/>
 *
 * @author varkhan
 * @date 3/31/12
 * @time 3:11 PM
 */
public class JsonTest extends TestCase {

    public void testWriteObject() throws Exception {
        assertEquals("{\"a\":2,\"b\":true,\"c\":null,\"d\":\"D\"}", Json.writeObject(new StringBuilder(), asMap(String.class, Object.class, "a", 2, "b", true, "c", null, "d", "D")).toString());
        assertEquals("[\"a\",\"b\",\"c\",\"d\"]", Json.writeObject(new StringBuilder(), Arrays.asList("a", "b", "c", "d")).toString());
        assertEquals("\"abc _\\t\\f\\r\\u4a9dgh\\u0003\"",Json.writeObject(new StringBuilder(),"abc _\t\f\r\u4a9dgh\3").toString());
        assertEquals("{\"a\":2,\"b\":true,\"c\":null,\"d\":[\"x\",\"y\",1.2,\"abc _\\t\\f\\r\\u4a9dgh\\u0003\"]}",
                     Json.writeObject(new StringBuilder(),
                                      asMap(String.class, Object.class, "a", 2, "b", true, "c", null, "d", Arrays.asList("x", "y", 1.2, "abc _\t\f\r\u4a9dgh\3"))
                                     ).toString());
    }

    public void testWriteMap() throws Exception {
        assertEquals("\"a\":2,\"b\":true,\"c\":null,\"d\":\"D\"", Json.writeMap(new StringBuilder(), asMap(String.class, Object.class, "a", 2, "b", true, "c", null, "d", "D")).toString());
    }

    public void testWriteList() throws Exception {
        assertEquals("\"a\",\"b\",\"c\",\"d\"", Json.writeList(new StringBuilder(), Arrays.asList("a", "b", "c", "d")).toString());

    }

    public void testWriteJavaList() throws Exception {
        assertEquals("\"a\",\"b\",\"c\",\"d\"",Json.writeList(new StringBuilder(), java.util.Arrays.asList("a", "b", "c", "d")).toString());

    }

    public void testWriteArray() throws Exception {
        assertEquals("", Json.writeArray(new StringBuilder()).toString());
        assertEquals("\"a\",\"b\",\"c\",\"d\"",Json.writeArray(new StringBuilder(),"a","b","c","d").toString());
    }

    public void testWriteString() throws Exception {
        assertEquals("", Json.writeString(new StringBuilder(), "").toString());
        assertEquals("abc _\\t\\f\\r\\u4a9dgh\\u0003",Json.writeString(new StringBuilder(),"abc _\t\f\r\u4a9dgh\3").toString());
    }

    public void testReadObject() throws Exception {
        String json="{\"a\":2,\"b\":true,\"c\":null,\"d\":[\"x\",\"y\",1.2,\"abc _\\t\\f\\r\\u4a9dgh\\u0003\"]}";
        assertEquals(json,
                     Json.toJson(Json.readObject(new StringReader(json))),
                     Json.toJson(asMap(String.class, Object.class, "a", 2, "b", true, "c", null, "d", Arrays.asList("x", "y", 1.2, "abc _\t\f\r\u4a9dgh\3")))
                    );
    }

    public void testReadBoolean() throws Exception {
        assertEquals(true,Json.readBoolean(new StringReader("true")));
        assertEquals(false,Json.readBoolean(new StringReader("false")));
    }

    public void testReadNumber() throws Exception {
        assertEquals(1L,Json.readNumber(new StringReader("1")));
        assertEquals(1.0,Json.readNumber(new StringReader("1.0")));
    }

    public void testReadString() throws Exception {
        assertEquals("abc _\t\f\r\u4a9dgh\3",Json.readString(new StringReader("\"abc _\\t\\f\\r\\u4a9dgh\\u0003\"")));
    }

    public void testReadList() throws Exception {
        String json="[\"x\",\"y\",1.2,\"abc _\\t\\f\\r\\u4a9dgh\\u0003\"]";
        assertEquals(json,
                     Json.toJson(Arrays.asList((Object) "x", "y", 1.2, "abc _\t\f\r\u4a9dgh\3")),
                     Json.toJson(Json.readList(new StringReader(json)))
                    );
    }

    public void testReadMap() throws Exception {
        String json="{\"a\":2,\"b\":true,\"c\":null,\"d\":null}";
        assertEquals(json,
                     Json.toJson(asMap(CharSequence.class, Object.class, "a", 2, "b", true, "c", null, "d", null)),
                     Json.toJson(Json.readObject(new StringReader(json)))
                    );
    }


    public void testReadWriteFile() throws Exception {
        InputStream in;
        try {
            in = new FileInputStream("VCom4j/Conversion/tst/test.json");
        }
        catch(Exception e) {
            in = this.getClass().getResourceAsStream("/test.json");
        }
        if(in==null) throw new RuntimeException("Could not find test.json");
        Reader r1 = new InputStreamReader(in);
        Object o1 = Json.readObject(r1);
        Json.writeObject(System.out,o1);
        StringWriter w1 = new StringWriter();
        Json.writeObject(w1,o1);
        Reader r2 = new StringReader(w1.toString());
        Object o2 = Json.readObject(r2);
        StringWriter w2 = new StringWriter();
        Json.writeObject(w2,o2);
        assertEquals("ReadWrite",w1.toString(),w2.toString());
    }

    @SuppressWarnings("unchecked")
    public static <K,V> Map<K,V> asMap(final Class<K> kclass, final Class<V> vclass, final Object... values) {
        Map<K,V> map = new LinkedHashMap<K,V>();
        if(values==null) return map;
        if((values.length&1)!=0) throw new IllegalArgumentException("Key/Value array must be of even size");
        for(int i=0; i<values.length; i+=2) {
            Object key=values[i];
            if(key!=null && !kclass.isAssignableFrom(key.getClass())) throw new IllegalArgumentException("Invalid key type at "+i);
            Object val=values[i+1];
            if(val!=null && !vclass.isAssignableFrom(val.getClass())) throw new IllegalArgumentException("Invalid value type at "+(i+1));
            map.put((K) key, (V) val);
        }
        return map;
    }


}
