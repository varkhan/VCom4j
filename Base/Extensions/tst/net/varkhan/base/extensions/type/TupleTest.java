package net.varkhan.base.extensions.type;

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

    public void testVectorSize() throws Exception {
        Tuple<Integer,? extends Tuple<?,?>> t1 = new Tuple.Vector<Integer,Tuple<String,Tuple<?,?>>>(3);
        assertEquals("size",1,t1.size());
        Tuple<Integer,? extends Tuple<String,? extends Tuple<?,?>>> t2 = new Tuple.Vector<Integer,Tuple<String,Tuple<?,?>>>(3,"s");
        assertEquals("size",2,t2.size());
    }

    public void testVectorHasNext() throws Exception {
        Tuple<Integer,? extends Tuple<?,?>> t1 = new Tuple.Vector<Integer,Tuple<String,Tuple<?,?>>>(3);
        assertFalse("hasnext", t1.hasNext());
        Tuple<Integer,? extends Tuple<String,? extends Tuple<?,?>>> t2 = new Tuple.Vector<Integer,Tuple<String,Tuple<?,?>>>(3,"s");
        assertTrue("hasnext", t2.hasNext());
        assertFalse("hasnext", t2.next().hasNext());
    }

    public void testVectorValue() throws Exception {
        Tuple<Integer,? extends Tuple<?,?>> t1 = new Tuple.Vector<Integer,Tuple<String,Tuple<?,?>>>(3);
        assertEquals("value",(Object)3,t1.value());
        Tuple<Integer,? extends Tuple<String,? extends Tuple<?,?>>> t2 = new Tuple.Vector<Integer,Tuple<String,Tuple<?,?>>>(3,"s");
        assertEquals("value",(Object)3,t2.value());
    }

    public void testVectorNext() throws Exception {
        Tuple<Integer,? extends Tuple<String,Tuple<?,?>>> t1 = new Tuple.Vector<Integer,Tuple<String,Tuple<?,?>>>(3);
        assertNull("next", t1.next());
        Tuple<Integer,? extends Tuple<String,? extends Tuple<?,?>>> t2 = new Tuple.Vector<Integer,Tuple<String,Tuple<?,?>>>(3,"s");
        assertNotNull("next", t2.next());
        assertNull("next", t2.next().next());
    }

    public void testVectorValues() throws Exception {
        Tuple<Integer,? extends Tuple<?,?>> t1 = new Tuple.Vector<Integer,Tuple<String,Tuple<?,?>>>(3);
        assertEquals("values",1,t1.values().length);
        assertEquals("values",3,t1.values()[0]);
        Tuple<Integer,? extends Tuple<String,? extends Tuple<?,?>>> t2 = new Tuple.Vector<Integer,Tuple<String,Tuple<?,?>>>(3,"s");
        assertEquals("values",2,t2.values().length);
        assertEquals("values",3,t2.values()[0]);
        assertEquals("values","s",t2.values()[1]);
    }

    public void testVectorToString() throws Exception {
        Tuple<Integer,? extends Tuple<?,?>> t1 = new Tuple.Vector<Integer,Tuple<String,Tuple<?,?>>>(3);
        assertEquals("toString","(3)",t1.toString());
        Tuple<Integer,? extends Tuple<String,? extends Tuple<?,?>>> t2 = new Tuple.Vector<Integer,Tuple<String,Tuple<?,?>>>(3,"s");
        assertEquals("toString","(3,s)",t2.toString());
        Tuple<Integer,? extends Tuple<String,? extends Tuple<Boolean,? extends Tuple<?,?>>>> t3 = new Tuple.Vector<Integer,Tuple<String,Tuple<Boolean,Tuple<?,?>>>>(3,"s",true);
        assertEquals("toString","(3,s,true)",t3.toString());
    }


    public void testChainSize() throws Exception {
        Tuple<Integer,? extends Tuple<?,?>> t1 = new Tuple.Chain<Integer,Tuple<String,Tuple<?,?>>>(3, null);
        assertEquals("size",1,t1.size());
        Tuple<Integer,? extends Tuple<String,? extends Tuple<?,?>>> t2 = new Tuple.Chain<Integer,Tuple<String,Tuple<?,?>>>(3,new Tuple.Chain<>("s", null));
        assertEquals("size",2,t2.size());
    }

    public void testChainHasNext() throws Exception {
        Tuple<Integer,? extends Tuple<?,?>> t1 = new Tuple.Chain<Integer,Tuple<String,Tuple<?,?>>>(3, null);
        assertFalse("hasnext", t1.hasNext());
        Tuple<Integer,? extends Tuple<String,? extends Tuple<?,?>>> t2 = new Tuple.Chain<Integer,Tuple<String,Tuple<?,?>>>(3,new Tuple.Chain<>("s", null));
        assertTrue("hasnext", t2.hasNext());
        assertFalse("hasnext", t2.next().hasNext());
    }

    public void testChainValue() throws Exception {
        Tuple<Integer,? extends Tuple<?,?>> t1 = new Tuple.Chain<Integer,Tuple<String,Tuple<?,?>>>(3, null);
        assertEquals("value",(Object)3,t1.value());
        Tuple<Integer,? extends Tuple<String,? extends Tuple<?,?>>> t2 = new Tuple.Chain<Integer,Tuple<String,Tuple<?,?>>>(3,new Tuple.Chain<>("s", null));
        assertEquals("value",(Object)3,t2.value());
    }

    public void testChainNext() throws Exception {
        Tuple<Integer,? extends Tuple<String,Tuple<?,?>>> t1 = new Tuple.Chain<Integer,Tuple<String,Tuple<?,?>>>(3, null);
        assertNull("next", t1.next());
        Tuple<Integer,? extends Tuple<String,? extends Tuple<?,?>>> t2 = new Tuple.Chain<Integer,Tuple<String,Tuple<?,?>>>(3,new Tuple.Chain<>("s", null));
        assertNotNull("next", t2.next());
        assertNull("next", t2.next().next());
    }

    public void testChainValues() throws Exception {
        Tuple<Integer,? extends Tuple<?,?>> t1 = new Tuple.Chain<Integer,Tuple<String,Tuple<?,?>>>(3, null);
        assertEquals("values",1,t1.values().length);
        assertEquals("values",3,t1.values()[0]);
        Tuple<Integer,? extends Tuple<String,? extends Tuple<?,?>>> t2 = new Tuple.Chain<Integer,Tuple<String,Tuple<?,?>>>(3,new Tuple.Chain<>("s", null));
        assertEquals("values",2,t2.values().length);
        assertEquals("values",3,t2.values()[0]);
        assertEquals("values","s",t2.values()[1]);
    }

    public void testChainToString() throws Exception {
        Tuple<Integer,? extends Tuple<?,?>> t1 = new Tuple.Chain<Integer,Tuple<String,Tuple<?,?>>>(3, null);
        assertEquals("toString","(3)",t1.toString());
        Tuple<Integer,? extends Tuple<String,? extends Tuple<?,?>>> t2 = new Tuple.Chain<Integer,Tuple<String,Tuple<?,?>>>(3,new Tuple.Chain<>("s", null));
        assertEquals("toString","(3,s)",t2.toString());
        Tuple<Integer,? extends Tuple<String,? extends Tuple<Boolean,? extends Tuple<?,?>>>> t3 = new Tuple.Chain<Integer,Tuple<String,Tuple<Boolean,Tuple<?,?>>>>(3,new Tuple.Chain<>("s",new Tuple.Chain<>(true, null)));
        assertEquals("toString","(3,s,true)",t3.toString());
    }
    


    public void testPairSize() throws Exception {
        Tuple<Integer,? extends Tuple<String,? extends Tuple<?,?>>> t2 = new Tuple.Pair<Integer,String>((Integer) 3,"s");
        assertEquals("size",2,t2.size());
    }

    public void testPairHasNext() throws Exception {
        Tuple<Integer,? extends Tuple<String,? extends Tuple<?,?>>> t2 = new Tuple.Pair<Integer,String>(3,"s");
        assertTrue("hasnext", t2.hasNext());
    }

    public void testPairValue() throws Exception {
        Tuple<Integer,? extends Tuple<String,? extends Tuple<?,?>>> t2 = new Tuple.Pair<Integer,String>(3,"s");
        assertEquals("value",(Object)3,t2.value());
    }

    public void testPairNext() throws Exception {
        Tuple<Integer,? extends Tuple<String,? extends Tuple<?,?>>> t2 = new Tuple.Pair<Integer,String>(3,"s");
        assertEquals("next","s",t2.next().value());
    }

    public void testPairValues() throws Exception {
        Tuple<Integer,? extends Tuple<String,? extends Tuple<?,?>>> t2 = new Tuple.Pair<Integer,String>(3,"s");
        assertEquals("vals",2,t2.values().length);
        assertEquals("vals",3,t2.values()[0]);
        assertEquals("vals","s",t2.values()[1]);
    }

    public void testPairToString() throws Exception {
        Tuple<Integer,? extends Tuple<String,? extends Tuple<?,?>>> t2 = new Tuple.Pair<Integer,String>(3,"s");
        assertEquals("toString","(3,s)",t2.toString());
    }



    public void testAtomSize() throws Exception {
        Tuple<Integer,? extends Tuple<?,?>> t1 = new Tuple.Atom<Integer>(3);
        assertEquals("size",1,t1.size());
    }

    public void testAtomHasNext() throws Exception {
        Tuple<Integer,? extends Tuple<?,?>> t1 = new Tuple.Atom<Integer>(3);
        assertFalse("hasnext", t1.hasNext());
   }

    public void testAtomValue() throws Exception {
        Tuple<Integer,? extends Tuple<?,?>> t1 = new Tuple.Atom<Integer>(3);
        assertEquals("value",(Object)3,t1.value());
    }

    public void testAtomNext() throws Exception {
        Tuple<Integer,? extends Tuple<?,?>> t1 = new Tuple.Atom<Integer>(3);
        assertNull("next", t1.next());
    }

    public void testAtomValues() throws Exception {
        Tuple<Integer,? extends Tuple<?,?>> t1 = new Tuple.Atom<Integer>(3);
        assertEquals("values",1,t1.values().length);
        assertEquals("values",3,t1.values()[0]);
    }

    public void testAtomToString() throws Exception {
        Tuple<Integer,? extends Tuple<?,?>> t1 = new Tuple.Atom<Integer>(3);
        assertEquals("toString","(3)",t1.toString());
    }

}
