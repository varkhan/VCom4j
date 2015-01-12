package net.varkhan.base.conversion.character;

import junit.framework.TestCase;


public class AsciiStringTest extends TestCase {

    public void testLength() throws Exception {
        assertEquals("length(\"\")",0,new AsciiString(new byte[0],0,0).length());
        assertEquals("length(\"Foo\")",3,new AsciiString("Foo bar baz".toCharArray(),0,3).length());
        assertEquals("length(\"Foo bar baz\")",11,new AsciiString("Foo bar baz".toCharArray(),0,11).length());
        assertEquals("length(\"Foo bar $$\u00fe\")",11,new AsciiString("Foo bar $$þ".toCharArray(),0,11,false).length());
        assertEquals("length(\"Foo bar $$\u00fe\")",11,new AsciiString("Foo bar $$þ".toCharArray(),0,11,true).length());
    }

    public void testCharAt() throws Exception {
        assertEquals("charAt(\"Foo\",3)",'o',new AsciiString("Foo bar baz".toCharArray(),0,3).charAt(2));
        assertEquals("charAt(\"Foo bar baz\",7)",' ',new AsciiString("Foo bar baz".toCharArray(),0,11).charAt(7));
        assertEquals("charAt(\"Foo bar $$\u00fe\",10)",0xfe,(int)new AsciiString("Foo bar $$þ".toCharArray(),0,11,false).charAt(10));
        assertEquals("charAt(\"Foo bar $$\u00fe\",10)",0x7e,(int)new AsciiString("Foo bar $$þ".toCharArray(),0,11,true).charAt(10));
    }

    public void testEquals() throws Exception {
        assertTrue("equals(\"\")", new AsciiString(new byte[0], 0, 0).equals(new AsciiString(new byte[0], 0, 0)));
        assertTrue("equals(\"Foo\")", new AsciiString("Foo".toCharArray(), 0, 3).equals(new AsciiString("Foo bar baz".toCharArray(), 0, 3)));
        assertFalse("equals(\"Foo\")", new AsciiString(new byte[0], 0, 0).equals(new AsciiString("Foo bar baz".toCharArray(), 0, 3)));
        assertFalse("equals(\"Foo\")", new AsciiString("Foo".toCharArray(), 0, 3).equals(new AsciiString(new byte[0], 0, 0)));
        assertTrue("equals(\"Foo bar baz\")", new AsciiString("Foo bar baz".toCharArray(), 0, 11).equals(new AsciiString("Foo bar baz".toCharArray(), 0, 11)));
        assertTrue("equals(\"Foo bar $$\u00fe\")", new AsciiString("Foo bar $$þ".toCharArray(), 0, 11, false).equals(new AsciiString("Foo bar $$þ".toCharArray(), 0, 11, false)));
        assertTrue("equals(\"Foo bar $$\u00fe\")", new AsciiString("Foo bar $$þ".toCharArray(), 0, 11, true).equals(new AsciiString("Foo bar $$þ".toCharArray(), 0, 11, true)));
        assertFalse("equals(\"Foo bar $$\u00fe\")", new AsciiString("Foo bar $$þ".toCharArray(), 0, 11, true).equals(new AsciiString("Foo bar $$þ".toCharArray(), 0, 11, false)));
        assertFalse("equals(\"Foo bar $$\u00fe\")", new AsciiString("Foo bar $$þ".toCharArray(), 0, 11, false).equals(new AsciiString("Foo bar $$þ".toCharArray(), 0, 11, true)));
    }

    public void testHashCode() throws Exception {
        assertEquals("hashCode(\"\")",0,new AsciiString(new byte[0],0,0).hashCode());
        assertEquals("hashCode(\"Foo\")",new AsciiString("Foo bar baz".toCharArray(),0,3).hashCode(),new AsciiString("Foo bar baz".toCharArray(),0,3).hashCode());
        assertEquals("hashCode(\"Foo bar baz\")",new AsciiString("Foo bar baz".toCharArray(),0,11).hashCode(),new AsciiString("Foo bar baz".toCharArray(),0,11).hashCode());
        assertEquals("hashCode(\"Foo bar $$\u00fe\")",new AsciiString("Foo bar $$þ".toCharArray(),0,11,false).hashCode(),new AsciiString("Foo bar $$þ".toCharArray(),0,11,false).hashCode());
        assertEquals("hashCode(\"Foo bar $$\u00fe\")",new AsciiString("Foo bar $$þ".toCharArray(),0,11,true).hashCode(),new AsciiString("Foo bar $$þ".toCharArray(),0,11,true).hashCode());
        assertEquals("hashCode(\"Foo bar $$\u00fe\")",128+new AsciiString("Foo bar $$þ".toCharArray(), 0, 11, false).hashCode(), new AsciiString("Foo bar $$þ".toCharArray(), 0, 11, true).hashCode());
    }

    public void testToString() throws Exception {
        assertEquals("length(\"\")","",new AsciiString(new byte[0],0,0).toString());
        assertEquals("length(\"Foo\")","Foo",new AsciiString("Foo bar baz".toCharArray(),0,3).toString());
        assertEquals("length(\"Foo bar baz\")","Foo bar baz",new AsciiString("Foo bar baz".toCharArray(),0,11).toString());
        assertEquals("length(\"Foo bar $$\u00fe\")","Foo bar $$þ",new AsciiString("Foo bar $$þ".toCharArray(),0,11,false).toString());
        assertEquals("length(\"Foo bar $$\u00fe\")","Foo bar $$~",new AsciiString("Foo bar $$þ".toCharArray(),0,11,true).toString());
    }

    public void testSubSequence() throws Exception {
        assertEquals("subSequence(\"\",0,0)",new AsciiString(new byte[0],0,0),new AsciiString(new byte[0],0,0).subSequence(0,0));
        assertEquals("subSequence(\"Foo bar baz\", 0, 0)",new AsciiString(new byte[0],0,0),new AsciiString("Foo bar baz".toCharArray(),0,11).subSequence(0, 0));
        assertEquals("subSequence(\"Foo bar baz\",11,11)",new AsciiString(new byte[0],0,0),new AsciiString("Foo bar baz".toCharArray(),0,11).subSequence(11, 11));
        assertEquals("subSequence(\"Foo bar baz\", 0, 4)",new AsciiString("Foo ".toCharArray()),new AsciiString("Foo bar baz".toCharArray(),0,11).subSequence(0, 4));
        assertEquals("subSequence(\"Foo bar baz\", 4, 8)",new AsciiString("bar ".toCharArray()),new AsciiString("Foo bar baz".toCharArray(),0,11).subSequence(4, 8));
        assertEquals("subSequence(\"Foo bar baz\", 8,11)",new AsciiString("baz".toCharArray()),new AsciiString("Foo bar baz".toCharArray(),0,11).subSequence(8, 11));
        assertEquals("subSequence(\"Foo bar baz\", 0,11)",new AsciiString("Foo bar baz".toCharArray()),new AsciiString("Foo bar baz".toCharArray(),0,11).subSequence(0,11));
        assertEquals("subSequence(\"Foo bar $$\u00fe\",10)",0xfe,(int)new AsciiString("Foo bar $$þ".toCharArray(),0,11,false).subSequence(10,11).charAt(0));
        assertEquals("subSequence(\"Foo bar $$\u00fe\",10)",0x7e,(int)new AsciiString("Foo bar $$þ".toCharArray(),0,11,true).subSequence(10,11).charAt(0));
    }

}