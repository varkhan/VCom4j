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
public class BooleanSerializer<C> implements Serializer<Boolean,C> {

    public Boolean decode(InputStream stm, C ctx) { return _decode(stm); }

    public Boolean decode(ByteBuffer buf, C ctx) { return _decode(buf); }

    public Boolean decode(byte[] dat, long pos, long len, C ctx) { return _decode(dat, pos, len); }

    public long encode(Boolean obj, OutputStream stm, C ctx) { return _encode(obj, stm); }

    public long encode(Boolean obj, ByteBuffer buf, C ctx) { return _encode(obj, buf); }

    public long encode(Boolean obj, byte[] dat, long pos, long len, C ctx) { return _encode(obj, dat, pos, len); }

    public long length(Boolean obj, C ctx) { return _length(obj); }

    public static boolean _decode(InputStream stm) {
        try {
            int r=stm.read();
            if(r<0) throw new DecodingException();
            return r!=0;
        }
        catch(IOException e) {
            throw new DecodingException(e);
        }
    }

    public static boolean _decode(ByteBuffer buf) {
        try {
            return buf.get()!=0;
        }
        catch(BufferUnderflowException e) {
            throw new DecodingException(e);
        }
    }

    public static boolean _decode(byte[] dat, long pos, long len) {
        try {
            return dat[(int) pos]!=0;
        }
        catch(ArrayIndexOutOfBoundsException e) {
            throw new DecodingException(e);
        }
    }

    public static long _encode(boolean v, OutputStream stm) {
        try {
            stm.write(v ? 1 : 0);
            return 1;
        }
        catch(IOException e) {
            throw new EncodingException(e);
        }
    }

    public static long _encode(boolean v, ByteBuffer buf) {
        try {
            buf.put((byte) (v ? 1 : 0));
            return 1;
        }
        catch(BufferOverflowException e) {
            throw new EncodingException(e);
        }
        catch(ReadOnlyBufferException e) {
            throw new EncodingException(e);
        }
    }

    public static long _encode(boolean v, byte[] dat, long pos, long len) {
        dat[(int) pos]=(byte) (v ? 1 : 0);
        return 1;
    }

    public static long _length(boolean v) {
        return 1;
    }


}
