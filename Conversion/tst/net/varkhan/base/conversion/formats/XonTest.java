package net.varkhan.base.conversion.formats;

import com.sun.xml.internal.fastinfoset.util.StringArray;
import junit.framework.ComparisonFailure;
import junit.framework.TestCase;
import net.varkhan.base.containers.array.Arrays;
import net.varkhan.base.containers.array.StringArrays;
import net.varkhan.base.containers.list.List;
import net.varkhan.base.containers.map.Map;
import net.varkhan.base.containers.set.ArrayOpenHashSet;
import net.varkhan.base.containers.set.Set;

import java.io.*;


/**
 * <b></b>.
 * <p/>
 *
 * @author varkhan
 * @date 3/31/12
 * @time 3:11 PM
 */
public class XonTest extends TestCase {

    public void testWriteObject() throws Exception {
        assertEquals("{\"a\":2,\"b\":true,\"c\",\"d\":\"D\"}",Xon.writeObject(new StringBuilder(), Arrays.asMap(String.class, Object.class, "a", 2, "b", true, "c", null, "d", "D")).toString());
        assertEquals("[\"a\",\"b\",\"c\",\"d\"]",Xon.writeObject(new StringBuilder(), Arrays.asList("a", "b", "c", "d")).toString());
        assertEquals("(\"a\",\"b\",\"c\",\"d\")", Xon.writeObject(new StringBuilder(), new Object[] { "a", "b", "c", "d" }).toString());
        assertEquals("\"abc _\\t\\f\\r\\u4a9dgh\\u0003\"",Xon.writeObject(new StringBuilder(),"abc _\t\f\r\u4a9dgh\3").toString());
        assertEquals("{\"a\":2,\"b\":true,\"c\",\"d\":[\"x\",\"y\",1.2,\"abc _\\t\\f\\r\\u4a9dgh\\u0003\"]}",
                     Xon.writeObject(new StringBuilder(),
                                      Arrays.asMap(String.class, Object.class, "a", 2, "b", true, "c", null, "d", Arrays.asList("x", "y", 1.2, "abc _\t\f\r\u4a9dgh\3"))
                                     ).toString());
    }

    public void testWriteMap() throws Exception {
        assertEquals("\"a\":2,\"b\":true,\"c\",\"d\":\"D\"",Xon.writeMap(new StringBuilder(), Arrays.asMap(String.class, Object.class, "a", 2, "b", true, "c", null, "d", "D")).toString());
    }

    public void testWriteList() throws Exception {
        assertEquals("\"a\",\"b\",\"c\",\"d\"",Xon.writeList(new StringBuilder(), Arrays.asList("a", "b", "c", "d")).toString());
    }

    public void testWriteJavaList() throws Exception {
        assertEquals("\"a\",\"b\",\"c\",\"d\"",Xon.writeList(new StringBuilder(), java.util.Arrays.asList("a", "b", "c", "d")).toString());

    }

    public void testWriteArray() throws Exception {
        assertEquals("", Xon.writeArray(new StringBuilder()).toString());
        assertEquals("\"a\",\"b\",\"c\",\"d\"",Xon.writeArray(new StringBuilder(),"a","b","c","d").toString());
    }

    public void testWriteString() throws Exception {
        assertEquals("", Xon.writeString(new StringBuilder(), "").toString());
        assertEquals("abc _\\t\\f\\r\\u4a9dgh\\u0003",Xon.writeString(new StringBuilder(),"abc _\t\f\r\u4a9dgh\3").toString());
    }

    public void testReadObject() throws Exception {
        String json="{\"a\":2,\"b\":true,\"c\",\"d\":[\"x\",\"y\",1.2,\"abc _\\t\\f\\r\\u4a9dgh\\u0003\"], \"e\":(\"m\",1,2,3,.14)}";
        assertEquals(json,
                     Xon.toXon(Xon.readObject(new StringReader(json))),
                     Xon.toXon(Arrays.asMap(String.class, Object.class, "a", 2, "b", true, "c", null, "d", Arrays.asList("x", "y", 1.2, "abc _\t\f\r\u4a9dgh\3"), "e", new Object[] {"m",1,2,3,.14}))
                    );
    }

    public void testReadString() throws Exception {
        assertEquals("abc _\t\f\r\u4a9dgh\3",Xon.readString(new StringReader("\"abc _\\t\\f\\r\\u4a9dgh\\u0003\"")));
    }

    public void testReadList() throws Exception {
        String json="[\"x\",\"y\",1.2,\"abc _\\t\\f\\r\\u4a9dgh\\u0003\"]";
        assertListEquals(json,
                         Arrays.<Object>asList("x", "y", 1.2, "abc _\t\f\r\u4a9dgh\3"),
                         Xon.readList(new StringReader(json))
                        );
    }

    public void testReadArray() throws Exception {
        String json="(\"x\",\"y\",1.2,\"abc _\\t\\f\\r\\u4a9dgh\\u0003\")";
        assertArrayEquals(json,
                         new Object[]{"x", "y", 1.2, "abc _\t\f\r\u4a9dgh\3"},
                         Xon.readArray(new StringReader(json))
                        );
    }

    public void testReadMap() throws Exception {
        String json="{\"a\":2,\"b\":true,\"c\",\"d\":null}";
        assertMapEquals(json,
                        Arrays.<CharSequence,Object>asMap(CharSequence.class, Object.class, "a", 2, "b", true, "c", null, "d", null),
                        Xon.readMap(new StringReader(json))
                       );
    }


    public void testReadWriteFile() throws Exception {
        InputStream in;
        try {
            in = new FileInputStream("VCom4j/Conversion/tst/test.xon");
        }
        catch(Exception e) {
            in = this.getClass().getResourceAsStream("/test.xon");
        }
        Reader r1 = new InputStreamReader(in);
        Object o1 = Xon.readObject(r1);
        Xon.writeObject(System.out,o1);
        StringWriter w1 = new StringWriter();
        Xon.writeObject(w1,o1);
        Reader r2 = new StringReader(w1.toString());
        Object o2 = Xon.readObject(r2);
        StringWriter w2 = new StringWriter();
        Xon.writeObject(w2,o2);
        assertEquals("ReadWrite",w1.toString(),w2.toString());
    }








    public static <T> void assertArrayEquals(String message, T[] expected, T[] actual) {
        if(expected==actual) return;
        if(expected==null || actual==null || expected.length!=actual.length) {
            throw new ComparisonFailure(
                    message,
                    Xon.toXon(expected),
                    Xon.toXon(actual)
            );
        }
        for(int i=0; i<expected.length && i<actual.length; i++) {
            try {
                assertDeepEquals(message+" ["+i+"]",expected[i],actual[i]);
            }
            catch(ComparisonFailure e) {
                throw new ComparisonFailure(
                        message,
                        Xon.toXon(expected),
                        Xon.toXon(actual)
                );
            }
        }
    }

    public static <T> void assertListEquals(String message, List<T> expected, List<T> actual) {
        if(expected==actual) return;
        if(expected==null || actual==null || expected.size()!=actual.size()) {
            throw new ComparisonFailure(
                    message,
                    Xon.toXon(expected),
                    Xon.toXon(actual)
            );
        }
        for(int i=0; i<expected.size() && i<actual.size(); i++) {
            try {
                assertDeepEquals(message+" ["+i+"]",expected.get(i),actual.get(i));
            }
            catch(ComparisonFailure e) {
                throw new ComparisonFailure(
                        message,
                        Xon.toXon(expected),
                        Xon.toXon(actual)
                );
            }
        }
    }

    public static <K,V> void assertMapEquals(String message, Map<K,V> expected, Map<K,V> actual) {
        if(expected==actual) return;
        if(expected==null || actual==null || expected.size()!=actual.size()) {
            throw new ComparisonFailure(
                    message,
                    Xon.toXon(expected),
                    Xon.toXon(actual)
            );
        }
        for(Map.Entry<K,V> x: (Iterable<Map.Entry<K,V>>)expected) {
            try {
                assertDeepEquals(message+" ["+Xon.toXon(x.getKey())+"]",x.getValue(),actual.get(x.getKey()));
            }
            catch(ComparisonFailure e) {
                throw new ComparisonFailure(
                        message,
                        Xon.toXon(expected),
                        Xon.toXon(actual)
                );
            }
        }
    }

    public static <T> void assertDeepEquals(String message, T expected, T actual) {
        if(expected==actual) return;
        if(expected==null || actual==null) {
            throw new ComparisonFailure(
                    message,
                    Xon.toXon(expected),
                    Xon.toXon(actual)
            );
        }
        if(expected instanceof Object[]) {
            if(!(actual instanceof Object[]))
                throw new ComparisonFailure(
                        message,
                        Xon.toXon(expected),
                        Xon.toXon(actual)
                );
            assertArrayEquals(message, (Object[]) expected, (Object[]) actual);
            return;
        }
        if(expected instanceof List) {
            if(!(actual instanceof List))
                throw new ComparisonFailure(
                        message,
                        Xon.toXon(expected),
                        Xon.toXon(actual)
                );
            assertListEquals(message, (List<Object>) expected, (List<Object>) actual);
            return;
        }
        if(expected instanceof Map) {
            if(!(actual instanceof Map))
                throw new ComparisonFailure(
                        message,
                        Xon.toXon(expected),
                        Xon.toXon(actual)
                );
            assertMapEquals(message, (Map<Object,Object>) expected, (Map<Object,Object>) actual);
            return;
        }
        if(expected instanceof CharSequence) {
            if(!(actual instanceof CharSequence)) {
                throw new ComparisonFailure(
                        message,
                        Xon.toXon(expected),
                        Xon.toXon(actual)
                );
            }
            if(!expected.equals(actual)) {
                throw new ComparisonFailure(
                        message,
                        Xon.toXon(expected),
                        Xon.toXon(actual)
                );
            }
            return;
        }
        if(expected instanceof Number) {
            if(!(actual instanceof Number)) {
                throw new ComparisonFailure(
                        message,
                        Xon.toXon(expected),
                        Xon.toXon(actual)
                );
            }
            if(((Number) expected).doubleValue() != ((Number) actual).doubleValue()) {
                throw new ComparisonFailure(
                        message,
                        Xon.toXon(expected),
                        Xon.toXon(actual)
                );
            }
            return;
        }
        assertEquals(message,expected,actual);
    }

}
