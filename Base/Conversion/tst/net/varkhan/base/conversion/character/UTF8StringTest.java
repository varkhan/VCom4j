package net.varkhan.base.conversion.character;

import junit.framework.TestCase;

import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.CharacterCodingException;
import java.nio.charset.CharsetEncoder;
import java.nio.charset.CoderResult;
import java.util.Arrays;


public class UTF8StringTest extends TestCase {

    public void testLength() throws Exception {
        String[] ss = { "Foo", "Foo$", "Foo\u00ef", "دبي", "الشرقيه",
                        "aköy", "zığ", "büyük", "Çor", "niğ",
                        "一", "你好", "龵", "ホ", "࿊",
                        "ﬅ", "⣿", "꜕", "\uE425>"
        };
        for (int i = 0; i < ss.length; i++) {
            String s = ss[i];
            byte[] buf=encode_native(s);
            UTF8String enc=new UTF8String(buf, 0, buf.length);
            assertEquals("length() \""+s+"\" "+
                         "\n\t"+s+
                         "\n\t"+enc+
                         "\n", s.length(), enc.length());
        }
    }

    public void testCharAt() throws Exception {
        String[] ss = { "Foo", "Foo$", "Foo\u00ef", "دبي", "الشرقيه",
                        "aköy", "zığ", "büyük", "Çor", "niğ",
                        "一", "你好", "龵", "ホ", "࿊",
                        "ﬅ", "⣿", "꜕", "\uE425>"
        };
        for (int i = 0; i < ss.length; i++) {
            String s = ss[i];
            byte[] buf=encode_native(s);
            UTF8String enc=new UTF8String(buf, 0, buf.length);
            for(int j=0; j<s.length(); j++) {
                assertEquals("charAt() \"" + s + "\" " +
                             "\n\t" + s +
                             "\n\t" + enc +
                             "\n", s.charAt(j), enc.charAt(j));
            }
        }
    }


    public void testToString() throws Exception {
        String[] ss = { "Foo", "Foo$", "Foo\u00ef", "دبي", "الشرقيه",
                        "aköy", "zığ", "büyük", "Çor", "niğ",
                        "一", "你好", "龵", "ホ", "࿊",
                        "ﬅ", "⣿", "꜕", "\uE425>"
        };
        for (int i = 0; i < ss.length; i++) {
            String s = ss[i];
            byte[] buf=encode_native(s);
            UTF8String enc=new UTF8String(buf, 0, buf.length);
            assertEquals("toString() \""+s+"\" "+
                         "\n\t"+s+
                         "\n\t"+enc+
                         "\n", s, enc.toString());
        }
    }

    public void testEquals() throws Exception {
        String[] ss = { "Foo", "Foo$", "Foo\u00ef", "دبي", "الشرقيه",
                        "aköy", "zığ", "büyük", "Çor", "niğ",
                        "一", "你好", "龵", "ホ", "࿊",
                        "ﬅ", "⣿", "꜕", "\uE425>"
        };
        for (int i = 0; i < ss.length; i++) {
            String s = ss[i];
            byte[] buf=encode_native(s);
            UTF8String enc=new UTF8String(buf, 0, buf.length);
            assertTrue("equals() \""+s+"\" "+
                       "\n\t"+s+
                       "\n\t"+enc+
                       "\n", enc.equals(s));
            assertTrue("equals() \""+s+"\" "+
                       "\n\t"+s+
                       "\n\t"+enc+
                       "\n", enc.equals(new UTF8String(buf, 0, buf.length)));
        }
    }

    public void testSubSequence() throws Exception {
        String[] ss = { "Foo", "Foo$", "Foo\u00ef", "دبي", "الشرقيه",
                        "aköy", "zığ", "büyük", "Çor", "niğ",
                        " 一", "你好", " 龵", " ホ", " ࿊",
                        " ﬅ", " ⣿", " ꜕", "\uE425>"
        };
        for (int i = 0; i < ss.length; i++) {
            String s = ss[i];
            byte[] buf=encode_native(s);
            UTF8String enc=new UTF8String(buf, 0, buf.length);
            byte[] buf2=encode_native(s.substring(1,s.length()-1));
            UTF8String enc2=new UTF8String(buf2, 0, buf2.length);
            assertEquals("subSequence("+1+","+(enc.length()-1)+") \""+s+"\" "+
                         "\n\t"+s+
                         "\n\t"+enc+
                         "\n", enc2, enc.subSequence(1,enc.length()-1));
        }
    }

    public void testIndexOf() throws Exception {
        String[] ss={ "Foo", "Foo$", "Foo\u00ef", "دبي", "الشرقيه",
                      "aköy", "zığ", "büyük", "Çor", "niğ",
                      " 一", "你好", " 龵", " ホ", " ࿊",
                      " ﬅ", " ⣿", " ꜕", "\uE425>"
        };
        for(int i=0;i<ss.length;i++) {
            String s = ss[i];
            byte[] buf=encode_native(s);
            UTF8String enc=new UTF8String(buf, 0, buf.length);
            for(int j=0; j+3<s.length(); j++) {
                String x = s.substring(j,j+3);
                assertEquals("indexOf("+x+")",s.indexOf(x),enc.indexOf(x));
                assertEquals("indexOf("+x+")",s.indexOf(x.charAt(0)),enc.indexOf(x.charAt(0)));
            }
            for(int j=0; j+2<s.length(); j++) {
                String x = s.substring(j,j+2);
                assertEquals("indexOf("+x+")",s.indexOf(x),enc.indexOf(x));
                assertEquals("indexOf("+x+")",s.indexOf(x.charAt(0)),enc.indexOf(x.charAt(0)));
            }
            for(int j=0; j+1<s.length(); j++) {
                String x = s.substring(j,j+1);
                assertEquals("indexOf("+x+")",s.indexOf(x),enc.indexOf(x));
                assertEquals("indexOf("+x+")",s.indexOf(x.charAt(0)),enc.indexOf(x.charAt(0)));
            }
        }
    }

    public static byte[] encode_native(String str) {
        CharsetEncoder ce = UTF8Decoder.UTF_8.newEncoder();
        char[] chars = str.toCharArray();
        CharBuffer cb = CharBuffer.wrap(chars, 0, chars.length);
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