package net.varkhan.base.conversion.serializer.record;

import net.varkhan.base.conversion.serializer.Serializer;
import net.varkhan.base.conversion.serializer.composite.ArraySerializer;
import net.varkhan.base.conversion.serializer.composite.UnionSerializer;
import net.varkhan.base.conversion.serializer.primitives.*;

import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.*;
import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.Map;


/**
 * <b></b>.
 * <p/>
 *
 * @author varkhan
 * @date 5/28/11
 * @time 3:37 AM
 */
public class ReflectSerializer<T,C> implements Serializer<T,C> {

    private final Class<T> kls;
    private final UnionSerializer<Object,C> vs;
    private final RecordSerializer<C> rs;

    public ReflectSerializer(Class<T> kls) {
        this.kls = kls;
        vs=new UnionSerializer<Object,C>();
        vs.setSerializer(Void.class, new NullSerializer<Void,C>());
        vs.setSerializer(Boolean.class, new BooleanSerializer<C>());
        vs.setSerializer(Byte.class, new ByteSerializer<C>());
        vs.setSerializer(Short.class, new ShortSerializer<C>());
        vs.setSerializer(Integer.class, new IntSerializer<C>());
        vs.setSerializer(Long.class, new LongSerializer<C>());
        vs.setSerializer(Float.class, new FloatSerializer<C>());
        vs.setSerializer(Double.class, new DoubleSerializer<C>());
        vs.setSerializer(CharSequence.class, new StringSerializer<C>());
        vs.setSerializer(boolean[].class, new BooleanArraySerializer<C>());
        vs.setSerializer(byte[].class, new ByteArraySerializer<C>());
        vs.setSerializer(short[].class, new ShortArraySerializer<C>());
        vs.setSerializer(int[].class, new IntArraySerializer<C>());
        vs.setSerializer(long[].class, new LongArraySerializer<C>());
        vs.setSerializer(float[].class, new FloatArraySerializer<C>());
        vs.setSerializer(double[].class, new DoubleArraySerializer<C>());
        vs.setSerializer(Object[].class, new ArraySerializer<Object,C>(vs));
        vs.setSerializer(Record.class, rs=new RecordSerializer<C>(vs));
    }

    public T decode(InputStream stm, C ctx) {
        return asObject(rs.decode(stm, ctx),kls);
    }

    public T decode(ByteBuffer buf, C ctx) {
        return asObject(rs.decode(buf, ctx),kls);
    }

    public T decode(byte[] dat, long pos, long len, C ctx) {
        return asObject(rs.decode(dat, pos, len, ctx),kls);
    }

    public long encode(T obj, OutputStream stm, C ctx) {
        return vs.encode(asRecord(obj), stm, ctx);
    }

    public long encode(T obj, ByteBuffer buf, C ctx) {
        return rs.encode(asRecord(obj), buf, ctx);
    }

    public long encode(T obj, byte[] dat, long pos, long len, C ctx) {
        return rs.encode(asRecord(obj), dat, pos, len, ctx);
    }

    public long length(T obj, C ctx) {
        return rs.length(asRecord(obj), ctx);
    }


    private final class RecordSerializer<C> implements Serializer<Record,C> {

        private final Serializer<Object,C> ds;

        public RecordSerializer(Serializer<Object,C> ds) {
            this.ds=ds;
        }

        public Record decode(InputStream stm, C ctx) {
            long size=VariadicSerializer._decode(stm);
            Record rec=new ObjectRecord();
            for(long i=0;i<size;i++) {
                String key=StringSerializer._decode(stm);
                Object val=ds.decode(stm, ctx);
                rec.put(key, val);
            }
            return rec;
        }

        public Record decode(ByteBuffer buf, C ctx) {
            long size=VariadicSerializer._decode(buf);
            Record rec=new ObjectRecord();
            for(long i=0;i<size;i++) {
                String key=StringSerializer._decode(buf);
                Object val=ds.decode(buf, ctx);
                rec.put(key, val);
            }
            return rec;
        }

        public Record decode(byte[] dat, long pos, long len, C ctx) {
            long size=VariadicSerializer._decode(dat, pos, len);
            pos+=VariadicSerializer._length(size);
            Record rec=new ObjectRecord();
            for(long i=0;i<size;i++) {
                String key=StringSerializer._decode(dat, pos, len);
                pos+=StringSerializer._length(key);
                Object val=ds.decode(dat, pos, len, ctx);
                pos+=ds.length(val, ctx);
                rec.put(key, val);
            }
            return rec;
        }

        public long encode(Record obj, OutputStream stm, C ctx) {
            long len=VariadicSerializer._encode(obj.size(), stm);
            for(Map.Entry<String,Object> ent : obj.entrySet()) {
                len+=StringSerializer._encode(ent.getKey(), stm);
                len+=ds.encode(ent.getValue(), stm, ctx);
            }
            return len;
        }

        public long encode(Record obj, ByteBuffer buf, C ctx) {
            long len=VariadicSerializer._encode(obj.size(), buf);
            for(Map.Entry<String,Object> ent : obj.entrySet()) {
                len+=StringSerializer._encode(ent.getKey(), buf);
                len+=ds.encode(ent.getValue(), buf, ctx);
            }
            return len;
        }

        public long encode(Record obj, byte[] dat, long pos, long l, C ctx) {
            long len=VariadicSerializer._encode(obj.size(), dat, pos, l);
            for(Map.Entry<String,Object> ent : obj.entrySet()) {
                len+=StringSerializer._encode(ent.getKey(), dat, pos, l);
                len+=ds.encode(ent.getValue(), dat, pos, l, ctx);
            }
            return len;
        }

        public long length(Record obj, C ctx) {
            long len=VariadicSerializer._length(obj.size());
            for(Map.Entry<String,Object> ent : obj.entrySet()) {
                len+=StringSerializer._length(ent.getKey());
                len+=ds.length(ent.getValue(), ctx);
            }
            return len;
        }

    }

    @SuppressWarnings( { "unchecked" })
    public static <T> T asObject(final Record rec, Class<T>... kls) {
        return (T) Proxy.newProxyInstance(null, kls, new InvocationHandler() {
            public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                String name = method.getName();
                if(name.startsWith("get") && args.length==0) {
                    return rec.get(Character.toLowerCase(name.charAt(3))+name.substring(4));
                }
                if(name.startsWith("set") && args.length==1) {
                    return rec.put(Character.toLowerCase(name.charAt(3))+name.substring(4),args[0]);
                }
                return new NoSuchMethodError(method.toGenericString());
            }
        });
    }


    public static <T> Record asRecord(final T obj) {
        final Record rec = new ObjectRecord();
        Class c=obj.getClass();
        while(c!=null&&c!=Object.class) {
            for(Field f : c.getDeclaredFields()) {
                if((f.getModifiers()&(Modifier.STATIC|Modifier.TRANSIENT))==0) {
                    // Ignore obscured fields
                    if(!rec.containsKey(f.getName())) try {
                        rec.put(f.getName(), f.get(obj));
                    }
                    catch(IllegalAccessException e) { /* ignore */ }
                }
            }
            c=c.getSuperclass();
        }
        return rec;
    }

    public static interface Record extends Map<String,Object> {

    }

    private static final class ObjectRecord extends HashMap<String,Object> implements Record {

    }
}
