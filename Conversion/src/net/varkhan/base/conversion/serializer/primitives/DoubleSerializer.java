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
public class DoubleSerializer<C> implements Serializer<Double,C> {

    public Double decode(InputStream stm, C ctx) { return _decode(stm); }

    public Double decode(ByteBuffer buf, C ctx) { return _decode(buf); }

    public Double decode(byte[] dat, long pos, long len, C ctx) { return _decode(dat, pos, len); }

    public long encode(Double obj, OutputStream stm, C ctx) { return _encode(obj, stm); }

    public long encode(Double obj, ByteBuffer buf, C ctx) { return _encode(obj, buf); }

    public long encode(Double obj, byte[] dat, long pos, long len, C ctx) { return _encode(obj, dat, pos, len); }

    public long length(Double obj, C ctx) { return _length(obj); }

    public static double _decode(InputStream stm) {
        try {
            int r=stm.read();
            if(r<0) throw new DecodingException();
            long v=r&0xFF;
            r=stm.read();
            if(r<0) throw new DecodingException();
            v=(v<<8)|(r&0xFF);
            r=stm.read();
            if(r<0) throw new DecodingException();
            v=(v<<8)|(r&0xFF);
            r=stm.read();
            if(r<0) throw new DecodingException();
            v=(v<<8)|(r&0xFF);
            r=stm.read();
            if(r<0) throw new DecodingException();
            v=(v<<8)|(r&0xFF);
            r=stm.read();
            if(r<0) throw new DecodingException();
            v=(v<<8)|(r&0xFF);
            r=stm.read();
            if(r<0) throw new DecodingException();
            v=(v<<8)|(r&0xFF);
            r=stm.read();
            if(r<0) throw new DecodingException();
            v=(v<<8)|(r&0xFF);
            return Double.longBitsToDouble(v);
        }
        catch(IOException e) {
            throw new DecodingException(e);
        }
    }

    public static double _decode(ByteBuffer buf) {
        try {
            long v=buf.get()&0xFF;
            v=(v<<8)|(buf.get()&0xFF);
            v=(v<<8)|(buf.get()&0xFF);
            v=(v<<8)|(buf.get()&0xFF);
            v=(v<<8)|(buf.get()&0xFF);
            v=(v<<8)|(buf.get()&0xFF);
            v=(v<<8)|(buf.get()&0xFF);
            v=(v<<8)|(buf.get()&0xFF);
            return Double.longBitsToDouble(v);
        }
        catch(BufferUnderflowException e) {
            throw new DecodingException(e);
        }
    }

    public static double _decode(byte[] dat, long pos, long len) {
        try {
            long v=dat[(int) pos++]&0xFF;
            v=(v<<8)|(dat[(int) pos++]&0xFF);
            v=(v<<8)|(dat[(int) pos++]&0xFF);
            v=(v<<8)|(dat[(int) pos++]&0xFF);
            v=(v<<8)|(dat[(int) pos++]&0xFF);
            v=(v<<8)|(dat[(int) pos++]&0xFF);
            v=(v<<8)|(dat[(int) pos++]&0xFF);
            v=(v<<8)|(dat[(int) pos]&0xFF);
            return Double.longBitsToDouble(v);
        }
        catch(ArrayIndexOutOfBoundsException e) {
            throw new DecodingException(e);
        }
    }

    public static long _encode(double d, OutputStream stm) {
        try {
            long v=Double.doubleToRawLongBits(d);
            stm.write((byte) ((v>>>56)&0xFF));
            stm.write((byte) ((v>>>48)&0xFF));
            stm.write((byte) ((v>>>40)&0xFF));
            stm.write((byte) ((v>>>32)&0xFF));
            stm.write((byte) ((v>>>24)&0xFF));
            stm.write((byte) ((v>>>16)&0xFF));
            stm.write((byte) ((v>>>8)&0xFF));
            stm.write((byte) ((v)&0xFF));
            return 8;
        }
        catch(IOException e) {
            throw new EncodingException(e);
        }
    }

    public static long _encode(double d, ByteBuffer buf) {
        try {
            long v=Double.doubleToRawLongBits(d);
            buf.put((byte) ((v>>>56)&0xFF));
            buf.put((byte) ((v>>>48)&0xFF));
            buf.put((byte) ((v>>>40)&0xFF));
            buf.put((byte) ((v>>>32)&0xFF));
            buf.put((byte) ((v>>>24)&0xFF));
            buf.put((byte) ((v>>>16)&0xFF));
            buf.put((byte) ((v>>>8)&0xFF));
            buf.put((byte) ((v)&0xFF));
            return 8;
        }
        catch(BufferOverflowException e) {
            throw new EncodingException(e);
        }
        catch(ReadOnlyBufferException e) {
            throw new EncodingException(e);
        }
    }

    public static long _encode(double d, byte[] dat, long pos, long len) {
        long v=Double.doubleToRawLongBits(d);
        dat[(int) pos++]=(byte) ((v>>>56)&0xFF);
        dat[(int) pos++]=(byte) ((v>>>48)&0xFF);
        dat[(int) pos++]=(byte) ((v>>>40)&0xFF);
        dat[(int) pos++]=(byte) ((v>>>32)&0xFF);
        dat[(int) pos++]=(byte) ((v>>>24)&0xFF);
        dat[(int) pos++]=(byte) ((v>>>16)&0xFF);
        dat[(int) pos++]=(byte) ((v>>>8)&0xFF);
        dat[(int) pos++]=(byte) ((v)&0xFF);
        return 8;
    }

    public static long _length(double v) {
        return 8;
    }


}
