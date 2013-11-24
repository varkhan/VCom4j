package net.varkhan.base.conversion;


/**
 * <b></b>.
 * <p/>
 *
 * @author varkhan
 * @date 11/23/13
 * @time 4:58 PM
 */
public abstract class AbstractEncoder<T,C> implements Encoder<T, C> {

    public byte[] encode(T obj, C ctx) {
        long len = length(obj, ctx);
        byte[] buf = new byte[(int)len];
        long act = encode(obj, buf, 0, len, ctx);
        if(act>=len) return buf;
        byte[] cpy = new byte[(int)act];
        System.arraycopy(buf, 0, cpy, 0, (int)act);
        return cpy;
    }


}
