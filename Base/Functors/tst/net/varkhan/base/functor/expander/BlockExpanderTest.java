package net.varkhan.base.functor.expander;

import junit.framework.TestCase;
import net.varkhan.base.functor.Expander;

import java.util.Iterator;


/**
 * <b></b>.
 * <p/>
 * @author varkhan
 * @date 11/5/13
 * @time 5:13 PM
 */
public class BlockExpanderTest extends TestCase {

    public void testExpander() throws Exception {
        String[] str1 = new String[] { "a"};
        String[] str2 = new String[] { "a", "b"};
        String[] str3 = new String[] { "a", "b", "c" };
        String[] str6 = new String[] { "a", "b", "c", "d", "e", "f" };

        Expander<String[],String[],Object> exp11 = new BlockExpander<String, String[], Object>(String.class,new ArrayExpander<String,Object>(),1,1);
        Iterable<String[]> itb111 = exp11.invoke(str1,null);
        Iterator<String[]> itr111 = itb111.iterator();
        assertTrue(itr111.hasNext());
        assertEquals("a",toString(itr111.next()));
        assertFalse(itr111.hasNext());
        Iterable<String[]> itb113 = exp11.invoke(str3,null);
        Iterator<String[]> itr113 = itb113.iterator();
        assertTrue(itr113.hasNext());
        assertEquals("a",toString(itr113.next()));
        assertEquals("b",toString(itr113.next()));
        assertEquals("c",toString(itr113.next()));
        assertFalse(itr113.hasNext());

        Expander<String[],String[],Object> exp23 = new BlockExpander<String, String[], Object>(String.class,new ArrayExpander<String,Object>(),2,3);
        Iterable<String[]> itb231 = exp23.invoke(str1,null);
        Iterator<String[]> itr231 = itb231.iterator();
        assertFalse(itr231.hasNext());
        Iterable<String[]> itb232 = exp23.invoke(str2,null);
        Iterator<String[]> itr232 = itb232.iterator();
        assertTrue(itr232.hasNext());
        assertEquals("a b",toString(itr232.next()));
        assertFalse(itr232.hasNext());
        Iterable<String[]> itb233 = exp23.invoke(str3,null);
        Iterator<String[]> itr233 = itb233.iterator();
        assertTrue(itr233.hasNext());
        assertEquals("a b",toString(itr233.next()));
        assertEquals("b c",toString(itr233.next()));
        assertEquals("a b c",toString(itr233.next()));
        assertFalse(itr233.hasNext());
        Iterable<String[]> itb236 = exp23.invoke(str6,null);
        Iterator<String[]> itr236 = itb236.iterator();
        assertTrue(itr236.hasNext());
        assertEquals("a b",toString(itr236.next()));
        assertEquals("b c",toString(itr236.next()));
        assertEquals("a b c",toString(itr236.next()));
        assertEquals("c d",toString(itr236.next()));
        assertEquals("b c d",toString(itr236.next()));
        assertEquals("d e",toString(itr236.next()));
        assertEquals("c d e",toString(itr236.next()));
        assertEquals("e f",toString(itr236.next()));
        assertEquals("d e f",toString(itr236.next()));
        assertFalse(itr236.hasNext());

    }

    private String toString(String[] a) {
        StringBuilder buf = new StringBuilder();
        boolean first = true;
        for(String s: a) {
            if(first) first = false;
            else buf.append(' ');
            buf.append(s);
        }
        return buf.toString();
    }

}
