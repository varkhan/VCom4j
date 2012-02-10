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
public class FloatArraySerializer<C> implements Serializer<float[],C> {

    public float[] decode(InputStream stm, C ctx) { return _decode(stm); }

    public float[] decode(ByteBuffer buf, C ctx) { return _decode(buf); }

    public float[] decode(byte[] dat, long pos, long len, C ctx) { return _decode(dat, pos, len); }

    public long encode(float[] obj, OutputStream stm, C ctx) { return _encode(obj, stm); }

    public long encode(float[] obj, ByteBuffer buf, C ctx) { return _encode(obj, buf); }

    public long encode(float[] obj, byte[] dat, long pos, long len, C ctx) { return _encode(obj, dat, pos, len); }

    public long length(float[] obj, C ctx) { return _length(obj); }

    public static float[] _decode(InputStream stm) {
        try {
            long l=VariadicSerializer._decode(stm);
            float[] v=new float[(int) l];
            int i=0;
            while(i<l) {
                int x=0;
                int r=stm.read();
                if(r<0) throw new DecodingException();
                x|=0xFF&r;
                r=stm.read();
                if(r<0) throw new DecodingException();
                x|=(0xFF&r)<<8;
                r=stm.read();
                if(r<0) throw new DecodingException();
                x|=(0xFF&r)<<16;
                r=stm.read();
                if(r<0) throw new DecodingException();
                x|=(0xFF&r)<<24;
                v[i++]=Float.intBitsToFloat(x);
            }
            return v;
        }
        catch(IOException e) {
            throw new DecodingException(e);
        }
    }

    public static float[] _decode(ByteBuffer buf) {
        try {
            long l=VariadicSerializer._decode(buf);
            float[] v=new float[(int) l];
            int i=0;
            while(i<l) {
                int x=0;
                x|=0xFF&buf.get();
                x|=(0xFF&buf.get())<<8;
                x|=(0xFF&buf.get())<<16;
                x|=(0xFF&buf.get())<<24;
                v[i++]=Float.intBitsToFloat(x);
            }
            return v;
        }
        catch(BufferUnderflowException e) {
            throw new DecodingException(e);
        }
    }

    public static float[] _decode(byte[] dat, long pos, long len) {
        try {
            long l=VariadicSerializer._decode(dat, pos, len);
            pos+=VariadicSerializer._length(l);
            float[] v=new float[(int) l];
            int i=0;
            while(i<l) {
                int x=0;
                x|=0xFF&dat[(int) pos++];
                x|=(0xFF&dat[(int) pos++])<<8;
                x|=(0xFF&dat[(int) pos++])<<16;
                x|=(0xFF&dat[(int) pos++])<<24;
                v[i++]=Float.intBitsToFloat(x);
            }
            return v;
        }
        catch(ArrayIndexOutOfBoundsException e) {
            throw new DecodingException(e);
        }
    }

    public static long _encode(float[] v, OutputStream stm) {
        try {
            long l=v.length;
            long c=VariadicSerializer._encode(l, stm);
            int i=0;
            while(i<l) {
                int x=Float.floatToRawIntBits(v[i++]);
                stm.write(x&0xFF);
                stm.write((x>>>8)&0xFF);
                stm.write((x>>>16)&0xFF);
                stm.write((x>>>24)&0xFF);
            }
            return c+(i<<2);
        }
        catch(IOException e) {
            throw new EncodingException(e);
        }
    }

    public static long _encode(float[] v, ByteBuffer buf) {
        try {
            long l=v.length;
            long c=VariadicSerializer._encode(l, buf);
            int i=0;
            while(i<l) {
                int x=Float.floatToRawIntBits(v[i++]);
                buf.put((byte) (x&0xFF));
                buf.put((byte) ((x>>>8)&0xFF));
                buf.put((byte) ((x>>>16)&0xFF));
                buf.put((byte) ((x>>>24)&0xFF));
            }
            return c+(i<<2);
        }
        catch(BufferOverflowException e) {
            throw new EncodingException(e);
        }
        catch(ReadOnlyBufferException e) {
            throw new EncodingException(e);
        }
    }

    public static long _encode(float[] v, byte[] dat, long pos, long len) {
        long l=v.length;
        long c=VariadicSerializer._encode(l, dat, pos, len);
        pos+=c;
        int i=0;
        while(i<l) {
            int x=Float.floatToRawIntBits(v[i++]);
            dat[(int) pos++]=(byte) (x&0xFF);
            dat[(int) pos++]=(byte) ((x>>>8)&0xFF);
            dat[(int) pos++]=(byte) ((x>>>16)&0xFF);
            dat[(int) pos++]=(byte) ((x>>>24)&0xFF);
        }
        return c+(i<<2);
    }

    public static long _length(float[] v) {
        long l=v.length;
        return ((l<<2)+VariadicSerializer._length(l));
    }


}
