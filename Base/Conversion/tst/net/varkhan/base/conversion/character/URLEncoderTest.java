package net.varkhan.base.conversion.character;

import junit.framework.TestCase;

import java.io.ByteArrayOutputStream;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;


public class URLEncoderTest extends TestCase {

    public void testEncodeStr() throws Exception {
        verifyEncode("hash%23t4g%2Fs%25%2C","hash#t4g/s%,");
        verifyEncode("sefak%C3%B6y%20cekmece", "sefaköy cekmece");
        verifyEncode("elaz%C4%B1%C4%9F", "elazığ");
        verifyEncode("%D8%AF%D8%A8%D9%8A", "دبي");
        verifyEncode("%D8%A7%D9%84%D8%B4%D8%B1%D9%82%D9%8A%D9%87", "الشرقيه");
        verifyEncode("b%C3%BCy%C3%BCkdere%20mahallesi", "büyükdere mahallesi");
        verifyEncode("%C3%87orlu", "Çorlu");
        verifyEncode("ni%C4%9Fde", "niğde");
        verifyEncode("%E4%B8%80", "\u4e00");
        verifyEncode("%E4%BD%A0%E5%A5%BD", "你好");
        verifyEncode("%EF%AC%85", "ﬅ");
        verifyEncode("%EE%90%A5", "");
    }

    private static void verifyEncode(String expected, String encode) throws Exception {
        assertEquals("_encode(\""+encode+"\")",expected, URLEncoder._encode(encode, new StringBuilder()).toString());
        URLEncoder<Object> enc = new URLEncoder<Object>();
        assertEquals("encode(\""+encode+"\")",expected,new String(enc.encode(encode,null),Charset.forName("US-ASCII")));
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        assertEquals("encode(\""+encode+"\",OutputStream).length()",expected.length(), enc.encode(encode, out, null));
        assertEquals("encode(\""+encode+"\",OutputStream).toByteArray()",expected,new String(out.toByteArray(),Charset.forName("US-ASCII")));
        ByteBuffer buf = ByteBuffer.allocate(4*expected.length());
        assertEquals("encode(\""+encode+"\",ByteBuffer).length()",expected.length(), enc.encode(encode, buf, null));
        assertEquals("encode(\""+encode+"\",ByteBuffer).array()",expected,new String(buf.array(),buf.arrayOffset(),buf.arrayOffset()+buf.position(),Charset.forName("US-ASCII")));
    }

}