package net.varkhan.data.learn;

import net.varkhan.base.functor.Mapper;


/**
 * <b></b>.
 * <p/>
 *
 * @author varkhan
 * @date 11/30/13
 * @time 6:56 PM
 */
public class ExactClassifier<K,T,C> implements Classifier<K, T, C> {


    protected final Mapper<K,T,C> par;

    public ExactClassifier(Mapper<K,T,C> par) {
        this.par=par;
    }

    @Override
    public K invoke(T obs, C ctx) {
        return par.invoke(obs, ctx);
    }

    @Override
    public double confidence(K key, T obs, C ctx) {
        return par.invoke(obs, ctx).equals(key)?1.0:0.0;
    }

}
