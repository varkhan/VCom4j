/**
 *
 */
package net.varkhan.base.conversion.serializer;


import net.varkhan.base.conversion.Decoder;
import net.varkhan.base.conversion.Encoder;

import java.io.InputStream;
import java.io.OutputStream;
import java.nio.ByteBuffer;


/**
 * <b>A serialization transcoder.</b>
 * <p/>
 *
 * @param <T> the type of objects that can be transcoded
 * @param <C> the type of the serialization context
 *
 * @author varkhan
 * @date Nov 7, 2010
 * @time 11:39:28 PM
 */
public interface Serializer<T,C> extends Decoder<T,C>, Encoder<T,C> {

    public long encode(T obj, OutputStream stm, C ctx);

    public long encode(T obj, ByteBuffer buf, C ctx);

    public long encode(T obj, byte[] dat, long pos, long len, C ctx);

    public long length(T obj, C ctx);

    public T decode(InputStream stm, C ctx);

    public T decode(ByteBuffer buf, C ctx);

    public T decode(byte[] dat, long pos, long len, C ctx);

}
