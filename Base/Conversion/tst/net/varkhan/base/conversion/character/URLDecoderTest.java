package net.varkhan.base.conversion.character;

import junit.framework.TestCase;

import java.io.ByteArrayInputStream;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;


public class URLDecoderTest extends TestCase {

    public void testDecodeStr() throws Exception {
        verifyDecode("hash#t4g/s%,", "hash%23t4g%2Fs%25%2C");
        verifyDecode("sefaköy cekmece", "sefak%C3%B6y+cekmece");
        verifyDecode("elazığ", "elaz%C4%B1%C4%9F");
        verifyDecode("دبي", "%D8%AF%D8%A8%D9%8A");
        verifyDecode("الشرقيه", "%D8%A7%D9%84%D8%B4%D8%B1%D9%82%D9%8A%D9%87");
        verifyDecode("büyükdere mahallesi", "b%C3%BCy%C3%BCkdere+mahallesi");
        verifyDecode("Çorlu", "%C3%87orlu");
        verifyDecode("niğde", "ni%C4%9Fde");
        verifyDecode("\u4e00", "%E4%B8%80");
        verifyDecode("你好", "%E4%BD%A0%E5%A5%BD");
        verifyDecode("ﬅ", "%EF%AC%85");
        verifyDecode("", "%EE%90%A5");
    }

    public void verifyDecode(String expected, String decode) throws java.io.IOException {
        assertEquals("_decode(\""+decode+"\")",expected, URLDecoder._decode(new StringBuilder(), decode).toString());
        URLDecoder<Object> dec = new URLDecoder<Object>();
        assertEquals("decode(\""+decode+"\".getBytes())",expected,dec.decode(decode.getBytes(Charset.forName("US-ASCII")),null));
        byte[] buf = decode.getBytes(Charset.forName("ASCII"));
        assertEquals("decode(\""+decode+".getBytes(),0,<>\")",expected,dec.decode(buf,0,buf.length,null));
        assertEquals("decode(new ByteArrayInputStream(\""+decode+"\"))",expected,dec.decode(new ByteArrayInputStream(buf),null));
        assertEquals("decode(new ByteBuffer(\""+decode+"\"))",expected,dec.decode(ByteBuffer.wrap(buf), null));
    }


}