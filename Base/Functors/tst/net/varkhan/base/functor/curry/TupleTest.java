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
public class TupleTest extends TestCase {

    public void testLvalue() throws Exception {
        Tuple<Integer,Tuple<String,Tuple>> t1 = new Tuple.Value<Integer,Tuple<String,Tuple>>(3);
        assertEquals("lval",(Object)3,t1.lvalue());
        assertEquals("rval",null,t1._value());
        Tuple<Integer,Tuple<String,Tuple>> t2 = new Tuple.Value<Integer,Tuple<String,Tuple>>(3,"s");
        assertEquals("lval",(Object)3,t2.lvalue());
    }

    public void test_value() throws Exception {
        Tuple<Integer,Tuple<String,Tuple>> t1 = new Tuple.Value<Integer,Tuple<String,Tuple>>(3);
        assertEquals("rval",null,t1._value());
        Tuple<Integer,Tuple<String,Tuple>> t2 = new Tuple.Value<Integer,Tuple<String,Tuple>>(3,"s");
        assertEquals("rval","s",t2._value().lvalue());
        assertEquals("rval",null,t2._value()._value());
    }

    public void testValues() throws Exception {
        Tuple<Integer,Tuple<String,Tuple>> t1 = new Tuple.Value<Integer,Tuple<String,Tuple>>(3);
        assertEquals("vals",1,t1.values().length);
        assertEquals("vals",3,t1.values()[0]);
        Tuple<Integer,Tuple<String,Tuple>> t2 = new Tuple.Value<Integer,Tuple<String,Tuple>>(3,"s");
        assertEquals("vals",2,t2.values().length);
        assertEquals("vals",3,t2.values()[0]);
        assertEquals("vals","s",t2.values()[1]);
    }
}
