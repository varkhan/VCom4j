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
public class CPairTest extends TestCase {
    public void testLvalue() throws Exception {
        CPair<Integer,String> t2 = new CPair.Value<Integer,String>(Integer.class,String.class,3,"s");
        assertEquals("ltyp",Integer.class,t2.ltype());
        assertEquals("lval",(Object)3,t2.lvalue());
    }

    public void testRvalue() throws Exception {
        CPair<Integer,String> t2 = new CPair.Value<Integer,String>(Integer.class,String.class,3,"s");
        assertEquals("rtyp",String.class,t2.rtype());
        assertEquals("rval","s",t2.rvalue());
    }

    public void testValues() throws Exception {
        CPair<Integer,String> t2 = new CPair.Value<Integer,String>(Integer.class,String.class,3,"s");
        assertEquals("typs",2,t2.types().length);
        assertEquals("typs",Integer.class,t2.types()[0]);
        assertEquals("typs",String.class,t2.types()[1]);
        assertEquals("vals",2,t2.values().length);
        assertEquals("vals",3,t2.values()[0]);
        assertEquals("vals","s",t2.values()[1]);
    }
}
