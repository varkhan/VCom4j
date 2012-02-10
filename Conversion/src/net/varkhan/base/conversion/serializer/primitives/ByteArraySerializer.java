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
public class ByteArraySerializer<C> implements Serializer<byte[],C> {

    public byte[] decode(InputStream stm, C ctx) { return _decode(stm); }

    public byte[] decode(ByteBuffer buf, C ctx) { return _decode(buf); }

    public byte[] decode(byte[] dat, long pos, long len, C ctx) { return _decode(dat, pos, len); }

    public long encode(byte[] obj, OutputStream stm, C ctx) { return _encode(obj, stm); }

    public long encode(byte[] obj, ByteBuffer buf, C ctx) { return _encode(obj, buf); }

    public long encode(byte[] obj, byte[] dat, long pos, long len, C ctx) { return _encode(obj, dat, pos, len); }

    public long length(byte[] obj, C ctx) { return _length(obj); }

    public static byte[] _decode(InputStream stm) {
        try {
            long l=VariadicSerializer._decode(stm);
            byte[] v=new byte[(int) l];
            int i=0;
            while(i<l) {
                int r=stm.read();
                if(r<0) throw new DecodingException();
                v[i++]=(byte) r;
            }
            return v;
        }
        catch(IOException e) {
            throw new DecodingException(e);
        }
    }

    public static byte[] _decode(ByteBuffer buf) {
        try {
            long l=VariadicSerializer._decode(buf);
            byte[] v=new byte[(int) l];
            int i=0;
            while(i<l) {
                v[i++]=buf.get();
            }
            return v;
        }
        catch(BufferUnderflowException e) {
            throw new DecodingException(e);
        }
    }

    public static byte[] _decode(byte[] dat, long pos, long len) {
        try {
            long l=VariadicSerializer._decode(dat, pos, len);
            pos+=VariadicSerializer._length(l);
            byte[] v=new byte[(int) l];
            int i=0;
            while(i<l) {
                v[i++]=dat[(int) pos++];
            }
            return v;
        }
        catch(ArrayIndexOutOfBoundsException e) {
            throw new DecodingException(e);
        }
    }

    public static long _encode(byte[] v, OutputStream stm) {
        try {
            long l=v.length;
            long c=VariadicSerializer._encode(l, stm);
            int i=0;
            while(i<l) {
                stm.write(v[i++]);
            }
            return c+i;
        }
        catch(IOException e) {
            throw new EncodingException(e);
        }
    }

    public static long _encode(byte[] v, ByteBuffer buf) {
        try {
            long l=v.length;
            long c=VariadicSerializer._encode(l, buf);
            int i=0;
            while(i<l) {
                buf.put(v[i++]);
            }
            return c+i;
        }
        catch(BufferOverflowException e) {
            throw new EncodingException(e);
        }
        catch(ReadOnlyBufferException e) {
            throw new EncodingException(e);
        }
    }

    public static long _encode(byte[] v, byte[] dat, long pos, long len) {
        long l=v.length;
        long c=VariadicSerializer._encode(l, dat, pos, len);
        pos+=c;
        System.arraycopy(v, 0, dat, (int) pos, (int) l);
        return c+l;
    }

    public static long _length(byte[] v) {
        long l=v.length;
        return (l+VariadicSerializer._length(l));
    }


}
