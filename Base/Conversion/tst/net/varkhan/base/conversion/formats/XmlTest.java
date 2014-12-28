package net.varkhan.base.conversion.formats;

import junit.framework.TestCase;

import java.io.StringReader;
import java.util.LinkedHashMap;
import java.util.Map;


public class XmlTest extends TestCase {

    public void testWriteElmt() throws Exception {
        StringBuilder buf = new StringBuilder();
        Xml.writeElmt(buf,"foo",null,asMap("a","b","c","","d",null));
        assertEquals("empty attr","<foo a=\"b\" c/>",buf.toString());
        buf.setLength(0);
        Xml.writeElmt(buf,"foo",null,asMap());
        assertEquals("empty bare","<foo/>",buf.toString());
        buf.setLength(0);
        Xml.writeElmt(buf,"foo","bar",asMap("a","b","c","","d",null));
        assertEquals("value attr","<foo a=\"b\" c>bar</foo>",buf.toString());
        buf.setLength(0);
        Xml.writeElmt(buf,"foo","bar",asMap());
        assertEquals("value bare","<foo>bar</foo>",buf.toString());
    }

    public void testWriteElmtA() throws Exception {
        StringBuilder buf = new StringBuilder();
        Xml.writeElmt(buf,"foo",null,new Object[]{"a","b","c","","d",null});
        assertEquals("empty attr","<foo a=\"b\" c/>",buf.toString());
        buf.setLength(0);
        Xml.writeElmt(buf,"foo",null);
        assertEquals("empty bare","<foo/>",buf.toString());
        buf.setLength(0);
        Xml.writeElmt(buf,"foo","bar",new Object[]{"a","b","c","","d",null});
        assertEquals("value attr","<foo a=\"b\" c>bar</foo>",buf.toString());
        buf.setLength(0);
        Xml.writeElmt(buf,"foo","bar");
        assertEquals("value bare","<foo>bar</foo>",buf.toString());
        buf.setLength(0);
        Xml.writeElmt(buf,"foo","bar",new Object[]{"a","b\"x\'y&z<w>","c","","d",null});
        assertEquals("value attr","<foo a=\"b&quot;x'y&amp;z&lt;w&gt;\" c>bar</foo>",buf.toString());
    }

    public void testWriteElmtOpen() throws Exception {
        StringBuilder buf = new StringBuilder();
        Xml.writeElmtOpen(buf,"foo",asMap("a","b","c","","d",null));
        assertEquals("attr","<foo a=\"b\" c>",buf.toString());
        buf.setLength(0);
        Xml.writeElmtOpen(buf,"foo",asMap());
        assertEquals("bare","<foo>",buf.toString());
    }

    public void testWriteElmtOpenA() throws Exception {
        StringBuilder buf = new StringBuilder();
        Xml.writeElmtOpen(buf,"foo",new Object[]{"a","b","c","","d",null});
        assertEquals("attr","<foo a=\"b\" c>",buf.toString());
        buf.setLength(0);
        Xml.writeElmtOpen(buf,"foo");
        assertEquals("bare","<foo>",buf.toString());
    }

    public void testWriteElmtClose() throws Exception {
        StringBuilder buf = new StringBuilder();
        Xml.writeElmtClose(buf,"foo");
        assertEquals("bare","</foo>",buf.toString());
    }

    public void testWriteAttr() throws Exception {
        StringBuilder buf = new StringBuilder();
        Xml.writeAttr(buf,asMap("a","b","c","","d",null));
        assertEquals("attr"," a=\"b\" c",buf.toString());
    }

    public void testWriteAttrA() throws Exception {
        StringBuilder buf = new StringBuilder();
        Xml.writeAttr(buf,new Object[]{"a","b","c","","d",null});
        assertEquals("attr"," a=\"b\" c",buf.toString());
    }

    public void testWriteComm() throws Exception {
        StringBuilder buf = new StringBuilder();
        Xml.writeComm(buf,"bar");
        assertEquals("comm","<!--bar-->",buf.toString());
        buf.setLength(0);
        Xml.writeComm(buf,"bar--baz");
        assertEquals("comm","<!--bar- -baz-->",buf.toString());
    }

    public void testWriteCommA() throws Exception {
        StringBuilder buf = new StringBuilder();
        Xml.writeComm(buf,new String[]{"bar","baz"});
        assertEquals("comm","<!--bar\nbaz\n-->",buf.toString());
    }

    public void testWriteText() throws Exception {
        StringBuilder buf = new StringBuilder();
        Xml.writeText(buf,"<&bar>");
        assertEquals("text","&lt;&amp;bar&gt;",buf.toString());
    }

    public void testWriteTextA() throws Exception {
        StringBuilder buf = new StringBuilder();
        Xml.writeText(buf,new String[]{"bar","baz","ba\"<>"});
        assertEquals("text","bar\nbaz\nba&quot;&lt;&gt;\n",buf.toString());
    }

    public void testReadElemInline() throws Exception {
        StringReader r = new StringReader("  \n<foo a=\"b\" c=\"&quot;\"/>\t\n");
        Xml.Parser p = new Xml.Parser(r);
        Xml.Event e = p.readEvent();
        assertEquals("inline",Xml.Event.Phase.Inline,e.phase());
        assertEquals("elem",Xml.Node.Type.ELEM,e.type());
        assertEquals("<foo>","foo",e.name());
        assertEquals("a=\"b\"","b",e.attr().get("a"));
        assertEquals("c=\"\\\"\"","\"",e.attr().get("c"));
    }

    public void testReadElemOpenClose() throws Exception {
        StringReader r = new StringReader("  \n<foo a=\"b\">\t\nbar&quot;x&lt;y&gt;&#x20;</foo>");
        Xml.Parser p = new Xml.Parser(r);
        Xml.Event e = p.readEvent();
        assertEquals("open",Xml.Event.Phase.Open,e.phase());
        assertEquals("elem",Xml.Node.Type.ELEM,e.type());
        assertEquals("<foo>","foo",e.name());
        e = p.readEvent();
        assertEquals("inline",Xml.Event.Phase.Inline,e.phase());
        assertEquals("text",Xml.Node.Type.TEXT,e.type());
        assertEquals("\"bar\\\"x<y> \"","bar\"x<y> ",e.text());
        e = p.readEvent();
        assertEquals("close",Xml.Event.Phase.Close,e.phase());
        assertEquals("elem",Xml.Node.Type.ELEM,e.type());
        assertEquals("<foo>","foo",e.name());
    }

    public void testReadElemOpenCloseInner() throws Exception {
        StringReader r = new StringReader("  \n<foo a=\"b\">\t\n<bar c=\"d\"/>baz<bat e=\"f\">bal</bat></foo>");
        Xml.Parser p = new Xml.Parser(r);
        Xml.Event e = p.readEvent();
        assertEquals("open",Xml.Event.Phase.Open,e.phase());
        assertEquals("elem",Xml.Node.Type.ELEM,e.type());
        assertEquals("<foo>","foo",e.name());
        e = p.readEvent();
        assertEquals("inline",Xml.Event.Phase.Inline,e.phase());
        assertEquals("elem",Xml.Node.Type.ELEM,e.type());
        assertEquals("<bar>","bar",e.name());
        e = p.readEvent();
        assertEquals("inline",Xml.Event.Phase.Inline,e.phase());
        assertEquals("text",Xml.Node.Type.TEXT,e.type());
        assertEquals("\"baz\"","baz",e.text());
        e = p.readEvent();
        assertEquals("open",Xml.Event.Phase.Open,e.phase());
        assertEquals("elem",Xml.Node.Type.ELEM,e.type());
        assertEquals("<bat>","bat",e.name());
        e = p.readEvent();
        assertEquals("inline",Xml.Event.Phase.Inline,e.phase());
        assertEquals("text",Xml.Node.Type.TEXT,e.type());
        assertEquals("\"bal\"","bal",e.text());
        e = p.readEvent();
        assertEquals("close",Xml.Event.Phase.Close,e.phase());
        assertEquals("elem",Xml.Node.Type.ELEM,e.type());
        assertEquals("<bat>","bat",e.name());
        e = p.readEvent();
        assertEquals("close",Xml.Event.Phase.Close,e.phase());
        assertEquals("elem",Xml.Node.Type.ELEM,e.type());
        assertEquals("<foo>","foo",e.name());
    }

    private static Map<CharSequence,Object> asMap(Object... kv) {
        Map<CharSequence,Object> m = new LinkedHashMap<CharSequence,Object>();
        for(int i=0; i+1<kv.length; i+=2) m.put((CharSequence) kv[i], kv[i+1]);
        return m;
    }


}