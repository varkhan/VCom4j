package net.varkhan.base.conversion.serializer.record;

import net.varkhan.base.containers.Container;
import net.varkhan.base.containers.map.ArrayOpenHashMap;
import net.varkhan.base.containers.map.EmptyMap;
import net.varkhan.base.containers.map.Map;
import net.varkhan.base.conversion.serializer.Serializer;
import net.varkhan.base.conversion.serializer.composite.ArraySerializer;
import net.varkhan.base.conversion.serializer.composite.UnionSerializer;
import net.varkhan.base.conversion.serializer.primitives.*;

import java.io.InputStream;
import java.io.OutputStream;
import java.nio.ByteBuffer;


/**
 * <b></b>.
 * <p/>
 *
 * @author varkhan
 * @date 1/30/11
 * @time 4:40 AM
 */
public class RecordSerializer<C> implements Serializer<RecordSerializer.Record,C> {

    private final UnionSerializer<Object,C> vs;

    public RecordSerializer() {
        vs=new UnionSerializer<Object,C>();
        vs.setSerializer(Void.class, new NullSerializer<Void,C>());
        vs.setSerializer(Boolean.class, new BooleanSerializer<C>());
        vs.setSerializer(Byte.class, new ByteSerializer<C>());
        vs.setSerializer(Short.class, new ShortSerializer<C>());
        vs.setSerializer(Integer.class, new IntSerializer<C>());
        vs.setSerializer(Long.class, new LongSerializer<C>());
        vs.setSerializer(Float.class, new FloatSerializer<C>());
        vs.setSerializer(Double.class, new DoubleSerializer<C>());
        vs.setSerializer(boolean[].class, new BooleanArraySerializer<C>());
        vs.setSerializer(byte[].class, new ByteArraySerializer<C>());
        vs.setSerializer(short[].class, new ShortArraySerializer<C>());
        vs.setSerializer(int[].class, new IntArraySerializer<C>());
        vs.setSerializer(long[].class, new LongArraySerializer<C>());
        vs.setSerializer(float[].class, new FloatArraySerializer<C>());
        vs.setSerializer(double[].class, new DoubleArraySerializer<C>());
        vs.setSerializer(Object[].class, new ArraySerializer<Object,C>(vs));
        vs.setSerializer(Record.class, this);
    }

    public Record decode(InputStream stm, C ctx) {
        long size=VariadicSerializer._decode(stm);
        RecordMap rec=new RecordMap(size);
        for(long i=0;i<size;i++) {
            String key=StringSerializer._decode(stm);
            Object val=vs.decode(stm, ctx);
            if(val instanceof Record) rec.add(key, (Record) val);
            else rec.add(key, new RecordValue(val));
        }
        return rec;
    }

    public Record decode(ByteBuffer buf, C ctx) {
        long size=VariadicSerializer._decode(buf);
        RecordMap rec=new RecordMap(size);
        for(long i=0;i<size;i++) {
            String key=StringSerializer._decode(buf);
            Object val=vs.decode(buf, ctx);
            if(val instanceof Record) rec.add(key, (Record) val);
            else rec.add(key, new RecordValue(val));
        }
        return rec;
    }

    public Record decode(byte[] dat, long pos, long len, C ctx) {
        long size=VariadicSerializer._decode(dat, pos, len);
        pos+=VariadicSerializer._length(size);
        RecordMap rec=new RecordMap(size);
        for(long i=0;i<size;i++) {
            String key=StringSerializer._decode(dat, pos, len);
            pos+=StringSerializer._length(key);
            Object val=vs.decode(dat, pos, len, ctx);
            pos+=vs.length(val, ctx);
            if(val instanceof Record) rec.add(key, (Record) val);
            else rec.add(key, new RecordValue(val));
        }
        return rec;
    }

    public long encode(Record obj, OutputStream stm, C ctx) {
        long len=VariadicSerializer._encode(obj.size(), stm);
        for(Map.Entry<String,Record> ent : (Iterable<Map.Entry<String,Record>>) obj) {
            len+=StringSerializer._encode(ent.getKey(), stm);
            Record val=ent.getValue();
            if(val.isEmpty()) len+=vs.encode(val.value(), stm, ctx);
            else len+=vs.encode(val, stm, ctx);
        }
        return len;
    }

    public long encode(Record obj, ByteBuffer buf, C ctx) {
        long len=VariadicSerializer._encode(obj.size(), buf);
        for(Map.Entry<String,Record> ent : (Iterable<Map.Entry<String,Record>>) obj) {
            len+=StringSerializer._encode(ent.getKey(), buf);
            Record val=ent.getValue();
            if(val.isEmpty()) len+=vs.encode(val.value(), buf, ctx);
            else len+=vs.encode(val, buf, ctx);
        }
        return len;
    }

    public long encode(Record obj, byte[] dat, long pos, long l, C ctx) {
        long len=VariadicSerializer._encode(obj.size(), dat, pos, l);
        for(Map.Entry<String,Record> ent : (Iterable<Map.Entry<String,Record>>) obj) {
            len+=StringSerializer._encode(ent.getKey(), dat, pos, l);
            Record val=ent.getValue();
            if(val.isEmpty()) len+=vs.encode(val.value(), dat, pos, l, ctx);
            else len+=vs.encode(val, dat, pos, l, ctx);
        }
        return len;
    }

    public long length(Record obj, C ctx) {
        long len=VariadicSerializer._length(obj.size());
        for(Map.Entry<String,Record> ent : (Iterable<Map.Entry<String,Record>>) obj) {
            len+=StringSerializer._length(ent.getKey());
            Record val=ent.getValue();
            if(val.isEmpty()) len+=vs.length(val.value(), ctx);
            else len+=vs.length(val, ctx);
        }
        return len;
    }

    /**
     * <b></b>.
     * <p/>
     *
     * @author varkhan
     * @date 2/10/11
     * @time 4:03 AM
     */
    public static interface Record/* extends Map<String,Record>*/ {
        public boolean isPrimitive();

        public Object value();

        public <T> T cast(Class<T> klass);

        public boolean boolValue();

        public byte byteValue();

        public short shortValue();

        public char charValue();

        public int intValue();

        public long longValue();

        public float floatValue();

        public double doubleValue();

        public String stringValue();

        /**
         * Returns the number of elements in this container.
         *
         * @return the number of elements stored in this list
         */
        public long size();

        /**
         * Indicates whether this container is empty.
         *
         * @return {@literal true} if this container contains no element,
         *         {@literal false} otherwise
         */
        public boolean isEmpty();

        /**
         * Indicate whether a given key is present in this container.
         *
         * @param key the key part of the entry
         *
         * @return {@literal true} if this key is in the container,
         *         or {@literal false} if this key is absent
         */
        public boolean has(String key);

        /**
         * Retrieves a value for a given key in this map.
         *
         * @param key the key
         *
         * @return the value matching the key in the map,
         *         or {@literal null} if the key is not present in the map
         */
        public Record get(String key);

        /**
         * Iterates over all elements in the container.
         *
         * @return an iterable over all the elements stored in the container
         */
    //    public Iterator<? extends Map.Entry<String,Record>> iterator();

        /**
         * A container of all the keys in this map
         *
         * @return a container, backed by the map, providing a view of the keys in the map
         */
        public Container<String> keys();

        /**
         * A container of all the values in this map
         *
         * @return a container, backed by the map, providing a view of the values in the map
         */
        public Container<Record> values();

    }


    public static final class RecordMap extends ArrayOpenHashMap<String,Record> implements Record {
        public RecordMap(long size) { super(size); }

        public boolean isPrimitive() { return false; }

        public Object value() { return null; }

        public <T> T cast(Class<T> klass) { throw new ClassCastException("Cannot convert Void to "+klass.getName()); }

        public boolean boolValue() { throw new ClassCastException("Cannot convert Void to boolean"); }

        public byte byteValue() { throw new ClassCastException("Cannot convert Void to byte"); }

        public short shortValue() { throw new ClassCastException("Cannot convert Void to short"); }

        public char charValue() { throw new ClassCastException("Cannot convert Void to char"); }

        public int intValue() { throw new ClassCastException("Cannot convert Void to int"); }

        public long longValue() { throw new ClassCastException("Cannot convert Void to long"); }

        public float floatValue() { throw new ClassCastException("Cannot convert Void to float"); }

        public double doubleValue() { throw new ClassCastException("Cannot convert Void to double"); }

        public String stringValue() { throw new ClassCastException("Cannot convert Void to String"); }
    }


    @SuppressWarnings( { "UnnecessaryUnboxing" })
    public static final class RecordValue extends EmptyMap<String,Record> implements Record {
        private final Object val;

        public RecordValue(Object val) { this.val=val; }

        public boolean isPrimitive() { return true; }

        public Object value() { return val; }

        public <T> T cast(Class<T> klass) { return klass.cast(val); }

        public boolean boolValue() { return Boolean.class.cast(val).booleanValue(); }

        public byte byteValue() { return Byte.class.cast(val).byteValue(); }

        public short shortValue() { return Short.class.cast(val).shortValue(); }

        public char charValue() { return Character.class.cast(val).charValue(); }

        public int intValue() { return Integer.class.cast(val).intValue(); }

        public long longValue() { return Long.class.cast(val).longValue(); }

        public float floatValue() { return Float.class.cast(val).floatValue(); }

        public double doubleValue() { return Double.class.cast(val).doubleValue(); }

        public String stringValue() { return String.class.cast(val); }
    }


}
