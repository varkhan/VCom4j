package net.varkhan.base.functor.curry;

import junit.framework.TestCase;
import net.varkhan.base.functor._;


/**
 * <b></b>.
 * <p/>
 *
 * @author varkhan
 * @date 12/1/13
 * @time 1:22 PM
 */
public class CTupleTest extends TestCase {

    public void testLvalue() throws Exception {
        CTuple<Integer,CTuple<String,CTuple>> t1 = new CTuple.Value<Integer,CTuple<String,CTuple>>(Integer.class,null,3,null);
        assertEquals("ltyp",Integer.class,t1.ltype());
        assertEquals("lval",(Object)3,t1.lvalue());
        CTuple<Integer,CTuple<String,CTuple>> t2 = new CTuple.Value<Integer,CTuple<String,CTuple>>(Integer.class,new Class<?>[]{String.class},3,new Object[]{"s"});
        assertEquals("ltyp",Integer.class,t2.ltype());
        assertEquals("lval",(Object)3,t2.lvalue());
    }


    public void test_value() throws Exception {
        CTuple<Integer,CTuple<String,CTuple>> t1 = new CTuple.Value<Integer,CTuple<String,CTuple>>(Integer.class,null,3,null);
        assertEquals("rval",null,t1._value());
        CTuple<Integer,CTuple<String,CTuple>> t2 = new CTuple.Value<Integer,CTuple<String,CTuple>>(Integer.class,new Class<?>[]{String.class},3,new Object[]{"s"});
        assertEquals("rval","s",t2._value().lvalue());
        assertEquals("rval",null,t2._value()._value());
    }

    public void testValues() throws Exception {
        CTuple<Integer,CTuple<String,CTuple>> t1 = new CTuple.Value<Integer,CTuple<String,CTuple>>(Integer.class,null,3,null);
        assertEquals("typs",1,t1.types().length);
        assertEquals("typs",Integer.class,t1.types()[0]);
        assertEquals("vals",1,t1.values().length);
        assertEquals("vals",3,t1.values()[0]);
        CTuple<Integer,CTuple<String,CTuple>> t2 = new CTuple.Value<Integer,CTuple<String,CTuple>>(Integer.class,new Class<?>[]{String.class},3,new Object[]{"s"});
        assertEquals("typs",2,t2.types().length);
        assertEquals("typs",Integer.class,t2.types()[0]);
        assertEquals("typs",String.class,t2.types()[1]);
        assertEquals("vals",2,t2.values().length);
        assertEquals("vals",3,t2.values()[0]);
        assertEquals("vals","s",t2.values()[1]);
    }
}
