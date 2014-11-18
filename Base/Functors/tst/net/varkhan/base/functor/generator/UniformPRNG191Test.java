package net.varkhan.base.functor.generator;

/**
 * <b></b>.
 * <p/>
 *
 * @author varkhan
 * @date 2/2/14
 * @time 3:49 PM
 */
public class UniformPRNG191Test extends AbstractRNGTest {

    public void testLong() throws Exception {
        testLong(new UniformPRNG191(System.nanoTime()),100000000L,2.0);
    }

}
