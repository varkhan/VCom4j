package net.varkhan.base.conversion.character;

import junit.framework.TestCase;

import java.io.ByteArrayInputStream;
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
public class UTF8DecoderTest extends TestCase {
    public void testDecode() throws Exception {
        UTF8Decoder<Object> dec = new UTF8Decoder<Object>();
        assertEquals("Foo bar baz",dec.decode("Foo bar baz".getBytes(Charset.forName("UTF-8")),null));
        byte[] buf ="Foo bar $$þ".getBytes(Charset.forName("UTF-8"));
        assertEquals("Foo bar $$þ",dec.decode(buf,0,buf.length,null));
        assertEquals("Foo bar $$þ",dec.decode(new ByteArrayInputStream(buf),null));
        assertEquals("Foo bar $$þ",dec.decode(ByteBuffer.wrap(buf),null));
    }

    public void testDecode2() throws Exception {
        String[] ss = { "دبي", "الشرقيه",
                        "aköy", "zığ", "büyük", "Çor", "niğ",
                        "一", "你好", "龵", "ホ", "࿊",
                        "ﬅ", "⣿", "꜕", "\uE425>"
        };
        UTF8Decoder<Object> dec = new UTF8Decoder<Object>();
        for (int i = 0; i < ss.length; i++) {
            String s = ss[i];
            byte[] buf=encode_native(s);
            assertEquals("encode(decode_native()) \"" + s + "\" " +
                         "\n\t" + s +
                         "\n\t" + dec.decode(buf,null) +
                         "\n", s, dec.decode(buf, null));
            assertEquals("encode(decode_native()) \"" + s + "\" " +
                         "\n\t" + s +
                         "\n\t" + dec.decode(new ByteArrayInputStream(buf),null) +
                         "\n", s, dec.decode(new ByteArrayInputStream(buf), null));
            assertEquals("encode(decode_native()) \"" + s + "\" " +
                         "\n\t" + s +
                         "\n\t" + dec.decode(ByteBuffer.wrap(buf),null) +
                         "\n", s, dec.decode(ByteBuffer.wrap(buf), null));
        }
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
