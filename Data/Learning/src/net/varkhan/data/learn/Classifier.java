package net.varkhan.data.learn;

import net.varkhan.base.functor.Mapper;


/**
 * <b></b>.
 * <p/>
 *
 * @author varkhan
 * @date 11/30/13
 * @time 12:48 PM
 */
public interface Classifier<K,T,C> extends Mapper<K,T,C> {

    public K invoke(T obs, C ctx);

    public double confidence(K key, T obs, C ctx);

}
