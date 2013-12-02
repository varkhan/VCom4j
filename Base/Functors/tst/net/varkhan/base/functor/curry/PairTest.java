package net.varkhan.base.functor.curry;

import junit.framework.TestCase;


/**
 * <b></b>.
 * <p/>
 *
 * @author varkhan
 * @date 12/1/13
 * @time 1:38 PM
 */
public class PairTest extends TestCase {
    public void testLvalue() throws Exception {
        Pair<Integer,String> t2 = new Pair.Value<Integer,String>(3,"s");
        assertEquals("lval",(Object)3,t2.lvalue());
    }

    public void testRvalue() throws Exception {
        Pair<Integer,String> t2 = new Pair.Value<Integer,String>(3,"s");
        assertEquals("rval","s",t2.rvalue());
    }

    public void testValues() throws Exception {
        Pair<Integer,String> t2 = new Pair.Value<Integer,String>(3,"s");
        assertEquals("vals",2,t2.values().length);
        assertEquals("vals",3,t2.values()[0]);
        assertEquals("vals","s",t2.values()[1]);
    }
}
