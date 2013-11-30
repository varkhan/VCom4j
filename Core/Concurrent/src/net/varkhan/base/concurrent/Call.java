package net.varkhan.base.concurrent;

/**
 * <b></b>.
 * <p/>
 *
 * @author varkhan
 * @date 2/16/11
 * @time 5:38 AM
 */
public interface Call<V> {

    public V get() throws Exception;

}
