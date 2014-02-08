package net.varkhan.base.functor.generator;

/**
 * <b></b>.
 * <p/>
 *
 * @author varkhan
 * @date 2/2/14
 * @time 3:49 PM
 */
public class UniformPseudoRNS64Test extends AbstractRandomNumberSourceTest {

    public void testLong() throws Exception {
        testLong(new UniformPseudoRNS64(System.currentTimeMillis()),1000000000L,1.5);
    }

}
