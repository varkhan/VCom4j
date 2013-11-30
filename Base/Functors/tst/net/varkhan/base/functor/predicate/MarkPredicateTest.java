package net.varkhan.base.functor.predicate;

import junit.framework.TestCase;
import net.varkhan.base.functor.Predicate;


/**
 * <b></b>.
 * <p/>
 *
 * @author varkhan
 * @date 11/10/13
 * @time 12:52 PM
 */
public class MarkPredicateTest extends TestCase {
    public void testMark() throws Exception {
        Object o1 = new Object();
        Object o2 = new Object();
        Predicate<Object,Object> p = new MarkPredicate<Object,Object>();
        assertTrue("null==null",p.invoke(null,null));
        assertFalse("1!=null",p.invoke(o1,null));
        assertFalse("null!=1",p.invoke(null,o1));
        assertTrue("1==1",p.invoke(o1,o1));
        assertFalse("1!=2",p.invoke(o1,o2));
        assertFalse("2!=1",p.invoke(o2,o1));
    }
}
