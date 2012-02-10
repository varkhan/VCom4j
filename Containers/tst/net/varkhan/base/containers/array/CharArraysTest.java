/**
 *
 */
package net.varkhan.base.containers.array;

import junit.framework.TestCase;


/**
 * @author varkhan
 * @date Mar 12, 2009
 * @time 6:07:21 PM
 */
public class CharArraysTest extends TestCase {


    public void testTr() {
        assertEquals("tr(\"abcdefghijkl\",45djlk\",\"67DJL\")", "abcDefghiJkL", CharArrays.tr("abcdefghijkl", "45djlk".toCharArray(), "67DJL".toCharArray()));
        assertEquals("tr(\"abcdefghijkl\",45djlk\",\"67DJL\")", 4, CharArrays.tr("abcdefghijkl".toCharArray(), "45djlk".toCharArray(), "67DJL".toCharArray()));
    }

    public void testRepl() {
        assertEquals(CharArrays.repl("tototaaatatiti", "taa", "ghi"), "totoghiatatiti");
        assertEquals(CharArrays.repl("tototaaatatiti", "ta", "gh"), "totoghaaghtiti");
        assertEquals(CharArrays.repl("tototaaatatiti", new String[] { "taa", "ta" }, new String[] { "ghi", "gh" }), "totoghiaghtiti");
        assertEquals(CharArrays.repl("tototaaatatiti", new String[] { "ta", "taa" }, new String[] { "gh", "ghi" }), "totoghaaghtiti");
        assertEquals(CharArrays.repl("tototaaatatiti", new String[] { "ta", "taa", "tit" }, new String[] { "gh", "ghi", "ff" }), "totoghaaghffi");
        assertEquals(CharArrays.repl("tototaaatatiti", new String[] { "ta", "taa", "tip" }, new String[] { "gh", "ghi", "ff" }), "totoghaaghtiti");
    }

    public void testChop() {
        assertEquals("chop(\"tata:titi:tutu\",:)", "tata", CharArrays.chop("tata:titi:tutu", ':'));
        assertEquals("chop(\"tata:titi:tutu\"[6,13],:)", "iti", new String(CharArrays.chop("tata:titi:tutu".toCharArray(), 6, 7, ':')));
    }

    public void testSplit() {
        assertEqualsArrays("split(\"tata:titi:tutu\",:,2)", new String[] { "tata", "titi:tutu" }, CharArrays.split("tata:titi:tutu", ':', 2));
        assertEqualsArrays("split(\"tata:titi:tutu\"[6,13],:,2)", new String[] { "iti", "tut" }, toString(CharArrays.split("tata:titi:tutu".toCharArray(), 6, 7, ':', 2)));
        assertEqualsArrays("split(\"tata:titi:tutu\",:,+INF)", new String[] { "tata", "titi", "tutu" }, CharArrays.split("tata:titi:tutu", ':', Integer.MAX_VALUE));
        assertEqualsArrays("split(\"tata:titi:tutu\",:,+INF)", new String[] { "tata", "titi", "tutu" }, toString(CharArrays.split("tata:titi:tutu".toCharArray(), ':', Integer.MAX_VALUE)));
        assertEqualsArrays("split(\"tata:titi:tutu:\",:,+INF)", new String[] { "tata", "titi", "tutu", "" }, CharArrays.split("tata:titi:tutu:", ':', Integer.MAX_VALUE));
    }

    public void testFormat() {
        assertEquals("format(\"%s\",\"toto\"", "toto", CharArrays.format("%s", "toto"));
        assertEquals("format(\"%<.12s\",\"toto\"", "toto........", CharArrays.format("%<.12s", "toto"));
        assertEquals("format(\"%>.12s\",\"toto\"", "........toto", CharArrays.format("%>.12s", "toto"));
        assertEquals("format(\"% .2u\",7", "111", CharArrays.format("% .2u", 7));
        assertEquals("format(\"%04.2u\",7", "0111", CharArrays.format("%04.2u", 7));
        assertEquals("format(\"%<04.2u\",7", "1110", CharArrays.format("%<04.2u", 7));
    }

    private static String[] toString(char[][] a) {
        if(a==null) return null;
        if(a.length==0) return new String[0];
        String[] s=new String[a.length];
        for(int i=0;i<a.length;i++) {
            s[i]=new String(a[i]);
        }
        return s;
    }

    private static String toString(String[] a) {
        if(a==null) return "null";
        if(a.length==0) return "[]";
        return "[\""+StringArrays.join("\",\"", a)+"\"]";
    }

    private static void assertEqualsArrays(String msg, String[] exp, String[] ret) {
        assertEquals(msg, toString(exp), toString(ret));
    }

}
