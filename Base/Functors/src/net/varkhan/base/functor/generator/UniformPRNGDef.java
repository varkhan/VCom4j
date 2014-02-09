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
public class UniformPRNGDef extends AbstractRNG {
    protected Random r;

    public UniformPRNGDef(long s) {
        r = new Random(s);
    }

    public UniformPRNGDef(Random r) {
        this.r=r;
    }

    @Override
    public long nextLong() {
        return r.nextLong();
    }
}
