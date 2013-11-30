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
 * Encodes Java CharSequences (UTF-16) as a series of UTF-8 variable-width code
 * points (on 1 to 4 bytes, depending on the code point).
 * <p/>
 *
 * @author varkhan
 * @date 1/30/11
 * @time 5:42 AM
 */
public class UTF8Encoder<C> extends AbstractEncoder<CharSequence,C> implements Encoder<CharSequence,C> {


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
                if(c<0x80) {
                    stm.write((byte) (0x7F&c));
                    len++;
                }
                else if(c<0x800) {
                    stm.write((byte) (0xC0|(0x1F&(c>>>6))));
                    stm.write((byte) (0x80|(0x3F&c)));
                    len+=2;
                }
                else if(c<0x10000) {
                    stm.write((byte) (0xE0|(0x0F&(c>>>12))));
                    stm.write((byte) (0x80|(0x3F&(c>>>6))));
                    stm.write((byte) (0x80|(0x3F&c)));
                    len+=3;
                }
                else {
                    stm.write((byte) (0xF0|(0x07&(c>>>18))));
                    stm.write((byte) (0x80|(0x3F&(c>>>12))));
                    stm.write((byte) (0x80|(0x3F&(c>>>6))));
                    stm.write((byte) (0x80|(0x3F&c)));
                    len+=4;
                }
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
                if(c<0x80) {
                    buf.put((byte) (0x7F&c));
                    len++;
                }
                else if(c<0x800) {
                    buf.put((byte) (0xC0|(0x1F&(c>>>6))));
                    buf.put((byte) (0x80|(0x3F&c)));
                    len+=2;
                }
                else if(c<0x10000) {
                    buf.put((byte) (0xE0|(0x0F&(c>>>12))));
                    buf.put((byte) (0x80|(0x3F&(c>>>6))));
                    buf.put((byte) (0x80|(0x3F&c)));
                    len+=3;
                }
                else {
                    buf.put((byte) (0xF0|(0x07&(c>>>18))));
                    buf.put((byte) (0x80|(0x3F&(c>>>12))));
                    buf.put((byte) (0x80|(0x3F&(c>>>6))));
                    buf.put((byte) (0x80|(0x3F&c)));
                    len+=4;
                }
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
            while(i<obj.length()) {
                if(p-pos>=len) return p-pos;
                char c=obj.charAt(i++);
                if(c<0x80) {
                    dat[p++]=(byte) (0x7F&c);
                }
                else if(c<0x800) {
                    dat[p++]=(byte) (0xC0|(0x1F&(c>>>6)));
                    if(p-pos>=len) throw new EncodingException();
                    dat[p++]=(byte) (0x80|(0x3F&c));
                }
                else if(c<0x10000) {
                    dat[p++]=(byte) (0xE0|(0x0F&(c>>>12)));
                    if(p-pos>=len) throw new EncodingException();
                    dat[p++]=(byte) (0x80|(0x3F&(c>>>6)));
                    if(p-pos>=len) throw new EncodingException();
                    dat[p++]=(byte) (0x80|(0x3F&c));
                }
                else {
                    dat[p++]=(byte) (0xF0|(0x07&(c>>>18)));
                    if(p-pos>=len) throw new EncodingException();
                    dat[p++]=(byte) (0x80|(0x3F&(c>>>12)));
                    if(p-pos>=len) throw new EncodingException();
                    dat[p++]=(byte) (0x80|(0x3F&(c>>>6)));
                    if(p-pos>=len) throw new EncodingException();
                    dat[p++]=(byte) (0x80|(0x3F&c));
                }
            }
            return p-pos;
        }
        catch(ArrayIndexOutOfBoundsException e) {
            throw new EncodingException(e);
        }
    }

    public static long _length(CharSequence obj) {
        long len=0;
        for(int i=0;i<obj.length();i++) {
            char c=obj.charAt(i);
            if(c<0x80) len++;
            else if(c<0x800) len+=2;
            else if(c<0x10000) len+=3;
            else len+=4;
        }
        return len;
    }

}
