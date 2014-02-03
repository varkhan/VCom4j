package net.varkhan.base.functor.generator;

/**
 * <b></b>.
 * <p/>
 *
 * @author varkhan
 * @date 2/2/14
 * @time 3:49 PM
 */
public class UniformRandomGenerator191Test extends AbstractUniformRandomGeneratorTest {

    public void testLong() throws Exception {
        testLong(new UniformRandomGenerator191(System.currentTimeMillis()),1000000000L,1.5);
    }

}
