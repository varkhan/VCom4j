package net.varkhan.base.functor.functional;

import junit.framework.TestCase;
import net.varkhan.base.functor.Functional;


/**
 * <b></b>.
 * <p/>
 *
 * @author varkhan
 * @date 11/9/13
 * @time 5:29 PM
 */
public class AggregateFunctionalTest extends TestCase {

    public void testAggregate() throws Exception {
        final String ref = "@REF";
        Functional<String,Object> x = new Functional<String,Object>() {
            @Override
            public double invoke(String arg, Object ctx) {
                return arg==ctx?+1.0:-1.0;
            }
        };
        AggregateFunctional<String,Object> p = new AggregateFunctional<String,Object>(x) {
            @Override
            public double invoke(String arg, Object ctx) {
                return funcs.length==0?0.0:funcs[0].invoke(arg, ctx);
            }
        };
        assertEquals("components()",1,p.components().length);
        assertSame("components()[0]",x,p.components()[0]);
        assertEquals("invoke(null,null)",+1.0,p.invoke(null,null));
        assertEquals("invoke(@REF,null)",-1.0,p.invoke(ref,null));
        assertEquals("invoke(@REF,@REF)",+1.0,p.invoke(ref,ref));
    }


    public void testSum() throws Exception {
        assertEquals("sum()",0.0,AggregateFunctional.sum().invoke("foo",null));
        assertEquals("sum()",0.0,AggregateFunctional.sum(ConstFunctional.as(0.0)).invoke("foo",null));
        assertEquals("sum()",1.0,AggregateFunctional.sum(ConstFunctional.as(1.0)).invoke("foo",null));
        assertEquals("sum()",3.0,AggregateFunctional.sum(ConstFunctional.as(1.0),ConstFunctional.as(2.0)).invoke("foo",null));
        assertEquals("sum()",2.0,AggregateFunctional.sum(ConstFunctional.as(1.0),ConstFunctional.as(2.0),ConstFunctional.as(-1.0)).invoke("foo",null));
    }

    public void testProd() throws Exception {
        assertEquals("sum()",1.0,AggregateFunctional.prod().invoke("foo",null));
        assertEquals("sum()",0.0,AggregateFunctional.prod(ConstFunctional.as(0.0)).invoke("foo",null));
        assertEquals("sum()",1.0,AggregateFunctional.prod(ConstFunctional.as(1.0)).invoke("foo",null));
        assertEquals("sum()",2.0,AggregateFunctional.prod(ConstFunctional.as(1.0),ConstFunctional.as(2.0)).invoke("foo",null));
        assertEquals("sum()",-4.0,AggregateFunctional.prod(ConstFunctional.as(2.0),ConstFunctional.as(2.0),ConstFunctional.as(-1.0)).invoke("foo",null));
    }

    public void testMin() throws Exception {
        assertEquals("min()",+Double.MAX_VALUE,AggregateFunctional.min().invoke("foo",null));
        assertEquals("min()",0.0,AggregateFunctional.min(ConstFunctional.as(0.0)).invoke("foo",null));
        assertEquals("min()",1.0,AggregateFunctional.min(ConstFunctional.as(1.0)).invoke("foo",null));
        assertEquals("min()",1.0,AggregateFunctional.min(ConstFunctional.as(1.0),ConstFunctional.as(2.0)).invoke("foo",null));
        assertEquals("min()",-1.0,AggregateFunctional.min(ConstFunctional.as(1.0),ConstFunctional.as(2.0),ConstFunctional.as(-1.0)).invoke("foo",null));
    }

    public void testMax() throws Exception {
        assertEquals("max()",-Double.MAX_VALUE,AggregateFunctional.max().invoke("foo",null));
        assertEquals("max()",0.0,AggregateFunctional.max(ConstFunctional.as(0.0)).invoke("foo",null));
        assertEquals("max()",1.0,AggregateFunctional.max(ConstFunctional.as(1.0)).invoke("foo",null));
        assertEquals("max()",2.0,AggregateFunctional.max(ConstFunctional.as(1.0),ConstFunctional.as(2.0)).invoke("foo",null));
        assertEquals("max()",2.0,AggregateFunctional.max(ConstFunctional.as(1.0),ConstFunctional.as(2.0),ConstFunctional.as(-1.0)).invoke("foo",null));
    }
}
