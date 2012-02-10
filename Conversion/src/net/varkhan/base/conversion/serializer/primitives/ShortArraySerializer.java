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
public class ShortArraySerializer<C> implements Serializer<short[],C> {

    public short[] decode(InputStream stm, C ctx) { return _decode(stm); }

    public short[] decode(ByteBuffer buf, C ctx) { return _decode(buf); }

    public short[] decode(byte[] dat, long pos, long len, C ctx) { return _decode(dat, pos, len); }

    public long encode(short[] obj, OutputStream stm, C ctx) { return _encode(obj, stm); }

    public long encode(short[] obj, ByteBuffer buf, C ctx) { return _encode(obj, buf); }

    public long encode(short[] obj, byte[] dat, long pos, long len, C ctx) { return _encode(obj, dat, pos, len); }

    public long length(short[] obj, C ctx) { return _length(obj); }

    public static short[] _decode(InputStream stm) {
        try {
            long l=VariadicSerializer._decode(stm);
            short[] v=new short[(int) l];
            int i=0;
            while(i<l) {
                short x=0;
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

    public static short[] _decode(ByteBuffer buf) {
        try {
            long l=VariadicSerializer._decode(buf);
            short[] v=new short[(int) l];
            int i=0;
            while(i<l) {
                short x=0;
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

    public static short[] _decode(byte[] dat, long pos, long len) {
        try {
            long l=VariadicSerializer._decode(dat, pos, len);
            pos+=VariadicSerializer._length(l);
            short[] v=new short[(int) l];
            int i=0;
            while(i<l) {
                short x=0;
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

    public static long _encode(short[] v, OutputStream stm) {
        try {
            long l=v.length;
            long c=VariadicSerializer._encode(l, stm);
            int i=0;
            while(i<l) {
                short x=v[i++];
                stm.write(x&0xFF);
                stm.write((x>>>8)&0xFF);
            }
            return c+(i<<1);
        }
        catch(IOException e) {
            throw new EncodingException(e);
        }
    }

    public static long _encode(short[] v, ByteBuffer buf) {
        try {
            long l=v.length;
            long c=VariadicSerializer._encode(l, buf);
            int i=0;
            while(i<l) {
                short x=v[i++];
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

    public static long _encode(short[] v, byte[] dat, long pos, long len) {
        long l=v.length;
        long c=VariadicSerializer._encode(l, dat, pos, len);
        pos+=c;
        int i=0;
        while(i<l) {
            short x=v[i++];
            dat[(int) pos++]=(byte) (x&0xFF);
            dat[(int) pos++]=(byte) ((x>>>8)&0xFF);
        }
        return c+(i<<1);
    }

    public static long _length(short[] v) {
        long l=v.length;
        return ((l<<1)+VariadicSerializer._length(l));
    }


}
