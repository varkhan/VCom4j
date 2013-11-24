package net.varkhan.base.conversion.character;

import junit.framework.TestCase;

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
        AsciiDecoder<Object> dec = new AsciiDecoder<Object>(true);
        assertEquals("Foo bar baz",dec.decode("Foo bar baz".getBytes(Charset.forName("ASCII")),null));
        byte[] buf ="Foo bar $$$".getBytes(Charset.forName("ASCII"));
        buf[buf.length-1] = (byte)0xFE;
        assertEquals("Foo bar $$~",dec.decode(buf,0,buf.length,null));
    }
}
