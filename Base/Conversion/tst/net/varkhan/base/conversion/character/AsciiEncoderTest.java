package net.varkhan.base.conversion.character;

import junit.framework.TestCase;

import java.io.ByteArrayOutputStream;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;


/**
 * <b></b>.
 * <p/>
 *
 * @author varkhan
 * @date 11/23/13
 * @time 4:23 PM
 */
public class AsciiEncoderTest extends TestCase {
    public void testEncode() throws Exception {
        verifyEncode("Foo bar baz", "Foo bar baz");
        verifyEncode("Foo bar $$~", "Foo bar $$\u00fe");
        verifyEncode("Foo bar $$~", "Foo bar $$\u01fe");
        verifyEncode("Foo bar $$~", "Foo bar $$\u11fe");
    }

    public void verifyEncode(String expected, String encode) {
        AsciiEncoder<Object> enc = new AsciiEncoder<Object>();
        assertEquals("encode(\""+expected+"\")",expected,new String(enc.encode(encode,null), Charset.forName("US-ASCII")));
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        assertEquals("encode(\""+expected+"\",OutputStream).length()",expected.length(), enc.encode(encode, out, null));
        assertEquals("encode(\""+expected+"\",OutputStream)",expected,new String(out.toByteArray(),Charset.forName("US-ASCII")));
        ByteBuffer buf = ByteBuffer.allocate(50);
        assertEquals("encode(\""+expected+"\",ByteBuffer).length()",expected.length(), enc.encode(encode, buf, null));
        assertEquals("encode(\""+expected+"\",ByteBuffer).array()",expected,new String(buf.array(),buf.arrayOffset(),buf.arrayOffset()+buf.position(),Charset.forName("US-ASCII")));
    }
}
