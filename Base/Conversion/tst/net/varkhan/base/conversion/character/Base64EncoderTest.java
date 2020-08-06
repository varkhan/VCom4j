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
        assertEquals(1,enc.length("AA==",null));
        assertEquals(1,enc.encode("AA==",null).length);
        assertEquals(0,enc.encode("AA==",null)[0]);
        assertEquals("F",new String(enc.encode("Rg==",null)));
        assertEquals(2,enc.length("Rm8=",null));
        assertEquals("Fo",new String(enc.encode("Rm8=",null)));
        assertEquals(7,enc.length("Rm9vIEJhcg==",null));
        assertEquals("Foo Bar",new String(enc.encode("Rm9vIEJhcg==",null)));
        assertEquals(9,enc.length("Rm9vIEJhciB+",null));
        assertEquals("Foo Bar ~",new String(enc.encode("Rm9vIEJhciB+",null)));
        Base64Encoder<Object> encn = new Base64Encoder<Object>(Base64Decoder.CHARMAP_UTF7,Base64Decoder.NO_PADDING);
        assertEquals(1,encn.length("AA",null));
        assertEquals(1,encn.encode("AA",null).length);
        assertEquals(0,encn.encode("AA",null)[0]);
        assertEquals(1,encn.length("Rg",null));
        assertEquals("F",new String(encn.encode("Rg",null)));
        assertEquals(2,encn.length("Rm8",null));
        assertEquals("Fo",new String(encn.encode("Rm8",null)));
        assertEquals(7,encn.length("Rm9vIEJhcg",null));
        assertEquals("Foo Bar",new String(encn.encode("Rm9vIEJhcg",null)));
        assertEquals(9,encn.length("Rm9vIEJhciB+",null));
        assertEquals("Foo Bar ~",new String(encn.encode("Rm9vIEJhciB+",null)));
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        assertEquals(7, encn.encode("Rm9vIEJhcg", out, null));
        assertEquals("Foo Bar",new String(out.toByteArray(), Charset.forName("US-ASCII")));
        ByteBuffer buf = ByteBuffer.allocate(50);
        assertEquals(9, encn.encode("Rm9vIEJhciB+", buf, null));
        assertEquals("Foo Bar ~",new String(buf.array(),buf.arrayOffset(),buf.arrayOffset()+buf.position(),Charset.forName("US-ASCII")));
    }

}
