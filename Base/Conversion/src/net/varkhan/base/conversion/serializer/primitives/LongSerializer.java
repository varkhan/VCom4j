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
public class LongSerializer<C> implements Serializer<Long,C> {

    public Long decode(InputStream stm, C ctx) { return _decode(stm); }

    public Long decode(ByteBuffer buf, C ctx) { return _decode(buf); }

    public Long decode(byte[] dat, long pos, long len, C ctx) { return _decode(dat, pos, len); }

    public long encode(Long obj, OutputStream stm, C ctx) { return _encode(obj, stm); }

    public long encode(Long obj, ByteBuffer buf, C ctx) { return _encode(obj, buf); }

    public long encode(Long obj, byte[] dat, long pos, long len, C ctx) { return _encode(obj, dat, pos, len); }

    public long length(Long obj, C ctx) { return _length(obj); }

    public static long _decode(InputStream stm) {
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
            return v;
        }
        catch(IOException e) {
            throw new DecodingException(e);
        }
    }

    public static long _decode(ByteBuffer buf) {
        try {
            long v=buf.get()&0xFF;
            v=(v<<8)|(buf.get()&0xFF);
            v=(v<<8)|(buf.get()&0xFF);
            v=(v<<8)|(buf.get()&0xFF);
            v=(v<<8)|(buf.get()&0xFF);
            v=(v<<8)|(buf.get()&0xFF);
            v=(v<<8)|(buf.get()&0xFF);
            v=(v<<8)|(buf.get()&0xFF);
            return v;
        }
        catch(BufferUnderflowException e) {
            throw new DecodingException(e);
        }
    }

    public static long _decode(byte[] dat, long pos, long len) {
        try {
            long v=dat[(int) pos++]&0xFF;
            v=(v<<8)|(dat[(int) pos++]&0xFF);
            v=(v<<8)|(dat[(int) pos++]&0xFF);
            v=(v<<8)|(dat[(int) pos++]&0xFF);
            v=(v<<8)|(dat[(int) pos++]&0xFF);
            v=(v<<8)|(dat[(int) pos++]&0xFF);
            v=(v<<8)|(dat[(int) pos++]&0xFF);
            v=(v<<8)|(dat[(int) pos]&0xFF);
            return v;
        }
        catch(ArrayIndexOutOfBoundsException e) {
            throw new DecodingException(e);
        }
    }

    public static long _encode(long v, OutputStream stm) {
        try {
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

    public static long _encode(long v, ByteBuffer buf) {
        try {
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

    public static long _encode(long v, byte[] dat, long pos, long len) {
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

    public static long _length(long v) {
        return 8;
    }


}
