package net.varkhan.base.functor.functional;

import junit.framework.TestCase;
import net.varkhan.base.functor.Functional;


/**
 * <b></b>.
 * <p/>
 *
 * @author varkhan
 * @date 11/9/13
 * @time 6:10 PM
 */
public class TransformFunctionalTest extends TestCase {

    public void testTransform() throws Exception {
        final String ref = "@REF";
        Functional<String,Object> x = new Functional<String,Object>() {
            @Override
            public double invoke(String arg, Object ctx) {
                return arg==ctx?+1.0:-1.0;
            }
        };
        TransformFunctional<String,Object> p = new TransformFunctional<String,Object>(x) {
            @Override
            public double invoke(String arg, Object ctx) {
                return -func.invoke(arg, ctx);
            }
        };
        assertSame("component()",x,p.component());
        assertEquals("invoke(null,null)",-1.0,p.invoke(null,null));
        assertEquals("invoke(@REF,null)",+1.0,p.invoke(ref,null));
        assertEquals("invoke(@REF,@REF)",-1.0,p.invoke(ref,ref));
    }

    public void testLog() throws Exception {
        assertEquals("log(0)",Double.NEGATIVE_INFINITY,TransformFunctional.log(ConstFunctional.as(0.0)).invoke("foo",null));
        assertEquals("log(1)",0.0,TransformFunctional.log(ConstFunctional.as(1.0)).invoke("foo",null));
        assertEquals("log(2)",Math.log(2.0),TransformFunctional.log(ConstFunctional.as(2.0)).invoke("foo",null));
        assertEquals("log(e)",1.0,TransformFunctional.log(ConstFunctional.as(Math.E)).invoke("foo",null));
    }

    public void testExp() throws Exception {
        assertEquals("exp(0)",1.0,TransformFunctional.exp(ConstFunctional.as(0.0)).invoke("foo",null));
        assertEquals("exp(1)",Math.exp(1.0),TransformFunctional.exp(ConstFunctional.as(1.0)).invoke("foo",null));
        assertEquals("exp(-1)",Math.exp(-1.0),TransformFunctional.exp(ConstFunctional.as(-1.0)).invoke("foo",null));
        assertEquals("exp(2)",Math.exp(2.0),TransformFunctional.exp(ConstFunctional.as(2.0)).invoke("foo",null));
    }

    public void testSin() throws Exception {
        assertEquals("sin(0)",0.0,TransformFunctional.sin(ConstFunctional.as(0.0)).invoke("foo",null));
        assertEquals("sin(1)",Math.sin(1.0),TransformFunctional.sin(ConstFunctional.as(1.0)).invoke("foo",null));
        assertEquals("sin(-1)",Math.sin(-1.0),TransformFunctional.sin(ConstFunctional.as(-1.0)).invoke("foo",null));
        assertEquals("sin(2)",Math.sin(2.0),TransformFunctional.sin(ConstFunctional.as(2.0)).invoke("foo",null));
    }

    public void testCos() throws Exception {
        assertEquals("cos(0)",1.0,TransformFunctional.cos(ConstFunctional.as(0.0)).invoke("foo",null));
        assertEquals("cos(1)",Math.cos(1.0),TransformFunctional.cos(ConstFunctional.as(1.0)).invoke("foo",null));
        assertEquals("cos(-1)",Math.cos(-1.0),TransformFunctional.cos(ConstFunctional.as(-1.0)).invoke("foo",null));
        assertEquals("cos(2)",Math.cos(2.0),TransformFunctional.cos(ConstFunctional.as(2.0)).invoke("foo",null));
    }

    public void testTan() throws Exception {
        assertEquals("tan(0)",0.0,TransformFunctional.tan(ConstFunctional.as(0.0)).invoke("foo",null));
        assertEquals("tan(1)",Math.tan(1.0),TransformFunctional.tan(ConstFunctional.as(1.0)).invoke("foo",null));
        assertEquals("tan(-1)",Math.tan(-1.0),TransformFunctional.tan(ConstFunctional.as(-1.0)).invoke("foo",null));
        assertEquals("tan(2)",Math.tan(2.0),TransformFunctional.tan(ConstFunctional.as(2.0)).invoke("foo",null));
    }

    public void testAbs() throws Exception {
        assertEquals("abs(0)",0.0,TransformFunctional.abs(ConstFunctional.as(0.0)).invoke("foo",null));
        assertEquals("abs(1)",1.0,TransformFunctional.abs(ConstFunctional.as(1.0)).invoke("foo",null));
        assertEquals("abs(-1)",1.0,TransformFunctional.abs(ConstFunctional.as(-1.0)).invoke("foo",null));
        assertEquals("abs(2)",2.0,TransformFunctional.abs(ConstFunctional.as(2.0)).invoke("foo",null));
    }

    public void testAsin() throws Exception {
        assertEquals("asin(0)",0.0,TransformFunctional.asin(ConstFunctional.as(0.0)).invoke("foo",null));
        assertEquals("asin(1)",Math.asin(1.0),TransformFunctional.asin(ConstFunctional.as(1.0)).invoke("foo",null));
        assertEquals("asin(-1)",Math.asin(-1.0),TransformFunctional.asin(ConstFunctional.as(-1.0)).invoke("foo",null));
        assertEquals("asin(2)",Math.asin(2.0),TransformFunctional.asin(ConstFunctional.as(2.0)).invoke("foo",null));
    }

    public void testAcos() throws Exception {
        assertEquals("acos(0)",0.0,TransformFunctional.acos(ConstFunctional.as(1.0)).invoke("foo",null));
        assertEquals("acos(1)",Math.acos(1.0),TransformFunctional.acos(ConstFunctional.as(1.0)).invoke("foo",null));
        assertEquals("acos(-1)",Math.acos(-1.0),TransformFunctional.acos(ConstFunctional.as(-1.0)).invoke("foo",null));
        assertEquals("acos(2)",Math.acos(2.0),TransformFunctional.acos(ConstFunctional.as(2.0)).invoke("foo",null));
    }

    public void testAtan() throws Exception {
        assertEquals("atan(0)",0.0,TransformFunctional.atan(ConstFunctional.as(0.0)).invoke("foo",null));
        assertEquals("atan(1)",Math.atan(1.0),TransformFunctional.atan(ConstFunctional.as(1.0)).invoke("foo",null));
        assertEquals("atan(-1)",Math.atan(-1.0),TransformFunctional.atan(ConstFunctional.as(-1.0)).invoke("foo",null));
        assertEquals("atan(2)",Math.atan(2.0),TransformFunctional.atan(ConstFunctional.as(2.0)).invoke("foo",null));
    }

    public void testSinh() throws Exception {
        assertEquals("sinh(0)",0.0,TransformFunctional.sinh(ConstFunctional.as(0.0)).invoke("foo",null));
        assertEquals("sinh(1)",Math.sinh(1.0),TransformFunctional.sinh(ConstFunctional.as(1.0)).invoke("foo",null));
        assertEquals("sinh(-1)",Math.sinh(-1.0),TransformFunctional.sinh(ConstFunctional.as(-1.0)).invoke("foo",null));
        assertEquals("sinh(2)",Math.sinh(2.0),TransformFunctional.sinh(ConstFunctional.as(2.0)).invoke("foo",null));
    }

    public void testCosh() throws Exception {
        assertEquals("cosh(0)",1.0,TransformFunctional.cosh(ConstFunctional.as(0.0)).invoke("foo",null));
        assertEquals("cosh(1)",Math.cosh(1.0),TransformFunctional.cosh(ConstFunctional.as(1.0)).invoke("foo",null));
        assertEquals("cosh(-1)",Math.cosh(-1.0),TransformFunctional.cosh(ConstFunctional.as(-1.0)).invoke("foo",null));
        assertEquals("cosh(2)",Math.cosh(2.0),TransformFunctional.cosh(ConstFunctional.as(2.0)).invoke("foo",null));
    }

    public void testTanh() throws Exception {
        assertEquals("tanh(0)",0.0,TransformFunctional.tanh(ConstFunctional.as(0.0)).invoke("foo",null));
        assertEquals("tanh(1)",Math.tanh(1.0),TransformFunctional.tanh(ConstFunctional.as(1.0)).invoke("foo",null));
        assertEquals("tanh(-1)",Math.tanh(-1.0),TransformFunctional.tanh(ConstFunctional.as(-1.0)).invoke("foo",null));
        assertEquals("tanh(2)",Math.tanh(2.0),TransformFunctional.tanh(ConstFunctional.as(2.0)).invoke("foo",null));
    }

    public void testSqrt() throws Exception {
        assertEquals("sqrt(0)",0.0,TransformFunctional.sqrt(ConstFunctional.as(0.0)).invoke("foo",null));
        assertEquals("sqrt(1)",1.0,TransformFunctional.sqrt(ConstFunctional.as(1.0)).invoke("foo",null));
        assertEquals("sqrt(-1)",Double.NaN,TransformFunctional.sqrt(ConstFunctional.as(-1.0)).invoke("foo",null));
        assertEquals("sqrt(2)",Math.sqrt(2.0),TransformFunctional.sqrt(ConstFunctional.as(2.0)).invoke("foo",null));
        assertEquals("sqrt(4)",2.0,TransformFunctional.sqrt(ConstFunctional.as(4.0)).invoke("foo",null));
    }
}
