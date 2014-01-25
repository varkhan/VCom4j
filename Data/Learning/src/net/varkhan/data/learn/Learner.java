package net.varkhan.data.learn;

/**
 * <b></b>.
 * <p/>
 *
 * @author varkhan
 * @date 11/30/13
 * @time 7:07 PM
 */
public interface Learner<K,T,C> {

    public Classifier<K,T,C> model();

}
