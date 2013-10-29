package net.varkhan.base.functor.predicate;

import junit.framework.TestCase;


/**
 * <b></b>.
 * <p/>
 *
 * @author varkhan
 * @date 3/22/13
 * @time 1:13 PM
 */
public class TransformPredicateTest extends TestCase {

    public void testNot() {
        assertEquals("false", false, TransformPredicate.not(ConstPredicate.TRUE()).invoke("foo",null));
        assertEquals("true", true, TransformPredicate.not(ConstPredicate.FALSE()).invoke("foo",null));
    }

}
