package net.varkhan.base.conversion.character;

import junit.framework.TestCase;

import java.io.ByteArrayOutputStream;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.CharacterCodingException;
import java.nio.charset.Charset;
import java.nio.charset.CharsetEncoder;
import java.nio.charset.CoderResult;
import java.util.Arrays;


/**
 * <b></b>.
 * <p/>
 *
 * @author varkhan
 * @date 11/23/13
 * @time 4:23 PM
 */
public class UTF8EncoderTest extends TestCase {
    public void testEncode() throws Exception {
        UTF8Encoder<Object> enc = new UTF8Encoder<Object>();
        assertEquals("Foo bar baz",new String(enc.encode("Foo bar baz",null),Charset.forName("UTF-8")));
        assertEquals("Foo bar $$þ",new String(enc.encode("Foo bar $$\u00fe",null),Charset.forName("UTF-8")));
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        assertEquals(12, enc.encode("Foo bar $$\u00fe", out, null));
        assertEquals("Foo bar $$þ",new String(out.toByteArray(),Charset.forName("UTF-8")));
        ByteBuffer buf = ByteBuffer.allocate(50);
        assertEquals(12, enc.encode("Foo bar $$\u00fe", buf, null));
        assertEquals("Foo bar $$þ",new String(buf.array(),buf.arrayOffset(),buf.arrayOffset()+buf.position(),Charset.forName("UTF-8")));
    }

    public void testEncode2() throws Exception {
        String[] ss = { "دبي", "الشرقيه",
                        "aköy", "zığ", "büyük", "Çor", "niğ",
                        "一", "你好", "龵", "ホ", "࿊",
                        "ﬅ", "⣿", "꜕", "\uE425>"
        };
        UTF8Encoder<Object> enc = new UTF8Encoder<Object>();
        for (int i = 0; i < ss.length; i++) {
            String s = ss[i];
            byte[] buf=encode_native(s);
            assertEquals("encode_native().length<>length(): \""+s+"\"", buf.length, enc.length(s, null));
            assertArrayEquals("encode_native(decode()) \""+s+"\" "+
                              "\n\t"+Arrays.toString(buf)+
                              "\n\t"+Arrays.toString(enc.encode(s, null))+
                              "\n", buf, enc.encode(s, null));
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            assertEquals("encode_native(decode()) \""+s+"\" "+
                              "\n\t"+Arrays.toString(buf)+
                              "\n\t"+Arrays.toString(enc.encode(s, null))+
                              "\n", buf.length, enc.encode(s, out, null));
            assertEquals(s,new String(out.toByteArray(),Charset.forName("UTF-8")));
            ByteBuffer bby = ByteBuffer.allocate(50);
            assertEquals("encode_native(decode()) \""+s+"\" "+
                              "\n\t"+Arrays.toString(buf)+
                              "\n\t"+Arrays.toString(enc.encode(s, null))+
                              "\n", buf.length, enc.encode(s, bby, null));
            assertEquals(s,new String(bby.array(),bby.arrayOffset(),bby.arrayOffset()+bby.position(),Charset.forName("UTF-8")));
        }
    }

    private void assertArrayEquals(String message, byte[] expected, byte[] actual) {
        if(!Arrays.equals(expected, actual)) assertEquals(message,Arrays.toString(expected),Arrays.toString(actual));
    }

    public static byte[] encode_native(String str) {
        CharsetEncoder ce = UTF8Decoder.UTF_8.newEncoder();
        char[] chars = str.toCharArray();
        CharBuffer cb = CharBuffer.wrap(chars,0,chars.length);
        byte[] bytes = new byte[(int)(chars.length*ce.maxBytesPerChar())];
        ByteBuffer bb = ByteBuffer.wrap(bytes);
        try {
            CoderResult cr = ce.encode(cb, bb, true);
            if (!cr.isUnderflow()) cr.throwException();
            cr = ce.flush(bb);
            if (!cr.isUnderflow()) cr.throwException();
        } catch (CharacterCodingException x) {
            // Substitution is always enabled,
            // so this shouldn't happen
            throw new Error(x);
        }
        return (bb.position()>=bytes.length)?bytes:Arrays.copyOf(bytes,bb.position());
    }

}
