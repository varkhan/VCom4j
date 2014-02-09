package net.varkhan.base.functor.generator;

/**
 * <b></b>.
 * <p/>
 *
 * @author varkhan
 * @date 2/2/14
 * @time 2:46 PM
 */
public abstract class AbstractRNG implements RNG {

    protected static final double DNORM = 5.42101086242752217e-20;

    @Override
    public boolean nextBoolean() {
        return 1==(1&nextLong());
    }

    @Override
    public int nextInt() {
        return (int) (0xFFFFFFFFL&nextLong());
    }

    @Override
    public int nextInt(int n) {
        if (n <= 0) throw new IllegalArgumentException("n must be positive");
        long bits, val;
        do {
            bits = nextLong();
            val = bits % n;
        } while (bits - val + (n-1) < 0);
        return (int) (0xFFFFFFFFL&val);
    }

    @Override
    public long nextLong(long n) {
        if (n <= 0) throw new IllegalArgumentException("n must be positive");
        long bits, val;
        do {
            bits = nextLong();
            val = bits % n;
        } while (bits - val + (n-1) < 0);
        return val;
    }

    @Override
    public float nextFloat() {
        return (float)(DNORM*nextLong());
    }

    @Override
    public float nextFloat(float n) {
        return (float)(n*DNORM*nextLong());
    }

    @Override
    public double nextDouble() {
        return DNORM*nextLong();
    }

    @Override
    public double nextDouble(double n) {
        return n*DNORM*nextLong();
    }
}
