package net.varkhan.data.learn.ensemble;

import net.varkhan.data.learn.Classifier;

import java.util.HashSet;
import java.util.Set;


/**
 * <b></b>.
 * <p/>
 *
 * @author varkhan
 * @date 12/26/13
 * @time 6:57 PM
 */
public class ConsensusClassifier<K,T,C> implements Classifier<K,T,C> {
    protected final Classifier<K,T,C>[] components;

    public ConsensusClassifier(Classifier<K,T,C>... components) {
        this.components = components;
    }

    @Override
    public K invoke(T obs, C ctx) {
        Set<K> keys = new HashSet<K>(components.length);
        for(Classifier<K,T,C> c: components) {
            keys.add(c.invoke(obs, ctx));
        }
        K key = null;
        double prb = 0;
        for(K k: keys) {
            double p = confidence(k, obs, ctx);
            if(prb<p) {
                key = k;
                prb = p;
            }
        }
        return key;
    }

    @Override
    public double confidence(K key, T obs, C ctx) {
        double p = 1;
        for(Classifier<K,T,C> c: components) {
            p *= c.confidence(key, obs, ctx);
        }
        return p;
    }

}
