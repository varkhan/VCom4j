/**
 *
 */
package net.varkhan.base.conversion;

import java.io.OutputStream;
import java.nio.ByteBuffer;


/**
 * <b>A serialization encoder.</b>
 * <p/>
 *
 * @param <T> the type of objects that can be encoded
 * @param <C> the type of the serialization context
 *
 * @author varkhan
 * @date Nov 7, 2010
 * @time 11:49:46 PM
 */
public interface Encoder<T,C> {

    public long encode(T obj, OutputStream stm, C ctx);

    public long encode(T obj, ByteBuffer buf, C ctx);

    public long encode(T obj, byte[] dat, long pos, long len, C ctx);

    public long length(T obj, C ctx);

}