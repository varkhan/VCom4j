package net.varkhan.base.conversion.character;

import junit.framework.TestCase;


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
        assertEquals("Rg==",dec.decode("F".getBytes(),null));
        assertEquals("Rm8=",dec.decode("Fo".getBytes(),null));
        assertEquals("Rm9vIEJhcg==",dec.decode("Foo Bar".getBytes(),null));
        assertEquals("Rm9vIEJhciB+",dec.decode("Foo Bar ~".getBytes(),null));
        Base64Decoder<Object> decn = new Base64Decoder<Object>(Base64Decoder.CHARMAP_UTF7,Base64Decoder.NO_PADDING);
        assertEquals("Rg",decn.decode("F".getBytes(),null));
        assertEquals("Rm8",decn.decode("Fo".getBytes(),null));
        assertEquals("Rm9vIEJhcg",decn.decode("Foo Bar".getBytes(),null));
        assertEquals("Rm9vIEJhciB+",decn.decode("Foo Bar ~".getBytes(),null));
    }

}
