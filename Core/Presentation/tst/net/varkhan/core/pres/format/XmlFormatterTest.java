package net.varkhan.core.pres.format;

import junit.framework.TestCase;


/**
 * <b></b>.
 * <p/>
 *
 * @author varkhan
 * @date 11/30/13
 * @time 6:07 PM
 */
public class XmlFormatterTest extends TestCase {

    public void testOpen() throws Exception {
        StringBuilder buf = new StringBuilder();
        XmlFormatter fmt = new XmlFormatter(buf);
        fmt.open();
        assertTrue(fmt.isOpen());
    }


    public void testClose() throws Exception {
        StringBuilder buf = new StringBuilder();
        XmlFormatter fmt = new XmlFormatter(buf);
        assertFalse("open()",fmt.isOpen());
        assertFalse("closed()",fmt.isClosed());
        fmt.open();
        assertTrue("open()",fmt.isOpen());
        assertFalse("closed()",fmt.isClosed());
        fmt.close();
        assertTrue("open()",fmt.isOpen());
        assertTrue("closed()",fmt.isClosed());
    }
    public void testAppend() throws Exception {
        StringBuilder buf = new StringBuilder();
        XmlFormatter fmt = new XmlFormatter(buf);
        fmt.open();
        fmt.append("foo");
        fmt.append("bar","baz");
        fmt.append("chewbacca",3,7);
        fmt.close();
        assertEquals("foobar\nbaz\nwbac",buf.toString());
    }

    public void testLn() throws Exception {
        StringBuilder buf = new StringBuilder();
        XmlFormatter fmt = new XmlFormatter(buf);
        fmt.open();
        fmt.append("foo");
        fmt.ln();
        fmt.append("bar");
        fmt.close();
        assertEquals("foo\nbar",buf.toString());
    }

    public void testCmnt() throws Exception {
        StringBuilder buf = new StringBuilder();
        XmlFormatter fmt = new XmlFormatter(buf);
        fmt.open();
        fmt.append("foo");
        fmt.cmnt("super-comment");
        fmt.append("bar");
        fmt.close();
        assertEquals("foo<!--super-comment-->bar",buf.toString());
    }

    public void testText() throws Exception {
        StringBuilder buf = new StringBuilder();
        XmlFormatter fmt = new XmlFormatter(buf);
        fmt.open();
        fmt.append("foo");
        fmt.text("text & signs < blurb");
        fmt.append("bar");
        fmt.close();
        assertEquals("footext &amp; signs &lt; blurbbar",buf.toString());
    }

    public void testElmt() throws Exception {
        StringBuilder buf = new StringBuilder();
        XmlFormatter fmt = new XmlFormatter(buf);
        fmt.open();
        fmt.append("foo");
        fmt.elmt("super-element");
        fmt.append("bar");
        fmt.close();
        assertEquals("foo<super-element/>bar",buf.toString());
    }

    public void testElmt_() throws Exception {
        StringBuilder buf = new StringBuilder();
        XmlFormatter fmt = new XmlFormatter(buf,false);
        fmt.open();
        fmt.append("foo");
        fmt.elmt_("super-element");
        fmt.append("bar");
        try {
            fmt.close();
            fail("Should catch unclosed element");
        }
        catch(IllegalStateException e) {
            // success
        }
        assertEquals("foo<super-element>bar",buf.toString());
        buf = new StringBuilder();
        fmt = new XmlFormatter(buf,true);
        fmt.open();
        fmt.append("foo");
        fmt.elmt_("super-element");
        fmt.append("bar");
        fmt.close();
        assertEquals("foo<super-element>bar</super-element>",buf.toString());
    }

    public void test_Elmt() throws Exception {
        StringBuilder buf = new StringBuilder();
        XmlFormatter fmt = new XmlFormatter(buf);
        fmt.open();
        fmt.append("foo");
        try {
            fmt._elmt("super-element");
            fail("Should catch unopened element");
        }
        catch(IllegalStateException e) {
            // success
        }
        buf = new StringBuilder();
        fmt = new XmlFormatter(buf);
        fmt.open();
        fmt.append("foo");
        fmt.elmt_("super-element");
        fmt.append("bar");
        fmt._elmt("super-element");
        fmt.append("baz");
        fmt.close();
        assertEquals("foo<super-element>bar</super-element>baz",buf.toString());
    }

    public void test_Within() throws Exception {
        StringBuilder buf = new StringBuilder();
        XmlFormatter fmt = new XmlFormatter(buf,false);
        fmt.open();
        fmt.append("foo");
        fmt.elmt_("super-element1");
        fmt.append("bar");
        fmt.elmt_("super-element2");
        fmt.append("baz");
        fmt.elmt_("super-element3");
        fmt.append("whee");
        fmt._within("super-element1");
        fmt._elmt("super-element1");
        fmt.append("whap");
        fmt.close();
        assertEquals("foo<super-element1>bar<super-element2>baz<super-element3>whee</super-element3></super-element2></super-element1>whap",buf.toString());
    }

    public void test_All() throws Exception {
        StringBuilder buf = new StringBuilder();
        XmlFormatter fmt = new XmlFormatter(buf,false);
        fmt.open();
        fmt.append("foo");
        fmt.elmt_("super-element1");
        fmt.append("bar");
        fmt.elmt_("super-element2");
        fmt.append("baz");
        fmt.elmt_("super-element3");
        fmt.append("whee");
        fmt._all();
        fmt.append("whap");
        fmt.close();
        assertEquals("foo<super-element1>bar<super-element2>baz<super-element3>whee</super-element3></super-element2></super-element1>whap",buf.toString());
    }
}
