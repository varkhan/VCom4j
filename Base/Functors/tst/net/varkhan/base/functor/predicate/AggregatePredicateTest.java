package net.varkhan.base.functor.predicate;

import junit.framework.TestCase;
import net.varkhan.base.functor.Predicate;


/**
 * <b></b>.
 * <p/>
 *
 * @author varkhan
 * @date 11/9/13
 * @time 5:19 PM
 */
public class AggregatePredicateTest extends TestCase {

    public void testAggregate() throws Exception {
        final String ref = "@REF";
        Predicate<String,Object> x = new Predicate<String,Object>() {
            @Override
            public boolean invoke(String arg, Object ctx) {
                return arg==ctx;
            }
        };
        AggregatePredicate<String,Object> p = new AggregatePredicate<String,Object>(x) {
            @Override
            public boolean invoke(String arg, Object ctx) {
                return preds.length>0 && preds[0].invoke(arg, ctx);
            }
        };
        assertEquals("components()",1,p.components().length);
        assertSame("components()[0]",x,p.components()[0]);
        assertTrue("invoke(null,null)",p.invoke(null,null));
        assertFalse("invoke(@REF,null)",p.invoke(ref,null));
        assertTrue("invoke(@REF,@REF)",p.invoke(ref,ref));
    }

    public void testAnd() throws Exception {
        assertEquals("and()",true,AggregatePredicate.and().invoke("foo",null));
        assertEquals("and(false)",false,AggregatePredicate.and(ConstPredicate.FALSE()).invoke("foo",null));
        assertEquals("and(true)",true,AggregatePredicate.and(ConstPredicate.TRUE()).invoke("foo",null));
        assertEquals("and(false,false)",false,AggregatePredicate.and(ConstPredicate.FALSE(),ConstPredicate.FALSE()).invoke("foo",null));
        assertEquals("and(false,true)",false,AggregatePredicate.and(ConstPredicate.FALSE(),ConstPredicate.TRUE()).invoke("foo",null));
        assertEquals("and(true,false)",false,AggregatePredicate.and(ConstPredicate.TRUE(),ConstPredicate.FALSE()).invoke("foo",null));
        assertEquals("and(true,true)",true,AggregatePredicate.and(ConstPredicate.TRUE(),ConstPredicate.TRUE()).invoke("foo",null));
    }

    public void testNand() throws Exception {
        assertEquals("nand()",false,AggregatePredicate.nand().invoke("foo",null));
        assertEquals("nand(false)",true,AggregatePredicate.nand(ConstPredicate.FALSE()).invoke("foo",null));
        assertEquals("nand(true)",false,AggregatePredicate.nand(ConstPredicate.TRUE()).invoke("foo",null));
        assertEquals("nand(false,false)",true,AggregatePredicate.nand(ConstPredicate.FALSE(), ConstPredicate.FALSE()).invoke("foo",null));
        assertEquals("nand(false,true)",true,AggregatePredicate.nand(ConstPredicate.FALSE(), ConstPredicate.TRUE()).invoke("foo",null));
        assertEquals("nand(true,false)",true,AggregatePredicate.nand(ConstPredicate.TRUE(), ConstPredicate.FALSE()).invoke("foo",null));
        assertEquals("nand(true,true)",false,AggregatePredicate.nand(ConstPredicate.TRUE(), ConstPredicate.TRUE()).invoke("foo",null));
    }

    public void testOr() throws Exception {
        assertEquals("or()",false,AggregatePredicate.or().invoke("foo",null));
        assertEquals("or(false)",false,AggregatePredicate.or(ConstPredicate.FALSE()).invoke("foo",null));
        assertEquals("or(true)",true,AggregatePredicate.or(ConstPredicate.TRUE()).invoke("foo",null));
        assertEquals("or(false,false)",false,AggregatePredicate.or(ConstPredicate.FALSE(), ConstPredicate.FALSE()).invoke("foo",null));
        assertEquals("or(false,true)",true,AggregatePredicate.or(ConstPredicate.FALSE(), ConstPredicate.TRUE()).invoke("foo",null));
        assertEquals("or(true,false)",true,AggregatePredicate.or(ConstPredicate.TRUE(), ConstPredicate.FALSE()).invoke("foo",null));
        assertEquals("or(true,true)",true,AggregatePredicate.or(ConstPredicate.TRUE(), ConstPredicate.TRUE()).invoke("foo",null));
    }

    public void testNor() throws Exception {
        assertEquals("nor()",true,AggregatePredicate.nor().invoke("foo",null));
        assertEquals("nor(false)",true,AggregatePredicate.nor(ConstPredicate.FALSE()).invoke("foo",null));
        assertEquals("nor(true)",false,AggregatePredicate.nor(ConstPredicate.TRUE()).invoke("foo",null));
        assertEquals("nor(false,false)",true,AggregatePredicate.nor(ConstPredicate.FALSE(), ConstPredicate.FALSE()).invoke("foo",null));
        assertEquals("nor(false,true)",false,AggregatePredicate.nor(ConstPredicate.FALSE(), ConstPredicate.TRUE()).invoke("foo",null));
        assertEquals("nor(true,false)",false,AggregatePredicate.nor(ConstPredicate.TRUE(), ConstPredicate.FALSE()).invoke("foo",null));
        assertEquals("nor(true,true)",false,AggregatePredicate.nor(ConstPredicate.TRUE(), ConstPredicate.TRUE()).invoke("foo",null));
    }

}
