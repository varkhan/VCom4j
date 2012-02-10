package net.varkhan.base.conversion.serializer.composite;

import net.varkhan.base.conversion.serializer.DecodingException;
import net.varkhan.base.conversion.serializer.EncodingException;
import net.varkhan.base.conversion.serializer.Serializer;
import net.varkhan.base.conversion.serializer.primitives.NullSerializer;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.BufferOverflowException;
import java.nio.BufferUnderflowException;
import java.nio.ByteBuffer;
import java.nio.ReadOnlyBufferException;


/**
 * <b>Type union serializer</b>.
 * <p/>
 * Serializes an union of types.
 * <p/>
 *
 * @author varkhan
 * @date 1/30/11
 * @time 4:59 AM
 */
@SuppressWarnings( { "UnusedDeclaration" })
public class UnionSerializer<U,C> implements Serializer<U,C> {
    protected static final int                 MAX_TYPES=255;
    private                int                      size=0;
    @SuppressWarnings( { "unchecked" })
    private final          Class<? extends U>[]    types=new Class[MAX_TYPES];
    @SuppressWarnings( { "unchecked" })
    private final          Serializer<U,C>[] serializers=new Serializer[MAX_TYPES];

    public UnionSerializer() { }

    @SuppressWarnings( { "unchecked", "RedundantCast" })
    public <T extends U> int setSerializer(Class<T> klass, Serializer<T,C> serializer) {
        if(size>=MAX_TYPES)
            throw new IllegalArgumentException("Can not add serializer for type "+klass.getName()+": type table is full");
        types[size]=klass;
        serializers[size]=(Serializer<U,C>) serializer;
        return ++size;
    }

    @SuppressWarnings( { "unchecked", "RedundantCast" })
    public <T extends U> Serializer<T,C> getSerializer(Class<? extends T> klass) {
        for(int i=0;i<size;i++) {
            if(types[i].isAssignableFrom(klass)) {
                return (Serializer<T,C>) serializers[i];
            }
        }
        return null;
    }

    @SuppressWarnings( { "unchecked", "RedundantCast" })
    public <T extends U> Serializer<T,C> getSerializer(int id) {
        if(id==0) return NullSerializer.instance();
        if(id>size) return null;
        return (Serializer<T,C>) serializers[id-1];
    }

    public U decode(InputStream stm, C ctx) {
        try {
            int r=stm.read();
            if(r<0) throw new DecodingException();
            if(r==0) return null;
            if(r>size) throw new DecodingException("Unknown type ID "+r);
            return serializers[r-1].decode(stm, ctx);
        }
        catch(IOException e) {
            throw new DecodingException(e);
        }
    }

    public U decode(ByteBuffer buf, C ctx) {
        try {
            byte r=buf.get();
            if(r==0) return null;
            if(r>size) throw new DecodingException("Unknown type ID "+r);
            return serializers[r-1].decode(buf, ctx);
        }
        catch(BufferUnderflowException e) {
            throw new DecodingException(e);
        }
        catch(ReadOnlyBufferException e) {
            throw new DecodingException(e);
        }
    }

    public U decode(byte[] dat, long pos, long len, C ctx) {
        try {
            byte r=dat[(int) pos];
            if(r==0) return null;
            if(r>size) throw new DecodingException("Unknown type ID "+r);
            return serializers[r-1].decode(dat, pos+1, len-1, ctx);
        }
        catch(ArrayIndexOutOfBoundsException e) {
            throw new DecodingException(e);
        }
    }

    public long encode(U obj, OutputStream stm, C ctx) {
        try {
            if(obj==null) {
                stm.write(0);
                return 1;
            }
            for(int i=0;i<size;i++) {
                if(types[i].isAssignableFrom(obj.getClass())) {
                    stm.write(i+1);
                    return 1+serializers[i].encode(obj, stm, ctx);
                }
            }
            throw new EncodingException("Unknown type "+obj.getClass().getName());
        }
        catch(IOException e) {
            throw new EncodingException(e);
        }
    }

    public long encode(U obj, ByteBuffer buf, C ctx) {
        try {
            if(obj==null) {
                buf.put((byte) 0);
                return 1;
            }
            for(int i=0;i<size;i++) {
                if(types[i].isAssignableFrom(obj.getClass())) {
                    buf.put((byte) (i+1));
                    return 1+serializers[i].encode(obj, buf, ctx);
                }
            }
            throw new EncodingException("Unknown type "+obj.getClass().getName());
        }
        catch(BufferOverflowException e) {
            throw new EncodingException(e);
        }
        catch(ReadOnlyBufferException e) {
            throw new EncodingException(e);
        }
    }

    public long encode(U obj, byte[] dat, long pos, long len, C ctx) {
        try {
            if(obj==null) {
                dat[(int) pos]=0;
                return 1;
            }
            for(int i=0;i<size;i++) {
                if(types[i].isAssignableFrom(obj.getClass())) {
                    dat[(int) pos]=(byte) (i+1);
                    return 1+serializers[i].encode(obj, dat, pos, len, ctx);
                }
            }
            throw new EncodingException("Unknown type "+obj.getClass().getName());
        }
        catch(ArrayIndexOutOfBoundsException e) {
            throw new EncodingException(e);
        }
    }

    public long length(U obj, C ctx) {
        if(obj==null) return 1;
        for(int i=0;i<size;i++) {
            if(types[i].isAssignableFrom(obj.getClass())) {
                return 1+serializers[i].length(obj, ctx);
            }
        }
        throw new EncodingException("Unknown type "+obj.getClass().getName());
    }

}
