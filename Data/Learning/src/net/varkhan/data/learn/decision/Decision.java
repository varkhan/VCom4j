package net.varkhan.data.learn.decision;

import net.varkhan.base.functor.Mapper;
import net.varkhan.base.functor.Ordinal;
import net.varkhan.data.learn.Classifier;

import java.util.Collection;
import java.util.List;


/**
 * <b></b>.
 * <p/>
 *
 * @author varkhan
 * @date 12/26/13
 * @time 7:32 PM
 */
public interface Decision<K,T,C> extends Classifier<K,T,C> {

    public K invoke(T obs, C ctx);
    public double confidence(K key, T obs, C ctx);
    public Ordinal<T,C> partition();
    public List<? extends Classifier<K,T,C>> classes();

    public static interface Tree<K,T,C> extends Decision<K,T,C> {
        public K invoke(T obs, C ctx);
        public Decision<K,T,C> decision(T obs, C ctx);
        public double confidence(K key, T obs, C ctx);
        public Collection<? extends Mapper<?,T,C>> attributes();
    }

}
