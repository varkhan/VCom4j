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
public class ByteSerializer<C> implements Serializer<Byte,C> {

    public Byte decode(InputStream stm, C ctx) { return _decode(stm); }

    public Byte decode(ByteBuffer buf, C ctx) { return _decode(buf); }

    public Byte decode(byte[] dat, long pos, long len, C ctx) { return _decode(dat, pos, len); }

    public long encode(Byte obj, OutputStream stm, C ctx) { return _encode(obj, stm); }

    public long encode(Byte obj, ByteBuffer buf, C ctx) { return _encode(obj, buf); }

    public long encode(Byte obj, byte[] dat, long pos, long len, C ctx) { return _encode(obj, dat, pos, len); }

    public long length(Byte obj, C ctx) { return _length(obj); }

    public static byte _decode(InputStream stm) {
        try {
            int r=stm.read();
            if(r<0) throw new DecodingException();
            return (byte) r;
        }
        catch(IOException e) {
            throw new DecodingException(e);
        }
    }

    public static byte _decode(ByteBuffer buf) {
        try {
            return (byte) (buf.get()&0xFF);
        }
        catch(BufferUnderflowException e) {
            throw new DecodingException(e);
        }
    }

    public static byte _decode(byte[] dat, long pos, long len) {
        try {
            return (byte) (dat[(int) pos++]&0xFF);
        }
        catch(ArrayIndexOutOfBoundsException e) {
            throw new DecodingException(e);
        }
    }

    public static long _encode(byte v, OutputStream stm) {
        try {
            stm.write((byte) (v&0xFF));
            return 1;
        }
        catch(IOException e) {
            throw new EncodingException(e);
        }
    }

    public static long _encode(short v, ByteBuffer buf) {
        try {
            buf.put((byte) (v&0xFF));
            return 1;
        }
        catch(BufferOverflowException e) {
            throw new EncodingException(e);
        }
        catch(ReadOnlyBufferException e) {
            throw new EncodingException(e);
        }
    }

    public static long _encode(byte v, byte[] dat, long pos, long lenn) {
        dat[(int) pos]=(byte) (v&0xFF);
        return 1;
    }

    public static long _length(byte v) {
        return 1;
    }


}
