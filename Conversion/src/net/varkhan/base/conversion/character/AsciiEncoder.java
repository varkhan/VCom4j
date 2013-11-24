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
 * <b>An UTF-8 CharSequence encoder.</b>.
 * <p/>
 * Encodes Java CharSequences (UTF-16) as a series of ASCII 7-bit characters.
 * <p/>
 *
 * @author varkhan
 * @date 1/30/11
 * @time 5:42 AM
 */
public class AsciiEncoder<C> extends AbstractEncoder<CharSequence,C> implements Encoder<CharSequence,C> {


    public long encode(CharSequence obj, OutputStream stm, C ctx) { return _encode(obj, stm); }

    public long encode(CharSequence obj, ByteBuffer buf, C ctx) { return _encode(obj, buf); }

    public long encode(CharSequence obj, byte[] dat, long pos, long len, C ctx) { return _encode(obj, dat, pos, len); }

    public long length(CharSequence obj, C ctx) { return _length(obj); }


    public static long _encode(CharSequence obj, OutputStream stm) {
        try {
            long len=0;
            int i=0;
            while(i<obj.length()) {
                char c=obj.charAt(i++);
                stm.write((byte) (0x7F&c));
                len++;
            }
            return len;
        }
        catch(IOException e) {
            throw new EncodingException(e);
        }
    }

    public static long _encode(CharSequence obj, ByteBuffer buf) {
        try {
            long len=0;
            int i=0;
            while(i<obj.length()) {
                char c=obj.charAt(i++);
                buf.put((byte) (0x7F&c));
                len++;
            }
            return len;
        }
        catch(BufferOverflowException e) {
            throw new EncodingException(e);
        }
        catch(ReadOnlyBufferException e) {
            throw new EncodingException(e);
        }
    }

    public static long _encode(CharSequence obj, byte[] dat, long pos, long len) {
        try {
            int p=(int) pos;
            int i=0;
            while(i<obj.length() && p<pos+len) {
                char c=obj.charAt(i++);
                dat[p++]=(byte) (0x7F&c);
            }
            return p-pos;
        }
        catch(ArrayIndexOutOfBoundsException e) {
            throw new EncodingException(e);
        }
    }

    public static long _length(CharSequence obj) {
        return obj.length();
    }

}
