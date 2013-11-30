/**
 *
 */
package net.varkhan.base.conversion;

import java.io.InputStream;
import java.nio.ByteBuffer;


/**
 * <b>A serialization decoder.</b>
 * <p/>
 *
 * @param <T> the type of objects that can be decoded
 * @param <C> the type of the serialization context
 *
 * @author varkhan
 * @date Nov 7, 2010
 * @time 11:49:22 PM
 */
public interface Decoder<T,C> {

    public T decode(InputStream stm, C ctx);

    public T decode(ByteBuffer buf, C ctx);

    public T decode(byte[] dat, long pos, long len, C ctx);

}