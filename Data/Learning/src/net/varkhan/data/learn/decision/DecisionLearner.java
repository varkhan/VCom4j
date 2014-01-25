package net.varkhan.data.learn.decision;

import net.varkhan.base.functor.Mapper;
import net.varkhan.base.functor.curry.Pair;
import net.varkhan.data.learn.Classifier;
import net.varkhan.data.learn.SupervisedLearner;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;


/**
 * <b></b>.
 * <p/>
 *
 * @author varkhan
 * @date 12/28/13
 * @time 12:39 PM
 */
public class DecisionLearner<K,T,C> implements SupervisedLearner<K,T,C> {

    protected final Collection<Pair<Mapper<?,T,C>,Partition.Factory<K,?,T,C>>> attributes = new HashSet<Pair<Mapper<?,T,C>,Partition.Factory<K,?,T,C>>>();
    protected final Collection<Pair.Value<T,K>>                                observed   = new ArrayList<Pair.Value<T,K>>();


    @Override

    public boolean train(T obs, K key, C ctx) {
        return observed.add(new Pair.Value<T,K>(obs, key));
    }

    @Override
    public DecisionTree<K,T,C> model() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }
}
