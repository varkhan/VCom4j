package net.varkhan.data.learn;

/**
 * <b></b>.
 * <p/>
 *
 * @author varkhan
 * @date 11/30/13
 * @time 6:56 PM
 */
public class SingletonClassifier<K,T,C> implements Classifier<K, T, C> {


    protected final K      key;
    protected final double cnf;

    public SingletonClassifier(K key) { this(key, 1); }

    public SingletonClassifier(K key, double cnf) {
        this.key=key;
        this.cnf=cnf;
    }

    @Override
    public K invoke(T obs, C ctx) {
        return key;
    }

    @Override
    public double confidence(K key, T obs, C ctx) {
        return this.key.equals(key) ? cnf : (1.0-cnf);
    }

}
