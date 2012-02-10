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
public class BooleanArraySerializer<C> implements Serializer<boolean[],C> {

    public boolean[] decode(InputStream stm, C ctx) { return _decode(stm); }

    public boolean[] decode(ByteBuffer buf, C ctx) { return _decode(buf); }

    public boolean[] decode(byte[] dat, long pos, long len, C ctx) { return _decode(dat, pos, len); }

    public long encode(boolean[] obj, OutputStream stm, C ctx) { return _encode(obj, stm); }

    public long encode(boolean[] obj, ByteBuffer buf, C ctx) { return _encode(obj, buf); }

    public long encode(boolean[] obj, byte[] dat, long pos, long len, C ctx) { return _encode(obj, dat, pos, len); }

    public long length(boolean[] obj, C ctx) { return _length(obj); }

    public static boolean[] _decode(InputStream stm) {
        try {
            long l=VariadicSerializer._decode(stm);
            boolean[] v=new boolean[(int) l];
            int i=0;
            while(i<l) {
                int r=stm.read();
                if(r<0) throw new DecodingException();
                v[i++]=(r&0x80)!=0;
                if(i>=l) break;
                v[i++]=(r&0x40)!=0;
                if(i>=l) break;
                v[i++]=(r&0x20)!=0;
                if(i>=l) break;
                v[i++]=(r&0x10)!=0;
                if(i>=l) break;
                v[i++]=(r&0x08)!=0;
                if(i>=l) break;
                v[i++]=(r&0x04)!=0;
                if(i>=l) break;
                v[i++]=(r&0x02)!=0;
                if(i>=l) break;
                v[i++]=(r&0x01)!=0;
                if(i>=l) break;
            }
            return v;
        }
        catch(IOException e) {
            throw new DecodingException(e);
        }
    }

    public static boolean[] _decode(ByteBuffer buf) {
        try {
            long l=VariadicSerializer._decode(buf);
            boolean[] v=new boolean[(int) l];
            int i=0;
            while(i<l) {
                byte b=buf.get();
                v[i++]=(b&0x80)!=0;
                if(i>=l) break;
                v[i++]=(b&0x40)!=0;
                if(i>=l) break;
                v[i++]=(b&0x20)!=0;
                if(i>=l) break;
                v[i++]=(b&0x10)!=0;
                if(i>=l) break;
                v[i++]=(b&0x08)!=0;
                if(i>=l) break;
                v[i++]=(b&0x04)!=0;
                if(i>=l) break;
                v[i++]=(b&0x02)!=0;
                if(i>=l) break;
                v[i++]=(b&0x01)!=0;
                if(i>=l) break;
            }
            return v;
        }
        catch(BufferUnderflowException e) {
            throw new DecodingException(e);
        }
    }

    public static boolean[] _decode(byte[] dat, long pos, long len) {
        try {
            long l=VariadicSerializer._decode(dat, pos, len);
            pos+=VariadicSerializer._length(l);
            boolean[] v=new boolean[(int) l];
            int i=0;
            while(i<l) {
                byte b=dat[(int) pos++];
                v[i++]=(b&0x80)!=0;
                if(i>=l) break;
                v[i++]=(b&0x40)!=0;
                if(i>=l) break;
                v[i++]=(b&0x20)!=0;
                if(i>=l) break;
                v[i++]=(b&0x10)!=0;
                if(i>=l) break;
                v[i++]=(b&0x08)!=0;
                if(i>=l) break;
                v[i++]=(b&0x04)!=0;
                if(i>=l) break;
                v[i++]=(b&0x02)!=0;
                if(i>=l) break;
                v[i++]=(b&0x01)!=0;
                if(i>=l) break;
            }
            return v;
        }
        catch(ArrayIndexOutOfBoundsException e) {
            throw new DecodingException(e);
        }
    }

    public static long _encode(boolean[] v, OutputStream stm) {
        try {
            long l=v.length;
            long c=VariadicSerializer._encode(l, stm);
            int i=0, p=0;
            while(i<l) {
                byte b=0;
                if(i<l&&v[i++]) b|=0x80;
                if(i<l&&v[i++]) b|=0x40;
                if(i<l&&v[i++]) b|=0x20;
                if(i<l&&v[i++]) b|=0x10;
                if(i<l&&v[i++]) b|=0x08;
                if(i<l&&v[i++]) b|=0x04;
                if(i<l&&v[i++]) b|=0x02;
                if(i<l&&v[i++]) b|=0x01;
                stm.write(b);
                p++;
            }
            return c+p;
        }
        catch(IOException e) {
            throw new EncodingException(e);
        }
    }

    public static long _encode(boolean[] v, ByteBuffer buf) {
        try {
            long l=v.length;
            long c=VariadicSerializer._encode(l, buf);
            int i=0, p=0;
            while(i<l) {
                byte b=0;
                if(i<l&&v[i++]) b|=0x80;
                if(i<l&&v[i++]) b|=0x40;
                if(i<l&&v[i++]) b|=0x20;
                if(i<l&&v[i++]) b|=0x10;
                if(i<l&&v[i++]) b|=0x08;
                if(i<l&&v[i++]) b|=0x04;
                if(i<l&&v[i++]) b|=0x02;
                if(i<l&&v[i++]) b|=0x01;
                buf.put(b);
                p++;
            }
            return c+p;
        }
        catch(BufferOverflowException e) {
            throw new EncodingException(e);
        }
        catch(ReadOnlyBufferException e) {
            throw new EncodingException(e);
        }
    }

    public static long _encode(boolean[] v, byte[] dat, long pos, long len) {
        long l=v.length;
        long c=VariadicSerializer._encode(l, dat, pos, len);
        pos+=c;
        int i=0;
        while(i<l) {
            byte b=0;
            if(i<l&&v[i++]) b|=0x80;
            if(i<l&&v[i++]) b|=0x40;
            if(i<l&&v[i++]) b|=0x20;
            if(i<l&&v[i++]) b|=0x10;
            if(i<l&&v[i++]) b|=0x08;
            if(i<l&&v[i++]) b|=0x04;
            if(i<l&&v[i++]) b|=0x02;
            if(i<l&&v[i++]) b|=0x01;
            dat[(int) pos++]=b;
        }
        return ((l+7)>>3)+c;
    }

    public static long _length(boolean[] v) {
        long l=v.length;
        return (((l+7)>>3)+VariadicSerializer._length(l));
    }


}
