package net.varkhan.base.functor.generator;

/**
 * <b></b>.
 * <p/>
 *
 * @author varkhan
 * @date 2/2/14
 * @time 3:04 PM
 */
public class UniformPRNG191 extends AbstractRNG {
    protected long u, v, w;


    public UniformPRNG191(long s) {
        v = 4101842887655102017L;
        w = 1;
        u = s ^ v;
        nextLong();
        v = u;
        nextLong();
        w = v;
        nextLong();
    }

    @Override
    public long nextLong() {
        u = u * 2862933555777941757L + 7046029254386353087L;
        v ^= v >>> 17;
        v ^= v << 31;
        v ^= v >>> 8;
        w = 4294957665L * (w & 0xFFFFFFFFL) + (w >>> 32);
        long x = u ^ (u << 21);
        x ^= x >>> 35;
        x ^= x << 4;
        return (x + v) ^ w;
    }

}
