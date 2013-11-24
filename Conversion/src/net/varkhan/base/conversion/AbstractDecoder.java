package net.varkhan.base.conversion;

/**
 * <b></b>.
 * <p/>
 *
 * @author varkhan
 * @date 11/23/13
 * @time 5:01 PM
 */
public abstract class AbstractDecoder<T,C> implements Decoder<T, C> {

    public T decode(byte[] dat, C ctx) {
        return decode(dat, 0, dat.length, ctx);
    }

}
