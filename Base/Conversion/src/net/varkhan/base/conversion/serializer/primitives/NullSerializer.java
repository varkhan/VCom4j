package net.varkhan.base.conversion.serializer.primitives;

import net.varkhan.base.conversion.serializer.Serializer;

import java.io.InputStream;
import java.io.OutputStream;
import java.nio.ByteBuffer;


/**
 * <b>A no-op serializer for {@literal null} objects</b>.
 * <p/>
 * This serializer does not output any bytes when encoding, and
 * always returns {@literal null} when decoding, consuming 0 bytes.
 * <p/>
 *
 * @author varkhan
 * @date 1/30/11
 * @time 4:53 AM
 */
public class NullSerializer<T,C> implements Serializer<T,C> {

    protected static final NullSerializer instance=new NullSerializer();

    @SuppressWarnings("unchecked")
    public static <T,C> NullSerializer<T,C> instance() { return instance; }

    public T decode(InputStream stm, C ctx) { return null; }

    public T decode(ByteBuffer buf, C ctx) { return null; }

    public T decode(byte[] dat, long pos, long len, C ctx) { return null; }

    public long encode(T obj, OutputStream stm, C ctx) { return 0; }

    public long encode(T obj, ByteBuffer buf, C ctx) { return 0; }

    public long encode(T obj, byte[] dat, long pos, long len, C ctx) { return 0; }

    public long length(T obj, C ctx) { return 0; }

}
