package net.varkhan.data.learn.cluster;

import net.varkhan.data.learn.Classifier;
import net.varkhan.data.learn.UnsupervisedLearner;
import net.varkhan.data.learn.distance.Distance;

import java.util.Collection;


/**
 * <b>A clustering method</b>.
 * <p/>
 *
 * @author varkhan
 * @date 12/1/13
 * @time 12:17 PM
 */
public interface Clustering<K extends Clustering.Cluster<T,C>,T,C> extends UnsupervisedLearner<K,T,C> {

    /**
     * <b>An individual cluster</b>.
     */
    public static interface Cluster<T,C> {

        public T center();

        public double confidence(T obs, C ctx);

        public double diameter(double p, C ctx);

    }

    /**
     * <b>A set of clusters</b>.
     *
     * @param <K> the cluster type
     */
    public static interface Clusters<K extends Cluster<T,C>,T,C> extends Classifier<K,T,C> {

        public Collection<K> clusters();

        public K invoke(T obs, C ctx);

        public double confidence(K cls, T obs, C ctx);

    }

    public Distance<T,C> distance();

    public Clusters<K,T,C> model();

    public boolean train(T obs, C ctx);

}
