package net.varkhan.base.extensions.type;

import junit.framework.ComparisonFailure;
import junit.framework.TestCase;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class KindTest extends TestCase {



    public void testValue() {
        Kind.ValueKind<Boolean> kind = new Kind.ValueKind<Boolean>("kind", Boolean.class) {};
        assertEquals("kind.toString()","kind",kind.toString());
        assertTrue("kind.isAssignableFrom(kind)", kind.isAssignableFrom(kind));
        assertTrue("kind.assign(true)",kind.assignFrom(kind).apply(true));
        assertFalse("kind.assign(false)",kind.assignFrom(kind).apply(false));
        try {
            kind.assignFrom(kind).apply(null);
            fail("kind.assign(null)");
        }
        catch (ClassCastException e) {
            // success
        }
    }

    public void testNullable() {
        Kind.ValueKind<Boolean> kind = new Kind.ValueKind<Boolean>("kind", Boolean.class) {};
        assert kind.isAssignableFrom(kind);
        assert kind.assignFrom(kind).apply(true);
        assert !kind.assignFrom(kind).apply(false);
        assert "kind".equals(kind.toString());
        Kind.Nullable<Boolean> nulltype = Kind.nullable(kind);
        assertEquals("nullable(kind).toString()","nullable<kind>",nulltype.toString());
        assertFalse("kind.isAssignableFrom(nullable(kind))",kind.isAssignableFrom(nulltype));
        assertTrue("nullable(kind).isAssignableFrom(kind)", nulltype.isAssignableFrom(kind));
        assertTrue("nullable(kind).isAssignableFrom(nullable(kind))", nulltype.isAssignableFrom(nulltype));
        assertTrue("nullable(kind).assign(true)",nulltype.assignFrom(kind).apply(true));
        assertFalse("nullable(kind).assign(false)",nulltype.assignFrom(kind).apply(false));
        assertNull("nullable(kind).assign(null)",nulltype.assignFrom(kind).apply(null));
    }

    public void testBoolean() {
        Kind.Primitive<Boolean> kind = new Kind.Primitive<Boolean>("bool", Boolean.class, boolean.class) {
        };
        assertTrue("bool.isAssignableFrom(bool)", kind.isAssignableFrom(kind));
        assertTrue("bool.assign(true)", kind.assignFrom(kind).apply(true));
        assertFalse("bool.assign(false)", kind.assignFrom(kind).apply(false));
        try {
            kind.assignFrom(kind).apply(null);
            fail("bool.assign(null)");
        } catch (ClassCastException e) {
            // success
        }
    }

    public void testShort() {
        Kind.Primitive<Short> kind = new Kind.Primitive<Short>("short", Short.class, short.class) {};
        assertTrue("short.isAssignableFrom(short)",kind.isAssignableFrom(kind));
        assertEquals("short.assign(0)",0,(int)kind.assignFrom(kind).apply((short)0));
        assertEquals("short.assign(1)",1,(int)kind.assignFrom(kind).apply((short)1));
        assertEquals("short.assign(-1)",-1,(int)kind.assignFrom(kind).apply((short)-1));
        try {
            kind.assignFrom(kind).apply(null);
            fail("short.assign(null)");
        }
        catch (ClassCastException e) {
            // success
        }
    }

    public void testInt() {
        Kind.Primitive<Integer> kind = new Kind.Primitive<Integer>("int", Integer.class, int.class) {};
        assertTrue("int.isAssignableFrom(int)",kind.isAssignableFrom(kind));
        assertEquals("int.assign(0)",0,(int)kind.assignFrom(kind).apply(0));
        assertEquals("int.assign(1)",1,(int)kind.assignFrom(kind).apply(1));
        assertEquals("int.assign(-1)",-1,(int)kind.assignFrom(kind).apply(-1));
        try {
            kind.assignFrom(kind).apply(null);
            fail("int.assign(null)");
        }
        catch (ClassCastException e) {
            // success
        }
    }

    public void testLong() {
        Kind.Primitive<Long> kind = new Kind.Primitive<Long>("long", Long.class, long.class) {};
        assertTrue("long.isAssignableFrom(long)",kind.isAssignableFrom(kind));
        assertEquals("long.assign(0)",0L,(long)kind.assignFrom(kind).apply(0L));
        assertEquals("long.assign(1)",1L,(long)kind.assignFrom(kind).apply(1L));
        assertEquals("long.assign(-1)",-1L,(long)kind.assignFrom(kind).apply(-1L));
        try {
            kind.assignFrom(kind).apply(null);
            fail("long.assign(null)");
        }
        catch (ClassCastException e) {
            // success
        }
    }

    public void testFloat() {
        Kind.Primitive<Float> kind = new Kind.Primitive<Float>("float", Float.class, float.class) {};
        assertTrue("float.isAssignableFrom(float)",kind.isAssignableFrom(kind));
        assertEquals("float.assign(0)",0.0f, kind.assignFrom(kind).apply(0.0f),0);
        assertEquals("float.assign(1)",1.0f, kind.assignFrom(kind).apply(1.0f),0);
        assertEquals("float.assign(-1)",-1.23f, kind.assignFrom(kind).apply(-1.23f),0);
        try {
            kind.assignFrom(kind).apply(null);
            fail("float.assign(null)");
        }
        catch (ClassCastException e) {
            // success
        }
    }

    public void testDouble() {
        Kind.Primitive<Double> kind = new Kind.Primitive<Double>("double", Double.class, double.class) {};
        assertTrue("double.isAssignableFrom(double)",kind.isAssignableFrom(kind));
        assertEquals("double.assign(0)",0.0, kind.assignFrom(kind).apply(0.0),0);
        assertEquals("double.assign(1)",1.0, kind.assignFrom(kind).apply(1.0),0);
        assertEquals("double.assign(-1)",-1.23, kind.assignFrom(kind).apply(-1.23),0);
        try {
            kind.assignFrom(kind).apply(null);
            fail("double.assign(null)");
        }
        catch (ClassCastException e) {
            // success
        }
    }

    public void testString() {
        Kind.CharsKind<String> kind = new Kind.CharsKind<String>("string", String.class) {};
        assertTrue("string.isAssignableFrom(string)",kind.isAssignableFrom(kind));
        assertEquals("string.assign('')","",kind.assignFrom(kind).apply(""));
        assertEquals("string.assign('1')","1",kind.assignFrom(kind).apply("1"));
        try {
            kind.assignFrom(kind).apply(null);
            fail("string.assign(null)");
        }
        catch (ClassCastException e) {
            // success
        }

        Kind.CharsKind<StringBuilder> kind2 = new Kind.CharsKind<StringBuilder>("stringbuilder", StringBuilder.class) {};
        assertTrue("stringbuilder.isAssignableFrom(string)",kind2.isAssignableFrom(kind));
        assertEquals("stringbuilder.assign('')","",kind2.assignFrom(kind).apply("").toString());
        assertEquals("stringbuilder.assign('1')","1",kind2.assignFrom(kind).apply("1").toString());
        assertTrue("stringbuilder.isAssignableFrom(string)",kind.isAssignableFrom(kind2));
        assertEquals("stringbuilder.assign('')","",kind.assignFrom(kind2).apply(new StringBuilder()));
        assertEquals("stringbuilder.assign('1')","1",kind.assignFrom(kind2).apply(new StringBuilder("1")));
    }

    public void testArray() {
        Kind.CharsKind<String> elKind = new Kind.CharsKind<String>("string", String.class) {};
        Kind<String[]> kind = new Kind.ArrayKind<String>(elKind) {};
        assertEquals("ArrayKind<StringKind>","array<string>",kind.toString());
        assertArrayEquals("string.assign([])",new String[]{},kind.assignFrom(kind).apply(new String[] {}));
        assertArrayEquals("string.assign([''])",new String[] {""},kind.assignFrom(kind).apply(new String[] {""}));
        assertArrayEquals("string.assign(['0','1'])",new String[] {"0", "1"},kind.assignFrom(kind).apply(new String[] {"0", "1"}));
    }

    public void testBoolArray() {
        Kind<boolean[]> kind = new Kind.BoolArrayKind() {};
        assertEquals("ArrayKind<StringKind>","array<bool>",kind.toString());
        assertArrayEquals("string.assign([])",new boolean[]{},kind.assignFrom(kind).apply(new boolean[]{}));
        assertArrayEquals("string.assign([true])",new boolean[]{true},kind.assignFrom(kind).apply(new boolean[]{true}));
        assertArrayEquals("string.assign([true,false])",new boolean[]{true,false},kind.assignFrom(kind).apply(new boolean[]{true,false}));
    }

    public void testList() {
        Kind.CharsKind<String> stringKind = new Kind.CharsKind<String>("string", String.class) {};
        Kind<List<String>> listKind = new Kind.ListKind<String>(stringKind) {};
        assertEquals("ListKind<StringKind>","list<string>",listKind.toString());
    }

    public void testMap() {
        Kind.Primitive<Integer> intKind = new Kind.Primitive<Integer>("int", Integer.class, int.class) {};
        Kind.CharsKind<String> stringKind = new Kind.CharsKind<String>("string", String.class) {};
        Kind<Map<String,Integer>> et = new Kind.MapKind<String,Integer>(stringKind, intKind) {};
        assertEquals("MapKind<StringKind,Integer>","map<string,int>",et.toString());
    }

    private static <T> void assertArrayEquals(String message, T[] expected, T[] actual) {
        if (expected != null || actual != null) {
            if (expected == null || !Arrays.equals(expected, actual)) {
                String cleanMessage = message == null ? "" : message;
                throw new ComparisonFailure(cleanMessage, Arrays.toString(expected), Arrays.toString(actual));
            }
        }
    }

    private static void assertArrayEquals(String message, boolean[] expected, boolean[] actual) {
        if (expected != null || actual != null) {
            if (expected == null || !Arrays.equals(expected, actual)) {
                String cleanMessage = message == null ? "" : message;
                throw new ComparisonFailure(cleanMessage, Arrays.toString(expected), Arrays.toString(actual));
            }
        }

    }

}