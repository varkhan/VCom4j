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
public class GaussianNumberGenerator<C> implements Generator<Double,C> {

    protected final RandomNumberSource rand;
    protected final double             mean;
    protected final double             sdev;
    protected volatile Double          next = null;

    public GaussianNumberGenerator(RandomNumberSource rand, double mean, double sdev) {
        this.rand = rand;
        this.mean = mean;
        this.sdev = sdev;
    }

    @Override
    public Double invoke(C ctx) {
        // Do we have a saved deviate?
        if(next!=null) {
            Double n = next;
            next = null;
            return n;
        }
        // Compute a new pair of deviates, using the Box-Muller transformation
        // This is in fact a particular case of a general rejection method applied to the unit circle
        double v1, v2, s;
        do {
            v1 = 2 * rand.nextDouble() - 1; // between -1 and 1
            v2 = 2 * rand.nextDouble() - 1; // between -1 and 1
            s = v1 * v1 + v2 * v2;
        } while (s >= 1 || s == 0);
        double ml = sdev * StrictMath.sqrt(-2 * StrictMath.log(s)/s);
        next = mean + v2 * ml;
        return mean + v1 * ml;
    }
}
