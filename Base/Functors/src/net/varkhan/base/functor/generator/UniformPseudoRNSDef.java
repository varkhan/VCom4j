package net.varkhan.base.functor.generator;

import java.util.Random;


/**
 * <b></b>.
 * <p/>
 *
 * @author varkhan
 * @date 2/2/14
 * @time 3:15 PM
 */
public class UniformPseudoRNSDef extends AbstractRandomNumberSource {
    protected Random r;

    public UniformPseudoRNSDef(long s) {
        r = new Random(s);
    }

    @Override
    public long nextLong() {
        return r.nextLong();
    }
}
