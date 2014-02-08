package net.varkhan.base.functor.generator;

/**
 * <b></b>.
 * <p/>
 *
 * @author varkhan
 * @date 2/2/14
 * @time 3:15 PM
 */
public class UniformPseudoRNS64 extends AbstractRandomNumberSource {
    protected long v;

    public UniformPseudoRNS64(long s) {
        v = s ^ 4101842887655102017L;
        v = nextLong();
    }

    @Override
    public long nextLong() {
        v ^= v >>> 21;
        v ^= v << 35;
        v ^= v >>> 4;
        return v * 2685821657736338717L;
    }
}
