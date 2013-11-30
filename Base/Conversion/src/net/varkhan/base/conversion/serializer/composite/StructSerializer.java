package net.varkhan.base.conversion.serializer.composite;

import net.varkhan.base.conversion.serializer.DecodingException;
import net.varkhan.base.conversion.serializer.EncodingException;
import net.varkhan.base.conversion.serializer.Serializer;
import net.varkhan.base.conversion.serializer.primitives.StringSerializer;
import net.varkhan.base.conversion.serializer.primitives.VariadicSerializer;

import java.io.InputStream;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.Map;


/**
 * <b>Generic data structure serializer</b>.
 * <p/>
 * Serializes a structure containing typed, named fields.
 * <p/>
 *
 * @author varkhan
 * @date 5/28/11
 * @time 3:52 AM
 */
@SuppressWarnings( { "UnusedDeclaration" })
public class StructSerializer<C> implements Serializer<Map<String,Object>,C> {

    private final Map<String,Serializer<Object,C>> serializers=new HashMap<String,Serializer<Object,C>>();

    public StructSerializer() { }

    @SuppressWarnings( { "unchecked" })
    public void setSerializer(String field, Serializer<?,C> serializer) {
        serializers.put(field, (Serializer<Object, C>) serializer);
    }

    public void remSerializer(String field) {
        serializers.remove(field);
    }

    public Serializer<?,C> getSerializer(String field) {
        return serializers.get(field);
    }

    public Map<String,Object> decode(InputStream stm, C ctx) {
        long size=VariadicSerializer._decode(stm);
        Map<String,Object> rec=new HashMap<String,Object>();
        for(long i=0;i<size;i++) {
            String key=StringSerializer._decode(stm);
            Serializer<Object,C> s = serializers.get(key);
            if(s==null) throw new DecodingException("Decoder not found for field \""+key+"\"");
            Object val=s.decode(stm, ctx);
            rec.put(key, val);
        }
        return rec;
    }

    public Map<String,Object> decode(ByteBuffer buf, C ctx) {
        long size=VariadicSerializer._decode(buf);
        Map<String,Object> rec=new HashMap<String,Object>();
        for(long i=0;i<size;i++) {
            String key=StringSerializer._decode(buf);
            Serializer<Object,C> s = serializers.get(key);
            if(s==null) throw new DecodingException("Decoder not found for field \""+key+"\"");
            Object val=s.decode(buf, ctx);
            rec.put(key, val);
        }
        return rec;
    }

    public Map<String,Object> decode(byte[] dat, long pos, long len, C ctx) {
        long size=VariadicSerializer._decode(dat, pos, len);
        pos+=VariadicSerializer._length(size);
        Map<String,Object> rec=new HashMap<String,Object>();
        for(long i=0;i<size;i++) {
            String key=StringSerializer._decode(dat, pos, len);
            pos+=StringSerializer._length(key);
            Serializer<Object,C> s = serializers.get(key);
            if(s==null) throw new DecodingException("Decoder not found for field \""+key+"\"");
            Object val=s.decode(dat, pos, len, ctx);
            pos+=s.length(val, ctx);
            rec.put(key, val);
        }
        return rec;
    }

    public long encode(Map<String,Object> obj, OutputStream stm, C ctx) {
        long c=VariadicSerializer._encode(obj.size(), stm);
        for(Map.Entry<String,Object> ent : obj.entrySet()) {
            String key=ent.getKey();
            Serializer<Object,C> s = serializers.get(key);
            if(s==null) throw new EncodingException("Encoder not found for field \""+key+"\"");
            c+=StringSerializer._encode(key, stm);
            c+=s.encode(ent.getValue(), stm, ctx);
        }
        return c;
    }

    public long encode(Map<String,Object> obj, ByteBuffer buf, C ctx) {
        long c=VariadicSerializer._encode(obj.size(), buf);
        for(Map.Entry<String,Object> ent : obj.entrySet()) {
            String key=ent.getKey();
            Serializer<Object,C> s = serializers.get(key);
            if(s==null) throw new EncodingException("Encoder not found for field \""+key+"\"");
            c+=StringSerializer._encode(key, buf);
            c+=s.encode(ent.getValue(), buf, ctx);
        }
        return c;
    }

    public long encode(Map<String,Object> obj, byte[] dat, long pos, long len, C ctx) {
        long c=VariadicSerializer._encode(obj.size(), dat, pos, len);
        for(Map.Entry<String,Object> ent : obj.entrySet()) {
            String key=ent.getKey();
            Serializer<Object,C> s = serializers.get(key);
            if(s==null) throw new EncodingException("Encoder not found for field \""+key+"\"");
            c+=StringSerializer._encode(key, dat, pos+c, len-c);
            c+=s.encode(ent.getValue(), dat, pos+c, len-c, ctx);
        }
        return c;
    }

    public long length(Map<String,Object> obj, C ctx) {
        long c=VariadicSerializer._length(obj.size());
        for(Map.Entry<String,Object> ent : obj.entrySet()) {
            String key=ent.getKey();
            Serializer<Object,C> s = serializers.get(key);
            if(s==null) throw new EncodingException("Encoder not found for field \""+key+"\"");
            c+=StringSerializer._length(key);
            c+=s.length(ent.getValue(), ctx);
        }
        return c;
    }

}
