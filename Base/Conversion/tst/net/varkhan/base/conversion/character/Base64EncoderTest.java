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
 * @time 4:51 PM
 */
public class Base64EncoderTest extends TestCase {
    private final String longstring = "AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA";
    private final String longencode = "QUFBQUFBQUFBQUFBQUFBQUFBQUFBQUFBQUFBQUFBQUFBQUFBQUFBQUFBQUFBQUFB";

    public void testEncode() throws Exception {
        Base64Encoder<Object> enc = new Base64Encoder<Object>();
        assertEquals(1, enc.length("AA==", null));
        assertEquals(1, enc.encode("AA==", null).length);
        assertEquals(0, enc.encode("AA==", null)[0]);
        assertEquals("F", new String(enc.encode("Rg==", null)));
        assertEquals(2, enc.length("Rm8=", null));
        assertEquals("Fo", new String(enc.encode("Rm8=", null)));
        assertEquals(7, enc.length("Rm9vIEJhcg==", null));
        assertEquals("Foo Bar", new String(enc.encode("Rm9vIEJhcg==", null)));
        assertEquals(9, enc.length("Rm9vIEJhciB+", null));
        assertEquals("Foo Bar ~", new String(enc.encode("Rm9vIEJhciB+", null)));

        assertEquals(
                longstring,
                new String(enc.encode(longencode + "QUE=", null)));
        assertEquals(
                longstring + longstring,
                new String(enc.encode(longencode + longencode + "QUFBQQ==", null)));
        assertEquals(
                longstring + longstring + longstring,
                new String(enc.encode(longencode + longencode + longencode + "QUFBQUFB", null)));
    }

    public void testEncodeNopad() throws Exception {
        Base64Encoder<Object> enc = new Base64Encoder<Object>(Base64Decoder.CHARMAP_UTF7,Base64Decoder.NO_PADDING);
        assertEquals(1,enc.length("AA",null));
        assertEquals(1,enc.encode("AA",null).length);
        assertEquals(0,enc.encode("AA",null)[0]);
        assertEquals(1,enc.length("Rg",null));
        assertEquals("F",new String(enc.encode("Rg",null)));
        assertEquals(2,enc.length("Rm8",null));
        assertEquals("Fo",new String(enc.encode("Rm8",null)));
        assertEquals(7,enc.length("Rm9vIEJhcg",null));
        assertEquals("Foo Bar",new String(enc.encode("Rm9vIEJhcg",null)));
        assertEquals(9,enc.length("Rm9vIEJhciB+",null));
        assertEquals("Foo Bar ~",new String(enc.encode("Rm9vIEJhciB+",null)));
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        assertEquals(7, enc.encode("Rm9vIEJhcg", out, null));
        assertEquals("Foo Bar",new String(out.toByteArray(), Charset.forName("US-ASCII")));
        ByteBuffer buf = ByteBuffer.allocate(50);
        assertEquals(9, enc.encode("Rm9vIEJhciB+", buf, null));
        assertEquals("Foo Bar ~",new String(buf.array(),buf.arrayOffset(),buf.arrayOffset()+buf.position(),Charset.forName("US-ASCII")));

        assertEquals(
                longstring,
                new String(enc.encode(longencode + "QUE", null)));
        assertEquals(
                longstring + longstring,
                new String(enc.encode(longencode + longencode + "QUFBQQ", null)));
        assertEquals(
                longstring + longstring + longstring,
                new String(enc.encode(longencode + longencode + longencode + "QUFBQUFB", null)));
    }

}
