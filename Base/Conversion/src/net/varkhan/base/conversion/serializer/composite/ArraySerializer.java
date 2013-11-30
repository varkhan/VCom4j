/**
 *
 */
package net.varkhan.base.conversion.serializer.composite;

import net.varkhan.base.conversion.serializer.Serializer;
import net.varkhan.base.conversion.serializer.primitives.VariadicSerializer;

import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Array;
import java.nio.ByteBuffer;


/**
 * <b>Generic array serializer</b>.
 * <p/>
 * Serializes an array of objects.
 * <p/>
 *
 * @author varkhan
 * @date Nov 7, 2010
 * @time 11:58:15 PM
 */
public class ArraySerializer<S,C> implements Serializer<S[],C> {

    private final Class<S>        type;
    private final Serializer<S,C> serializer;

    /**
     * @param serializer the underlying type serializer
     */
    @SuppressWarnings("unchecked")
    public ArraySerializer(Serializer<S,C> serializer) {
        this.type=(Class<S>) Object.class;
        this.serializer=serializer;
    }

    /**
     * @param type       the array element type
     * @param serializer the underlying type serializer
     */
    public ArraySerializer(Class<S> type, Serializer<S,C> serializer) {
        this.type=type;
        this.serializer=serializer;
    }

    public S[] decode(InputStream stm, C ctx) {
        long l=VariadicSerializer._decode(stm);
        @SuppressWarnings("unchecked")
        S[] v=(S[]) Array.newInstance(type, (int) l);
        int i=0;
        while(i<l) {
            v[i++]=serializer.decode(stm, ctx);
        }
        return v;
    }

    public S[] decode(ByteBuffer buf, C ctx) {
        long l=VariadicSerializer._decode(buf);
        @SuppressWarnings("unchecked")
        S[] v=(S[]) Array.newInstance(type, (int) l);
        int i=0;
        while(i<l) {
            v[i++]=serializer.decode(buf, ctx);
        }
        return v;
    }

    public S[] decode(byte[] dat, long pos, long len, C ctx) {
        long l=VariadicSerializer._decode(dat, pos, len);
        long c=VariadicSerializer._length(l);
        @SuppressWarnings("unchecked")
        S[] v=(S[]) Array.newInstance(type, (int) l);
        int i=0;
        while(i<l) {
            c+=serializer.length(v[i++]=serializer.decode(dat, pos+c, len-c, ctx), ctx);
        }
        return v;
    }

    public long encode(S[] obj, OutputStream stm, C ctx) {
        long c=VariadicSerializer._encode(obj.length, stm);
        for(S v : obj) c+=serializer.encode(v, stm, ctx);
        return c;
    }

    public long encode(S[] obj, ByteBuffer buf, C ctx) {
        long c=VariadicSerializer._encode(obj.length, buf);
        for(S v : obj) c+=serializer.encode(v, buf, ctx);
        return c;
    }

    public long encode(S[] obj, byte[] dat, long pos, long len, C ctx) {
        long c=VariadicSerializer._encode(obj.length, dat, pos, len);
        for(S v : obj) c+=serializer.encode(v, dat, pos+c, len-c, ctx);
        return c;
    }

    public long length(S[] obj, C ctx) {
        long c=VariadicSerializer._length(obj.length);
        for(S v : obj) c+=serializer.length(v, ctx);
        return c;
    }


}
