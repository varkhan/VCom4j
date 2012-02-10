/**
 *
 */
package net.varkhan.base.conversion.serializer.record;

import net.varkhan.base.containers.set.IndexedSet;
import net.varkhan.base.conversion.serializer.Serializer;
import net.varkhan.base.conversion.serializer.primitives.VariadicSerializer;

import java.io.InputStream;
import java.io.OutputStream;
import java.nio.ByteBuffer;


/**
 * <b>.</b>
 * <p/>
 *
 * @author varkhan
 * @date Nov 14, 2010
 * @time 10:59:55 PM
 */
public class ReferenceSerializer<S,C extends IndexedSet<S>> implements Serializer<S,C> {

    private final Serializer<S,C> serializer;

    /**
     * @param serializer the underlying type serializer
     */
    public ReferenceSerializer(Serializer<S,C> serializer) {
        this.serializer=serializer;
    }

    public S decode(InputStream stm, C ctx) {
        long idx=VariadicSerializer._decode(stm);
        if(idx==0) return null;
        if(idx==1) {
            S val=serializer.decode(stm, ctx);
            idx=ctx.add(val);
            return val;
        }
        return ctx.get(idx-2);
    }

    public S decode(ByteBuffer buf, C ctx) {
        long idx=VariadicSerializer._decode(buf);
        if(idx==0) return null;
        if(idx==1) {
            S val=serializer.decode(buf, ctx);
            idx=ctx.add(val);
            return val;
        }
        return ctx.get(idx-2);
    }

    public S decode(byte[] dat, long pos, long len, C ctx) {
        long idx=VariadicSerializer._decode(dat, pos, len);
        if(idx==0) return null;
        if(idx==1) {
            pos+=VariadicSerializer._length(idx);
            S val=serializer.decode(dat, pos, len, ctx);
            idx=ctx.add(val);
            return val;
        }
        return ctx.get(idx-2);
    }

    public long encode(S obj, OutputStream stm, C ctx) {
        if(obj==null) return VariadicSerializer._encode(0, stm);
        long idx=ctx.index(obj);
        if(idx<0) {
            idx=ctx.add(obj);
            long c=VariadicSerializer._encode(1, stm);
            return c+serializer.encode(obj, stm, ctx);
        }
        return VariadicSerializer._encode(idx+2, stm);
    }

    public long encode(S obj, ByteBuffer buf, C ctx) {
        if(obj==null) return VariadicSerializer._encode(0, buf);
        long idx=ctx.index(obj);
        if(idx<0) {
            idx=ctx.add(obj);
            long c=VariadicSerializer._encode(1, buf);
            return c+serializer.encode(obj, buf, ctx);
        }
        return VariadicSerializer._encode(idx+2, buf);
    }

    public long encode(S obj, byte[] dat, long pos, long len, C ctx) {
        if(obj==null) return VariadicSerializer._encode(0, dat, pos, len);
        long idx=ctx.index(obj);
        if(idx<0) {
            idx=ctx.add(obj);
            long c=VariadicSerializer._encode(1, dat, pos, len);
            return c+serializer.encode(obj, dat, pos+c, len-c, ctx);
        }
        return VariadicSerializer._encode(idx+2, dat, pos, len);
    }

    public long length(S obj, C ctx) {
        if(obj==null) return VariadicSerializer._length(0);
        long idx=ctx.index(obj);
        if(idx<0) {
            // Note that we add the data in the reference map here: we should have a different context from the one used for writes!!
            idx=ctx.add(obj);
            return VariadicSerializer._length(1)+serializer.length(obj, ctx);
        }
        return VariadicSerializer._length(idx+2);
    }
}
