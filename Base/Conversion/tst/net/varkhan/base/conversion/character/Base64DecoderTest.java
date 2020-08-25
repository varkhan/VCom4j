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
    private final String longstring = "AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA";
    private final String longencode = "QUFBQUFBQUFBQUFBQUFBQUFBQUFBQUFBQUFBQUFBQUFBQUFBQUFBQUFBQUFBQUFB";

    public void testDecode() throws Exception {
        Base64Decoder<Object> dec = new Base64Decoder<Object>();
        assertEquals("AA==", dec.decode(new byte[]{0}, null));
        assertEquals("Rg==", dec.decode("F".getBytes(Charset.forName("US-ASCII")), null));
        assertEquals("Rm8=", dec.decode("Fo".getBytes(Charset.forName("US-ASCII")), null));
        assertEquals("Rm9vIEJhcg==", dec.decode("Foo Bar".getBytes(Charset.forName("US-ASCII")), null));
        assertEquals("Rm9vIEJhciB+", dec.decode("Foo Bar ~".getBytes(Charset.forName("US-ASCII")), null));
        assertEquals("Rm9vIEJhcg==", dec.decode(new ByteArrayInputStream("Foo Bar".getBytes(Charset.forName("US-ASCII"))), null));
        assertEquals("Rm9vIEJhciB+", dec.decode(ByteBuffer.wrap("Foo Bar ~".getBytes(Charset.forName("US-ASCII"))), null));
        assertEquals(
                longencode + "QUE=",
                dec.decode(ByteBuffer.wrap(longstring.getBytes(Charset.forName("US-ASCII"))), null));
        assertEquals(
                longencode + longencode + "QUFBQQ==",
                dec.decode(ByteBuffer.wrap((longstring + longstring).getBytes(Charset.forName("US-ASCII"))), null));
        assertEquals(
                longencode + longencode + longencode + "QUFBQUFB",
                dec.decode(ByteBuffer.wrap((longstring + longstring + longstring).getBytes(Charset.forName("US-ASCII"))), null));
    }

    public void testDecodeNopad() throws Exception {
        Base64Decoder<Object> dec = new Base64Decoder<Object>(Base64Decoder.CHARMAP_UTF7,Base64Decoder.NO_PADDING);
        assertEquals("AA",dec.decode(new byte[] {0},null));
        assertEquals("Rg",dec.decode("F".getBytes(Charset.forName("US-ASCII")),null));
        assertEquals("Rm8",dec.decode("Fo".getBytes(Charset.forName("US-ASCII")),null));
        assertEquals("Rm9vIEJhcg",dec.decode("Foo Bar".getBytes(Charset.forName("US-ASCII")),null));
        assertEquals("Rm9vIEJhciB+",dec.decode("Foo Bar ~".getBytes(Charset.forName("US-ASCII")),null));
        assertEquals("Rm9vIEJhcg",dec.decode(new ByteArrayInputStream("Foo Bar".getBytes(Charset.forName("US-ASCII"))),null));
        assertEquals("Rm9vIEJhciB+",dec.decode(ByteBuffer.wrap("Foo Bar ~".getBytes(Charset.forName("US-ASCII"))),null));
        assertEquals("Rm9vIEJhciB+",dec.decode(ByteBuffer.wrap("Foo Bar ~".getBytes(Charset.forName("US-ASCII"))),null));
        assertEquals(
                longencode +"QUE",
                dec.decode(ByteBuffer.wrap(longstring.getBytes(Charset.forName("US-ASCII"))),null));
        assertEquals(
                longencode+longencode+"QUFBQQ",
                dec.decode(ByteBuffer.wrap((longstring+longstring).getBytes(Charset.forName("US-ASCII"))),null));
        assertEquals(
                longencode+longencode+longencode +"QUFBQUFB",
                dec.decode(ByteBuffer.wrap((longstring+longstring+longstring).getBytes(Charset.forName("US-ASCII"))),null));
    }

}
