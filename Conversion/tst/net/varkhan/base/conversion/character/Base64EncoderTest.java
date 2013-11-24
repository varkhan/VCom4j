package net.varkhan.base.conversion.character;

import junit.framework.TestCase;


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
    }

}
