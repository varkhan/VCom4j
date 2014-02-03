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
public class GaussianGenerator<C> implements Generator<Double,C> {

    protected Double next=null;
    protected final UniformRandomGenerator rand;

    public GaussianGenerator(UniformRandomGenerator rand) {this.rand=rand;}

    @Override
    public Double invoke(C ctx) {
        // Do we have a saved deviate?
        if(next!=null) {
            Double n=next;
            next=null;
            return n;
        }
        // Compute a new pair of deviates, using the Box-Muller transformation
        // This is actually a general rejection method applied to the unit circle
        double v1, v2, s;
        do {
            v1 = 2 * rand.nextDouble() - 1; // between -1 and 1
            v2 = 2 * rand.nextDouble() - 1; // between -1 and 1
            s = v1 * v1 + v2 * v2;
        } while (s >= 1 || s == 0);
        double ml = StrictMath.sqrt(-2 * StrictMath.log(s)/s);
        next = v2 * ml;
        return v1 * ml;
    }
}
