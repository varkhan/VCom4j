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
        AsciiDecoder<Object> dec = new AsciiDecoder<Object>(true);
        assertEquals("Foo bar baz",dec.decode("Foo bar baz".getBytes(Charset.forName("US-ASCII")),null));
        byte[] buf ="Foo bar $$$".getBytes(Charset.forName("ASCII"));
        buf[buf.length-1] = (byte)0xFE;
        assertEquals("Foo bar $$~",dec.decode(buf,0,buf.length,null));
        assertEquals("Foo bar $$~",dec.decode(new ByteArrayInputStream(buf),null));
        assertEquals("Foo bar $$~",dec.decode(ByteBuffer.wrap(buf),null));
    }

    public void testDecodeOtherRanges() throws Exception {
        String[] ss = { "دبي", "الشرقيه",
                        "aköy", "zığ", "büyük", "Çor", "niğ",
                        "一", "你好", "龵", "ホ", "࿊",
                        "ﬅ", "⣿", "꜕", "\uE425>"
        };
        UTF8Decoder<Object> dec = new UTF8Decoder<Object>();
        for (int i = 0; i < ss.length; i++) {
            String s = ss[i];
            assertEquals("encode(decode_native()) \"" + s + "\" " +
                         "\n\t" + s +
                         "\n\t" + dec.decode(encode_native(s),null) +
                         "\n", s, dec.decode(encode_native(s), null));
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
