/**
 *
 */
package net.varkhan.base.conversion.serializer.primitives;

import net.varkhan.base.conversion.serializer.DecodingException;
import net.varkhan.base.conversion.serializer.EncodingException;
import net.varkhan.base.conversion.serializer.Serializer;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.BufferOverflowException;
import java.nio.BufferUnderflowException;
import java.nio.ByteBuffer;
import java.nio.ReadOnlyBufferException;


/**
 * <b>.</b>
 * <p/>
 *
 * @author varkhan
 * @date Nov 7, 2010
 * @time 11:58:15 PM
 */
public class CharArraySerializer<C> implements Serializer<char[],C> {

    public char[] decode(InputStream stm, C ctx) { return _decode(stm); }

    public char[] decode(ByteBuffer buf, C ctx) { return _decode(buf); }

    public char[] decode(byte[] dat, long pos, long len, C ctx) { return _decode(dat, pos, len); }

    public long encode(char[] obj, OutputStream stm, C ctx) { return _encode(obj, stm); }

    public long encode(char[] obj, ByteBuffer buf, C ctx) { return _encode(obj, buf); }

    public long encode(char[] obj, byte[] dat, long pos, long len, C ctx) { return _encode(obj, dat, pos, len); }

    public long length(char[] obj, C ctx) { return _length(obj); }

    public static char[] _decode(InputStream stm) {
        try {
            long l=VariadicSerializer._decode(stm);
            char[] v=new char[(int) l];
            int i=0;
            while(i<l) {
                char x=0;
                int r=stm.read();
                if(r<0) throw new DecodingException();
                x|=0xFF&r;
                r=stm.read();
                if(r<0) throw new DecodingException();
                x|=(0xFF&r)<<8;
                v[i++]=x;
            }
            return v;
        }
        catch(IOException e) {
            throw new DecodingException(e);
        }
    }

    public static char[] _decode(ByteBuffer buf) {
        try {
            long l=VariadicSerializer._decode(buf);
            char[] v=new char[(int) l];
            int i=0;
            while(i<l) {
                char x=0;
                x|=0xFF&buf.get();
                x|=(0xFF&buf.get())<<8;
                v[i++]=x;
            }
            return v;
        }
        catch(BufferUnderflowException e) {
            throw new DecodingException(e);
        }
    }

    public static char[] _decode(byte[] dat, long pos, long len) {
        try {
            long l=VariadicSerializer._decode(dat, pos, len);
            pos+=VariadicSerializer._length(l);
            char[] v=new char[(int) l];
            int i=0;
            while(i<l) {
                char x=0;
                x|=0xFF&dat[(int) pos++];
                x|=(0xFF&dat[(int) pos++])<<8;
                v[i++]=x;
            }
            return v;
        }
        catch(ArrayIndexOutOfBoundsException e) {
            throw new DecodingException(e);
        }
    }

    public static long _encode(char[] v, OutputStream stm) {
        try {
            long l=v.length;
            long c=VariadicSerializer._encode(l, stm);
            int i=0;
            while(i<l) {
                char x=v[i++];
                stm.write(x&0xFF);
                stm.write((x>>>8)&0xFF);
            }
            return c+(i<<1);
        }
        catch(IOException e) {
            throw new EncodingException(e);
        }
    }

    public static long _encode(char[] v, ByteBuffer buf) {
        try {
            long l=v.length;
            long c=VariadicSerializer._encode(l, buf);
            int i=0;
            while(i<l) {
                char x=v[i++];
                buf.put((byte) (x&0xFF));
                buf.put((byte) ((x>>>8)&0xFF));
            }
            return c+(i<<1);
        }
        catch(BufferOverflowException e) {
            throw new EncodingException(e);
        }
        catch(ReadOnlyBufferException e) {
            throw new EncodingException(e);
        }
    }

    public static long _encode(char[] v, byte[] dat, long pos, long len) {
        long l=v.length;
        long c=VariadicSerializer._encode(l, dat, pos, len);
        pos+=c;
        int i=0;
        while(i<l) {
            char x=v[i++];
            dat[(int) pos++]=(byte) (x&0xFF);
            dat[(int) pos++]=(byte) ((x>>>8)&0xFF);
        }
        return c+(i<<1);
    }

    public static long _length(char[] v) {
        long l=v.length;
        return ((l<<1)+VariadicSerializer._length(l));
    }


}
