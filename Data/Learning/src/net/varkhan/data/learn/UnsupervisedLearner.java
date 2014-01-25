package net.varkhan.data.learn;

/**
 * <b></b>.
 * <p/>
 *
 * @author varkhan
 * @date 12/1/13
 * @time 6:52 PM
 */
public interface UnsupervisedLearner<K,T,C> extends Learner<K,T,C> {

    public boolean train(T obs, C ctx);

}
