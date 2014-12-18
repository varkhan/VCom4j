package net.varkhan.base.conversion.character;

import net.varkhan.base.conversion.AbstractEncoder;
import net.varkhan.base.conversion.Encoder;
import net.varkhan.base.conversion.serializer.EncodingException;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.BufferOverflowException;
import java.nio.ByteBuffer;
import java.nio.ReadOnlyBufferException;


/**
 * <b></b>.
 * <p/>
 *
 * @author varkhan
 * @date 12/17/14
 * @time 7:40 PM
 */
public class URLEncoder<C> extends AbstractEncoder<CharSequence,C> implements Encoder<CharSequence,C> {

    public long encode(CharSequence obj, OutputStream stm, C ctx) { return _encode(obj, stm); }

    public long encode(CharSequence obj, ByteBuffer buf, C ctx) { return _encode(obj, buf); }

    public long encode(CharSequence obj, byte[] dat, long pos, long len, C ctx) { return _encode(obj, dat, pos, len); }

    public long length(CharSequence obj, C ctx) { return _length(obj); }

    public static long _encode(CharSequence obj, OutputStream stm) {
        long cnt = 0;
        for (int i = 0; i < obj.length(); i++) {
            try {
                int c = obj.charAt(i);
//                if (c == ' ') {
//                    stm.write('+');
//                    cnt ++;
//                } else
                if(c<0x80) {
                    if (isUnreserved(c)) {
                        stm.write(c);
                        cnt++;
                    }
                    else {
                        stm.write('%');
                        stm.write(toByte((c&0x70)>>>4));
                        stm.write(toByte((c&0x0F)));
                        cnt += 3;
                    }
                } else if (c < 0x800) {
                    final int b1 = 0x80 | (0xBF & c);
                    final int b2 = 0xC0 | (0x1F & (c >>> 6));
                    stm.write('%');
                    stm.write(toByte((b2&0xF0)>>>4));
                    stm.write(toByte(b2&0x0F));
                    stm.write('%');
                    stm.write(toByte((b1&0xF0)>>>4));
                    stm.write(toByte(b1&0x0F));
                    cnt += 6;
                } else if (c < 0x10000) {
                    final int b1 = 0x80 | (0xBF & c);
                    final int b2 = 0x80 | (0x3F & (c >>> 6));
                    final int b3 = 0xE0 | (0x0F & (c >>> 12));
                    stm.write('%');
                    stm.write(toByte((b3&0xF0)>>>4));
                    stm.write(toByte(b3&0x0F));
                    stm.write('%');
                    stm.write(toByte((b2&0xF0)>>>4));
                    stm.write(toByte(b2&0x0F));
                    stm.write('%');
                    stm.write(toByte((b1&0xF0)>>>4));
                    stm.write(toByte(b1&0x0F));
                    cnt += 9;
                } else {
                    final int b1 = 0x80 | (0x3F & c);
                    final int b2 = 0x80 | (0x3F & (c >>> 6));
                    final int b3 = 0x80 | (0x3F & (c >>> 12));
                    final int b4 = 0xF0 | (0x07 & (c >>> 18));
                    stm.write('%');
                    stm.write(toByte((b4&0xF0)>>>4));
                    stm.write(toByte(b4&0x0F));
                    stm.write('%');
                    stm.write(toByte((b3&0xF0)>>>4));
                    stm.write(toByte(b3&0x0F));
                    stm.write('%');
                    stm.write(toByte((b2&0xF0)>>>4));
                    stm.write(toByte(b2&0x0F));
                    stm.write('%');
                    stm.write(toByte((b1&0xF0)>>>4));
                    stm.write(toByte(b1&0x0F));
                    cnt += 12;
                }
            }
            catch(IOException e) {
                throw new EncodingException(e);
            }
        }
        return cnt;
    }

    public static long _encode(CharSequence obj, ByteBuffer buf) {
        long cnt = 0;
        for (int i = 0; i < obj.length(); i++) {
            try {
                int c = obj.charAt(i);
//                if (c == ' ') {
//                    buf.put((byte) '+');
//                    cnt ++;
//                } else
                if(c<0x80) {
                    if (isUnreserved(c)) {
                        buf.put((byte) c);
                        cnt++;
                    }
                    else {
                        buf.put((byte) '%');
                        buf.put(toByte((c&0x70)>>>4));
                        buf.put(toByte((c&0x0F)));
                        cnt += 3;
                    }
                } else if (c < 0x800) {
                    final int b1 = 0x80 | (0xBF & c);
                    final int b2 = 0xC0 | (0x1F & (c >>> 6));
                    buf.put((byte) '%');
                    buf.put(toByte((b2&0xF0)>>>4));
                    buf.put(toByte(b2&0x0F));
                    buf.put((byte) '%');
                    buf.put(toByte((b1&0xF0)>>>4));
                    buf.put(toByte(b1&0x0F));
                    cnt += 6;
                } else if (c < 0x10000) {
                    final int b1 = 0x80 | (0xBF & c);
                    final int b2 = 0x80 | (0x3F & (c >>> 6));
                    final int b3 = 0xE0 | (0x0F & (c >>> 12));
                    buf.put((byte) '%');
                    buf.put(toByte((b3&0xF0)>>>4));
                    buf.put(toByte(b3&0x0F));
                    buf.put((byte) '%');
                    buf.put(toByte((b2&0xF0)>>>4));
                    buf.put(toByte(b2&0x0F));
                    buf.put((byte) '%');
                    buf.put(toByte((b1&0xF0)>>>4));
                    buf.put(toByte(b1&0x0F));
                    cnt += 9;
                } else {
                    final int b1 = 0x80 | (0x3F & c);
                    final int b2 = 0x80 | (0x3F & (c >>> 6));
                    final int b3 = 0x80 | (0x3F & (c >>> 12));
                    final int b4 = 0xF0 | (0x07 & (c >>> 18));
                    buf.put((byte) '%');
                    buf.put(toByte((b4&0xF0)>>>4));
                    buf.put(toByte(b4&0x0F));
                    buf.put((byte) '%');
                    buf.put(toByte((b3&0xF0)>>>4));
                    buf.put(toByte(b3&0x0F));
                    buf.put((byte) '%');
                    buf.put(toByte((b2&0xF0)>>>4));
                    buf.put(toByte(b2&0x0F));
                    buf.put((byte) '%');
                    buf.put(toByte((b1&0xF0)>>>4));
                    buf.put(toByte(b1&0x0F));
                    cnt += 12;
                }
            }
            catch(BufferOverflowException e) {
                throw new EncodingException(e);
            }
            catch(ReadOnlyBufferException e) {
                throw new EncodingException(e);
            }
        }
        return cnt;
    }

    public static long _encode(CharSequence obj, byte[] dat, long pos, long len) {
        long cnt = 0;
        int p=(int)pos;
        for (int i = 0; i < obj.length() && p<pos+len; i++) {
            try {
                int c = obj.charAt(i);
//                if (c == ' ') {
//                    dat[p++]=(byte) '+';
//                    cnt ++;
//                } else
                if(c<0x80) {
                    if (isUnreserved(c)) {
                        dat[p++]=(byte) c;
                        cnt++;
                    }
                    else {
                        dat[p++]=(byte) '%';
                        dat[p++]=toByte((c&0x70)>>>4);
                        dat[p++]=toByte((c&0x0F));
                        cnt += 3;
                    }
                } else if (c < 0x800) {
                    final int b1 = 0x80 | (0xBF & c);
                    final int b2 = 0xC0 | (0x1F & (c >>> 6));
                    dat[p++]=(byte) '%';
                    dat[p++]=toByte((b2&0xF0)>>>4);
                    dat[p++]=toByte(b2&0x0F);
                    dat[p++]=(byte) '%';
                    dat[p++]=toByte((b1&0xF0)>>>4);
                    dat[p++]=toByte(b1&0x0F);
                    cnt += 6;
                } else if (c < 0x10000) {
                    final int b1 = 0x80 | (0xBF & c);
                    final int b2 = 0x80 | (0x3F & (c >>> 6));
                    final int b3 = 0xE0 | (0x0F & (c >>> 12));
                    dat[p++]=(byte) '%';
                    dat[p++]=toByte((b3&0xF0)>>>4);
                    dat[p++]=toByte(b3&0x0F);
                    dat[p++]=(byte) '%';
                    dat[p++]=toByte((b2&0xF0)>>>4);
                    dat[p++]=toByte(b2&0x0F);
                    dat[p++]=(byte) '%';
                    dat[p++]=toByte((b1&0xF0)>>>4);
                    dat[p++]=toByte(b1&0x0F);
                    cnt += 9;
                } else {
                    final int b1 = 0x80 | (0x3F & c);
                    final int b2 = 0x80 | (0x3F & (c >>> 6));
                    final int b3 = 0x80 | (0x3F & (c >>> 12));
                    final int b4 = 0xF0 | (0x07 & (c >>> 18));
                    dat[p++]=(byte) '%';
                    dat[p++]=toByte((b4&0xF0)>>>4);
                    dat[p++]=toByte(b4&0x0F);
                    dat[p++]=(byte) '%';
                    dat[p++]=toByte((b3&0xF0)>>>4);
                    dat[p++]=toByte(b3&0x0F);
                    dat[p++]=(byte) '%';
                    dat[p++]=toByte((b2&0xF0)>>>4);
                    dat[p++]=toByte(b2&0x0F);
                    dat[p++]=(byte) '%';
                    dat[p++]=toByte((b1&0xF0)>>>4);
                    dat[p++]=toByte(b1&0x0F);
                    cnt += 12;
                }
            }
            catch(ArrayIndexOutOfBoundsException e) {
                throw new EncodingException(e);
            }
        }
        return cnt;
    }

    public static long _length(CharSequence obj) {
        long len = 0;
        for (int i = 0; i < obj.length(); i++) {
            int c = obj.charAt(i);
            if (c < 0x80) {
                if (isUnreserved(c)) len++;
                else len += 3;
            } else if (c < 0x800) {
                len += 6;
            } else if (c < 0x10000) {
                len += 9;
            } else {
                len += 12;
            }
        }
        return len;
    }


    public static <A extends Appendable> A _encode(CharSequence obj, A out) throws IOException {
        final int len = obj.length();
        for (int i = 0; i < len; i++) {
            int c = obj.charAt(i);
//            if (c == ' ') out.append('+');
//            else
            if(c<0x80) {
                if (isUnreserved(c))
                    out.append((char) c);
                else {
                    out.append('%');
                    out.append(toChar((c&0x70)>>>4));
                    out.append(toChar((c&0x0F)));
                }
            } else if (c < 0x800) {
                final int b1 = 0x80 | (0xBF & c);
                final int b2 = 0xC0 | (0x1F & (c >>> 6));
                out.append('%');
                out.append(toChar((b2&0xF0)>>>4));
                out.append(toChar(b2&0x0F));
                out.append('%');
                out.append(toChar((b1&0xF0)>>>4));
                out.append(toChar(b1&0x0F));
            } else if (c < 0x10000) {
                final int b1 = 0x80 | (0xBF & c);
                final int b2 = 0x80 | (0x3F & (c >>> 6));
                final int b3 = 0xE0 | (0x0F & (c >>> 12));
                out.append('%');
                out.append(toChar((b3&0xF0)>>>4));
                out.append(toChar(b3&0x0F));
                out.append('%');
                out.append(toChar((b2&0xF0)>>>4));
                out.append(toChar(b2&0x0F));
                out.append('%');
                out.append(toChar((b1&0xF0)>>>4));
                out.append(toChar(b1&0x0F));
            } else {
                final int b1 = 0x80 | (0x3F & c);
                final int b2 = 0x80 | (0x3F & (c >>> 6));
                final int b3 = 0x80 | (0x3F & (c >>> 12));
                final int b4 = 0xF0 | (0x07 & (c >>> 18));
                out.append('%');
                out.append(toChar((b4&0xF0)>>>4));
                out.append(toChar(b4&0x0F));
                out.append('%');
                out.append(toChar((b3&0xF0)>>>4));
                out.append(toChar(b3&0x0F));
                out.append('%');
                out.append(toChar((b2&0xF0)>>>4));
                out.append(toChar(b2&0x0F));
                out.append('%');
                out.append(toChar((b1&0xF0)>>>4));
                out.append(toChar(b1&0x0F));
            }
        }
        return out;
    }


    /**
     * The set of unreserved characters, per RFC 3986
     *
     * @param c a character
     * @return {@literal true} if the character is <em>unreserved</em>
     */
    public static boolean isUnreserved(int c) {
        return (c >= 'a' && c <= 'z') || (c >= 'A' && c <= 'Z') || (c >= '0' && c <= '9') || (c == '-' || c == '_' || c == '.' || c == '~');
    }

    /**
     * The set of reserved characters, per RFC 3986
     *
     * @param c a character
     * @return {@literal true} if the character is <em>reserved</em>
     */
    public static boolean isReserved(int c) {
        switch (c) {
            case '!':
            case '*':
            case '\'':
            case '(':
            case ')':
            case ';':
            case ':':
            case '@':
            case '&':
            case '=':
            case '+':
            case '$':
            case ',':
            case '/':
            case '?':
            case '#':
            case '[':
            case ']':
                return true;
            default:
                return false;
        }
    }

    private static char toChar(int v) {
        if (v < 0) throw new IllegalArgumentException("Invalid character code " + v);
        if (v < 10) return (char) ('0' + v);
        if (v < 16) return (char) ('A' + v - 10);
        throw new IllegalArgumentException("Invalid character code " + v);
    }

    private static byte toByte(int v) {
        if (v < 0) throw new IllegalArgumentException("Invalid character code " + v);
        if (v < 10) return (byte) ('0' + v);
        if (v < 16) return (byte) ('A' + v - 10);
        throw new IllegalArgumentException("Invalid character code " + v);
    }

}
