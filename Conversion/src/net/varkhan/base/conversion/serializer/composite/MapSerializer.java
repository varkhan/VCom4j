package net.varkhan.base.conversion.serializer.composite;

import net.varkhan.base.conversion.serializer.Serializer;
import net.varkhan.base.conversion.serializer.primitives.VariadicSerializer;

import java.io.InputStream;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.Map;


/**
 * <b>Generic map serializer</b>.
 * <p/>
 * Serializes a key-value map.
 * <p/>
 *
 * @author varkhan
 * @date 5/28/11
 * @time 3:52 AM
 */
public class MapSerializer<K,V,C> implements Serializer<Map<K,V>,C> {

    private final Serializer<K,C> kser;
    private final Serializer<V,C> vser;

    public MapSerializer(Serializer<K,C> kser, Serializer<V,C> vser) {
        this.kser=kser;
        this.vser=vser;
    }

    public Map<K,V> decode(InputStream stm, C ctx) {
        long size=VariadicSerializer._decode(stm);
        Map<K,V> map=new HashMap<K,V>((int)size);
        for(long i=0;i<size;i++) {
            K key=kser.decode(stm, ctx);
            V val=vser.decode(stm, ctx);
            map.put(key, val);
        }
        return map;
    }

    public Map<K,V> decode(ByteBuffer buf, C ctx) {
        long size=VariadicSerializer._decode(buf);
        Map<K,V> map=new HashMap<K,V>((int)size);
        for(long i=0;i<size;i++) {
            K key=kser.decode(buf, ctx);
            V val=vser.decode(buf, ctx);
            map.put(key, val);
        }
        return map;
    }

    public Map<K,V> decode(byte[] dat, long pos, long len, C ctx) {
        long size=VariadicSerializer._decode(dat, pos, len);
        pos+=VariadicSerializer._length(size);
        Map<K,V> map=new HashMap<K,V>((int)size);
        for(long i=0;i<size;i++) {
            K key=kser.decode(dat, pos, len, ctx);
            pos+=kser.length(key, ctx);
            V val=vser.decode(dat, pos, len, ctx);
            pos+=vser.length(val, ctx);
            map.put(key, val);
        }
        return map;
    }

    public long encode(Map<K,V> obj, OutputStream stm, C ctx) {
        long c=VariadicSerializer._encode(obj.size(), stm);
        for(Map.Entry<K,V> ent : obj.entrySet()) {
            c+=kser.encode(ent.getKey(), stm, ctx);
            c+=vser.encode(ent.getValue(), stm, ctx);
        }
        return c;
    }

    public long encode(Map<K,V> obj, ByteBuffer buf, C ctx) {
        long c=VariadicSerializer._encode(obj.size(), buf);
        for(Map.Entry<K,V> ent : obj.entrySet()) {
            c+=kser.encode(ent.getKey(), buf, ctx);
            c+=vser.encode(ent.getValue(), buf, ctx);
        }
        return c;
    }

    public long encode(Map<K,V> obj, byte[] dat, long pos, long len, C ctx) {
        long c=VariadicSerializer._encode(obj.size(), dat, pos, len);
        for(Map.Entry<K,V> ent : obj.entrySet()) {
            c+=kser.encode(ent.getKey(), dat, pos+c, len-c, ctx);
            c+=vser.encode(ent.getValue(), dat, pos+c, len-c, ctx);
        }
        return c;
    }

    public long length(Map<K,V> obj, C ctx) {
        long c=VariadicSerializer._length(obj.size());
        for(Map.Entry<K,V> ent : obj.entrySet()) {
            c+=kser.length(ent.getKey(), ctx);
            c+=vser.length(ent.getValue(), ctx);
        }
        return c;
    }

}
