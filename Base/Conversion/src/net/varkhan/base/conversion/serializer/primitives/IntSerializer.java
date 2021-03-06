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
public class IntSerializer<C> implements Serializer<Integer,C> {

    public Integer decode(InputStream stm, C ctx) { return _decode(stm); }

    public Integer decode(ByteBuffer buf, C ctx) { return _decode(buf); }

    public Integer decode(byte[] dat, long pos, long len, C ctx) { return _decode(dat, pos, len); }

    public long encode(Integer obj, OutputStream stm, C ctx) { return _encode(obj, stm); }

    public long encode(Integer obj, ByteBuffer buf, C ctx) { return _encode(obj, buf); }

    public long encode(Integer obj, byte[] dat, long pos, long len, C ctx) { return _encode(obj, dat, pos, len); }

    public long length(Integer obj, C ctx) { return _length(obj); }

    public static int _decode(InputStream stm) {
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
            return v;
        }
        catch(IOException e) {
            throw new DecodingException(e);
        }
    }

    public static int _decode(ByteBuffer buf) {
        try {
            int v=buf.get()&0xFF;
            v=(v<<8)|(buf.get()&0xFF);
            v=(v<<8)|(buf.get()&0xFF);
            v=(v<<8)|(buf.get()&0xFF);
            return v;
        }
        catch(BufferUnderflowException e) {
            throw new DecodingException(e);
        }
    }

    public static int _decode(byte[] dat, long pos, long len) {
        try {
            int v=dat[(int) pos++]&0xFF;
            v=(v<<8)|(dat[(int) pos++]&0xFF);
            v=(v<<8)|(dat[(int) pos++]&0xFF);
            v=(v<<8)|(dat[(int) pos]&0xFF);
            return v;
        }
        catch(ArrayIndexOutOfBoundsException e) {
            throw new DecodingException(e);
        }
    }

    public static long _encode(int v, OutputStream stm) {
        try {
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

    public static long _encode(int v, ByteBuffer buf) {
        try {
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

    public static long _encode(int v, byte[] dat, long pos, long len) {
        dat[(int) pos++]=(byte) ((v>>>24)&0xFF);
        dat[(int) pos++]=(byte) ((v>>>16)&0xFF);
        dat[(int) pos++]=(byte) ((v>>>8)&0xFF);
        dat[(int) pos++]=(byte) ((v)&0xFF);
        return 4;
    }

    public static long _length(int v) {
        return 4;
    }


}
