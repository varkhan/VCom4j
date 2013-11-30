package net.varkhan.base.conversion.character;

import junit.framework.TestCase;

import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
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
        AsciiEncoder<Object> enc = new AsciiEncoder<Object>();
        assertEquals("Foo bar baz",new String(enc.encode("Foo bar baz",null),Charset.forName("US-ASCII")));
        assertEquals("Foo bar $$~",new String(enc.encode("Foo bar $$\u00fe",null),Charset.forName("US-ASCII")));
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        assertEquals(11, enc.encode("Foo bar $$\u00fe", out, null));
        assertEquals("Foo bar $$~",new String(out.toByteArray(),Charset.forName("US-ASCII")));
        ByteBuffer buf = ByteBuffer.allocate(50);
        assertEquals(11, enc.encode("Foo bar $$\u00fe", buf, null));
        assertEquals("Foo bar $$~",new String(buf.array(),buf.arrayOffset(),buf.arrayOffset()+buf.position(),Charset.forName("US-ASCII")));
    }
}
