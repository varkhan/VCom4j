package net.varkhan.base.conversion.character;

import junit.framework.TestCase;

import java.io.ByteArrayInputStream;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;


/**
 * <b></b>.
 * <p/>
 *
 * @author varkhan
 * @date 11/23/13
 * @time 4:00 PM
 */
public class Base64DecoderTest extends TestCase {

    public void testDecode() throws Exception {
        Base64Decoder<Object> dec = new Base64Decoder<Object>();
        assertEquals("AA==",dec.decode(new byte[] {0},null));
        assertEquals("Rg==",dec.decode("F".getBytes(Charset.forName("US-ASCII")),null));
        assertEquals("Rm8=",dec.decode("Fo".getBytes(Charset.forName("US-ASCII")),null));
        assertEquals("Rm9vIEJhcg==",dec.decode("Foo Bar".getBytes(Charset.forName("US-ASCII")),null));
        assertEquals("Rm9vIEJhciB+",dec.decode("Foo Bar ~".getBytes(Charset.forName("US-ASCII")),null));
        assertEquals("Rm9vIEJhcg==",dec.decode(new ByteArrayInputStream("Foo Bar".getBytes(Charset.forName("US-ASCII"))),null));
        assertEquals("Rm9vIEJhciB+",dec.decode(ByteBuffer.wrap("Foo Bar ~".getBytes(Charset.forName("US-ASCII"))),null));
        Base64Decoder<Object> decn = new Base64Decoder<Object>(Base64Decoder.CHARMAP_UTF7,Base64Decoder.NO_PADDING);
        assertEquals("AA",decn.decode(new byte[] {0},null));
        assertEquals("Rg",decn.decode("F".getBytes(Charset.forName("US-ASCII")),null));
        assertEquals("Rm8",decn.decode("Fo".getBytes(Charset.forName("US-ASCII")),null));
        assertEquals("Rm9vIEJhcg",decn.decode("Foo Bar".getBytes(Charset.forName("US-ASCII")),null));
        assertEquals("Rm9vIEJhciB+",decn.decode("Foo Bar ~".getBytes(Charset.forName("US-ASCII")),null));
        assertEquals("Rm9vIEJhcg",decn.decode(new ByteArrayInputStream("Foo Bar".getBytes(Charset.forName("US-ASCII"))),null));
        assertEquals("Rm9vIEJhciB+",decn.decode(ByteBuffer.wrap("Foo Bar ~".getBytes(Charset.forName("US-ASCII"))),null));
    }

}
