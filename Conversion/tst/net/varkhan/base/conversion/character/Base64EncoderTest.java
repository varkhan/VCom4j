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

    public void testEncode() throws Exception {
        Base64Encoder<Object> enc = new Base64Encoder<Object>();
        assertEquals("F",new String(enc.encode("Rg==",null)));
        assertEquals("Fo",new String(enc.encode("Rm8=",null)));
        assertEquals("Foo Bar",new String(enc.encode("Rm9vIEJhcg==",null)));
        assertEquals("Foo Bar ~",new String(enc.encode("Rm9vIEJhciB+",null)));
        Base64Encoder<Object> encn = new Base64Encoder<Object>(Base64Decoder.CHARMAP_UTF7,Base64Decoder.NO_PADDING);
        assertEquals("F",new String(encn.encode("Rg",null)));
        assertEquals("Fo",new String(encn.encode("Rm8",null)));
        assertEquals("Foo Bar",new String(encn.encode("Rm9vIEJhcg",null)));
        assertEquals("Foo Bar ~",new String(encn.encode("Rm9vIEJhciB+",null)));
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        assertEquals(7, encn.encode("Rm9vIEJhcg", out, null));
        assertEquals("Foo Bar",new String(out.toByteArray(), Charset.forName("US-ASCII")));
        ByteBuffer buf = ByteBuffer.allocate(50);
        assertEquals(9, encn.encode("Rm9vIEJhciB+", buf, null));
        assertEquals("Foo Bar ~",new String(buf.array(),buf.arrayOffset(),buf.arrayOffset()+buf.position(),Charset.forName("US-ASCII")));
    }

}
