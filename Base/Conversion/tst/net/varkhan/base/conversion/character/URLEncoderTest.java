package net.varkhan.base.conversion.character;

import junit.framework.TestCase;


public class URLEncoderTest extends TestCase {

    public void testEncode() throws Exception {
        assertEquals("hash%23t4g%2Fs%25%2C", URLEncoder._encode("hash#t4g/s%,", new StringBuilder()).toString());
        assertEquals("sefak%C3%B6y%20cekmece", URLEncoder._encode("sefaköy cekmece", new StringBuilder()).toString());
        assertEquals("elaz%C4%B1%C4%9F", URLEncoder._encode("elazığ", new StringBuilder()).toString());
        assertEquals("%D8%AF%D8%A8%D9%8A", URLEncoder._encode("دبي", new StringBuilder()).toString());
        assertEquals("%D8%A7%D9%84%D8%B4%D8%B1%D9%82%D9%8A%D9%87", URLEncoder._encode("الشرقيه", new StringBuilder()).toString());
        assertEquals("b%C3%BCy%C3%BCkdere%20mahallesi", URLEncoder._encode("büyükdere mahallesi", new StringBuilder()).toString());
        assertEquals("%C3%87orlu", URLEncoder._encode("Çorlu", new StringBuilder()).toString());
        assertEquals("ni%C4%9Fde", URLEncoder._encode("niğde", new StringBuilder()).toString());
        assertEquals("%E4%B8%80", URLEncoder._encode("\u4e00", new StringBuilder()).toString());
        assertEquals("%E4%BD%A0%E5%A5%BD", URLEncoder._encode("你好", new StringBuilder()).toString());
        assertEquals("%EF%AC%85", URLEncoder._encode("ﬅ", new StringBuilder()).toString());
        assertEquals("%EE%90%A5", URLEncoder._encode("", new StringBuilder()).toString());
    }

}