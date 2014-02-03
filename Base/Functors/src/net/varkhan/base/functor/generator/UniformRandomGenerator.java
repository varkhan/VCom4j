package net.varkhan.base.functor.generator;

/**
 * <b></b>.
 * <p/>
 *
 * @author varkhan
 * @date 2/2/14
 * @time 2:28 PM
 */
public interface UniformRandomGenerator {

    public boolean nextBoolean();

    public int nextInt();

    public int nextInt(int n);

    public long nextLong();

    public long nextLong(long n);

    public float nextFloat();

    public float nextFloat(float n);

    public double nextDouble();

    public double nextDouble(double n);

}
