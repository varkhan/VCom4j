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
public class FloatSerializer<C> implements Serializer<Float,C> {

    public Float decode(InputStream stm, C ctx) { return _decode(stm); }

    public Float decode(ByteBuffer buf, C ctx) { return _decode(buf); }

    public Float decode(byte[] dat, long pos, long len, C ctx) { return _decode(dat, pos, len); }

    public long encode(Float obj, OutputStream stm, C ctx) { return _encode(obj, stm); }

    public long encode(Float obj, ByteBuffer buf, C ctx) { return _encode(obj, buf); }

    public long encode(Float obj, byte[] dat, long pos, long len, C ctx) { return _encode(obj, dat, pos, len); }

    public long length(Float obj, C ctx) { return _length(obj); }

    public static float _decode(InputStream stm) {
        try {
            int r=stm.read();
            if(r<0) throw new DecodingException();
            int v=r&0xFF;
            r=stm.read();
            if(r<0) throw new DecodingException();
            v=(v<<8)|(r&0xFF);
            r=stm.read();
            if(r<0) throw new DecodingException();
            v=(v<<8)|(r&0xFF);
            r=stm.read();
            if(r<0) throw new DecodingException();
            v=(v<<8)|(r&0xFF);
            return Float.intBitsToFloat(v);
        }
        catch(IOException e) {
            throw new DecodingException(e);
        }
    }

    public static float _decode(ByteBuffer buf) {
        try {
            int v=buf.get()&0xFF;
            v=(v<<8)|(buf.get()&0xFF);
            v=(v<<8)|(buf.get()&0xFF);
            v=(v<<8)|(buf.get()&0xFF);
            return Float.intBitsToFloat(v);
        }
        catch(BufferUnderflowException e) {
            throw new DecodingException(e);
        }
    }

    public static float _decode(byte[] dat, long pos, long len) {
        try {
            int v=dat[(int) pos++]&0xFF;
            v=(v<<8)|(dat[(int) pos++]&0xFF);
            v=(v<<8)|(dat[(int) pos++]&0xFF);
            v=(v<<8)|(dat[(int) pos]&0xFF);
            return Float.intBitsToFloat(v);
        }
        catch(ArrayIndexOutOfBoundsException e) {
            throw new DecodingException(e);
        }
    }

    public static long _encode(float f, OutputStream stm) {
        try {
            int v=Float.floatToRawIntBits(f);
            stm.write((byte) ((v>>>24)&0xFF));
            stm.write((byte) ((v>>>16)&0xFF));
            stm.write((byte) ((v>>>8)&0xFF));
            stm.write((byte) ((v)&0xFF));
            return 4;
        }
        catch(IOException e) {
            throw new EncodingException(e);
        }
    }

    public static long _encode(float f, ByteBuffer buf) {
        try {
            int v=Float.floatToRawIntBits(f);
            buf.put((byte) ((v>>>24)&0xFF));
            buf.put((byte) ((v>>>16)&0xFF));
            buf.put((byte) ((v>>>8)&0xFF));
            buf.put((byte) ((v)&0xFF));
            return 4;
        }
        catch(BufferOverflowException e) {
            throw new EncodingException(e);
        }
        catch(ReadOnlyBufferException e) {
            throw new EncodingException(e);
        }
    }

    public static long _encode(float f, byte[] dat, long pos, long len) {
        int v=Float.floatToRawIntBits(f);
        dat[(int) pos++]=(byte) ((v>>>24)&0xFF);
        dat[(int) pos++]=(byte) ((v>>>16)&0xFF);
        dat[(int) pos++]=(byte) ((v>>>8)&0xFF);
        dat[(int) pos++]=(byte) ((v)&0xFF);
        return 4;
    }

    public static long _length(float v) {
        return 4;
    }


}
