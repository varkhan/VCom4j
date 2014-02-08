package net.varkhan.base.functor.generator;

import net.varkhan.base.functor.Generator;


/**
 * <b></b>.
 * <p/>
 *
 * @author varkhan
 * @date 2/2/14
 * @time 3:22 PM
 */
public class UniformNumberGenerator<C> implements Generator<Double,C> {

    protected final RandomNumberSource rand;
    protected final double min;
    protected final double dev;

    public UniformNumberGenerator(RandomNumberSource rand, double min, double max) {
        this.rand = rand;
        this.min = min;
        this.dev = max-min;
    }

    @Override
    public Double invoke(C ctx) {
        return rand.nextDouble();
    }
}
