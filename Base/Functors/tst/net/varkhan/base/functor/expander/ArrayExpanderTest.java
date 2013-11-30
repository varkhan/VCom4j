package net.varkhan.base.functor.expander;

import junit.framework.TestCase;
import net.varkhan.base.functor.Expander;

import java.util.Iterator;


/**
 * <b></b>.
 * <p/>
 * @author varkhan
 * @date 11/5/13
 * @time 5:17 PM
 */
public class ArrayExpanderTest extends TestCase {

    public void testExpander() {
        Expander<String,String[],Object> exp = new ArrayExpander<String, Object>();
        String[] vals0 = new String[] {} ;
        Iterable<String> itb0 = exp.invoke(vals0,null);
        Iterator<String> itr0 = itb0.iterator();
        assertFalse(itr0.hasNext());
        String[] vals1 = new String[] {"a"} ;
        Iterable<String> itb1 = exp.invoke(vals1,null);
        Iterator<String> itr1 = itb1.iterator();
        assertTrue(itr1.hasNext());
        assertEquals("a",itr1.next());
        assertFalse(itr1.hasNext());
        String[] vals2 = new String[] {"a", "b"} ;
        Iterable<String> itb2 = exp.invoke(vals2,null);
        Iterator<String> itr2 = itb2.iterator();
        assertTrue(itr2.hasNext());
        assertEquals("a",itr2.next());
        assertTrue(itr2.hasNext());
        assertEquals("b",itr2.next());
        assertFalse(itr2.hasNext());
    }

}
