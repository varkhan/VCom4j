package net.varkhan.base.conversion.formats;

import junit.framework.ComparisonFailure;
import junit.framework.TestCase;

import java.io.*;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;


/**
 * <b></b>.
 * <p/>
 *
 * @author varkhan
 * @date 3/31/12
 * @time 3:11 PM
 */
public class XonTest extends TestCase {

    public void testEquals() throws Exception {
        assertTrue("null | null",Xon.equals(null,null));
        assertTrue("true | true",Xon.equals(true,true));
        assertTrue("false | false",Xon.equals(false,false));
        assertFalse("true | false", Xon.equals(true, false));
        assertTrue("1 | 1.0", Xon.equals(1, 1.0));
        assertFalse("1 | 1.000001", Xon.equals(1, 1.000001));
        assertTrue("'' | ''", Xon.equals("", ""));
        assertTrue("'d' | 'd'", Xon.equals("d", "d"));
        assertFalse("'d' | ''", Xon.equals("d", ""));
        assertFalse("'d' | 'e'", Xon.equals("d", "e"));
        assertFalse("'d' | 'dd'",Xon.equals("d","dd"));
        assertTrue("[]|[]", Xon.equals(new Object[] { }, new Object[] { }));
        assertFalse("[null]|[]", Xon.equals(new Object[] { null }, new Object[] { }));
        assertTrue("[null, false, true, 1, 1.0, \"a\"]|[null, false, true, 1, 1.0, \"a\"]", Xon.equals(
                new Object[] { null, false, true, 1, 1.0, "a" },
                new Object[] { null, false, true, 1, 1.0, "a" }
                                                                                                      ));
        assertTrue("()|()", Xon.equals(Arrays.asList(), Arrays.asList()));
        assertFalse("(null)|()", Xon.equals(Arrays.asList((Object) null), Arrays.asList()));
        assertTrue("(null, false, true, 1, 1.0, \"a\")|(null, false, true, 1, 1.0, \"a\")", Xon.equals(
                Arrays.asList(null, false, true, 1, 1.0, "a"),
                Arrays.asList(null, false, true, 1, 1.0, "a")
                                                                                                      ));
        assertTrue("{}|{}", Xon.equals(asMap(CharSequence.class, Object.class), asMap(CharSequence.class, Object.class)));
        assertTrue("{\"n\",null,\"f\",false,\"t\",true,\"1\",1,\"1.0\",1.0,\"a\",\"a\"}|{\"n\",null,\"f\",false,\"t\",true,\"1\",1,\"1.0\",1.0,\"a\",\"a\"}",
                   Xon.equals(
                           asMap(CharSequence.class,Object.class, "n",null,"f",false,"t",true,"1",1,"1.0",1.0,"a","a"),
                           asMap(CharSequence.class,Object.class, "n",null,"f",false,"t",true,"1",1,"1.0",1.0,"a","a")
                             ));
        assertTrue("{...}|{...}",
                   Xon.equals(
                           asMap(CharSequence.class,Object.class, "n",null,"f",false,"t",true,"1",1,"1.0",1.0,"a","a",
                                 "x",new Object[] { null, false, true, 1, 1.0, "a" },
                                 "y",Arrays.asList(null, false, true, 1, 1.0, "a")),
                           asMap(CharSequence.class,Object.class, "n",null,"f",false,"t",true,"1",1,"1.0",1.0,"a","a",
                                 "x",new Object[] { null, false, true, 1, 1.0, "a" },
                                 "y",Arrays.asList(null, false, true, 1, 1.0, "a"))
                             ));
    }

    public void testWrite() throws Exception {
        assertEquals("{\"a\":2,\"b\":true,\"c\",\"d\":\"D\"}", Xon.write(new StringBuilder(), asMap(String.class, Object.class, "a", 2, "b", true, "c", null, "d", "D")).toString());
        assertEquals("(\"a\",\"b\",\"c\",\"d\")",Xon.write(new StringBuilder(), Arrays.asList("a", "b", "c", "d")).toString());
        assertEquals("[\"a\",\"b\",\"c\",\"d\"]", Xon.write(new StringBuilder(), new Object[] { "a", "b", "c", "d" }).toString());
        assertEquals("\"abc _\\t\\f\\r\\u4a9dgh\\u0003\"",Xon.write(new StringBuilder(), "abc _\t\f\r\u4a9dgh\3").toString());
        assertEquals("{\"a\":2,\"b\":true,\"c\",\"d\":(\"x\",\"y\",1.2,\"abc _\\t\\f\\r\\u4a9dgh\\u0003\"),\"e\":[\"m\",1,2,3,0.14]}",
                     Xon.write(new StringBuilder(),
                               asMap(String.class, Object.class, "a", 2, "b", true, "c", null, "d", Arrays.asList("x", "y", 1.2, "abc _\t\f\r\u4a9dgh\3"), "e", new Object[] { "m", 1, 2, 3, .14 })
                              ).toString());
    }

    public void testWriteObject() throws Exception {
        assertEquals("\"a\":2,\"b\":true,\"c\",\"d\":\"D\"", Xon.writeObject(new StringBuilder(), asMap(String.class, Object.class, "a", 2, "b", true, "c", null, "d", "D")).toString());
    }

    public void testWriteCollec() throws Exception {
        assertEquals("\"a\",\"b\",\"c\",\"d\"",Xon.writeCollec(new StringBuilder(), Arrays.asList("a", "b", "c", "d")).toString());
    }

    public void testWriteVector() throws Exception {
        assertEquals("", Xon.writeVector(new StringBuilder()).toString());
        assertEquals("\"a\",\"b\",\"c\",\"d\"",Xon.writeVector(new StringBuilder(), "a", "b", "c", "d").toString());
    }

    public void testWriteString() throws Exception {
        assertEquals("", Xon.writeString(new StringBuilder(), "").toString());
        assertEquals("abc _\\t\\f\\r\\u4a9dgh\\u0003",Xon.writeString(new StringBuilder(),"abc _\t\f\r\u4a9dgh\3").toString());
    }

    public void testReadObject() throws Exception {
        String json="{\"a\":2,\"b\":true,\"c\",\"d\":(\"x\",\"y\",1.2,\"abc _\\t\\f\\r\\u4a9dgh\\u0003\"), \"e\":[\"m\",1,2,3,.14]}";
        assertEquals(json,
                     Xon.write(Xon.read(new StringReader(json))),
                     Xon.write(asMap(String.class, Object.class, "a", 2, "b", true, "c", null, "d", Arrays.asList("x", "y", 1.2, "abc _\t\f\r\u4a9dgh\3"), "e", new Object[] { "m", 1, 2, 3, .14 }))
                    );
    }

    public void testReadBoolean() throws Exception {
        assertEquals(true,Xon.readBoolean(new StringReader("true")));
        assertEquals(false,Xon.readBoolean(new StringReader("false")));
    }

    public void testReadNumber() throws Exception {
        assertEquals(1L,Xon.readNumber(new StringReader("1")));
        assertEquals(1.0,Xon.readNumber(new StringReader("1.0")));
    }

    public void testReadString() throws Exception {
        assertEquals("abc _\t\f\r\u4a9dgh\3",Xon.readString(new StringReader("\"abc _\\t\\f\\r\\u4a9dgh\\u0003\"")));
    }

    public void testReadCollec() throws Exception {
        String json="(\"x\",\"y\",1.2,\"abc _\\t\\f\\r\\u4a9dgh\\u0003\")";
        assertListEquals(json,
                         Arrays.asList((Object) "x", "y", 1.2, "abc _\t\f\r\u4a9dgh\3"),
                         Xon.readCollec(new StringReader(json))
                        );
    }

    public void testReadArray() throws Exception {
        String json="[\"x\",\"y\",1.2,\"abc _\\t\\f\\r\\u4a9dgh\\u0003\"]";
        assertArrayEquals(json,
                         new Object[]{"x", "y", 1.2, "abc _\t\f\r\u4a9dgh\3"},
                         Xon.readVector(new StringReader(json))
                        );
    }

    public void testReadMap() throws Exception {
        String json="{\"a\":2,\"b\":true,\"c\",\"d\":null}";
        assertMapEquals(json,
                        asMap(CharSequence.class, Object.class, "a", 2, "b", true, "c", null, "d", null),
                        Xon.readObject(new StringReader(json))
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
        Object o1 = Xon.read(r1);
        Xon.write(System.out, o1);
        StringWriter w1 = new StringWriter();
        Xon.write(w1, o1);
        Reader r2 = new StringReader(w1.toString());
        Object o2 = Xon.read(r2);
        StringWriter w2 = new StringWriter();
        Xon.write(w2, o2);
        assertEquals("ReadWrite",w1.toString(),w2.toString());
    }


    public void testPerf() throws IOException {
        String json="{\"a\":2,\"b\":true,\"c\",\"d\":(\"x\",\"y\",1.2,\"abc _\\t\\f\\r\\u4a9dgh\\u0003\"), \"e\":[\"m\",1,2,3,.14]}";
        int n=10000000;
        int q=0;
        for(int i=0; i<n/100; i++) {
            if(Xon.read(json)!=null) q++;
        }
        assertTrue(q>0);
        q=0;
        long t0 = System.currentTimeMillis();
        for(int i=0; i<n; i++) {
            if(Xon.read(json)!=null) q++;
        }
        long t1 = System.currentTimeMillis();
        System.out.println("Read "+q+"/"+n+" XON strings in "+(t1-t0)+"ms, "+(1000000*(t1-t0)/n)+"ns / string");
    }





    public static <T> void assertArrayEquals(String message, T[] expected, T[] actual) {
        if(expected==actual) return;
        if(expected==null || actual==null || expected.length!=actual.length) {
            throw new ComparisonFailure(
                    message,
                    Xon.write(expected),
                    Xon.write(actual)
            );
        }
        for(int i=0; i<expected.length && i<actual.length; i++) {
            try {
                assertDeepEquals(message+" ["+i+"]",expected[i],actual[i]);
            }
            catch(ComparisonFailure e) {
                throw new ComparisonFailure(
                        message,
                        Xon.write(expected),
                        Xon.write(actual)
                );
            }
        }
    }

    public static <T> void assertListEquals(String message, List<T> expected, List<T> actual) {
        if(expected==actual) return;
        if(expected==null || actual==null || expected.size()!=actual.size()) {
            throw new ComparisonFailure(
                    message,
                    Xon.write(expected),
                    Xon.write(actual)
            );
        }
        for(int i=0; i<expected.size() && i<actual.size(); i++) {
            try {
                assertDeepEquals(message+" ["+i+"]",expected.get(i),actual.get(i));
            }
            catch(ComparisonFailure e) {
                throw new ComparisonFailure(
                        message,
                        Xon.write(expected),
                        Xon.write(actual)
                );
            }
        }
    }

    public static <K,V> void assertMapEquals(String message, Map<K,V> expected, Map<K,V> actual) {
        if(expected==actual) return;
        if(expected==null || actual==null || expected.size()!=actual.size()) {
            throw new ComparisonFailure(
                    message,
                    Xon.write(expected),
                    Xon.write(actual)
            );
        }
        for(Map.Entry<K,V> x: expected.entrySet()) {
            try {
                assertDeepEquals(message+" ["+Xon.write(x.getKey())+"]",x.getValue(),actual.get(x.getKey()));
            }
            catch(ComparisonFailure e) {
                throw new ComparisonFailure(
                        message,
                        Xon.write(expected),
                        Xon.write(actual)
                );
            }
        }
    }

    public static <T> void assertDeepEquals(String message, T expected, T actual) {
        if(expected==actual) return;
        if(expected==null || actual==null) {
            throw new ComparisonFailure(
                    message,
                    Xon.write(expected),
                    Xon.write(actual)
            );
        }
        if(expected instanceof Object[]) {
            if(!(actual instanceof Object[]))
                throw new ComparisonFailure(
                        message,
                        Xon.write(expected),
                        Xon.write(actual)
                );
            assertArrayEquals(message, (Object[]) expected, (Object[]) actual);
            return;
        }
        if(expected instanceof List) {
            if(!(actual instanceof List))
                throw new ComparisonFailure(
                        message,
                        Xon.write(expected),
                        Xon.write(actual)
                );
            assertListEquals(message, (List<Object>) expected, (List<Object>) actual);
            return;
        }
        if(expected instanceof Map) {
            if(!(actual instanceof Map))
                throw new ComparisonFailure(
                        message,
                        Xon.write(expected),
                        Xon.write(actual)
                );
            assertMapEquals(message, (Map<Object,Object>) expected, (Map<Object,Object>) actual);
            return;
        }
        if(expected instanceof CharSequence) {
            if(!(actual instanceof CharSequence)) {
                throw new ComparisonFailure(
                        message,
                        Xon.write(expected),
                        Xon.write(actual)
                );
            }
            if(!expected.equals(actual)) {
                throw new ComparisonFailure(
                        message,
                        Xon.write(expected),
                        Xon.write(actual)
                );
            }
            return;
        }
        if(expected instanceof Number) {
            if(!(actual instanceof Number)) {
                throw new ComparisonFailure(
                        message,
                        Xon.write(expected),
                        Xon.write(actual)
                );
            }
            if(((Number) expected).doubleValue() != ((Number) actual).doubleValue()) {
                throw new ComparisonFailure(
                        message,
                        Xon.write(expected),
                        Xon.write(actual)
                );
            }
            return;
        }
        assertEquals(message,expected,actual);
    }

    @SuppressWarnings("unchecked")
    public static <K,V> java.util.Map<K,V> asMap(final Class<K> kclass, final Class<V> vclass, final Object... values) {
        java.util.Map<K,V> map = new LinkedHashMap<K,V>();
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
