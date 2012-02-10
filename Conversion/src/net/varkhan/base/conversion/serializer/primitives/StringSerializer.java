package net.varkhan.base.conversion.serializer.primitives;

import net.varkhan.base.conversion.serializer.DecodingException;
import net.varkhan.base.conversion.serializer.EncodingException;
import net.varkhan.base.conversion.serializer.Serializer;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.BufferOverflowException;
import java.nio.ByteBuffer;
import java.nio.ReadOnlyBufferException;


/**
 * <b>A String serializer</b>.
 * <p/>
 * Serializes Java Strings (UTF-16) as a variadic length and a series of UTF-8
 * variable-width code points (on 1 to 4 bytes, depending on the code point).
 * <p/>
 *
 * @author varkhan
 * @date 1/30/11
 * @time 5:42 AM
 */
public class StringSerializer<C> implements Serializer<CharSequence,C> {


    public String decode(InputStream stm, C ctx) { return _decode(stm); }

    public String decode(ByteBuffer buf, C ctx) { return _decode(buf); }

    public String decode(byte[] dat, long pos, long len, C ctx) { return _decode(dat, pos, len); }

    public long encode(CharSequence obj, OutputStream stm, C ctx) { return _encode(obj, stm); }

    public long encode(CharSequence obj, ByteBuffer buf, C ctx) { return _encode(obj, buf); }

    public long encode(CharSequence obj, byte[] dat, long pos, long len, C ctx) { return _encode(obj, dat, pos, len); }

    public long length(CharSequence obj, C ctx) { return _length(obj); }


    public static String _decode(InputStream stm) {
        try {
            int l=(int) VariadicSerializer._decode(stm);
            char[] ca=new char[l];
            int p=0;
            while(p<l) {
                int r=stm.read();
                if(r<0) throw new DecodingException();
                int b=0xFF&r;
                if(b<0x80) {
                    ca[p++]=(char) (b&0x7F);
                }
                else if(b<0xC0) {
                    // Invalid character encoding, or in the middle of a character
                    throw new DecodingException();
                }
                else if(b<0xE0) {
                    r=stm.read();
                    if(r<0) throw new DecodingException();
                    int b2=0xFF&r;
                    ca[p++]=(char) (((b&0x1F)<<6)|(b2&0x3F));
                }
                else if(b<0xF0) {
                    r=stm.read();
                    if(r<0) throw new DecodingException();
                    int b2=0xFF&r;
                    r=stm.read();
                    if(r<0) throw new DecodingException();
                    int b3=0xFF&r;
                    ca[p++]=(char) (((b&0x0F)<<12)|((b2&0x3F)<<6)|(b3&0x3F));
                }
                else if(b<0xF8) {
                    r=stm.read();
                    if(r<0) throw new DecodingException();
                    int b2=0xFF&r;
                    r=stm.read();
                    if(r<0) throw new DecodingException();
                    int b3=0xFF&r;
                    r=stm.read();
                    if(r<0) throw new DecodingException();
                    int b4=0xFF&r;
                    ca[p++]=(char) (((b&0x07)<<18)|((b2&0x3F)<<12)|((b3&0x3F)<<6)|(b4&0x3F));
                }
                else {
                    // Invalid character encoding, or in the middle of a character
                    throw new DecodingException();
                }
            }
            return new String(ca);
        }
        catch(ArrayIndexOutOfBoundsException e) {
            throw new DecodingException(e);
        }
        catch(IOException e) {
            throw new DecodingException(e);
        }
    }

    public static String _decode(ByteBuffer buf) {
        try {
            int l=(int) VariadicSerializer._decode(buf);
            char[] ca=new char[l];
            int p=0;
            while(p<l) {
                int b=0xFF&buf.get();
                if(b<0x80) {
                    ca[p++]=(char) (b&0x7F);
                }
                else if(b<0xC0) {
                    // Invalid character encoding, or in the middle of a character
                    throw new DecodingException();
                }
                else if(b<0xE0) {
                    ca[p++]=(char) (((b&0x1F)<<6)|(buf.get()&0x3F));
                }
                else if(b<0xF0) {
                    ca[p++]=(char) (((b&0x0F)<<12)|((buf.get()&0x3F)<<6)|(buf.get()&0x3F));
                    p++;
                }
                else if(b<0xF8) {
                    ca[p++]=(char) (((b&0x07)<<18)|((buf.get()&0x3F)<<12)|((buf.get()&0x3F)<<6)|(buf.get()&0x3F));
                    p++;
                }
                else {
                    // Invalid character encoding, or in the middle of a character
                    throw new DecodingException();
                }
            }
            return new String(ca);
        }
        catch(BufferOverflowException e) {
            throw new DecodingException(e);
        }
        catch(ReadOnlyBufferException e) {
            throw new DecodingException(e);
        }
    }

    public static String _decode(byte[] dat, long pos, long len) {
        try {
            int l=(int) VariadicSerializer._decode(dat, pos, len);
            char[] ca=new char[l];
            pos+=VariadicSerializer._length(l);
            len-=VariadicSerializer._length(l);
            int i=0, p=0;
            while(p<l) {
                int b=0xFF&dat[i++];
                if(b<0x80) {
                    ca[p++]=(char) (b&0x7F);
                }
                else if(b<0xC0) {
                    // Invalid character encoding, or in the middle of a character
                    throw new DecodingException();
                }
                else if(b<0xE0) {
                    ca[p++]=(char) (((b&0x1F)<<6)|(dat[i++]&0x3F));
                }
                else if(b<0xF0) {
                    ca[p++]=(char) (((b&0x0F)<<12)|((dat[i++]&0x3F)<<6)|(dat[i++]&0x3F));
                    p++;
                }
                else if(b<0xF8) {
                    ca[p++]=(char) (((b&0x07)<<18)|((dat[i++]&0x3F)<<12)|((dat[i++]&0x3F)<<6)|(dat[i++]&0x3F));
                    p++;
                }
                else {
                    // Invalid character encoding, or in the middle of a character
                    throw new DecodingException();
                }
            }
            return new String(ca);
        }
        catch(ArrayIndexOutOfBoundsException e) {
            throw new DecodingException(e);
        }
    }

    public static long _encode(CharSequence obj, OutputStream stm) {
        try {
            long len=VariadicSerializer._encode((long) obj.length(), stm);
            int i=0;
            while(i<obj.length()) {
                char c=obj.charAt(i++);
                if(c<0x80) {
                    stm.write((byte) (0x7F&c));
                    len++;
                }
                else if(c<0x800) {
                    stm.write((byte) (0xC0|(0x1F&(c>>>6))));
                    stm.write((byte) (0x3F&c));
                    len+=2;
                }
                else if(c<0x10000) {
                    stm.write((byte) (0xE0|(0x0F&(c>>>12))));
                    stm.write((byte) (0x3F&(c>>>6)));
                    stm.write((byte) (0x3F&c));
                    len+=3;
                }
                else {
                    stm.write((byte) (0xF0|(0x07&(c>>>18))));
                    stm.write((byte) (0x3F&(c>>>12)));
                    stm.write((byte) (0x3F&(c>>>6)));
                    stm.write((byte) (0x3F&c));
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
            long len=VariadicSerializer._encode((long) obj.length(), buf);
            int i=0;
            while(i<obj.length()) {
                char c=obj.charAt(i++);
                if(c<0x80) {
                    buf.put((byte) (0x7F&c));
                    len++;
                }
                else if(c<0x800) {
                    buf.put((byte) (0xC0|(0x1F&(c>>>6))));
                    buf.put((byte) (0x3F&c));
                    len+=2;
                }
                else if(c<0x10000) {
                    buf.put((byte) (0xE0|(0x0F&(c>>>12))));
                    buf.put((byte) (0x3F&(c>>>6)));
                    buf.put((byte) (0x3F&c));
                    len+=3;
                }
                else {
                    buf.put((byte) (0xF0|(0x07&(c>>>18))));
                    buf.put((byte) (0x3F&(c>>>12)));
                    buf.put((byte) (0x3F&(c>>>6)));
                    buf.put((byte) (0x3F&c));
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
            p+=VariadicSerializer._encode((long) obj.length(), dat, p, len);
            int i=0;
            while(i<obj.length()) {
                char c=obj.charAt(i++);
                if(c<0x80) {
                    dat[p++]=(byte) (0x7F&c);
                }
                else if(c<0x800) {
                    dat[p++]=(byte) (0xC0|(0x1F&(c>>>6)));
                    dat[p++]=(byte) (0x3F&c);
                }
                else if(c<0x10000) {
                    dat[p++]=(byte) (0xE0|(0x0F&(c>>>12)));
                    dat[p++]=(byte) (0x3F&(c>>>6));
                    dat[p++]=(byte) (0x3F&c);
                }
                else {
                    dat[p++]=(byte) (0xF0|(0x07&(c>>>18)));
                    dat[p++]=(byte) (0x3F&(c>>>12));
                    dat[p++]=(byte) (0x3F&(c>>>6));
                    dat[p++]=(byte) (0x3F&c);
                }
            }
            return p-pos;
        }
        catch(ArrayIndexOutOfBoundsException e) {
            throw new EncodingException(e);
        }
    }

    public static long _length(CharSequence obj) {
        long len=VariadicSerializer._length((long) obj.length());
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
