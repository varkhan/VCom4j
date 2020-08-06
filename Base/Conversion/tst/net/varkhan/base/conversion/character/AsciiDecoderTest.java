package net.varkhan.base.conversion.character;

import junit.framework.TestCase;
import net.varkhan.base.conversion.serializer.DecodingException;

import java.io.ByteArrayInputStream;
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
public class AsciiDecoderTest extends TestCase {
    public void testDecode() throws Exception {
        verifyDecode("Foo bar baz", "Foo bar baz", true);
        byte[] buf ="Foo bar $$$".getBytes(Charset.forName("ASCII"));
        buf[buf.length-1] = (byte)0x7E;
        verifyDecode("Foo bar $$~", buf, true);
        verifyDecode("Foo bar $$~", buf, false);
        buf[buf.length-1] = (byte)0xFE;
        verifyDecode("Foo bar $$~", buf, true);
        try {
            verifyDecode("Foo bar $$~", buf, false);
            fail("No squash on >07F");
        }
        catch(DecodingException e) {
            // success
        }
    }

    public void verifyDecode(String expected, byte[] buf, boolean squash) {
        AsciiDecoder<Object> dec = new AsciiDecoder<Object>(squash);
        assertEquals(expected,dec.decode(buf,0,buf.length,null));
        assertEquals(expected,dec.decode(new ByteArrayInputStream(buf),null));
        assertEquals(expected,dec.decode(ByteBuffer.wrap(buf),null));
    }

    public void verifyDecode(String expected, String decode, boolean squash) throws java.io.IOException {
        AsciiDecoder<Object> dec = new AsciiDecoder<Object>(squash);
        assertEquals("decode(\""+decode+"\".getBytes())",expected,dec.decode(decode.getBytes(Charset.forName("US-ASCII")),null));
        byte[] buf = decode.getBytes(Charset.forName("ASCII"));
        assertEquals("decode(\""+decode+".getBytes(),0,<>\")",expected,dec.decode(buf,0,buf.length,null));
        assertEquals("decode(new ByteArrayInputStream(\""+decode+"\"))",expected,dec.decode(new ByteArrayInputStream(buf),null));
        assertEquals("decode(new ByteBuffer(\""+decode+"\"))",expected,dec.decode(ByteBuffer.wrap(buf), null));
    }
}
