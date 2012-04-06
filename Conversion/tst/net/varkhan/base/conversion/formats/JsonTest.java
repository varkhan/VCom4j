package net.varkhan.base.conversion.formats;

import junit.framework.TestCase;
import net.varkhan.base.containers.array.Arrays;
import net.varkhan.base.containers.map.Map;

import java.io.*;


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
        assertEquals("{\"a\":2,\"b\":true,\"c\":null,\"d\":\"D\"}",Json.writeObject(new StringBuilder(), Arrays.asMap(String.class, Object.class, "a", 2, "b", true, "c", null, "d", "D")).toString());
        assertEquals("[\"a\",\"b\",\"c\",\"d\"]",Json.writeObject(new StringBuilder(), Arrays.asList("a", "b", "c", "d")).toString());
        assertEquals("\"abc _\\t\\f\\r\\u4a9dgh\\u0003\"",Json.writeObject(new StringBuilder(),"abc _\t\f\r\u4a9dgh\3").toString());
        assertEquals("{\"a\":2,\"b\":true,\"c\":null,\"d\":[\"x\",\"y\",1.2,\"abc _\\t\\f\\r\\u4a9dgh\\u0003\"]}",
                     Json.writeObject(new StringBuilder(),
                                      Arrays.asMap(String.class, Object.class, "a", 2, "b", true, "c", null, "d", Arrays.asList("x", "y", 1.2, "abc _\t\f\r\u4a9dgh\3"))
                                     ).toString());
    }

    public void testWriteMap() throws Exception {
        assertEquals("\"a\":2,\"b\":true,\"c\":null,\"d\":\"D\"",Json.writeMap(new StringBuilder(), Arrays.asMap(String.class, Object.class, "a", 2, "b", true, "c", null, "d", "D")).toString());
    }

    public void testWriteList() throws Exception {
        assertEquals("\"a\",\"b\",\"c\",\"d\"",Json.writeList(new StringBuilder(), Arrays.asList("a", "b", "c", "d")).toString());

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
                     Json.toJson(Arrays.asMap(String.class, Object.class, "a", 2, "b", true, "c", null, "d", Arrays.asList("x", "y", 1.2, "abc _\t\f\r\u4a9dgh\3")))
                    );
    }

    public void testReadString() throws Exception {
        assertEquals("abc _\t\f\r\u4a9dgh\3",Json.readString(new StringReader("\"abc _\\t\\f\\r\\u4a9dgh\\u0003\"")));
    }

    public void testReadList() throws Exception {

    }

    public void testReadMap() throws Exception {

    }


    public void testReadWriteFile() throws Exception {
        InputStream in;
        try {
            in = new FileInputStream("VCom4j/Conversion/tst/test.json");
        }
        catch(Exception e) {
            in = this.getClass().getResourceAsStream("/test.json");
        }
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

}
