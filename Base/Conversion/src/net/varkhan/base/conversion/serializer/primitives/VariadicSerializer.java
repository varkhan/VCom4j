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
 * <b>Variadic integer serializer</b>.
 * <p/>
 * Serializes long integer values as a variable number of bytes.
 * <p/>
 * Values between 0 and 127 are serialized as this value, on one byte.
 * Higher values are serialized on 2 to 10 bytes, by increments of 7 bits
 * of value size. Negative values are encoded on 10 bytes.
 * <p/>
 * All bytes except the final one have their bit sign set to one,
 * the absence of the bit sign signals the end of the sequence.
 * <p/>
 *
 * @author varkhan
 * @date Nov 7, 2010
 * @time 11:58:15 PM
 */
public class VariadicSerializer<C> implements Serializer<Long,C> {

    public Long decode(InputStream stm, C ctx) { return _decode(stm); }

    public Long decode(ByteBuffer buf, C ctx) { return _decode(buf); }

    public Long decode(byte[] dat, long pos, long len, C ctx) { return _decode(dat, pos, len); }

    public long encode(Long obj, OutputStream stm, C ctx) { return _encode(obj, stm); }

    public long encode(Long obj, ByteBuffer buf, C ctx) { return _encode(obj, buf); }

    public long encode(Long obj, byte[] dat, long pos, long len, C ctx) { return _encode(obj, dat, pos, len); }

    public long length(Long obj, C ctx) { return _length(obj); }


    public static long _decode(InputStream stm) {
        try {
            long v=0;
            int off=0;
            int r=stm.read();
            if(r<0) throw new DecodingException();
            byte b=(byte) r;
            while(off++<9) {
                if(b>=0) return v|b;
                v=(v|(-b-1))<<7;
                r=stm.read();
                if(r<0) throw new DecodingException();
                b=(byte) r;
            }
            return v|b;
        }
        catch(IOException e) {
            throw new DecodingException(e);
        }
    }

    public static long _decode(ByteBuffer buf) {
        try {
            long v=0;
            int off=0;
            byte b=buf.get();
            while(off++<9) {
                if(b>=0) return v|b;
                v=(v|(-b-1))<<7;
                b=buf.get();
            }
            return v|b;
        }
        catch(BufferUnderflowException e) {
            throw new DecodingException(e);
        }
    }

    public static long _decode(byte[] dat, long pos, long len) {
        try {
            long v=0;
            int off=0;
            byte b=dat[(int) pos++];
            while(off++<9) {
                if(b>=0) return v|b;
                v=(v|(-b-1))<<7;
                if(off>len) throw new DecodingException("Buffer underflow at byte "+off);
                b=dat[(int) pos++];
            }
            return v|b;
        }
        catch(ArrayIndexOutOfBoundsException e) {
            throw new DecodingException(e);
        }
    }

    public static long _encode(long v, OutputStream stm) {
        try {
            final int l=(int) _length(v);
            final byte[] dat=new byte[l];
            int off=l-1;
            dat[(int) off]=(byte) (v&0x7F);
            while(off-->0) {
                v>>>=7;
                dat[off]=(byte) (-(v&0x7F)-1);
            }
            stm.write(dat);
            return l;
        }
        catch(IOException e) {
            throw new EncodingException(e);
        }
    }

    public static long _encode(long v, ByteBuffer buf) {
        try {
            final int l=(int) _length(v);
            final byte[] dat=new byte[l];
            int off=l-1;
            dat[(int) off]=(byte) (v&0x7F);
            while(off-->0) {
                v>>>=7;
                dat[off]=(byte) (-(v&0x7F)-1);
            }
            buf.put(dat);
            return l;
        }
        catch(BufferOverflowException e) {
            throw new EncodingException(e);
        }
        catch(ReadOnlyBufferException e) {
            throw new EncodingException(e);
        }
    }

    public static long _encode(long v, byte[] dat, long pos, long len) {
        final int l=(int) _length(v);
        int off=l-1;
        pos+=off;
        dat[(int) pos]=(byte) (v&0x7F);
        while(off-->0) {
            v>>>=7;
            dat[(int) --pos]=(byte) (-(v&0x7F)-1);
        }
        return l;
    }

    public static long _length(long v) {
        if(v<0) return 10;
        if(v<(1L<<7)) return 1;
        if(v<(1L<<14)) return 2;
        if(v<(1L<<21)) return 3;
        if(v<(1L<<28)) return 4;
        if(v<(1L<<35)) return 5;
        if(v<(1L<<42)) return 6;
        if(v<(1L<<49)) return 7;
        if(v<(1L<<56)) return 8;
        return 9;
    }

}
