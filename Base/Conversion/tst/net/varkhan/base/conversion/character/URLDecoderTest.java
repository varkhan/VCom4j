package net.varkhan.base.conversion.character;

import junit.framework.TestCase;


public class URLDecoderTest extends TestCase {

    public void testDecode() throws Exception {
        assertEquals("hash#t4g/s%,", URLDecoder._decode(new StringBuilder(), "hash%23t4g%2Fs%25%2C").toString());
        assertEquals("sefaköy cekmece", URLDecoder._decode(new StringBuilder(), "sefak%C3%B6y+cekmece").toString());
        assertEquals("elazığ", URLDecoder._decode(new StringBuilder(), "elaz%C4%B1%C4%9F").toString());
        assertEquals("دبي", URLDecoder._decode(new StringBuilder(), "%D8%AF%D8%A8%D9%8A").toString());
        assertEquals("الشرقيه", URLDecoder._decode(new StringBuilder(), "%D8%A7%D9%84%D8%B4%D8%B1%D9%82%D9%8A%D9%87").toString());
        assertEquals("büyükdere mahallesi", URLDecoder._decode(new StringBuilder(), "b%C3%BCy%C3%BCkdere+mahallesi").toString());
        assertEquals("Çorlu", URLDecoder._decode(new StringBuilder(), "%C3%87orlu").toString());
        assertEquals("niğde", URLDecoder._decode(new StringBuilder(), "ni%C4%9Fde").toString());
        assertEquals("\u4e00", URLDecoder._decode(new StringBuilder(), "%E4%B8%80").toString());
        assertEquals("你好", URLDecoder._decode(new StringBuilder(), "%E4%BD%A0%E5%A5%BD").toString());
        assertEquals("ﬅ", URLDecoder._decode(new StringBuilder(), "%EF%AC%85").toString());
        assertEquals("", URLDecoder._decode(new StringBuilder(), "%EE%90%A5").toString());
    }
}