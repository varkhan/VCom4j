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
public class AsciiEncoderTest extends TestCase {
    public void testEncode() throws Exception {
        AsciiEncoder<Object> enc = new AsciiEncoder<Object>();
        assertEquals("Foo bar baz",new String(enc.encode("Foo bar baz",null),Charset.forName("ASCII")));
        assertEquals("Foo bar $$~",new String(enc.encode("Foo bar $$\u00fe",null),Charset.forName("ASCII")));
    }
}
