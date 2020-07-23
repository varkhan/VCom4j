package net.varkhan.base.functor.curry;

import junit.framework.TestCase;


/**
 * <b></b>.
 * <p/>
 *
 * @author varkhan
 * @date 12/1/13
 * @time 1:22 PM
 */
public class TupleTest extends TestCase {

    public void testTupleValue() throws Exception {
        Tuple<Integer,Tuple<String,Tuple<?,?>>> t1 = new Tuple.Vector<Integer,Tuple<String,Tuple<?,?>>>(3);
        assertEquals("lval",(Object)3,t1.value());
        assertEquals("rval",null,t1.next());
        Tuple<Integer,Tuple<String,Tuple<?,?>>> t2 = new Tuple.Vector<Integer,Tuple<String,Tuple<?,?>>>(3,"s");
        assertEquals("lval",(Object)3,t2.value());
    }

    public void testTupleNext() throws Exception {
        Tuple<Integer,Tuple<String,Tuple<?,?>>> t1 = new Tuple.Vector<Integer,Tuple<String,Tuple<?,?>>>(3);
        assertEquals("rval",null,t1.next());
        Tuple<Integer,Tuple<String,Tuple<?,?>>> t2 = new Tuple.Vector<Integer,Tuple<String,Tuple<?,?>>>(3,"s");
        assertEquals("rval","s",t2.next().value());
        assertEquals("rval",null,t2.next().next());
    }

    public void testTupleValues() throws Exception {
        Tuple<Integer,Tuple<String,Tuple<?,?>>> t1 = new Tuple.Vector<Integer,Tuple<String,Tuple<?,?>>>(3);
        assertEquals("vals",1,t1.values().length);
        assertEquals("vals",3,t1.values()[0]);
        Tuple<Integer,Tuple<String,Tuple<?,?>>> t2 = new Tuple.Vector<Integer,Tuple<String,Tuple<?,?>>>(3,"s");
        assertEquals("vals",2,t2.values().length);
        assertEquals("vals",3,t2.values()[0]);
        assertEquals("vals","s",t2.values()[1]);
    }

    public void testString() throws Exception {
        Tuple<Integer,Tuple<String,Tuple<Boolean,Tuple<?,?>>>> t2 = new Tuple.Vector<Integer,Tuple<String,Tuple<Boolean,Tuple<?,?>>>>(3,"s",true);
        assertEquals("toString","(3,s,true)",t2.toString());
    }

    public void testPairValue() throws Exception {
        Tuple.Pair<Integer,String> t2 = new Tuple.Pair<Integer,String>(3,"s");
        assertEquals("lval",(Object)3,t2.value());
    }

    public void testPairNext() throws Exception {
        Tuple.Pair<Integer,String> t2 = new Tuple.Pair<Integer,String>(3,"s");
        assertEquals("rval","s",t2.next().value());
    }

    public void testPairValues() throws Exception {
        Tuple.Pair<Integer,String> t2 = new Tuple.Pair<Integer,String>(3,"s");
        assertEquals("vals",2,t2.values().length);
        assertEquals("vals",3,t2.values()[0]);
        assertEquals("vals","s",t2.values()[1]);
    }

}
