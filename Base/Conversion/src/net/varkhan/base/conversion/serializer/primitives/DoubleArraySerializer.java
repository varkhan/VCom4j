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
public class DoubleArraySerializer<C> implements Serializer<double[],C> {

    public double[] decode(InputStream stm, C ctx) { return _decode(stm); }

    public double[] decode(ByteBuffer buf, C ctx) { return _decode(buf); }

    public double[] decode(byte[] dat, long pos, long len, C ctx) { return _decode(dat, pos, len); }

    public long encode(double[] obj, OutputStream stm, C ctx) { return _encode(obj, stm); }

    public long encode(double[] obj, ByteBuffer buf, C ctx) { return _encode(obj, buf); }

    public long encode(double[] obj, byte[] dat, long pos, long len, C ctx) { return _encode(obj, dat, pos, len); }

    public long length(double[] obj, C ctx) { return _length(obj); }

    public static double[] _decode(InputStream stm) {
        try {
            long l=VariadicSerializer._decode(stm);
            double[] v=new double[(int) l];
            int i=0;
            while(i<l) {
                long x=0;
                int r=stm.read();
                if(r<0) throw new DecodingException();
                x|=0xFFL&r;
                r=stm.read();
                if(r<0) throw new DecodingException();
                x|=(0xFFL&r)<<8;
                r=stm.read();
                if(r<0) throw new DecodingException();
                x|=(0xFFL&r)<<16;
                r=stm.read();
                if(r<0) throw new DecodingException();
                x|=(0xFFL&r)<<24;
                r=stm.read();
                if(r<0) throw new DecodingException();
                x|=(0xFFL&r)<<32;
                r=stm.read();
                if(r<0) throw new DecodingException();
                x|=(0xFFL&r)<<40;
                r=stm.read();
                if(r<0) throw new DecodingException();
                x|=(0xFFL&r)<<48;
                r=stm.read();
                if(r<0) throw new DecodingException();
                x|=(0xFFL&r)<<56;
                v[i++]=Double.longBitsToDouble(x);
            }
            return v;
        }
        catch(IOException e) {
            throw new DecodingException(e);
        }
    }

    public static double[] _decode(ByteBuffer buf) {
        try {
            long l=VariadicSerializer._decode(buf);
            double[] v=new double[(int) l];
            int i=0;
            while(i<l) {
                long x=0;
                x|=0xFFL&buf.get();
                x|=(0xFFL&buf.get())<<8;
                x|=(0xFFL&buf.get())<<16;
                x|=(0xFFL&buf.get())<<24;
                x|=(0xFFL&buf.get())<<32;
                x|=(0xFFL&buf.get())<<40;
                x|=(0xFFL&buf.get())<<48;
                x|=(0xFFL&buf.get())<<56;
                v[i++]=Double.longBitsToDouble(x);
            }
            return v;
        }
        catch(BufferUnderflowException e) {
            throw new DecodingException(e);
        }
    }

    public static double[] _decode(byte[] dat, long pos, long len) {
        try {
            long l=VariadicSerializer._decode(dat, pos, len);
            pos+=VariadicSerializer._length(l);
            double[] v=new double[(int) l];
            int i=0;
            while(i<l) {
                long x=0;
                x|=0xFFL&dat[(int) pos++];
                x|=(0xFFL&dat[(int) pos++])<<8;
                x|=(0xFFL&dat[(int) pos++])<<16;
                x|=(0xFFL&dat[(int) pos++])<<24;
                x|=(0xFFL&dat[(int) pos++])<<32;
                x|=(0xFFL&dat[(int) pos++])<<40;
                x|=(0xFFL&dat[(int) pos++])<<48;
                x|=(0xFFL&dat[(int) pos++])<<56;
                v[i++]=Double.longBitsToDouble(x);
            }
            return v;
        }
        catch(ArrayIndexOutOfBoundsException e) {
            throw new DecodingException(e);
        }
    }

    public static long _encode(double[] v, OutputStream stm) {
        try {
            long l=v.length;
            long c=VariadicSerializer._encode(l, stm);
            int i=0;
            while(i<l) {
                long x=Double.doubleToRawLongBits(v[i++]);
                stm.write((int) (x&0xFF));
                stm.write((int) ((x>>>8)&0xFF));
                stm.write((int) ((x>>>16)&0xFF));
                stm.write((int) ((x>>>24)&0xFF));
                stm.write((int) ((x>>>32)&0xFF));
                stm.write((int) ((x>>>40)&0xFF));
                stm.write((int) ((x>>>48)&0xFF));
                stm.write((int) ((x>>>56)&0xFF));
            }
            return c+(i<<3);
        }
        catch(IOException e) {
            throw new EncodingException(e);
        }
    }

    public static long _encode(double[] v, ByteBuffer buf) {
        try {
            long l=v.length;
            long c=VariadicSerializer._encode(l, buf);
            int i=0;
            while(i<l) {
                long x=Double.doubleToRawLongBits(v[i++]);
                buf.put((byte) (x&0xFF));
                buf.put((byte) ((x>>>8)&0xFF));
                buf.put((byte) ((x>>>16)&0xFF));
                buf.put((byte) ((x>>>24)&0xFF));
                buf.put((byte) ((x>>>32)&0xFF));
                buf.put((byte) ((x>>>40)&0xFF));
                buf.put((byte) ((x>>>48)&0xFF));
                buf.put((byte) ((x>>>56)&0xFF));
            }
            return c+(i<<3);
        }
        catch(BufferOverflowException e) {
            throw new EncodingException(e);
        }
        catch(ReadOnlyBufferException e) {
            throw new EncodingException(e);
        }
    }

    public static long _encode(double[] v, byte[] dat, long pos, long len) {
        long l=v.length;
        long c=VariadicSerializer._encode(l, dat, pos, len);
        pos+=c;
        int i=0;
        while(i<l) {
            long x=Double.doubleToRawLongBits(v[i++]);
            dat[(int) pos++]=(byte) (x&0xFF);
            dat[(int) pos++]=(byte) ((x>>>8)&0xFF);
            dat[(int) pos++]=(byte) ((x>>>16)&0xFF);
            dat[(int) pos++]=(byte) ((x>>>24)&0xFF);
            dat[(int) pos++]=(byte) ((x>>>32)&0xFF);
            dat[(int) pos++]=(byte) ((x>>>40)&0xFF);
            dat[(int) pos++]=(byte) ((x>>>48)&0xFF);
            dat[(int) pos++]=(byte) ((x>>>56)&0xFF);
        }
        return c+(i<<3);
    }

    public static long _length(double[] v) {
        long l=v.length;
        return ((l<<3)+VariadicSerializer._length(l));
    }


}
